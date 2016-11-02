package org.goldenroute.portfolio.controller.api;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class QuickResponseCodeControllerAdvice
{
    @ResponseBody
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    VndErrors restClientExceptionHandler(RestClientException ex)
    {
        return new VndErrors("error", ex.getMessage());
    }
}
