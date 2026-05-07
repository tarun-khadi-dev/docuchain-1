package com.dapp.docuchain.dto;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentHolderDTO {

    private Long documentHolderId;
    private String documentFileNumber;
    private String documentHolderName;
    private String documentHolderDescription;
    private String documentHolderType;
    private Long userId;
    private String organizationName;
    private Long vesselIds [];
    private String vesselName;

    private String type;
    private Date updatedDate;
    private String placeholderBy;
    private Long vesselid;
}
