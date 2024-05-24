package com.tmsproject.restaurantcollection.error;

import com.tmsproject.restaurantcollection.exception.DuplicateEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.function.BiFunction;

/**
 * Allows to choose the HTTP status code for the given error.
 */
@Slf4j // Аннотация для логгирования
@ControllerAdvice // Обработчик глобальных исключений
public class GlobalExceptionHandler {

    // Функция для формирования сообщения об ошибке по умолчанию
    private static final BiFunction<HttpServletRequest, Exception, String> defaultMessageBuilder = (request, ex) ->
            String.format("GlobalExceptionHandler: [%s] in [%s %s%s]", ex.getMessage(), request.getMethod(),
                    request.getRequestURI(), (request.getQueryString() == null ? "" : "?" + request.getQueryString()));

    // Обработчик для исключения, когда отсутствует параметр запроса
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    ErrorDescription handleMissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        log.warn(defaultMessageBuilder.apply(request, ex), ex); // Логгирование предупреждения
        // Возвращение объекта ErrorDescription с информацией об ошибке
        return ErrorDescription.builder()
                .code(ErrorCode.INVALID)
                .severity(ErrorSeverity.FATAL)
                .message("Required parameter [" + ex.getParameterName() + "] missed")
                .build();
    }

    // Обработчик для исключения, когда сущность не найдена
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    ErrorDescription handleErrorResponseException(HttpServletRequest request, EntityNotFoundException ex) {
        log.info(defaultMessageBuilder.apply(request, ex), ex); // Логгирование информации
        // Возвращение объекта ErrorDescription с информацией об ошибке
        return ErrorDescription.builder()
                .code(ErrorCode.EXCEPTION)
                .severity(ErrorSeverity.FATAL)
                .message(ex.getMessage())
                .build();
    }

    // Обработчик для исключения дублирования сущности
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseBody
    ErrorDescription handleErrorResponseException(HttpServletRequest request, DuplicateEntityException ex) {
        log.info(defaultMessageBuilder.apply(request, ex), ex); // Логгирование информации
        // Возвращение объекта ErrorDescription с информацией об ошибке
        return ErrorDescription.builder()
                .code(ErrorCode.DUPLICATE)
                .severity(ErrorSeverity.FATAL)
                .message(ex.getMessage())
                .build();
    }

    // Обработчик для исключения, когда невозможно прочитать сообщение HTTP
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ErrorDescription handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        log.warn(defaultMessageBuilder.apply(request, ex), ex); // Логгирование предупреждения
        // Возвращение объекта ErrorDescription с информацией об ошибке
        return ErrorDescription.builder()
                .code(ErrorCode.INVALID)
                .severity(ErrorSeverity.FATAL)
                .message(ex.getMessage())
                .build();
    }

    // Обработчик для исключения неподдерживаемой операции
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseBody
    ErrorDescription handleErrorResponseException(HttpServletRequest request, UnsupportedOperationException ex) {
        log.warn(defaultMessageBuilder.apply(request, ex), ex); // Логгирование предупреждения
        // Возвращение объекта ErrorDescription с информацией об ошибке
        return ErrorDescription.builder()
                .code(ErrorCode.NOT_SUPPORTED)
                .severity(ErrorSeverity.FATAL)
                .message(ex.getMessage())
                .build();
    }

    // Обработчик для неизвестных исключений
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorDescription handleUnknownException(HttpServletRequest request, Exception ex) {
        log.error(defaultMessageBuilder.apply(request, ex), ex); // Логгирование ошибки
        // Возвращение объекта ErrorDescription с информацией об ошибке
        return ErrorDescription.builder()
                .code(ErrorCode.EXCEPTION)
                .severity(ErrorSeverity.FATAL)
                .message(ex.getMessage())
                .build();
    }
}
