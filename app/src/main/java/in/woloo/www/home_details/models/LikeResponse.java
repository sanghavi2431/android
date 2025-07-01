package in.woloo.www.home_details.models;


@SuppressWarnings("all")
public class LikeResponse {
    private final int code;

    private final String status;

    private final String message;

    public LikeResponse(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
