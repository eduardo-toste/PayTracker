package com.eduardo.paytracker.service;

import com.eduardo.paytracker.model.Transaction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(Transaction transaction) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(transaction.getUser().getEmail());
            helper.setSubject("🔔 Sua conta vence em breve!");

            String htmlTemplate = loadTemplate("templates/email-template.html");

            String html = htmlTemplate
                    .replace("{{nome}}", transaction.getUser().getName())
                    .replace("{{descricao}}", transaction.getDescription())
                    .replace("{{vencimento}}", transaction.getDueDate().toString())
                    .replace("{{valor}}", String.format("R$ %.2f", transaction.getAmount()))
                    .replace("{{categoria}}", transaction.getType().toString());

            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private String loadTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
    }
}
