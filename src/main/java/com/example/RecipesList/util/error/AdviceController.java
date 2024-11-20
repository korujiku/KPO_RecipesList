package com.example.RecipesList.util.error;

import com.example.RecipesList.recipe.exception.RecipeNotFoundException;
import com.example.RecipesList.recipe.exception.UserNotFoundException;
import com.example.RecipesList.util.validation.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

public class AdviceController {
    @ExceptionHandler({
            UserNotFoundException.class,
            RecipeNotFoundException.class,
            ValidationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Object> handleException(Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBindException(MethodArgumentNotValidException e) {
        final ValidationException validationException = new ValidationException(
                e.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toSet()));
        return handleException(validationException);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Throwable e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
