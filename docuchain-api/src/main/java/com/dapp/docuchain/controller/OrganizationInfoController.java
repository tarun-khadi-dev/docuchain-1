package com.dapp.docuchain.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dapp.docuchain.dto.OrganizationDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.UserDTO;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.FileService;
import com.dapp.docuchain.service.OrganizationInfoService;
import com.dapp.docuchain.utility.CommonMethodsUtility;
import com.dapp.docuchain.utility.OrganizationInfoUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping(value = "/docuchain/api/organization")
@Api(value = "OrganizationController", description = "Organization Controller API")
@CrossOrigin
public class OrganizationInfoController {

	@Autowired
	private Environment env;

	@Autowired
	private OrganizationInfoService organizationInfoService;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private CommonMethodsUtility commonMethodsUtility;

	@Autowired
	private FileService fileService;

	@Autowired
	private OrganizationInfoUtility organizationInfoUtility;

	@CrossOrigin
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation(value = "CREATE ORGANIZATION", notes = "This Method is used to create new Organization Info")
	public @ResponseBody ResponseEntity<String> createOrganization(@ApiParam(value = "Required Organization info creation details", required = true) @RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String createOrganization = organizationInfoService.saveOrganizationInformation(organizationDTO);
			if (!createOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(createOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (createOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.create.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/create/with/subscription", produces = MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation(value = "CREATE ORGANIZATION", notes = "This Method is used to create new Organization Info")
	public @ResponseBody ResponseEntity<String> saveOrganizationWithSubscriptionAndAdmin(@ApiParam(value = "Required Organization with subscription and Amin profile required", required = true) @RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String createOrganization = organizationInfoService.saveOrganizationWithSubscriptionAndAdmin(organizationDTO);
			if (!createOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(createOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (createOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.create.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}


	@CrossOrigin
    @GetMapping(value = "/list/{superAdminId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "LIST ORGANIZATIONS", notes = "This Mehtord is used to get organization list")
    public ResponseEntity<String> getAllOrganizationList(@ApiParam(value = "Super Admin ID required", required =true)@PathVariable Long superAdminId){
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
        	if(superAdminId != null){
        		UserProfileInfo profileInfo = userProfileRepository.findOne(superAdminId);
        		if (profileInfo == null){
        			statusResponseDTO.setStatus(env.getProperty("failure"));
    				statusResponseDTO.setMessage(env.getProperty("user.not.found"));
    				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        		}
        		if (!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)){
        			statusResponseDTO.setStatus(env.getProperty("failure"));
    				statusResponseDTO.setMessage(env.getProperty("organization.admin.only.list"));
    				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        		}

        		List<OrganizationDTO> organizationDTOs = organizationInfoService.getAllOrganizationList();
        		if(organizationDTOs != null && organizationDTOs.size() > 0){
        			statusResponseDTO.setMessage(env.getProperty("organization.list.success"));
        		}else{
        			statusResponseDTO.setMessage(env.getProperty("organization.list.empty"));
        		}
        		statusResponseDTO.setStatus(env.getProperty("success"));
        		statusResponseDTO.setOrganizationInfos(organizationDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

        	}else {
        		statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("user.id.null"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        	}
        } catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@CrossOrigin
	@GetMapping(value = "/view/{organizationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "VIEW ORGANIZATION", notes = "This Mehtord is used to view organization Inforamtion")
	public ResponseEntity<String> viewOrganizationInformation(
			@ApiParam(value = "Organization ID required", required = true) @PathVariable Long organizationId) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String isOrganizationExist = organizationInfoService.isOrganizationExist(organizationId);
			if (!isOrganizationExist.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(isOrganizationExist);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			OrganizationDTO organizationDTO = organizationInfoService.viewOrganizationInformation(organizationId);
			if (organizationDTO != null) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.view.success"));
				statusResponseDTO.setOrganizationInfo(organizationDTO);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			} else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.not.found"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@CrossOrigin
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "UPDATE ORGANIZATION", notes = "Update Organization information based by super admin profile")
	public @ResponseBody ResponseEntity<String> updateOrganization(
			@ApiParam(value = "Organization information is required", required = true) @RequestBody OrganizationDTO organizationDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String updateOrganization = organizationInfoService.updateOrganizationInformation(organizationDTO);
			if (!updateOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(updateOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (updateOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.updated.successfully"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else{
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.updated.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}


	@CrossOrigin
	@DeleteMapping(value = "/delete/{superAdminId}/{organizationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "DELETE ORGANIZATION", notes = "Delete Organization and corresponding Subscription")
	public @ResponseBody ResponseEntity<String> deleteOrganizationInformation(@ApiParam(value="Super Admin ID required", required = true)@PathVariable(required = true)Long superAdminId, @ApiParam(value="Organization ID required", required = true)@PathVariable(required = true)Long organizationId) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String deleteOrganization = organizationInfoService.deleteOrganizationInformation(superAdminId, organizationId);
			if (!deleteOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(deleteOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (deleteOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.deleted.successfully"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.deleted.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/blockchain/active", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation(value = "SAVE IN BLOCKCHAIN ORGANIZATION", notes = "This Method is used to enable/disable save in blockhain Organization Info")
	public @ResponseBody ResponseEntity<String> enableSaveInBlochainOrganizationInformation(@ApiParam(value = "Required super admin id and organization id and save in blochain status required", required = true) @RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String saveInBlockchainOrganization = organizationInfoService.setSaveInBlockchainOrganizationInformation(organizationDTO);
			if (!saveInBlockchainOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(saveInBlockchainOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (saveInBlockchainOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage("Status updated successfully");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("Status update is failed");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/active", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation(value = "ACTIVE ORGANIZATION", notes = "This Method is used to acitve Organization Info")
	public @ResponseBody ResponseEntity<String> enableActiveAndDeactiveOrganizationInformation(@ApiParam(value = "Required super admin id and organization id and active status required", required = true) @RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String activeOrganization = organizationInfoService.activeActiveAndDeactiveOrganizationInformation(organizationDTO);
			if (!activeOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(activeOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (activeOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage("Status updated successfully");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("Status update is failed");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@PostMapping(value = "/dual/approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation(value = "DUAL APPROVAL ORGANIZATION", notes = "This Method is used to enable Dual approval Organization Info")
	public @ResponseBody ResponseEntity<String> enableDualApprovalOrganizationInformation(@ApiParam(value = "Required super admin id and organization id and dual approval active status required", required = true) @RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String dualApprovalOrganization = organizationInfoService.enableOrDiableDualApprovalOrganizationInformation(organizationDTO);
			if (!dualApprovalOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(dualApprovalOrganization);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (dualApprovalOrganization.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.dual.approval.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.dual.approval.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@GetMapping(value = "/list/admin/{organizationId}", produces = MediaType.APPLICATION_JSON_VALUE )
	@ApiOperation(value = "LIST ADMINS BASED ORGANIZATION", notes = "This Method is used to list admin based on Organization Info")
	public @ResponseBody ResponseEntity<String> listAdminsBasedOrganizationInformation(@ApiParam(value = "Required Organization id", required = true) @PathVariable Long organizationId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
        	if(organizationId != null){
        		String isOrganizationExist = organizationInfoService.isOrganizationExist(organizationId);
        		if (!isOrganizationExist.equalsIgnoreCase(env.getProperty("success"))){
        			statusResponseDTO.setStatus(env.getProperty("failure"));
    				statusResponseDTO.setMessage(isOrganizationExist);
    				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        		}
        		List<UserDTO> userDTOs = organizationInfoService.listAdminsBasedOrganizationInformation(organizationId);
        		statusResponseDTO.setStatus(env.getProperty("success"));
        		statusResponseDTO.setAdminInfos(userDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

        	}else {
        		statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.id.null"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        	}
        } catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "CREATE ADMINS BASED ORGANIZATION", notes = "This Method is used to create admin based on Organization Info")
	@PostMapping(value = "/save/admin",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveAdminInformationBasedOrganization(@RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String saveAdmin = organizationInfoService.saveAdminInformationBasedOrganization(organizationDTO);
			if (!saveAdmin.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(saveAdmin);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (saveAdmin.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("admin.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("admin.create.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@CrossOrigin
	@GetMapping(value = "/top/count", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "ORGANIZATION TOP COUNT", notes = "This Mehtord is used to get Top count for superAdmin ")
	public @ResponseBody ResponseEntity<String> getOrganizationTopCountDetails() throws Exception {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			OrganizationDTO organizationDTOs = organizationInfoService.getOrganizationTopCount();
			if (organizationDTOs != null) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setOrganizationInfo(organizationDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@GetMapping(value = "/statistics/detail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "ORGANIZATION STATISTICS", notes = "This Mehtord is used to get Top count for superAdmin ")
	public @ResponseBody ResponseEntity<String> getOrganizationStatistics() throws Exception {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			List<OrganizationDTO> organizationDTOs = organizationInfoService.getOrganizationStatistics();
			if (organizationDTOs != null && organizationDTOs.size() > 0) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setOrganizationInfos(organizationDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setOrganizationInfos(organizationDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

	}

	@CrossOrigin
	@PostMapping(value = "/addLogo", produces = { "application/json" })
	@ApiOperation(value = "add Logo", notes = "Add Ship Proile Details ")
	public ResponseEntity<String> addshipProfile(
			@ApiParam(value = "Required addLogo", required = true) @RequestParam(name = "adminId", value = "adminId", required = true) Long adminId,
			@ApiParam(value = "Required file attachment", required = true) @RequestParam(name = "addLogoImage", value = "addLogoImage", required = true) MultipartFile addLogoImage) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			ObjectMapper mapper = new ObjectMapper();
			Long adminIdback ;
			String logoPicPath = null;
			try {
				System.out.println("adminId" + adminId);
				adminIdback = adminId;
			} catch (Exception e) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage("Problem in ship profile data");
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
				if (!commonMethodsUtility.isImage(addLogoImage)) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("image.type.invalid"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
			if (addLogoImage != null) {
				logoPicPath = fileService.organizationImageUpload(adminId, addLogoImage);
				if (logoPicPath == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage("Please upload proper document");
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
			}
			OrganizationDTO isshipprofile = organizationInfoUtility.createOrganizationLogo(adminId, logoPicPath);

			if (isshipprofile != null) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("logoupload.success"));
				statusResponseDTO.setOrganizationInfo(isshipprofile);
				return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("logoupload.failure"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		}catch (Exception ex) {
			ex.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("shipprofile.servererror"));
			return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	/*@CrossOrigin
	@ApiOperation(value = "UPDATE SUBSCRIPTION AND ORGANIZATION", notes = "This Method is used to update supscription and organization information")
	@PostMapping(value = "/subscription/update",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateSubscriptionAndOrganization(@RequestBody OrganizationDTO organizationDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String updateOrganizationAndSubscription = organizationInfoService.updateSubscription(organizationDTO);
			if (!updateOrganizationAndSubscription.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(updateOrganizationAndSubscription);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			if (updateOrganizationAndSubscription.equalsIgnoreCase(env.getProperty("success"))) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("organization.subscription.update.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("organization.subscription.update.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

}
