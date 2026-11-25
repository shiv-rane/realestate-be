package com.realestate.app.service.impl;

import com.realestate.app.exception.EmailSendingException;
import com.realestate.app.service.EmailService;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Production implementation of {@link EmailService} using the Resend HTTPS API
 * (Port 443).
 * Sends rich HTML emails containing the OTP verification code to users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final Resend resend;

    @Value("${resend.from-email:noreply@bhoomiisetu.in}")
    private String fromEmail;

    @Value("${app.mail.from-name:Bhoomi Setu}")
    private String fromName;

    @Value("${app.otp.expiry-minutes:10}")
    private int otpExpiryMinutes;

    @Override
    public void sendOtp(String toEmail, String otp) {
        log.info("======================================================");
        log.info("  [OTP CODE] To: {} | Code: {}", toEmail, otp);
        log.info("======================================================");

        try {
            String htmlContent = buildOtpHtmlTemplate(otp);
            String plainText = buildOtpPlainText(otp);

            String sender = StringUtils.hasText(fromName)
                    ? fromName + " <" + fromEmail + ">"
                    : fromEmail;

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(sender)
                    .to(toEmail)
                    .subject(otp + " is your " + fromName + " verification code")
                    .html(htmlContent)
                    .text(plainText)
                    .build();

            CreateEmailResponse response = resend.emails().send(params);
            log.info("OTP email successfully sent via Resend to [{}] with ID: {}", toEmail, response.getId());

        } catch (ResendException ex) {
            log.error("Resend API error sending OTP email to [{}]: {}", toEmail, ex.getMessage(), ex);
            throw new EmailSendingException(
                    "Unable to send verification email. Please check your email address or try again later.",
                    ex);
        } catch (Exception ex) {
            log.error("Unexpected error sending OTP email to [{}]: {}", toEmail, ex.getMessage(), ex);
            throw new EmailSendingException(
                    "Unable to send verification email. Please check your email address or try again later.",
                    ex);
        }
    }

    private String buildOtpPlainText(String otp) {
        return "Hello,\n\n"
                + "Your One-Time Password (OTP) for account verification is:\n\n"
                + otp + "\n\n"
                + "This OTP is valid for " + otpExpiryMinutes + " minutes. Please do not share this code with anyone.\n\n"
                + "If you didn't request this code, please ignore this email.\n\n"
                + "Thank you for using our service!";
    }

    private String buildOtpHtmlTemplate(String otp) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>OTP Verification</title>\n"
                + "</head>\n"
                + "<body style=\"margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f4f4f5; color: #333333;\">\n"
                + "    <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #f4f4f5; padding: 60px 20px;\">\n"
                + "        <tr>\n"
                + "            <td align=\"center\">\n"
                + "                <!-- Main Container -->\n"
                + "                <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"max-width: 480px; background-color: #ffffff; border-radius: 8px; padding: 40px; box-shadow: 0 2px 8px rgba(0,0,0,0.04);\">\n"
                + "                    <tr>\n"
                + "                        <td style=\"text-align: left;\">\n"
                + "                            \n"
                + "                            <h1 style=\"margin-top: 0; font-size: 20px; color: #111827; font-weight: 600;\">Hello,</h1>\n"
                + "                            \n"
                + "                            <p style=\"font-size: 15px; line-height: 1.6; color: #4b5563; margin-bottom: 24px;\">\n"
                + "                                Your One-Time Password (OTP) for account verification is:\n"
                + "                            </p>\n"
                + "                            \n"
                + "                            <!-- OTP Box -->\n"
                + "                            <div style=\"text-align: center; margin: 32px 0;\">\n"
                + "                                <span style=\"display: inline-block; font-size: 32px; font-weight: 700; letter-spacing: 8px; color: #111827; background-color: #f9fafb; border: 1px solid #e5e7eb; padding: 16px 24px; border-radius: 6px; text-indent: 8px;\">\n"
                + "                                    " + otp + "\n"
                + "                                </span>\n"
                + "                            </div>\n"
                + "\n"
                + "                            <p style=\"font-size: 14px; line-height: 1.6; color: #4b5563; margin-bottom: 16px;\">\n"
                + "                                This OTP is valid for <strong>" + otpExpiryMinutes + " minutes</strong>. Please do not share this code with anyone.\n"
                + "                            </p>\n"
                + "                            \n"
                + "                            <p style=\"font-size: 14px; line-height: 1.6; color: #6b7280; margin-bottom: 32px;\">\n"
                + "                                If you didn't request this code, please ignore this email.\n"
                + "                            </p>\n"
                + "                            \n"
                + "                            <!-- Divider -->\n"
                + "                            <hr style=\"border: none; border-top: 1px solid #f3f4f6; margin: 0 0 24px 0;\">\n"
                + "                            \n"
                + "                            <!-- Footer -->\n"
                + "                            <p style=\"font-size: 13px; color: #9ca3af; margin: 0;\">\n"
                + "                                Thank you for using our service!\n"
                + "                            </p>\n"
                + "\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>\n"
                + "                <!-- End Main Container -->\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>\n"
                + "</body>\n"
                + "</html>";
    }
}
