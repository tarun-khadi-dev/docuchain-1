package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "document_holder_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentHolderInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "document_holder_name")
    private String documentHolderName;

    @Column(name = "document_holder_description")
    private String documentHolderDescription;

    @Column(name = "document_holder_type")
    private String documentHolderType;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column (name = "type")
    private String type;

    @Column (name = "updatedDate")
    private Date updatedDate;

    @Column (name = "vesselId")
    private Long vesselId;

    @Column (name = "parentId")
    private Long parantId;

    @OneToMany(mappedBy = "documentHolderInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ExpiryDocumentInfo> expiryDocumentInfos;

    public DocumentHolderInfo(){

    }

	public DocumentHolderInfo(Long id, String documentHolderName,
			String documentHolderDescription, String documentHolderType, String type,Date updatedDate) {
		super();
		this.id = id;
		this.documentHolderName = documentHolderName;
		this.documentHolderDescription = documentHolderDescription;
		this.documentHolderType = documentHolderType;
		this.type = type;
		this.updatedDate = updatedDate;
	}



}
