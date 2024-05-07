package com.example.cafekiosk.spring.api.service.mail;

import com.example.cafekiosk.spring.client.MailSendClient;
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("메일 전송 테스트")
    void sendMail() {
        // given - 상황 만들기
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // when - 동작
        boolean result = mailService.sendMail("", "", "", "");

        // then - 검증
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
    }

}