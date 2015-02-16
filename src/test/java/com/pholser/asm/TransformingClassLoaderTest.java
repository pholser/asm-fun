package com.pholser.asm;

import java.lang.reflect.Modifier;

import com.pholser.asm.visitor.Definalizer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class TransformingClassLoaderTest {
    private TransformingClassLoader loader;

    @Before
    public void setUp() {
        loader = new TransformingClassLoader(getClass().getClassLoader(), Definalizer.factory());
    }

    @Test
    public void stripsFinalModifierFromClass() throws Exception {
        assumeTrue(Modifier.isFinal(Final.class.getModifiers()));

        Class<?> transformed = loader.loadClass("com.pholser.asm.Final");

        assertFalse(Modifier.isFinal(transformed.getModifiers()));
    }

    @Test
    public void stripsFinalModifierFromMethods() throws Exception {
        assumeTrue(Modifier.isFinal(WithFinalMethods.class.getMethod("foo").getModifiers()));

        Class<?> transformed = loader.loadClass("com.pholser.asm.WithFinalMethods");

        assertFalse(Modifier.isFinal(transformed.getMethod("foo").getModifiers()));
    }
}
