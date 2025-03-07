package com.ptit.asset.dto.data;

import io.vavr.control.Try;
import org.springframework.data.domain.Page;

import java.util.List;

public class RestResponseUtils {

    public static <T> GenericBaseResponse<?> create(Try<T> result,
                                                    String successStatus, Integer successCode,
                                                    String failureStatus, Integer failureCode){
        if (result instanceof Try.Success){ // Success
            GenericBaseResponse.Meta meta = GenericBaseResponse.Meta.builder()
                .status(successStatus).code(successCode).message("Success")
                .build();
            return new GenericBaseResponse<>(result.get(),meta);
        }
        else { // Failure
            GenericBaseResponse.Meta meta = GenericBaseResponse.Meta.builder()
                .status(failureStatus).code(failureCode).message(result.getCause().getMessage())
                .build();
            return new GenericBaseResponse<>(new EmptyDataObject(), meta);
        }
    }

    public static <T> GenericBaseResponse<?> create(Page<T> result, String successStatus, Integer successCode){
        GenericBaseResponse.Meta meta = GenericBaseResponse.Meta.builder()
                .status(successStatus).code(successCode).message("success").build();
        return new GenericBaseResponse<>(result.get(),meta);
    }

    public static <T> GenericBaseResponse<?> create(List<T> result, String successStatus, Integer successCode){
        GenericBaseResponse.Meta meta = GenericBaseResponse.Meta.builder()
                .status(successStatus).code(successCode).message("success").build();
        return new GenericBaseResponse<>(result,meta);
    }


}
