package com.swp391.DogCatLoverPlatform.controller;

import com.swp391.DogCatLoverPlatform.dto.CommentDTO;
//import com.swp391.DogCatLoverPlatform.service.CommentService;
import com.swp391.DogCatLoverPlatform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

  @Autowired
  CommentService commentService;

   @GetMapping ("/view/{id}")
    public ResponseEntity<?> getComment(Model model, @PathVariable int id){
     List<CommentDTO> listCmt =  commentService.getCommentsByBlogId(id);
     model.addAttribute("list",listCmt);
        return new ResponseEntity<>(listCmt,HttpStatus.OK);
    }


//   @PostMapping("/{id}")
//   public String deleteCmt(@PathVariable int id) {
//     commentService.deleteCmtById(id);
//     return "redirect:/blog/{id}/detail";
//   }


}
