package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer> {

    @Query(value = "SELECT s.*, b.title FROM service s  JOIN blog b ON s.id_blog = b.id WHERE b.confirm = :confirm", nativeQuery = true)
    Page<ServiceEntity> findByConfirm(Boolean confirm, Pageable pageable);

    @Query(value = "SELECT s.* \n" +
            "FROM service s \n" +
            "JOIN blog b ON s.id_blog = b.id \n" +
            "WHERE b.confirm = :confirm \n" +
            "ORDER BY b.create_date  DESC \n" +
            "LIMIT 3", nativeQuery = true)
    List<ServiceEntity> findFirst3OrderByCreateDateDesc(Boolean confirm);

    //View My Service
    @Query(value = "SELECT s.*, b.confirm ,b.title FROM service s  JOIN blog b ON s.id_blog = b.id WHERE b.id_user_created = :userId AND b.status is null", nativeQuery = true)
    Page<ServiceEntity> findByUserEntityIdAndStatus(int userId, Pageable pageable);

}
