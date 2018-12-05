package com.kaihei.esportingplus.auth.server.controller;

import com.kaihei.esportingplus.common.constant.SecurityConstants;
import io.swagger.annotations.Api;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@Api(tags = {"用户信息"})
public class UserController {

    @GetMapping("/auth/user")
    public Principal user(Principal principal) {
        return principal;
    }

}