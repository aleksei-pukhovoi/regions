package com.example.regions.servcie;

import static com.example.regions.exception.error_code.RegionServiceErrorCode.PAGE_OF_REGION_NOT_EXIST;
import static com.example.regions.exception.error_code.RegionServiceErrorCode.REGION_NOT_EXIST;

import com.example.regions.exception.ServiceException;
import com.example.regions.model.Region;
import com.example.regions.repository.RegionRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegionServiceImpl implements RegionService {

  private final RegionRepository repository;

  public RegionServiceImpl(RegionRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Region> findAllRegions() {
    return repository.getAll();
  }

  @Override
  public List<Region> findAllByPage(int startIndex, int size) {
    List<Region> regions = repository.getALLByPage((startIndex-1) * size, size);
        if (regions.isEmpty()) {
          throw new ServiceException(PAGE_OF_REGION_NOT_EXIST);
        }
    return regions;
  }

  @Override
  @Cacheable(cacheNames = "regions", key = "#id")
  public Region findRegionById(Long id) {
    Region region = repository.getById(id);
    if (Objects.isNull(region)) {
      throw new ServiceException(REGION_NOT_EXIST);
    }
    return region;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @CachePut(cacheNames = "regions", key = "#id")
  public Region updateRegion(Long id, Region region) {
    if (repository.update(region) == 0) {
      throw new ServiceException(REGION_NOT_EXIST);
    }
    return repository.getById(id);
  }
}
