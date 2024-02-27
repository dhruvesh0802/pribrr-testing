package com.pb.service;

import com.pb.config.GlobalConfiguration;
import com.pb.constants.UserType;
import com.pb.dto.profile.*;
import com.pb.dto.profile.response.*;
import com.pb.exception.CustomException;
import com.pb.model.*;
import com.pb.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ProfileCompanyDetailsRepository profileCompanyDetailsRepository;
    @Autowired
    ProfileMemberInfoRepository profileMemberInfoRepository;
    @Autowired
    ProfileCategoryAndProductRepository profileCategoryAndProductRepository;
    @Autowired
    ProfileArtWorkRepository profileArtWorkRepository;
    @Autowired
    ProfileQARepository profileQARepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired CompanyRepository companyRepository;
    @Autowired TimezoneRepository timezoneRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired AWSS3Service awss3Service;
    @Autowired
    private GlobalConfiguration globalConfiguration;
    @Autowired private PrivateBrandRepository privateBrandRepository;

    public ProfileEntity getProfileEntityObject(Long userId){
        ProfileEntity profileEntity = new ProfileEntity();
        if(profileRepository.findByUserEntityId(userId).isPresent()){
            profileEntity = profileRepository.findByUserEntityId(userId).get();
        } else {
            if(userId!=null) {
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "USER iDF IS NOT VALID"));
                profileEntity.setUserEntity(user);
            } else {
                throw new CustomException(HttpStatus.NOT_FOUND, "USER iD IS NOT VALID");
            }
        }
        return profileEntity;
    }

    public Integer addProfileCompanyDetails(CompanyDetailsDTO companyDetailsDTO) {
        ProfileEntity profileEntity = getProfileEntityObject(companyDetailsDTO.getUserId());

        ProfileCompanyDetailsEntity companyDetailsEntity = new ProfileCompanyDetailsEntity();
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        if(profileEntity.getProfileCompanyDetailsEntity()!=null){
            companyDetailsEntity = profileEntity.getProfileCompanyDetailsEntity();
            if(companyDetailsDTO.getCompanyLogo() != null) {
                String filePath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getUserProfile() + amazonS3.getCompanyLogo() + companyDetailsEntity.getCompanyLogoURL();
                awss3Service.deleteFile(filePath);
            }
        }
        BeanUtils.copyProperties(companyDetailsDTO,companyDetailsEntity);
        // uploading doc logo
        if(companyDetailsDTO.getCompanyLogo() != null) {
            String fileName = awss3Service.generateFileName(companyDetailsDTO.getCompanyLogo());
            String filePath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getUserProfile() + amazonS3.getCompanyLogo() + fileName;
            companyDetailsEntity.setCompanyLogoURL(fileName);
            awss3Service.uploadFile(companyDetailsDTO.getCompanyLogo(), filePath);
        }
        if(companyDetailsDTO.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(companyDetailsDTO.getCategoryId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category Not Found"));
            companyDetailsEntity.setCategoryEntity(category);
        }
        if(companyDetailsDTO.getCompanyTimezoneId() != null) {
            TimezoneEntity companyTimezone = timezoneRepository.findById(companyDetailsDTO.getCompanyTimezoneId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Timezone Not Found"));
            companyDetailsEntity.setCompanyTimezoneEntity(companyTimezone);
        }
        Optional<UserEntity> byId = userRepository.findById(companyDetailsDTO.getUserId());
        if(byId.isPresent()){
            UserEntity userEntity = byId.get();
            userEntity.setCompanyName(companyDetailsDTO.getCompanyName());
            userRepository.save(userEntity);
        }

        ProfileCompanyDetailsEntity save1 = profileCompanyDetailsRepository.save(companyDetailsEntity);
        profileEntity.setProfileCompanyDetailsEntity(save1);

        ProfileEntity save = profileRepository.save(profileEntity);
        return getUserProfileCompletePercentage(save.getUserEntity().getId());
    }

    public Integer addProfileMemberDetails(MemberDetailsDTO memberDetailsDTO) {
        ProfileEntity profileEntity = getProfileEntityObject(memberDetailsDTO.getUserId());

        ProfileMemberInfoEntity memberInfoEntity = new ProfileMemberInfoEntity();
        if(profileEntity.getProfileMemberInfoEntity()!=null){
            memberInfoEntity = profileEntity.getProfileMemberInfoEntity();
        }
        BeanUtils.copyProperties(memberDetailsDTO,memberInfoEntity);
        Optional<UserEntity> byId = userRepository.findById(memberDetailsDTO.getUserId());
        if(byId.isPresent()){
            UserEntity userEntity = byId.get();
            userEntity.setUserName(memberDetailsDTO.getName());
            if(!userEntity.getUserType().equals(UserType.ADMIN)){
                if(memberDetailsDTO.getProfilePic() != null) {
                    GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
                    String fileName = awss3Service.generateFileName(memberDetailsDTO.getProfilePic());
                    String filePath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getProfilePhoto() + fileName;
                    userEntity.setProfileUrl(fileName);
                    awss3Service.uploadFile(memberDetailsDTO.getProfilePic(), filePath);
                }
            }
            userRepository.save(userEntity);
        }
        memberInfoEntity.setMemberTimezoneEntity(timezoneRepository.findById(memberDetailsDTO.getMemberTimezoneId()).get());
        if(memberDetailsDTO.getDepartmentId() != null) {
            memberInfoEntity.setDepartmentEntity(departmentRepository.findById(memberDetailsDTO.getDepartmentId()).get());
        }
        ProfileMemberInfoEntity save2 = profileMemberInfoRepository.save(memberInfoEntity);
        profileEntity.setProfileMemberInfoEntity(save2);

        ProfileEntity save = profileRepository.save(profileEntity);


        return getUserProfileCompletePercentage(save.getUserEntity().getId());
    }

    public Integer addProfileProductDetails(ProductDetailsDTO productDetailsDTO) {
        ProfileEntity profileEntity = getProfileEntityObject(productDetailsDTO.getUserId());

        ProfileCategoryAndProductEntity categoryAndProduct = new ProfileCategoryAndProductEntity();
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        String name ="";
        if(profileEntity.getProfileCategoryAndProductEntity()!=null){
            categoryAndProduct = profileEntity.getProfileCategoryAndProductEntity();
            String folderPath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() +amazonS3.getUserProfile() ;
            if(productDetailsDTO.getFactoryAuditReport() != null) {
                String filePath = folderPath + amazonS3.getFactoryAuditReport() + categoryAndProduct.getFactoryAuditReportPath();
                awss3Service.deleteFile(filePath);
            }
            if(productDetailsDTO.getCertificateOfInsurance() != null) {
                String filePath = folderPath + amazonS3.getCertificateOfInsurance() + categoryAndProduct.getCertificateOfInsurancePath();
                awss3Service.deleteFile(filePath);
            }

            name = categoryAndProduct.getOtherCertificatePath()+",";
            if(Objects.nonNull(productDetailsDTO.getDeletedOtherCertificate()) && !productDetailsDTO.getDeletedOtherCertificate().isEmpty()) {
                for (String deletedFile : productDetailsDTO.getDeletedOtherCertificate()) {
                    deletedFile = deletedFile.replace(amazonS3.getS3BaseUrl(), "");
                    name = name.replace(deletedFile.split("/")[deletedFile.split("/").length-1]+",","");
                    awss3Service.deleteFile(amazonS3.getSubFolderName()+deletedFile);
                }
            }

            if(productDetailsDTO.getDocumentAndForum() != null) {
                String filePath = folderPath + amazonS3.getDocumentAndForum() + categoryAndProduct.getDocumentAndForumPath();
                awss3Service.deleteFile(filePath);
            }
            if(productDetailsDTO.getNDA() != null) {
                String filePath = folderPath + amazonS3.getNDA() + categoryAndProduct.getNDAPath();
                awss3Service.deleteFile(filePath);
            }
            String filePath = folderPath + amazonS3.getPrivateBrand()+productDetailsDTO.getUserId();
            List<Long> deletedPrivateBrand = productDetailsDTO.getDeletedPrivateBrand();
            if(deletedPrivateBrand!= null) {
                for (Long brandId : deletedPrivateBrand) {
                    PrivateBrandEntity privateBrandEntity = privateBrandRepository.findById(brandId).get();
                    awss3Service.deleteFile(filePath + "/" + privateBrandEntity.getLogoUrl());
                    privateBrandRepository.deleteById(brandId);
                }
            }
            /* awss3Service.deleteFileAndFolder(filePath,null);
            for (PrivateBrandEntity privateBrandEntity : categoryAndProduct.getPrivateBrand()) {
                privateBrandRepository.delete(privateBrandEntity);
            }
            categoryAndProduct.setPrivateBrand(null);*/

        }
        BeanUtils.copyProperties(productDetailsDTO,categoryAndProduct);
        String folderPath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() +amazonS3.getUserProfile() ;
        if(productDetailsDTO.getFactoryAuditReport()!=null) {
            String fileName = awss3Service.generateFileName(productDetailsDTO.getFactoryAuditReport());
            String filePath = folderPath + amazonS3.getFactoryAuditReport() + fileName;
            categoryAndProduct.setFactoryAuditReportPath(fileName);
            awss3Service.uploadFile(productDetailsDTO.getFactoryAuditReport(), filePath);
        }
        if(productDetailsDTO.getCertificateOfInsurance()!=null) {
            String fileName = awss3Service.generateFileName(productDetailsDTO.getCertificateOfInsurance());
            String filePath = folderPath + amazonS3.getCertificateOfInsurance() + fileName;
            categoryAndProduct.setCertificateOfInsurancePath(fileName);
            awss3Service.uploadFile(productDetailsDTO.getCertificateOfInsurance(), filePath);
        }
       /* if(productDetailsDTO.getOtherCertificate()!= null) {
            String fileName = awss3Service.generateFileName(productDetailsDTO.getOtherCertificate());
            String filePath = folderPath + amazonS3.getOtherCertificate() + fileName;
            categoryAndProduct.setOtherCertificatePath(fileName);
            awss3Service.uploadFile(productDetailsDTO.getOtherCertificate(), filePath);
        }*/

        List<MultipartFile> otherCertificate = productDetailsDTO.getOtherCertificate();
        if(otherCertificate != null) {
            for (MultipartFile multipartFile : otherCertificate) {
                String fileName = awss3Service.generateFileName(multipartFile);
                String filePath = folderPath + amazonS3.getOtherCertificate()+productDetailsDTO.getUserId()+"/" + fileName;
                name += fileName + ",";
                awss3Service.uploadFile(multipartFile, filePath);
            }
            name = name.substring(0,name.length()-1);
            categoryAndProduct.setOtherCertificatePath(name);
        }

        if(productDetailsDTO.getDocumentAndForum()!= null) {
            String fileName = awss3Service.generateFileName(productDetailsDTO.getDocumentAndForum());
            String filePath = folderPath + amazonS3.getDocumentAndForum() + fileName;
            categoryAndProduct.setDocumentAndForumPath(fileName);
            awss3Service.uploadFile(productDetailsDTO.getDocumentAndForum(), filePath);
        }
        if(productDetailsDTO.getNDA()!= null) {
            String fileName = awss3Service.generateFileName(productDetailsDTO.getNDA());
            String filePath = folderPath + amazonS3.getNDA() + fileName;
            categoryAndProduct.setNDAPath(fileName);
            awss3Service.uploadFile(productDetailsDTO.getNDA(), filePath);
        }

        
        ProfileCategoryAndProductEntity save3 = profileCategoryAndProductRepository.save(categoryAndProduct);
        if(productDetailsDTO.getPrivateBrand() != null) {
            List<PrivateBrandEntity> privateBrandEntityList = new ArrayList<>();

            for (PrivateBrandDto privateBrandDto : productDetailsDTO.getPrivateBrand()) {
                PrivateBrandEntity privateBrandEntity = new PrivateBrandEntity();
                privateBrandEntity.setName(privateBrandDto.getName());
                String fileName = awss3Service.generateFileName(privateBrandDto.getLogo());
                String filePath = folderPath + amazonS3.getPrivateBrand() + productDetailsDTO.getUserId() + "/" + fileName;
                privateBrandEntity.setLogoUrl(fileName);
                privateBrandEntity.setProfileCategoryAndProduct(save3);
                privateBrandEntityList.add(privateBrandEntity);
                privateBrandRepository.save(privateBrandEntity);

                awss3Service.uploadFile(privateBrandDto.getLogo(), filePath);
            }

            if (privateBrandEntityList.size() != 0) {
                categoryAndProduct.setPrivateBrand(privateBrandEntityList);
            }
        }
        profileEntity.setProfileCategoryAndProductEntity(save3);
        ProfileCategoryAndProductEntity profileCategoryAndProductEntity = profileCategoryAndProductRepository.findById(save3.getId()).get();
        ProfileEntity save = profileRepository.save(profileEntity);
        return getUserProfileCompletePercentage(save.getUserEntity().getId());
    }

    public Integer addProfileArtworkDetails(ArtworkDetailsDTO artworkDetailsDTO) {
        ProfileEntity profileEntity = getProfileEntityObject(artworkDetailsDTO.getUserId());
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        ProfileArtworkEntity artworkEntity = new ProfileArtworkEntity();
        String name="";
        if(profileEntity.getProfileArtworkEntity()!=null){
            artworkEntity = profileEntity.getProfileArtworkEntity();
            String folderpath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() +amazonS3.getUserProfile() ;
            if(artworkDetailsDTO.getArtworkRequirement() !=null) {
                String artworkReqpath = folderpath + amazonS3.getArtworkReq() + artworkEntity.getArtworkRequirementPath();
                awss3Service.deleteFile(artworkReqpath);
            }
            if(artworkDetailsDTO.getPrinterSpecs() !=null) {
                String printerspecPath = folderpath + amazonS3.getPrinterSpecs() + artworkEntity.getPrinterSpecsPath();
                awss3Service.deleteFile(printerspecPath);
            }
            if(artworkDetailsDTO.getDielines() !=null) {
                String dielinesPath = folderpath + amazonS3.getDielines() + artworkEntity.getDielinesPath();
                awss3Service.deleteFile(dielinesPath);
            }
            name = artworkEntity.getProductCatalogPath()+",";
            if(Objects.nonNull(artworkDetailsDTO.getDeletedProductCatalog()) && !artworkDetailsDTO.getDeletedProductCatalog().isEmpty()) {
                for (String deletedFile : artworkDetailsDTO.getDeletedProductCatalog()) {
                    deletedFile = deletedFile.replace(amazonS3.getS3BaseUrl(), "");
                    name = name.replace(deletedFile.split("/")[deletedFile.split("/").length-1]+",","");
                    awss3Service.deleteFile(amazonS3.getSubFolderName()+deletedFile);
                }

            }
        }

        BeanUtils.copyProperties(artworkDetailsDTO,artworkEntity);
        String folderpath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() +amazonS3.getUserProfile() ;
        if(artworkDetailsDTO.getArtworkRequirement()!=null) {
            String fileName = awss3Service.generateFileName(artworkDetailsDTO.getArtworkRequirement());
            String filePath = folderpath + amazonS3.getArtworkReq() + fileName;
            artworkEntity.setArtworkRequirementPath(fileName);
            awss3Service.uploadFile(artworkDetailsDTO.getArtworkRequirement(), filePath);
        }
        if(artworkDetailsDTO.getPrinterSpecs()!=null) {
            String fileName = awss3Service.generateFileName(artworkDetailsDTO.getPrinterSpecs());
            String filePath = folderpath + amazonS3.getPrinterSpecs() + fileName;
            artworkEntity.setPrinterSpecsPath(fileName);
            awss3Service.uploadFile(artworkDetailsDTO.getPrinterSpecs(), filePath);
        }
        if(artworkDetailsDTO.getDielines() != null) {
            String fileName = awss3Service.generateFileName(artworkDetailsDTO.getDielines());
            String filePath = folderpath + amazonS3.getDielines() + fileName;
            artworkEntity.setDielinesPath(fileName);
            awss3Service.uploadFile(artworkDetailsDTO.getDielines(), filePath);
        }
        List<MultipartFile> productCatalog = artworkDetailsDTO.getProductCatalog();
        if(productCatalog != null) {
            for (MultipartFile multipartFile : productCatalog) {
                String fileName = awss3Service.generateFileName(multipartFile);
                String filePath = folderpath + amazonS3.getProductCatalog()+artworkDetailsDTO.getUserId()+"/" + fileName;
                name += fileName + ",";
                awss3Service.uploadFile(multipartFile, filePath);
            }
            name = name.substring(0,name.length()-1);
            artworkEntity.setProductCatalogPath(name);
        }

        ProfileArtworkEntity save4 = profileArtWorkRepository.save(artworkEntity);
        profileEntity.setProfileArtworkEntity(save4);
        ProfileEntity save = profileRepository.save(profileEntity);
        return getUserProfileCompletePercentage(save.getUserEntity().getId());
    }

    public Integer addProfileQADetails(QADetailsDTO qaDetailsDTO) {
        ProfileEntity profileEntity = getProfileEntityObject(qaDetailsDTO.getUserId());

        ProfileQAEntity qaEntity = new ProfileQAEntity();
        if(profileEntity.getProfileQAEntity()!=null){
            qaEntity = profileEntity.getProfileQAEntity();
        }
        BeanUtils.copyProperties(qaDetailsDTO,qaEntity);
        ProfileQAEntity save5 = profileQARepository.save(qaEntity);
        profileEntity.setProfileQAEntity(save5);

        ProfileEntity save = profileRepository.save(profileEntity);
        return getUserProfileCompletePercentage(save.getUserEntity().getId());
    }
    public Integer getUserProfileCompletePercentage(Long userId) {
        Integer count = 0;
        ProfileEntity profileEntity = profileRepository.findByUserEntityId(userId).
                orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "PROFILE IS NOT CREATED YET"));;
        if(profileEntity.getProfileCompanyDetailsEntity()!=null){
            count =count+20;
        }
        if(profileEntity.getProfileMemberInfoEntity()!=null){
            count =count+20;
        }
        if(profileEntity.getProfileCategoryAndProductEntity()!=null){
            count =count+20;
        } if(profileEntity.getProfileArtworkEntity()!=null){
            count =count+20;
        } if(profileEntity.getProfileQAEntity()!=null){
            count =count+20;
        }
        return count;
    }

    public CompanyDetailsResDTO getProfileCompanyDetails(Long userId) {

        CompanyDetailsResDTO companyDetailsDTO = new CompanyDetailsResDTO();
        Optional<UserEntity> byId = userRepository.findById(userId);
        if(byId.isPresent()){
            UserEntity userEntity = byId.get();
            companyDetailsDTO.setCompanyName(userEntity.getCompanyName());
        }
        ProfileEntity profileEntity = null;
        Optional<ProfileEntity> byUserEntityId = profileRepository.findByUserEntityId(userId);
        if(byUserEntityId.isPresent()){
            profileEntity = byUserEntityId.get();
        }
        if(profileEntity != null){
            ProfileCompanyDetailsEntity companyDetailsEntity = new ProfileCompanyDetailsEntity();
            GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
            if(profileEntity.getProfileCompanyDetailsEntity()!=null){
                companyDetailsEntity = profileEntity.getProfileCompanyDetailsEntity();

            }

            BeanUtils.copyProperties(companyDetailsEntity,companyDetailsDTO);

            if(companyDetailsEntity.getCategoryEntity() != null) {
                companyDetailsDTO.setCategoryId(companyDetailsEntity.getCategoryEntity().getId());
            }
            if(companyDetailsEntity.getCompanyTimezoneEntity() != null) {
                companyDetailsDTO.setCompanyTimezoneId(companyDetailsEntity.getCompanyTimezoneEntity().getId());
            }
            // uploading doc logo
            String filePath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName()+amazonS3.getUserProfile() + amazonS3.getCompanyLogo() + companyDetailsEntity.getCompanyLogoURL();
            companyDetailsDTO.setCompanyLogoURL(filePath);
        }

        return companyDetailsDTO;
    }
    public MemberDetailsResDTO getProfileMemberDetails(Long userId) {
        MemberDetailsResDTO memberDetailsResDTO = new MemberDetailsResDTO();
        Optional<UserEntity> byId = userRepository.findById(userId);
        if(byId.isPresent()){
            UserEntity userEntity = byId.get();
            memberDetailsResDTO.setName(userEntity.getUserName());
            GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
            String filePath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName()+ amazonS3.getProfilePhoto() + userEntity.getProfileUrl();
            memberDetailsResDTO.setProfilePath(filePath);
        }
        ProfileEntity profileEntity = null;
        Optional<ProfileEntity> byUserEntityId = profileRepository.findByUserEntityId(userId);
        if(byUserEntityId.isPresent()){
            profileEntity = byUserEntityId.get();
        }
        if(profileEntity != null){
            ProfileMemberInfoEntity profileMemberInfoEntity = new ProfileMemberInfoEntity();
            //GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
            if(profileEntity.getProfileMemberInfoEntity()!=null){
                profileMemberInfoEntity = profileEntity.getProfileMemberInfoEntity();
            }


            BeanUtils.copyProperties(profileMemberInfoEntity,memberDetailsResDTO);
            if(profileMemberInfoEntity.getMemberTimezoneEntity() != null) {
                memberDetailsResDTO.setMemberTimezoneId(profileMemberInfoEntity.getMemberTimezoneEntity().getId());
            }
            memberDetailsResDTO.setIsBroker(profileMemberInfoEntity.getIsBroker());
            if (profileMemberInfoEntity.getDepartmentEntity() != null) {
                memberDetailsResDTO.setDepartmentId(profileMemberInfoEntity.getDepartmentEntity().getId());
            }
        }


        return memberDetailsResDTO;
    }

    public ProductDetailsResDTO getProfileProductDetails(Long userId) {
        ProfileEntity profileEntity = profileRepository.findByUserEntityId(userId).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"PROFILE NOT FOUND"));

        ProfileCategoryAndProductEntity profileCategoryAndProductEntity = new ProfileCategoryAndProductEntity();
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        if(profileEntity.getProfileCategoryAndProductEntity()!=null){
            profileCategoryAndProductEntity = profileEntity.getProfileCategoryAndProductEntity();

        }
        ProductDetailsResDTO productDetailsResDTO = new ProductDetailsResDTO();
        BeanUtils.copyProperties(profileCategoryAndProductEntity,productDetailsResDTO);
        String folderPath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName() +amazonS3.getUserProfile() ;
        productDetailsResDTO.setCertificateOfInsurancePath(folderPath+amazonS3.getCertificateOfInsurance()+profileCategoryAndProductEntity.getCertificateOfInsurancePath());
        productDetailsResDTO.setDocumentAndForumPath(folderPath+amazonS3.getDocumentAndForum()+profileCategoryAndProductEntity.getDocumentAndForumPath());
        productDetailsResDTO.setFactoryAuditReportPath(folderPath+amazonS3.getFactoryAuditReport()+profileCategoryAndProductEntity.getFactoryAuditReportPath());
        productDetailsResDTO.setNDAPath(folderPath+amazonS3.getNDA()+profileCategoryAndProductEntity.getNDAPath());
        /*productDetailsResDTO.setOtherCertificatePath(folderPath+amazonS3.getOtherCertificate()+profileCategoryAndProductEntity.getOtherCertificatePath());*/
        List<String> fileUrls = new ArrayList<>();
        if (Objects.nonNull(profileCategoryAndProductEntity.getOtherCertificatePath())){
            for (String fileName : profileCategoryAndProductEntity.getOtherCertificatePath().split(",")) {
                String filePath = folderPath+ amazonS3.getOtherCertificate()+userId+"/" + fileName;
                fileUrls.add(filePath);
            }
            productDetailsResDTO.setOtherCertificatePath(fileUrls);
        }

        List<PrivateBrandEntity> privateBrand = profileCategoryAndProductEntity.getPrivateBrand();
        List<PrivateBrandResDto> privateBrandResDtos = new ArrayList<>();
        for (PrivateBrandEntity privateBrandEntity : privateBrand) {
            PrivateBrandResDto dto = new PrivateBrandResDto();
            dto.setId(privateBrandEntity.getId());
            dto.setName(privateBrandEntity.getName());
            String filePath = folderPath + amazonS3.getPrivateBrand() + userId + "/" + privateBrandEntity.getLogoUrl();
            dto.setLogoPath(filePath);
            privateBrandResDtos.add(dto);
        }
        productDetailsResDTO.setPrivateBrand(privateBrandResDtos);
        return productDetailsResDTO;
    }
    public ArtworkDetailsResDTO getProfileArtWorkDetails(Long userId) {
        ProfileEntity profileEntity = profileRepository.findByUserEntityId(userId).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"PROFILE NOT FOUND"));

        ProfileArtworkEntity profileArtworkEntity = new ProfileArtworkEntity();
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        if(profileEntity.getProfileArtworkEntity()!=null){
            profileArtworkEntity = profileEntity.getProfileArtworkEntity();
        }
        ArtworkDetailsResDTO artworkDetailsResDTO = new ArtworkDetailsResDTO();
        BeanUtils.copyProperties(profileArtworkEntity,artworkDetailsResDTO);
        String folderPath = amazonS3.getS3BaseUrl()+ amazonS3.getUploadFolderName() +amazonS3.getUserProfile();
        List<String> fileUrls = new ArrayList<>();
       if (Objects.nonNull(profileArtworkEntity.getProductCatalogPath())){
           for (String fileName : profileArtworkEntity.getProductCatalogPath().split(",")) {
               String filePath = folderPath+ amazonS3.getProductCatalog()+userId+"/" + fileName;
               fileUrls.add(filePath);
           }
           artworkDetailsResDTO.setProductCatalogPath(fileUrls);
       }
        artworkDetailsResDTO.setArtworkRequirementPath(folderPath+amazonS3.getArtworkReq() +profileArtworkEntity.getArtworkRequirementPath());
        artworkDetailsResDTO.setPrinterSpecsPath(folderPath+amazonS3.getPrinterSpecs() +profileArtworkEntity.getPrinterSpecsPath());
        artworkDetailsResDTO.setDielinesPath(folderPath+amazonS3.getDielines() +profileArtworkEntity.getDielinesPath());
        return artworkDetailsResDTO;
    }

    public QADetailsResDTO getProfileQADetails(Long userId) {
        ProfileEntity profileEntity = profileRepository.findByUserEntityId(userId).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"PROFILE NOT FOUND"));
        ProfileQAEntity profileQAEntity = new ProfileQAEntity();
        if(profileEntity.getProfileQAEntity()!=null){
            profileQAEntity = profileEntity.getProfileQAEntity();

        }
        QADetailsResDTO qaDetailsResDTO = new QADetailsResDTO();
        BeanUtils.copyProperties(profileQAEntity,qaDetailsResDTO);

        return qaDetailsResDTO;
    }

    public Map<String, String> getUserProfileCompletePercentageMap(Long userId) {
        Integer count = 0;
        ProfileEntity profileEntity = profileRepository.findByUserEntityId(userId).
                orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "PROFILE IS NOT CREATED YET"));

        Map<String,String> map = new HashMap<>();
        map.put("CompanyDetails","false");
        map.put("ProfileMember","false");
        map.put("ProfileCategoryAndProduct","false");
        map.put("ProfileArtwork","false");
        map.put("ProfileQA","false");

        if(profileEntity.getProfileCompanyDetailsEntity()!=null){
            if(profileEntity.getProfileCompanyDetailsEntity().getNoOfEmployee() != null) {
                count = count + 20;
                map.put("CompanyDetails", "true");
            }
        }
        if(profileEntity.getProfileMemberInfoEntity()!=null){
            if(profileEntity.getProfileMemberInfoEntity().getPhone()!= null) {
                count = count + 20;
                map.put("ProfileMember", "true");
            }
        }
        if(profileEntity.getProfileCategoryAndProductEntity()!=null){
            count =count+20;
            map.put("ProfileCategoryAndProduct","true");
        } if(profileEntity.getProfileArtworkEntity()!=null){
            count =count+20;
            map.put("ProfileArtwork","true");
        } if(profileEntity.getProfileQAEntity()!=null){
            count =count+20;
            map.put("ProfileQA","true");
        }
        map.put("Percentage",count.toString());
        return map;
    }
}
