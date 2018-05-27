package ru.otus;


import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;


import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new RuntimeException("Illegal length parameters:" + args.length);
        }
        String type = args[0];
        String name = args[1];
        Class<?> clazz = Class.forName(name);
        Object obj = clazz.newInstance();

        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("ru.otus.TestClass"))));

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);

        for (Class<?> cl : allClasses) {
            if (type == "class" && cl.getName() != name) {
                continue;
            }

            List<Method> BeforeMethods = new ArrayList<Method>();
            List<Method> AfterMethods = new ArrayList<Method>();
            List<Method> TestMethods = new ArrayList<Method>();
            Method[] MyMethods = cl.getDeclaredMethods();
            for (Method m : MyMethods) {
                Annotation[] MyAnnotations = m.getAnnotations();
                for (Annotation a : MyAnnotations) {

                    if (a.annotationType().getSimpleName().equals("Before")) {
                        BeforeMethods.add(m);
                    }
                    if (a.annotationType().getSimpleName().equals("Test")) {
                        TestMethods.add(m);
                    }
                    if (a.annotationType().getSimpleName().equals("After")) {
                        AfterMethods.add(m);
                    }


                }
            }
            for (Method m : BeforeMethods) {
                m.invoke(obj);
            }
            for (Method m : TestMethods) {
                m.invoke(obj);
            }
            for (Method m : AfterMethods) {
                m.invoke(obj);
            }
        }


    }
}

