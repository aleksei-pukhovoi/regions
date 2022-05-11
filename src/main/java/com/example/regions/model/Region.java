package com.example.regions.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Region {

  @NotNull
  private Long id;

  @NotBlank (message = "Name is required")
  private String name;

  @NotBlank(message = "Short name is required")
  private String shortName;

  public Region() {
  }

  public Region(Long id, String name, String shortName) {
    this.id = id;
    this.name = name;
    this.shortName = shortName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @Override
  public String toString() {
    return "Region{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", shortName='" + shortName + '\'' +
        '}';
  }
}
