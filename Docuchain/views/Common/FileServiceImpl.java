package com.dapp.docuchain.service.impl;
import com.dapp.docuchain.repository.PortInfoRepository; // Add this line
// Add these to your existing imports in FileServiceImpl.java
import java.util.Optional;
import com.dapp.docuchain.model.PortInfo;

import com.dapp.docuchain.dto.ExpiryDocumentDTO;
import com.dapp.docuchain.dto.ShipProfileDTO;
import com.dapp.docuchain.model.Config;
import com.dapp.docuchain.model.ScanDelimiterInfo;
import com.dapp.docuchain.model.ScanFieldType;
import com.dapp.docuchain.repository.ConfigInfoRepository;
import com.dapp.docuchain.repository.ScanDelimiterRepository;
import com.dapp.docuchain.service.FileService;
import com.google.common.io.Files;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.dapp.docuchain.dto.VesselDocumentDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import java.util.List;
import java.util.ArrayList;
@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    @Autowired
    private Environment env;
    @Autowired
    private ScanDelimiterRepository scanDelimiterRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
	ConfigInfoRepository configInfoRepository;
    @Autowired
private PortInfoRepository portInfoRepository;
public static Date parseDate(String inputDate) {
    if (inputDate == null || inputDate.trim().isEmpty()) {
        return null;
    }

    Date outputDate = null;
    String cleanedDate = inputDate;

    // 1. REGEX EXTRACTION: Added pattern to match "Month d, yyyy"
    Matcher m = Pattern.compile("(\\d{1,2}(?:st|nd|rd|th|sth)?\\s+(?:day\\s+of\\s+)?[a-zA-Z]{3,}\\s+\\d{4}|[a-zA-Z]{3,}\\s+\\d{1,2},?\\s+\\d{4}|\\d{2}[-/]\\d{2}[-/]\\d{4})", Pattern.CASE_INSENSITIVE).matcher(inputDate);

    if (m.find()) {
        String rawMatch = m.group(1).trim();
        cleanedDate = rawMatch
                .replaceAll(",", "") // Removes comma from "January 16, 2008" -> "January 16 2008" [cite: 11]
                .replaceAll("(?i)day of", "")
                .replaceAll("(?i)(?<=\\d)(st|nd|rd|th|sth)", "")
                .replaceAll("\\s+", " ")
                .trim();
    } else {
        if (!inputDate.matches(".*\\d.*")) {
            return null;
        }

        cleanedDate = inputDate
                .replaceAll("(?i)noon gmt", "")
                .replaceAll("(?i)day of", "")
                .replaceAll("(?i)(?<=\\d)(st|nd|rd|th|sth)", "")
                .replaceAll("(?i)sth", "8")
                .replaceAll(",", "")
                .replaceAll("\\?", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // 2. Updated formats to include Month-first patterns [cite: 11]
    // String[] possibleDateFormats = {
    //     "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",  // <--- FIX: Added for React/Angular frontend dates
    //         "yyyy-MM-dd'T'HH:mm:ss.SSSZ",    // <--- FIX: Backup timezone format
    //         "yyyy-MM-dd'T'HH:mm:ss.SSS",     // <--- FIX: Backup for dates without timezone
    //         "yyyy-MM-dd",                    // <--- FIX: Standard HTML5 date format
    //         "MMMM d yyyy",    // January 16 2008
    //         "MMMM dd yyyy",   // November 15 2007
    //         "dd MMMM yyyy",
    //         "d MMMM yyyy",
    //         "dd MMM yyyy",
    //         "dd-MMM-yyyy",
    //         "dd/MM/yyyy",
    //         "dd-MM-yyyy",
    //         "yyyyMMdd"
    // };

    // 2. Combined formats for API requests and OCR document scanning
    String[] possibleDateFormats = {
            // --- API & System Formats (From your friend) ---
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", // <- Added to fix your specific frontend error
            "yyyy.MM.dd G 'at' HH:mm:ss z",
            "EEE, MMM d, ''yy",
            "h:mm a",
            "hh 'o''clock' a, zzzz",
            "K:mm a, z",
            "yyyyy.MMMMM.dd GGG hh:mm aaa",
            "EEE, d MMM yyyy HH:mm:ss Z",
            "yyMMddHHmmssZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "YYYY-'W'ww-u",
            "EEE, dd MMM yyyy HH:mm:ss z",
            "EEE, dd MMM yyyy HH:mm zzzz",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSzzzz",
            "yyyy-MM-dd'T'HH:mm:sszzzz",
            "yyyy-MM-dd'T'HH:mm:ss z",
            "yyyy-MM-dd'T'HH:mm:ssz",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HHmmss.SSSz",
            "dd/MM/yy",

            // --- OCR Document Formats (From your original code) ---
            "MMMM d yyyy",
            "MMMM dd yyyy",
            "dd MMMM yyyy",
            "d MMMM yyyy",
            "dd-MMM-yyyy",

            // --- Common formats shared by both ---
            "yyyyMMdd",
            "dd/MM/yyyy",
            "dd MMM yyyy",
            "dd-MM-yyyy"
    };

    try {
        outputDate = org.apache.commons.lang.time.DateUtils.parseDate(cleanedDate, possibleDateFormats);
        LOGGER.info("Successfully parsed: [" + cleanedDate + "]");
    } catch (ParseException e) {
        try {
            outputDate = org.apache.commons.lang.time.DateUtils.parseDate(cleanedDate.toLowerCase(), possibleDateFormats);
        } catch (ParseException e2) {
            LOGGER.error("Date Parsing Failed for input: " + inputDate + " (Cleaned: " + cleanedDate + ")");
        }
    }
    return outputDate;
}
    // 1. Add this public method to handle the file upload and Tesseract init
@Override
public VesselDocumentDTO scanVesselImageFile(MultipartFile file) {
    VesselDocumentDTO vesselDTO = new VesselDocumentDTO();

    try (PDDocument document = PDDocument.load(file.getInputStream())) {
        PDFTextStripper stripper = new PDFTextStripper();
        String fullText = stripper.getText(document);
          System.out.println("===== CLEAN PDF TEXT START =====");
        System.out.println(fullText);
        System.out.println("===== CLEAN PDF TEXT END =====");

        if (fullText != null && !fullText.trim().isEmpty()) {
            // Step 1: Extract basic data from PDF (IMO, Port Name, etc.)
            vesselDTO = scanVesselDocInfo(fullText);

            // Step 2: Database Lookup for Nationality
            if (vesselDTO.getPort() != null) {
                String extractedPort = vesselDTO.getPort().trim();

                // Search database for the Port Name
                Optional<PortInfo> portEntity = portInfoRepository.findByPortNameIgnoreCase(extractedPort);

                if (portEntity.isPresent()) {
                    // Get the Nationality (Country Name) from the linked CountryInfo entity
                    String countryName = portEntity.get().getCountryInfo().getCountryName();
                    vesselDTO.setNationality(countryName);

                    // Optional: Update the Port name to match your DB's official spelling
                    vesselDTO.setPort(portEntity.get().getPortName());
                }
            }
        }
    } catch (Exception e) {
        LOGGER.error("Error in scanVesselImageFile: ", e);
    }
    return vesselDTO;
}
// private VesselDocumentDTO scanVesselDocInfo(String cleanText) {
//     VesselDocumentDTO dto = new VesselDocumentDTO();
//     DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

//     // 1. Vessel Name: Extracts the name inside quotes following M. V.
//     // Example: M. V. "PEARL RIVER BRIDGE "
//     Matcher mVessel = Pattern.compile("M\\.\\s*V\\.\\s*[\"“]([^\"”]+)[\"”]", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     if (mVessel.find()) {
//         dto.setVesselName(mVessel.group(1).trim());
//     }

//     // 2. Builder / Yard: Look for the Shipbuilding company or Shipyard name
//     // Example: IMABARI SHIPBUILDING CO., LTD. or IMABARI SHIPYARD
//     // Matcher mBuilder = Pattern.compile("([A-Z\\s,.]+SHIPBUILDING\\s+CO\\.,\\s+LTD\\.)", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     Matcher mBuilder = Pattern.compile("(?m)^(?!.*(?:PROPERTY|CAUTION|DRAWING))([A-Z\\s,.]+?(?:CO\\.,\\s+LTD\\.|SHIPYARD|SHIPBUILDING))", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     if (mBuilder.find()) {
//         dto.setBuilderYard(mBuilder.group(1).trim());
//     } else {
//         // Fallback to find "SHIPYARD" if the full corporate name isn't matched
//         Matcher mYard = Pattern.compile("([A-Z\\s]+SHIPYARD)", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//         if (mYard.find()) {
//             dto.setBuilderYard(mYard.group(1).trim());
//         }
//     }

//     // ... existing IMO, Port, Dates, and Tonnage extraction logic ...

//     // 3. Port & IMO Number
//     Matcher mPortImo = Pattern.compile("REGISTRATI\\s*ON\\s+([A-Z\\s]+?)\\s+(\\d{7})", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     if (mPortImo.find()) {
//         dto.setPort(mPortImo.group(1).trim());
//         dto.setImoNumber(mPortImo.group(2));
//     }

//     // 4. Dates
//     Matcher mDates = Pattern.compile("[A-Z][a-z]+\\s+\\d{1,2},\\s+\\d{4}").matcher(cleanText);
//     List<String> foundDates = new ArrayList<>();
//     while (mDates.find()) {
//         foundDates.add(mDates.group());
//     }

//     if (foundDates.size() >= 2) {
//         Date dKeel = parseDate(foundDates.get(1));
//         dto.setKeelLaidDate(dKeel);
//         if(dKeel != null) dto.setKeelLaidDateString(dateFormat.format(dKeel));
//     }
//     if (foundDates.size() >= 4) {
//         Date dDel = parseDate(foundDates.get(3));
//         dto.setDeliveryDate(dDel);
//         if(dDel != null) dto.setDeliveryDateString(dateFormat.format(dDel));
//     }

//     // 5. Dimensions
//     Matcher mLen = Pattern.compile("LENGTH 0\\.\\s*A\\.\\s*([\\d.\\s]+)\\s*m", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     if (mLen.find()) dto.setLengthOA(mLen.group(1).replaceAll("\\s", ""));

//     Matcher mBreadth = Pattern.compile("BREADTH MLD\\.\\s*([\\d.\\s]+)\\s*m", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     if (mBreadth.find()) dto.setBreadth(mBreadth.group(1).replaceAll("\\s", ""));

//     // 6. Gross Tonnage & DWT
//     Matcher mTonnage = Pattern.compile("GROSS TONNAGE[\\s\\d,.]+m3\\s+([\\d,.\\s]+)").matcher(cleanText);
//     if (mTonnage.find()) {
//         String tonnageVal = mTonnage.group(1).trim();
//         dto.setGrossTonnageSuez(tonnageVal.replaceAll(",", "").replaceAll("\\s", ""));
//     }

//     Matcher mDwt = Pattern.compile("SUMMER\\s+s\\s+[\\d,\\s]+\\s+[\\d.\\s]+\\s+([\\d,\\s]{5,})", Pattern.CASE_INSENSITIVE).matcher(cleanText);
//     if (mDwt.find()) {
//         String mtValue = mDwt.group(1).trim();
//         String firstBlock = mtValue.split("\\s+")[0] + (mtValue.contains(" ") ? mtValue.split("\\s+")[1] : "");
//         dto.setDwtSummer(firstBlock.replaceAll(",", "").replaceAll("\\s", ""));
//     }

//     return dto;
// }
private VesselDocumentDTO scanVesselDocInfo(String cleanText) {
    VesselDocumentDTO dto = new VesselDocumentDTO();
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    // 1. Vessel Name: Extracts text inside quotes after M. V.
    Matcher mVessel = Pattern.compile("M\\.\\s*V\\.\\s*[\"“]([^\"”]+)[\"”]", Pattern.CASE_INSENSITIVE).matcher(cleanText);
    if (mVessel.find()) {
        dto.setVesselName(mVessel.group(1).trim());
    }

    // 2. Dynamic Builder / Yard: Captures names ending in CO., LTD. or SHIPYARD
    // Uses a negative lookahead to ignore the "PROPERTY OF" legal disclaimer
    Matcher mBuilder = Pattern.compile("(?m)^(?!.*(?:PROPERTY|CAUTION|DRAWING))([A-Z\\s,.]+?(?:CO\\.,\\s+LTD\\.|SHIPYARD|SHIPBUILDING))", Pattern.CASE_INSENSITIVE).matcher(cleanText);
    if (mBuilder.find()) {
        dto.setBuilderYard(mBuilder.group(1).trim());
    }

    // 3. Dynamic Port & IMO Number Extraction
    Matcher mPortImo = Pattern.compile("REGISTRATI\\s*ON\\s+([A-Z\\s]+?)\\s+(\\d{7})", Pattern.CASE_INSENSITIVE).matcher(cleanText);
    if (mPortImo.find()) {
        dto.setPort(mPortImo.group(1).trim());
        dto.setImoNumber(mPortImo.group(2));
    }

    // 4. Dates (Keel Laid & Delivery)
    // List sequence: [0] Cover Date, [1] Keel Laid, [2] Launching, [3] Delivery
    Matcher mDates = Pattern.compile("[A-Z][a-z]+\\s+\\d{1,2},\\s+\\d{4}").matcher(cleanText);
    List<String> foundDates = new ArrayList<>();
    while (mDates.find()) {
        foundDates.add(mDates.group());
    }

    if (foundDates.size() >= 2) {
        Date dKeel = parseDate(foundDates.get(1));
        dto.setKeelLaidDate(dKeel);
        if(dKeel != null) dto.setKeelLaidDateString(dateFormat.format(dKeel));
    }
    if (foundDates.size() >= 4) {
        Date dDel = parseDate(foundDates.get(3));
        dto.setDeliveryDate(dDel);
        if(dDel != null) dto.setDeliveryDateString(dateFormat.format(dDel));
    }

    // 5. Length O.A & Breadth (Clean Numeric)
    Matcher mLen = Pattern.compile("LENGTH 0\\.\\s*A\\.\\s*([\\d.\\s]+)\\s*m", Pattern.CASE_INSENSITIVE).matcher(cleanText);
    if (mLen.find()) dto.setLengthOA(mLen.group(1).replaceAll("\\s", ""));

    Matcher mBreadth = Pattern.compile("BREADTH MLD\\.\\s*([\\d.\\s]+)\\s*m", Pattern.CASE_INSENSITIVE).matcher(cleanText);
    if (mBreadth.find()) dto.setBreadth(mBreadth.group(1).replaceAll("\\s", ""));

    // 6. Gross Tonnage Suez (Clean Numeric - No Commas)
    Matcher mTonnage = Pattern.compile("GROSS TONNAGE[\\s\\d,.]+m3\\s+([\\d,.\\s]+)").matcher(cleanText);
    if (mTonnage.find()) {
        String tonnageVal = mTonnage.group(1).trim();
        dto.setGrossTonnageSuez(tonnageVal.replaceAll(",", "").replaceAll("\\s", ""));
    }

    // 7. DWT Summer (Clean Numeric - No Commas)
    Matcher mDwt = Pattern.compile("SUMMER\\s+s\\s+[\\d,\\s]+\\s+[\\d.\\s]+\\s+([\\d,\\s]{5,})", Pattern.CASE_INSENSITIVE).matcher(cleanText);
    if (mDwt.find()) {
        String mtValue = mDwt.group(1).trim();
        String firstBlock = mtValue.split("\\s+")[0] + (mtValue.contains(" ") ? mtValue.split("\\s+")[1] : "");
        dto.setDwtSummer(firstBlock.replaceAll(",", "").replaceAll("\\s", ""));
    }

    return dto;
}

    @Override
    public String fileRetriveFromStorej(String fileName, String fileHashCode) {

        String messageString = new String();
        String encodedBase64 = null;


        try {
        	String statUrl = env.getProperty("getURL") + URLEncoder.encode(fileName, "UTF-8");
            if (fileHashCode != null) {
                statUrl = statUrl + "/" + fileHashCode;
    		}

    		HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet(statUrl);
            HttpResponse response;
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                messageString += line;
            }
            // print result
            JSONObject myResponse = new JSONObject(messageString);
            // JSONObject fileObject=myResponse.getJSONObject("fileObjectDTO");
            encodedBase64 = myResponse.optString("fileArray");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return encodedBase64;

    }

		@Override
public ExpiryDocumentDTO scanImageFile(MultipartFile file) {
	 System.out.println("🔥🔥🔥 SCAN API HIT 🔥🔥🔥");

    ITesseract instance = new Tesseract();
    ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();

    File tessDataFolder = LoadLibs.extractTessResources("tessdata");
    instance.setDatapath(tessDataFolder.getAbsolutePath());
    instance.setLanguage("eng");

    File convFile = null;

    try {

        convFile = File.createTempFile("upload_", ".pdf");
        // if it not work then use below code and set the path where you want to save the file temporarily
//         convFile = File.createTempFile(
//     "upload_",
//     ".pdf",
//     new File("D:/xampp/htdocs/docuchain/docuchain-api/docuchain-temp")
// );

        file.transferTo(convFile);

        System.out.println("===== FILE SAVED =====");
        System.out.println("File Path: " + convFile.getAbsolutePath());

        String result = instance.doOCR(convFile);

        System.out.println("===== OCR RAW TEXT START =====");
        System.out.println(result);
        System.out.println("===== OCR RAW TEXT END =====");

        if(result == null || result.trim().isEmpty()){
            System.out.println("🚨 OCR RETURNED EMPTY TEXT");
        } else {
            System.out.println("✅ OCR RETURNED TEXT SUCCESSFULLY");
        }

        // return new ExpiryDocumentDTO();
				ExpiryDocumentDTO dto = scanDocInfo(result);
return dto;


    } catch (Exception e) {
        e.printStackTrace();
        return expiryDocumentDTO;
    } finally {
        if (convFile != null && convFile.exists()) {
            convFile.delete();
        }
    }
}


    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    @Override
    public String uploadFile(MultipartFile file, Long saveInBlockchain) {
        String message = "";
        if (!file.isEmpty()) {
            try {
                ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        LOGGER.info("Original File Name --->" + file.getOriginalFilename());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
                        String dateAsString = simpleDateFormat.format(new Date());
                        String fileExtn = Files.getFileExtension(file.getOriginalFilename());
                        String fileName = Files.getNameWithoutExtension(file.getOriginalFilename()).replaceAll("[-+.^:, ]", "");
                        LOGGER.info("File name --->" + fileName + dateAsString + "." + fileExtn);
                        if (fileName != null && fileExtn != null && !fileName.isEmpty() && !fileExtn.isEmpty()) {
                            return fileName + dateAsString + "." + fileExtn;
                        }
                        return file.getOriginalFilename();
                    }
                };
                LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("file", fileAsResource);
						System.out.println("Map Content: " + map);

                map.add("saveInBlockchain", saveInBlockchain);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
                String url = env.getProperty("postURL");

                ResponseEntity<String> response = restTemplate.exchange(url,
                        HttpMethod.POST,
                        entity,
                        String.class);
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    LOGGER.info("FileServiceImpl.uploadFile --->" + response);
                    message = response.getBody();
                    return message;
                }
            } catch (Exception e) {
                LOGGER.error("Failed to upload attachment", e);
                return env.getProperty("failure");
            }
        }
        return env.getProperty("failure");
    }

		private ExpiryDocumentDTO scanDocInfo(String scannedText) {
        ExpiryDocumentDTO expiryDocumentDTO = new ExpiryDocumentDTO();
        ScanFieldType scanFieldTypes[] = ScanFieldType.values();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        for (ScanFieldType scanFieldType : scanFieldTypes) {
            LOGGER.info("scanFieldType = [" + scanFieldType.name() + "]");
            String match = "";
            Set<ScanDelimiterInfo> scanDelimiterInfos = scanDelimiterRepository.findByFieldType(scanFieldType.name());
            for (ScanDelimiterInfo scanDelimiterInfo : scanDelimiterInfos) {
                LOGGER.info("scanDelimiterInfo = [" + scanDelimiterInfo.getStartingPattern() + "]");
                LOGGER.info("scanDelimiterInfo = [" + scanDelimiterInfo.getEndingPattern() + "]");
                Matcher m = Pattern.compile(Pattern.quote(scanDelimiterInfo.getStartingPattern()) + "(.*?)" + Pattern.quote(scanDelimiterInfo.getEndingPattern()),Pattern.CASE_INSENSITIVE).matcher(scannedText); //certificate no
                while (m.find()) {
                    match = m.group(1);
                    // --- ADD THESE LINES TO PRINT TO CMD ---
        System.out.println("-----------------------------------------");
        System.out.println("FIELD TYPE: " + scanFieldType.name());
        System.out.println("EXTRACTED VALUE: >" + match + "<");
        System.out.println("-----------------------------------------");
                    LOGGER.info(">" + match.trim() + "<");
                    break;
                }
                if (match != null && !match.isEmpty()) {
                    break;
                }
            }
            if (scanFieldType.equals(ScanFieldType.CERT_NO)) {
                LOGGER.info("Certificate No = [" + match + "]");
                if (match != null && !match.isEmpty()) {
                    expiryDocumentDTO.setCertificateNumber(match.trim());
                }
            } else if (scanFieldType.equals(ScanFieldType.ISSUE_PLACE)) {
                LOGGER.info("Issue Place = [" + match + "]");
                if (match != null && !match.isEmpty()) {
                    expiryDocumentDTO.setPlaceOfIssue(match.trim());
                }
            } else if (scanFieldType.equals(ScanFieldType.ISSUE_DATE)) {
                LOGGER.info("Issue Date = [" + match + "]");
                Date date = null;
                if (match != null && !match.isEmpty()) {
                    date = parseDate(match.trim());
                }
                expiryDocumentDTO.setIssueDate(date);
                if(date!=null) {
                  expiryDocumentDTO.setIssueDateString(dateFormat.format(date));
                }
            } else if (scanFieldType.equals(ScanFieldType.EXPIRY_DATE)) {
                LOGGER.info("Expiry Date = [" + match + "]");
                Date date = null;
                if (match != null && !match.isEmpty()) {
                    date = parseDate(match.trim());
                }
                if(date!=null) {
                  expiryDocumentDTO.setExpiryDateString(dateFormat.format(date));
                }
            } else if (scanFieldType.name().equals("ISSUING_AUTHORITY")) {
                // ADDED THIS BLOCK FOR ISSUING AUTHORITY
                LOGGER.info("Issuing Authority = [" + match + "]");
                if (match != null && !match.isEmpty()) {
                    expiryDocumentDTO.setIssuingAuthority(match.trim());
                }
            }
        }
        return expiryDocumentDTO;
    }
    @Override
	public boolean fileWriteIntoLocation(MultipartFile file, String oldPath, String newPath, String fileName) {
		// Check file Already Exist!
		if (oldPath != null) {
			oldPath = oldPath.replace("/", "\\");
			File oldFile = new File(oldPath);
			if (oldFile.exists()) {
				oldFile.delete();
			}
		}

		FileInputStream reader = null;
		FileOutputStream writer = null;

		try {
			// Create Folder Location
			newPath = newPath.replace("/", "\\");
			File createfolder = new File(newPath);
			if (!createfolder.exists()) {
				createfolder.mkdirs();
			}

			// Create File in Folder Location
			reader = (FileInputStream) file.getInputStream();
			byte[] buffer = new byte[1000];
			File outputFile = new File(newPath + fileName);
			outputFile.createNewFile();
			writer = new FileOutputStream(outputFile);
			while ((reader.read(buffer)) != -1) {
				writer.write(buffer);
			}
			LOGGER.info("Created file in the Path=" + newPath + fileName);
			return true;
		} catch (Exception e) {
			LOGGER.info("Failed to create file in the  Path= " + newPath + fileName);
			return false;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				LOGGER.info("Failed to create file in the Path= " + newPath + fileName);
				return false;
			}
		}
	}

	@Override
	public String shipProfileUpload(ShipProfileDTO shipProfileDTO, MultipartFile shipPic) {
		FileInputStream reader = null;
		FileOutputStream writer = null;
		String path = null;
		String dbPath = null;
		LOGGER.info("In  userDocumentUpload : start");
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("docuchainfileslocation");
			String basePath = configInfo.getConfigValue();
			LOGGER.info(" Base path from config table : " + basePath);
			String uploadingdir = basePath + File.separator + env.getProperty("ship.profilepic.location");
			LOGGER.info(" uploadingdir : " + uploadingdir);
			File file = new File(uploadingdir);
			if (!file.exists()) {
				LOGGER.info(" In mkdir : " + uploadingdir);
				file.mkdirs();
			}
			LOGGER.info(" uploadingdir : " + uploadingdir);

			String fileType = Files.getFileExtension(shipPic.getOriginalFilename());
			path = uploadingdir + File.separator + shipProfileDTO.getImo() + "." + fileType;
			dbPath = File.separator + env.getProperty("ship.profilepic.location")
					+ File.separator + shipProfileDTO.getImo() + "." + fileType;
			LOGGER.info(" file path : " + path);
			LOGGER.info("dbPath : " + dbPath);
			byte[] buffer = new byte[1000];
			File outputFile = new File(path);

			int totalBytes = 0;
			if(outputFile.exists()){
				outputFile.delete();
				outputFile.createNewFile();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			reader = (FileInputStream) shipPic.getInputStream();
			writer = new FileOutputStream(outputFile);

			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer);
				totalBytes += bytesRead;
			}
			dbPath = dbPath.replace(File.separator, "/");
		} catch (IOException e) {
			path = null;
			dbPath = null;
			LOGGER.error("Problem in userDocumentUpload file path : " + path);
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("In  userDocumentUpload : end : dbPath : " + dbPath);
		return dbPath;
	}

	@Override
	public String uploadUserPictureAndSaveFileFromLocal(MultipartFile multipartFile, String fileName) {

		FileInputStream reader = null;
		FileOutputStream writer = null;
		String path = null;
		String dbPath = null;
		LOGGER.info("In  userDocumentUpload : start");
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("docuchainfileslocation");
			String basePath = configInfo.getConfigValue();
			LOGGER.info(" Base path from config table : " + basePath);
			String uploadingdir = basePath + File.separator + env.getProperty("user.profile.picture.location");
			LOGGER.info(" uploadingdir : " + uploadingdir);
			File file = new File(uploadingdir);
			if (!file.exists()) {
				LOGGER.info(" In mkdir : " + uploadingdir);
				file.mkdirs();
			}
			LOGGER.info(" uploadingdir : " + uploadingdir);

			String fileType = Files.getFileExtension(multipartFile.getOriginalFilename());
			path = uploadingdir + File.separator + fileName.trim() + "." + fileType;
			dbPath = File.separator + env.getProperty("user.profile.picture.location") + File.separator + fileName.trim() + "." + fileType;
			LOGGER.info(" file path : " + path);
			LOGGER.info("dbPath : " + dbPath);
			byte[] buffer = new byte[1000];
			File outputFile = new File(path);

			int totalBytes = 0;
			if(outputFile.exists()){
				outputFile.delete();
				outputFile.createNewFile();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			reader = (FileInputStream) multipartFile.getInputStream();
			writer = new FileOutputStream(outputFile);

            int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer);
				totalBytes += bytesRead;
			}
			dbPath = dbPath.replace(File.separator, "/");
		} catch (IOException e) {
			path = null;
			dbPath = null;
			LOGGER.error("Problem in userDocumentUpload file path : " + path);
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("In  userDocumentUpload : end : dbPath : " + dbPath);
		return dbPath;
	}

	@Override
	public String organizationImageUpload(Long userId, MultipartFile shipPic) {
		FileInputStream reader = null;
		FileOutputStream writer = null;
		String path = null;
		String dbPath = null;
		LOGGER.info("In  userDocumentUpload : start");
		try {
			Config configInfo = configInfoRepository.findConfigByConfigKey("docuchainfileslocation");
			String basePath = configInfo.getConfigValue();
			LOGGER.info(" Base path from config table : " + basePath);
			String uploadingdir = basePath + File.separator + env.getProperty("logo.pic.path");
			LOGGER.info(" uploadingdir : " + uploadingdir);
			File file = new File(uploadingdir);
			if (!file.exists()) {
				LOGGER.info(" In mkdir : " + uploadingdir);
				file.mkdirs();
			}
			LOGGER.info(" uploadingdir : " + uploadingdir);

			String fileType = Files.getFileExtension(shipPic.getOriginalFilename());
			path = uploadingdir + File.separator + userId + "." + fileType;
			dbPath = File.separator + env.getProperty("organization.profilepic.location")
					+ File.separator + userId + "." + fileType;
			LOGGER.info(" file path : " + path);
			LOGGER.info("dbPath : " + dbPath);
			byte[] buffer = new byte[1000];
			File outputFile = new File(path);

			int totalBytes = 0;
			if(outputFile.exists()){
				outputFile.delete();
				outputFile.createNewFile();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			reader = (FileInputStream) shipPic.getInputStream();
			writer = new FileOutputStream(outputFile);

			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer);
				totalBytes += bytesRead;
			}
			dbPath = dbPath.replace(File.separator, "/");
		} catch (IOException e) {
			path = null;
			dbPath = null;
			LOGGER.error("Problem in userDocumentUpload file path : " + path);
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
				if(writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("In  userDocumentUpload : end : dbPath : " + dbPath);
		return dbPath;
	}
}

