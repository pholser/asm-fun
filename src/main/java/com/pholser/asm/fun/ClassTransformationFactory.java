package com.pholser.asm.fun;

import org.objectweb.asm.ClassVisitor;

public interface ClassTransformationFactory {
    ClassVisitor makeTransformation(ClassVisitor delegate);
}
