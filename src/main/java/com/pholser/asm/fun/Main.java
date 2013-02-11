package com.pholser.asm.fun;

import org.objectweb.asm.ClassReader;

public class Main {
    public static void main(String[] args) throws Exception {
        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader("java.lang.Runnable");
        cr.accept(cp, 0);
    }
}
