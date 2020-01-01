package org.d8vjork.jcocktail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang3.StringUtils;

class Project implements Serializable {

    /**
     * Serial unique version for this class.
     */
    private static final long serialVersionUID = 935354289723687528L;

    private String title;
    private String path;
    private ArrayList<File> nodeList;
    public JTree tree;
    private ArrayList<Tab> tabs;

    // private final ImageIcon folderIcon = new ImageIcon(App.getPathFromResources("icons/folder.png"));

    Project(final App parent) {
        tree = new JTree();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(final TreeSelectionEvent e) {
                parent.projectTreeNodeFileClicked(e);
            }
        });

        this.tree = tree;

        // Tabs initialization
        this.tabs = new ArrayList<Tab>();
        this.tabs.add(new Tab("New"));
    }

    Project(final App parent, final String title, final String path) {
        this(parent);

        this.title = title;
        this.path = path;
        this.setFiles();
        this.constructTree();
    }

    public JTree getTree() {
        return this.tree;
    }

    public void setFiles() {
        if (this.path != null) {
            this.nodeList = new ArrayList<File>(Arrays.asList(new File(this.path).listFiles()));
        }
    }

    public ArrayList<Tab> getTabs() {
        return this.tabs;
    }

    private void constructTree() {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.title);
        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        treeModel.

        this.retrieveChildren(root);

        this.tree.setModel(new DefaultTreeModel(root).addTreeModelListener(l););
    }

    private void retrieveChildren(final DefaultMutableTreeNode rootTree) {
        if (!this.nodeList.isEmpty()) {
            DefaultMutableTreeNode childTreeNode;

            for (final File file : nodeList) {
                if (file.isDirectory()) {
                    getFilesFromDirectory(file, rootTree);
                } else {
                    childTreeNode = new DefaultMutableTreeNode(file.getName());
                    rootTree.add(childTreeNode);
                }
            }
        }
    }

    private void getFilesFromDirectory(final File folder, final DefaultMutableTreeNode parentTree) {
        final ArrayList<File> fileArrList = new ArrayList<File>(Arrays.asList(folder.listFiles()));
        final DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(folder.getName());

        for (final File file : fileArrList) {
            if (file.isDirectory()) {
                getFilesFromDirectory(file, childTreeNode);
            }

            final DefaultMutableTreeNode childTreeNodeItem = new DefaultMutableTreeNode(file.getName());
            childTreeNode.add(childTreeNodeItem);
        }

        parentTree.add(childTreeNode);
    }

    // Save actual project
    public void saveProject() {
        if (this.path != null) {
            try {
                final FileOutputStream fileOut = new FileOutputStream(path + File.separator + ".project");
                final ObjectOutputStream out = new ObjectOutputStream(fileOut);

                // Serialize object and save it
                out.writeObject(this);

                out.flush();
                out.close();
                fileOut.close();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // Open if project file exists
    public void openProject(final String path) {
        try {
            final FileInputStream fileIn = new FileInputStream(path + File.separator + ".project");
            final ObjectInputStream in = new ObjectInputStream(fileIn);

            // Serialize object and save it
            this.tabs = (ArrayList<Tab>) in.readObject();

            in.close();
            fileIn.close();
        } catch (final IOException e) {
            // TODO: Auto-generated catch block
            e.printStackTrace();
        } catch (final ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
    }
}
