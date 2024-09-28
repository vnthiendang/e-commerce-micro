package com.dvt.ecommerce.email;

import com.dvt.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dvt.ecommerce.email.EmailTemplates.ORDER_CONFIRMATION;
import static com.dvt.ecommerce.email.EmailTemplates.PAYMENT_CONFIRMATION;

@Service
@Slf4j
public class EmailService {

//    @Bean
//    public static EmailService emailService(){
//        return new EmailService();
//    }

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    @Bean
    public static SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/template/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);  // Disable cache for dev
        return templateResolver;
    }

    @Bean
    public static SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }


    @Async
    public void sendEmail(String destinationEmail, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            messageHelper.setFrom("contact@gmail.com");
            messageHelper.setTo(destinationEmail);
            messageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            mailSender.send(mimeMessage);
            log.info("INFO - Email successfully sent to {} with template {}", destinationEmail, templateName);
        } catch (MessagingException e) {
            log.error("ERROR - Cannot send Email to {}. Exception: {}", destinationEmail, e.getMessage(), e);
        }
    }

    @Async
    public void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("amount", amount);
        variables.put("orderReference", orderReference);
        sendEmail(destinationEmail, PAYMENT_CONFIRMATION.getSubject(), PAYMENT_CONFIRMATION.getTemplate(), variables);
    }

    @Async
    public void sendOrderConfirmationEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference, List<Product> products) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("totalAmount", amount);
        variables.put("orderReference", orderReference);
        variables.put("products", products);
        sendEmail(destinationEmail, ORDER_CONFIRMATION.getSubject(), ORDER_CONFIRMATION.getTemplate(), variables);
    }
}
