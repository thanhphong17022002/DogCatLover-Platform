package com.swp391.DogCatLoverPlatform.service;

import com.swp391.DogCatLoverPlatform.dto.BlogDTO;
import com.swp391.DogCatLoverPlatform.dto.BookingDTO;
import com.swp391.DogCatLoverPlatform.dto.UserDTO;
import com.swp391.DogCatLoverPlatform.entity.BlogEntity;
import com.swp391.DogCatLoverPlatform.entity.BookingEntity;
import com.swp391.DogCatLoverPlatform.entity.UserEntity;
import com.swp391.DogCatLoverPlatform.exception.MessageException;
import com.swp391.DogCatLoverPlatform.repository.BlogRepository;
import com.swp391.DogCatLoverPlatform.repository.BookingEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BookingEntityRepository bookingEntityRepository;

    @Autowired
    private BlogRepository blogRepository;

    public boolean createBooking(BookingEntity booking, UserDTO userDTO){
        UserEntity user = userService.userRepository.findByEmail(userDTO.getEmail());
        Optional<BlogEntity> blog = blogRepository.findById(booking.getBlogEntity_BookingEntity().getId());

        booking.setCreate_date(new java.util.Date(System.currentTimeMillis()));
        booking.setStatus(false);
        booking.setUserEntity_BookingEntity(user);
        booking.setTotal_price(blog.get().getPrice());
        bookingEntityRepository.save(booking);
        return true;
    }

    public List<BookingDTO> getByDateAndIdblog(Date date, Integer idBlog){
        List<BookingEntity> result = bookingEntityRepository.findByDate(date,idBlog);
        List<BookingDTO> dtos = new ArrayList<>();
        for(BookingEntity bookingEntity : result){
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(bookingEntity.getId());
            bookingDTO.setBookingDate(bookingEntity.getBookingDate());
            bookingDTO.setBookingTime(bookingEntity.getBookingTime());
            dtos.add(bookingDTO);
        }
        return dtos;
    }

    public List<BookingDTO> getBookingManager(int id_user){
        List<BookingEntity> bookingEntities = bookingEntityRepository.findByUserCreate(id_user);

        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for(BookingEntity booking : bookingEntities){
            BlogDTO blogDTO = blogService.getBlogById(booking.getBlogEntity_BookingEntity().getId());
            UserDTO userDTO = userService.getUserById(booking.getUserEntity_BookingEntity().getId());
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setPaying_method(booking.getPaying_method());
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingDate(booking.getBookingDate());
            bookingDTO.setBookingTime(booking.getBookingTime());
            bookingDTO.setTotal_price(booking.getBlogEntity_BookingEntity().getPrice());
            bookingDTO.setPaying_method(booking.getPaying_method());
            bookingDTO.setBlogDTO(blogDTO);
            bookingDTO.setUserDTO(userDTO);
            bookingDTO.setStatus(booking.isStatus());
            bookingDTOList.add(bookingDTO);
        }
        return bookingDTOList;
    }

    public List<BookingDTO> findByUserBooking(int id_user) {
        List<BookingEntity> bookingEntities = bookingEntityRepository.findByUserBooking(id_user);

        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for(BookingEntity booking : bookingEntities){
            BlogDTO blogDTO = blogService.getBlogById(booking.getBlogEntity_BookingEntity().getId());
            UserDTO userDTO = userService.getUserById(booking.getUserEntity_BookingEntity().getId());
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingDate(booking.getBookingDate());
            bookingDTO.setBookingTime(booking.getBookingTime());
            bookingDTO.setTotal_price(booking.getTotal_price());
            bookingDTO.setPaying_method(booking.getPaying_method());
            bookingDTO.setBlogDTO(blogDTO);
            bookingDTO.setUserDTO(userDTO);
            bookingDTOList.add(bookingDTO);
        }
        return bookingDTOList;
    }

    public void updateStatus(int id_user) {
        List<BookingDTO> list = findByUserBooking(id_user);
        for(BookingDTO dto : list){
            bookingEntityRepository.updateStatus(dto.getId());
        }

    }

    public void deleteById(int id_booking) {
        bookingEntityRepository.deleteById(id_booking);
    }

    public List<BookingDTO> getBookingHistory(int id_user) {
        List<BookingEntity> bookingEntities = bookingEntityRepository.findByUserBookingHistory(id_user);

        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for(BookingEntity booking : bookingEntities){
            BlogDTO blogDTO = blogService.getBlogById(booking.getBlogEntity_BookingEntity().getId());
            UserDTO userDTO = userService.getUserById(booking.getUserEntity_BookingEntity().getId());
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setBookingDate(booking.getBookingDate());
            bookingDTO.setBookingTime(booking.getBookingTime());
            bookingDTO.setTotal_price(booking.getTotal_price());
            bookingDTO.setPaying_method(booking.getPaying_method());
            bookingDTO.setStatus(booking.isStatus());
            bookingDTO.setBlogDTO(blogDTO);
            bookingDTO.setUserDTO(userDTO);
            bookingDTOList.add(bookingDTO);
        }
        return bookingDTOList;
    }
    public Long getCountBookingByBookingDate(Date date){
        Long bookingDTOS = bookingEntityRepository.countBookingByBookingDate(date);
        return bookingDTOS;
    }



    public List<BookingEntity> getFindUserBooking(int user){
        List<BookingEntity> listBookingEntity = bookingEntityRepository.findByUserBooking(user);
        return listBookingEntity;
    }

}
