package com.dapp.docuchain.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dapp.docuchain.dto.RequestUserDTO;
import com.dapp.docuchain.dto.TaskDTO;
import com.dapp.docuchain.model.AssignedUserTaskInfo;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.RequestUserInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.TaskDetailsInfo;
import com.dapp.docuchain.model.TaskStatusInfo;
import com.dapp.docuchain.model.UserNotificationInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.RequestUserRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.repository.TaskStatusRepository;
import com.dapp.docuchain.repository.UserNotificationRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.ShipProfileService;

@Service
public class NotificationUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationUtility.class);

    @Autowired
    Environment env;

    @Autowired
    private ExpiryDocumentRepository expiryDocumentRepository;

    @Autowired
    private ShipProfileRepository shipProfileRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private RequestUserRepository requestUserRepository;

    @Autowired
    private ShipProfileService shipProfileService;

    @Autowired
    private DocumentHolderRepository documentHolderRepository;

    @Autowired
    private RoleInfoRepository roleInfoRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Scheduled(cron = "0 0 12 * * ?")
    public boolean addExpiryBasedDocument() {
        try {
            List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByStatus(1);
            for (ShipProfileInfo shipProfileInfo : shipProfileInfos) {
                List<ExpiryDocumentInfo> expiryDocumentInfos = expiryDocumentRepository.findByShipProfileInfoAndCurrentVersion(shipProfileInfo, 1);
                LOGGER.info("listSize::::" + expiryDocumentInfos.size());
                Date currentDate = new Date();
                if (expiryDocumentInfos != null && expiryDocumentInfos.size() > 0) {
                    for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfos) {
                        DocumentHolderInfo documentHolderInfo = expiryDocumentInfo.getDocumentHolderInfo();
                        LOGGER.info("expiryDocumentName" + expiryDocumentInfo.getDocumentName());
                        UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
                        userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
                        Date expiryDate = expiryDocumentInfo.getExpiryDate();
                        long diff = expiryDate.getTime() - currentDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        /*if (diffDays >= 30) {
                            userNotificationInfo.setDescription(documentHolderInfo.getDocumentHolderName() + "Still have time to till date" + expiryDocumentInfo.getExpiryDate());
                        } else*/ if (diffDays <= 30 && diffDays >= 0) {
                            userNotificationInfo.setDescription(documentHolderInfo.getDocumentHolderName() + " about to expire on " + new SimpleDateFormat("dd-MM-yyyy").format(expiryDocumentInfo.getExpiryDate()));
                            userNotificationInfo.setColor("Yellow");
                        } else if (diffDays <= 0) {
                            userNotificationInfo.setDescription(documentHolderInfo.getDocumentHolderName() + " already expired on " + new SimpleDateFormat("dd-MM-yyyy").format(expiryDocumentInfo.getExpiryDate()));
                            userNotificationInfo.setColor("Red");
                        }
                        if (expiryDocumentInfo.getUploadedBy() != null) {
                            userNotificationInfo.setNotifiedTo(expiryDocumentInfo.getShipProfileInfo().getShipMaster());
                            userNotificationInfo.setNotificationTime(new Date());
                        }
                        userNotificationInfo.setNotificationType("Document List Page");
                        userNotificationRepository.save(userNotificationInfo);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            return false;
        }
    }

    @Scheduled(cron = "0 5 12 * * ?")
    public boolean addExpiryBasedDocumentforTechManager() {
        try {
            List<ShipProfileInfo> shipProfileInfos = shipProfileRepository.findByStatus(1);
            for (ShipProfileInfo shipProfileInfo : shipProfileInfos) {
                List<ExpiryDocumentInfo> expiryDocumentInfos = expiryDocumentRepository.findByShipProfileInfoAndCurrentVersion(shipProfileInfo, 1);
                LOGGER.info("listSize::::" + expiryDocumentInfos.size());
                Date currentDate = new Date();
                if (expiryDocumentInfos != null && expiryDocumentInfos.size() > 0) {
                    for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfos) {
                        DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(expiryDocumentInfo.getDocumentHolderInfo().getId());
                        LOGGER.info("expiryDocumentName" + expiryDocumentInfo.getDocumentName());
                        UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
                        userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
                        Date expiryDate = expiryDocumentInfo.getExpiryDate();
                        long diff = expiryDate.getTime() - currentDate.getTime();
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                      /*  if (diffDays >= 30) {
                            userNotificationInfo.setDescription(documentHolderInfo.getDocumentHolderName() + "Still have time to till date" + expiryDocumentInfo.getExpiryDate());
                        } else */if (diffDays < 30 && diffDays >= 15) {
                            userNotificationInfo.setDescription(documentHolderInfo.getDocumentHolderName() + " about to expire on " + new SimpleDateFormat("dd-MM-yyyy").format(expiryDocumentInfo.getExpiryDate()));
                            userNotificationInfo.setColor("Yellow");
                        } else if (diffDays < 15 && diffDays >= 0) {
                            userNotificationInfo.setDescription(documentHolderInfo.getDocumentHolderName() + " already expired on " + new SimpleDateFormat("dd-MM-yyyy").format(expiryDocumentInfo.getExpiryDate()));
                            userNotificationInfo.setColor("Red");
                        }
                        if (expiryDocumentInfo.getUploadedBy() != null) {
                           // userNotificationInfo.setNotifiedTo(expiryDocumentInfo.getShipProfileInfo().getTechMaster());
                            userNotificationInfo.setNotificationTime(new Date());
                        }
                        userNotificationInfo.setNotificationType("Document List Page");
                        userNotificationRepository.save(userNotificationInfo);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            return false;
        }
    }

    public String findNotificationColor(Date expiryDate) {
        String color = "grey";
        Date currentDate = new Date();
        long diff = expiryDate.getTime() - currentDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffDays >= 30) {
            color = "Green";
            return color;
        } else if (diffDays < 30 && diffDays >= 0) {
            color = "Yellow";
            return color;
        } else if (diffDays < 0) {
            color = "Red";
            return color;
        }
        return color;
    }

    public String getDateDifference(Date notificationTime) {
        Date currentDate = new Date();
        StringBuilder dateDifference = new StringBuilder();
        try {
            long diff = currentDate.getTime() - notificationTime.getTime();

            long diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.print(diffDays + " days ");
            if (diffDays > 0) {
                dateDifference.append(diffDays + " Days ago");
            } else {
                dateDifference.append("Today");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateDifference.toString();
    }

//     public boolean notifyDocumentUpload(Long userId, ExpiryDocumentInfo expiryDocumentInfo) {


//         UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
// 	        Set<UserProfileInfo> notifierInfos = new HashSet<UserProfileInfo>();
// 	        List<UserNotificationInfo> userNotificationInfos = new ArrayList<UserNotificationInfo>();
// 	        if (userProfileInfo != null && !userProfileInfo.getUserName().isEmpty()) {
// 	            RoleInfo roleInfo = userProfileInfo.getRoleId();
// 	            if (roleInfo.getRoleName().equals(Role.ShipMaster)) {
// 	                ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
// 	                notifierInfos = shipProfileInfo.getTechMasters();
// 	                if (shipProfileInfo.getCommercialMasters() != null)
// 	                	notifierInfos = shipProfileInfo.getCommercialMasters();
// 	            } else if (roleInfo.getRoleName().equals(Role.TechManager)) {
// 	                ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
// 	                if (shipProfileInfo.getShipMaster() != null)
// 	                	notifierInfos.add(shipProfileInfo.getShipMaster());
// 	                if (shipProfileInfo.getCommercialMasters() != null)
// 	                	notifierInfos = shipProfileInfo.getCommercialMasters();
// 	            }
// 	        }
//        List<UserNotificationInfo> userNotificat = new ArrayList<UserNotificationInfo>();
//        for (UserProfileInfo userForNotification : notifierInfos) {
//        	if (expiryDocumentInfo != null && expiryDocumentInfo.getId() != null) {
//                UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
//                userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
//                userNotificationInfo.setNotifiedTo(userForNotification);
//                userNotificationInfo.setNotificationTime(new Date());
//                userNotificationInfo.setDescription(String.format(env.getProperty("document.approve.success"), expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName()));
//                userNotificationInfo.setNotificationType("Approval page");
//                userNotificationInfo.setReadStatus(0);
//                userNotificationInfo.setColor("Green");
//                userNotificationInfos.add(userNotificationInfo);
//            }
// 		}

//        if(userNotificationInfos !=null && userNotificationInfos.size() > 0) {
//        	userNotificationInfos = userNotificationRepository.save(userNotificationInfos);
//            if (userNotificationInfos != null && userNotificationInfos.size() > 0 ) {
//                return true;
//            }
//        }


//        return false;

//    }
// public boolean notifyDocumentUpload(Long userId, ExpiryDocumentInfo expiryDocumentInfo) {

//     UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
//     Set<UserProfileInfo> notifierInfos = new HashSet<UserProfileInfo>();
//     List<UserNotificationInfo> userNotificationInfos = new ArrayList<UserNotificationInfo>();

//     if (userProfileInfo != null && !userProfileInfo.getUserName().isEmpty()) {
//         RoleInfo roleInfo = userProfileInfo.getRoleId();

//         // ---------------------------------------------------------
//         // FIX APPLIED: Logic for ShipMaster
//         // ---------------------------------------------------------
//         if (roleInfo.getRoleName().equals(Role.ShipMaster)) {
//             ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
//             // Correctly add TechMasters without overwriting
//             if (shipProfileInfo.getTechMasters() != null) {
//                 notifierInfos.addAll(shipProfileInfo.getTechMasters());
//             }
//         }
//         // ---------------------------------------------------------
//         // FIX APPLIED: Logic for TechManager
//         // ---------------------------------------------------------
//         else if (roleInfo.getRoleName().equals(Role.TechManager)) {
//             ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
//             // Correctly add ShipMaster without overwriting
//             if (shipProfileInfo.getShipMaster() != null) {
//                 notifierInfos.add(shipProfileInfo.getShipMaster());
//             }
//         }
//     }

//     // Iterate over the identified notifiers and create notification objects
//     for (UserProfileInfo userForNotification : notifierInfos) {
//         if (expiryDocumentInfo != null && expiryDocumentInfo.getId() != null) {
//             UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
//             userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
//             userNotificationInfo.setNotifiedTo(userForNotification);
//             userNotificationInfo.setNotificationTime(new Date());
//             userNotificationInfo.setDescription(String.format(env.getProperty("document.approve.success"), expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName()));
//             userNotificationInfo.setNotificationType("Approval page");
//             userNotificationInfo.setReadStatus(0);
//             userNotificationInfo.setColor("Green");
//             userNotificationInfos.add(userNotificationInfo);
//         }
//     }

//     // Save notifications to the database
//     if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
//         userNotificationInfos = userNotificationRepository.save(userNotificationInfos);
//         if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
//             return true;
//         }
//     }

//     return false;
// }
public boolean notifyDocumentUpload(Long userId, ExpiryDocumentInfo expiryDocumentInfo) {

    UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
    // Use a Set to avoid duplicate notifications if a user has multiple roles
    Set<UserProfileInfo> notifierInfos = new HashSet<UserProfileInfo>();
    List<UserNotificationInfo> userNotificationInfos = new ArrayList<UserNotificationInfo>();

    if (userProfileInfo != null && !userProfileInfo.getUserName().isEmpty()) {
        RoleInfo roleInfo = userProfileInfo.getRoleId();
        ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();

        if (shipProfileInfo != null) {
            // ---------------------------------------------------------
            // SCENARIO 1: Uploader is ShipMaster
            // Notify: Tech Managers + Commercial Managers
            // ---------------------------------------------------------
            if (roleInfo.getRoleName().equals(Role.ShipMaster)) {

                // Add Tech Managers
                if (shipProfileInfo.getTechMasters() != null) {
                    notifierInfos.addAll(shipProfileInfo.getTechMasters());
                }

                // Add Commercial Managers
                if (shipProfileInfo.getCommercialMasters() != null) {
                    notifierInfos.addAll(shipProfileInfo.getCommercialMasters());
                }
            }
            // ---------------------------------------------------------
            // SCENARIO 2: Uploader is TechManager
            // Notify: Ship Master + Commercial Managers
            // ---------------------------------------------------------
            else if (roleInfo.getRoleName().equals(Role.TechManager)) {

                // Add Ship Master (Single User)
                if (shipProfileInfo.getShipMaster() != null) {
                    notifierInfos.add(shipProfileInfo.getShipMaster());
                }

                // Add Commercial Managers
                if (shipProfileInfo.getCommercialMasters() != null) {
                    notifierInfos.addAll(shipProfileInfo.getCommercialMasters());
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Create Notification Objects
    // ---------------------------------------------------------
    for (UserProfileInfo userForNotification : notifierInfos) {
        // Validate userForNotification is not null before processing
        if (userForNotification != null && expiryDocumentInfo != null && expiryDocumentInfo.getId() != null) {
            UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
            userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
            userNotificationInfo.setNotifiedTo(userForNotification);
            userNotificationInfo.setNotificationTime(new Date());

            // Safe string formatting
            String docHolderName = (expiryDocumentInfo.getDocumentHolderInfo() != null)
                                    ? expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName()
                                    : "Unknown";

            userNotificationInfo.setDescription(String.format(env.getProperty("document.approve.success"), docHolderName));
            userNotificationInfo.setNotificationType("Approval page");
            userNotificationInfo.setReadStatus(0);
            userNotificationInfo.setColor("Green");

            userNotificationInfos.add(userNotificationInfo);
        }
    }

    // ---------------------------------------------------------
    // Save to DB
    // ---------------------------------------------------------
    if (userNotificationInfos.size() > 0) {
        List<UserNotificationInfo> savedInfos = userNotificationRepository.save(userNotificationInfos);
        if (savedInfos != null && savedInfos.size() > 0) {
            return true;
        }
    }

    return false;
}
    public boolean requestUserNotification(Long userId, Long requestId) {
        RequestUserInfo requestUserInfo = requestUserRepository.findOne(requestId);
        RoleInfo roleInfo = roleInfoRepository.findByRoleName(Role.Admin);
        List<UserProfileInfo> userProfileInfos = userProfileRepository.findByRoleId(roleInfo);
        if (userProfileInfos != null && userProfileInfos.size() > 0) {
            for (UserProfileInfo userProfileInfo : userProfileInfos) {
                UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
                userNotificationInfo.setNotifiedTo(userProfileInfo);
                userNotificationInfo.setRequestUserInfo(requestUserInfo);
                userNotificationInfo.setDescription(String.format(env.getProperty("request.user"), userProfileInfo.getUserName()));
                userNotificationInfo.setNotificationType("User request page");
                userNotificationInfo.setNotificationTime(new Date());
                userNotificationInfo.setColor("Green");
                userNotificationInfo.setReadStatus(0);
                userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
                if (userNotificationInfo.getId() != null) {

                }
            }
            return true;
        }

           /* UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
            userNotificationInfo.setNotifiedTo(userProfileInfo);
            userNotificationInfo.setRequestUserInfo(requestUserInfo);
            userNotificationInfo.setDescription("Approve or reject user request");
            userNotificationInfo.setNotificationTime(new Date());
            userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
            if (userNotificationInfo.getId() != null) {
                return true;
        }*/
        return false;
    }

    public String findNotificationColornew(Date expiryDate,Date startDate) {
        String color = "grey";
        long diff = startDate.getTime() - expiryDate.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffDays >= 30) {
            color = "Green";
            return color;
        } else if (diffDays < 30 && diffDays >= 0) {
            color = "Yellow";
            return color;
        } else if (diffDays < 0) {
            color = "Red";
            return color;
        }
        return color;
    }

	public boolean taskDetailNotification(TaskDetailsInfo taskDetailsInfo, UserProfileInfo userProfileInfo) {
        if (taskDetailsInfo != null && userProfileInfo != null) {
            UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
            userNotificationInfo.setTaskDetailsInfo(taskDetailsInfo);
            userNotificationInfo.setNotifiedTo(userProfileInfo);
            userNotificationInfo.setDescription(String.format(env.getProperty("task.assign.status"),taskDetailsInfo.getTaskName()));
            userNotificationInfo.setNotificationType("Task page");
            userNotificationInfo.setNotificationTime(new Date());
            userNotificationInfo.setColor(findNotificationColornew(taskDetailsInfo.getCreateDate(), taskDetailsInfo.getEndDate()));
            userNotificationInfo.setReadStatus(0);
            if (taskDetailsInfo.getShipProfileInfo() != null)
               userNotificationInfo.setVesselId(taskDetailsInfo.getShipProfileInfo().getId());
            userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
            if (userNotificationInfo.getId() != null) {
                return true;
            }
        }
        return false;
    }

	  public boolean notifyDocumentChanges(Long userId, ExpiryDocumentInfo expiryDocumentInfo) {

	        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
	        Set<UserProfileInfo> notifierInfos = new HashSet<UserProfileInfo>();
	        List<UserNotificationInfo> userNotificationInfos = new ArrayList<UserNotificationInfo>();
	        if (userProfileInfo != null && !userProfileInfo.getUserName().isEmpty()) {
	            RoleInfo roleInfo = userProfileInfo.getRoleId();
	            if (roleInfo.getRoleName().equals(Role.ShipMaster)) {
	                ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
	                notifierInfos = shipProfileInfo.getTechMasters();
	            } else if (roleInfo.getRoleName().equals(Role.TechManager)) {
	                ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
	                notifierInfos.add(shipProfileInfo.getShipMaster());
	            }
	        }
	        for (UserProfileInfo userForNotification : notifierInfos) {
		        if (expiryDocumentInfo != null && expiryDocumentInfo.getId() != null) {
		            UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
		            userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
		            userNotificationInfo.setNotifiedTo(userForNotification);
		            userNotificationInfo.setNotificationTime(new Date());
		            userNotificationInfo.setDescription(String.format(env.getProperty("notification.approve.status"),expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName(),expiryDocumentInfo.getDocumentStatus()));
		            userNotificationInfo.setReadStatus(0);
		            if (expiryDocumentInfo.getDocumentStatus().equals(env.getProperty("document.status.reject"))){
		            	userNotificationInfo.setColor("Red");
		            }else if (expiryDocumentInfo.getDocumentStatus().equals(env.getProperty("document.status.approve"))){
		            	userNotificationInfo.setColor("Green");
		            }
		            userNotificationInfo.setNotificationType("Approval page");
		            userNotificationInfos.add(userNotificationInfo);
		        }
	        }

	        if(userNotificationInfos !=null && userNotificationInfos.size() > 0) {
	        	userNotificationInfos = userNotificationRepository.save(userNotificationInfos);
	            if (userNotificationInfos != null && userNotificationInfos.size() > 0 ) {
	                return true;
	            }
	        }

	        return false;

	    }

	  public boolean notificationTaskStatusChanges(TaskDetailsInfo taskDetailInfo,TaskDTO taskDTO) {

		 UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
		 if (taskDTO.getCreatedBy() != null){
			UserProfileInfo userProfileInfo = userProfileRepository.findById(taskDTO.getCreatedBy());
			userNotificationInfo.setNotifiedTo(taskDetailInfo.getCreatedBy());
			TaskStatusInfo taskStatusInfo = taskStatusRepository.findOne(taskDTO.getTaskStatusId());
			if (taskStatusInfo != null) {
				if (taskStatusInfo.getTaskStatus().equals(env.getProperty("document.status.reject"))) {
					userNotificationInfo.setColor("Red");
					userNotificationInfo.setDescription(String.format(env.getProperty("task.rejected"), userProfileInfo.getUserName()));
				} else if (taskStatusInfo.getTaskStatus().equals(env.getProperty("task.status.completed"))) {
					userNotificationInfo.setColor("Green");
					userNotificationInfo.setDescription(String.format(env.getProperty("task.completed"), userProfileInfo.getUserName()));
				}
				 else if (taskStatusInfo.getTaskStatus().equals(env.getProperty("task.status.inProgress"))) {
						userNotificationInfo.setColor("Yellow");
						userNotificationInfo.setDescription(String.format(env.getProperty("task.accepted"), userProfileInfo.getUserName()));
					}
				userNotificationInfo.setNotificationTime(new Date());
				userNotificationInfo.setReadStatus(0);
				userNotificationInfo.setNotificationType("Task page");
				userNotificationInfo.setTaskDetailsInfo(taskDetailInfo);
				userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
				if (userNotificationInfo.getId() != null) {
					return true;
         }
		 }
		 }
		return false;
	  }

	public boolean requestUserNotificationChanges(RequestUserDTO requestUserDTO, RequestUserInfo requestUserInfo) {

		UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
		if (requestUserDTO.getLoginId() != null){
			UserProfileInfo userProfileInfo = userProfileRepository.findById(requestUserDTO.getLoginId());
			if (requestUserDTO.getRequestUserStatus().equalsIgnoreCase("Rejected")){
				userNotificationInfo.setDescription(String.format(env.getProperty("request.user.rejected"), userProfileInfo.getUserName()));
				userNotificationInfo.setColor("Red");
			}
			else{
				userNotificationInfo.setDescription(String.format(env.getProperty("request.user.accepted"), userProfileInfo.getUserName()));
				userNotificationInfo.setColor("Green");
			}
			userNotificationInfo.setNotificationTime(new Date());
			userNotificationInfo.setNotificationType("User request page");
			userNotificationInfo.setReadStatus(0);
			userNotificationInfo.setNotifiedTo(requestUserInfo.getRequesterInfo());
			userNotificationInfo.setRequestUserInfo(requestUserInfo);
			userNotificationInfo = userNotificationRepository.saveAndFlush(userNotificationInfo);
			if (userNotificationInfo != null)
				return true;

		}
		return false;
	}

	public boolean notificationToUpdateNewUserToTask(Set<AssignedUserTaskInfo> contactList, Long loginId) {

		for (AssignedUserTaskInfo assignedUserTaskInfo : contactList){
			UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
			userNotificationInfo.setNotifiedTo(assignedUserTaskInfo.getUserProfileInfo());
			userNotificationInfo.setDescription(String.format(env.getProperty("task.assign.status"),assignedUserTaskInfo.getTaskInfo().getTaskName()));
			userNotificationInfo.setTaskDetailsInfo(assignedUserTaskInfo.getTaskInfo());
            userNotificationInfo.setNotificationType("Task page");
            userNotificationInfo.setNotificationTime(new Date());
            userNotificationInfo.setColor("Green");
            userNotificationInfo.setReadStatus(0);
            userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
		}
		return true;
	}

	public boolean notificationToRemoveUserToTask(List<AssignedUserTaskInfo> removeContactInfoList, Long loginId) {
		for (AssignedUserTaskInfo assignedUserTaskInfo : removeContactInfoList){
			UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
			userNotificationInfo.setNotifiedTo(assignedUserTaskInfo.getUserProfileInfo());
			userNotificationInfo.setDescription(String.format(env.getProperty("task.remove.status"),assignedUserTaskInfo.getTaskInfo().getTaskName()));
			userNotificationInfo.setTaskDetailsInfo(assignedUserTaskInfo.getTaskInfo());
            userNotificationInfo.setNotificationType("Task page");
            userNotificationInfo.setNotificationTime(new Date());
            userNotificationInfo.setColor("Red");
            userNotificationInfo.setReadStatus(0);

            userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
		}
		return true;
	}

	public boolean notificationToAddGeoLocation(UserProfileInfo userProfileInfo){
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findByShipMaster(userProfileInfo);
		if (shipProfileInfo != null){
			Set<UserProfileInfo> userProfileToTechmaster = shipProfileInfo.getTechMasters();
			Set<UserProfileInfo> userProfileToCommercialManager = shipProfileInfo.getCommercialMasters();
			for (UserProfileInfo userProfileInfo2 : userProfileToTechmaster){
				UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
				userNotificationInfo.setVesselId(shipProfileInfo.getId());
				userNotificationInfo.setColor("Green");
				userNotificationInfo.setDescription(env.getProperty("ship.geo.notification"));
				userNotificationInfo.setNotificationTime(new Date());
				userNotificationInfo.setNotificationType("GeoLocationUpload");
				userNotificationInfo.setNotifiedTo(userProfileInfo2);
				userNotificationInfo.setReadStatus(0);
				userNotificationInfo = userNotificationRepository.save(userNotificationInfo);
			}
			for (UserProfileInfo userProfileInfo2 : userProfileToCommercialManager){
				UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
				userNotificationInfo.setVesselId(shipProfileInfo.getId());
				userNotificationInfo.setColor("Green");
				userNotificationInfo.setDescription(env.getProperty("ship.geo.notification"));
				userNotificationInfo.setNotificationTime(new Date());
				userNotificationInfo.setNotificationType("GeoLocationUpload");
				userNotificationInfo.setNotifiedTo(userProfileInfo2);
				userNotificationInfo.setReadStatus(0);
				userNotificationInfo = userNotificationRepository.saveAndFlush(userNotificationInfo);
			}
			return true;
		}
		return false;

	}

	 public boolean notifyDocumentEdit(Long userId, ExpiryDocumentInfo expiryDocumentInfo) {


	        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
		        Set<UserProfileInfo> notifierInfos = new HashSet<UserProfileInfo>();
		        List<UserNotificationInfo> userNotificationInfos = new ArrayList<UserNotificationInfo>();
		        if (userProfileInfo != null && !userProfileInfo.getUserName().isEmpty()) {
		            RoleInfo roleInfo = userProfileInfo.getRoleId();
		            if (roleInfo.getRoleName().equals(Role.ShipMaster)) {
		                ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
		                notifierInfos = shipProfileInfo.getTechMasters();
		                if (shipProfileInfo.getCommercialMasters() != null)
		                	notifierInfos = shipProfileInfo.getCommercialMasters();
		            } else if (roleInfo.getRoleName().equals(Role.TechManager)) {
		                ShipProfileInfo shipProfileInfo = expiryDocumentInfo.getShipProfileInfo();
		                if (shipProfileInfo.getShipMaster() != null)
		                	notifierInfos.add(shipProfileInfo.getShipMaster());
		                if (shipProfileInfo.getCommercialMasters() != null)
		                	notifierInfos = shipProfileInfo.getCommercialMasters();
		            }
		        }
	       List<UserNotificationInfo> userNotificat = new ArrayList<UserNotificationInfo>();
	       for (UserProfileInfo userForNotification : notifierInfos) {
	       	if (expiryDocumentInfo != null && expiryDocumentInfo.getId() != null) {
	               UserNotificationInfo userNotificationInfo = new UserNotificationInfo();
	               userNotificationInfo.setExpiryDocumentInfo(expiryDocumentInfo);
	               userNotificationInfo.setNotifiedTo(userForNotification);
	               userNotificationInfo.setNotificationTime(new Date());
	               userNotificationInfo.setDescription(String.format(env.getProperty("document.approve.edit.success"), expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName()));
	               userNotificationInfo.setNotificationType("Approval page");
	               userNotificationInfo.setReadStatus(0);
	               userNotificationInfo.setColor("Green");
	               userNotificationInfos.add(userNotificationInfo);
	           }
			}

	       if(userNotificationInfos !=null && userNotificationInfos.size() > 0) {
	       	userNotificationInfos = userNotificationRepository.save(userNotificationInfos);
	           if (userNotificationInfos != null && userNotificationInfos.size() > 0 ) {
	               return true;
	           }
	       }


	       return false;

	   }
}
