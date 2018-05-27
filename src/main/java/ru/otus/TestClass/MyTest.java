package ru.otus.TestClass;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface After{}
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface Before{}
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface Test{}
public class MyTest{
    private static int MySize;

    public MyTest() {
    }

    @Override
    public String toString() {
        return "MyTest{}";
    }

    @After
    public void MyMethod1(){
        System.out.println("do MyMethod1");
    }
    @Before
    public void MyMethod2(){
        System.out.println("do MyMethod2");
    }
    @Test
    public void MyMethod3(){
        System.out.println("do MyMethod3");
    }
}
