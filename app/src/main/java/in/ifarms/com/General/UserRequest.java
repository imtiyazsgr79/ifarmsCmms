package in.ifarms.com.General;

public class UserRequest {

    String username;
    String password;
    String deviceToken;

    public UserRequest(String username, String password, String deviceToken) {
        this.username = username;
        this.password = password;
        this.deviceToken = deviceToken;
    }

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
