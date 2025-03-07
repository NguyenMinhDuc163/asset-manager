//package com.ptit.asset.exception;
//
//import org.hibernate.TypeMismatchException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse("Server Error", details);
//        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(RecordNotFoundException.class)
//    public final ResponseEntity<Object> handleUserNotFoundException(RecordNotFoundException ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse("Record Not Found", details);
//        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
//    }
//
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
////        List<String> details = new ArrayList<>();
////        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
////            details.add(error.getDefaultMessage());
////        }
////        ErrorResponse error = new ErrorResponse("Validation Failed", details);
////        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
////    }
//
////    @ResponseStatus(HttpStatus.BAD_REQUEST)
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public Map<String, String> handleValidationExceptions(
////            MethodArgumentNotValidException ex) {
////        Map<String, String> errors = new HashMap<>();
////        ex.getBindingResult().getAllErrors().forEach((error) -> {
////            String fieldName = ((FieldError) error).getField();
////            String errorMessage = error.getDefaultMessage();
////            errors.put(fieldName, errorMessage);
////        });
////        return errors;
////    }
//
//
////    @ExceptionHandler(Exception.class)
////    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
////        List<String> details = new ArrayList<>();
////        details.add(ex.getLocalizedMessage());
////        ErrorResponse error = new ErrorResponse("Server Error", details);
////        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
////    }
//
////    @ExceptionHandler(RecordNotFoundException.class)
////    public final ResponseEntity<Object> handleUserNotFoundException(RecordNotFoundException ex, WebRequest request) {
////        List<String> details = new ArrayList<>();
////        details.add(ex.getLocalizedMessage());
////        ErrorResponse error = new ErrorResponse("Record Not Found", details);
////        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
////    }
//
////    @Override
////    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
////        List<String> details = new ArrayList<>();
////        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
////            details.add(error.getDefaultMessage());
////        }
////        ErrorResponse error = new ErrorResponse("Validation Failed", details);
////        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
////    }
//
//
//
////    @ExceptionHandler(TypeMismatchException::class)
////    fun handleTypeMismatchException(e: TypeMismatchException): HttpStatus {
////        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid value '${e.value}'", e)
////    }
////
////    @ExceptionHandler(WebExchangeBindException::class)
////    fun handleWebExchangeBindException(e: WebExchangeBindException): HttpStatus {
////        throw object : WebExchangeBindException(e.methodParameter!!, e.bindingResult) {
////            override val message = "${fieldError?.field} has invalid value '${fieldError?.rejectedValue}'"
////        }
////    }
//
//}
