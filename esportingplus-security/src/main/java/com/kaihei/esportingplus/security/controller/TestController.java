package com.kaihei.esportingplus.security.controller;

import com.kaihei.esportingplus.security.aspect.TestAli;
import com.kaihei.esportingplus.trade.api.params.OrderQueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class TestController {

    @Autowired
    private TestAli testAli;

    @RequestMapping(value = "/test/testAli",method = RequestMethod.POST)
    public void test(@RequestBody OrderQueryParams params) {
        testAli.test(params);
    }
}