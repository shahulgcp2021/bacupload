package com.bacuti.service;

import com.bacuti.repository.UnitOfMeasureRepository;
import com.bacuti.service.dto.UnitOfMeasureDTO;
import com.bacuti.service.mapper.UnitofMeasureMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitofMeasureService {
    private final Logger log = LoggerFactory.getLogger(UnitofMeasureService.class);

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final UnitofMeasureMapper unitofMeasureMapper;

    public UnitofMeasureService(UnitOfMeasureRepository unitOfMeasureRepository, UnitofMeasureMapper unitofMeasureMapper) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitofMeasureMapper = unitofMeasureMapper;
    }


    @Transactional
    public List<UnitOfMeasureDTO> findAll() {
        return unitofMeasureMapper.toDto(unitOfMeasureRepository.findAll());
    }


}
