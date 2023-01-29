package com.kyc.batch.executive.management.mappers;

import com.kyc.batch.executive.management.entity.KycExecutive;
import com.kyc.batch.executive.management.model.ExecutiveRawData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExecutiveMapper {

    @Mappings({
            @Mapping(target = "firstName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getFirstName()))"),
            @Mapping(target = "secondName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getSecondName()))"),
            @Mapping(target = "lastName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getLastName()))"),
            @Mapping(target = "secondLastName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getSecondLastName()))"),
            @Mapping(target = "rfc", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getRfc()))"),
            @Mapping(target = "phone", source = "source.phone"),
            @Mapping(target = "extension", source = "source.extension"),
            @Mapping(target = "email", source = "source.email"),
            @Mapping(target = "idBranch", source = "source.idBranch"),
            @Mapping(target = "status", constant = "true"),
            @Mapping(target = "creationDate", expression = "java(new java.util.Date())"),
            @Mapping(target = "batchOperationControl", expression =  "java(com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum.REGISTRATION.getOperation())")
    })
    KycExecutive toNewKycExecutive(ExecutiveRawData source);


    @Mappings({
            @Mapping(target = "firstName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getFirstName()))"),
            @Mapping(target = "secondName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getSecondName()))"),
            @Mapping(target = "lastName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getLastName()))"),
            @Mapping(target = "secondLastName", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getSecondLastName()))"),
            @Mapping(target = "rfc", expression = "java(org.apache.commons.lang3.StringUtils.upperCase(source.getRfc()))"),
            @Mapping(target = "phone", source = "source.phone"),
            @Mapping(target = "extension", source = "source.extension"),
            @Mapping(target = "email", source = "source.email"),
            @Mapping(target = "idBranch", source = "source.idBranch"),
            @Mapping(target = "updatedDate", expression = "java(new java.util.Date())"),
            @Mapping(target = "batchOperationControl", expression =  "java(com.kyc.batch.executive.management.enums.BatchExecutiveProcessEnum.MODIFICATION.getOperation())")

    })
    void updateKycExecutive(ExecutiveRawData source, @MappingTarget KycExecutive target);
}
