package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.OtpRequestDTO;
import com.dapp.docuchain.dto.OtpVerifyDTO;
import com.dapp.docuchain.service.OtpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docuchain/api")
@CrossOrigin
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/sendOtp")
    public ResponseEntity<?> sendOtp(
            @RequestBody OtpRequestDTO dto) {

        return ResponseEntity.ok(
                otpService.sendOtp(dto.getEmail()));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(
            @RequestBody OtpVerifyDTO dto) {

        return ResponseEntity.ok(
                otpService.verifyOtp(
                        dto.getEmail(),
                        dto.getOtp()));
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<?> resendOtp(
            @RequestBody OtpRequestDTO dto) {

        return ResponseEntity.ok(
                otpService.sendOtp(dto.getEmail()));
    }
}
