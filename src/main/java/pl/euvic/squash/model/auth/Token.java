package pl.euvic.squash.model.auth;

import pl.euvic.squash.model.response.UserRestModel;

import java.util.Date;
import java.util.Objects;

public class Token {

    private String token;

    private Date expirationDate;

    private UserRestModel user;

    public Token() {
    }

    public Token(String token, Date expirationDate, UserRestModel user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserRestModel getUser() {
        return user;
    }

    public void setUser(UserRestModel user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return Objects.equals(token, token1.token) &&
                Objects.equals(expirationDate, token1.expirationDate) &&
                Objects.equals(user, token1.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, expirationDate, user);
    }
}
