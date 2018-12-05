package com.kaihei.esportingplus.payment.controller;

import com.kaihei.esportingplus.payment.migrate.MigrateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: tangtao
 **/
@ConditionalOnProperty(name = "migrate.enable", havingValue = "true")
@RestController
@Api(tags = {"迁移相关API"})
@RequestMapping("/migrate")
public class MigrateController {

    @Autowired
    private MigrateService migrateService;

    @GetMapping("/start")
    public String start(@RequestParam("types") String types, @RequestParam(name = "threadSize", required = false) Integer threadSize,
            @RequestParam(name = "maxTaskSize", required = false) Integer maxTaskSize,
            @RequestParam(name = "maxPageSize", required = false) Integer maxPageSize) {
        migrateService.migrate(types, threadSize, maxTaskSize, maxPageSize);
        return "running";
    }
}
