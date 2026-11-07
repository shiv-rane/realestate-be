package com.realestate.app.service.impl;

import com.realestate.app.exception.EmailSendingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "devconnect2027@gmail.com");
        ReflectionTestUtils.setField(emailService, "fromName", "Real Estate Marketplace");
        ReflectionTestUtils.setField(emailService, "otpExpiryMinutes", 10);

        mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    @DisplayName("Should successfully send OTP email with correct recipient and subject")
    void sendOtp_Success() throws Exception {
        String recipient = "buyer@example.com";
        String otp = "123456";

        emailService.sendOtp(recipient, otp);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);

        assertThat(mimeMessage.getAllRecipients()[0].toString()).isEqualTo(recipient);
        assertThat(mimeMessage.getSubject()).contains("123456");
        assertThat(mimeMessage.getSubject()).contains("Real Estate Marketplace");
    }

    @Test
    @DisplayName("Should throw EmailSendingException when mail delivery fails")
    void sendOtp_MailException_ThrowsEmailSendingException() {
        String recipient = "buyer@example.com";
        String otp = "654321";

        doThrow(new MailSendException("SMTP connection timed out"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> emailService.sendOtp(recipient, otp))
                .isInstanceOf(EmailSendingException.class)
                .hasMessageContaining("Unable to send verification email");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
