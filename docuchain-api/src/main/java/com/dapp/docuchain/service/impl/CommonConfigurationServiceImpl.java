package com.dapp.docuchain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.dapp.docuchain.dto.CountryDTO;
import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.DocumentHolderDTO;
import com.dapp.docuchain.dto.ExpiryCertificateTypeDTO;
import com.dapp.docuchain.dto.FaqDTO;
import com.dapp.docuchain.dto.PortDTO;
import com.dapp.docuchain.dto.RoleAliasDTO;
import com.dapp.docuchain.dto.UserReportAnIssueDTO;
import com.dapp.docuchain.dto.VesselsTypeDTO;
import com.dapp.docuchain.model.CountryInfo;
import com.dapp.docuchain.model.DataDeletedHistoryInfo;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.DocumentHolderType;
import com.dapp.docuchain.model.ExpiryCertificateTypeInfo;
import com.dapp.docuchain.model.FaqInfo;
import com.dapp.docuchain.model.OrganizationInfo;
import com.dapp.docuchain.model.PortInfo;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.RoleAliasInfo;
import com.dapp.docuchain.model.RoleInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.model.ShipTypesInfo;
import com.dapp.docuchain.model.User;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.model.UserReportAnIssueInfo;
import com.dapp.docuchain.repository.CountryInfoRepository;
import com.dapp.docuchain.repository.DocumentHolderRepository;
import com.dapp.docuchain.repository.ExpiryDocCertificateRepository;
import com.dapp.docuchain.repository.FaqRepository;
import com.dapp.docuchain.repository.OrganizationInfoRepository;
import com.dapp.docuchain.repository.PortInfoRepository;
import com.dapp.docuchain.repository.RoleAliasInfoRepository;
import com.dapp.docuchain.repository.RoleInfoRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.repository.ShipTypesRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.repository.UserReportAnIssueInfoRepository;
import com.dapp.docuchain.service.CommonConfigurationService;
import com.dapp.docuchain.service.EmailService;
import com.dapp.docuchain.service.UserService;
import com.dapp.docuchain.utility.CommonMethodsUtility;

@Service
public class CommonConfigurationServiceImpl implements CommonConfigurationService {

	@Autowired
	private Environment env;

	@Autowired
	private CountryInfoRepository countryInfoRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private RoleAliasInfoRepository roleAliasInfoRepository;

	@Autowired
	private RoleInfoRepository roleInfoRepository;

	@Autowired
	private ShipTypesRepository shipTypesRepository;

	@Autowired
	private PortInfoRepository portInfoRepository;

	@Autowired
	private DocumentHolderRepository documentHolderRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ExpiryDocCertificateRepository expiryDocCertificateRepository;

	@Autowired
	private UserReportAnIssueInfoRepository userReportAnIssueInfoRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private ShipProfileRepository shipProfileRepository;

	@Autowired
	private FaqRepository faqRepository;

	private final String ONLY_LETTERS = "[a-zA-Z\\s]+";

	private final String ONLY_NUMBERS = "[0-9]+";

	private final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
	public String addCountryInformationBySuperAdmin(CountryDTO countryDTO) {
		if (countryDTO.getAdminId() == null)
			return env.getProperty("country.admin.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(countryDTO.getAdminId())))
			return env.getProperty("country.admin.id.special.characters");

		if (countryDTO.getCountryName() == null && StringUtils.isEmpty(countryDTO.getCountryName())
				&& StringUtils.isBlank(countryDTO.getCountryName()))
			return env.getProperty("country.name.empty");

		if (countryDTO.getCountryCode() == null && StringUtils.isEmpty(countryDTO.getCountryCode())
				&& StringUtils.isBlank(countryDTO.getCountryCode()))
			return env.getProperty("country.code.empty");

		if (!countryDTO.getCountryName().matches(ONLY_LETTERS))
			return env.getProperty("country.name.special.characters");

		if (!countryDTO.getCountryCode().matches(ONLY_LETTERS))
			return env.getProperty("country.code.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(countryDTO.getAdminId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("country.admin.only.create");

		if(countryInfoRepository.findByCountryName(countryDTO.getCountryName()) != null)
			return env.getProperty("country.name.exist");

		if(countryInfoRepository.findByCountryCode(countryDTO.getCountryCode()) != null)
			return env.getProperty("country.code.exist");

		if (countryInfoRepository.findByCountryNameAndCountryCode(countryDTO.getCountryName(),
				countryDTO.getCountryCode()) != null)
			return env.getProperty("country.already.exists");
		CountryInfo countryInfo = countryInfoRepository.save(convertCountryDTOToCountryInfo(countryDTO));
		if (countryInfo != null) {
			commonMethodsUtility.maintainHistory(countryInfo.getId(), countryInfo.getCountryName(), "Country",
					env.getProperty("history.created"), countryDTO.getAdminId());

			return env.getProperty("success");
		} else {
			return null;
		}
	}

	@Override
	public List<CountryDTO> listCountryInformationForAllUser() {
		List<CountryInfo> countryInfos = countryInfoRepository.findAll();
		if (countryInfos != null && countryInfos.size() > 0) {
			return convertCountryInfosToCountryDTOs(countryInfos);
		}
		return null;
	}

	@Override
	public String updateCountryInformation(CountryDTO countryDTO) {

		if (countryDTO.getCountryId() == null)
			return env.getProperty("country.id.null");

		if (countryDTO.getAdminId() == null)
			return env.getProperty("country.admin.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(countryDTO.getAdminId())))
			return env.getProperty("country.admin.id.special.characters");

		if (countryDTO.getCountryName() == null && StringUtils.isEmpty(countryDTO.getCountryName())
				&& StringUtils.isBlank(countryDTO.getCountryName()))
			return env.getProperty("country.name.empty");

		if (countryDTO.getCountryCode() == null && StringUtils.isEmpty(countryDTO.getCountryCode())
				&& StringUtils.isBlank(countryDTO.getCountryCode()))
			return env.getProperty("country.code.empty");

		if (!countryDTO.getCountryName().matches(ONLY_LETTERS))
			return env.getProperty("country.name.special.characters");

		if (!countryDTO.getCountryCode().matches(ONLY_LETTERS))
			return env.getProperty("country.code.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(countryDTO.getAdminId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin))
			return env.getProperty("country.admin.only.update");

		CountryInfo countryInfo = countryInfoRepository.findOne(countryDTO.getCountryId());
		if (countryInfo == null)
			return env.getProperty("country.not.found");

		CountryInfo countryNameExist = countryInfoRepository.findByCountryName(countryDTO.getCountryName());
		if(countryNameExist != null){
			if (!countryInfo.equals(countryNameExist))
				return env.getProperty("country.name.exist");
		}

		CountryInfo countryCodeExist = countryInfoRepository.findByCountryCode(countryDTO.getCountryCode());
		if(countryCodeExist != null){
			if (!countryInfo.equals(countryCodeExist))
				return env.getProperty("country.code.exist");
		}

		CountryInfo isAlreadycountryInfoExists = countryInfoRepository
				.findByCountryNameAndCountryCode(countryDTO.getCountryName(), countryDTO.getCountryCode());
		if (isAlreadycountryInfoExists != null) {
			if (!countryInfo.equals(isAlreadycountryInfoExists))
				return env.getProperty("country.already.exists");
		}
		CountryInfo country = countryInfoRepository.saveAndFlush(convertCountryDTOToCountryInfo(countryDTO));
		if (country != null) {
			commonMethodsUtility.maintainHistory(country.getId(), country.getCountryName(), "Country",
					env.getProperty("history.updated"), countryDTO.getAdminId());

			return env.getProperty("success");
		}
		return null;
	}

	@Override
	public String deleteCountryInformation(Long countryId, Long userId) {
		if (countryId == null)
			return env.getProperty("country.id.null");

		if (userId == null)
			return env.getProperty("country.admin.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(userId)))
			return env.getProperty("country.admin.id.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		CountryInfo countryInfo = countryInfoRepository.findOne(countryId);
		if (countryInfo == null)
			return env.getProperty("country.not.found");

		if (countryInfo != null) {
			DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
			deletedHistoryDTO.setObjectId(countryInfo.getId());
			deletedHistoryDTO.setObjectOne(countryInfo.getCountryName());
			deletedHistoryDTO.setObjectTwo(countryInfo.getCountryCode());
			DataDeletedHistoryInfo dataDeletedHistoryInfo = commonMethodsUtility
					.maintainDeletedHistory(deletedHistoryDTO);
			countryInfoRepository.delete(countryInfo);
			if (dataDeletedHistoryInfo != null)
				commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(), countryInfo.getCountryName(),
						"Country", env.getProperty("history.deleted"), userId);

		}
		return env.getProperty("success");
	}

	@Override
	public PortDTO addCountryPortInformationOrganizationByAdmin(PortDTO portDTO) {
		PortInfo portInfo = null;
		if (portDTO != null) {
			portInfo = portInfoRepository.save(convertPortDTOToPortInfo(portDTO));
			if (portInfo != null) {
				commonMethodsUtility.maintainHistory(portInfo.getId(), portInfo.getPortName(), "Port",
						env.getProperty("history.created"), portDTO.getUserId());
			}
		}
		return convertPortInfoToPortDTO(portInfo);
	}

	/**
	 * This method is used Port information listing based on the user
	 * organization All users can able to list the port information based
	 * organization
	 *
	 * @author Prabakaran
	 *
	 */

	@Override
	public List<PortDTO> listPortInformationBasedOnCountryProfile(Long countryId) {
		if (countryId != null)
			return convertPortInfosToPortDTOs(
					portInfoRepository.findByCountryInfo(countryInfoRepository.getOne(countryId)));
		return null;
	}

	/**
	 * This method is used update the country Port information This method will
	 * invoke whenever need to update the port information Administrator only
	 * can able to update the port information
	 *
	 * @author Prabakaran
	 *
	 */
	@Override
	public String updateCountryPortInformationBasedOrganization(PortDTO portDTO) {
		if (portDTO.getCountryId() == null)
			return env.getProperty("country.id.null");
		if (portDTO.getUserId() == null)
			return env.getProperty("country.admin.id.null");
		if (portDTO.getPortId() == null)
			return env.getProperty("port.id.null");
		if (ONLY_NUMBERS.matches(Long.toString(portDTO.getUserId())))
			return env.getProperty("country.admin.id.special.characters");
		if (portDTO.getPortName() == null && StringUtils.isEmpty(portDTO.getPortName())
				&& StringUtils.isBlank(portDTO.getPortName()))
			return env.getProperty("port.name.empty");
		if (!portDTO.getPortName().matches(ONLY_LETTERS))
			return env.getProperty("port.name.special.characters");
		UserProfileInfo profileInfo = userProfileRepository.findOne(portDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");
		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
			return env.getProperty("port.super.admin.only.update");
		}
		CountryInfo countryInfo = countryInfoRepository.findOne(portDTO.getCountryId());
		if (countryInfo == null)
			return env.getProperty("country.not.found");
		PortInfo portInfo = portInfoRepository.findByIdAndCountryInfo(portDTO.getPortId(), countryInfo);
		if (portInfo == null)
			return env.getProperty("port.not.found");
		PortInfo isAlreadyExists = portInfoRepository.findByPortNameAndCountryInfo(portDTO.getPortName(), countryInfo);
		if (isAlreadyExists != null) {
			if (!portInfo.equals(isAlreadyExists))
				return env.getProperty("port.name.already.exists");
		}
		if (portInfo != null) {
			portInfo.setPortName(portDTO.getPortName());
			PortInfo portInf=portInfoRepository.saveAndFlush(portInfo);
			if (portInf != null)
				commonMethodsUtility.maintainHistory(portInf.getId(),portInf.getPortName(),"Port", env.getProperty("history.updated"), portDTO.getUserId());
				return env.getProperty("success");
		}
		return null;
	}

	@Override
	public String deleteCountryPortInformationBasedOrganization(PortDTO portDTO) {
		List<PortInfo> portInfos = new ArrayList<>();
		if (portDTO.getCountryId() == null)
			return env.getProperty("country.id.null");
		if (portDTO.getUserId() == null)
			return env.getProperty("user.id.null");
		if (portDTO.getPortIds() == null)
			return env.getProperty("port.id.null");
		if (portDTO.getPortIds().length == 0)
			return env.getProperty("port.id.null");
		if (ONLY_NUMBERS.matches(Long.toString(portDTO.getUserId())))
			return env.getProperty("user.id.special.character");
		UserProfileInfo profileInfo = userProfileRepository.findOne(portDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}
		CountryInfo countryInfo = countryInfoRepository.findOne(portDTO.getCountryId());
		if (countryInfo == null)
			return env.getProperty("country.not.found");
		for (Long portId : portDTO.getPortIds()) {
			PortInfo portInfo = portInfoRepository.findByIdAndCountryInfo(portId, countryInfo);
			if (portInfo == null) {
				return env.getProperty("port.not.found");
			} else {
				portInfos.add(portInfo);
				DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
	            deletedHistoryDTO.setObjectId(portInfo.getId());
	            deletedHistoryDTO.setObjectOne(portInfo.getPortName());
	            deletedHistoryDTO.setObjectTwo(portInfo.getCountryInfo().getCountryName());
	            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
	            if(dataDeletedHistoryInfo!=null)
	            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),portInfo.getPortName(),"Port", env.getProperty("history.deleted"), portDTO.getUserId());
			}
		}
		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
			return env.getProperty("port.super.admin.only.delete");
		}
		portInfoRepository.delete(portInfos);
		return env.getProperty("success");
	}

	@Override
	public String deleteAllCountryPortInformationBasedOrganization(PortDTO portDTO) {
		if (portDTO.getCountryId() == null)
			return env.getProperty("country.id.null");
		if (portDTO.getUserId() == null)
			return env.getProperty("user.id.null");
		if (ONLY_NUMBERS.matches(Long.toString(portDTO.getUserId())))
			return env.getProperty("user.id.special.character");
		UserProfileInfo profileInfo = userProfileRepository.findOne(portDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}
		CountryInfo countryInfo = countryInfoRepository.findOne(portDTO.getCountryId());
		if (countryInfo == null)
			return env.getProperty("country.not.found");
		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
			return env.getProperty("port.super.admin.only.delete");
		}
		List<PortInfo> portInfos = portInfoRepository.findByCountryInfo(countryInfo);
		if (portInfos != null && portInfos.size() > 0) {
			portInfoRepository.delete(portInfos);
			return env.getProperty("success");
		} else {
			return env.getProperty("port.list.failure");
		}
	}

	@Override
	public RoleAliasDTO addRoleAliasInformationBasedAdmin(RoleAliasDTO roleAliasDTO) {
		if (roleAliasDTO != null) {
			RoleAliasInfo roleAliasInfo = roleAliasInfoRepository
					.save(convertRoleAliasDTOToRoleAliasInfo(roleAliasDTO));
			if (roleAliasInfo != null) {
				commonMethodsUtility.maintainHistory(roleAliasInfo.getId(), roleAliasInfo.getRoleAliasName(), "Role",
						env.getProperty("history.created"), roleAliasDTO.getAdminId());
			}
			return convertRoleAliasInfoToRoleAliasDTO(roleAliasInfo);
		}
		return null;
	}

	@Override
	public List<RoleAliasDTO> listRoleAliasInformationBasedOrganization(OrganizationInfo organizationInfo) {
		if (organizationInfo != null) {
			List<RoleAliasDTO> roleAliasDTOs = new ArrayList<>();
			List<RoleInfo> roleInfos = roleInfoRepository.findAll();
			List<RoleAliasInfo> roleAliasInfos = roleAliasInfoRepository.findByOrganizationInfo(organizationInfo);
			if (roleAliasInfos != null && roleAliasInfos.size() > 0) {
				for (RoleInfo roleInfo : roleInfos) {
					boolean isRoleExists = false;
					RoleAliasDTO roleAliasDTO = new RoleAliasDTO();
					for (RoleAliasInfo roleAliasInfo : roleAliasInfos) {
						if (roleInfo.equals(roleAliasInfo.getRoleId())) {
							isRoleExists = true;
						}
					}
					if (isRoleExists) {
						RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(roleInfo,
								organizationInfo);
						roleAliasDTO.setRoleAliasId(roleAliasInfo.getId());
						roleAliasDTO.setRoleId(roleAliasInfo.getRoleId().getId());
						roleAliasDTO.setRoleName(roleInfo.getRoleName().name().toString());
						roleAliasDTO.setRoleAliasName(roleAliasInfo.getRoleAliasName());
						roleAliasDTOs.add(roleAliasDTO);
					}
					if (!isRoleExists) {
						RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(roleInfo,
								organizationInfo);
						if(roleAliasInfo != null){
							if (!roleInfo.getRoleName().equals(Role.Admin)
									&& !roleInfo.getRoleName().equals(Role.SuperAdmin)) {
								roleAliasDTO.setRoleId(roleInfo.getId());
								roleAliasDTO.setRoleName(roleInfo.getRoleName().name().toString());
								roleAliasDTO.setRoleAliasName(roleAliasInfo.getRoleAliasName());
								roleAliasDTOs.add(roleAliasDTO);
							}
						}else {
							if (!roleInfo.getRoleName().equals(Role.Admin)
									&& !roleInfo.getRoleName().equals(Role.SuperAdmin)) {
								roleAliasDTO.setRoleId(roleInfo.getId());
								roleAliasDTO.setRoleName(roleInfo.getRoleName().name().toString());
								roleAliasDTO.setRoleAliasName(roleInfo.getRoleName().name().toString());
								roleAliasDTOs.add(roleAliasDTO);
							}
						}

					}
				}

				return roleAliasDTOs;
			} else {
				if (roleInfos != null && roleInfos.size() > 0) {
					for (RoleInfo roleInfo : roleInfos) {
						RoleAliasDTO roleAliasDTO = new RoleAliasDTO();
						if (!roleInfo.getRoleName().equals(Role.Admin)
								&& !roleInfo.getRoleName().equals(Role.SuperAdmin)) {
							roleAliasDTO.setRoleId(roleInfo.getId());
							roleAliasDTO.setRoleName(roleInfo.getRoleName().name().toString());
							roleAliasDTO.setRoleAliasName(roleInfo.getRoleName().name());
							roleAliasDTOs.add(roleAliasDTO);
						}
					}
					return roleAliasDTOs;
				}
			}
		}
		return null;
	}

	@Override
	public VesselsTypeDTO addVesselsTypeInformationBasedOrganizationByAdmin(VesselsTypeDTO vesselsTypeDTO) {
		if (vesselsTypeDTO != null){
			ShipTypesInfo shipTypesInfo=shipTypesRepository.save(convertVesselsTypeDTOToShipTypesInfo(vesselsTypeDTO));
			if(shipTypesInfo!=null){
                commonMethodsUtility.maintainHistory(shipTypesInfo.getId(),shipTypesInfo.getShipTypesName(),"Vessel", env.getProperty("history.created"), vesselsTypeDTO.getUserId());
                }
			return convertShipTypesInfoToVesselsTypeDTO(shipTypesInfo);
		}
		return null;
	}

	@Override
	public List<VesselsTypeDTO> listVesselsTypeInformationBasedOrganization(OrganizationInfo organizationInfo) {
		if (organizationInfo != null) {
			return convertShipTypesInfosToVesselsTypeDTOs(shipTypesRepository.findByOrganizationInfo(organizationInfo));
		}
		return null;
	}

	@SuppressWarnings("unused")
	private CountryDTO convertCountryInfoToCountryDTO(CountryInfo countryInfo) {
		if (countryInfo != null) {
			CountryDTO countryDTO = new CountryDTO();
			countryDTO.setCountryId(countryInfo.getId());
			countryDTO.setCountryName(countryInfo.getCountryName());
			countryDTO.setCountryCode(countryInfo.getCountryCode());
			countryDTO.setIsdCode(countryInfo.getIsdCode());
			return countryDTO;
		}
		return null;
	}

	private CountryInfo convertCountryDTOToCountryInfo(CountryDTO countryDTO) {
		if (countryDTO != null) {
			CountryInfo countryInfo = new CountryInfo();
			if (countryDTO.getCountryId() != null)
				countryInfo.setId(countryDTO.getCountryId());
			countryInfo.setCountryName(countryDTO.getCountryName());
			countryInfo.setCountryCode(countryDTO.getCountryCode());
			if (countryDTO.getIsdCode() != null)
				countryInfo.setIsdCode(countryDTO.getIsdCode());
			return countryInfo;
		}
		return null;
	}

	private List<CountryDTO> convertCountryInfosToCountryDTOs(List<CountryInfo> countryInfos) {
		List<CountryDTO> countryDTOs = new ArrayList<>();
		if (countryInfos != null && countryInfos.size() > 0) {
			for (CountryInfo countryInfo : countryInfos) {
				CountryDTO countryDTO = new CountryDTO();
				countryDTO.setCountryId(countryInfo.getId());
				countryDTO.setCountryName(countryInfo.getCountryName());
				countryDTO.setCountryCode(countryInfo.getCountryCode());
				countryDTO.setIsdCode(countryInfo.getIsdCode());
				countryDTOs.add(countryDTO);
			}
			return countryDTOs;
		}
		return null;
	}

	@Override
	public String addRoleAliasInformationBasedOrganization(RoleAliasDTO roleAliasDTO) {
		if (roleAliasDTO.getAdminId() == null)
			return env.getProperty("role.user.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(roleAliasDTO.getAdminId())))
			return env.getProperty("role.alias.admin.id.special.characters");

		if (roleAliasDTO.getRoleId() == null)
			return env.getProperty("role.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(roleAliasDTO.getRoleId())))
			return env.getProperty("role.id.special.characters");

		if (roleAliasDTO.getRoleAliasName() == null && StringUtils.isEmpty(roleAliasDTO.getRoleAliasName())
				&& StringUtils.isBlank(roleAliasDTO.getRoleAliasName()))
			return env.getProperty("country.name.empty");

		if (!roleAliasDTO.getRoleAliasName().matches(ONLY_LETTERS))
			return env.getProperty("role.alias.name.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(roleAliasDTO.getAdminId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin))
			return env.getProperty("role.alias.admin.only.create");

		if (roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(
				roleInfoRepository.findById(roleAliasDTO.getRoleId()), profileInfo.getOrganizationInfo()) != null)
			return env.getProperty("role.alias.role.exists");

		RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleAliasNameAndOrganizationInfo(
				roleAliasDTO.getRoleAliasName(), profileInfo.getOrganizationInfo());
		if (roleAliasInfo != null)
			return env.getProperty("role.alias.name.exists");

		return env.getProperty("success");
	}

	private RoleAliasInfo convertRoleAliasDTOToRoleAliasInfo(RoleAliasDTO roleAliasDTO) {
		if (roleAliasDTO != null) {
			RoleAliasInfo roleAliasInfo = new RoleAliasInfo();
			roleAliasInfo.setRoleAliasName(roleAliasDTO.getRoleAliasName());
			if (roleAliasDTO.getRoleId() != null)
				roleAliasInfo.setRoleId(roleInfoRepository.findOne(roleAliasDTO.getRoleId()));
			if (roleAliasDTO.getAdminId() != null)
				roleAliasInfo.setOrganizationInfo(
						userProfileRepository.findOne(roleAliasDTO.getAdminId()).getOrganizationInfo());
			return roleAliasInfo;
		}
		return null;
	}

	private RoleAliasDTO convertRoleAliasInfoToRoleAliasDTO(RoleAliasInfo roleAliasInfo) {
		if (roleAliasInfo != null) {
			RoleAliasDTO roleAliasDTO = new RoleAliasDTO();
			roleAliasDTO.setRoleAliasId(roleAliasInfo.getId());
			roleAliasDTO.setRoleAliasName(roleAliasInfo.getRoleAliasName());
			roleAliasDTO.setRoleId(roleAliasInfo.getRoleId().getId());
			return roleAliasDTO;
		}
		return null;
	}

	@Override
	public String addVesselsTypeInformationValidation(VesselsTypeDTO vesselsTypeDTO) {
		if (vesselsTypeDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(vesselsTypeDTO.getUserId())))
			return env.getProperty("user.id.special.characters");

		if (vesselsTypeDTO.getVesselsTypeName() == null && StringUtils.isEmpty(vesselsTypeDTO.getVesselsTypeName())
				&& StringUtils.isBlank(vesselsTypeDTO.getVesselsTypeName()))
			return env.getProperty("vessels.type.name.empty");

		if (!vesselsTypeDTO.getVesselsTypeName().matches(ONLY_LETTERS))
			return env.getProperty("vessels.type.name.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(vesselsTypeDTO.getUserId());
		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin))
			return env.getProperty("vessels.type.admin.only.create");

		ShipTypesInfo shipTypesInfo = shipTypesRepository.findByShipTypesNameAndOrganizationInfo(
				vesselsTypeDTO.getVesselsTypeName(), profileInfo.getOrganizationInfo());
		if (shipTypesInfo != null)
			return env.getProperty("vessels.type.name.exists");

		return env.getProperty("success");
	}

	private ShipTypesInfo convertVesselsTypeDTOToShipTypesInfo(VesselsTypeDTO vesselsTypeDTO) {
		if (vesselsTypeDTO != null) {
			ShipTypesInfo shipTypesInfo = new ShipTypesInfo();
			shipTypesInfo.setShipTypesName(vesselsTypeDTO.getVesselsTypeName());
			if (vesselsTypeDTO.getUserId() != null)
				shipTypesInfo.setOrganizationInfo(
						userProfileRepository.findOne(vesselsTypeDTO.getUserId()).getOrganizationInfo());
			return shipTypesInfo;
		}
		return null;
	}

	private VesselsTypeDTO convertShipTypesInfoToVesselsTypeDTO(ShipTypesInfo shipTypesInfo) {
		if (shipTypesInfo != null) {
			VesselsTypeDTO vesselsTypeDTO = new VesselsTypeDTO();
			vesselsTypeDTO.setVesselsTypeId(shipTypesInfo.getId());
			vesselsTypeDTO.setVesselsTypeName(shipTypesInfo.getShipTypesName());
			return vesselsTypeDTO;
		}
		return null;
	}

	private List<VesselsTypeDTO> convertShipTypesInfosToVesselsTypeDTOs(List<ShipTypesInfo> shipTypesInfos) {
		List<VesselsTypeDTO> vesselsTypeDTOs = new ArrayList<>();
		if (shipTypesInfos != null && shipTypesInfos.size() > 0) {
			for (ShipTypesInfo shipTypesInfo : shipTypesInfos) {
				VesselsTypeDTO vesselsTypeDTO = new VesselsTypeDTO();
				vesselsTypeDTO.setVesselsTypeId(shipTypesInfo.getId());
				vesselsTypeDTO.setVesselsTypeName(shipTypesInfo.getShipTypesName());
				vesselsTypeDTOs.add(vesselsTypeDTO);
			}
			return vesselsTypeDTOs;
		}
		return null;
	}

	@Override
	public String addCountryPortInformationValidation(PortDTO portDTO) {
		if (portDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(portDTO.getUserId())))
			return env.getProperty("user.id.special.characters");

		if (portDTO.getCountryId() == null)
			return env.getProperty("country.id.empty");

		if (ONLY_NUMBERS.matches(Long.toString(portDTO.getCountryId())))
			return env.getProperty("country.id.special.characters");

		if (portDTO.getPortName() == null && StringUtils.isEmpty(portDTO.getPortName())
				&& StringUtils.isBlank(portDTO.getPortName()))
			return env.getProperty("port.name.empty");

		if (!portDTO.getPortName().matches(ONLY_LETTERS))
			return env.getProperty("port.name.special.characters");

		return env.getProperty("success");
	}

	private PortInfo convertPortDTOToPortInfo(PortDTO portDTO) {
		if (portDTO != null) {
			PortInfo portInfo = new PortInfo();
			portInfo.setPortName(portDTO.getPortName());
			portInfo.setCountryInfo(countryInfoRepository.findOne(portDTO.getCountryId()));
			return portInfo;
		}
		return null;
	}

	private PortDTO convertPortInfoToPortDTO(PortInfo portInfo) {
		if (portInfo != null) {
			PortDTO portDTO = new PortDTO();
			portDTO.setPortId(portInfo.getId());
			portDTO.setPortName(portInfo.getPortName());
			portDTO.setCountryId(portInfo.getCountryInfo().getId());
			return portDTO;
		}
		return null;
	}

	@Override
	public String isCountryAndPortNameAndPortAlreadyExists(PortDTO portDTO) {
		CountryInfo countryInfo = countryInfoRepository.findOne(portDTO.getCountryId());
		if (countryInfo == null)
			return env.getProperty("country.not.found");

		if (portInfoRepository.findByPortNameAndCountryInfo(portDTO.getPortName(), countryInfo) != null)
			return env.getProperty("port.name.already.exists");

		return env.getProperty("success");
	}

	private List<PortDTO> convertPortInfosToPortDTOs(List<PortInfo> portInfos) {
		List<PortDTO> portDTOs = new ArrayList<>();
		if (portInfos != null && portInfos.size() > 0) {
			for (PortInfo portInfo : portInfos) {
				PortDTO portDTO = new PortDTO();
				portDTO.setPortId(portInfo.getId());
				portDTO.setPortName(portInfo.getPortName());
				portDTO.setCountryId(portInfo.getCountryInfo().getId());
				portDTOs.add(portDTO);
			}
			return portDTOs;
		}
		return null;
	}

	@Override
	public String deleteMultipleAndSingleCountryInformation(CountryDTO countryDTO) {
		List<CountryInfo> countryInfos = new ArrayList<>();

		if (countryDTO == null)
			return env.getProperty("country.parameter.null");

		if (countryDTO.getCountryIds() == null)
			return env.getProperty("country.id.null");

		if (countryDTO.getCountryIds().length == 0)
			return env.getProperty("country.id.null");

		if (countryDTO.getAdminId() == null)
			return env.getProperty("country.admin.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(countryDTO.getAdminId())))
			return env.getProperty("country.admin.id.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(countryDTO.getAdminId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}
		for (Long countryId : countryDTO.getCountryIds()) {
			CountryInfo countryInfo = countryInfoRepository.findOne(countryId);
			if (countryInfo == null) {
				return env.getProperty("country.not.found");
			} else {
				countryInfos.add(countryInfo);
				DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
				deletedHistoryDTO.setObjectId(countryInfo.getId());
				deletedHistoryDTO.setObjectOne(countryInfo.getCountryName());
				deletedHistoryDTO.setObjectTwo(countryInfo.getCountryCode());
				DataDeletedHistoryInfo dataDeletedHistoryInfo = commonMethodsUtility
						.maintainDeletedHistory(deletedHistoryDTO);
				if (dataDeletedHistoryInfo != null)
					commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(), countryInfo.getCountryName(),
							"Country", env.getProperty("history.deleted"), countryDTO.getAdminId());

			}
		}
		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
			return env.getProperty("country.admin.only.delete");
		}

		countryInfoRepository.delete(countryInfos);
		return env.getProperty("success");
	}

	@Override
	public String deleteAllCountryInformationBasedOrganization(Long userId) {
		if (userId == null)
			return env.getProperty("country.user.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
		if (profileInfo == null)
			return env.getProperty("country.user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
			return env.getProperty("country.admin.only.delete");
		}

		List<CountryInfo> countryInfos = countryInfoRepository.findAll();
		if (countryInfos != null && countryInfos.size() > 0) {
			countryInfoRepository.delete(countryInfos);
			return env.getProperty("success");
		} else {
			return env.getProperty("country.list.failure");
		}
	}

	@Override
	public String updateRoleAliasInformationBasedOrganization(RoleAliasDTO roleAliasDTO) {
//		if (roleAliasDTO.getRoleAliasId() == null)
//			return env.getProperty("role.alias.id.null");

		if (roleAliasDTO.getAdminId() == null)
			return env.getProperty("user.id.null");

		if (roleAliasDTO.getRoleId() == null)
			return env.getProperty("role.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(roleAliasDTO.getRoleAliasId())))
			return env.getProperty("role.alias.id.special.characters");

		if (ONLY_NUMBERS.matches(Long.toString(roleAliasDTO.getAdminId())))
			return env.getProperty("user.id.special.characters");

		if (ONLY_NUMBERS.matches(Long.toString(roleAliasDTO.getRoleId())))
			return env.getProperty("role.id.special.characters");

		if (roleAliasDTO.getRoleAliasName() == null && StringUtils.isEmpty(roleAliasDTO.getRoleAliasName())
				&& StringUtils.isBlank(roleAliasDTO.getRoleAliasName()))
			return env.getProperty("role.alias.name.empty");

		if (!roleAliasDTO.getRoleAliasName().matches(ONLY_LETTERS))
			return env.getProperty("role.alias.name.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(roleAliasDTO.getAdminId());

		if (profileInfo == null)
			return env.getProperty("user.not.found");

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("role.alias.admin.only.update");
		}
		RoleInfo roleInfo = roleInfoRepository.findOne(roleAliasDTO.getRoleId());
		if (roleInfo == null)
			return env.getProperty("role.alias.role.not.found");

		RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByRoleIdAndOrganizationInfo(roleInfo,
				profileInfo.getOrganizationInfo());
//		RoleAliasInfo isAlreadyExist = roleAliasInfoRepository.findByRoleAliasNameAndOrganizationInfo(
//				roleAliasDTO.getRoleAliasName(), profileInfo.getOrganizationInfo());
//		if (isAlreadyExist != null) {
//			if (!roleAliasInfo.equals(isAlreadyExist))
//				return env.getProperty("role.alias.name.exists");
//		}

		if (roleAliasInfo == null) {
			roleAliasInfo = new RoleAliasInfo();
			roleAliasInfo.setOrganizationInfo(profileInfo.getOrganizationInfo());
			roleAliasInfo.setRoleAliasName(roleAliasDTO.getRoleAliasName());
			roleAliasInfo.setRoleId(roleInfo);
			roleAliasInfo = roleAliasInfoRepository.saveAndFlush(roleAliasInfo);
			return env.getProperty("success");
		} else {
			roleAliasInfo.setOrganizationInfo(profileInfo.getOrganizationInfo());
			roleAliasInfo.setRoleAliasName(roleAliasDTO.getRoleAliasName());
			roleAliasInfo.setRoleId(roleInfo);
			roleAliasInfo = roleAliasInfoRepository.saveAndFlush(roleAliasInfo);
		}

		/*roleAliasInfo.setOrganizationInfo(profileInfo.getOrganizationInfo());
		roleAliasInfo.setRoleAliasName(roleAliasDTO.getRoleAliasName());
		roleAliasInfo.setRoleId(roleInfo);
		RoleAliasInfo roleAlias = roleAliasInfoRepository.saveAndFlush(roleAliasInfo);*/
		if (roleAliasInfo != null) {
			commonMethodsUtility.maintainHistory(roleAliasInfo.getId(), roleAliasInfo.getRoleAliasName(), "Role",
					env.getProperty("history.updated"), roleAliasDTO.getAdminId());
			return env.getProperty("success");

		}
		return null;
	}

	@Override
	public String deleteRoleAliasInformationBasedOrganization(RoleAliasDTO roleAliasDTO) {
		List<RoleAliasInfo> roleAliasInfos = new ArrayList<>();
		if (roleAliasDTO.getAdminId() == null)
			return env.getProperty("user.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(roleAliasDTO.getAdminId())))
			return env.getProperty("user.id.special.characters");

		if (roleAliasDTO.getRoleAliasIds() == null)
			return env.getProperty("role.alias.id.null");

		if (roleAliasDTO.getRoleAliasIds().length == 0)
			return env.getProperty("role.alias.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(roleAliasDTO.getAdminId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		for (Long roleAliasId : roleAliasDTO.getRoleAliasIds()) {
			RoleAliasInfo roleAliasInfo = roleAliasInfoRepository.findByIdAndOrganizationInfo(roleAliasId,
					profileInfo.getOrganizationInfo());
			if (roleAliasInfo == null) {
				return env.getProperty("role.alias.not.found");
			} else {
				roleAliasInfos.add(roleAliasInfo);
				DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
	            deletedHistoryDTO.setObjectId(roleAliasInfo.getId());
	            deletedHistoryDTO.setObjectOne(roleAliasInfo.getRoleAliasName());
	            deletedHistoryDTO.setObjectTwo(roleAliasInfo.getOrganizationInfo().getOrganizationName());
	            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
	            if(dataDeletedHistoryInfo!=null)
	            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),roleAliasInfo.getRoleAliasName(),"Role", env.getProperty("history.deleted"), roleAliasDTO.getAdminId());
			}
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("role.alias.admin.only.delete");
		}

		roleAliasInfoRepository.delete(roleAliasInfos);
		return env.getProperty("success");
	}

	@Override
	public String deleteAllRoleAliasInformationBasedOrganization(Long userId) {
		if (userId == null)
			return env.getProperty("country.admin.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(userId)))
			return env.getProperty("user.id.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("role.alias.admin.only.delete");
		}

		List<RoleAliasInfo> roleAliasInfos = roleAliasInfoRepository
				.findByOrganizationInfo(profileInfo.getOrganizationInfo());
		if (roleAliasInfos != null && roleAliasInfos.size() > 0) {
			roleAliasInfoRepository.delete(roleAliasInfos);
			return env.getProperty("success");
		} else {
			return env.getProperty("role.alias.list.failure");
		}
	}

	@Override
	public String updateVesselsTypeInformationBasedOrganization(VesselsTypeDTO vesselsTypeDTO) {
		if (vesselsTypeDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (vesselsTypeDTO.getVesselsTypeId() == null)
			return env.getProperty("vessels.type.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(vesselsTypeDTO.getUserId())))
			return env.getProperty("user.id.special.characters");

		if (ONLY_NUMBERS.matches(Long.toString(vesselsTypeDTO.getVesselsTypeId())))
			return env.getProperty("vessels.type.id.special.characters");

		if (vesselsTypeDTO.getVesselsTypeName() == null && StringUtils.isEmpty(vesselsTypeDTO.getVesselsTypeName())
				&& StringUtils.isBlank(vesselsTypeDTO.getVesselsTypeName()))
			return env.getProperty("vessels.type.name.empty");

		if (!vesselsTypeDTO.getVesselsTypeName().matches(ONLY_LETTERS))
			return env.getProperty("vessels.type.name.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(vesselsTypeDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("vessels.type.admin.only.update");
		}

		ShipTypesInfo shipTypesInfo = shipTypesRepository.findByIdAndOrganizationInfo(vesselsTypeDTO.getVesselsTypeId(),
				profileInfo.getOrganizationInfo());
		if (shipTypesInfo == null)
			return env.getProperty("vessels.type.not.found");

		ShipTypesInfo typesInfoExists = shipTypesRepository.findByShipTypesNameAndOrganizationInfo(
				vesselsTypeDTO.getVesselsTypeName(), profileInfo.getOrganizationInfo());
		if (typesInfoExists != null) {
			if (!shipTypesInfo.equals(typesInfoExists))
				env.getProperty("vessels.type.name.exists");
		}

		shipTypesInfo.setShipTypesName(vesselsTypeDTO.getVesselsTypeName());
		ShipTypesInfo shipTypesInf=shipTypesRepository.saveAndFlush(shipTypesInfo);
		if (shipTypesInf != null){
			commonMethodsUtility.maintainHistory(shipTypesInf.getId(),shipTypesInf.getShipTypesName(),"Vessel", env.getProperty("history.updated"), vesselsTypeDTO.getUserId());
			return env.getProperty("success");
		}

		return null;
	}

	@Override
	public String deleteVesselsTypeInformationBasedOrganization(VesselsTypeDTO vesselsTypeDTO) {
		List<ShipTypesInfo> shipTypesInfos = new ArrayList<>();
		if (vesselsTypeDTO.getUserId() == null)
			return env.getProperty("user.id.null");

		if (vesselsTypeDTO.getVesselsTypeIds() == null)
			return env.getProperty("vessels.type.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(vesselsTypeDTO.getUserId())))
			return env.getProperty("user.id.special.characters");

		if (vesselsTypeDTO.getVesselsTypeIds().length == 0)
			return env.getProperty("vessels.type.id.null");

		UserProfileInfo profileInfo = userProfileRepository.findOne(vesselsTypeDTO.getUserId());
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("vessels.type.admin.only.delete");
		}

		for (Long vesselsTypeId : vesselsTypeDTO.getVesselsTypeIds()) {
			ShipTypesInfo shipTypesInfo = shipTypesRepository.findByIdAndOrganizationInfo(vesselsTypeId,
					profileInfo.getOrganizationInfo());
			if (shipTypesInfo == null) {
				return env.getProperty("vessels.type.not.found");
			} else {
				shipTypesInfos.add(shipTypesInfo);
				DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
	            deletedHistoryDTO.setObjectId(shipTypesInfo.getId());
	            deletedHistoryDTO.setObjectOne(shipTypesInfo.getShipTypesName());
	            deletedHistoryDTO.setObjectTwo(shipTypesInfo.getOrganizationInfo().getOrganizationName());
	            DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
	            if(dataDeletedHistoryInfo!=null)
	            commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),shipTypesInfo.getShipTypesName(),"Vessel", env.getProperty("history.deleted"), vesselsTypeDTO.getUserId());
			}
		}

		if (shipTypesInfos != null && shipTypesInfos.size() > 0) {
			shipTypesRepository.delete(shipTypesInfos);
			return env.getProperty("success");
		}

		return null;
	}

	@Override
	public String deleteAllVesselsTypeInformationBasedOrganization(Long userId) {
		if (userId == null)
			return env.getProperty("user.id.null");

		if (ONLY_NUMBERS.matches(Long.toString(userId)))
			return env.getProperty("user.id.special.characters");

		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
		if (profileInfo == null) {
			return env.getProperty("user.not.found");
		}

		if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
			return env.getProperty("vessels.type.admin.only.delete");
		}

		List<ShipTypesInfo> shipTypesInfos = shipTypesRepository
				.findByOrganizationInfo(profileInfo.getOrganizationInfo());
		if (shipTypesInfos != null && shipTypesInfos.size() > 0) {
			shipTypesRepository.delete(shipTypesInfos);
			return env.getProperty("success");
		} else {
			return env.getProperty("vessels.type.list.failure");
		}
	}

	// @Override
	// public String addDocumentHolderInformationBasedSuperAdmin(DocumentHolderDTO documentHolderDTO) {
	// 	OrganizationInfo organizationInfo = null ;
	// 	if (documentHolderDTO.getUserId() == null)
	// 		return env.getProperty("user.id.null");

	// 	if (documentHolderDTO.getDocumentHolderName() == null
	// 			&& StringUtils.isEmpty(documentHolderDTO.getDocumentHolderName())
	// 			&& StringUtils.isNotBlank(documentHolderDTO.getDocumentHolderName()))
	// 		return env.getProperty("document.holder.file.name.empty");

	// 	UserProfileInfo profileInfo = userProfileRepository.findOne(documentHolderDTO.getUserId());
	// 	if (profileInfo == null) {
	// 		return env.getProperty("user.not.found");
	// 	}
	// 	if (documentHolderRepository.findByDocumentHolderName(documentHolderDTO.getDocumentHolderName()) != null)
	// 		return env.getProperty("document.holder.name.already.exist");
	// 	if (documentHolderDTO.getOrganizationName() != null){
	// 		 organizationInfo = organizationInfoRepository.findByOrganizationName(documentHolderDTO.getOrganizationName());
	// 	}
	// 	DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
	// 	 if (documentHolderDTO.getPlaceholderBy() != null && documentHolderDTO.getPlaceholderBy().equalsIgnoreCase("InVesselPart")) {
	// 		 for (Long id : documentHolderDTO.getVesselIds()) {
	// 				 documentHolderInfo = documentHolderRepository.findOne(id);
	// 				 DocumentHolderInfo documentHolderInf = new DocumentHolderInfo();
	// 				 documentHolderInf.setOrganizationId(documentHolderInfo.getOrganizationId());
	// 				 documentHolderInf.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
	// 				 documentHolderInf.setDocumentHolderDescription(documentHolderInfo.getDocumentHolderDescription());
	// 				 documentHolderInf.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
	// 				 documentHolderInf.setType(documentHolderInfo.getType());
	// 				 documentHolderInf.setUpdatedDate(new Date());
	// 				 documentHolderInf.setVesselId(id);
	// 				 documentHolderInf = documentHolderRepository.saveAndFlush(documentHolderInf);

	// 		 }
	// 	 }else {
	// 	if (organizationInfo != null)
	// 	documentHolderInfo.setOrganizationId(organizationInfo.getId());
	// 	documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
	// 	documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
	// 	documentHolderInfo.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
	// 	documentHolderInfo.setType(documentHolderDTO.getType());
	// 	documentHolderInfo.setUpdatedDate(new Date());
	// 	 documentHolderInfo=documentHolderRepository.save(documentHolderInfo);

	// 	if (documentHolderInfo != null && documentHolderDTO.getVesselIds() != null) {
	// 		for (Long id : documentHolderDTO.getVesselIds()) {
	// 			DocumentHolderInfo documentHolderInf = new DocumentHolderInfo();
	// 			documentHolderInf.setOrganizationId(organizationInfo.getId());
	// 			documentHolderInf.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
	// 			documentHolderInf.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
	// 			documentHolderInf.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
	// 			documentHolderInf.setType(documentHolderDTO.getType());
	// 			documentHolderInf.setUpdatedDate(new Date());
	// 			documentHolderInf.setVesselId(id);
	// 			documentHolderInf.setParantId(documentHolderInfo.getId());
	// 			documentHolderInf = documentHolderRepository.saveAndFlush(documentHolderInf);
	// 		}
	// 	}
	// 	 }
	// 	if (documentHolderInfo != null){
	// 		commonMethodsUtility.maintainHistory(documentHolderInfo.getId(),documentHolderInfo.getDocumentHolderName(),"DocumentHolder", env.getProperty("history.created"), documentHolderDTO.getUserId());
	// 		return env.getProperty("success");
	// 	}

	// 	return null;
	// }
	// CommonConfigurationServiceImpl.java (Snippet)

@Override
public String addDocumentHolderInformationBasedSuperAdmin(DocumentHolderDTO documentHolderDTO) {
    OrganizationInfo organizationInfo = null ;
    if (documentHolderDTO.getUserId() == null)
        return env.getProperty("user.id.null");

    // 🔥 BONUS FIX: Use OR (||) instead of AND (&&) so the validation actually works
    if (documentHolderDTO.getDocumentHolderName() == null || documentHolderDTO.getDocumentHolderName().trim().isEmpty()) {
        return env.getProperty("document.holder.file.name.empty");
    }

    UserProfileInfo profileInfo = userProfileRepository.findOne(documentHolderDTO.getUserId());
    if (profileInfo == null) {
        return env.getProperty("user.not.found");
    }

    // 🔥 MAIN FIX: Check if the list is empty rather than checking for a single null object
    List<DocumentHolderInfo> existingDocs = documentHolderRepository.findByDocumentHolderName(documentHolderDTO.getDocumentHolderName());
    if (existingDocs != null && !existingDocs.isEmpty()) {
        return env.getProperty("document.holder.name.already.exist");
    }

    if (documentHolderDTO.getOrganizationName() != null){
        organizationInfo = organizationInfoRepository.findByOrganizationName(documentHolderDTO.getOrganizationName());
    }

    DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();

    if (documentHolderDTO.getPlaceholderBy() != null && documentHolderDTO.getPlaceholderBy().equalsIgnoreCase("InVesselPart")) {
        for (Long id : documentHolderDTO.getVesselIds()) {
            documentHolderInfo = documentHolderRepository.findOne(id);
            DocumentHolderInfo documentHolderInf = new DocumentHolderInfo();
            documentHolderInf.setOrganizationId(documentHolderInfo.getOrganizationId());
            documentHolderInf.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
            documentHolderInf.setDocumentHolderDescription(documentHolderInfo.getDocumentHolderDescription());
            documentHolderInf.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
            documentHolderInf.setType(documentHolderInfo.getType());
            documentHolderInf.setUpdatedDate(new Date());
            documentHolderInf.setVesselId(id);
            documentHolderInf = documentHolderRepository.saveAndFlush(documentHolderInf);
        }
    } else {
        if (organizationInfo != null)
            documentHolderInfo.setOrganizationId(organizationInfo.getId());

        documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
        documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
        documentHolderInfo.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
        documentHolderInfo.setType(documentHolderDTO.getType());
        documentHolderInfo.setUpdatedDate(new Date());
        documentHolderInfo = documentHolderRepository.save(documentHolderInfo);

        if (documentHolderInfo != null && documentHolderDTO.getVesselIds() != null) {
            for (Long id : documentHolderDTO.getVesselIds()) {
                DocumentHolderInfo documentHolderInf = new DocumentHolderInfo();
                documentHolderInf.setOrganizationId(organizationInfo.getId());

                // Note: This is where duplicates are being created in your database!
                documentHolderInf.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
                documentHolderInf.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
                documentHolderInf.setDocumentHolderType(DocumentHolderType.EXPIRY_TYPE.name());
                documentHolderInf.setType(documentHolderDTO.getType());
                documentHolderInf.setUpdatedDate(new Date());
                documentHolderInf.setVesselId(id);
                documentHolderInf.setParantId(documentHolderInfo.getId());
                documentHolderInf = documentHolderRepository.saveAndFlush(documentHolderInf);
            }
        }
    }

    if (documentHolderInfo != null){
        commonMethodsUtility.maintainHistory(documentHolderInfo.getId(), documentHolderInfo.getDocumentHolderName(), "DocumentHolder", env.getProperty("history.created"), documentHolderDTO.getUserId());
        return env.getProperty("success"); // Make sure this matches your expected "Success" mapping for the frontend
    }

    return null;
}

	@Override
	public List<PortDTO> findAllPortInformationWithCountryDetails() {
		return convertPortInfosToPortDTOsWithCountry(portInfoRepository.findAll());
	}

	private List<PortDTO> convertPortInfosToPortDTOsWithCountry(List<PortInfo> portInfos) {
		List<PortDTO> portDTOs = new ArrayList<>();
		if (portInfos != null && portInfos.size() > 0) {
			for (PortInfo portInfo : portInfos) {
				PortDTO portDTO = new PortDTO();
				portDTO.setPortId(portInfo.getId());
				portDTO.setPortName(portInfo.getPortName());
				portDTO.setCountryId(portInfo.getCountryInfo().getId());
				portDTO.setCountryName(portInfo.getCountryInfo().getCountryName());
				portDTOs.add(portDTO);
			}
			return portDTOs;
		}
		return null;
	}

	@Override
	public List<DocumentHolderDTO> listDocumentHolderInformation() {
		return convertDocumentHolderInfosToDocumentHolderDTOs(documentHolderRepository.findByTypeOrTypeIsNullAndVesselIdIsNull("Standard"));
	}

	// private List<DocumentHolderDTO> convertDocumentHolderInfosToDocumentHolderDTOs(
	// 		List<DocumentHolderInfo> documentHolderInfos) {
	// 	OrganizationInfo organizationInfo = null;
	// 	List<DocumentHolderDTO> documentHolderDTOs = new ArrayList<>();
	// 	for (DocumentHolderInfo documentHolderInfo : documentHolderInfos) {
	// 		DocumentHolderDTO documentHolderDTO = new DocumentHolderDTO();
	// 		documentHolderDTO.setDocumentHolderId(documentHolderInfo.getId());
	// 		documentHolderDTO.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
	// 		documentHolderDTO.setDocumentHolderDescription(documentHolderInfo.getDocumentHolderDescription());
	// 		documentHolderDTO.setDocumentHolderType(documentHolderInfo.getDocumentHolderType());
	// 		//documentHolderDTO.setDocumentFileNumber(documentHolderInfo.getDocumentFileNumber());
	// 		documentHolderDTO.setType(documentHolderInfo.getType());
	// 		documentHolderDTO.setUpdatedDate(documentHolderInfo.getUpdatedDate());
	// 		if (documentHolderInfo.getOrganizationId() != null)
	// 			organizationInfo = organizationInfoRepository.findOne(documentHolderInfo.getOrganizationId());
	// 		if (organizationInfo != null)
	// 			documentHolderDTO.setOrganizationName(organizationInfo.getOrganizationName());
	// 		documentHolderDTOs.add(documentHolderDTO);
	// 	}
	// 	return documentHolderDTOs;
	// }
	private List<DocumentHolderDTO> convertDocumentHolderInfosToDocumentHolderDTOs(
        List<DocumentHolderInfo> documentHolderInfos) {

    OrganizationInfo organizationInfo = null;
    List<DocumentHolderDTO> documentHolderDTOs = new ArrayList<>();

    for (DocumentHolderInfo documentHolderInfo : documentHolderInfos) {

        DocumentHolderDTO documentHolderDTO = new DocumentHolderDTO();

        documentHolderDTO.setDocumentHolderId(documentHolderInfo.getId());
        documentHolderDTO.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
        documentHolderDTO.setDocumentHolderDescription(documentHolderInfo.getDocumentHolderDescription());
        documentHolderDTO.setDocumentHolderType(documentHolderInfo.getDocumentHolderType());
        documentHolderDTO.setType(documentHolderInfo.getType());
        documentHolderDTO.setUpdatedDate(documentHolderInfo.getUpdatedDate());

        // ✅ Set Organization Name
        if (documentHolderInfo.getOrganizationId() != null) {
            organizationInfo =
                    organizationInfoRepository.findOne(documentHolderInfo.getOrganizationId());

            if (organizationInfo != null) {
                documentHolderDTO.setOrganizationName(
                        organizationInfo.getOrganizationName());
            }
        }

        // ✅🔥 Set Vessel Name
        if (documentHolderInfo.getVesselId() != null) {

            ShipProfileInfo ship =
                    shipProfileRepository.findOne(documentHolderInfo.getVesselId());

            if (ship != null) {
                documentHolderDTO.setVesselName(ship.getShipName());
            }
        }

        documentHolderDTOs.add(documentHolderDTO);
    }

    return documentHolderDTOs;
}


// 	@Override
// 	public String updateDocumentHolderInformation(DocumentHolderDTO documentHolderDTO) {
// 		if (documentHolderDTO.getUserId() == null)
// 			return env.getProperty("user.id.null");

// 		if (documentHolderDTO.getDocumentHolderId() == null)
// 			return env.getProperty("document.holder.id.null");

// 		if (documentHolderDTO.getDocumentHolderName() == null
// 				&& StringUtils.isEmpty(documentHolderDTO.getDocumentHolderName())
// 				&& StringUtils.isNotBlank(documentHolderDTO.getDocumentHolderName()))
// 			return env.getProperty("document.holder.file.name.empty");

// 		UserProfileInfo profileInfo = userProfileRepository.findOne(documentHolderDTO.getUserId());
// 		if (profileInfo == null) {
// 			return env.getProperty("user.not.found");
// 		}

// //		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
// //			return env.getProperty("document.holder.super.admin.only.update");
// //		}

// 		DocumentHolderInfo documentHolderInfo = documentHolderRepository
// 				.findOne(documentHolderDTO.getDocumentHolderId());
// 		if (documentHolderInfo == null)
// 			return env.getProperty("document.holder.not.found");

// 		DocumentHolderInfo documentHolderInfoExist = documentHolderRepository
// 				.findByDocumentHolderName(documentHolderDTO.getDocumentHolderName());
// 		if (documentHolderInfoExist != null) {
// 			if (!documentHolderInfo.equals(documentHolderInfoExist))
// 				return env.getProperty("document.holder.name.already.exist");
// 		}
// 		if (documentHolderDTO.getOrganizationName() != null){
// 			OrganizationInfo organizationInfo = organizationInfoRepository.findByOrganizationName(documentHolderDTO.getOrganizationName());
// 			documentHolderInfo.setOrganizationId(organizationInfo.getId());
// 		}

// 		//documentHolderInfo.setDocumentFileNumber(documentHolderDTO.getDocumentFileNumber());
// 		documentHolderInfo.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
// 		documentHolderInfo.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
// 		documentHolderInfo.setUpdatedDate(new Date());
// 		DocumentHolderInfo documentHolderInf=documentHolderRepository.save(documentHolderInfo);
// 		if (documentHolderInf != null){
// 			commonMethodsUtility.maintainHistory(documentHolderInf.getId(),documentHolderInf.getDocumentHolderName(),"DocumentHolder", env.getProperty("history.updated"), documentHolderDTO.getUserId());
// 			return env.getProperty("success");
// 			}

// 		return null;
// 	}


@Override
public String updateDocumentHolderInformation(
        DocumentHolderDTO documentHolderDTO) {

    if (documentHolderDTO.getUserId() == null)
        return env.getProperty("user.id.null");

    if (documentHolderDTO.getDocumentHolderId() == null)
        return env.getProperty("document.holder.id.null");

    DocumentHolderInfo current =
            documentHolderRepository
                    .findOne(documentHolderDTO.getDocumentHolderId());

    if (current == null)
        return env.getProperty("document.holder.not.found");

    // 🔥 Step 1: Determine Parent ID
    Long parentId;

    if (current.getParantId() == null) {
        // This is parent
        parentId = current.getId();
    } else {
        // This is child
        parentId = current.getParantId();
    }

    // 🔥 Step 2: Update Parent
    DocumentHolderInfo parent =
            documentHolderRepository.findOne(parentId);

    parent.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
    parent.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
    parent.setUpdatedDate(new Date());

    documentHolderRepository.save(parent);

    // 🔥 Step 3: Update All Children
    List<DocumentHolderInfo> children =
            documentHolderRepository.findByParantId(parentId);

    if (children != null && !children.isEmpty()) {
        for (DocumentHolderInfo child : children) {
            child.setDocumentHolderName(documentHolderDTO.getDocumentHolderName());
            child.setDocumentHolderDescription(documentHolderDTO.getDocumentHolderDescription());
            child.setUpdatedDate(new Date());

            documentHolderRepository.save(child);
        }
    }

    return env.getProperty("success");
}
// 	@Override
// 	public String deleteDocumentHolderInformation(Long userId, Long documentHolderId) {
// 		if (userId == null)
// 			return env.getProperty("user.id.null");

// 		if (documentHolderId == null)
// 			return env.getProperty("document.holder.id.null");

// 		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
// 		if (profileInfo == null) {
// 			return env.getProperty("user.not.found");
// 		}

// //		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)) {
// //			return env.getProperty("document.holder.super.admin.only.delete");
// //		}return env.getProperty("vessel.exit");

// 		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(documentHolderId);
// 		if (documentHolderInfo == null)
// 			return env.getProperty("document.holder.not.found");

// 		if (profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
// 			List<ShipProfileInfo> shipProfileList = new ArrayList<>();
// 			List<ShipProfileInfo> allShipList = shipProfileRepository.findAll();

// 			for (ShipProfileInfo shipProfileInfo : allShipList) {
// 				Set<DocumentHolderInfo> documentHolderList = shipProfileInfo.getCustomDocumentHolders();
// 				if (documentHolderList != null && !documentHolderList.isEmpty()) {
// 					for (DocumentHolderInfo docInfo : documentHolderList) {
// 						if (docInfo.getId().equals(documentHolderInfo.getId())) {
// 							shipProfileList.add(shipProfileInfo);
// 						}
// 					}

// 					for (ShipProfileInfo shipProfileInfo5 : shipProfileList) {
// 						shipProfileInfo.getCustomDocumentHolders().remove(documentHolderInfo);
// 						shipProfileInfo5 = shipProfileRepository.saveAndFlush(shipProfileInfo5);
// 					}
// 				}
// 			}
// 		}

// 		if (documentHolderInfo != null){
// 		DeletedHistoryDTO deletedHistoryDTO=new DeletedHistoryDTO();
//         deletedHistoryDTO.setObjectId(documentHolderInfo.getId());
//         deletedHistoryDTO.setObjectOne(documentHolderInfo.getDocumentHolderName());
//         deletedHistoryDTO.setObjectTwo(documentHolderInfo.getDocumentHolderType());
//         DataDeletedHistoryInfo dataDeletedHistoryInfo=commonMethodsUtility.maintainDeletedHistory(deletedHistoryDTO);
//         documentHolderRepository.delete(documentHolderInfo);
//         if(dataDeletedHistoryInfo!=null)
//         commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),documentHolderInfo.getDocumentHolderName(),"DocumentHolder", env.getProperty("history.deleted"), userId);

// 		}
// 		return env.getProperty("success");
// 	}
@Override
public String deleteDocumentHolderInformation(Long userId,
                                              Long documentHolderId) {

    if (userId == null)
        return env.getProperty("user.id.null");

    if (documentHolderId == null)
        return env.getProperty("document.holder.id.null");

    DocumentHolderInfo current =
            documentHolderRepository.findOne(documentHolderId);

    if (current == null)
        return env.getProperty("document.holder.not.found");

    // 🔥 Step 1: Determine parentId
    Long parentId;

    if (current.getParantId() == null) {
        parentId = current.getId(); // parent
    } else {
        parentId = current.getParantId(); // child
    }

    // 🔥 Step 2: Get parent
    DocumentHolderInfo parent =
            documentHolderRepository.findOne(parentId);

    // 🔥 Step 3: Get all children
    List<DocumentHolderInfo> children =
            documentHolderRepository.findByParantId(parentId);

    // 🔥 Step 4: Check if any uploaded documents exist

    // Check parent
    if (parent.getExpiryDocumentInfos() != null
            && !parent.getExpiryDocumentInfos().isEmpty()) {
        return "Cannot delete. Document already uploaded.";
    }

    // Check children
    if (children != null) {
        for (DocumentHolderInfo child : children) {
            if (child.getExpiryDocumentInfos() != null
                    && !child.getExpiryDocumentInfos().isEmpty()) {
                return "Cannot delete. Document already uploaded.";
            }
        }
    }

    // 🔥 Step 5: Safe to delete

    // Delete children first
    if (children != null && !children.isEmpty()) {
        documentHolderRepository.delete(children);
    }

    // Delete parent
    documentHolderRepository.delete(parent);

    return env.getProperty("success");
}


	// @Override
	// public String saveUserReportAnIssueInformation(UserReportAnIssueDTO userReportAnIssueDTO) {
	// 	if(userReportAnIssueDTO.getUserId() == null)
	// 		return env.getProperty("user.id.null");

	// 	if(userReportAnIssueDTO.getName() == null && StringUtils.isBlank(userReportAnIssueDTO.getName()) && StringUtils.isEmpty(userReportAnIssueDTO.getName()))
	// 		return env.getProperty("report.issue.name.null");

	// 	if(userReportAnIssueDTO.getEmail() == null && StringUtils.isBlank(userReportAnIssueDTO.getEmail()) && StringUtils.isEmpty(userReportAnIssueDTO.getEmail()))
	// 		return env.getProperty("report.issue.email.null");

	// 	if(userReportAnIssueDTO.getOrganizationName() == null || StringUtils.isBlank(userReportAnIssueDTO.getOrganizationName().trim()) && StringUtils.isEmpty(userReportAnIssueDTO.getOrganizationName().trim()))
	// 		return env.getProperty("report.issue.oraganization.name.null");

	// 	if(userReportAnIssueDTO.getPhoneNumber() == null || StringUtils.isBlank(userReportAnIssueDTO.getPhoneNumber().trim()) && StringUtils.isEmpty(userReportAnIssueDTO.getPhoneNumber().trim()))
	// 		return env.getProperty("report.issue.phone.number.null");

	// 	if(userReportAnIssueDTO.getReason() == null && StringUtils.isBlank(userReportAnIssueDTO.getReason()) && StringUtils.isEmpty(userReportAnIssueDTO.getReason()))
	// 		return env.getProperty("report.issue.reason.null");

	// 	if(!userReportAnIssueDTO.getName().matches(ONLY_LETTERS))
	// 		return env.getProperty("report.issue.name.specialcharacter.found");

	// 	if (userReportAnIssueDTO.getEmail()  != null) {
	// 		Pattern pattern = Pattern.compile(EMAIL_REGEX);
	// 		Matcher matcher = pattern.matcher(userReportAnIssueDTO.getEmail());
	// 		if (!matcher.matches())
	// 			return env.getProperty("report.issue.email.not.valid");
	// 	}

	// 	if(ONLY_NUMBERS.matches(userReportAnIssueDTO.getPhoneNumber()))
	// 		return env.getProperty("report.issue.phone.number.character.found");

	// 	UserProfileInfo profileInfo = userProfileRepository.findOne(userReportAnIssueDTO.getUserId());
	// 	if (profileInfo == null) {
	// 		return env.getProperty("user.not.found");
	// 	}

	// 	UserReportAnIssueInfo userReportAnIssueInfo = convertUserReportAnIssueDTOToUserReportAnIssueInfo(userReportAnIssueDTO);
	// 	userReportAnIssueInfo.setCreatorUserProfileInfo(profileInfo);
	// 	UserReportAnIssueInfo savedUserReportAnIssueInfo = userReportAnIssueInfoRepository.save(userReportAnIssueInfo);
	// 	if(savedUserReportAnIssueInfo != null){
	// 		List<UserProfileInfo> userProfileInfos = userProfileRepository.findByRoleId(roleInfoRepository.findByRoleName(Role.SuperAdmin));
	// 		if(userProfileInfos != null && userProfileInfos.size() > 0){
	// 			String emailText = emailService.sendReportAnIssueNotification(savedUserReportAnIssueInfo);
	// 			User user = userService.findUserByUsername(userProfileInfos.get(0).getUserName());
	// 			boolean emailSendStatus = emailService.sendEmailDEV(user.getMail().toLowerCase(), env.getProperty("report.issue.email.subject"), emailText);
	// 			if(emailSendStatus)
	// 				return env.getProperty("success");
	// 		}

	// 	}

	// 	return null;
	// }
	@Override
  public String saveUserReportAnIssueInformation(UserReportAnIssueDTO userReportAnIssueDTO) {
    // --- 1. Validation Logic (Kept exactly as you had it) ---
    if(userReportAnIssueDTO.getUserId() == null)
      return env.getProperty("user.id.null");

    if(userReportAnIssueDTO.getName() == null && StringUtils.isBlank(userReportAnIssueDTO.getName()) && StringUtils.isEmpty(userReportAnIssueDTO.getName()))
      return env.getProperty("report.issue.name.null");

    if(userReportAnIssueDTO.getEmail() == null && StringUtils.isBlank(userReportAnIssueDTO.getEmail()) && StringUtils.isEmpty(userReportAnIssueDTO.getEmail()))
      return env.getProperty("report.issue.email.null");

    if(userReportAnIssueDTO.getOrganizationName() == null || StringUtils.isBlank(userReportAnIssueDTO.getOrganizationName().trim()) && StringUtils.isEmpty(userReportAnIssueDTO.getOrganizationName().trim()))
      return env.getProperty("report.issue.oraganization.name.null");

    if(userReportAnIssueDTO.getPhoneNumber() == null || StringUtils.isBlank(userReportAnIssueDTO.getPhoneNumber().trim()) && StringUtils.isEmpty(userReportAnIssueDTO.getPhoneNumber().trim()))
      return env.getProperty("report.issue.phone.number.null");

    if(userReportAnIssueDTO.getReason() == null && StringUtils.isBlank(userReportAnIssueDTO.getReason()) && StringUtils.isEmpty(userReportAnIssueDTO.getReason()))
      return env.getProperty("report.issue.reason.null");

    if(!userReportAnIssueDTO.getName().matches(ONLY_LETTERS))
      return env.getProperty("report.issue.name.specialcharacter.found");

    if (userReportAnIssueDTO.getEmail()  != null) {
      Pattern pattern = Pattern.compile(EMAIL_REGEX);
      Matcher matcher = pattern.matcher(userReportAnIssueDTO.getEmail());
      if (!matcher.matches())
        return env.getProperty("report.issue.email.not.valid");
    }

    if(ONLY_NUMBERS.matches(userReportAnIssueDTO.getPhoneNumber()))
      return env.getProperty("report.issue.phone.number.character.found");

    UserProfileInfo profileInfo = userProfileRepository.findOne(userReportAnIssueDTO.getUserId());
    if (profileInfo == null) {
      return env.getProperty("user.not.found");
    }

    // --- 2. Saving Logic ---
    UserReportAnIssueInfo userReportAnIssueInfo = convertUserReportAnIssueDTOToUserReportAnIssueInfo(userReportAnIssueDTO);
    userReportAnIssueInfo.setCreatorUserProfileInfo(profileInfo);

    UserReportAnIssueInfo savedUserReportAnIssueInfo = userReportAnIssueInfoRepository.save(userReportAnIssueInfo);

    // --- 3. FIX: Email Logic Wrapped in Try-Catch ---
    if(savedUserReportAnIssueInfo != null){
      try {
          List<UserProfileInfo> userProfileInfos = userProfileRepository.findByRoleId(roleInfoRepository.findByRoleName(Role.SuperAdmin));

          if(userProfileInfos != null && userProfileInfos.size() > 0){
            String emailText = emailService.sendReportAnIssueNotification(savedUserReportAnIssueInfo);

            // Added check to prevent crash if User or Mail is null
            if (userProfileInfos.get(0).getUserName() != null) {
                User user = userService.findUserByUsername(userProfileInfos.get(0).getUserName());

                if (user != null && user.getMail() != null) {
                    emailService.sendEmailDEV(user.getMail().toLowerCase(), env.getProperty("report.issue.email.subject"), emailText);
                }
            }
          }
      } catch (Exception e) {
          // Log the error, but DO NOT stop the process. The issue is already saved.
          e.printStackTrace();
          System.out.println("Email sending failed for Report Issue: " + e.getMessage());
      }

      // FIX: Return Success because the Database Save worked!
      // Previously, this line was inside the email check, so if email failed, it returned null.
      return env.getProperty("success");
    }

    return env.getProperty("report.issue.create.failure");
  }
	private UserReportAnIssueInfo convertUserReportAnIssueDTOToUserReportAnIssueInfo(UserReportAnIssueDTO userReportAnIssueDTO){
		UserReportAnIssueInfo userReportAnIssueInfo = new UserReportAnIssueInfo();
		userReportAnIssueInfo.setName(userReportAnIssueDTO.getName());
		userReportAnIssueInfo.setOrganizationName(userReportAnIssueDTO.getOrganizationName());
		userReportAnIssueInfo.setEmail(userReportAnIssueDTO.getEmail());
		userReportAnIssueInfo.setPhoneNumber(userReportAnIssueDTO.getPhoneNumber());
		userReportAnIssueInfo.setReason(userReportAnIssueDTO.getReason());
		return userReportAnIssueInfo;
	}

	@Override
	public String addExpiryDocumentCertifcateTypeBasedSuperAdmin(ExpiryCertificateTypeDTO expiryCertificateTypeDTO) {

		ExpiryCertificateTypeInfo expiryCertificateTypeInfo = commonMethodsUtility.convertExpiryCertificateTypeDTOToExpiryCertificateTypeInfo(expiryCertificateTypeDTO);
		expiryCertificateTypeInfo = expiryDocCertificateRepository.saveAndFlush(expiryCertificateTypeInfo);
		if (expiryCertificateTypeInfo != null){
			return env.getProperty("success");
		}
		return null;
	}

	@Override
	public boolean isValidData(ExpiryCertificateTypeDTO expiryCertificateTypeDTO) {
		if (expiryCertificateTypeDTO.getCertificateType() != null && StringUtils.isNotEmpty(expiryCertificateTypeDTO.getCertificateType())
				&& expiryCertificateTypeDTO.getExpiryHolderDescription() != null && StringUtils.isNotEmpty(expiryCertificateTypeDTO.getExpiryHolderDescription())){
			return true;
		}
		return false;
	}

	@Override
	public ExpiryCertificateTypeDTO updateExpiryDocumentCertifcateTypeBasedSuperAdmin(ExpiryCertificateTypeDTO expiryCertificateTypeDTO) {

		ExpiryCertificateTypeInfo expiryCertificateTypeInfo = expiryDocCertificateRepository.findOne(expiryCertificateTypeDTO.getCertificateTypeId());
		if (expiryCertificateTypeInfo.getId() != null){
			expiryCertificateTypeInfo.setCertificateType(expiryCertificateTypeDTO.getCertificateType());
			expiryCertificateTypeInfo.setExpiryHolderDescription(expiryCertificateTypeDTO.getExpiryHolderDescription());
			expiryCertificateTypeInfo = expiryDocCertificateRepository.saveAndFlush(expiryCertificateTypeInfo);
			if (expiryCertificateTypeInfo != null){
				return commonMethodsUtility.convertExpiryCertificateTypeInfoToExpiryCertificateTypeDTO(expiryCertificateTypeInfo);
			}
		}

		return null;
	}

	@Override
	public boolean isValidDataForUpdate(ExpiryCertificateTypeDTO expiryCertificateTypeDTO) {
		if (expiryCertificateTypeDTO.getCertificateType() != null && StringUtils.isNotEmpty(expiryCertificateTypeDTO.getCertificateType())
				&& expiryCertificateTypeDTO.getExpiryHolderDescription() != null && StringUtils.isNotEmpty(expiryCertificateTypeDTO.getExpiryHolderDescription())
				&& expiryCertificateTypeDTO.getCertificateTypeId() != null){
			return true;
		}
		return false;
	}

	@Override
	public String deleteExpiryCertificateTypeInfp(ExpiryCertificateTypeDTO expiryCertificateTypeDTO) {
		if (expiryCertificateTypeDTO.getCertificateTypeId() != null){
			ExpiryCertificateTypeInfo expiryCertificateTypeInfo = expiryDocCertificateRepository.findOne(expiryCertificateTypeDTO.getCertificateTypeId());
			if (expiryCertificateTypeInfo.getId() != null) {
				expiryDocCertificateRepository.delete(expiryCertificateTypeInfo);
				return env.getProperty("success");
			}
		}
		return null;
	}

	@Override
	public List<ExpiryCertificateTypeDTO> getExpiryCertificateTypeInfo() {

		List<ExpiryCertificateTypeInfo> expiryCertificateTypeInfos = expiryDocCertificateRepository.findAll();
		return commonMethodsUtility.convertExpiryCertificateTypeInfosToExpiryCertificateTypeDTO(expiryCertificateTypeInfos);
	}

	@Override
	public List<DocumentHolderDTO> listDocumentHolderInformationByOrganization(
			DocumentHolderDTO documentHolderDTO) {
		List<DocumentHolderInfo> documentHolderInfos;
		UserProfileInfo userProfileInfo = userProfileRepository.findById(documentHolderDTO.getUserId());
		if (documentHolderDTO.getType() != null &&( documentHolderDTO.getType().equalsIgnoreCase("Custom")|| documentHolderDTO.getType().equalsIgnoreCase("Standard") )) {
			documentHolderInfos = documentHolderRepository.findByVesselIdAndType(documentHolderDTO.getVesselid(),documentHolderDTO.getType());
		}else {
			documentHolderInfos = documentHolderRepository.findDistinctDocumentHolderInfoByOrganizationIdAndVesselIdIsNull(userProfileInfo.getOrganizationInfo().getId());
		}
		if(documentHolderInfos != null && documentHolderInfos.size() >0){
			List<DocumentHolderDTO> documentHolderDTOs = convertDocumentHolderInfosToDocumentHolderDTOs (documentHolderInfos);
			return documentHolderDTOs;
		}
		return null;
	}

	@Override
	public List<FaqDTO> findAllQuestionAndAnswerDetails() {

		List<FaqInfo> faqInfos = faqRepository.findAll();
		return commonMethodsUtility.convertFaqInfoAndFaqDTO(faqInfos);
	}

}
