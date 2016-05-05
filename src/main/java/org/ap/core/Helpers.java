package org.ap.core;

/**
 * Created by ymetelkin on 5/5/16.
 */
public class Helpers {
    public static String safeTrim(String input) {
        if (input == null) {
            return null;
        }
        String test = input.trim();
        return test.length() == 0 ? null : test;
    }
}
