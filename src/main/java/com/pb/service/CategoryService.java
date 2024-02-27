package com.pb.service;

import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.CategoryDTO;
import com.pb.dto.CategoryFilteredDTO;
import com.pb.dto.DepartmentDTO;
import com.pb.exception.CustomException;
import com.pb.model.CategoryEntity;
import com.pb.model.DepartmentEntity;
import com.pb.repository.CategoryRepository;
import com.pb.repository.DepartmentRepository;
import com.pb.response.CategoryResponse;
import com.pb.response.PaginationResponse;
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
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DepartmentRepository departmentRepository;
    
    
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Optional<CategoryEntity> categoryEntityOptional =
                categoryRepository.findByName(categoryDTO.getName());
        if (categoryEntityOptional.isPresent())
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.CATEGORY_ALREADY_EXISTS);
        
        CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
        
        DepartmentEntity departmentEntity = departmentRepository.findById(categoryDTO.getDepartmentId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.DEPARTMENT_NOT_FOUND));
        
        categoryEntity.setDepartmentEntity(departmentEntity);
        
        return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
    }
    
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity dbCategoryEntity = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CATEGORY_NOT_FOUND));
        
        Optional<CategoryEntity> categoryCheck = categoryRepository.findByNameAndIdNot(categoryDTO.getName(), categoryDTO.getId());
        if (categoryCheck.isPresent())
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.CATEGORY_ALREADY_EXISTS);
        
        DepartmentEntity departmentEntity = departmentRepository.findById(categoryDTO.getDepartmentId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.DEPARTMENT_NOT_FOUND));
        
        BeanUtils.copyProperties(categoryDTO, dbCategoryEntity);
        
        dbCategoryEntity.setDepartmentEntity(departmentEntity);
        
        return modelMapper.map(categoryRepository.save(dbCategoryEntity), CategoryDTO.class);
        
    }
    
    public PaginationResponse getAllCategory(CategoryFilteredDTO categoryFilteredDTO, Pageable pageable) {
        Page<CategoryEntity> categoryEntityPage;
        String filteredName = Objects.nonNull(categoryFilteredDTO.getName()) ? categoryFilteredDTO.getName() : "";
        
        if (Boolean.FALSE.equals(categoryFilteredDTO.isPagination()))
            categoryEntityPage = categoryRepository.findAllByNameContainingOrderByNameAsc(PaginationUtils.getPageRequestOfMaxInteger(),
                    filteredName);
        else
            categoryEntityPage = categoryRepository.findAllByNameContainingOrderByNameAsc(pageable, filteredName);
        
        log.info("getAllCategory ::  CATEGORY LIST SIZE : {} for PAGE : {}",
                categoryEntityPage.getContent().size(),
                pageable.getPageNumber());
        
        List<CategoryResponse> categoryResponseList = categoryEntityPage.getContent()
                .stream()
                .map(categoryEntity -> {
                    CategoryResponse map = modelMapper.map(categoryEntity, CategoryResponse.class);
                    DepartmentDTO departmentDTO = new DepartmentDTO();
                    departmentDTO.setId(categoryEntity.getDepartmentEntity().getId());
                    departmentDTO.setName(categoryEntity.getDepartmentEntity().getName());
                    map.setDepartment(departmentDTO);
                    return map;
                })
                .collect(Collectors.toList());
        
        PageImpl<CategoryResponse> bannerDTOSPageImpl = new PageImpl<>(categoryResponseList, pageable, categoryEntityPage.getTotalElements());
        return PaginationUtils.getPaginationResponse(bannerDTOSPageImpl, Boolean.FALSE);
        
    }

    public List<CategoryResponse> getAllCategoryByDepartment(CategoryFilteredDTO categoryFilteredDTO) {

       return categoryRepository.findAllByDepartmentEntityId(categoryFilteredDTO.getDepartmentId())
                .stream()
                .map(categoryEntity -> {
                    CategoryResponse map = modelMapper.map(categoryEntity, CategoryResponse.class);
                    DepartmentDTO departmentDTO = new DepartmentDTO();
                    departmentDTO.setId(categoryEntity.getDepartmentEntity().getId());
                    departmentDTO.setName(categoryEntity.getDepartmentEntity().getName());
                    map.setDepartment(departmentDTO);
                    return map;
                })
                .collect(Collectors.toList());

    }
    
    public void deleteCategory(Long id) {
        CategoryEntity dbCategoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CATEGORY_NOT_FOUND));
        
        categoryRepository.delete(dbCategoryEntity);
    }
    
    public void changeCategoryStatus(Long id) {
        CategoryEntity dbCategoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CATEGORY_NOT_FOUND));
        dbCategoryEntity.setIsActive(!dbCategoryEntity.getIsActive());
        categoryRepository.save(dbCategoryEntity);
    }
    
    public List<CategoryDTO> getAllCategoryByDepartmentId(Long departmentId) {
    
        return categoryRepository.findAllByDepartmentEntityId(departmentId)
                .stream()
                .map(categoryEntity -> {
                    CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
                    categoryDTO.setDepartmentId(categoryEntity.getDepartmentEntity().getId());
                    return categoryDTO;
                }).collect(Collectors.toList());
    }
    
 
}
