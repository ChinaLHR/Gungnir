package io.github.chinalhr.gungnir.enums;

/**
 * @Author : ChinaLHR
 * @Date : Create in 21:44 2018/5/20
 * @Email : 13435500980@163.com
 */
public enum  HearbeatEnum {

    PING((byte) 0x01),PONG((byte)0x02),DEFAULT((byte)0x00);

    private byte type;

    HearbeatEnum(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
