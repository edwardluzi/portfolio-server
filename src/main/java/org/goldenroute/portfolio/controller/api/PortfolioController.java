package org.goldenroute.portfolio.controller.api;

import java.security.Principal;

import org.goldenroute.portfolio.ArrayUtils;
import org.goldenroute.portfolio.InvalidParameterException;
import org.goldenroute.portfolio.PortfolioNotFoundException;
import org.goldenroute.portfolio.UserNotFoundException;
import org.goldenroute.portfolio.model.Account;
import org.goldenroute.portfolio.model.Portfolio;
import org.goldenroute.portfolio.service.AccountService;
import org.goldenroute.portfolio.service.PortfolioReport;
import org.goldenroute.portfolio.service.PortfolioReportParameters;
import org.goldenroute.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController
{
    private final AccountService accountService;
    private final PortfolioService portfolioService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio create(@RequestBody Portfolio portfolio, Principal principal)
    {
        Account account = this.accountService.findByUsername(principal.getName());

        if (account != null)
        {
            account.addOrUpdate(portfolio);
            portfolio.setOwner(account);

            this.portfolioService.save(portfolio);

            return portfolio;
        }
        else
        {
            throw new UserNotFoundException(principal.getName());
        }
    }

    @RequestMapping(value = "/{pids}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Boolean delete(@PathVariable String[] pids)
    {
        Long[] lpids = null;

        try
        {
            lpids = ArrayUtils.toLong(pids);
        }
        catch (Exception e)
        {
            throw new InvalidParameterException("tids", e.getMessage());
        }

        if (lpids.length == 0)
        {
            throw new InvalidParameterException("tids", "Portfolio is required");
        }

        Portfolio portfolio = null;

        for (Long pid : lpids)
        {
            portfolio = this.portfolioService.findOne(pid, false);

            if (portfolio != null)
            {
                break;
            }
        }

        if (portfolio == null)
        {
            throw new PortfolioNotFoundException(lpids.toString());
        }

        Account account = portfolio.getOwner();

        for (Long pid : lpids)
        {
            account.remove(pid);
        }

        this.accountService.save(account);

        return true;
    }

    @RequestMapping(value = "/{pid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Portfolio update(@PathVariable Long pid, @RequestBody Portfolio portfolio)
    {
        Portfolio exists = this.portfolioService.findOne(pid, false);

        if (exists != null)
        {
            exists.setName(portfolio.getName());
            exists.setBenchmark(portfolio.getBenchmark());
            exists.setCurrency(portfolio.getCurrency());
            exists.setPrimaryAssetClass(portfolio.getPrimaryAssetClass());
            exists.setDescription(portfolio.getDescription());
            this.portfolioService.save(exists);

            return this.portfolioService.findOne(pid, false);
        }
        else
        {
            throw new PortfolioNotFoundException(pid);
        }
    }

    @RequestMapping(value = "/{pid}/analysis", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public PortfolioReport analysis(@PathVariable Long pid, @RequestBody PortfolioReportParameters parameters)
    {
        Portfolio exists = this.portfolioService.findOne(pid, true);

        if (exists != null)
        {
            return this.portfolioService.generateReport(exists, parameters);
        }
        else
        {
            throw new PortfolioNotFoundException(pid);
        }
    }

    @Autowired
    PortfolioController(AccountService accountService, PortfolioService portfolioService)
    {
        this.accountService = accountService;
        this.portfolioService = portfolioService;
    }
}
