package com.ios.backend;

import com.ios.backend.entities.Passcode;
import com.ios.backend.entities.Program;
import com.ios.backend.repositories.PasscodeRepository;
import com.ios.backend.services.MailService;
import com.ios.backend.utils.AuthUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.testng.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceMutationTest {

    @Mock
    JavaMailSender mailSender;

    @Mock
    PasscodeRepository passcodeRepository;

    @Mock
    AuthUtils authUtils;


    @InjectMocks
    private MailService mailService;


    @Test
    public void generatePasscodeForProgramTest() {
        long programId = 1L;
        String code = AuthUtils.OTP(8, programId);
        Passcode passcode = new Passcode();
        passcode.setCode(code);
        passcode.setPid(programId);
        Passcode result = mailService.generatePasscodeForProgram(programId);
        assertEquals(result.getCode(), code);
        assertEquals(result.getPid(), programId);
    }

    @Test
    public void sendPasscodeTestShouldSendMailSuccesfully() throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        String email = "test@test.com";
        String passcode = "passcode";
        Program program = new Program();
        program.setId(1L);
        program.setName("New program");
        String text = "<html><body>You are invited to join program:\n" + program.getId() + " | " + program.getName();
        text = text + "\n\nUse passcode given below:\n" + passcode + "\n\n";
        text = text + program.getDescription() + "</body></html>";
        String subject = "Invitation to Join Program: " + program.getId() + " | " + program.getName();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        try {
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        assertEquals(mailService.sendPasscode(email, passcode, program), passcode);
        Mockito.verify(mailSender, Mockito.times(1)).send(mimeMessage);
    }

    @Test
    public void sendMaildSuccessfully() throws MessagingException {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        MimeMessage message = javaMailSender.createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(message);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setSubject("subject");
        mimeMessageHelper.setText("text", true);
        mimeMessageHelper.setTo("test");
        MimeMessage result = mailService.sendMail("test", "text", "subject");
        assertEquals("subject",result.getSubject());
        assertEquals(message,result);
    }

    @Test
    public void sendMailShouldThrowException() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        MimeMessage message = javaMailSender.createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(message);
        Assert.assertThrows(IllegalArgumentException.class,
                () -> mailService.sendMail(null, "text", "subject"));
        Assert.assertThrows(IllegalArgumentException.class,
                () -> mailService.sendMail("to", null, "subject"));
        Assert.assertThrows(IllegalArgumentException.class,
                () -> mailService.sendMail("to", "text", null));
    }
}
