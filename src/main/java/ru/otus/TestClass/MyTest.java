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
    public void MyMethod1() {
        System.out.println("do MyMethod1");
    }

    @Before
    public void MyMethod2() {
        System.out.println("do MyMethod2");
    }

    @Test
    public void MyMethod3() {
        System.out.println("do MyMethod3");
    }
}
