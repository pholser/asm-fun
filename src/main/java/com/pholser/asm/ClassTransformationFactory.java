package com.pholser.asm;

import org.objectweb.asm.ClassVisitor;

public interface ClassTransformationFactory {
    ClassVisitor makeTransformation(ClassVisitor delegate);
}
