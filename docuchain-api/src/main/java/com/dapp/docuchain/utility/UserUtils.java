package com.dapp.docuchain.utility;

import com.dapp.docuchain.dto.GeoLocationDTO;
import com.dapp.docuchain.dto.RequestUserDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserUtils {

	static final String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class);

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	ShipProfileRepository shipProfileRepo;

	@Autowired
	CountryInfoRepository countryinforepo;

	@Autowired
	private PortInfoRepository portInfoRepository;

	@Autowired
	ShipTypesRepository shiptypesrepo;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserLastSeenHistory userLastSeen;

	@Autowired
	private ShipProfileRepository shipProfileRepository;

	@Autowired
	private RoleInfoRepository roleInfoRepository;

	@Autowired
	private SubscriptionInfoRepository subscriptionInfoRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private GeoLocationRepository geoLocationRepository;

	@Autowired
	Environment env;

	@Autowired
	private RoleAliasInfoRepository roleAliasInfoRepository;

	public UserDTO converUsertoUserDTO(UserProfileInfo userProfileInfo2) {
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName(userProfileInfo2.getUserName());
		// userDTO.setPassword(user.getPassword());
		RoleInfo roleInfo = roleInfoRepository.findByRoleName(Role.ShipMaster);
		UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(userProfileInfo2.getUserName());
		if (userProfileInfo != null) {
			userDTO.setRoleId(userProfileInfo.getRoleId().getId());
			userDTO.setUserId(userProfileInfo.getId());
			if (roleInfo != null && userProfileInfo.getRoleId().getId().equals(roleInfo.getId())) {
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(userProfileInfo);
				if (shipProfileInfo != null) {
					userDTO.setShipName(shipProfileInfo.getShipName());
				}

			}
		}
		userDTO.setBusinessCategory(userProfileInfo2.getRoleId().getRoleName().name());
		return userDTO;
	}

	public UserDTO converUsertoUserDTO(User userInfo) {
		List<ShipProfileDTO> shipProfileDTOs = new ArrayList<>();
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName(userInfo.getUsername());
		// userDTO.setPassword(user.getPassword());
		//RoleInfo roleInfo = roleInfoRepository.findByRoleName(Role.ShipMaster);
		UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(userInfo.getUsername());
		if (userProfileInfo != null) {
			userDTO.setFirstName(userProfileInfo.getFirstName());
			userDTO.setLastName(userProfileInfo.getLastName());
			//userDTO.setPassword(userInfo.getPassword());
			//userDTO.setRoleId(userProfileInfo.getRoleId().getId());
			userDTO.setUserId(userProfileInfo.getId());
			if(!userProfileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)){
				userDTO.setOrganizationId(userProfileInfo.getOrganizationInfo().getId());
			}
			userDTO.setMail(userInfo.getMail());
			RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(userProfileInfo.getRoleId(), userProfileInfo.getOrganizationInfo());
			if(roleAliasInfo != null){
				userDTO.setRole(roleAliasInfo.getRoleAliasName());
				userDTO.setRoleId(userProfileInfo.getRoleId().getId());
			}else {
				userDTO.setRole(userProfileInfo.getRoleId().getRoleName().name());
				userDTO.setRoleId(userProfileInfo.getRoleId().getId());
			}
			userDTO.setStatus(userProfileInfo.getStatus().longValue());
			//userDTO.setRole(userProfileInfo.getRoleId().getRoleName().name());
			if(userProfileInfo.getProfilePicturePath() != null){
				String profilePicturePath = env.getProperty("picture.path")+userProfileInfo.getProfilePicturePath();
				//System.out.println("Profile Picture path :"+profilePicturePath.replace(File.separator, "/"));
				userDTO.setProfilePicture(profilePicturePath.replace(File.separator, "/"));
			}
			if(userProfileInfo.getOrganizationInfo() != null)
			{
				if(userProfileInfo.getOrganizationInfo().getId() != null) {
					List<OrganizationInfo> organizationInfo = organizationInfoRepository.findById(userProfileInfo.getOrganizationInfo().getId());
					String companyLogo = null;
					for (OrganizationInfo organizationInfo2 : organizationInfo){
						 companyLogo = env.getProperty("picture.path") + organizationInfo2.getCompanyLogo();
					}
					userDTO.setLogoPicture(companyLogo.replace(File.separator, "/"));
				}
			}
			if (userProfileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMasterAndStatus(userProfileInfo,1);
				if (shipProfileInfo != null) {
					ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
					shipProfileDTO.setId(shipProfileInfo.getId());
					shipProfileDTO.setShipName(shipProfileInfo.getShipName());
					shipProfileDTO.setImo(shipProfileInfo.getIMO());
					shipProfileDTO.setPortName(shipProfileInfo.getStateName());
					shipProfileDTOs.add(shipProfileDTO);
				}
				userDTO.setShipProfileInfos(shipProfileDTOs);
			}
			if (userProfileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
				List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByStatusAndTechMasters(1,userProfileInfo);
				for(ShipProfileInfo shipProfileInfo :shipProfileInfos){
					ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
					shipProfileDTO.setId(shipProfileInfo.getId());
					shipProfileDTO.setShipName(shipProfileInfo.getShipName());
					shipProfileDTO.setPortName(shipProfileInfo.getStateName());
					shipProfileDTO.setImo(shipProfileInfo.getIMO());
					shipProfileDTOs.add(shipProfileDTO);
				}
				userDTO.setShipProfileInfos(shipProfileDTOs);
			}
			if (userProfileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
				List<ShipProfileInfo> shipProfileInfos =  shipProfileRepository.findByStatusAndCommercialMasters(1,userProfileInfo);
				for(ShipProfileInfo shipProfileInfo :shipProfileInfos){
					ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
					shipProfileDTO.setId(shipProfileInfo.getId());
					shipProfileDTO.setShipName(shipProfileInfo.getShipName());
					shipProfileDTO.setPortName(shipProfileInfo.getStateName());
					shipProfileDTO.setImo(shipProfileInfo.getIMO());
					shipProfileDTOs.add(shipProfileDTO);
				}
				userDTO.setShipProfileInfos(shipProfileDTOs);
			}
			if (userProfileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
				if(userProfileInfo.getOrganizationInfo().getOrganizationName() != null){
					userDTO.setOrganizationName(userProfileInfo.getOrganizationInfo().getOrganizationName());
				}
				SubscriptionInfo subscriptionInfo = userProfileInfo.getOrganizationInfo().getSubscriptionInfo();
				if(subscriptionInfo!=null) {
					userDTO.setMaxShipCount(subscriptionInfo.getShipmentCount());
					userDTO.setMaxUserCount(subscriptionInfo.getUserCount());
					Date currentDate = new Date();
					try {
						if(DATE_FORMAT.parse(DATE_FORMAT.format(currentDate)).compareTo(subscriptionInfo.getSubscriptionExpireDate()) > 0){
							userDTO.setSubscriptionExpireStatus(env.getProperty("subscription.expire.status"));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		userDTO.setBusinessCategory(userInfo.getBusinessCategory());
		return userDTO;
	}

	public boolean isValidRequest(RequestUserDTO requestUserDTO) {

		if (requestUserDTO.getNumberOfUser() != null && requestUserDTO.getRemarks() != null
				&& StringUtils.isNotEmpty(requestUserDTO.getRemarks()) && requestUserDTO.getShipId() != null
				&& requestUserDTO.getUploadUserId() != null) {
			return true;
		}
		return false;
	}

	public boolean validateShipProfileParam(ShipProfileDTO shipProfileDTO) {
		if (shipProfileDTO.getBhp() != null && StringUtils.isNotBlank(shipProfileDTO.getBhp())
				&& shipProfileDTO.getBuilder() != null && StringUtils.isNotBlank(shipProfileDTO.getBuilder())
				&& shipProfileDTO.getCallSign() != null && StringUtils.isNotBlank(shipProfileDTO.getCallSign())
				&& shipProfileDTO.getCountryName() != null && StringUtils.isNotBlank(shipProfileDTO.getCountryName())
				&& shipProfileDTO.getDelivered() != null && StringUtils.isNotBlank(shipProfileDTO.getDelivered())
				&& shipProfileDTO.getEngineType() != null && StringUtils.isNotBlank(shipProfileDTO.getEngineType())
				&& shipProfileDTO.getInternationalNRT() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getInternationalNRT()) && shipProfileDTO.getShipName() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getShipName()) && shipProfileDTO.getShipTypes() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getShipTypes()) && shipProfileDTO.getStateName() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getStateName()) && shipProfileDTO.getShipOwner() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getShipOwner()) && shipProfileDTO.getInternationalGRT() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getInternationalGRT()) && shipProfileDTO.getKeelLaid() != null
				&& shipProfileDTO.getOfficialNo() != null

		) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateUpdateParam(ShipProfileDTO shipProfileDTO) {
		if (shipProfileDTO.getCountryId() != null && shipProfileDTO.getStateId() != null
				&& shipProfileDTO.getShiptypeId() != null && shipProfileDTO.getId() != 0
				&& shipProfileDTO.getBhp() != null && StringUtils.isNotBlank(shipProfileDTO.getBhp())
				&& shipProfileDTO.getBuilder() != null && StringUtils.isNotBlank(shipProfileDTO.getBuilder())
				&& shipProfileDTO.getCallSign() != null && StringUtils.isNotBlank(shipProfileDTO.getCallSign())
				&& shipProfileDTO.getDelivered() != null && StringUtils.isNotBlank(shipProfileDTO.getDelivered())
				&& shipProfileDTO.getEngineType() != null && StringUtils.isNotBlank(shipProfileDTO.getEngineType())
				&& shipProfileDTO.getInternationalGRT() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getInternationalGRT())
				&& shipProfileDTO.getInternationalNRT() != null
				&& StringUtils.isNotBlank(shipProfileDTO.getInternationalNRT()) && shipProfileDTO.getKeelLaid() != null
				&& shipProfileDTO.getShipOwner() != null && StringUtils.isNotBlank(shipProfileDTO.getShipOwner())
				&& shipProfileDTO.getStatus() != null && StringUtils.isNotBlank(shipProfileDTO.getStatus().toString())

		) {

			CountryInfo countryInfo = countryinforepo.findById(shipProfileDTO.getCountryId());
			PortInfo portInfo = portInfoRepository.findOne(shipProfileDTO.getStateId());
			ShipTypesInfo shipTypeInfo = shiptypesrepo.findById(shipProfileDTO.getShiptypeId());
			if (countryInfo != null && portInfo != null && shipTypeInfo != null) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	public String offNoValidateForShipProfile(ShipProfileDTO shipProfileDTO) {
		List<ShipProfileInfo> shipname = shipProfileRepo.findShipProfileInfoByShipName(shipProfileDTO.getShipName());
		ShipProfileInfo shipProfile = shipProfileRepo.findByIMO(shipProfileDTO.getImo());
		LOGGER.info("GGDFGGDG::" + shipProfile);
		if (shipProfile != null) {
			LOGGER.info("mgjgg");
			return "IMO number already exist";
		}
		/*if (shipname != null && shipname.size() > 0) {
			return "Ship Name already exits";
		}*/

		return "Success";

	}

	public boolean validateCountryIdorName(ShipProfileDTO shipProfileDTO) {
		LOGGER.info("getCountryName()" + shipProfileDTO.getCountryName());
		if (shipProfileDTO.getCountryName() != null && StringUtils.isNotBlank(shipProfileDTO.getCountryName())) {
			CountryInfo countryInfo = countryinforepo.findByCountryName(shipProfileDTO.getCountryName());
			LOGGER.info("countryInfo" + countryInfo);
			if (countryInfo.getPortInfos() != null && countryInfo.getPortInfos().size() > 0) {
				LOGGER.info("countryInfo.getState()" + countryInfo.getPortInfos());
				return true;

			}
			return false;
		} else if (shipProfileDTO.getCountryId() != null) {
			CountryInfo countryInfo1 = countryinforepo.findById(shipProfileDTO.getCountryId());
			if (countryInfo1.getPortInfos() != null && countryInfo1.getPortInfos().size() > 0) {
				LOGGER.info("countryInfo.getState()" + countryInfo1.getPortInfos());
				return true;

			}
			return false;
		}
		return false;

	}

	public boolean validateOffNo(Long officialNum) {

		if (officialNum == null) {
			return false;

		} else {
			ShipProfileInfo shipProfile = shipProfileRepo.findByIMO(officialNum);
			if (shipProfile == null) {

				return false;
			} else {
				return true;
			}

		}

	}

	public boolean validateUserId(Long userId) {

		if (userId == null) {
			return false;

		} else {
			UserProfileInfo validateUserId = userProfileRepository.findById(userId);
			if (validateUserId == null) {

				return false;
			} else {
				return true;
			}

		}

	}

	public Boolean vaildateUserName(String userName) {
		if (userName != null && StringUtils.isNotBlank(userName)) {
			return true;
		} else {
			return false;
		}

	}

	public Boolean vaildateResetPasswordParam(UserDTO userDTO) {
		if (userDTO.getPassword() != null && userDTO.getUserName() != null
				&& StringUtils.isNotBlank(userDTO.getPassword()) && StringUtils.isNotBlank(userDTO.getUserName())) {
			return true;
		}
		return false;
	}

	public List<RequestUserDTO> convertRequestUserInfostoRequestUserDTOs(List<RequestUserInfo> requestUserInfos) {

		List<RequestUserDTO> requestUserDTOs = new ArrayList<>();
		for (int i = 0; i < requestUserInfos.size(); i++) {
			RequestUserDTO requestUserDTO = new RequestUserDTO();
			UserProfileInfo userProfileInfo1 = userProfileRepository.findOne(requestUserInfos.get(i).getRequesterInfo().getId());
			if (userProfileInfo1 != null){
			requestUserDTO.setRequesterName(userProfileInfo1.getUserName());
			requestUserDTO.setRoleName(userProfileInfo1.getRoleId().getRoleName().name());
			}
			requestUserDTO.setRequestId(requestUserInfos.get(i).getId());
			requestUserDTO.setNumberOfUser(requestUserInfos.get(i).getNumberOfUser());
			requestUserDTO.setRemarks(requestUserInfos.get(i).getRemarks());
			requestUserDTO.setRequestUserStatus(requestUserInfos.get(i).getRequestUserStatus());
			requestUserDTO.setUserName(requestUserInfos.get(i).getRequesterInfo().getUserName());
			requestUserDTO.setShipName(requestUserInfos.get(i).getUserRequestShipProfile().getShipName());
			Set<UserProfileInfo> userProfileInfos = requestUserInfos.get(i).getRequestedUserList();
			List<UserDTO> userDTOs = new ArrayList<>();
			for (UserProfileInfo userProfileInfo : userProfileInfos) {
				UserDTO userDTO = new UserDTO();
				userDTO.setUserName(userProfileInfo.getUserName());
				userDTO.setUserId(userProfileInfo.getId());
				userDTOs.add(userDTO);
			}
			requestUserDTO.setUserProfileInfos(userDTOs);
			requestUserDTOs.add(requestUserDTO);
		}
		return requestUserDTOs;
	}

	public Boolean validUserID(Long userId) {
		if (userId != 0) {
			UserProfileInfo userInfo = userProfileRepository.findOne(userId);
			List<UserLastSeenInfo> lastseenUSer = userLastSeen.findByUserName(userInfo.getUserName());

			if (lastseenUSer != null) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean isAlreadyExits(String emailId) {
		List<User> userList = userRepository.findByMail(emailId);
		if (userList != null && userList.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean validateEmail(String emailId) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidForgerPassParaam(UserDTO userDTO) {
		if (userDTO.getMail() != null && StringUtils.isNotBlank(userDTO.getMail())) {
			return true;
		}
		return false;
	}

	public boolean isEmailIdExits(String mail) {
		List<User> userList = userRepository.findByMail(mail);
		if (userList != null && userList.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean validRole(String businessCategory) {
		RoleInfo roleInfo = roleInfoRepository.findByRoleName(Role.valueOf(businessCategory));
		if (roleInfo.getRoleName() != null && StringUtils.isNotEmpty(roleInfo.getRoleName().name())) {
			return true;
		}
		return false;
	}

	public boolean checkAdminParam(UserDTO userDTO) {
		if (userDTO.getOrganizationId() != null && StringUtils.isNotBlank(userDTO.getOrganizationId().toString())) {
			return true;
		}
		return false;
	}

	public String verifyParamResponse(UserDTO userDTO) {

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(userDTO.getOrganizationId());
		if (organizationInfo == null) {
			return env.getProperty("organization.id.not.exist");
		}

		if(userDTO.getRoleId() == null)
			return env.getProperty("role.id.null");

		RoleInfo roleInfo = roleInfoRepository.findOne(userDTO.getRoleId());
		if(roleInfo == null)
			return env.getProperty("role.not.found");

		if(!roleInfo.getRoleName().equals(Role.SuperAdmin) && !roleInfo.getRoleName().equals(Role.Admin)){
			if(vaildAdminUserCount(userDTO)){
				return env.getProperty("user.count.exists");
			}
		}

		return env.getProperty("success");

	}

	public boolean vaildAdminUserCount(UserDTO userDTO) {
		System.out.println("or I" + userDTO.getOrganizationId());
		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(userDTO.getOrganizationId());
		SubscriptionInfo subscriptionInfo = subscriptionInfoRepository
				.findOne(organizationInfo.getSubscriptionInfo().getId());
		// SubscriptionInfo subscriptionInfo=subscriptionInfoRepository.findOne((long)
		// 1);
		List<UserProfileInfo> userInfoList = userProfileRepository.findByOrganizationInfo(organizationInfo);
		List<UserProfileInfo> userInfos = new ArrayList<>();
		for (UserProfileInfo userInfo : userInfoList) {
			if (!userInfo.getRoleId().getRoleName().equals(Role.Admin)
					&& !userInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
				userInfos.add(userInfo);
			}
		}
		if (subscriptionInfo.getUserCount() == userInfos.size()) {
			return true;
		}
		return false;
	}

	public boolean validUserDTO(UserDTO userDTO) {
		if (userDTO != null)
			return true;
		return false;
	}

	public boolean isAdminExists(Long adminid) {
		UserProfileInfo adminInfo = userProfileRepository.findOne(adminid);
		if (adminInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return true;
		}
		return false;
	}

	public boolean validateShipId(ShipProfileDTO shipProfileDTO) {
		if (shipProfileDTO.getId() != null && StringUtils.isNotBlank(shipProfileDTO.getId().toString())) {
			return true;
		}
		return false;
	}

	public String validateUpdateShipParam(ShipProfileDTO shipProfileDTO) {
		ShipProfileInfo shipProfile = shipProfileRepo.findByIMO(shipProfileDTO.getImo());
		if (shipProfile != null && shipProfile.getId() != shipProfileDTO.getId()) {
			return "IMO number already exist";
		}
		/*if (shipProfile != null && !shipProfile.getShipName().equals(shipProfileDTO.getShipName())) {
			return "Ship Name already exits";
		}*/
		return "Success";
	}

	public String isVaildStatusUpateParam(UserDTO userDTO) {
		if (userDTO.getUserIds() == null || userDTO.getUserIds().length == 0) {
			return "Please select user to activate user";
		}
		return "Success";
	}

	public String checkUserIdExists(UserDTO userDTO) {
		for (Long userId : userDTO.getUserIds()) {
			UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);;
			if (userProfileInfo == null) {
				return userId+" "+ env.getProperty("user.not.exists");
			}
		}
		return "Success";

	}

	public String checkShipProfileIsExists(UserDTO userDTO){
		if(userDTO.getShipProfileIds() != null && userDTO.getShipProfileIds().length > 0){
			for(Long shipId : userDTO.getShipProfileIds()){
				if(shipProfileRepository.findOne(shipId) == null)
					return env.getProperty("ship.not.found");
			}
		}

		return env.getProperty("success");
	}

	public boolean isValidLocation(GeoLocationDTO geoLocationDTO) {
		if (geoLocationDTO.getLatitude() != null
				&& geoLocationDTO.getLongitute() != null){

			return true;
		}

		return false;
	}

	public GeoLocationDTO convertGeoLocationInfoToGeoLocationDTO(GeoLocationInfo geoLocationInfo) {
		GeoLocationDTO geoLocationDTO = new GeoLocationDTO();
		geoLocationDTO.setId(geoLocationInfo.getId());
		geoLocationDTO.setShipId(geoLocationInfo.getShipId());
		geoLocationDTO.setLatitude(geoLocationInfo.getLatitude());
		geoLocationDTO.setLongitute(geoLocationInfo.getLongitute());
		return geoLocationDTO;
	}

	public List<UserDTO> convertShipInfosToGeoLocation(List<ShipProfileInfo> shipProfileInfos) {
		List<UserDTO> userDTOs = new ArrayList<>();
		GeoLocationInfo geoLocationInfo = null;
		for (ShipProfileInfo shipProfileInfo : shipProfileInfos){
			ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
			UserDTO userDTO = new UserDTO();
			GeoLocationDTO geoLocationDTO = new GeoLocationDTO();
			shipProfileDTO.setId(shipProfileInfo.getId());
			shipProfileDTO.setShipName(shipProfileInfo.getShipName());
			shipProfileDTO.setImo(shipProfileInfo.getIMO());
			userDTO.setShipProfileDTO(shipProfileDTO);
			geoLocationInfo = geoLocationRepository.findByShipId(shipProfileInfo.getId());
			if (geoLocationInfo != null){
				geoLocationDTO.setId(geoLocationInfo.getId());
				geoLocationDTO.setLatitude(geoLocationInfo.getLatitude());
				geoLocationDTO.setLongitute(geoLocationInfo.getLongitute());
				geoLocationDTO.setUpdateDate(geoLocationInfo.getUpdateDate());
			}
			userDTO.setGeoLocationInfos(geoLocationDTO);
			userDTOs.add(userDTO);
		}

		return userDTOs;
	}

}
