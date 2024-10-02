package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    @Query(value = "SELECT c.*, u.user_name, u.full_name, u.email, u.address, u.phone, u.image FROM comment c \n" +
            "JOIN users u ON c.id_user = u.id \n" +
            "WHERE c.id_blog= :id_blog", nativeQuery = true)
    List<CommentEntity> findCommentsByBlogId(@Param("id_blog") Integer id_blog);

//    @Query(value = "SELECT c.*, u.user_name, b.title, bt.name FROM comment c " +
//            "JOIN users u ON c.id_user = u.id " +
//            "JOIN blog b ON c.id_blog = b.id " +
//            "JOIN blog_type bt ON b.id_blog_type = bt.id " +
//            "WHERE u.id = :idUser AND b.id = :idBlog ", nativeQuery = true)
//    List<CommentEntity> findCommentsRequest(@Param("idUser") Integer idUser, @Param("idBlog") Integer idBlog);

}


