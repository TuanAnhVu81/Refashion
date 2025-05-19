package exe201.Refashion.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception"),
    USER_EXIST(1001, "User existed"),
    EMAIL_EXIST(1002, "Email existed"),
    INVALID_VERIFICATION_TOKEN(1003, "Token to verify is invalid"),
    EMAIL_ALREADY_VERIFIED(1004, "Email is verified already"),
    EMAIL_NOT_EXIST(1005, "User is not exist"),
    EMAIL_NOT_VERIFIED(1006, "You need to verify your email first"),
    UNAUTHENTICATED(1007, "Unauthenticated"),
    EMAIL_NOT_PROVIDED(1008, "You need to provide email"),
    ROLE_NOT_FOUND(1009, "Role not found"),
    INVALID_RESET_PASSWORD_TOKEN(1010, "Invalid reset password token"),
    RESET_PASSWORD_TOKEN_EXPIRED(1011, "Reset password token expired"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
