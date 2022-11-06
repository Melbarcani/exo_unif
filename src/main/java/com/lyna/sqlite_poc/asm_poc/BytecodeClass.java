package com.lyna.sqlite_poc.asm_poc;

import lombok.Data;

@Data
public class BytecodeClass {
    private String name;
    private String access;
    private String superName;
    private String[] interfaces;
    private String[] fields;
    private BytecodeMethod bytecodeMethod;
}
