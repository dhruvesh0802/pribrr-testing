package com.pb.utils;

import java.util.Random;

public class BaseUtils {
    public static int generateRandomOTP(){
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }
}
