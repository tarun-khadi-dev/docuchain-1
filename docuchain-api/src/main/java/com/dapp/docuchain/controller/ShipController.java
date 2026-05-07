package com.dapp.docuchain.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.service.FileService;
import com.dapp.docuchain.service.ShipProfileService;
import com.dapp.docuchain.service.UserService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.ShipProfileUtility;
import com.dapp.docuchain.utility.UserUtils;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/docuchain/api/ship")
@Api(value = "ShipController", description = "Ship Controller API")
@CrossOrigin
public class ShipController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserUtils userUtils;

	@Autowired
	ShipProfileUtility shipProfileUtility;

	@Autowired
	private FileService fileService;

	@Autowired
	private ShipProfileService shipProfileService;

	@Autowired
	private Environment env;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private UserService userService;

	@CrossOrigin
	@PostMapping(value = "/create", produces = { "application/json" })
	@ApiOperation(value = "add shipprofile", notes = "Add Ship Proile Details ")
	public ResponseEntity<String> addshipProfile(
			@ApiParam(value = "Required shipprofile", required = true) @RequestParam(name = "shipProfileInfo", value = "shipProfileInfo", required = true) String shipProfileDTOStr,
			@ApiParam(value = "Required file attachment", required = false) @RequestParam(name = "shipPic", value = "shipPic", required = false) MultipartFile shipPic) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			ObjectMapper mapper = new ObjectMapper();
			ShipProfileDTO shipProfileDTO = null;
			String shipProfilePicPath = null;
			try {
				System.out.println("shipProfileDTOStr" + shipProfileDTOStr);
				shipProfileDTO = mapper.readValue(shipProfileDTOStr, ShipProfileDTO.class);
			} catch (Exception e) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("Problem in ship profile data");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (shipPic != null) {
				if (!commonMethodsUtility.isImage(shipPic)) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("image.type.invalid"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				String isOrganizationValid = shipProfileService.isOrganizationValid(shipProfileDTO.getOrganizationId());
				if (env.getProperty("failure").equals(isOrganizationValid)) {
					statusResponseDTO.setStatus(env.getProperty("success"));
					statusResponseDTO.setMessage(env.getProperty("organization.id.not.exist"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				shipProfilePicPath = fileService.shipProfileUpload(shipProfileDTO, shipPic);
				if (shipProfilePicPath == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage("Please upload proper document");
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
			}
			boolean isshipCountIsExceed = shipProfileUtility.isshipCountIsExceed(shipProfileDTO);
			if (isshipCountIsExceed) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ship.count.exists"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isValid = shipProfileUtility.validateShipProfileCreateParam(shipProfileDTO);
			if (!isValid.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isValid);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			String isValidoffNo = userUtils.offNoValidateForShipProfile(shipProfileDTO);
			if (!isValidoffNo.equalsIgnoreCase("Success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isValidoffNo);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isshipprofile = shipProfileService.createShipProfile(shipProfileDTO, shipProfilePicPath);

			if (isshipprofile) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.success"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/delete", produces = { "application/json" })
	@ApiOperation(value = "DELETE SHIP PROFILE", notes = "Admin only delete single ship profile")
	public ResponseEntity<String> deleteShipProfileInformationBasedOrganization(
			@ApiParam(value = "User ID and Ship ID requires", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String deleteResult = shipProfileService.deleteShipProfileInformationBasedOrganization(shipProfileDTO);
			if(!deleteResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(deleteResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("ship.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ship.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/delete/all", produces = { "application/json" })
	@ApiOperation(value = "DELETE ALL SHIP PROFILE", notes = "Organization based Admin only delelte All Ship Profile")
	public ResponseEntity<String> shipDeleteAll(
			@ApiParam(value = "User Id and Ship Id's are required", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValidcountrytostate = shipProfileUtility.validateDeleteAll(shipProfileDTO);
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			UserProfileInfo profileInfo = userService.isUserProfileInfoExists(shipProfileDTO.getUserId());
			if (profileInfo == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("user.not.found"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if (!profileInfo.getRoleId().getRoleName().equals(Role.Admin)) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ship.delete.admin.only"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			String isShipExist = shipProfileUtility.isShipExist(shipProfileDTO);
			if (!isShipExist.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isShipExist);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			Boolean atd = shipProfileService.shipDeleteAll(shipProfileDTO);
			if (!atd) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ship.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("ship.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@GetMapping(value = "/list/{userId}", produces = { "application/json" })
	@ApiOperation(value = "LIST SHIP PROFILES", notes = "List Ship profiles informations")
	public ResponseEntity<String> listShipProfilesBasedOrganization(@ApiParam(value = "User ID required", required = true) @PathVariable Long userId) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isUserExist = userService.isExistingUser(userId);
			if (!isUserExist){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> shipProfileInfoList = shipProfileService.shipListBasedOnOrg(userId);
			statusResponseDTO.setStatus(env.getProperty("success"));
			if(shipProfileInfoList != null && shipProfileInfoList.size() > 0){
				statusResponseDTO.setMessage(env.getProperty("ship.list.success"));
			}else {
				statusResponseDTO.setMessage(env.getProperty("ship.list.failure"));
			}
			statusResponseDTO.setShipProfileList(shipProfileInfoList);
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/update", produces = { "application/json" })
	@ApiOperation(value = "update shipprofile", notes = "update Ship Proile Details ")
	public ResponseEntity<String> updateShipProfile(
			@ApiParam(value = "Required shipprofile", required = true) @RequestParam(name = "shipProfileInfo", value = "shipProfileInfo", required = true) String shipProfileDTOStr,
			@ApiParam(value = "Required file attachment", required = false) @RequestParam(name = "updateshipPic", value = "updateshipPic", required = false) MultipartFile shipPic) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			ObjectMapper mapper = new ObjectMapper();
			ShipProfileDTO shipProfileDTO = null;
			String shipProfilePicPath = null;
			try {
				shipProfileDTO = mapper.readValue(shipProfileDTOStr, ShipProfileDTO.class);
			} catch (Exception e) {
				System.out.println("eee"+e);
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("Problem in ship profile data");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (shipPic != null) {
				if (!commonMethodsUtility.isImage(shipPic)) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("image.type.invalid"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				shipProfilePicPath = fileService.shipProfileUpload(shipProfileDTO, shipPic);
				if (shipProfilePicPath == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage("Please upload proper document");
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
			}
			String isValid = shipProfileUtility.validateShipProfileCreateParam(shipProfileDTO);
			if (!isValid.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isValid);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isShipExist = shipProfileUtility.isShipExistorNOt(shipProfileDTO);
			if (!isShipExist.equalsIgnoreCase("success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isShipExist);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}
			String isValidoffNo = userUtils.validateUpdateShipParam(shipProfileDTO);
			if (!isValidoffNo.equalsIgnoreCase("Success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isValidoffNo);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isshipprofile = shipProfileService.updateShipProfile(shipProfileDTO, shipProfilePicPath);

			if (isshipprofile) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.update"));
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.failure"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/update/status", produces = { "application/json" })
	@ApiOperation(value = "Delete Ship Info", notes = "Delete Individual Ship Info")
	public ResponseEntity<String> updateShipStatus(
			@ApiParam(value = "Delete Ship  Info", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isvalidateShipId = userUtils.validateShipId(shipProfileDTO);
			if (!isvalidateShipId) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String isShipExist = shipProfileUtility.isShipExistorNOt(shipProfileDTO);
			if (!isShipExist.equalsIgnoreCase("success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isShipExist);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}
			Boolean atd = shipProfileService.updateShipStatus(shipProfileDTO);
			if (atd) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.status.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.status.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/update/statusall", produces = { "application/json" })
	@ApiOperation(value = "Delete Ship Info", notes = "Delete Individual Ship Info")
	public ResponseEntity<String> updateShipStatusAll(
			@ApiParam(value = "Delete Ship  Info", required = true) @RequestBody ShipProfileDTO shipProfileDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValidcountrytostate = shipProfileUtility.validateDeleteAll(shipProfileDTO);
			if (!isValidcountrytostate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			String isShipExist = shipProfileUtility.isShipExist(shipProfileDTO);
			if (!isShipExist.equalsIgnoreCase("success")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isShipExist);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}

			Boolean atd = shipProfileService.updateShipStatusAll(shipProfileDTO);
			if (!atd) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.status.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.status.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		} catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/viewShipProfile/{userId}", method = RequestMethod.GET, produces = { "application/json" })
	@ApiOperation(value = "viewShipProfile request", notes = "getting viewShipProfile")
	public ResponseEntity<String> getPendingRequest(
			@ApiParam(value = "viewShipProfile ", required = true) @PathVariable Long userId) {
		LOGGER.info("inside shipProfile controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isUserExist = userService.isExistingUser(userId);
			if (!isUserExist) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> shipProfileDTOs = shipProfileService.viewShipProfile(userId);
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setShipProfileList(shipProfileDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/getDashboardTopCount/{userId}", method = RequestMethod.GET, produces = {
			"application/json" })
	@ApiOperation(value = "getDashboardTopCount request", notes = "getting getDashboardTopCount")
	public ResponseEntity<String> getDashboardTopCount(
			@ApiParam(value = "viewShipProfile ", required = true) @PathVariable Long userId) {
		LOGGER.info("inside shipProfile controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isUserExist = userService.isExistingUser(userId);
			if (!isUserExist) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("noUserProfile"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> shipProfileDTOs = shipProfileService.getDashboardTopCount(userId);
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setShipProfileList(shipProfileDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}

	}

	@CrossOrigin
	@ApiOperation(value = "ACTIVE ALL VESSELS",notes ="This API is used to activate all vessles based organization")
	@PostMapping(value = "/active/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> activeAllVesselsBasedOrganization(@ApiParam(value = "User id and Ship Id's are required") @RequestBody(required = true) ShipProfileDTO shipProfileDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			String activateResult = shipProfileService.activateAllVesselsBasedOrganization(shipProfileDTO);
			if(!activateResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(activateResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(activateResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("ship.activate.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ship.activate.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e){
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@ApiOperation(value = "DEACTIVE ALL VESSELS",notes ="This API is used to deactivate all vessles based organization")
	@PostMapping(value = "/deactive/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deActiveAllVesselsBasedOrganization(@ApiParam(value = "User id and Ship Id's are required") @RequestBody(required = true) ShipProfileDTO shipProfileDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			String activateResult = shipProfileService.deActivateAllVesselsBasedOrganization(shipProfileDTO);
			if(!activateResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(activateResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(activateResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("ship.deactivate.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ship.deactivate.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e){
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@ApiOperation(value = "DELETE SHIPMASTER FROM SHIP", notes = "Admin only reomve ship master from ship profile ")
	@PostMapping(value = "/shipmaster/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteShipMasterFromShipProfileInfoBasedOrganization(@ApiParam(value = "User Id and Ship Id and ship master Id") @RequestBody ShipProfileDTO shipProfileDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			String deleteShipMasterResult = shipProfileService.deleteShipMasterFromShipProfileInfoBasedOrganization(shipProfileDTO);
			if(!deleteShipMasterResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteShipMasterResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(deleteShipMasterResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("tech.manager.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("tech.manager.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e){
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE TECHMANAGER FROM SHIP", notes = "Admin only reomve technical manager from ship profile ")
	@PostMapping(value = "/techmanager/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteTechnicalManagersFromShipProfileInfoBasedOrganization(@ApiParam(value = "User Id and Ship Id and Tech managers Id") @RequestBody ShipProfileDTO shipProfileDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			String deleteTechManagerResult = shipProfileService.deleteTechnicalManagersFromShipProfileInfoBasedOrganization(shipProfileDTO);
			if(!deleteTechManagerResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteTechManagerResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(deleteTechManagerResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("tech.manager.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("tech.manager.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e){
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE COMMERCIAL MANAGER FROM SHIP", notes = "Admin only reomve Commercial manager from ship profile")
	@PostMapping(value = "/commercialmanager/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteCommercialManagersFromShipProfileInfoBasedOrganization(@ApiParam(value = "User Id and Ship Id and Commercial managers Id") @RequestBody ShipProfileDTO shipProfileDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			String deleteCommManagerResult = shipProfileService.deleteCommercialManagersFromShipProfileInfoBasedOrganization(shipProfileDTO);
			if(!deleteCommManagerResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteCommManagerResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if(deleteCommManagerResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("tech.manager.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("tech.manager.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e){
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "LIST SHIP BASED ORGANIZATION", notes = "Ship profile list are shown based on organization")
	@GetMapping(value = "/list/organization/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> listShipProfileInfoBasedOrganization(@ApiParam(value = "User ID required")@PathVariable(required = true)Long userId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{

			UserProfileInfo profileInfo = userService.isUserProfileInfoExists(userId);
			if (profileInfo == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("user.not.found"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			List<ShipProfileDTO> listShipProfiles = shipProfileService.listShipProfileInfoBasedOrganization(profileInfo.getOrganizationInfo());
			if(listShipProfiles != null){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setShipProfileList(listShipProfiles);
				statusResponseDTO.setMessage(env.getProperty("shipprofile.list.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("shipprofile.list.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e){
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/getDashboardTopCountBasedOnVessel/{vesselId}", method = RequestMethod.GET, produces = {
			"application/json" })
	@ApiOperation(value = "getDashboardTopCount request", notes = "getting getDashboardTopCount based on vessel")
	public ResponseEntity<String> getDashboardTopCountBasedOnVessel(
			@ApiParam(value = "viewShipProfile ", required = true) @PathVariable Long vesselId) {
		LOGGER.info("inside shipProfile controller");
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			ShipProfileDTO shipProfileDTO = shipProfileService.getDashboardTopCountBasedOnVessel(vesselId);
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setUsershipCount(shipProfileDTO);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}

	}
}
