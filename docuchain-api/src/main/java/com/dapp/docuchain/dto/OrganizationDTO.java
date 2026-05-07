package com.dapp.docuchain.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OrganizationDTO {

	private Long organizationId;

	private String organizationName;

	private String registrationNumber;

	// private String address;
	private String addressLine1;
	private String addressLine2;

	private String emailId;

	private String phoneNumber;

	private String alternatePhoneNumber;

	private String bankAccountNumber;

	private String contactPersonName;

	private String contactPersonEmail;

	private String contactPersonPhoneNo;

	private String contactPersonAlternatePhoneNo;

	private Long isStatusActive;

	private Long isDualApprovalActive;

	private Long isSaveInBlockchain;

	private Long userId;

	private SubscriptionDTO subscriptionInfo;

	private UserDTO userInfo;

	private String contact;

	private Long subscriptionId;

	private Set<UserDTO> userInfos;

	private Long userCount;

	private Long shipmentCount;

	private Integer organizationCount;

	private Long activeCount;

	private Long renewalCount;

	private Long expiryCount;

	private String companyName;

	private Long adminCount;

	private SubscriptionDTO subscriptionDTO;

	private String logoPicture;






}
