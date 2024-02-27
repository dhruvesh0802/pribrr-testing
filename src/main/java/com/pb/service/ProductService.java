package com.pb.service;

import com.pb.config.GlobalConfiguration;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.ProductDTO;
import com.pb.dto.ProductResDTO;
import com.pb.exception.CustomException;
import com.pb.model.CategoryEntity;
import com.pb.model.DepartmentEntity;
import com.pb.model.ProductEntity;
import com.pb.model.UserEntity;
import com.pb.repository.CategoryRepository;
import com.pb.repository.DepartmentRepository;
import com.pb.repository.ProductRepository;
import com.pb.repository.UserRepository;
import com.pb.response.PaginationResponse;
import com.pb.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AWSS3Service awss3Service;
    @Autowired
    private GlobalConfiguration globalConfiguration;
    @Autowired
    private ModelMapper modelMapper;
    private final MapperFacade mapperFacade;
    ProductService(MapperFacade mapperFacade){
        this.mapperFacade =mapperFacade;
    }
    
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, List<MultipartFile> productMedia) {

        ProductEntity productEntity = new ProductEntity();
        if(productDTO.getId() != null ){
            productEntity = productRepository.findById(productDTO.getId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "PRODUCT NOT FOUND"));
        }
        BeanUtils.copyProperties(productDTO,productEntity);
        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
        String name = "";
        if(productDTO.getId() != null){
            name = productEntity.getMediaUrl()+",";
        }
        if( productDTO.getDeletedFiles() != null) {
            for (String deletedFile : productDTO.getDeletedFiles()) {
                deletedFile = deletedFile.replace(amazonS3.getS3BaseUrl(), "");
                name = name.replace(deletedFile.split("/")[deletedFile.split("/").length-1]+",","");
                awss3Service.deleteFile(amazonS3.getSubFolderName()+deletedFile);
            }
        }
        if(productMedia != null) {
            for (MultipartFile file : productMedia) {
                String fileName = awss3Service.generateFileName(file);
                String filePath = amazonS3.getSubFolderName() + amazonS3.getUploadFolderName() + amazonS3.getProductMedia() + productDTO.getUserId() + "/" + fileName;
                name += fileName + ",";
                awss3Service.uploadFile(file, filePath);
            }
        }
        UserEntity user = userRepository.findById(productDTO.getUserId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "USER NOT FOUND"));
        productEntity.setUserEntity(user);
        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "CATEGORY NOT FOUND"));
        productEntity.setCategoryEntity(category);
        DepartmentEntity department = departmentRepository.findById(productDTO.getDepartmentId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "DEPARTMENT NOT FOUND"));
        productEntity.setDepartmentEntity(department);
        name = name.substring(0,name.length()-1);
        productEntity.setMediaUrl(name);
        productRepository.save(productEntity);
        log.info("addProduct :: Product '{}' IS ADDED ", productEntity.getName());
        return mapperFacade.map(productEntity, ProductDTO.class);
    }

    public PaginationResponse getAllProduct(Pageable pageable,Long userId) {
        Page<ProductEntity> productEntityPage = productRepository.findAllByUserEntityId(pageable,userId);
    
        log.info("getAllProduct ::  Product LIST SIZE : {} for PAGE : {}",
                productEntityPage.getContent().size(),
                pageable.getPageNumber());
    
        List<ProductResDTO> productResDTOList = productEntityPage.getContent()
                .stream()
                .map(productEntity -> {
                    ProductResDTO map = mapperFacade.map(productEntity, ProductResDTO.class);
                    map.setCategoryId(productEntity.getCategoryEntity().getId());
                    map.setDepartmentId(productEntity.getDepartmentEntity().getId());
                    map.setUserId(productEntity.getUserEntity().getId());
                    List<String> fileUrls = new ArrayList<>();
                    for (String fileName : productEntity.getMediaUrl().split(",")) {
                        GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
                        String filePath = amazonS3.getS3BaseUrl()+amazonS3.getUploadFolderName() + amazonS3.getProductMedia()+productEntity.getUserEntity().getId()+"/" + fileName;
                        fileUrls.add(filePath);
                    }
                    map.setMediaURLs(fileUrls);
                    return map;
                })
                .collect(Collectors.toList());
        
        PageImpl<ProductResDTO> productResDTOPage = new PageImpl<>(productResDTOList,pageable,productEntityPage.getTotalElements());
        return PaginationUtils.getPaginationResponse(productResDTOPage, Boolean.FALSE);
    }
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PRODUCT_NOT_FOUND));
    
        productRepository.delete(productEntity);
    
        log.info("deleteProduct ::  Product '{}' IS DELETED ", productEntity.getName());
    }
}
