package org.example.backend.mapper;

import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.model.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", ignore = true)  // Atualização de email requer validação adicional co consulta ao banco de dados
    @Mapping(target = "role", ignore = true)   // Atualização de role não é permitida
    void atualizaEntidadePeloDTO(UsuarioPatchDTO dto, @MappingTarget Usuario entity);

    UsuarioRespostaDTO criaUsuarioRespostaDTOPelaEntidade(Usuario usuario);
}
