package pl.euvic.squash.exception;

public enum ExceptionMessage {

    ROLE_NOT_FOUND("There is no such role in the database"),
    TOKEN_NOT_ANY_USER("Token does not specify any user"),
    UNAUTHORIZED("Unauthorized"),
    WRONG_CREDENTIALS("Wrong credentials"),
    WRONG_EMAIL("Existing user is already assigned to this email"),
    WRONG_TOKEN("You are not authorized to do this operation"),
    WRONG_TIME("The reservation must be on the full hour"),
    USER_NOT_FOUND("There is no user with such an ID"),
    NOT_ACTIVE("This court is not active"),
    CLUB_ALREADY_EXIST("Club with this name or adress already exist"),
    CLUB_NOT_EXIST("There is no club with such an id"),
    COURT_NOT_EXIST("There is no court with such an id"),
    RESERVATION_IS_PRESENT("This date and time is already reserved"),
    RESERVATION_NOT_EXIST("There is no reservation with such an ID"),
    TIME_BEFORE_CURRENT_DATE("Chosen time is in past"),
    TOO_MANY_RESERVATIONS("You can have only 2 active reservations"),
    INCORRECT_PHONE_NUMBER("Incorrect phone number"),
    WRONG_PHONE_NUMBER("This phone number is already used");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
