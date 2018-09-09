package io.github.chinalhr.gungnir_uid;

import io.github.chinalhr.gungnir_uid.core.MachineSignGenerateIpStrategy;
import io.github.chinalhr.gungnir_uid.core.SnowFlakeUidGenerate;
import io.github.chinalhr.gungnir_uid.server.http.HttpServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author : ChinaLHR
 * @Date : Create in 23:58 2018/9/9
 * @Email : 13435500980@163.com
 */
@Slf4j
public class ServerStartup {

    public static void main(String[] args) {
        //todo 增加Redis Set策略选择
        MachineSignGenerateIpStrategy machineSignGenerate = new MachineSignGenerateIpStrategy();
        try {
            Long machineSign = machineSignGenerate.GenerateMachineSign();
            SnowFlakeUidGenerate.initMachineId(machineSign);
            log.info("ServerStartup GenerateMachineSign Success machineSign:{}", machineSign);
        } catch (Exception e) {
            log.error("ServerStartup GenerateMachineSign Failure", e);
        }

        final HttpServer server = new HttpServer();
        server.init();
        server.start();
        //关闭钩子注册
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            System.exit(0);
        }));
    }

}
