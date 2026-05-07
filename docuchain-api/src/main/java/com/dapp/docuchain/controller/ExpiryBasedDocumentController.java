package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.HistoryDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.model.ExpiryDocumentInfo;
import com.dapp.docuchain.service.ExpiryDocumentService;
import com.dapp.docuchain.service.FileService;
import com.dapp.docuchain.service.ShipProfileService;
import com.dapp.docuchain.utility.ExpiryDocumentUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/docuchain/api/expiry/document")
@Api(value = "ExpiryBasedDcoumentController", description = "Expiry Based Document Controller API")
@CrossOrigin
public class ExpiryBasedDocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpiryBasedDocumentController.class);
    @Autowired
    FileService fileService;
    @Autowired
    ExpiryDocumentService expiryDocumentService;
    @Autowired
    ExpiryDocumentUtility expiryDocumentUtility;
    @Autowired
    ShipProfileService shipProfileService;
    @Autowired
    private Environment env;

    @CrossOrigin
    @PostMapping(value = "/scan", produces = {
            "application/json"})
    @ApiOperation(value = "upload scanning document", notes = "scan document and read values")
    public ResponseEntity<String> documentScan(
            @ApiParam(value = "Required file attachment", required = true) @RequestParam(name = "scanFile", value = "scanFile", required = true) MultipartFile scanFile,
            HttpServletRequest request, HttpServletResponse response) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
            LOGGER.info("Scan File is Empty --->" + scanFile.getOriginalFilename());
            if (scanFile.isEmpty()) {
                LOGGER.info("Scan File is Empty --->" + scanFile.getOriginalFilename());
                statusResponseDTO.setMessage(env.getProperty("file.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            String imageType = Files.getFileExtension(scanFile.getOriginalFilename());
            LOGGER.info("File extension" + imageType);
        //     if (!scanFile.isEmpty()) {
        //         //ExpiryDocumentDTO expiryDocumentDTO = fileService.scanImageFile(scanFile);
        //     	// ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
        //         ExpiryDocumentDTO expiryDocumentDTO =
        // expiryDocumentService.scanDocument(scanFile);

        //         statusResponseDTO.setExpiryDocumentDTOs(expiryDocumentDTO);
        //         statusResponseDTO.setStatus(env.getProperty("success"));
        //         return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        //     }
        if (!scanFile.isEmpty()) {

    ExpiryDocumentDTO expiryDocumentDTO =
            fileService.scanImageFile(scanFile);

    statusResponseDTO.setExpiryDocumentDTOs(expiryDocumentDTO);
    statusResponseDTO.setStatus(env.getProperty("success"));

    return new ResponseEntity<>(
            new Gson().toJson(statusResponseDTO),
            HttpStatus.OK);
}


            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Problem in documentScan  : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }

    }

//     @CrossOrigin
//     @PostMapping(value = "/add", produces = {"application/json"})
//     @ApiOperation(value = "add dcoument", notes = "add document and the details")
//     public ResponseEntity<String> documentAdd(
//             @ApiParam(value = "Required document details", required = true) @RequestParam(name = "ExpiryDocumentInfo", value = "ExpiryDocumentInfo", required = true) String expiryDocumentDTOStr,
//             @ApiParam(value = "Required file attachment", required = true) @RequestParam(name = "scanFile", value = "scanFile", required = true) MultipartFile scanFile,
//             HttpServletRequest request, HttpServletResponse response) {
//         StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
//         statusResponseDTO.setStatus("failure");
//         try {
//             ObjectMapper mapper = new ObjectMapper();
//             ExpiryDocumentDTO expiryDocumentDTO = null;
//             try {
//                 expiryDocumentDTO = mapper.readValue(expiryDocumentDTOStr, ExpiryDocumentDTO.class);
//             } catch (Exception e) {
//                 LOGGER.error("error" + e);
//                 statusResponseDTO.setStatus(env.getProperty("failure"));
//                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
//             }
//             if (scanFile == null) {
//                 statusResponseDTO.setMessage(env.getProperty("file.not.exists"));
//                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
//             }
//             String fileResponse = expiryDocumentService.saveDocumentDetails(expiryDocumentDTO, scanFile);
//             if (fileResponse.equalsIgnoreCase(env.getProperty("success"))) {
//                 statusResponseDTO.setStatus(env.getProperty("success"));
//                 statusResponseDTO.setMessage(env.getProperty("document.upload.success"));
//                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
//             }
//             statusResponseDTO.setMessage(env.getProperty("document.upload.failed"));
//             return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//         } catch (Exception e) {
//             e.printStackTrace();
//             LOGGER.error("Problem in documentScan  : ", e);
//             statusResponseDTO.setMessage(env.getProperty("server.problem"));
//             return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//         }

//     }

// @CrossOrigin
// @PostMapping(value = "/save-draft", produces = {"application/json"})
// @ApiOperation(value = "save document as draft in separate table")
// public ResponseEntity<String> saveDraft(
//         @RequestParam(name = "ExpiryDocumentInfo") String expiryDocumentDTOStr,
//         @RequestParam(name = "scanFile") MultipartFile scanFile) {

//     StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
//     try {
//         ObjectMapper mapper = new ObjectMapper();
//         ExpiryDocumentDTO expiryDocumentDTO = mapper.readValue(expiryDocumentDTOStr, ExpiryDocumentDTO.class);

//         // Call the NEW draft-specific service method
//         String fileResponse = expiryDocumentService.saveDraftDetails(expiryDocumentDTO, scanFile);

//         if (fileResponse.equalsIgnoreCase(env.getProperty("success"))) {
//             statusResponseDTO.setStatus("success");
//             statusResponseDTO.setMessage("Draft saved successfully");
//             return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
//         }

//         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//     } catch (Exception e) {
//         return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
//     }
// }

@CrossOrigin
    @PostMapping(value = "/add", produces = {"application/json"})
    @ApiOperation(value = "add dcoument", notes = "add document and the details")
    public ResponseEntity<String> documentAdd(
            @ApiParam(value = "Required document details", required = true) @RequestParam(name = "ExpiryDocumentInfo", value = "ExpiryDocumentInfo", required = true) String expiryDocumentDTOStr,
            @ApiParam(value = "Required file attachment", required = false) @RequestParam(name = "scanFile", value = "scanFile", required = false) MultipartFile scanFile,
            HttpServletRequest request, HttpServletResponse response) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
            ObjectMapper mapper = new ObjectMapper();
            ExpiryDocumentDTO expiryDocumentDTO = null;
            try {
                expiryDocumentDTO = mapper.readValue(expiryDocumentDTOStr, ExpiryDocumentDTO.class);
            } catch (Exception e) {
                LOGGER.error("error" + e);
                statusResponseDTO.setStatus(env.getProperty("failure"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }

            // Allow null scanFile ONLY if we are resuming a draft (documentDataId is already present)
            if (scanFile == null && expiryDocumentDTO.getDocumentDataId() == null) {
                statusResponseDTO.setMessage(env.getProperty("file.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }

            String fileResponse = expiryDocumentService.saveDocumentDetails(expiryDocumentDTO, scanFile);
            if (fileResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("document.upload.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setMessage(env.getProperty("document.upload.failed"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Problem in documentScan  : ", e);
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }

    }


    @CrossOrigin
@PostMapping(value = "/save-draft", produces = {"application/json"})
@ApiOperation(value = "save document as draft in separate table")
public ResponseEntity<String> saveDraft(
        @RequestParam(name = "ExpiryDocumentInfo") String expiryDocumentDTOStr,
        @RequestParam(name = "scanFile", required = false) MultipartFile scanFile) {

    StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
    try {
        ObjectMapper mapper = new ObjectMapper();
        ExpiryDocumentDTO expiryDocumentDTO = mapper.readValue(expiryDocumentDTOStr, ExpiryDocumentDTO.class);

        // Call the NEW draft-specific service method
        String fileResponse = expiryDocumentService.saveDraftDetails(expiryDocumentDTO, scanFile);

        if (fileResponse.equalsIgnoreCase(env.getProperty("success"))) {
            statusResponseDTO.setStatus("success");
            statusResponseDTO.setMessage("Draft saved successfully");
            return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT);
    }
}


@CrossOrigin
@RequestMapping(value = "/preview/draft/{documentDataId}", method = RequestMethod.GET)
public ResponseEntity<InputStreamResource> downloadDraftPreview(@ApiParam(value = "Required documentDataId ", required = true) @PathVariable(value = "documentDataId") Long documentDataId) throws IOException {
    System.out.println("Calling Draft Preview Download:- " + documentDataId);
    ExpiryDocumentDTO expiryDocumentDTO = expiryDocumentService.downloadFileBasedOnDocumentDataId(documentDataId);

    if (expiryDocumentDTO == null || expiryDocumentDTO.getFileArray() == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    ByteArrayResource resource = new ByteArrayResource(expiryDocumentDTO.getFileArray());
    HttpHeaders headers = new HttpHeaders();

    if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("jpg")||expiryDocumentDTO.getFileExtension().equalsIgnoreCase("jpeg")) {
        headers.setContentType(MediaType.parseMediaType("image/jpg"));
    } else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("gif")) {
        headers.setContentType(MediaType.parseMediaType("image/gif"));
    } else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("png")) {
        headers.setContentType(MediaType.parseMediaType("image/png"));
    } else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("pdf")) {
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
    } else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("html") ) {
        headers.setContentType(MediaType.parseMediaType("text/html"));
    } else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("zip") ) {
        headers.setContentType(MediaType.parseMediaType("application/zip"));
    }

    headers.add("Access-Control-Allow-Origin", "*");
    headers.add("Access-Control-Allow-Headers", "Content-Type");
    headers.add("Content-Disposition", "filename=" +  URLEncoder.encode(expiryDocumentDTO.getDocumentName(), "UTF-8")+ "." + expiryDocumentDTO.getFileExtension() );
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    headers.setContentLength(resource.contentLength());
    return new ResponseEntity<InputStreamResource>(new InputStreamResource(resource.getInputStream()), headers, HttpStatus.OK);
}




    @CrossOrigin
    @PostMapping(value = "/update", produces = {"application/json"})
    @ApiOperation(value = "update dcoument", notes = "add document and the details")
    public ResponseEntity<String> documentUpdate(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
        	  String verfiyParamResponse = expiryDocumentUtility.verifyUpdateParamResponse(expiryDocumentDTO);
              if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
                  statusResponseDTO.setStatus(env.getProperty("failure"));
                  statusResponseDTO.setMessage(verfiyParamResponse);
                  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
              }
//              String dataExistsResponse = expiryDocumentService.checkExpiryDocumentsExists(expiryDocumentDTO);
//              if (!dataExistsResponse.equalsIgnoreCase(env.getProperty("success"))) {
//                  statusResponseDTO.setMessage(dataExistsResponse);
//                  return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//              }
              ExpiryDocumentInfo fileResponse = expiryDocumentService.updateDocument(expiryDocumentDTO);
            if (fileResponse!=null) {

                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("document.upload.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setMessage(env.getProperty("document.upload.failed"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Problem in documentScan  : ", e);
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }

    }

    @CrossOrigin
    @GetMapping(value = "/holder/all/{shipId}/{archivedStatus}", produces = {
            "application/json"})
    @ApiOperation(value = "Get all expiry document list", notes = "Get all document expiry and holder list")
    public ResponseEntity<String> getAllDocumentHolderAndExpiryList(@ApiParam(value = "Required shipId ", required = true) @PathVariable(value = "shipId") Long shipId,
                                                                    @ApiParam(value = "Required archivedStatus ", required = true) @PathVariable(value = "archivedStatus") Integer archivedStatus) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        List<ExpiryDocumentDTO> expiryDocumentList = null;
        try {
            expiryDocumentList = expiryDocumentService.getAllExpiryDocumentAndHolderInfo(shipId, archivedStatus);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("expirydocumentholder.list"));
            statusResponseDTO.setExpiryDocumentList(expiryDocumentList);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info("problem in getAllDocumentHolderAndExpiryList method in ExpiryDocumentController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }

    }

    @CrossOrigin
    @GetMapping(value = "/holder/all/{shipId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "GET EXPIRY TYPE DOCUMENT LIST", notes = "This method fetch if upload document is already there in database")
    public ResponseEntity<String> getExpiryTypeDocumentList(@ApiParam(value = "Required shipId ", required = true) @PathVariable(value = "shipId", required = true) Long shipId){
    	StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
    	 try {
    		 if(shipId != null){
    			 if(expiryDocumentService.findShipProfileInfo(shipId) != null){
    				 statusResponseDTO.setStatus(env.getProperty("success"));
    		         statusResponseDTO.setMessage(env.getProperty("expirydocumentholder.list"));
		             statusResponseDTO.setExpiryDocumentList(expiryDocumentService.getExpiryTypeDocumentList(shipId));
		             return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
    			 }else {
    				 statusResponseDTO.setStatus(env.getProperty("failure"));
    				 statusResponseDTO.setMessage(env.getProperty("ship.not.found"));
    				 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    			 }
    		 }else {
    			 statusResponseDTO.setStatus(env.getProperty("failure"));
				 statusResponseDTO.setMessage(env.getProperty("ship.id.null"));
				 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    		 }
         } catch (Exception e) {
             e.printStackTrace();
             statusResponseDTO.setStatus(env.getProperty("failure"));
             statusResponseDTO.setMessage(env.getProperty("server.problem"));
             return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
         }

    }

    // @CrossOrigin
    // @GetMapping(value = "/file/download/{expiryDocumentId}", produces = {
    //         "application/json"})
    // @ApiOperation(value = "File download", notes = "Get file based on expiry id")
    // public ResponseEntity<Resource> downloadExpiryDocumentFile(@ApiParam(value = "Required expiryDocumentId ", required = true) @PathVariable(value = "expiryDocumentId") Long expiryDocumentId) throws IOException {
    //     StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
    //     statusResponseDTO.setStatus("failure");
    //     LOGGER.info("port::" + env.getProperty("local.server.port"));
    //     LOGGER.info("port::" + env.getProperty("local.server.address"));
    //     ExpiryDocumentDTO expiryDocumentDTO = expiryDocumentService.downloadFileBasedOnExpiryId(expiryDocumentId);
    //     ByteArrayResource resource = new ByteArrayResource(expiryDocumentDTO.getFileArray());
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    //     headers.add("Pragma", "no-cache");
    //     headers.add("Expires", "0");
    //     headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(expiryDocumentDTO.getDocumentName(), "UTF-8") + "." + expiryDocumentDTO.getFileExtension());
    //     return ResponseEntity.ok()
    //             .headers(headers)
    //             .contentType(MediaType.parseMediaType("application/octet-stream"))
    //             .body(resource);


    // }
    @CrossOrigin
    @GetMapping(value = "/file/download/{expiryDocumentId}", produces = {"application/json"})
    @ApiOperation(value = "File download", notes = "Get file based on expiry id")
    public ResponseEntity<Resource> downloadExpiryDocumentFile(@ApiParam(value = "Required expiryDocumentId ", required = true) @PathVariable(value = "expiryDocumentId") Long expiryDocumentId) throws IOException {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        ExpiryDocumentDTO expiryDocumentDTO = expiryDocumentService.downloadFileBasedOnExpiryId(expiryDocumentId);

        if (expiryDocumentDTO == null || expiryDocumentDTO.getFileArray() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(expiryDocumentDTO.getFileArray());
        HttpHeaders headers = new HttpHeaders();

        // FIX: Safe Filename Check to prevent NullPointerException
        String safeFileName = expiryDocumentDTO.getDocumentName();
        if (safeFileName == null || safeFileName.trim().isEmpty()) {
            safeFileName = "Document_" + expiryDocumentId; // Fallback name
        }
        String safeExt = expiryDocumentDTO.getFileExtension();
        if (safeExt == null || safeExt.isEmpty()) safeExt = "pdf";

        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(safeFileName, "UTF-8") + "." + safeExt);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    // @CrossOrigin
    // @RequestMapping(value = "/preview/{expiryDocumentId}", method = RequestMethod.GET)
    // public ResponseEntity<InputStreamResource> download(@ApiParam(value = "Required expiryDocumentId ", required = true) @PathVariable(value = "expiryDocumentId") Long expiryDocumentId) throws IOException {
    //  System.out.println("Calling Download:- " + expiryDocumentId);
    //  ExpiryDocumentDTO expiryDocumentDTO = expiryDocumentService.downloadFileBasedOnExpiryId(expiryDocumentId);
    //  ByteArrayResource resource = new ByteArrayResource(expiryDocumentDTO.getFileArray());
    //  HttpHeaders headers = new HttpHeaders();
    //  //headers.setContentType(MediaType.parseMediaType("application/pdf"));
    //  if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("jpg")||expiryDocumentDTO.getFileExtension().equalsIgnoreCase("jpeg")) {
    // 	 headers.setContentType(MediaType.parseMediaType("image/jpg"));
    // 	} else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("gif")) {
    // 		 headers.setContentType(MediaType.parseMediaType("image/gif"));
    // 	}else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("png")) {
   	// 	 	headers.setContentType(MediaType.parseMediaType("image/png"));
    // 	}else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("pdf")) {
    // 		 headers.setContentType(MediaType.parseMediaType("application/pdf"));
    // 	} else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("html") ) {
    // 		 headers.setContentType(MediaType.parseMediaType("text/html"));
    // 	} else if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("zip") ) {
    // 		 headers.setContentType(MediaType.parseMediaType("application/zip"));
    // 	}
    //  headers.add("Access-Control-Allow-Origin", "*");
    //  headers.add("Access-Control-Allow-Headers", "Content-Type");
    //  headers.add("Content-Disposition", "filename=" +  URLEncoder.encode(expiryDocumentDTO.getDocumentName(), "UTF-8")+ "." + expiryDocumentDTO.getFileExtension() );
    //  headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    //  headers.add("Pragma", "no-cache");
    //  headers.add("Expires", "0");

    //  headers.setContentLength(resource.contentLength());
    //  ResponseEntity<InputStreamResource> response = new ResponseEntity<InputStreamResource>(
    //    new InputStreamResource(resource.getInputStream()), headers, HttpStatus.OK);
    //  return response;
    // }
    @CrossOrigin
    @RequestMapping(value = "/preview/{expiryDocumentId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@ApiParam(value = "Required expiryDocumentId ", required = true) @PathVariable(value = "expiryDocumentId") Long expiryDocumentId) throws IOException {
        System.out.println("Calling Download:- " + expiryDocumentId);
        ExpiryDocumentDTO expiryDocumentDTO = expiryDocumentService.downloadFileBasedOnExpiryId(expiryDocumentId);

        if (expiryDocumentDTO == null || expiryDocumentDTO.getFileArray() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(expiryDocumentDTO.getFileArray());
        HttpHeaders headers = new HttpHeaders();

        // FIX: Safe Filename Check to prevent NullPointerException
        String safeFileName = expiryDocumentDTO.getDocumentName();
        if (safeFileName == null || safeFileName.trim().isEmpty()) {
            safeFileName = "Document_" + expiryDocumentId; // Fallback name
        }
        String safeExt = expiryDocumentDTO.getFileExtension();
        if (safeExt == null || safeExt.isEmpty()) safeExt = "pdf";

        if (safeExt.equalsIgnoreCase("jpg")||safeExt.equalsIgnoreCase("jpeg")) {
            headers.setContentType(MediaType.parseMediaType("image/jpg"));
        } else if (safeExt.equalsIgnoreCase("gif")) {
            headers.setContentType(MediaType.parseMediaType("image/gif"));
        } else if (safeExt.equalsIgnoreCase("png")) {
            headers.setContentType(MediaType.parseMediaType("image/png"));
        } else if (safeExt.equalsIgnoreCase("pdf")) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
        } else if (safeExt.equalsIgnoreCase("html") ) {
            headers.setContentType(MediaType.parseMediaType("text/html"));
        } else if (safeExt.equalsIgnoreCase("zip") ) {
            headers.setContentType(MediaType.parseMediaType("application/zip"));
        }

        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "filename=" + URLEncoder.encode(safeFileName, "UTF-8") + "." + safeExt);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        headers.setContentLength(resource.contentLength());
        return new ResponseEntity<InputStreamResource>(new InputStreamResource(resource.getInputStream()), headers, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/histroy", produces = {
            "application/json"})
    @ApiOperation(value = "Get history of document holder", notes = "Get particular document holder history")
    public ResponseEntity<String> getHistoryByDocumentHolder(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        List<ExpiryDocumentDTO> expiryDocumentList = null;
        try {
            String verfiyParamResponse = expiryDocumentUtility.verifyParamResponse(expiryDocumentDTO);
            if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            expiryDocumentList = expiryDocumentService.getDocumentHolderHistory(expiryDocumentDTO);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("expiry.document.history.success"));
            statusResponseDTO.setExpiryDocumentList(expiryDocumentList);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info("problem in getHistoryByDocumentHolder method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }

    }

    @CrossOrigin
    @PostMapping(value = "/archive", produces = {
            "application/json"})
    @ApiOperation(value = "Archive dcoument holder", notes = "Archive document holder details")
    public ResponseEntity<String> archiveDocumentHolder(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
            String verfiyParamResponse = expiryDocumentUtility.verifyParamResponse(expiryDocumentDTO);
            if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(verfiyParamResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            String dataExistsResponse = expiryDocumentService.checkExpiryDocumentsExists(expiryDocumentDTO);
            if (!dataExistsResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(dataExistsResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            String archiveReponse = expiryDocumentService.archiveAndUnArchiveDocumentHolderInfo(expiryDocumentDTO);
            if (!archiveReponse.equalsIgnoreCase(env.getProperty("failure"))) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(archiveReponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setMessage(env.getProperty("archive.document.failed"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            LOGGER.info("problem in archiveDocumentHolder method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }


    }

    @CrossOrigin
    @PostMapping(value = "/share", produces = {
            "application/json"})
    @ApiOperation(value = "Share document", notes = "Share expiry document to email")
    public ResponseEntity<String> shareExpiryDocument(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
            String verfiyParamResponse = expiryDocumentUtility.verifyShareDocumentParam(expiryDocumentDTO);
            if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(verfiyParamResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            String validateResponse = expiryDocumentService.checkExpiryDocumentIdExits(expiryDocumentDTO);
            if (!validateResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(validateResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String shareResponse = expiryDocumentService.shareExpiryDocument(expiryDocumentDTO);
            if (shareResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("share.document.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setMessage(env.getProperty("share.document.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.info("problem in shareExpiryDocument method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }

    }

    @CrossOrigin
    @PostMapping(value = "/custom/folder", produces = {
            "application/json"})
    @ApiOperation(value = "Create custom folder", notes = "Api to create custom document folder")
    public ResponseEntity<String> customDocumentFolder(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
            String validateResponse = expiryDocumentUtility.validateCreateFolderParam(expiryDocumentDTO);
            if (!validateResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(validateResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            String verifyShipProfileResponse = shipProfileService.verifiyShipProfileExists(expiryDocumentDTO.getShipProfileId());
            if (!verifyShipProfileResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(env.getProperty("shipprofile.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String folderNameExists = expiryDocumentService.checkCustomFolderNameAlreadyExists(expiryDocumentDTO);
            if (!folderNameExists.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(folderNameExists);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String saveResponse = expiryDocumentService.saveCustomDocumentFolder(expiryDocumentDTO);
            if (saveResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("custom.document.holder.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setMessage(env.getProperty("custom.document.holder.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.info("problem in shareExpiryDocument method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/pending", produces = {
            "application/json"})
    @ApiOperation(value = "Get pending document", notes = "Api for reterive pending document information")
    public ResponseEntity<String> pendingDcouments(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        List<ExpiryDocumentDTO> expiryDocumentDTOList;
        try {
            String checkPendingResponse = expiryDocumentUtility.checkShipProfileAndUserIdExsits(expiryDocumentDTO);
            if (!checkPendingResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(checkPendingResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
           /* String verifyShipProfileResponse = shipProfileService.verifiyShipProfileExists(expiryDocumentDTO.getShipProfileId());
            if (!verifyShipProfileResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(env.getProperty("shipprofile.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }*/
            expiryDocumentDTOList = expiryDocumentService.getPendingExpiryDocumentList(expiryDocumentDTO);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("expirydocumentholder.list"));
            statusResponseDTO.setExpiryDocumentList(expiryDocumentDTOList);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info("problem in pendingDcouments method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/approved/status", produces = {
            "application/json"})
    @ApiOperation(value = "Get by document status", notes = "Api for reterive document information by document status")
    public ResponseEntity<String> getExpiryDocumentListByStatus(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        List<ExpiryDocumentDTO> expiryDocumentDTOList;
        try {
            String checkPendingResponse = expiryDocumentUtility.checkShipProfileAndUserIdExsits(expiryDocumentDTO);
            if (!checkPendingResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(checkPendingResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            String verifyShipProfileResponse = shipProfileService.verifiyShipProfileExists(expiryDocumentDTO.getShipProfileId());
            if (!verifyShipProfileResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(env.getProperty("shipprofile.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            expiryDocumentDTOList = expiryDocumentService.getExpiryDocumentInfosByDocumentStatus(expiryDocumentDTO);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("expirydocumentholder.list"));
            statusResponseDTO.setExpiryDocumentList(expiryDocumentDTOList);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info("problem in getExpiryDocumentListByStatus method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/dualapproval/changeflag", produces = {
            "application/json"})
    @ApiOperation(value = "Change flag in dual approval", notes = "Api for Change flag in dual approval")
    public ResponseEntity<String> changeFlagByAdmin(
            @ApiParam(value = "ChangeFlag", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
        	ExpiryDocumentUtility expiryDocumentUtility =new ExpiryDocumentUtility();
        	boolean validationStatus=expiryDocumentUtility.dualApprovalValidation(expiryDocumentDTO);
        	if(validationStatus!=true){
        		 statusResponseDTO.setMessage(env.getProperty("dualapproval.validation"));
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        	}
            boolean isAdminId=expiryDocumentService.isSuperAdminId(expiryDocumentDTO);
            if(isAdminId!=true){
            	 statusResponseDTO.setMessage(env.getProperty("history.notadmin"));
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isExistOrganization=expiryDocumentService.isExistOrganization(expiryDocumentDTO);
            if(isExistOrganization!=true){
            	statusResponseDTO.setStatus(env.getProperty("success"));
            	 statusResponseDTO.setMessage(env.getProperty("dualapproval.organization"));
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean changeStatus=expiryDocumentService.changeStatus(expiryDocumentDTO);
            if(changeStatus==true){
            	statusResponseDTO.setMessage(env.getProperty("dualapproval.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }else{
            	statusResponseDTO.setMessage(env.getProperty("dualapproval.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

        } catch (Exception e) {
            LOGGER.info("problem in change flag in dualapproval method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }
    }


    @CrossOrigin
	@PostMapping(value = "/getExpDocumentInfo", produces = { "application/json" })
	@ApiOperation(value = "Get Expiry Based Document Information", notes = "This Method is used to get Expiry based Document Information")
	public @ResponseBody ResponseEntity<String> getHistoryBasedOnSuperAdmin(
			@ApiParam(value = "get expiry based document information", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		statusResponseDTO.setStatus(env.getProperty("failure"));
		List<ExpiryDocumentDTO> expiryDocumentDTOList = new ArrayList<ExpiryDocumentDTO>();
		try {
			boolean validateStatus=expiryDocumentUtility.validateLoginId(expiryDocumentDTO);
			if(validateStatus!=true){
			statusResponseDTO.setMessage(env.getProperty("validate.loginid"));
	        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isExistStatus=expiryDocumentService.isExistLoginId(expiryDocumentDTO);
			if(isExistStatus!=true){
			statusResponseDTO.setMessage(env.getProperty("expiry.loginid"));
	        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

            expiryDocumentDTOList = expiryDocumentService.getExpiryDocumentList(expiryDocumentDTO);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setMessage(env.getProperty("expirydocumentholder.list"));
            statusResponseDTO.setExpiryDocumentList(expiryDocumentDTOList);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info("problem in pendingDcouments method in ExpiryDocumnetController");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }
	}


}
