package org.goldenroute.portfolio;

public class PortfolioNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 3581131820947605871L;

    public PortfolioNotFoundException(Long portfolioId)
    {
        super("Could not find portfolio '" + portfolioId.toString() + "'.");
    }

    public PortfolioNotFoundException(String portfolio)
    {
        super("Could not find portfolio '" + portfolio + "'.");
    }
}
