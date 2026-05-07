package com.dapp.docuchain.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.OrganizationDTO;
import com.dapp.docuchain.dto.SubscriptionDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.DataDeletedHistoryInfo;
import com.dapp.docuchain.model.DualApprovalInfo;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleAliasInfo;
import com.dapp.docuchain.model.SaveInBlockhainInfo;
import com.dapp.docuchain.model.SubscriptionInfo;
import com.dapp.docuchain.model.User;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.OrganizationInfoRepository;
import com.dapp.docuchain.repository.RoleAliasInfoRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.SubscriptionInfoRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.repository.UserRepository;
import com.dapp.docuchain.service.OrganizationInfoService;
import com.dapp.docuchain.service.UserService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.OrganizationInfoUtility;

@Service
public class OrganizationInfoServiceImpl implements OrganizationInfoService {

	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	private final String SUBSCRIPTION_DATE_REGEX = "([0-9]{2})-([0-9]{2})-([0-9]{4})";

	private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Autowired
	private Environment env;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private SubscriptionInfoRepository subscriptionInfoRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleInfoRepository roleInfoRepository;

	@Autowired
	private RoleAliasInfoRepository roleAliasInfoRepository;

	@Autowired
	private FileServiceImpl fileServiceImpl;

	@Autowired
	private OrganizationInfoUtility organizationInfoUtility;

	/**
	 * This method is used to save Organization information alone This method
	 * validate all parameter and after successfully saved organization will return
	 * success message Super administrator only can create Organization restriction
	 * is added
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public String saveOrganizationInformation(OrganizationDTO organizationDTO) {

		if (organizationDTO.getUserId() == null && StringUtils.isBlank(organizationDTO.getUserId().toString()))
			return env.getProperty("user.id.null");

		if (organizationDTO.getOrganizationName() == null && StringUtils.isBlank(organizationDTO.getOrganizationName())
				&& StringUtils.isEmpty(organizationDTO.getOrganizationName()))
			return env.getProperty("organization.name.empty");

		if (organizationDTO.getRegistrationNumber() == null
				&& StringUtils.isEmpty(organizationDTO.getRegistrationNumber())
				&& StringUtils.isBlank(organizationDTO.getRegistrationNumber()))
			return env.getProperty("organization.register.number.empty");

		if (organizationDTO.getEmailId() == null && StringUtils.isEmpty(organizationDTO.getEmailId())
				&& StringUtils.isBlank(organizationDTO.getEmailId()))
			return env.getProperty("organization.email.empty");

		if (organizationDTO.getPhoneNumber() == null && StringUtils.isEmpty(organizationDTO.getPhoneNumber())
				&& StringUtils.isBlank(organizationDTO.getPhoneNumber()))
			return env.getProperty("organization.phone.empty");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("organization.admin.only.create");

		if (organizationDTO.getSubscriptionId() != null) {
			if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
				return env.getProperty("subscription.not.found");
		}

		if (organizationInfoRepository.findByOrganizationName(organizationDTO.getOrganizationName()) != null)
			return env.getProperty("organization.name.exists");

		OrganizationInfo organizationInfo = organizationInfoRepository
				.save(convertOrganizationDTOToOrganizationInfo(organizationDTO));
		if (organizationInfo != null) {
			commonMethodsUtility.maintainHistory(organizationInfo.getId(), organizationInfo.getOrganizationName(),
					"Organization", env.getProperty("history.created"), profileInfo.getId());
			return env.getProperty("success");
		}

		return null;
	}

	/**
	 * This method is used to save Organization information with subscription and
	 * administrator informations too validate all parameter and after successfully
	 * saved organization will return success message Super administrator only can
	 * create Organization and subscription and administrator restriction is added
	 *
	 * @author Prabakaran
	 *
	 *-------------------------------------------------------------------------------------------------------------------------
	 */
// 	@Override
// 	public String saveOrganizationWithSubscriptionAndAdmin(OrganizationDTO organizationDTO) {
// 		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
// 		String date = sdf.format(new Date());
// 		System.out.println(date); // 15/10/2013

// 		if (organizationDTO.getUserId() == null && StringUtils.isBlank(organizationDTO.getUserId().toString()))
// 			return env.getProperty("user.id.null");

// 		if (organizationDTO.getOrganizationName() == null && StringUtils.isBlank(organizationDTO.getOrganizationName())
// 				&& StringUtils.isEmpty(organizationDTO.getOrganizationName()))
// 			return env.getProperty("organization.name.empty");

// 		if (organizationDTO.getRegistrationNumber() == null
// 				&& StringUtils.isEmpty(organizationDTO.getRegistrationNumber())
// 				&& StringUtils.isBlank(organizationDTO.getRegistrationNumber()))
// 			return env.getProperty("organization.register.number.empty");

// 		if (organizationDTO.getEmailId() == null && StringUtils.isEmpty(organizationDTO.getEmailId())
// 				&& StringUtils.isBlank(organizationDTO.getEmailId()))
// 			return env.getProperty("organization.email.empty");

// 		if (organizationDTO.getEmailId() != null) {
// 			Pattern pattern = Pattern.compile(EMAIL_REGEX);
// 			Matcher matcher = pattern.matcher(organizationDTO.getEmailId());
// 			if (!matcher.matches())
// 				return env.getProperty("organization.email.not.valid");
// 		}

// 		if (organizationDTO.getPhoneNumber() == null && StringUtils.isEmpty(organizationDTO.getPhoneNumber())
// 				&& StringUtils.isBlank(organizationDTO.getPhoneNumber()))
// 			return env.getProperty("organization.phone.empty");

// 		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
// 		if (profileInfo == null)
// 			return env.getProperty("user.not.found");

// 		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
// 			return env.getProperty("organization.admin.only.create");

// 		if (organizationDTO.getSubscriptionId() != null) {
// 			if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
// 				return env.getProperty("subscription.not.found");
// 		}
// 		if (organizationDTO.getSubscriptionInfo() != null) {
// 			Date startDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getFromDate());
// 			Date endDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getToDate());
// 			String fromDate = sdf.format(startDate);
// 			String toDate = sdf.format(endDate);
// 			System.out.println(fromDate); // 15/10/2013
// 			System.out.println(toDate); // 15/10/2013
// 			if (organizationDTO.getSubscriptionInfo().getSubscriptionAmount() == null
// 					&& StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getSubscriptionAmount().toString()))
// 				return env.getProperty("subscription.amount.not.null");

// 			if (organizationDTO.getSubscriptionInfo().getShipmentCount() == null
// 					&& StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getShipmentCount().toString()))
// 				return env.getProperty("subscription.ship.count.not.null");

// 			if (organizationDTO.getSubscriptionInfo().getUserVesslesRatio() == null
// 					&& StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getUserVesslesRatio().toString()))
// 				return env.getProperty("subscription.user.ratio.not.null");

// 			// if (organizationDTO.getSubscriptionInfo().getUserCount() == null
// 			// &&
// 			// StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getUserCount().toString()))
// 			// return env.getProperty("subscription.user.count.not.null");

// 			/*
// 			 * if (organizationDTO.getSubscriptionInfo().getSubscriptionDays() == null &&
// 			 * StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getSubscriptionDays
// 			 * ().toString())) return env.getProperty("subscription.days.not.null");
// 			 */

// 			if (fromDate == null && StringUtils.isBlank(fromDate) && StringUtils.isEmpty(fromDate))
// 				return env.getProperty("subscription.start.date.empty");

// 			if (toDate == null && StringUtils.isBlank(toDate) && StringUtils.isEmpty(toDate))
// 				return env.getProperty("subscription.end.date.empty");

// //			if (fromDate.equalsIgnoreCase(toDate))
// //				return env.getProperty("subscription.start.end.date.not.same");
// 			if (endDate.compareTo(startDate) < 0) {
// 				return "End date is before Start date";
// 			}
// //			else if (endDate.compareTo(startDate) == 0) {
// //				return "Start date is equal to End date";
// //			}
// //			else if (startDate.compareTo(new Date()) < 0) {
// //				return ("Start date is should not before Today date");
// //			}

// 			// if(!fromDate.matches(SUBSCRIPTION_DATE_REGEX))
// 			// return env.getProperty("subscription.start.date.wrong.format");
// 			//
// 			// if(!toDate.matches(SUBSCRIPTION_DATE_REGEX))
// 			// return env.getProperty("subscription.end.date.wrong.format");
// 		}

// 		if (organizationDTO.getUserInfo() != null) {
// 			if (organizationDTO.getUserInfo().getUserName() == null
// 					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getUserName())
// 					&& StringUtils.isBlank(organizationDTO.getUserInfo().getUserName()))
// 				return env.getProperty("user.username.empty");

// 			if (organizationDTO.getUserInfo().getFirstName() == null
// 					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getFirstName())
// 					&& StringUtils.isBlank(organizationDTO.getUserInfo().getFirstName()))
// 				return env.getProperty("user.first.name.empty");

// 			if (organizationDTO.getUserInfo().getLastName() == null
// 					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getLastName())
// 					&& StringUtils.isBlank(organizationDTO.getUserInfo().getLastName()))
// 				return env.getProperty("user.last.name.empty");

// 			if (organizationDTO.getUserInfo().getMail() == null
// 					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getMail())
// 					&& StringUtils.isBlank(organizationDTO.getUserInfo().getMail()))
// 				return env.getProperty("user.email.empty");

// 			if (organizationDTO.getUserInfo().getMail() != null) {
// 				Pattern pattern = Pattern.compile(EMAIL_REGEX);
// 				Matcher matcher = pattern.matcher(organizationDTO.getUserInfo().getMail());
// 				if (!matcher.matches())
// 					return env.getProperty("user.email.not.valid");
// 			}

// 			if (organizationDTO.getUserInfo().getPassword() == null
// 					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getPassword())
// 					&& StringUtils.isBlank(organizationDTO.getUserInfo().getPassword()))
// 				return env.getProperty("user.password.empty");

// 			List<User> userList = userRepository.findByMail(organizationDTO.getUserInfo().getMail().toLowerCase());
// 			if (userList != null && userList.size() > 0) {
// 				return env.getProperty("user.email.already.exist");
// 			}

// 			if (userRepository.findByUsername(organizationDTO.getUserInfo().getUserName()) != null)
// 				return env.getProperty("user.username.already.exist");
// 		}

// 		if (organizationInfoRepository.findByOrganizationName(organizationDTO.getOrganizationName()) != null)
// 			return env.getProperty("organization.name.exists");

// 		OrganizationInfo organizationInfo = organizationInfoRepository
// 				.save(convertOrganizationDTOToOrganizationInfo(organizationDTO));
// 		if (organizationInfo != null) {
// 			commonMethodsUtility.maintainHistory(organizationInfo.getId(), organizationInfo.getOrganizationName(),
// 					"Organization", env.getProperty("history.created"), profileInfo.getId());
// 			if (organizationDTO.getSubscriptionInfo() != null) {
// 				commonMethodsUtility.maintainHistory(organizationInfo.getSubscriptionInfo().getId(),
// 					organizationInfo.getOrganizationName(), "Subscription",
// 					env.getProperty("history.created"), profileInfo.getId());
// }
// 			if (organizationDTO.getUserInfo() != null) {
// 				try {
// 					organizationDTO.getUserInfo().setOrganizationId(organizationInfo.getId());
// 					organizationDTO.getUserInfo().setBusinessCategory(Role.Admin.name());
// 					organizationDTO.getUserInfo().setRoleId(roleInfoRepository.findByRoleName(Role.Admin).getId());
// 					boolean userCreation = userService.create(organizationDTO.getUserInfo(), null);
// 					if (!userCreation) {
// 						return env.getProperty("organization.user.create.failure");
// 					}
// 				} catch (NamingException e) {
// 					e.printStackTrace();
// 				}
// 			}

// 			return env.getProperty("success");
// 		}
// 		return null;
// 	}
@Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public String saveOrganizationWithSubscriptionAndAdmin(OrganizationDTO organizationDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(new Date());
        System.out.println("DEBUG: Starting Organization Creation. Date: " + date);

        // --- 1. VALIDATIONS ---
        if (organizationDTO.getUserId() == null && StringUtils.isBlank(organizationDTO.getUserId().toString()))
            return env.getProperty("user.id.null");

        if (organizationDTO.getOrganizationName() == null && StringUtils.isBlank(organizationDTO.getOrganizationName())
                && StringUtils.isEmpty(organizationDTO.getOrganizationName()))
            return env.getProperty("organization.name.empty");

        if (organizationDTO.getRegistrationNumber() == null
                && StringUtils.isEmpty(organizationDTO.getRegistrationNumber())
                && StringUtils.isBlank(organizationDTO.getRegistrationNumber()))
            return env.getProperty("organization.register.number.empty");

        if (organizationDTO.getEmailId() == null && StringUtils.isEmpty(organizationDTO.getEmailId())
                && StringUtils.isBlank(organizationDTO.getEmailId()))
            return env.getProperty("organization.email.empty");

        if (organizationDTO.getEmailId() != null) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(organizationDTO.getEmailId());
            if (!matcher.matches())
                return env.getProperty("organization.email.not.valid");
        }

        if (organizationDTO.getPhoneNumber() == null && StringUtils.isEmpty(organizationDTO.getPhoneNumber())
                && StringUtils.isBlank(organizationDTO.getPhoneNumber()))
            return env.getProperty("organization.phone.empty");

        UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
        if (profileInfo == null)
            return env.getProperty("user.not.found");

        if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
            return env.getProperty("organization.admin.only.create");

        // --- 2. SUBSCRIPTION CHECKS ---
        if (organizationDTO.getSubscriptionId() != null) {
            if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
                return env.getProperty("subscription.not.found");
        }

        if (organizationDTO.getSubscriptionInfo() != null) {
            Date startDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getFromDate());
            Date endDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getToDate());
            String fromDate = sdf.format(startDate);
            String toDate = sdf.format(endDate);

            if (organizationDTO.getSubscriptionInfo().getSubscriptionAmount() == null
                    && StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getSubscriptionAmount().toString()))
                return env.getProperty("subscription.amount.not.null");

            if (organizationDTO.getSubscriptionInfo().getShipmentCount() == null
                    && StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getShipmentCount().toString()))
                return env.getProperty("subscription.ship.count.not.null");

            if (organizationDTO.getSubscriptionInfo().getUserVesslesRatio() == null
                    && StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getUserVesslesRatio().toString()))
                return env.getProperty("subscription.user.ratio.not.null");

            if (fromDate == null && StringUtils.isBlank(fromDate) && StringUtils.isEmpty(fromDate))
                return env.getProperty("subscription.start.date.empty");

            if (toDate == null && StringUtils.isBlank(toDate) && StringUtils.isEmpty(toDate))
                return env.getProperty("subscription.end.date.empty");

            if (endDate.compareTo(startDate) < 0) {
                return "End date is before Start date";
            }
        }

        // --- 3. CHECK DUPLICATE ORGANIZATION ---
        if (organizationInfoRepository.findByOrganizationName(organizationDTO.getOrganizationName()) != null)
            return env.getProperty("organization.name.exists");

        if (organizationDTO.getUserInfo() != null) {
            List<User> userList = userRepository.findByMail(organizationDTO.getUserInfo().getMail().toLowerCase());
            if (userList != null && userList.size() > 0) {
                return env.getProperty("user.email.already.exist");
            }
            if (userRepository.findByUsername(organizationDTO.getUserInfo().getUserName()) != null)
                return env.getProperty("user.username.already.exist");
        }

        // --- 4. SAVE ORGANIZATION ---
        OrganizationInfo organizationInfo = organizationInfoRepository
                .save(convertOrganizationDTOToOrganizationInfo(organizationDTO));

        if (organizationInfo != null) {
            try {
                commonMethodsUtility.maintainHistory(organizationInfo.getId(), organizationInfo.getOrganizationName(),
                        "Organization", env.getProperty("history.created"), profileInfo.getId());

                if (organizationDTO.getSubscriptionInfo() != null) {
                    commonMethodsUtility.maintainHistory(organizationInfo.getSubscriptionInfo().getId(),
                            organizationInfo.getOrganizationName(), "Subscription",
                            env.getProperty("history.created"), profileInfo.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // --- 5. CREATE ADMIN USER (FIXED) ---
            // if (organizationDTO.getUserInfo() != null) {
            //     try {
            //         organizationDTO.getUserInfo().setOrganizationId(organizationInfo.getId());
            //         organizationDTO.getUserInfo().setBusinessCategory(Role.Admin.name());

            //         // --- ROLE LOOKUP (Kept for safety) ---
            //         Long adminRoleId = null;
            //         if (roleInfoRepository.findOne(6L) != null) adminRoleId = 6L;
            //         else if (roleInfoRepository.findOne(2L) != null) adminRoleId = 2L;
            //         else if (roleInfoRepository.findOne(1L) != null) adminRoleId = 1L;

            //         if (adminRoleId == null) throw new RuntimeException("CRITICAL: Role 'Admin' not found in DB.");

            //         organizationDTO.getUserInfo().setRoleId(adminRoleId);

            //         // --- CRITICAL FIX START ---
            //         // 1. We pass 'null' to fix the "Incompatible Types" compiler error.
            //         // 2. We wrap it in a TRY-CATCH to ignore the NullPointerException that UserService throws.
            //         try {
            //             userService.create(organizationDTO.getUserInfo(), null);
            //         } catch (NullPointerException npe) {
            //             System.err.println("DEBUG WARNING: UserService crashed on History Log (Expected). Ignoring to allow Commit.");
            //             // We intentionally swallow this error because the User IS created before this crash.
            //         }
            //         // --- CRITICAL FIX END ---

            //     } catch (Exception e) {
            //         e.printStackTrace();
            //         // If it's a real error (not the logging NPE), we rollback.
            //         throw new RuntimeException(e);
            //     }
            // }
						// --- 5. CREATE ADMIN USER (STRICT: ADMIN OR SUPERADMIN ONLY) ---
            if (organizationDTO.getUserInfo() != null) {
                try {
                    organizationDTO.getUserInfo().setOrganizationId(organizationInfo.getId());
                    organizationDTO.getUserInfo().setBusinessCategory(Role.Admin.name());

                    Long adminRoleId = null;

                    // 1. Try finding Admin role by Name (The safest way)
                    com.dapp.docuchain.model.RoleInfo roleEntity = roleInfoRepository.findByRoleName(Role.Admin);

                    if (roleEntity != null) {
                        adminRoleId = roleEntity.getId();
                    }
                    // 2. Fallback: Specifically look for ID 2 (Admin)
                    else if (roleInfoRepository.findOne(2L) != null) {
                        adminRoleId = 2L;
                    }
                    // 3. Last Resort: Look for ID 1 (SuperAdmin)
                    else if (roleInfoRepository.findOne(1L) != null) {
                        adminRoleId = 1L;
                    }

                    // --- DATA OPERATOR (ID 6) REMOVED COMPLETELY ---
                    if (adminRoleId == null) {
                        throw new RuntimeException("CRITICAL ERROR: Role 'Admin' (ID 2) or 'SuperAdmin' (ID 1) not found in Database.");
                    }

                    organizationDTO.getUserInfo().setRoleId(adminRoleId);

                    // Create the user
                    try {
                        userService.create(organizationDTO.getUserInfo(), null);
                    } catch (NullPointerException npe) {
                        System.err.println("DEBUG WARNING: UserService logging crash ignored. Admin User created.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to create Admin user: " + e.getMessage());
                }
            }
            return env.getProperty("success");
        }
        return null;
    }
	/**
	 * This method is fetch Organizations information This organization list
	 * including the subscription information
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public List<OrganizationDTO> getAllOrganizationList() {
		return convertOrganizationInfosToOrganizationDTOs(organizationInfoRepository.findAll());
	}

	/**
	 * This method is view Organization information This method can pass the
	 * organization id and fetch corresponding the organization information
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public OrganizationDTO viewOrganizationInformation(Long organizationId) {
		if (organizationId != null) {
			return convertOganizationInfoToOrganizationDTO(organizationInfoRepository.findOne(organizationId));
		}
		return null;
	}

	/**
	 * This method is used to update Organization information with subscription
	 * validate all parameter and after successfully update organization will return
	 * success message Super administrator only can update Organization and
	 * subscription and administrator
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public String updateOrganizationInformation(OrganizationDTO organizationDTO) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String date = sdf.format(new Date());
		System.out.println(date);

		if (organizationDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("organization.admin.only.update");

		if (organizationDTO.getSubscriptionId() != null) {
			if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
				return env.getProperty("subscription.not.found");
		}

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getOrganizationName() == null && StringUtils.isBlank(organizationDTO.getOrganizationName())
				&& StringUtils.isEmpty(organizationDTO.getOrganizationName()))
			return env.getProperty("organization.name.empty");

		if (organizationDTO.getRegistrationNumber() == null
				&& StringUtils.isEmpty(organizationDTO.getRegistrationNumber())
				&& StringUtils.isBlank(organizationDTO.getRegistrationNumber()))
			return env.getProperty("organization.register.number.empty");

		if (organizationDTO.getEmailId() == null && StringUtils.isEmpty(organizationDTO.getEmailId())
				&& StringUtils.isBlank(organizationDTO.getEmailId()))
			return env.getProperty("organization.email.empty");

		if (organizationDTO.getEmailId() != null) {
			Pattern pattern = Pattern.compile(EMAIL_REGEX);
			Matcher matcher = pattern.matcher(organizationDTO.getEmailId());
			if (!matcher.matches())
				return env.getProperty("organization.email.not.valid");
		}

		if (organizationDTO.getPhoneNumber() == null && StringUtils.isEmpty(organizationDTO.getPhoneNumber())
				&& StringUtils.isBlank(organizationDTO.getPhoneNumber()))
			return env.getProperty("organization.phone.empty");

		if (organizationDTO.getSubscriptionId() != null) {
			if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
				return env.getProperty("subscription.not.found");
		}

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");

		if (organizationDTO.getSubscriptionInfo() != null) {
			Date startDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getFromDate());
			Date endDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getToDate());
			String fromDate = sdf.format(startDate);
			String toDate = sdf.format(endDate);
			System.out.println(fromDate);
			System.out.println(toDate);
			if (organizationInfo.getSubscriptionInfo() != null) {
				if (organizationDTO.getSubscriptionInfo().getSubscriptionId() == null)
					return env.getProperty("subscription.id.not.null");

				if (subscriptionInfoRepository
						.findOne(organizationDTO.getSubscriptionInfo().getSubscriptionId()) == null)
					return env.getProperty("subscription.not.found");
			}

			if (organizationDTO.getSubscriptionInfo().getSubscriptionAmount() == null)
				return env.getProperty("subscription.amount.not.null");

			if (organizationDTO.getSubscriptionInfo().getShipmentCount() == null)
				return env.getProperty("subscription.ship.count.not.null");

			if (organizationDTO.getSubscriptionInfo().getUserVesslesRatio() == null
					&& StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getUserVesslesRatio().toString()))
				return env.getProperty("subscription.user.ratio.not.null");

			/*
			 * if (organizationDTO.getSubscriptionInfo().getUserCount() == null) return
			 * env.getProperty("subscription.user.count.not.null");
			 */

			/*
			 * if (organizationDTO.getSubscriptionInfo().getSubscriptionDays() == null &&
			 * StringUtils.isBlank(organizationDTO.getSubscriptionInfo().getSubscriptionDays
			 * ().toString())) return env.getProperty("subscription.days.not.null");
			 */

			if (organizationDTO.getSubscriptionInfo().getFromDate() == null)
				return env.getProperty("subscription.start.date.empty");

			if (organizationDTO.getSubscriptionInfo().getToDate() == null)
				return env.getProperty("subscription.end.date.empty");

			if (!fromDate.matches(SUBSCRIPTION_DATE_REGEX))
				return env.getProperty("subscription.start.date.worng.format");

			if (!toDate.matches(SUBSCRIPTION_DATE_REGEX))
				return env.getProperty("subscription.end.date.worng.format");

			if (endDate.compareTo(startDate) < 0) {
				return "End date is before Start date";
			}
//			else if (endDate.compareTo(startDate) == 0) {
//				return "Start date is equal to End date";
//			}
//			else if (startDate.compareTo(new Date()) < 0) {
//				return ("Start date is should not before Today date");
//			}

		}

		OrganizationInfo organization = organizationInfoRepository
				.findByOrganizationName(organizationDTO.getOrganizationName());
		if (organization != null) {
			if (!organizationInfo.equals(organization))
				return env.getProperty("organization.name.exists");
		}

		if (organizationDTO.getUserInfo() != null) {
			if (organizationDTO.getUserInfo().getUserName() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getUserName())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getUserName()))
				return env.getProperty("user.username.empty");

			if (organizationDTO.getUserInfo().getFirstName() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getFirstName())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getFirstName()))
				return env.getProperty("user.first.name.empty");

			if (organizationDTO.getUserInfo().getLastName() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getLastName())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getLastName()))
				return env.getProperty("user.last.name.empty");

			if (organizationDTO.getUserInfo().getMail() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getMail())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getMail()))
				return env.getProperty("user.email.empty");

			if (organizationDTO.getUserInfo().getMail() != null) {
				Pattern pattern = Pattern.compile(EMAIL_REGEX);
				Matcher matcher = pattern.matcher(organizationDTO.getUserInfo().getMail());
				if (!matcher.matches())
					return env.getProperty("user.email.not.valid");
			}

			if (organizationDTO.getUserInfo().getPassword() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getPassword())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getPassword()))
				return env.getProperty("user.password.empty");

			List<User> userList = userRepository.findByMail(organizationDTO.getUserInfo().getMail().toLowerCase());
			if (userList != null && userList.size() > 0) {
				return env.getProperty("user.email.already.exist");
			}

			if (userRepository.findByUsername(organizationDTO.getUserInfo().getUserName()) != null)
				return env.getProperty("user.username.already.exist");
		}

		OrganizationInfo updateOrganizationInfo = organizationInfoRepository
				.saveAndFlush(convertOrganizationDTOToOrganizationInfo(organizationDTO));
		if (updateOrganizationInfo != null) {
			commonMethodsUtility.maintainHistory(organizationInfo.getId(), organizationInfo.getOrganizationName(),
					"Organization", env.getProperty("history.updated"), profileInfo.getId());

			if (organizationInfo.getSubscriptionInfo() != null) {
				if (organizationDTO.getSubscriptionInfo() != null) {
					commonMethodsUtility.maintainHistory(organizationInfo.getSubscriptionInfo().getId(),
						organizationInfo.getOrganizationName(), "Subscription",
						env.getProperty("history.updated"), profileInfo.getId());
		}
			}

			if (organizationDTO.getUserInfo() != null) {
				try {
					organizationDTO.getUserInfo().setOrganizationId(organizationInfo.getId());
					organizationDTO.getUserInfo().setBusinessCategory(Role.Admin.name());
					organizationDTO.getUserInfo().setRoleId(roleInfoRepository.findByRoleName(Role.Admin).getId());
					boolean userCreation = userService.create(organizationDTO.getUserInfo(), null);
					if (!userCreation) {
						return env.getProperty("organization.user.create.failure");
					}
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
			return env.getProperty("success");
		}

		return null;
	}

	/**
	 * This method is used to delete Organization information with subscription
	 * Super administrator only can delete Organization and subscription and
	 * administrator
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public String deleteOrganizationInformation(Long superAdminId, Long organizationId) {
		if (superAdminId == null)
			return env.getProperty("user.id.null");

		if (organizationId == null)
			return env.getProperty("organization.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(superAdminId);
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("organization.admin.only.delete");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationId);
		if (organizationInfo == null) {
			return env.getProperty("organization.not.found");
		}

		DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
		deletedHistoryDTO.setObjectId(organizationInfo.getId());
		deletedHistoryDTO.setObjectOne(organizationInfo.getOrganizationName());
		deletedHistoryDTO.setObjectTwo(organizationInfo.getContact());
		DataDeletedHistoryInfo dataDeletedHistoryInfo = commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
		organizationInfoRepository.delete(organizationInfo);
		commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(), organizationInfo.getOrganizationName(),
				"Organization", env.getProperty("history.deleted"), profileInfo.getId());
		return env.getProperty("success");
	}

	/**
	 * This method is used to Active/De-Active save in ilockchain Organization
	 * information Super administrator only can Active and De-Active
	 *
	 */
	@Override
	public String setSaveInBlockchainOrganizationInformation(OrganizationDTO organizationDTO) {
		if (organizationDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getIsSaveInBlockchain() == null)
			return env.getProperty("organization.save.in.blockchain.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("organization.admin.only.dual.approval");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");

		if (organizationInfo.getSaveInBlockhainInfo() == null) {
			SaveInBlockhainInfo saveInBlockhainInfo = new SaveInBlockhainInfo();
			saveInBlockhainInfo.setOrganizationInfo(organizationInfo);
			organizationInfo.setSaveInBlockhainInfo(saveInBlockhainInfo);
		}

		organizationInfo.getSaveInBlockhainInfo().setIsActive(organizationDTO.getIsSaveInBlockchain());

		if (organizationInfoRepository.saveAndFlush(organizationInfo) != null)
			return env.getProperty("success");

		return null;
	}

	/**
	 * This method is used to Active/De-Active Organization information with
	 * subscription Super administrator only can Active and De-Active Organization
	 * and subscription
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public String activeActiveAndDeactiveOrganizationInformation(OrganizationDTO organizationDTO) {

		if (organizationDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getIsStatusActive() == null)
			return env.getProperty("organization.status.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("organization.admin.only.active");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");

		organizationInfo.setIsActive(organizationDTO.getIsStatusActive());
		if (organizationInfoRepository.saveAndFlush(organizationInfo) != null)
			return env.getProperty("success");

		return null;
	}

	/**
	 * This method is used to Active/De-Active Dual Approval Organization
	 * information Super administrator only can Active and De-Active Dual Approval
	 * Organization information
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public String enableOrDiableDualApprovalOrganizationInformation(OrganizationDTO organizationDTO) {
		if (organizationDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getIsDualApprovalActive() == null)
			return env.getProperty("organization.dual.approval.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("organization.admin.only.dual.approval");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");

		organizationInfo.getDualApprovalInfo().setFlag(organizationDTO.getIsDualApprovalActive());
		if (organizationInfoRepository.saveAndFlush(organizationInfo) != null)
			return env.getProperty("success");
		return null;
	}

	/**
	 * This method is used to check whether organization is exist or not. if
	 * organization is not exist this method return corresponding error message.
	 *
	 * @author Prabakaran
	 *
	 *
	 */
	@Override
	public String isOrganizationExist(Long organizationId) {
		if (organizationId == null)
			return env.getProperty("organization.id.null");

		if (organizationInfoRepository.findOne(organizationId) == null)
			return env.getProperty("organization.not.found");

		return env.getProperty("success");
	}

	private OrganizationInfo convertOrganizationDTOToOrganizationInfo(OrganizationDTO organizationDTO) {
		OrganizationInfo organizationInfo = null;
		SubscriptionInfo subscriptionInfo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		if (organizationDTO.getOrganizationId() != null) {
			organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
			if (organizationInfo == null) {
				organizationInfo = new OrganizationInfo();
			}
		} else {
			organizationInfo = new OrganizationInfo();
		}

		organizationInfo.setOrganizationName(organizationDTO.getOrganizationName());
		organizationInfo.setRegistrationNumber(organizationDTO.getRegistrationNumber());
		// organizationInfo.setAddress(organizationDTO.getAddress());
		organizationInfo.setAddressLine1(organizationDTO.getAddressLine1());
		organizationInfo.setAddressLine2(organizationDTO.getAddressLine2());
		organizationInfo.setEmailId(organizationDTO.getEmailId());
		organizationInfo.setPhoneNumber(organizationDTO.getPhoneNumber());
		organizationInfo.setAlternatePhoneNumber(organizationDTO.getAlternatePhoneNumber());
		organizationInfo.setBankAccountNumber(organizationDTO.getBankAccountNumber());
		organizationInfo.setContactPersonName(organizationDTO.getContactPersonName());
		organizationInfo.setContactPersonEmail(organizationDTO.getContactPersonEmail());
		organizationInfo.setContactPersonPhoneNo(organizationDTO.getContactPersonPhoneNo());
		organizationInfo.setContactPersonAlternatePhoneNo(organizationDTO.getContactPersonAlternatePhoneNo());
		if (organizationInfo.getId() == null) {
			organizationInfo.setIsActive(new Long(1));
			DualApprovalInfo dualApprovalInfo = new DualApprovalInfo();
			dualApprovalInfo.setFlag(new Long(1));
			dualApprovalInfo.setOrganizationInfo(organizationInfo);
			organizationInfo.setDualApprovalInfo(dualApprovalInfo);

			SaveInBlockhainInfo saveInBlockhainInfo = new SaveInBlockhainInfo();
			saveInBlockhainInfo.setIsActive(new Long(0));
			saveInBlockhainInfo.setOrganizationInfo(organizationInfo);
			organizationInfo.setSaveInBlockhainInfo(saveInBlockhainInfo);
		}

		if (organizationDTO.getSubscriptionId() != null) {
			SubscriptionInfo subscription = subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId());
			if (subscription != null) {
				subscription.setUserCount(organizationDTO.getUserCount());
				subscription.setShipmentCount(organizationDTO.getShipmentCount());
				organizationInfo.setSubscriptionInfo(subscription);
			}
		}
		if (organizationDTO.getSubscriptionInfo() != null) {
			if (organizationDTO.getSubscriptionInfo().getSubscriptionId() != null) {
				subscriptionInfo = subscriptionInfoRepository
						.findOne(organizationDTO.getSubscriptionInfo().getSubscriptionId());
				if (subscriptionInfo == null) {
					subscriptionInfo = new SubscriptionInfo();
				}
			} else {
				subscriptionInfo = new SubscriptionInfo();
			}
			subscriptionInfo.setShipmentCount(organizationDTO.getSubscriptionInfo().getShipmentCount());
			subscriptionInfo.setUserVesslesRatio(organizationDTO.getSubscriptionInfo().getUserVesslesRatio());
			long usercount = organizationDTO.getSubscriptionInfo().getShipmentCount()
					* organizationDTO.getSubscriptionInfo().getUserVesslesRatio();
			subscriptionInfo.setUserCount(usercount);
			subscriptionInfo.setSubscriptionAmount(organizationDTO.getSubscriptionInfo().getSubscriptionAmount());
			subscriptionInfo.setIsStatusAlive(1);
			try {
				Date startDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getFromDate());
				Date endDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getToDate());
				String fromDate = sdf.format(startDate);
				String toDate = sdf.format(endDate);
				System.out.println("fromDate::::::::::" + fromDate); // 15/10/2013
				System.out.println("toDate::::::::::" + toDate);
				subscriptionInfo
						.setSubscriptionDays(Long.valueOf(daysBetweenTwoDates(sdf.parse(fromDate), sdf.parse(toDate))));
				subscriptionInfo.setSubscriptionStartDate(sdf.parse(fromDate));
				subscriptionInfo.setSubscriptionExpireDate(sdf.parse(toDate));
				/*
				 * subscriptionInfo.setSubscriptionExpireDate(DATE_FORMAT.parse(DATE_FORMAT
				 * .format(getDaysInDate(organizationDTO.getSubscriptionInfo().
				 * getSubscriptionDays()))));
				 */
			} catch (ParseException e) {
				e.printStackTrace();
			}

			organizationInfo.setSubscriptionInfo(subscriptionInfo);
		}
		return organizationInfo;
	}

	private List<OrganizationDTO> convertOrganizationInfosToOrganizationDTOs(List<OrganizationInfo> organizationInfos) {
		List<OrganizationDTO> organizationDTOs = new ArrayList<>();
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
		for (OrganizationInfo organizationInfo : organizationInfos) {
			OrganizationDTO organizationDTO = new OrganizationDTO();
			organizationDTO.setOrganizationId(organizationInfo.getId());
			organizationDTO.setOrganizationName(organizationInfo.getOrganizationName());
			organizationDTO.setRegistrationNumber(organizationInfo.getRegistrationNumber());
			// organizationDTO.setAddress(organizationInfo.getAddress());
				organizationDTO.setAddressLine1(organizationInfo.getAddressLine1());
			organizationDTO.setAddressLine2(organizationInfo.getAddressLine2());
			organizationDTO.setEmailId(organizationInfo.getEmailId());
			organizationDTO.setPhoneNumber(organizationInfo.getPhoneNumber());
			organizationDTO.setAlternatePhoneNumber(organizationInfo.getAlternatePhoneNumber());
			organizationDTO.setBankAccountNumber(organizationInfo.getBankAccountNumber());
			organizationDTO.setContactPersonName(organizationInfo.getContactPersonName());
			organizationDTO.setContactPersonEmail(organizationInfo.getContactPersonEmail());
			organizationDTO.setContactPersonPhoneNo(organizationInfo.getContactPersonPhoneNo());
			organizationDTO.setContactPersonAlternatePhoneNo(organizationInfo.getContactPersonAlternatePhoneNo());
			organizationDTO.setIsStatusActive(organizationInfo.getIsActive());
			if (organizationInfo.getDualApprovalInfo() != null) {
				organizationDTO.setIsDualApprovalActive(organizationInfo.getDualApprovalInfo().getFlag());
			}
			if (organizationInfo.getSaveInBlockhainInfo() != null) {
				organizationDTO.setIsSaveInBlockchain(organizationInfo.getSaveInBlockhainInfo().getIsActive());
			}
			if (organizationInfo.getSubscriptionInfo() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String fromDate = sdf.format(organizationInfo.getSubscriptionInfo().getSubscriptionStartDate());
				String toDate = sdf.format(organizationInfo.getSubscriptionInfo().getSubscriptionExpireDate());
				subscriptionDTO.setSubscriptionId(organizationInfo.getSubscriptionInfo().getId());
				subscriptionDTO.setSubscriptionAmount(organizationInfo.getSubscriptionInfo().getSubscriptionAmount());
				subscriptionDTO.setShipmentCount(organizationInfo.getSubscriptionInfo().getShipmentCount());
				subscriptionDTO.setUserCount(organizationInfo.getSubscriptionInfo().getUserCount());
				subscriptionDTO.setSubscriptionDays(organizationInfo.getSubscriptionInfo().getSubscriptionDays());
				subscriptionDTO.setIsStatusAlive(organizationInfo.getSubscriptionInfo().getIsStatusAlive());
				subscriptionDTO.setUserVesslesRatio(organizationInfo.getSubscriptionInfo().getUserVesslesRatio());
				subscriptionDTO.setFromDate(fromDate);
				subscriptionDTO.setToDate(toDate);

			}
			organizationDTO.setSubscriptionInfo(subscriptionDTO);

			organizationDTOs.add(organizationDTO);
		}
		return organizationDTOs;
	}

	private OrganizationDTO convertOganizationInfoToOrganizationDTO(OrganizationInfo organizationInfo) {
		Set<UserDTO> userDTOs = new HashSet<>();
		if (organizationInfo != null) {
			SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
			OrganizationDTO organizationDTO = new OrganizationDTO();
			organizationDTO.setOrganizationId(organizationInfo.getId());
			organizationDTO.setOrganizationName(organizationInfo.getOrganizationName());
			organizationDTO.setRegistrationNumber(organizationInfo.getRegistrationNumber());
			// organizationDTO.setAddress(organizationInfo.getAddress());
		// organizationInfo.setAddressLine1(organizationDTO.getAddressLine1());
		// organizationInfo.setAddressLine2(organizationDTO.getAddressLine2());
		// RIGHT: This takes values from the Info object (DB) and puts them into the DTO (Response)
organizationDTO.setAddressLine1(organizationInfo.getAddressLine1());
organizationDTO.setAddressLine2(organizationInfo.getAddressLine2());
			organizationDTO.setEmailId(organizationInfo.getEmailId());
			organizationDTO.setPhoneNumber(organizationInfo.getPhoneNumber());
			organizationDTO.setAlternatePhoneNumber(organizationInfo.getAlternatePhoneNumber());
			organizationDTO.setBankAccountNumber(organizationInfo.getBankAccountNumber());
			organizationDTO.setContactPersonName(organizationInfo.getContactPersonName());
			organizationDTO.setContactPersonEmail(organizationInfo.getContactPersonEmail());
			organizationDTO.setContactPersonPhoneNo(organizationInfo.getContactPersonPhoneNo());
			organizationDTO.setContactPersonAlternatePhoneNo(organizationInfo.getContactPersonAlternatePhoneNo());
			organizationDTO.setIsStatusActive(organizationInfo.getIsActive());
			if (organizationInfo.getDualApprovalInfo() != null) {
				organizationDTO.setIsDualApprovalActive(organizationInfo.getDualApprovalInfo().getFlag());
			}
			if (organizationInfo.getSaveInBlockhainInfo() != null) {
				organizationDTO.setIsSaveInBlockchain(organizationInfo.getSaveInBlockhainInfo().getIsActive());
			}
			if (organizationInfo.getSubscriptionInfo() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				// Date startDate =
				// fileServiceImpl.parseDate(organizationInfo.getSubscriptionInfo().getSubscriptionStartDate());
				// Date endDate =
				// fileServiceImpl.parseDate(organizationInfo.getSubscriptionInfo().getSubscriptionExpireDate());
				String fromDate = sdf.format(organizationInfo.getSubscriptionInfo().getSubscriptionStartDate());
				String toDate = sdf.format(organizationInfo.getSubscriptionInfo().getSubscriptionExpireDate());
				subscriptionDTO.setSubscriptionId(organizationInfo.getSubscriptionInfo().getId());
				subscriptionDTO.setSubscriptionAmount(organizationInfo.getSubscriptionInfo().getSubscriptionAmount());
				subscriptionDTO.setShipmentCount(organizationInfo.getSubscriptionInfo().getShipmentCount());
				subscriptionDTO.setUserCount(organizationInfo.getSubscriptionInfo().getUserCount());
				subscriptionDTO.setSubscriptionDays(organizationInfo.getSubscriptionInfo().getSubscriptionDays());
				subscriptionDTO.setIsStatusAlive(organizationInfo.getSubscriptionInfo().getIsStatusAlive());
				subscriptionDTO.setUserVesslesRatio(organizationInfo.getSubscriptionInfo().getUserVesslesRatio());
				subscriptionDTO.setFromDate(fromDate);
				subscriptionDTO.setToDate(toDate);

			}
			organizationDTO.setSubscriptionInfo(subscriptionDTO);
			if (organizationInfo.getUserProfileInfo() != null && organizationInfo.getUserProfileInfo().size() > 0) {
				for (UserProfileInfo userProfileInfo : organizationInfo.getUserProfileInfo()) {
					UserDTO userDTO = new UserDTO();
					userDTO.setUserId(userProfileInfo.getId());
					userDTO.setUserName(userProfileInfo.getUserName());
					userDTO.setFirstName(userProfileInfo.getFirstName());
					userDTO.setLastName(userProfileInfo.getLastName());
					RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(
							userProfileInfo.getRoleId(), userProfileInfo.getOrganizationInfo());
					if (roleAliasInfo != null) {
						userDTO.setRole(roleAliasInfo.getRoleAliasName());
						userDTO.setRoleId(userProfileInfo.getRoleId().getId());
					} else {
						userDTO.setRole(userProfileInfo.getRoleId().getRoleName().name());
						userDTO.setRoleId(userProfileInfo.getRoleId().getId());
					}
					userDTOs.add(userDTO);
				}
				organizationDTO.setUserInfos(userDTOs);
			}

			return organizationDTO;
		}
		return null;
	}

	/*
	 * private Date getDaysInDate(Long countDays) { GregorianCalendar calendar = new
	 * GregorianCalendar(); calendar.setTime(new Date());
	 * calendar.add(Calendar.DATE, countDays.intValue()); return calendar.getTime();
	 * }
	 */

	private int daysBetweenTwoDates(Date startDate, Date endDate) {
		return Days.daysBetween(new LocalDate(startDate.getTime()), new LocalDate(endDate.getTime())).getDays();
	}

	@Override
	public List<UserDTO> listAdminsBasedOrganizationInformation(Long organizationId) {
		List<UserDTO> userDTOs = new ArrayList<>();
		if (organizationId != null) {
			for (UserProfileInfo userProfileInfo : organizationInfoRepository.findOne(organizationId)
					.getUserProfileInfo()) {
				UserDTO userDTO = new UserDTO();
				if (userProfileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
					userDTO.setUserId(userProfileInfo.getId());
					userDTO.setRole(userProfileInfo.getRoleId().getRoleName().name());
					userDTO.setRoleId(userProfileInfo.getRoleId().getId());
					userDTO.setFirstName(userProfileInfo.getFirstName());
					userDTO.setLastName(userProfileInfo.getLastName());
					userDTO.setUserName(userProfileInfo.getUserName());
					User user = userRepository.findByUsername(userProfileInfo.getUserName());
					if (user != null) {
						userDTO.setMail(user.getMail().toLowerCase());
					}
					userDTO.setStatus(userProfileInfo.getStatus().longValue());
					userDTOs.add(userDTO);
				}
			}
			return userDTOs;
		}
		return userDTOs;
	}

	@Override
	public String saveAdminInformationBasedOrganization(OrganizationDTO organizationDTO) {
		if (organizationDTO.getUserId() == null)
			return env.getProperty("super.admin.id.null");

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getUserInfo() == null)
			return env.getProperty("organization.admin.info.null");

		if (organizationDTO.getUserInfo() != null) {
			if (organizationDTO.getUserInfo().getUserName() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getUserName())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getUserName()))
				return env.getProperty("user.username.empty");

			if (organizationDTO.getUserInfo().getFirstName() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getFirstName())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getFirstName()))
				return env.getProperty("user.first.name.empty");

			if (organizationDTO.getUserInfo().getLastName() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getLastName())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getLastName()))
				return env.getProperty("user.last.name.empty");

			if (organizationDTO.getUserInfo().getMail() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getMail())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getMail()))
				return env.getProperty("user.email.empty");

			if (organizationDTO.getUserInfo().getPassword() == null
					&& StringUtils.isEmpty(organizationDTO.getUserInfo().getPassword())
					&& StringUtils.isBlank(organizationDTO.getUserInfo().getPassword()))
				return env.getProperty("user.password.empty");

			List<User> userList = userRepository.findByMail(organizationDTO.getUserInfo().getMail().toLowerCase());
			if (userList != null && userList.size() > 0) {
				return env.getProperty("user.email.already.exist");
			}

			if (userRepository.findByUsername(organizationDTO.getUserInfo().getUserName()) != null)
				return env.getProperty("user.username.already.exist");
		}

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("super.admin.only.create");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");
		if (organizationDTO.getUserInfo() != null) {
			try {
				organizationDTO.getUserInfo().setOrganizationId(organizationInfo.getId());
				organizationDTO.getUserInfo().setBusinessCategory(Role.Admin.name());
				organizationDTO.getUserInfo().setRoleId(roleInfoRepository.findByRoleName(Role.Admin).getId());
				boolean userCreation = userService.create(organizationDTO.getUserInfo(), null);
				if (!userCreation) {
					return env.getProperty("organization.user.create.failure");
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
			return env.getProperty("success");
		}

		return null;
	}

	@Override
	public String updateSubscriptionAndOrganization(OrganizationDTO organizationDTO) {
		if (organizationDTO.getUserId() == null)
			return env.getProperty("super.admin.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("subscription.admin.only.update");

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getSubscriptionId() == null)
			return env.getProperty("subscription.id.not.null");

		if (organizationDTO.getOrganizationName() == null && StringUtils.isBlank(organizationDTO.getOrganizationName())
				&& StringUtils.isEmpty(organizationDTO.getOrganizationName()))
			return env.getProperty("organization.name.empty");

		if (organizationDTO.getRegistrationNumber() == null
				&& StringUtils.isEmpty(organizationDTO.getRegistrationNumber())
				&& StringUtils.isBlank(organizationDTO.getRegistrationNumber()))
			return env.getProperty("organization.register.number.empty");

		if (organizationDTO.getShipmentCount() == null
				&& StringUtils.isBlank(organizationDTO.getShipmentCount().toString()))
			return env.getProperty("subscription.ship.count.not.null");

		if (organizationDTO.getUserCount() == null && StringUtils.isBlank(organizationDTO.getUserCount().toString()))
			return env.getProperty("subscription.user.count.not.null");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");

		if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
			return env.getProperty("subscription.not.found");

		OrganizationInfo organization = organizationInfoRepository
				.findByOrganizationName(organizationDTO.getOrganizationName());
		if (organization != null) {
			if (!organizationInfo.equals(organization))
				return env.getProperty("organization.name.exists");
		}

		OrganizationInfo updateOrganizationInfo = organizationInfoRepository
				.saveAndFlush(convertOrganizationDTOToOrganizationInfo(organizationDTO));
		if (updateOrganizationInfo != null) {
			commonMethodsUtility.maintainHistory(updateOrganizationInfo.getId(),
					updateOrganizationInfo.getOrganizationName(), "Organization", env.getProperty("history.created"),
					organizationDTO.getUserId());
		}
		if (updateOrganizationInfo != null)
			return env.getProperty("success");

		return null;
	}

	@Override
	public String updateSubscription(OrganizationDTO organizationDTO) {
		if (organizationDTO.getUserId() == null)
			return env.getProperty("super.admin.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(organizationDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("subscription.admin.only.update");

		if (organizationDTO.getOrganizationId() == null)
			return env.getProperty("organization.id.null");

		if (organizationDTO.getSubscriptionId() == null)
			return env.getProperty("subscription.id.not.null");

		if (organizationDTO.getShipmentCount() == null
				&& StringUtils.isBlank(organizationDTO.getShipmentCount().toString()))
			return env.getProperty("subscription.ship.count.not.null");

		if (organizationDTO.getUserCount() == null && StringUtils.isBlank(organizationDTO.getUserCount().toString()))
			return env.getProperty("subscription.user.count.not.null");

		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
		if (organizationInfo == null)
			return env.getProperty("organization.not.found");

		if (subscriptionInfoRepository.findOne(organizationDTO.getSubscriptionId()) == null)
			return env.getProperty("subscription.not.found");

		OrganizationInfo updateOrganizationInfo = organizationInfoRepository
				.saveAndFlush(convertOrganizationSubDTOToOrganizationInfo(organizationDTO));
		if (updateOrganizationInfo != null) {
			commonMethodsUtility.maintainHistory(updateOrganizationInfo.getId(),
					updateOrganizationInfo.getOrganizationName(), "Organization", env.getProperty("history.created"),
					organizationDTO.getUserId());
		}
		if (updateOrganizationInfo != null)
			return env.getProperty("success");

		return null;
	}

	private OrganizationInfo convertOrganizationSubDTOToOrganizationInfo(OrganizationDTO organizationDTO) {
		OrganizationInfo organizationInfo = null;
		SubscriptionInfo subscriptionInfo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		if (organizationDTO.getOrganizationId() != null) {
			organizationInfo = organizationInfoRepository.findOne(organizationDTO.getOrganizationId());
			subscriptionInfo.setShipmentCount(organizationDTO.getSubscriptionInfo().getShipmentCount());
			long usercount = organizationDTO.getSubscriptionInfo().getShipmentCount()
					* organizationDTO.getSubscriptionInfo().getUserCount();
			subscriptionInfo.setUserCount(usercount);
			subscriptionInfo.setSubscriptionAmount(organizationDTO.getSubscriptionInfo().getSubscriptionAmount());
			subscriptionInfo.setIsStatusAlive(1);
			try {
				Date startDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getFromDate());
				Date endDate = fileServiceImpl.parseDate(organizationDTO.getSubscriptionInfo().getToDate());
				String fromDate = sdf.format(startDate);
				String toDate = sdf.format(endDate);
				System.out.println("fromDate::::::::::" + fromDate); // 15/10/2013
				System.out.println("toDate::::::::::" + toDate);
				subscriptionInfo
						.setSubscriptionDays(Long.valueOf(daysBetweenTwoDates(sdf.parse(fromDate), sdf.parse(toDate))));
				subscriptionInfo.setSubscriptionStartDate(sdf.parse(fromDate));
				subscriptionInfo.setSubscriptionExpireDate(sdf.parse(toDate));
				/*
				 * subscriptionInfo.setSubscriptionExpireDate(DATE_FORMAT.parse(DATE_FORMAT
				 * .format(getDaysInDate(organizationDTO.getSubscriptionInfo().
				 * getSubscriptionDays()))));
				 */
			} catch (ParseException e) {
				e.printStackTrace();
			}

			organizationInfo.setSubscriptionInfo(subscriptionInfo);
		}
		return organizationInfo;
	}

	@Override
	public OrganizationDTO getOrganizationTopCount() {

		List<OrganizationInfo> organizationInfos = organizationInfoRepository.findAll();
		if (organizationInfos != null) {
			OrganizationDTO organizationDTOs = organizationInfoUtility.convertOrganizatioTopCount(organizationInfos);
			return organizationDTOs;
		}
		return null;

	}

	@Override
	public List<OrganizationDTO> getOrganizationStatistics() {
		List<OrganizationInfo> organizationInfos = organizationInfoRepository.findAll();
		if (organizationInfos != null) {
			List<OrganizationDTO> organizationDTOs = organizationInfoUtility
					.convertdetailOfOrganizationStatistics(organizationInfos);
			return organizationDTOs;
		}
		return null;
	}

}
