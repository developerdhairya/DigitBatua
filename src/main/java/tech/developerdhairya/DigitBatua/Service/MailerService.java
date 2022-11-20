package tech.developerdhairya.DigitBatua.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.LocalDate;

@Component @EnableAsync
public class MailerService {

    @Autowired
    Environment env;

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendVerificationToken(String recipient,String token){
//        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
//        javaMailSender.setHost(host);
//        javaMailSender.setPort(Integer.parseInt(port));
//        javaMailSender.setUsername(username);
//        javaMailSender.setPassword(password);
//        javaMailSender.setProtocol("smtp");
        try{
            SimpleMailMessage msg=new SimpleMailMessage();
            msg.setFrom("taneja.dhairya12@gmail.com");
            msg.setTo(recipient);
            msg.setSubject("DigitBatua");
            msg.setText("Thanks for testing my project.Click on the link to verify your account"+"url"+"/verifyUser?userId="+recipient+"&token="+token);
            msg.setSentDate(Date.valueOf(LocalDate.now()));
            msg.setReplyTo("taneja.dhairya12@gmail.com");
            System.out.println(msg);
            javaMailSender.send(msg);
            System.out.println("Mail sent Successfully");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Async
    public void sendForgotPassword(String recipient,String token){
        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setTo(recipient);
        msg.setSubject("DigitBatua");
        msg.setText("Thanks for testing my project.Click on the link to reset your account"+"url"+"/forgotPassword?userId="+recipient+"&token="+token);
        javaMailSender.send(msg);
    }

}
