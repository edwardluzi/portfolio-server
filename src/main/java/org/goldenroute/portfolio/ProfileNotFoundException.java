package org.goldenroute.portfolio;

public class ProfileNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 3791647812815506692L;

    public ProfileNotFoundException(Long pid)
    {
        super("Could not find profile '" + pid.toString() + "'.");
    }
}