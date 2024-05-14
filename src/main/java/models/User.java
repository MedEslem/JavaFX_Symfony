package models;

import java.util.Arrays;

public class User {
    private int id;
    private String email;

    public User() {};

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String retrievedUsername, int retrievedId) {
        this.username = retrievedUsername;
        this.id = retrievedId;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String image_path;
    private String roles;

    public User(int id, String email, String image_path, String roles, String password, double wallet, String username, int is_verified, int access) {
        this.id = id;
        this.email = email;
        this.image_path = image_path;
        this.roles = roles;
        this.password = password;
        this.wallet = wallet;
        this.username = username;
        this.is_verified = is_verified;
        this.access = access;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    private String password;

    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    private double wallet;
    private String username;
    private int is_verified;
    private int access;
    public static final String ADMIN_ROLE_ID ="[\"ROLE_ADMIN\"]" ;
    public static final String CLIENT_ROLE_ID = "[]";

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", image_path='" + image_path + '\'' +
                ", roles='" + roles + '\'' +
                ", password='" + password + '\'' +
                ", wallet=" + wallet +
                ", username='" + username + '\'' +
                ", is_verified=" + is_verified +
                ", access=" + access +
                '}';
    }

    public String parseRolesFromString(String roleString) {
        if (roleString == null || roleString.isEmpty()) {
            return null;
        }

        // Handle single role string (assuming mapping to ID)
        if (roleString.equals("Admin")) {
            return ADMIN_ROLE_ID;
        } else if (roleString.equals("Client")) {
            return CLIENT_ROLE_ID;
        } else {
            return null;
        }
    }
    public String buildRoleStringFromRoles(String roles) {
        String R=null ;
        if (roles.equals(ADMIN_ROLE_ID)) {
                R="Admin";
            } else if (roles.equals(CLIENT_ROLE_ID)) {
            R="Client";
            } else {
                // Handle unexpected role IDs (e.g., log a warning or throw an exception)
                System.out.println("Warning: Unexpected role ID: " + roles);
            }


        return R;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }


}