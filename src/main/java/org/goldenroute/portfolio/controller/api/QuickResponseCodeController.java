package org.goldenroute.portfolio.controller.api;

import org.goldenroute.Wrapper;
import org.goldenroute.portfolio.UserNotFoundException;
import org.goldenroute.portfolio.model.Account;
import org.goldenroute.portfolio.model.Binding;
import org.goldenroute.portfolio.service.AccountService;
import org.goldenroute.portfolio.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.wechat.api.ActionInfo;
import org.springframework.social.wechat.api.QuickResponseCodeRequest;
import org.springframework.social.wechat.api.QuickResponseCodeTicket;
import org.springframework.social.wechat.api.Scene;
import org.springframework.social.wechat.api.Wechat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/v1/accounts/{uid}/qrcode")
public class QuickResponseCodeController
{
    private final AccountService accountService;
    private final ProfileService profileService;
    private final Wrapper<Wechat> wechatWrapper;

    @RequestMapping(value = "/wechat", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public QuickResponseCodeTicket create(@PathVariable Long uid)
    {
        Account account = accountService.findOne(uid);

        if (account == null)
        {
            throw new UserNotFoundException(uid);
        }

        Wechat wechat = wechatWrapper.get();

        if (wechat == null)
        {
            throw new RestClientException("Failed to connect to Wechat server.");
        }

        Binding binding = profileService.createBinding(account.getProfile());

        QuickResponseCodeRequest request = new QuickResponseCodeRequest();

        request.setExpireSeconds((int) binding.getExpireSeconds());
        request.setActionName("QR_SCENE");
        request.setActionInfo(new ActionInfo(new Scene(binding.getParameter())));

        return wechat.accountOperations().createQuickResponseCode(request);
    }

    @Autowired
    QuickResponseCodeController(AccountService accountService, ProfileService profileService,
            Wrapper<Wechat> wechatWrapper)
    {
        this.accountService = accountService;
        this.profileService = profileService;
        this.wechatWrapper = wechatWrapper;
    }
}
