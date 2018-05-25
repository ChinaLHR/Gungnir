package io.github.chinalhr.provider1;

import io.github.chinalhr.gungnir.annonation.GService;
import io.github.chinalhr.serviceapi.IDataService;

/**
 * @Author : ChinaLHR
 * @Date : Create in 17:05 2018/5/25
 * @Email : 13435500980@163.com
 */
@GService(value = IDataService.class,
        version = "1.0.0",
        groupName = "dev",
        weight = 10)
public class DataServiceImpl implements IDataService{

    @Override
    public String helloWorld() {
        String s = "Gungnir Hello World by 8000";
        return s;
    }
}
