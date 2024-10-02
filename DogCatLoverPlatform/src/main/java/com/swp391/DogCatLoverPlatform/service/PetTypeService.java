package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.entity.PetTypeEntity;
import com.swp391.DogCatLoverPlatform.repository.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetTypeService {

    @Autowired
    private PetTypeRepository petTypeRepository;

    public List<PetTypeEntity> getAllPetType() {
        return petTypeRepository.findAll();
    }

    public PetTypeEntity getPetTypeById(int id){
        return petTypeRepository.findById(id).orElseThrow();
    }
}
