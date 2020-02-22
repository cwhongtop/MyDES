package mydes;

import java.io.UnsupportedEncodingException;

public class InitFunction
{
    /**
    *将任意格式的密钥获取hash值，格式化为char[64]
    *二进制数
    *生成64
    **/
    public static char[] keypre(String k)
    {
        String ha = String.format("%08d",k.hashCode());
        ha = ha.replace("-","");
        String key64 = "";

        for (int i = 0;i < 8;i++)
        {
            key64 += String.format("%08d",
                Integer.parseInt(Integer.toBinaryString(
                    Integer.parseInt(ha.substring(i,i+1)))));

        }
        return key64.toCharArray();
    }

    /****
    *明文初始化处理
    *将每一位byte转化为64位二进制，并以String类型保存
    *
    */
    public static String[] byte_data_64(byte[] dataByte)
    {

        String[] string = new String[dataByte.length];
        if (dataByte.length%8==0)
        {
            String dataString[] = new String[dataByte.length/8];
            for (int i = 0;i < dataString.length;i++)
            {
                dataString[i] = "";
                for (int j=i*8;j<(i+1)*8;j++)
                {
                    dataString[i] += String.format("%08d",
                        Integer.parseInt(
                            Integer.toBinaryString(dataByte[j]&0xff)));
                }
            }
            string = dataString;
        }
        else
        {
            int temp = dataByte.length%8;
            int n = 8 - temp;
            String dataString[] = new String[dataByte.length/8 + 1];
            int i;
            for (i = 0;i < dataString.length-1;i++)
            {
                dataString[i] = "";
                for (int j=i*8;j<(i+1)*8;j++)
                {
                    dataString[i] += String.format("%08d",
                        Integer.parseInt(
                            Integer.toBinaryString(dataByte[j]&0xff)));
                }
            }
            dataString[i] = "";
            for (int j=i*8;j<i*8+temp;j++)
            {
                dataString[i] += String.format("%08d",
                    Integer.parseInt(
                        Integer.toBinaryString(dataByte[j]&0xff)));
            }
            for (int j=0;j<n;j++)
            {
                dataString[i] = dataString[i] + "00000000";
            }

            string = dataString;
        }

        // for (int i = 0;i < dataByte.length;i++)
        // {
        //     dataString[i] = String.format("%064d",
        //         Integer.parseInt(
        //             Integer.toBinaryString(dataByte[i]&0xff)));
        // }
        return string;
    }

    /*
    *将二进制转化为String
    */
    public static byte[] en64_String(String en64)
    {
        String enstring = "";
        // try
        // {

            int temp = en64.length()%6;
            if (temp!=0)
            {
                for (int i=0;i<6-temp;i++)
                {
                    en64 = en64 + "0";
                }
            }

            byte string[] = new byte[en64.length()/6];
            for (int i=0;i<string.length;i++)
            {
                string[i] = (byte) (two_ten(en64.substring(i*6,(i+1)*6)) + 32);
            }
            // enstring = new String(string);
        // }catch (UnsupportedEncodingException e)
        // {
        //     System.out.println("encoding error");
        // }
        return string;
    }

    /**
    *密文解密预处理
    *将密文每64位二进制一组截取成String数组
    *
    */
    public static String[] data_64_string(String data64)
    {
        String string[] = new String[data64.length()/64];
        for (int i= 0;i<string.length;i++)
        {
            string[i] = data64.substring(i*64,(i+1)*64);
        }
        return string;
    }

    /**
    *将String类型密文转成二进制
    */
    public static String enByte_data_64(byte[] dataByte)
    {
        String[] string = new String[dataByte.length];
        String dataString = "";
        for (int i = 0;i < dataByte.length;i++)
        {
            dataString += String.format("%06d",
                Integer.parseInt(
                    Integer.toBinaryString((dataByte[i]-32)&0xff)));
        }

        if (dataString.length()%8!=0)
        {
            int temp = dataString.length()%8;
            dataString = dataString.substring(0,dataString.length()-temp);
        }

        return dataString;
    }

    /**
    *将解密后的二进制转为String
    *还原密文
    */
    public static byte[] de64_String(String bytestring)
    {

        while (bytestring.substring(bytestring.length()-8,bytestring.length()).matches("00000000"))
        {
            bytestring = bytestring.substring(0,bytestring.length()-8);
        }
        byte string[] = new byte[bytestring.length()/8];
        for (int i=0;i<string.length;i++)
        {
            string[i] = (byte) two_ten(
                bytestring.substring(i*8,(i+1)*8));
        }
        return string;
    }

    /**
    *二进制转十进制
    */
    public static int two_ten(String two)
    {
        String twoString = two;
        int ten = 0;
        int pow = 0;

        for (int i=twoString.length()-1;i>=0;i--)
        {
            ten += Math.pow(2,pow) * (twoString.charAt(i) == '1'?1:0);
            pow++;
        }
        return ten;
    }

    /**
    *二进制转十六进制
    */
    public static String two_hex(String two)
    {
        if (two == null || two.equals("") || two.length()%8 !=0)
        {
            return null;
        }
        StringBuffer temp = new StringBuffer();
        int inttemp = 0;
        for (int i=0;i<two.length();i+=4)
        {
            inttemp = 0;
            for (int j = 0;j<4;j++)
            {
                inttemp +=Integer.parseInt(two.substring(i+j,i+j+1))<<(4-j-1);
            }
            temp.append(Integer.toHexString(inttemp));
        }
        return temp.toString();
    }

    /**
    *将64位int数组转化为64位char数组
    */
    public static char[] int64tochar64(int[] int64)
    {
        char char64[] = new char[64];
        String string64 = new String();
        for(int i=0;i<64;i++)
        {
            string64 += char64[i];
        }
        return string64.toCharArray();
    }
}
