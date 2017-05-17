package primary;

import java.awt.FileDialog;
import java.awt.event.WindowAdapter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	
    Tab curTab;
    
    private final String app_version = "v0.8.3-beta";

    public Principal() {
        initComponents();
        curTab = project.getTabs().get(tabs.getSelectedIndex());
    }

    private void initComponents() {

    	project = new Project();
    	jScrollPane1 = new JScrollPane();
        tabs = new JTabbedPane();
        jmMenuBar = new JMenuBar();
        jmFiles = new JMenu();
        jmOpenFile = new JMenuItem();
        jmOpenFolder = new JMenuItem();
        jmSaveFile = new JMenuItem();
        jmSaveAsFile = new JMenuItem();
        jmEditor = new JMenu();
        jmDebug = new JMenu();
        jmNewTab = new JMenuItem();
        jmCloseTab = new JMenuItem();
        jmAbout = new JMenu();
        filler1 = new Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        toolbar = new JToolBar();
        codeLang = new JLabel();
        fileHash = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        // Execute on close window
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                project.saveProject();
            	System.exit(0);
            }
        });
        
        this.setFocusable(true);
        
        setTitle("Cocktail");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(new ImageIcon(getClass().getResource("app-logo.png")).getImage());
        
        Tab newTab = new Tab("Nuevo");
        project.getTabs().add(newTab);
        tabs.addTab(newTab.getTitle(), newTab.getPane());
        
        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsStateChanged(evt);
            }
        });
        
        /*curPro.getTree().setExpandsSelectedPaths(false);
        curPro.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        curPro.getTree().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
            	jFileTreeMouseClicked(evt);
            }
        });*/

        jmFiles.setText("Archivo");

        jmOpenFile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jmOpenFile.setText("Abrir...");
        jmOpenFile.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmOpenFileActionPerformed(evt);
            }
        });
        jmFiles.add(jmOpenFile);

        jmOpenFolder.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        jmOpenFolder.setText("Abrir carpeta...");
        jmOpenFolder.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmOpenFolderActionPerformed(evt);
            }
        });
        jmFiles.add(jmOpenFolder);
        
        jmSaveFile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jmSaveFile.setText("Guardar...");
        jmSaveFile.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSaveFileActionPerformed(evt);
            }
        });
        jmFiles.add(jmSaveFile);
        
        jmSaveAsFile.setText("Guardar como...");
        jmSaveAsFile.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSaveFileAsActionPerformed(evt);
            }
        });
        jmFiles.add(jmSaveAsFile);

        jmMenuBar.add(jmFiles);

        jmEditor.setText("Editar");

        jmNewTab.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jmNewTab.setText("Nueva pestaña");
        jmNewTab.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmNewTabActionPerformed(evt);
            }
        });
        jmEditor.add(jmNewTab);

        jmCloseTab.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jmCloseTab.setText("Cerrar pestaña");
        jmCloseTab.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmCloseTabActionPerformed(evt);
            }
        });
        jmEditor.add(jmCloseTab);

        jmMenuBar.add(jmEditor);

        jmAbout.setText("Acerca");
        jmAbout.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
                jmAboutMouseClicked(evt);
            }
        });
        jmMenuBar.add(jmAbout);
        
        jmDebug.setText("Debug");
        jmDebug.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
            	debugKeyPressed(evt);
            }
        });
        jmMenuBar.add(jmDebug);

        setJMenuBar(jmMenuBar);
        
        toolbar.setBackground(new java.awt.Color(255, 255, 255));
        toolbar.setBorder(BorderFactory.createEtchedBorder());
        toolbar.setFloatable(false);
        
        toolbar.add(fileHash);
        toolbar.add(filler1);
        
        codeLang.setText("text/plain");
        toolbar.add(codeLang);
        
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(toolbar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(tabs, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                    .addComponent(tabs))
                .addGap(0, 0, 0)
                .addComponent(toolbar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {
    	// getSelectedIndex() because it change index
    	fileHash.setText(project.getTabs().get(tabs.getSelectedIndex()).getChecksum());
    	codeLang.setText(project.getTabs().get(tabs.getSelectedIndex()).getCodeLang());
    	System.out.println("tabs=" + tabs.getSelectedIndex() + ", project=" + project.getTabs().size());
    }
    
    private void jFileTreeMouseClicked(java.awt.event.MouseEvent evt) {
    	Object that = project.getTree().getLastSelectedPathComponent();
    	DefaultMutableTreeNode selected = (DefaultMutableTreeNode) project.getTree().getSelectionPath().getLastPathComponent();
    	//if ()
    	//this.openNewTab(that.toString());
    	System.out.println(selected.getUserObject());
    }

    private void jmSaveFileActionPerformed(java.awt.event.ActionEvent evt) {
    	int check = -1;
    	
    	if (curTab.getFile() != null) {
    		check = searchFileNamed(curTab.getFile().getName(), project.getTabs());
    	}
    	
    	if (check != -1) {
    		try {
				IO.writeFile(curTab.getFile().getAbsolutePath()).write(curTab.getTextarea().getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else {
	        FileDialog fd = new FileDialog(this, "Guardar...", FileDialog.SAVE);
	        fd.setVisible(true);
	        if (fd.getFile() != null) {
	        	String path = fd.getDirectory() + fd.getFile();
	        	int index = tabs.getSelectedIndex();
	        	
	        	// Set filename as tab title and path as tooltip
	            tabs.setTitleAt(index, fd.getFile());
	            tabs.setToolTipTextAt(index, path);
	            
	            // Save file to path
	            try {
					IO.writeFile(path).write(curTab.getTextarea().getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            // Update file to currentTab
	            curTab.setFile(new File(path));
	            
	            // Set hash and code syntax language
	            fileHash.setText(curTab.getChecksum());
	            codeLang.setText(curTab.getCodeLang());
	        }
    	}
    }
    
    private void debugKeyPressed(java.awt.event.MouseEvent evt) {
    	// Print Debug info
    	//System.out.flush();
    	for (int i = 0; i < project.getTabs().size(); i++) {
			Tab cur = project.getTabs().get(i);
			String filename = "null";
			
			if (cur.getFile() != null) {
				filename = cur.getFile().getName();
			}
			
			System.out.println("title=" + cur.getTitle() + " filename=" + filename + " mime=" + cur.getCodeLang() + " checksum=" + cur.getChecksum());
		}
    }
    
    private void jmSaveFileAsActionPerformed(java.awt.event.ActionEvent evt) {
    	FileDialog fd = new FileDialog(this, "Guardar como...", FileDialog.SAVE);
        fd.setVisible(true);
        
        if (fd.getFile() != null) {
        	String path = fd.getDirectory() + fd.getFile();
        	int index = tabs.getSelectedIndex();
        	
            // Set new filename as tab title and new path as tooltip
            tabs.setTitleAt(index, fd.getFile());
            tabs.setToolTipTextAt(index, path);
            
            // Save file to path
            try {
				IO.writeFile(path).write(curTab.getTextarea().getText());;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            // Update file to currentTab
            curTab.setFile(new File(path));
            
            // Set hash and code syntax language
            fileHash.setText(curTab.getChecksum());
            codeLang.setText(curTab.getCodeLang());
        }
    }
    
    private int searchFileNamed(String name, ArrayList<Tab> tabs) {
    	int r = -1;
    	
    	for (int i = 0; i <= tabs.size()-1; i++) {
    		if (tabs.get(i).getFile() != null) {
	    		if (tabs.get(i).getFile().getName().toString() == name) {
	    			r = i;
	    			break;
	    		}
    		}
    	}
    	
    	return r;
    }

    private void jmOpenFileActionPerformed(java.awt.event.ActionEvent evt) {
    	FileDialog fd = new FileDialog(this, "Abrir...", FileDialog.LOAD);
        fd.setVisible(true);
    	
        if (fd.getFile() != null) {
        	int check = searchFileNamed(fd.getFile(), project.getTabs());
        	System.out.println("getFile()=" + fd.getFile() + " check=" + check);
	        if (check != -1) {
	        	// Dont reopen same file again
	        	tabs.setSelectedIndex(check);
	        	System.out.println("Noup! check=" + check);
	    	} else {
	    		String path = fd.getDirectory() + fd.getFile();
	    		Tab newTab = new Tab(fd.getFile(), new File(path));
	    		int index = tabs.getSelectedIndex();
	    		BufferedReader br;
	    		
	    		// Add to tablist
	    		project.getTabs().remove(index);
	    		project.getTabs().add(index, newTab);
	    		tabs.setIconAt(index, new ImageIcon(getClass().getResource("icons/" + project.getTabs().get(index).getFileExt() + ".png")));
	    		//System.out.println("icons/" + tabList.get(index).getFileExt() + ".png");
	    		
	    		// Read file and fill textarea
	    		try {
	    			br = IO.readFile(path);
	    			String line;
	    			
	    			while ((line = br.readLine()) != null) {
	    				//tabList.get(index).getTextarea().append(line + "\n");
	                	curTab.getTextarea().append(line + "\n");
	                }
	    			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	            // Set filename as tab title and path as tooltip
	            tabs.setTitleAt(index, fd.getFile());
	            tabs.setToolTipTextAt(index, path);
	            
	            // MD5 Hash
	            fileHash.setText(newTab.getChecksum());
	            codeLang.setText(newTab.getCodeLang());
	        }
    	}
    }
    
    private void jmOpenFolderActionPerformed(java.awt.event.ActionEvent evt) {
    	JFileChooser fc = new JFileChooser();
    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int r = fc.showOpenDialog(this);
    	
        if (r == JFileChooser.APPROVE_OPTION) {
        	if (new File(fc.getSelectedFile().getAbsolutePath() + File.separator + ".project").exists() == true) {
        		project.openProject(fc.getSelectedFile().getAbsolutePath());
        	}
        	
        	project = new Project(fc.getSelectedFile().getParent(), fc.getSelectedFile().getAbsolutePath());
        	jScrollPane1.setViewportView(project.getTree());
        }
    }

    private void jmAboutMouseClicked(java.awt.event.MouseEvent evt) {
        JOptionPane.showMessageDialog(this, "Cocktail Editor " + app_version, "Acerca de", JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("app-logo.png")));
    }

    private void jmNewTabActionPerformed(java.awt.event.ActionEvent evt) {
    	this.openNewTab(null);
    }
    
    private void openNewTab(String title) {
    	// Check if haven't got any open tab
    	if (tabs.getTabCount() == 0) {
    		// Create tabs pane
    	} else {
    		Tab newTab;
    		
    		// If title was given or not
    		if (title == null) {
    			newTab = new Tab("Nuevo " + project.getTabs().size());
    		} else {
    			newTab = new Tab(title);
    		}
    		
	        // Add tab to ArrayList
    		project.getTabs().add(newTab);
	        tabs.addTab(newTab.getTitle(), newTab.getPane());
	        
	        // Select this tab
	        //tabs.getComponentAt(tabs.getSelectedIndex());
	        tabs.setSelectedComponent(newTab.getPane());
    	}
    }

    private void jmCloseTabActionPerformed(java.awt.event.ActionEvent evt) {
    	int index = tabs.getSelectedIndex();
    	
        if (index == 0) {
        	// Reset current tab textarea
        	curTab.setTitle("Nuevo");
            // Reset tab title filename and icon
            tabs.setTitleAt(index, "Nuevo");
            tabs.setIconAt(index, null);
            //tabList.remove(index);
        } else {
        	tabs.remove(index);
        	// Remove from ArrayList
        	project.getTabs().remove(index);
        }
    }
    
    //tabs.setIconAt(tabs.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/other.png")));

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }
    
    private Project project;
    private JScrollPane jScrollPane1;
    private Box.Filler filler1;
    private JMenu jmAbout, jmFiles, jmEditor, jmDebug;
    private JMenuBar jmMenuBar;
    private JMenuItem jmOpenFile, jmOpenFolder, jmCloseTab, jmNewTab, jmSaveFile, jmSaveAsFile;
    private JTabbedPane tabs;
    private JPopupMenu.Separator jmSeparator;
    private JToolBar toolbar;
    private JLabel codeLang, fileHash;
}
