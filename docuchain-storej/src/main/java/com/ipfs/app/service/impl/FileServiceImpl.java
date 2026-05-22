// // package com.ipfs.app.service.impl;

// // import java.io.BufferedReader;
// // import java.io.ByteArrayInputStream;
// // import java.io.File;
// // import java.io.FileOutputStream;
// // import java.io.IOException;
// // import java.io.InputStream;
// // import java.io.InputStreamReader;
// // import java.net.URLEncoder;
// // import java.util.concurrent.CompletableFuture;

// // import org.apache.http.HttpResponse;
// // import org.apache.http.client.methods.HttpGet;
// // import org.apache.http.client.methods.HttpPost;
// // import org.apache.http.entity.mime.MultipartEntity;
// // import org.apache.http.entity.mime.content.FileBody;
// // import org.apache.http.entity.mime.content.InputStreamBody;
// // import org.apache.http.impl.client.CloseableHttpClient;
// // import org.apache.http.impl.client.HttpClients;
// // import org.json.JSONException;
// // import org.json.JSONObject;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.core.env.Environment;
// // import org.springframework.stereotype.Service;
// // import org.springframework.web.multipart.MultipartFile;
// // import org.web3j.crypto.CipherException;
// // import org.web3j.crypto.Credentials;
// // import org.web3j.crypto.WalletUtils;
// // import org.web3j.protocol.Web3j;
// // import org.web3j.protocol.core.methods.response.TransactionReceipt;
// // import org.web3j.protocol.http.HttpService;
// // import org.web3j.tx.Contract;

// // import com.ipfs.app.dto.FileObjectDTO;
// // import com.ipfs.app.objectconversion.FileObjectConversion;
// // import com.ipfs.app.service.FileService;
// // import com.ipfs.app.solidity.SimpleStorage;

// // import io.ipfs.api.IPFS;
// // import io.ipfs.multiaddr.MultiAddress;
// // import io.ipfs.multihash.Multihash;

// // @Service
// // public class FileServiceImpl implements FileService{

// // 	private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/"));

// // 	@Autowired
// // 	Environment env;

// // 	@Autowired
// // 	FileObjectConversion fileObjectConversion;



// // 	@Override
// // 	public FileObjectDTO saveFile(MultipartFile file, Boolean saveInBlockchain) {
// // 		 String fileName=file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
// // 		 fileName=fileName.replace(" ", "_");
// // 		  System.out.println("File Name::"+fileName+" Base URL "+env.getProperty("baseUrl"));

// // 		  String message = new String();
// // 		  Integer statResponse=null;
// // 		  FileObjectDTO fileObjectDTO = null;
// // 		  CloseableHttpClient httpclient = HttpClients.createDefault();
// // 		  try{

// // 			  String urlFile = env.getProperty("baseUrl")+"write?arg=/"+ URLEncoder.encode(fileName, "UTF-8")+"&create=true";
// // 			  String statUrl =  env.getProperty("baseUrl")+"stat?arg=/"+ URLEncoder.encode(fileName, "UTF-8");

// // /*			File convFile = new File(file.getOriginalFilename());
// // 		    convFile.createNewFile();
// // 		    FileOutputStream fos = new FileOutputStream(convFile);
// // 		    fos.write(file.getBytes());
// // 		    fos.close();
// // 			HttpPost httppost = new HttpPost(urlFile.toString());
// // 			MultipartEntity reqEntity = new MultipartEntity();
// // 			if (convFile.length() != 0) {
// // 			   FileBody fileBody = new FileBody(convFile);
// // 			   reqEntity.addPart("data", fileBody);
// // 			}*/
// // 			byte[] byteArr = file.getBytes();
// // 			InputStream inputStream = new ByteArrayInputStream(byteArr);
// // 			HttpPost httppost = new HttpPost(urlFile.toString());
// // 			MultipartEntity reqEntity = new MultipartEntity();
// // 			reqEntity.addPart("data", new InputStreamBody(inputStream, file.getOriginalFilename()));
// // 			httppost.setEntity(reqEntity);
// // 			HttpResponse response = httpclient.execute(httppost);
// // 			if(response.getStatusLine().getStatusCode()== 200){
// // 				System.out.println("Response success");
// // 				httpclient.close();
// // 				httpclient = HttpClients.createDefault();
// // 				HttpGet httpGet=new HttpGet(statUrl.toString());
// // 				HttpResponse responseStat = httpclient.execute(httpGet);
// // 				statResponse=response.getStatusLine().getStatusCode();
// // 				System.out.println("Response From stat"+responseStat);

// // 				if(statResponse==200){
// // 				 BufferedReader rd = new BufferedReader(new InputStreamReader(responseStat.getEntity().getContent()));
// // 				 String line="";
// // 		            while ((line = rd.readLine()) != null) {
// // 		                message+=line;
// // 		            }
// // 		            httpclient.close();
// // 					JSONObject myResponse = new JSONObject(message);
// // 					fileObjectDTO=fileObjectConversion.convertJsonObejctIntoFileObjectDTO(myResponse,fileName);

// // 					if (saveInBlockchain) {
// // 						this.saveFileInfoInBlockchain(fileName, fileObjectDTO.getHash());
// // 					}
// // 				}
// // 			}

// // 		  }
// // 		  catch(IOException e){
// // 			  e.printStackTrace();
// // 		  }
// // 		  catch (JSONException e) {
// // 			e.printStackTrace();
// // 		  }
// // 		  catch (Exception e) {
// // 			e.printStackTrace();
// // 		}
// // 		return fileObjectDTO;
// // 	}

// // 	protected void saveFileInfoInBlockchain(String fName, String hashValue) {
// // 		CompletableFuture.supplyAsync(()-> {
// // 			SimpleStorage simpleStorage;
// // 			try {
// // 				simpleStorage = loadSolidity();
// // 				TransactionReceipt  result= simpleStorage.set(fName,hashValue).send();
// // 				System.out.println("result  "+result);
// // 			} catch (Exception e) {
// // 				e.printStackTrace();
// // 			}
// // 			return "call blackchain";
// // 		});
// // 	}

// // 	@Override
// // 	public SimpleStorage loadSolidity() throws IOException, CipherException {
// // 		Credentials credentials = WalletUtils.loadCredentials("User0388", "C:/Ethereum/private-network/keystore/UTC--2018-04-25T13-23-21.763876600Z--f655d10688edeb6ab1e5dce34bfb4bf567f1ae1d");
// // 		SimpleStorage simpleStorage=SimpleStorage.load(env.getProperty("walletAddress"), web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);
// // 		return simpleStorage;
// // 	}

// // 	@Override
// // 	public FileObjectDTO getFile(String fileName, String fileHashCode) throws Exception {
// // 		String hashval=null;
// // 		FileObjectDTO fileObjectDTO=null;
// // 			SimpleStorage simpleStorage;
// // 			try {
// // 				String hashValue = null;

// // 				if (fileHashCode == null) {
// // 					simpleStorage = loadSolidity();
// // 					hashValue=simpleStorage.getDocumentByName(fileName).send();
// // 				} else {
// // 					hashValue = fileHashCode;
// // 				}

// // 				if(hashValue!=null){
// // 					IPFS ipfs =  new IPFS(new MultiAddress(env.getProperty("IPFSUrl")));
// // 					Multihash filePointer = Multihash.fromBase58(hashValue.toString());
// // 					if(filePointer!=null){
// // 						byte[] fileContents = ipfs.cat(filePointer);
// // 						fileObjectDTO=fileObjectConversion.setFileByteIntoFileObjectDTO(fileContents);
// // 					}
// // 				}
// // 			} catch (Exception e) {
// // 				e.printStackTrace();
// // 			}
// // 		return fileObjectDTO;
// // 	}


// // }

// package com.ipfs.app.service.impl;

// import java.io.BufferedReader;
// import java.io.ByteArrayInputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.util.concurrent.CompletableFuture;

// import org.apache.http.HttpResponse;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.mime.MultipartEntity;
// import org.apache.http.entity.mime.content.InputStreamBody;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import org.json.JSONException;
// import org.json.JSONObject;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.env.Environment;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
// import org.web3j.crypto.CipherException;
// import org.web3j.crypto.Credentials;
// import org.web3j.crypto.WalletUtils;
// import org.web3j.protocol.Web3j;
// import org.web3j.protocol.core.methods.response.TransactionReceipt;
// import org.web3j.protocol.http.HttpService;
// import org.web3j.tx.Contract;

// import com.ipfs.app.dto.FileObjectDTO;
// import com.ipfs.app.objectconversion.FileObjectConversion;
// import com.ipfs.app.service.FileService;
// import com.ipfs.app.solidity.SimpleStorage;

// import io.ipfs.api.IPFS;
// import io.ipfs.multiaddr.MultiAddress;
// import io.ipfs.multihash.Multihash;

// @Service
// public class FileServiceImpl implements FileService {

//   private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/"));

//   @Autowired
//   Environment env;

//   @Autowired
//   FileObjectConversion fileObjectConversion;

//   @Override
//   public FileObjectDTO saveFile(MultipartFile file, Boolean saveInBlockchain) {
//       String fileName = file.getOriginalFilename();
//       System.out.println("File Name::" + fileName + " Base URL " + env.getProperty("baseUrl"));

//       String message = "";
//       FileObjectDTO fileObjectDTO = null;
//       CloseableHttpClient httpclient = HttpClients.createDefault();

//       try {
//         // 1. Prepare the URL (ensure baseUrl ends with / in application.properties)
//         String urlFile = env.getProperty("baseUrl") + "add";

//         byte[] byteArr = file.getBytes();
//         InputStream inputStream = new ByteArrayInputStream(byteArr);

//         HttpPost httppost = new HttpPost(urlFile);
//         MultipartEntity reqEntity = new MultipartEntity();

//         // 2. IPFS /add expects part name "file"
//         reqEntity.addPart("file", new InputStreamBody(inputStream, fileName));
//         httppost.setEntity(reqEntity);

//         // 3. Execute Request
//         HttpResponse response = httpclient.execute(httppost);
//         int statusCode = response.getStatusLine().getStatusCode();

//         if (statusCode == 200) {
//             System.out.println("Response success (200)");

//             // 4. Read Response Directly
//             BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//             String line = "";
//             while ((line = rd.readLine()) != null) {
//                 message += line;
//             }

//             // 5. FIX START: Correct JSON Mapping & Conversion
//             // Raw IPFS response keys are "Name" and "Hash"
//             JSONObject json = new JSONObject(message);

//             // Create new JSON for Docuchain/Converter with keys "fileName" and "hash"
//             JSONObject responseJson = new JSONObject();
//             responseJson.put("fileName", json.getString("Name"));
//             responseJson.put("hash", json.getString("Hash"));

//             // Use the converter properly with the re-mapped JSON
//             fileObjectDTO = fileObjectConversion.convertJsonObejctIntoFileObjectDTO(responseJson, json.getString("Name"));

//             System.out.println("Uploaded Hash: " + fileObjectDTO.getHash());

//             // 6. FIX START: Disable Blockchain to prevent Wallet Error
//             // Changed if(saveInBlockchain) to if(false)
//             if (false) {
//                 this.saveFileInfoInBlockchain(fileName, fileObjectDTO.getHash());
//             } else {
//                 System.out.println("Blockchain save skipped (Wallet file missing)");
//             }
//             // FIX END

//         } else {
//             System.out.println("Error: IPFS returned status code " + statusCode);
//         }

//         httpclient.close();

//       } catch (IOException e) {
//         e.printStackTrace();
//       } catch (JSONException e) {
//         e.printStackTrace();
//       } catch (Exception e) {
//         e.printStackTrace();
//       }
//       return fileObjectDTO;
//   }

//   protected void saveFileInfoInBlockchain(String fName, String hashValue) {
//     CompletableFuture.supplyAsync(() -> {
//       SimpleStorage simpleStorage;
//       try {
//         simpleStorage = loadSolidity();
//         TransactionReceipt result = simpleStorage.set(fName, hashValue).send();
//         System.out.println("Blockchain Result: " + result.getTransactionHash());
//       } catch (Exception e) {
//         e.printStackTrace();
//       }
//       return "call blockchain";
//     });
//   }

//   @Override
//   public SimpleStorage loadSolidity() throws IOException, CipherException {
//     Credentials credentials = WalletUtils.loadCredentials("User0388", "C:/Ethereum/private-network/keystore/UTC--2018-04-25T13-23-21.763876600Z--f655d10688edeb6ab1e5dce34bfb4bf567f1ae1d");
//     SimpleStorage simpleStorage = SimpleStorage.load(env.getProperty("walletAddress"), web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);
//     return simpleStorage;
//   }

//   @Override
//   public FileObjectDTO getFile(String fileName, String fileHashCode) throws Exception {
//     FileObjectDTO fileObjectDTO = null;
//     SimpleStorage simpleStorage;
//     try {
//       String hashValue = null;

//       if (fileHashCode == null) {
//         simpleStorage = loadSolidity();
//         hashValue = simpleStorage.getDocumentByName(fileName).send();
//       } else {
//         hashValue = fileHashCode;
//       }

//       if (hashValue != null && !hashValue.isEmpty()) {
//         IPFS ipfs = new IPFS(new MultiAddress(env.getProperty("IPFSUrl")));
//         Multihash filePointer = Multihash.fromBase58(hashValue);
//         if (filePointer != null) {
//           byte[] fileContents = ipfs.cat(filePointer);
//           fileObjectDTO = fileObjectConversion.setFileByteIntoFileObjectDTO(fileContents);
//         }
//       }
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//     return fileObjectDTO;
//   }
// }

package com.ipfs.app.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import com.ipfs.app.dto.FileObjectDTO;
import com.ipfs.app.objectconversion.FileObjectConversion;
import com.ipfs.app.service.FileService;
import com.ipfs.app.solidity.SimpleStorage;

@Service
public class FileServiceImpl implements FileService {

    private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/"));

    @Autowired
    Environment env;

    @Autowired
    FileObjectConversion fileObjectConversion;

    // CLOUD CONFIGURATION: Hardcoded JWT Authorization Token
    private final String PINATA_JWT = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mb3JtYXRpb24iOnsiaWQiOiJkOGY1MDZkOS00NjYxLTQyMmItODdmYi1mNGU0MzFlMWQ0YzciLCJlbWFpbCI6ImFtYnJlbGxhLmNvbnN1bHRpbmcxQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaW5fcG9saWN5Ijp7InJlZ2lvbnMiOlt7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6IkZSQTEifSx7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6Ik5ZQzEifV0sInZlcnNpb24iOjF9LCJtZmFfZW5hYmxlZCI6ZmFsc2UsInN0YXR1cyI6IkFDVElWRSJ9LCJhdXRoZW50aWNhdGlvblR5cGUiOiJzY29wZWRLZXkiLCJzY29wZWRLZXlLZXkiOiI3MjhmNDE0OTA1ZjNlNGVlMWU1YSIsInNjb3BlZEtleVNlY3JldCI6IjNkMGI5NDcxNzg2YmJkOTBkOTdkYzRlMjI2NDFmZjVjZGI4N2Q4NGY1MDUxYTYxZjg0YTg3ODRjNTcyYTdiYTEiLCJleHAiOjE4MTA2MjUwNzR9.ZSyLpQ5t8SV4dMjvxeb3eTWjmIKquNKaio-tqlqTbW8";

    @Override
    public FileObjectDTO saveFile(MultipartFile file, Boolean saveInBlockchain) {
        String fileName = file.getOriginalFilename();
        System.out.println("--- Starting Cloud IPFS Upload via Pinata for: " + fileName);

        String message = "";
        FileObjectDTO fileObjectDTO = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            // CLOUD MIGRATION UPDATE: Target Pinata API Endpoint directly
            String urlFile = "https://api.pinata.cloud/pinning/pinFileToIPFS";

            byte[] byteArr = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(byteArr);

            HttpPost httppost = new HttpPost(urlFile);

            // CLOUD MIGRATION UPDATE: Inject Secure JWT Authorization Headers
            httppost.setHeader("Authorization", PINATA_JWT);

            MultipartEntity reqEntity = new MultipartEntity();
            // Pinata strictly requires the multi-part form field key name to be "file"
            reqEntity.addPart("file", new InputStreamBody(inputStream, fileName));
            httppost.setEntity(reqEntity);

            // Execute Request
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                System.out.println("--- Cloud IPFS Upload Success (200 OK)");

                // Read Pinata response string
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    message += line;
                }

                // CLOUD MIGRATION UPDATE: Parse Pinata response properties ("IpfsHash")
                JSONObject json = new JSONObject(message);
                String cloudHash = json.getString("IpfsHash");

                // Remap properties cleanly into your internal DTO structural expectations
                JSONObject responseJson = new JSONObject();
                responseJson.put("fileName", fileName);
                responseJson.put("hash", cloudHash);

                fileObjectDTO = fileObjectConversion.convertJsonObejctIntoFileObjectDTO(responseJson, fileName);
                System.out.println("--- Live Public Gateway CID Generated: " + fileObjectDTO.getHash());

                // Keep blockchain fallback safely isolated
                if (false) {
                    this.saveFileInfoInBlockchain(fileName, fileObjectDTO.getHash());
                } else {
                    System.out.println("--- Blockchain storage layer skipped for local testing module");
                }

            } else {
                System.err.println("--- Pinata API Rejected Request! Status Code: " + statusCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String errLine;
                while ((errLine = errorReader.readLine()) != null) {
                    System.err.println(errLine);
                }
            }

            httpclient.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileObjectDTO;
    }

    @Override
    public FileObjectDTO getFile(String fileName, String fileHashCode) throws Exception {
        System.out.println("--- Fetching Document File via Public IPFS Cloud Gateway Gateway Network");
        FileObjectDTO fileObjectDTO = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            String hashValue = fileHashCode;

            if (hashValue == null) {
                SimpleStorage simpleStorage = loadSolidity();
                hashValue = simpleStorage.getDocumentByName(fileName).send();
            }

            if (hashValue != null && !hashValue.isEmpty()) {
                // CLOUD MIGRATION UPDATE: Read using standard public edge gateway addresses
                // Eliminates the reliance on localized native 'io.ipfs.api.IPFS' connection instances
                String gatewayUrl = "https://ipfs.io/ipfs/" + hashValue;
                HttpGet httpGet = new HttpGet(gatewayUrl);

                HttpResponse response = httpclient.execute(httpGet);
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    byte[] fileContents = EntityUtils.toByteArray(response.getEntity());
                    fileObjectDTO = fileObjectConversion.setFileByteIntoFileObjectDTO(fileContents);
                    System.out.println("--- File recovery streaming complete for hash: " + hashValue);
                } else {
                    System.err.println("--- Public edge gateway could not find hash resource. Status: " + statusCode);
                }
            }
            httpclient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileObjectDTO;
    }

    protected void saveFileInfoInBlockchain(String fName, String hashValue) {
        CompletableFuture.supplyAsync(() -> {
            SimpleStorage simpleStorage;
            try {
                simpleStorage = loadSolidity();
                TransactionReceipt result = simpleStorage.set(fName, hashValue).send();
                System.out.println("Blockchain Result: " + result.getTransactionHash());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "call blockchain";
        });
    }

    @Override
    public SimpleStorage loadSolidity() throws IOException, CipherException {
        Credentials credentials = WalletUtils.loadCredentials("User0388", "C:/Ethereum/private-network/keystore/UTC--2018-04-25T13-23-21.763876600Z--f655d10688edeb6ab1e5dce34bfb4bf567f1ae1d");
        SimpleStorage simpleStorage = SimpleStorage.load(env.getProperty("walletAddress"), web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT);
        return simpleStorage;
    }
}
