package pl.euvic.squash.exception;

public class ExceptionView {
    Integer status;
    String name;
    String message;

    public ExceptionView(Integer status, String name, String message) {
        this.status = status;
        this.name = name;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
