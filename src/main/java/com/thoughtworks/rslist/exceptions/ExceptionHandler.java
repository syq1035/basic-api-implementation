package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({IndexOutOfBoundsException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<CommentError> handleIndexOutOfBoundsException(Exception ex) {
        CommentError commentError = new CommentError();
        if(ex instanceof MethodArgumentNotValidException) {
            commentError.setError("invalid param");
            return ResponseEntity.badRequest().body(commentError);
        }
        commentError.setError("invalid index");
        if(ex.getMessage() != null) {
            commentError.setError(ex.getMessage());
        }
        return ResponseEntity.badRequest().body(commentError);
    }
}
