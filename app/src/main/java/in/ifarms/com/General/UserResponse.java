package in.ifarms.com.General;

import java.util.ArrayList;

public class UserResponse {

    String token;
    String role;
    String username;
    ArrayList workspacelist;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList getWorkspacelist() {
        return workspacelist;
    }

    public void setWorkspacelist(ArrayList workspacelist) {
        this.workspacelist = workspacelist;
    }
}
