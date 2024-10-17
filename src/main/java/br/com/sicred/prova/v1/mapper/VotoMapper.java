package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.model.Associado;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.model.Voto;
import br.com.sicred.prova.utils.AppUtils;
import br.com.sicred.prova.v1.controller.request.VotoRequest;
import br.com.sicred.prova.v1.controller.response.VotoResponse;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = {AppUtils.class, UUID.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VotoMapper {

    @Mapping(target = "idSecao", source = "secao.id")
    @Mapping(target = "cpf", source = "associado.cpf")
    VotoResponse mapToResponse(Voto entity);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(AppUtils.getCurrentDatetime())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    Voto mapToEntityInsert(VotoRequest dto, Secao secao, Associado associado);

    @AfterMapping
    default void afterMapToEntityInsert(VotoRequest dto,
                                        Secao secao,
                                        Associado associado,
                                        @MappingTarget Voto entity) {
        entity.setSecao(secao);
        entity.setAssociado(associado);
    }

}
