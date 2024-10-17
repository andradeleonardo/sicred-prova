package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.model.Pauta;
import br.com.sicred.prova.model.Secao;
import br.com.sicred.prova.utils.AppUtils;
import br.com.sicred.prova.utils.Constants;
import br.com.sicred.prova.v1.controller.request.SecaoRequest;
import br.com.sicred.prova.v1.controller.response.SecaoResponse;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {AppUtils.class, UUID.class, Constants.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SecaoMapper {

    SecaoResponse mapToResponse(Secao entity);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(AppUtils.getCurrentDatetime())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    Secao mapToEntityInsert(SecaoRequest dto, Pauta pauta);

    @AfterMapping
    default void afterMapToEntityInsert(SecaoRequest dto,
                                        Pauta pauta,
                                        @MappingTarget Secao entity) {
        entity.setPauta(pauta);
        if (ObjectUtils.isEmpty(dto.getDuracaoEmMinutos())) {
            entity.setDataExpiracao(LocalDateTime.now().plusMinutes(Constants.VOTING_SECTION_DEFAULT_TTL));
        } else {
            entity.setDataExpiracao(LocalDateTime.now().plusMinutes(dto.getDuracaoEmMinutos()));
        }
    }

}
