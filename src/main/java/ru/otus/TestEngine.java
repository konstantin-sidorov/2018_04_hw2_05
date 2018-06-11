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
    private List<Method> beforeMethods;
    private List<Method> afterMethods;
    private List<Method> testMethods;
    private String type;
    private String name;

    private void setMethod(Class clazz) {
        this.beforeMethods = new ArrayList<Method>();
        this.afterMethods = new ArrayList<Method>();
        this.testMethods = new ArrayList<Method>();
        Method[] myMethods = clazz.getDeclaredMethods();
        for (Method m : myMethods) {
            Annotation[] myAnnotations = m.getAnnotations();
            for (Annotation a : myAnnotations) {

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

    private void executeMethod(Class clazz) throws Exception {
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

    public void test() throws Exception {
        if ("Class".equals(type) && !name.isEmpty()) {
            Class<?> clazz = Class.forName(name);
            setMethod(clazz);
            executeMethod(clazz);
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
                setMethod(clazz);
                executeMethod(clazz);
            }
        }
    }

    public TestEngine(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
