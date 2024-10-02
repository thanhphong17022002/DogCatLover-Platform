package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.config.ModelMapperConfig;
import com.swp391.DogCatLoverPlatform.dto.RequestDTO;
import com.swp391.DogCatLoverPlatform.dto.UserNotificationDTO;
import com.swp391.DogCatLoverPlatform.entity.RequestEntity;
import com.swp391.DogCatLoverPlatform.entity.UserNotificationEntity;
import com.swp391.DogCatLoverPlatform.repository.UserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserNotificationService {
    @Autowired
    private ModelMapperConfig modelMapperConfig;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    //Hàm để hiển thị số lượng thông báo(người dùng nào được chấp nhận hay từ chối)
    public List<UserNotificationDTO> viewAllNotificationCount(int id_user) {
        List<UserNotificationEntity> listNotification = userNotificationRepository.viewCountNotification(id_user);

        List<UserNotificationDTO> listNotificated = listNotification.stream()
                .map(entity -> modelMapperConfig.modelMapper().map(entity, UserNotificationDTO.class))
                .collect(Collectors.toList());

        return listNotificated;
    }


    //Hàm đánh dấu các thông báo đã được xem
    public void markAsRead(int id_user){
        List<UserNotificationEntity> listNotification = userNotificationRepository.findAllByIdUser(id_user);
        for (UserNotificationEntity userNotification : listNotification) {
            userNotification.setStatus(false);
        }
        // Lưu các thay đổi vào cơ sở dữ liệu
        userNotificationRepository.saveAll(listNotification);
    }



    //Hiển thị thông báo (người dùng nào được chấp nhận hay từ chối)
    public List<UserNotificationDTO> viewAllNotification(int id_user) {
        List<UserNotificationEntity> listNotification = userNotificationRepository.findAllByIdUser(id_user);

        List<UserNotificationDTO> listNotificated = listNotification.stream()
                .map(entity -> modelMapperConfig.modelMapper().map(entity, UserNotificationDTO.class))
                .collect(Collectors.toList());

        return listNotificated;
    }

    public boolean checkExistAcceptedRequest(int userId, int blogId){
        boolean isExist = false;    //true (tồn tại),  false (chưa tồn tại)

        List<UserNotificationEntity> requested = userNotificationRepository.findAllByUserSenderAndIdBlog(userId, blogId);
        if(!requested.isEmpty()){
            isExist = true;
        }

        return isExist;
    }
}
