package com.swp391.DogCatLoverPlatform.repository;
import com.swp391.DogCatLoverPlatform.entity.DispositHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
public interface DispositHistoryRepository extends JpaRepository<DispositHistory, Integer>{
    @Query("select h from disposit_history h where h.orderId = ?1 and h.requestId = ?2")
    Optional<DispositHistory> findByOrderIdAndRequestId(String orderid, String requestId);

    @Query("select h from disposit_history h where h.paymentId = ?2 and h.token = ?1")
    Optional<DispositHistory> findBypaymentIdAndToken(String token, String paymentId);

    @Query("select h from disposit_history h where h.userEntity.id = ?1")
    public List<DispositHistory> findByUser(Integer userId);
}
