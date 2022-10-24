package com.lyna.sqlite_poc.infrastructure;

import com.lyna.sqlite_poc.infrastructure.repository.DemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDemos {

    private final DemoRepository demoRepository;

    public String execute() {
        return demoRepository.findAll().toString();
    }
}
