package utils;

import models.User;

public class SessionManager {
    private static String username;
    private static int id;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        SessionManager.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        SessionManager.email = email;
    }

    public static String getImage_path() {
        return image_path;
    }

    public static void setImage_path(String image_path) {
        SessionManager.image_path = image_path;
    }

    public static long[] getRoles() {
        return roles;
    }

    public static void setRoles(long[] roles) {
        SessionManager.roles = roles;
    }

    private static String email;
    private static String image_path;
    private static long[] roles;
    private static double wallet;
    private static String password;

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SessionManager.password = password;
    }

    public static int getAccess() {
        return access;
    }

    public static void setAccess(int access) {
        SessionManager.access = access;
    }

    private static int access;
    private final int is_verified=0;

    public static double getWallet() {
        return wallet;
    }

    public static void setWallet(double wallet) {
        SessionManager.wallet = wallet;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionManager.username = username;
    }
    public static User UpdateUser() {
        return new User(id,email,image_path,roles,password,wallet,username,0, access);
    }
}