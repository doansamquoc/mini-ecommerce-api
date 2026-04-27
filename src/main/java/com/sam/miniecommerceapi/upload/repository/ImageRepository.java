package com.sam.miniecommerceapi.upload.repository;

import com.sam.miniecommerceapi.upload.entity.Image;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	@Query("SELECT i.id FROM Image i WHERE i.id IN :ids")
	List<Long> findExistingImages(@Param("ids") List<Long> ids);
}
