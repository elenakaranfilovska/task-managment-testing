package com.ios.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ios.backend.entities.Passcode;
import com.ios.backend.entities.Program;
import com.ios.backend.repositories.PasscodeRepository;
import com.ios.backend.utils.AuthUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {
  @Autowired
  JavaMailSender mailSender;
  
  @Autowired
  PasscodeRepository passcodeRepository;

  
  public Passcode generatePasscodeForProgram(long programId) {
    String code = AuthUtils.OTP(8,programId);
    Passcode passcode = new Passcode();
    passcode.setCode(code);
    passcode.setPid(programId);
    passcodeRepository.save(passcode);
    return passcode;
  }
  
  public String sendPasscode(String email, String passcode, Program prgm) {
    String text = "<html><body>You are invited to join program:\n"+prgm.getId()+" | "+prgm.getName();
    text = text + "\n\nUse passcode given below:\n"+ passcode + "\n\n";
    text = text + prgm.getDescription()+ "</body></html>";
    String subject = "Invitation to Join Program: "+ prgm.getId()+" | "+prgm.getName();
    try {
      sendMail(email, text, subject);
    } catch (MessagingException e) {
      return ""; //no-use
    }
    return passcode;
  }


  public MimeMessage sendMail(String to, String text, String subject) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message,true);
    try {
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text,true);
    } catch (MessagingException e) {
      e.printStackTrace();
      return null;
    }
    mailSender.send(message);
    return message;
  }
}
