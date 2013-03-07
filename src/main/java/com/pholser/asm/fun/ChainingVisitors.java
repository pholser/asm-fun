package com.pholser.asm.fun;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.ClassWriter.*;
import static org.objectweb.asm.Opcodes.*;

public class ChainingVisitors {
    public static void main(String[] args) throws Exception {
        ClassReader reader = new ClassReader("com.foo.Bar");
        ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
        ClassVisitor v3 = new Link(3, writer);
        ClassVisitor v2 = new Link(2, v3);
        ClassVisitor v1 = new Link(1, v2);
        reader.accept(v1, 0);
        System.out.printf("how many bytes in %s? %d\n", Runnable.class, writer.toByteArray().length);
    }

    private static class Link extends ClassVisitor {
        private final int order;

        Link(int order, ClassVisitor next) {
            super(ASM4, next);
            this.order = order;
        }

        @Override public void visit(int i, int i2, String s, String s2, String s3, String[] strings) {
            System.out.printf("In visitor %d\n", order);
            super.visit(i, i2, s, s2, s3, strings);
        }
    }
}
