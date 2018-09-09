package io.github.chinalhr.gungnir_rpc.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @Author : ChinaLHR
 * @Date : Create in 16:34 2018/4/27
 * @Email : 13435500980@163.com
 */
public class GeneralUtils {

    private static int cpu = Runtime.getRuntime().availableProcessors();

    private static String cacheIp;

    /**
     *
     * 获取UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取本机IP地址
     * @return
     * @throws SocketException
     */
    public static String getHostAddress() throws SocketException {
        if (null!=cacheIp){
            return cacheIp;
        }else {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;

            while (allNetInterfaces.hasMoreElements())
            {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements())
                {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address)
                    {
                        cacheIp = ip.getHostAddress();
                        return ip.getHostAddress();
                    }
                }
            }
            return "";
        }
    }

    public static int getThreadConfigNumberOfIO(){
        return cpu*2+1;
    }

    public static int getThreadConfigNumberOfCPU(){
        return cpu+1;
    }


}
