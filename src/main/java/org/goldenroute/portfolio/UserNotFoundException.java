package org.goldenroute.portfolio;

public class UserNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 8751703587073903364L;

    public UserNotFoundException(Long uid)
    {
        super("Could not find user '" + uid.toString() + "'.");
    }

    public UserNotFoundException(String username)
    {
        super("Could not find user '" + username + "'.");
    }
}