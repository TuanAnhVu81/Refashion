package exe201.Refashion.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * Gửi email xác thực đến người dùng với link xác thực.
     *
     * @param to                Địa chỉ email người nhận
     * @param verificationToken Token xác thực để tạo link
     * @throws MessagingException Nếu có lỗi khi gửi email
     */
    public void sendVerificationEmail(String to, String verificationToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Cấu hình thông tin cơ bản của email
        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com"); // Thay bằng email của bạn
        helper.setSubject("Xác Thực Email Của Bạn - Refashion");

        // Tạo link xác thực
        String verificationLink = "https://refashion-fqe8c7bfcgg5h0e7.southeastasia-01.azurewebsites.net/api/users/verify-email?token=" + verificationToken;

        // Nội dung HTML được định dạng đẹp và chuyên nghiệp
        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .button { display: inline-block; padding: 10px 20px; background-color: #007BFF; color: #fff !important; text-decoration: none; border-radius: 5px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Cảm ơn bạn đã đăng ký tài khoản tại Refashion. Vui lòng xác thực email của bạn bằng cách nhấn vào nút bên dưới:</p>
                    <p><a href="%s" class="button">Xác Thực Email Ngay</a></p>
                    <p>Link này sẽ hết hạn sau <strong>24 giờ</strong>. Nếu bạn không thực hiện hành động này, bạn có thể yêu cầu một link xác thực mới.</p>
                    <p>Nếu bạn không đăng ký tài khoản, vui lòng bỏ qua email này.</p>
                    <div class="footer">
                        <p>Trân trọng,<br>Đội ngũ Refashion</p>
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, verificationLink);

        // Đặt nội dung email dưới dạng HTML
        helper.setText(htmlContent, true);

        // Gửi email
        mailSender.send(message);
    }

    /**
     * Gửi email đặt lại mật khẩu với link chứa token.
     *
     * @param to    Địa chỉ email người nhận
     * @param resetToken Token để reset mật khẩu
     * @throws MessagingException Nếu có lỗi khi gửi email
     */
    public void sendResetPasswordEmail(String to, String resetToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com"); // email của bạn
        helper.setSubject("Yêu Cầu Đặt Lại Mật Khẩu - Refashion");

        String resetLink = "https://refashion-fqe8c7bfcgg5h0e7.southeastasia-01.azurewebsites.net/api/reset-password?token=" + resetToken;

        String htmlContent = String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .button { display: inline-block; padding: 10px 20px; background-color: #28a745; color: #fff !important; text-decoration: none; border-radius: 5px; }
                .footer { font-size: 12px; color: #777; margin-top: 20px; }
            </style>
        </head>
        <body>
            <div class="container">
                <h3>Xin chào!</h3>
                <p>Bạn hoặc ai đó đã yêu cầu đặt lại mật khẩu cho tài khoản tại Refashion.</p>
                <p>Vui lòng nhấn vào nút bên dưới để tiến hành đặt lại mật khẩu:</p>
                <p><a href="%s" class="button">Đặt Lại Mật Khẩu</a></p>
                <p>Link này sẽ hết hạn sau <strong>30 phút</strong>. Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email.</p>
                <div class="footer">
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <p>Email: tbinhduong0101@gmail.com</p>
                </div>
            </div>
        </body>
        </html>
        """, resetLink);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

}