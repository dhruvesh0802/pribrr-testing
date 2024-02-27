package com.pb.config;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for AWS s3 bucket.
 */
@Configuration
public class AWSConfiguration {

    private final GlobalConfiguration constant;

    @Autowired
    public AWSConfiguration(GlobalConfiguration constant) {
        this.constant = constant;
    }

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(constant.getAmazonS3().getRegion())
                .withCredentials(getAWSCredentialProvider())
                .build();
    }

    /**
     * This method use AccessKey,SecretKey from Constant file.
     *
     * @return provides authentication for AWS.
     */
    private AWSCredentialsProvider getAWSCredentialProvider() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(constant.getAmazonS3().getAccessKey(), constant.getAmazonS3().getSecretKey());
        return new AWSStaticCredentialsProvider(credentials);
    }
}
