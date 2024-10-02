package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.config.ModelMapperConfig;
import com.swp391.DogCatLoverPlatform.dto.PetCategoryDTO;
import com.swp391.DogCatLoverPlatform.entity.PetCategoryEntity;
import com.swp391.DogCatLoverPlatform.repository.PetCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetCategoryService {
    @Autowired
    private PetCategoryRepository petCategoryRepository;

    @Autowired
    private ModelMapperConfig modelMapperConfig;

    public PetCategoryDTO createPetCategory(PetCategoryDTO petCategoryDTO) {
        PetCategoryEntity petCategoryEntity = new PetCategoryEntity();
        // Sao chép thông tin từ petCategoryDTO vào petCategoryEntity
        // Ví dụ: petCategoryEntity.setName(petCategoryDTO.getName());
        // Tương tự cho các trường khác
        petCategoryEntity = petCategoryRepository.save(petCategoryEntity);

        return modelMapperConfig.modelMapper().map(petCategoryEntity, PetCategoryDTO.class);
    }
}
