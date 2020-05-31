package main.view;

import main.controller.FileController;
import main.model.FileModel;
import main.model.FileTreeModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * App 启动类
 */
public class App extends JFrame{
    private JScrollPane jspList; // 文件列表的滑动块
    private JScrollPane jspTree; // 文件树的滑动块
    private JButton undoButton; // 后退
    private JButton redoButton; // 前进
    private FileDisplay fileDisplay; //
    private JToolBar toolBar; // 工具栏
    private JTextField urlBar; // 地址栏
    private FilesTree filesTree; // 文件树
    private JButton goButton; // 跳转
    public FileDisplay getFileDisplay() {
        return fileDisplay;
    }

    public App(){
        init();
    }

    /**
     * 得到url地址栏信息
     * @return url
     */
    public String getUrlInfo() {
        return urlBar.getText();
    }

    /**
     * 初始化
     */
    public void init(){
        setTitle("file manager");
        setSize(500, 400);
        setLocationRelativeTo(null);

        // toolbar
        toolBar = new JToolBar();
        toolBar.setAlignmentY(10.5F);
        toolBar.setBorder(null);
        toolBar.setFloatable(false);
        toolBar.setEnabled(false);
        toolBar.setFocusCycleRoot(true);
        toolBar.addSeparator(new Dimension(30, 10));
        add(toolBar, BorderLayout.NORTH);
        // undoButton 后退按钮
        undoButton = new JButton();
        undoButton.setText("undo");
        undoButton.setBorderPainted(false);
        undoButton.setBackground(new Color(203, 205, 205));
        toolBar.add(undoButton);

        // redoButton 前进按钮
        redoButton = new JButton();
        redoButton.setText("redo");
        redoButton.setBorderPainted(false);
        redoButton.setBackground(new Color(148, 152, 154));

        toolBar.add(redoButton);
        toolBar.addSeparator(new Dimension(20, 10));

        // 文件 List 滑动块
        fileDisplay = new FileDisplay();
        jspList = new JScrollPane(fileDisplay);
        jspList.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(jspList, BorderLayout.CENTER);

        // url bar
        urlBar = new JTextField();
        toolBar.add(urlBar);

        // goButton 跳转按钮
        goButton = new JButton();
        goButton.setText("go");
        goButton.setBorderPainted(false);
        goButton.setBackground(new Color(203, 205, 205));
        toolBar.add(goButton);


        // filesTree 文件树
        filesTree = new FilesTree();
        jspTree = new JScrollPane(filesTree);
        jspTree.setPreferredSize(new Dimension(200, 0));
        add(jspTree, BorderLayout.WEST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 更新URL地址栏
     * @param info 地址栏信息
     */
    public void updateUrl(String info){
        urlBar.setText(info);
    }

    /**
     * 添加undo 监听
     * @param undo undoListener
     */
    public void addUndoListener(ActionListener undo){
        undoButton.addActionListener(undo);
    }

    /**
     * 添加redo监听
     * @param redo redoListener
     */
    public void addRedoListener(ActionListener redo){
        redoButton.addActionListener(redo);
    }

    /**
     * 添加go监听
     * @param go goListener
     */
    public void addGoListener(ActionListener go){goButton.addActionListener(go);}

    /**
     * 得到内置model
     * @return
     */
    public FilesTree getFilesTree() {
        return filesTree;
    }

    /**
     * 启动入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        App app = new App(); // main.view
        FileModel fileModel = new FileModel(); // file main.model
        FileTreeModel treeModel = new FileTreeModel(); // file tree main.model
        FileController fileController = new FileController(app, fileModel, treeModel); // file main.controller
        app.setVisible(true); // 可见
    }


}
