package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) {
        TestProcessor.runTest(MyTest.class);
    }


    static class MyTest {

        @Test(order = 1)
        public void firstTest() {
            System.out.println("First test");
        }

        @Test(order = 2)
        @Skip
        public void secondTest() {
            System.out.println("Second test");
        }

        @Test
        @Skip
        public void thirdTest() {
            System.out.println("Third test");

        }

        @Test(order = 23)
        public void fifthTest() {
            System.out.println("Third test");

        }

        @Test(order = -3)
        public void fourthTest() {
            System.out.println("Fourth test");
        }

        @Test(order = -22123)
        public void sixthTest() {
            System.out.println("Sixth test");
        }

        @AfterEach
        public void runAfterTests1() {
            System.out.println("Doing smth after test 1");
        }

        @AfterEach
        public void runAfterTests3() {
            System.out.println("Doing smth after test 3");
        }

        @AfterEach
        public void runAfterTests2() {
            System.out.println("Doing smth after test 2");
        }

        @BeforeEach
        public void runBeforeTests1() {
            System.out.println("Doing smth before test 1");
        }

        @BeforeEach
        public void runBeforeTests2() {
            System.out.println("Doing smth before test 2");
        }

        @BeforeEach
        public void runBeforeTests3() {
            System.out.println("Doing smth before test 3");
        }
    }
}