package main.controller;

import main.model.FileModel;
import main.view.FileDisplay;
import main.model.FileTreeModel;
import main.view.FilesTree;
import main.view.App;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileController {
    private FileDisplay fileDisplay; // 文件列表显示区
    private App app; // 主窗口UI
    private FileModel fileModel; // 文件数据
    private FilesTree filesTree; // 文件树
    private FileTreeModel treeModel; // 文件树数据
    public FileController(App app, FileModel fileModel, FileTreeModel treeModel) {
        // 初始化赋值
        this.fileDisplay = app.getFileDisplay();
        this.app = app;
        this.fileModel = fileModel;
        this.filesTree = app.getFilesTree();
        this.treeModel = treeModel;
        // 初始化添加事件监听
        fileDisplay.addCopyListener(new CopyListener());
        fileDisplay.addCreateListener(new CreateFileListener());
        fileDisplay.addPasteListener(new PasteListener());
        fileDisplay.addUnzipListener(new UnZipListener());
        fileDisplay.addZipListener(new ZipListener());
        fileDisplay.addDeleteListener(new DeleteListener());
        fileDisplay.addMyMouseListener(new FileClickListener());
        fileDisplay.addEncodeListener(new EncodeListener());
        fileDisplay.addDecodeListener(new DecodeListener());
        app.addUndoListener(new UndoListener());
        app.addRedoListener(new RedoListener());
        app.addGoListener(new GoListener());
        filesTree.addTreeSelectionListener(new FileTreeSelListener());
        filesTree.setRootVisible(false);
        // 初始化更新视图
        fileDisplay.updateView(fileModel.getDefaultListModel());

    }

    /**
     * copy 事件监听类
     */
    class CopyListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked")
			List<File> valuesList = fileDisplay.getSelectedValuesList();
            fileModel.addCopySources(valuesList); // 添加进剪切板
            fileDisplay.updateView(fileModel.getDefaultListModel()); // 更新视图
        }

    }

    /**
     * 压缩监听类
     */
    class ZipListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked")
			List<File> valuesList = fileDisplay.getSelectedValuesList();
            String name = JOptionPane.showInputDialog("input the name of zip");// 得到文件名
            fileModel.zipCompress(valuesList, name);// 压缩
            fileDisplay.updateView(fileModel.getDefaultListModel()); // 更新视图
        }
    }

    /**
     * 解压事件类
     */
    class UnZipListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked")
			List<File> fileList = fileDisplay.getSelectedValuesList();
            fileModel.zipUnCompress(fileList);
            fileDisplay.updateView(fileModel.getDefaultListModel());
        }
    }

    /**
     * 粘贴事件类
     */
    class PasteListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            fileModel.paste();
            fileDisplay.updateView(fileModel.getDefaultListModel());
        }
    }

    /**
     * 新建文件夹事件类
     */
    class CreateFileListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("please input the name of dict");
            if(name != null){
                fileModel.createFile(name);
            }
            fileDisplay.updateView(fileModel.getDefaultListModel());
        }
    }

    /**
     * 删除事件类
     */
    class DeleteListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked")
			List<File> fileList = fileDisplay.getSelectedValuesList();
            fileModel.delete(fileList);
            fileDisplay.updateView(fileModel.getDefaultListModel());
        }
    }

    /**
     * 加密事件类
     */
    class EncodeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked")
			List<File> files = (List<File>) fileDisplay.getSelectedValuesList();
            int key = Integer.parseInt(JOptionPane.showInputDialog("please input the key(int)"));
            fileModel.enCrypt(files, key);
            fileDisplay.updateView(fileModel.getDefaultListModel());
        }
    }

    /**
     * 解压事件类
     */
    class DecodeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            @SuppressWarnings("unchecked")
			List<File> files = (List<File>) fileDisplay.getSelectedValuesList();
            int key = Integer.parseInt(JOptionPane.showInputDialog("please input the key(int)"));
            fileModel.decrypt(files, key);
            fileDisplay.updateView(fileModel.getDefaultListModel());
            JOptionPane.showMessageDialog(null, "Please check it.\nIf it can't be opened, make sure key is right");
        }
    }

    /**
     * 文件点击事件类
     */
    class FileClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JList<?> list = (JList<?>) e.getSource();
            if (list.locationToIndex(e.getPoint()) == -1 && !e.isShiftDown()){
                list.clearSelection();
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3){ // 如果是右键
//                    这是右键选中
//                    int index = locationToIndex(e.getPoint());
//                    setSelectedIndex(index);
                if(!fileDisplay.getSelectedValuesList().isEmpty()){ // 在空白区域点击
                    fileDisplay.showFileMenu(e);
                }else{ // 选中文件点击
                    fileDisplay.showDicMenu(e);
                }
            }else if(e.getButton() == MouseEvent.BUTTON1 && !fileDisplay.getSelectedValuesList().isEmpty()) { // 左键单击选中给定的文件
                if (e.getClickCount() == 2) { // 双击
                    File file = (File) fileDisplay.getSelectedValue();
                    if(!fileModel.openFile(file)){
                        JOptionPane.showMessageDialog(null, "No application is associated with \nthe specified file for this operation");
                    }
                    fileDisplay.updateView(fileModel.getDefaultListModel());
                    String url = fileModel.getUrl();
                    app.updateUrl(url);
                }
            }
        }
    }

    /**
     * 撤回事件类
     */
    class UndoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            fileModel.undo();
            fileDisplay.updateView(fileModel.getDefaultListModel());
            String url = fileModel.getUrl();
            app.updateUrl(url);
        }
    }

    /**
     * 前进事件类
     */
    class RedoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            fileModel.redo();
            fileDisplay.updateView(fileModel.getDefaultListModel());
            String url = fileModel.getUrl();
            app.updateUrl(url);
        }
    }

    /**
     * 跳转事件类
     */
    class GoListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String url = app.getUrlInfo();
            File file = new File(url);
            fileModel.updateModels(file);
            fileDisplay.updateView(fileModel.getDefaultListModel());
        }
    }

    /**
     * 文件树选择事件类
     */
    class FileTreeSelListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            File file = filesTree.getCurrentFile();
            filesTree.loadingTree((DefaultMutableTreeNode) filesTree.getLastSelectedPathComponent(), treeModel.getFiles(file));
            fileModel.updateModels(file);
            fileDisplay.updateView(fileModel.getDefaultListModel());
            String url = fileModel.getUrl();
            app.updateUrl(url);
        }
    }
}
