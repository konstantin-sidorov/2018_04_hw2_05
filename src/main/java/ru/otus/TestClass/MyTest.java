package ru.otus.TestClass;

import ru.otus.Before;
import ru.otus.Test;
import ru.otus.After;


public class MyTest {
    @Override
    public String toString() {
        return "MyTest{}";
    }

    @After
    public void myMethod1() {
        System.out.println("do myMethod1");
    }

    @Before
    public void myMethod2() {
        System.out.println("do myMethod2");
    }

    @Test
    public void myMethod3() {
        System.out.println("do myMethod3");
    }
}
