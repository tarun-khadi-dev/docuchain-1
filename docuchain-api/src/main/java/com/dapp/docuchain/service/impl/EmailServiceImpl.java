// package com.dapp.docuchain.service.impl;

// import com.dapp.docuchain.dto.ExpiryDocumentDTO;
// import com.dapp.docuchain.dto.GroupTagDTO;
// import com.dapp.docuchain.model.UserReportAnIssueInfo;
// import com.dapp.docuchain.service.EmailService;
// import com.dapp.docuchain.service.FileService;
// import org.apache.commons.codec.binary.Base64;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
// import org.springframework.mail.MailException;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.stereotype.Service;
// import javax.activation.DataHandler;
// import javax.activation.DataSource;
// import javax.mail.*;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeBodyPart;
// import javax.mail.internet.MimeMessage;
// import javax.mail.internet.MimeMultipart;
// import javax.mail.util.ByteArrayDataSource;
// import java.util.List;
// import java.util.Properties;
// import java.time.Year;

// @Service
// public class EmailServiceImpl implements EmailService {

//     private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

//     @Autowired
//     FileService fileService;

//     @Autowired
//     private Environment env;

//     // Add this new variable right here!
//     @Value("${docuchain.email.logo.url}")
//     private String logoUrl;

//     // ... rest of your code ...

// @Service
// public class EmailServiceImpl implements EmailService {

//     private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

//     @Autowired
//     FileService fileService;

//     @Autowired
//     private Environment env;

//     public boolean sendEmailDEV(String toEamilId, String subject, String content) {
// 		try {
// 			SimpleMailMessage mail = new SimpleMailMessage();
// 			mail.setTo(toEamilId);
// 			mail.setFrom(env.getProperty("email.username"));
// 			mail.setSubject(subject);
// 			Properties properties = new Properties();
// 			properties.put("mail.smtp.host", env.getProperty("email.host"));
// 			properties.put("mail.smtp.port", env.getProperty("email.port"));
// 			properties.put("mail.smtp.auth", "true");
// 			properties.put("mail.smtp.starttls.enable", "true");
// 			properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
// 			// creates a new session with an authenticator
// 			Authenticator auth = new Authenticator() {
// 				public PasswordAuthentication getPasswordAuthentication() {
// 					return new PasswordAuthentication(env.getProperty("email.username"),
// 							env.getProperty("email.password"));
// 				}
// 			};
// 			Session session = Session.getInstance(properties, auth);
// 			Message message = new MimeMessage(session);
// 			message.setFrom(new InternetAddress(env.getProperty("email.username")));
// 			InternetAddress[] toAddresses = { new InternetAddress(toEamilId) };
// 			message.setRecipients(Message.RecipientType.TO, toAddresses);
// 			message.setSubject(subject);
// 			// This mail has 2 part, the BODY and the embedded image
// 			MimeMultipart multipart = new MimeMultipart("related");
// 			// first part (the html)
// 			BodyPart messageBodyPart = new MimeBodyPart();
// 			messageBodyPart.setContent(content, "text/html");
// 			multipart.addBodyPart(messageBodyPart);
// 			// put everything together
// 			message.setContent(multipart);
// 			Transport.send(message);
// 			LOG.info("Email sent successfully !");
// 			return true;
// 		} catch (MailException e) {
// 			LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
// 			e.printStackTrace();
// 			return false;
// 		} catch (Exception e) {
// 			LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
// 			e.printStackTrace();
// 			return false;
// 		}
// 	}

//     @Override
//     public String SendEmailWithAttachment(ExpiryDocumentDTO expiryDocumentDTO) {

//         String toEamilId = expiryDocumentDTO.getEmailIds();
//         // 1) get the session object
//         try {
//             Properties properties = System.getProperties();
//             properties.put("mail.smtp.host", env.getProperty("email.host"));
//             properties.put("mail.smtp.port", env.getProperty("email.port"));
//             properties.put("mail.smtp.auth", "true");
//             properties.put("mail.smtp.starttls.enable", "true");
//             properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
//             Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
//                 protected PasswordAuthentication getPasswordAuthentication() {
//                     return new PasswordAuthentication(env.getProperty("email.username"),
//                             env.getProperty("email.password"));
//                 }
//             });
//             MimeMessage message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(env.getProperty("email.username")));
//             InternetAddress[] parse = InternetAddress.parse(toEamilId, true);
//             message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
//             message.setSubject("Message Aleart");

//             // 3) create MimeBodyPart object and set your message text
//             BodyPart messageBodyPart1 = new MimeBodyPart();
//             messageBodyPart1.setText("This is message body");

//             String documentName = expiryDocumentDTO.getDocumentDataInfo().getDocumentName();
// 			String documentHashCode = expiryDocumentDTO.getDocumentDataInfo().getDocumentHashCode();
//             String fileResponse = fileService.fileRetriveFromStorej(documentName, documentHashCode);

//             byte[] decodedBytes = Base64.decodeBase64(fileResponse);
//             LOG.info(decodedBytes.toString());
//             DataSource dataSource = null;
//             if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("pdf")) {
//                 dataSource = new ByteArrayDataSource(decodedBytes, "application/pdf");
//             } else {
//                 dataSource = new ByteArrayDataSource(decodedBytes, "application/jpg");
//             }
//             MimeBodyPart pdfBodyPart = new MimeBodyPart();
//             pdfBodyPart.setDataHandler(new DataHandler(dataSource));
//             pdfBodyPart.setFileName(expiryDocumentDTO.getDocumentName() + "." + expiryDocumentDTO.getFileExtension());

//             // 5) create Multipart object and add MimeBodyPart objects to this object
//             Multipart multipart = new MimeMultipart();
//             multipart.addBodyPart(messageBodyPart1);
//             multipart.addBodyPart(pdfBodyPart);

//             // 6) set the multiplart object to the message object
//             message.setContent(multipart);

//             // 7) send message
//             Transport.send(message);
//         } catch (MessagingException e) {
//             LOG.error("SendEmailWithAttachment" + e);
//             e.printStackTrace();
//             return "failed";
//         }

//         return "success";
//     }

//     @Override
//     public boolean forgotPasswordNotificationEmail(String mail, String username, String forgetPassword) {

//         try {
//             Properties properties = System.getProperties();
//             properties.put("mail.smtp.host", env.getProperty("email.host"));
//             properties.put("mail.smtp.port", env.getProperty("email.port"));
//             properties.put("mail.smtp.auth", "true");
//             properties.put("mail.smtp.starttls.enable", "true");
//             properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
//             Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
//                 protected PasswordAuthentication getPasswordAuthentication() {
//                     return new PasswordAuthentication(env.getProperty("email.username"),
//                             env.getProperty("email.password"));
//                 }
//             });
//             MimeMessage message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(env.getProperty("email.username")));
//             InternetAddress[] parse = InternetAddress.parse(mail, true);
//             message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
//             message.setSubject("Docuchain Password Reset");

//             // 3) create MimeBodyPart object and set your message text
//             BodyPart messageBodyPart = new MimeBodyPart();
//             messageBodyPart.setText("This is message body");

//             String htmlText = "<!-- saved from url=(0022)http://internet.e-mail -->\n" + "<!DOCTYPE html>\n" + "\n"
//                     + "<head>\n" + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
//                     + "  <title>::Docuchain::</title>\n" + "</head>\n" + "<!-- bgcolor=\"#5f77ad\" -->\n" + "\n"
//                     + "<body>\n"
//                     + "  <table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n"
//                     + "    <tr style=\"background:#5677b2\">\n"
//                     /*+ "      <td align=\"left\" valign=\"middle\" style=\"padding:20px; color:#ffffff;\">\n"
//                     + "        <img src=\"http://cipldesigns.colaninfotechapps.in/logo.png\" width=\"200\" style=\"display:block;\">\n"
//                     + "      </td>\n"*/
// /*                    + "      <td align=\"right\" valign=\"middle\" style=\"padding:20px; color:#ffffff;\">\n"
//                     + "        <img src=\"http://cipldesigns.colaninfotechapps.in/logod.png\" width=\"120\" style=\"display:block;\">\n"
//                     + "      </td>\n" */
//                     + "    </tr>\n" + "  </table>\n"
//                     + "  <table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#971800\" style=\"background-color:#971800; border:1px solid #dddddd;\">\n"
//                     + "    <tr>\n"
//                     + "      <td align=\"center\" valign=\"top\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;\">\n"
//                     + "        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
//                     + "          <tr>\n"
//                     + "            <td align=\"left\" valign=\"top\" bgcolor=\"#971800\" style=\"background-color:#8e3d57; padding:20px; font-family:Arial, Helvetica, sans-serif; \">\n"
//                     + "              <div style=\"color:#ffffff; font-size:18px; font-weight: bold;\">Forgot password</div>\n"
//                     + "            </td>\n" + "          </tr>\n" + "          <tr>\n"
//                     + "            <td align=\"left\" valign=\"top\" bgcolor=\"#f7f7f7\" style=\"background-color:#f7f7f7; padding:20px;\">\n"
//                     + "              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\" style=\"margin-bottom:10px;\">\n"
//                     + "                <tr>\n"
//                     + "                  <td align=\"left\" valign=\"top\" style=\"font-family:Arial, Helvetica, sans-serif; font-size:13px; font-weight:600; color:#53231a;\">\n"
//                     + "                    <p style=\"margin:0 0 12px 0; font-size: 15px;\">Hi " + username + ",<br /> \n "
//                     + "                      <span style=\"font-weight:300; font-size: 14px;\">Your new password for accessing docuchain.</span>\n"
//                     + "                    </p>\n" + "     <div style=\" background:#ededed;padding:2px 10px;\">\n"
//                     + "     <p style=\"line-height:22px; font-family:Arial, Helvetica, sans-serif; font-size:13px; font-weight:600; color:#660066;\">\n"
//                     + "        <b>E-mail&nbsp;:</b>&nbsp;&nbsp;&nbsp;" + mail + " <br>\n"
//                     + "        <b>User Name&nbsp;:</b>&nbsp;&nbsp;&nbsp;" + username + " <br>\n"
//                     + "        <b>Password&nbsp;&nbsp;&nbsp;&nbsp;:</b>&nbsp;&nbsp;&nbsp;" + forgetPassword + " <br>\n"
//                     + "     </p>\n" + "    </div>\n" + "    <p style=\"margin:10px 0 10px 0;\">\n"
//                     + "                      <span style=\"font-weight:300; font-size: 14px;\"> Please do not share this password with anyone. In case you haven't requested this password reset, please contact your administrator.</span>\n"
//                     + "                    </p>\n"
// //                    + "     <p style=\"margin:0 0 0 0;\">\n"
// //                    + "                      <span style=\"font-weight:300; font-size: 11px;\"> We hope that you will have a wonderful experience using our website. Any suggestions or feedback is more than welcome, we are always happy to hear from you in order to improve our services. </span>\n"
// //                    + "                    </p>\n"
//                     + "                  </td>\n" + "                </tr>\n"
//                     + "                <tr>\n" + "                  <td>\n"
//                     + "                    <h1 style=\"font-family:Arial, Helvetica, sans-serif; font-weight:600; color:#800000; font-size: 15px; margin-top: 15px; margin-bottom:0; padding: 0;\">Thanks,</h1>\n"
//                     + "                    <h2 style=\"font-family:Arial, Helvetica, sans-serif; color:#001a4d; font-weight:400; font-size: 15px; margin:5px 0 3px 0; padding: 0;\">DocuChain</h2>\n"
//                     + "     <p style=\"margin:0 0 0 0;\">\n"
//                     + "                      <span style=\"font-size: 15px;\"><a href=\"https://www.docuchain.sg/\">www.docuchain.sg</a></span>\n"
//                     + "                    </p>\n" + "                  </td>\n" + "                </tr>\n"
//                     + "              </table>\n" + "            </td>\n" + "          </tr>\n"
//                     + "    	   <tr style=\"background:#5677b2\">\n"
//                     + "            <td align=\"center\" valign=\"middle\" style=\" padding:15px;\">\n"
//                     + "              <p style=\"color:#ffffff;font-family:calibri;\">&copy; Copyright 2018. All Rights Reserved.</p>\n"
//                     + "            </td>\n" + "          </tr>\n" + "        </table>\n" + "      </td>\n"
//                     + "    </tr>\n" + "  </table>\n" + "</body>\n" + "\n" + "</html>";

//             messageBodyPart.setContent(htmlText, "text/html");
//             Multipart multipart = new MimeMultipart();
//             multipart.addBodyPart(messageBodyPart);

//             // 6) set the multiplart object to the message object
//             message.setContent(multipart);

//             // 7) send message
//             Transport.send(message);
//         } catch (MessagingException e) {
//             LOG.error("SendEmailWithAttachment" + e);
//             e.printStackTrace();
//             return false;
//         }

//         return true;
//     }

//    /* @Override
//     public String shareExpDoc(GroupTagDTO groupTagDTO) {

//         String toEamilId = groupTagDTO.getEmailId();
//         // 1) get the session object
//         try {
//             Properties properties = System.getProperties();
//             properties.put("mail.smtp.host", env.getProperty("email.host"));
//             properties.put("mail.smtp.port", env.getProperty("email.port"));
//             properties.put("mail.smtp.auth", "true");
//             properties.put("mail.smtp.starttls.enable", "true");
//             properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
//             Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
//                 protected PasswordAuthentication getPasswordAuthentication() {
//                     return new PasswordAuthentication(env.getProperty("email.username"),
//                             env.getProperty("email.password"));
//                 }
//             });
//             MimeMessage message = new MimeMessage(session);
//             message.setFrom(new InternetAddress(env.getProperty("email.username")));
//             InternetAddress[] parse = InternetAddress.parse(toEamilId, true);
//             message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
//             message.setSubject("Message Aleart");

//             // 3) create MimeBodyPart object and set your message text
//             BodyPart messageBodyPart1 = new MimeBodyPart();
//             messageBodyPart1.setText("This is message body");
//             Multipart multipart = new MimeMultipart();
//             for (DocumentHolderInfo groupDTO : groupTagDTO.getDocumentHolderInfos()) {

//                 String fileResponse = fileService.fileRetriveFromStorej(groupDTO.getDocumentName());
//                 byte[] decodedBytes = Base64.decodeBase64(fileResponse);
//                 LOG.info(decodedBytes.toString());
//                 DataSource dataSource = null;
//                 if (groupDTO.getDocumentHolderInfos().getDocumentFormat().equalsIgnoreCase("pdf")) {
//                     dataSource = new ByteArrayDataSource(decodedBytes, "application/pdf");
//                 } else {
//                     dataSource = new ByteArrayDataSource(decodedBytes, "application/jpg");
//                 }

//                 MimeBodyPart pdfBodyPart = new MimeBodyPart();
//                 pdfBodyPart.setDataHandler(new DataHandler(dataSource));
//                 pdfBodyPart.setFileName(
//                         groupDTO.getDocumentName() + "." + groupDTO.getDocumentDataInfo().getDocumentFormat());
//                 multipart = new MimeMultipart();
//                 multipart.addBodyPart(messageBodyPart1);
//                 multipart.addBodyPart(pdfBodyPart);

//             }

//             // 5) create Multipart object and add MimeBodyPart objects to this object

//             // 6) set the multiplart object to the message object
//             message.setContent(multipart);

//             // 7) send message
//             Transport.send(message);
//         } catch (MessagingException e) {
//             LOG.error("SendEmailWithAttachment" + e);
//             e.printStackTrace();
//             return "failed";
//         }

//         return "success";
//     }*/

// 	@Override
// 	public String shareGroupExpiryDocument(GroupTagDTO groupDTO) {
// 		 String toEamilId = groupDTO.getEmailId();
// 	        // 1) get the session object
// 	        try {
// 	            Properties properties = System.getProperties();
// 	            properties.put("mail.smtp.host", env.getProperty("email.host"));
// 	            properties.put("mail.smtp.port", env.getProperty("email.port"));
// 	            properties.put("mail.smtp.auth", "true");
// 	            properties.put("mail.smtp.starttls.enable", "true");
// 	            properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
// 	            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
// 	                protected PasswordAuthentication getPasswordAuthentication() {
// 	                    return new PasswordAuthentication(env.getProperty("email.username"),
// 	                            env.getProperty("email.password"));
// 	                }
// 	            });
// 	            MimeMessage message = new MimeMessage(session);
// 	            message.setFrom(new InternetAddress(env.getProperty("email.username")));
// 	            InternetAddress[] parse = InternetAddress.parse(toEamilId, true);
// 	            message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
// 	            message.setSubject("Message Aleart");

// 	            // 3) create MimeBodyPart object and set your message text
// 	            BodyPart messageBodyPart1 = new MimeBodyPart();
// 	            messageBodyPart1.setText("This is message body");
// 	            Multipart  multipart = new MimeMultipart();
//                 multipart.addBodyPart(messageBodyPart1);
// 	            List<ExpiryDocumentDTO> expiryDocumentDTOList=groupDTO.getExpiryDocumentDtos();
// 	            if(expiryDocumentDTOList!=null)
// 	            	for(ExpiryDocumentDTO expiryDocumentDTO:expiryDocumentDTOList)
// 	            	{
//                         String documentName = expiryDocumentDTO.getDocumentDataInfo().getDocumentName();
//                         String documentHashCode = expiryDocumentDTO.getDocumentDataInfo().getDocumentHashCode();
//                         String fileResponse = fileService.fileRetriveFromStorej(documentName, documentHashCode);

//                         byte[] decodedBytes = Base64.decodeBase64(fileResponse);
//                         LOG.info(decodedBytes.toString());
//                         DataSource dataSource = null;
//                         if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("pdf")) {
//                             dataSource = new ByteArrayDataSource(decodedBytes, "application/pdf");
//                         } else {
//                             dataSource = new ByteArrayDataSource(decodedBytes, "application/jpg");
//                         }

//                         MimeBodyPart pdfBodyPart = new MimeBodyPart();
//                         pdfBodyPart.setDataHandler(new DataHandler(dataSource));
//                         pdfBodyPart.setFileName(
//                                 expiryDocumentDTO.getDocumentName() + "." + expiryDocumentDTO.getFileExtension());
//                         multipart.addBodyPart(pdfBodyPart);
// 	            }

// 	            // 6) set the multiplart object to the message object
//  	            message.setContent(multipart);



// 	            // 7) send message
// 	            Transport.send(message);
// 	        } catch (MessagingException e) {
// 	            LOG.error("SendEmailWithAttachment" + e);
// 	            e.printStackTrace();
// 	            return "failed";
// 	        }

// 	        return "success";
// 	}

// 	// public String sendReportAnIssueNotification(UserReportAnIssueInfo userReportAnIssueInfo) {
// 	// 	String htmlText = "<html>" + "<head>"
// 	// 			+ "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
// 	// 			+ "  <title>::Docuchain::</title>" + "</head>" + "<body>"
// 	// 			+ "  <table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">"
// 	// 			+ "    <tr style=\"background:#5677b2\">"
// 	// 			+ "      <td align=\"left\" valign=\"middle\" style=\"padding:20px; color:#ffffff;\">"
// 	// 			+ "        <img src=\"http://cipldesigns.colaninfotechapps.in/logod.png\" width=\"200\" style=\"display:block;\">"
// 	// 			+ "      </td>" + "      <td align=\"right\" valign=\"middle\" style=\"padding:20px; color:#ffffff;\">"
// 	// 			+ "        <img src=\"http://cipldesigns.colaninfotechapps.in/GNLogo.png\" width=\"120\" style=\"display:block;\">"
// 	// 			+ "      </td>" + "    </tr>" + "  </table>"
// 	// 			+ "  <table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#971800\" style=\"background-color:#971800; border:1px solid #dddddd;\">"
// 	// 			+ "    <tr>"
// 	// 			+ "      <td align=\"center\" valign=\"top\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;\">"
// 	// 			+ "        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" + "          <tr>"
// 	// 			+ "		  <td align=\"left\" valign=\"top\" bgcolor=\"#971800\" style=\"background-color:#8e3d57; padding:20px; font-family:Arial, Helvetica, sans-serif; \">"
// 	// 			+ "              <div style=\"color:#ffffff; font-size:18px; font-weight: bold; font-style: italic;\">Report An Issue Details</div>"
// 	// 			+ "            </td>" + "          </tr>" + "          <tr>"
// 	// 			+ "            <td align=\"left\" valign=\"top\" bgcolor=\"#f7f7f7\" style=\"background-color:#f7f7f7; padding:20px;\">"
// 	// 			+ "              <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\" style=\"margin-bottom:10px;\">"
// 	// 			+ "                <tr>"
// 	// 			+ "					<td align=\"left\" valign=\"top\" style=\"font-family:Arial, Helvetica, sans-serif; font-size:13px; font-weight:600; color:#53231a;\">"
// 	// 			+ "				<p style=\"margin:0 0 12px 0; font-size: 15px;\">Hi Team,"
// 	// 			+ "                      <span style=\"font-weight:300; font-size: 12px;\">  Mr/Ms :"+userReportAnIssueInfo.getCreatorUserProfileInfo().getUserName()+",  is draft a report an issue.</span>"
// 	// 			+ "                    </p>" + "				<div style=\" background:#ededed;padding:2px 10px;\">"
// 	// 			+ "					<p style=\"line-height:22px; font-family:Arial, Helvetica, sans-serif; font-size:11px; font-weight:600; color:#006666\">"
// 	// 			+ "					   <span style=\"font-weight:600; font-size: 12px; color:#660066;\">Organization Name :</span> "+userReportAnIssueInfo.getOrganizationName()+" <br>"
// 	// 			+ "					   <span style=\"font-weight:600; font-size: 12px; color:#660066;\">Name                        :</span> "+userReportAnIssueInfo.getName()+" <br>"
// 	// 			+ "					   <span style=\"font-weight:600; font-size: 12px; color:#660066;\">Email                        :</span> "+userReportAnIssueInfo.getEmail()+" <br>"
// 	// 			+ "					   <span style=\"font-weight:600; font-size: 12px; color:#660066;\">Phone Number        :</span> "+userReportAnIssueInfo.getPhoneNumber()+" <br>"
// 	// 			+ "					   <span style=\"font-weight:600; font-size: 12px; color:#660066;\">Reason                     :</span> "+userReportAnIssueInfo.getReason()+"<br>"
// 	// 			+ "					</p>" + "				</div>" + "				<p style=\"margin:10px 0 10px 0;\">"
// 	// 			+ "                      <span style=\"font-weight:300; font-size: 14px;\"></span>"
// 	// 			+ "                    </p>" + "					<p style=\"margin:0 0 0 0;\">"
// 	// 			+ "                      <span style=\"font-weight:300; font-size: 11px;\"> We hope that you will have a wonderful experience using our website. Any suggestions or feedback is more than welcome, we are always happy to hear from you in order to improve our services. </span>"
// 	// 			+ "                    </p>" + "			  </td>" + "                </tr>" + "                <tr>"
// 	// 			+ "                  <td>"
// 	// 			+ "                    <h1 style=\"font-family:Arial, Helvetica, sans-serif; font-weight:600; color:#800000; font-size: 15px; font-style: italic; margin-top: 15px; margin-bottom:0; padding: 0;\">Thanks,</h1>"
// 	// 			+ "                    <h2 style=\"font-family:Arial, Helvetica, sans-serif; color:#001a4d; font-weight:400; font-size: 15px; font-style: italic; margin:5px 0 3px 0; padding: 0;\">Docuchain Team,</h2>"
// 	// 			+ "					<p style=\"margin:0 0 0 0;\">"
// 	// 			+ "                      <span style=\"font-size: 15px; font-style: italic;\"><a href=\"http://groupnautical.com/docuchain/\">www.groupnautical.com</a></span>"
// 	// 			+ "                    </p>" + "                  </td>" + "                </tr>"
// 	// 			+ "              </table>" + "            </td>" + "          </tr>"
// 	// 			+ "          <tr style=\"background:#5677b2\">"
// 	// 			+ "            <td align=\"center\" valign=\"middle\" style=\" padding:10px;\">"
// 	// 			+ "              <p style=\"color:#ffffff;font-family:calibri;\">© Copyright 2018. All Rights Reserved.</p>"
// 	// 			+ "            </td>" + "          </tr>" + "        </table>" + "      </td>" + "    </tr>"
// 	// 			+ "  </table>" + "</body>" + "</html>";

// 	// 	return htmlText;
// 	// }
//  public String sendReportAnIssueNotification(UserReportAnIssueInfo userReportAnIssueInfo) {

//         int currentYear = Year.now().getValue();

//         // Extracting variables safely to prevent NullPointerExceptions
//         String creatorName = (userReportAnIssueInfo.getCreatorUserProfileInfo() != null && userReportAnIssueInfo.getCreatorUserProfileInfo().getUserName() != null)
//                              ? userReportAnIssueInfo.getCreatorUserProfileInfo().getUserName() : "User";

//         String orgName = userReportAnIssueInfo.getOrganizationName() != null ? userReportAnIssueInfo.getOrganizationName() : "N/A";
//         String reportedName = userReportAnIssueInfo.getName() != null ? userReportAnIssueInfo.getName() : "N/A";
//         String email = userReportAnIssueInfo.getEmail() != null ? userReportAnIssueInfo.getEmail() : "N/A";
//         String phone = userReportAnIssueInfo.getPhoneNumber() != null ? userReportAnIssueInfo.getPhoneNumber() : "N/A";
//         String reason = userReportAnIssueInfo.getReason() != null ? userReportAnIssueInfo.getReason() : "N/A";

//         String htmlText = "<!DOCTYPE html>" +
//                 "<html>" +
//                 "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;'>" +
//                 "  <div style='max-width: 600px; background-color: #ffffff; margin: 0 auto; border: 1px solid #dddddd;'>" +

//                 // --- HEADER: CENTERED LOGO (Restored Original Size and Spacing) ---
//                 "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
//                 "      <tr>" +
//                 "        <td align='center' valign='middle' style='padding: 20px;'>" +
//                 "          <img src='" + logoUrl + "' width='200' style='display:block;' alt='DocuChain Logo' />" +
//                 "        </td>" +
//                 "      </tr>" +
//                 "    </table>" +

//                 // --- MAROON TITLE BAR ---
//                 "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
//                 "      <tr>" +
//                 "        <td align='left' valign='top' bgcolor='#8e3d57' style='background-color:#8e3d57; padding:20px; font-family:Arial, Helvetica, sans-serif;'>" +
//                 "          <div style='color:#ffffff; font-size:18px; font-weight: bold; font-style: italic;'>Report An Issue Details</div>" +
//                 "        </td>" +
//                 "      </tr>" +
//                 "    </table>" +

//                 // --- CONTENT AREA ---
//                 "    <div style='padding: 25px; color: #333; line-height: 1.6; font-size: 14px; background-color: #f7f7f7;'>" +
//                 "      <p style='margin-top: 0;'><strong>Hi Team,</strong> Mr/Ms " + creatorName + " has drafted a report for an issue.</p>" +

//                 // --- GRAY DETAILS BOX (Table format for perfect alignment) ---
//                 "      <div style='background-color: #ededed; padding: 15px; margin-bottom: 20px; border-radius: 4px;'>" +
//                 "        <table style='width: 100%; border-collapse: collapse; font-size: 12px; color: #006666; font-weight: bold; font-family: Arial, Helvetica, sans-serif;'>" +
//                 "          <tr><td style='width: 150px; padding: 4px 0; color: #660066;'>Organization Name</td><td>: " + orgName + "</td></tr>" +
//                 "          <tr><td style='padding: 4px 0; color: #660066;'>Name</td><td>: " + reportedName + "</td></tr>" +
//                 "          <tr><td style='padding: 4px 0; color: #660066;'>Email</td><td>: <a href='mailto:" + email + "' style='color: #008080; text-decoration: none;'>"+ email +"</a></td></tr>" +
//                 "          <tr><td style='padding: 4px 0; color: #660066;'>Phone Number</td><td>: " + phone + "</td></tr>" +
//                 "          <tr><td style='padding: 4px 0; color: #660066;'>Reason</td><td>: " + reason + "</td></tr>" +
//                 "        </table>" +
//                 "      </div>" +

//                 "      <p style='font-size: 11px; font-weight: 300; color: #333;'>We hope that you will have a wonderful experience using our website. Any suggestions or feedback is more than welcome, we are always happy to hear from you in order to improve our services.</p>" +

//                 // --- SIGNATURE (.info link) ---
//                 "      <div style='margin-top: 25px;'>" +
//                 "        <h1 style='font-family:Arial, Helvetica, sans-serif; font-weight:600; color:#800000; font-size: 15px; font-style: italic; margin: 0; padding: 0;'>Thanks,</h1>" +
//                 "        <h2 style='font-family:Arial, Helvetica, sans-serif; color:#001a4d; font-weight:400; font-size: 15px; font-style: italic; margin: 5px 0 3px 0; padding: 0;'>Docuchain Team,</h2>" +
//                 "        <p style='margin:0;'>" +
//                 "          <a href='https://docuchain.info/#/' style='color: #0000EE; font-size: 15px; font-style: italic; text-decoration: underline;'>www.docuchain.info</a>" +
//                 "        </p>" +
//                 "      </div>" +
//                 "    </div>" +

//                 // --- BLUE FOOTER (Dynamic Year) ---
//                 "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
//                 "      <tr style='background:#5677b2;'>" +
//                 "        <td align='center' valign='middle' style='padding: 10px;'>" +
//                 "          <p style='color:#ffffff; font-family:calibri, sans-serif; font-size: 12px; margin: 0;'>© Copyright " + currentYear + ". All Rights Reserved.</p>" +
//                 "        </td>" +
//                 "      </tr>" +
//                 "    </table>" +

//                 "  </div>" +
//                 "</body>" +
//                 "</html>";

//         return htmlText;
//     }
// }

package com.dapp.docuchain.service.impl;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.GroupTagDTO;
import com.dapp.docuchain.model.UserReportAnIssueInfo;
import com.dapp.docuchain.service.EmailService;
import com.dapp.docuchain.service.FileService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;
import java.util.Properties;
import java.time.Year;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    FileService fileService;

    @Autowired
    private Environment env;

    // Pulling the logo URL from application.properties
    @Value("${docuchain.email.logo.url}")
    private String logoUrl;

    public boolean sendEmailDEV(String toEamilId, String subject, String content) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(toEamilId);
            mail.setFrom(env.getProperty("email.username"));
            mail.setSubject(subject);
            Properties properties = new Properties();
            properties.put("mail.smtp.host", env.getProperty("email.host"));
            properties.put("mail.smtp.port", env.getProperty("email.port"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("email.username"),
                            env.getProperty("email.password"));
                }
            };
            Session session = Session.getInstance(properties, auth);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(env.getProperty("email.username")));
            InternetAddress[] toAddresses = { new InternetAddress(toEamilId) };
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            message.setSubject(subject);
            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");
            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html");
            multipart.addBodyPart(messageBodyPart);
            // put everything together
            message.setContent(multipart);
            Transport.send(message);
            LOG.info("Email sent successfully !");
            return true;
        } catch (MailException e) {
            LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            LOG.info("Problem in sending mail sendEmailDEV  END: " + env);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String SendEmailWithAttachment(ExpiryDocumentDTO expiryDocumentDTO) {
        String toEamilId = expiryDocumentDTO.getEmailIds();
        // 1) get the session object
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", env.getProperty("email.host"));
            properties.put("mail.smtp.port", env.getProperty("email.port"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("email.username"),
                            env.getProperty("email.password"));
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(env.getProperty("email.username")));
            InternetAddress[] parse = InternetAddress.parse(toEamilId, true);
            message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
            message.setSubject("Message Aleart");

            // 3) create MimeBodyPart object and set your message text
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("This is message body");

            String documentName = expiryDocumentDTO.getDocumentDataInfo().getDocumentName();
            String documentHashCode = expiryDocumentDTO.getDocumentDataInfo().getDocumentHashCode();
            String fileResponse = fileService.fileRetriveFromStorej(documentName, documentHashCode);

            byte[] decodedBytes = Base64.decodeBase64(fileResponse);
            LOG.info(decodedBytes.toString());
            DataSource dataSource = null;
            if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("pdf")) {
                dataSource = new ByteArrayDataSource(decodedBytes, "application/pdf");
            } else {
                dataSource = new ByteArrayDataSource(decodedBytes, "application/jpg");
            }
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(expiryDocumentDTO.getDocumentName() + "." + expiryDocumentDTO.getFileExtension());

            // 5) create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(pdfBodyPart);

            // 6) set the multiplart object to the message object
            message.setContent(multipart);

            // 7) send message
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error("SendEmailWithAttachment" + e);
            e.printStackTrace();
            return "failed";
        }

        return "success";
    }

    @Override
    public boolean forgotPasswordNotificationEmail(String mail, String username, String forgetPassword) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", env.getProperty("email.host"));
            properties.put("mail.smtp.port", env.getProperty("email.port"));
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
            Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(env.getProperty("email.username"),
                            env.getProperty("email.password"));
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(env.getProperty("email.username")));
            InternetAddress[] parse = InternetAddress.parse(mail, true);
            message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
            message.setSubject("Docuchain Password Reset");

            // 3) create MimeBodyPart object and set your message text
            BodyPart messageBodyPart = new MimeBodyPart();

            int currentYear = Year.now().getValue();

            // Unified Template for Forgot Password
            String htmlText = "<!DOCTYPE html>" +
                    "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;'>" +
                    "  <div style='max-width: 600px; background-color: #ffffff; margin: 0 auto; border: 1px solid #dddddd;'>" +

                    // --- HEADER: CENTERED LOGO ---
                    "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                    "      <tr>" +
                    "        <td align='center' valign='middle' style='padding: 20px;'>" +
                    "          <img src='" + logoUrl + "' width='200' style='display:block;' alt='DocuChain Logo' />" +
                    "        </td>" +
                    "      </tr>" +
                    "    </table>" +

                    // --- MAROON TITLE BAR ---
                    "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                    "      <tr>" +
                    "        <td align='left' valign='top' bgcolor='#8e3d57' style='background-color:#8e3d57; padding:20px; font-family:Arial, Helvetica, sans-serif;'>" +
                    "          <div style='color:#ffffff; font-size:18px; font-weight: bold; font-style: italic;'>Forgot Password</div>" +
                    "        </td>" +
                    "      </tr>" +
                    "    </table>" +

                    // --- CONTENT AREA ---
                    "    <div style='padding: 25px; color: #333; line-height: 1.6; font-size: 14px; background-color: #f7f7f7;'>" +
                    "      <p style='margin-top: 0; font-size: 15px;'><strong>Hi " + username + ",</strong></p>" +
                    "      <p style='font-size: 14px; font-weight: 300;'>Your new password for accessing DocuChain is below.</p>" +

                    // --- GRAY DETAILS BOX ---
                    "      <div style='background-color: #ededed; padding: 15px; margin-bottom: 20px; border-radius: 4px;'>" +
                    "        <table style='width: 100%; border-collapse: collapse; font-size: 13px; color: #006666; font-weight: bold; font-family: Arial, Helvetica, sans-serif;'>" +
                    "          <tr><td style='width: 100px; padding: 4px 0; color: #660066;'>E-mail</td><td>: " + mail + "</td></tr>" +
                    "          <tr><td style='padding: 4px 0; color: #660066;'>User Name</td><td>: " + username + "</td></tr>" +
                    "          <tr><td style='padding: 4px 0; color: #660066;'>Password</td><td>: " + forgetPassword + "</td></tr>" +
                    "        </table>" +
                    "      </div>" +

                    "      <p style='font-size: 13px; font-weight: 300; color: #333;'>Please do not share this password with anyone. In case you haven't requested this password reset, please contact your administrator.</p>" +

                    // --- SIGNATURE (.info link) ---
                    "      <div style='margin-top: 25px;'>" +
                    "        <h1 style='font-family:Arial, Helvetica, sans-serif; font-weight:600; color:#800000; font-size: 15px; font-style: italic; margin: 0; padding: 0;'>Thanks,</h1>" +
                    "        <h2 style='font-family:Arial, Helvetica, sans-serif; color:#001a4d; font-weight:400; font-size: 15px; font-style: italic; margin: 5px 0 3px 0; padding: 0;'>Docuchain Team,</h2>" +
                    "        <p style='margin:0;'>" +
                    "          <a href='https://docuchain.info/#/' style='color: #0000EE; font-size: 15px; font-style: italic; text-decoration: underline;'>www.docuchain.info</a>" +
                    "        </p>" +
                    "      </div>" +
                    "    </div>" +

                    // --- BLUE FOOTER (Dynamic Year) ---
                    "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                    "      <tr style='background:#5677b2;'>" +
                    "        <td align='center' valign='middle' style='padding: 10px;'>" +
                    "          <p style='color:#ffffff; font-family:calibri, sans-serif; font-size: 12px; margin: 0;'>© Copyright " + currentYear + ". All Rights Reserved.</p>" +
                    "        </td>" +
                    "      </tr>" +
                    "    </table>" +

                    "  </div>" +
                    "</body>" +
                    "</html>";

            messageBodyPart.setContent(htmlText, "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // 6) set the multiplart object to the message object
            message.setContent(multipart);

            // 7) send message
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error("SendEmailWithAttachment" + e);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String shareGroupExpiryDocument(GroupTagDTO groupDTO) {
         String toEamilId = groupDTO.getEmailId();
            // 1) get the session object
            try {
                Properties properties = System.getProperties();
                properties.put("mail.smtp.host", env.getProperty("email.host"));
                properties.put("mail.smtp.port", env.getProperty("email.port"));
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.ssl.trust", env.getProperty("email.host"));
                Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.getProperty("email.username"),
                                env.getProperty("email.password"));
                    }
                });
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(env.getProperty("email.username")));
                InternetAddress[] parse = InternetAddress.parse(toEamilId, true);
                message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
                message.setSubject("Message Aleart");

                // 3) create MimeBodyPart object and set your message text
                BodyPart messageBodyPart1 = new MimeBodyPart();
                messageBodyPart1.setText("This is message body");
                Multipart  multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart1);
                List<ExpiryDocumentDTO> expiryDocumentDTOList=groupDTO.getExpiryDocumentDtos();
                if(expiryDocumentDTOList!=null)
                    for(ExpiryDocumentDTO expiryDocumentDTO:expiryDocumentDTOList)
                    {
                        String documentName = expiryDocumentDTO.getDocumentDataInfo().getDocumentName();
                        String documentHashCode = expiryDocumentDTO.getDocumentDataInfo().getDocumentHashCode();
                        String fileResponse = fileService.fileRetriveFromStorej(documentName, documentHashCode);

                        byte[] decodedBytes = Base64.decodeBase64(fileResponse);
                        LOG.info(decodedBytes.toString());
                        DataSource dataSource = null;
                        if (expiryDocumentDTO.getFileExtension().equalsIgnoreCase("pdf")) {
                            dataSource = new ByteArrayDataSource(decodedBytes, "application/pdf");
                        } else {
                            dataSource = new ByteArrayDataSource(decodedBytes, "application/jpg");
                        }

                        MimeBodyPart pdfBodyPart = new MimeBodyPart();
                        pdfBodyPart.setDataHandler(new DataHandler(dataSource));
                        pdfBodyPart.setFileName(
                                expiryDocumentDTO.getDocumentName() + "." + expiryDocumentDTO.getFileExtension());
                        multipart.addBodyPart(pdfBodyPart);
                }

                // 6) set the multiplart object to the message object
                message.setContent(multipart);

                // 7) send message
                Transport.send(message);
            } catch (MessagingException e) {
                LOG.error("SendEmailWithAttachment" + e);
                e.printStackTrace();
                return "failed";
            }

            return "success";
    }

    public String sendReportAnIssueNotification(UserReportAnIssueInfo userReportAnIssueInfo) {

        int currentYear = Year.now().getValue();

        // Extracting variables safely to prevent NullPointerExceptions
        String creatorName = (userReportAnIssueInfo.getCreatorUserProfileInfo() != null && userReportAnIssueInfo.getCreatorUserProfileInfo().getUserName() != null)
                             ? userReportAnIssueInfo.getCreatorUserProfileInfo().getUserName() : "User";

        String orgName = userReportAnIssueInfo.getOrganizationName() != null ? userReportAnIssueInfo.getOrganizationName() : "N/A";
        String reportedName = userReportAnIssueInfo.getName() != null ? userReportAnIssueInfo.getName() : "N/A";
        String email = userReportAnIssueInfo.getEmail() != null ? userReportAnIssueInfo.getEmail() : "N/A";
        String phone = userReportAnIssueInfo.getPhoneNumber() != null ? userReportAnIssueInfo.getPhoneNumber() : "N/A";
        String reason = userReportAnIssueInfo.getReason() != null ? userReportAnIssueInfo.getReason() : "N/A";

        // Unified Template for Report An Issue
        String htmlText = "<!DOCTYPE html>" +
                "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;'>" +
                "  <div style='max-width: 600px; background-color: #ffffff; margin: 0 auto; border: 1px solid #dddddd;'>" +

                // --- HEADER: CENTERED LOGO ---
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "      <tr>" +
                "        <td align='center' valign='middle' style='padding: 20px;'>" +
                "          <img src='" + logoUrl + "' width='200' style='display:block;' alt='DocuChain Logo' />" +
                "        </td>" +
                "      </tr>" +
                "    </table>" +

                // --- MAROON TITLE BAR ---
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "      <tr>" +
                "        <td align='left' valign='top' bgcolor='#8e3d57' style='background-color:#8e3d57; padding:20px; font-family:Arial, Helvetica, sans-serif;'>" +
                "          <div style='color:#ffffff; font-size:18px; font-weight: bold; font-style: italic;'>Report An Issue Details</div>" +
                "        </td>" +
                "      </tr>" +
                "    </table>" +

                // --- CONTENT AREA ---
                "    <div style='padding: 25px; color: #333; line-height: 1.6; font-size: 14px; background-color: #f7f7f7;'>" +
                "      <p style='margin-top: 0;'><strong>Hi Team,</strong> Mr/Ms " + creatorName + " has drafted a report for an issue.</p>" +

                // --- GRAY DETAILS BOX ---
                "      <div style='background-color: #ededed; padding: 15px; margin-bottom: 20px; border-radius: 4px;'>" +
                "        <table style='width: 100%; border-collapse: collapse; font-size: 12px; color: #006666; font-weight: bold; font-family: Arial, Helvetica, sans-serif;'>" +
                "          <tr><td style='width: 150px; padding: 4px 0; color: #660066;'>Organization Name</td><td>: " + orgName + "</td></tr>" +
                "          <tr><td style='padding: 4px 0; color: #660066;'>Name</td><td>: " + reportedName + "</td></tr>" +
                "          <tr><td style='padding: 4px 0; color: #660066;'>Email</td><td>: <a href='mailto:" + email + "' style='color: #008080; text-decoration: none;'>"+ email +"</a></td></tr>" +
                "          <tr><td style='padding: 4px 0; color: #660066;'>Phone Number</td><td>: " + phone + "</td></tr>" +
                "          <tr><td style='padding: 4px 0; color: #660066;'>Reason</td><td>: " + reason + "</td></tr>" +
                "        </table>" +
                "      </div>" +

                "      <p style='font-size: 11px; font-weight: 300; color: #333;'>We hope that you will have a wonderful experience using our website. Any suggestions or feedback is more than welcome, we are always happy to hear from you in order to improve our services.</p>" +

                // --- SIGNATURE (.info link) ---
                "      <div style='margin-top: 25px;'>" +
                "        <h1 style='font-family:Arial, Helvetica, sans-serif; font-weight:600; color:#800000; font-size: 15px; font-style: italic; margin: 0; padding: 0;'>Thanks,</h1>" +
                "        <h2 style='font-family:Arial, Helvetica, sans-serif; color:#001a4d; font-weight:400; font-size: 15px; font-style: italic; margin: 5px 0 3px 0; padding: 0;'>Docuchain Team,</h2>" +
                "        <p style='margin:0;'>" +
                "          <a href='https://docuchain.info/#/' style='color: #0000EE; font-size: 15px; font-style: italic; text-decoration: underline;'>www.docuchain.info</a>" +
                "        </p>" +
                "      </div>" +
                "    </div>" +

                // --- BLUE FOOTER (Dynamic Year) ---
                "    <table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
                "      <tr style='background:#5677b2;'>" +
                "        <td align='center' valign='middle' style='padding: 10px;'>" +
                "          <p style='color:#ffffff; font-family:calibri, sans-serif; font-size: 12px; margin: 0;'>© Copyright " + currentYear + ". All Rights Reserved.</p>" +
                "        </td>" +
                "      </tr>" +
                "    </table>" +

                "  </div>" +
                "</body>" +
                "</html>";

        return htmlText;
    }
}
