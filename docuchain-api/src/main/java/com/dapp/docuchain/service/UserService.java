package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.GeoLocationDTO;
import com.dapp.docuchain.dto.RequestUserDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.*;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.NotificationUtility;
import com.dapp.docuchain.utility.UserUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.naming.Name;
import javax.naming.NamingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private final String ONLY_LETTERS = "[a-zA-Z\\s]+";

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private UserLastSeenHistory userLastSeenRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private RoleInfoRepository roleRepo;

	@Autowired
	private RequestUserRepository requestUserRepository;

	@Autowired
	private ShipProfileRepository shipProfileRepository;

	@Autowired
	private DataModifiedHistoryRepository dataModifiedHistoryRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private EmailService emailService;

	@Autowired
	private Environment env;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private RoleAliasInfoRepository roleAliasInfoRepository;

	@Autowired
	private GeoLocationRepository geoLocationRepository;

	@Autowired
	private NotificationUtility notificationUtility;

	@Autowired
	private ExpiryDocumentRepository expiryDocumentRepository;

	// public UserDTO authenticate(final String username, final String password) {
	// 	User user = userRepository.findByUsernameAndPassword(username, digestSHA(password));
	// 	if (user != null) {
	// 		UserLastSeenInfo userLastSeenInfo = new UserLastSeenInfo();
	// 		userLastSeenInfo.setUserName(user.getUsername());
	// 		LOGGER.info("UserName" + userLastSeenInfo.getUserName());
	// 		Date date = new Date();
	// 		userLastSeenInfo.setUserLastSeenDateTime(date);
	// 		userLastSeenRepo.save(userLastSeenInfo);
	// 		LOGGER.info("Last Seen Date" + userLastSeenInfo.getUserLastSeenDateTime());
	// 		if (user != null) {
	// 			UserDTO userDTO = userUtils.converUsertoUserDTO(user);
	// 			return userDTO;
	// 		}
	// 	}
	// 	return null;

	// }
//   public UserDTO authenticate(final String username, final String password) {
//     // ✅ CORRECT: Only find by username. Password was already checked by LDAP.
//     User user = userRepository.findByUsername(username);

//     if (user != null) {
//         UserLastSeenInfo userLastSeenInfo = new UserLastSeenInfo();
//         userLastSeenInfo.setUserName(user.getUsername());
//         LOGGER.info("UserName" + userLastSeenInfo.getUserName());

//         Date date = new Date();
//         userLastSeenInfo.setUserLastSeenDateTime(date);
//         userLastSeenRepo.save(userLastSeenInfo);
//         LOGGER.info("Last Seen Date" + userLastSeenInfo.getUserLastSeenDateTime());

//         return userUtils.converUsertoUserDTO(user);
//     }
//     return null;
// }
public UserDTO authenticate(final String username, final String password) {

    User user = userRepository.findByUsernameAndPassword(
            username,
            digestSHA(password)
    );

    if (user == null) {
        return null;
    }

    UserLastSeenInfo userLastSeenInfo = new UserLastSeenInfo();
    userLastSeenInfo.setUserName(user.getUsername());
    userLastSeenInfo.setUserLastSeenDateTime(new Date());
    userLastSeenRepo.save(userLastSeenInfo);

    return userUtils.converUsertoUserDTO(user);
}


	public UserDTO getUserInformationDetails(Long userId){
		UserDTO userDTO = new UserDTO();
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
		if(userProfileInfo != null){
			return userUtils.converUsertoUserDTO(userRepository.findByUsername(userProfileInfo.getUserName()));
		}
		return userDTO;
	}

	// public String loginUserBasicValidation(UserDTO userDTO){
	// 	if(StringUtils.isBlank(userDTO.getUserName()) && StringUtils.isEmpty(userDTO.getUserName()))
	// 		return env.getProperty("user.username.empty");

	// 	if(StringUtils.isBlank(userDTO.getPassword()) && StringUtils.isEmpty(userDTO.getPassword()))
	// 		return env.getProperty("user.password.empty");

	// 	if(userRepository.findByUsername(userDTO.getUserName()) == null)
	// 		return env.getProperty("user.username.not.found");

	// 	User user = userRepository.findByUsernameAndPassword(userDTO.getUserName(), digestSHA(userDTO.getPassword()));
	// 	if(user == null)
	// 		return env.getProperty("user.username.password.not.matching");

	// 	UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(userDTO.getUserName());
	// 	if(userProfileInfo == null)
	// 		return env.getProperty("user.not.found");

	// 	userProfileInfo = userProfileRepository.findByUserNameAndStatus(userDTO.getUserName(), new Long(1));
	// 	if(userProfileInfo == null)
	// 		return env.getProperty("user.not.active");

	// 	if(userProfileInfo.getOrganizationInfo() != null){
	// 		if(userProfileInfo.getOrganizationInfo().getIsActive() == 0)
	// 			return env.getProperty("user.organization.not.active");

	// 		if(userProfileInfo.getOrganizationInfo().getSubscriptionInfo() != null){
	// 			if(!userProfileInfo.getRoleId().getRoleName().equals(Role.Admin)){
	// 				Date currentDate = new Date();

	// 				try {
	// 					if(DATE_FORMAT.parse(DATE_FORMAT.format(currentDate)).compareTo(userProfileInfo.getOrganizationInfo().getSubscriptionInfo().getSubscriptionExpireDate()) > 0)
	// 						return env.getProperty("organization.subscription.expired");
	// 				} catch (ParseException e) {
	// 					e.printStackTrace();
	// 				}
	// 			}
	// 		}else{
	// 			env.getProperty("organization.subscription.not.found");
	// 		}

	// 	}

	// 	return env.getProperty("success");
	// }
  public String loginUserBasicValidation(UserDTO userDTO){
    // 1. Check if username is empty
    if(StringUtils.isBlank(userDTO.getUserName()) && StringUtils.isEmpty(userDTO.getUserName()))
      return env.getProperty("user.username.empty");

    // 2. Check if password is empty
    if(StringUtils.isBlank(userDTO.getPassword()) && StringUtils.isEmpty(userDTO.getPassword()))
      return env.getProperty("user.password.empty");

    // 3. Check if user exists in DB (Username only, NO PASSWORD CHECK)
    if(userRepository.findByUsername(userDTO.getUserName()) == null)
      return env.getProperty("user.username.not.found");

    // ❌ DELETED: findByUsernameAndPassword (This was the problem!)
    // We do NOT check the password here anymore because LDAP already did it.

    // 4. Continue with Profile and Organization checks
    UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(userDTO.getUserName());
    if(userProfileInfo == null)
      return env.getProperty("user.not.found");

    userProfileInfo = userProfileRepository.findByUserNameAndStatus(userDTO.getUserName(), new Long(1));
    if(userProfileInfo == null)
      return env.getProperty("user.not.active");

    if(userProfileInfo.getOrganizationInfo() != null){
      if(userProfileInfo.getOrganizationInfo().getIsActive() == 0)
        return env.getProperty("user.organization.not.active");

      if(userProfileInfo.getOrganizationInfo().getSubscriptionInfo() != null){
        if(!userProfileInfo.getRoleId().getRoleName().equals(Role.Admin)){
          Date currentDate = new Date();

          try {
            if(DATE_FORMAT.parse(DATE_FORMAT.format(currentDate)).compareTo(userProfileInfo.getOrganizationInfo().getSubscriptionInfo().getSubscriptionExpireDate()) > 0)
              return env.getProperty("organization.subscription.expired");
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      }else{
        env.getProperty("organization.subscription.not.found");
      }

    }

    return env.getProperty("success");
  }

	public List<UserDTO> search(String roll) {
		// LOGGER.info("user
		// object"+user.getBusinessCategory()+user.getUsername()+user.getPassword());
		List<UserDTO> userDTOs = new ArrayList<>();
		if (roll.equalsIgnoreCase("DataOperator")) {
			List<UserProfileInfo> userList = userProfileRepository.findAll();
			if (userList == null) {
				return Collections.emptyList();
			}
			for (UserProfileInfo user : userList) {
				UserDTO userDTO = new UserDTO();
				UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(user.getUserName());
				if (!userProfileInfo.getUserName().equalsIgnoreCase("Admin")) {
					userDTO = userUtils.converUsertoUserDTO(userProfileInfo);
					userDTOs.add(userDTO);
				}
			}
			LOGGER.info("userSize:::" + userList.size());
			return userDTOs;
		} else {
			RoleInfo roleInfo = roleRepo.findByRoleName(Role.valueOf(roll));
			List<UserProfileInfo> userList = userProfileRepository.findByRoleId(roleInfo);
			if (userList == null) {
				return Collections.emptyList();
			}
			for (UserProfileInfo userName : userList) {
				UserDTO userDTO = new UserDTO();
				UserProfileInfo user = userProfileRepository.findByUserName(userName.getUserName());
				userDTO = userUtils.converUsertoUserDTO(user);
				userDTOs.add(userDTO);
			}
			LOGGER.info("userSize:::" + userList.size());
			return userDTOs;
		}
	}

	/*
	 * public void create(final String username, final String password) { User
	 * newUser = new
	 * User().builder().username(username).surname(username).password(digestSHA(
	 * password)).mail("admin@admin.com").businessCategory("admin").build();
	 * newUser.setId(LdapUtils.emptyLdapName()); userRepository.save(newUser);
	 *
	 * }
	 */
	public boolean create(UserDTO userDTO, MultipartFile profilePictureFile) throws NamingException {
		UserProfileInfo userProfileInfo = new UserProfileInfo();
		RoleInfo roleInfo = roleRepo.findOne(userDTO.getRoleId());
		if(roleInfo != null){
			String username = userDTO.getUserName();
			String password = userDTO.getPassword();
			Name dn = LdapNameBuilder.newInstance().add("ou", "Users").add("cn", username).build();
			DirContextAdapter context = new DirContextAdapter(dn);

			context.setAttributeValues("objectclass",
					new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
			// context.setAttributeValue("ou","Groups123");
			context.setAttributeValue("cn", username);
			context.setAttributeValue("sn", username);
			// context.setAttributeValue("role","admin");
			context.setAttributeValue("userPassword", digestSHA(password));
			context.setAttributeValue("businessCategory", roleInfo.getRoleName().name());
			context.setAttributeValue("mail", userDTO.getMail().toLowerCase());
			ldapTemplate.bind(context);
			if (ldapTemplate != null) {

				userProfileInfo = userProfileRepository.findByUserName(username);
				if (userProfileInfo == null) {
					// fileService.uploadPictureAndSaveFileFromLocal(profilePictureFile, fileName)
					userProfileInfo = new UserProfileInfo();
					userProfileInfo.setUserName(username);
					userProfileInfo.setFirstName(userDTO.getFirstName());
					userProfileInfo.setLastName(userDTO.getLastName());
					userProfileInfo.setStatus(new Long(1));
					OrganizationInfo organizationInfo = organizationInfoRepository.findOne(userDTO.getOrganizationId());
					userProfileInfo.setOrganizationInfo(organizationInfo);
					userProfileInfo.setRoleId(roleInfo);

					UserProfileInfo savedUserProfile = userProfileRepository.save(userProfileInfo);
					DataModifiedHistoryInfo dataModifiedHistoryInfo = new DataModifiedHistoryInfo();

					dataModifiedHistoryInfo.setObjectId(new Long(2));
					dataModifiedHistoryInfo.setObjectStatus("dfgfd");
					dataModifiedHistoryInfo.setModifiedBy(new Long(3));
					DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					String requiredDate = df.format(new Date());
					dataModifiedHistoryInfo.setDate(requiredDate);
					if (dataModifiedHistoryInfo != null) {
						dataModifiedHistoryRepository.save(dataModifiedHistoryInfo);
					}
					if (savedUserProfile != null) {

						if (userDTO.getShipProfileIds() != null && userDTO.getShipProfileIds().length > 0) {
							for (Long shipId : userDTO.getShipProfileIds()) {
								ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
								if (shipProfileInfo != null) {
									if (savedUserProfile.getRoleId().getRoleName().equals(Role.ShipMaster)) {
										shipProfileInfo.setShipMaster(savedUserProfile);
										shipProfileRepository.saveAndFlush(shipProfileInfo);
									}
									if (savedUserProfile.getRoleId().getRoleName().equals(Role.TechManager)) {
										shipProfileInfo.getTechMasters().add(savedUserProfile);
										shipProfileRepository.saveAndFlush(shipProfileInfo);
									}
									if (savedUserProfile.getRoleId().getRoleName().equals(Role.CommercialManager)) {
										shipProfileInfo.getCommercialMasters().add(savedUserProfile);
										shipProfileRepository.saveAndFlush(shipProfileInfo);
									}
								}
							}
						}

						commonMethodsUtility.maintainHistory(savedUserProfile.getId(), savedUserProfile.getUserName(),
								"User", env.getProperty("history.created"), userDTO.getLoginId());
						if (profilePictureFile != null) {
							if (!profilePictureFile.isEmpty()) {
								String profilePicturePath = fileService.uploadUserPictureAndSaveFileFromLocal(
										profilePictureFile,
										env.getProperty("user.profile.picture.name") + savedUserProfile.getId());
								if (profilePicturePath != null) {
									savedUserProfile.setProfilePicturePath(profilePicturePath);
									userProfileRepository.saveAndFlush(savedUserProfile);
								}
							}
						}

					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean modify(UserDTO userDTO) {
		try {
			User user = userRepository.findByUsername(userDTO.getUserName());
			// user.setUsername(username);
			user.setPassword(digestSHA(userDTO.getPassword()));
			userRepository.save(user);
			LOGGER.info("modified successfully");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(UserDTO userDTO) {

		try {
			User user = userRepository.findByUsername(userDTO.getUserName());
			user.setBusinessCategory(userDTO.getBusinessCategory());
			user.setMail(userDTO.getMail());
			userRepository.save(user);
			LOGGER.info("updated successfully!!!!");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String digestSHA(final String password) {
		String base64;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(password.getBytes());
			base64 = Base64.getEncoder().encodeToString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return "{SHA}" + base64;
	}

	public boolean isSearchValid(String roll) {
		if (roll != null && StringUtils.isNotEmpty(roll)) {
			return true;
		}
		return false;
	}

	public boolean isValid(UserDTO userDTO) {
		if (userDTO.getUserName() != null && StringUtils.isNotEmpty(userDTO.getUserName())
				&& userDTO.getPassword() != null && StringUtils.isNotEmpty(userDTO.getPassword())) {
			return true;
		}
		return false;
	}

	public boolean isValidPassword(UserDTO userDTO) {
		if (userDTO.getUserName() != null && StringUtils.isNotEmpty(userDTO.getUserName())
				&& userDTO.getPassword() != null && StringUtils.isNotEmpty(userDTO.getPassword())
				&& userDTO.getCurrentPassword() != null && StringUtils.isNotEmpty(userDTO.getCurrentPassword())) {
			User user = userRepository.findByUsernameAndPassword(userDTO.getUserName(),
					digestSHA(userDTO.getCurrentPassword()));
			if (user != null) {
				return true;
			}
		}
		return false;
	}

	public Boolean isValidInput(UserDTO userDTO) {

		if (userDTO.getRoleId() != null && userDTO.getMail() != null && userDTO.getUserName() != null
				&& StringUtils.isNotBlank(userDTO.getRoleId().toString()) && StringUtils.isNotBlank(userDTO.getMail())
				&& StringUtils.isNotBlank(userDTO.getUserName())) {

			return true;

		} else {
			return false;
		}

	}

	public boolean createRequest(RequestUserDTO requestUserDTO) {
		RequestUserInfo requestUserInfo = new RequestUserInfo();
		requestUserInfo.setNumberOfUser(requestUserDTO.getNumberOfUser());
		requestUserInfo.setRemarks(requestUserDTO.getRemarks());
		requestUserInfo.setRequestUserStatus("Pending");
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(requestUserDTO.getShipId());
		UserProfileInfo uploadUserInfo = userProfileRepository.findOne(requestUserDTO.getUploadUserId());
		if (uploadUserInfo != null) {
			requestUserInfo.setOrganizationInfo(uploadUserInfo.getOrganizationInfo());
			requestUserInfo.setRequesterInfo(uploadUserInfo);
		} else {
			return false;
		}
		if (shipProfileInfo != null) {
			requestUserInfo.setUserRequestShipProfile(shipProfileInfo);
		} else {
			return false;
		}
		if (requestUserDTO.getNumberOfUser() > 0) {
			Set<UserProfileInfo> userProfileInfos = new HashSet<>();
			if (requestUserDTO.getUserList() != null && requestUserDTO.getUserList().size() > 0) {
				for (int i = 0; i < requestUserDTO.getUserList().size(); i++) {
					UserProfileInfo userProfileInfo = userProfileRepository
							.findByUserName(requestUserDTO.getUserList().get(i));
					userProfileInfos.add(userProfileInfo);
				}
				requestUserInfo.setRequestedUserList(userProfileInfos);
				requestUserInfo = requestUserRepository.saveAndFlush(requestUserInfo);
				if (requestUserInfo != null) {
					// commonMethodsUtility.maintainHistory(requestUserInfo.getId(),
					// requestUserInfo.getRemarks(), "RequestUser",
					// env.getProperty("history.created"), requestUserDTO.getLoginId());
				}
			} else {
				return false;
			}

			 boolean requestUserNotification =
			 notificationUtility.requestUserNotification(requestUserDTO.getUploadUserId(),
			 requestUserInfo.getId()); if (!requestUserNotification) { return false; }

			return true;
		}
		return false;
	}

	public List<RequestUserDTO> getPendingRequestList(Long userId) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
		if (userProfileInfo != null){
		List<RequestUserInfo> requestUserInfos = requestUserRepository.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
		if (requestUserInfos != null) {
			List<RequestUserDTO> requestUserDTOs = userUtils.convertRequestUserInfostoRequestUserDTOs(requestUserInfos);
			return requestUserDTOs;
		}
		}
		return null;
	}

	public boolean approvelUser(RequestUserDTO requestUserDTO) {

		if (requestUserDTO.getRequestUserStatus().equalsIgnoreCase("Rejected")) {
			RequestUserInfo requestUserInfo = requestUserRepository.findOne(requestUserDTO.getRequestId());
			if (requestUserInfo != null) {
				requestUserInfo.setRemarks(requestUserDTO.getRemarks());
				requestUserInfo.setRequestUserStatus(requestUserDTO.getRequestUserStatus());
				requestUserInfo = requestUserRepository.saveAndFlush(requestUserInfo);
				notificationUtility.requestUserNotificationChanges(requestUserDTO, requestUserInfo);
				if (requestUserInfo != null) {
					commonMethodsUtility.maintainHistory(requestUserInfo.getId(), requestUserInfo.getRemarks(),
							"RejectedUser", env.getProperty("history.approved"), requestUserDTO.getLoginId());

				}
				return true;
			}
		} else {
			RequestUserInfo requestUserInfo = requestUserRepository.findOne(requestUserDTO.getRequestId());
			if (requestUserInfo != null) {
				ShipProfileInfo shipProfileInfo = requestUserInfo.getUserRequestShipProfile();
				Set<UserProfileInfo> techManagerUsers = new HashSet<>();
				Set<UserProfileInfo> commercialUsers = new HashSet<>();
				if (shipProfileInfo != null) {
					if (requestUserInfo != null && requestUserInfo.getRequestedUserList() != null
							&& !requestUserInfo.getRequestedUserList().isEmpty()) {
						for (UserProfileInfo userProfileInfo : requestUserInfo.getRequestedUserList()) {
							if (userProfileInfo != null
									&& userProfileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
								techManagerUsers.add(userProfileInfo);
							} else if (userProfileInfo != null
									&& userProfileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
								commercialUsers.add(userProfileInfo);
							}
						}
						if (!techManagerUsers.isEmpty()) {
							// shipProfileInfo.setTechMasters(techManagerUsers);
							shipProfileInfo.getTechMasters().addAll(techManagerUsers);
						}
						if (!commercialUsers.isEmpty()) {
							shipProfileInfo.getCommercialMasters().addAll(commercialUsers);
							// shipProfileInfo.setCommercialMasters(commercialUsers);
						}
						requestUserInfo.setRemarks(requestUserDTO.getRemarks());
						shipProfileInfo = shipProfileRepository.saveAndFlush(shipProfileInfo);
						if (shipProfileInfo != null) {
							if (requestUserInfo != null) {
								requestUserInfo.setRequestUserStatus(requestUserDTO.getRequestUserStatus());
								requestUserInfo = requestUserRepository.saveAndFlush(requestUserInfo);
								notificationUtility.requestUserNotificationChanges(requestUserDTO, requestUserInfo);
								if (requestUserInfo != null) {
									commonMethodsUtility.maintainHistory(requestUserInfo.getId(),
											requestUserInfo.getRemarks(), "approvelUser",
											env.getProperty("history.approved"), requestUserDTO.getLoginId());
								}
								return true;
							}
						}

					}
				}

			}
			return false;
		}
		return false;
	}

	public List<UserDTO> getAllUser(Long userid) {
		// List<UserProfileInfo> users = userProfileRepository.findAll();
		List<UserProfileInfo> users = userProfileRepository.findByStatus(new Long(1));
		RoleInfo roleInfo = roleRepo.findByRoleName(Role.Admin);
		List<UserDTO> userDTOs = new ArrayList<>();
		for (UserProfileInfo user : users) {
			if (user.getRoleId() != roleInfo && user.getId() != userid) {
				UserDTO userDTO = new UserDTO();
				userDTO.setUserName(user.getUserName());
				userDTO.setUserId(user.getId());
				userDTOs.add(userDTO);
			}
		}
		return userDTOs;
	}

	public String generatePassword() {
		/*
		 * String result = " "; return result = RandomStringUtils.randomAlphanumeric(6);
		 */
		return new String(RandomStringUtils.randomAlphanumeric(6));
	}

	public boolean userPassword(String mail, String forgetPassword) {
		List<User> userList = userRepository.findByMail(mail);
		if (userList != null && userList.size() > 0) {
			for (User user : userList) {
				List<User> userInfo = userRepository.findByMail(user.getMail());
				if (userInfo != null && userInfo.size() > 0) {
					for (User mailid : userInfo) {
						mailid.setPassword(digestSHA(forgetPassword));
						userRepository.save(mailid);
						LOGGER.info("modified successfully");
						boolean isEmailSent = emailService.forgotPasswordNotificationEmail(mailid.getMail(),
								mailid.getUsername(), forgetPassword);
						if (isEmailSent) {
							return true;
						}
					}
				}

			}
		}

		return false;
	}

	public boolean isValidcreate(UserDTO userDTO) {
		if (userDTO.getUserName() != null && StringUtils.isNotEmpty(userDTO.getUserName())
				&& userDTO.getPassword() != null && StringUtils.isNotEmpty(userDTO.getPassword())
				&& userDTO.getMail() != null && StringUtils.isNotEmpty(userDTO.getMail())) {
			return true;
		}
		return false;
	}

	public List<UserDTO> searchProfile(Long userId) {
		List<UserDTO> userDTOs = new ArrayList<>();
		UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
		if (userProfileInfo != null) {
			User userDetails = userRepository.findByUsername(userProfileInfo.getUserName());
			if (userDetails == null) {
				return Collections.emptyList();
			} else {
				UserDTO userDTO = new UserDTO();
				userDTO = userUtils.converUsertoUserDTO(userDetails);
				userDTOs.add(userDTO);
			}
			LOGGER.info("userSize:::" + userDTOs.toString());
			return userDTOs;

		}
		return null;
	}

	public boolean userparam(UserDTO userDTO) {
		if (userDTO.getAdminId() != null && StringUtils.isNotEmpty(userDTO.getAdminId().toString())) {
			return true;
		}
		return false;
	}

	public List<UserDTO> getAdminAllUser(Long adminId) {
		// UserProfileInfo userProfileInfo = userProfileRepository.findById(adminId);
		return null;

		// List<UserProfileInfo> users = userProfileRepository.findByAdminInfo();
		// List<UserDTO> userDTOs = new ArrayList<>();
		// for (UserProfileInfo user : users) {
		// UserDTO userDTO = new UserDTO();
		// userDTO.setUserName(user.getUserName());
		// userDTO.setUserId(user.getId());
		// userDTO.setStatus(user.getStatus().longValue());
		// userDTOs.add(userDTO);
		// }
		// return userDTOs;
	}

	public List<UserDTO> getOrganizationUser(Long adminId) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(adminId);
		List<UserProfileInfo> users = userProfileRepository
				.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
		List<UserDTO> userDTOs = new ArrayList<>();
		for (UserProfileInfo user : users) {
			if (!user.getRoleId().getRoleName().equals(Role.SuperAdmin) && !user.getRoleId().getRoleName().equals(Role.Admin)) {
				User userDetails = userRepository.findByUsername(user.getUserName());
				UserDTO userDTO = new UserDTO();
				if (userDetails != null){
				userDTO = userUtils.converUsertoUserDTO(userDetails);
				userDTOs.add(userDTO);
				}
			}
		}
		return userDTOs;
	}

	public List<UserProfileInfo> activatealluser(Long adminid) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(adminid);
		List<UserProfileInfo> users = userProfileRepository
				.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
		List<UserProfileInfo> userProfileInfos = new ArrayList<>();
		if (users != null && users.size() > 0) {
			for (UserProfileInfo user : users) {
				user.setStatus(new Long(1));
				userProfileInfos.add(user);
				// users.save(userProfileInfo);
			}
		}
		return userProfileInfos;
	}

	public List<UserProfileInfo> deactivatealluser(Long adminid) {
		UserProfileInfo userProfileInfo = userProfileRepository.findById(adminid);
		List<UserProfileInfo> users = userProfileRepository
				.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
		List<UserProfileInfo> userProfileInfos = new ArrayList<>();
		if (users != null && users.size() > 0) {
			for (UserProfileInfo user : users) {
				user.setStatus(new Long(0));
				userProfileInfos.add(user);
				// users.save(userProfileInfo);
			}
		}
		return userProfileInfos;
	}

	public boolean isExistingUser(Long userId) {

		UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
		if (userProfileInfo != null) {
			return true;
		}
		return false;

	}

	public String isvailduserdeleteparam(UserDTO userDTO) {
		if (userDTO.getUserId() == null) {
			return "User ID should not be null";
		}
		if (userDTO.getLoginId() == null) {
			return "Login ID should not be null";
		}
		return env.getProperty("success");
	}

	public boolean delete(UserDTO userDTO) {
		if(userDTO.getUserIds().length > 0){

		}
		UserProfileInfo userProfileInfo = userProfileRepository.findById(userDTO.getUserId());
		User userDetails = userRepository.findByUsername(userProfileInfo.getUserName());
		if (userProfileInfo != null && userDetails != null) {
			if(userProfileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(userProfileInfo);
				if(shipProfileInfo != null) {
					shipProfileInfo.setShipMaster(null);
					shipProfileRepository.saveAndFlush(shipProfileInfo);
				}
			}
			if(userProfileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
				List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByShipOrganizationInfoAndTechMasters(userProfileInfo.getOrganizationInfo(), userProfileInfo);
				if(shipProfileInfos != null && shipProfileInfos.size() > 0) {
					for(ShipProfileInfo shipProfileInfo : shipProfileInfos) {
						for(UserProfileInfo profileInfo : shipProfileInfo.getTechMasters()) {
							if(userProfileInfo.equals(profileInfo)) {
								shipProfileInfo.getTechMasters().remove(profileInfo);
								shipProfileRepository.saveAndFlush(shipProfileInfo);
							}
						}
					}
				}
			}
			if(userProfileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
				List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByShipOrganizationInfoAndCommercialMasters(userProfileInfo.getOrganizationInfo(), userProfileInfo);
				if(shipProfileInfos != null && shipProfileInfos.size() > 0) {
					for(ShipProfileInfo shipProfileInfo : shipProfileInfos) {
						for(UserProfileInfo profileInfo : shipProfileInfo.getCommercialMasters()) {
							if(userProfileInfo.equals(profileInfo)) {
								shipProfileInfo.getCommercialMasters().remove(profileInfo);
								shipProfileRepository.saveAndFlush(shipProfileInfo);
							}
						}
					}
				}
			}
			List<RequestUserInfo> requestUserInfos =  requestUserRepository.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
			if(requestUserInfos != null && requestUserInfos.size() > 0){
				for(RequestUserInfo requestUserInfo : requestUserInfos){
					for(UserProfileInfo profileInfo : requestUserInfo.getRequestedUserList()){
						if(userProfileInfo.equals(profileInfo)) {
							requestUserInfo.getRequestedUserList().remove(profileInfo);
							requestUserRepository.saveAndFlush(requestUserInfo);
						}
					}
				}
			}
			userProfileRepository.delete(userProfileInfo);
			userRepository.delete(userDetails);
			return true;
		}
		return false;
	}

	public Boolean activatealluser(UserDTO userDTO) {
		for (Long userId : userDTO.getUserIds()) {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
			if (userProfileInfo != null) {
				userProfileInfo.setStatus(userDTO.getStatus().longValue());
				userProfileInfo = userProfileRepository.save(userProfileInfo);
			} else {
				return false;
			}
		}
		return true;
	}

	public UserDTO getListShipMasterAndCommericalManagerAndTechMangerBasedOrganization(Long userId) {
		UserDTO responseUserDTO = new UserDTO();
		if (userId != null) {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
			if (userProfileInfo != null) {
				List<UserProfileInfo> userProfileInfos = userProfileRepository
						.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
				if (userProfileInfos != null && userProfileInfos.size() > 0) {
					List<UserDTO> shipMasterDTOs = new ArrayList<>();
					List<UserDTO> technicalManagerDTOs = new ArrayList<>();
					List<UserDTO> commericalManagerDTOs = new ArrayList<>();
					for (UserProfileInfo profileInfo : userProfileInfos) {
						UserDTO userDTO = new UserDTO();
						if (profileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
							userDTO.setUserId(profileInfo.getId());
							RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
							if(roleAliasInfo != null){
								userDTO.setRole(roleAliasInfo.getRoleAliasName());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}else {
								userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}
							userDTO.setFirstName(profileInfo.getFirstName());
							userDTO.setLastName(profileInfo.getLastName());
							userDTO.setStatus(profileInfo.getStatus().longValue());
							userDTO.setUserName(profileInfo.getUserName());
							shipMasterDTOs.add(userDTO);
						}

						if (profileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
							userDTO.setUserId(profileInfo.getId());
							RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
							if(roleAliasInfo != null){
								userDTO.setRole(roleAliasInfo.getRoleAliasName());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}else {
								userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}
							userDTO.setFirstName(profileInfo.getFirstName());
							userDTO.setLastName(profileInfo.getLastName());
							userDTO.setStatus(profileInfo.getStatus().longValue());
							userDTO.setUserName(profileInfo.getUserName());
							commericalManagerDTOs.add(userDTO);
						}

						if (profileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
							userDTO.setUserId(profileInfo.getId());
							RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
							if(roleAliasInfo != null){
								userDTO.setRole(roleAliasInfo.getRoleAliasName());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}else {
								userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}
							userDTO.setFirstName(profileInfo.getFirstName());
							userDTO.setLastName(profileInfo.getLastName());
							userDTO.setStatus(profileInfo.getStatus().longValue());
							userDTO.setUserName(profileInfo.getUserName());
							technicalManagerDTOs.add(userDTO);
						}

					}
					responseUserDTO.setShipMasterInfos(shipMasterDTOs);
					responseUserDTO.setTechnicalManagerInfos(technicalManagerDTOs);
					responseUserDTO.setCommercialManagerInfos(commericalManagerDTOs);
					return responseUserDTO;
				}
			}
		}

		return null;
	}

	public List<UserDTO> listAllUserBasedOnOrganization(Long userId) {
		List<UserDTO> userDTOs = new ArrayList<>();
		if (userId != null) {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
			if (userProfileInfo != null) {
				List<UserProfileInfo> userProfileInfos = userProfileRepository
						.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
				for (UserProfileInfo profileInfo : userProfileInfos) {
					UserDTO userDTO = new UserDTO();
					userDTO.setUserId(profileInfo.getId());
					RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
					if(roleAliasInfo != null){
						userDTO.setRole(roleAliasInfo.getRoleAliasName());
						userDTO.setRoleId(profileInfo.getRoleId().getId());
					}else {
						userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
						userDTO.setRoleId(profileInfo.getRoleId().getId());
					}
					userDTO.setFirstName(profileInfo.getFirstName());
					userDTO.setLastName(profileInfo.getLastName());
					userDTO.setStatus(profileInfo.getStatus().longValue());
					userDTO.setUserName(profileInfo.getUserName());
					User userInfo = userRepository.findByUsername(profileInfo.getUserName());
					if(userInfo!=null) {
						userDTO.setMail(userInfo.getMail());
					}
					userDTOs.add(userDTO);
				}
				return userDTOs;
			}
		}
		return null;
	}

	public List<UserDTO> listShipMasterUsersBasedOrganization(Long userId) {
		List<UserDTO> shipMasterDTOs = new ArrayList<>();
		if (userId != null) {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
			if (userProfileInfo != null) {
				List<UserProfileInfo> userProfileInfos = userProfileRepository
						.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
				if (userProfileInfos != null && userProfileInfos.size() > 0) {
					for (UserProfileInfo profileInfo : userProfileInfos) {
						UserDTO userDTO = new UserDTO();
						if (profileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
							if(shipProfileRepository.findByShipMaster(profileInfo) == null){
								User userInfo = userRepository.findByUsername(profileInfo.getUserName());
								if (userInfo != null) {
									userDTO.setMail(userInfo.getMail());
								}
								userDTO.setUserId(profileInfo.getId());
								RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
								if(roleAliasInfo != null){
									userDTO.setRole(roleAliasInfo.getRoleAliasName());
									userDTO.setRoleId(profileInfo.getRoleId().getId());
								}else {
									userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
									userDTO.setRoleId(profileInfo.getRoleId().getId());
								}
								userDTO.setFirstName(profileInfo.getFirstName());
								userDTO.setLastName(profileInfo.getLastName());
								userDTO.setStatus(profileInfo.getStatus().longValue());
								userDTO.setUserName(profileInfo.getUserName());
								shipMasterDTOs.add(userDTO);
							}
						}
					}
					return shipMasterDTOs;
				}
			}
		}
		return null;
	}

	public List<UserDTO> listCommercialManagerUsersBasedOrganization(Long userId) {
		List<UserDTO> commericalManagerDTOs = new ArrayList<>();
		if (userId != null) {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
			if (userProfileInfo != null) {
				List<UserProfileInfo> userProfileInfos = userProfileRepository
						.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
				if (userProfileInfos != null && userProfileInfos.size() > 0) {
					for (UserProfileInfo profileInfo : userProfileInfos) {
						UserDTO userDTO = new UserDTO();
						if (profileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
							User userInfo = userRepository.findByUsername(profileInfo.getUserName());
							if (userInfo != null) {
								userDTO.setMail(userInfo.getMail());
							}
							userDTO.setUserId(profileInfo.getId());
							RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
							if(roleAliasInfo != null){
								userDTO.setRole(roleAliasInfo.getRoleAliasName());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}else {
								userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}
							userDTO.setFirstName(profileInfo.getFirstName());
							userDTO.setLastName(profileInfo.getLastName());
							userDTO.setStatus(profileInfo.getStatus().longValue());
							userDTO.setUserName(profileInfo.getUserName());
							commericalManagerDTOs.add(userDTO);
						}
					}
					return commericalManagerDTOs;
				}
			}
		}
		return null;
	}

	public List<UserDTO> listTechnicalManagerUsersBasedOrganization(Long userId) {
		List<UserDTO> technicalManagerDTOs = new ArrayList<>();
		if (userId != null) {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
			if (userProfileInfo != null) {
				List<UserProfileInfo> userProfileInfos = userProfileRepository
						.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
				if (userProfileInfos != null && userProfileInfos.size() > 0) {
					for (UserProfileInfo profileInfo : userProfileInfos) {
						UserDTO userDTO = new UserDTO();
						if (profileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
							User userInfo = userRepository.findByUsername(profileInfo.getUserName());
							if (userInfo != null) {
								userDTO.setMail(userInfo.getMail());
							}
							userDTO.setUserId(profileInfo.getId());
							RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(profileInfo.getRoleId(), profileInfo.getOrganizationInfo());
							if(roleAliasInfo != null){
								userDTO.setRole(roleAliasInfo.getRoleAliasName());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}else {
								userDTO.setRole(profileInfo.getRoleId().getRoleName().name());
								userDTO.setRoleId(profileInfo.getRoleId().getId());
							}
							userDTO.setFirstName(profileInfo.getFirstName());
							userDTO.setLastName(profileInfo.getLastName());
							userDTO.setStatus(profileInfo.getStatus().longValue());
							userDTO.setUserName(profileInfo.getUserName());
							technicalManagerDTOs.add(userDTO);
						}
					}
					return technicalManagerDTOs;
				}
			}
		}
		return null;
	}

	public UserProfileInfo isUserProfileInfoExists(Long userId) {
		if (userId != null) {
			return userProfileRepository.findOne(userId);
		}

		return null;
	}

	public String updateUserProfileInfo(UserDTO userDTO, MultipartFile profilePictureFile) {
		if (userDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (userDTO.getFirstName() == null && StringUtils.isEmpty(userDTO.getFirstName())
				&& StringUtils.isBlank(userDTO.getFirstName()))
			return env.getProperty("user.first.name.empty");

		if (userDTO.getLastName() == null && StringUtils.isEmpty(userDTO.getLastName())
				&& StringUtils.isBlank(userDTO.getLastName()))
			return env.getProperty("user.last.name.empty");

		if (userDTO.getMail() == null && StringUtils.isEmpty(userDTO.getMail())
				&& StringUtils.isBlank(userDTO.getMail()))
			return env.getProperty("user.emai.empty");

		if (!userDTO.getFirstName().matches(ONLY_LETTERS))
			return env.getProperty("user.first.name.special.characters");

		if (!userDTO.getLastName().matches(ONLY_LETTERS))
			return env.getProperty("user.last.name.special.characters");

		if (userDTO.getMail() != null) {
			Pattern pattern = Pattern.compile(EMAIL_REGEX);
			Matcher matcher = pattern.matcher(userDTO.getMail());
			if (!matcher.matches())
				return env.getProperty("user.email.not.valid");
		}

		UserProfileInfo profileInfo = userProfileRepository.findOne(userDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		User user = userRepository.findUserByMail(userDTO.getMail());
		if (user != null) {
			if (!user.getUsername().equalsIgnoreCase(profileInfo.getUserName()))
				return env.getProperty("user.email.already.exist");
		}

		for (Long shipId : userDTO.getShipProfileIds()) {
			ShipProfileInfo shipInfo = shipProfileRepository.findOne(shipId);
			if (shipInfo == null) {
				return env.getProperty("ship.not.found");
			}
		}

		if (userDTO.getShipProfileIds() != null && userDTO.getShipProfileIds().length > 0) {
			if (profileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				if (userDTO.getShipProfileIds().length > 1)
					return env.getProperty("user.update.ship.master.not.allowed");
			}
		}

		if (userDTO.getShipProfileIds() != null && userDTO.getShipProfileIds().length > 0) {
			if (profileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(profileInfo);
				ShipProfileInfo shipProfile = shipProfileRepository.findOne(userDTO.getShipProfileIds()[0]);
				if (shipProfile != null) {
					if (shipProfileInfo != null) {
						if (!shipProfileInfo.equals(shipProfile)) {
							shipProfileInfo.setShipMaster(null);
							shipProfileRepository.saveAndFlush(shipProfileInfo);
							shipProfile.setShipMaster(profileInfo);
							shipProfileRepository.saveAndFlush(shipProfile);
						}
					} else {
						shipProfile.setShipMaster(profileInfo);
						shipProfileRepository.saveAndFlush(shipProfile);
					}
				}
			}

			if (profileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
				List<ShipProfileInfo> techManagerShipProfileInfos = shipProfileRepository.findByStatusAndTechMasters(1,
						profileInfo);
				if (techManagerShipProfileInfos != null && techManagerShipProfileInfos.size() > 0) {
					for (ShipProfileInfo shipProfile : techManagerShipProfileInfos) {
						shipProfile.getTechMasters().removeAll(shipProfile.getTechMasters());
						shipProfileRepository.saveAndFlush(shipProfile);
					}
				}
				for (Long shipId : userDTO.getShipProfileIds()) {
					ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
					if (shipProfileInfo != null) {
						shipProfileInfo.getTechMasters().add(profileInfo);
						shipProfileRepository.saveAndFlush(shipProfileInfo);
					}
				}
			}

			if (profileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
				List<ShipProfileInfo> commercialManagerShipProfileInfos = shipProfileRepository
						.findByStatusAndCommercialMasters(1, profileInfo);
				if (commercialManagerShipProfileInfos != null && commercialManagerShipProfileInfos.size() > 0) {
					for (ShipProfileInfo shipProfile : commercialManagerShipProfileInfos) {
						shipProfile.getCommercialMasters().removeAll(shipProfile.getCommercialMasters());
						shipProfileRepository.saveAndFlush(shipProfile);
					}
				}
				for (Long shipId : userDTO.getShipProfileIds()) {
					ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
					if (shipProfileInfo != null) {
						shipProfileInfo.getCommercialMasters().add(profileInfo);
						shipProfileRepository.saveAndFlush(shipProfileInfo);
					}
				}
			}
		} else {
			if (profileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(profileInfo);
				if (shipProfileInfo != null) {
					shipProfileInfo.setShipMaster(null);
					shipProfileRepository.saveAndFlush(shipProfileInfo);
				}
			}

			if (profileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
				List<ShipProfileInfo> techManagerShipProfileInfos = shipProfileRepository.findByStatusAndTechMasters(1,
						profileInfo);
				if (techManagerShipProfileInfos != null && techManagerShipProfileInfos.size() > 0) {
					for (ShipProfileInfo shipProfile : techManagerShipProfileInfos) {
						shipProfile.getTechMasters().removeAll(shipProfile.getTechMasters());
						shipProfileRepository.saveAndFlush(shipProfile);
					}
				}
			}

			if (profileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
				List<ShipProfileInfo> commercialManagerShipProfileInfos = shipProfileRepository
						.findByStatusAndCommercialMasters(1, profileInfo);
				if (commercialManagerShipProfileInfos != null && commercialManagerShipProfileInfos.size() > 0) {
					for (ShipProfileInfo shipProfile : commercialManagerShipProfileInfos) {
						shipProfile.getCommercialMasters().removeAll(shipProfile.getCommercialMasters());
						shipProfileRepository.saveAndFlush(shipProfile);
					}
				}
			}
		}

		if (profilePictureFile != null) {
			if (!profilePictureFile.isEmpty()) {
				String profilePicturePath = fileService.uploadUserPictureAndSaveFileFromLocal(profilePictureFile,
						env.getProperty("user.profile.picture.name") + profileInfo.getId());
				if (profilePicturePath != null) {
					profileInfo.setProfilePicturePath(profilePicturePath);
				}
			}
		}

		profileInfo.setFirstName(userDTO.getFirstName());
		profileInfo.setLastName(userDTO.getLastName());
		User userInfo = userRepository.findByUsername(profileInfo.getUserName());
		if (userInfo != null) {
			userInfo.setMail(userDTO.getMail().toLowerCase());
			userRepository.save(userInfo);
		}

		if (userProfileRepository.saveAndFlush(profileInfo) != null) {
			return env.getProperty("success");
		}

		return null;
	}

	public String updateAdminProfileInformation(UserDTO userDTO, MultipartFile profilePictureFile) {
		if(userDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (userDTO.getFirstName() == null
				&& StringUtils.isEmpty(userDTO.getFirstName())
				&& StringUtils.isBlank(userDTO.getFirstName()))
			return env.getProperty("user.first.name.empty");

		if (userDTO.getLastName() == null
				&& StringUtils.isEmpty(userDTO.getLastName())
				&& StringUtils.isBlank(userDTO.getLastName()))
			return env.getProperty("user.last.name.empty");

		if (userDTO.getMail() == null
				&& StringUtils.isEmpty(userDTO.getMail())
				&& StringUtils.isBlank(userDTO.getMail()))
			return env.getProperty("user.email.empty");

		if (userDTO.getMail() != null) {
			Pattern pattern = Pattern.compile(EMAIL_REGEX);
			Matcher matcher = pattern.matcher(userDTO.getMail());
			if (!matcher.matches())
				return env.getProperty("user.email.not.valid");
		}

		User user = userRepository.findUserByMail(userDTO.getMail().toLowerCase());
		if(user != null){
			if(!profileInfo.getUserName().equalsIgnoreCase(user.getUsername())){
				return env.getProperty("user.email.already.exist");
			}
		}

		User userInfo = userRepository.findByUsername(profileInfo.getUserName());
//		if(userInfo != null){
//			userInfo.setMail(userDTO.getMail().toLowerCase());
//			userRepository.save(userInfo);
//		}
//

		if (profilePictureFile != null) {
			if (!profilePictureFile.isEmpty()) {
				String profilePicturePath = fileService.uploadUserPictureAndSaveFileFromLocal(
						profilePictureFile,
						env.getProperty("user.profile.picture.name") + profileInfo.getId());
				if (profilePicturePath != null) {
					profileInfo.setProfilePicturePath(profilePicturePath);
				}
			}
		}

		profileInfo.setFirstName(userDTO.getFirstName());
		profileInfo.setLastName(userDTO.getLastName());


		if (userProfileRepository.saveAndFlush(profileInfo) != null) {
			return env.getProperty("success");
		}

		return null;
	}

	public String deleteAdministratorProfileInfo(Long superAdminId, Long adminId) {
		if (superAdminId == null)
			return env.getProperty("super.admin.id.null");

		if(adminId == null)
			return env.getProperty("user.id.null");

		UserProfileInfo userProfileInfo = userProfileRepository.findOne(superAdminId);
		if (userProfileInfo == null)
			return env.getProperty("super.admin.not.found");

		if (!userProfileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("super.admin.only.delete.admin");

		UserProfileInfo profileInfo = userProfileRepository.findOne(adminId);
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		User user = userRepository.findByUsername(profileInfo.getUserName());
		if(user != null){
			userRepository.delete(user);
		}
		userProfileRepository.delete(profileInfo);
		return env.getProperty("success");

	}

	public String resetAdministratorProfilePassword(UserDTO userDTO) {
		if (userDTO.getAdminId() == null)
			return env.getProperty("super.admin.id.null");

		if(userDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		UserProfileInfo userProfileInfo = userProfileRepository.findOne(userDTO.getAdminId());
		if (userProfileInfo == null)
			return env.getProperty("super.admin.not.found");

		if (!userProfileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("super.admin.only.rest.admin.password");

		if(userDTO.getPassword() == null &&StringUtils.isBlank(userDTO.getPassword()) &&StringUtils.isEmpty(userDTO.getPassword()))
			return env.getProperty("admin.password.empty");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		User user = userRepository.findByUsername(profileInfo.getUserName());
		if(user != null){
			user.setPassword(digestSHA(userDTO.getPassword()));
			userRepository.save(user);
			return env.getProperty("success");
		}
		return null;
	}

	public String updateUserProfilePassword(UserDTO userDTO) {
		if(userDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if(userDTO.getCurrentPassword() == null)
			return env.getProperty("user.current.password.null");

		if(userDTO.getPassword() == null)
			return env.getProperty("user.password.null");

		if(userDTO.getConfirmPassword() == null)
			return env.getProperty("user.confirm.password.null");

		if(!userDTO.getPassword().equalsIgnoreCase(userDTO.getConfirmPassword()))
			return env.getProperty("user.confirm.password.not.matching");

		User user = userRepository.findByUsernameAndPassword(profileInfo.getUserName(), digestSHA(userDTO.getCurrentPassword()));
		if(user != null){
			user.setPassword(digestSHA(userDTO.getPassword()));
			userRepository.save(user);
			return env.getProperty("success");
		}else {
			return env.getProperty("user.password.not.matching");
		}
	}

	public GeoLocationDTO createGeoLocartion(GeoLocationDTO geoLocationDTO) {

		UserProfileInfo userProfileInfo = userProfileRepository.findById(geoLocationDTO.getUserId());
		GeoLocationInfo geoLocationInfo = geoLocationRepository.findByShipId(geoLocationDTO.getShipId());
		if (geoLocationInfo != null){
			geoLocationInfo.setLatitude(geoLocationDTO.getLatitude());
			geoLocationInfo.setLongitute(geoLocationDTO.getLongitute());
			geoLocationInfo.setUpdateDate(new Date());
			geoLocationInfo = geoLocationRepository.saveAndFlush(geoLocationInfo);

			if (geoLocationInfo != null)
			{
				notificationUtility.notificationToAddGeoLocation(userProfileInfo);
				geoLocationDTO = userUtils.convertGeoLocationInfoToGeoLocationDTO(geoLocationInfo);
				return geoLocationDTO;
			}
		}else{
				 geoLocationInfo = new GeoLocationInfo();
				 geoLocationInfo.setLatitude(geoLocationDTO.getLatitude());
				 geoLocationInfo.setLongitute(geoLocationDTO.getLongitute());
				 geoLocationInfo.setShipId(geoLocationDTO.getShipId());
				 geoLocationInfo.setUpdateDate(new Date());
				 geoLocationInfo = geoLocationRepository.saveAndFlush(geoLocationInfo);
					if (geoLocationInfo != null)
					{
						notificationUtility.notificationToAddGeoLocation(userProfileInfo);
						geoLocationDTO = userUtils.convertGeoLocationInfoToGeoLocationDTO(geoLocationInfo);
						return geoLocationDTO;
					}

			}
		return null;
	}

	public List<UserDTO> getGeoLocations(Long userId) {

		UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
		OrganizationInfo organizationInfo = organizationInfoRepository
				.findOne(userProfileInfo.getOrganizationInfo().getId());
		List<ShipProfileInfo> shipProfileInfos = null;
		List<UserDTO> userDTOs = new ArrayList<>();
		if (userProfileInfo != null){
			Role roleName = userProfileInfo.getRoleId().getRoleName();
			if (roleName.equals(Role.TechManager)) {
				shipProfileInfos = shipProfileRepository.findByTechMasters_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					userDTOs = userUtils.convertShipInfosToGeoLocation(shipProfileInfos);
					return userDTOs;
				}
			} else if (roleName.equals(Role.ShipMaster)) {
				shipProfileInfos = shipProfileRepository.findByShipMaster_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					userDTOs = userUtils.convertShipInfosToGeoLocation(shipProfileInfos);
					return userDTOs;

				}
			} else if (roleName.equals(Role.CommercialManager)) {
				shipProfileInfos = shipProfileRepository
						.findByCommercialMasters_UserNameAndStatusAndShipOrganizationInfo(userProfileInfo.getUserName(),
								1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					userDTOs = userUtils.convertShipInfosToGeoLocation(shipProfileInfos);
					return userDTOs;
				}
			} else if (roleName.equals(Role.Admin)) {
				shipProfileInfos = shipProfileRepository.findByStatusAndShipOrganizationInfo(1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					userDTOs = userUtils.convertShipInfosToGeoLocation(shipProfileInfos);
					return userDTOs;
				}

			}


		}

		return null;
	}

	public String deleteUserInformation(UserDTO userDTO) {
		List<UserProfileInfo> userProfileInfos = new ArrayList<>();
		List<User> users = new ArrayList<>();
		if(userDTO.getUserIds().length == 0)
			return env.getProperty("user.id.null");

		if(userDTO.getLoginId() == null)
			return env.getProperty("admin.id.null");

		UserProfileInfo user = userProfileRepository.findOne(userDTO.getLoginId());
		if(user == null)
			return env.getProperty("user.admin.only.delete");

		if(user != null){
			if(!user.getRoleId().getRoleName().equals(Role.Admin))
				return env.getProperty("user.admin.only.delete");
		}

		if(userDTO.getUserIds().length > 0){
			for(Long userId : userDTO.getUserIds()){

				UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
				User userDetails = userRepository.findByUsername(userProfileInfo.getUserName());
				if (userProfileInfo != null && userDetails != null) {
					if(userProfileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
						ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(userProfileInfo);
						if(shipProfileInfo != null) {
							shipProfileInfo.setShipMaster(null);
							shipProfileRepository.saveAndFlush(shipProfileInfo);
						}
					}
					if(userProfileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
						List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByShipOrganizationInfoAndTechMasters(userProfileInfo.getOrganizationInfo(), userProfileInfo);
						if(shipProfileInfos != null && shipProfileInfos.size() > 0) {
							for(ShipProfileInfo shipProfileInfo : shipProfileInfos) {
								for(UserProfileInfo profileInfo : shipProfileInfo.getTechMasters()) {
									if(userProfileInfo.equals(profileInfo)) {
										shipProfileInfo.getTechMasters().remove(profileInfo);
										shipProfileRepository.saveAndFlush(shipProfileInfo);
									}
								}
							}
						}
					}
					if(userProfileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
						List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByShipOrganizationInfoAndCommercialMasters(userProfileInfo.getOrganizationInfo(), userProfileInfo);
						if(shipProfileInfos != null && shipProfileInfos.size() > 0) {
							for(ShipProfileInfo shipProfileInfo : shipProfileInfos) {
								for(UserProfileInfo profileInfo : shipProfileInfo.getCommercialMasters()) {
									if(userProfileInfo.equals(profileInfo)) {
										shipProfileInfo.getCommercialMasters().remove(profileInfo);
										shipProfileRepository.saveAndFlush(shipProfileInfo);
									}
								}
							}
						}
					}
					List<RequestUserInfo> requestUserInfos =  requestUserRepository.findByOrganizationInfo(userProfileInfo.getOrganizationInfo());
					if(requestUserInfos != null && requestUserInfos.size() > 0){
						for(RequestUserInfo requestUserInfo : requestUserInfos){
							for(UserProfileInfo profileInfo : requestUserInfo.getRequestedUserList()){
								if(userProfileInfo.equals(profileInfo)) {
									requestUserInfo.getRequestedUserList().remove(profileInfo);
									requestUserRepository.saveAndFlush(requestUserInfo);
								}
							}
						}
					}

					Set<ExpiryDocumentInfo> expiryDocumentInfos = userProfileInfo.getUploadedExpiryDocuments();
					if(expiryDocumentInfos != null && expiryDocumentInfos.size() > 0) {
						for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfos) {
							expiryDocumentInfo.setUploadedBy(null);
							expiryDocumentRepository.save(expiryDocumentInfo);
						}
					}

					Set<ExpiryDocumentInfo> approvedExpiryDocumentInfos = userProfileInfo.getApprovedExpiryDocuments();
					if(approvedExpiryDocumentInfos != null && approvedExpiryDocumentInfos.size() > 0) {
						for (ExpiryDocumentInfo expiryDocumentInfo : approvedExpiryDocumentInfos) {
							expiryDocumentInfo.setApprovedBy(null);
							expiryDocumentRepository.save(expiryDocumentInfo);
						}
					}
					/*userProfileRepository.delete(userProfileInfo);
					userRepository.delete(userDetails);
					return null;*/
					userProfileInfos.add(userProfileInfo);
					users.add(userDetails);
				}
			}
			if(userProfileInfos != null && userProfileInfos.size() > 0){
				if(users != null && users.size() > 0){
					userRepository.delete(users);
				}
				userProfileRepository.delete(userProfileInfos);
				return env.getProperty("success");
			}

		}
		return env.getProperty("user.delete.error");
	}

	public User findUserByUsername(String username){
		return userRepository.findByUsername(username);
	}
}
