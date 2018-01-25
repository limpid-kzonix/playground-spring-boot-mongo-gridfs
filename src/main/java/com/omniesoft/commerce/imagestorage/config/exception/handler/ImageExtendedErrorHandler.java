package com.omniesoft.commerce.imagestorage.config.exception.handler;

import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.omniesoft.commerce.common.handler.exception.custom.enums.ImageModuleErrorCodes;
import com.omniesoft.commerce.common.handler.exception.handler.RestResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ImageExtendedErrorHandler extends RestResponseEntityExceptionHandler {

    /**
     * Handle RuntimeException. Triggered when throw .
     *
     * @param ex      RuntimeException ({IllegalArgumentException.class, IllegalStateException.class})
     * @param request WebRequest
     * @return the ApiError object
     */
    @ExceptionHandler(value = {MongoTimeoutException.class, MongoException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {

        log.error(this.getClass().getSimpleName(), ex);
        String bodyOfResponse = "Image service unavailable";
        return getObjectResponseEntity(ex, HttpStatus.SERVICE_UNAVAILABLE, bodyOfResponse, ImageModuleErrorCodes.SERVICE_UNAVAILABLE);
    }

}
