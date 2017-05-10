package primary;

import java.awt.FileDialog;
import javax.swing.*;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rtextarea.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	
	File f;
    FileReader fr;
    BufferedReader br;
    FileWriter fw;
    BufferedWriter bw;
    ArrayList<Tabs> tabList;
    Tabs curTab;
    
    private final String app_version = "v0.8.2-beta";

    public Principal() {
        tabList = new ArrayList();
        initComponents();
        curTab = tabList.get(tabs.getSelectedIndex());
    }

    private void initComponents() {

        tabs = new JTabbedPane();
        jmMenuBar = new JMenuBar();
        jmFiles = new JMenu();
        jmOpenFile = new JMenuItem();
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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        this.setFocusable(true);
        
        setTitle("Cocktail");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(new ImageIcon(getClass().getResource("app-logo.png")).getImage());
        
        Tabs newTab = new Tabs("Nuevo");
        tabList.add(newTab);
        tabs.addTab(newTab.getTitle(), newTab.getPane());
        
        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsStateChanged(evt);
            }
        });

        jmFiles.setText("Archivo");

        jmOpenFile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jmOpenFile.setText("Abrir...");
        jmOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmOpenFileActionPerformed(evt);
            }
        });
        jmFiles.add(jmOpenFile);

        jmSaveFile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jmSaveFile.setText("Guardar...");
        jmSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSaveFileActionPerformed(evt);
            }
        });
        jmFiles.add(jmSaveFile);
        
        jmSaveAsFile.setText("Guardar como...");
        jmSaveAsFile.addActionListener(new java.awt.event.ActionListener() {
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
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmNewTabActionPerformed(evt);
            }
        });
        jmEditor.add(jmNewTab);

        jmCloseTab.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jmCloseTab.setText("Cerrar pestaña");
        jmCloseTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmCloseTabActionPerformed(evt);
            }
        });
        jmEditor.add(jmCloseTab);

        jmMenuBar.add(jmEditor);

        jmAbout.setText("Acerca");
        jmAbout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jmAboutMouseClicked(evt);
            }
        });
        jmMenuBar.add(jmAbout);
        
        jmDebug.setText("Debug");
        //jmDebug.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jmDebug.addMouseListener(new java.awt.event.MouseAdapter() {
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
        toolbar.add(codeLang);
        
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(toolbar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabs, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tabs, GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(toolbar, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
        );
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {
    	fileHash.setText(tabList.get(tabs.getSelectedIndex()).getChecksum());
    	codeLang.setText(tabList.get(tabs.getSelectedIndex()).getCodeLang());
    }

    private void jmSaveFileActionPerformed(java.awt.event.ActionEvent evt) {
    	int check = -1;
    	
    	if (curTab.getFile() != null) {
    		check = searchFileNamed(curTab.getFile().getName(), tabList);
    	}
    	
    	if (check != -1) {
    		guardaFichero(curTab.getFile().getAbsolutePath());
    	} else {
	        FileDialog fd = new FileDialog(this, "Guardar fichero", FileDialog.SAVE);
	        fd.setVisible(true);
	        if (fd.getFile() != null) {
	        	String path = fd.getDirectory() + fd.getFile();
	        	int index = tabs.getSelectedIndex();
	        	
	        	// Set filename as tab title and path as tooltip
	            tabs.setTitleAt(index, fd.getFile());
	            tabs.setToolTipTextAt(index, path);
	            
	            // Save file to path
	            guardaFichero(path);
	            
	            // Update file to currentTab
	            curTab.setFile(new File(path));
	            
	            // Set hash and lang
	            fileHash.setText(curTab.getChecksum());
	            codeLang.setText(curTab.getCodeLang());
	        }
    	}
    }
    
    private void debugKeyPressed(java.awt.event.MouseEvent evt) {
    	// Print Debug info
    	System.out.flush();
    	for (int i = 0; i < tabList.size(); i++) {
			Tabs cur = tabList.get(i);
			String filename = "null";
			
			if (cur.getFile() != null) {
				filename = cur.getFile().getName();
			}
			
			System.out.println("title=" + cur.getTitle() + " filename=" + filename + " mime=" + cur.getCodeLang() + " checksum=" + cur.getChecksum());
		}
    }
    
    private void jmSaveFileAsActionPerformed(java.awt.event.ActionEvent evt) {
    	FileDialog fd = new FileDialog(this, "Guardar fichero como", FileDialog.SAVE);
        fd.setVisible(true);
        
        if (fd.getFile() != null) {
        	String path = fd.getDirectory() + fd.getFile();
        	int index = tabs.getSelectedIndex();
        	
            // Set new filename as tab title and new path as tooltip
            tabs.setTitleAt(index, fd.getFile());
            tabs.setToolTipTextAt(index, path);
            
            // Save file to path
            guardaFichero(path);
            
            // Update file to currentTab
            curTab.setFile(new File(path));
            
            // Set hash and lang
            fileHash.setText(curTab.getChecksum());
            codeLang.setText(curTab.getCodeLang());
        }
    }
    
    private int searchFileNamed(String name, ArrayList<Tabs> tabs) {
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
    	FileDialog fd = new FileDialog(this, "Abrir fichero", FileDialog.LOAD);
        fd.setVisible(true);
    	
        if (fd.getFile() != null) {
        	int check = searchFileNamed(fd.getFile(), tabList);
        	System.out.println("getFile()=" + fd.getFile() + " check=" + check);
	        if (check != -1) {
	        	// Dont reopen same file again
	        	tabs.setSelectedIndex(check);
	        	System.out.println("Noup! check=" + check);
	    	} else {
	    		String path = fd.getDirectory() + fd.getFile();
	    		Tabs newTab = new Tabs(fd.getFile(), new File(path));
	    		int index = tabs.getSelectedIndex();
	    		
	    		// Add to tablist
	    		tabList.remove(index);
	    		tabList.add(index, newTab);
	    		
	            // Set filename as tab title and path as tooltip
	            tabs.setTitleAt(index, fd.getFile());
	            tabs.setToolTipTextAt(index, path);
	            
	            // MD5 Hash
	            fileHash.setText(newTab.getChecksum());
	            codeLang.setText(newTab.getCodeLang());
	        }
    	}
    }

    private void jmAboutMouseClicked(java.awt.event.MouseEvent evt) {
        JOptionPane.showMessageDialog(this, "Cocktail Editor " + app_version, "Acerca de", JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("app-logo.png")));
    }

    // Open new tab
    private void jmNewTabActionPerformed(java.awt.event.ActionEvent evt) {
        Tabs newTab = new Tabs("Nuevo " + tabList.size());
        // Add tab to ArrayList
        tabList.add(newTab);
        tabs.addTab(newTab.getTitle(), newTab.getPane());
        // Select this tab
        //tabs.getComponentAt(tabs.getSelectedIndex());
        tabs.setSelectedComponent(newTab.getPane());
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
	        tabList.remove(index);
        }
    }

    void cargaFichero(String ruta) {
        try {
            f = new File(ruta);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String linea;
            
            while ((linea = br.readLine()) != null) {
            	curTab.getTextarea().append(linea + "\n");
            }
            
            curTab.setPane();
            tabs.addTab(curTab.getTitle(), null, curTab.getPane());
            curTab.getTextarea().setEditable(false);
            //curTab.setPane();
            //tabs.getSelectedComponent().getParent().repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de lectura");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cerrar fichero");
            }
        }
    }

    void guardaFichero(String ruta) {
        try {
            f = new File(ruta);
            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);

            bw.write(tabList.get(tabs.getSelectedIndex()).getTextarea().getText());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de escritura");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cerrar fichero");
            }
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
    
    private Box.Filler filler1;
    private JMenu jmAbout, jmFiles, jmEditor, jmDebug;
    private JMenuBar jmMenuBar;
    private JMenuItem jmOpenFile, jmCloseTab, jmNewTab, jmSaveFile, jmSaveAsFile;
    private JTabbedPane tabs;
    private JPopupMenu.Separator jmSeparator;
    private JToolBar toolbar;
    private JLabel codeLang, fileHash;
}
