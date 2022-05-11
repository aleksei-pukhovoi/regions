package com.example.regions.integration;

import static java.util.Optional.ofNullable;

import com.example.regions.model.Region;
import com.example.regions.servcie.RegionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class RegionServiceIntegrationTest {

  @Autowired
  private RegionService service;

  @Autowired
  private CacheManager cacheManager;


  @Test
  public void findRegionById_whenGetId_thenReturnCachedRegion() {
    //given
    Long regionId = 1L;

    //when
    Region region = service.findRegionById(regionId);

    //then
    Region cachedRegion = getCachedRegion(regionId);
    Assertions.assertEquals(region.getName(), cachedRegion.getName());
    Assertions.assertEquals(region.getShortName(), cachedRegion.getShortName());
  }

  @Test
  public void updateRegion_whenUpdateRegion_thenUpdateCache() {
    //given
      Region region = new Region(1L, "Saint-Petersburg", "SPB");

    //when
    Region updatedRegion = service.updateRegion(region.getId(), region);

    //then
    Region cachedRegion = getCachedRegion(region.getId());
    Assertions.assertEquals(updatedRegion.getName(), cachedRegion.getName());
    Assertions.assertEquals(updatedRegion.getShortName(), cachedRegion.getShortName());
  }

  private Region getCachedRegion(Long id) {
    return ofNullable(cacheManager.getCache("regions"))
        .map(c -> c.get(id, Region.class))
        .get();
  }
}