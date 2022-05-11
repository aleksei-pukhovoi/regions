package com.example.regions.controller;

import com.example.regions.model.Region;
import com.example.regions.servcie.RegionService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
@Validated
public class RegionController {

  private final RegionService service;

  public RegionController(RegionService service) {
    this.service = service;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Region> getAllRegions() {
    return service.findAllRegions();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Region getRegionById(@PathVariable @Positive Long id) {
    return service.findRegionById(id);
  }

  @GetMapping("/page")
  @ResponseStatus(HttpStatus.OK)
  public List<Region> getAllRegionsByPage(@RequestParam("pageNumber") @Positive int page,
      @Positive @RequestParam("pageSize") int size) {
    return service.findAllByPage(page, size);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public Region updateRegion(@Valid @RequestBody Region region) {
    return service.updateRegion(region.getId(), region);
  }
}
