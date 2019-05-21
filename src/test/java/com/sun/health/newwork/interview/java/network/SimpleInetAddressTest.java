package com.sun.health.newwork.interview.java.network;

import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by 华硕 on 2019-05-21.
 */
public class SimpleInetAddressTest {

    @Test
    public void test1() throws UnknownHostException {
//        InetAddress inetAddress = InetAddress.getByName("localhost");
//        InetAddress inetAddress = InetAddress.getByName("163.com");
//        InetAddress inetAddress = InetAddress.getByName("123.58.180.7");
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        System.out.println(inetAddress.getHostName());
        System.out.println(inetAddress.getHostAddress());

        InetAddress[] allByName = InetAddress.getAllByName("163.com");
        for (InetAddress address : allByName) {
            System.out.println(address.getHostName());
            System.out.println(address.getHostAddress());
        }

        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost.getHostName());
        System.out.println(localHost.getHostAddress());
        System.out.println(localHost.getAddress());
        System.out.println(localHost);

        InetAddress byAddress = InetAddress.getByAddress(new byte[]{123, 58, (byte) 180, 7});
        System.out.println(byAddress);

        String cacheTTL = System.getProperty("networkaddress.cache.ttl");
        System.out.println(cacheTTL);
        String cacheNegativeTTL = System.getProperty("networkaddress.cache.negative.ttl");
        System.out.println(cacheNegativeTTL);

        InetAddress inetAddress1 = InetAddress.getByAddress(new byte[]{123, 58, (byte) 180, 8});
        System.out.println(inetAddress1.getCanonicalHostName());

    }

    @Test
    public void test2() throws UnknownHostException {
        try {
            NetworkInterface localhost = NetworkInterface.getByInetAddress(InetAddress.getByName("localhost"));
            System.out.println(localhost);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                System.out.println(networkInterface);
                System.out.println(networkInterface.getDisplayName());
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while(inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    System.out.println(address);
                }
                System.out.println("==============================");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
