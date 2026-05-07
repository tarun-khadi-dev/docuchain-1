package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentDraftInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpiryDocumentDraftRepository extends JpaRepository<ExpiryDocumentDraftInfo, Long> {

    // Find a draft for a specific holder and ship
    List<ExpiryDocumentDraftInfo> findByDocumentHolderInfoAndShipProfileInfo(
            DocumentHolderInfo documentHolderInfo,
            ShipProfileInfo shipProfileInfo
    );
}
