package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.entity.BlogTypeEntity;
import com.swp391.DogCatLoverPlatform.repository.BlogTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogTypeService {

    @Autowired
    BlogTypeRepository blogTypeRepository;

    public List<BlogTypeEntity> getAllBlogType() {
        return blogTypeRepository.findAll();
    }

    public BlogTypeEntity getBlogTypeById(int id){
        return blogTypeRepository.findById(id).orElseThrow();
    }
}
