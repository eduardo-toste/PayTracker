package com.eduardo.paytracker.config;

import com.eduardo.paytracker.utils.GetEnvUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(GetEnvUtil.get("SMTP_HOST"));
        sender.setPort(Integer.parseInt(GetEnvUtil.get("SMTP_PORT")));
        sender.setUsername(GetEnvUtil.get("EMAIL_USERNAME"));
        sender.setPassword(GetEnvUtil.get("EMAIL_PASSWORD"));

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return sender;
    }
}
