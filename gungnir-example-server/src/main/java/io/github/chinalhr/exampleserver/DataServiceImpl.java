package io.github.chinalhr.exampleserver;

import io.github.chinalhr.api.IDataService;
import io.github.chinalhr.gungnir.annonation.GService;
import org.springframework.stereotype.Service;

/**
 * @Author : ChinaLHR
 * @Date : Create in 15:40 2018/4/16
 * @Email : 13435500980@163.com
 */

@GService(value = IDataService.class,
          version = "1.0.0",
          groupName = "dev",
          weight = 10)
@Service
public class DataServiceImpl implements IDataService{

    @Override
    public String helloWorld() {
        String s = "Gungnir Hello World by 8000";
        System.out.println(s);
        return s;
    }
}
