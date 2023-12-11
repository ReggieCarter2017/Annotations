package org.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestProcessor {
    public static void runTest(Class<?> testClass) {
        final Constructor<?> declaredConstructor;
        try {
            declaredConstructor = testClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        final Object testObj;

        try {
            testObj = declaredConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

        List<Method> beforeEach = new ArrayList<>();
        List<Method> afterEach = new ArrayList<>();
        HashMap<Integer, Method> testMap = new HashMap<>();

        for (Method method : testClass.getDeclaredMethods()) {
            try {
                if (checkTestMethod(method, BeforeEach.class, void.class, 0) &&
                        !checkTestMethod(method, Skip.class, void.class, 0)) {
                    beforeEach.add(method);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            try {
                if (checkTestMethod(method, AfterEach.class, void.class, 0) &&
                        !checkTestMethod(method, Skip.class, void.class, 0)) {
                    afterEach.add(method);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            try {
                if (checkTestMethod(method, Test.class, void.class, 0) &&
                !checkTestMethod(method, Skip.class, void.class, 0)) {
                    testMap.put(method.getAnnotation(Test.class).order(), method);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
            beforeEach.forEach(it -> runTest(it, testObj));
            for (Integer key : testMap.keySet().stream().sorted().toList()) {
                System.out.print("Order " + key + ": ");
                runTest(testMap.get(key), testObj);
            }
            afterEach.forEach(it -> runTest(it, testObj));

    }

    private static boolean checkTestMethod(Method method, Class<? extends Annotation> annotation, Class returnClass, int paramCount) throws IllegalAccessException {
        return method.getReturnType().isAssignableFrom(returnClass) &&
                method.getParameterCount() == paramCount &&
                method.isAnnotationPresent(annotation);
    }

    private static void runTest(Method method, Object testObj) {
        try {
                method.invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
                throw new RuntimeException("\"" + method.getName() + "\"" + " не запустился.");
            }
    }
}
