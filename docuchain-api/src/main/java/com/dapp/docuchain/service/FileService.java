package com.dapp.docuchain.service;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.dapp.docuchain.dto.VesselDocumentDTO;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

    public String fileRetriveFromStorej(String fileName, String fileHashCode);

    public ExpiryDocumentDTO scanImageFile(MultipartFile file);

    public String uploadFile(MultipartFile file, Long saveInBlockchain);

    boolean fileWriteIntoLocation(MultipartFile file, String oldPath, String newPath, String fileName);

	public String shipProfileUpload(ShipProfileDTO shipProfileDTO, MultipartFile shipPic);

	public String uploadUserPictureAndSaveFileFromLocal(MultipartFile multipartFile, String fileName);

    String organizationImageUpload(Long userId, MultipartFile shipPic);
    // Inside your existing FileService interface, add this:
VesselDocumentDTO scanVesselImageFile(MultipartFile file);


}
