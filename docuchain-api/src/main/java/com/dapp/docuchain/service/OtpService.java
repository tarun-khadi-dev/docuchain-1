package com.dapp.docuchain.service;

import com.dapp.docuchain.model.OtpInfo;
import com.dapp.docuchain.repository.OtpRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from.email}")
    private String fromEmail;

    // ================= GENERATE OTP =================
    private String generateOtp() {
        return String.valueOf(
                new Random().nextInt(900000) + 100000);
    }

    // ================= SEND OTP =================
    public String sendOtp(String email) {

        String otp = generateOtp();

        OtpInfo info = otpRepository.findByEmail(email);

        if (info == null)
            info = new OtpInfo();

        info.setEmail(email);
        info.setOtp(otp);
        info.setVerified(false);

        // expiry = 5 minutes
        info.setExpiryTime(
                new Date(System.currentTimeMillis() + 300000));

        otpRepository.save(info);

        sendOtpMail(email, otp);

        return "OTP Sent Successfully";
    }

    // ================= VERIFY OTP =================
    public String verifyOtp(String email, String otp) {

        OtpInfo info = otpRepository.findByEmail(email);

        if (info == null)
            return "OTP Not Found";

        if (info.getExpiryTime().before(new Date()))
            return "OTP Expired";

        if (!info.getOtp().equals(otp))
            return "Invalid OTP";

        info.setVerified(true);
        otpRepository.save(info);

        return "OTP Verified Successfully";
    }

    // ================= SEND EMAIL =================
    private void sendOtpMail(String email, String otp) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("DocuChain OTP Verification");

            String body =
                    "<h3>Your OTP is:</h3>" +
                    "<h2>" + otp + "</h2>" +
                    "<p>Valid for 5 minutes</p>";

            helper.setText(body, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
