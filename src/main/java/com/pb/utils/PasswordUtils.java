package com.pb.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtils {
    public static String encryptPassword(String password)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            
            byte[] messageDigest = md.digest(password.getBytes());
    
            /* The bytes array has bytes in decimal form.
               Converting it into hexadecimal format.
            */
            StringBuilder s = new StringBuilder();
            for(int i=0; i< messageDigest.length ;i++)
            {
                s.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            return s.toString();
        }
        
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
