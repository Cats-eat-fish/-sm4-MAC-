package com.me.mac.guomi;

import java.io.UnsupportedEncodingException;

public class TransformUtils {

    private static char[] hexChar = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 十六进制的字符串转化为字符串  例如 ：“3642464437323641463333363636333334323134373944414242334543463736”--> “6BFD726AF3366633421479DABB3ECF76”
     *
     * @param hex 十六进制字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String HexStringToString(String hex) {
        return new String(HexStringToByteArr(hex));
    }
    public static byte[] HexStringToByteArr(String hex) {
        int len = hex.length() % 2 == 0 ? hex.length() / 2 : hex.length() / 2 + 1;
        byte[] bytes = new byte[len];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * 字符串转为十六进制字符串
     *
     * @param str
     * @return
     */
    public static String StringToHexString(String str) {
        String data = bytesToHexString(str.getBytes(), 32);
        return data;
    }

    /**
     * 字节数组转化为十六进制字符串 并按照len的倍数进行补"0"
     *
     * @param bytes 字节数据
     * @param len   部位原则  不够len倍数补"0"
     * @return
     */
    public static String bytesToHexString(byte[] bytes, int len) {
//        char[] hexStr = new char[bytes.length * 2];
//        int index = 0;
//        for (byte b:bytes) {
//            hexStr[index++] = hexStr[(b >>> 4) & 0xf];
//            hexStr[index++] = hexStr[b & 0xf];
//        }
        StringBuffer hexStr = new StringBuffer();
        for (byte b : bytes) {
            hexStr.append(String.format("%02x", new Integer(b & 0xff)));
        }
        //长度不满32的整数倍 在后边添加 "0"
        while (hexStr.length() % len != 0) {
            hexStr.append("0");
        }

        return hexStr.toString();
    }

    /**
     * 将字符串str 按照长度len 进行分组
     *
     * @param str 字符串
     * @param len 每组字符长度
     * @return
     */
    public static String[] dataGrouping(String str, int len) {
        int lenth = str.length() % len == 0 ? str.length() / len : str.length() / len + 1;
        String[] data = new String[lenth];
        for (int i = 0; i < lenth; i++) {
            data[i] = str.substring(i * len, i * len + len);
        }
        return data;
    }

    /**
     * 将字符串数组进行异或运算
     * @param strs
     * @return
     */
    public static String handleXOrStringArr(String[] strs){
        String result = "";
        for (int i = 1;i < strs.length;i++){
            if (i == 1){
                result = xOr(strs[0],strs[1]);
            }else {
                result = xOr(strs[i],result);
            }
        }
        return result;
    }

    /**
     * 异或运算
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String xOr(String s1, String s2) {
        String data = IntArr2String(xOr(String2IntArr(s1),String2IntArr(s2)));
        return data;
    }
    public static int [] xOr(int[] i1,int[] i2){
        int[] xor = new int[i1.length];
        for (int i = 0;i < i1.length & i < i2.length;i++){
            xor[i] = i1[i]^i2[i];
        }
        return xor;
    }

    public static String IntArr2String(int[] arr){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0;i < arr.length;i++){
            stringBuffer.append(hexChar[arr[i]]);
        }
        return stringBuffer.toString();
    }

    public static int[] String2IntArr(String str) {
        int[] data = new int[str.length()];
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            data[i] = getIntByChar(chars[i]);
        }
        return data;
    }

    public static int getIntByChar(char c) {
        int data = 0;
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                data = Integer.parseInt(Character.toString(c));
                break;
            case 'a':
            case 'A':
                data = 10;
                break;
            case 'b':
            case 'B':
                data = 11;
                break;
            case 'c':
            case 'C':
                data = 12;
                break;
            case 'd':
            case 'D':
                data = 13;
                break;
            case 'e':
            case 'E':
                data = 14;
                break;
            case 'f':
            case 'F':
                data = 15;
                break;
        }
        return data;
    }
}
