package mydes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.util.ArrayList;

import mydes.InitFunction;
import mydes.DataTable;

public class DESCore
{
    private DataTable data = new DataTable();
    private InitFunction function = new InitFunction();
    private String key;

    public DESCore()
    {
        this.key = "0123456789";
    }

    public DESCore(String key)
    {
        this.key = key;
    }

    /****************************************
    *
    ***************密钥处理*******************
    *
    ****************************************/
    /**
    *密钥置换1
    *将64为初始密钥压缩为56位
    **/
    private char[] keyReplace_1(char[] key64)
    {
        char[] key56 = new char[56];
        for (int i = 0;i<56 ;i++)
        {
            key56[i] = key64[data.PC_1[i]-1];
        }

        return key56;
    }

    /**
    *密钥分成前后两份各28位
    *对密钥循环左移生成子密钥
    */
    private char[] keyLoop(char[] key56,int loopNum)
    {
        String key1 = new String();
        String key2 = new String();
        String key = new String(key56);
        //密钥前28位循环左移
        key1 = (key.substring(0,28) +
            key.substring(0,2)).substring(loopNum,loopNum + 28);
        //密钥后28位循环左移
        key2 = (key.substring(28,56) +
            key.substring(28,30)).substring(loopNum,loopNum + 28);

        return (key1 + key2).toCharArray();

    }

    /**
    * 密钥置换2
    * 将56位密钥压缩成48位
    */
    private char[] keyReplace_2(char[] key56)
    {
        char[] key48 = new char[48];
        for (int i = 0;i<48;i++)
        {
            key48[i] = key56[data.PC_2[i]-1];
        }

        return key48;
    }

    /**
    * 生成16轮子密钥
    * 每一轮都是48位密钥
    */
    private char[][] getKey(char[] key64)
    {
        char[][] key16_56 = new char[16][56];
        char[][] key16_48 = new char[16][48];
        char[] key56 = new char[56];

        key56 = keyReplace_1(key64);
        key16_56[0] = keyLoop(key56,data.LeftMove[0]);

        for (int i = 1;i < 16;i++)
        {
            key16_56[i] = keyLoop(key16_56[i-1],data.LeftMove[i]);
        }

        for (int i=0;i<16;i++)
        {
            key16_48[i] = keyReplace_2(key16_56[i]);
        }

        return key16_48;
    }

    /****************************************
    *
    ***************明文处理*******************
    *
    ****************************************/

    /**
    *初始置换
    */
    private char[] initReplace(char[] data64)
    {
        char[] replaceData64 = new char[64];
        for (int i = 0;i<64;i++)
        {
            replaceData64[i] = data64[data.IP[i]-1];
        }

        return replaceData64;
    }

    /**
    *逆置换
    */
    private char[] finalReplace(char[] data64)
    {
        char[] replaceData64 = new char[64];
        for (int i = 0;i<64;i++)
        {
            replaceData64[i] = data64[data.IP_1[i]-1];
        }

        return replaceData64;
    }

    /**
    * E表置换 (拓展置换)
    * 将32位右部分拓展为48位
    */
    private char[] eReplace(char[] dataR32)
    {
        char[] replaceDataR48 = new char[48];
        for (int i= 0;i<48;i++)
        {
            replaceDataR48[i] = dataR32[data.E[i]-1];
        }

        return replaceDataR48;
    }

    /**
    *xor 异或操作
    *生成48位轮密钥
    */
    private char[] xor(char[] dataR48,char[] key)
    {
        String xKey = new String();
        for (int i=0;i<dataR48.length;i++)
        {
            xKey += dataR48[i] ^ key[i];
        }

        return xKey.toCharArray();
    }

    /**
    * S 盒置换
    */
    private char[] sReplace(char[] xorR48)
    {
        char[][] replaceXorR48 = new char[8][6];
        String string = new String();
        for (int i= 0;i<8;i++)
        {
            for (int j=0;j<6;j++)
            {
                replaceXorR48[i][j] = xorR48[i*6+j];
            }
            int p = function.two_ten(String.valueOf(replaceXorR48[i][0])+
                String.valueOf(replaceXorR48[i][5]));
            int q = function.two_ten(String.valueOf(replaceXorR48[i][1])+
                String.valueOf(replaceXorR48[i][2]) +
                String.valueOf(replaceXorR48[i][3]) +
                String.valueOf(replaceXorR48[i][4]));
            string += String.format("%04d",Integer.parseInt(
                Integer.toBinaryString(data.S_Box[i][p][q])));
        }
        return string.toCharArray();
    }

    /**
    *P 表置换
    */
    private char[] pReplace(char[] sR32)
    {
        char[] pR32 = new char[32];
        for (int i=0;i<32;i++)
        {
            pR32[i] = sR32[data.P[i]-1];
        }
        return pR32;
    }

    /**
    *轮置换
    */
    public char[] core(char[] dataR32,char[] key48)
    {
        //E_Table置换
        char[] eR48 = eReplace(dataR32);
        //异或
        char[] xorR48 = xor(eR48,key48);
        //S_Box置换
        char[] sR32 = sReplace(xorR48);
        //P_Table置换
        char[] pR32 = pReplace(sR32);
        String pp = new String(pR32);

        return pR32;
    }

    public String enCode(char[] data64,char[] key64)
    {
        char[][] dataL32 = new char[17][32];
        char[][] dataR32 = new char[17][32];
        char[][] key16_48 = new char[16][48];
        char[] replaceData64 = new char[64];
        char[] finalData64 = new char[64];


        data64 = initReplace(data64);
        key16_48 = getKey(key64);
        for (int i=0;i<32;i++)
        {
            dataL32[0][i] = data64[i];
            dataR32[0][i] = data64[i+32];
        }

        for (int i=1;i<17;i++)
        {
            char[] xorR48 = xor(dataL32[i-1],core(dataR32[i-1],key16_48[i-1]));
            for (int j=0;j<32;j++)
            {
                dataL32[i][j] = dataR32[i-1][j];
                dataR32[i][j] = xorR48[j];
            }
        }

        for (int i = 0;i<32;i++)
        {
            replaceData64[i] = dataR32[16][i];
            replaceData64[i+32] = dataL32[16][i];
        }
        finalData64 = finalReplace(replaceData64);
        String string = new String(finalData64);
        return string;
    }

    public String deCode(char[] data64,char[] key64)
    {
        char[][] dataL32 = new char[17][32];
        char[][] dataR32 = new char[17][32];
        char[][] key16_48 = new char[16][48];
        char[] replaceData64 = new char[64];
        char[] finalData64 = new char[64];

        data64 = initReplace(data64);
        key16_48 = getKey(key64);
        for (int i=0;i<32;i++)
        {
            dataL32[0][i] = data64[i];
            dataR32[0][i] = data64[i+32];
        }

        for (int i=1;i<17;i++)
        {
            char[] xorR48 = xor(dataL32[i-1],core(dataR32[i-1],key16_48[16-i]));
            for (int j=0;j<32;j++)
            {
                dataL32[i][j] = dataR32[i-1][j];
                dataR32[i][j] = xorR48[j];
            }
        }

        for (int i = 0;i<32;i++)
        {
            replaceData64[i] = dataR32[16][i];
            replaceData64[i+32] = dataL32[16][i];
        }
        finalData64 = finalReplace(replaceData64);
        String string = new String(finalData64);
        return string;

    }

    //加密byte数组
    public byte[] encrypt(byte[] data,String key)
    {
        String[] data64 = function.byte_data_64(data);
        System.out.println(data64);
        char[] key64 = function.keypre(key);
        String enString = "";
        for (int i=0;i<data64.length;i++)
        {
            char[] dataChar = data64[i].toCharArray();
            enString += enCode(dataChar,key64);
        }
        return function.en64_String(enString);

    }

    //解密byte数组
    public byte[] decrypt(byte[] data,String key)
    {
        String enString = function.enByte_data_64(data);
        String[] data64 = function.data_64_string(enString);
        System.out.println(data64);
        char[] key64 = function.keypre(key);
        String deString = "";
        for (int i=0;i<data64.length;i++)
        {
            char[] dataChar = data64[i].toCharArray();
            deString += deCode(dataChar,key64);
        }
        return function.de64_String(deString);
    }

    //加密String
    public String encryptString(String data)
    {
        byte[] dataByte = data.getBytes();
        byte[] endata = encrypt(dataByte,key);
        return new String(endata);
    }

    //解密String
    public String decryptString(String data)
    {
        byte[] dataByte = data.getBytes();
        byte[] dedata = decrypt(dataByte,key);
        return new String(dedata);
    }

    //加密File
    public void encryptFile(String soursePath,String resultPath)
    {
        try
        {
            File file= new File(soursePath);    //filename为 文件目录，请自行设置
            InputStream in = new FileInputStream(file);    //真正要用到的是FileInputStream类的read()方法
            byte[] bytes = new byte[in.available()];    //in.available()是得到文件的字节数


            in.read(bytes);    //把文件的字节一个一个地填到bytes数组中
            in.close();    //记得要关闭in
            byte[] outbytes = encrypt(bytes,key);
            FileOutputStream os = new FileOutputStream(resultPath);   // 写入输出流

            os.write(outbytes,0,outbytes.length);   //把字节写入文件
            os.close();   // 关闭输出流


        }catch (FileNotFoundException e)
        {

        }catch (IOException e)
        {

        }
    }

    //加密File
    public void decryptFile(String soursePath,String resultPath)
    {
        try
        {
            File file= new File(soursePath);    //filename为 文件目录，请自行设置
            InputStream in = new FileInputStream(file);    //真正要用到的是FileInputStream类的read()方法
            byte[] bytes = new byte[in.available()];    //in.available()是得到文件的字节数

            FileOutputStream os = new FileOutputStream(resultPath);   // 写入输出流

            in.read(bytes);    //把文件的字节一个一个地填到bytes数组中
            byte[] outbytes = decrypt(bytes,key);
            os.write(outbytes,0,outbytes.length);    //把字节写入文件
            os.close();   // 关闭输出流
            in.close();    //记得要关闭in

        }catch (FileNotFoundException e)
        {

        }catch (IOException e)
        {

        }
    }

}
