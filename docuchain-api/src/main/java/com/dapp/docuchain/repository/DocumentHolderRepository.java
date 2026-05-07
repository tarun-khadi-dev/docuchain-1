// package com.dapp.docuchain.repository;

// import com.dapp.docuchain.model.DocumentHolderInfo;
// import com.dapp.docuchain.model.ShipProfileInfo;

// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;
// import java.util.Set;

// public interface DocumentHolderRepository extends JpaRepository<DocumentHolderInfo, Long> {

//     DocumentHolderInfo findByDocumentHolderName(String documentName);

//     List<DocumentHolderInfo> findByDocumentHolderTypeOrderByDocumentHolderName(String type);

//     List<DocumentHolderInfo> findDistinctDocumentHolderInfoByExpiryDocumentInfos_ShipProfileInfo(ShipProfileInfo shipProfileInfo);

//     List<DocumentHolderInfo> findDistinctDocumentHolderInfoByOrganizationIdAndVesselIdIsNull(Long organizaionId);

//     List<DocumentHolderInfo> findByOrganizationIdAndTypeAndVesselIdIsNull(Long organizaionId, String type);

//     List<DocumentHolderInfo> findByVesselIdAndType(Long vesselId,String type);

//     List<DocumentHolderInfo> findByTypeOrTypeIsNullAndVesselIdIsNull(String documentType);

//     Set<DocumentHolderInfo> findByVesselId(Long vesselId);

//     // 🔥 ADD THIS
//     List<DocumentHolderInfo> findByParantId(Long parantId);
// }

// DocumentHolderRepository.java

package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DocumentHolderRepository extends JpaRepository<DocumentHolderInfo, Long> {

    // 🔥 FIX: Change return type to List to handle multiple records with the same name
    List<DocumentHolderInfo> findByDocumentHolderName(String documentName);

    List<DocumentHolderInfo> findByDocumentHolderTypeOrderByDocumentHolderName(String type);

    List<DocumentHolderInfo> findDistinctDocumentHolderInfoByExpiryDocumentInfos_ShipProfileInfo(ShipProfileInfo shipProfileInfo);

    List<DocumentHolderInfo> findDistinctDocumentHolderInfoByOrganizationIdAndVesselIdIsNull(Long organizaionId);

    List<DocumentHolderInfo> findByOrganizationIdAndTypeAndVesselIdIsNull(Long organizaionId, String type);

    List<DocumentHolderInfo> findByVesselIdAndType(Long vesselId,String type);

    List<DocumentHolderInfo> findByTypeOrTypeIsNullAndVesselIdIsNull(String documentType);

    Set<DocumentHolderInfo> findByVesselId(Long vesselId);

    List<DocumentHolderInfo> findByParantId(Long parantId);
}
