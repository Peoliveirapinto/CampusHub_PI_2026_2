package org.example.backend.mapper;

import org.example.backend.dto.perfil.PerfilCriarDTO;
import org.example.backend.dto.perfil.PerfilCriarMeuDTO;
import org.example.backend.dto.perfil.PerfilPatchDTO;
import org.example.backend.dto.perfil.PerfilRespostaDTO;
import org.example.backend.model.Perfil;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface PerfilMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)           // Atualização de id não é permitida
    @Mapping(target = "usuario", ignore = true)      // Atualização de usuario não é permitida
    void atualizaEntidadePeloDTO(PerfilPatchDTO dto, @MappingTarget Perfil entity);

    Perfil criaEntidadePeloDTO(PerfilCriarMeuDTO perfilCriarMeuDTO);
    Perfil criaEntidadePeloDTO(PerfilCriarDTO perfilCriarDTO);

    PerfilRespostaDTO criaPerfilRespostaDTOPelaEntidade(Perfil perfil);
}
