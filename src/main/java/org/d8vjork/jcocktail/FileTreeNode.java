package org.d8vjork.jcocktail;

import java.io.File;

import javax.swing.tree.TreeNode;

class FileTreeNode extends TreeNode {

    /**
     * Node file.
     */
    private File file;

    /**
     * Children of the node file.
     */
    private File[] children;

    /**
     * Parent node.
     */
    private TreeNode parent;

    /**
     * Indication whether this node corresponds to a file system root.
     */
    private boolean isFileSystemRoot;

    public File getFile() {
        return file;
    }
}
