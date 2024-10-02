package com.swp391.DogCatLoverPlatform.controller;

import com.swp391.DogCatLoverPlatform.dto.*;
import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.BlogTypeEntity;
import com.swp391.DogCatLoverPlatform.entity.*;
import com.swp391.DogCatLoverPlatform.exception.MessageException;
import com.swp391.DogCatLoverPlatform.service.*;
//import com.swp391.DogCatLoverPlatform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blog")

public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    UserService userService;

    @Autowired
    RequestService requestService;

    @Autowired
    CommentService commentService;

    @Autowired
    BlogTypeService blogTypeService;

    @Autowired
    PetCategoryService petCategoryService;

    @Autowired
    PetTypeService petTypeService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ChartService chartService;


    @PostMapping("/accepted-request")
    public String acceptedRequest(@RequestParam(name="requestId") int requestId,
                                  @RequestParam(name="currentUserId") int userIdAccepted,
                                  @RequestParam(name="requestUserId") int userIdRequest,
                                  @RequestParam(name="blogId") int blogId,
                                  RedirectAttributes redirectAttributes,
                                  @ModelAttribute("request") UserNotificationDTO userNotificationDTO){



        boolean isExist = userNotificationService.checkExistAcceptedRequest(userIdAccepted, blogId);

        if(isExist){
            redirectAttributes.addFlashAttribute("notice", "You've already chosen!");
        }else{
            requestService.acceptedRequest(userNotificationDTO, userIdRequest, userIdAccepted, requestId, blogId);
        }

        return "redirect:/blog/detail/myblog/" + blogId ;
    }

    //Gửi request (Dũng)
    @PostMapping("/view/send-request")
    public String addRequest(
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "blogId") int blogId,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("request") RequestDTO requestDTO) {

       boolean isExist = requestService.checkExistRequest(userId, blogId);
       if(isExist){
           redirectAttributes.addFlashAttribute("error", "Your request is on pending!");
       }else{
           requestService.AddRequest(requestDTO,userId, blogId);
           redirectAttributes.addFlashAttribute("sent", "Your request have been sent!");
       }

        return "redirect:/blog/detail/myblog/" +blogId;
    }

    //View List Request --> Đổi tên Notification
    @GetMapping("/view/view-request")
    public String viewNotification(Model model, HttpServletRequest req){
        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Mỗi lần bấm vào giao diện này, nó sẽ đánh dấu là đã xem và set status notification lại, để biến đếm không đếm nữa
            userNotificationService.markAsRead(user.getId());

            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotification(user.getId());
            //User đăng nhập hiện tại sẽ được xem danh sách những user khác đã gửi request trong bài Blog của mình.
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());

            model.addAttribute("listBlog", bookingDTOS);
            model.addAttribute("listNotification", userNotificationDTOS);
            model.addAttribute("currentUserId", user.getId());
        }
        return "list-request";
    }


    //Test phân trang
    @GetMapping("/view")
    public String GetPaginatedBlogs(Model model,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "3") int size
                                    ,HttpServletRequest req) {

        Page<BlogDTO> list = blogService.GetApprovedBlogs(page, size);
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listBlog", list);

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

        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();
        model.addAttribute("latestBlogs", latestBlogs);


        return "blog-standard";
    }


    //View My Blog
    @GetMapping("/view/myblog")
    public String viewMyBlog(Model model,
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

        Page<BlogDTO> list = blogService.GetAllMyBlog(user.getId(), page, size);
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listBlog", list);


        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();
        model.addAttribute("latestBlogs", latestBlogs);
        return "myblog";
    }



    @GetMapping("/search")
    public String viewSearch(Model model, @RequestParam("title") String title,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "3") int size,
                             HttpServletRequest req) {

        Page<BlogDTO> list = blogService.GetApprovedBlogs(page, size);
        model.addAttribute("totalPage", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("listBlog", list);
        model.addAttribute("title", title);

        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();
        model.addAttribute("latestBlogs", latestBlogs);

        UserDTO user  = getUserIdFromCookie(req);
        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);

        return "redirect:/blog/view";
    }

    @GetMapping("/searchTitle")
    public String viewSearchTitle(Model model, @RequestParam("title") String title,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "3") int size,
                             HttpServletRequest req) {

        Page<BlogDTO> lists = blogService.GetBlogsByTitle(title, page, size);
        model.addAttribute("totalPage", lists.getTotalPages()); // Thêm biến totalPage vào Model
        model.addAttribute("currentPage", page);
        model.addAttribute("listBlogs", lists);
        model.addAttribute("title",title);

        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();
        model.addAttribute("latestBlogs", latestBlogs);

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);

        return "blog-search-title";
    }

    @PostMapping("/search")
    public String searchBlogByTitle(
            @RequestParam("title") String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            Model model, HttpServletRequest req) {

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);

        if (title.trim().isEmpty()) {
           //Trường hợp tiêu đề rỗng hoặc khoảng trắng
            Page<BlogDTO> list = blogService.GetApprovedBlogs(page, size);
            model.addAttribute("totalPage", list.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("listBlog", list);

            if(user != null){
                //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
                List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
                List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
                int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
                model.addAttribute("count", totalCount);
            }

            return "redirect:/blog/view";

        } else {
            // Trường hợp có tiêu đề, phân trang theo tiêu đề
            Page<BlogDTO> lists = blogService.GetBlogsByTitle(title, page, size);
            model.addAttribute("totalPage", lists.getTotalPages()); // Thêm biến totalPage vào Model
            model.addAttribute("currentPage", page);
            model.addAttribute("listBlogs", lists);
            model.addAttribute("title", title);
            if(user != null){
                //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
                List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
                List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
                int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
                model.addAttribute("count", totalCount);
            }

            if (lists.isEmpty()) {
                model.addAttribute("msg", "Searching result is not found!!");
                if(user != null){
                    //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
                    List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
                    List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
                    int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
                    model.addAttribute("count", totalCount);
                }
            } else {
                model.addAttribute("listBlogs", lists);
                model.addAttribute("title", title);
                if(user != null){
                    //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
                    List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
                    List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
                    int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
                    model.addAttribute("count", totalCount);
                }
            }
        }

        // Lấy danh sách 3 bài viết mới nhất
        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();
        model.addAttribute("latestBlogs", latestBlogs);

        return "blog-search-title";
    }

    @GetMapping("/byType")
    public String showBlogsByType(@RequestParam("type") String type,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "3") int size,
                                  Model model, HttpServletRequest req) {

        model.addAttribute("blogType", type);
        Page<BlogDTO> blogs = blogService.getBlogsByType(type, page, size);
        model.addAttribute("totalPage", blogs.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("blogs", blogs);

        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();
        model.addAttribute("latestBlogs", latestBlogs);

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);
        return "blog-type";
    }

    @GetMapping("/{id}/edit")
    public String viewUpdateForm(@PathVariable("id") int id, Model model, HttpServletRequest req) {
        BlogDTO blogDTO = blogService.getBlogById(id);
        model.addAttribute("blog", blogDTO);

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);
        return "update-blog-form";
    }

    @PostMapping("/{id}/edit")
    public String updateBlog(@PathVariable("id") int id, @ModelAttribute("blog") BlogUpdateDTO blogUpdateDTO) throws ParseException {
        blogService.updateBlog(id, blogUpdateDTO);
        return "redirect:/blog/view";
    }

    @GetMapping("/create")
    public String viewCreateForm(Model model, HttpServletRequest req) {
        List<BlogTypeEntity> listBlogType = blogTypeService.getAllBlogType();

        List<PetTypeEntity> listPettype = petTypeService.getAllPetType();

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);

        model.addAttribute("blogTypes", listBlogType);
        model.addAttribute("petTypes",listPettype);
        model.addAttribute("blog", new BlogDTO());
        return "create-blog-form";
    }


@PostMapping("/create")
public String createNewBlog(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
    int blogType = Integer.parseInt(request.getParameter("idBlogType"));
    int petType = Integer.parseInt(request.getParameter("idPetType"));
    String image = blogService.saveImageAndReturnPath(file);
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    double price = Double.parseDouble(request.getParameter("price"));
    String petName = request.getParameter("petName");
    String petBreed = request.getParameter("petBreed");
    int petAge = Integer.parseInt(request.getParameter("petAge"));
    double petWeight = Double.parseDouble(request.getParameter("petWeight"));
    String petColor = request.getParameter("petColor");
    UserDTO userDTO = getUserIdFromCookie(request);
    BlogEntity blogEntity = blogService.createNewBlog(blogType,petType,image,title,content,price,petName,petBreed,petAge,petWeight,petColor,userDTO.getId());


    return "redirect:/blog/view";
}



    @PostMapping("/create_comment")
    public String createComment(@ModelAttribute("comment") CommentDTO commentDTO, HttpServletRequest req){
        UserDTO user  = getUserIdFromCookie(req);
        String description = req.getParameter("description");

        int id_blog = commentDTO.getId();
        BlogDTO blog = blogService.getBlogById(id_blog);
        commentService.createComment(commentDTO, description, id_blog, user.getId());

        // Chuyển hướng người dùng đến trang chi tiết của bài blog

        return "redirect:/blog/detail/myblog/"+ id_blog;
    }

    @PostMapping("/delete")
    public String deleteBlog(HttpServletRequest req) {
        // Extract the user's ID from the cookies (if available)
        int idBlog = Integer.parseInt(req.getParameter("idBlog"));
        UserDTO user  = getUserIdFromCookie(req);
        blogService.deleteBlogById(idBlog);
        return "redirect:/blog/view/myblog";
    }


    @GetMapping("/detail/{id}")
    public String viewDetailsBlog(@PathVariable("id") int id, Model model, HttpServletRequest req) {
        BlogDTO blogDTO = blogService.getBlogById(id);
        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();

        // Get comments for the blog by its ID
        List<CommentDTO> comments = commentService.getCommentsByBlogId(id);

        model.addAttribute("latestBlogs", latestBlogs);
        model.addAttribute("blog", blogDTO);

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }
        model.addAttribute("user", user);

        // Add the comments to the model
        model.addAttribute("comments", comments);

        return "blog-details";
    }


    @GetMapping("/detail/myblog/{id}")
    public String viewMyBlogDetails(@PathVariable("id") int id, Model model, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        BlogDTO blogDTO = blogService.getBlogById(id);
        List<BlogDTO> latestBlogs = blogService.getThreeLatestBlogs();

        model.addAttribute("latestBlogs", latestBlogs);
        model.addAttribute("blog", blogDTO);

        UserDTO user  = getUserIdFromCookie(req);

        if(user != null){
            //Đã cập nhật lại, mỗi lần xem thông báo rồi sẽ set lại số lượng cho biến count
            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());

            //User đăng nhập hiện tại sẽ được xem danh sách những user khác đã gửi request trong bài Blog của mình.
            List<RequestDTO> bookingDTOS = requestService.viewSendRequest(user.getId());

            //Số lượng Blog đang có request của User bên trong
            List<RequestDTO> listBlogRequest = requestService.viewSendBlogRequest(user.getId());

            int totalCount = listBlogRequest.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
            //Danh sách request gửi trong bài Blog
            model.addAttribute("listBlog", bookingDTOS);

            //User đăng nhập hiện tại
            model.addAttribute("currentUserId", user.getId());
        }
        model.addAttribute("user", user);

        // Get comments for the blog by its ID
        // Add the comments to the model
        List<CommentDTO> comments = commentService.getCommentsByBlogId(id);
        model.addAttribute("comments", comments);

        //Thông báo đã gửi request rồi, không được gửi nữa (tránh spam)
        String error = (String) redirectAttributes.getFlashAttributes().get("error");
        String sent = (String) redirectAttributes.getFlashAttributes().get("sent");

        //Thông báo đã chọn được 1 người trong danh sách gửi request rồi
        String notice = (String) redirectAttributes.getFlashAttributes().get("notice");

        if (error != null) {
            model.addAttribute("error", error);
        }

        if(sent != null){
            model.addAttribute("sent", sent);
        }

        if (notice != null) {
            model.addAttribute("notice", notice);
        }


        return "blog-details-myblog";
    }


    @PostMapping("/delete_comment")
    public String deleteComment(@RequestParam("commentId") int commentId, HttpServletRequest req) {
        UserDTO currentUser = getUserIdFromCookie(req);
        int blogId = Integer.parseInt(req.getParameter("blogId"));
        BlogDTO blogDTO = blogService.getBlogById(blogId);


        if (currentUser != null) {
            if (blogDTO != null && currentUser.getUserName().equals(blogDTO.getUserName()))
                commentService.deleteComment(commentId);
        } else {

        }

        return "redirect:/blog/" + blogId + "/detail";
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

    @PostMapping("/staff/process")
    public String processBlog(
            @RequestParam("blogId") int blogId,
            @RequestParam("action") String action,
            @RequestParam(value = "reason", required = false) String reason,
            HttpServletRequest req
    ) {
        UserDTO user = getUserIdFromCookie(req);
        if ("approve".equals(action)) {
            // Approve the blog
            blogService.approveBlog(blogId);

        } else if ("reject".equals(action)) {
            // Reject the blog
            blogService.rejectBlog(blogId, reason); // Add this method to your BlogService
            // Send a rejection email

            if (user != null) {
               Optional<BlogEntity> blogEntity = blogService.blogRepository.findById(blogId);
                emailService.sendEmail(blogEntity.get().getUserEntity().getEmail(), reason);
            }
        }
        return "redirect:/staff/view/pending";
    }

    @GetMapping("/trash")
    public String viewUserTrash(Model model, @RequestParam(value = "updated", required = false) String updated, HttpServletRequest req) {
        UserDTO user = getUserIdFromCookie(req);

        if (user != null) {
            List<BlogDTO> userRejectBlogs = blogService.getBlogsReject(user.getId());
            model.addAttribute("rejectBlog", userRejectBlogs);
            model.addAttribute("user", user);

            List<UserNotificationDTO> userNotificationDTOS = userNotificationService.viewAllNotificationCount(user.getId());
            List<RequestDTO> bookingDTOS = requestService.viewSendBlogRequest(user.getId());
            int totalCount = bookingDTOS.size() + userNotificationDTOS.size();
            model.addAttribute("count", totalCount);
        }

        List<BlogTypeEntity> listBlogType = blogTypeService.getAllBlogType();
        List<PetTypeEntity> listPettype = petTypeService.getAllPetType();
        model.addAttribute("blogTypes", listBlogType);
        model.addAttribute("petTypes", listPettype);
        model.addAttribute("updated", updated);

        return "trash";
    }


    @PostMapping("/trash")
    public String updateAndResubmitOrDeleteBlog(
            @RequestParam("blogId") int blogId,
            @ModelAttribute("blog") BlogUpdateDTO blogUpdateDTO,
            @RequestParam("action") String action) {

        if ("resubmit".equals(action)) {
            // Update the blog and set its confirmation status to null
            blogService.updateAndSetConfirmToNull(blogId, blogUpdateDTO);
        } else if ("delete".equals(action)) {
            // Delete the blog
            blogService.deleteBlogById(blogId);
        }

        return "redirect:/blog/trash";
    }


    @PostMapping("/buyByWallet")
    public String buyByWallet(HttpServletRequest req, Model model) {
        UserDTO user = getUserIdFromCookie(req);
        int idBlog = Integer.parseInt(req.getParameter("idBlog"));
        Optional<BlogEntity> blogEntity = blogService.blogRepository.findById(idBlog);
        boolean result = false;
        if (user == null) {
            return "redirect:/index/login";
        } else {

            if (user.getBalance() == null || user.getBalance() < blogEntity.get().getPrice()) {
                model.addAttribute("errBalance",true);
                return "redirect:/deposite-history/deposit";
            }


            userService.transfer(user.getId(),idBlog);
            InvoiceEntity invoiceEntity = invoiceService.saveInvoiceWallet(idBlog, user.getId());
            blogService.updateBlogToFalse(idBlog);



            return "redirect:/invoice?id=" + invoiceEntity.getId();
        }
    }

    @GetMapping("/statistic-blog-service")
    public ResponseEntity<?> statisticBlogNService(){
        StatisticBlogNServiceDTO getAllBlogNService = chartService.countBlogNService();

        return new ResponseEntity<>(getAllBlogNService, HttpStatus.OK);
    }

}