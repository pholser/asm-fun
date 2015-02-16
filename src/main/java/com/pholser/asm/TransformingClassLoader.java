package com.pholser.asm;

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
    private final List<ClassTransformationFactory> transformationFactories = new ArrayList<>();

    public TransformingClassLoader(
            ClassLoader parent,
            ClassTransformationFactory... transformationFactories) {

        super(parent);

        Collections.addAll(this.transformationFactories, transformationFactories);
        Collections.reverse(this.transformationFactories);
    }

    @Override protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            if (name.startsWith("java.") || name.equals(TransformingClassLoader.class.getName()))
                return getParent().loadClass(name);

            Class<?> c = findLoadedClass(name);
            if (c != null)
                return c;

            return transformedClass(name);
        }
    }

    private Class<?> transformedClass(String name) throws ClassNotFoundException {
        InputStream classIn = getResourceAsStream(name.replace('.', '/') + ".class");

        try {
            ClassReader reader = new ClassReader(classIn);
            ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES | COMPUTE_MAXS);

            ClassVisitor chain = writer;
            for (ClassTransformationFactory each : transformationFactories)
                chain = each.makeTransformation(chain);

            reader.accept(chain, 0);

            byte[] classBytes = writer.toByteArray();
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Problem finding class named " + name, e);
        }
    }
}
