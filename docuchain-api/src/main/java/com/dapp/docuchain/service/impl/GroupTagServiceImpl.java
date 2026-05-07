package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.DeletedHistoryDTO;
import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import com.dapp.docuchain.model.*;
import com.dapp.docuchain.repository.*;
import com.dapp.docuchain.service.EmailService;
import com.dapp.docuchain.service.GroupTagService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.ExpiryDocumentUtility;
import com.dapp.docuchain.utility.GroupTagUtils;

import ch.qos.logback.core.net.SyslogOutputStream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupTagServiceImpl implements GroupTagService {
	@Autowired
	EmailService emailService;
	@Autowired
	Environment env;
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private GroupTagDocumentRepository groupTagDocumentRepository;
	@Autowired
	private ExpiryDocumentRepository expiryDocumentRepository;
	@Autowired
	private ShipProfileRepository shipProfileRepository;
	@Autowired
	private GroupTagUtils groupTagUtils;
	@Autowired
	private DocumentHolderRepository documentHolderRepository;
	@Autowired
	private GroupSharedDocumentRepository groupSharedDocumentRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private ExpiryDocumentUtility expiryDocumentUtility;

	@Override
	public String validateSourceExist(GroupTagDTO groupTagDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		if (userProfileInfo == null) {
			return env.getProperty("user.not.found");
		}

		for (Long shipId : groupTagDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
			if (shipProfileInfo != null) {
				GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
						.findByGroupNameAndUserProfileInfoAndShipProfileInfo(groupTagDTO.getGroupName(),
								userProfileInfo, shipProfileInfo);
				if (groupTagDocumentInfo != null) {
					return "Group Name Already exist for ship" + " " + shipProfileInfo.getShipName();
				}
				/*
				 * GroupTagDocumentInfo groupTagDocumentInfo1 = groupTagDocumentRepository
				 * .findByUserProfileInfoAndShipProfileInfoAndEmailId(userProfileInfo,
				 * shipProfileInfo, groupTagDTO.getEmailId()); if (groupTagDocumentInfo1 !=
				 * null) { return "Group already exist for this email id"; }
				 */
			} else {
				return env.getProperty("ship.not.found");
			}
		}
		return env.getProperty("success");
	}

	@Override
	public boolean saveGroupTag(GroupTagDTO groupTagDTO) {
		List<GroupTagDocumentInfo> groupTagDocumentInfos = new ArrayList<>();
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		for (Long shipId : groupTagDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
			if (userProfileInfo != null && shipProfileInfo != null) {
				GroupTagDocumentInfo groupTagDocumentInfo = new GroupTagDocumentInfo();
				groupTagDocumentInfo.setGroupName(groupTagDTO.getGroupName());
				groupTagDocumentInfo.setEmailId(groupTagDTO.getEmailId());
				groupTagDocumentInfo.setUserProfileInfo(userProfileInfo);
				if (groupTagDTO.getKeyword() != null && StringUtils.isNotBlank(groupTagDTO.getKeyword())) {
					groupTagDocumentInfo.setKeyword(groupTagDTO.getKeyword());
				}
				groupTagDocumentInfo.setShipProfileInfo(shipProfileInfo);
				GroupTagDocumentInfo groupTagDocument = groupTagDocumentRepository.save(groupTagDocumentInfo);
				if (groupTagDocument != null) {
					groupTagDocumentInfos.add(groupTagDocument);
					commonMethodsUtility.maintainHistory(groupTagDocument.getId(), groupTagDocument.getGroupName(),
							"Group", env.getProperty("history.created"), groupTagDTO.getLoginId());
				}
			}
		}
		if (groupTagDocumentInfos != null && groupTagDocumentInfos.size() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public boolean saveGroupwithExp(GroupTagDTO groupTagDTO) {

		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		for (Long shipId : groupTagDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
			if (userProfileInfo != null && shipProfileInfo != null) {
				GroupTagDocumentInfo groupTagDocumentInfo = new GroupTagDocumentInfo();
				groupTagDocumentInfo.setGroupName(groupTagDTO.getGroupName());
				groupTagDocumentInfo.setEmailId(groupTagDTO.getEmailId());
				groupTagDocumentInfo.setUserProfileInfo(userProfileInfo);
				groupTagDocumentInfo.setShipProfileInfo(shipProfileInfo);
				if (groupTagDTO.getKeyword() != null && StringUtils.isNotBlank(groupTagDTO.getKeyword())) {
					groupTagDocumentInfo.setKeyword(groupTagDTO.getKeyword());
				}
				Set<DocumentHolderInfo> documentHolderInfos = new HashSet<DocumentHolderInfo>();
				if (groupTagDTO.getDocumentHolderIds() != null && groupTagDTO.getDocumentHolderIds().length > 0) {
					for (int i = 0; i < groupTagDTO.getDocumentHolderIds().length; i++) {
						DocumentHolderInfo documentHolderInfo = documentHolderRepository
								.findOne(groupTagDTO.getDocumentHolderIds()[i]);
						if (documentHolderInfo != null) {
							groupTagDocumentInfo.setId(groupTagDTO.getGroupId());
							groupTagDocumentInfo.setDocumentHolderInfo(documentHolderInfos);
							documentHolderInfos.add(documentHolderInfo);
						}
					}
					// groupTagDocumentInfo = groupTagDocumentRepository.save(groupTagDocumentInfo);
				}

				groupTagDocumentInfo = groupTagDocumentRepository.save(groupTagDocumentInfo);
				if (groupTagDocumentInfo != null) {
					commonMethodsUtility.maintainHistory(groupTagDocumentInfo.getId(),
							groupTagDocumentInfo.getGroupName(), "Group", env.getProperty("history.created"),
							groupTagDTO.getLoginId());
				}

			}
		}
		return true;
	}

	@Override
	public boolean isGroupExts(Long groupId) {
		boolean groupTagDocumentInfo = groupTagDocumentRepository.exists(groupId);
		return groupTagDocumentInfo;
	}

	@Override
	public boolean updateGroupTag(GroupTagDTO groupTagDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		if (userProfileInfo != null) {
			GroupTagDocumentInfo groupTagDocumentInfoExits = groupTagDocumentRepository.findOne(groupTagDTO.getId());
			groupTagDocumentInfoExits.setGroupName(groupTagDTO.getGroupName());
			groupTagDocumentInfoExits.setKeyword(groupTagDTO.getKeyword());
			groupTagDocumentInfoExits.setUserProfileInfo(userProfileInfo);
			groupTagDocumentInfoExits.setEmailId(groupTagDTO.getEmailId());
			GroupTagDocumentInfo groupTagDocumentExits = groupTagDocumentRepository.save(groupTagDocumentInfoExits);
			if (groupTagDocumentExits != null) {
				commonMethodsUtility.maintainHistory(groupTagDocumentExits.getId(),
						groupTagDocumentExits.getGroupName(), "Group", env.getProperty("history.updated"),
						groupTagDTO.getUserProfileId());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean isUserProfileIdExits(Long userProfileId) {
		boolean userProfileInfo = userProfileRepository.exists(userProfileId);
		return userProfileInfo;
	}

	@Override
	public List<GroupTagDTO> getAllGroupInfo(Integer userProfileId) {
		List<GroupTagDTO> groupTagDTOs = new ArrayList<GroupTagDTO>();
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(userProfileId.longValue());
		if (userProfileInfo != null) {
			List<GroupTagDocumentInfo> allGroup = groupTagDocumentRepository.findByUserProfileInfo(userProfileInfo);

			for (GroupTagDocumentInfo groupTagDocumentInfo : allGroup) {
				GroupTagDTO groupTagDTO = new GroupTagDTO();
				groupTagDTO.setId(groupTagDocumentInfo.getId());
				groupTagDTO.setGroupName(groupTagDocumentInfo.getGroupName());
				groupTagDTO.setEmailId(groupTagDocumentInfo.getEmailId());
				groupTagDTO.setKeyword(groupTagDocumentInfo.getKeyword());
				// groupTagDTO.setUserProfileId(groupTagDocumentInfo.getUserProfileInfo().getId());
				groupTagDTOs.add(groupTagDTO);
			}
			return groupTagDTOs;
		}
		return groupTagDTOs;
	}

	@Override
	public boolean deleteGroup(GroupTagDTO groupTagDTO) {
		// GroupTagDocumentInfo groupTagDocumentInfo =
		// groupTagDocumentRepository.findOne(groupId);
		List<GroupTagDocumentInfo> expDocumentgroup = groupTagDocumentRepository.findById(groupTagDTO.getGroupId());
		if (expDocumentgroup != null && expDocumentgroup.size() > 0) {
			groupTagDocumentRepository.delete(expDocumentgroup);
			for (GroupTagDocumentInfo groupTagDocumentInfo : expDocumentgroup) {
				DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
				deletedHistoryDTO.setObjectId(groupTagDocumentInfo.getId());
				deletedHistoryDTO.setObjectOne(groupTagDocumentInfo.getGroupName());
				deletedHistoryDTO.setObjectTwo(groupTagDocumentInfo.getKeyword());
				DataDeletedHistoryInfo dataDeletedHistoryInfo = commonMethodsUtility
						.maintainDeletedHistory(deletedHistoryDTO);
				if (dataDeletedHistoryInfo != null)
					commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),
							groupTagDocumentInfo.getGroupName(), "Group", env.getProperty("history.deleted"),
							groupTagDTO.getLoginId());
			}

		}
		return true;
	}

	@Override
	public String isValidateExpiryTagGroupParamExist(GroupTagDTO groupTagDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		if (userProfileInfo == null) {
			return "The given user profile not exits";
		}
		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(groupTagDTO.getDocumentHolderId());
		if (documentHolderInfo == null) {
			return "The given expiry document not exits";
		}
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupTagDTO.getGroupId());
		if (groupTagDocumentInfo == null) {
			return "The given group tag not exits";
		}
		return "Success";
	}

	@Override
	public String isExpiryTagGroupExits(GroupTagDTO groupTagDTO) {
		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(groupTagDTO.getDocumentHolderId());
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
				.findByIdAndDocumentHolderInfo(groupTagDTO.getGroupId(), documentHolderInfo);
		if (groupTagDocumentInfo != null) {
			return "This document already  exist in given group";
		}
		return "Success";
	}

	@Override
	public boolean saveExpiryTagGroup(GroupTagDTO groupTagDTO) {
		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(groupTagDTO.getDocumentHolderId());
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupTagDTO.getGroupId());
		Set<DocumentHolderInfo> documentHolderInfos = groupTagDocumentInfo.getDocumentHolderInfo();
		if (documentHolderInfos != null && documentHolderInfos.size() > 0) {
			documentHolderInfos.add(documentHolderInfo);
			groupTagDocumentInfo.setDocumentHolderInfo(documentHolderInfos);
			groupTagDocumentInfo.setId(groupTagDTO.getGroupId());
			GroupTagDocumentInfo groupTagDocument = groupTagDocumentRepository.save(groupTagDocumentInfo);
			if (groupTagDocument != null) {
				commonMethodsUtility.maintainHistory(groupTagDocument.getId(), groupTagDocument.getGroupName(), "Group",
						env.getProperty("history.created"), groupTagDTO.getLoginId());
			}
			return true;
		} else {
			documentHolderInfos = new HashSet<DocumentHolderInfo>();
			documentHolderInfos.add(documentHolderInfo);
			groupTagDocumentInfo.setDocumentHolderInfo(documentHolderInfos);
			groupTagDocumentInfo.setId(groupTagDTO.getGroupId());
			GroupTagDocumentInfo groupTagDocument = groupTagDocumentRepository.save(groupTagDocumentInfo);
			if (groupTagDocument != null) {
				commonMethodsUtility.maintainHistory(groupTagDocument.getId(), groupTagDocument.getGroupName(), "Group",
						env.getProperty("history.created"), groupTagDTO.getLoginId());
			}
			return true;
		}
	}

	@Override
	public String isExpiryTagGroupExitsOrNot(GroupTagDTO groupTagDTO) {

		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(groupTagDTO.getDocumentHolderId());
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
				.findByIdAndDocumentHolderInfo(groupTagDTO.getGroupId(), documentHolderInfo);
		if (groupTagDocumentInfo == null) {
			return "The given expiry document not  exits in given group";
		}
		return "Success";
	}

	@Override
	public boolean deleteExipryDocumentInGroup(GroupTagDTO groupTagDTO) {
		// UserProfileInfo userProfileInfo =
		// userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(groupTagDTO.getDocumentHolderId());
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
				.findByIdAndDocumentHolderInfo(groupTagDTO.getGroupId(), documentHolderInfo);
		if (groupTagDocumentInfo != null) {
			Set<DocumentHolderInfo> documentHolderInfos = groupTagDocumentInfo.getDocumentHolderInfo();
			boolean expr = documentHolderInfos.contains(documentHolderInfo);
			if (expr) {
				DeletedHistoryDTO deletedHistoryDTO = new DeletedHistoryDTO();
				deletedHistoryDTO.setObjectId(documentHolderInfo.getId());
				deletedHistoryDTO.setObjectTwo(documentHolderInfo.getDocumentHolderName());
				DataDeletedHistoryInfo dataDeletedHistoryInfo = commonMethodsUtility
						.maintainDeletedHistory(deletedHistoryDTO);
				documentHolderInfos.remove(documentHolderInfo);
				if (dataDeletedHistoryInfo != null)
					commonMethodsUtility.maintainHistory(dataDeletedHistoryInfo.getId(),
							documentHolderInfo.getDocumentHolderName(), "Group", env.getProperty("history.deleted"),
							groupTagDTO.getLoginId());
				groupTagDocumentRepository.save(groupTagDocumentInfo);
				return true;
			}

		}
		return false;
	}

	@Override
	public List<ExpiryDocumentDTO> getShipExipryDocumentInGroup(GroupTagDTO groupTagDTO) {
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(groupTagDTO.getShipId());
		List<ExpiryDocumentInfo> expiryDocumentInfoList = expiryDocumentRepository
				.findByShipProfileInfo(shipProfileInfo);
		List<ExpiryDocumentDTO> expiryDocumentDTOs = new ArrayList<ExpiryDocumentDTO>();
		for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfoList) {
			DocumentHolderInfo documentHolderInfo = documentHolderRepository
					.findOne(expiryDocumentInfo.getDocumentHolderInfo().getId());
			// ExpiryDocumentInfo expirydoc =
			// expiryDocumentRepository.findExpiryDocumentInfoByDocumentHolderInfo(documentHolderInfo);
			GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
					.findByIdAndDocumentHolderInfo(groupTagDTO.getGroupId(), documentHolderInfo);
			if (groupTagDocumentInfo != null) {
				ExpiryDocumentDTO expiryDocumentDTO = groupTagUtils
						.convertDocumentHolderInfoToExpiryDocumentDTO(groupTagDocumentInfo, documentHolderInfo);
				expiryDocumentDTOs.add(expiryDocumentDTO);
			}
		}
		return expiryDocumentDTOs;
	}

	@Override
	public List<ExpiryDocumentDTO> getDocumentDataBasedExpiryList(GroupTagDTO groupTagDTO) {
		DocumentHolderInfo documentHolderInfo = documentHolderRepository.findOne(groupTagDTO.getDocumentHolderId());
		List<ExpiryDocumentInfo> expiryDocumentInfoList = expiryDocumentRepository
				.findByDocumentHolderInfo(documentHolderInfo);
		List<ExpiryDocumentDTO> expiryDocumentDTOs = new ArrayList<ExpiryDocumentDTO>();
		for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfoList) {
			// DocumentHolderInfo documentHolderInfo =
			// documentHolderRepository.findOne(expiryDocumentInfo.getDocumentHolderInfo().getId());
			GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
					.findByIdAndDocumentHolderInfo(groupTagDTO.getGroupId(), documentHolderInfo);
			if (groupTagDocumentInfo != null) {
				ExpiryDocumentDTO expiryDocumentDTO = groupTagUtils
						.convertDocumentHolderInfoToExpiryDocumentDTO(groupTagDocumentInfo, documentHolderInfo);
				expiryDocumentDTOs.add(expiryDocumentDTO);
			}
		}
		return expiryDocumentDTOs;
	}

	@Override
	public List<ExpiryDocumentDTO> getAllExpiredocumentinGroup(Long groupId) {
		List<ExpiryDocumentDTO> expirydocs = new ArrayList<ExpiryDocumentDTO>();
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		Set<DocumentHolderInfo> documentHolderList = groupTagDocumentInfo.getDocumentHolderInfo();
		ShipProfileInfo shipProfileInfo = groupTagDocumentInfo.getShipProfileInfo();
		// ShipProfileInfo
		// shipProfileInfo=shipProfileRepository.findOne(groupTagDocumentInfo.get);
		if (shipProfileInfo != null && documentHolderList != null) {
			for (DocumentHolderInfo documentHolderInfo : documentHolderList) {
				List<ExpiryDocumentInfo> expirydocList = expiryDocumentRepository
						.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
								documentHolderInfo, shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
								env.getProperty("active"));
				// ExpiryDocumentInfo expirydoc =
				// expiryDocumentRepository.findOne(expiryDocumentInfo.getId());

				for (ExpiryDocumentInfo expirydoc : expirydocList) {
					ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
					expiryDocumentDTO.setId(expirydoc.getId());
					expiryDocumentDTO.setGroupId(groupId);
					expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
					expiryDocumentDTO.setDocumentHolderName(expirydoc.getDocumentHolderInfo().getDocumentHolderName());
					expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
					expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
					expiryDocumentDTO.setGroupName(groupTagDocumentInfo.getGroupName());
					expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
					expiryDocumentDTO.setShipName(expirydoc.getShipProfileInfo().getShipName());
					expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
					expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
					expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
					expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
					expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
					expiryDocumentDTO
							.setDocumentDownloadUrl(env.getProperty("expiryDocument.download.url") + expirydoc.getId());
					expiryDocumentDTO
							.setDocumentPreviewUrl(env.getProperty("expiryDocument.preview.url") + expirydoc.getId());
					expiryDocumentDTO.setFileExtension(expirydoc.getDocumentDataInfo().getDocumentFormat());
					if (expirydoc.getExpiryDate() != null){
						String colorCode = expiryDocumentUtility.getExpiryDocumentColor(expirydoc.getExpiryDate());
						expiryDocumentDTO.setStatusColor(colorCode);
					}else{
						expiryDocumentDTO.setStatusColor("green");
					}
					expirydocs.add(expiryDocumentDTO);
				}

			}
		}
		return expirydocs;
	}

	@Override
	public boolean isGroupExpiredocumentExits(Long groupId) {
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		if (groupTagDocumentInfo.getDocumentHolderInfo() != null
				&& groupTagDocumentInfo.getDocumentHolderInfo().size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String validateShipExists(Long shipId) {
		boolean shipIdExist = shipProfileRepository.exists(shipId);
		if (!shipIdExist) {
			return "Given ship id not exist";
		}
		return "Success";
	}

	@Override
	public List<GroupTagDTO> getAllGroupInfoByShip(GroupTagDTO groupTagDTO) {
		List<GroupTagDTO> groupTagDTOs = new ArrayList<GroupTagDTO>();
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(groupTagDTO.getShipId());
		if (shipProfileInfo != null) {
			List<GroupTagDocumentInfo> allGroup = groupTagDocumentRepository
					.findByShipProfileInfoAndUserProfileInfo(shipProfileInfo, userProfileInfo);

			for (GroupTagDocumentInfo groupTagDocumentInfo : allGroup) {
				groupTagDTO = new GroupTagDTO();
				groupTagDTO.setId(groupTagDocumentInfo.getId());
				groupTagDTO.setGroupName(groupTagDocumentInfo.getGroupName());
				groupTagDTO.setUserProfileId(groupTagDocumentInfo.getUserProfileInfo().getId());
				groupTagDTO.setKeyword(groupTagDocumentInfo.getKeyword());
				groupTagDTO.setEmailId(groupTagDocumentInfo.getEmailId());
				groupTagDTO.setUserProfileId(groupTagDocumentInfo.getUserProfileInfo().getId());
				groupTagDTO.setKeyword(groupTagDocumentInfo.getKeyword());
				// groupTagDTO.setShipId(groupTagDocumentInfo.getShipProfileInfo().getId());
				List<ExpiryDocumentDTO> groupExpiryDocumentList = getAllExpiredocumentinGroup(
						groupTagDocumentInfo.getId());
				if (groupExpiryDocumentList != null)
					groupTagDTO.setExpiryDocumentDtos(groupExpiryDocumentList);
				groupTagDTOs.add(groupTagDTO);
			}
			return groupTagDTOs;
		}
		return groupTagDTOs;
	}

	@Override
	public String verifyShareDocumentParam(GroupTagDTO groupTagDTO) {
		if (!(groupTagDTO.getId() != null && StringUtils.isNotBlank(groupTagDTO.getId().toString()))) {
			return env.getProperty("expiry.document.id.missing");
		}
		if (!(groupTagDTO.getEmailId() != null && StringUtils.isNotBlank(groupTagDTO.getEmailId().toString()))) {
			return env.getProperty("emailids.missing");
		}
		return env.getProperty("success");
	}

	@Override
	public String shareExpiryDocument(GroupTagDTO groupTagDTO) {
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupTagDTO.getId());
		try {
			if (groupTagDocumentInfo != null) {
				GroupTagDTO group = new GroupTagDTO();
				GroupSharedDocumentInfo groupshare = new GroupSharedDocumentInfo();
				group.setEmailId(groupTagDTO.getEmailId());
				group.setTitle(groupTagDTO.getTitle());
				group.setSubject(groupTagDTO.getSubject());
				group.setGroupName(groupTagDocumentInfo.getGroupName());
				group.setShipName(groupTagDocumentInfo.getShipProfileInfo().getShipName());
				group.setDocumentHolderInfos(groupTagDocumentInfo.getDocumentHolderInfo());
				Set<DocumentHolderInfo> documentHolderInfos = groupTagDocumentInfo.getDocumentHolderInfo();
				Set<DocumentHolderInfo> docHolderList = new HashSet<DocumentHolderInfo>();
				docHolderList.addAll(documentHolderInfos);
				groupshare.setToEmailId(groupTagDTO.getEmailId());
				groupshare.setTitle(groupTagDTO.getTitle());
				groupshare.setSubject(groupTagDTO.getSubject());
				groupshare.setGroupTagDocumentInfo(groupTagDocumentInfo);
				GroupSharedDocumentInfo groupSharedDocumentInfo = groupSharedDocumentRepository
						.saveAndFlush(groupshare);

				// if(documentHolderInfos!=null && groupSharedDocument.getId()!=null) {
				// for(DocumentHolderInfo documentHolderInfoObj: documentHolderInfos) {
				// groupSharedDocument.getDocumentHolderInfo().add(documentHolderInfoObj);
				// }
				// }
				// groupshare.setDocumentHolderInfo(documentHolderInfos);
				groupSharedDocumentInfo.setDocumentHolderInfo(docHolderList);
				// groupSharedDocument.getDocumentHolderInfo().addAll(documentHolderInfos);
				groupSharedDocumentInfo = groupSharedDocumentRepository.save(groupSharedDocumentInfo);
				// groupshare.setDocumentHolderInfo(documentHolderInfos);
				// groupshare.getDocumentHolderInfo().addAll(groupTagDocumentInfo.getDocumentHolderInfo());
				if (groupSharedDocumentInfo != null) {
					commonMethodsUtility.maintainHistory(groupSharedDocumentInfo.getId(),
							groupSharedDocumentInfo.getTitle(), "Group", env.getProperty("history.shared"),
							groupTagDTO.getLoginId());
				}
				ShipProfileInfo shipProfileInfo = groupTagDocumentInfo.getShipProfileInfo();
				Set<DocumentHolderInfo> documentHolderInfoList = groupTagDocumentInfo.getDocumentHolderInfo();
				List<ExpiryDocumentDTO> expiryDocumentDTOList = new ArrayList<ExpiryDocumentDTO>();
				if (documentHolderInfoList != null) {
					for (DocumentHolderInfo documentHolderInfo : documentHolderInfoList) {
						List<ExpiryDocumentInfo> expiryDocumentInfoList = expiryDocumentRepository
								.findByDocumentHolderInfoAndShipProfileInfoAndDocumentStatus(documentHolderInfo,
										shipProfileInfo, env.getProperty("document.status.approve"));
						if (expiryDocumentInfoList != null) {
							for (ExpiryDocumentInfo expiryDocumentInfo : expiryDocumentInfoList) {
								ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
								expiryDocumentDTO.setId(expiryDocumentInfo.getId());
								expiryDocumentDTO
										.setDocumentName(expiryDocumentInfo.getDocumentDataInfo().getDocumentName());
								expiryDocumentDTO
										.setFileExtension(expiryDocumentInfo.getDocumentDataInfo().getDocumentFormat());
								expiryDocumentDTOList.add(expiryDocumentDTO);
							}
						}
					}

				}
				group.setExpiryDocumentDtos(expiryDocumentDTOList);
				String emailResponse = emailService.shareGroupExpiryDocument(group);

				return env.getProperty("success");
			}
			return env.getProperty("faliure");
		} catch (Exception e) {
			e.printStackTrace();
			return env.getProperty("faliure");
		}
	}

	@Override
	public GroupTagDTO viewGroup(Long groupId) {
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		if (groupTagDocumentInfo != null) {
			GroupTagDTO group = new GroupTagDTO();
			group.setGroupName(groupTagDocumentInfo.getGroupName());
			group.setShipName(groupTagDocumentInfo.getShipProfileInfo().getShipName());
			group.setShipId(groupTagDocumentInfo.getShipProfileInfo().getId());
			group.setUserProfileId(groupTagDocumentInfo.getUserProfileInfo().getId());

			Set<DocumentHolderInfo> documentHolderList = groupTagDocumentInfo.getDocumentHolderInfo();
			ShipProfileInfo shipProfileInfo = groupTagDocumentInfo.getShipProfileInfo();
			List<ExpiryDocumentDTO> expirydocs = new ArrayList<ExpiryDocumentDTO>();
			for (DocumentHolderInfo documentHolderInfo : documentHolderList) {
				List<ExpiryDocumentInfo> expirydocList = expiryDocumentRepository
						.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
								documentHolderInfo, shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
								env.getProperty("active")); // ExpiryDocumentInfo expirydoc =
				// expiryDocumentRepository.findOne(expiryDocumentInfo.getId());
				for (ExpiryDocumentInfo expirydoc : expirydocList) {
					ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
					expiryDocumentDTO.setId(expirydoc.getId());
					expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
					expiryDocumentDTO.setDocumentHolderName(expirydoc.getDocumentHolderInfo().getDocumentHolderName());
					expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
					expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
					expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
					expiryDocumentDTO.setShipName(expirydoc.getShipProfileInfo().getShipName());
					expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
					expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
					expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
					expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
					expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
					expiryDocumentDTO.setRemarks(expirydoc.getRemarks());
					if (expirydoc.getExpiryDate() != null){
						String colorCode = expiryDocumentUtility.getExpiryDocumentColor(expirydoc.getExpiryDate());
						expiryDocumentDTO.setStatusColor(colorCode);
					}else{
						expiryDocumentDTO.setStatusColor("green");
					}
					expiryDocumentDTO
							.setDocumentDownloadUrl(env.getProperty("expiryDocument.download.url") + expirydoc.getId());
					expiryDocumentDTO
							.setDocumentPreviewUrl(env.getProperty("expiryDocument.preview.url") + expirydoc.getId());
					expirydocs.add(expiryDocumentDTO);
				}
				group.setExpiryDocumentDtos(expirydocs);
			}
			return group;
		}
		return null;
	}

	@Override
	public boolean isGroupForwardExpiredocumentExits(Long groupId) {
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		List<GroupSharedDocumentInfo> groupSharedDocumentInfoList = groupSharedDocumentRepository
				.findByGroupTagDocumentInfo(groupTagDocumentInfo);
		if (groupSharedDocumentInfoList != null && groupSharedDocumentInfoList.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<GroupTagDTO> getAllSenItemsInGroup(Long groupId) {
		List<ExpiryDocumentDTO> expirydocs = new ArrayList<ExpiryDocumentDTO>();
		List<GroupTagDTO> groupTagDTOs = new ArrayList<GroupTagDTO>();
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		List<GroupSharedDocumentInfo> groupSharedDocumentInfoList = groupSharedDocumentRepository
				.findByGroupTagDocumentInfo(groupTagDocumentInfo);
		for (GroupSharedDocumentInfo groupSharedDocumentInfoObj : groupSharedDocumentInfoList) {
			GroupTagDTO groupTagDTO = new GroupTagDTO();
			groupTagDTO.setGroupId(groupId);
			if (groupSharedDocumentInfoObj.getTitle() != null)
				groupTagDTO.setTitle(groupSharedDocumentInfoObj.getTitle());
			if (groupSharedDocumentInfoObj.getSubject() != null)
				groupTagDTO.setSubject(groupSharedDocumentInfoObj.getSubject());

			groupTagDTO.setEmailId(groupSharedDocumentInfoObj.getToEmailId());
			Set<DocumentHolderInfo> documentHolderList = groupTagDocumentInfo.getDocumentHolderInfo();
			ShipProfileInfo shipProfileInfo = groupTagDocumentInfo.getShipProfileInfo();
			for (DocumentHolderInfo documentHolderInfo : documentHolderList) {
				List<ExpiryDocumentInfo> expirydocList = expiryDocumentRepository
						.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
								documentHolderInfo, shipProfileInfo, 1, env.getProperty("document.status.approve"), 0,
								env.getProperty("active")); // ExpiryDocumentInfo expirydoc =
				// expiryDocumentRepository.findOne(expiryDocumentInfo.getId());
				for (ExpiryDocumentInfo expirydoc : expirydocList) {
					ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
					expiryDocumentDTO.setId(expirydoc.getId());
					expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
					expiryDocumentDTO.setDocumentHolderName(expirydoc.getDocumentHolderInfo().getDocumentHolderName());
					expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
					expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
					expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
					expiryDocumentDTO.setShipName(expirydoc.getShipProfileInfo().getShipName());
					expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
					expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
					expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
					expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
					expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
					expiryDocumentDTO.setRemarks(expirydoc.getRemarks());
					if (expirydoc.getExpiryDate() != null){
						String colorCode = expiryDocumentUtility.getExpiryDocumentColor(expirydoc.getExpiryDate());
						expiryDocumentDTO.setStatusColor(colorCode);
					}else{
						expiryDocumentDTO.setStatusColor("green");
					}
					expirydocs.add(expiryDocumentDTO);
				}
				groupTagDTO.setExpiryDocumentDtos(expirydocs);
			}
			groupTagDTOs.add(groupTagDTO);
		}
		return groupTagDTOs;
	}

	@Override
	public boolean isForwardExts(Long groupId) {
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		List<GroupSharedDocumentInfo> groupSharedDocumentInfoList = groupSharedDocumentRepository
				.findByGroupTagDocumentInfo(groupTagDocumentInfo);
		if (groupSharedDocumentInfoList != null && groupSharedDocumentInfoList.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<ExpiryDocumentDTO> checkGroupAlreadyExists(GroupTagDTO groupTagDTO) {
		ShipProfileInfo shipProfileInfo = shipProfileRepository.findById(groupTagDTO.getShipId());
		List<ExpiryDocumentDTO> expirydocs = new ArrayList<ExpiryDocumentDTO>();
		if (shipProfileInfo != null) {
			GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
					.findByShipProfileInfoAndEmailIdAndMode(shipProfileInfo, groupTagDTO.getEmailId(),
							groupTagDTO.getMode());
			if (groupTagDocumentInfo != null) {
				Set<DocumentHolderInfo> documentHolderList = groupTagDocumentInfo.getDocumentHolderInfo();
				if (shipProfileInfo != null && documentHolderList != null) {
					for (DocumentHolderInfo documentHolderInfo : documentHolderList) {
						List<ExpiryDocumentInfo> expirydocList = expiryDocumentRepository
								.findByDocumentHolderInfoAndShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(
										documentHolderInfo, shipProfileInfo, 1,
										env.getProperty("document.status.approve"), 0, env.getProperty("active"));
						// ExpiryDocumentInfo expirydoc =
						// expiryDocumentRepository.findOne(expiryDocumentInfo.getId());

						for (ExpiryDocumentInfo expirydoc : expirydocList) {
							ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
							expiryDocumentDTO.setId(expirydoc.getId());
							expiryDocumentDTO.setGroupId(groupTagDocumentInfo.getId());
							expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
							expiryDocumentDTO
									.setDocumentHolderName(expirydoc.getDocumentHolderInfo().getDocumentHolderName());
							expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
							expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
							expiryDocumentDTO.setGroupName(groupTagDocumentInfo.getGroupName());
							expiryDocumentDTO.setGroupId(groupTagDocumentInfo.getId());
							expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
							expiryDocumentDTO.setShipName(expirydoc.getShipProfileInfo().getShipName());
							expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
							expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
							expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
							expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
							expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
							expiryDocumentDTO.setDocumentDownloadUrl(
									env.getProperty("expiryDocument.download.url") + expirydoc.getId());
							if (expirydoc.getExpiryDate() != null){
								String colorCode = expiryDocumentUtility.getExpiryDocumentColor(expirydoc.getExpiryDate());
								expiryDocumentDTO.setStatusColor(colorCode);
							}else{
								expiryDocumentDTO.setStatusColor("green");
							}
							expirydocs.add(expiryDocumentDTO);
						}

					}
				}
			}
		}
		return expirydocs;
	}

	@Override
	public boolean updateGroupTagwithExp(GroupTagDTO groupTagDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		if (userProfileInfo != null) {
			GroupTagDocumentInfo groupTagDocumentInfoExits = groupTagDocumentRepository
					.findOne(groupTagDTO.getGroupId());
			Set<DocumentHolderInfo> documentHolderInfos = new HashSet<DocumentHolderInfo>();
			Set<DocumentHolderInfo> documentHolderInfosExist = groupTagDocumentInfoExits.getDocumentHolderInfo();
			if (groupTagDTO.getDocumentHolderIds() != null && groupTagDTO.getDocumentHolderIds().length > 0) {
				for (int i = 0; i < groupTagDTO.getDocumentHolderIds().length; i++) {
					DocumentHolderInfo documentHolderInfo = documentHolderRepository
							.findOne(groupTagDTO.getDocumentHolderIds()[i]);

// boolean exist = documentHolderInfosExist.contains(documentHolderInfo);
boolean exist = documentHolderInfosExist.stream().anyMatch(d -> d.getId().equals(documentHolderInfo.getId()));
					if (!exist) {
						documentHolderInfos.add(documentHolderInfo);
						System.out.println("documentHolderInfo" + documentHolderInfo.getId());
					}
				}
				groupTagDocumentInfoExits.getDocumentHolderInfo().addAll(documentHolderInfos);
				groupTagDocumentInfoExits = groupTagDocumentRepository.save(groupTagDocumentInfoExits);
				if (groupTagDocumentInfoExits != null) {
					commonMethodsUtility.maintainHistory(groupTagDocumentInfoExits.getId(),
							groupTagDocumentInfoExits.getGroupName(), "Group", env.getProperty("history.updated"),
							groupTagDTO.getLoginId());
				}
				return true;
			}

		}

		return false;
	}

	@Override
	public String validateSourceExistForAddExp(GroupTagDTO groupTagDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		if (userProfileInfo == null) {
			return env.getProperty("user.not.found");
		}

		for (Long shipId : groupTagDTO.getShipIds()) {
			ShipProfileInfo shipProfileInfo = shipProfileRepository.findOne(shipId);
			if (shipProfileInfo != null) {
				GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
						.findByGroupNameAndUserProfileInfoAndShipProfileInfo(groupTagDTO.getGroupName(),
								userProfileInfo, shipProfileInfo);
				if (groupTagDocumentInfo != null) {
					return "Group Name Already exist for ship" + " " + shipProfileInfo.getShipName();
				}
				/*
				 * GroupTagDocumentInfo groupTagDocumentInfo1 = groupTagDocumentRepository
				 * .findByUserProfileInfoAndShipProfileInfoAndEmailId(userProfileInfo,
				 * shipProfileInfo, groupTagDTO.getEmailId()); if (groupTagDocumentInfo1 !=
				 * null) { return "Group already exist for this email id"; }
				 */
			} else {
				return env.getProperty("ship.not.found");
			}
		}
		return env.getProperty("success");
	}

	@Override
	public String validateUpdateGroupParam(GroupTagDTO groupTagDTO) {
		UserProfileInfo userProfileInfo = userProfileRepository.findOne(groupTagDTO.getUserProfileId());
		if (userProfileInfo == null) {
			return env.getProperty("user.not.found");
		}
		GroupTagDocumentInfo groupTagDocumentInfoExits = groupTagDocumentRepository.findOne(groupTagDTO.getId());
		if (groupTagDocumentInfoExits != null) {
			GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository
					.findByGroupNameAndUserProfileInfoAndShipProfileInfo(groupTagDTO.getGroupName(), userProfileInfo,
							groupTagDocumentInfoExits.getShipProfileInfo());
			if (groupTagDocumentInfo != null) {
				return "Group Name Already exist for ship" + " "
						+ groupTagDocumentInfoExits.getShipProfileInfo().getShipName();
			}
		}

		return env.getProperty("success");
	}

	@Override
	public GroupTagDTO remainingDocument(Long groupId) {
		GroupTagDocumentInfo groupTagDocumentInfo = groupTagDocumentRepository.findOne(groupId);
		if (groupTagDocumentInfo != null) {
			GroupTagDTO group = new GroupTagDTO();
			group.setGroupName(groupTagDocumentInfo.getGroupName());
			group.setShipName(groupTagDocumentInfo.getShipProfileInfo().getShipName());
			// group.setShipId(groupTagDocumentInfo.getShipProfileInfo().getId());
			// group.setUserProfileId(groupTagDocumentInfo.getUserProfileInfo().getId());

			Set<DocumentHolderInfo> documentHolderList = groupTagDocumentInfo.getDocumentHolderInfo();
			ShipProfileInfo shipProfileInfo = groupTagDocumentInfo.getShipProfileInfo();
			List<ExpiryDocumentDTO> remainingdocumentHolderList = new ArrayList<ExpiryDocumentDTO>();
			List<ExpiryDocumentInfo> exitingdocumentHolderList = expiryDocumentRepository
					.findByShipProfileInfoAndCurrentVersionAndDocumentStatusAndArchiveStatusAndStatus(shipProfileInfo,
							1, env.getProperty("document.status.approve"), 0, env.getProperty("active"));

			for (ExpiryDocumentInfo expirydoc : exitingdocumentHolderList) {
				if (documentHolderList != null && documentHolderList.size() > 0) {
					for (DocumentHolderInfo doc : documentHolderList) {
						System.err.println("DocumentHolderInfo1::::" + expirydoc.getDocumentHolderInfo().getId());
						System.err.println("DocumentHolderInfo2::::" + doc.getId());
						if (expirydoc.getDocumentHolderInfo().getId() != doc.getId()) {
							ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
							expiryDocumentDTO.setId(expirydoc.getId());
							expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
							expiryDocumentDTO
									.setDocumentHolderName(expirydoc.getDocumentHolderInfo().getDocumentHolderName());
							expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
							expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
							expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
							expiryDocumentDTO.setShipName(expirydoc.getShipProfileInfo().getShipName());
							expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
							expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
							expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
							expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
							expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
							expiryDocumentDTO.setRemarks(expirydoc.getRemarks());
							String colorCode = expiryDocumentUtility.getExpiryDocumentColor(expirydoc.getExpiryDate());
							expiryDocumentDTO.setStatusColor(colorCode);

							expiryDocumentDTO.setDocumentDownloadUrl(
									env.getProperty("expiryDocument.download.url") + expirydoc.getId());
							expiryDocumentDTO.setDocumentPreviewUrl(
									env.getProperty("expiryDocument.preview.url") + expirydoc.getId());
							remainingdocumentHolderList.add(expiryDocumentDTO);
						}
						group.setRemainingExpiryDocumentList(remainingdocumentHolderList);
					}
					return group;
				} else {
					ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
					expiryDocumentDTO.setId(expirydoc.getId());
					expiryDocumentDTO.setDocumentName(expirydoc.getDocumentName());
					expiryDocumentDTO.setDocumentHolderName(expirydoc.getDocumentHolderInfo().getDocumentHolderName());
					expiryDocumentDTO.setDocumentHolderId(expirydoc.getDocumentHolderInfo().getId());
					expiryDocumentDTO.setDocumentDataId(expirydoc.getDocumentDataInfo().getId());
					expiryDocumentDTO.setCertificateNumber(expirydoc.getCertificateNumber());
					expiryDocumentDTO.setShipName(expirydoc.getShipProfileInfo().getShipName());
					expiryDocumentDTO.setPlaceOfIssue(expirydoc.getPlaceOfIssue());
					expiryDocumentDTO.setIssueDate(expirydoc.getIssueDate());
					expiryDocumentDTO.setExpiryDate(expirydoc.getExpiryDate());
					expiryDocumentDTO.setLastAnnual(expirydoc.getLastAnnual());
					expiryDocumentDTO.setNextAnnual(expirydoc.getNextAnnual());
					expiryDocumentDTO.setRemarks(expirydoc.getRemarks());
					if (expirydoc.getExpiryDate() != null){
						String colorCode = expiryDocumentUtility.getExpiryDocumentColor(expirydoc.getExpiryDate());
						expiryDocumentDTO.setStatusColor(colorCode);
					}else{
						expiryDocumentDTO.setStatusColor("green");
					}

					expiryDocumentDTO
							.setDocumentDownloadUrl(env.getProperty("expiryDocument.download.url") + expirydoc.getId());
					expiryDocumentDTO
							.setDocumentPreviewUrl(env.getProperty("expiryDocument.preview.url") + expirydoc.getId());
					remainingdocumentHolderList.add(expiryDocumentDTO);
				}
				group.setRemainingExpiryDocumentList(remainingdocumentHolderList);
			}
			return group;
		}

		return null;
	}
}
