package com.swp391.DogCatLoverPlatform.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil  {

    public static String generateOTP() {
        Random random = new Random();
        int otp = 100_000 + random.nextInt(900_000); // Random number between 100000 and 999999
        return String.valueOf(otp);
    }
}
