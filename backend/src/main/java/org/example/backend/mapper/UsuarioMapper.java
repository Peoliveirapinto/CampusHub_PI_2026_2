package org.example.backend.mapper;

import org.example.backend.dto.usuario.UsuarioPatchDTO;
import org.example.backend.dto.usuario.UsuarioRespostaDTO;
import org.example.backend.model.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)      // Atualização de id não é permitida
    @Mapping(target = "email", ignore = true)   // Atualização de email requer validação adicional co consulta ao banco de dados
    @Mapping(target = "senha", ignore = true)   // Atualização de senha é cuidada separadamente
    @Mapping(target = "role", ignore = true)    // Atualização de role não é permitida (no futuro seria cuidada separadamente)
    void updateFromDTO(UsuarioPatchDTO dto, @MappingTarget Usuario entity);

    UsuarioRespostaDTO toUsuarioRespostaDTO(Usuario usuario);
}
