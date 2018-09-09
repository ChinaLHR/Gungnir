package io.github.chinalhr.consumer1;

import io.github.chinalhr.gungnir_rpc.lock.redis.RedisGDistributedLockHandler;
import io.github.chinalhr.serviceapi.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:48 2018/5/25
 * @Email : 13435500980@163.com
 */
@RestController
@RequestMapping("/TestInvoker")
public class ConsumerController {

    @Autowired
    private IDataService iDataService;

    @GetMapping
    public String TestInvoker(){
//        long startTime=System.currentTimeMillis();
//        for (int i = 0; i < 500; i++) {
//            String s = iDataService.helloWorld();
//            System.out.println("获取到数据==================>"+s);
//        }
//        long endTime=System.currentTimeMillis();
//        return "程序运行时间： "+(endTime-startTime)+"ms";
        RedisGDistributedLockHandler lockHandler = new RedisGDistributedLockHandler();
//        lockHandler.tryLock()
        return "OK";
    }

}
