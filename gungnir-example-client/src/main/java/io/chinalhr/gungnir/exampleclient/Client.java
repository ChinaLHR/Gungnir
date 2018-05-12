package io.chinalhr.gungnir.exampleclient;

import io.github.chinalhr.api.IDataService;
import io.github.chinalhr.gungnir.utils.GeneralUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:21 2018/4/23
 * @Email : 13435500980@163.com
 */
public class Client {
    public static void main(String[] args) throws SocketException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        for (int i = 0; i < 100; i++) {
            IDataService service = context.getBean(IDataService.class);
            String s = service.helloWorld();
            System.out.println("获取到数据==================>"+s);

        }
        System.exit(0);
    }
}
