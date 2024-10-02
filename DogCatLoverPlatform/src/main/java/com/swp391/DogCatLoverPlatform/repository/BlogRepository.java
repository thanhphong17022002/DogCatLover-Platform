package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.BlogTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Integer> {

        List<BlogEntity> findAll();
        List<BlogEntity> findFirst3ByConfirmAndStatusNotNullOrderByCreateDateDesc(boolean confirm);
        Page<BlogEntity> findByBlogTypeEntityAndConfirmAndStatusNotNull(BlogTypeEntity blogTypeEntity, boolean confirm, Pageable pageable);
        Page<BlogEntity> findByTitleContainingAndConfirmAndStatusNotNull(String title, boolean confirm, Pageable pageable);


        //View My Blog
        @Query("SELECT b FROM blog b WHERE b.userEntity.id = :userId AND b.status is not null")
        Page<BlogEntity> findByUserEntityIdAndConfirm(int userId, Pageable pageable);


        //Hiển thị bên Blog Pending Approve             Confirm: null (bắt buộc)
        //Hiển thị bên thùng rác các Blog bị từ chối    Confirm: false (bắt buộc)
        List<BlogEntity> findByConfirm(Boolean confirm);

        //Hiển thị danh sách Blog trên giao diện chính (trước khi thực hiện sell, gift) --> Confirm: true (bắt buộc)
        Page<BlogEntity> findByConfirmAndStatusNotNull(boolean confirm, Pageable pageable);


        @Transactional
        @Modifying
        @Query("UPDATE blog b SET b.status = false WHERE b.id = :id")
        void updateStatus(int id);

        List<BlogEntity> findByUserEntityIdAndConfirm(Integer userId, Boolean confirm);


        @Query(value ="SELECT b.* FROM blog b join invoice i on b.id = i.id_blog where b.id_user_created =:id_user_created",nativeQuery = true)
         List<BlogEntity> findIdSeller(Integer id_user_created);

//        Chart
        @Query(value = "SELECT COUNT(b.id_pet_type), COUNT(s.id) FROM blog b LEFT JOIN service s ON s.id = b.id", nativeQuery = true)
        List<Object[]> getBlogAndServiceCounts();


}
