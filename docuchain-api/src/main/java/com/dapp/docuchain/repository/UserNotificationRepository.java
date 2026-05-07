package com.dapp.docuchain.repository;

import com.dapp.docuchain.model.TaskDetailsInfo;
import com.dapp.docuchain.model.UserNotificationInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotificationInfo, Long> {

    List<UserNotificationInfo> findByNotifiedToOrderByNotificationTimeDesc(UserProfileInfo notifiedTo);

    List<UserNotificationInfo> findByTaskDetailsInfo(TaskDetailsInfo taskInfo);

    UserNotificationInfo findByNotifiedToAndTaskDetailsInfo(UserProfileInfo userProfileInfo, TaskDetailsInfo taskDetailsInfo);

    List<UserNotificationInfo> findByNotifiedTo (UserProfileInfo userProfileInfo);
    List<UserNotificationInfo> findByNotifiedToAndReadStatusOrderByNotificationTimeDesc(UserProfileInfo notifiedTo, Integer status);

	List<UserNotificationInfo> findByNotifiedToAndNotificationTimeBetween(
			UserProfileInfo userProfileInfo, Date date, Date date2);

	List<UserNotificationInfo> findByNotifiedToAndNotificationTimeBefore(
			UserProfileInfo userProfileInfo, Date date);
}
