package org.goldenroute.portfolio.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.goldenroute.portfolio.ArrayUtils;
import org.goldenroute.portfolio.InvalidParameterException;
import org.goldenroute.portfolio.PortfolioNotFoundException;
import org.goldenroute.portfolio.model.Portfolio;
import org.goldenroute.portfolio.model.Transaction;
import org.goldenroute.portfolio.service.PortfolioService;
import org.goldenroute.portfolio.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/portfolios/{pid}/transactions")
public class TransactionController
{
    private final PortfolioService portfolioService;
    private final TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio create(@PathVariable Long pid, @RequestBody Transaction transaction)
    {
        Portfolio portfolio = this.portfolioService.findOne(pid, false);

        if (portfolio != null)
        {
            List<Transaction> transactions = portfolio.getTransactions();

            if (transactions == null)
            {
                transactions = new ArrayList<Transaction>();
                portfolio.setTransactions(transactions);
            }

            transactions.add(transaction);
            transaction.setOwner(portfolio);

            this.transactionService.save(transaction);

            return this.portfolioService.findOne(pid, true);
        }
        else
        {
            throw new PortfolioNotFoundException(pid);
        }
    }

    @RequestMapping(value = "/{tids}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Portfolio delete(@PathVariable Long pid, @PathVariable String[] tids)
    {
        Long[] ltids = null;

        try
        {
            ltids = ArrayUtils.toLong(tids);
        }
        catch (Exception e)
        {
            throw new InvalidParameterException("tids", e.getMessage());
        }

        Portfolio portfolio = this.portfolioService.findOne(pid, false);

        if (portfolio != null)
        {
            for (Long tid : ltids)
            {
                portfolio.remove(tid);
            }

            this.portfolioService.save(portfolio);

            return this.portfolioService.findOne(pid, true);
        }
        else
        {
            throw new PortfolioNotFoundException(pid);
        }
    }

    @RequestMapping(value = "/{tid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Portfolio update(@PathVariable Long pid, @PathVariable Long tid, @RequestBody Transaction transaction)
    {
        Portfolio portfolio = this.portfolioService.findOne(pid, false);

        if (portfolio != null)
        {
            Transaction exists = portfolio.find(tid);

            if (exists != null)
            {
                exists.setTimestamp(transaction.getTimestamp());
                exists.setTicker(transaction.getTicker());
                exists.setType(transaction.getType());
                exists.setPrice(transaction.getPrice());
                exists.setAmount(transaction.getAmount());
                exists.setCommission(transaction.getCommission());
                exists.setOtherCharges(transaction.getOtherCharges());

                this.transactionService.save(exists);

                return this.portfolioService.findOne(pid, true);
            }
            else
            {
                throw new PortfolioNotFoundException(pid);
            }
        }
        else
        {
            throw new PortfolioNotFoundException(pid);
        }
    }

    @Autowired
    TransactionController(PortfolioService portfolioService, TransactionService transactionService)
    {
        this.portfolioService = portfolioService;
        this.transactionService = transactionService;
    }
}
