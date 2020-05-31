package main.model;

import main.Util.FileCode;
import main.Util.FileCopy;
import main.Util.FileZip;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * 文件数据模型
 */
public class FileModel{
    private List<File> defaultListModel; // 文件列表
    private List<String> copySources; // 剪切板
    private File currentFile; // 当前文件路径
    private Stack<File> redoStack; // redo 栈

        /**
         * 得到当前url
         * @return url地址
         */
    public String getUrl(){
        if(currentFile == null) return null;
        return currentFile.getPath();
    }
    public FileModel(){
        init();
    }
    public List<File> getDefaultListModel() {
        return defaultListModel;
    }

    private void init(){
        defaultListModel = new ArrayList<>();
        copySources = new ArrayList<>();
        redoStack = new Stack<>();
        initRoots();
    }

    /**
     * 初始化根目录
     */
    public void initRoots(){
        defaultListModel.clear();
        defaultListModel.addAll(Arrays.asList(File.listRoots()));
    }

    /**
     * 更新文件数据
     * @param file 当前的文件
     */
    public void updateModels(File file){
        currentFile = file;
        defaultListModel.clear();
        File[] files = file.listFiles();
        defaultListModel.addAll(Arrays.asList(files));
    }

    /**
     * 添加文件到剪切板
     * @param files 文件列表
     */
    public void addCopySources(List<File> files){
        for(File file: files){
            String str = file.getPath();
            copySources.add(str);
        }
    }

    /**
     * 压缩
     * @param files 文件列表
     * @param name 文件名
     */
    public void zipCompress(List<File> files, String name){
        List<String> zipFiles = new ArrayList<>();
        for(File file:files ){
            zipFiles.add(file.getPath());
        }

        if(name != null){
            FileZip.compress(zipFiles, currentFile.getPath() + File.separator + name + ".zip");
            updateModels(currentFile);
        }
    }

    /**
     * 解压
     * @param fileList 文件列表
     */
    public void zipUnCompress(List<File> fileList){
        for(File f: fileList){
            try {
                FileZip.zipUncompress(f.getPath(), currentFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateModels(currentFile);
    }

    /**
     * 剪切
     */
    public void paste(){
        for(String s : copySources){
            File tmp = new File(s);
            if(tmp.isFile()) {
                FileCopy.copyFile(s, currentFile.getPath() + File.separator + tmp.getName());
            } else {
                FileCopy.copyDir(s, currentFile.getPath() + File.separator + tmp.getName());
            }
        }
        copySources.clear();
        updateModels(currentFile);
    }

    /**
     * 新建文件夹爱
     * @param name 文件夹名字
     */
    public void createFile(String name){
        File dir = new File( currentFile.getPath() + File.separator + name);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
            getDefaultListModel().add(dir);
        }
    }

    /**
     * 递归删除函数
     * @param file 要递归删除的文件
     * @return 是否成功删除
     */
    private boolean deleteHelp(File file){
        if(!file.exists()){
            return false;
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                deleteHelp(f);
            }
        }
        return file.delete();
    }

    /**
     * 删除
     * @param fileList 要删除的文件列表
     */
    public void delete(List<File> fileList){
        for(File f: fileList){
            defaultListModel.remove(f);
            deleteHelp(f);
        }
    }

    /**
     * 加密
     * @param files 要加密的文件列表
     * @param key 加密密钥
     */
    public void enCrypt(List<File> files, int key){
        try {
            for(File file : files){
                if(file.isFile()){
                    String name = file.getName();
                    String parent = file.getParent();
                    File tmp = new File(parent + File.separator + "encode_" + name);
                    FileCode.EncFile(file, tmp, key);
                    file.delete();
                    tmp.renameTo(new File(parent + File.separator + name));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密
     * @param files 要解密的文件
     * @param key 密钥
     */
    public void decrypt(List<File> files, int key){
        try {
            for(File file:files){
                File tmp = new File(file.getParent() + File.separator + "decode_" + file.getName());
                FileCode.DecFile(file, tmp, key);
                if(!defaultListModel.contains(tmp)){
                    defaultListModel.add(tmp);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开文件
     * @param file 文件
     * @return 是否打开成功
     */
    public boolean openFile(File file){
        if(file.isFile()){
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                return false;
                // ex.printStackTrace();
            }
        }else{
            updateModels(file);
            redoStack.clear();
        }
        return true;
    }

    /**
     * 后退操作
     */
    public void undo(){
        File file = currentFile;
        // 如果是根目录
        if(file == null){
            initRoots();
            currentFile = null;
        }
        // 如果父级目录是根目录
        if(file.getParentFile() == null){
            initRoots();
            currentFile = null;
        }else{
            updateModels(file.getParentFile());
        }
        redoStack.push(file);
    }

    /**
     * 前进操作
     */
    public void redo(){
        if(redoStack.isEmpty()){
            return;
        }
        File file = redoStack.pop();
        updateModels(file);
    }
}
