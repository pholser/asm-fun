package com.pholser.asm.fun;

import java.lang.reflect.Modifier;

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
        assumeTrue(Modifier.isFinal(FinalledUp.class.getModifiers()));

        Class<?> definalized = loader.loadClass("com.pholser.asm.fun.FinalledUp");

        assertFalse(Modifier.isFinal(definalized.getModifiers()));
    }

    @Test
    public void stripsFinalModifierFromMethods() throws Exception {
        assumeTrue(Modifier.isFinal(WithFinalledUpMethods.class.getMethod("foo").getModifiers()));

        Class<?> definalized = loader.loadClass("com.pholser.asm.fun.WithFinalledUpMethods");

        assertFalse(Modifier.isFinal(definalized.getMethod("foo").getModifiers()));
    }
}
