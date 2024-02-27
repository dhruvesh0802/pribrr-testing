package com.pb.service;

import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.CategoryDTO;
import com.pb.dto.CategoryFilteredDTO;
import com.pb.dto.SubCategoryDTO;
import com.pb.exception.CustomException;
import com.pb.model.CategoryEntity;
import com.pb.model.SubCategoryEntity;
import com.pb.repository.CategoryRepository;
import com.pb.repository.SubCategoryRepository;
import com.pb.response.PaginationResponse;
import com.pb.response.SubCategoryResponse;
import com.pb.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubCategoryService {
    
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    
    
    public SubCategoryDTO addSubCategory(SubCategoryDTO subCategoryDTO) {
        Optional<SubCategoryEntity> subCategoryEntityOptional =
                subCategoryRepository.findByName(subCategoryDTO.getName());
        if(subCategoryEntityOptional.isPresent())
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.SUB_CATEGORY_ALREADY_EXISTS);
    
        SubCategoryEntity subCategoryEntity = modelMapper.map(subCategoryDTO, SubCategoryEntity.class);
    

        CategoryEntity categoryEntity = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CATEGORY_NOT_FOUND));
        subCategoryEntity.setCategoryEntity(categoryEntity);
        
        return modelMapper.map(subCategoryRepository.save(subCategoryEntity),SubCategoryDTO.class);
    }
    
    public SubCategoryDTO updateSubCategory(SubCategoryDTO subCategoryDTO) {
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(subCategoryDTO.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUB_CATEGORY_NOT_FOUND));
        
        Optional<SubCategoryEntity> subCategoryCheck = subCategoryRepository.findByNameAndIdNot(subCategoryDTO.getName(),subCategoryDTO.getId());
        if(subCategoryCheck.isPresent())
            throw new CustomException(HttpStatus.CONFLICT,ResponseMessageConstant.SUB_CATEGORY_ALREADY_EXISTS);
        CategoryEntity categoryEntity = categoryRepository.findById(subCategoryDTO.getCategoryId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CATEGORY_NOT_FOUND));
        BeanUtils.copyProperties(subCategoryDTO,subCategoryEntity);
        subCategoryEntity.setCategoryEntity(categoryEntity);
        return modelMapper.map(subCategoryRepository.save(subCategoryEntity),SubCategoryDTO.class);
    }
    
    public PaginationResponse getAllSubCategory(CategoryFilteredDTO categoryFilteredDTO, Pageable pageable) {
        Page<SubCategoryEntity> subCategoryEntityPage;
        String filteredName = Objects.nonNull(categoryFilteredDTO.getName()) ? categoryFilteredDTO.getName() : "";
    
        if (Boolean.FALSE.equals(categoryFilteredDTO.isPagination()))
            subCategoryEntityPage = subCategoryRepository.findAllByNameContaining(PaginationUtils.getPageRequestOfMaxInteger(),
                    filteredName);
        else
            subCategoryEntityPage = subCategoryRepository.findAllByNameContaining(pageable, filteredName);

        log.info("getAllCategory ::  CATEGORY LIST SIZE : {} for PAGE : {}",
                subCategoryEntityPage.getContent().size(),
                pageable.getPageNumber());
    
        List<SubCategoryResponse> subCategoryResponseList = subCategoryEntityPage.getContent()
                .stream()
                .map(subCategoryEntity -> {
                    SubCategoryResponse map = modelMapper.map(subCategoryEntity, SubCategoryResponse.class);
                    CategoryDTO categoryDTO =new CategoryDTO();
                    categoryDTO.setId(subCategoryEntity.getCategoryEntity().getId());
                    categoryDTO.setName(subCategoryEntity.getCategoryEntity().getName());
                    map.setCategoryDTO(categoryDTO);
                    return map;
                })
                .collect(Collectors.toList());
    
        PageImpl<SubCategoryResponse> subCategoryResponses = new PageImpl<>(subCategoryResponseList,pageable,subCategoryResponseList.size());
        PaginationResponse paginationResponse = PaginationUtils.getPaginationResponse(subCategoryResponses, Boolean.FALSE);
        
        return paginationResponse;
    }
    
    public void deleteSubCategory(Long id) {
        SubCategoryEntity dbSubCategoryEntity = subCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUB_CATEGORY_NOT_FOUND));
    
        subCategoryRepository.delete(dbSubCategoryEntity);
    }
    
    public void changeSubCategoryStatus(Long id) {
        SubCategoryEntity dbSubCategoryEntity = subCategoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUB_CATEGORY_NOT_FOUND));
        dbSubCategoryEntity.setIsActive(!dbSubCategoryEntity.getIsActive());
        subCategoryRepository.save(dbSubCategoryEntity);
    }
}
