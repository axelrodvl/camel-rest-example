package co.axelrod.camel.rest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Vadim Axelrod (vadim@axelrod.co) on 01.06.2018.
 */
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
        context.start();
    }
}
