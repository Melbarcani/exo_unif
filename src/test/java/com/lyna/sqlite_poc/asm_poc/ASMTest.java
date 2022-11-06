package com.lyna.sqlite_poc.asm_poc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ASMTest {

    @Test
    void should_load_resource() throws IOException {
        ASM asm = new ASM();
        asm.loadResources("artifact1");
        File file = asm.getJarFile();
        assertNotNull(file);
        asm.readByteCode();
    }

    /*@Test
    void test_bytecode() throws IOException, ClassNotFoundException {
        ByteCodeConverter byteCodeConverter = new ByteCodeConverter();
        byteCodeConverter.loadResources("artifact1");
        byteCodeConverter.convertToByteCode();
    }*/

}