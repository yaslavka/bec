package matrixbackend.exception.helper;

import matrixbackend.exception.BaseRuntimeException;
import matrixbackend.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<ErrorResponse> generalHandler(BaseRuntimeException exception) {
        HttpStatus status = exception.getStatus();

        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage());

        return new ResponseEntity<>(error, status);
    }

}
