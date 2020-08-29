package pl.euvic.squash.security.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import pl.euvic.squash.exception.WrongRequestException;
import pl.euvic.squash.model.auth.Credentials;
import pl.euvic.squash.model.auth.Token;
import pl.euvic.squash.model.entity.User;
import pl.euvic.squash.model.repository.UserRepository;
import pl.euvic.squash.model.response.UserRestModel;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static pl.euvic.squash.exception.ExceptionMessage.TOKEN_NOT_ANY_USER;

@Component
public class JwtHelper {

    private static final int VALIDATION_TIME = 168 * 3600; //7 days
    private static final String SECRET = "hacktyki_2020";
    private final UserRepository userRepository;

    public JwtHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getUserFromSubject(String emailName) {
        try {
            return userRepository.findByEmail(emailName).get();
        } catch (WrongRequestException e) {
            throw new WrongRequestException(TOKEN_NOT_ANY_USER.getMessage());
        }
    }

    public User getUserFromToken(String token) throws WrongRequestException {
        if (token.startsWith("Bearer "))
            token = token.substring(7);

        String emailName = getSubjectFromToken(token);
        User user = getUserFromSubject(emailName);

        return user;
    }

    private Date calculateExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(Date.from(Instant.now()));
        c.add(Calendar.MILLISECOND, VALIDATION_TIME * 1000);
        return c.getTime();
    }

    public Token getToken(Credentials credentials) {
        Token token = new Token();
        Date expirationDate = calculateExpirationDate();
        User user = getUserFromSubject(credentials.getEmail());
        token.setToken(
                JWT.create()
                        .withIssuedAt(new Date())
                        .withSubject(credentials.getEmail())
                        .withExpiresAt(expirationDate)
                        // .withClaim("active", user.getActive())
                        .sign(HMAC512(SECRET.getBytes()))
        );

        token.setUser(new UserRestModel(user));
        token.setExpirationDate(expirationDate);
        return token;
    }

    public static String getSubjectFromToken(String token) {
        DecodedJWT decoded = JWT.require(HMAC512(SECRET.getBytes()))
                .build()
                .verify(token);

        return decoded.getSubject();
    }
}