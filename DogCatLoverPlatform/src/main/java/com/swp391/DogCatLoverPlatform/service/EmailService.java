package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.dto.BlogDTO;
import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.PetCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BlogService blogService;




    public void sendEmail(String to, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("minhtampro4545@gmail.com");
        message.setTo(to);
        message.setSubject("Blog Notice");
        message.setText("From Scooby with love:\n\n" + reason);

        javaMailSender.send(message);
    }

    public void sendPurchaseNotification(String to, String blogTitle, int blogID) {
        String subject = "Notice: Have someone buy your cat/dog in Scooby";
        String message = "Hi friend,\n\n";
        message += "Have someone buy cat/dog from DogCatLoverPlatform.\n";
        message += "Blog title: " + blogTitle + "\n";
        message += "Thank you for using our service.\n";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("minhtampro4545@gmail.com");
        Optional<BlogEntity> blogEntity = blogService.blogRepository.findById(blogID);

        if (blogEntity.isPresent()) {
            emailMessage.setTo(blogEntity.get().getUserEntity().getEmail());
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            javaMailSender.send(emailMessage);
        }
    }

}



