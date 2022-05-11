package com.example.regions.unit.controller;

import static com.example.regions.exception.error_code.RegionServiceErrorCode.REGION_NOT_EXIST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.regions.controller.RegionController;
import com.example.regions.exception.ExceptionHandlerAdvice;
import com.example.regions.exception.ServiceException;
import com.example.regions.model.Region;
import com.example.regions.servcie.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;

@ExtendWith(MockitoExtension.class)
class RegionControllerTest {

  private MockMvc mockMvc;

  @Mock
  private RegionService service;

  @InjectMocks
  private RegionController controller;

  private Region region;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new ExceptionHandlerAdvice())
        .build();
  }

  @Test
  public void getAllRegions_whenGet_thenReturnRegionListAndStatusCode200() throws Exception {
    //given
    region = new Region(1L, "Saint-Petersburg", "SPB");
    List<Region> regionList = Collections.singletonList(region);
    when(service.findAllRegions()).thenReturn(regionList);

    //when
    mockMvc.perform(get("/regions"))

        // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(region.getId().intValue())))
        .andExpect(jsonPath("$[0].name", is(region.getName())))
        .andExpect(jsonPath("$[0].shortName", is(region.getShortName())));
    verify(service, times(1)).findAllRegions();
    verifyNoMoreInteractions(service);
  }

  @Test
  public void findAllByPage_whenGet_thenReturnRegionPageAndStatusCode200() throws Exception {
    //given
    region = new Region(1L, "Saint-Petersburg", "SPB");
    List<Region> regionList = Collections.singletonList(region);
    when(service.findAllByPage(anyInt(), anyInt())).thenReturn(regionList);

    //when
    mockMvc.perform(get("/regions/page?pageNumber={pageNumber}&pageSize={pageSize}",
        1,1))

        // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(region.getId().intValue())))
        .andExpect(jsonPath("$[0].name", is(region.getName())))
        .andExpect(jsonPath("$[0].shortName", is(region.getShortName())));
    verify(service, times(1)).findAllByPage(anyInt(), anyInt());
    verifyNoMoreInteractions(service);
  }

  @Test
  public void getAllRegions_whenInvalidAcceptHeader_thenNotAcceptableReturned() throws Exception {
    // given
    String invalidAcceptMimeType = MimeTypeUtils.APPLICATION_XML_VALUE;
    region = new Region(1L, "Saint-Petersburg", "SPB");
    List<Region> regionList = Collections.singletonList(region);
    when(service.findAllRegions()).thenReturn(regionList);

    // when
    mockMvc.perform(get("/regions").accept(invalidAcceptMimeType))
        // then
        .andExpect(status().isNotAcceptable());
  }

  @Test
  public void getRegionById_whenGetId_thenReturnRegionAndStatusCode200() throws Exception {
    //given
    region = new Region(1L, "Saint-Petersburg", "SPB");
    when(service.findRegionById(anyLong())).thenReturn(region);

    //when
    mockMvc.perform(get("/regions/1"))

        // then
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(region.getId().intValue())))
        .andExpect(jsonPath("$.name", is(region.getName())))
        .andExpect(jsonPath("$.shortName", is(region.getShortName())));
    verify(service, times(1)).findRegionById(anyLong());
    verifyNoMoreInteractions(service);
  }

  @Test
  public void getRegionById_whenIdIsNotExist_thenReturnStatusCode400() throws Exception {
    // given
    when(service.findRegionById(anyLong())).thenThrow(new ServiceException(REGION_NOT_EXIST));

    // when
    mockMvc.perform(get("/regions/10"))

        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateRegion_whenUpdateRegion_thenReturnUpdatedRegionAndStatusCode200()
      throws Exception {
    //given
    region = new Region(1L, "Saint-Petersburg", "SPB");
    when(service.updateRegion(anyLong(), any(Region.class))).thenReturn(region);
    ObjectMapper mapper = new ObjectMapper();

    //when
    mockMvc.perform(put("/regions")
        .content(mapper.writeValueAsString(region))
        .contentType(MediaType.APPLICATION_JSON))

        //then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(region.getId().intValue())))
        .andExpect(jsonPath("$.name", is(region.getName())))
        .andExpect(jsonPath("$.shortName", is(region.getShortName())));
    verify(service, Mockito.times(1)).updateRegion(anyLong(), any(Region.class));
    verifyNoMoreInteractions(service);
  }

  @Test
  public void updateRegion_whenIdIsNotExist_thenReturnStatusCode400() throws Exception {
    // given
    region = new Region(10L, "Saint-Petersburg", "SPB");
    when(service.updateRegion(anyLong(), any(Region.class))).thenThrow(new ServiceException(REGION_NOT_EXIST));
    ObjectMapper mapper = new ObjectMapper();

    // when
    mockMvc.perform(put("/regions")
        .content(mapper.writeValueAsString(region))
        .contentType(MediaType.APPLICATION_JSON))

        // then
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateRegion_whenRegionHasEmptyFields_thenReturnStatusCode400() throws Exception {
    //given
    region = new Region(1L, "", "");
    ObjectMapper mapper = new ObjectMapper();

    // when
    mockMvc.perform(put("/regions")
        .content(mapper.writeValueAsString(region))
        .contentType(MediaType.APPLICATION_JSON))

        // then
        .andExpect(status().isBadRequest());
  }

}