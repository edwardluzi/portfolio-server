package org.goldenroute.portfolio.controller.api;

import org.goldenroute.portfolio.ProfileNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProfileControllerAdvice
{
    @ResponseBody
    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    VndErrors profileNotFoundExceptionHandler(ProfileNotFoundException ex)
    {
        return new VndErrors("error", ex.getMessage());
    }
}
