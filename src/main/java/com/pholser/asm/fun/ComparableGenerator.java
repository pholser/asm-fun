package com.pholser.asm.fun;

import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.*;

public class ComparableGenerator {
    public static void main(String[] args) {
        ClassWriter writer = new ClassWriter(0);

        writer.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "com/pholser/asm/fun/Comparable", null,
            "java/lang/Object", new String[] { "com/pholser/asm/fun/Mesurable" });
        writer.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, new Integer(-1)).visitEnd();
        writer.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, new Integer(0)).visitEnd();
        writer.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, new Integer(1)).visitEnd();
        writer.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I", null, null).visitEnd();
        writer.visitEnd();

        byte[] b = writer.toByteArray();
    }
}
