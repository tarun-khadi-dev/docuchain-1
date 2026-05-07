package com.dapp.docuchain.model;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_notification_info")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserNotificationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "notification_desc")
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "notification_time")
    private Date notificationTime;

    @Column(name = "notification_read_status")
    private Integer readStatus;

    @Column (name = "notification_type")
    private String notificationType;

    @Column (name = "color")
    private String color;

    @Column (name = "snooze")
    private String snooze;

    @Column (name = "organizationId")
    private Long organizationId;

    @Column (name = "vesselId")
    private Long vesselId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notified_user_id", referencedColumnName = "id", nullable = false)
    private UserProfileInfo notifiedTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expiry_document_id", referencedColumnName = "id", nullable = true)
    private ExpiryDocumentInfo expiryDocumentInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_user_id", referencedColumnName = "id", nullable = true)
    private RequestUserInfo requestUserInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskDetailsInfo taskDetailsInfo;

}
