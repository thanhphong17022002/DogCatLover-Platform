package com.swp391.DogCatLoverPlatform.controller;

import com.swp391.DogCatLoverPlatform.dto.*;

import com.swp391.DogCatLoverPlatform.entity.BlogEntity;

import com.swp391.DogCatLoverPlatform.entity.InvoiceEntity;
import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/paymethod")
public class PaypalController {

    @Autowired
    PaypalService service;
    @Autowired
    BookingService bookingService;
    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    EmailService emailService;

    public static final String SUCCESS_URL = "/pay/success";
    public static final String CANCEL_URL = "/pay/cancel";

    public static final String SUCCESSSELL_URL = "/paysell/success";

    @GetMapping("")
    public String home() {
        return "home";
    }

    @PostMapping("/pay")
    public String payment(@ModelAttribute("order") Order order) {
        order.setCurrency("USD");
        try {
            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),

                    order.getIntent(), order.getDescription(), "http://localhost:8080/paymethod" + CANCEL_URL,
                    "http://localhost:8080/paymethod" + SUCCESS_URL);

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/service/cart";
    }
    @PostMapping("/paysell")
    public String paymentSell(@ModelAttribute("order") Order order) {
        try {
            Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),

                    order.getIntent(), order.getDescription(),"http://localhost:8080/paymethod" + CANCEL_URL,
                    "http://localhost:8080/paymethod" + SUCCESSSELL_URL+"?idBlog="+order.getIdBlog());

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/service/cart";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(HttpServletRequest request, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                UserDTO userDTO = getUserIdFromCookie(request);
                bookingService.updateStatus(userDTO.getId());


                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(value = SUCCESSSELL_URL)
    public String successPaySell(HttpServletRequest request, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                int idBlog = Integer.parseInt(request.getParameter("idBlog"));
                UserDTO user = getUserIdFromCookie(request);
                InvoiceEntity invoiceEntity = invoiceService.saveInvoice(idBlog, user.getId());
                BlogDTO blogDTO = blogService.getBlogById(idBlog);
                blogService.updateBlogToFalse(idBlog);

                // Lấy thông tin người dùng tạo bài viết
                Optional<BlogEntity> blogEntity = blogService.blogRepository.findById(idBlog);
                //add money for seller
                UserEntity userSeller = userService.getFindByEmail(blogEntity.get().getUserEntity().getEmail());
                userSeller.setAccountBalance(userSeller.getAccountBalance() + blogEntity.get().getPrice());
                userService.getSaveUser(userSeller);
                if (blogEntity.isPresent()) {
                    String sellerEmail = blogEntity.get().getUserEntity().getEmail(); // Lấy địa chỉ email của người bán

                    // Gửi thông báo email cho người bán
                    String blogTitle = blogDTO.getTitle(); // Lấy tiêu đề của bài viết từ đối tượng blogDTO



                    //emailService.sendPurchaseNotification(sellerEmail, blogTitle, idBlog);

                    // Redirect to the invoice page with the invoice ID as a query parameter
                    return "redirect:/invoice?id=" + invoiceEntity.getId();
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
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

}