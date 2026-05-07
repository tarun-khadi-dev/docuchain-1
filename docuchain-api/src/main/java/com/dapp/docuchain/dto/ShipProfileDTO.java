package com.dapp.docuchain.dto;

import com.dapp.docuchain.model.DocumentHolderInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipProfileDTO {

    private Long id;
    private String shipName;
    private String vesselsName;
    private String shipOwner;
    private Long officialNo;
    private Long imo;
    private String engineType;
    private String callSign;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date keelLaid;
    private String keelLaidString;
    private String delivered;
    private String builder;
    private String internationalGRT;
    private String internationalNRT;
    private String bhp;
    private Long shiptypeId;
    private String countryName;
    private Long countryId;
    private String stateName;
    private Long stateId;
    private Long shipTypeId;
    private String shipTypes;
    private Integer status;
    private Double dwt;
    private Double weight;
    private Double length;
      private Double breadth;
    private String roleName;
    private String UserName;
    private Long commercialMasterId;
    private Long techManagerId;
    private Long shipMasterId;
    private List<ShipProfileDTO> countryList;
    private List<ShipProfileDTO> shipTypeList;
    private List<ShipProfileDTO> stateList;
    private List<ShipProfileDTO> shipInfoList;
    private List<ShipProfileDTO> roleList;
    private ShipProfileDTO singleShipInfo;
    private List<String> roleNameList;
    private List<String> shipNameList;

    private Long[] techManagerIds;
    private Long[] commercialMasterIds;
    private Long[] dataOperatorsIds;
    private List<ShipProfileDTO> allUserList;
    private Long statusCode;
    private Long userId;
    private Long loginId;
    private Long roleId;
    private String mailId;
    private Integer shipCount;
    private Integer userCount;
    private Date userLastSeen;
    private ShipProfileDTO ddd;
    private String commercialMasterName;
    private String techManagerName;
    private String shipMasterName;
    private String dataOperatorName;
    private List<ShipProfileDTO> commercialManagerInfos;
    private List<ShipProfileDTO> techManagerInfos;
    private List<ShipProfileDTO> dataOperatorListNames;
    private byte[] pic;
    private Integer yellowCount;
    private Integer greenCount;
    private Integer redCount;
    private Long[] shipIds;
    private Long organizationId;
    private String registeredOwner;
    private  UserDTO shiMasterInfo;
    private Set<UserDTO> commercialManagerInfoList;
    private Set<UserDTO> techManagerInfoList;
    private Set<UserDTO> dataOperatorList;
    private String shipProfilePicPath;
    private Integer activeCount;
	private Integer renewelCount;
	private Integer expiryedCount;
	private Integer pendingCount;
	private Integer totalCount;
	private Integer missingCount;
	private Integer vesselsCount;
	private String portName;
	private String documentHolderName;
	private String documentHolderDescription;
	private Date updatedDate;
	private String type;
	private long docIds [];
	private Set<DocumentHolderDTO> customDocumentHolders;
	private Set<DocumentHolderDTO> standardDocumentHolders;
}


