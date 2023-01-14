package com.kyc.batchs.executive.management.mappers;

import com.kyc.batchs.executive.management.entity.KycUser;
import com.kyc.batchs.executive.management.model.ExecutiveRawData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

   /* @Mappings({
            @Mapping(target = "username", source = "source.username"),
            @Mapping(target = "secret", qualifiedByName = "getSecret"),
            @Mapping(target = "active", defaultValue = "true"),
            @Mapping(target = "locked", defaultValue = "false"),
            @Mapping(target = "dateCreation", source = "java(new java.util.Date())"),
            @Mapping(target = "userRelation", qualifiedByName = "getUserRelation")
    })*/
    KycUser toKycUser(ExecutiveRawData source);

}
