package primary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

class Project {

	private String title;
	private String path;
	private ArrayList<File> nodeList;
	private JTree tree;
	private final ImageIcon foldersIcon = new ImageIcon(getClass().getResource("icons/folders.png"));
	private final ImageIcon folderIcon = new ImageIcon(getClass().getResource("icons/folder.png"));
	
	Project() {
		this.tree = new JTree();
	}
	
	Project(String title, String path) {
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
			this.nodeList = new ArrayList(Arrays.asList(new File(this.path).listFiles()));
		}
	}
	
	private void constructTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(this.title);
		this.retrieveChildren(top);
		this.tree = new JTree(top);
	}
	
	private void retrieveChildren(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode child;
		ArrayList<File> auxList;
		
		if (!this.nodeList.isEmpty()) {
			for (File file : nodeList) {
				if (file.isFile()) {
					System.out.println(file.getName());
					child = new DefaultMutableTreeNode(file.getName());
					top.add(child);
				} else if (file.isDirectory()) {
					System.out.println(file.getName());
					child = new DefaultMutableTreeNode(file.getName());
					top.add(child);
					auxList = new ArrayList(Arrays.asList(file.listFiles()));
					for (File sfile : auxList) {
						System.out.println("->" + sfile.getName());
						DefaultMutableTreeNode schild = new DefaultMutableTreeNode(sfile.getName());
						child.add(schild);
					}
				}
			}
		}
	}
	
	// Save actual project
	public void saveProject() {
		
	}
}
