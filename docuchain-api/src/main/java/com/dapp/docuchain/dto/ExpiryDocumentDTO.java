package com.dapp.docuchain.dto;

import com.dapp.docuchain.model.DocumentDataInfo;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.OrganizationInfo;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryDocumentDTO {

    private Long id;
    private Long groupId;
    private String groupName;
    private String status;
    private String documentName;
    private Long documentDataId;
    private Long documentHolderId;
    private String documentHolderName;
    private String documentHolderType;
    private String documentStatus;
    private Long shipProfileId;
    private String shipName;

    private String certificateNumber;
    private String certificateNo;
    private String imoNumber;
    // private String rawExtractedText;

    private String placeOfIssue;
    private Date issueDate;
    private String issueDateString;
    private Date expiryDate;
    private String expiryDateString;
    private Date lastAnnual;
    private String lastAnnualString;
    private Date nextAnnual;
    private String nextAnnualString;
    private Date modifiedDate;
    private Long uploadedUserId;
    private String uploadedUserName;
    private Long approverId;
    private String approvedBy;
    private String fileExtension;
    private DocumentDataInfo documentDataInfo;
    private DocumentHolderInfo documentHolderInfo;
    private String remarks;
    private Integer currentVersion;
    private Integer archivedStatus;
    private String encodedFile;
    private String documentDownloadUrl;
    private String documentPreviewUrl;
    private byte[] fileArray;
    private String emailIds;
    private String customFolderName;
    private Long userId;
    private String statusColor;
    private Integer historyVersion;
    private String category;
    private Long loginId;
    private Long organizationId;
    private Long flag;
    private Long adminId;
    private OrganizationInfo organizationInfo;
    private String issuingAuthority;
    private String currentVersionString;
    private Date uploadDate;
// Add these to your ExpiryDocumentDTO class
private Boolean isDraft;
private Long draftId;
    List<ExpiryDocumentDTO> expiryDocumentDTO;
}
