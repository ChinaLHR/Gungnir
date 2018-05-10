package io.chinalhr.gungnir.exampleclient;

import io.github.chinalhr.api.IDataService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:21 2018/4/23
 * @Email : 13435500980@163.com
 */
public class Client {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        IDataService service = context.getBean(IDataService.class);
        String s = service.helloWorld();
        System.out.println("获取到数据==================>"+s);
//        System.exit(0);
    }
}
