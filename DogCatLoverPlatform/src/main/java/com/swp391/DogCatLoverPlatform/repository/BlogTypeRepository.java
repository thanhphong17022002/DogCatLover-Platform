package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.BlogTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogTypeRepository extends JpaRepository<BlogTypeEntity, Integer> {

    BlogTypeEntity findByName(String name);
}
