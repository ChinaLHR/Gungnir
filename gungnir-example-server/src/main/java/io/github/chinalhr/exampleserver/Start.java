package io.github.chinalhr.exampleserver;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:43 2018/4/16
 * @Email : 13435500980@163.com
 */
public class Start {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("application.xml");
    }

}
