package org.goldenroute.portfolio.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.goldenroute.portfolio.wechat.EventDispatcher;
import org.goldenroute.portfolio.wechat.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.wechat.api.EventMessage;
import org.springframework.social.wechat.api.Message;
import org.springframework.social.wechat.api.MessageFactory;
import org.springframework.social.wechat.api.MessageType;
import org.springframework.social.wechat.api.Signature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechat")
public class WechatController
{
    private static final Logger logger = Logger.getLogger(WechatController.class);

    @Value("${social.wechat.Token}")
    private String wechatToken;

    @Autowired
    private EventDispatcher eventDispatcher;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String validate(@RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr)
    {

        String response = "";

        if (Signature.check(signature, wechatToken, timestamp, nonce))
        {
            if (echostr != null && !echostr.equals(""))
            {
                response = echostr;
            }
        }
        else
        {
            response = "Failed to validate request!";
            logger.info(response);
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String process(HttpServletRequest request)
    {
        String response = "";

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        if (!Signature.check(signature, wechatToken, timestamp, nonce))
        {
            response = "Failed to validate request!";
            logger.info(response);
        }
        else
        {
            try
            {
                Message message = MessageFactory.create(request);

                if (message.getType() == MessageType.event)
                {
                    response = eventDispatcher.process((EventMessage) message);
                }
                else
                {
                    response = messageDispatcher.process(message);
                }
            }
            catch (Exception e)
            {
                logger.error(e, e);
            }
        }

        return response;
    }
}
