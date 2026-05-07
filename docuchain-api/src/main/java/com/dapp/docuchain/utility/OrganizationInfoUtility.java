package com.dapp.docuchain.utility;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.OrganizationDTO;
import com.dapp.docuchain.dto.SubscriptionDTO;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.SubscriptionInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.OrganizationInfoRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.SubscriptionInfoRepository;
import com.dapp.docuchain.repository.UserProfileRepository;


@Service
public class OrganizationInfoUtility {

	private static final Logger LOG = LoggerFactory.getLogger(OrganizationInfoUtility.class);

	private final Long USER_ACTIVE_STATUS = Long.valueOf(1);

	static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	@Autowired
	private Environment env;

	@Autowired
	private SubscriptionInfoRepository subscriptionInfoRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private RoleInfoRepository roleInfoRepository;
	/*
	 * In that function used for validate input params for creating organization
	 * In that function we need to pass organizationName,organization address,contat,emailid
	 */
	public String validateOrganizationParameter(OrganizationDTO organizationDTO) {
		// TODO Auto-generated method stub

		if (!(organizationDTO.getOrganizationName() != null
				&& StringUtils.isNotBlank(organizationDTO.getOrganizationName()))) {
			return "Organization name missing";
		}
		// if (!(organizationDTO.getAddress() != null
		// 		&& StringUtils.isNotBlank(organizationDTO.getAddress()))) {
		// 	return "Organization address missing";
		// }
		if(!(organizationDTO.getAddressLine1()!=null &&
     	StringUtils.isNotBlank(organizationDTO.getAddressLine1()))){
    	return "Organization address line 1 missing";
		}

		if (!(organizationDTO.getContact() != null
				&& StringUtils.isNotBlank(organizationDTO.getContact()))) {
			return "Organization contact missing";
		}

		if (!(organizationDTO.getEmailId() != null
				&& StringUtils.isNotBlank(organizationDTO.getEmailId()))) {
			return "Organization mail missing";
		}

		if (!(organizationDTO.getSubscriptionId() != null
				&& StringUtils.isNotBlank(organizationDTO.getSubscriptionId().toString()))) {
			return "Subscription plan is missing";
		}

		if (!(organizationDTO.getUserId() != null
				&& StringUtils.isNotBlank(organizationDTO.getUserId().toString()))) {
			return "login details is missing";
		}


		return env.getProperty("success");

	}
	/*
	 * In that function used to check that email format correct or not.
	 */
	public boolean validateEmail(String emailId) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		LOG.info(emailId + " : " + matcher.matches());
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public List<OrganizationDTO> convertSubscriptionInfotoSubscriptionInfoDTO(
			List<OrganizationInfo> organizationInfos) {
		// TODO Auto-generated method stub
		List<OrganizationDTO> listOrganizationDTO = new ArrayList<>();
		for (OrganizationInfo organizationInfo : organizationInfos) {
			OrganizationDTO organizationDTO = new OrganizationDTO();
			organizationDTO.setOrganizationId(organizationInfo.getId());
			organizationDTO.setOrganizationName(organizationInfo.getOrganizationName());
			// organizationDTO.setAddress(organizationInfo.getAddress());
				organizationDTO.setAddressLine1(organizationInfo.getAddressLine1());
			organizationDTO.setAddressLine2(organizationInfo.getAddressLine2());

			organizationDTO.setEmailId(organizationInfo.getEmailId());
			organizationDTO.setContact(organizationInfo.getContact());

			listOrganizationDTO.add(organizationDTO);
		}
		return listOrganizationDTO;
		//return null;
	}
	public String isSubscriptionValid(OrganizationDTO organizationDTO) {
		if(organizationDTO.getSubscriptionId() ==null)
		{
			return env.getProperty("failure");
		}
		SubscriptionInfo subscriptionInfo = subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId());
		if(subscriptionInfo!=null)
		{
			return env.getProperty("success");
		}
		return env.getProperty("failure");
	}
	public OrganizationDTO convertOrganizatioTopCount(List<OrganizationInfo> organizationInfos) {
		 long activeCount = 0;
		 long renewalCount = 0;
		 long expiryCount = 0;
		 SubscriptionInfo subscriptionInfo;
		 OrganizationDTO organizationDTO = new OrganizationDTO();
		 for (OrganizationInfo organizationInfo : organizationInfos){
//			 if (organizationInfo.getIsActive() != 0){
//				 activeCount++;
//			 }
//			 if (organizationInfo.getSubscriptionInfo() != null){
//				 subscriptionInfo = subscriptionInfoRepository.findOne(organizationInfo.getSubscriptionInfo().getId());
//				 String findDiff = findDiffrenceDate(subscriptionInfo.getSubscriptionExpireDate());
//				 if(findDiff != null){
//					 if (findDiff.equalsIgnoreCase("renewal"))
//						 renewalCount++;
//					 if (findDiff.equalsIgnoreCase("expiry"))
//						 expiryCount++;
//				 }
//			 }else{
//				 expiryCount++;
//			 }

			 Date currentDate1 = new Date();
				//LocalDate renewalDate = currentDate1.plusDays(30);
			    Calendar c = Calendar.getInstance();
		        c.add(Calendar.DATE, 30);
		        Date renewalDate= c.getTime();
				List<OrganizationInfo> activeExpiryList = organizationInfoRepository.findByIsActive((long)1);
				activeCount = activeExpiryList.size();

				List<SubscriptionInfo> expiredExpiryList = subscriptionInfoRepository.findByIsStatusAliveAndSubscriptionExpireDateBefore(1,currentDate1);
				expiryCount = expiredExpiryList.size();

				List<SubscriptionInfo> renewalExpiryList =  subscriptionInfoRepository.findByIsStatusAliveAndSubscriptionExpireDateBetween( 1, currentDate1, renewalDate);
				renewalCount = renewalExpiryList.size();

		 }
		 organizationDTO.setActiveCount(activeCount);
		 organizationDTO.setExpiryCount(expiryCount);
		 organizationDTO.setRenewalCount(renewalCount);
		 organizationDTO.setOrganizationCount(organizationInfos.size());
		return organizationDTO;
	}

	  public String findDiffrenceDate(Date expiryDate) {
	        String diffrent ;
	        Date currentDate = new Date();
	        long diff = expiryDate.getTime() - currentDate.getTime();
	        long diffDays = diff / (24 * 60 * 60 * 1000);
	        if (diffDays >= 30) {
	        	diffrent = "renewal";
	            return diffrent;
	        } else if (diffDays <= 0) {
	        	diffrent = "expiry";
	            return diffrent;
	        }
	        return null;
	    }
	public List<OrganizationDTO> convertdetailOfOrganizationStatistics(List<OrganizationInfo> organizationInfos) {
		  String companyName;
		  String registrationNumber;
		  long adminCount = 0;
		  Date currentDate = new Date();
		  List<OrganizationDTO> organizationDTOs = new ArrayList<>();
		  for (OrganizationInfo organizationInfo : organizationInfos){
			  SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
			  OrganizationDTO organizationDTO = new OrganizationDTO();
			  companyName = organizationInfo.getOrganizationName();
			  registrationNumber = organizationInfo.getRegistrationNumber();
			  List<UserProfileInfo> adminProfileInfos = userProfileRepository.findByOrganizationInfoAndRoleIdAndStatus(organizationInfo, roleInfoRepository.findByRoleName(Role.Admin), USER_ACTIVE_STATUS);
			  if(organizationInfo.getSubscriptionInfo() != null){
				  if (organizationInfo.getSubscriptionInfo() != null){
					  subscriptionDTO.setNumberOfUser(organizationInfo.getSubscriptionInfo().getUserCount());
					  subscriptionDTO.setNumberOfVessels(organizationInfo.getSubscriptionInfo().getShipmentCount());
					  long remaingDays = organizationInfo.getSubscriptionInfo().getSubscriptionExpireDate().getTime() - currentDate.getTime();
					  if(remaingDays<0) {
						  subscriptionDTO.setRemaingDayOfSubscription((long) 0);
					  }else {
					  subscriptionDTO.setRemaingDayOfSubscription(remaingDays / (24 * 60 * 60 * 1000));
					  }
				  }
			  }else {
				  subscriptionDTO.setNumberOfUser((long) 0);
				  subscriptionDTO.setNumberOfVessels((long) 0);
				  subscriptionDTO.setRemaingDayOfSubscription((long) 0);
			  }
			  registrationNumber = organizationInfo.getRegistrationNumber();
			  organizationDTO.setCompanyName(companyName);
			  organizationDTO.setRegistrationNumber(registrationNumber);
			  organizationDTO.setAdminCount(Long.valueOf(adminProfileInfos.size()));
			  organizationDTO.setSubscriptionDTO(subscriptionDTO);
			  organizationDTOs.add(organizationDTO);

		  }
		return organizationDTOs;
	}
	public OrganizationDTO createOrganizationLogo(Long adminId, String logoPicPath) {

		UserProfileInfo userProfileInfo = userProfileRepository.findById(adminId);
		OrganizationDTO organizationDTO = new OrganizationDTO();
		if (userProfileInfo != null){
			List<OrganizationInfo> organizationInfos = organizationInfoRepository.findById(userProfileInfo.getOrganizationInfo().getId());
			for(OrganizationInfo organizationInfo : organizationInfos){
				organizationInfo.setCompanyLogo(logoPicPath);
				organizationInfo = organizationInfoRepository.saveAndFlush(organizationInfo);
			}
				if(logoPicPath != null) {
					List<OrganizationInfo> organizationInfo = organizationInfoRepository.findById(userProfileInfo.getOrganizationInfo().getId());
					String companyLogo = null;
					for (OrganizationInfo organizationInfo2 : organizationInfo){
						 companyLogo = env.getProperty("picture.path") + logoPicPath;
					}
					organizationDTO.setLogoPicture(companyLogo.replace(File.separator, "/"));
				}
			return organizationDTO;
		}
		return null;
	}


}
