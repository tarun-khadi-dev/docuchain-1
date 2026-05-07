package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ship_profile_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipProfileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ship_name")
    private String shipName;

    @Column(name = "bhp")
    private String bhp;

    @Column(name = "ship_owner")
    private String shipOwner;

    @Column(name = "imo", unique = true)
    private Long iMO;

    @Column(name = "engine_type")
    private String engineType;

    @Column(name = "callsign")
    private String callsign;

    @Column(name = "keel_laid")
    private Date keellaid;

    @Column(name = "delivered")
    private String delivered;

    @Column(name = "builder")
    private String builder;

    @Column(name = "internationalGRT")
    private String internationalGRT;

    @Column(name = "internationalNRT")
    private String internationalNRT;

    @Column(name = "countryname")
    private String countryName;

    @Column(name = "statename")
    private String stateName;

    @Column(name = "shiptype")
    private String shipType;

    @Column(name = "status")
    private Integer status;
    @Column(name = "dwt")
    private Double dwt;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "length")
    private Double length;

        @Column(name = "breadth")
        private Double breadth;

    @Column(name = "ship_profile_pic_path")
	private String shipProfilePicPath;

    @CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

    @OneToMany(mappedBy = "shipProfileInfo", fetch = FetchType.LAZY)
    private Set<ExpiryDocumentInfo> expiryDocuments;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ship_profile_xref_custom_document",
            joinColumns = {@JoinColumn(name = "ship_profile_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "document_holder_id", referencedColumnName = "id", unique = false)})
    private Set<DocumentHolderInfo> customDocumentHolders = new HashSet<DocumentHolderInfo>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_master_id")
    private UserProfileInfo shipMaster;

  /*  @OneToOne
    private UserProfileInfo techMaster;

    @OneToOne
    private UserProfileInfo commercialMaster;*/


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ship_profile_xref_tech_masters",
            joinColumns = {@JoinColumn(name = "ship_profile_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_profile_id", referencedColumnName = "id")})
    private Set<UserProfileInfo> techMasters;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ship_profile_xref_commercial_masters",
            joinColumns = {@JoinColumn(name = "ship_profile_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_profile_id", referencedColumnName = "id")})
    private Set<UserProfileInfo> commercialMasters;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ship_profile_xref_data_operators",
            joinColumns = {@JoinColumn(name = "ship_profile_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_profile_id", referencedColumnName = "id")})
    private Set<UserProfileInfo> dataOperators;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = true)
	private OrganizationInfo shipOrganizationInfo;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "userRequestShipProfile", cascade=CascadeType.ALL)
    private Set<RequestUserInfo> requestUserInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="shipProfileInfo" ,cascade = CascadeType.ALL, orphanRemoval=false)
    private List<TaskDetailsInfo> taskDetailsInfos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="shipProfileInfo" ,cascade = CascadeType.ALL, orphanRemoval=false)
    private List<ExpiryDocumentInfo> ExpiryDocumentInfo;
}
