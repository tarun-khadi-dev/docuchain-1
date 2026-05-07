package com.dapp.docuchain.controller;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import com.dapp.docuchain.dto.StatusResponseDTO;
import com.dapp.docuchain.service.ExpiryDocumentService;
import com.dapp.docuchain.service.GroupTagService;
import com.dapp.docuchain.utility.ExpiryDocumentUtility;
import com.dapp.docuchain.utility.GroupTagUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/docuchain/api/group")
@Api(value = "GroupTagController ", description = "Group Expiry Document Controller API")
@CrossOrigin
public class GroupTagController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupTagController.class);

    @Autowired
    ExpiryDocumentService expiryDocumentService;

    @Autowired
    ExpiryDocumentUtility expiryDocumentUtility;

    @Autowired
    GroupTagUtils groupTagUtils;

    @Autowired
    GroupTagService groupTagService;

    @Autowired
    private Environment env;

    @CrossOrigin
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "CREATE NEW GROUP", notes = "This Mehtord is used to add new CustomerGroup ")
    public ResponseEntity<String> createNewGroup(@ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
            LOGGER.info("user id is" + groupTagDTO.getUserProfileId());
            if (!groupTagUtils.isValidateTagGroupParam(groupTagDTO)) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            String response = groupTagService.validateSourceExist(groupTagDTO);
            if (!response.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(response);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            boolean isSaved = groupTagService.saveGroupTag(groupTagDTO);
            if (isSaved) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("group.create.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.create.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

            }
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/add/exp", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "add new Group tag", notes = "This Mehtord is used to add new CustomerGroup ")
    public @ResponseBody
    ResponseEntity<String> createGroup(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
            LOGGER.info("user id is" + groupTagDTO.getUserProfileId());
            String verfiyParamResponse = groupTagUtils.validateSourceExistForAddExp(groupTagDTO);
            if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(verfiyParamResponse);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            String response = groupTagService.validateSourceExistForAddExp(groupTagDTO);
            if (!response.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(response);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            boolean isSaved = groupTagService.saveGroupwithExp(groupTagDTO);
            if (isSaved) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("share.group.create.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("share.group.create.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

            }
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/update/exp", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "add new Group tag", notes = "This Mehtord is used to add new CustomerGroup ")
    public @ResponseBody
    ResponseEntity<String> updateGroupExp(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
        	LOGGER.info("user id is" + groupTagDTO.getUserProfileId());
            if (!groupTagUtils.isvalidateUpdateExpGroupParam(groupTagDTO)) {
                statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isGroupExts = groupTagService.isGroupExts(groupTagDTO.getGroupId());
            if (!isGroupExts) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isSaved = groupTagService.updateGroupTagwithExp(groupTagDTO);
            if (isSaved) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("share.group.create.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("share.group.create.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

            }
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "update group tag", notes = "This Mehtord is used to add new CustomerGroup ")
    public @ResponseBody
    ResponseEntity<String> updateGroup(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
            LOGGER.info("user id is" + groupTagDTO.getUserProfileId());
            if (!groupTagUtils.isValidateUpdateTagGroupParam(groupTagDTO)) {
                statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isGroupExts = groupTagService.isGroupExts(groupTagDTO.getId());
            if (!isGroupExts) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String response = groupTagService.validateUpdateGroupParam(groupTagDTO);
            if (!response.equalsIgnoreCase("Success")) {
                statusResponseDTO.setMessage(response);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isSaved = groupTagService.updateGroupTag(groupTagDTO);
            if (isSaved) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("group.update.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.update.failure"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/all/{userprofileid}", method = RequestMethod.GET, produces = {"application/json"})
    @ApiOperation(value = "get All  Group Details", notes = "This Mehtord is used to get All  Group Details based on user profile id")
    public @ResponseBody
    ResponseEntity<String> getAllRole(@ApiParam(value = "Required user profileId", required = true) @PathVariable(value = "userprofileid") Integer userProfileId) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            boolean isUserProfileIdExits = groupTagService.isUserProfileIdExits(userProfileId.longValue());
            if (!isUserProfileIdExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("user.profileid.not.exits"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            List<GroupTagDTO> listAllGroup = groupTagService.getAllGroupInfo(userProfileId);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setUserProfileId(userProfileId.longValue());
            statusResponseDTO.setGroupList(listAllGroup);
            statusResponseDTO.setMessage(env.getProperty("customer.groups.available"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Problem in getAllCustomerGroupInfo  : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

//    @CrossOrigin
//    @RequestMapping(value = "/shipgrouplist", method = RequestMethod.GET, produces = {"application/json"})
//    @ApiOperation(value = "get All  Group Details", notes = "This Mehtord is used to get All  Group Details based on user profile id")
//    public @ResponseBody
//    ResponseEntity<String> getShipGroupList(@ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {
//        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
//        try {
//
//            boolean isUserProfileIdExits = groupTagService.isUserProfileIdExits(groupTagDTO.getUserProfileId());
//            if (!isUserProfileIdExits) {
//                statusResponseDTO.setStatus(env.getProperty("failure"));
//                statusResponseDTO.setMessage(env.getProperty("user.profileid.not.exits"));
//                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//            }
//            List<GroupTagDTO> listAllGroup = groupTagService.getAllGroupInfo(userProfileId);
//            statusResponseDTO.setStatus(env.getProperty("success"));
//            statusResponseDTO.setUserProfileId(userProfileId.longValue());
//            statusResponseDTO.setGroupList(listAllGroup);
//            statusResponseDTO.setMessage(env.getProperty("customer.groups.available"));
//            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
//        } catch (Exception e) {
//            LOGGER.error("Problem in getAllCustomerGroupInfo  : ", e);
//            statusResponseDTO.setStatus(env.getProperty("failure"));
//            statusResponseDTO.setMessage(env.getProperty("server.problem"));
//            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//        }
//    }


    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "update group tag", notes = "This Mehtord is used to add new CustomerGroup ")
    public @ResponseBody
    ResponseEntity<String> groupList(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            String shipExists = groupTagService.validateShipExists(groupTagDTO.getShipId());
            if (!shipExists.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(shipExists);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            boolean isUserProfileIdExits = groupTagService.isUserProfileIdExits(groupTagDTO.getUserProfileId());
            if (!isUserProfileIdExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("user.profileid.not.exits"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            List<GroupTagDTO> listAllGroup = groupTagService.getAllGroupInfoByShip(groupTagDTO);
            if (listAllGroup != null && listAllGroup.size() > 0) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                //statusResponseDTO.setUserProfileId(userProfileId.longValue());
                statusResponseDTO.setGroupList(listAllGroup);
                statusResponseDTO.setMessage(env.getProperty("ship.group.available"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            //statusResponseDTO.setUserProfileId(userProfileId.longValue());
            statusResponseDTO.setMessage(env.getProperty("ship.group.notavailable"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            LOGGER.error("Problem in getAllCustomerGroupInfo  : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }
   @PostMapping("/add-multiple")
    public ResponseEntity<String> addMultipleExpiryDocsToGroup(@RequestBody GroupTagDTO groupTagDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            boolean updated = groupTagService.updateGroupTagwithExp(groupTagDTO);

            if (updated) {
                statusResponseDTO.setStatus("Success");
                statusResponseDTO.setMessage("Documents added to group successfully");
                return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }

            statusResponseDTO.setStatus("Failure");
            statusResponseDTO.setMessage("Failed to add documents");
            return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            e.printStackTrace();
            statusResponseDTO.setStatus("Failure");
            statusResponseDTO.setMessage("Server error");
            return new ResponseEntity<>(new Gson().toJson(statusResponseDTO), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @CrossOrigin
    @PostMapping(value = "/delete", produces = {"application/json"})
    @ApiOperation(value = "Delete tag group ", notes = "This Method is used to deletetaggroup")
    public @ResponseBody
    ResponseEntity<String> deleteTag(
            @ApiParam(value = "Required tag Id ", required = true)@RequestBody GroupTagDTO groupTagDTO) {

        LOGGER.info("Inside tag controller Method in deletetag");
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            boolean isGroupExits = groupTagService.isGroupExts(groupTagDTO.getGroupId());
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean groupInfoDelete = groupTagService.deleteGroup(groupTagDTO);
            if (groupInfoDelete) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("group.delete.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("group.delete.failure"));

            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            LOGGER.info("problem  in gettaglist Method in TagController ");
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/add/exiprydocument", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "add new exiprydocument in group", notes = "This Mehtord is used to add new CustomerGroup ")
    public @ResponseBody
    ResponseEntity<String> addExpiryDouments(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {

        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus(env.getProperty("failure"));
        try {
            LOGGER.info("user id is" + groupTagDTO.getUserProfileId());
            if (!groupTagUtils.isValidateExpiryTagGroupParam(groupTagDTO)) {
                statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            String response = groupTagService.isValidateExpiryTagGroupParamExist(groupTagDTO);
            if (!response.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(response);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            String isExpiryTagGroupExits = groupTagService.isExpiryTagGroupExits(groupTagDTO);
            if (!isExpiryTagGroupExits.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(isExpiryTagGroupExits);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

            boolean isSaved = groupTagService.saveExpiryTagGroup(groupTagDTO);
            if (isSaved) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("expiry.group.create.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("expiry.group.create.failed"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

            }
        } catch (Exception e) {
            LOGGER.info("Exception is" + e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "delete/exiprydocument", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "Delete Exipry Document InGroup ", notes = "This Method is used to deletetaggroup")
    public @ResponseBody
    ResponseEntity<String> deleteExipryDocumentInGroup(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {
        LOGGER.info("Inside tag controller Method in deletetag");
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            String isExpiryTagGroupExits = groupTagService.isExpiryTagGroupExitsOrNot(groupTagDTO);
            if (!isExpiryTagGroupExits.equalsIgnoreCase("Success")) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(isExpiryTagGroupExits);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean groupInfoDelete = groupTagService.deleteExipryDocumentInGroup(groupTagDTO);
            if (groupInfoDelete) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("group.expDoc.delete.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            }
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("group.expDoc.delete.failure"));

            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        } catch (Exception e) {
            LOGGER.info("problem  in gettaglist Method in TagController ");
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/expirydocument/{groupid}", method = RequestMethod.GET, produces = {"application/json"})
    @ApiOperation(value = "get All  Group Details", notes = "This Mehtord is used to get All  Group Details based on user profile id")
    public @ResponseBody
    ResponseEntity<String> getAllExpiredocumentinGroup(@ApiParam(value = "Required user profileId", required = true) @PathVariable(value = "groupid") Long groupId) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            boolean isGroupExits = groupTagService.isGroupExts(groupId);
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            /*boolean isGroupExpiredocumentExits = groupTagService.isGroupExpiredocumentExits(groupId);
            if (!isGroupExpiredocumentExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("This group dont have expire documents");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }*/
            List<ExpiryDocumentDTO> listAllGroup = groupTagService.getAllExpiredocumentinGroup(groupId);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setExpiryDocumentList(listAllGroup);
            statusResponseDTO.setMessage(env.getProperty("customer.groups.available"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Problem in getAllExpiredocumentinGroup  : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "sentitems/{groupid}", method = RequestMethod.GET, produces = {"application/json"})
    @ApiOperation(value = "get All  Group Details", notes = "This Mehtord is used to get All  Group Details based on user profile id")
    public @ResponseBody
    ResponseEntity<String> getAllSharedExpiryDcoumentinGroup(@ApiParam(value = "Required user profileId", required = true) @PathVariable(value = "groupid") Long groupId) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {

            boolean isGroupExits = groupTagService.isForwardExts(groupId);
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isGroupExpiredocumentExits = groupTagService.isGroupForwardExpiredocumentExits(groupId);
            if (!isGroupExpiredocumentExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("This group dont have expire documents");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            List<GroupTagDTO> listAllGroup = groupTagService.getAllSenItemsInGroup(groupId);
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setGroupTagListSentItems(listAllGroup);
            statusResponseDTO.setMessage(env.getProperty("customer.groups.available"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Problem in getAllSharedExpiryDcoumentinGroup  : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "getlist/shipexiprydocument", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "get List Based On ShipId Exipry Document In Group ", notes = "This Method is used to deletetaggroup")
    public @ResponseBody
    ResponseEntity<String> getListBasedOnShipID(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {
        LOGGER.info("Inside tag controller Method in deletetag");
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            boolean isUserProfileIdExits = groupTagService.isUserProfileIdExits(groupTagDTO.getUserProfileId());
            if (!isUserProfileIdExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("user.profileid.not.exits"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isGroupExits = groupTagService.isGroupExts(groupTagDTO.getGroupId());
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            List<ExpiryDocumentDTO> listAllGroup = groupTagService.getShipExipryDocumentInGroup(groupTagDTO);
            if (listAllGroup != null && listAllGroup.size() > 0) {
                statusResponseDTO.setExpiryDocumentList(listAllGroup);
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setUserProfileId(groupTagDTO.getUserProfileId());
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("Ship expiry document not exits in group");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

        } catch (Exception e) {
            LOGGER.error("get List Based On ShipId Exipry Document In Group : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }


    @CrossOrigin
    @RequestMapping(value = "getlist/documentdataexiprydocument", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "get List Based On ShipId Exipry Document In Group ", notes = "This Method is used to deletetaggroup")
    public @ResponseBody
    ResponseEntity<String> getDocumentDataBasedExpiryList(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {
        LOGGER.info("Inside tag controller Method in deletetag");
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            boolean isUserProfileIdExits = groupTagService.isUserProfileIdExits(groupTagDTO.getUserProfileId());
            if (!isUserProfileIdExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("user.profileid.not.exits"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isGroupExits = groupTagService.isGroupExts(groupTagDTO.getGroupId());
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            List<ExpiryDocumentDTO> listAllGroup = groupTagService.getDocumentDataBasedExpiryList(groupTagDTO);
            if (listAllGroup != null && listAllGroup.size() > 0) {
                statusResponseDTO.setExpiryDocumentList(listAllGroup);
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setUserProfileId(groupTagDTO.getUserProfileId());
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("Document data expiry document not exits in group");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

        } catch (Exception e) {
            LOGGER.error("get List Based On ShipId Exipry Document In Group : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }

    /*@CrossOrigin
    @RequestMapping(value = "getlist/documentdata/shipprofile", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation(value = "get List Based On ShipId Exipry Document In Group ", notes = "This Method is used to deletetaggroup")
    public @ResponseBody
    ResponseEntity<String> getDocumentDataAndShipBasedExpiryList(
            @ApiParam(value = "Required Partners Details", required = true) @RequestBody GroupTagDTO groupTagDTO) {
        LOGGER.info("Inside tag controller Method in deletetag");
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        try {
            boolean isUserProfileIdExits = groupTagService.isUserProfileIdExits(groupTagDTO.getUserProfileId());
            if (!isUserProfileIdExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("user.profileid.not.exits"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            boolean isGroupExits = groupTagService.isGroupExts(groupTagDTO.getGroupId());
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            List<ExpiryDocumentDTO> listAllGroup = groupTagService.getDocumentDataAndShipBasedExpiryList(groupTagDTO);
            if (listAllGroup != null && listAllGroup.size() > 0) {
                statusResponseDTO.setExpiryDocumentList(listAllGroup);
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setUserProfileId(groupTagDTO.getUserProfileId());
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage("expiry document not exits in group");
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }

        } catch (Exception e) {
            LOGGER.error("get List Based On ShipId Exipry Document In Group : ", e);
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
    }
*/

    @CrossOrigin
    @PostMapping(value = "/share", produces = {
            "application/json"})
    @ApiOperation(value = "Share document", notes = "Share expiry document to email")
    public ResponseEntity<String> shareGroupExpiryDocument(
            @ApiParam(value = "ExpiryDocumentInfo ", required = true) @RequestBody GroupTagDTO GroupTagDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
            String verfiyShareParam = groupTagService.verifyShareDocumentParam(GroupTagDTO);
            if (!verfiyShareParam.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setMessage(verfiyShareParam);
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
            }
            boolean isGroupExits = groupTagService.isGroupExts(GroupTagDTO.getId());
            if (!isGroupExits) {
                statusResponseDTO.setStatus(env.getProperty("failure"));
                statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
            }
            String shareResponse = groupTagService.shareExpiryDocument(GroupTagDTO);
            if (shareResponse.equalsIgnoreCase(env.getProperty("success"))) {
                statusResponseDTO.setStatus(env.getProperty("success"));
                statusResponseDTO.setMessage(env.getProperty("share.group.success"));
                return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
            } else {
                statusResponseDTO.setMessage(env.getProperty("share.group.failed"));
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
    @GetMapping(value = "/view/{groupid}", produces = {"application/json"})
    @ApiOperation(value = "TaskStatusList", notes = "This Mehtord is used to get TaskStatusList ")
    public @ResponseBody
    ResponseEntity<String> viewGroup(@ApiParam(value = "Required group id", required = true) @PathVariable(value = "groupid") Long groupId) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        boolean isGroupExits = groupTagService.isGroupExts(groupId);
        if (!isGroupExits) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        GroupTagDTO viewGroup = groupTagService.viewGroup(groupId);
        if (viewGroup != null) {
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setGroupInfo(viewGroup);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        }
        statusResponseDTO.setMessage(" Group Details Not Available");
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    }

    @CrossOrigin
    @PostMapping(value = "/check/share", produces = {"application/json"})
    @ApiOperation(value = "check Group Exists While Sharing", notes = "check Group Exists While Sharing expiry document")
    public ResponseEntity<String> checkGroupExistsWhileSharing(
            @ApiParam(value = "GroupTagInfo ", required = true) @RequestBody GroupTagDTO groupTagDTO) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setStatus("failure");
        try {
        	 String verfiyParamResponse = expiryDocumentUtility.verifyGroupDocumentShareParam(groupTagDTO);
             if (!verfiyParamResponse.equalsIgnoreCase(env.getProperty("success"))) {
                 statusResponseDTO.setMessage(verfiyParamResponse);
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
             }
             List<ExpiryDocumentDTO> listAllGroup = groupTagService.checkGroupAlreadyExists(groupTagDTO);
             if (listAllGroup != null && listAllGroup.size() > 0) {
                 statusResponseDTO.setExpiryDocumentList(listAllGroup);
                 statusResponseDTO.setStatus(env.getProperty("success"));
                 statusResponseDTO.setMessage(env.getProperty("group.share.exists"));
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
             }
             else {
                 statusResponseDTO.setMessage(env.getProperty("group.share.not.exists"));
                 return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
             }
        }
        catch (Exception e) {
            LOGGER.info("problem in GroupTagController method in checkGroupExistsWhileSharing");
            e.printStackTrace();
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("server.problem"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
        }

    }
    @CrossOrigin
    @GetMapping(value = "/remainingdocument/{groupid}", produces = {"application/json"})
    @ApiOperation(value = "remaining Document to add group", notes = "This Mehtord is used to get remaining Document to add group")
    public @ResponseBody
    ResponseEntity<String> remainingDocument(@ApiParam(value = "Required group id", required = true) @PathVariable(value = "groupid") Long groupId) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        boolean isGroupExits = groupTagService.isGroupExts(groupId);
        if (!isGroupExits) {
            statusResponseDTO.setStatus(env.getProperty("failure"));
            statusResponseDTO.setMessage(env.getProperty("group.not.exists"));
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
        }
        GroupTagDTO viewGroup = groupTagService.remainingDocument(groupId);
        if (viewGroup != null) {
            statusResponseDTO.setStatus(env.getProperty("success"));
            statusResponseDTO.setGroupInfo(viewGroup);
            return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
        }
        statusResponseDTO.setMessage(" Group Details Not Available");
        return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
    }

}
