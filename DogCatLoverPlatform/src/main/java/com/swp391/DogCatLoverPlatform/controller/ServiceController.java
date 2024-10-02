package com.swp391.DogCatLoverPlatform.controller;


import com.swp391.DogCatLoverPlatform.dto.*;

import com.swp391.DogCatLoverPlatform.entity.ServiceEntity;


import com.swp391.DogCatLoverPlatform.service.*;

import com.swp391.DogCatLoverPlatform.repository.BookingEntityRepository;


import com.swp391.DogCatLoverPlatform.service.RequestService;

import com.swp391.DogCatLoverPlatform.repository.BookingEntityRepository;
import com.swp391.DogCatLoverPlatform.service.BlogService;
import com.swp391.DogCatLoverPlatform.service.BookingService;

import com.swp391.DogCatLoverPlatform.service.ServiceService;
import com.swp391.DogCatLoverPlatform.service.UserNotificationService;
import com.swp391.DogCatLoverPlatform.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    ServiceService serviceService;
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    BookingService bookingService;
    @Autowired
    BlogService blogService;

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    RequestService requestService;



    @GetMapping("/view/myservice/details/{id}")
    public String viewMyServiceDetail(@PathVariable("id") int id, Model model, HttpServletRequest req){
        List<ServiceDTO> latestServices = serviceService.getThreeLatestBlogs();
        model.addAttribute("latestService", latestServices);

        ServiceDTO serviceDTO = serviceService.getServiceDetail(id);
        model.addAttribute("service", serviceDTO);

        UserDTO user = getUserIdFromCookie(req);
        model.addAttribute("user", user);

        //Lấy comment by Blog Id
        List<CommentDTO> comments = commentService.getCommentsByBlogId(serviceDTO.getId_blog());
        model.addAttribute("comments", comments);

        //Hiện thông báo (trường hợp có)
        if (user != null) {
             //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());

            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        return "service-details-myservice";
    }

    //View My Blog Service
    @GetMapping("/view/myservice")
    public String viewMyService(Model model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "3") int size,
                                HttpServletRequest req) {

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){

            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());

            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);

        Page<ServiceDTO> listMyService = serviceService.GetAllMyService(user.getId(), page, size);
        model.addAttribute("totalPage", listMyService.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listService", listMyService);


        List<ServiceDTO> latestServices = serviceService.getThreeLatestBlogs();
        model.addAttribute("latestService", latestServices);
        return "myservice";
    }

    @GetMapping("/view")
    public String viewAllService(Model model,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "3") int size,
                                 HttpServletRequest req) {


        Page<ServiceDTO> serviceDTOList = serviceService.getAllService(page, size);
        model.addAttribute("totalPage", serviceDTOList.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listService", serviceDTOList);

        UserDTO user = getUserIdFromCookie(req);
        model.addAttribute("user", user);

        //Hiện thông báo (trường hợp có)
        if (user != null) {

            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        List<ServiceDTO> latestServices = serviceService.getThreeLatestBlogs();
        model.addAttribute("latestService", latestServices);

        return "service-standard";
    }

    @GetMapping("/detail/{id}")
    public String viewDetailService(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        ServiceDTO serviceDTO = serviceService.getServiceDetail(id);
        model.addAttribute("service", serviceDTO);

        UserDTO user = getUserIdFromCookie(request);
        model.addAttribute("user", user);

        //Lấy comment by Blog Id
        List<CommentDTO> comments = commentService.getCommentsByBlogId(serviceDTO.getId_blog());
        model.addAttribute("comments", comments);

        //Hiện thông báo (trường hợp có)
        if (user != null) {

            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        return "service-details";
    }


    @GetMapping("/create")
    public String createService(Model model, HttpServletRequest req) {
        List<ServiceCategoryDTO> listServiceCategory = serviceService.getServiceCategoryEntityList();
        UserDTO user = getUserIdFromCookie(req);

        model.addAttribute("user", user);

        model.addAttribute("serviceCategories", listServiceCategory);
        model.addAttribute("service", new ServiceDTO());

        //Hiện thông báo (trường hợp có)
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        return "service-create-form";
    }

    @PostMapping("/create")
    public String createService(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) throws IOException {

        String image = blogService.saveImageAndReturnPath(file);
        Date startDate = Date.valueOf(request.getParameter("dateStart"));
        Date endDate = Date.valueOf(request.getParameter("dateEnd"));
        String Content = request.getParameter("content");
        double price = Double.parseDouble(request.getParameter("price"));
        String title = request.getParameter("title");
        UserDTO userDTO = getUserIdFromCookie(request);
        System.out.println(userDTO.getId());
        int serviceCategory = Integer.parseInt(request.getParameter("serviceCategory"));

        serviceService.createService(Content, price, title, userDTO.getId(), serviceCategory, image, startDate, endDate);

        UserDTO user = getUserIdFromCookie(request);
        model.addAttribute("user", user);


        //Hiện thông báo (trường hợp có)
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());

            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        return "redirect:/service/view";
    }

    @GetMapping("cart")
    public String cart(HttpServletRequest request, Model model) {
        UserDTO userDTO = getUserIdFromCookie(request);
        if (userDTO == null) {
            return "redirect:/index/login";
        }

        //Hiện thông báo (trường hợp có)
        if (userDTO != null) {

            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(userDTO.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(userDTO.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        List<BookingDTO> list = bookingService.findByUserBooking(userDTO.getId());

        double price = 0;
        for (BookingDTO booking : list) {
            price += booking.getTotal_price();
            System.out.println(price);
        }
        model.addAttribute("user", userDTO);
        model.addAttribute("listBooking", list);
        model.addAttribute("quantity", list.size());
        model.addAttribute("totalPrice", price);
        return "service_cart";
    }

    //GetUserIdFromCookie cực kỳ quan trọng!!!
    private UserDTO getUserIdFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        UserDTO userDTO = new UserDTO();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("User".equals(cookie.getName())) {
                    String email = cookie.getValue();
                    userDTO = userService.getUserByEmail(email);
                    return userDTO;
                }
            }
        }
        return null;
    }


    @PostMapping("/create_comment")
    public String createComment(@ModelAttribute("comment") CommentDTO commentDTO, HttpServletRequest req){
        UserDTO user  = getUserIdFromCookie(req);
        String description = req.getParameter("description");

        int id = Integer.parseInt(req.getParameter("id"));

        int id_blog = Integer.parseInt(req.getParameter("id_blog"));

        BlogDTO blog = blogService.getBlogById(id_blog);
        commentService.createComment(commentDTO, description, id_blog, user.getId());

        // Chuyển hướng người dùng đến trang chi tiết của service
        return "redirect:/service/detail/"+id;
    }


    @GetMapping("/savedraft")
    public String savedraft(HttpServletRequest request, Model model){
        UserDTO userDTO = getUserIdFromCookie(request);
        if (userDTO == null) {
            return "redirect:/index/login";
        }
        List<ServiceCategoryDTO> listServiceCategory = serviceService.getServiceCategoryEntityList();
        model.addAttribute("serviceCategories", listServiceCategory);

        int id_service = Integer.parseInt(request.getParameter("idService"));
        ServiceDTO serviceDTO = serviceService.findService(id_service);
        model.addAttribute("service",serviceDTO);
        return "savedraft";
    }

    @PostMapping("/savedraft")
    public String saveDraft(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) throws IOException {
        //String image = blogService.saveImageAndReturnPath(file);
        String image = request.getParameter("fileOriginal");
        if(!file.isEmpty()){
            image = blogService.saveImageAndReturnPath(file);;
        }
        Date startDate = Date.valueOf(request.getParameter("dateStart"));
        Date endDate = Date.valueOf(request.getParameter("dateEnd"));
        String Content = request.getParameter("content");
        double price = Double.parseDouble(request.getParameter("price"));
        String title = request.getParameter("title");
        UserDTO userDTO = getUserIdFromCookie(request);
        System.out.println(userDTO.getId());
        int serviceCategory = Integer.parseInt(request.getParameter("serviceCategory"));

        serviceService.createService(Content, price, title, userDTO.getId(), serviceCategory, image, startDate, endDate);

        UserDTO user = getUserIdFromCookie(request);
        model.addAttribute("user", user);


        //Hiện thông báo (trường hợp có)
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());

            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        return "redirect:/service/view";
    }

}

