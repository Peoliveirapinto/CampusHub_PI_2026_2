package org.example.backend.mapper;

import org.example.backend.dto.PerfilPatchDTO;
import org.example.backend.model.Perfil;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PerfilMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizaEntidadePeloDTO(PerfilPatchDTO dto, @MappingTarget Perfil entity);
}
