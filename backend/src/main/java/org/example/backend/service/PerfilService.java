package org.example.backend.service;

import org.example.backend.repository.PerfilRepository;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository){
        this.perfilRepository = perfilRepository;
    }
}
