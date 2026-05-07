package com.dapp.docuchain.service;

import java.util.List;
import com.dapp.docuchain.dto.CountryDTO;
import com.dapp.docuchain.dto.DocumentHolderDTO;
import com.dapp.docuchain.dto.ExpiryCertificateTypeDTO;
import com.dapp.docuchain.dto.FaqDTO;
import com.dapp.docuchain.dto.PortDTO;
import com.dapp.docuchain.dto.RoleAliasDTO;
import com.dapp.docuchain.dto.UserReportAnIssueDTO;
import com.dapp.docuchain.dto.VesselsTypeDTO;
import com.dapp.docuchain.model.OrganizationInfo;

public interface CommonConfigurationService {

	//public CountryDTO addCountryInformation(CountryDTO countryDTO);

	public String addCountryInformationBySuperAdmin(CountryDTO countryDTO);

	public List<CountryDTO> listCountryInformationForAllUser();

	public String updateCountryInformation(CountryDTO countryDTO);

	public String deleteMultipleAndSingleCountryInformation(CountryDTO countryDTO);

	public String deleteCountryInformation(Long countryId, Long userId);

	public RoleAliasDTO addRoleAliasInformationBasedAdmin(RoleAliasDTO roleAliasDTO);

	public List<RoleAliasDTO> listRoleAliasInformationBasedOrganization(OrganizationInfo organizationInfo);

	public String addRoleAliasInformationBasedOrganization(RoleAliasDTO roleAliasDTO);

	public VesselsTypeDTO addVesselsTypeInformationBasedOrganizationByAdmin(VesselsTypeDTO vesselsTypeDTO);

	public List<VesselsTypeDTO> listVesselsTypeInformationBasedOrganization(OrganizationInfo organizationInfo);

	public String addVesselsTypeInformationValidation(VesselsTypeDTO vesselsTypeDTO);

	public String addCountryPortInformationValidation(PortDTO portDTO);

	public PortDTO addCountryPortInformationOrganizationByAdmin(PortDTO portDTO);

	public String isCountryAndPortNameAndPortAlreadyExists(PortDTO portDTO);

	public List<PortDTO> listPortInformationBasedOnCountryProfile(Long countryId);

	public String deleteAllCountryInformationBasedOrganization(Long userId);

	public String updateCountryPortInformationBasedOrganization(PortDTO portDTO);

	public String deleteCountryPortInformationBasedOrganization(PortDTO portDTO);

	public String deleteAllCountryPortInformationBasedOrganization(PortDTO portDTO);

	public String updateRoleAliasInformationBasedOrganization(RoleAliasDTO roleAliasDTO);

	public String deleteRoleAliasInformationBasedOrganization(RoleAliasDTO roleAliasDTO);

	public String deleteAllRoleAliasInformationBasedOrganization(Long userId);

	public String updateVesselsTypeInformationBasedOrganization(VesselsTypeDTO vesselsTypeDTO);

	public String deleteVesselsTypeInformationBasedOrganization(VesselsTypeDTO vesselsTypeDTO);

	public String deleteAllVesselsTypeInformationBasedOrganization(Long userId);

	public String addDocumentHolderInformationBasedSuperAdmin(DocumentHolderDTO documentHolderDTO);

	public List<PortDTO> findAllPortInformationWithCountryDetails();

	public List<DocumentHolderDTO> listDocumentHolderInformation();

	public String updateDocumentHolderInformation(DocumentHolderDTO documentHolderDTO);

	public String deleteDocumentHolderInformation(Long userId, Long documentHolderId);

	public String saveUserReportAnIssueInformation(UserReportAnIssueDTO userReportAnIssueDTO);

	public String addExpiryDocumentCertifcateTypeBasedSuperAdmin(ExpiryCertificateTypeDTO expiryCertificateTypeDTO);

	public boolean isValidData(ExpiryCertificateTypeDTO expiryCertificateTypeDTO);

	public ExpiryCertificateTypeDTO updateExpiryDocumentCertifcateTypeBasedSuperAdmin(ExpiryCertificateTypeDTO expiryCertificateTypeDTO);

	public boolean isValidDataForUpdate(ExpiryCertificateTypeDTO expiryCertificateTypeDTO);

	public String deleteExpiryCertificateTypeInfp(ExpiryCertificateTypeDTO expiryCertificateTypeDTO);

	public List<ExpiryCertificateTypeDTO> getExpiryCertificateTypeInfo();

	public List<DocumentHolderDTO> listDocumentHolderInformationByOrganization(
			DocumentHolderDTO documentHolderDTO);

	public List<FaqDTO> findAllQuestionAndAnswerDetails();

}
