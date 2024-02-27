package com.pb.service;

import com.pb.config.GlobalConfiguration;
import com.pb.constants.ResponseMessageConstant;
import com.pb.constants.ValidationMessageConstant;
import com.pb.dto.BannerDTO;
import com.pb.exception.CustomException;
import com.pb.model.BannerEntity;
import com.pb.model.CategoryEntity;
import com.pb.repository.BannerRepository;
import com.pb.response.PaginationResponse;
import com.pb.response.UserResponse;
import com.pb.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BannerService {
    
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private AWSS3Service awss3Service;
    @Autowired
    private GlobalConfiguration globalConfiguration;
    @Autowired
    private ModelMapper modelMapper;
    private final MapperFacade mapperFacade;
    BannerService(MapperFacade mapperFacade){
        this.mapperFacade =mapperFacade;
    }
    
    @Transactional
    public BannerDTO addBanner(BannerDTO bannerDTO, MultipartFile bannerImage) {
        if(Objects.isNull(bannerImage))
            throw new CustomException(HttpStatus.BAD_REQUEST, ValidationMessageConstant.PLEASE_UPLOAD_BANNER_IMAGE);
       

        
        BannerEntity bannerEntity = modelMapper.map(bannerDTO, BannerEntity.class);
        String fileName = awss3Service.generateFileName(bannerImage);
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        String filePath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getBannerImages() + fileName;
        bannerEntity.setBannerPath(fileName);
        awss3Service.uploadFile(bannerImage,filePath);
        
        bannerRepository.save(bannerEntity);
    
        log.info("addBanner :: BANNER '{}' IS ADDED ", bannerEntity.getTitle());
    
        return mapperFacade.map(bannerEntity, BannerDTO.class);
    }
    
    public PaginationResponse getAllBanner(Pageable pageable) {
        Page<BannerEntity> bannerEntityPage = bannerRepository.findAll(pageable);

        log.info("getAllBanner ::  BANNER LIST SIZE : {} for PAGE : {}",
                bannerEntityPage.getContent().size(),
                pageable.getPageNumber());
    
        List<BannerDTO> bannerDTOList = bannerEntityPage.getContent()
                .stream()
                .map(bannerEntity -> mapperFacade.map(bannerEntity, BannerDTO.class))
                .collect(Collectors.toList());
        PageImpl<BannerDTO> bannerDTOSPageImpl = new PageImpl<>(bannerDTOList,pageable,bannerEntityPage.getTotalElements());

        return PaginationUtils.getPaginationResponse(bannerDTOSPageImpl, Boolean.FALSE);
    }
    
    public BannerDTO updateBanner(BannerDTO bannerDTO, MultipartFile bannerImage) {
        BannerEntity dbBannerDetails = bannerRepository.findById(bannerDTO.getId()).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.BANNER_NOT_FOUND));
    
        bannerDTO.setBannerPath(dbBannerDetails.getBannerPath());
        BeanUtils.copyProperties(bannerDTO, dbBannerDetails);
        if(Objects.nonNull(bannerImage)){
            GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
            String bannerDirectoryPath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getBannerImages();
            awss3Service.deleteFile(bannerDirectoryPath + dbBannerDetails.getBannerPath());
            String fileName = awss3Service.generateFileName(bannerImage);
            String filePath = bannerDirectoryPath + fileName;
            dbBannerDetails.setBannerPath(fileName);
            awss3Service.uploadFile(bannerImage,filePath);
        }
    
        BannerEntity bannerEntity = bannerRepository.save(dbBannerDetails);
    
        log.info("updateBanner :: BANNER '{}' IS UPDATED ", bannerEntity.getTitle());
        
        return mapperFacade.map(bannerEntity,BannerDTO.class);
    }
    
    public void deleteBanner(Long id) {
        BannerEntity bannerEntity = bannerRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.BANNER_NOT_FOUND));
    
        bannerRepository.delete(bannerEntity);
    
        log.info("deleteBanner ::  BANNER '{}' IS DELETED ", bannerEntity.getTitle());
    }
    public PaginationResponse getAllBannerByLocation(Pageable pageable,String location) {
        Page<BannerEntity> bannerEntityPage = bannerRepository.findAllByLocation(pageable, location);

        log.info("getAllBanner ::  BANNER LIST SIZE : {} for PAGE : {}",
                bannerEntityPage.getContent().size(),
                pageable.getPageNumber());

        List<BannerDTO> bannerDTOList = bannerEntityPage.getContent()
                .stream()
                .map(bannerEntity -> mapperFacade.map(bannerEntity, BannerDTO.class))
                .collect(Collectors.toList());

        PageImpl<BannerDTO> bannerDTOSPageImpl = new PageImpl<>(bannerDTOList,pageable,bannerDTOList.size());
        return PaginationUtils.getPaginationResponse(bannerDTOSPageImpl, Boolean.FALSE);
    }
}
