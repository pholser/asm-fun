package com.pholser.asm.fun;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.ClassWriter.*;

public class TransformingClassLoader extends ClassLoader {
    private final List<ClassTransformationFactory> transformationFactories =
        new ArrayList<ClassTransformationFactory>();

    public TransformingClassLoader(ClassLoader parent, ClassTransformationFactory... transformationFactories) {
        super(parent);

        Collections.addAll(this.transformationFactories, transformationFactories);
        Collections.reverse(this.transformationFactories);
    }

    @Override protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (name.startsWith("java.") || name.equals("com.pholser.asm.fun.TransformingClassLoader"))
            return getParent().loadClass(name);

        Class<?> c = findLoadedClass(name);
        if (c != null)
            return c;

        InputStream resourceIn = getResourceAsStream(name.replace('.', '/') + ".class");
        try {
            ClassReader reader = new ClassReader(resourceIn);
            ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES | COMPUTE_MAXS);

            ClassVisitor intermediary = writer;
            for (ClassTransformationFactory each : transformationFactories)
                intermediary = each.makeTransformation(intermediary);

            reader.accept(intermediary, 0);

            byte[] classBytes = writer.toByteArray();
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Problem finding class named " + name, e);
        }
    }
}
