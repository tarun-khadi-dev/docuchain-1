package com.dapp.docuchain.utility;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import com.dapp.docuchain.model.DocumentDataInfo;
import com.dapp.docuchain.model.DocumentHolderInfo;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.ShipProfileInfo;
import com.dapp.docuchain.repository.ExpiryDocumentRepository;
import com.dapp.docuchain.repository.ShipProfileRepository;
import com.dapp.docuchain.service.FileService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExpiryDocumentUtility {

	@Autowired
	Environment env;

	@Autowired
	ExpiryDocumentRepository expiryDocumentRepository;

	@Autowired
	ShipProfileRepository shipProfileRepository;

	@Autowired
	FileService fileService;

	// This method is used to convert JSON Object to Document Data info
	public DocumentDataInfo convertJSONObjectToDocumentDataInfo(JSONObject jSONObject, String fileExtn)
			throws JSONException {
		DocumentDataInfo documentDataInfo = new DocumentDataInfo();
		JSONObject jSONObjectNew = jSONObject.getJSONObject("fileObjectDTO");
		documentDataInfo.setDocumentFormat(fileExtn);
		documentDataInfo.setDocumentHashCode(jSONObjectNew.getString("hash"));
		documentDataInfo.setDocumentName(jSONObjectNew.getString("fileName"));
		return documentDataInfo;
	}

	// This method is used to convert Expirydocument DTO to object
	public ExpiryDocumentInfo convertExpiryDocumentDTOToObject(ExpiryDocumentDTO expiryDocumentDTO,
			DocumentDataInfo documentDataInfo) {
		ExpiryDocumentInfo expiryDocumentInfo = new ExpiryDocumentInfo();
		expiryDocumentInfo.setIssuingAuthority(expiryDocumentDTO.getIssuingAuthority());
		expiryDocumentInfo.setCertificateNumber(expiryDocumentDTO.getCertificateNumber());
		expiryDocumentInfo.setDocumentName(expiryDocumentDTO.getDocumentName());
		expiryDocumentInfo.setDocumentStatus(env.getProperty("document.status.pending"));
		expiryDocumentInfo.setPlaceOfIssue(expiryDocumentDTO.getPlaceOfIssue());
		expiryDocumentInfo.setIssueDate(expiryDocumentDTO.getIssueDate());
		expiryDocumentInfo.setExpiryDate(expiryDocumentDTO.getExpiryDate());
		if (expiryDocumentDTO.getLastAnnual() != null)
			expiryDocumentInfo.setLastAnnual(expiryDocumentDTO.getLastAnnual());
		if (expiryDocumentDTO.getNextAnnual() != null)
			expiryDocumentInfo.setNextAnnual(expiryDocumentDTO.getNextAnnual());
		expiryDocumentInfo.setRemarks(expiryDocumentDTO.getRemarks());
		expiryDocumentInfo.setDocumentDataInfo(documentDataInfo);
		expiryDocumentInfo.setModifiedDate(new Date());
		expiryDocumentInfo.setCurrentVersion(0);
		expiryDocumentInfo.setArchiveStatus(0);
		expiryDocumentInfo.setStatus(env.getProperty("active"));
		return expiryDocumentInfo;
	}

	public ExpiryDocumentDTO convertExpiryDocumentInfoToExpiryDocumentDTO(DocumentHolderInfo documentHolderInfo,
			Long shipId, Integer archivedStatus) {
		ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findById(shipId);
		expiryDocumentDTO.setDocumentHolderId(documentHolderInfo.getId());
		expiryDocumentDTO.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
		expiryDocumentDTO.setDocumentHolderType(documentHolderInfo.getDocumentHolderType());
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		List<ExpiryDocumentInfo> expiryDocumentInfoList = expiryDocumentRepository
				.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
						documentHolderInfo, shipProfileInfo, 1, env.getProperty("document.status.approve"),
						archivedStatus, env.getProperty("active"));
		if (expiryDocumentInfoList != null) {
			for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfoList) {
				// String
				// fileResponse=fileService.fileRetriveFromStorej(expiryDocumentInfo.getDocumentName());
				if (expiryDocumentInfo.getShipProfileInfo().getId() == shipId
						&& expiryDocumentInfo.getCurrentVersion() == 1) {
					if (expiryDocumentInfo.getCertificateNumber() != null)
						expiryDocumentDTO.setCertificateNumber(expiryDocumentInfo.getCertificateNumber());
					if (expiryDocumentInfo.getId() != null)
						expiryDocumentDTO.setId(expiryDocumentInfo.getId());

					if (expiryDocumentInfo.getDocumentName() != null)
						expiryDocumentDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
					if (expiryDocumentInfo.getExpiryDate() != null)
						expiryDocumentDTO.setExpiryDateString(date.format(expiryDocumentInfo.getExpiryDate()));
					if (expiryDocumentInfo.getLastAnnual() != null)
						expiryDocumentDTO.setLastAnnualString((date.format(expiryDocumentInfo.getLastAnnual())));
					if (expiryDocumentInfo.getNextAnnual() != null)
						expiryDocumentDTO.setNextAnnualString((date.format(expiryDocumentInfo.getNextAnnual())));
					if (expiryDocumentInfo.getIssueDate() != null)
						expiryDocumentDTO.setIssueDateString(date.format(expiryDocumentInfo.getIssueDate()));
					if (expiryDocumentInfo.getPlaceOfIssue() != null)
						expiryDocumentDTO.setPlaceOfIssue(expiryDocumentInfo.getPlaceOfIssue());
					if (expiryDocumentInfo.getDocumentStatus() != null)
						expiryDocumentDTO.setDocumentStatus(expiryDocumentInfo.getDocumentStatus());
					if (expiryDocumentInfo.getModifiedDate() != null)
						expiryDocumentDTO.setModifiedDate(expiryDocumentInfo.getModifiedDate());
					if (expiryDocumentInfo.getCurrentVersion() != null)
						expiryDocumentDTO.setCurrentVersion(expiryDocumentInfo.getCurrentVersion());
					if (expiryDocumentInfo.getArchiveStatus() != null)
						expiryDocumentDTO.setArchivedStatus(expiryDocumentInfo.getArchiveStatus());
					if (expiryDocumentInfo.getIssuingAuthority() != null)
						expiryDocumentDTO.setIssuingAuthority(expiryDocumentInfo.getIssuingAuthority());
					expiryDocumentDTO.setRemarks(expiryDocumentInfo.getRemarks());
					if (expiryDocumentInfo.getExpiryDate() != null ) {
						String colorCode = getExpiryDocumentColor(expiryDocumentInfo.getExpiryDate());
						expiryDocumentDTO.setStatusColor(colorCode);
					}
					if (expiryDocumentInfo.getExpiryDate() == null){
						expiryDocumentDTO.setExpiryDateString("");
						expiryDocumentDTO.setStatusColor("green");
					}

					// expiryDocumentDTO.setEncodedFile(fileResponse);
					expiryDocumentDTO.setDocumentDownloadUrl(
							env.getProperty("expiryDocument.download.url") + expiryDocumentInfo.getId());
					expiryDocumentDTO.setDocumentPreviewUrl(
							env.getProperty("expiryDocument.preview.url") + expiryDocumentInfo.getId());
					expiryDocumentDTO.setFileExtension(expiryDocumentInfo.getDocumentDataInfo().getDocumentFormat());
				}
			}
		}
		return expiryDocumentDTO;
	}

	public ExpiryDocumentDTO convertExpiryTypeDocumentInfoToExpiryDocumentDTO(DocumentHolderInfo documentHolderInfo,
			Long shipId, Integer archivedStatus) {
		ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findById(shipId);
		DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		List<ExpiryDocumentInfo> expiryDocumentInfoList = expiryDocumentRepository
				.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
						documentHolderInfo, shipProfileInfo, 1, env.getProperty("document.status.approve"),
						archivedStatus, env.getProperty("active"));
		if (expiryDocumentInfoList != null) {
			for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfoList) {
				// String
				// fileResponse=fileService.fileRetriveFromStorej(expiryDocumentInfo.getDocumentName());
				if (expiryDocumentInfo.getShipProfileInfo().getId() == shipId
						&& expiryDocumentInfo.getCurrentVersion() == 1) {
					expiryDocumentDTO.setDocumentHolderId(documentHolderInfo.getId());
					expiryDocumentDTO.setDocumentHolderName(documentHolderInfo.getDocumentHolderName());
					expiryDocumentDTO.setDocumentHolderType(documentHolderInfo.getDocumentHolderType());
					if (expiryDocumentInfo.getCertificateNumber() != null)
						expiryDocumentDTO.setCertificateNumber(expiryDocumentInfo.getCertificateNumber());
					if (expiryDocumentInfo.getId() != null)
						expiryDocumentDTO.setId(expiryDocumentInfo.getId());

					if (expiryDocumentInfo.getDocumentName() != null)
						expiryDocumentDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
					if (expiryDocumentInfo.getExpiryDate() != null)
						expiryDocumentDTO.setExpiryDateString(date.format(expiryDocumentInfo.getExpiryDate()));
					if (expiryDocumentInfo.getLastAnnual() != null)
						expiryDocumentDTO.setLastAnnualString((date.format(expiryDocumentInfo.getLastAnnual())));
					if (expiryDocumentInfo.getNextAnnual() != null)
						expiryDocumentDTO.setNextAnnualString((date.format(expiryDocumentInfo.getNextAnnual())));
					if (expiryDocumentInfo.getIssueDate() != null)
						expiryDocumentDTO.setIssueDateString(date.format(expiryDocumentInfo.getIssueDate()));
					if (expiryDocumentInfo.getPlaceOfIssue() != null)
						expiryDocumentDTO.setPlaceOfIssue(expiryDocumentInfo.getPlaceOfIssue());
					if (expiryDocumentInfo.getDocumentStatus() != null)
						expiryDocumentDTO.setDocumentStatus(expiryDocumentInfo.getDocumentStatus());
					if (expiryDocumentInfo.getModifiedDate() != null)
						expiryDocumentDTO.setModifiedDate(expiryDocumentInfo.getModifiedDate());
					if (expiryDocumentInfo.getCurrentVersion() != null)
						expiryDocumentDTO.setCurrentVersion(expiryDocumentInfo.getCurrentVersion());
					if (expiryDocumentInfo.getArchiveStatus() != null)
						expiryDocumentDTO.setArchivedStatus(expiryDocumentInfo.getArchiveStatus());
					expiryDocumentDTO.setRemarks(expiryDocumentInfo.getRemarks());
					if (expiryDocumentInfo.getExpiryDate() != null) {
						String colorCode = getExpiryDocumentColor(expiryDocumentInfo.getExpiryDate());
						expiryDocumentDTO.setStatusColor(colorCode);
					}

					// expiryDocumentDTO.setEncodedFile(fileResponse);
					expiryDocumentDTO.setDocumentDownloadUrl(
							env.getProperty("expiryDocument.download.url") + expiryDocumentInfo.getId());
					expiryDocumentDTO.setDocumentPreviewUrl(
							env.getProperty("expiryDocument.preview.url") + expiryDocumentInfo.getId());
					expiryDocumentDTO.setFileExtension(expiryDocumentInfo.getDocumentDataInfo().getDocumentFormat());
				}
			}
		}
		return expiryDocumentDTO;
	}

	// This method is used to verify parameter
	public String verifyParamResponse(ExpiryDocumentDTO expiryDocumentDTO) {
		if (!(expiryDocumentDTO.getDocumentHolderId() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getDocumentHolderId().toString()))) {
			return env.getProperty("documentholder.id.missing");
		}
		if (!(expiryDocumentDTO.getShipProfileId() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getShipProfileId().toString()))) {
			return env.getProperty("shipprofile.id.missing");
		}
		return env.getProperty("success");
	}

	// This method is used to verifiy share document param
	public String verifyShareDocumentParam(ExpiryDocumentDTO expiryDocumentDTO) {
		if (!(expiryDocumentDTO.getId() != null && StringUtils.isNotBlank(expiryDocumentDTO.getId().toString()))) {
			return env.getProperty("expiry.document.id.missing");
		}
		if (!(expiryDocumentDTO.getEmailIds() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getEmailIds().toString()))) {
			return env.getProperty("emailids.missing");
		}
		return env.getProperty("success");
	}

	// This method is used to convert expiryDocument info to DTO object
	public ExpiryDocumentDTO convertExpiryDocumentToDTO(ExpiryDocumentInfo expiryDocumentInfo) {
		ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
		expiryDocumentDTO.setCertificateNumber(expiryDocumentInfo.getCertificateNumber());
		expiryDocumentDTO.setId(expiryDocumentInfo.getId());
		expiryDocumentDTO.setDocumentName(expiryDocumentInfo.getDocumentName());
		expiryDocumentDTO.setDocumentHolderName(expiryDocumentInfo.getDocumentHolderInfo().getDocumentHolderName());
		expiryDocumentDTO.setUploadDate(expiryDocumentInfo.getCreateDate());
		expiryDocumentDTO.setExpiryDate(expiryDocumentInfo.getExpiryDate());
		expiryDocumentDTO.setLastAnnual(expiryDocumentInfo.getLastAnnual());
		expiryDocumentDTO.setNextAnnual(expiryDocumentInfo.getNextAnnual());
		expiryDocumentDTO.setIssueDate(expiryDocumentInfo.getIssueDate());
		expiryDocumentDTO.setPlaceOfIssue(expiryDocumentInfo.getPlaceOfIssue());
		expiryDocumentDTO.setDocumentStatus(expiryDocumentInfo.getDocumentStatus());
		expiryDocumentDTO.setModifiedDate(expiryDocumentInfo.getModifiedDate());
		expiryDocumentDTO.setCurrentVersion(expiryDocumentInfo.getCurrentVersion());
		expiryDocumentDTO.setArchivedStatus(expiryDocumentInfo.getArchiveStatus());
		expiryDocumentDTO.setRemarks(expiryDocumentInfo.getRemarks());
		if (expiryDocumentInfo.getExpiryDate() != null){
			String colorCode = getExpiryDocumentColor(expiryDocumentInfo.getExpiryDate());
			expiryDocumentDTO.setStatusColor(colorCode);
		}else {
			expiryDocumentDTO.setStatusColor("green");
		}

		// expiryDocumentDTO.setEncodedFile(fileResponse);
		expiryDocumentDTO
				.setDocumentDownloadUrl(env.getProperty("expiryDocument.download.url") + expiryDocumentInfo.getId());
		expiryDocumentDTO
				.setDocumentPreviewUrl(env.getProperty("expiryDocument.preview.url") + expiryDocumentInfo.getId());
		expiryDocumentDTO.setFileExtension(expiryDocumentInfo.getDocumentDataInfo().getDocumentFormat());
		if (expiryDocumentInfo.getApprovedBy() != null)
			expiryDocumentDTO.setApprovedBy(expiryDocumentInfo.getApprovedBy().getUserName());
		if (expiryDocumentInfo.getUploadedBy() != null)
			expiryDocumentDTO.setUploadedUserName(expiryDocumentInfo.getUploadedBy().getUserName());

		return expiryDocumentDTO;

	}

	// This method is used to validate create folder param
	public String validateCreateFolderParam(ExpiryDocumentDTO expiryDocumentDTO) {
		if (!(expiryDocumentDTO.getShipProfileId() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getShipProfileId().toString()))) {
			return env.getProperty("shipprofile.id.missing");
		}
		if (!(expiryDocumentDTO.getCustomFolderName() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getCustomFolderName().toString()))) {
			return env.getProperty("custom.folder.name.missing");
		}
		return env.getProperty("success");

	}

	// This method is used to create custom document holder info object
	public DocumentHolderInfo convertExpiryDocumentToCustomDocumentHolderObject(ExpiryDocumentDTO expiryDocumentDTO) {
		DocumentHolderInfo documentHolderInfo = new DocumentHolderInfo();
		documentHolderInfo.setDocumentHolderName(expiryDocumentDTO.getCustomFolderName());
		documentHolderInfo.setDocumentHolderType(env.getProperty("custom.document.holder.type"));
		return documentHolderInfo;
	}

	// This method is used to verify ship profile id and user id param exists or
	// not
	public String checkShipProfileAndUserIdExsits(ExpiryDocumentDTO expiryDocumentDTO) {
		/*
		 * if (!(expiryDocumentDTO.getShipProfileId() != null &&
		 * StringUtils.isNotBlank(expiryDocumentDTO.getShipProfileId().toString()))) {
		 * return env.getProperty("shipprofile.id.missing"); }
		 */
		if (!(expiryDocumentDTO.getUserId() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getUserId().toString()))) {
			return env.getProperty("user.id.missing");
		}
		return env.getProperty("success");

	}

	// This method is used for verfiying expiry document group share param
	public String verifyGroupDocumentShareParam(GroupTagDTO groupTagDTO) {
		if (!(groupTagDTO.getShipId() != null && StringUtils.isNotBlank(groupTagDTO.getShipId().toString()))) {
			return env.getProperty("shipprofile.id.missing");
		}
		if (!(groupTagDTO.getMode() != null && StringUtils.isNotBlank(groupTagDTO.getMode()))) {
			return env.getProperty("group.mode.missing");
		}
		if (!(groupTagDTO.getEmailId() != null && StringUtils.isNotBlank(groupTagDTO.getEmailId()))) {
			return env.getProperty("group.mode.details.missing");
		}
		return env.getProperty("success");
	}

	// This method is used for get the color for expiry document
	public String getExpiryDocumentColor(Date expiryDcoumentDate) {
		String color = "grey";
		Date expiryDate = expiryDcoumentDate;
		Date currentDate = new Date();
		long diff = expiryDate.getTime() - currentDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if (diffDays >= 30) {
			color = "green";
			return color;
		} else if (diffDays < 30 && diffDays >= 0) {
			color = "yellow";
			return color;
		} else if (diffDays < 0) {
			color = "red";
			return color;
		}
		return color;
	}

	public boolean dualApprovalValidation(ExpiryDocumentDTO expiryDocumentDTO) {
		if (StringUtils.isNotBlank(expiryDocumentDTO.getLoginId().toString())
				&& StringUtils.isNotBlank(expiryDocumentDTO.getOrganizationId().toString())
				&& StringUtils.isNotBlank(expiryDocumentDTO.getFlag().toString())) {
			return true;
		}
		return false;
	}

	public boolean validateLoginId(ExpiryDocumentDTO expiryDocumentDTO) {
		if (StringUtils.isNotBlank(expiryDocumentDTO.getLoginId().toString())) {
			return true;
		}
		return false;
	}

	public String verifyUpdateParamResponse(ExpiryDocumentDTO expiryDocumentDTO) {
		if (!(expiryDocumentDTO.getId() != null && StringUtils.isNotBlank(expiryDocumentDTO.getId().toString()))) {
			return env.getProperty("expiry.document.id.missing");
		}
		ExpiryDocumentInfo expiryDocumentInfo = expiryDocumentRepository.findOne(expiryDocumentDTO.getId());
		if (expiryDocumentInfo==null) {
			return env.getProperty("expiry.document.not.exists");
		}
		if (!(expiryDocumentDTO.getShipProfileId() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getShipProfileId().toString()))) {
			return env.getProperty("shipprofile.id.missing");
		}
//		if (!(expiryDocumentDTO.getIssueDate() != null
//				&& StringUtils.isNotBlank(expiryDocumentDTO.getIssueDate().toString()))) {
//			return env.getProperty("Issue date is missing");
//		}
//		if (!(expiryDocumentDTO.getExpiryDate() != null
//				&& StringUtils.isNotBlank(expiryDocumentDTO.getExpiryDate().toString()))) {
//			return env.getProperty("Expiry date is missing");
//		}
		if (!(expiryDocumentDTO.getUploadedUserId() != null
				&& StringUtils.isNotBlank(expiryDocumentDTO.getUploadedUserId().toString()))) {
			return env.getProperty("uploaded.user.id.missing");
		}
//		if (!(expiryDocumentDTO.getCertificateNumber() != null
//				&& StringUtils.isNotBlank(expiryDocumentDTO.getCertificateNumber().toString()))) {
//			return env.getProperty("Certificate number is missing");
//		}
		return env.getProperty("success");
	}

	public List<ExpiryDocumentDTO> convertAvailbleDocumentAndEmptyDocument(
			List<ExpiryDocumentDTO> expirAvailableDocument, List<ExpiryDocumentDTO> expiryEmptyDocument) {
		List<ExpiryDocumentDTO> expiryDocumentDTOs = new ArrayList<>();
		for (ExpiryDocumentDTO expiryDocumentDTO : expirAvailableDocument) {
			expiryDocumentDTOs.add(expiryDocumentDTO);
		}
		for (ExpiryDocumentDTO expiryDocumentDTO : expiryEmptyDocument){
			expiryDocumentDTOs.add(expiryDocumentDTO);
		}
		return expiryDocumentDTOs;
	}

}
