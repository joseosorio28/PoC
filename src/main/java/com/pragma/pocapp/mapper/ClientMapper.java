package com.pragma.pocapp.mapper;

import com.pragma.pocapp.dto.ClientImageDto;
import com.pragma.pocapp.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Use maybe beanutils.copyproperties

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "clientId", source = "dto.clientId")
    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "lastName", source = "dto.lastName")
    @Mapping(target = "idType", source = "dto.idType")
    @Mapping(target = "idNumber", source = "dto.idNumber")
    @Mapping(target = "age", source = "dto.age")
    @Mapping(target = "cityOfBirth", source = "dto.cityOfBirth")
    Client toClient(ClientImageDto dto);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "imageB64", source = "dto.imageB64")
    @Mapping(target = "idType", source = "dto.idType")
    @Mapping(target = "idNumber", source = "dto.idNumber")
    Image toImage(ClientImageDto dto);

    @Mapping(target = "clientId", source = "client.clientId")
    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
    @Mapping(target = "idType", source = "client.idType")
    @Mapping(target = "idNumber", source = "client.idNumber")
    @Mapping(target = "age", source = "client.age")
    @Mapping(target = "cityOfBirth", source = "client.cityOfBirth")
    @Mapping(target = "id", source = "image.id")
    @Mapping(target = "imageB64", source = "image.imageB64")
    ClientImageDto toDto(Client client, Image image);

    default List<ClientImageDto> toDtos(List<Client> clients, List<Image> images) {
        if (clients == null || images == null) {
            return Collections.emptyList();
        }

        List<ClientImageDto> list = new ArrayList<>(clients.size());
        for (Client client : clients) {
            Image imageFound = images.stream()
                    .filter(image -> image.getIdType().equals(client.getIdType()) && image.getIdNumber().equals(client.getIdNumber()))
                    .findFirst()
                    .orElseGet(() -> new Image("Image not found in DB","",client.getIdType(),client.getIdNumber()));
            list.add(toDto(client, imageFound));
        }
        return list;
    }
}
