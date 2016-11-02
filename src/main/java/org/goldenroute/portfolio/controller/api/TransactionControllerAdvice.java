package org.goldenroute.portfolio.controller.api;

import org.goldenroute.portfolio.InvalidParameterException;
import org.goldenroute.portfolio.PortfolioNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TransactionControllerAdvice
{
    @ResponseBody
    @ExceptionHandler(PortfolioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors portfolioNotFoundExceptionHandler(PortfolioNotFoundException ex)
    {
        return new VndErrors("error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors invalidParameterException(InvalidParameterException ex)
    {
        return new VndErrors("error", ex.getMessage());
    }
}
