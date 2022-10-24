package com.lyna.sqlite_poc.asm_poc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ASMTest {

    @Autowired
    private ResourceLoader resourceLoader = null;

    @Test
    void should_load_resource() throws IOException {
        ASM asm = new ASM(resourceLoader);
        File file = asm.loadResource();
        assertNotNull(file);
        asm.method();
    }

}