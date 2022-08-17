package serializers;

public class Error {
    private final String type = "error";
    private String message;

    public Error(String message) {
        this.message = message;
    }

}
