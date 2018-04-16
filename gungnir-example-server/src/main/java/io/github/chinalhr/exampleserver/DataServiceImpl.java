package io.github.chinalhr.exampleserver;

import io.github.chinalhr.gungnir.annonation.GService;
import org.springframework.stereotype.Service;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:40 2018/4/16
 * @Email : 13435500980@163.com
 */

@GService(value = IDataService.class)
@Service
public class DataServiceImpl implements IDataService{

    @Override
    public void helloWorld() {
        System.out.println("Gungnir Hello World");
    }
}
