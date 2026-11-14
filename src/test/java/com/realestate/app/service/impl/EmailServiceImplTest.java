package com.realestate.app.service.impl;

import com.realestate.app.exception.EmailSendingException;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.Emails;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private Resend resend;

    @Mock
    private Emails emails;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@bhoomiisetu.in");
        ReflectionTestUtils.setField(emailService, "fromName", "Bhoomi Setu");
        ReflectionTestUtils.setField(emailService, "otpExpiryMinutes", 10);

        when(resend.emails()).thenReturn(emails);
    }

    @Test
    @DisplayName("Should successfully send OTP email via Resend")
    void sendOtp_Success() throws Exception {
        String recipient = "buyer@example.com";
        String otp = "123456";

        CreateEmailResponse response = mock(CreateEmailResponse.class);
        when(response.getId()).thenReturn("resend-id-123");
        when(emails.send(any(CreateEmailOptions.class))).thenReturn(response);

        emailService.sendOtp(recipient, otp);

        ArgumentCaptor<CreateEmailOptions> captor = ArgumentCaptor.forClass(CreateEmailOptions.class);
        verify(emails, times(1)).send(captor.capture());

        CreateEmailOptions options = captor.getValue();
        assertThat(options.getTo()).contains(recipient);
        assertThat(options.getSubject()).contains("123456");
        assertThat(options.getFrom()).contains("noreply@bhoomiisetu.in");
    }

    @Test
    @DisplayName("Should throw EmailSendingException when Resend API fails")
    void sendOtp_ResendException_ThrowsEmailSendingException() throws Exception {
        String recipient = "buyer@example.com";
        String otp = "654321";

        when(emails.send(any(CreateEmailOptions.class)))
                .thenThrow(new ResendException("Resend API rate limit or error"));

        assertThatThrownBy(() -> emailService.sendOtp(recipient, otp))
                .isInstanceOf(EmailSendingException.class)
                .hasMessageContaining("Unable to send verification email");

        verify(emails, times(1)).send(any(CreateEmailOptions.class));
    }
}
