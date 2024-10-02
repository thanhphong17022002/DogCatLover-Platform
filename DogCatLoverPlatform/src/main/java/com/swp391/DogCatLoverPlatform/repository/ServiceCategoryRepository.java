package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.ServiceCategoryEntity;
import com.swp391.DogCatLoverPlatform.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryEntity, Integer> {
}
