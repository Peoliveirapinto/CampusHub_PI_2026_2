package org.example.backend.exception;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ResponseMessageErrorDTO;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    private final Locale locale = Locale.getDefault();

    private ResponseEntity<ResponseMessageErrorDTO> construirResposta(HttpStatus httpStatus, String mensagem, Object[] args){
        String translatedMessage = messageSource.getMessage(
                mensagem,
                args,
                mensagem,
                this.locale
        );

        ResponseMessageErrorDTO corpoDaResposta = new ResponseMessageErrorDTO(
                LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                translatedMessage
        );

        return ResponseEntity.status(httpStatus).body(corpoDaResposta);
    }

    private ResponseEntity<ResponseMessageErrorDTO> construirResposta(HttpStatus httpStatus, String mensagem){
        return this.construirResposta(httpStatus, mensagem, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleResourceNotFound(ResourceNotFoundException ex){
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getArgs());
    }

    @ExceptionHandler(JaExisteException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleJaExiste(JaExisteException ex){
        return construirResposta(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleCredenciaisInvalidas(CredenciaisInvalidasException ex){
        return construirResposta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoAutenticadoException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleUsuarioNaoAutenticado(UsuarioNaoAutenticadoException ex){
        return construirResposta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(TokenPayloadInvalidoException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleTokenPayloadInvalido(TokenPayloadInvalidoException ex){
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleBadRequest(BadRequestException ex){
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessageErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        ArrayList<String> mensagensDeErro = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                mensagensDeErro.add(error.getDefaultMessage())
        );

        return construirResposta(HttpStatus.BAD_REQUEST, mensagensDeErro.toString());
    }
}
