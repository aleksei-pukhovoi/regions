package com.example.regions.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.regions.RegionsApplication;
import com.example.regions.controller.RegionController;
import com.example.regions.model.Region;
import com.example.regions.servcie.RegionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RegionsApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegionsApplicationTests {

  private Region region;

  @LocalServerPort
  private int port;

  @Autowired
  private RegionController controller;

  @Autowired
  private RegionService service;


  private ObjectMapper mapper;

  private TestRestTemplate restTemplate;

  private HttpHeaders requestHeaders;

  @BeforeEach
  void setUp() throws IOException {
    restTemplate = new TestRestTemplate();
    requestHeaders = new HttpHeaders();
    requestHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  void contextLoads() {
    assertThat(controller).isNotNull();
    assertThat(service).isNotNull();
  }

  @Test
  public void getAllRegions_whenGet_thenReturnRegionListAndStatusCode200() throws Exception {
    //given
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
    ParameterizedTypeReference<List<Region>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/regions")
        .build();

    //when
    ResponseEntity<List<Region>> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        requestEntity,
        parameterizedTypeReference
    );

    // then
    List<Region> actualRegionList = response.getBody();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");

    assertThat(actualRegionList.size()).isEqualTo(6);
  }

  @Test
  public void findAllByPage_whenGet_thenReturnRegionPageAndStatusCode200() throws Exception {
    //given
    int pageNumber = 2;
    int pageSize = 3;
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
    ParameterizedTypeReference<List<Region>> parameterizedTypeReference =
        new ParameterizedTypeReference<>() {
        };
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/regions/page")
        .queryParam("pageNumber", pageNumber)
        .queryParam("pageSize", pageSize)
        .build();

    //when
    ResponseEntity<List<Region>> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        requestEntity,
        parameterizedTypeReference
    );

    // then
    List<Region> actualRegionList = response.getBody();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");

    assertThat(actualRegionList.size()).isEqualTo(3);
    assertThat(actualRegionList.get(0).getId()).isEqualTo(4L);
  }

  @Test
  public void findAllByPage_whenPageIsNotExist_thenReturnStatusCode400() throws Exception {
    //given
    int pageNumber = 6;
    int pageSize = 2;
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/regions/page")
        .queryParam("pageNumber", pageNumber)
        .queryParam("pageSize", pageSize)
        .build();

    //when
    ResponseEntity<?> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        requestEntity,
        Object.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void getRegionById_whenGetId_thenReturnRegionAndStatusCode200() throws Exception {
    //given
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .pathSegment("regions")
        .path("{id}")
        .buildAndExpand(1L);

    //when
    ResponseEntity<Region> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        requestEntity,
        Region.class
    );

    // then
    Region actualRegion = response.getBody();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");
    assertThat(actualRegion.getName()).isEqualTo("Khanty-Mansi autonomous area - Yugra");
    assertThat(actualRegion.getShortName()).isEqualTo("Ugra");
  }

  @Test
  public void getRegionById_whenIdIsNotExist_thenReturnStatusCode400() {
    //given
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .pathSegment("regions")
        .path("{id}")
        .buildAndExpand(10L);

    //when
    ResponseEntity<Region> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        requestEntity,
        Region.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  public void getRegionById_whenIdIsNull_thenReturnStatusCode400() throws Exception {
    //given
    HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .pathSegment("regions")
        .path("{id}")
        .buildAndExpand(0);

    //when
    ResponseEntity<Region> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.GET,
        requestEntity,
        Region.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);

  }


  @Test
  public void updateRegion_whenUpdateRegion_thenReturnUpdatedRegionAndStatusCode200()
      throws Exception {
    //given
    region = new Region(1L, "Saint-Petersburg", "SPB");
    Region currentRegion = service.findRegionById(1L);

    HttpEntity<Object> requestEntity = new HttpEntity<>(region, requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/regions")
        .build();

    //when
    ResponseEntity<Region> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.PUT,
        requestEntity,
        Region.class
    );

    // then
    Region updatedRegion = response.getBody();
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    assertThat(response.getHeaders()
        .getFirst("Content-Type")).isEqualToIgnoringCase("application/json");
    assertThat(updatedRegion.getName()).isEqualTo("Saint-Petersburg");
    assertThat(updatedRegion.getShortName()).isEqualTo("SPB");
    service.updateRegion(1L, currentRegion);
  }

  @Test
  public void updateRegion_whenIdIsNotExist_thenReturnStatusCode400()
      throws Exception {
    //given
    region = new Region(10L, "Saint-Petersburg", "SPB");

    HttpEntity<Object> requestEntity = new HttpEntity<>(region, requestHeaders);
    UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/regions")
        .build();

    //when
    ResponseEntity<Region> response = restTemplate.exchange(
        uriComponents.toUri(),
        HttpMethod.PUT,
        requestEntity,
        Region.class
    );

    // then
    assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
  }
}
