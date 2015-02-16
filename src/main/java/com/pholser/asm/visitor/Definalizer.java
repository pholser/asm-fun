package com.pholser.asm.visitor;

import com.pholser.asm.ClassTransformationFactory;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class Definalizer extends ClassVisitor {
    private Definalizer(ClassVisitor delegate) {
        super(ASM4, delegate);
    }

    @Override public void visit(
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces) {

        super.visit(
                version,
                access & ~ACC_FINAL,
                name,
                signature,
                superName,
                interfaces);
    }

    @Override public MethodVisitor visitMethod(
            int access,
            String name,
            String descriptor,
            String signature,
            String[] exceptions) {

        return super.visitMethod(
                access & ~ACC_FINAL,
                name,
                descriptor,
                signature,
                exceptions);
    }

    public static ClassTransformationFactory factory() {
        return Definalizer::new;
    }
}
