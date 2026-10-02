package org.example.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.backend.service.SenhaService;
import org.springframework.beans.factory.annotation.Autowired;

public class SenhaValidaValidator implements ConstraintValidator<SenhaValida, String> {
    @Autowired
    private SenhaService senhaService;

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext context){
        if(senha == null || senha.isBlank()){
            return true; // Retorna true para que o @NotBlank do DTO cuide da mensagem de obrigatoriedade sem conflito
        }

        return senhaService.ehValida(senha);
    }
}
