package br.com.sicred.prova.v1.mapper;

import br.com.sicred.prova.enums.ValorVotoEnum;
import br.com.sicred.prova.v1.controller.response.ResultadoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResultadoMapper {

    default List<ResultadoResponse> montaResultadoFinal(List<ResultadoResponse> source) {
        final ResultadoResponse sim = new ResultadoResponse(ValorVotoEnum.SIM, source.stream().filter(el ->
                ValorVotoEnum.SIM.equals(el.getValor())).mapToLong(
                        ResultadoResponse::getQuantidade).findFirst().orElse(0));
        final ResultadoResponse nao = new ResultadoResponse(ValorVotoEnum.NAO, source.stream().filter(el ->
                ValorVotoEnum.NAO.equals(el.getValor())).mapToLong(
                        ResultadoResponse::getQuantidade).findFirst().orElse(0));
        final List<ResultadoResponse> resp = List.of(sim, nao);
        final long totalVotos = resp.stream().mapToLong(ResultadoResponse::getQuantidade).sum();
        if (totalVotos > 0) {
            resp.forEach(el -> el.setPercentual((double) ((el.getQuantidade() * 100) / totalVotos)));
        }
        return resp;
    }

}
