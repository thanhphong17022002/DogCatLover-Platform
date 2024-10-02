package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.config.ModelMapperConfig;
import com.swp391.DogCatLoverPlatform.dto.CommentDTO;
import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.CommentEntity;
import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapperConfig modelMapperConfig;


    public List<CommentDTO> getCommentsByBlogId(int blogId) {
        List<CommentEntity> commentEntities = commentRepository.findCommentsByBlogId(blogId);

        List<CommentDTO> commentDTOs = commentEntities.stream()
                .map(entity -> modelMapperConfig.modelMapper().map(entity, CommentDTO.class))
                .collect(Collectors.toList());


        Collections.sort(commentDTOs, (comment1, comment2) ->
                comment2.getCreateDate().compareTo(comment1.getCreateDate()));

        return commentDTOs;
    }



//    public List<CommentDTO> getCommentsByUserId(int userId, int blogId){
//        List<CommentEntity> commentEntities = commentRepository.findCommentsRequest(userId, blogId);
//        List<CommentDTO> commentRequest = new ArrayList<>();
//
//        for(CommentEntity commentEntity : commentEntities){
//            CommentDTO commentDTO = modelMapperConfig.modelMapper().map(commentEntity, CommentDTO.class);
//            commentRequest.add(commentDTO);
//        }
//
//        return commentRequest;
//    }

    public CommentDTO createComment(CommentDTO commentDTO, String description, int id_blog, int id_user) {
        CommentEntity commentEntity = modelMapperConfig.modelMapper().map(commentDTO, CommentEntity.class);
        commentEntity.setDescription(description);

        commentEntity.setUserEntity_CommentEntity(new UserEntity());
        commentEntity.getUserEntity_CommentEntity().setId(id_user);

        commentEntity.setBlogEntity_CommentEntity(new BlogEntity());
        commentEntity.getBlogEntity_CommentEntity().setId(id_blog);

        Date createDate = new Date();
        commentEntity.setCreateDate(createDate);

        CommentEntity savedComment = commentRepository.save(commentEntity);
        CommentDTO createdComment = modelMapperConfig.modelMapper().map(savedComment, CommentDTO.class);

        createdComment.setCreateDate(createDate);
        return createdComment;
    }

    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }

    public CommentDTO getCommentById(int id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow();
        CommentDTO commentDTO = modelMapperConfig.modelMapper().map(commentEntity, CommentDTO.class);
        return commentDTO;

    }
}


