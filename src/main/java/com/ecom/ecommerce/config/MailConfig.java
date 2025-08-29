package com.ecom.ecommerce.config;

import com.ecom.ecommerce.utility.AESGCMUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration for e-mails encryption/decryption
 */
@Configuration
public class MailConfig {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String encryptedPassword;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String startTls;

    @Autowired
    AESGCMUtil aesgcmUtil;

    @Bean
    public JavaMailSender javaMailSender() throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(aesgcmUtil.decrypt(username));
        mailSender.setPassword(aesgcmUtil.decrypt(encryptedPassword));
        mailSender.getJavaMailProperties().put("mail.smtp.auth", smtpAuth);
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", startTls);
        return mailSender;
    }
}
