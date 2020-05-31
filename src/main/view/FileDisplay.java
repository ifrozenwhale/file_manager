package main.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.List;

public class FileDisplay extends JList {

    private JPopupMenu filePopupMenu; // 文件菜单
    private JPopupMenu dictPopupMenu; // 目录菜单
    private JMenuItem copyItem; // 拷贝菜单项
    private JMenuItem encodeItem; // 加密菜单项
    private JMenuItem decodeItem; // 解密菜单项
    private JMenuItem zipItem; // 压缩菜单项
    private JMenuItem unzipItem; // 解压菜单项
    private JMenuItem createItem; // 创建文件夹菜单项
    private JMenuItem pasteItem; // 粘贴菜单项
    private JMenuItem deleteItem; // 删除菜单项

    /**
     * 更新视图
     * @param files 文件列表
     */
    public void updateView(List<File> files){
        DefaultListModel<File> defaultListModel = new DefaultListModel<>();
        for(File file : files){
            defaultListModel.addElement(file);
        }
        setModel(defaultListModel);
    }

    @Override
    public int locationToIndex(Point location) {
        int index = super.locationToIndex(location);
        if (index != -1 && !getCellBounds(index, index).contains(location)) {
            return -1;
        } else {
            return index;
        }
    }

    /**
     * 初始化UI,全部字体默认fnt
     * @param fnt 字体
     */
    private void initGlobalFontSetting(Font fnt){
        FontUIResource fontRes = new FontUIResource(fnt);
        for(Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if(value instanceof FontUIResource)
                UIManager.put(key, fontRes);
        }
    }

    /**
     * 构造函数
     */
    public FileDisplay() {
        /**
         * 初始化UI, 采用windows10样式
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Font f = new Font("微软雅黑", Font.PLAIN,12);
        initGlobalFontSetting(f);

        // pop menu
        filePopupMenu = new JPopupMenu();
        dictPopupMenu = new JPopupMenu();
        copyItem = new JMenuItem();
        encodeItem = new JMenuItem();
        decodeItem = new JMenuItem();
        zipItem = new JMenuItem();
        unzipItem = new JMenuItem();
        createItem = new JMenuItem();
        pasteItem = new JMenuItem();
        deleteItem = new JMenuItem();

        filePopupMenu.setBorderPainted(false);
        //---- styleMenuitem ----
        setMenuItemStyle(copyItem, "copy");
        filePopupMenu.add(copyItem);

        setMenuItemStyle(encodeItem, "encode");
        filePopupMenu.add(encodeItem);

        setMenuItemStyle(decodeItem, "decode");
        filePopupMenu.add(decodeItem);

        setMenuItemStyle(zipItem, "zip");
        filePopupMenu.add(zipItem);

        setMenuItemStyle(unzipItem, "unzip");
        filePopupMenu.add(unzipItem);

        setMenuItemStyle(createItem, "create");
        dictPopupMenu.add(createItem);

        dictPopupMenu.setBorderPainted(false);
        setMenuItemStyle(pasteItem, "paste");
        dictPopupMenu.add(pasteItem);

        setMenuItemStyle(deleteItem, "delete");
        filePopupMenu.add(deleteItem);
        // 渲染样式
        setCellRenderer(new FileListCellRenderer());
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    /**
     * 添加监听
     * @param copy copyListener
     */
    public void addCopyListener(ActionListener copy){
        copyItem.addActionListener(copy);
    }
    public void addPasteListener(ActionListener paste){
        pasteItem.addActionListener(paste);
    }
    public void addCreateListener(ActionListener create){
        createItem.addActionListener(create);
    }
    public void addZipListener(ActionListener zip){
        zipItem.addActionListener(zip);
    }
    public void addUnzipListener(ActionListener unzip){
        unzipItem.addActionListener(unzip);
    }
    public void addDeleteListener(ActionListener del){
        deleteItem.addActionListener(del);
    }
    public void addMyMouseListener(MouseAdapter mouseAdapter){
        addMouseListener(mouseAdapter);
    }
    public void showFileMenu(MouseEvent e){
        filePopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }
    public void showDicMenu(MouseEvent e){
        dictPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    public void addEncodeListener(ActionListener encode) {encodeItem.addActionListener(encode);}
    public void addDecodeListener(ActionListener decode){decodeItem.addActionListener(decode);}
    private void setMenuItemStyle(JMenuItem styleMenuitem, String text){
        styleMenuitem.setText(text);
        styleMenuitem.setBorderPainted(false);
    }

    /**
     * 启动入口
     * @param args 命令参数,测试用
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("File tree");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        final JScrollPane jsp = new JScrollPane(new FileDisplay());
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
        frame.add(jsp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}
