package org.example.utils;

public class UsernameUtils {
    private static final String username_regex = "^[a-zA-Z0-9._-]{3,16}$";
    public static boolean isValidUsername(String username) {
        return username.matches(username_regex);
    }
}
