package com.dapp.docuchain.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "expiry_document_draft_info")
@Getter
@Setter
public class ExpiryDocumentDraftInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentName;
    private String certificateNumber;
    private String issuingAuthority;
    private String placeOfIssue;

    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Temporal(TemporalType.DATE)
    private Date lastAnnual;

    @Temporal(TemporalType.DATE)
    private Date nextAnnual;

    private String remarks;

    // Relationships
    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "document_data_id")
    // private DocumentDataInfo documentDataInfo;
    @ManyToOne
    @JoinColumn(name = "document_data_id")
    private DocumentDataInfo documentDataInfo;

    @ManyToOne
    @JoinColumn(name = "ship_profile_id")
    private ShipProfileInfo shipProfileInfo;

    @ManyToOne
    @JoinColumn(name = "document_holder_id")
    private DocumentHolderInfo documentHolderInfo;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private UserProfileInfo uploadedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate = new Date();
}
