package com.pragma.pocapp.mapper;

import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mappings({
            @Mapping(target="clientId", source="dto.clientId"),
            @Mapping(target="firstName", source="dto.firstName"),
            @Mapping(target="lastName", source="lastName"),
            @Mapping(target="idType", source="idType"),
            @Mapping(target="idNumber", source="idNumber"),
            @Mapping(target="age", source="age"),
            @Mapping(target="image.imageB64", source="imageB64")
    })
    Client toEntity(ClientImageDto dto);

    @Mappings({
            @Mapping(target="clientId", source="entity.clientId"),
            @Mapping(target="firstName", source="firstName"),
            @Mapping(target="lastName", source="lastName"),
            @Mapping(target="idType", source="idType"),
            @Mapping(target="idNumber", source="idNumber"),
            @Mapping(target="age", source="age"),
            @Mapping(target="imageB64", source="image.imageB64")

    })
    ClientImageDto toDto(Client entity);

}
