package com.pb.service;

import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.PlanDTO;
import com.pb.exception.CustomException;
import com.pb.model.PlanEntity;
import com.pb.repository.PlanRepository;
import com.pb.response.PaginationResponse;
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

import java.util.Objects;

@Service
@Slf4j
public class PlanService {
    
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    public PlanDTO addPlan(PlanDTO planDTO) {
        PlanEntity planEntity = planRepository.findByName(planDTO.getName());
        if (Objects.nonNull(planEntity))
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.PLAN_ALREADY_EXISTS);
        
        PlanEntity requestedPlanEntity = modelMapper.map(planDTO, PlanEntity.class);
        PlanEntity savePlanEntity = planRepository.save(requestedPlanEntity);
        
        log.info("addPlan :: PLAN '{}' IS ADDED ", savePlanEntity.getName());
        
        return modelMapper.map(savePlanEntity, PlanDTO.class);
    }
    
    public PlanDTO updatePlan(PlanDTO planDTO) {
        
        PlanEntity dbPlanDetails = planRepository.findById(planDTO.getId()).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PLAN_NOT_FOUND));
        
        PlanEntity planEntity = planRepository.findByNameAndIdNot(planDTO.getName(), planDTO.getId());
        if (Objects.nonNull(planEntity))
            throw new CustomException(HttpStatus.CONFLICT, ResponseMessageConstant.BANNER_ALREADY_EXIST);
        
        BeanUtils.copyProperties(planDTO, dbPlanDetails);
        dbPlanDetails.setIsActive(Boolean.TRUE);
        PlanEntity savePlanEntity = planRepository.save(dbPlanDetails);
        
        log.info("updatePlan :: PLAN '{}' IS UPDATED ", savePlanEntity.getName());
        
        return modelMapper.map(savePlanEntity, PlanDTO.class);
    }
    
    public PaginationResponse getAllPlans(Pageable pageable) {
        Page<PlanEntity> planEntityPage = planRepository.findAll(pageable);
        
        log.info("getAllPlans ::  PLAN LIST SIZE : {} for PAGE : {}",
                planEntityPage.getContent().size(),
                pageable.getPageNumber());
        
        TypeToken<Page<PlanDTO>> typeToken = new TypeToken<Page<PlanDTO>>() {
        };
        Page<PlanDTO> planDTOPage = modelMapper.map(planEntityPage, typeToken.getType());
        
        return PaginationUtils.getPaginationResponse(planDTOPage, Boolean.FALSE);
        
    }
    
    public void deletePlan(Long id) {
        PlanEntity planEntity = planRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PLAN_NOT_FOUND));
        
        planRepository.delete(planEntity);
        
        log.info("deletePlan ::  PLAN '{}' IS DELETED ", planEntity.getName());
    }
    public void changePlanStatus(Long id) {
        PlanEntity planEntity = planRepository.findById(id).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PLAN_NOT_FOUND));
    
        planEntity.setIsActive(!planEntity.getIsActive());
        planRepository.save(planEntity);
        
        log.info("changePlanStatus ::  PLAN '{}' STATUS IS CHANGED ", planEntity.getName());
    }
}
