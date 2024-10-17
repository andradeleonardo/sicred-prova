package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.utils.AppUtils;
import br.com.sicred.prova.v1.controller.request.PautaRequest;
import br.com.sicred.prova.v1.controller.response.PautaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {AppUtils.class, UUID.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PautaMapper {

    PautaResponse mapToResponse(Pauta entity);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(AppUtils.getCurrentDatetime())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    Pauta mapToEntityInsert(PautaRequest dto);

}
