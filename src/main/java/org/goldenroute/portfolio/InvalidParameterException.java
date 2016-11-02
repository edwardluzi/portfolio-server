package org.goldenroute.portfolio;

public class InvalidParameterException extends RuntimeException
{
    private static final long serialVersionUID = -763951025199960754L;

    public InvalidParameterException(String name, String message)
    {
        super("Value of parameter '" + name + "' is invalid, " + message);
    }
}
