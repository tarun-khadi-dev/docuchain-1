package com.dapp.docuchain.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Organization_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "organization_name", nullable = false)
	private String organizationName;

	@Column(name = "registration_number", nullable = false)
	private String registrationNumber;

	// @Column(name = "address")
	// private String address;
	@Column(name="address_line1")
	private String addressLine1;

	@Column(name="address_line2")
	private String addressLine2;

	@Column(name = "contact")
	private String contact;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "alternate_phone_number")
	private String alternatePhoneNumber;

	@Column(name = "bank_account_number")
	private String bankAccountNumber;

	@Column(name = "contact_person_name")
	private String contactPersonName;

	@Column(name = "contact_person_email")
	private String contactPersonEmail;

	@Column(name = "contact_person_phoneNo")
	private String contactPersonPhoneNo;

	@Column(name = "contact_person_alternate_phone_no")
	private String contactPersonAlternatePhoneNo;

	@Column(name = "is_active")
	private Long isActive;

	@Column(name = "companyLogo")
	private String companyLogo;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date")
	private Date modifyDate;

	@OneToMany(mappedBy = "organizationInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<UserProfileInfo> userProfileInfo;

	@OneToMany(mappedBy = "shipOrganizationInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ShipProfileInfo> shipProfileInfo;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "subscription_id", referencedColumnName = "id")
	private SubscriptionInfo subscriptionInfo;

	@OneToOne(mappedBy = "organizationInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private SaveInBlockhainInfo saveInBlockhainInfo;

	@OneToOne(mappedBy = "organizationInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private DualApprovalInfo dualApprovalInfo;

	@OneToMany(mappedBy = "organizationInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RoleAliasInfo> roleAliasInfos;

	@OneToMany(mappedBy = "organizationInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ShipTypesInfo> shipTypesInfos;

}
