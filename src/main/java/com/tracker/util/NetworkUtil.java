package com.tracker.util;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class NetworkUtil {

    public static String getLocalIpAddress() {

        try {

            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {

                NetworkInterface network =
                        interfaces.nextElement();

                if (!network.isUp()
                        || network.isLoopback()
                        || network.isVirtual()) {
                    continue;
                }

                Enumeration<java.net.InetAddress> addresses =
                        network.getInetAddresses();

                while (addresses.hasMoreElements()) {

                    java.net.InetAddress address =
                            addresses.nextElement();

                    if (address instanceof Inet4Address
                            && !address.isLoopbackAddress()) {

                        return address.getHostAddress();
                    }
                }
            }

        } catch (Exception e) {

           log.error(e.getMessage());
        }

        return "Unknown";
    }

    public static String getMacAddress() {

        try {

            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {

                NetworkInterface network =
                        interfaces.nextElement();

                if (!network.isUp()
                        || network.isLoopback()
                        || network.isVirtual()) {
                    continue;
                }

                byte[] mac = network.getHardwareAddress();

                if (mac == null || mac.length == 0) {
                    continue;
                }

                StringBuilder sb = new StringBuilder();

                for (byte b : mac) {
                    sb.append(String.format("%02X-", b));
                }

                sb.deleteCharAt(sb.length() - 1);

                return sb.toString();
            }

        } catch (Exception e) {

           log.error(e.getMessage());
        }

        return "Unknown";
    }
}