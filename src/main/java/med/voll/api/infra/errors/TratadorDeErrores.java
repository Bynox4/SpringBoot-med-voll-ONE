package med.voll.api.infra.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class TratadorDeErrores {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> tratarError404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> tratarError400(MethodArgumentNotValidException e){
        var errores = e.getFieldErrors().stream().map(DatosErrorValidadcion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    private record DatosErrorValidadcion(
        String campo,
        String error
    ) {
        public DatosErrorValidadcion(FieldError e){
            this(
                e.getField(),
                e.getDefaultMessage()
            );
        }
    }
}
