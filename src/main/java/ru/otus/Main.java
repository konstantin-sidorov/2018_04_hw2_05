package ru.otus;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new RuntimeException("Illegal length parameters:" + args.length);
        }
        String type = args[0];
        String name = args[1];
        TestEngine testObj  =   new TestEngine(type,name);
        testObj.test();


    }
}

