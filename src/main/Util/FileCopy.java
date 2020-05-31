package main.Util;

import javax.swing.*;
import java.io.*;

/**
 * 文件拷贝工具类
 */
public class FileCopy {
    /**
     * 拷贝文件
     * @param sourceUrl 源文件
     * @param targetUrl 目标文件
     */
    public static void copyFile(String sourceUrl, String targetUrl){
        File source = new File(sourceUrl);
        File target = new File(targetUrl);
        try(BufferedInputStream bis=new BufferedInputStream(new FileInputStream(source));
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(target))) {
            int len = 0;
            byte[] flush = new byte[1024];
            while((len=bis.read(flush)) != -1) {
                bos.write(flush, 0, len);
            }
            bos.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是其父级目录
     * @param f1 f1 is Parent?
     * @param f2 f2 is child?
     * @return true if f1 is parent of f2, else not
     */
    private static boolean isParent(File f1, File f2){
        if(f2.getParentFile() ==  null){
            return false;
        }else if(f2.getParentFile().equals(f1)){
            return true;
        }else{
            return isParent(f1, f2.getParentFile());
        }
    }

    /**
     * 拷贝文件夹
     * @param sourceUrl 源文件夹
     * @param targetUrl 目标文件夹
     */
    public static void copyDir(String sourceUrl, String targetUrl){
        File source = new File(sourceUrl);
        File target = new File(targetUrl);
        if(isParent(source, target)){
            System.out.println("Can't copy!");
            JOptionPane.showMessageDialog(null, "source path is father of target", "Copy Error",JOptionPane.ERROR_MESSAGE);
            return;
        };
        String[] filesUrl = source.list();
        // 新建
        if(!target.exists()){
            target.mkdir();
        }
        for(String fileUrl : filesUrl){
            String tmp1 = sourceUrl + File.separator + fileUrl;
            String tmp2 = targetUrl + File.separator + fileUrl;
            if(new File(tmp1).isDirectory()){
                copyDir(tmp1, tmp2);//递归调用
            }else{
                copyFile(tmp1, tmp2); // 直接拷贝文件
            }
        }
    }
}
