package com.dapp.docuchain.utility;

import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.SubscriptionInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.OrganizationInfoRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.repository.SubscriptionInfoRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ShipProfileUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShipProfileUtility.class);

	@Autowired
	Environment env;
	@Autowired
	ShipProfileRepository shipProfileRepository;

	@Autowired
	private SubscriptionInfoRepository subscriptionInfoRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private ExpiryDocumentRepository expirDocumentRepository;

	@Autowired
	private DocumentHolderRepository documentHolderRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	public String validateShipProfileUserAndRoleId(ShipProfileDTO shipProfileDTO) {
		if (!(shipProfileDTO.getUserId() != null && StringUtils.isNotBlank(shipProfileDTO.getUserId().toString()))) {
			return env.getProperty("user.id.missing");
		}
		if (!(shipProfileDTO.getRoleId() != null && StringUtils.isNotBlank(shipProfileDTO.getRoleId().toString()))) {
			return env.getProperty("role.id.missing");
		}
		return env.getProperty("success");
	}

	// This method is used to convert ship profile object into DTO
	public ShipProfileDTO convertShipProfileInfoToShipProfileDTO(ShipProfileInfo shipProfileInfo) {
		ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
		shipProfileDTO.setId(shipProfileInfo.getId());
		shipProfileDTO.setBhp(shipProfileInfo.getBhp());
		shipProfileDTO.setBuilder(shipProfileInfo.getBuilder());
		shipProfileDTO.setCallSign(shipProfileInfo.getCallsign());
		shipProfileDTO.setCountryName(shipProfileInfo.getCountryName());
		shipProfileDTO.setDelivered(shipProfileInfo.getDelivered());
		shipProfileDTO.setEngineType(shipProfileInfo.getEngineType());
		shipProfileDTO.setInternationalGRT(shipProfileInfo.getInternationalGRT());
		shipProfileDTO.setInternationalNRT(shipProfileInfo.getInternationalNRT());
		shipProfileDTO.setKeelLaid(shipProfileInfo.getKeellaid());
		shipProfileDTO.setOfficialNo(shipProfileInfo.getIMO());
		shipProfileDTO.setShipName(shipProfileInfo.getShipName());
		shipProfileDTO.setShipOwner(shipProfileInfo.getShipOwner());
		shipProfileDTO.setShipTypes(shipProfileInfo.getShipType());
		shipProfileDTO.setStateName(shipProfileInfo.getStateName());
				//ship measaurment
		shipProfileDTO.setDwt(shipProfileInfo.getDwt());
		shipProfileDTO.setBreadth(shipProfileInfo.getBreadth());
		shipProfileDTO.setWeight(shipProfileInfo.getWeight());
		shipProfileDTO.setLength(shipProfileInfo.getLength());
		shipProfileDTO.setStatus(shipProfileInfo.getStatus());
		/*
		 * if (shipProfileInfo.getCommercialMaster() != null) {
		 * shipProfileDTO.setCommercialMasterId(shipProfileInfo.getCommercialMaster().
		 * getId()); } if (shipProfileInfo.getTechMaster() != null) {
		 * shipProfileDTO.setTechManagerId(shipProfileInfo.getTechMaster().getId()); }
		 */
		if(shipProfileInfo.getShipProfilePicPath()!=null) {
			String shipProfilePicturePath = env.getProperty("picture.path")+shipProfileInfo.getShipProfilePicPath();
			shipProfileDTO.setShipProfilePicPath(shipProfilePicturePath.replace(File.separator, "/"));
		}
		if (shipProfileInfo.getShipMaster() != null) {
			shipProfileDTO.setShipMasterId(shipProfileInfo.getShipMaster().getId());
		}
		return shipProfileDTO;

	}

	public String validateShipProfileCreateParam(ShipProfileDTO shipProfileDTO) {
		/*if (!(shipProfileDTO.getBhp() != null && StringUtils.isNotBlank(shipProfileDTO.getBhp()))) {
			return "Bhp is missing";
		}
		if (!(shipProfileDTO.getBuilder() != null && StringUtils.isNotBlank(shipProfileDTO.getBuilder()))) {
			return "Ship builder/yard is missing";
		}*/
		if (!(shipProfileDTO.getCallSign() != null && StringUtils.isNotBlank(shipProfileDTO.getCallSign()))) {
			return "call sign is missing";
		}
		if (!(shipProfileDTO.getCountryName() != null && StringUtils.isNotBlank(shipProfileDTO.getCountryName()))) {
			return "Country name is missing";
		}
		/*if (!(shipProfileDTO.getDelivered() != null && StringUtils.isNotBlank(shipProfileDTO.getDelivered()))) {
			return "Delivered date is missing";
		}
		if (!(shipProfileDTO.getEngineType() != null && StringUtils.isNotBlank(shipProfileDTO.getEngineType()))) {
			return "Main Engine Type is missing";
		}
		if (!(shipProfileDTO.getInternationalNRT() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getInternationalNRT()))) {
			return "International N.R.T is missing";
		}*/
		if (!(shipProfileDTO.getShipName() != null && StringUtils.isNotBlank(shipProfileDTO.getShipName()))) {
			return "Vessel name is missing";
		}
		if (!(shipProfileDTO.getShipTypes() != null && StringUtils.isNotBlank(shipProfileDTO.getShipTypes()))) {
			return "Type of vessel is missing";
		}
		if (!(shipProfileDTO.getRegisteredOwner() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getRegisteredOwner()))) {
			return "Registered Owner details is missing";
		}
		/*if (!(shipProfileDTO.getInternationalGRT() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getInternationalGRT()))) {
			return "International G.R.T is missing";
		}
		if (!(shipProfileDTO.getKeelLaid() != null)) {
			return "Keel Laid date is missing";
		}*/
		if (!(shipProfileDTO.getImo() != null && StringUtils.isNotBlank(shipProfileDTO.getImo().toString()))) {
			return "IMO number is missing";
		}

		if(shipProfileDTO.getShipMasterId() != null){
			if(shipProfileDTO.getId() != null){
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipProfileDTO.getId());
				if(shipProfileInfo != null){
					UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getShipMasterId());
					if(userProfileInfo != null){
						if(shipProfileInfo.getShipMaster() != null){
							ShipProfileInfo shipProfileInfo2 = shipProfileRepository.findByShipMaster(userProfileInfo);
							if(shipProfileInfo2 != null){
								if(!shipProfileInfo.equals(shipProfileRepository.findByShipMaster(userProfileInfo)))
									return env.getProperty("ship.already.master.mapped");
							}
						}
					}else {
						return env.getProperty("ship.master.not.found");
					}
				}else{
					return env.getProperty("ship.not.found");
				}
			}else {
				UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getShipMasterId());
				if(userProfileInfo != null){
					if(shipProfileRepository.findByShipMaster(userProfileInfo) != null)
						return env.getProperty("ship.master.already.mapped");
				}else {
					return env.getProperty("ship.master.not.found");
				}
			}
		}



		/*if (!(shipProfileDTO.getShipMasterId() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getShipMasterId().toString()))) {
			return "Ship master detail is missing";
		}*/
		/*
		 * if (!(shipProfileDTO.getCommercialMasterIds() != null &&
		 * shipProfileDTO.getCommercialMasterIds().length > 0)) { return
		 * "Commercial master details is missing"; }
		 */
		/*
		 * if (!(shipProfileDTO.getShipMasterId() != null&&
		 * StringUtils.isNotBlank(shipProfileDTO.getShipMasterId().toString()))) {
		 * return "Ship master detail is missing"; } if
		 * (!(shipProfileDTO.getTechManagerIds() != null &&
		 * shipProfileDTO.getTechManagerIds().length > 0)) { return
		 * "Tech Manager detail is missing"; } if
		 * (!(shipProfileDTO.getDataOperatorsIds() != null &&
		 * shipProfileDTO.getDataOperatorsIds().length > 0)) { return
		 * "Data Operators detail is missing"; }
		 */
		return env.getProperty("success");

	}

	public boolean validateDeleteAll(ShipProfileDTO shipProfileDTO) {
		if (shipProfileDTO.getShipIds() != null && shipProfileDTO.getShipIds().length > 0
				&& shipProfileDTO.getUserId() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getUserId().toString())) {
			return true;
		}
		return false;
	}

	public String isShipExist(ShipProfileDTO shipProfileDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		for (Long shipId : shipProfileDTO.getShipIds()) {
			ShipProfileInfo shipProfile = shipProfileRepository.findByIdAndShipOrganizationInfo(shipId,
					userProfileInfo.getOrganizationInfo());
			if (shipProfile == null) {
				return shipId + " " + env.getProperty("shipprofile.not.exists");
			}
		}
		return env.getProperty("success");
	}

	public String isShipExistorNOt(ShipProfileDTO shipProfileDTO) {
		if (shipProfileDTO.getId() == null) {
			return env.getProperty("Ship profile detail is missing");
		}
		ShipProfileInfo shipProfile = shipProfileRepository.findOne(shipProfileDTO.getId());
		if (shipProfile == null) {
			return env.getProperty("shipprofile.not.exists");
		}
		return "Success";
	}

	public boolean isshipCountIsExceed(ShipProfileDTO shipProfileDTO) {
		System.out.println("OrganizationId" + shipProfileDTO.getOrganizationId());
		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(shipProfileDTO.getOrganizationId());
		SubscriptionInfo subscriptionInfo = subscriptionInfoRepository
				.findOne(organizationInfo.getSubscriptionInfo().getId());
		// SubscriptionInfo subscriptionInfo=subscriptionInfoRepository.findOne((long)
		// 1);
		List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByShipOrganizationInfo(organizationInfo);
		System.out.println(subscriptionInfo.getShipmentCount());
		System.out.println(shipProfileInfos.size());
		if (subscriptionInfo.getShipmentCount() <= shipProfileInfos.size()) {
			return true;
		}
		return false;
	}

	public List<ShipProfileDTO> convertShipProfileINfoToShipProfileDTOs(List<ShipProfileInfo> shipProfileInfos) {

		List<ShipProfileDTO> shipProfileDTOs = new ArrayList<>();
		for (ShipProfileInfo shipProfileInfo : shipProfileInfos) {
			ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
			shipProfileDTO.setVesselsName(shipProfileInfo.getShipName());
			if (shipProfileInfo.getShipProfilePicPath() != null) {
				String shipProfilePicturePath = env.getProperty("picture.path")+shipProfileInfo.getShipProfilePicPath();
				shipProfileDTO.setShipProfilePicPath(shipProfilePicturePath.replace(File.separator, "/"));
			}
			shipProfileDTO.setImo(shipProfileInfo.getIMO());
			shipProfileDTO.setShipTypes(shipProfileInfo.getShipType());
			shipProfileDTO.setId(shipProfileInfo.getId());
			shipProfileDTO.setPortName(shipProfileInfo.getStateName());
			// ship mesaurment
			shipProfileDTO.setDwt(shipProfileInfo.getDwt());
			shipProfileDTO.setBreadth(shipProfileInfo.getBreadth());
			shipProfileDTO.setWeight(shipProfileInfo.getWeight());
			shipProfileDTO.setLength(shipProfileInfo.getLength());
			// code for get expiryDocument detail gor current ship
			int activeCount = 0;
			int renewelCount = 0;
			int expiryedCount = 0;
			int pendingCount = 0;
			int totalCount = 0;
			int missingCount = 0;
			int count = 0;
			int newTotalcount = 0;
			List<DocumentHolderInfo> documentHolderInfos = documentHolderRepository.findDistinctDocumentHolderInfoByExpiryDocumentInfos_ShipProfileInfo(shipProfileInfo);
			// code for get expiryDocument detail gor current ship
			//List<ExpiryDocumentInfo> expiryDocumentInfos = expirDocumentRepository.findByShipProfileInfo(shipProfileInfo);
			List<DocumentHolderInfo> totalDocumentHolderInfoList = documentHolderRepository.findByDocumentHolderTypeOrderByDocumentHolderName(env.getProperty("document.holder.type"));
			totalCount = totalDocumentHolderInfoList.size();
			// for (int i = 0; i < documentHolderInfoList.size(); i++){
			// for (int j = 0; j < expiryDocumentInfos.size(); j++){
			// if (documentHolderInfoList.get(i).getId() ==
			// expiryDocumentInfos.get(j).getId())
			// count = count +1;
			// }
			// }
			if (documentHolderInfos != null && totalDocumentHolderInfoList != null) {
				count = documentHolderInfos.size();
				missingCount = totalDocumentHolderInfoList.size() - count;
			}
			LocalDate currentDate = LocalDate.now();
			LocalDate renewalDate = currentDate.plusDays(30);

			LOGGER.info("Current Date --->"+currentDate);
			LOGGER.info("Renewal Date --->"+renewalDate);

			//Date currentDate = new Date();
			List<ExpiryDocumentInfo> activeExpiryList = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateAfter(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
					env.getProperty("active"), java.sql.Date.valueOf(renewalDate));
			List<ExpiryDocumentInfo> activeByExpiryDate = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateIsNull(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
					env.getProperty("active"));
			if (activeByExpiryDate != null && activeByExpiryDate.size() >0)
				activeCount = activeExpiryList.size() + activeByExpiryDate.size();
			else{
				activeCount = activeExpiryList.size();
			}

			LOGGER.info("active items for ship "+shipProfileInfo.getShipName()+" with count"+activeCount);

			List<ExpiryDocumentInfo> expiredExpiryList = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBefore(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
					env.getProperty("active"), java.sql.Date.valueOf(currentDate));
			expiryedCount = expiredExpiryList.size();
			LOGGER.info("expired items for ship "+shipProfileInfo.getShipName()+" with count"+expiryedCount);

			List<ExpiryDocumentInfo> renewalExpiryList =  expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBetween(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
					env.getProperty("active"), java.sql.Date.valueOf(currentDate), java.sql.Date.valueOf(renewalDate));
			renewelCount = renewalExpiryList.size();
			LOGGER.info("renewal items for ship "+shipProfileInfo.getShipName()+" with count"+renewelCount);

			List<ExpiryDocumentInfo> pendingExpiryList = expirDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatus(shipProfileInfo,env.getProperty("document.status.pending"),env.getProperty("active"));
			pendingCount = pendingExpiryList.size();

			 Set<DocumentHolderInfo> documentHolderInfos2  = documentHolderRepository.findByVesselId(shipProfileInfo.getId());
			 newTotalcount += documentHolderInfos2.size();
			/*if (expiryDocumentInfos != null && expiryDocumentInfos.size() > 0) {
				for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfos) {
					LOGGER.info("expiryDocumentName" + expiryDocumentInfo.getDocumentName());
					DocumentNotification documentNotification = new DocumentNotification();
					documentNotification.setDocumentId(expiryDocumentInfo.getId());
					if (!expiryDocumentInfo.getDocumentStatus().equals("Pending")) {
						Date expiryDate = expiryDocumentInfo.getExpiryDate();
						long diff = expiryDate.getTime() - currentDate.getTime();
						long diffDays = diff / (24 * 60 * 60 * 1000);
						if (diffDays >= 30)
							activeCount = activeCount + 1;
						else if (diffDays < 30 && diffDays >= 15)
							renewelCount = renewelCount + 1;
						else if (diffDays < 15 && diffDays >= 0)
							expiryedCount = expiryedCount + 1;
					}
					pendingCount = pendingCount + 1;
				}
			}*/
			shipProfileDTO.setActiveCount(activeCount);
			shipProfileDTO.setRenewelCount(renewelCount);
			shipProfileDTO.setExpiryedCount(expiryedCount);
			shipProfileDTO.setPendingCount(pendingCount);
			shipProfileDTO.setTotalCount(newTotalcount);
			//shipProfileDTO.setMissingCount(missingCount);
			int total =  newTotalcount - (activeCount + renewelCount + expiryedCount) ;
			shipProfileDTO.setMissingCount(total);
			shipProfileDTOs.add(shipProfileDTO);

		}
		return shipProfileDTOs;
	}

	public List<ShipProfileDTO> convertShipProfileINfoToShipProfileDTOsForDashboardCount(
			List<ShipProfileInfo> shipProfileInfos, Role roleName,UserProfileInfo userProfileInfo) {

		List<ShipProfileDTO> shipProfileDTOs = new ArrayList<>();
		//Date currentDate = new Date();
		ShipProfileDTO shipProfileDTO = new ShipProfileDTO();

		int activeCount = 0;
		int renewelCount = 0;
		int expiryedCount = 0;
		int pendingCount = 0;
		int totalCount = 0;
		int missingCount = 0;
		int count = 0;
		int userCount = 0;
		int vesselsCount = 0;
		int newTotalcount = 0;
		if(shipProfileInfos!=null) {
			vesselsCount = shipProfileInfos.size();
		}
		List<UserProfileInfo> userProfileInfos = userProfileRepository.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
		if(shipProfileInfos !=null && shipProfileInfos.size()>0) {
			for (ShipProfileInfo shipProfileInfo : shipProfileInfos) {
				if (roleName.equals(Role.ShipMaster))
					shipProfileDTO.setShipName(shipProfileInfo.getShipName());

				List<DocumentHolderInfo> documentHolderInfos = documentHolderRepository.findDistinctDocumentHolderInfoByExpiryDocumentInfos_ShipProfileInfo(shipProfileInfo);
				// code for get expiryDocument detail gor current ship
				//List<ExpiryDocumentInfo> expiryDocumentInfos = expirDocumentRepository.findByShipProfileInfo(shipProfileInfo);
				List<DocumentHolderInfo> documentHolderInfoList = documentHolderRepository.findByDocumentHolderTypeOrderByDocumentHolderName(env.getProperty("document.holder.type"));
				totalCount += documentHolderInfoList.size();
				// for (int i = 0; i < documentHolderInfoList.size(); i++){
				// for (int j = 0; j < expiryDocumentInfos.size(); j++){
				// if (documentHolderInfoList.get(i).getId() ==
				// expiryDocumentInfos.get(j).getId())
				// count = count +1;
				// }
				// }
				 Set<DocumentHolderInfo> documentHolderInfos2  = documentHolderRepository.findByVesselId(shipProfileInfo.getId());
				 List<ExpiryDocumentInfo> activeExpiryList2 = expirDocumentRepository.findByShipProfileInfo(shipProfileInfo);
				 newTotalcount += documentHolderInfos2.size();
				if (documentHolderInfos != null && documentHolderInfoList != null) {
					count = documentHolderInfos.size();
					missingCount += documentHolderInfoList.size() - count;
				}

				/*if (expiryDocumentInfos != null && expiryDocumentInfos.size() > 0) {
					for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfos) {
						LOGGER.info("expiryDocumentName" + expiryDocumentInfo.getDocumentName());
						DocumentNotification documentNotification = new DocumentNotification();
						documentNotification.setDocumentId(expiryDocumentInfo.getId());
						if (!expiryDocumentInfo.getDocumentStatus().equals("Pending")) {
							Date expiryDate = expiryDocumentInfo.getExpiryDate();
							long diff = expiryDate.getTime() - currentDate.getTime();
							long diffDays = diff / (24 * 60 * 60 * 1000);
							if (diffDays >= 30)
								activeCount = activeCount + 1;
							else if (diffDays < 30 && diffDays >= 15)
								renewelCount = renewelCount + 1;
							else if (diffDays < 15 && diffDays >= 0)
								expiryedCount = expiryedCount + 1;
						}
						pendingCount = pendingCount + 1;
					}
				}*/
				LocalDate currentDate = LocalDate.now();
				LocalDate renewalDate = currentDate.plusDays(30);

				LOGGER.info("Current Date --->"+currentDate);
				LOGGER.info("Renewal Date --->"+renewalDate);

				//Date currentDate = new Date();
				List<ExpiryDocumentInfo> activeExpiryList = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateAfter(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
						env.getProperty("active"), java.sql.Date.valueOf(renewalDate));
				List<ExpiryDocumentInfo> activeByExpiryDate = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateIsNull(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
						env.getProperty("active"));
				if (activeByExpiryDate != null && activeByExpiryDate.size() >0)
					activeCount += activeExpiryList.size() + activeByExpiryDate.size();
				else
				activeCount += activeExpiryList.size();
				LOGGER.info("active items for ship "+shipProfileInfo.getShipName()+" with count"+activeCount);

				List<ExpiryDocumentInfo> expiredExpiryList = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBefore(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
						env.getProperty("active"), java.sql.Date.valueOf(currentDate));
				expiryedCount += expiredExpiryList.size();
				LOGGER.info("expired items for ship "+shipProfileInfo.getShipName()+" with count"+expiryedCount);

				List<ExpiryDocumentInfo> renewalExpiryList =  expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBetween(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
						env.getProperty("active"), java.sql.Date.valueOf(currentDate), java.sql.Date.valueOf(renewalDate));
				renewelCount += renewalExpiryList.size();
				LOGGER.info("renewal items for ship "+shipProfileInfo.getShipName()+" with count"+renewelCount);

				List<ExpiryDocumentInfo> pendingExpiryList = expirDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatus(shipProfileInfo,env.getProperty("document.status.pending"),env.getProperty("active"));
				pendingCount += pendingExpiryList.size();

			}
		}

		if (userProfileInfos != null) {
			for (UserProfileInfo userProfileInfo1 : userProfileInfos) {
				RoleInfo roleInfo = userProfileInfo1.getRoleId();
				if (userProfileInfo1 != null && !roleInfo.getRoleName().equals(Role.Admin)
						&& !roleInfo.getRoleName().equals(Role.SuperAdmin)) {
					userCount = userCount + 1;
				}
			}
		}
		shipProfileDTO.setVesselsCount(vesselsCount);
		shipProfileDTO.setUserCount(userCount);
		shipProfileDTO.setActiveCount(activeCount);
		shipProfileDTO.setRenewelCount(renewelCount);
		shipProfileDTO.setExpiryedCount(expiryedCount);
		shipProfileDTO.setPendingCount(pendingCount);
		shipProfileDTO.setTotalCount(totalCount);
//		shipProfileDTO.setMissingCount(missingCount);
		int total =  newTotalcount - (activeCount + renewelCount + expiryedCount) ;
		shipProfileDTO.setMissingCount(total);
		shipProfileDTOs.add(shipProfileDTO);
		return shipProfileDTOs;
	}

	public ShipProfileDTO convertShipProfileINfoToShipProfileDTOsForDashboardCountBasedOnVessel(
			ShipProfileInfo shipProfileInfo) {
		ShipProfileDTO  shipProfileDTO = new ShipProfileDTO();
		int activeCount = 0;
		int renewelCount = 0;
		int expiryedCount = 0;
		int pendingCount = 0;
		int totalCount = 0;
		int missingCount = 0;
		int count;
		List<DocumentHolderInfo> documentHolderInfos = documentHolderRepository.findDistinctDocumentHolderInfoByExpiryDocumentInfos_ShipProfileInfo(shipProfileInfo);
		List<DocumentHolderInfo> documentHolderInfoList = documentHolderRepository.findByDocumentHolderTypeOrderByDocumentHolderName(env.getProperty("document.holder.type"));
		totalCount += documentHolderInfoList.size();
		if (documentHolderInfos != null && documentHolderInfoList != null) {
			count = documentHolderInfos.size();
			//missingCount += documentHolderInfoList.size() - count;
		}
		Set<DocumentHolderInfo> documentHolderInfos2  = documentHolderRepository.findByVesselId(shipProfileInfo.getId());
		missingCount += documentHolderInfos2.size();
		LocalDate currentDate = LocalDate.now();
		LocalDate renewalDate = currentDate.plusDays(30);

		LOGGER.info("Current Date --->"+currentDate);
		LOGGER.info("Renewal Date --->"+renewalDate);

		//Date currentDate = new Date();
		List<ExpiryDocumentInfo> activeExpiryList = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateAfter(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
				env.getProperty("active"), java.sql.Date.valueOf(renewalDate));
		List<ExpiryDocumentInfo> activeByExpiryDate = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateIsNull(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
				env.getProperty("active"));
		if (activeByExpiryDate != null && activeByExpiryDate.size() >0)
			activeCount += activeExpiryList.size() + activeByExpiryDate.size();
		else
		activeCount += activeExpiryList.size();
		LOGGER.info("active items for ship "+shipProfileInfo.getShipName()+" with count"+activeCount);

		List<ExpiryDocumentInfo> expiredExpiryList = expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBefore(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
				env.getProperty("active"), java.sql.Date.valueOf(currentDate));
		expiryedCount += expiredExpiryList.size();
		LOGGER.info("expired items for ship "+shipProfileInfo.getShipName()+" with count"+expiryedCount);

		List<ExpiryDocumentInfo> renewalExpiryList =  expirDocumentRepository.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatusAndExpiryDateBetween(shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
				env.getProperty("active"), java.sql.Date.valueOf(currentDate), java.sql.Date.valueOf(renewalDate));
		renewelCount += renewalExpiryList.size();
		LOGGER.info("renewal items for ship "+shipProfileInfo.getShipName()+" with count"+renewelCount);

		List<ExpiryDocumentInfo> pendingExpiryList = expirDocumentRepository.findByShipProfileInfoAndDocumentStatusAndStatus(shipProfileInfo,env.getProperty("document.status.pending"),env.getProperty("active"));
		pendingCount += pendingExpiryList.size();
		shipProfileDTO.setActiveCount(activeCount);
		shipProfileDTO.setRenewelCount(renewelCount);
		shipProfileDTO.setExpiryedCount(expiryedCount);
		shipProfileDTO.setPendingCount(pendingCount);
		shipProfileDTO.setTotalCount(totalCount);
		int total =  missingCount - (activeCount + renewelCount + expiryedCount) ;
		shipProfileDTO.setMissingCount(total);
		shipProfileDTO.setVesselsName(shipProfileInfo.getShipName());
				// ship measurement
		shipProfileDTO.setDwt(shipProfileInfo.getDwt());
		shipProfileDTO.setWeight(shipProfileInfo.getWeight());
		shipProfileDTO.setLength(shipProfileInfo.getLength());
		shipProfileDTO.setBreadth(shipProfileInfo.getBreadth());
		return shipProfileDTO;
	}

}
