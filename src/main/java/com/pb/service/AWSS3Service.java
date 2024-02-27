package com.pb.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.pb.config.GlobalConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AWSS3Service {
    private static final Logger LOG = LoggerFactory.getLogger(AWSS3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private GlobalConfiguration config;

    public String uploadFile(MultipartFile multipartFile, String path) {
        String fileName = null;
        File file = null;
        try {
            file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            PutObjectRequest request = new PutObjectRequest(config.getAmazonS3().getBucket(), path, file);
            s3Client.putObject(request);
            
            log.info("uploadFile :: FILE {} IS UPLOADED ",fileName);
            
            request.setCannedAcl(CannedAccessControlList.PublicReadWrite);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
        return fileName;
    }
    public String uploadFile1(File file, String path) {
        String fileName = null;
        try {
            fileName = generateFileName1(file);
            PutObjectRequest request = new PutObjectRequest(config.getAmazonS3().getBucket(), path, file);
            s3Client.putObject(request);
            s3Client.setObjectAcl(config.getAmazonS3().getBucket(), path, CannedAccessControlList.Private);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
        return fileName;
    }
    public String generateFileName1(File multiPart) {
        return UUID.randomUUID().toString() + "." + getFileExtension(multiPart.getName());
    }
    public String generateFileName(MultipartFile multiPart) {
        String filename = multiPart.getOriginalFilename();
        String fileExtension = getFileExtension(multiPart.getOriginalFilename());
        filename = filename.replace("."+fileExtension,"");
        filename = filename + "-T-"+UUID.randomUUID().toString()+"."+fileExtension;
        return filename;
    }

    private File convertMultiPartToFile(MultipartFile file) /*throws IOException*/ {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    public void deleteFile(String path) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(config.getAmazonS3().getBucket(), path));
       
            log.info("deleteFile : FILE IS DELETED FROM '{}' ",path);
        } catch (AmazonServiceException ex) {
            LOG.error("error [" + ex.getMessage() + "] occurred while deleting file");
        }
    }

    public URL downloadFile(String path, String receiptName) {
        GeneratePresignedUrlRequest req = null;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 15);

        req = new GeneratePresignedUrlRequest(config.getAmazonS3().getBucket(), path);
        ResponseHeaderOverrides overrides = new ResponseHeaderOverrides();
        overrides.setContentDisposition("attachment; filename=" + receiptName);

        req.setResponseHeaders(overrides);
        req.setExpiration(calendar.getTime());
        return s3Client.generatePresignedUrl(req);
    }

    public void deleteFileAndFolder(String serverPath, List<String> remainFiles) {

        String bucketPath = config.getAmazonS3().getBucket();
        try {
            for (S3ObjectSummary file : s3Client.listObjects(bucketPath, serverPath).getObjectSummaries()) {

                if(remainFiles == null ) {
                    s3Client.deleteObject(bucketPath, file.getKey());
                } else if(!remainFiles.contains(file.getKey())){
                    s3Client.deleteObject(bucketPath, file.getKey());
                }
            }
        } catch (AmazonServiceException ase) {
            log.error("Caught an AmazonServiceException, which means your request made it" +
                    " to Amazon S3, but was rejected with an error response for some reason.");
            log.error("Error Message:    " + ase.getMessage());
            log.error("AWS Error Code:   " + ase.getErrorCode());
            log.error("Error Type:       " + ase.getErrorType());
            log.error("Request ID:       " + ase.getRequestId());
        }
    }

    public String generateURL(String path){
        AmazonS3 s3 = s3Client;

        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis = expTimeMillis + (1000*60*60*10);
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest  = new GeneratePresignedUrlRequest(config.getAmazonS3().getBucket(), path)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        return s3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
