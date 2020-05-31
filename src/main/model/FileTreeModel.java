package main.model;

import java.io.File;

/**
 * 文件目录树数据模型
 */
public class FileTreeModel{
    public File[] getRoots(){
        return File.listRoots();
    }

    /**
     * 得到文件列表
     * @param currentFile 文件
     * @return 文件目录下的文件列表
     */
    public File[] getFiles(File currentFile) {
        return currentFile.listFiles();
    }
}
