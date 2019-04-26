package com.me.mac.guomi;

public class MacTest {
    public static void main(String[] args){
        try {
            //密钥
            String kkey = "0123456789ABCDEFFEDCBA9876543210";
            //待加密数据
            String data = "0200 199559993500000000002 000000 000000010000 0706160321 003924 7011 00 0801020002 0801020002 F4112345 F42012345678912";
            //POS终端MAC值
            String mac_1 = getMac_1(kkey, data);
            System.out.println("POS终端MAC值：" + mac_1);
            //联机MAC值
            String mac_2 = getMac_2(kkey, data);
            System.out.println("联机MAC值：" + mac_2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sm4国密计算mac（POS终端MAC）
     * @param key 16进制字符串密钥
     * @param data
     * @return
     */
    public static String getMac_1(String key,String data){
        //将key转换为字节数组
        byte[] keys = TransformUtils.HexStringToByteArr(key);
        //将加密数据转换为16进制字符串
        String hexData = TransformUtils.StringToHexString(data);
        //将数据分组 32位为一组
        String[] dataGroup = TransformUtils.dataGrouping(hexData, 32);
        //进行异或运算
        String xorData = TransformUtils.handleXOrStringArr(dataGroup);
        //将异或结果转化为16进制字符串
        String hexOxrData = TransformUtils.StringToHexString(xorData);
        //取前16字节和后16字节  两个16进制位表示一个字节
        String start32Bit = hexOxrData.substring(0,32);
        String end32Bit = hexOxrData.substring(hexOxrData.length() - 32,hexOxrData.length());
        //前16位进行首次加密
        byte[] encodeSMS4_1 = SMS4.encodeSMS4(TransformUtils.HexStringToByteArr(start32Bit), keys);
        //加密结果与后16位进行异或
        String xOrEnd = TransformUtils.xOr(TransformUtils.bytesToHexString(encodeSMS4_1, 16), end32Bit);
        //异或结果转成字节数组
        byte[] bytes = TransformUtils.HexStringToByteArr(xOrEnd);
        //进行二次加密
        byte[] resultByte = SMS4.encodeSMS4(bytes, keys);
        //将加密结果转换成16进制字符串 并取前八个字符作为mac值返回
        String result = TransformUtils.bytesToHexString(resultByte, 16);
        //取前8字节作为mac值返回
        result = result.substring(0,16);
        return result.toUpperCase();
    }

    /**
     * sm4国密计算mac（POS终端MAC）
     * @param key 16进制字符串密钥
     * @param data 待加密字符串
     * @return
     * @throws Exception
     */
    public static String getMac_2(String key,String data) throws Exception {
        //将key转换为字节数组
        byte[] keys = TransformUtils.HexStringToByteArr(key);
        //将加密数据转换为16进制字符串
        String hexData = TransformUtils.StringToHexString(data);
        //将数据分组 32位为一组
        String[] dataGroup = TransformUtils.dataGrouping(hexData, 32);
        //如果没有数据抛出异常
        if (dataGroup.length <= 0)
            throw new Exception("no data");
        //开始加密过程
        //1、对第0组明文数据进行加密 得到第0组明文数据的密文
        //2、对第0组数据的密文与第1组明文数据进行异或 得到下次运算要加密的数据
        //循环1、2过程
        String value = dataGroup[0];
        for (int i = 0; i < dataGroup.length - 1;i++){
            String mi0 = TransformUtils.bytesToHexString(SMS4.encodeSMS4(TransformUtils.HexStringToByteArr(value), keys), 16);
            value = TransformUtils.xOr(dataGroup[i+1], mi0);
        }
        //将最后异或的结果加密得到最后的密文
        String result = TransformUtils.bytesToHexString(SMS4.encodeSMS4(TransformUtils.HexStringToByteArr(value), keys), 16);

        return result.substring(0,16).toUpperCase();
    }
}
