package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.EmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

// Using javax.mail for Spring Boot 2 compatibility
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.Year; // Imported to handle dynamic year
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/docuchain/api/email")
@CrossOrigin
public class EmailController {

    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from.email}")
    private String fromEmail;

    // Pulling the logo URL from application.properties
    @Value("${docuchain.email.logo.url}")
    private String logoUrl;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendHtmlMail(@RequestBody EmailRequest req) {

        Map<String, String> response = new HashMap<>();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(req.getTo().toArray(new String[0]));

            // Use the subject from the request
            String subject = (req.getSubject() != null && !req.getSubject().isEmpty())
                             ? req.getSubject()
                             : "Document Sharing Notification";
            helper.setSubject(subject);

            // 1. Bypass Regex Processing: The frontend CKEditor already sends valid HTML.
            String bodyContent = req.getBody() != null ? req.getBody() : "";

            // 2. Wrap it in the Maroon/Blue Template
            String htmlContent = getHtmlTemplate(subject, bodyContent);

            helper.setText(htmlContent, true);

            mailSender.send(message);

            response.put("status", "success");
            response.put("message", "HTML Email Sent Successfully");

            return ResponseEntity.ok(response);

        } catch (MessagingException e) {
            LOG.error("Mail Creation Error", e);
            response.put("status", "error");
            response.put("message", "Mail Creation Failed");
            return ResponseEntity.status(500).body(response);

        } catch (Exception e) {
            LOG.error("Mail Send Error", e);
            response.put("status", "error");
            response.put("message", "Mail Sending Failed");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * The Clean Template (Centered Logo / Maroon Header / Blue Footer)
     */
    private String getHtmlTemplate(String subject, String content) {

        // Get the current year dynamically
        int currentYear = Year.now().getValue();

        return "<!DOCTYPE html>" +
                "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;'>" +
                "  <div style='max-width: 600px; background-color: #ffffff; margin: 0 auto; border: 1px solid #dddddd;'>" +

                // --- HEADER: CENTERED LOGO ---
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "      <tr>" +
                "        <td align='center' valign='middle' style='padding: 20px;'>" +
                "          <img src='" + logoUrl + "' width='200' style='display:block;' alt='DocuChain Logo' />" +
                "        </td>" +
                "      </tr>" +
                "    </table>" +

                // --- MAROON TITLE BAR (Dynamic Subject Added!) ---
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "      <tr>" +
                "        <td align='left' valign='top' bgcolor='#8e3d57' style='background-color:#8e3d57; padding:20px; font-family:Arial, Helvetica, sans-serif;'>" +
                "          <div style='color:#ffffff; font-size:18px; font-weight: bold; font-style: italic;'>" + subject + "</div>" +
                "        </td>" +
                "      </tr>" +
                "    </table>" +

                 // --- CONTENT AREA ---
                "    <div style='padding: 25px; color: #333; line-height: 1.6; font-size: 14px; background-color: #f7f7f7;'>" +
                "      <p style='margin-top: 0; font-weight: bold;'>Here are the documents shared with you:</p>" +

                // Dynamic Content from JSON (The list of files + The Note)
                "      <div style='margin-bottom: 20px; padding: 15px; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 4px;'>" +
                         content +
                "      </div>" +

                // --- SIGNATURE (.info link) ---
                "      <div style='margin-top: 25px;'>" +
                "        <h1 style='font-family:Arial, Helvetica, sans-serif; font-weight:600; color:#800000; font-size: 15px; font-style: italic; margin: 0; padding: 0;'>Thanks,</h1>" +
                "        <h2 style='font-family:Arial, Helvetica, sans-serif; color:#001a4d; font-weight:400; font-size: 15px; font-style: italic; margin: 5px 0 3px 0; padding: 0;'>Docuchain Team,</h2>" +
                "        <p style='margin:0;'>" +
                "          <a href='https://docuchain.info/#/' style='color: #0000EE; font-size: 15px; font-style: italic; text-decoration: underline;'>www.docuchain.info</a>" +
                "        </p>" +
                "      </div>" +
                "    </div>" +

                // --- BLUE FOOTER (Dynamic Year) ---
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "      <tr style='background:#5677b2;'>" +
                "        <td align='center' valign='middle' style='padding: 10px;'>" +
                "          <p style='color:#ffffff; font-family:calibri, sans-serif; font-size: 12px; margin: 0;'>© Copyright " + currentYear + ". All Rights Reserved.</p>" +
                "        </td>" +
                "      </tr>" +
                "    </table>" +

                "  </div>" +
                "</body>" +
                "</html>";
    }
}
