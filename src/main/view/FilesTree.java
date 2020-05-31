package main.view;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

public class FilesTree extends JTree {
    class DefaultFileNodes extends DefaultMutableTreeNode{
        public DefaultFileNodes(){
            // 内部类 默认结点为系统盘
            for(File file : File.listRoots()){
                add(new DefaultMutableTreeNode(file));
            }
        }
    }

    /**
     * 默认结点
     */
    public FilesTree(){
        setModel(new DefaultTreeModel(new DefaultFileNodes()));
    }

    /**
     * 得到当前的文件路径
     * @return file
     */
    public File getCurrentFile(){
        DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
        File file = (File) defaultMutableTreeNode.getUserObject();
        return file;
    }

    /**
     * 更新视图, 加载文件
     * @param node 加载的结点
     * @param files 要加载在结点下的文件
     */
    public void loadingTree(DefaultMutableTreeNode node, File[] files) {
        File file = (File) node.getUserObject(); // 得到文件
        // 获得用户选择的节点
        if (file.isDirectory()) {// 如果File类型对象是文件夹
            if(files == null) return;
            for (File f : files) {// 将符合条件的File类型对象增加到用户选择的节点中
                if(f.isDirectory()){
                    node.add(new DefaultMutableTreeNode(f));
                }
            }
        } else{
            return;
        }
    }

    /**
     * 做测试
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("File tree");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        JScrollPane jsp = new JScrollPane(new FilesTree());
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
        frame.add(jsp, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
