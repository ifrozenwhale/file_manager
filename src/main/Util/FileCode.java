package main.Util;

import java.io.*;

/**
 * 文件加密工具类
 */
public class  FileCode {
    private static int dataOfFile = 0; //文件字节内容

    /**
     * 加密
     * @param srcFile 要加密的文件
     * @param encFile 加密后的文件
     * @param key 密钥
     * @throws Exception 异常
     */
    public static void EncFile(File srcFile, File encFile, int key) throws Exception {
        // 检查是否存在
        if(!srcFile.exists()){
            System.out.println("source file not exixt");
            return;
        }
        if(!encFile.exists()){
            System.out.println("encrypt file created");
            encFile.createNewFile();
        }
        InputStream fis  = new FileInputStream(srcFile);
        OutputStream fos = new FileOutputStream(encFile);

        while ((dataOfFile = fis.read()) > -1) {
            fos.write(dataOfFile^key);
        }

        fis.close();
        fos.flush();
        fos.close();
    }

    /**
     * 解密
     * @param encFile 加密了的文件
     * @param decFile 要解密生成的文件
     * @param key 密钥
     * @throws Exception 异常
     */
    public static void DecFile(File encFile, File decFile, int key) throws Exception {
        if(!encFile.exists()){
            System.out.println("encrypt file not exixt");
            return;
        }

        if(!decFile.exists()){
            System.out.println("decrypt file created");
            decFile.createNewFile();
        }
        InputStream fis  = new FileInputStream(encFile);
        OutputStream fos = new FileOutputStream(decFile);
        while ((dataOfFile = fis.read()) > -1) {fos.write(dataOfFile^key); }

        fis.close();
        fos.flush();
        fos.close();
    }

    /**
     * 测试类
     * @param args 参数
     */
    public static void main(String[] args) {
        File srcFile = new File("E:\\Alice\\html\\whale2.jpg"); //初始文件
        File encFile = new File("E:\\Alice\\html\\whale2Encode.jpg"); //加密文件
        File decFile = new File("E:\\Alice\\html\\whale2Decode.jpg"); //解密文件

        try {
            EncFile(srcFile, encFile,5123); //加密操作
            DecFile(encFile, decFile, 51223);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}