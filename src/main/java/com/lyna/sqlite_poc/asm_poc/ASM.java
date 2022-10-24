package com.lyna.sqlite_poc.asm_poc;

import lombok.RequiredArgsConstructor;
import org.objectweb.asm.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ASM {

    private final ResourceLoader resourceLoader;

    public File loadResource() throws IOException {
        return resourceLoader.getResource("classpath:jars/jar-1.jar").getFile();
    }

    public void method() throws IOException {
        var jarOneFile = loadResource();


        var finder = ModuleFinder.of(Path.of(jarOneFile.getAbsolutePath()));
        var moduleReference = finder.findAll().stream().findFirst().orElseThrow();
        try (var reader = moduleReference.open()) {
            for (var filename : (Iterable<String>) reader.list()::iterator) {
                if (!filename.endsWith(".class")) {
                    continue;
                }
                try (var inputStream = reader.open(filename).orElseThrow()) {
                    var classReader = new ClassReader(inputStream);
                    classReader.accept(new ClassVisitor(Opcodes.ASM9) {

                        private static String modifier(int access) {
                            if (Modifier.isPublic(access)) {
                                return "public";
                            }
                            if (Modifier.isPrivate(access)) {
                                return "private";
                            }
                            if (Modifier.isProtected(access)) {
                                return "protected";
                            }
                            return "";
                        }

                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            System.err.println("class " + modifier(access) + " " + name + " " + superName + " " + (interfaces != null ? Arrays.toString(interfaces) : ""));
                        }

                        @Override
                        public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                            System.err.println("  component " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName());
                            return null;
                        }

                        @Override
                        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                            System.err.println("  field " + modifier(access) + " " + name + " " + ClassDesc.ofDescriptor(descriptor).displayName() + " " + signature);
                            return null;
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            System.err.println("  method " + modifier(access) + " " + name + " " + MethodTypeDesc.ofDescriptor(descriptor).displayDescriptor() + " " + signature);
                            return new MethodVisitor(Opcodes.ASM9) {
                                @Override
                                public void visitInsn(int opcode) {
                                    System.err.println("    opcode " + opcode);
                                }

                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    System.err.println("    opcode " + opcode + " " + owner + "." + name + descriptor);
                                }

                                // + the other visit methods to get all the opcodes
                            };
                        }
                    }, 0);
                }
            }
        }
    }
}
