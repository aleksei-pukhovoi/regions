package com.example.regions.servcie;

import com.example.regions.model.Region;
import java.util.List;

public interface RegionService {

  List<Region> findAllRegions();

  List<Region> findAllByPage(int startIndex, int size);

  Region findRegionById(Long id);

  Region updateRegion(Long id, Region region);
}
