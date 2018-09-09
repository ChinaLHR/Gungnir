package io.github.chinalhr.gungnir_uid.core;

import io.github.chinalhr.gungnir_uid.exception.GungnirUidException;

import javax.annotation.Resource;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : ChinaLHR
 * @Date : Create in 22:12 2018/9/9
 * @Email : 13435500980@163.com
 */
public class SnowFlakeUidGenerate {

    /**
     * 起始时间戳 2018-09-05 14:00:00
     */
    private final static long START_STMP = 1536127200000L;
    private static ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * 每一部分占用的位数
     * SEQUENCE_BIT:序列号占用的位数
     * MACHINE_BIT:机器标识占用的位数(0-1024)
     */
    private final static long SEQUENCE_BIT = 12;
    private final static long MACHINE_BIT = 10;

    /**
     * 每一部分占用的最大值
     * MAX_SEQUENCE_NUM:序列号最大值
     * MAX_MACHINE_NUM:机器标识最大值
     */
    private final static long MAX_SEQUENCE_NUM = -1L ^ (-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);

    /**
     * 每一部分向左移动
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long TIMESTMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    private static long machineId;
    private static long sequence = 0L;
    private static long lastStmp = -1L;

    /**
     * 产生下一个Id
     *
     * @return
     */
    public static long nextId() {
        reentrantLock.lock();
        try {
            long currStmp = getNewstmp();
            if (currStmp < lastStmp) {
                throw new GungnirUidException("SnowFlakeUtil: 机器时钟发生错误,无法生成UUID");
            }

            if (currStmp == lastStmp) {
                //相同毫秒内，序列号自增
                sequence = (sequence + 1) & MAX_SEQUENCE_NUM;
                //同一毫秒的序列数已经达到最大
                if (sequence == 0L) {
                    currStmp = getNextMill();
                }
            } else {
                //不同毫秒内，序列号置为0
                sequence = 0L;
            }

            lastStmp = currStmp;

            return ((currStmp - START_STMP) << TIMESTMP_LEFT)
                    | machineId << MACHINE_LEFT
                    | sequence;
        } finally {
            reentrantLock.unlock();
        }

    }

    private static long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private static long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void initMachineId(long currentMachineId){
        if (currentMachineId > MAX_MACHINE_NUM || currentMachineId < 0) {
            throw new GungnirUidException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        machineId = currentMachineId;
    }

}
