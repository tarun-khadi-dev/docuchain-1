package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.DocumentService;
import com.dapp.docuchain.service.NotificationService;
import com.dapp.docuchain.utility.DocumentUtility;
import com.dapp.docuchain.utility.UserUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import com.dapp.docuchain.dto.VesselDocumentDTO;
import com.dapp.docuchain.service.FileService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping(value = "/docuchain/api/document")
@Api(value = "DocumentController", description = "Document Controller API")
@CrossOrigin
public class DocumentController {

	@Autowired
	private Environment env;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private DocumentUtility documentUtility;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserUtils userUtils;
	@Autowired

private FileService fileService;

// Add this new endpoint
@PostMapping(value = "/scanVesselDocument")
public ResponseEntity<VesselDocumentDTO> scanVesselDocument(@RequestParam("file") MultipartFile file) {
    try {
        VesselDocumentDTO vesselDocumentDTO = fileService.scanVesselImageFile(file);
        if (vesselDocumentDTO != null) {
            return new ResponseEntity<>(vesselDocumentDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

	private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

	@CrossOrigin
	@RequestMapping(value = "/approval", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
			"application/json" })
	@ApiOperation(value = "approval user", notes = "Validate user and allow user approvel")
	public ResponseEntity<String> approvingDocument(
			@ApiParam(value = "Approvel user ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.info("inside create Document controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			LOG.info("ExpiryDocument--->" + expiryDocumentDTO);

			boolean isValidDoc = documentUtility.isValid(expiryDocumentDTO);
			if (!isValidDoc) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.NOT_FOUND);
			}

			expiryDocumentDTO = documentService.approveDocument(expiryDocumentDTO);
			if (expiryDocumentDTO != null) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("document.approvel.success"));
				statusResponseDTO.setExpiryDocumentDTOs(expiryDocumentDTO);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("document.approvel.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/notification", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "document notification", notes = "get document notification")
	public @ResponseBody ResponseEntity<String> getNotification(
			@ApiParam(value = "Approvel user ", required = true) @RequestBody UserDTO userDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.info("inside create Document notification controller");
		LOG.info("userId is::::" + userDTO.getUserId());
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<UserDTO> userDTOs = new ArrayList<>();
		try {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(userDTO.getUserId());
			if (userProfileInfo != null) {
				LOG.info("get document notification by user" + userProfileInfo.getUserName());
			}
			// userDTOs = documentService.getNotification();
			userDTOs = notificationService.getNotification(userDTO);
			if (userDTOs != null && userDTOs.size() > 0) {
				statusResponseDTO.setStatus(env.getProperty("success"));
			}else{
				statusResponseDTO.setStatus(env.getProperty("failure"));
			}
				statusResponseDTO.setUserList(userDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}
	@CrossOrigin
	@RequestMapping(value = "/notification/byCategory", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "document notification", notes = "get document notification")
	public @ResponseBody ResponseEntity<String> getNotificationByCategory(
			@ApiParam(value = "Approvel user ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.info("inside create Document notification controller");
		LOG.info("userId is::::" + expiryDocumentDTO.getUserId());
		LOG.info("category::::"+expiryDocumentDTO.getCategory());
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<UserDTO> userDTOs = new ArrayList<>();
		try {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getUserId());
			if (userProfileInfo != null) {
				LOG.info("get document notification by user" + userProfileInfo.getUserName());
			}
			// userDTOs = documentService.getNotification();
			userDTOs = notificationService.getNotificationByCategory(expiryDocumentDTO);
			if (userDTOs != null && userDTOs.size() > 0) {
				statusResponseDTO.setStatus(env.getProperty("success"));
			}else{
				statusResponseDTO.setStatus(env.getProperty("failure"));
			}
				statusResponseDTO.setUserList(userDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	// @CrossOrigin
	// @RequestMapping(value = "/notification/count", method = RequestMethod.POST, consumes = {
	// 		"application/json" }, produces = { "application/json" })
	// @ApiOperation(value = "document notification", notes = "get document notification")
	// public @ResponseBody ResponseEntity<String> getNotificationCount(
	// 		@ApiParam(value = "Notification Count", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO,
	// 		HttpServletRequest request, HttpServletResponse response) {
	// 	LOG.info("inside create Document notification controller");
	// 	LOG.info("userId is::::" + expiryDocumentDTO.getUserId());
	// 	StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
	// 	List<UserDTO> userDTOs = new ArrayList<>();
	// 	try {
	// 		UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getUserId());
	// 		if (userProfileInfo != null) {
	// 			LOG.info("get document notification by user" + userProfileInfo.getUserName());
	// 		}
	// 		// userDTOs = documentService.getNotification();
	// 		userDTOs = notificationService.getNotificatonCount(expiryDocumentDTO.getUserId());
	// 		if (userDTOs != null && userDTOs.size() > 0) {
	// 			statusResponseDTO.setStatus(env.getProperty("success"));
	// 			statusResponseDTO.setUserList(userDTOs);
	// 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
	// 		}
	// 		statusResponseDTO.setStatus(env.getProperty("failure"));
	// 		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);

	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		statusResponseDTO.setStatus(env.getProperty("failure"));
	// 		statusResponseDTO.setMessage(env.getProperty("server.problem"));
	// 		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
	// 	}

	// }

@RequestMapping(value = "/notification/count", method = RequestMethod.POST,
        consumes = "application/json", produces = "application/json")
public ResponseEntity<String> getNotificationCount(
        @RequestBody ExpiryDocumentDTO expiryDocumentDTO) {

    StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

    try {

        Long userId = expiryDocumentDTO.getUserId();

        if (userId == null) {
            statusResponseDTO.setStatus("failure");
            statusResponseDTO.setMessage("UserId is required");
            return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.BAD_REQUEST);
        }

        List<UserDTO> userDTOs =
                notificationService.getNotificatonCount(userId);

        statusResponseDTO.setStatus("success");
        statusResponseDTO.setUserList(userDTOs != null ? userDTOs : new ArrayList<>());

        return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

    } catch (Exception e) {
        e.printStackTrace();
        statusResponseDTO.setStatus("failure");
        statusResponseDTO.setMessage("Server problem");
        return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

	@CrossOrigin
	@RequestMapping(value = "/notification/viewed", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "document notification", notes = "set notifivation viewed")
	public @ResponseBody ResponseEntity<String> setNotificationViewed(
			@ApiParam(value = "Approvel user ", required = true) @RequestBody ExpiryDocumentDTO expiryDocumentDTO,
			HttpServletRequest request, HttpServletResponse response) {
		LOG.info("inside create Document notification viewed controller");
		LOG.info("userId is::::" + expiryDocumentDTO.getUserId());
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<UserDTO> userDTOs = new ArrayList<>();
		try {
			UserProfileInfo userProfileInfo = userProfileRepository.findById(expiryDocumentDTO.getUserId());
			if (userProfileInfo != null) {
				LOG.info("get document notification by user" + userProfileInfo.getUserName());
			}
			// userDTOs = documentService.getNotification();
			Boolean setView = notificationService.setViewedNotification(expiryDocumentDTO.getUserId());
			if (setView) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	 @CrossOrigin
	    @RequestMapping(value = "/notification/delete", method = RequestMethod.POST, consumes = {
		"application/json" }, produces = { "application/json" })
	    @ApiOperation(value = "search user", notes = "Validate user and allow user search")
	    public ResponseEntity<String> deleteNotification(
	            @ApiParam(value = "Required notification id ", required = true) @RequestBody UserDTO userDTO,
	            HttpServletRequest request, HttpServletResponse response) {
		 	StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		 	LOG.info("inside notification delete"+userDTO);
		 		try{
		 			boolean isvalid = userUtils.validUserDTO(userDTO);
		 			if (!isvalid){
						statusResponseDTO.setStatus(env.getProperty("failure"));
						statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
						return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.NOT_FOUND);
		 			}
		 			boolean isDelete = notificationService.deleteNotification(userDTO);
		 			if (!isDelete){
		 				statusResponseDTO.setStatus(env.getProperty("notification.delete.failure"));
			 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		 			}
		 			statusResponseDTO.setStatus(env.getProperty("notification.delete.success"));
		 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		 		}catch (Exception e){
		 			e.printStackTrace();
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("server.problem"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		 		}
		 		}
	 @CrossOrigin
		@RequestMapping(value = "/notification/deleteAll", method = RequestMethod.POST, consumes = {
				"application/json" }, produces = { "application/json" })
		@ApiOperation(value = "document notification", notes = "get document notification")
		public @ResponseBody ResponseEntity<String> deleteAllNotification(
				@ApiParam(value = "Approvel user ", required = true) @RequestBody UserDTO userDTO,
				HttpServletRequest request, HttpServletResponse response) {
			LOG.info("inside create Document notification controller");
			LOG.info("notification all delete list size is::::" + userDTO);
			StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
			try {
				boolean isDelete = notificationService.deleteAllNotification(userDTO);
				if (!isDelete){
	 				statusResponseDTO.setStatus(env.getProperty("notification.delete.failure"));
		 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	 			}
	 			statusResponseDTO.setStatus(env.getProperty("notification.delete.success"));
	 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("server.problem"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		}
	 @CrossOrigin
		@RequestMapping(value = "/notification/snooze/update", method = RequestMethod.POST, consumes = {
				"application/json" }, produces = { "application/json" })
		@ApiOperation(value = "document notification ", notes = " document notification snoozeupdate")
		public @ResponseBody ResponseEntity<String> snoozeupdate(
				@ApiParam(value = "Approvel user ", required = true) @RequestBody UserDTO userDTO,
				HttpServletRequest request, HttpServletResponse response) {
			LOG.info("inside create Document notification controller");
			StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
			try {
				boolean isUpdate = notificationService.updateSnooze(userDTO);
				if (!isUpdate){
					statusResponseDTO.setStatus(env.getProperty("failure"));
		 			statusResponseDTO.setMessage(env.getProperty("notification.snooze.update.failure"));
		 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
	 			}
	 			statusResponseDTO.setStatus(env.getProperty("success"));
	 			statusResponseDTO.setMessage(env.getProperty("notification.snooze.update.success"));
	 			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("server.problem"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		}


}
