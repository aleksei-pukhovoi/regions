package com.example.regions.repository;

import com.example.regions.model.Region;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RegionRepository {

  @Select("SELECT * FROM REGIONS")
  List<Region> getAll();

  @Select("SELECT * FROM REGIONS LIMIT #{size} offset #{startIndex}")
  List<Region> getALLByPage(int startIndex, int size);

  @Select("SELECT * FROM REGIONS WHERE id = #{id}")
  Region getById(@Param("id") Long id);

  @Update("UPDATE REGIONS SET name = #{region.name}, short_name = #{region.shortName} "
      + "WHERE id = #{region.id}")
  Integer update(@Param("region") Region region);
}
