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
    CATEGORY_NAME_EXISTED(1012,"Category name already existed"),
    CATEGORY_NOT_FOUND(1013, "Category not found"),
    USER_NOT_FOUND(1014, "User not found"),
    PRODUCT_NOT_FOUND(1015, "Product not found"),
    UPLOAD_IMAGE_FAIL(1016, " Upload image failed"),
    PRODUCT_ALREADY_IN_WISHLIST(1017, " Product already in Wishlist"),
    WISHLIST_NOT_FOUND(1018, "Wishlist not found"),
    UNAUTHORIZED(1019, "User not own the product"),
    INVALID_PAYMENT_AMOUNT(1020, "Invalid payment amount"),
    ORDER_NOT_FOUND(1021, "Order not found"),
    PAYMENT_NOT_FOUND(1022, "Payment not found"),
    INVALID_STATUS_TRANSITION(1023, " Invalid status transition"),
    INVALID_ORDER_STATUS(1024, "Invalid order status"),
    PASSWORD_CONFIRM_NOT_MATCH(1025, "Password confirm not match"),
    INVALID_ROLE(1026, "Invalid role"),
    BLOG_NOT_FOUND(1027, "Blog not found"),
    INVALID_PRODUCT_STATUS(1028, "Invalid product status"),
    PRODUCT_ALREADY_REVIEWED(1029, "Product already reviewed"),
    CART_ALREADY_EXISTS(1030, "Cart already exists"),
    CART_NOT_FOUND(1031, "Cart not found"),
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
