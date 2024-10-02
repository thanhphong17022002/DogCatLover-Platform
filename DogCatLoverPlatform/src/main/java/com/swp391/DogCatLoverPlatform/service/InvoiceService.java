package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.config.ModelMapperConfig;
import com.swp391.DogCatLoverPlatform.dto.BlogDTO;
import com.swp391.DogCatLoverPlatform.dto.BookingDTO;
import com.swp391.DogCatLoverPlatform.dto.InvoiceDTO;
import com.swp391.DogCatLoverPlatform.dto.UserDTO;
import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.BookingEntity;
import com.swp391.DogCatLoverPlatform.entity.InvoiceEntity;
import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.repository.BlogRepository;
import com.swp391.DogCatLoverPlatform.repository.InvoiceRepository;
import com.swp391.DogCatLoverPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.sql.*;
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapperConfig modelMapperConfig;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;


    public InvoiceEntity saveInvoiceWallet(int idBlog, int idUser) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        Date date = new Date();
        invoiceEntity.setInvoice_date(date);

        Optional<BlogEntity> blogEntityOptional = blogRepository.findById(idBlog);

        if (blogEntityOptional.isPresent()) {
            BlogEntity blogEntity = blogEntityOptional.get();
            // Lấy giá từ đối tượng blog
            double price = blogEntity.getPrice();
            invoiceEntity.setTotal_amount(price);
            invoiceEntity.setPaying_method("Wallet");
            String title = blogEntity.getTitle();

            Optional<UserEntity> userEntityOptional = userRepository.findById(idUser);

            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                String email = userEntity.getEmail();
                invoiceEntity.setUserEntity(userEntity);
            } else {
                // Xử lý trường hợp không tìm thấy người dùng
                return null;
            }

            invoiceEntity.setBlogEntity(blogEntity);
            invoiceRepository.save(invoiceEntity);
            return invoiceEntity;
        } else {
            // Xử lý trường hợp không tìm thấy bài viết
            return null;
        }
    }

    public InvoiceEntity saveInvoice(int idBlog, int idUser) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        Date date = new Date();
        invoiceEntity.setInvoice_date(date);

        Optional<BlogEntity> blogEntityOptional = blogRepository.findById(idBlog);

        if (blogEntityOptional.isPresent()) {
            BlogEntity blogEntity = blogEntityOptional.get();
            // Lấy giá từ đối tượng blog
            double price = blogEntity.getPrice();
            invoiceEntity.setTotal_amount(price);
            invoiceEntity.setPaying_method("Paypal");
            String title = blogEntity.getTitle();

            Optional<UserEntity> userEntityOptional = userRepository.findById(idUser);

            if (userEntityOptional.isPresent()) {
                UserEntity userEntity = userEntityOptional.get();
                String email = userEntity.getEmail();
                invoiceEntity.setUserEntity(userEntity);
            } else {
                // Xử lý trường hợp không tìm thấy người dùng
                return null;
            }

            invoiceEntity.setBlogEntity(blogEntity);
            invoiceRepository.save(invoiceEntity);
            return invoiceEntity;
        } else {
            // Xử lý trường hợp không tìm thấy bài viết
            return null;
        }
    }

    public InvoiceDTO getInvoiceById(int id) {
        InvoiceEntity invoiceEntity = invoiceRepository.findById(id).orElse(null);
        if (invoiceEntity == null) {
            return null;
        }
        return modelMapperConfig.modelMapper().map(invoiceEntity, InvoiceDTO.class);
    }

    public List<InvoiceDTO> getSellManager(int id_user) {
        List<InvoiceEntity> invoiceEntities = invoiceRepository.findByUserCreate(id_user);
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();

        for (InvoiceEntity invoice : invoiceEntities) {
            BlogDTO blogDTO = blogService.getBlogById(invoice.getBlogEntity().getId());
            UserDTO userDTO = userService.getUserById(invoice.getUserEntity().getId());

            InvoiceDTO invoiceDTO = new InvoiceDTO();
            invoiceDTO.setPaying_method(invoice.getPaying_method());
            invoiceDTO.setId(invoice.getId());
            invoiceDTO.setInvoice_date(invoice.getInvoice_date());
            invoiceDTO.setTotal_amount(invoice.getTotal_amount());
            invoiceDTO.setIdBlog(blogDTO.getId());
            invoiceDTO.setIdUser(userDTO.getId());

            invoiceDTO.setEmail(userDTO.getEmail());
            invoiceDTO.setUserName(userDTO.getUserName());

            invoiceDTO.setTitle(blogDTO.getTitle());

            invoiceDTOS.add(invoiceDTO);
        }

        return invoiceDTOS;
    }


    public Long getCountInvoiceByBlogDate(Date dates){
        Long invoiceDTOs = invoiceRepository.countInvoiceByBlogDate((java.sql.Date) dates);
        return invoiceDTOs;
    }


}