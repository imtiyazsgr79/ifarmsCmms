package in.ifarms.com.General;

public class CardDetails {
    String cardName;
    int cardImage;
    String token;
    String user;
    String role;

    public String getUser() {
        return user;
    }



    public String getToken() {
        return token;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCardName() {
        return cardName;
    }

    public int getCardImage() {
        return cardImage;
    }

    public CardDetails(String cardName, int cardImage, String token, String user, String role) {
        this.cardName = cardName;
        this.user = user;
        this.cardImage = cardImage;
        this.token = token;
        this.role = role;
    }
}
