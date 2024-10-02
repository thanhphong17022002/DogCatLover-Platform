package com.swp391.DogCatLoverPlatform.controller;


import com.swp391.DogCatLoverPlatform.dto.InvoiceDTO;
import com.swp391.DogCatLoverPlatform.dto.RequestDTO;
import com.swp391.DogCatLoverPlatform.dto.UserDTO;
import com.swp391.DogCatLoverPlatform.dto.UserNotificationDTO;
import com.swp391.DogCatLoverPlatform.dto.*;
import com.swp391.DogCatLoverPlatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    RequestService requestService;


    @GetMapping("")
    public String showInvoice(@RequestParam("id") int invoiceId, Model model, HttpServletRequest req) {
        // Fetch invoice details by ID (You should implement this logic in your service)
        InvoiceDTO invoice = invoiceService.getInvoiceById(invoiceId);

        if (invoice != null) {
            // Add invoice details to the model
            model.addAttribute("invoiceDate", invoice.getInvoice_date());
            model.addAttribute("totalAmount", invoice.getTotal_amount());
            model.addAttribute("blogId", invoice.getIdBlog());
            model.addAttribute("userId", invoice.getIdUser());
            model.addAttribute("invoice",invoice);

            // Lấy email từ UserEntity trong InvoiceDTO
            String email = userService.getUserById(invoice.getIdUser()).getEmail();
            model.addAttribute("email", email);

            String title = blogService.getBlogById(invoice.getIdBlog()).getTitle();
            model.addAttribute("title", title);

            UserDTO userDTO = getUserIdFromCookie(req);
            model.addAttribute("user", userDTO);
        }

        UserDTO userDTO = getUserIdFromCookie(req);
        model.addAttribute("user", userDTO);

        if(userDTO != null){
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(userDTO.getId());

            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(userDTO.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        // Return the invoice template (invoice.html)
        return "invoice";
    }

    @GetMapping("/manager")
    public String manager(Model model, HttpServletRequest req) {
        UserDTO userDTO = getUserIdFromCookie(req);
        model.addAttribute("user", userDTO);

        if(userDTO == null){
            return "redirect:/index/login";
        }

        if(userDTO != null){
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(userDTO.getId());

            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(userDTO.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        List<InvoiceDTO> list = invoiceService.getSellManager(userDTO.getId());
        model.addAttribute("list", list);
        model.addAttribute("quantity", list.size());

        return "invoice-manager";
    }

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


    @GetMapping("/statistic-invoice")
    public ResponseEntity<?> statisticInvoice(@RequestParam("date") Date date){
        List<StatisticInvoiceDTO> list = new ArrayList<>();
        Long startDate = date.getTime();
        for (int i = 0; i <7; i++){
            Date str = new Date(startDate + (1000L * 60L * 60L * 24L * i));
            StatisticInvoiceDTO statisticDTO = new StatisticInvoiceDTO();
            statisticDTO.setDateInvoice(str);
            statisticDTO.setNumberInvoice(invoiceService.getCountInvoiceByBlogDate(str));
            list.add((statisticDTO));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
