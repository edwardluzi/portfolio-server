package org.goldenroute.portfolio.controller.api;

import org.goldenroute.portfolio.ProfileNotFoundException;
import org.goldenroute.portfolio.model.Profile;
import org.goldenroute.portfolio.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController
{
    private final ProfileService profileService;

    @RequestMapping(value = "{pid}/bind", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public boolean bind(@PathVariable Long pid, @RequestBody Integer parameter)
    {
        Profile profile = profileService.findOne(pid);

        if (profile != null)
        {
            profileService.bindWechat(profile, parameter);

            return true;
        }
        else
        {
            throw new ProfileNotFoundException(pid);
        }
    }

    @Autowired
    ProfileController(ProfileService profileService)
    {
        this.profileService = profileService;
    }
}
