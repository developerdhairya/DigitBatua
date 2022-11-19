package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailerService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    public String sender;

    @Value("${server.servlet.context-path}")
    public String url;

    public void sendVerificationToken(String recipient,String token){
        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setFrom(sender);
        msg.setTo(recipient);
        msg.setSubject("DigitBatua");
        msg.setText("Thanks for testing my project.Click on the link to verify your account"+url+"/verifyUser?userId="+recipient+"&token="+token);
        javaMailSender.send(msg);
    }

    public void sendForgotPassword(String recipient,String token){
        SimpleMailMessage msg=new SimpleMailMessage();
        msg.setFrom(sender);
        msg.setTo(recipient);
        msg.setSubject("DigitBatua");
        msg.setText("Thanks for testing my project.Click on the link to reset your account"+url+"/forgotPassword?userId="+recipient+"&token="+token);
        javaMailSender.send(msg);
    }

}
