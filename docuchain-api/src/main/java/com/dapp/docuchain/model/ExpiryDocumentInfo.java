package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expiry_document_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExpiryDocumentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "certificate_number")
    private String certificateNumber;

    @Column(name = "place_of_issue")
    private String placeOfIssue;

    @Temporal(TemporalType.DATE)
    @Column(name = "issue_date")
    private Date issueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date")
    private Date expiryDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_annual")
    private Date lastAnnual;

    @Temporal(TemporalType.DATE)
    @Column(name = "next_annual")
    private Date nextAnnual;

    @Column(name = "document_status")
    private String documentStatus;

    @Column(name = "archive_status")
    private Integer archiveStatus;

    @Column(name = "current_version")
    private Integer currentVersion;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "issuingAuthority")
    private String issuingAuthority;

    @CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_user_id", referencedColumnName = "id", nullable = true)
    private UserProfileInfo uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_user_id", referencedColumnName = "id", nullable = true)
    private UserProfileInfo approvedBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_data_id", referencedColumnName = "id", nullable = false)
    private DocumentDataInfo documentDataInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_holder_id", referencedColumnName = "id", nullable = false)
    private DocumentHolderInfo documentHolderInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ship_profile_id", referencedColumnName = "id", nullable = false)
    private ShipProfileInfo shipProfileInfo;

}
