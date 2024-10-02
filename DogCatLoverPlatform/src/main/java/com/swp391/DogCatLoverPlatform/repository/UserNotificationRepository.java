package com.swp391.DogCatLoverPlatform.repository;

import com.swp391.DogCatLoverPlatform.entity.UserNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotificationEntity, Integer> {

    //Dùng để hiển thị số lượng thông báo
    @Query(value="SELECT un.*, u.id, u.user_name, b.title, un.message \n" +
            "FROM users_notification un \n" +
            "JOIN users u ON un.id_sender = u.id \n" +
            "JOIN request r ON r.id = un.id_request \n" +
            "JOIN blog b ON b.id = r.id_blog \n" +
            "WHERE un.id_receiver = :idUser AND un.status IS NULL", nativeQuery = true)
    List<UserNotificationEntity> viewCountNotification(Integer idUser);


    //Hiển thị danh sách các thông báo được gửi về cho User (chấp nhận hay bị từ chối)
    @Query(value="SELECT un.*, u.id, u.user_name, b.title, un.message \n" +
            "FROM users_notification un \n" +
            "JOIN users u ON un.id_sender = u.id \n" +
            "JOIN request r ON r.id = un.id_request \n" +
            "JOIN blog b ON b.id = r.id_blog \n" +
            "WHERE un.id_receiver = :idUser", nativeQuery = true)
    List<UserNotificationEntity> findAllByIdUser(Integer idUser);

    //Kiểm tra trường hợp danh sách đã được duyệt rồi thì không được gửi nữa.
    @Query(value = "SELECT un.* FROM users_notification un \n" +
            "JOIN request r ON r.id = un.id_request \n" +
            "JOIN blog b ON b.id = r.id_blog \n" +
            "WHERE un.id_sender = :idUser AND r.id_blog = :idBlog", nativeQuery = true)
    List<UserNotificationEntity> findAllByUserSenderAndIdBlog(Integer idUser, Integer idBlog);

}
