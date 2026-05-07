package com.dapp.docuchain.service.impl;

import java.io.File;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.DocumentHolderDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.CountryInfo;
import com.dapp.docuchain.model.DataDeletedHistoryInfo;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.DocumentHolderType;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.PortInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.ShipTypesInfo;
import com.dapp.docuchain.model.User;
import com.dapp.docuchain.model.UserLastSeenInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.CountryInfoRepository;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.OrganizationInfoRepository;
import com.dapp.docuchain.repository.PortInfoRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.repository.ShipTypesRepository;
import com.dapp.docuchain.repository.UserLastSeenHistory;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.repository.UserRepository;
import com.dapp.docuchain.service.ShipProfileService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.ShipProfileUtility;

@Service
public class ShipProfileServiceImpl implements ShipProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShipProfileServiceImpl.class);

	@Autowired
	ShipProfileRepository shipprofilerepository;

	@Autowired
	CountryInfoRepository countryinforepo;

	@Autowired
	private PortInfoRepository portInfoRepository;

	@Autowired
	private ShipTypesRepository shiptypesrepo;

	@Autowired
	private RoleInfoRepository roleRepo;

	@Autowired
	private UserProfileRepository userRepo;

	@Autowired
	private ShipProfileUtility shipProfileUtility;

	@Autowired
	private Environment env;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserLastSeenHistory userLastSeenRepo;

	@Autowired
	private ExpiryDocumentRepository expiryRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private DocumentHolderRepository documentHolderRepository;

	@Override
	public boolean addShipProfile(ShipProfileDTO shipProfileDTO) {

		ShipProfileInfo shipProfileInfo = new ShipProfileInfo();

		shipProfileInfo.setBhp(shipProfileDTO.getBhp());
		shipProfileInfo.setBuilder(shipProfileDTO.getBuilder());
		shipProfileInfo.setCallsign(shipProfileDTO.getCallSign());
		shipProfileInfo.setDelivered(shipProfileDTO.getDelivered());
		shipProfileInfo.setEngineType(shipProfileDTO.getEngineType());
		shipProfileInfo.setInternationalGRT(shipProfileDTO.getInternationalGRT());
		shipProfileInfo.setInternationalNRT(shipProfileDTO.getInternationalNRT());
		shipProfileInfo.setKeellaid(shipProfileDTO.getKeelLaid());
		shipProfileInfo.setIMO(shipProfileDTO.getOfficialNo());
		shipProfileInfo.setShipName(shipProfileDTO.getShipName());
		shipProfileInfo.setShipOwner(shipProfileDTO.getShipOwner());
		shipProfileInfo.setCountryName(shipProfileDTO.getCountryName());
		shipProfileInfo.setStateName(shipProfileDTO.getStateName());
		shipProfileInfo.setDwt(shipProfileDTO.getDwt());
		shipProfileInfo.setWeight(shipProfileDTO.getWeight());
		shipProfileInfo.setLength(shipProfileDTO.getLength());
		shipProfileInfo.setBreadth(shipProfileDTO.getBreadth());
		shipProfileInfo.setShipType(shipProfileDTO.getShipTypes());
		shipProfileInfo.setStatus(1);
		ShipProfileInfo shipProfileinfo = shipprofilerepository.save(shipProfileInfo);
		if (shipProfileinfo != null) {
			commonMethodsUtility.maintainHistory(shipProfileinfo.getId(), shipProfileInfo.getShipName(), "Ship",
					env.getProperty("history.created"), shipProfileDTO.getLoginId());
		}
		ShipProfileInfo record = shipprofilerepository.findByIMO(shipProfileDTO.getImo());
		if (record == null) {

			return false;

		} else {

			return true;
		}
	}
	@Override
public Long getPortCountByOrganization(Long organizationId) {

    OrganizationInfo org = organizationInfoRepository.findOne(organizationId);

    if (org == null) {
        return 0L;
    }

    List<ShipProfileInfo> ships = shipprofilerepository.findByShipOrganizationInfo(org);

    Set<String> countryNames = new HashSet<>();

    for (ShipProfileInfo ship : ships) {
        if (ship.getCountryName() != null) {
            countryNames.add(ship.getCountryName().trim());
        }
    }

    long portCount = 0;

    for (String countryName : countryNames) {

        CountryInfo country = countryinforepo.findByCountryName(countryName);

        if (country != null && country.getPortInfos() != null) {
            portCount += country.getPortInfos().size();
        }
    }

    return portCount;
}

	@Override
	public ShipProfileDTO stateLists(ShipProfileDTO shipProfileDTO) {

		ShipProfileDTO shipDTO = new ShipProfileDTO();
		if (shipProfileDTO.getCountryName() != null) {
			String countryName = shipProfileDTO.getCountryName();
			CountryInfo countryInfo = countryinforepo.findByCountryName(countryName.trim());
			List<PortInfo> portInfos = countryInfo.getPortInfos();
			List<ShipProfileDTO> shipListDto = new ArrayList<>();
			for (PortInfo portInfo : portInfos) {
				ShipProfileDTO shipProfile = new ShipProfileDTO();
				shipProfile.setStateId(portInfo.getId());
				shipProfile.setStateName(portInfo.getPortName());
				shipListDto.add(shipProfile);
			}
			shipDTO.setStateList(shipListDto);
			if (countryInfo.getPortInfos().isEmpty()) {

				return null;
			} else {
				return shipDTO;
			}
		} else if (shipProfileDTO.getCountryId() != 0) {
			Long id = shipProfileDTO.getCountryId();
			CountryInfo country_info = countryinforepo.findById(id);
			List<PortInfo> portInfos = country_info.getPortInfos();
			List<ShipProfileDTO> shipListDto = new ArrayList<>();
			for (PortInfo portInfo : portInfos) {
				ShipProfileDTO shipProfile = new ShipProfileDTO();
				shipProfile.setStateId(portInfo.getId());
				shipProfile.setStateName(portInfo.getPortName());
				shipListDto.add(shipProfile);
			}
			shipDTO.setStateList(shipListDto);
			if (country_info.getPortInfos().isEmpty()) {
				return null;
			} else {
				return shipDTO;
			}
		}

		return null;

	}

	@Override
	public ShipProfileDTO typesOfShip() {

		ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
		List<ShipTypesInfo> shipTypes = shiptypesrepo.findAll();
		List<CountryInfo> countryList = countryinforepo.findAll();

		List<ShipProfileDTO> country = new ArrayList<>();
		List<ShipProfileDTO> shiptype = new ArrayList<>();

		if (shipTypes != null && shipTypes.size() > 0) {
			for (ShipTypesInfo shipTypesInfo : shipTypes) {
				ShipProfileDTO shipTypesDTO = new ShipProfileDTO();
				shipTypesDTO.setShiptypeId(shipTypesInfo.getId());
				shipTypesDTO.setShipTypes(shipTypesInfo.getShipTypesName());
				shiptype.add(shipTypesDTO);
			}
			shipProfileDTO.setShipTypeList(shiptype);
		}
		if (countryList != null && countryList.size() > 0) {
			for (CountryInfo countryInfo : countryList) {
				ShipProfileDTO shipDTO = new ShipProfileDTO();
				shipDTO.setCountryId(countryInfo.getId());
				shipDTO.setCountryName(countryInfo.getCountryName());
				country.add(shipDTO);
			}
			shipProfileDTO.setCountryList(country);
		}

		return shipProfileDTO;

	}

	@Override
	public ShipProfileDTO findAllShip() {

		ShipProfileDTO shipInfoDTO = new ShipProfileDTO();
		List<ShipProfileDTO> shipDTO = new ArrayList<ShipProfileDTO>();
		List<ShipProfileInfo> shipProfileInfo = shipprofilerepository.findAll();
		for (ShipProfileInfo shipProfileInfo2 : shipProfileInfo) {
			ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
			shipProfileDTO.setId(shipProfileInfo2.getId());
			shipProfileDTO.setOfficialNo(shipProfileInfo2.getIMO());
			shipProfileDTO.setShipName(shipProfileInfo2.getShipName());
			shipProfileDTO.setShipTypes(shipProfileInfo2.getShipType());
			shipProfileDTO.setStatus(shipProfileInfo2.getStatus());
			/*
			 * if (shipProfileInfo2.getCommercialMaster() != null) {
			 * shipProfileDTO.setCommercialMasterId(shipProfileInfo2.getCommercialMaster().
			 * getId()); } if (shipProfileInfo2.getTechMaster() != null) {
			 * shipProfileDTO.setTechManagerId(shipProfileInfo2.getTechMaster().getId()); }
			 */
			if (shipProfileInfo2.getShipMaster() != null) {
				shipProfileDTO.setShipMasterId(shipProfileInfo2.getShipMaster().getId());
			}

			shipDTO.add(shipProfileDTO);

		}
		shipInfoDTO.setShipInfoList(shipDTO);
		if (shipInfoDTO.getShipInfoList() == null) {
			return null;
		} else {

			return shipInfoDTO;

		}

	}

	@Override
	public Boolean changeStatus(Long officialNum) {

		Long off_No = officialNum;
		ShipProfileInfo ship_Info = shipprofilerepository.findByIMO(off_No);
		if (ship_Info.getStatus().equals(env.getProperty("true"))) {
			ship_Info.setStatus(0);
			shipprofilerepository.save(ship_Info);

			return true;
		} else if (ship_Info.getStatus().equals(env.getProperty("false"))) {
			ship_Info.setStatus(1);
			shipprofilerepository.save(ship_Info);

			return true;
		} else {
			return false;
		}

	}

	@Override
	public Boolean statusChangeUserLists(Long userId) {

		LOGGER.info("USERID" + userId);
		UserProfileInfo userInfo = userProfileRepository.findById(userId);

		if (userInfo.getStatus() == 1) {
			userInfo.setStatus(new Long(0));

			userProfileRepository.save(userInfo);

			return true;
		} else if (userInfo.getStatus() == 0) {
			userInfo.setStatus(new Long(1));
			userProfileRepository.save(userInfo);

			return true;
		} else {
			return false;
		}

	}

	@Override
	public ShipProfileDTO getDetailsForUpdateShip(Long off_no) {

		ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
		ShipProfileInfo shipProfileInfo2 = shipprofilerepository.findByIMO(off_no);
		if (shipProfileInfo2 != null) {
			String shipType = shipProfileInfo2.getShipType();
			ShipTypesInfo typeInfo = shiptypesrepo.findByShipTypesName(shipType.trim());
			CountryInfo country_Info = countryinforepo.findByCountryName(shipProfileInfo2.getCountryName());
			String state_name = shipProfileInfo2.getStateName().trim();
			// StateInfo state_Info = stateinforepo.findByStateName(state_name);
			PortInfo portInfo = portInfoRepository.findByPortName(state_name);
			List<PortInfo> portInfos = country_Info.getPortInfos();
			List<CountryInfo> countryList = countryinforepo.findAll();
			List<ShipTypesInfo> shipTypes = shiptypesrepo.findAll();
			List<ShipProfileDTO> stateListInfo = new ArrayList<>();
			List<ShipProfileDTO> countryListInfo = new ArrayList<>();
			List<ShipProfileDTO> shipTypeListInfo = new ArrayList<>();
			ShipProfileDTO shipProfile = new ShipProfileDTO();

			for (PortInfo portInfo1 : portInfos) {
				ShipProfileDTO shipStateDTO = new ShipProfileDTO();
				shipStateDTO.setStateId(portInfo1.getId());
				shipStateDTO.setStateName(portInfo1.getPortName());
				stateListInfo.add(shipStateDTO);
			}
			for (CountryInfo country : countryList) {
				ShipProfileDTO shipCountryDTO = new ShipProfileDTO();
				shipCountryDTO.setCountryId(country.getId());
				shipCountryDTO.setCountryName(country.getCountryName());
				countryListInfo.add(shipCountryDTO);
			}
			for (ShipTypesInfo typeInfo1 : shipTypes) {
				ShipProfileDTO shipTypeDTO = new ShipProfileDTO();
				shipTypeDTO.setShiptypeId(typeInfo1.getId());
				shipTypeDTO.setShipTypes(typeInfo1.getShipTypesName());
				shipTypeListInfo.add(shipTypeDTO);
			}

			shipProfileDTO.setStateList(stateListInfo);
			shipProfileDTO.setShipTypes(shipType);
			shipProfileDTO.setShiptypeId(typeInfo.getId());

			shipProfile.setId(shipProfileInfo2.getId());
			shipProfile.setBhp(shipProfileInfo2.getBhp());
			shipProfile.setBuilder(shipProfileInfo2.getBuilder());
			shipProfile.setCallSign(shipProfileInfo2.getCallsign());
			shipProfile.setCountryName(shipProfileInfo2.getCountryName());
			shipProfile.setDelivered(shipProfileInfo2.getDelivered());
			shipProfile.setEngineType(shipProfileInfo2.getEngineType());
			shipProfile.setInternationalGRT(shipProfileInfo2.getInternationalGRT());
			shipProfile.setInternationalNRT(shipProfileInfo2.getInternationalNRT());

			SimpleDateFormat formatter = new SimpleDateFormat(env.getProperty("YYYY-MM-dd"));
			String strDate = formatter.format(shipProfileInfo2.getKeellaid());

			shipProfile.setKeelLaidString(strDate);
			shipProfile.setOfficialNo(shipProfileInfo2.getIMO());
			shipProfile.setShipName(shipProfileInfo2.getShipName());
			shipProfile.setShipOwner(shipProfileInfo2.getShipOwner());
			shipProfile.setShipTypes(shipProfileInfo2.getShipType());
			shipProfile.setStateName(shipProfileInfo2.getStateName());
			shipProfile.setDwt(shipProfileInfo2.getDwt());
			shipProfile.setWeight(shipProfileInfo2.getWeight());
			shipProfile.setLength(shipProfileInfo2.getLength());
			shipProfile.setBreadth(shipProfileInfo2.getBreadth());
			shipProfile.setStatus(shipProfileInfo2.getStatus());

			shipProfileDTO.setSingleShipInfo(shipProfile);
			shipProfileDTO.setStateId(portInfo.getId());
			shipProfileDTO.setCountryList(countryListInfo);
			shipProfileDTO.setCountryId(country_Info.getId());
			shipProfileDTO.setShipTypeList(shipTypeListInfo);

			/*
			 * if (shipProfileInfo2.getCommercialMaster() != null) {
			 * shipProfileDTO.setCommercialMasterId(shipProfileInfo2.getCommercialMaster().
			 * getId()); } if (shipProfileInfo2.getTechMaster() != null) {
			 * shipProfileDTO.setTechManagerId(shipProfileInfo2.getTechMaster().getId()); }
			 */
			if (shipProfileInfo2.getShipMaster() != null) {
				shipProfileDTO.setShipMasterId(shipProfileInfo2.getShipMaster().getId());
			}

			if (shipProfileDTO != null) {
				return shipProfileDTO;
			}
		}
		return null;
	}

	@Override
	public Boolean shipDelete(ShipProfileDTO shipProfileDTO) {

		ShipProfileInfo ship_Info = shipprofilerepository.findOne(shipProfileDTO.getId());
		if (ship_Info != null) {
			shipprofilerepository.delete(ship_Info);
			DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
			deletedHistoryDTO.setObjectId(ship_Info.getId());
			deletedHistoryDTO.setObjectOne(ship_Info.getShipName());
			deletedHistoryDTO.setObjectTwo(ship_Info.getShipType());
			return true;
		}
		return false;

	}

	@Override
	public Boolean shipUpdate(ShipProfileDTO shipProfileDTO) {

		Long countryID = shipProfileDTO.getCountryId();
		Long stateId = shipProfileDTO.getStateId();
		Long shipTypeId = shipProfileDTO.getShiptypeId();

		CountryInfo countryInfo = countryinforepo.findById(countryID);
		String countryName = countryInfo.getCountryName();

		PortInfo portInfo = portInfoRepository.findOne(stateId);
		// StateInfo stateInfo = stateinforepo.findById(stateId);
		String stateName = portInfo.getPortName();

		ShipTypesInfo shipTypeInfo = shiptypesrepo.findById(shipTypeId);
		String shipTypeName = shipTypeInfo.getShipTypesName();

		ShipProfileInfo shipProfileInfo = shipprofilerepository.findById(shipProfileDTO.getId());
		shipProfileInfo.setId(shipProfileInfo.getId());
		shipProfileInfo.setBhp(shipProfileDTO.getBhp());
		shipProfileInfo.setBuilder(shipProfileDTO.getBuilder());
		shipProfileInfo.setCallsign(shipProfileDTO.getCallSign());
		shipProfileInfo.setDelivered(shipProfileDTO.getDelivered());
		shipProfileInfo.setEngineType(shipProfileDTO.getEngineType());
		shipProfileInfo.setInternationalGRT(shipProfileDTO.getInternationalGRT());
		shipProfileInfo.setInternationalNRT(shipProfileDTO.getInternationalNRT());
		shipProfileInfo.setKeellaid(shipProfileDTO.getKeelLaid());
		// shipProfileInfo.setOfficialNo(shipProfileDTO.getOfficialNo());
		// shipProfileInfo.setShipName(shipProfileDTO.getShipName());
		shipProfileInfo.setShipOwner(shipProfileDTO.getShipOwner());
		shipProfileInfo.setCountryName(countryName);
		shipProfileInfo.setStateName(stateName);
		shipProfileInfo.setShipType(shipTypeName);
				// ship mesaurment
		shipProfileInfo.setDwt(shipProfileDTO.getDwt());
		shipProfileInfo.setBreadth(shipProfileDTO.getBreadth());
		shipProfileInfo.setWeight(shipProfileDTO.getWeight());
		shipProfileInfo.setLength(shipProfileDTO.getLength());
		shipProfileInfo.setStatus(shipProfileDTO.getStatus());

		ShipProfileInfo shipProfile = shipprofilerepository.save(shipProfileInfo);
		if (shipProfile != null) {
			commonMethodsUtility.maintainHistory(shipProfile.getId(), shipProfile.getShipName(), "Ship",
					env.getProperty("history.updated"), shipProfileDTO.getLoginId());
		}
		return true;
	}

	@Override
	public ShipProfileDTO roleShip() {

		ShipProfileDTO shipDTO = new ShipProfileDTO();
		List<RoleInfo> roles = roleRepo.findAll();
		List<String> roleNew = new ArrayList<>();

		for (RoleInfo roleInfo : roles) {

			if (!roleInfo.getRoleName().equals(Role.SuperAdmin) && !roleInfo.getRoleName().equals(Role.Admin)
					&& !roleInfo.getRoleName().equals(Role.DataOperator)) {
				roleNew.add(roleInfo.getRoleName().name());
			}

			LOGGER.info("ROLES" + roleInfo.getRoleName());
		}

		List<ShipProfileInfo> shipInfo = shipprofilerepository.findAll();
		List<String> shipList = new ArrayList<>();

		for (ShipProfileInfo shipProfileInfo : shipInfo) {
			if (shipProfileInfo.getShipName() != null && shipProfileInfo.getStatus() != 0) {
				shipList.add(shipProfileInfo.getShipName());
			} else {
				continue;
			}

		}

		if (roles != null && shipInfo != null) {
			shipDTO.setRoleNameList(roleNew);
			shipDTO.setShipNameList(shipList);
			return shipDTO;
		} else {
			return null;
		}

	}

	@Override
	public ShipProfileDTO saveRoles(ShipProfileDTO shipProfileDTO) {

		ShipProfileDTO shipDTO = new ShipProfileDTO();
		ShipProfileInfo shipInfo = shipprofilerepository.findByShipName(shipProfileDTO.getShipName());
		RoleInfo roleInfo = roleRepo.findByRoleName(Role.valueOf(shipProfileDTO.getRoleName()));
		UserProfileInfo userInfo = userRepo.findByUserName(shipProfileDTO.getUserName());

		if (userInfo == null) {
			userInfo = new UserProfileInfo();
			userInfo.setRoleId(roleInfo);
			userInfo.setUserName(shipProfileDTO.getUserName());
			userRepo.saveAndFlush(userInfo);
		}

		if (roleInfo.getRoleName() != null) {
			if (roleInfo.getRoleName().equals(env.getProperty("role.commercialmanager"))) {
				// shipInfo.setCommercialMaster(userInfo);
				shipInfo.getCommercialMasters().add(userInfo);
				ShipProfileInfo shipInfoStataus = shipprofilerepository.save(shipInfo);
				if (shipInfoStataus != null) {
					commonMethodsUtility.maintainHistory(shipInfoStataus.getId(), shipInfoStataus.getShipName(), "Ship",
							env.getProperty("history.created"), shipProfileDTO.getLoginId());
				}
				// LOGGER.info("CommercialManager" + shipInfo.getCommercialMaster().toString());
				return null;

			} else if (roleInfo.getRoleName().equals(env.getProperty("role.techmanager"))) {
				// shipInfo.setTechMaster(userInfo);
				shipInfo.getTechMasters().add(userInfo);
				ShipProfileInfo shipInfoStataus = shipprofilerepository.save(shipInfo);
				if (shipInfoStataus != null) {
					commonMethodsUtility.maintainHistory(shipInfoStataus.getId(), shipInfoStataus.getShipName(), "Ship",
							env.getProperty("history.created"), shipProfileDTO.getLoginId());
				}
				// LOGGER.info("techmaster" + shipInfo.getTechMaster().getId());
				return null;

			} else if (roleInfo.getRoleName().equals(env.getProperty("role.shipmaster"))) {
				ShipProfileInfo shipProfile = shipprofilerepository.findByShipMaster(userInfo);
				if (shipProfile == null) {

					shipDTO.setStatusCode(new Long(123));
					LOGGER.info("USERINFO " + userInfo.getUserName());
					shipInfo.setShipMaster(userInfo);

					ShipProfileInfo shipInfoStataus = shipprofilerepository.save(shipInfo);
					if (shipInfoStataus != null) {
						commonMethodsUtility.maintainHistory(shipInfoStataus.getId(), shipInfoStataus.getShipName(),
								"Ship", env.getProperty("history.created"), shipProfileDTO.getLoginId());
					}
					LOGGER.info("ShipMaster" + shipInfo.getShipMaster().toString());
					return null;
				} else {
					shipDTO.setStatusCode(new Long(111));
					return shipDTO;
				}

			}

		} else {

			shipDTO.setStatusCode(new Long(100));
			return shipDTO;
		}
		return null;

	}

	@Override
	public List<ShipProfileDTO> getAllUserLists() {
		List<UserProfileInfo> userList = userProfileRepository.findAll();
		if (userList != null && userList.size() > 0) {
			List<ShipProfileDTO> shipProfileDTO = new ArrayList<>();
			for (UserProfileInfo user : userList) {
				ShipProfileDTO shipDTO = new ShipProfileDTO();
				// shipDTO.setUserName(user.getUserName());
				LOGGER.info("USERNAME>>>>>>" + user.getUserName());
				LOGGER.info("USEID>>>>>>" + user.getId());
				shipDTO.setUserId(user.getId());
				shipDTO.setStatusCode(user.getStatus());
				LOGGER.info("StatusCode>>>>>>" + user.getStatus());
				if (user.getUserName() != null) {
					User userDetails = userRepository.findByUsername(user.getUserName());
					if (userDetails != null) {
						shipDTO.setUserName(userDetails.getUsername());
						shipDTO.setMailId(userDetails.getMail());
						shipDTO.setRoleName(userDetails.getBusinessCategory());
						shipProfileDTO.add(shipDTO);
					} else {
						continue;
					}
				}

			}
			return shipProfileDTO;
		}
		return null;
	}

	@Override
	public List<ShipProfileDTO> getShipProfileInfoUsingRoleIdAndUserId(ShipProfileDTO shipProfileDTO) {
		List<ShipProfileDTO> shipProfileDTOList = new ArrayList<ShipProfileDTO>();
		// RoleInfo roleInfo = roleRepo.findOne(shipProfileDTO.getRoleId());
		UserProfileInfo userProfileInfo = userRepo.findOne(shipProfileDTO.getUserId());
		OrganizationInfo organizationInfo = organizationInfoRepository
				.findOne(userProfileInfo.getOrganizationInfo().getId());
		RoleInfo roleInfo = userProfileInfo.getRoleId();
		List<ShipProfileInfo> shipProfileInfos = new ArrayList<ShipProfileInfo>();
		ShipProfileInfo shipProfileInfo;
		if (roleInfo != null) {
			if (roleInfo.getRoleName().equals(Role.ShipMaster)) {
				shipProfileInfo = shipprofilerepository.findByShipMasterAndStatus(userProfileInfo, 1);
				if (shipProfileInfo != null)
					shipProfileInfos.add(shipProfileInfo);
			}
			if (roleInfo.getRoleName().equals(Role.TechManager)) {
				shipProfileInfos = shipprofilerepository.findByTechMasters_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
			}
			if (roleInfo.getRoleName().equals(Role.CommercialManager)) {
				shipProfileInfos = shipprofilerepository
						.findByCommercialMasters_UserNameAndStatusAndShipOrganizationInfo(userProfileInfo.getUserName(),
								1, organizationInfo);
			}
		}
		for (ShipProfileInfo shipProfileInfoObj : shipProfileInfos) {
			ShipProfileDTO shipProfileDTOObj = shipProfileUtility
					.convertShipProfileInfoToShipProfileDTO(shipProfileInfoObj);
			shipProfileDTOList.add(shipProfileDTOObj);
		}
		return shipProfileDTOList;
	}

	@Override
	public String checkUserIdExists(Long userId) {
		UserProfileInfo userProfileInfo = userRepo.findOne(userId);
		if (userProfileInfo != null) {
			return env.getProperty("success");
		}
		return env.getProperty("user.not.exists");
	}

	@Override
	public ShipProfileDTO getUserProfileListsInfoforUpdate(String userName) {
		User userInfo = userRepository.findByUsername(userName);
		if (userInfo != null) {
			RoleInfo roleInfo = roleRepo.findByRoleName(Role.valueOf(userInfo.getBusinessCategory()));
			ShipProfileDTO shipdto = new ShipProfileDTO();
			List<ShipProfileDTO> shipListDTO = new ArrayList<>();
			List<RoleInfo> findAllRole = roleRepo.findAll();

			for (RoleInfo roleInfo2 : findAllRole) {
				if (roleInfo2 != null) {
					ShipProfileDTO shipDto = new ShipProfileDTO();

					if (!roleInfo2.getRoleName().equals(Role.Admin)) {
						shipDto.setRoleId(roleInfo2.getId());
						shipDto.setRoleName(roleInfo2.getRoleName().name());
					} else {
						continue;
					}

					shipListDTO.add(shipDto);

				}
			}
			if (userInfo != null) {
				shipdto.setUserName(userInfo.getUsername());
				shipdto.setRoleName(userInfo.getBusinessCategory());
				shipdto.setMailId(userInfo.getMail());
			}

			if (roleInfo != null) {

				shipdto.setRoleId(roleInfo.getId());
			}

			shipdto.setRoleList(shipListDTO);
			if (shipdto != null) {
				return shipdto;

			}
		}

		return null;
	}

	@Override
	public ShipProfileDTO getUserShipCount() {
		List<ShipProfileInfo> shipInfo = shipprofilerepository.findAll();
		if (shipInfo != null && shipInfo.size() > 0) {
			Integer shipInfoLength = shipInfo.size();
			List<UserProfileInfo> userInfo = userRepo.findAll();
			Integer userInfoLength = userInfo.size();
			LOGGER.info("ShipLength" + shipInfoLength);
			LOGGER.info("UserInfoLength" + userInfoLength);
			ShipProfileDTO shipDTO = new ShipProfileDTO();
			shipDTO.setShipCount(shipInfoLength);
			shipDTO.setUserCount(userInfoLength);
			if (shipDTO != null)
				return shipDTO;
		}
		return null;
	}

	@Override
	public List<ShipProfileDTO> getUserLastSeenAdmin() {

		List<UserLastSeenInfo> userLastSeenHistory = userLastSeenRepo
				.findTop5ByUserNameOrderByUserLastSeenDateTimeDesc(env.getProperty("admin"));
		List<ShipProfileDTO> shipListDTO = new ArrayList<>();
		for (UserLastSeenInfo userLastSeenInfo : userLastSeenHistory) {
			ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
			shipProfileDTO.setUserName(userLastSeenInfo.getUserName());
			LOGGER.info("" + userLastSeenInfo.getUserName());
			shipProfileDTO.setUserLastSeen(userLastSeenInfo.getUserLastSeenDateTime());
			LOGGER.info("" + userLastSeenInfo.getUserLastSeenDateTime());
			shipListDTO.add(shipProfileDTO);
		}
		if (shipListDTO.size() < 0) {
			return null;
		} else {

			return shipListDTO;
		}

	}

	@Override
	public List<ShipProfileDTO> assignShipCount() {
		int count = 0;

		List<ShipProfileInfo> shipInfo = shipprofilerepository.findAll();
		List<ShipProfileDTO> shipListDTO = new ArrayList<>();
		if (shipInfo != null) {
			for (ShipProfileInfo shipProfileInfo : shipInfo) {

				ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
				if (shipProfileInfo != null) {
					/*
					 * if (shipProfileInfo.getTechMaster() != null) { count = count + 1;
					 * LOGGER.info("TechMaster Count" + count); } if
					 * (shipProfileInfo.getCommercialMaster() != null) { count = count + 1;
					 * LOGGER.info("CommercialMaster Count" + count); }
					 */
					if (shipProfileInfo.getShipMaster() != null) {

						count = count + 1;
						LOGGER.info("ShipMaster Count" + count);
					}

					Integer dataOperator = shipProfileInfo.getDataOperators().size();
					int newCount = count + dataOperator;
					LOGGER.info("newCount" + newCount);
					if (shipProfileInfo.getStatus() != null && shipProfileInfo.getShipName() != null
							&& shipProfileInfo.getIMO() != null) {
						if (shipProfileInfo.getStatus() == 1) {
							shipProfileDTO.setShipName(shipProfileInfo.getShipName());
							shipProfileDTO.setOfficialNo(shipProfileInfo.getIMO());
							shipProfileDTO.setUserCount(newCount);
						} else {
							continue;
						}
					}

					shipListDTO.add(shipProfileDTO);
					count = 0;
				} else {
					continue;
				}

			}

		}

		if (shipListDTO.size() != 0) {
			return shipListDTO;
		} else {
			return null;
		}

	}

	@Override
	public List<ShipProfileDTO> getAssignedUserNames(Long offNo) {

		ShipProfileInfo shipInfo = shipprofilerepository.findByIMO(offNo);
		List<ShipProfileDTO> shipListDTO = new ArrayList<>();
		ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
		/*
		 * if (shipInfo != null) { if (shipInfo.getCommercialMaster() != null)
		 * shipProfileDTO.setCommercialMasterName(shipInfo.getCommercialMaster().
		 * getUserName()); if (shipInfo.getShipMaster() != null)
		 * shipProfileDTO.setShipMasterName(shipInfo.getShipMaster().getUserName()); if
		 * (shipInfo.getTechMaster() != null)
		 * shipProfileDTO.setTechManagerName(shipInfo.getTechMaster().getUserName()); }
		 */

		List<ShipProfileDTO> shipList = new ArrayList<>();
		for (UserProfileInfo userDataOperator : shipInfo.getDataOperators()) {
			ShipProfileDTO shipProfileDTOO = new ShipProfileDTO();

			shipProfileDTOO.setDataOperatorName(userDataOperator.getUserName());
			shipList.add(shipProfileDTOO);
		}
		shipProfileDTO.setDataOperatorListNames(shipList);
		shipListDTO.add(shipProfileDTO);

		if (shipListDTO.size() != 0) {
			return shipListDTO;
		} else {
			return null;
		}

	}

	@Override
	public String verifiyShipProfileExists(Long id) {
		ShipProfileInfo shipProfileInfo = shipprofilerepository.findById(id);
		if (shipProfileInfo != null) {
			return env.getProperty("success");
		}
		return env.getProperty("failure");
	}

	@Override
	public ShipProfileDTO getExpiryDetails(ShipProfileDTO shipProfileDTO) {
		ShipProfileDTO shipProfileDto = new ShipProfileDTO();
		RoleInfo roleInfo = roleRepo.findById(shipProfileDTO.getRoleId());
		UserProfileInfo userProfileInfo = userRepo.findOne(shipProfileDTO.getUserId());
		OrganizationInfo organizationInfo = organizationInfoRepository
				.findOne(userProfileInfo.getOrganizationInfo().getId());
		List<ShipProfileInfo> shipProfileInfos = new ArrayList<ShipProfileInfo>();
		String shipName = null;
		Integer shipCount = null;
		if (roleInfo != null) {
			if (roleInfo.getRoleName().equals(Role.ShipMaster)) {
				shipProfileInfos = shipprofilerepository.findByShipMaster_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null) {
					shipName = shipProfileInfos.get(0).getShipName();
					LOGGER.info("Ship Name Expiry Details" + shipProfileInfos.size());

				}

			}
			if (roleInfo.getRoleName().equals(Role.TechManager)) {
				shipProfileInfos = shipprofilerepository.findByTechMasters_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				shipCount = shipProfileInfos.size();
				LOGGER.info("TechManager Expiry Details" + shipCount);

			}
			if (roleInfo.getRoleName().equals(Role.CommercialManager)) {
				shipProfileInfos = shipprofilerepository
						.findByCommercialMasters_UserNameAndStatusAndShipOrganizationInfo(userProfileInfo.getUserName(),
								1, organizationInfo);
				shipCount = shipProfileInfos.size();
				LOGGER.info("Commercial Expiry Details" + shipCount);

			}

		}
		int greenCount = 0;
		int yellowCount = 0;
		int redCount = 0;
		for (ShipProfileInfo shipProfileInfoObj : shipProfileInfos) {
			int currentVersion = 1;
			List<ExpiryDocumentInfo> expiryInfo = expiryRepository
					.findByShipProfileInfoAndCurrentVersion(shipProfileInfoObj, currentVersion);
			for (ExpiryDocumentInfo expiryDocumentInfo : expiryInfo) {
				Date date = new Date();
				Date days30After = new Date();
				days30After.setTime(days30After.getTime() + 30L * 24 * 60 * 60 * 1000);
				if (expiryDocumentInfo.getExpiryDate().before(date)) {
					LOGGER.info("Before Date" + expiryDocumentInfo.getExpiryDate());
					redCount = redCount + 1;
				} else if (expiryDocumentInfo.getExpiryDate().after(date)
						&& expiryDocumentInfo.getExpiryDate().before(days30After)) {
					LOGGER.info("BetweenDate Date" + expiryDocumentInfo.getExpiryDate());
					yellowCount = yellowCount + 1;
				} else if (expiryDocumentInfo.getExpiryDate().after(days30After)) {
					LOGGER.info("After Date" + expiryDocumentInfo.getExpiryDate());
					greenCount = greenCount + 1;
				}
			}

		}

		if (shipCount != null) {
			shipProfileDto.setShipCount(shipCount);
		}

		if (shipName != null) {
			shipProfileDto.setShipName(shipName);
		}
		LOGGER.info("greenCount" + greenCount);
		shipProfileDto.setGreenCount(greenCount);

		LOGGER.info("yellowCount" + yellowCount);
		shipProfileDto.setYellowCount(yellowCount);

		LOGGER.info("redCount" + redCount);
		shipProfileDto.setRedCount(redCount);
		if (shipProfileDto != null) {
			return shipProfileDto;
		}

		return null;
	}

	@Override
	public List<ShipProfileDTO> getLastSeen(Long userId) {

		UserProfileInfo userInfo = userRepo.findOne(userId);
		List<UserLastSeenInfo> userLastSeenHistory = userLastSeenRepo
				.findTop5ByUserNameOrderByUserLastSeenDateTimeDesc(userInfo.getUserName());
		List<ShipProfileDTO> shipListDTO = new ArrayList<>();
		for (UserLastSeenInfo userLastSeenInfo : userLastSeenHistory) {
			ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
			shipProfileDTO.setUserName(userLastSeenInfo.getUserName());
			LOGGER.info("" + userLastSeenInfo.getUserName());
			shipProfileDTO.setUserLastSeen(userLastSeenInfo.getUserLastSeenDateTime());
			LOGGER.info("" + userLastSeenInfo.getUserLastSeenDateTime());
			shipListDTO.add(shipProfileDTO);
		}
		if (shipListDTO.size() < 0) {
			return null;

		} else {
			return shipListDTO;
		}

	}

	@Override
	public Boolean scanImageFile(String userName, MultipartFile file) {
		LOGGER.info("IMAGE SERVICE");
		UserProfileInfo userProfileInfo1 = userProfileRepository.findByUserName(userName);
		if (userProfileInfo1 != null) {
			/*userProfileInfo1.setPic(file.getOriginalFilename().getBytes());
			userProfileRepository.save(userProfileInfo1);
			if (userProfileInfo1 != null)
				return true;*/
		}
		return false;
	}

	@Override
	public byte[] getByteDataForUserProfile(String userName) {
		UserProfileInfo userProfileInfo = userProfileRepository.findByUserName(userName);
		if (userProfileInfo != null) {
			/*if (userProfileInfo.getPic() != null) {
				return userProfileInfo.getPic();
			}*/
		}
		return null;
	}

	@Override
	public boolean createShipProfile(ShipProfileDTO shipProfileDTO, String shipProfilePicPath) {
		ShipProfileInfo shipProfileInfo = new ShipProfileInfo();

		shipProfileInfo.setBhp(shipProfileDTO.getBhp());
		shipProfileInfo.setBuilder(shipProfileDTO.getBuilder());
		shipProfileInfo.setCallsign(shipProfileDTO.getCallSign());
		shipProfileInfo.setDelivered(shipProfileDTO.getDelivered());
		shipProfileInfo.setEngineType(shipProfileDTO.getEngineType());
		shipProfileInfo.setInternationalGRT(shipProfileDTO.getInternationalGRT());
		shipProfileInfo.setInternationalNRT(shipProfileDTO.getInternationalNRT());
		shipProfileInfo.setKeellaid(shipProfileDTO.getKeelLaid());
		shipProfileInfo.setIMO(shipProfileDTO.getImo());
		shipProfileInfo.setShipName(shipProfileDTO.getShipName());
		shipProfileInfo.setShipOwner(shipProfileDTO.getRegisteredOwner());
		shipProfileInfo.setCountryName(shipProfileDTO.getCountryName());
		shipProfileInfo.setStateName(shipProfileDTO.getStateName());
				// ship mesaurment
		shipProfileInfo.setDwt(shipProfileDTO.getDwt());
		shipProfileInfo.setBreadth(shipProfileDTO.getBreadth());
		shipProfileInfo.setWeight(shipProfileDTO.getWeight());
		shipProfileInfo.setLength(shipProfileDTO.getLength());
		shipProfileInfo.setShipType(shipProfileDTO.getShipTypes());
		shipProfileInfo.setStatus(1);
		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(shipProfileDTO.getOrganizationId());
		shipProfileInfo.setShipOrganizationInfo(organizationInfo);
		Set<UserProfileInfo> techMasters = new HashSet<UserProfileInfo>();
		Set<UserProfileInfo> dataOperators = new HashSet<UserProfileInfo>();

		Set<UserProfileInfo> commercialmanagers = new HashSet<UserProfileInfo>();
		if (shipProfileDTO.getTechManagerIds() != null && shipProfileDTO.getTechManagerIds().length > 0) {
			for (int i = 0; i < shipProfileDTO.getTechManagerIds().length; i++) {
				UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getTechManagerIds()[i]);
				if (userInfo != null) {
					techMasters.add(userInfo);
				}
			}
			shipProfileInfo.setTechMasters(techMasters);

		}
		if (shipProfileDTO.getDataOperatorsIds() != null && shipProfileDTO.getDataOperatorsIds().length > 0) {
			for (int i = 0; i < shipProfileDTO.getDataOperatorsIds().length; i++) {
				UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getTechManagerIds()[i]);
				if (userInfo != null) {
					dataOperators.add(userInfo);
				}
			}
			shipProfileInfo.setDataOperators(dataOperators);
		}

		if (shipProfileDTO.getCommercialMasterIds() != null && shipProfileDTO.getCommercialMasterIds().length > 0) {
			for (int i = 0; i < shipProfileDTO.getCommercialMasterIds().length; i++) {
				UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getCommercialMasterIds()[i]);
				if (userInfo != null) {
					commercialmanagers.add(userInfo);
				}
			}
			shipProfileInfo.setCommercialMasters(commercialmanagers);
		}

		if (shipProfileDTO.getShipMasterId() != null) {
			UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getShipMasterId());
			if (userProfileInfo != null)
				shipProfileInfo.setShipMaster(userProfileInfo);
		}

		if (shipProfilePicPath != null)
			shipProfileInfo.setShipProfilePicPath(shipProfilePicPath);

		shipProfileInfo = shipprofilerepository.save(shipProfileInfo);
		if (shipProfileDTO.getCustomDocumentHolders() != null && shipProfileDTO.getCustomDocumentHolders().size() >0) {
			for(DocumentHolderDTO shipProfileDTO2 : shipProfileDTO.getCustomDocumentHolders()) {
			DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
			documentHolderInfo.setDocumentHolderName(shipProfileDTO2.getDocumentHolderName());
			documentHolderInfo.setDocumentHolderDescription(shipProfileDTO2.getDocumentHolderDescription());
			documentHolderInfo.setOrganizationId(shipProfileDTO.getOrganizationId());
			documentHolderInfo.setUpdatedDate(new Date());
			documentHolderInfo.setType(shipProfileDTO.getType());
			documentHolderInfo.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
			documentHolderInfo.setVesselId(shipProfileInfo.getId());
			documentHolderInfo = documentHolderRepository.saveAndFlush(documentHolderInfo);
		}
		}
		if (shipProfileDTO.getDocumentHolderName() != null) {
			DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
			documentHolderInfo.setDocumentHolderName(shipProfileDTO.getDocumentHolderName());
			documentHolderInfo.setDocumentHolderDescription(shipProfileDTO.getDocumentHolderDescription());
			documentHolderInfo.setOrganizationId(shipProfileDTO.getOrganizationId());
			documentHolderInfo.setUpdatedDate(new Date());
			documentHolderInfo.setType(shipProfileDTO.getType());
			documentHolderInfo.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
			documentHolderInfo.setVesselId(shipProfileInfo.getId());
			documentHolderInfo = documentHolderRepository.saveAndFlush(documentHolderInfo);
			//shipProfileInfo.getCustomDocumentHolders().add(documentHolderInfo);
			//shipProfileInfo.getCustomDocumentHolders().remove(documentHolderInfo);
			//shipProfileInfo = shipprofilerepository.saveAndFlush(shipProfileInfo);
		}
		if (shipProfileDTO.getDocIds() != null) {
			for(long id : shipProfileDTO.getDocIds()) {
				DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(id);
				DocumentHolderInfo documentHolderInfo2 = new DocumentHolderInfo();
				documentHolderInfo2.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
				documentHolderInfo2.setOrganizationId(shipProfileDTO.getOrganizationId());
				documentHolderInfo2.setDocumentHolderDescription(documentHolderInfo.getDocumentHolderDescription());
				documentHolderInfo2.setDocumentHolderType(documentHolderInfo.getDocumentHolderType());
				documentHolderInfo2.setType(documentHolderInfo.getType());
				documentHolderInfo2.setUpdatedDate(new Date());
				documentHolderInfo2.setParantId(documentHolderInfo.getId());
				documentHolderInfo2.setVesselId(shipProfileInfo.getId());
				documentHolderInfo2= documentHolderRepository.saveAndFlush(documentHolderInfo2);
//				shipProfileInfo.getCustomDocumentHolders().add(documentHolderInfo);
//				shipProfileInfo = shipprofilerepository.saveAndFlush(shipProfileInfo);
			}


		}
		 if(shipProfileInfo!=null){
             commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.created"), shipProfileDTO.getLoginId());
             }
		ShipProfileInfo record = shipprofilerepository.findByIMO(shipProfileDTO.getImo());
		if (record == null) {
			return false;

		} else {

			return true;
		}
	}

	@Override
	public Boolean shipDeleteAll(ShipProfileDTO shipProfileDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (userProfileInfo != null) {
			for (Long shipId : shipProfileDTO.getShipIds()) {
				ShipProfileInfo shipProfile = shipprofilerepository.findByIdAndShipOrganizationInfo(shipId,
						userProfileInfo.getOrganizationInfo());
				if (shipProfile != null) {
					shipProfile.getTechMasters().removeAll(shipProfile.getTechMasters());
					shipProfile.getCommercialMasters().removeAll(shipProfile.getCommercialMasters());
					shipProfile.getDataOperators().removeAll(shipProfile.getDataOperators());
					shipprofilerepository.saveAndFlush(shipProfile);

					DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
		            deletedHistoryDTO.setObjectId(shipProfile.getId());
		            deletedHistoryDTO.setObjectOne(shipProfile.getShipName());
		            deletedHistoryDTO.setObjectTwo(shipProfile.getShipOwner());
		            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
		            shipprofilerepository.delete(shipProfile);
		            if(dataDeletedHistoryInfo!=null)
		            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),shipProfile.getShipName(),"Vessel", env.getProperty("history.deleted"), shipProfileDTO.getUserId());

				} else {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String isOrganizationValid(Long organizationid) {
		if (organizationid == null) {
			return env.getProperty("failure");
		}
		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(organizationid);
		if (organizationInfo != null) {
			return env.getProperty("success");
		}
		return env.getProperty("failure");
	}

	@Override
	public List<ShipProfileDTO> shipListBasedOnOrg(Long userId) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(userId);
		if (userProfileInfo != null) {
			List<ShipProfileInfo> shipProfileInfos = shipprofilerepository
					.findByShipOrganizationInfo(userProfileInfo.getOrganizationInfo());
			List<ShipProfileDTO> shipListDTOs = new ArrayList<>();
			for (ShipProfileInfo shipProfileInfo : shipProfileInfos) {
				ShipProfileDTO shipProfileDTO = new ShipProfileDTO();

				shipProfileDTO.setId(shipProfileInfo.getId());
				shipProfileDTO.setBhp(shipProfileInfo.getBhp());
				shipProfileDTO.setBuilder(shipProfileInfo.getBuilder());
				shipProfileDTO.setCallSign(shipProfileInfo.getCallsign());
				shipProfileDTO.setCountryName(shipProfileInfo.getCountryName());
				shipProfileDTO.setDelivered(shipProfileInfo.getDelivered());
				shipProfileDTO.setEngineType(shipProfileInfo.getEngineType());
				shipProfileDTO.setInternationalGRT(shipProfileInfo.getInternationalGRT());
				shipProfileDTO.setInternationalNRT(shipProfileInfo.getInternationalNRT());
				SimpleDateFormat formatter = new SimpleDateFormat(env.getProperty("YYYY-MM-dd"));
				if(shipProfileInfo.getKeellaid()!=null && !shipProfileInfo.getKeellaid().equals("")) {
					String strDate = formatter.format(shipProfileInfo.getKeellaid());
					shipProfileDTO.setKeelLaidString(strDate);
				}
				shipProfileDTO.setImo(shipProfileInfo.getIMO());
				shipProfileDTO.setShipName(shipProfileInfo.getShipName());
				shipProfileDTO.setRegisteredOwner(shipProfileInfo.getShipOwner());
				shipProfileDTO.setShipTypes(shipProfileInfo.getShipType());
				shipProfileDTO.setStateName(shipProfileInfo.getStateName());
								// ship mesaurment
				shipProfileDTO.setDwt(shipProfileInfo.getDwt());
				shipProfileDTO.setBreadth(shipProfileInfo.getBreadth());
				shipProfileDTO.setWeight(shipProfileInfo.getWeight());
				shipProfileDTO.setLength(shipProfileInfo.getLength());
				shipProfileDTO.setStatus(shipProfileInfo.getStatus());
				if(shipProfileInfo.getShipProfilePicPath() != null){
					String shipProfilePicturePath = env.getProperty("picture.path")+shipProfileInfo.getShipProfilePicPath();
					shipProfileDTO.setShipProfilePicPath(shipProfilePicturePath.replace(File.separator, "/"));
				}
				Set<UserDTO> techMasters = new HashSet<UserDTO>();
				Set<UserDTO> dataOperators = new HashSet<UserDTO>();
				Set<UserDTO> commercialmanagers = new HashSet<UserDTO>();
				if (shipProfileInfo.getShipMaster() != null) {
					UserDTO userDTO = new UserDTO();
					User userInfo = userRepository.findByUsername(shipProfileInfo.getShipMaster().getUserName());
					if (userInfo != null) {
						userDTO.setMail(userInfo.getMail());
					}
					userDTO.setUserId(shipProfileInfo.getShipMaster().getId());
					userDTO.setFirstName(shipProfileInfo.getShipMaster().getFirstName());
					userDTO.setLastName(shipProfileInfo.getShipMaster().getLastName());
					userDTO.setUserName(shipProfileInfo.getShipMaster().getUserName());
					userDTO.setStatus(shipProfileInfo.getShipMaster().getStatus().longValue());
					// userDTO.setRoleId(shipProfileInfo.getShipMaster().getRoleId().getId());
					userDTO.setRole(shipProfileInfo.getShipMaster().getRoleId().getRoleName().name());

					shipProfileDTO.setShiMasterInfo(userDTO);
				}

				if (shipProfileInfo.getCommercialMasters() != null
						&& shipProfileInfo.getCommercialMasters().size() > 0) {
					for (UserProfileInfo userProfileInfo1 : shipProfileInfo.getCommercialMasters()) {
						UserDTO userDTO = new UserDTO();
						User userInfo = userRepository.findByUsername(userProfileInfo1.getUserName());
						if (userInfo != null) {
							userDTO.setMail(userInfo.getMail());
						}
						userDTO.setUserId(userProfileInfo1.getId());
						userDTO.setFirstName(userProfileInfo1.getFirstName());
						userDTO.setLastName(userProfileInfo1.getLastName());
						userDTO.setUserName(userProfileInfo1.getUserName());
						userDTO.setStatus(userProfileInfo1.getStatus().longValue());
						// userDTO.setRoleId(userProfileInfo1.getRoleId().getId());
						userDTO.setRole(userProfileInfo1.getRoleId().getRoleName().name());
						// userDTO.setOrganizationId(userProfileInfo.getOrganizationInfo().getId());
						commercialmanagers.add(userDTO);
					}

				}
				if (shipProfileInfo.getTechMasters() != null && shipProfileInfo.getTechMasters().size() > 0) {
					for (UserProfileInfo userProfileInfo1 : shipProfileInfo.getTechMasters()) {
						UserDTO userDTO = new UserDTO();
						User userInfo = userRepository.findByUsername(userProfileInfo1.getUserName());
						if (userInfo != null) {
							userDTO.setMail(userInfo.getMail());
						}
						userDTO.setUserId(userProfileInfo1.getId());
						userDTO.setFirstName(userProfileInfo1.getFirstName());
						userDTO.setLastName(userProfileInfo1.getLastName());
						userDTO.setUserName(userProfileInfo1.getUserName());
						userDTO.setStatus(userProfileInfo1.getStatus().longValue());
						// userDTO.setRoleId(userProfileInfo1.getRoleId().getId());
						// userDTO.setOrganizationId(userProfileInfo.getOrganizationInfo().getId());
						userDTO.setRole(userProfileInfo1.getRoleId().getRoleName().name());
						techMasters.add(userDTO);
					}
				}
				if (shipProfileInfo.getDataOperators() != null && shipProfileInfo.getDataOperators().size() > 0) {
					for (UserProfileInfo userProfileInfo1 : shipProfileInfo.getDataOperators()) {
						UserDTO userDTO = new UserDTO();
						User userInfo = userRepository.findByUsername(userProfileInfo1.getUserName());
						if (userInfo != null) {
							userDTO.setMail(userInfo.getMail());
						}
						userDTO.setFirstName(userProfileInfo1.getFirstName());
						userDTO.setLastName(userProfileInfo1.getLastName());
						userDTO.setUserName(userProfileInfo1.getUserName());
						userDTO.setStatus(userProfileInfo1.getStatus().longValue());
						userDTO.setRoleId(userProfileInfo1.getRoleId().getId());
						// userDTO.setOrganizationId(userProfileInfo.getOrganizationInfo().getId());
						userDTO.setRole(userProfileInfo1.getRoleId().getRoleName().name());
						dataOperators.add(userDTO);
					}
				}
				if (shipProfileInfo.getCustomDocumentHolders() != null) {
					Set<DocumentHolderDTO> documentHolderDTOs = new HashSet<>();
					for (DocumentHolderInfo holderInfo : shipProfileInfo.getCustomDocumentHolders()) {
						DocumentHolderDTO documentHolderDTO = new DocumentHolderDTO();
						documentHolderDTO.setDocumentHolderId(holderInfo.getId());
						documentHolderDTO.setDocumentHolderName(holderInfo.getDocumentHolderName());
						documentHolderDTO.setDocumentHolderDescription(holderInfo.getDocumentHolderDescription());
						documentHolderDTO.setUpdatedDate(holderInfo.getUpdatedDate());
						documentHolderDTO.setType(holderInfo.getType());
						documentHolderDTOs.add(documentHolderDTO);

					}
					shipProfileDTO.setCustomDocumentHolders(documentHolderDTOs);
				}

				shipProfileDTO.setDataOperatorList(dataOperators);
				shipProfileDTO.setTechManagerInfoList(techMasters);
				shipProfileDTO.setCommercialManagerInfoList(commercialmanagers);
				shipListDTOs.add(shipProfileDTO);
			}
			return shipListDTOs;
		}
		return null;
	}

// 	@Override
// 	public boolean updateShipProfile(ShipProfileDTO shipProfileDTO, String shipProfilePicPath) {
// 		List<DocumentHolderDTO> addStandardDocumentHolderInfo = new ArrayList<>();
// 		List<DocumentHolderDTO> addCustomDocumentHolderInfo = new ArrayList<>();
// 		List<DocumentHolderDTO> existCustomDocumentHolderInfo = new ArrayList<>();
// 		List<DocumentHolderDTO> existStandardDocumentHolderInfo = new ArrayList<>();
// 		ShipProfileInfo shipProfileInfo = shipprofilerepository.findOne(shipProfileDTO.getId());
// 		shipProfileInfo.setBhp(shipProfileDTO.getBhp());
// 		shipProfileInfo.setBuilder(shipProfileDTO.getBuilder());
// 		shipProfileInfo.setCallsign(shipProfileDTO.getCallSign());
// 		shipProfileInfo.setDelivered(shipProfileDTO.getDelivered());
// 		shipProfileInfo.setEngineType(shipProfileDTO.getEngineType());
// 		shipProfileInfo.setInternationalGRT(shipProfileDTO.getInternationalGRT());
// 		shipProfileInfo.setInternationalNRT(shipProfileDTO.getInternationalNRT());
// 		shipProfileInfo.setKeellaid(shipProfileDTO.getKeelLaid());
// 		shipProfileInfo.setIMO(shipProfileDTO.getImo());
// 		shipProfileInfo.setShipName(shipProfileDTO.getShipName());
// 		shipProfileInfo.setShipOwner(shipProfileDTO.getRegisteredOwner());
// 		shipProfileInfo.setCountryName(shipProfileDTO.getCountryName());
// 		shipProfileInfo.setStateName(shipProfileDTO.getStateName());
// 		shipProfileInfo.setShipType(shipProfileDTO.getShipTypes());
// 		shipProfileInfo.setStatus(1);
// 		List<DocumentHolderInfo> standardDocumentHolderInfos = documentHolderRepository.findByVesselIdAndType(shipProfileDTO.getId(), env.getProperty("document.holder.type.standard"));
// 		List<DocumentHolderInfo> customDocumentHolderInfos = documentHolderRepository.findByVesselIdAndType(shipProfileDTO.getId(), env.getProperty("document.holder.type.custom"));
// 		OrganizationInfo organizationInfo = organizationInfoRepository.findOne(shipProfileDTO.getOrganizationId());
// 		shipProfileInfo.setShipOrganizationInfo(organizationInfo);
// 		Set<UserProfileInfo> techMasters = new HashSet<UserProfileInfo>();
// 		Set<UserProfileInfo> dataOperators = new HashSet<UserProfileInfo>();

// 		if(shipProfileDTO.getStandardDocumentHolders().size() > 0) {
// 			for(DocumentHolderDTO documentHolderDTO : shipProfileDTO.getStandardDocumentHolders()) {
// 				boolean standardDocumentHolderInfoExist = false;
// 				if(documentHolderDTO.getDocumentHolderId() != null) {
// 					if(standardDocumentHolderInfos.size() > 0) {
// 						for(DocumentHolderInfo documentHolderInfo : standardDocumentHolderInfos) {
// 							if(documentHolderDTO.getDocumentHolderId().longValue() == documentHolderInfo.getId().longValue()) {
// 								standardDocumentHolderInfoExist = true;
// 							}
// 						}
// 						if(standardDocumentHolderInfoExist) {
// 							addStandardDocumentHolderInfo.add(documentHolderDTO);
// 							existStandardDocumentHolderInfo.add(documentHolderDTO);
// 						}
// 						if(!standardDocumentHolderInfoExist) {
// 							addStandardDocumentHolderInfo.add(documentHolderDTO);
// 						}

// 					}else {
// 						addStandardDocumentHolderInfo.add(documentHolderDTO);
// 					}
// 				}else {
// 					addStandardDocumentHolderInfo.add(documentHolderDTO);
// 				}
// 			}
// 		}else {
// 			documentHolderRepository.delete(standardDocumentHolderInfos);
// 		}

// 		if(shipProfileDTO.getCustomDocumentHolders().size() > 0) {
// 			for(DocumentHolderDTO documentHolderDTO : shipProfileDTO.getCustomDocumentHolders()) {
// 				boolean customDocumentHolderInfoExist = false;
// 				if(documentHolderDTO.getDocumentHolderId() != null) {
// 					if(customDocumentHolderInfos.size() > 0) {
// 						for(DocumentHolderInfo documentHolderInfo : customDocumentHolderInfos) {
// 							if(documentHolderDTO.getDocumentHolderId().longValue() == documentHolderInfo.getId().longValue()) {
// 								customDocumentHolderInfoExist = true;
// 							}
// 						}
// 						if(customDocumentHolderInfoExist) {
// 							addCustomDocumentHolderInfo.add(documentHolderDTO);
// 							existCustomDocumentHolderInfo.add(documentHolderDTO);
// 						}
// 						if(!customDocumentHolderInfoExist) {
// 							addCustomDocumentHolderInfo.add(documentHolderDTO);
// 						}
// 					}else {
// 						addCustomDocumentHolderInfo.add(documentHolderDTO);
// 					}
// 				}else {
// 					addCustomDocumentHolderInfo.add(documentHolderDTO);
// 				}
// 			}
// 		}else {
// 			documentHolderRepository.delete(customDocumentHolderInfos);
// 		}

// 		if(existStandardDocumentHolderInfo.size() > 0) {
// 			List<DocumentHolderInfo> removeDocumentHolderInfo = new ArrayList<>();
// 			for(DocumentHolderInfo documentHolderInfo : standardDocumentHolderInfos) {
// 				boolean isExist = false;
// 				for(DocumentHolderDTO documentHolderDTO : existStandardDocumentHolderInfo) {
// 					if(documentHolderInfo.getId().longValue() == documentHolderDTO.getDocumentHolderId().longValue()) {
// 						isExist =true;
// 					}
// 				}
// 				if(!isExist) {
// 					removeDocumentHolderInfo.add(documentHolderInfo);
// 				}
// 			}

// 			if(!removeDocumentHolderInfo.isEmpty() && removeDocumentHolderInfo.size() > 0) {
// 				documentHolderRepository.delete(removeDocumentHolderInfo);
// 			}
// 		}


// 		if(existCustomDocumentHolderInfo.size() > 0) {
// 			List<DocumentHolderInfo> removeCustomDocumentHolderInfo = new ArrayList<>();
// 			for(DocumentHolderInfo documentHolderInfo : customDocumentHolderInfos) {
// 				boolean isExist = false;
// 				for(DocumentHolderDTO documentHolderDTO : existCustomDocumentHolderInfo) {
// 					if(documentHolderInfo.getId().longValue() == documentHolderDTO.getDocumentHolderId().longValue()) {
// 						isExist =true;
// 					}
// 				}
// 				if(!isExist) {
// 					removeCustomDocumentHolderInfo.add(documentHolderInfo);
// 				}
// 			}

// 			if(!removeCustomDocumentHolderInfo.isEmpty() && removeCustomDocumentHolderInfo.size() > 0) {
// 				documentHolderRepository.delete(removeCustomDocumentHolderInfo);
// 			}
// 		}
// 		if(addStandardDocumentHolderInfo.size() > 0) {
// 			Set<DocumentHolderInfo> documentHolderInfos = new HashSet<>();
// 			for(DocumentHolderDTO documentHolderDTO : addStandardDocumentHolderInfo) {

// 				if(documentHolderDTO.getDocumentHolderId() == null) {
// 					DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
// 					documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
// 					documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
// 					documentHolderInfo.setDocumentHolderType(env.getProperty("document.holder.type"));
// 					documentHolderInfo.setType(env.getProperty("document.holder.type.standard"));
// 					documentHolderInfo.setOrganizationId(shipProfileInfo.getShipOrganizationInfo().getId());
// 					documentHolderInfo.setUpdatedDate(new Date());
// 					documentHolderInfo.setVesselId(shipProfileInfo.getId());
// 					documentHolderInfos.add(documentHolderInfo);
// 				}else {
// 					DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(documentHolderDTO.getDocumentHolderId());
// 					if (documentHolderInfo.getVesselId()== null || documentHolderInfo.getVesselId().longValue() != shipProfileInfo.getId().longValue()) {
// 					DocumentHolderInfo documentHolderInfo2 = new DocumentHolderInfo();
// 					documentHolderInfo2.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
// 					documentHolderInfo2.setDocumentHolderDescription(documentHolderInfo.getDocumentHolderDescription());
// 					documentHolderInfo2.setDocumentHolderType(documentHolderInfo.getDocumentHolderType());
// 					documentHolderInfo2.setType(env.getProperty("document.holder.type.standard"));
// 					documentHolderInfo2.setVesselId(shipProfileInfo.getId());
// 					documentHolderInfo2.setUpdatedDate(new Date());
// 					documentHolderInfos.add(documentHolderInfo2);
// 				}else {
// 					documentHolderInfos.add(documentHolderInfo);
// 				}
// 				}
// 			}

// 			if(documentHolderInfos.size() > 0) {
// 				documentHolderRepository.save(documentHolderInfos);
// 			}
// 		}

// 		if(addCustomDocumentHolderInfo.size() > 0) {
// 			List<DocumentHolderInfo> documentHolderInfos = new ArrayList<>();
// 			for(DocumentHolderDTO documentHolderDTO : addCustomDocumentHolderInfo) {
// 				if(documentHolderDTO.getDocumentHolderId() == null) {
// 					DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
// 					documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
// 					documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
// 					documentHolderInfo.setDocumentHolderType(env.getProperty("document.holder.type"));
// 					documentHolderInfo.setType(env.getProperty("document.holder.type.custom"));
// 					documentHolderInfo.setOrganizationId(shipProfileInfo.getShipOrganizationInfo().getId());
// 					documentHolderInfo.setUpdatedDate(new Date());
// 					documentHolderInfo.setVesselId(shipProfileInfo.getId());
// 					documentHolderInfos.add(documentHolderInfo);
// 				}else {
// 					DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(documentHolderDTO.getDocumentHolderId());
// 					documentHolderInfos.add(documentHolderInfo);
// 				}
// 			}

// 			if(documentHolderInfos.size() > 0) {
// 				documentHolderRepository.save(documentHolderInfos);
// 			}
// 		}

// 		Set<UserProfileInfo> commercialmanagers = new HashSet<UserProfileInfo>();
// 		if (shipProfileDTO.getTechManagerIds() != null && shipProfileDTO.getTechManagerIds().length > 0) {
// 			for (int i = 0; i < shipProfileDTO.getTechManagerIds().length; i++) {
// 				UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getTechManagerIds()[i]);
// 				if (userInfo != null) {
// 					techMasters.add(userInfo);
// 				}
// 			}
// 			shipProfileInfo.setTechMasters(techMasters);
// 		}
// 		if (shipProfileDTO.getDataOperatorsIds() != null && shipProfileDTO.getDataOperatorsIds().length > 0) {
// 			for (int i = 0; i < shipProfileDTO.getDataOperatorsIds().length; i++) {
// 				UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getTechManagerIds()[i]);
// 				if (userInfo != null) {
// 					dataOperators.add(userInfo);
// 				}
// 			}
// 			shipProfileInfo.setDataOperators(dataOperators);
// 		}

// 		if (shipProfileDTO.getCommercialMasterIds() != null && shipProfileDTO.getCommercialMasterIds().length > 0) {
// 			for (int i = 0; i < shipProfileDTO.getCommercialMasterIds().length; i++) {
// 				UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getCommercialMasterIds()[i]);
// 				if (userInfo != null) {
// 					commercialmanagers.add(userInfo);
// 				}
// 			}
// 			shipProfileInfo.setCommercialMasters(commercialmanagers);
// 		}

// 		if (shipProfileDTO.getShipMasterId() != null) {
// 			UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getShipMasterId());
// 			if (userProfileInfo != null)
// 				shipProfileInfo.setShipMaster(userProfileInfo);
// 		}

// 		if (shipProfilePicPath != null)
// 			shipProfileInfo.setShipProfilePicPath(shipProfilePicPath);

// 		shipProfileInfo = shipprofilerepository.saveAndFlush(shipProfileInfo);
// //		if (shipProfileDTO.getDocIds() != null ) {
// //			for(Long documentHolderId : shipProfileDTO.getDocIds()) {
// //				DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(documentHolderId);
// //				if (documentHolderInfo != null) {
// //					documentHolderInfo.setVesselId((long)0);
// //					documentHolderInfo.setParantId((long)0);
// //					documentHolderRepository.saveAndFlush(documentHolderInfo);
// //				}
// //			}
// //		}
// 		 if(shipProfileInfo!=null){
//              commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.updated"), shipProfileDTO.getLoginId());
//              }
// 		ShipProfileInfo record = shipprofilerepository.findByIMO(shipProfileDTO.getImo());
// 		if (record == null) {
// 			return false;

// 		} else {
// 			return true;
// 		}
// 	}

@Override
  public boolean updateShipProfile(ShipProfileDTO shipProfileDTO, String shipProfilePicPath) {
    List<DocumentHolderDTO> addStandardDocumentHolderInfo = new ArrayList<>();
    List<DocumentHolderDTO> addCustomDocumentHolderInfo = new ArrayList<>();
    List<DocumentHolderDTO> existCustomDocumentHolderInfo = new ArrayList<>();
    List<DocumentHolderDTO> existStandardDocumentHolderInfo = new ArrayList<>();
    ShipProfileInfo shipProfileInfo = shipprofilerepository.findOne(shipProfileDTO.getId());
    shipProfileInfo.setBhp(shipProfileDTO.getBhp());
    shipProfileInfo.setBuilder(shipProfileDTO.getBuilder());
    shipProfileInfo.setCallsign(shipProfileDTO.getCallSign());
    shipProfileInfo.setDelivered(shipProfileDTO.getDelivered());
    shipProfileInfo.setEngineType(shipProfileDTO.getEngineType());
    shipProfileInfo.setInternationalGRT(shipProfileDTO.getInternationalGRT());
    shipProfileInfo.setInternationalNRT(shipProfileDTO.getInternationalNRT());
    shipProfileInfo.setKeellaid(shipProfileDTO.getKeelLaid());
    shipProfileInfo.setIMO(shipProfileDTO.getImo());
    shipProfileInfo.setShipName(shipProfileDTO.getShipName());
    shipProfileInfo.setShipOwner(shipProfileDTO.getRegisteredOwner());
    shipProfileInfo.setCountryName(shipProfileDTO.getCountryName());
    shipProfileInfo.setStateName(shipProfileDTO.getStateName());
		// ship measurement
		shipProfileInfo.setDwt(shipProfileDTO.getDwt());
		shipProfileInfo.setBreadth(shipProfileDTO.getBreadth());
		shipProfileInfo.setWeight(shipProfileDTO.getWeight());
		shipProfileInfo.setLength(shipProfileDTO.getLength());
    shipProfileInfo.setShipType(shipProfileDTO.getShipTypes());
    shipProfileInfo.setStatus(1);
    List<DocumentHolderInfo> standardDocumentHolderInfos = documentHolderRepository.findByVesselIdAndType(shipProfileDTO.getId(), env.getProperty("document.holder.type.standard"));
    List<DocumentHolderInfo> customDocumentHolderInfos = documentHolderRepository.findByVesselIdAndType(shipProfileDTO.getId(), env.getProperty("document.holder.type.custom"));
    OrganizationInfo organizationInfo = organizationInfoRepository.findOne(shipProfileDTO.getOrganizationId());
    shipProfileInfo.setShipOrganizationInfo(organizationInfo);
    Set<UserProfileInfo> techMasters = new HashSet<UserProfileInfo>();
    Set<UserProfileInfo> dataOperators = new HashSet<UserProfileInfo>();

    // if(shipProfileDTO.getStandardDocumentHolders().size() > 0) {
		if(shipProfileDTO.getStandardDocumentHolders() != null && shipProfileDTO.getStandardDocumentHolders().size() > 0) {
      for(DocumentHolderDTO documentHolderDTO : shipProfileDTO.getStandardDocumentHolders()) {
        boolean standardDocumentHolderInfoExist = false;
        if(documentHolderDTO.getDocumentHolderId() != null) {
          if(standardDocumentHolderInfos.size() > 0) {
            for(DocumentHolderInfo documentHolderInfo : standardDocumentHolderInfos) {
              if(documentHolderDTO.getDocumentHolderId().longValue() == documentHolderInfo.getId().longValue()) {
                standardDocumentHolderInfoExist = true;
              }
            }
            if(standardDocumentHolderInfoExist) {
              addStandardDocumentHolderInfo.add(documentHolderDTO);
              existStandardDocumentHolderInfo.add(documentHolderDTO);
            }
            if(!standardDocumentHolderInfoExist) {
              addStandardDocumentHolderInfo.add(documentHolderDTO);
            }

          }else {
            addStandardDocumentHolderInfo.add(documentHolderDTO);
          }
        }else {
          addStandardDocumentHolderInfo.add(documentHolderDTO);
        }
      }
    }else {
      documentHolderRepository.delete(standardDocumentHolderInfos);
    }

    // if(shipProfileDTO.getCustomDocumentHolders().size() > 0) {
		if(shipProfileDTO.getCustomDocumentHolders() != null && shipProfileDTO.getCustomDocumentHolders().size() > 0) {
      for(DocumentHolderDTO documentHolderDTO : shipProfileDTO.getCustomDocumentHolders()) {
        boolean customDocumentHolderInfoExist = false;
        if(documentHolderDTO.getDocumentHolderId() != null) {
          if(customDocumentHolderInfos.size() > 0) {
            for(DocumentHolderInfo documentHolderInfo : customDocumentHolderInfos) {
              if(documentHolderDTO.getDocumentHolderId().longValue() == documentHolderInfo.getId().longValue()) {
                customDocumentHolderInfoExist = true;
              }
            }
            if(customDocumentHolderInfoExist) {
              addCustomDocumentHolderInfo.add(documentHolderDTO);
              existCustomDocumentHolderInfo.add(documentHolderDTO);
            }
            if(!customDocumentHolderInfoExist) {
              addCustomDocumentHolderInfo.add(documentHolderDTO);
            }
          }else {
            addCustomDocumentHolderInfo.add(documentHolderDTO);
          }
        }else {
          addCustomDocumentHolderInfo.add(documentHolderDTO);
        }
      }
    }else {
      documentHolderRepository.delete(customDocumentHolderInfos);
    }

    // 🔥 NEW CORRECTED DELETE LOGIC 🔥
    if (standardDocumentHolderInfos != null && standardDocumentHolderInfos.size() > 0) {
        List<DocumentHolderInfo> removeDocumentHolderInfo = new ArrayList<>();

        for (DocumentHolderInfo child : standardDocumentHolderInfos) {
            boolean stillSelected = false;

            for (DocumentHolderDTO dto : shipProfileDTO.getStandardDocumentHolders()) {
                if (child.getParantId() != null &&
                    child.getParantId().longValue() == dto.getDocumentHolderId().longValue()) {
                    stillSelected = true;
                    break;
                }
            }

            if (!stillSelected) {
                removeDocumentHolderInfo.add(child);
            }
        }

        if (!removeDocumentHolderInfo.isEmpty()) {
            documentHolderRepository.delete(removeDocumentHolderInfo);
        }
    }


    if(existCustomDocumentHolderInfo.size() > 0) {
      List<DocumentHolderInfo> removeCustomDocumentHolderInfo = new ArrayList<>();
      for(DocumentHolderInfo documentHolderInfo : customDocumentHolderInfos) {
        boolean isExist = false;
        for(DocumentHolderDTO documentHolderDTO : existCustomDocumentHolderInfo) {
          if(documentHolderInfo.getId().longValue() == documentHolderDTO.getDocumentHolderId().longValue()) {
            isExist =true;
          }
        }
        if(!isExist) {
          removeCustomDocumentHolderInfo.add(documentHolderInfo);
        }
      }

      if(!removeCustomDocumentHolderInfo.isEmpty() && removeCustomDocumentHolderInfo.size() > 0) {
        documentHolderRepository.delete(removeCustomDocumentHolderInfo);
      }
    }

    // 🔥 NEW CORRECTED ADD LOGIC 🔥
    if(addStandardDocumentHolderInfo.size() > 0) {
      Set<DocumentHolderInfo> documentHolderInfos = new HashSet<>();
      for(DocumentHolderDTO documentHolderDTO : addStandardDocumentHolderInfo) {

        if(documentHolderDTO.getDocumentHolderId() == null) {
          DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
          documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
          documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
          documentHolderInfo.setDocumentHolderType(env.getProperty("document.holder.type"));
          documentHolderInfo.setType(env.getProperty("document.holder.type.standard"));
          documentHolderInfo.setOrganizationId(shipProfileInfo.getShipOrganizationInfo().getId());
          documentHolderInfo.setUpdatedDate(new Date());
          documentHolderInfo.setVesselId(shipProfileInfo.getId());
          documentHolderInfos.add(documentHolderInfo);
        } else {

          DocumentHolderInfo master = documentHolderRepository.findOne(documentHolderDTO.getDocumentHolderId());

          boolean alreadyExists = false;

          for (DocumentHolderInfo existingChild : standardDocumentHolderInfos) {
              if (existingChild.getParantId() != null &&
                  existingChild.getParantId().longValue() == master.getId().longValue()) {
                  alreadyExists = true;
                  break;
              }
          }

          if (!alreadyExists) {
              DocumentHolderInfo child = new DocumentHolderInfo();
              child.setDocumentHolderName(master.getDocumentHolderName());
              child.setDocumentHolderDescription(master.getDocumentHolderDescription());
              child.setDocumentHolderType(master.getDocumentHolderType());
              child.setType(env.getProperty("document.holder.type.standard"));
              child.setOrganizationId(master.getOrganizationId());
              child.setParantId(master.getId());     // 🔥 IMPORTANT
              child.setVesselId(shipProfileInfo.getId());
              child.setUpdatedDate(new Date());

              documentHolderInfos.add(child);
          }
        }
      }

      if(documentHolderInfos.size() > 0) {
        documentHolderRepository.save(documentHolderInfos);
      }
    }

    if(addCustomDocumentHolderInfo.size() > 0) {
      List<DocumentHolderInfo> documentHolderInfos = new ArrayList<>();
      for(DocumentHolderDTO documentHolderDTO : addCustomDocumentHolderInfo) {
        if(documentHolderDTO.getDocumentHolderId() == null) {
          DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
          documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
          documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
          documentHolderInfo.setDocumentHolderType(env.getProperty("document.holder.type"));
          documentHolderInfo.setType(env.getProperty("document.holder.type.custom"));
          documentHolderInfo.setOrganizationId(shipProfileInfo.getShipOrganizationInfo().getId());
          documentHolderInfo.setUpdatedDate(new Date());
          documentHolderInfo.setVesselId(shipProfileInfo.getId());
          documentHolderInfos.add(documentHolderInfo);
        }else {
          DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(documentHolderDTO.getDocumentHolderId());
          documentHolderInfos.add(documentHolderInfo);
        }
      }

      if(documentHolderInfos.size() > 0) {
        documentHolderRepository.save(documentHolderInfos);
      }
    }

    Set<UserProfileInfo> commercialmanagers = new HashSet<UserProfileInfo>();
    if (shipProfileDTO.getTechManagerIds() != null && shipProfileDTO.getTechManagerIds().length > 0) {
      for (int i = 0; i < shipProfileDTO.getTechManagerIds().length; i++) {
        UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getTechManagerIds()[i]);
        if (userInfo != null) {
          techMasters.add(userInfo);
        }
      }
      shipProfileInfo.setTechMasters(techMasters);
    }
    if (shipProfileDTO.getDataOperatorsIds() != null && shipProfileDTO.getDataOperatorsIds().length > 0) {
      for (int i = 0; i < shipProfileDTO.getDataOperatorsIds().length; i++) {
        UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getTechManagerIds()[i]);
        if (userInfo != null) {
          dataOperators.add(userInfo);
        }
      }
      shipProfileInfo.setDataOperators(dataOperators);
    }

    if (shipProfileDTO.getCommercialMasterIds() != null && shipProfileDTO.getCommercialMasterIds().length > 0) {
      for (int i = 0; i < shipProfileDTO.getCommercialMasterIds().length; i++) {
        UserProfileInfo userInfo = userProfileRepository.findOne(shipProfileDTO.getCommercialMasterIds()[i]);
        if (userInfo != null) {
          commercialmanagers.add(userInfo);
        }
      }
      shipProfileInfo.setCommercialMasters(commercialmanagers);
    }

    if (shipProfileDTO.getShipMasterId() != null) {
      UserProfileInfo userProfileInfo = userProfileRepository.findOne(shipProfileDTO.getShipMasterId());
      if (userProfileInfo != null)
        shipProfileInfo.setShipMaster(userProfileInfo);
    }

    if (shipProfilePicPath != null)
      shipProfileInfo.setShipProfilePicPath(shipProfilePicPath);

    shipProfileInfo = shipprofilerepository.saveAndFlush(shipProfileInfo);
//    if (shipProfileDTO.getDocIds() != null ) {
//      for(Long documentHolderId : shipProfileDTO.getDocIds()) {
//        DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(documentHolderId);
//        if (documentHolderInfo != null) {
//          documentHolderInfo.setVesselId((long)0);
//          documentHolderInfo.setParantId((long)0);
//          documentHolderRepository.saveAndFlush(documentHolderInfo);
//        }
//      }
//    }
     if(shipProfileInfo!=null){
             commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.updated"), shipProfileDTO.getLoginId());
             }
    ShipProfileInfo record = shipprofilerepository.findByIMO(shipProfileDTO.getImo());
    if (record == null) {
      return false;

    } else {
      return true;
    }
  }
	@Override
	public Boolean updateShipStatus(ShipProfileDTO shipProfileDTO) {
		ShipProfileInfo shipProfileInfo = shipprofilerepository.findOne(shipProfileDTO.getId());
		if (shipProfileInfo != null) {
			shipProfileInfo.setStatus(shipProfileDTO.getStatus());
			shipProfileInfo = shipprofilerepository.save(shipProfileInfo);
			 if(shipProfileInfo!=null){
                 commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.created"), shipProfileDTO.getLoginId());
                 }
			return true;
		}
		return false;
	}

	@Override
	public Boolean updateShipStatusAll(ShipProfileDTO shipProfileDTO) {
		for (Long shipId : shipProfileDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipprofilerepository.findOne(shipId);
			if (shipProfileInfo != null) {
				shipProfileInfo.setStatus(shipProfileDTO.getStatus());
				shipProfileInfo = shipprofilerepository.save(shipProfileInfo);
				if(shipProfileInfo!=null){
	                 commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.created"), shipProfileDTO.getLoginId());
	                 }
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<ShipProfileDTO> viewShipProfile(Long userId) {

		UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
		OrganizationInfo organizationInfo = organizationInfoRepository
				.findOne(userProfileInfo.getOrganizationInfo().getId());
		List<ShipProfileInfo> shipProfileInfos = null;
		List<ShipProfileDTO> shipProfileDTOs = new ArrayList<ShipProfileDTO>();
		if (userProfileInfo != null) {
			if (userProfileInfo.getRoleId().getRoleName().equals(Role.TechManager)) {
				shipProfileInfos = shipprofilerepository.findByTechMasters_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility.convertShipProfileINfoToShipProfileDTOs(shipProfileInfos);
					return shipProfileDTOs;
				}
			} else if (userProfileInfo.getRoleId().getRoleName().equals(Role.ShipMaster)) {
				shipProfileInfos = shipprofilerepository.findByShipMaster_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility.convertShipProfileINfoToShipProfileDTOs(shipProfileInfos);
					return shipProfileDTOs;
				}
			} else if (userProfileInfo.getRoleId().getRoleName().equals(Role.CommercialManager)) {
				shipProfileInfos = shipprofilerepository
						.findByCommercialMasters_UserNameAndStatusAndShipOrganizationInfo(userProfileInfo.getUserName(),
								1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility.convertShipProfileINfoToShipProfileDTOs(shipProfileInfos);
					return shipProfileDTOs;
				}
			} else if (userProfileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
				shipProfileInfos = shipprofilerepository.findByStatusAndShipOrganizationInfo(1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility.convertShipProfileINfoToShipProfileDTOs(shipProfileInfos);
					return shipProfileDTOs;
				}

			}

		}
		return shipProfileDTOs;
	}

	@Override
	public List<ShipProfileDTO> getDashboardTopCount(Long userId) {

		UserProfileInfo userProfileInfo = userProfileRepository.findById(userId);
		OrganizationInfo organizationInfo = organizationInfoRepository
				.findOne(userProfileInfo.getOrganizationInfo().getId());
		List<ShipProfileInfo> shipProfileInfos = null;
		List<ShipProfileDTO> shipProfileDTOs = new ArrayList<ShipProfileDTO>();
		if (userProfileInfo != null) {
			Role roleName = userProfileInfo.getRoleId().getRoleName();
			if (roleName.equals(Role.TechManager)) {
				shipProfileInfos = shipprofilerepository.findByTechMasters_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility
							.convertShipProfileINfoToShipProfileDTOsForDashboardCount(shipProfileInfos, roleName, userProfileInfo);
					return shipProfileDTOs;
				}
			} else if (roleName.equals(Role.ShipMaster)) {
				shipProfileInfos = shipprofilerepository.findByShipMaster_UserNameAndStatusAndShipOrganizationInfo(
						userProfileInfo.getUserName(), 1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility
							.convertShipProfileINfoToShipProfileDTOsForDashboardCount(shipProfileInfos, roleName, userProfileInfo);
					return shipProfileDTOs;

				}
			} else if (roleName.equals(Role.CommercialManager)) {
				shipProfileInfos = shipprofilerepository
						.findByCommercialMasters_UserNameAndStatusAndShipOrganizationInfo(userProfileInfo.getUserName(),
								1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility
							.convertShipProfileINfoToShipProfileDTOsForDashboardCount(shipProfileInfos, roleName, userProfileInfo);
					return shipProfileDTOs;
				}
			} else if (roleName.equals(Role.Admin)) {
				shipProfileInfos = shipprofilerepository.findByStatusAndShipOrganizationInfo(1, organizationInfo);
				if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
					shipProfileDTOs = shipProfileUtility
							.convertShipProfileINfoToShipProfileDTOsForDashboardCount(shipProfileInfos, roleName,userProfileInfo);
					return shipProfileDTOs;
				}else {
					shipProfileDTOs = shipProfileUtility
							.convertShipProfileINfoToShipProfileDTOsForDashboardCount(null, roleName,userProfileInfo);
					return shipProfileDTOs;
				}

			}

		}
		return shipProfileDTOs;
	}

	@Override
	public String activateAllVesselsBasedOrganization(ShipProfileDTO shipProfileDTO) {
		List<ShipProfileInfo> shipProfileInfos = new ArrayList<>();
		if (shipProfileDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (shipProfileDTO.getShipIds() == null)
			return env.getProperty("ship.ids.null");

		if (shipProfileDTO.getShipIds().length == 0)
			return env.getProperty("ship.ids.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("ship.activate.admin.only");
		}

		for (Long shipId : shipProfileDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipprofilerepository.findByIdAndShipOrganizationInfo(shipId,
					profileInfo.getOrganizationInfo());
			if (shipProfileInfo != null) {
				shipProfileInfo.setStatus(1);
				shipProfileInfos.add(shipProfileInfo);
				 if(shipProfileInfos!=null){
	                 commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.activated"), shipProfileDTO.getUserId());
	                 }
			} else {
				return env.getProperty("ship.not.found");
			}
		}

		if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
			shipprofilerepository.save(shipProfileInfos);
			return env.getProperty("success");
		}

		return null;
	}

	@Override
	public String deActivateAllVesselsBasedOrganization(ShipProfileDTO shipProfileDTO) {
		List<ShipProfileInfo> shipProfileInfos = new ArrayList<>();
		if (shipProfileDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (shipProfileDTO.getShipIds() == null)
			return env.getProperty("ship.ids.null");

		if (shipProfileDTO.getShipIds().length == 0)
			return env.getProperty("ship.ids.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("ship.deactivate.admin.only");
		}

		for (Long shipId : shipProfileDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipprofilerepository.findByIdAndShipOrganizationInfo(shipId,
					profileInfo.getOrganizationInfo());
			if (shipProfileInfo != null) {
				shipProfileInfo.setStatus(0);
				shipProfileInfos.add(shipProfileInfo);
				 if(shipProfileInfos!=null){
	                 commonMethodsUtility.maintainHistory(shipProfileInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.deactivated"), shipProfileDTO.getUserId());
	                 }
			} else {
				return env.getProperty("ship.not.found");
			}
		}

		if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
			shipprofilerepository.save(shipProfileInfos);
			return env.getProperty("success");
		}
		return null;
	}

	@Override
	public String deleteShipProfileInformationBasedOrganization(ShipProfileDTO shipProfileDTO) {
		if (shipProfileDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (shipProfileDTO.getId() == null)
			return env.getProperty("ship.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("ship.delete.admin.only");
		}

		ShipProfileInfo shipProfileInfo = shipprofilerepository.findByIdAndShipOrganizationInfo(shipProfileDTO.getId(),
				profileInfo.getOrganizationInfo());
		if (shipProfileInfo != null) {
			shipProfileInfo.getTechMasters().removeAll(shipProfileInfo.getTechMasters());
			shipProfileInfo.getCommercialMasters().removeAll(shipProfileInfo.getCommercialMasters());
			shipProfileInfo.getDataOperators().removeAll(shipProfileInfo.getDataOperators());
			shipprofilerepository.saveAndFlush(shipProfileInfo);
			DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
			deletedHistoryDTO.setObjectId(shipProfileInfo.getId());
			deletedHistoryDTO.setObjectOne(shipProfileInfo.getShipName());
			deletedHistoryDTO.setObjectTwo(shipProfileInfo.getShipOwner());
            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
            shipprofilerepository.delete(shipProfileInfo);
            if(dataDeletedHistoryInfo!=null)
            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.deleted"), shipProfileDTO.getUserId());

			return env.getProperty("success");
		} else {
			return env.getProperty("ship.not.found");
		}
	}

	@Override
	public String deleteShipMasterFromShipProfileInfoBasedOrganization(ShipProfileDTO shipProfileDTO) {
		List<UserProfileInfo> reomveShipMasterInfos = new ArrayList<>();
		if (shipProfileDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (shipProfileDTO.getId() == null)
			return env.getProperty("ship.id.null");

		if (shipProfileDTO.getShipMasterId() == null)
			return env.getProperty("ship.master.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}
		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("ship.master.delete.admin.only");
		}

		if (shipProfileDTO.getShipMasterId() != null) {
			UserProfileInfo shipMasterUserProfile = userProfileRepository.findOne(shipProfileDTO.getShipMasterId());
			if (shipMasterUserProfile != null) {
				if (shipMasterUserProfile.getRoleId().getRoleName().equals(Role.ShipMaster)) {
					// reomveShipMasterInfos.add(techMangerUserProfile);
					ShipProfileInfo shipProfileInfo = shipprofilerepository
							.findByIdAndShipOrganizationInfo(shipProfileDTO.getId(), profileInfo.getOrganizationInfo());
					if (shipProfileInfo != null) {
						shipProfileInfo.setShipMaster(null);
						DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
			            deletedHistoryDTO.setObjectId(shipProfileInfo.getId());
			            deletedHistoryDTO.setObjectOne(shipProfileInfo.getShipName());
			            deletedHistoryDTO.setObjectTwo(shipProfileInfo.getShipOwner());
			            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
			            shipprofilerepository.saveAndFlush(shipProfileInfo);
			            if(dataDeletedHistoryInfo!=null)
			            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),shipProfileInfo.getShipName(),"Vessel", env.getProperty("history.deleted"), shipProfileDTO.getUserId());
						return env.getProperty("success");
					} else {
						return env.getProperty("ship.not.found");
					}
				} else {
					return env.getProperty("ship.master.not.matching");
				}
			} else {
				return env.getProperty("ship.master.not.found");
			}
		}
		return env.getProperty("failure");
	}

	@Override
	public String deleteTechnicalManagersFromShipProfileInfoBasedOrganization(ShipProfileDTO shipProfileDTO) {
		List<UserProfileInfo> reomveTechMnagerInfos = new ArrayList<>();
		if (shipProfileDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (shipProfileDTO.getId() == null)
			return env.getProperty("ship.id.null");

		if (shipProfileDTO.getTechManagerIds() == null)
			return env.getProperty("tech.manager.id.null");

		if (shipProfileDTO.getTechManagerIds().length == 0)
			return env.getProperty("tech.manager.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}
		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("tech.manager.delete.admin.only");
		}

		for (Long techManagerId : shipProfileDTO.getTechManagerIds()) {
			UserProfileInfo techMangerUserProfile = userProfileRepository.findOne(techManagerId);
			if (techMangerUserProfile != null) {
				if (techMangerUserProfile.getRoleId().getRoleName().equals(Role.TechManager)) {
					reomveTechMnagerInfos.add(techMangerUserProfile);
				} else {
					return env.getProperty("tech.manager.not.matching");
				}

			} else {
				return env.getProperty("tech.manager.not.found");
			}
		}

		ShipProfileInfo shipProfileInfo = shipprofilerepository.findByIdAndShipOrganizationInfo(shipProfileDTO.getId(),
				profileInfo.getOrganizationInfo());
		if (shipProfileInfo != null) {
			if (shipProfileInfo.getTechMasters() != null && shipProfileInfo.getTechMasters().size() > 0) {
				/*
				 * for(UserProfileInfo removeUserProfileInfo : reomveTechMnagerInfos){
				 * for(UserProfileInfo userProfileInfo : shipProfileInfo.getTechMasters()){
				 * if(!removeUserProfileInfo.equals(userProfileInfo)){ return
				 * env.getProperty("tech.manager.not.available"); } } }
				 */
				shipProfileInfo.getTechMasters().removeAll(reomveTechMnagerInfos);
				DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
	            deletedHistoryDTO.setObjectId(shipProfileInfo.getId());
	            deletedHistoryDTO.setObjectOne(shipProfileInfo.getShipName());
	            deletedHistoryDTO.setObjectTwo(shipProfileInfo.getShipOwner());
	            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
	            shipprofilerepository.saveAndFlush(shipProfileInfo);
	            if(dataDeletedHistoryInfo!=null)
	            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),shipProfileInfo.getShipName(),"Ship", env.getProperty("history.deleted"), shipProfileDTO.getUserId());
				return env.getProperty("success");
			} else {
				return env.getProperty("tech.manager.list.not.available");
			}

		} else {
			return env.getProperty("ship.not.found");
		}
	}

	@Override
	public String deleteCommercialManagersFromShipProfileInfoBasedOrganization(ShipProfileDTO shipProfileDTO) {
		List<UserProfileInfo> removeCommercialManagerInfos = new ArrayList<>();
		if (shipProfileDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (shipProfileDTO.getId() == null)
			return env.getProperty("ship.id.null");

		if (shipProfileDTO.getCommercialMasterIds() == null)
			return env.getProperty("tech.manager.id.null");

		if (shipProfileDTO.getCommercialMasterIds().length == 0)
			return env.getProperty("tech.manager.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(shipProfileDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}
		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("commercial.manager.delete.admin.only");
		}

		for (Long commercialManagerId : shipProfileDTO.getCommercialMasterIds()) {
			UserProfileInfo commericalManagerUserProfile = userProfileRepository.findOne(commercialManagerId);
			if (commericalManagerUserProfile != null) {
				if (commericalManagerUserProfile.getRoleId().getRoleName().equals(Role.CommercialManager)) {
					removeCommercialManagerInfos.add(commericalManagerUserProfile);
				} else {
					return env.getProperty("commercial.manager.not.matching");
				}
			} else {
				return env.getProperty("commercial.manager.not.found");
			}
		}

		ShipProfileInfo shipProfileInfo = shipprofilerepository.findByIdAndShipOrganizationInfo(shipProfileDTO.getId(),
				profileInfo.getOrganizationInfo());
		if (shipProfileInfo != null) {
			if (shipProfileInfo.getCommercialMasters() != null && shipProfileInfo.getCommercialMasters().size() > 0) {
				/*
				 * for(UserProfileInfo removeUserProfileInfo : removeCommercialManagerInfos){
				 * for(UserProfileInfo userProfileInfo :
				 * shipProfileInfo.getCommercialMasters()){
				 * if(!removeUserProfileInfo.equals(userProfileInfo)){ return
				 * env.getProperty("commercial.manager.not.available"); } } }
				 */
				shipProfileInfo.getCommercialMasters().removeAll(removeCommercialManagerInfos);
				DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
	            deletedHistoryDTO.setObjectId(shipProfileInfo.getId());
	            deletedHistoryDTO.setObjectOne(shipProfileInfo.getShipName());
	            deletedHistoryDTO.setObjectTwo(shipProfileInfo.getShipOwner());
	            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
	            shipprofilerepository.saveAndFlush(shipProfileInfo);
	            if(dataDeletedHistoryInfo!=null)
	            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),shipProfileInfo.getShipName(),"Ship", env.getProperty("history.deleted"), shipProfileDTO.getUserId());
				return env.getProperty("success");
			} else {
				return env.getProperty("commercial.manager.list.not.available");
			}

		} else {
			return env.getProperty("ship.not.found");
		}
	}

	@Override
	public List<ShipProfileDTO> listShipProfileInfoBasedOrganization(OrganizationInfo organizationInfo) {
		List<ShipProfileDTO> shipProfileDTOs = new ArrayList<>();
		if (organizationInfo != null) {
			List<ShipProfileInfo> shipProfileInfos = shipprofilerepository.findByShipOrganizationInfoAndStatus(organizationInfo,1);
			if (shipProfileInfos != null && shipProfileInfos.size() > 0) {
				for (ShipProfileInfo shipProfileInfo : shipProfileInfos) {
					ShipProfileDTO shipProfileDTO = new ShipProfileDTO();
					shipProfileDTO.setId(shipProfileInfo.getId());
					shipProfileDTO.setShipName(shipProfileInfo.getShipName());
					shipProfileDTO.setImo(shipProfileInfo.getIMO());
					shipProfileDTOs.add(shipProfileDTO);
				}
				return shipProfileDTOs;
			}
		}
		return null;
	}

	@Override
	public ShipProfileDTO getDashboardTopCountBasedOnVessel(Long vesselId) {
		ShipProfileInfo shipProfileInfo = shipprofilerepository.findById(vesselId);
		if (shipProfileInfo != null){
			ShipProfileDTO shipProfileDTO = shipProfileUtility.convertShipProfileINfoToShipProfileDTOsForDashboardCountBasedOnVessel(shipProfileInfo);
			return shipProfileDTO;
		}
		return null;
	}

}
