package com.i5i58.service.pay.wxpay;

import java.math.BigInteger;

import com.sun.mail.imap.IMAPMultipartDataSource;

public class IPConvert {
//	/** 
//     * @param args 
//     */  
//    public static void main(String[] args)  
//    {  
//        String ipv4 = "127.255.1.1", ipv6 = "fe80:0:0:0:d48a:e3be:7d9d:ffff";  
//        // IPV4 all 4*8 =32 bit  
//        // java int is 32 bit but cann't store this.(it's signed.)  
//        long k = ip2long(ipv4);  
//        System.out.println(k);  
//        System.out.println(Long.toBinaryString(k));  
//        System.out.println(long2ip(k));  
//  
//        System.out.println("ipv6 start :" + ipv6);  
//        BigInteger big = ipv6toInt(ipv6);  
//        System.out.println(big.toString(16));  
//        System.out.println(int2ipv6(big));  
//          
//        ipv6 = "fe80::e054:d6c1:38df:bc73";  
//        System.out.println("ipv6 start :" + ipv6);  
//        big = ipv6toInt(ipv6);  
//        System.out.println(big.toString(16));  
//        System.out.println(int2ipv6(big));  
//        
//        System.out.println(long2ip(big.intValue()));  
//    }  
//  
    public static long ip2long(String ipv4)  
    {  
  
        String[] splits = ipv4.split("\\.");  
        long l = 0;  
        l = l + (Long.valueOf(splits[0], 10)) << 24;  
        l = l + (Long.valueOf(splits[1], 10) << 16);  
        l = l + (Long.valueOf(splits[2], 10) << 8);  
        // yunsuanfu youxianji  
        l = l + (Long.valueOf(splits[3], 10));  
        System.out.println(Long.toBinaryString(l));  
        return l;  
    }  
  
    public static String long2ip(long l)  
    {  
        String ip = "";  
        ip = ip + (l >>> 24);  
  
        ip = ip + "." + ((0x00ffffff & l) >>> 16);  
        ip = ip + "." + ((0x0000ffff & l) >>> 8);  
        ip = ip + "." + (0x000000ff & l);  
        return ip;  
    }  
  
    public static BigInteger ipv6toInt(String ipv6)  
    {  
  
        int compressIndex = ipv6.indexOf("::");  
        if (compressIndex != -1)  
        {  
            String part1s = ipv6.substring(0, compressIndex);  
            String part2s = ipv6.substring(compressIndex + 1);  
            BigInteger part1 = ipv6toInt(part1s);  
            BigInteger part2 = ipv6toInt(part2s);  
            int part1hasDot = 0;  
            char ch[] = part1s.toCharArray();  
            for (char c : ch)  
            {  
                if (c == ':')  
                {  
                    part1hasDot++;  
                }  
            }  
            // ipv6 has most 7 dot  
            return part1.shiftLeft(16 * (7 - part1hasDot )).add(part2);  
        }  
        String[] str = ipv6.split(":");  
        BigInteger big = BigInteger.ZERO;  
        for (int i = 0; i < str.length; i++)  
        {  
            //::1  
            if (str[i].isEmpty())  
            {  
                str[i] = "0";  
            }  
            big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16))  
                    .shiftLeft(16 * (str.length - i - 1)));  
        }  
        return big;  
    }  
      
    public static String int2ipv6(BigInteger big)  
    {  
        String str = "";  
        BigInteger ff = BigInteger.valueOf(0xffff);  
        for (int i = 0; i < 8 ; i++)  
        {  
            str = big.and(ff).toString(16) + ":" + str;  
              
            big = big.shiftRight(16);  
        }  
        //the last :  
        str = str.substring(0, str.length() - 1);  
          
        return str.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");  
    }  
    
    public static String ConvertToIpV4(String ipAddress){
    	int index = ipAddress.indexOf(':');
		if (index == -1){
			return ipAddress;
		}
		BigInteger big = ipv6toInt(ipAddress);
		return long2ip(big.intValue());
    }
}
