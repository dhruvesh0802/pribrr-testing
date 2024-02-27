package com.pb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:globalconfig-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "app")
public class GlobalConfiguration {

    private AmazonS3 amazonS3;

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    public void setAmazonS3(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }


    public static class AmazonS3 {
        private String region;
        private String bucket;
        private String subFolderName;
        private String accessKey;
        private String secretKey;
        private String urlUpToBucket;
        private String uploadFolderName;
        private String profilePhoto;

        private String bannerImages;
        private String companyLogo;
        private String companyDocs;
        private String productMedia;
        private String s3BaseUrl;
        private String userProfile;
        private String artworkReq;
        private String printerSpecs;
        private String dielines;
        private String factoryAuditReport;
        private String certificateOfInsurance;
        private String otherCertificate;
        private String documentAndForum;
        private String NDA;
        private String privateBrand;
        private String productCatalog;


        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getSubFolderName() {
            return subFolderName;
        }

        public void setSubFolderName(String subFolderName) {
            this.subFolderName = subFolderName;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getUrlUpToBucket() {
            return urlUpToBucket;
        }

        public void setUrlUpToBucket(String urlUpToBucket) {
            this.urlUpToBucket = urlUpToBucket;
        }

        public String getUploadFolderName() {
            return uploadFolderName;
        }

        public void setUploadFolderName(String uploadFolderName) {
            this.uploadFolderName = uploadFolderName;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }
    
        public String getBannerImages() {
            return bannerImages;
        }
    
        public void setBannerImages(String bannerImages) {
            this.bannerImages = bannerImages;
        }

        public String getCompanyLogo() {
            return companyLogo;
        }

        public void setCompanyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
        }

        public String getCompanyDocs() {
            return companyDocs;
        }

        public void setCompanyDocs(String companyDocs) {
            this.companyDocs = companyDocs;
        }

        public String getProductMedia() {
            return productMedia;
        }

        public void setProductMedia(String productMedia) {
            this.productMedia = productMedia;
        }

        public String getS3BaseUrl() {
            return s3BaseUrl;
        }
    
        public void setS3BaseUrl(String s3BaseUrl) {
            this.s3BaseUrl = s3BaseUrl;
        }

        public String getArtworkReq() {
            return artworkReq;
        }

        public void setArtworkReq(String artworkReq) {
            this.artworkReq = artworkReq;
        }

        public String getPrinterSpecs() {
            return printerSpecs;
        }

        public void setPrinterSpecs(String printerSpecs) {
            this.printerSpecs = printerSpecs;
        }

        public String getDielines() {
            return dielines;
        }

        public void setDielines(String dielines) {
            this.dielines = dielines;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public String getFactoryAuditReport() {
            return factoryAuditReport;
        }

        public void setFactoryAuditReport(String factoryAuditReport) {
            this.factoryAuditReport = factoryAuditReport;
        }

        public String getCertificateOfInsurance() {
            return certificateOfInsurance;
        }

        public void setCertificateOfInsurance(String certificateOfInsurance) {
            this.certificateOfInsurance = certificateOfInsurance;
        }

        public String getOtherCertificate() {
            return otherCertificate;
        }

        public void setOtherCertificate(String otherCertificate) {
            this.otherCertificate = otherCertificate;
        }

        public String getDocumentAndForum() {
            return documentAndForum;
        }

        public void setDocumentAndForum(String documentAndForum) {
            this.documentAndForum = documentAndForum;
        }

        public String getNDA() {
            return NDA;
        }

        public void setNDA(String NDA) {
            this.NDA = NDA;
        }

        public String getPrivateBrand() { return privateBrand; }

        public void setPrivateBrand(String privateBrand) { this.privateBrand = privateBrand; }

        public String getProductCatalog() {
            return productCatalog;
        }

        public void setProductCatalog(String productCatalog) {
            this.productCatalog = productCatalog;
        }
    }
}
