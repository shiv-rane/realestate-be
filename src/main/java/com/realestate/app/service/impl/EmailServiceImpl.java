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
        return "Your " + fromName + " verification code is: " + otp + "\n\n"
                + "This code is valid for " + otpExpiryMinutes + " minutes.\n"
                + "If you did not request this verification code, please ignore this email.";
    }

    private String buildOtpHtmlTemplate(String otp) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "  <meta charset=\"UTF-8\">"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "  <title>Verification Code</title>"
                + "</head>"
                + "<body style=\"margin:0;padding:0;background-color:#f4f6f9;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;color:#1e293b;\">"
                + "  <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f4f6f9;padding:40px 10px;\">"
                + "    <tr>"
                + "      <td align=\"center\">"
                + "        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width:560px;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 16px rgba(0,0,0,0.06);border:1px solid #e2e8f0;\">"
                + "          <!-- Header -->"
                + "          <tr>"
                + "            <td style=\"background:linear-gradient(135deg,#1e3a8a 0%,#2563eb 100%);padding:32px 40px;text-align:center;\">"
                + "              <h1 style=\"margin:0;font-size:22px;font-weight:700;color:#ffffff;letter-spacing:-0.5px;\">"
                + fromName + "</h1>"
                + "              <p style=\"margin:6px 0 0;font-size:13px;color:#bfdbfe;font-weight:400;\">Secure Authentication Service</p>"
                + "            </td>"
                + "          </tr>"
                + "          <!-- Body Content -->"
                + "          <tr>"
                + "            <td style=\"padding:36px 40px 24px;\">"
                + "              <h2 style=\"margin:0 0 12px;font-size:18px;font-weight:600;color:#0f172a;\">Verification Code</h2>"
                + "              <p style=\"margin:0 0 24px;font-size:14px;line-height:1.6;color:#475569;\">"
                + "                Use the following 6-digit One-Time Password (OTP) to complete your login or registration. "
                + "                This code is valid for <strong>" + otpExpiryMinutes + " minutes</strong>."
                + "              </p>"
                + "              <!-- OTP Badge -->"
                + "              <div style=\"text-align:center;margin:28px 0;padding:20px;background:#f8fafc;border-radius:8px;border:1px dashed #cbd5e1;\">"
                + "                <span style=\"display:inline-block;font-size:36px;font-weight:800;letter-spacing:10px;color:#1e3a8a;font-family:Consolas,Monaco,'Courier New',monospace;padding-left:10px;\">"
                + otp
                + "                </span>"
                + "              </div>"
                + "              <!-- Security Notice -->"
                + "              <div style=\"background-color:#eff6ff;border-left:4px solid #3b82f6;padding:12px 16px;border-radius:4px;margin-bottom:24px;\">"
                + "                <p style=\"margin:0;font-size:12px;line-height:1.5;color:#1e40af;\">"
                + "                  <strong>Security Tip:</strong> Never share this code with anyone. Our support team will never ask for your verification code."
                + "                </p>"
                + "              </div>"
                + "              <p style=\"margin:0;font-size:13px;line-height:1.5;color:#64748b;\">"
                + "                If you did not request this OTP, please disregard this email. Your account remains secure."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "          <!-- Footer -->"
                + "          <tr>"
                + "            <td style=\"background:#f8fafc;padding:20px 40px;border-top:1px solid #f1f5f9;text-align:center;\">"
                + "              <p style=\"margin:0;font-size:11px;color:#94a3b8;\">"
                + "                &copy; " + fromName + ". All rights reserved.<br>"
                + "                This is an automated system email, please do not reply directly to this message."
                + "              </p>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";
    }
}
