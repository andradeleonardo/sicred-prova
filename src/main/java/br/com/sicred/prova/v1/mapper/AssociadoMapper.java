package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.utils.AppUtils;
import br.com.sicred.prova.v1.controller.request.AssociadoRequest;
import br.com.sicred.prova.v1.controller.response.AssociadoResponse;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {AppUtils.class, UUID.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssociadoMapper {

    AssociadoResponse mapToResponse(Associado entity);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(AppUtils.getCurrentDatetime())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    Associado mapToEntityInsert(AssociadoRequest dto, Pauta pauta);

    @AfterMapping
    default void afterMapToEntityInsert(AssociadoRequest dto,
                                        Pauta pauta,
                                        @MappingTarget Associado entity) {
        entity.setPauta(pauta);
    }

}
