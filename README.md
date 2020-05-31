# 设计思想

## `MVC`设计模式

本项目引入`mvc`设计模式。

MVC 模式代表 Model-View-Controller（模型-视图-控制器） 模式。这种模式用于应用程序的分层开发。

- **Model（模型）** - 模型代表一个存取数据的对象或 JAVA POJO。它也可以带有逻辑，在数据变化时更新控制器。
- **View（视图）** - 视图代表模型包含的数据的可视化。
- **Controller（控制器）** - 控制器作用于模型和视图上。它控制数据流向模型对象，并在数据变化时更新视图。它使视图与模型分离开。

`Java swing`的设计本身就是MVC设计的典范。在这里我们也采用MVC设计模式（尽管并不是那么贴合），与`swing`本身相结合。

对我们来说，**view**由`Java swing`负责显示，**model**是我们自己建立的数据结构，同时结合`swing`自身的**model**，所有操作的具体实现由**model**负责，即涉及到和系统文件层的交互等，**controller**由我们自行创建，负责添加事件监听并做出相应的逻辑操作，对界面的多个部分进行协同配合，完成视图的更新和数据的更新。

# 各模块功能实现

## 功能实现清单

1. **界面跳转：**
   1. 后退，逐级返回到父级目录。
   2. 前进，按照后退的顺序依次进入子目录。
   3. 当前目录路径显示，输入目录地址进行跳转。
2. **界面显示：**
   1. 文件目录树显示（只显示文件夹）。
   2. 文件列表显示（只显示当前目录下的文件夹和文件），带有系统图标。
   3. 点击文件树中的结点，在文件列表同步显示。
3. **选择与打开**
   1. 支持多选，选中文件高亮显示，点击空白区域取消选中。
   2. 单击文件夹进入，单击文件打开应用。
4. **右键选中文件**
   1. 批量复制，包括所有子目录和文件。
   2. 批量压缩，格式为zip，将多个文件压缩成一个，自定义命名。
   3. 单文件加密，自定义加密密钥。
5. **右键空白区域**
   1. 创建文件夹，自定义命名；
   2. 批量粘贴，粘贴的目录不能是源文件目录的子目录；
   3. 批量解压，解压到当前目录下。
   4. 单文件解密，输入加密密钥，若密钥不正确，加密后的文件仍无法打开。

## 类的设计

采用MVC设计模式。

### package view

1. #### **class App**

   1. 总的界面窗口UI，继承自`JFrame`类，采用Border-layout布局。

   2. 工具栏`toolbar`位于上方，实现后退、前进、当前文件路径显示和路径跳转。

   3. 左侧是文件树，中部是文件显示区域。

   4. 实现对应的`addListener`函数，如：

      ```java
          public void addUndoListener(ActionListener undo){
              undoButton.addActionListener(undo);
          }
          public void addRedoListener(ActionListener redo){
              redoButton.addActionListener(redo);
          }
      ```

2. #### **class FileDisplay**

   1. 文件主显示区，继承自`JList`，以文件列表的形式显示。
   2. 内嵌了`JPopMenu`，添加`JMenuItem`，用以实现功能。
   3. 实现`updateView`函数，更新视图。
   4. 实现对应的`addListener`函数，添加事件监听。

3. #### **class FilesTree**

   1. 文件目录树，继承自`JTree`。
   2. 实现了`loadingTree`函数，更新视图。
   3. 实现对应的`addListener`函数，添加事件监听。

4. #### **class FileListCellRenderer**

   1. 文件列表渲染类，继承自`DefaultListCellRenderer`。

### package model

1. #### **class FileModel**

   1. 文件实体类，具体实现了文件管理的各个功能。

2. #### **class FileTreeModel**

   1. 对应于文件树。

### package controller

1. #### **class FileController**

   1. 包含了

      ```java
         	private App app;    // 窗体UI
          private FileDisplay fileDisplay; // 为了方便调用，内部存储
          private FilesTree filesTree; // 为了方便调用，内部存储    
          private FileModel fileModel; // File Model
          private FileTreeModel treeModel; // Tree Model
      ```

   2. 初始化的时候，由`model`,`view`参数：

      ```java
        public FileController(App app, FileModel fileModel, FileTreeModel treeModel)
      ```

   3. 实现了对应的事件监听类接口，添加方法。

   4. 调用`model`和`view`实现。

### package util

1. #### **class FileCode** 文件加密工具类

2. #### **class FileCopy** 文件复制工具类

3. #### **class FileIconCreate**  系统图标工具类

4. #### **class FileZip** 文件压缩工具类



## 算法设计

主要是`FileModel`的实现

1. ### 数据记录

   1. 由`List<File> defaultListModel`和`File currentFile`负责记录当前目录和目录下的文件、文件夹。

2. ### **初始化**

   1. `initRoots`方法，将磁盘目录，如`C:\`，`D:\`，`E:\`添加进文件列表。

3. ### **文件更新**

   1. `updateModels(File file)`方法，将当前文件设为`file`，并将其当前目录层级下的文件（夹）添加进文件列表。

4. ### **前进和后退**

   1. 由`redoStack`负责记录操作路径，undo时，得到父级目录下文件和文件夹，更新文件列表，并将文件压入`redoStack`。
   2. redo时，弹出栈顶文件，更新当前文件列表。
   3. 细节，如果父级为`null`，说明当前是磁盘根目录，如`D:\`，则调用初始化方法`initRoots`。

5. ### **文件复制**

   1. 由`copySources`负责存储粘贴板中的文件。
   2. 选中文件后，加入到`copySources`中。
   3. 粘贴时，判断是文件，还是文件夹
      1. 如果是文件，则进行文件复制。
      2. 如果是文件夹，递归调用。
   4. 异常处理，粘贴的目录不能是源目录的子目录，由`isParent`进行判断。

6. ### **创建和删除**

   1. 如果不存在，则创建。
   2. 递归删除，由`deleteHelp`负责递归，如果删除的是文件夹，就递归调用，最终删除根文件。
   3. 对选中的文件遍历执行，批量删除。

7. ### **压缩和解压**

   1. 对于文件，直接压缩。
   2. 对于文件夹，递归压缩。

8. ### **文件加密和解密**

   1. 对文件读取字节内容，与密钥进行简单的异或加密。
   2. 加密后得到新文件，删除原始文件，并将新文件重命名为原文件。（也可以不删除，防止出问题，重要文件）
   3. 解密的时候，与加密过程相同，输入密钥，读取文件，异或解密。

9. ### **取消选中**

   1. 重写`locationToIndex`函数，判断点击的位置是否有效

      ```java
      int index = super.locationToIndex(location);        
      if (index != -1 && !getCellBounds(index, index).contains(location))	return -1;
      ```

10. ### **样式渲染**

    1. 实现`FileListCellRenderer`类继承`DefaultListCellRenderer`
    2. 设置图标，设置显示信息，磁盘根目录使用`getPath()`，其他目录使用`getName()`。
    3. 选中设置颜色，取消选中恢复之前的颜色。

11. ### **文件树展开**

    1. 实现`TreeSelectionListener`接口，采用延迟加载，即每次点击展开时再加载目录文件。

# 软件调试分析

## 执行过程

1. 启动`App`类的`main`函数

2. 在`FileController`初始化的时候，完成对数据成员的初始化，包括添加事件监听，更新初始视图。

   ```java
   public FileController(App app, FileModel fileModel, FileTreeModel treeModel) {
           this.fileDisplay = app.getFileDisplay();
           this.app = app;
           this.fileModel = fileModel;
           this.filesTree = app.getFilesTree();
           this.treeModel = treeModel;
           // 添加事件监听
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
           // 初始化主视图
           fileDisplay.updateView(fileModel.getDefaultListModel());
       }
   ```

3. 监听到事件发生，调用`model`和`view`进行逻辑实现并更新视图。例如，展开文件目录树时，需要对三部分视图进行更新（文件树，文件列表，url地址栏）

   ```java
       class FileTreeSelListener implements TreeSelectionListener {
           @Override
           public void valueChanged(TreeSelectionEvent e) {
               File file = filesTree.getCurrentFile(); // 得到点击的文件结点
               // 加载目录树
               filesTree.loadingTree((DefaultMutableTreeNode)
               	filesTree.getLastSelectedPathComponent(), treeModel.getFiles(file));
               // 更新文件数据模型
               fileModel.updateModels(file);
               // 更新文件列表视图
               fileDisplay.updateView(fileModel.getDefaultListModel());
               // 更新url地址显示
               String url = fileModel.getUrl();
               app.updateUrl(url);
           }
       }
   ```

   再如，undo操作时，`model`进行undo，更新数据，然后视图更新

   ```java
       class UndoListener implements ActionListener{
           @Override
           public void actionPerformed(ActionEvent e) {
               fileModel.undo(); // 数据模型更新
               fileDisplay.updateView(fileModel.getDefaultListModel()); // 列表视图更新
               String url = fileModel.getUrl(); 
               app.updateUrl(url); // url地址栏更新
           }
       }
   ```

## 界面展示



### 主界面

<img src="https://frozenwhale.oss-cn-beijing.aliyuncs.com/img/1588229853020.png" width=50%/>

- 上侧可以后撤、前进，url地址栏显示
- 左侧目录树展开，并同步更新右侧文件列表

### 文件及文件夹拷贝

<img src="https://frozenwhale.oss-cn-beijing.aliyuncs.com/img/1588229881679.png" width=50% />

右键粘贴，将子目录下所有文件（夹）粘贴

### 解压压缩

<img src="https://frozenwhale.oss-cn-beijing.aliyuncs.com/img/1588230177743.png" width=50%/>





###  文件加密

（以整数`int`类型为密钥，**加密需要等待一些时间**）

解密后，检查是否可以正常打开.

**注：**对文件夹进行加密是无效的

### 其他

见项目