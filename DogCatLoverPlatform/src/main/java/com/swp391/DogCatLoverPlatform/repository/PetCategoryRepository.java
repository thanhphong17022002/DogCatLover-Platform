package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.PetCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface PetCategoryRepository extends JpaRepository<PetCategoryEntity, Integer> {

    List<PetCategoryEntity> findById(int id);
}
