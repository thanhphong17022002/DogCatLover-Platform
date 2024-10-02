package com.swp391.DogCatLoverPlatform.controller;


import com.google.gson.Gson;
import com.swp391.DogCatLoverPlatform.dto.RequestDTO;
import com.swp391.DogCatLoverPlatform.dto.Root;
import com.swp391.DogCatLoverPlatform.dto.UserDTO;
import com.swp391.DogCatLoverPlatform.dto.UserNotificationDTO;
import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.payload.BaseRespone;
import com.swp391.DogCatLoverPlatform.service.BlogService;
import com.swp391.DogCatLoverPlatform.service.RequestService;
import com.swp391.DogCatLoverPlatform.service.UserNotificationService;
import com.swp391.DogCatLoverPlatform.service.UserService;
import com.swp391.DogCatLoverPlatform.util.JwtHelper;
import com.swp391.DogCatLoverPlatform.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
/*import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
@RequestMapping("/index")
public class UserController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private RequestService requestService;

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserNotificationService userNotificationService;



    private Gson gson = new Gson();

    @GetMapping("/home")
    public String hello(Model model, HttpServletRequest req) {
        UserDTO user  = getUserIdFromCookie(req);
        model.addAttribute("user", user);

        //Hiện số lượng thông báo
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/blog")
    public String blogStandard() {
        return "blog-standard";
    }

    @GetMapping("/blog-details")
    public String blogDetails() {
        return "blog-details";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest req) {
        UserDTO user  = getUserIdFromCookie(req);
        model.addAttribute("user", user);

        //Hiện số lượng thông báo
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        return "about";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest req) {
        UserDTO user  = getUserIdFromCookie(req);
        model.addAttribute("user", user);

        //Hiện số lượng list
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        return "contact";
    }


    //Xem profile user của phần List Request
    @GetMapping("/profile/{userId}")
    public String profile(Model model, @PathVariable int userId, HttpServletRequest req) {
        // Sử dụng userId để truy vấn thông tin người dùng và chuẩn bị dữ liệu cho view
        UserDTO user = userService.getUserById(userId);
        model.addAttribute("user", user);


        UserDTO users  = getUserIdFromCookie(req);
        model.addAttribute("users", users);
        if(users != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(users.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(users.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        return "profile-user-request";
    }

    //Xem profile user của chính chủ
    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        String email = null;
        try {
            for (Cookie c : cookies) {
                if ("User".equals(c.getName())) {
                    email = c.getValue();
                    UserDTO user = userService.getUserByEmail(email);
                    //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
                    List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
                    List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
                    int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
                    model.addAttribute("count", totalCount);
                    model.addAttribute("user", user);
                    return "profile";
                }
            }

        } catch (Exception e) {
            model.addAttribute("error", "You didn't Login");
            return "redirect:/index/login";
        }
        return "redirect:/index/login";
    }



    @PostMapping(value = "/profile-update")
    public String profileUpdate(Model model, HttpServletRequest req, @RequestParam("file") File file) throws IOException {
        String image = req.getParameter("image");
        if(file.getName().length() > 2){
            image = file.getName();
        }
        String fullname = req.getParameter("fullName");
        String username = req.getParameter("userName");
        String address = req.getParameter("address");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");
        String description = req.getParameter("description");

        boolean isSuccess = userService.updateUser(fullname, username, phone, address, email, image, description);


        return "redirect:/index/profile";
    }



    @GetMapping("/sign-up")
    public String listTodo(Model model) {
        return "sign-up";
    }

    @PostMapping(value = "/sign-up-add")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO signUpRequest) {
        UserEntity checkEmail = userService.getFindByEmail(signUpRequest.getEmail());

        if(checkEmail == null){
            BaseRespone baseRespone = new BaseRespone();
            boolean isSuccess = userService.addUser(signUpRequest);
            baseRespone.setStatusCode(200);
            baseRespone.setMessage("");
            baseRespone.setData(isSuccess);
            return new ResponseEntity<>(baseRespone, HttpStatus.OK);
        }else{
            BaseRespone baseRespone = new BaseRespone();
            baseRespone.setStatusCode(400);
            return new ResponseEntity<>(baseRespone, HttpStatus.OK);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginInto(HttpServletRequest req, HttpServletResponse resp, Model model) {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UsernamePasswordAuthenticationToken authen = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authen);
        if (authenticationManager != null) {
            Cookie cookie = new Cookie("User", email);
            cookie.setMaxAge(3600);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            resp.addCookie(cookie);
        }
        //Lấy danh sách role đã lưu từ security context folder khi AuthenManager chứng thực thành công
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> roles = (List<SimpleGrantedAuthority>) authentication.getAuthorities();


        String jsonRole = gson.toJson(roles);

        String token = jwtHelper.generateToken(jsonRole);

        BaseRespone baseRespone = new BaseRespone();
        baseRespone.setStatusCode(200);
        baseRespone.setMessage(roles.toString());
        baseRespone.setData(token);

        return new ResponseEntity<>(baseRespone, HttpStatus.OK);

    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp) {
        // Get the user's session
        HttpSession session = req.getSession(false);

        if (session != null) {
            // Invalidate the session to log out the user
            session.invalidate();
        }

        // Remove the "User" cookie by setting its max age to 0
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("User".equals(cookie.getName())) {
                    cookie.setMaxAge(0); // Setting max age to 0 deletes the cookie
                    cookie.setDomain("localhost");
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                    break;
                }
            }
        }

        // Redirect to the login page after logout
        return "redirect:/index/login";
    }


    @GetMapping("/delete-user")
    public String deleteUser(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        userService.deleteUser(id);
        return "redirect:/DogCatLoverPlatform/Sign-up";
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

    @GetMapping("/forgot")
    public String forgot(){
        return "forgotPassword";
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendOTP(HttpServletRequest request) {
        String email = request.getParameter("email");
        System.out.println(email);
        UserDTO checkEmailExist = userService.getUserByEmail(email);
        if(checkEmailExist != null) {
            String otp = OtpUtil.generateOTP();
            userService.sendOTP(email, otp, checkEmailExist);

        }
        BaseRespone baseRespone = new BaseRespone();
        baseRespone.setStatusCode(200);
        baseRespone.setMessage("");
        baseRespone.setData(checkEmailExist);
        return new ResponseEntity<>(baseRespone,HttpStatus.OK);
    }

/*
    @GetMapping("/login/oauth2/code/google")
    public Map<String, Object> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken){
        System.out.println(toPerson(oAuth2AuthenticationToken.getPrincipal().getAttributes()).getEmail());
        System.out.println(toPerson(oAuth2AuthenticationToken.getPrincipal().getAttributes()).getName());
        System.out.println(toPerson(oAuth2AuthenticationToken.getPrincipal().getAttributes()).getPicture());
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }

    public Root toPerson(Map<String, Object> map){
        if(map== null){
            return null;
        }
        Root root = new Root();
        root.setEmail((String) map.get("email"));
        root.setName((String) map.get("name"));
        root.setPicture((String) map.get("picture"));
        return root;
    }
*/




}
