package com.lyna.sqlite_poc.infrastructure.controller;

import com.lyna.sqlite_poc.infrastructure.CreateDemo;
import com.lyna.sqlite_poc.infrastructure.DemoModel;
import com.lyna.sqlite_poc.infrastructure.GetDemos;
import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo")
public class DemoController {

    private final CreateDemo createDemo;
    private final GetDemos getDemos;

    @PostMapping()
    public String demo(@RequestBody String name) {
        DemoModel demoModel = new DemoModel();
        var random = Math.random();
        demoModel.setName(name + random);
        return createDemo.create(demoModel).get().getId().toString();
    }

    @GetMapping()
    public String getDemos() {
        return getDemos.execute();
    }
}
