package pl.euvic.squash.security.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.euvic.squash.exception.ExceptionView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletHelper {

    private static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static void sendException(ExceptionView exceptionView, HttpServletResponse response) {
        try {
            response.setStatus(exceptionView.getStatus());
            response.getWriter().write(convertObjectToJson(exceptionView));
        } catch (IOException ex) {

        }
    }

}
