package com.bacuti.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bacuti.domain.DataPoint;
import com.bacuti.repository.DataPointRepository;
import com.bacuti.service.DataUploadOperationService;
import com.bacuti.service.dto.datauploadoperation.DataPointDto;
import com.bacuti.service.dto.datauploadoperation.DataUploadOperationDto;
import com.bacuti.service.mapper.DataUploadOperationMapper;
import com.bacuti.common.errors.BusinessException;
import com.bacuti.config.Constants;

/**
 * <p>
 * Implementation base class for {@link DataUploadOperationService}
 * </p>
 *
 * @author Aakash created on Jun 24, 2022
 */
@Service
public class DataUploadOperationServiceImpl implements DataUploadOperationService {

    @Autowired
    DataPointRepository dataPointRepository;

    @Autowired
    DataUploadOperationMapper dataUploadOperationMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataUploadOperationDto> findAll() {
        List<DataUploadOperationDto> dataUploadOperationDtos = new ArrayList<>();
        Map<String, List<DataPointDto>> dataUploadOperationMap = new LinkedHashMap<>();
        List<DataPoint> dataPoints = dataPointRepository.findAllByOrderByIdAsc();
        dataPoints.forEach(dataPoint -> {
            if (dataUploadOperationMap.containsKey(dataPoint.getOperation())) {
                dataUploadOperationMap.get(dataPoint.getOperation()).add(dataUploadOperationMapper.dataPointToDataPointDto(dataPoint));
            } else {
                List<DataPointDto> dataPointDtos = new ArrayList<>();
                dataPointDtos.add(dataUploadOperationMapper.dataPointToDataPointDto(dataPoint));
                dataUploadOperationMap.put(dataPoint.getOperation(), dataPointDtos);
            }
        });
        dataUploadOperationMap.forEach((key, value) -> {
            DataUploadOperationDto dataUploadOperationDto = new DataUploadOperationDto();
            dataUploadOperationDto.setId(dataUploadOperationDtos.size() + 1L);
            dataUploadOperationDto.setOperation(key);
            dataUploadOperationDto.setIsSelected(findIsDataSelected(value));
            dataUploadOperationDto.setDataPointDtos(value);
            dataUploadOperationDtos.add(dataUploadOperationDto);
        });
        return dataUploadOperationDtos;
    }

    /**
     * Checks whether the data point of an operation is selected.
     *
     * @return true if  data point of an operation is selected.
     */
    private Boolean findIsDataSelected(List<DataPointDto> dataPointDtos) {
        return dataPointDtos.stream().anyMatch(DataPointDto::getIsSelected);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataPoint updateDataPoint(Long id, Boolean isSelected) {
        DataPoint dataPoint = dataPointRepository.findById(id).orElseThrow(() -> new BusinessException("Data Not found",
            HttpStatus.PRECONDITION_FAILED.value()));
        dataPoint.setIsSelected(isSelected);
        return dataPointRepository.save(dataPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String operationName, Boolean isSelected) {
        if (Objects.isNull(operationName) || Objects.isNull(isSelected))
            throw new BusinessException("Invalid Input",
                HttpStatus.BAD_REQUEST.value());
        if (!(Constants.MY_OPERATIONS.equalsIgnoreCase(operationName)
            || Constants.MY_SUPPLY_CHAIN.equalsIgnoreCase(operationName))) {
            throw new BusinessException("Invalid Data Upload Operations",
                HttpStatus.PRECONDITION_FAILED.value());
        }
        dataPointRepository.updateIsSelectedByOperation(operationName, isSelected);
    }
}
