package org.example.backend.mapper;

import org.example.backend.dto.UsuarioPatchDTO;
import org.example.backend.model.Usuario;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", ignore = true)
    void atualizaEntidadePeloDTO(UsuarioPatchDTO dto, @MappingTarget Usuario entity);
}
