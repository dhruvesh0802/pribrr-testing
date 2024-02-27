package com.pb.service;

import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.DepartmentDTO;
import com.pb.dto.DepartmentFilteredDTO;
import com.pb.exception.CustomException;
import com.pb.model.CategoryEntity;
import com.pb.model.DepartmentEntity;
import com.pb.repository.CategoryRepository;
import com.pb.repository.DepartmentRepository;
import com.pb.response.PaginationResponse;
import com.pb.response.UserResponse;
import com.pb.utils.PaginationUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        Optional<DepartmentEntity> departmentEntityOptional = departmentRepository.findByName(departmentDTO.getName());
        if (departmentEntityOptional.isPresent())
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.DEPARTMENT_ALREADY_EXISTS);
        
        DepartmentEntity departmentEntity = modelMapper.map(departmentDTO, DepartmentEntity.class);
        
        return modelMapper.map(departmentRepository.save(departmentEntity), DepartmentDTO.class);
        
    }
    
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = departmentRepository.findById(departmentDTO.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.DEPARTMENT_NOT_FOUND));
        
        DepartmentEntity departmentEntity1 =
                departmentRepository.findByNameAndIdNot(departmentDTO.getName(), departmentDTO.getId());
        if (Objects.nonNull(departmentEntity1))
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.DEPARTMENT_ALREADY_EXISTS);
        
        BeanUtils.copyProperties(departmentDTO, departmentEntity);
        
        return modelMapper.map(departmentRepository.save(departmentEntity), DepartmentDTO.class);
        
    }
    
    public PaginationResponse getAllDepartment(DepartmentFilteredDTO departmentFilteredDTO, Pageable pageable) {
        Page<DepartmentEntity> departmentEntityPage;
        String filteredName = Objects.nonNull(departmentFilteredDTO.getName()) ? departmentFilteredDTO.getName() : "";
        
        if (Boolean.FALSE.equals(departmentFilteredDTO.isPagination())) {
            if(departmentFilteredDTO.getIsCategory()){
                departmentEntityPage = departmentRepository.findAllByNameContainingAndIsActiveTrue(PaginationUtils.getPageRequestOfMaxInteger(),
                        filteredName);
            } else {
                departmentEntityPage = departmentRepository.findAllByNameContaining(PaginationUtils.getPageRequestOfMaxInteger(),
                        filteredName);
            }

        } else {
            if(departmentFilteredDTO.getIsCategory()) {
                departmentEntityPage = departmentRepository.findAllByNameContainingAndIsActiveTrue(pageable, filteredName);
            } else {
                departmentEntityPage = departmentRepository.findAllByNameContaining(pageable, filteredName);
            }
        }
        log.info("getAllDepartment ::  DEPARTMENT LIST SIZE : {} for PAGE : {}",
                departmentEntityPage.getContent().size(),
                pageable.getPageNumber());
        
        TypeToken<Page<UserResponse>> typeToken = new TypeToken<Page<UserResponse>>() {
        };
        
        Page<DepartmentDTO> departmentDTOPage = modelMapper.map(departmentEntityPage, typeToken.getType());
        
        return PaginationUtils.getPaginationResponse(departmentDTOPage, departmentFilteredDTO.isPagination());
        
    }
    
    public List<CategoryEntity> deleteDepartment(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.DEPARTMENT_NOT_FOUND));
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByDepartmentEntityId(departmentEntity.getId());
        if (categoryEntities.size()>0){
            return categoryEntities;
        }
        else {
            departmentRepository.delete(departmentEntity);
            return null;
        }
    }
    
    public List<CategoryEntity> changeDepartmentStatus(Long id) {
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.DEPARTMENT_NOT_FOUND));
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByDepartmentEntityId(departmentEntity.getId());
        if (categoryEntities.size()>0){
            return categoryEntities;
        }
        else {
            departmentEntity.setIsActive(!departmentEntity.getIsActive());
            departmentRepository.save(departmentEntity);
            return null;
        }
    }

    public DepartmentDTO getDepartmentById(Long id){
        DepartmentEntity departmentEntity = departmentRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.DEPARTMENT_NOT_FOUND));
        DepartmentDTO map = modelMapper.map(departmentEntity,DepartmentDTO.class);
        return map;
    }
}
