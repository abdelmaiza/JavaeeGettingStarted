package com.pluralsight.bookstore.util;

import java.util.Random;

/**
 * Created by b010nsm on 18/02/2019.
 */
public class IsbnGenerator implements NumberGenerator {

    @Override
    public String generateNumber() {

        return "13-84356-" + Math.abs(new Random().nextInt());
    }
}
