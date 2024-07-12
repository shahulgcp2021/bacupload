package com.bacuti.service.mapper;

import com.bacuti.domain.AbstractAuditingEntity;
import com.bacuti.service.dto.AbstractAuditingDTO;
import org.mapstruct.Mapper;


@Mapper
    public interface AbstractAuditingEntityMapper {
        AbstractAuditingDTO AbstractAuditingEntityToDTO(AbstractAuditingEntity entity);
    }

