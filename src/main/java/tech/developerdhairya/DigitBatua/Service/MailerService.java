package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class MailerService {


    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendVerificationToken(String recipient, String token) {
//        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
//        javaMailSender.setHost(host);
//        javaMailSender.setPort(Integer.parseInt(port));
//        javaMailSender.setUsername(username);
//        javaMailSender.setPassword(password);
//        javaMailSender.setProtocol("smtp");
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(sender);
            msg.setTo(recipient);
            msg.setSubject("DigitBatua");
            msg.setText("Thanks for testing my project.Your unique verification code is "+token+". It is valid for 10 minutes only");
            msg.setSentDate(Date.valueOf(LocalDate.now()));
            msg.setReplyTo(sender);
            javaMailSender.send(msg);
            System.out.println("Mail sent Successfully");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Async
    public void sendForgotPasswordToken(String recipient, String token) {
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(recipient);
            msg.setSubject("DigitBatua");
            msg.setText("Thanks for testing my project.Your unique Forgot code is"+token+". It is valid for 10 minutes only");
            javaMailSender.send(msg);
            System.out.println("Mail sent Successfully");
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
