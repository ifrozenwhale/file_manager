package main.view;

import main.Util.FileIconCreate;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;

/**
 * 继承默认样式, 自定义
 */
public class FileListCellRenderer extends DefaultListCellRenderer {
    Border lineBorder = BorderFactory.createLineBorder(new Color(0, 0, 0, 0),1, true);

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        File file = (File) value;
        setBorder(lineBorder);
        Icon ico = FileIconCreate.getSmallIcon(file);
        setIcon(ico);
        if(file.getName().equals("")){
            setText(file.getPath()); // 根路径
        }else{
            setText(file.getName()); // 非根路径
        }
        // 保存原始的颜色
        int color = getBackground().getRGB();
        if(isSelected){
            setBackground(new Color(19, 175, 137));
            setForeground(Color.white);
        }else{
            setBackground(Color.white);
            setForeground(Color.black);
        }
        return this;
    }
}
