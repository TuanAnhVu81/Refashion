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

    public void sendVerificationEmail(String to, String verificationToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Xác Thực Email Của Bạn - Refashion");

        String verificationLink = "https://refashion-fqe8c7bfcgg5h0e7.southeastasia-01.azurewebsites.net/api/users/verify-email?token=" + verificationToken;

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

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String to, String resetToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
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

    public void sendOrderPaidEmail(String to, String orderId, String totalAmount, String shippingAddress) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Đơn hàng #" + orderId + " đã được thanh toán");

        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Đơn hàng <strong>#%s</strong> đã được thanh toán thành công với tổng số tiền <strong>%s</strong>.</p>
                    <p>Vui lòng chuẩn bị và gửi hàng đến địa chỉ: <strong>%s</strong>.</p>
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <div class="footer">
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, orderId, totalAmount, shippingAddress);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendOrderPaidConfirmationToBuyer(String to, String orderId, String totalAmount) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Đơn hàng #" + orderId + " đã được duyệt");

        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Đơn hàng <strong>#%s</strong> với tổng số tiền <strong>%s</strong> đã được thanh toán và duyệt thành công.</p>
                    <p>Vui lòng theo dõi thông tin đơn hàng để cập nhật trạng thái giao hàng.</p>
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <div class="footer">
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, orderId, totalAmount);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendOrderUnpaidEmail(String to, String orderId, String totalAmount) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Thanh toán đơn hàng #" + orderId + " không thành công");

        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Thanh toán cho đơn hàng <strong>#%s</strong> với tổng số tiền <strong>%s</strong> chưa được hoàn tất.</p>
                    <p>Vui lòng kiểm tra và thực hiện lại thanh toán để tiếp tục quá trình đặt hàng.</p>
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <div class="footer">
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, orderId, totalAmount);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendFundsTransferConfirmation(String to, String orderId, String totalAmount) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Xác nhận chuyển khoản cho đơn hàng #" + orderId);

        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Khoản thanh toán cho đơn hàng <strong>#%s</strong> với tổng số tiền <strong>%s</strong> đã được chuyển đến bạn.</p>
                    <p>Vui lòng kiểm tra tài khoản của bạn để xác nhận giao dịch.</p>
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <div class="footer">
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, orderId, totalAmount);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendProductApprovalEmail(String toEmail, String productName) {
        String subject = "Sản phẩm của bạn đã được duyệt!";
        String content = String.format(
                "<p>Xin chào,</p>" +
                        "<p>Sản phẩm <strong>%s</strong> của bạn đã được duyệt và hiện đã hiển thị trên nền tảng.</p>" +
                        "<p>Trân trọng,<br/>Refashion Team</p>", productName
        );

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại", e);
        }
    }

    public void sendFeaturePaymentConfirmation(String to, String productName, String amount, Integer durationDays) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Xác nhận thanh toán nổi bật cho sản phẩm " + productName);

        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Thanh toán để nổi bật sản phẩm <strong>%s</strong> với số tiền <strong>%s</strong> đã được xác nhận.</p>
                    <p>Sản phẩm của bạn sẽ được hiển thị nổi bật trong <strong>%d ngày</strong>.</p>
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <div class="footer">
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, productName, amount, durationDays);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendFeaturePaymentRejection(String to, String productName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setFrom("tbinhduong0101@gmail.com");
        helper.setSubject("Thanh toán nổi bật cho sản phẩm " + productName + " bị từ chối");

        String htmlContent = String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .footer { font-size: 12px; color: #777; margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h3>Xin chào!</h3>
                    <p>Thanh toán để nổi bật sản phẩm <strong>%s</strong> đã bị từ chối.</p>
                    <p>Vui lòng kiểm tra lại thông tin thanh toán hoặc liên hệ hỗ trợ để biết thêm chi tiết.</p>
                    <p>Trân trọng,<br>Đội ngũ Refashion</p>
                    <div class="footer">
                        <p>Email: tbinhduong0101@gmail.com</p>
                    </div>
                </div>
            </body>
            </html>
            """, productName);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}