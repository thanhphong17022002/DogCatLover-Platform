package com.swp391.DogCatLoverPlatform.controller;


import com.swp391.DogCatLoverPlatform.dto.BookingDTO;
import com.swp391.DogCatLoverPlatform.dto.RequestDTO;
import com.swp391.DogCatLoverPlatform.dto.UserDTO;
import com.swp391.DogCatLoverPlatform.dto.UserNotificationDTO;
import com.swp391.DogCatLoverPlatform.dto.*;
import com.swp391.DogCatLoverPlatform.entity.BookingEntity;
import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.exception.MessageException;
import com.swp391.DogCatLoverPlatform.repository.BookingEntityRepository;
import com.swp391.DogCatLoverPlatform.repository.UserRepository;
import com.swp391.DogCatLoverPlatform.service.BookingService;
import com.swp391.DogCatLoverPlatform.service.RequestService;
import com.swp391.DogCatLoverPlatform.service.UserNotificationService;
import com.swp391.DogCatLoverPlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    RequestService requestService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/history")
    public String history(Model model, HttpServletRequest request){
        UserDTO userDTO = getUserIdFromCookie(request);

        if(userDTO == null){
            String noAccess = "cannot access";
            model.addAttribute("noAccess", noAccess);
        }

        if(userDTO != null){
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(userDTO.getId());

            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(userDTO.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        List<BookingDTO> listHistory = bookingService.getBookingHistory(userDTO.getId());

        model.addAttribute("listHistory",listHistory);
        model.addAttribute("user", userDTO);
        return "booking-history";
    }



    @GetMapping("/manager")
    public String manager(Model model, HttpServletRequest req) {
        UserDTO userDTO = getUserIdFromCookie(req);

        if(userDTO == null){
            return "redirect:/index/login";
        }

        if(userDTO != null){
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(userDTO.getId());

            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(userDTO.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        List<BookingDTO> list = bookingService.getBookingManager(userDTO.getId());
        model.addAttribute("listBooking", list);
        model.addAttribute("quantity", list.size());
        model.addAttribute("user", userDTO);

        return "booking-manager";
    }

    @Autowired
    private BookingEntityRepository bookingEntityRepository;

    @GetMapping("/booking-by-date-and-blog")
    public ResponseEntity<?> findByDateAndBlog(@RequestParam("date") Date date, @RequestParam("id") Integer idBlog){
        List<BookingDTO> dtos = bookingService.getByDateAndIdblog(date,idBlog);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/delete")
    public String deleteBooking (HttpServletRequest request){
        int id_booking = Integer.parseInt(request.getParameter("id_booking"));
        bookingService.deleteById(id_booking);
        return "redirect:/service/cart";
    }

    @PostMapping("/create-booking")
    public ResponseEntity<?> create(@RequestBody BookingEntity booking, HttpServletRequest req) {
        UserDTO user = getUserIdFromCookie(req);
        boolean result = false;
        if(user == null){
            throw new MessageException("You're not logged in!",444);
        }else{
            result = bookingService.createBooking(booking,user);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
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

    // thống kê số lượng đơn đặt trong 7 ngày tính từ ngày (parameter date)
    @GetMapping("/statistic")
    public ResponseEntity<?> statistic(@RequestParam("date") Date date){
        List<StatisticDTO> list = new ArrayList<>();
        Long startDate = date.getTime();
        for(int i=0; i<7; i++){
            Date str = new Date(startDate + (1000L * 60L * 60L * 24L * i));
            StatisticDTO statisticDto = new StatisticDTO();
            statisticDto.setDate(str);
            /*statisticDto.setNumberBooking(bookingEntityRepository.countBookingByBookingDate(str));*/
            statisticDto.setNumberBooking(bookingService.getCountBookingByBookingDate(str));
            list.add(statisticDto);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }





    /*
     * sau khi nhấn nút thanh toán bằng ví
     * lấy ra danh sách các booking chưa được thanh toán của người dùng
     * tính tổng tiền cần thanh toán, nếu không đủ số dư, thông báo và hủy thanh toán
     * nếu hợp lệ, trừ tiền của tài khoản mua, đồng thời dùng vòng lặp, tăng tiền cho các người bán
     * */
    @PostMapping("/confirm-booking")
    public ResponseEntity<?> create(HttpServletRequest req) {
        UserEntity user = getUserFromCookie(req);
        boolean result = false;
        if(user == null){
            throw new MessageException("You are not logged in",444);
        }else{
            List<BookingEntity> list = bookingService.getFindUserBooking(user.getId());
            Double total = 0D;
            for(BookingEntity b: list){
                total += b.getTotal_price();
            }
            if(user.getAccountBalance() == null){
                throw new MessageException("wallet balance not enough");
            }
            if(user.getAccountBalance() < total){
                throw new MessageException("wallet balance not enough");
            }
            if(list.isEmpty()){
                throw new MessageException("you must have at least 1 service to payment!");
            }
            user.setAccountBalance(user.getAccountBalance() - total);
            userService.getSaveUser(user);
            for(BookingEntity b: list){
                b.setPaying_method("Wallet");
                b.setStatus(true);
                UserEntity ch = b.getBlogEntity_BookingEntity().getUserEntity();
                if(ch.getAccountBalance() == null){
                    ch.setAccountBalance(b.getTotal_price());
                }
                else{
                    ch.setAccountBalance(b.getTotal_price() + ch.getAccountBalance());
                }
                userService.getSaveUser(ch);

            }
        }
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    private UserEntity getUserFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("User".equals(cookie.getName())) {
                    String email = cookie.getValue();
                    UserEntity user = userRepository.findByEmail(email);
                    return user;
                }
            }
        }
        return null;
    }
}
