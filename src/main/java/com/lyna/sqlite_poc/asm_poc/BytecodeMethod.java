package com.lyna.sqlite_poc.asm_poc;

import lombok.Data;

import java.util.List;

@Data
public class BytecodeMethod {
    private String modifier;
    private String name;
    private String descriptor;
    private List<Integer> opcodes;
    private List<Integer> vars;

    private List<CodeBloc> blocs;
    private String owner;

    private Integer operand;

    public String toString() {
        return "BytecodeMethod{" +
                "modifier='" + modifier + '\'' +
                ", name='" + name + '\'' +
                ", descriptor='" + descriptor + '\'' +
                ", opcodes=" + opcodes +
                ", vars=" + vars +
                ", owner='" + owner + '\'' +
                ", operand=" + operand +
                '}';
    }
}
