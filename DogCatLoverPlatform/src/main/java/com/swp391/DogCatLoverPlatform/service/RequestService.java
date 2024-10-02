package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.config.ModelMapperConfig;
import com.swp391.DogCatLoverPlatform.dto.RequestDTO;
import com.swp391.DogCatLoverPlatform.dto.UserDTO;
import com.swp391.DogCatLoverPlatform.dto.UserNotificationDTO;
import com.swp391.DogCatLoverPlatform.entity.*;
import com.swp391.DogCatLoverPlatform.repository.BlogRepository;
import com.swp391.DogCatLoverPlatform.repository.RequestRepository;
import com.swp391.DogCatLoverPlatform.repository.UserNotificationRepository;
import com.swp391.DogCatLoverPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private ModelMapperConfig modelMapperConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    public boolean checkExistRequest(int userId, int blogId){
        boolean isExist = false;    //true (tồn tại),  false (chưa tồn tại)

        Optional<RequestEntity> requested = requestRepository.findByUserIdAndBlogId(userId, blogId);
        if(requested.isPresent()){
            isExist = true;
        }

        return isExist;
    }

    public RequestDTO AddRequest(RequestDTO requestDTO, int userId, int blogId){
            RequestEntity request = modelMapperConfig.modelMapper().map(requestDTO, RequestEntity.class);

            Date createDate = new Date();
            request.setCreateDate(createDate);

            request.setStatus(null);

            UserEntity userEntity = userRepository.findById(userId).orElseThrow();
            request.setUserEntity_Request(userEntity);

            BlogEntity blogEntity = blogRepository.findById(blogId).orElseThrow();
            request.setBlogEntity_Request(blogEntity);

            RequestEntity sendRequest =  requestRepository.save(request);
            RequestDTO createdSendRequest = modelMapperConfig.modelMapper().map(sendRequest, RequestDTO.class);

            return createdSendRequest;
    }

    //Hiển thị người dùng nào gửi request bên myblogdetails.html
    public List<RequestDTO> viewSendRequest(int id_user_created) {
        List<RequestEntity> listRequest = requestRepository.findAllRequestToBlogOwner(id_user_created);

        List<RequestDTO> listRequested = listRequest.stream()
                .map(entity -> modelMapperConfig.modelMapper().map(entity, RequestDTO.class))
                .collect(Collectors.toList());

        return listRequested;
    }

    //Hiển thị blog nào được gửi request bên list-request.html
    public List<RequestDTO> viewSendBlogRequest(int id_user_created) {
        List<RequestEntity> listRequest = requestRepository.findAllRequest(id_user_created);

        List<RequestDTO> listRequested = listRequest.stream()
                .map(entity -> modelMapperConfig.modelMapper().map(entity, RequestDTO.class))
                .collect(Collectors.toList());

        return listRequested;
    }

//    public String viewBlogOwnerName(int id_user_created){
//       String blogOwnerName = requestRepository.findBlogOwnerName(id_user_created);
//
//        return blogOwnerName;
//    }

    public void acceptedRequest(UserNotificationDTO userNotificationDTO, int userIdRequest, int userIdAccepted, int requestId, int blogId) {
        String accepted = "Your request is accepted!";
        String denied = "Your request is denied!";

        //Lấy user Accepted (User_Sender)
        UserEntity userAccepted = userRepository.findById(userIdAccepted).orElseThrow();

        //Tìm Blog theo Id
        BlogEntity blog = blogRepository.findById(blogId).orElseThrow();

        //Duyệt qua danh sách các Request được gửi lên, theo id Blog
        List<RequestEntity> listRequest = requestRepository.findAllByIdBlog(blogId);

        //Duyệt qua từng request gửi lên theo id bài Blog, lấy Id của từng người gửi request.
        for(RequestEntity userRequest: listRequest){
            //Tạo ra mỗi notification mới sau mỗi lần lặp
            UserNotificationEntity userNotificationEntity = modelMapperConfig.modelMapper().map(userNotificationDTO, UserNotificationEntity.class);


            //Set lại trạng thái để ẩn bài Blog đi
            blog.setStatus(false);

            //Lấy id user gửi Request
            int userId = userRequest.getUserEntity_Request().getId();

            //Cập nhật lại trạng thái (nếu được chấp nhận thì request của user đó sẽ được set là true, không thì set là false)
            userRequest.setStatus(userId == userIdRequest? true : false);

            //Gửi message: accepted <--> Còn không thì gửi denied
            userNotificationEntity.setMessage(userId == userIdRequest ? accepted : denied);

            //Người gửi thông báo
            userNotificationEntity.setUserEntity_UserNotification(userAccepted);

            //Lấy user IdReceiver
            userNotificationEntity.setIdReceiver(userId);

            Date createDateTime = new Date();
            userNotificationEntity.setCreateDate(createDateTime);

            RequestEntity request = requestRepository.findById(requestId).orElseThrow();
            userNotificationEntity.setRequestEntity(request);

            userNotificationRepository.save(userNotificationEntity);
        }


    }
}
