package com.dapp.docuchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dapp.docuchain.model.OtpInfo;

@Repository
public interface OtpRepository
        extends JpaRepository<OtpInfo, Long> {

    OtpInfo findByEmail(String email);
}
