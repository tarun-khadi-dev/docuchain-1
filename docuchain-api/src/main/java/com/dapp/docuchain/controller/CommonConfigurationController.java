package com.dapp.docuchain.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;
import com.dapp.docuchain.dto.CountryDTO;
import com.dapp.docuchain.dto.DocumentHolderDTO;
import com.dapp.docuchain.dto.ExpiryCertificateTypeDTO;
import com.dapp.docuchain.dto.FaqDTO;
import com.dapp.docuchain.dto.PortDTO;
import com.dapp.docuchain.dto.RoleAliasDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.dto.UserReportAnIssueDTO;
import com.dapp.docuchain.dto.VesselsTypeDTO;
import com.dapp.docuchain.model.Role;
import com.dapp.docuchain.model.UserProfileInfo;
import com.dapp.docuchain.repository.CountryInfoRepository;
import com.dapp.docuchain.repository.UserProfileRepository;
import com.dapp.docuchain.service.CommonConfigurationService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import java.util.HashMap;
import com.dapp.docuchain.service.ShipProfileService;

@CrossOrigin
@RequestMapping(value = "/docuchain/api/common")
@Api(value = "CommonConfigurationController", description = " Common Configuration Controller API")
@RestController
public class CommonConfigurationController {

	private final Logger LOG = LoggerFactory.getLogger(CommonConfigurationController.class);

	@Autowired
	private Environment env;

	@Autowired
	private CommonConfigurationService commonConfigurationService;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private CountryInfoRepository countryInfoRepository;
	@Autowired
private ShipProfileService shipProfileService;

	@CrossOrigin
	@ApiOperation(value = "ADD NEW COUNTRY", notes = "Add new Country is super admin profile")
	@PostMapping(value = "/country/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  addCountryInformationBasedAdmin(@ApiParam(value ="Country name, code is required", required = true)@RequestBody CountryDTO countryDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Country Info : {} ",countryDTO);
		String validationResult =  commonConfigurationService.addCountryInformationBySuperAdmin(countryDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.create.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.create.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}
	@CrossOrigin
@GetMapping("/vessel/ports/{organizationId}")
public ResponseEntity<Map<String, Object>> getPortsCount(@PathVariable Long organizationId) {

    Long count = shipProfileService.getPortCountByOrganization(organizationId);

    Map<String, Object> response = new HashMap<>();
    response.put("portCount", count);

    return new ResponseEntity<>(response, HttpStatus.OK);
}

	@CrossOrigin
	@ApiOperation(value = "LIST COUNTRY", notes = "List Country")
	@GetMapping(value = "/country/list", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listCountryInformationForAllUser(){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<CountryDTO> countryDTOs = commonConfigurationService.listCountryInformationForAllUser();
		if(countryDTOs != null && countryDTOs.size() > 0){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.list.success"));
			statusResponseDTO.setCountryInfos(countryDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.list.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "UPDATE COUNTRY", notes = "Update Country only permission forsuper admin")
	@PutMapping(value = "/country/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  updateCountryInformationBasedAdmin(@ApiParam(value ="Country id,name,code,admin_id is required", required = true)@RequestBody CountryDTO countryDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String result = commonConfigurationService.updateCountryInformation(countryDTO);
		if(!result.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(result);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(result.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.update.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE MULTIPLE COUNTRY", notes = "Delete Multiple Country only having rights for super admin")
	@PostMapping(value = "/country/delete",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteCountryInformationBasedOrganization(@ApiParam(value ="User id and country id is required to delete country profile", required = true)@RequestBody CountryDTO countryDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteResult = commonConfigurationService.deleteMultipleAndSingleCountryInformation(countryDTO);
		if(!deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		if(deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE SINGLE COUNTRY", notes = "Delete Single Country rights having only super admin")
	@DeleteMapping(value = "/country/delete/{userId}/{countryId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteCountryInformation(@ApiParam(value ="User id and country id is required to delete single country profile", required = true)@PathVariable Long userId, @PathVariable Long countryId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteAllResult = commonConfigurationService.deleteCountryInformation(countryId,userId);
		if(!deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteAllResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "DELETE ALL COUNTRY", notes = "Delete All Country rights having only super admin")
	@DeleteMapping(value = "/country/delete/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteAllCountryInformationBasedOrganization(@ApiParam(value ="User id is required to delete all country profile", required = true)@PathVariable Long userId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteAllResult = commonConfigurationService.deleteAllCountryInformationBasedOrganization(userId);
		if(!deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteAllResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "ADD COUNTRY PORT INFORMATION", notes = "Add Country port information is based by admin profile")
	@PostMapping(value = "/country/port/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  addCountryPortInformationOrganizationByAdmin(@ApiParam(value ="Country id and port name and admin is required", required = true)@RequestBody PortDTO portDTO ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Vessels Type Information creation parameters : {} ",portDTO);
		String validationResult =  commonConfigurationService.addCountryPortInformationValidation(portDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		UserProfileInfo profileInfo = userProfileRepository.findOne(portDTO.getUserId());
		if(profileInfo == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(!profileInfo.getRoleId().getRoleName().equals(Role.SuperAdmin)){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("port.super.admin.only.create"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		String countryResult =  commonConfigurationService.isCountryAndPortNameAndPortAlreadyExists(portDTO);
		if(!countryResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(countryResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		PortDTO savedPort = commonConfigurationService.addCountryPortInformationOrganizationByAdmin(portDTO);
		if(savedPort != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("port.create.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("port.create.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "LIST PORT INFORMATION", notes = "List Port informations is based by country profile")
	@GetMapping(value = "/country/port/list/{countryId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listCountryPortInformationOrganization(@ApiParam(value ="Country id is required to fetch the belongs the port list", required = true)@PathVariable Long countryId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		if(countryId == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.id.null"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		if(countryInfoRepository.findOne(countryId) == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<PortDTO> portDTOs = commonConfigurationService.listPortInformationBasedOnCountryProfile(countryId);
		if(portDTOs != null && portDTOs.size() > 0){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("port.list.success"));
			statusResponseDTO.setPortInfos(portDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("port.list.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "PORT LIST INFORMATION", notes = "All List Port informations with country id and country name information")
	@GetMapping(value = "/country/port/list/all", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listAllCountryPortInformation(){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<PortDTO> portDTOs = commonConfigurationService.findAllPortInformationWithCountryDetails();
		statusResponseDTO.setStatus(env.getProperty("success"));
		statusResponseDTO.setMessage(env.getProperty("port.list.success"));
		statusResponseDTO.setPortInfos(portDTOs);
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
	}

	@CrossOrigin
	@ApiOperation(value = "UPDATE COUNTRY PORT INFORMATION", notes = "Update Country Port information is based by admin profile")
	@PostMapping(value = "/country/port/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  updateCountryPortInformationBasedOrganizationByAdmin(@ApiParam(value ="Country id and Port ID,name and admin_id is required", required = true)@RequestBody PortDTO portDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String validationResult =  commonConfigurationService.updateCountryPortInformationBasedOrganization(portDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("port.update.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("port.update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE COUNTRY PORT INFORMATION", notes = "Delete Country Port is based by user organization profile")
	@PostMapping(value = "/country/port/delete",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteCountryPortInformationBasedOrganizationByAdmin(@ApiParam(value ="User id and country id and port id is required to delete prot profile", required = true)@RequestBody PortDTO portDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteResult = commonConfigurationService.deleteCountryPortInformationBasedOrganization(portDTO);
		if(!deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		if(deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("port.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("port.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE ALL COUNTRY PORTS INFORMATION", notes = "Delete All Country Ports is based by user organization profile")
	@PostMapping(value = "/country/port/delete/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteAllCountryPortInformationBasedOrganizationByAdmin(@ApiParam(value ="User id and country id is required to delete all port profile", required = true)@RequestBody PortDTO portDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteAllResult = commonConfigurationService.deleteAllCountryPortInformationBasedOrganization(portDTO);
		if(!deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteAllResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("port.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("port.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}



	@CrossOrigin
	@ApiOperation(value = "ADD ROLE ALIAS INFORMATION BASED ON ADMIN", notes = "Add new role alias information is based by admin profile")
	@PostMapping(value = "/roles/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  addRoleAliasInformationBasedAdmin(@ApiParam(value ="Role alias name, role and admin is required", required = true)@RequestBody RoleAliasDTO roleAliasDTO ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Role Alias Information creation parameters : {} ",roleAliasDTO);
		String validationResult =  commonConfigurationService.addRoleAliasInformationBasedOrganization(roleAliasDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		RoleAliasDTO  savedRoleAliasInfo = commonConfigurationService.addRoleAliasInformationBasedAdmin(roleAliasDTO);
		if(savedRoleAliasInfo != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.create.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.create.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "LIST ROLE ALIAS NAME BASED ON USER PROFILE", notes = "List Role Alias name is based by user organization profile")
	@GetMapping(value = "/roles/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listRoleAliasInformationBasedOrganization(@ApiParam(value ="User id is required to fetch the belongs the roles alias name list", required = true)@PathVariable Long userId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		if(userId == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.user.id.null"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
		if(profileInfo == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<RoleAliasDTO> roleAliasDTOs = commonConfigurationService.listRoleAliasInformationBasedOrganization(profileInfo.getOrganizationInfo());
		if(roleAliasDTOs != null && roleAliasDTOs.size() > 0){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.list.success"));
			statusResponseDTO.setRoleAliasInfos(roleAliasDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.list.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "UPDATE ROLE ALIAS NAME INFORMATION", notes = "Update Role Alias name information is based on Organization profile")
	@PostMapping(value = "/roles/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  updateRoleAliasInformationBasedOrganization(@ApiParam(value ="Role id and Role_Alias ID, role_alias_name and admin_id is required", required = true)@RequestBody RoleAliasDTO roleAliasDTO ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String validationResult =  commonConfigurationService.updateRoleAliasInformationBasedOrganization(roleAliasDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.update.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "DELETE ROLE ALIAS NAME", notes = "Delete Role Alias Name proffile is based by user organization profile")
	@PostMapping(value = "/roles/delete",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteRoleAliasInformationBasedOrganization(@ApiParam(value ="User id and role alias ids is required to delete role alias profile", required = true)@RequestBody RoleAliasDTO roleAliasDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteResult = commonConfigurationService.deleteRoleAliasInformationBasedOrganization(roleAliasDTO);
		if(!deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		if(deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE ALL ROLE ALIAS NAME INFORMATION", notes = "Delete All Role Alias Name profile is based by user organization profile")
	@DeleteMapping(value = "/roles/delete/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteAllRoleAliasInformationBasedOrganization(@ApiParam(value ="User id is required to delete all role alias name profile", required = true)@PathVariable Long userId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteAllResult = commonConfigurationService.deleteAllRoleAliasInformationBasedOrganization(userId);
		if(!deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteAllResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("role.alias.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "ADD VESSELS TYPE INFORMATION", notes = "Add new vessels type information is based by admin profile")
	@PostMapping(value = "/vessels/type/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  addVesselsTypeInformationBasedAdmin(@ApiParam(value ="Vessels name and admin is required", required = true)@RequestBody VesselsTypeDTO vesselsTypeDTO ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Vessels Type Information creation parameters : {} ",vesselsTypeDTO);
		String validationResult =  commonConfigurationService.addVesselsTypeInformationValidation(vesselsTypeDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		VesselsTypeDTO savedVesselsType = commonConfigurationService.addVesselsTypeInformationBasedOrganizationByAdmin(vesselsTypeDTO);
		if(savedVesselsType != null){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.create.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.create.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "LIST VESSELS TYPE INFORMATION", notes = "List Vessels Type is based by user organization profile")
	@GetMapping(value = "/vessels/type/list/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listVesselsTypeInformationBasedOrganization(@ApiParam(value ="User id is required to fetch the belongs the vessels type list", required = true)@PathVariable Long userId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		if(userId == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.id.null"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		UserProfileInfo profileInfo = userProfileRepository.findOne(userId);
		if(profileInfo == null){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("user.not.found"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		List<VesselsTypeDTO> vesselsTypeDTOs = commonConfigurationService.listVesselsTypeInformationBasedOrganization(profileInfo.getOrganizationInfo());
		if(vesselsTypeDTOs != null && vesselsTypeDTOs.size() > 0){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.list.success"));
			statusResponseDTO.setVesselsTypeInfos(vesselsTypeDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.list.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "UPDATE VESSELS TYPE INFORMATION", notes = "Update Vessels Type information is based on Organization profile")
	@PostMapping(value = "/vessels/type/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  updateVesselsTypeInformationBasedOrganization(@ApiParam(value ="Vessle type_id and vessels type_name and admin_id is required", required = true)@RequestBody VesselsTypeDTO vesselsTypeDTO ){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String validationResult =  commonConfigurationService.updateVesselsTypeInformationBasedOrganization(vesselsTypeDTO);
		if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(validationResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(validationResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.update.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("Vessels.type.update.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "DELETE VESSELS TYPE", notes = "Delete Vessels Type is based by user organization profile")
	@PostMapping(value = "/vessels/type/delete",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteVesselsTypeInformationBasedOrganization(@ApiParam(value ="User id and Vessels type id is required to delete Vessels Type profile", required = true)@RequestBody VesselsTypeDTO vesselsTypeDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteResult = commonConfigurationService.deleteVesselsTypeInformationBasedOrganization(vesselsTypeDTO);
		if(!deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		if(deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("country.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE ALL VESSELS TYPE", notes = "Delete All Vessels Type is based by user organization profile")
	@DeleteMapping(value = "/vessels/type/delete/all/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteAllVesselsTypeInformationBasedOrganization(@ApiParam(value ="User id is required to delete  Vessels type profile", required = true)@PathVariable Long userId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteAllResult = commonConfigurationService.deleteAllVesselsTypeInformationBasedOrganization(userId);
		if(!deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(deleteAllResult);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
		if(deleteAllResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("vessels.type.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "ADD DOCUMENT HOLDER INFORMATION", notes = "Add Document Holder information is based by Super Admin")
	@PostMapping(value = "/document/holder/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  addDocumentHolderInformationBasedSuperAdmin(@ApiParam(value ="Document holder name is required", required = true)@RequestBody DocumentHolderDTO documentHolderDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Document Holder Information creation parameters : {} ",documentHolderDTO);
		try {
			String validationResult =  commonConfigurationService.addDocumentHolderInformationBasedSuperAdmin(documentHolderDTO);
			if(!validationResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(validationResult);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(validationResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("document.holder.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("document.holder.create.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "LIST DOCUMENT HOLDER INFORMATION", notes = "List Document holder information")
	@GetMapping(value = "/document/holder/list", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listDocumentHolderInformation(){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("document.holder.list.success"));
			statusResponseDTO.setDocumentHolderList(commonConfigurationService.listDocumentHolderInformation());
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		}catch(Exception e){
			e.printStackTrace();
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "UPDATE DOCUMENT HOLDER INFORMATION", notes = "Update Document Holder information based Super Admin")
	@PutMapping(value = "/document/holder/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  updateDocumentHolderInformation(@ApiParam(value ="Document Holder id and Documnet Holder", required = true)@RequestBody DocumentHolderDTO documentHolderDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String updateDocumentHolder =  commonConfigurationService.updateDocumentHolderInformation(documentHolderDTO);
			if(!updateDocumentHolder.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(updateDocumentHolder);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(updateDocumentHolder.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("document.holder.update.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("document.holder.update.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE DOCUMENT HOLDER INFO", notes = "Super admin only having the permision to delete Document holder information")
	@DeleteMapping(value = "/document/holder/delete/{userId}/{documentHolderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String> deleteDocumentHolderInfo(@ApiParam(value = "User ID and Document Holder id required")@PathVariable(required = true) Long userId, @PathVariable(required = true) Long documentHolderId){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String deleteDocumentHolder = commonConfigurationService.deleteDocumentHolderInformation(userId, documentHolderId);
			if(!deleteDocumentHolder.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(deleteDocumentHolder);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(deleteDocumentHolder.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("document.holder.delete.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("document.holder.delete.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REPORT AN ISSUE", notes = "Organization Admin Only can able to do this report an issue")
	@PostMapping(value = "/report/an/issue", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  saveReportAnIssueInformation(@ApiParam(value ="UserReportAnIssue Required", required = true)@RequestBody UserReportAnIssueDTO userReportAnIssueDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			String saveReportAnIssue =  commonConfigurationService.saveUserReportAnIssueInformation(userReportAnIssueDTO);
			if(!saveReportAnIssue.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(saveReportAnIssue);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if(saveReportAnIssue.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("report.issue.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("report.issue.create.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "ADD Expiry DOCUMENT CertifcateType INFORMATION", notes = "Add Expiry Document Type information is based by Super Admin")
	@PostMapping(value = "expiry/document/certificateType/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  addExpiryDocumentCertificateTypeInformationBasedSuperAdmin(@ApiParam(value ="Expiry Document Certificate is required", required = true)@RequestBody ExpiryCertificateTypeDTO expiryCertificateTypeDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Expiry DOCUMENT CertifcateType INFORMATION : {} ",expiryCertificateTypeDTO);
		try {
			boolean validData = commonConfigurationService.isValidData(expiryCertificateTypeDTO);
			if (!validData){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String validationResult =  commonConfigurationService.addExpiryDocumentCertifcateTypeBasedSuperAdmin(expiryCertificateTypeDTO);
			if(validationResult.equalsIgnoreCase(env.getProperty("success"))){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("expiry.certificate.create.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("expiry.certificate.create.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@CrossOrigin
	@ApiOperation(value = "Update Expiry DOCUMENT CertifcateType INFORMATION", notes = "Add Expiry Document Type information is based by Super Admin")
	@PostMapping(value = "expiry/document/certificateType/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  updateExpiryDocumentCertificateTypeBasedSuperAdmin(@ApiParam(value ="Expiry Document Certificate is required", required = true)@RequestBody ExpiryCertificateTypeDTO expiryCertificateTypeDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		LOG.debug("New Expiry DOCUMENT CertifcateType INFORMATION : {} ",expiryCertificateTypeDTO);
		try {
			boolean validData = commonConfigurationService.isValidDataForUpdate(expiryCertificateTypeDTO);
			if (!validData){
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			ExpiryCertificateTypeDTO certificateTypeDTO =  commonConfigurationService.updateExpiryDocumentCertifcateTypeBasedSuperAdmin(expiryCertificateTypeDTO);
			if(certificateTypeDTO != null){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("expiry.certificate.update.success"));
				statusResponseDTO.setExpiryCertificateTypeDTO(certificateTypeDTO);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}else {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("expiry.certificate.update.failure"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
		}catch(Exception e) {
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "DELETE MULTIPLE COUNTRY", notes = "Delete Multiple Country only having rights for super admin")
	@PostMapping(value = "expiry/document/certificateType/delete",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  deleteExpiryDocumentCertificateTypeBasedSuperAdmin(@ApiParam(value ="certificate id need to delete the information", required = true)@RequestBody ExpiryCertificateTypeDTO expiryCertificateTypeDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		String deleteResult = commonConfigurationService.deleteExpiryCertificateTypeInfp(expiryCertificateTypeDTO);
		if(!deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}

		if(deleteResult.equalsIgnoreCase(env.getProperty("success"))){
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("expiry.certificate.delete.success"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		}else {
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("expiry.certificate.delete.failure"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "LIST DOCUMENT HOLDER INFORMATION", notes = "List Document holder information")
	@GetMapping(value = "expiry/document/certificateType/list", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listExpiryDocumentCertificateTypeBasedSuperAdmin(){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<ExpiryCertificateTypeDTO> certificateTypeDTOs = new ArrayList<>();
			certificateTypeDTOs = commonConfigurationService.getExpiryCertificateTypeInfo();
			if (certificateTypeDTOs != null && certificateTypeDTOs.size() >0){
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setExpiryCertificateTypeDTOs(certificateTypeDTOs);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setExpiryCertificateTypeDTOs(certificateTypeDTOs);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

	}

	@CrossOrigin
	@ApiOperation(value = "LIST DOCUMENT HOLDER INFORMATION Organization", notes = "List Document holder information organization")
	@PostMapping(value = "/document/holder/list/organization", produces = {"application/json"})
	protected ResponseEntity<String>  listDocumentHolderInformationByorganizationId(@ApiParam(value ="certificate id need to delete the information", required = true)@RequestBody DocumentHolderDTO documentHolderDTO){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try{
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("document.holder.list.success"));
			statusResponseDTO.setDocumentHolderList(commonConfigurationService.listDocumentHolderInformationByOrganization(documentHolderDTO));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

		}catch(Exception e){
			e.printStackTrace();
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@CrossOrigin
	@ApiOperation(value = "Question and Answer INFORMATION", notes = "All List out question and answer informations ")
	@GetMapping(value = "question/list/all", produces = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<String>  listAllQuestionAndAnswer(){
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		List<FaqDTO> faqDTOs = commonConfigurationService.findAllQuestionAndAnswerDetails();
		statusResponseDTO.setStatus(env.getProperty("success"));
		statusResponseDTO.setMessage(env.getProperty("port.list.success"));
		statusResponseDTO.setFaqInfos(faqDTOs);
		return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
	}
}
