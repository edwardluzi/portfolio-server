package org.goldenroute.portfolio.controller.api;

import java.security.Principal;

import org.goldenroute.portfolio.model.Account;
import org.goldenroute.portfolio.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController
{
    private final AccountService accountService;

    @PostAuthorize("hasRole('ROLE_ADMIN') or hasPermission(returnObject, read)")
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Account read(@PathVariable Long uid, Principal principal)
    {
        if (uid == 0)
        {
            Account account = this.accountService.findByUsername(principal.getName());

            if (account != null)
            {
                return account;
            }
            else
            {
                return this.accountService.create(principal.getName());
            }
        }
        else
        {
            return this.accountService.findOne(uid);
        }
    }

    @Autowired
    AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }
}
