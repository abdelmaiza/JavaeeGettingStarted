package com.pluralsight.bookstore.util;

/**
 * Created by b010nsm on 18/02/2019.
 */
public class TextUtil {
    public String sanitize(String textToSanitize ){
        return textToSanitize.replaceAll("\\s+" , " ");
    }
}
