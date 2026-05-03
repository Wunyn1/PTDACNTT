package org.example.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashedPasswordUtils {
    private static final String password_regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static String hashedPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }
    public static boolean checkHashedPassword(String password, String hashed){
        try{
            return BCrypt.checkpw(password, hashed);
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isValidPassword(String password) {
        return password.matches(password_regex);
    }
}
