package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.model.ShipProfileInfo;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpiryDocumentService {

    public String saveDocumentDetails(ExpiryDocumentDTO expiryDocumentDTO, MultipartFile scanFile);

    public List<ExpiryDocumentDTO> getAllExpiryDocumentAndHolderInfo(Long shipId, Integer archivedStatus);

    public ExpiryDocumentDTO downloadFileBasedOnDocumentDataId(Long documentDataId);

    public ExpiryDocumentDTO downloadFileBasedOnExpiryId(Long id);

    public List<ExpiryDocumentDTO> getDocumentHolderHistory(ExpiryDocumentDTO expiryDocumentDTO);

    public String saveDraftDetails(ExpiryDocumentDTO expiryDocumentDTO, MultipartFile scanFile);

    // public ExpiryDocumentDTO scanDocument(MultipartFile scanFile);

    public String archiveDocumentHolderInfo(ExpiryDocumentDTO expiryDocumentDTO);

    public String unArchiveDocumentHolderInfo(ExpiryDocumentDTO expiryDocumentDTO);

    public String archiveAndUnArchiveDocumentHolderInfo(ExpiryDocumentDTO expiryDocumentDTO);

    public String checkExpiryDocumentsExists(ExpiryDocumentDTO expiryDocumentDTO);

    public String checkExpiryDocumentIdExits(ExpiryDocumentDTO expiryDocumentDTO);

    public String shareExpiryDocument(ExpiryDocumentDTO expiryDocumentDTO);

    public String checkCustomFolderNameAlreadyExists(ExpiryDocumentDTO expiryDocumentDTO);

    public String saveCustomDocumentFolder(ExpiryDocumentDTO expiryDocumentDTO);

    public List<ExpiryDocumentDTO> getPendingExpiryDocumentList(ExpiryDocumentDTO expiryDocumentDTO);

    public List<ExpiryDocumentDTO> getExpiryDocumentInfosByDocumentStatus(ExpiryDocumentDTO expiryDocumentDTO);

    public boolean isSuperAdminId(ExpiryDocumentDTO expiryDocumentDTO);

    public boolean isExistOrganization(ExpiryDocumentDTO expiryDocumentDTO);

    public boolean changeStatus(ExpiryDocumentDTO expiryDocumentDTO);

    public boolean isExistLoginId(ExpiryDocumentDTO expiryDocumentDTO);
    public List<ExpiryDocumentDTO> getExpiryDocumentList(ExpiryDocumentDTO expiryDocumentDTO);

    public List<ExpiryDocumentDTO> getExpiryTypeDocumentList(Long shipId);

    public ShipProfileInfo findShipProfileInfo(Long shipId);

	public ExpiryDocumentInfo updateDocument(ExpiryDocumentDTO expiryDocumentDTO);
}
