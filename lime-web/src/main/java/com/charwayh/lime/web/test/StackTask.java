package com.charwayh.lime.web.test;

/**
 * @author charwayH
 */
public class StackTask {
    static long count = 0;
    public static void main(String[] args) {
        count++;
        System.out.println(count);
        main(args);
    }
}
