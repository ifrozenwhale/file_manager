package main.Util;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * 创建文件图标工具类
 * 得到系统图标
 */
public class FileIconCreate {
    public static Icon getSmallIcon(File f ) {
        if ( f != null && f.exists() ) {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            return(fsv.getSystemIcon( f ) );
        }
        return(null);
    }
}
