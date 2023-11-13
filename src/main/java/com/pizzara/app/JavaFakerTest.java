package com.pizzara.app;

import com.github.javafaker.Faker;

public class JavaFakerTest {
    public static void main(String[] args) {
        var faker = new Faker();
        for (int i = 0; i < 100; i++) {
            System.out.println(faker.team().name());
        }
    }
}
