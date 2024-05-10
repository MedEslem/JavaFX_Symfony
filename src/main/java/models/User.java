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
    private long[] roles;

    public User(int id, String email, String image_path, long[] roles, String password, double wallet, String username, int is_verified, int access) {
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

    public long[] getRoles() {
        return roles;
    }

    public void setRoles(long[] roles) {
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
    public static final long ADMIN_ROLE_ID = 1;
    public static final long CLIENT_ROLE_ID = 2;
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", image_path='" + image_path + '\'' +
                ", roles='" + createRolesString(roles) + '\'' +
                ", password='" + password + '\'' +
                ", wallet=" + wallet +
                ", username='" + username + '\'' +
                ", is_verified=" + is_verified +
                ", access=" + access +
                '}';
    }
    public long[] parseRolesFromString(String roleString) {
        if (roleString == null || roleString.isEmpty()) {
            return new long[0];
        }

        // Handle single role string (assuming mapping to ID)
        if (roleString.equals("Admin")) {
            return new long[]{ ADMIN_ROLE_ID};
        } else if (roleString.equals("Client")) {
            return new long[]{ CLIENT_ROLE_ID };
        } else {
            return new long[0];
        }
    }
    public String createRolesString(long[] roles) {
        if (roles != null && roles.length > 0) {
            return String.join(",", Arrays.stream(roles).mapToObj(String::valueOf).toArray(String[]::new));
        } else {
            return ""; // Return an empty string if roles is null or empty
        }
    }
    public String buildRoleStringFromRoles(long[] roles) {
        if (roles == null || roles.length == 0) {
            return "";  // Empty string for no roles
        }

        // Use a StringBuilder for efficient string concatenation
        StringBuilder roleString = new StringBuilder();

        for (long roleId : roles) {
            if (roleId == ADMIN_ROLE_ID) {
                roleString.append("Admin");
            } else if (roleId == CLIENT_ROLE_ID) {
                roleString.append("Client");
            } else {
                // Handle unexpected role IDs (e.g., log a warning or throw an exception)
                System.out.println("Warning: Unexpected role ID: " + roleId);
            }

            // Use a comma as a separator if multiple roles
            if (roles.length > 1 && roleId != roles[roles.length - 1]) {
                roleString.append(", ");
            }
        }

        return roleString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }


}