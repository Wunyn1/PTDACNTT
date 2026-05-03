package org.example.utils;

public class UsernameUtils {
    private static final String username_regex = "^[a-zA-Z0-9_-]{3,16}$";
    public static boolean isValidUsername(String username) {
        return username.matches(username_regex);
    }
}
