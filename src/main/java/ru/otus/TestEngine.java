package ru.otus;


import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TestEngine {
    private static List<Method> beforeMethods = new ArrayList<Method>();
    private static List<Method> afterMethods = new ArrayList<Method>();
    private static List<Method> testMethods = new ArrayList<Method>();

    private static void SetMethod(Class clazz) {
        Method[] MyMethods = clazz.getDeclaredMethods();
        for (Method m : MyMethods) {
            Annotation[] MyAnnotations = m.getAnnotations();
            for (Annotation a : MyAnnotations) {

                if (a.annotationType().equals(Before.class)) {
                    beforeMethods.add(m);
                }
                if (a.annotationType().equals(Test.class)) {
                    testMethods.add(m);
                }
                if (a.annotationType().equals(After.class)) {
                    afterMethods.add(m);
                }
            }
        }
    }

    private static void ExecuteMethod(Class clazz) throws Exception {
        Object obj = clazz.newInstance();
        for (Method m : beforeMethods) {
            m.invoke(obj);
        }
        for (Method m : testMethods) {
            m.invoke(obj);
        }
        for (Method m : afterMethods) {
            m.invoke(obj);
        }
    }

    public static void TestObject(String type, String name) throws Exception {
        if ("Class".equals(type) && !name.isEmpty()) {
            Class<?> clazz = Class.forName(name);
            SetMethod(clazz);
            ExecuteMethod(clazz);
        } else if ("Package".equals(type) && !name.isEmpty()) {
            List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
            classLoadersList.add(ClasspathHelper.contextClassLoader());
            classLoadersList.add(ClasspathHelper.staticClassLoader());

            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(name))));

            Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

            for (Class<?> clazz : allClasses) {
                SetMethod(clazz);
                ExecuteMethod(clazz);
            }
        }
    }
}
