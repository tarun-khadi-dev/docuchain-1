// package com.dapp.docuchain.service.impl;

// import com.dapp.docuchain.dto.DeletedHistoryDTO;
// import com.dapp.docuchain.dto.ExpiryDocumentDTO;
// import com.dapp.docuchain.dto.UserDTO;
// import com.dapp.docuchain.model.*;
// import com.dapp.docuchain.repository.ShipProfileRepository;
// import com.dapp.docuchain.repository.UserNotificationRepository;
// import com.dapp.docuchain.repository.UserProfileRepository;
// import com.dapp.docuchain.service.NotificationService;
// import com.dapp.docuchain.utility.CommonMethodsUtility;
// import com.dapp.docuchain.utility.NotificationUtility;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
// import org.springframework.stereotype.Service;

// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Calendar;
// import java.util.Date;
// import java.util.List;

// @Service
// public class NotificationServiceImpl implements NotificationService {

//     private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

//     @Autowired
//     private Environment env;

//     @Autowired
//     private UserProfileRepository userProfileRepository;

//     @Autowired
//     private UserNotificationRepository userNotificationRepository;

//     @Autowired
//     private NotificationUtility notificationUtility;

//     @Autowired
//     private CommonMethodsUtility commonMethodsUtility;

//     @Autowired
//     private ShipProfileRepository shipProfileRepository;

//     @Override
//     public List<UserDTO> getNotification(UserDTO userDTObj) {

//         UserProfileInfo userProfileInfo = userProfileRepository.findOne(userDTObj.getUserId());
//         List<UserDTO> userDTOS = new ArrayList<UserDTO>();
//         LocalDate currentDate = LocalDate.now();
//         List<UserNotificationInfo> userNotificationInfos = new ArrayList<>();
//         LocalDate lastMonth = currentDate.minusDays(30);
//         LocalDate lastWeek = currentDate.minusDays(7);
//         LocalDate lastWeekOfWeek = currentDate.minusDays(14);
//         LocalDate lastMonthOfMonth = currentDate.minusDays(60);

//         if (userProfileInfo != null) {
//             // FIX: Added null check for getFilterByDay() to prevent NullPointerException
//             if (userDTObj.getFilterByDay() != null) {
//                 if (userDTObj.getFilterByDay().equalsIgnoreCase("Renewel")) {
//                     userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo, java.sql.Date.valueOf(lastWeek), java.sql.Date.valueOf(currentDate));
//                 } else if (userDTObj.getFilterByDay().equalsIgnoreCase("Lastweek")) {
//                     userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo, java.sql.Date.valueOf(lastWeekOfWeek), java.sql.Date.valueOf(lastWeek));
//                 } else if (userDTObj.getFilterByDay().equalsIgnoreCase("Lastmonth")) {
//                     userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo, java.sql.Date.valueOf(lastMonthOfMonth), java.sql.Date.valueOf(lastMonth));
//                 } else if (userDTObj.getFilterByDay().equalsIgnoreCase("Older")) {
//                     userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBefore(userProfileInfo, java.sql.Date.valueOf(lastMonthOfMonth));
//                 }
//             } else {
//                 // OPTIONAL: If no filter is provided, you might want to load all or recent notifications.
//                 // For now, I'm leaving this consistent with your original logic (returns empty list if null).
//                 // userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
//             }

//             if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
//                 LOGGER.info("Is this Coming here");
//                 for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
//                     LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
//                     UserDTO userDTO = new UserDTO();

//                     if (userNotificationInfo.getVesselId() != null) {
//                         ShipProfileInfo shipProfileInfo = shipProfileRepository.findById(userNotificationInfo.getVesselId());
//                         if (shipProfileInfo != null) { // Safety check for shipProfileInfo
//                             userDTO.setVesselName(shipProfileInfo.getShipName());
//                             userDTO.setImo(shipProfileInfo.getIMO());
//                             if (shipProfileInfo.getShipProfilePicPath() != null)
//                                 userDTO.setShipProfilePath(env.getProperty("picture.path") + shipProfileInfo.getShipProfilePicPath());
//                         }
//                     }

//                     userDTO.setNotificationMessage(userNotificationInfo.getDescription());
//                     userDTO.setNotificationId(userNotificationInfo.getId());
//                     userDTO.setNotificationType(userNotificationInfo.getNotificationType());
//                     userDTO.setNotifyDate(userNotificationInfo.getNotificationTime());

//                     RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
//                     ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
//                     TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

//                     if (requestUserInfo != null) {
//                         LOGGER.info("Request User");
//                         if(requestUserInfo.getUserRequestShipProfile() != null) {
//                              userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
//                              userDTO.setShipId(requestUserInfo.getUserRequestShipProfile().getId());
//                         }
//                         userDTO.setColor(userNotificationInfo.getColor());
//                         userDTO.setNotificationType(userNotificationInfo.getNotificationType());
//                     }
//                     if (expiryDocumentInfo != null) {
//                         LOGGER.info("Expiry Document");
//                         // Date expiryDate = expiryDocumentInfo.getExpiryDate(); // Unused variable
//                         if(expiryDocumentInfo.getShipProfileInfo() != null) {
//                             userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
//                             userDTO.setShipId(expiryDocumentInfo.getShipProfileInfo().getId());
//                             userDTO.setVesselName(expiryDocumentInfo.getShipProfileInfo().getShipName());
//                             userDTO.setImo(expiryDocumentInfo.getShipProfileInfo().getIMO());
//                         }
//                         userDTO.setColor(userNotificationInfo.getColor());
//                         userDTO.setNotificationType(userNotificationInfo.getNotificationType());
//                         userDTO.setExpiryDocumentId(expiryDocumentInfo.getId());
//                         userDTO.setNotificationType(env.getProperty("notification.document.typenew"));
//                         userDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
//                     }
//                     if (taskDetailsInfo != null) {
//                         userDTO.setNotificationName(taskDetailsInfo.getTaskName());
//                         userDTO.setNotificationType(env.getProperty("notification.task.typenew"));
//                         userDTO.setColor(userNotificationInfo.getColor());
//                         userDTO.setTaskId(taskDetailsInfo.getId());
//                         userDTO.setTaskName(taskDetailsInfo.getTaskName());
//                         if (taskDetailsInfo.getShipProfileInfo() != null) {
//                             userDTO.setVesselName(taskDetailsInfo.getShipProfileInfo().getShipName());
//                             userDTO.setImo(taskDetailsInfo.getShipProfileInfo().getIMO());
//                         }

//                         LOGGER.info("Task Details");
//                     }
//                     userDTOS.add(userDTO);
//                 }
//             }
//         }
//         return userDTOS;
//     }

//     @Override
//     public List<UserDTO> getNotificatonCount(Long userId) {

//         UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
//         List<UserDTO> userDTOS = new ArrayList<UserDTO>();
//         if (userProfileInfo != null) {
//             List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToAndReadStatusOrderByNotificationTimeDesc(userProfileInfo, 0);
//             if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
//                 LOGGER.info("Is this notification count Coming here");
//                 for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
//                     LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
//                     UserDTO userDTO = new UserDTO();
//                     userDTO.setNotificationMessage(userNotificationInfo.getDescription());

//                     RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
//                     ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
//                     TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

//                     if (requestUserInfo != null) {
//                         LOGGER.info("Request User");
//                         if(requestUserInfo.getUserRequestShipProfile() != null) {
//                              userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
//                         }
//                         userDTO.setColor("Green");
//                         userDTO.setNotificationType(env.getProperty("notification.user.type"));
//                     }
//                     if (expiryDocumentInfo != null) {
//                         LOGGER.info("Expiry Document");
//                         if(expiryDocumentInfo.getShipProfileInfo() != null) {
//                              userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
//                         }
//                         userDTO.setColor("Green");
//                         userDTO.setNotificationType(env.getProperty("notification.document.type"));
//                     }
//                     if (taskDetailsInfo != null) {
//                         userDTO.setNotificationName(taskDetailsInfo.getTaskName());
//                         userDTO.setNotificationType(env.getProperty("notification.task.type"));
//                         userDTO.setColor("Green");
//                         LOGGER.info("Task Details");
//                     }
//                     userDTOS.add(userDTO);
//                 }
//             }
//         }
//         return userDTOS;
//     }

//     @Override
//     public Boolean setViewedNotification(Long userId) {
//         UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
//         if (userProfileInfo != null) {
//             List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
//             if (userNotificationInfos != null && userNotificationInfos.size() > 0) {

//                 for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
//                     userNotificationInfo.setReadStatus(1);
//                     userNotificationRepository.save(userNotificationInfo);
//                 }
//                 return true;
//             }
//         }
//         return false;
//     }

//     @Override
//     public boolean deleteNotification(UserDTO userDTO) {
//         try {
//             UserNotificationInfo userNotificationInfo = userNotificationRepository.findOne(userDTO.getNotificationId());
//             if (userNotificationInfo != null) {
//                 DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
//                 deletedHistoryDTO.setObjectId(userNotificationInfo.getId());
//                 deletedHistoryDTO.setObjectOne(userNotificationInfo.getDescription());
//                 deletedHistoryDTO.setObjectTwo(userNotificationInfo.getNotificationType());
//                 // DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
//                 userNotificationRepository.delete(userNotificationInfo);
//                 return true;
//             }
//         } catch (Exception e) {
//             LOGGER.error("Error deleting notification", e);
//             return false;
//         }
//         return false;
//     }

//     @Override
//     public boolean deleteAllNotification(UserDTO userDTOs) {
//         try {
//             if (userDTOs != null) {
//                 UserProfileInfo userProfileInfo = userProfileRepository.findById(userDTOs.getUserId());
//                 List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedTo(userProfileInfo);
//                 if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
//                     for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
//                         DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
//                         deletedHistoryDTO.setObjectId(userNotificationInfo.getId());
//                         deletedHistoryDTO.setObjectOne(userNotificationInfo.getDescription());
//                         deletedHistoryDTO.setObjectTwo(userNotificationInfo.getNotificationType());
//                         //DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
//                         userNotificationRepository.delete(userNotificationInfo);
//                     }
//                     return true;
//                 }
//             }
//         } catch (Exception e) {
//             LOGGER.error("Error deleting all notifications", e);
//             return false;
//         }
//         return false;
//     }

//     @Override
//     public List<UserDTO> getNotificationByCategory(ExpiryDocumentDTO expiryDocumentDTO) {

//         UserProfileInfo userProfileInfo = userProfileRepository.findOne(expiryDocumentDTO.getUserId());
//         List<UserDTO> userDTOS = new ArrayList<UserDTO>();
//         if (userProfileInfo != null) {
//             List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
//             if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
//                 LOGGER.info("Is this Coming here");
//                 for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
//                     if (userNotificationInfo.getNotificationType().equals(expiryDocumentDTO.getCategory())) {
//                         LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
//                         UserDTO userDTO = new UserDTO();
//                         userDTO.setNotificationMessage(userNotificationInfo.getDescription());
//                         userDTO.setNotificationId(userNotificationInfo.getId());

//                         RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
//                         ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
//                         TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

//                         if (requestUserInfo != null) {
//                             LOGGER.info("Request User");
//                             if (requestUserInfo.getUserRequestShipProfile() != null) {
//                                 userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
//                                 userDTO.setShipId(requestUserInfo.getUserRequestShipProfile().getId());
//                             }
//                             userDTO.setColor(userNotificationInfo.getColor());
//                             userDTO.setNotificationType(userNotificationInfo.getNotificationType());
//                         }
//                         if (expiryDocumentInfo != null) {
//                             LOGGER.info("Expiry Document");
//                             if (expiryDocumentInfo.getShipProfileInfo() != null) {
//                                 userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
//                                 userDTO.setShipId(expiryDocumentInfo.getShipProfileInfo().getId());
//                             }
//                             userDTO.setColor(userNotificationInfo.getColor());
//                             userDTO.setNotificationType(userNotificationInfo.getNotificationType());
//                         }
//                         if (taskDetailsInfo != null) {
//                             userDTO.setNotificationName(taskDetailsInfo.getTaskName());
//                             userDTO.setNotificationType(userNotificationInfo.getNotificationType());
//                             userDTO.setColor(userNotificationInfo.getColor());
//                             LOGGER.info("Task Details");
//                         }
//                         userDTOS.add(userDTO);
//                     }
//                 }
//             }
//         }
//         return userDTOS;
//     }

//     @Override
//     public boolean updateSnooze(UserDTO userDTO) {
//         UserNotificationInfo userNotificationInfo = userNotificationRepository.findOne(userDTO.getNotificationId());
//         if (userNotificationInfo != null) {
//             int daysForIncrement = 0;
//             if (userDTO.getSnooze() == 1)
//                 daysForIncrement = 1;
//             else if (userDTO.getSnooze() == 2)
//                 daysForIncrement = 7;
//             else if (userDTO.getSnooze() == 3)
//                 daysForIncrement = 14;
//             else if (userDTO.getSnooze() == 4)
//                 daysForIncrement = 31;

//             // Fixed potential null pointer on getDate() if time is null, though unlikely in DB
//             if (userNotificationInfo.getNotificationTime() != null) {
//                 String currentDate = userNotificationInfo.getNotificationTime().toString();
//                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                 Calendar c = Calendar.getInstance();
//                 try {
//                     c.setTime(sdf.parse(currentDate));
//                     c.add(Calendar.DATE, daysForIncrement);
//                     currentDate = sdf.format(c.getTime());
//                     SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
//                     Date date2 = formatter2.parse(currentDate);
//                     userNotificationInfo.setNotificationTime(date2);
//                     userNotificationRepository.saveAndFlush(userNotificationInfo);
//                     System.out.println("Finally enter in the snooze option" + currentDate + date2);
//                 } catch (ParseException e) {
//                     e.printStackTrace();
//                 }
//             }
//         }
//         return true;
//     }
// }

package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.repository.UserNotificationRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.NotificationService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.NotificationUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private NotificationUtility notificationUtility;

    @Autowired
    private CommonMethodsUtility commonMethodsUtility;

    @Autowired
    private ShipProfileRepository shipProfileRepository;

    @Override
    public List<UserDTO> getNotification(UserDTO userDTObj) {
        // 🔥 FIX 1: Null check added here
        if (userDTObj == null || userDTObj.getUserId() == null) {
            LOGGER.warn("UserId is null in getNotification()");
            return new ArrayList<>();
        }

        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userDTObj.getUserId());
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        LocalDate currentDate = LocalDate.now();
        List<UserNotificationInfo> userNotificationInfos = new ArrayList<>();
        LocalDate lastMonth = currentDate.minusDays(30);
        LocalDate lastWeek = currentDate.minusDays(7);
        LocalDate lastWeekOfWeek = currentDate.minusDays(14);
        LocalDate lastMonthOfMonth = currentDate.minusDays(60);

        if (userProfileInfo != null) {
            // FIX: Added null check for getFilterByDay() to prevent NullPointerException
            if (userDTObj.getFilterByDay() != null) {
                if (userDTObj.getFilterByDay().equalsIgnoreCase("Renewel")) {
                    userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo, java.sql.Date.valueOf(lastWeek), java.sql.Date.valueOf(currentDate));
                } else if (userDTObj.getFilterByDay().equalsIgnoreCase("Lastweek")) {
                    userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo, java.sql.Date.valueOf(lastWeekOfWeek), java.sql.Date.valueOf(lastWeek));
                } else if (userDTObj.getFilterByDay().equalsIgnoreCase("Lastmonth")) {
                    userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBetween(userProfileInfo, java.sql.Date.valueOf(lastMonthOfMonth), java.sql.Date.valueOf(lastMonth));
                } else if (userDTObj.getFilterByDay().equalsIgnoreCase("Older")) {
                    userNotificationInfos = userNotificationRepository.findByNotifiedToAndNotificationTimeBefore(userProfileInfo, java.sql.Date.valueOf(lastMonthOfMonth));
                }
            } else {
                // OPTIONAL: If no filter is provided, you might want to load all or recent notifications.
                // For now, I'm leaving this consistent with your original logic (returns empty list if null).
                // userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
            }

            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                LOGGER.info("Is this Coming here");
                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                    LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
                    UserDTO userDTO = new UserDTO();

                    if (userNotificationInfo.getVesselId() != null) {
                        ShipProfileInfo shipProfileInfo = shipProfileRepository.findById(userNotificationInfo.getVesselId());
                        if (shipProfileInfo != null) { // Safety check for shipProfileInfo
                            userDTO.setVesselName(shipProfileInfo.getShipName());
                            userDTO.setImo(shipProfileInfo.getIMO());
                            if (shipProfileInfo.getShipProfilePicPath() != null)
                                userDTO.setShipProfilePath(env.getProperty("picture.path") + shipProfileInfo.getShipProfilePicPath());
                        }
                    }

                    userDTO.setNotificationMessage(userNotificationInfo.getDescription());
                    userDTO.setNotificationId(userNotificationInfo.getId());
                    userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                    userDTO.setNotifyDate(userNotificationInfo.getNotificationTime());

                    RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
                    ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
                    TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

                    if (requestUserInfo != null) {
                        LOGGER.info("Request User");
                        if(requestUserInfo.getUserRequestShipProfile() != null) {
                             userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
                             userDTO.setShipId(requestUserInfo.getUserRequestShipProfile().getId());
                        }
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                    }
                    if (expiryDocumentInfo != null) {
                        LOGGER.info("Expiry Document");
                        // Date expiryDate = expiryDocumentInfo.getExpiryDate(); // Unused variable
                        if(expiryDocumentInfo.getShipProfileInfo() != null) {
                            userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                            userDTO.setShipId(expiryDocumentInfo.getShipProfileInfo().getId());
                            userDTO.setVesselName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                            userDTO.setImo(expiryDocumentInfo.getShipProfileInfo().getIMO());
                        }
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                        userDTO.setExpiryDocumentId(expiryDocumentInfo.getId());
                        userDTO.setNotificationType(env.getProperty("notification.document.typenew"));
                        userDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
                    }
                    if (taskDetailsInfo != null) {
                        userDTO.setNotificationName(taskDetailsInfo.getTaskName());
                        userDTO.setNotificationType(env.getProperty("notification.task.typenew"));
                        userDTO.setColor(userNotificationInfo.getColor());
                        userDTO.setTaskId(taskDetailsInfo.getId());
                        userDTO.setTaskName(taskDetailsInfo.getTaskName());
                        if (taskDetailsInfo.getShipProfileInfo() != null) {
                            userDTO.setVesselName(taskDetailsInfo.getShipProfileInfo().getShipName());
                            userDTO.setImo(taskDetailsInfo.getShipProfileInfo().getIMO());
                        }

                        LOGGER.info("Task Details");
                    }
                    userDTOS.add(userDTO);
                }
            }
        }
        return userDTOS;
    }

    @Override
    public List<UserDTO> getNotificatonCount(Long userId) {
        // 🔥 FIX 2: Null check added here
        if (userId == null) {
            LOGGER.warn("UserId is null in getNotificatonCount()");
            return new ArrayList<>();
        }

        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        if (userProfileInfo != null) {
            List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToAndReadStatusOrderByNotificationTimeDesc(userProfileInfo, 0);
            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                LOGGER.info("Is this notification count Coming here");
                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                    LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
                    UserDTO userDTO = new UserDTO();
                    userDTO.setNotificationMessage(userNotificationInfo.getDescription());

                    RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
                    ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
                    TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

                    if (requestUserInfo != null) {
                        LOGGER.info("Request User");
                        if(requestUserInfo.getUserRequestShipProfile() != null) {
                             userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
                        }
                        userDTO.setColor("Green");
                        userDTO.setNotificationType(env.getProperty("notification.user.type"));
                    }
                    if (expiryDocumentInfo != null) {
                        LOGGER.info("Expiry Document");
                        if(expiryDocumentInfo.getShipProfileInfo() != null) {
                             userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                        }
                        userDTO.setColor("Green");
                        userDTO.setNotificationType(env.getProperty("notification.document.type"));
                    }
                    if (taskDetailsInfo != null) {
                        userDTO.setNotificationName(taskDetailsInfo.getTaskName());
                        userDTO.setNotificationType(env.getProperty("notification.task.type"));
                        userDTO.setColor("Green");
                        LOGGER.info("Task Details");
                    }
                    userDTOS.add(userDTO);
                }
            }
        }
        return userDTOS;
    }

    // @Override
    // public Boolean setViewedNotification(Long userId) {
    //     // 🔥 BONUS FIX: Null check added here for complete safety
    //     if (userId == null) {
    //         LOGGER.warn("UserId is null in setViewedNotification()");
    //         return false;
    //     }

    //     UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
    //     if (userProfileInfo != null) {
    //         List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
    //         if (userNotificationInfos != null && userNotificationInfos.size() > 0) {

    //             for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
    //                 userNotificationInfo.setReadStatus(1);
    //                 userNotificationRepository.save(userNotificationInfo);
    //             }
    //             return true;
    //         }
    //     }
    //     return false;
    // }
@Override
    public Boolean setViewedNotification(Long userId) {
        if (userId == null) {
            LOGGER.warn("UserId is null in setViewedNotification()");
            return false;
        }

        UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);

        if (userProfileInfo == null) {
            LOGGER.warn("User not found with ID: " + userId);
            return false; // Return false only if the user genuinely does not exist
        }

        List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);

        // REMOVED the .size() > 0 check so it returns true even if the user has 0 notifications
        if (userNotificationInfos != null && !userNotificationInfos.isEmpty()) {
            for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                userNotificationInfo.setReadStatus(1);
                userNotificationRepository.save(userNotificationInfo);
            }
        }

        return true; // Always return true if the user exists, meaning the action "succeeded"
    }
    @Override
    public boolean deleteNotification(UserDTO userDTO) {
        try {
            // 🔥 FIX 3: Null check added here
            if (userDTO == null || userDTO.getNotificationId() == null) {
                LOGGER.warn("NotificationId is null in deleteNotification()");
                return false;
            }

            UserNotificationInfo userNotificationInfo = userNotificationRepository.findOne(userDTO.getNotificationId());
            if (userNotificationInfo != null) {
                DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
                deletedHistoryDTO.setObjectId(userNotificationInfo.getId());
                deletedHistoryDTO.setObjectOne(userNotificationInfo.getDescription());
                deletedHistoryDTO.setObjectTwo(userNotificationInfo.getNotificationType());
                // DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
                userNotificationRepository.delete(userNotificationInfo);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting notification", e);
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteAllNotification(UserDTO userDTOs) {
        try {
            if (userDTOs != null) {
                // 🔥 BONUS FIX: Ensure userId is not null before using findById
                if (userDTOs.getUserId() == null) {
                    LOGGER.warn("UserId is null in deleteAllNotification()");
                    return false;
                }

                UserProfileInfo userProfileInfo = userProfileRepository.findById(userDTOs.getUserId());
                List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedTo(userProfileInfo);
                if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                    for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                        DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
                        deletedHistoryDTO.setObjectId(userNotificationInfo.getId());
                        deletedHistoryDTO.setObjectOne(userNotificationInfo.getDescription());
                        deletedHistoryDTO.setObjectTwo(userNotificationInfo.getNotificationType());
                        //DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
                        userNotificationRepository.delete(userNotificationInfo);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting all notifications", e);
            return false;
        }
        return false;
    }

    @Override
    public List<UserDTO> getNotificationByCategory(ExpiryDocumentDTO expiryDocumentDTO) {
        // 🔥 BONUS FIX: Null check added here for complete safety
        if (expiryDocumentDTO == null || expiryDocumentDTO.getUserId() == null) {
            LOGGER.warn("UserId is null in getNotificationByCategory()");
            return new ArrayList<>();
        }

        UserProfileInfo userProfileInfo = userProfileRepository.findOne(expiryDocumentDTO.getUserId());
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        if (userProfileInfo != null) {
            List<UserNotificationInfo> userNotificationInfos = userNotificationRepository.findByNotifiedToOrderByNotificationTimeDesc(userProfileInfo);
            if (userNotificationInfos != null && userNotificationInfos.size() > 0) {
                LOGGER.info("Is this Coming here");
                for (UserNotificationInfo userNotificationInfo : userNotificationInfos) {
                    if (userNotificationInfo.getNotificationType().equals(expiryDocumentDTO.getCategory())) {
                        LOGGER.info("userNotificationInfo time" + userNotificationInfo.getNotificationTime());
                        UserDTO userDTO = new UserDTO();
                        userDTO.setNotificationMessage(userNotificationInfo.getDescription());
                        userDTO.setNotificationId(userNotificationInfo.getId());

                        RequestUserInfo requestUserInfo = userNotificationInfo.getRequestUserInfo();
                        ExpiryDocumentInfo expiryDocumentInfo = userNotificationInfo.getExpiryDocumentInfo();
                        TaskDetailsInfo taskDetailsInfo = userNotificationInfo.getTaskDetailsInfo();

                        if (requestUserInfo != null) {
                            LOGGER.info("Request User");
                            if (requestUserInfo.getUserRequestShipProfile() != null) {
                                userDTO.setNotificationName(requestUserInfo.getUserRequestShipProfile().getShipName());
                                userDTO.setShipId(requestUserInfo.getUserRequestShipProfile().getId());
                            }
                            userDTO.setColor(userNotificationInfo.getColor());
                            userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                        }
                        if (expiryDocumentInfo != null) {
                            LOGGER.info("Expiry Document");
                            if (expiryDocumentInfo.getShipProfileInfo() != null) {
                                userDTO.setNotificationName(expiryDocumentInfo.getShipProfileInfo().getShipName());
                                userDTO.setShipId(expiryDocumentInfo.getShipProfileInfo().getId());
                            }
                            userDTO.setColor(userNotificationInfo.getColor());
                            userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                        }
                        if (taskDetailsInfo != null) {
                            userDTO.setNotificationName(taskDetailsInfo.getTaskName());
                            userDTO.setNotificationType(userNotificationInfo.getNotificationType());
                            userDTO.setColor(userNotificationInfo.getColor());
                            LOGGER.info("Task Details");
                        }
                        userDTOS.add(userDTO);
                    }
                }
            }
        }
        return userDTOS;
    }

    @Override
    public boolean updateSnooze(UserDTO userDTO) {
        // 🔥 FIX 4: Null check added here
        if (userDTO == null || userDTO.getNotificationId() == null) {
            LOGGER.warn("NotificationId is null in updateSnooze()");
            return false;
        }

        UserNotificationInfo userNotificationInfo = userNotificationRepository.findOne(userDTO.getNotificationId());
        if (userNotificationInfo != null) {
            int daysForIncrement = 0;
            if (userDTO.getSnooze() == 1)
                daysForIncrement = 1;
            else if (userDTO.getSnooze() == 2)
                daysForIncrement = 7;
            else if (userDTO.getSnooze() == 3)
                daysForIncrement = 14;
            else if (userDTO.getSnooze() == 4)
                daysForIncrement = 31;

            // Fixed potential null pointer on getDate() if time is null, though unlikely in DB
            if (userNotificationInfo.getNotificationTime() != null) {
                String currentDate = userNotificationInfo.getNotificationTime().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(currentDate));
                    c.add(Calendar.DATE, daysForIncrement);
                    currentDate = sdf.format(c.getTime());
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date2 = formatter2.parse(currentDate);
                    userNotificationInfo.setNotificationTime(date2);
                    userNotificationRepository.saveAndFlush(userNotificationInfo);
                    System.out.println("Finally enter in the snooze option" + currentDate + date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
