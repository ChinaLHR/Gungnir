package io.github.chinalhr.gungnir_uid.core;

import io.github.chinalhr.gungnir_uid.utils.NetUtil;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:41 2018/9/9
 * @Email : 13435500980@163.com
 * <p>
 * 根据IP地址后三位生成机器标识策略(0~255)
 */
public class MachineSignGenerateIpStrategy implements IMachineSignGenerate {

    @Override
    public Long GenerateMachineSign() throws Exception {
        String currentServerIp = NetUtil.getServerIp();
        int lastIndex = currentServerIp.lastIndexOf(".");
        Long machineSign = Long.valueOf(currentServerIp.substring(lastIndex + 1));
        return machineSign;
    }

}
