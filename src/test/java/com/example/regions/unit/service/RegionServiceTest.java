package com.example.regions.unit.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.regions.exception.ServiceException;
import com.example.regions.model.Region;
import com.example.regions.repository.RegionRepository;
import com.example.regions.servcie.RegionService;
import com.example.regions.servcie.RegionServiceImpl;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

  private RegionService service;

  @Mock
  private RegionRepository repository;

  private Region region;

  @BeforeEach
  public void setUp() {
    service = Mockito.spy(new RegionServiceImpl(repository));
    region = new Region(1L, "Saint-Petersburg", "SPB");
  }


  @Test
  public void findAllRegions_whenGet_thenReturnRegionList() {
    //given
    List<Region> regionList = Collections.singletonList(region);
    when(repository.getAll()).thenReturn(regionList);

    //when
    List<Region> actualRegionList = service.findAllRegions();

    //then
    assertThat(actualRegionList.size()).isEqualTo(1);
    assertThat(actualRegionList.get(0).getName()).isEqualTo("Saint-Petersburg");
    assertThat(actualRegionList.get(0).getShortName()).isEqualTo("SPB");
    verify(repository, times(1)).getAll();
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void findAllByPage_whenGet_thenReturnRegionPage() {
    //given
    List<Region> regionList = Collections.singletonList(region);
    when(repository.getALLByPage(anyInt(), anyInt())).thenReturn(regionList);

    //when
    List<Region> actualRegionList = service.findAllByPage(1,1);

    //then
    assertThat(actualRegionList.size()).isEqualTo(1);
    assertThat(actualRegionList.get(0).getName()).isEqualTo("Saint-Petersburg");
    assertThat(actualRegionList.get(0).getShortName()).isEqualTo("SPB");
    verify(repository, times(1)).getALLByPage(anyInt(), anyInt());
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void findAllByPage_whenPageIsNotExist_thenReturnException() {
    //given
    when(repository.getALLByPage(anyInt(), anyInt())).thenReturn(Collections.emptyList());

    //when
    Assertions.assertThrows(ServiceException.class, () -> service.findAllByPage(3, 3));

    //then
    verify(repository, times(1)).getALLByPage(anyInt(), anyInt());
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void findRegionById_whenGetId_thenReturnRegion() {
    //given
    when(repository.getById(anyLong())).thenReturn(region);

    //when
    Region actualRegion = service.findRegionById(1L);

    //then
    assertThat(actualRegion.getId()).isEqualTo(1L);
    assertThat(actualRegion.getName()).isEqualTo("Saint-Petersburg");
    assertThat(actualRegion.getShortName()).isEqualTo("SPB");

    verify(repository, times(1)).getById(anyLong());
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void updateRegion_whenGetRegion_thenReturnUpdatedRegion() {
    //given
    when(repository.update(any(Region.class))).thenReturn(1);
    when(repository.getById(anyLong())).thenReturn(region);

    //when
    Region actualRegion = service.updateRegion(1L, region);

    //then
    assertThat(actualRegion.getId()).isEqualTo(1L);
    assertThat(actualRegion.getName()).isEqualTo("Saint-Petersburg");
    assertThat(actualRegion.getShortName()).isEqualTo("SPB");

    verify(repository, times(1)).getById(anyLong());
    verify(repository, times(1)).update(any(Region.class));
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void updateRegion_whenIdIsNotExist_thenReturnException() {
    //given
    when(repository.update(any(Region.class))).thenReturn(0);

    //when
    Assertions.assertThrows(ServiceException.class, () -> service.updateRegion(1L, region));

    //then
    verify(repository, times(0)).getById(anyLong());
    verify(repository, times(1)).update(any(Region.class));
    verifyNoMoreInteractions(repository);
  }
}