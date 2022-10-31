package com.lyna.sqlite_poc.asm_poc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.*;
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
    private File jarFile;
    private static final String SEPARATOR = System.getProperty("file.separator");

    public void loadResources(String artifactName) throws IOException {
        var artifactPath = getArtifactPath(artifactName);
        var pomModel = getPomModel(artifactPath);
        jarFile = new File(artifactPath + SEPARATOR + "target" + SEPARATOR + artifactName + "-" + pomModel.version() + ".jar");
        ReadJavaFiles readJavaFiles = new ReadJavaFiles();
        var mainPath = artifactPath + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "java";
        var filesList = readJavaFiles.listJavaFilesPaths(mainPath);
        filesList.forEach(System.out::println);
    }

    private PomModel getPomModel(String artifactPath) throws IOException {
        File file = new File(artifactPath + SEPARATOR + "pom.xml");
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return xmlMapper.readValue(file, PomModel.class);
    }

    public String getArtifactPath(String artifactName) {
        String userDirectory = System.getProperty("user.dir");
        File file = new File(userDirectory).getParentFile();
        return file.getPath() + SEPARATOR + "artifacts" + SEPARATOR + artifactName;
    }

    public void readByteCode() throws IOException {

        var finder = ModuleFinder.of(Path.of(jarFile.getAbsolutePath()));
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

                                @Override
                                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                                    System.err.println("    opcode " + opcode + " " + owner + "." + name + descriptor);
                                }

                                @Override
                                public void visitLdcInsn(Object value) {
                                    System.err.println("    ldc " + value);
                                }

                                @Override
                                public void visitIntInsn(int opcode, int operand) {
                                    System.err.println("    int " + opcode + " " + operand);
                                }

                                @Override
                                public void visitVarInsn(int opcode, int var) {
                                    System.err.println("    var " + opcode + " " + var);
                                }

                                @Override
                                public void visitTypeInsn(int opcode, String type) {
                                    System.err.println("    type " + opcode + " " + type);
                                }

                                // + the other visit methods to get all the opcodes
                            };
                        }

                        @Override
                        public void visitEnd() {
                            System.err.println("end");
                        }

                        @Override
                        public void visitInnerClass(String name, String outerName, String innerName, int access) {
                            System.err.println("inner " + name + " " + outerName + " " + innerName + " " + access);
                        }

                        @Override
                        public void visitOuterClass(String owner, String name, String descriptor) {
                            System.err.println("outer " + owner + " " + name + " " + descriptor);
                        }

                        @Override
                        public void visitNestHost(String nestHost) {
                            System.err.println("nest " + nestHost);
                        }

                        @Override
                        public void visitNestMember(String nestMember) {
                            System.err.println("nest member " + nestMember);
                        }

                        @Override
                        public void visitPermittedSubclass(String permittedSubclass) {
                            System.err.println("permitted " + permittedSubclass);
                        }


                    }, 0);
                }
            }
        }
    }

    public File getJarFile() {
        return jarFile;
    }
}
