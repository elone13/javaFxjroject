package com.eventmanager.util;

public class SessionUtil {
    private static String userRole;

    public static void setUserRole(String role) {
        userRole = role;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void clear() {
        userRole = null;
    }
} 