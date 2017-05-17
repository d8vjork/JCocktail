package primary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

class Project {

	private String title;
	private String path;
	private ArrayList<File> nodeList;
	private JTree tree;
	private ArrayList<Tab> tabs;
	
	private final ImageIcon foldersIcon = new ImageIcon(getClass().getResource("icons/folders.png"));
	private final ImageIcon folderIcon = new ImageIcon(getClass().getResource("icons/folder.png"));
	
	Project() {
		this.tree = new JTree();
		// Tabs initialization
		this.tabs = new ArrayList();
		this.tabs.add(new Tab("Nuevo"));
	}
	
	Project(String title, String path) {
		this.title = title;
		this.path = path;
		// Project tree initialization
		this.tree = new JTree();
		this.setFiles();
		this.constructTree();
		// Project tabs initialization
		this.tabs = new ArrayList();
		this.tabs.add(new Tab("Nuevo"));
	}
	
	public JTree getTree() {
		return this.tree;
	}
	
	public void setFiles() {
		if (this.path != null) {
			this.nodeList = new ArrayList(Arrays.asList(new File(this.path).listFiles()));
		}
	}
	
	public ArrayList<Tab> getTabs() {
		return this.tabs;
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
		if (this.path != null) {
			try {
				FileOutputStream fileOut = new FileOutputStream(path + File.separator + ".project");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				
				// Serialize object and save it
				out.writeObject(this);
				
				out.flush();
				out.close();
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Open if project file exists
	public void openProject(String path) {
		try {
			FileInputStream fileIn = new FileInputStream(path + File.separator + ".project");
			ObjectInputStream in = new ObjectInputStream(fileIn);
				
			// Serialize object and save it
			this.tabs = (ArrayList) in.readObject();
				
			in.close();
			fileIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
		}
	}
}
