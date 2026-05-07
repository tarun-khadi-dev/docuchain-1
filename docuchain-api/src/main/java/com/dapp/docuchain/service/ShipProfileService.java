package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.model.OrganizationInfo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ShipProfileService {

    public boolean addShipProfile(ShipProfileDTO shipProfileDTO);

    public ShipProfileDTO stateLists(ShipProfileDTO shipProfileDTO);

    public ShipProfileDTO typesOfShip();

    public ShipProfileDTO findAllShip();

    public Boolean changeStatus(Long falseValue);

    public ShipProfileDTO getDetailsForUpdateShip(Long officialNum);

    public Boolean shipDelete(ShipProfileDTO shipProfileDTO);

    public Boolean shipUpdate(ShipProfileDTO shipProfileDTO);

    public ShipProfileDTO roleShip();

    public ShipProfileDTO saveRoles(ShipProfileDTO shipProfileDTO);

    public List<ShipProfileDTO> getShipProfileInfoUsingRoleIdAndUserId(ShipProfileDTO shipProfileDTO);

    public String checkUserIdExists(Long userId);

    public List<ShipProfileDTO> getAllUserLists();

    public ShipProfileDTO getUserProfileListsInfoforUpdate(String userName);

    public ShipProfileDTO getUserShipCount();

    public List<ShipProfileDTO> getUserLastSeenAdmin();

    public List<ShipProfileDTO> assignShipCount();

    public List<ShipProfileDTO> getAssignedUserNames(Long offNo);

    public String verifiyShipProfileExists(Long id);

    public ShipProfileDTO getExpiryDetails(ShipProfileDTO shipProfileDTO);

    public List<ShipProfileDTO> getLastSeen(Long userId);

    Boolean statusChangeUserLists(Long officialNum);

    public Boolean scanImageFile(String userName,MultipartFile scanFile);

	public byte[] getByteDataForUserProfile(String userName);

	public boolean createShipProfile(ShipProfileDTO shipProfileDTO, String shipProfilePicPath);

	public Boolean shipDeleteAll(ShipProfileDTO shipProfileDTO);

	public String isOrganizationValid(Long organizationid);

	public List<ShipProfileDTO> shipListBasedOnOrg(Long organizationid);

	public boolean updateShipProfile(ShipProfileDTO shipProfileDTO, String shipProfilePicPath);

	public Boolean updateShipStatus(ShipProfileDTO shipProfileDTO);

	public Boolean updateShipStatusAll(ShipProfileDTO shipProfileDTO);

	public List<ShipProfileDTO> viewShipProfile(Long userId);

	public List<ShipProfileDTO> getDashboardTopCount(Long userId);

	public String activateAllVesselsBasedOrganization(ShipProfileDTO shipProfileDTO);

	public String deActivateAllVesselsBasedOrganization(ShipProfileDTO shipProfileDTO);

	public String deleteShipProfileInformationBasedOrganization(ShipProfileDTO shipProfileDTO);

	public String deleteTechnicalManagersFromShipProfileInfoBasedOrganization(ShipProfileDTO shipProfileDTO);

	public String deleteCommercialManagersFromShipProfileInfoBasedOrganization(ShipProfileDTO shipProfileDTO);

	public List<ShipProfileDTO> listShipProfileInfoBasedOrganization(OrganizationInfo organizationInfo);

	public String deleteShipMasterFromShipProfileInfoBasedOrganization(ShipProfileDTO shipProfileDTO);

	public ShipProfileDTO getDashboardTopCountBasedOnVessel(Long vesselId);
    public Long getPortCountByOrganization(Long organizationId);

}
