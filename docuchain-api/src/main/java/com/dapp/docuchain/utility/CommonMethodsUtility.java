package com.dapp.docuchain.utility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.ExpiryCertificateTypeDTO;
import com.dapp.docuchain.dto.FaqDTO;
import com.dapp.docuchain.model.DataDeletedHistoryInfo;
import com.dapp.docuchain.model.DataModifiedHistoryInfo;
import com.dapp.docuchain.model.ExpiryCertificateTypeInfo;
import com.dapp.docuchain.model.FaqInfo;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.DataDeletedHistoryRepository;
import com.dapp.docuchain.repository.DataModifiedHistoryRepository;
import com.dapp.docuchain.repository.UserProfileRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CommonMethodsUtility {
	static final Logger LOG = LoggerFactory.getLogger(CommonMethodsUtility.class);
	@Autowired
	private DataModifiedHistoryRepository dataModifiedHistoryRepository;

	@Autowired
	private DataDeletedHistoryRepository dataDeletedHistoryRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;
	public boolean maintainHistory(Long objectId,String objectName,String selectType, String objectStatus, Long modifiedBy) {
		DataModifiedHistoryInfo dataModifiedHistoryInfo=new DataModifiedHistoryInfo();
		boolean validationStatus = fieldValidation(objectId, objectStatus, modifiedBy);
		if (validationStatus != true) {
			return false;
		}

		dataModifiedHistoryInfo.setObjectId(objectId);
		dataModifiedHistoryInfo.setObjectStatus(objectStatus);
		dataModifiedHistoryInfo.setModifiedBy(modifiedBy);
		UserProfileInfo userProfileInfo=userProfileRepository.findById(modifiedBy);
		if(userProfileInfo!=null){
			dataModifiedHistoryInfo.setModifiedByName(userProfileInfo.getUserName());
		}else{
			dataModifiedHistoryInfo.setModifiedByName("No Name");
		}

		dataModifiedHistoryInfo.setObjectName(objectName);
		dataModifiedHistoryInfo.setSelectType(selectType);
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String requiredDate = df.format(new Date());
		dataModifiedHistoryInfo.setDate(requiredDate);
		if(dataModifiedHistoryInfo!=null){
			dataModifiedHistoryRepository.save(dataModifiedHistoryInfo);
			return true;

		}
		return false;
	}

	public DataDeletedHistoryInfo maintainDeletedHistory(DeletedHistoryDTO deletedHistoryDTO) {
		DataDeletedHistoryInfo dataDeletedHistoryInfo=new DataDeletedHistoryInfo();

		dataDeletedHistoryInfo.setObjectId(deletedHistoryDTO.getObjectId());
		dataDeletedHistoryInfo.setObjectOne(deletedHistoryDTO.getObjectOne());
		dataDeletedHistoryInfo.setObjectTwo(deletedHistoryDTO.getObjectTwo());
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String requiredDate = df.format(new Date());
		dataDeletedHistoryInfo.setDate(requiredDate);
		DataDeletedHistoryInfo deletedHistoryInfo=dataDeletedHistoryRepository.save(dataDeletedHistoryInfo);
		return deletedHistoryInfo;
	}

	// public boolean fieldValidation(Long objectId, String objectStatus, Long modifiedBy) {
	// 	if (StringUtils.isNotBlank(objectId.toString()) && StringUtils.isNotBlank(objectStatus)
	// 			&& StringUtils.isNotBlank(modifiedBy.toString())) {
	// 		return true;
	// 	} else {
	// 		return false;
	// 	}
	// }
	public boolean fieldValidation(Long objectId, String objectStatus, Long modifiedBy) {
		if (objectId == null || modifiedBy == null || StringUtils.isBlank(objectStatus)) {
			return false;
		}
		return true;
	}
	public boolean isImage(MultipartFile file) {
		try {
			String mimetype= file.getContentType();
	        String type = mimetype.split("/")[0];
	        if(type.equals("image"))
	        {
	            System.out.println("It's an image");
	        	return true;
	        }
			LOG.info("Invalid image type");
			return false;
		} catch (Exception e) {
			LOG.info("Invalid image type");
			return false;
		}
	}

	public ExpiryCertificateTypeInfo convertExpiryCertificateTypeDTOToExpiryCertificateTypeInfo(
			ExpiryCertificateTypeDTO expiryCertificateTypeDTO) {
		ExpiryCertificateTypeInfo expiryCertificateTypeInfo = new ExpiryCertificateTypeInfo();
		expiryCertificateTypeInfo.setCertificateType(expiryCertificateTypeDTO.getCertificateType());
		expiryCertificateTypeInfo.setExpiryHolderDescription(expiryCertificateTypeDTO.getExpiryHolderDescription());
		return expiryCertificateTypeInfo;
	}

	public ExpiryCertificateTypeDTO convertExpiryCertificateTypeInfoToExpiryCertificateTypeDTO(
			ExpiryCertificateTypeInfo expiryCertificateTypeInfo) {
		ExpiryCertificateTypeDTO expiryCertificateTypeDTO = new ExpiryCertificateTypeDTO();
		expiryCertificateTypeDTO.setCertificateType(expiryCertificateTypeInfo.getCertificateType());
		expiryCertificateTypeDTO.setCertificateTypeId(expiryCertificateTypeDTO.getCertificateTypeId());
		expiryCertificateTypeDTO.setExpiryHolderDescription(expiryCertificateTypeInfo.getExpiryHolderDescription());
		return 	expiryCertificateTypeDTO;
	}

	public List<ExpiryCertificateTypeDTO> convertExpiryCertificateTypeInfosToExpiryCertificateTypeDTO(
			List<ExpiryCertificateTypeInfo> expiryCertificateTypeInfos) {
		List<ExpiryCertificateTypeDTO> expiryCertificateTypeDTOs = new ArrayList<>();
		for (ExpiryCertificateTypeInfo expiryCertificateTypeInfo : expiryCertificateTypeInfos){
			ExpiryCertificateTypeDTO expiryCertificateTypeDTO = new ExpiryCertificateTypeDTO();
			expiryCertificateTypeDTO.setCertificateTypeId(expiryCertificateTypeInfo.getId());
			expiryCertificateTypeDTO.setCertificateType(expiryCertificateTypeInfo.getCertificateType());
			expiryCertificateTypeDTO.setExpiryHolderDescription(expiryCertificateTypeInfo.getExpiryHolderDescription());
			expiryCertificateTypeDTOs.add(expiryCertificateTypeDTO);
		}
		return expiryCertificateTypeDTOs;
	}

	public List<FaqDTO> convertFaqInfoAndFaqDTO(List<FaqInfo> faqInfos) {
		List<FaqDTO> faqDTOs = new ArrayList<>();
		for (FaqInfo faqInfo : faqInfos) {
			FaqDTO faqDTO = new FaqDTO();
			faqDTO.setId(faqInfo.getId());
			faqDTO.setQuestion(faqInfo.getQuestion());
			faqDTO.setAnswer(faqInfo.getAnswer());
			faqDTOs.add(faqDTO);
		}

		return faqDTOs;
	}
}
