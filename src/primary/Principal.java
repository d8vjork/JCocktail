package primary;

import java.awt.FileDialog;
import javax.swing.*;
//import org.fife.ui.autocomplete.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	
	File f;
    FileReader fr;
    BufferedReader br;
    FileWriter fw;
    BufferedWriter bw;
    ArrayList<RSyntaxTextArea> textareas;
    ArrayList<File> openedFiles;
    
    private final String app_version = "v0.6.1-beta";

    public Principal() {
        initComponents();
        textareas = new ArrayList();
        openedFiles = new ArrayList();
        textareas.add(rTextArea1);
    }

    private void initComponents() {

        tab1 = new JTabbedPane();
        jScrollPane1 = new JScrollPane();
        area = new JTextArea();
        jMenuBar1 = new JMenuBar();
        jmOpenFile = new JMenu();
        jMenuItem1 = new JMenuItem();
        jmSaveFile = new JMenuItem();
        jmNewTab = new JMenu();
        jMenuItem3 = new JMenuItem();
        jMenuItem2 = new JMenuItem();
        jMenu3 = new JMenu();
        rTextArea1 = new RSyntaxTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cocktail");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(new ImageIcon(getClass().getResource("app-logo.png")).getImage());
        
        rTextArea1.setColumns(20);
        //rTextArea1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        rTextArea1.setCodeFoldingEnabled(true);
        rTextArea1.setTemplatesEnabled(true);
        rTextArea1.setRows(5);
        jScrollPane1.setViewportView(rTextArea1);
        
        setEditorTheme(rTextArea1, "themes/monokai.xml");
        
        //area.setColumns(20);
        //area.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        //area.setRows(5);
        //jScrollPane1.setViewportView(area);
        
        tab1.addTab("Nuevo", jScrollPane1);

        jmOpenFile.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Abrir...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jmOpenFile.add(jMenuItem1);

        jmSaveFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jmSaveFile.setText("Guardar...");
        jmSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmSaveFileActionPerformed(evt);
            }
        });
        jmOpenFile.add(jmSaveFile);

        jMenuBar1.add(jmOpenFile);

        jmNewTab.setText("Editar");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Nueva pestaña");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jmNewTab.add(jMenuItem3);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Cerrar pestaña");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jmNewTab.add(jMenuItem2);

        jMenuBar1.add(jmNewTab);

        jMenu3.setText("Acerca");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tab1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }
    
    private void setEditorTheme(RSyntaxTextArea textarea, String tpl) {
    	try {
            Theme theme = Theme.load(getClass().getResourceAsStream(tpl));
            theme.apply(textarea);
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
    }

    private void jmSaveFileActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	if (searchStringIn(tab1.getTitleAt(tab1.getSelectedIndex()), openedFiles)) {
    		guardaFichero(openedFiles.get(tab1.getSelectedIndex()).getAbsolutePath());
    	} else {
	        FileDialog fd = new FileDialog(this, "Guardar fichero", FileDialog.SAVE);
	        fd.setVisible(true);
	        if (fd.getFile() != null) {
	            // Set filename as tab title
	            tab1.setTitleAt(tab1.getSelectedIndex(), fd.getFile());
	            // Set tab icon
	            detectaFormato(fd.getFile());
	            guardaFichero(fd.getDirectory() + "\\" + fd.getFile());
	            openedFiles.add(new File(fd.getDirectory() + "\\" + fd.getFile()));
	        }
    	}
    }
    
    private boolean searchStringIn(String name, ArrayList<File> list) {
    	for (File file : list) {
    		if (file.getName().equals(new String(name))) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
    	FileDialog fd = new FileDialog(this, "Abrir fichero", FileDialog.LOAD);
        fd.setVisible(true);
    	
        if (fd.getFile() != null) {
	        if (searchStringIn(fd.getFile(), openedFiles)) {
	    		// Dont reopen same file again
	    	} else {
	            // Reset current tab textarea
	            textareas.get(tab1.getSelectedIndex()).setText("");
	            // Set filename as tab title
	            tab1.setTitleAt(tab1.getSelectedIndex(), fd.getFile());
	            // Set tab icon
	            detectaFormato(fd.getFile());
	            cargaFichero(fd.getDirectory() + "\\" + fd.getFile());
	            openedFiles.add(new File(fd.getDirectory() + "\\" + fd.getFile()));
	        }
    	}
    }

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {
        // test
    }

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {
        JOptionPane.showMessageDialog(this, "Cocktail Editor " + app_version, "Acerca de", JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getClass().getResource("app-logo.png")));
    }

    // Open new tab
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        JScrollPane temp = new JScrollPane();
        RSyntaxTextArea textarea = new RSyntaxTextArea();
        
        tab1.add("Nuevo", temp);
        textarea.setColumns(20);
        textarea.setCloseCurlyBraces(true);
        textarea.setMarginLineEnabled(true);
        textarea.setCodeFoldingEnabled(true);
        
        setEditorTheme(textarea, "themes/monokai.xml");
        
        //textarea.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        textarea.setRows(5);

        // Add to list
        textareas.add(textarea);

        temp.setViewportView(textarea);
        tab1.getComponentAt(tab1.getSelectedIndex());
        tab1.setSelectedComponent(temp);
    }

    // Close current tab
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        if(tab1.getSelectedIndex() == 0) {
        	// Reset current tab textarea
            textareas.get(tab1.getSelectedIndex()).setText("");
            // Reset tab title filename and icon
            tab1.setTitleAt(tab1.getSelectedIndex(), "Nuevo");
            tab1.setIconAt(tab1.getSelectedIndex(), null);
            // Delete file from openedFiles
            openedFiles.remove(tab1.getSelectedIndex());
        } else {
        	tab1.remove(tab1.getSelectedIndex());
	        // Remove from list
	        textareas.remove(tab1.getSelectedIndex());
	        // Delete file from openedFiles
            openedFiles.remove(tab1.getSelectedIndex());
        }
    }

    void cargaFichero(String ruta) {
        try {
            f = new File(ruta);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null) {
                textareas.get(tab1.getSelectedIndex()).append(linea + "\n");
            }

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

            bw.write(textareas.get(tab1.getSelectedIndex()).getText());

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

    void detectaFormato(String filename) {
        filename = filename.toLowerCase();

        if (filename.endsWith(".html") || filename.endsWith(".htm")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/html.png")));
        } else if (filename.endsWith(".css") || filename.endsWith(".scss") || filename.endsWith(".sass")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
        	tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/css.png")));
        } else if (filename.endsWith(".php") || filename.endsWith(".php4") || filename.endsWith(".php5")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/php.png")));
        } else if (filename.endsWith(".sql")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/sql.png")));
        } else if (filename.endsWith(".js")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/js.png")));
        } else if (filename.endsWith(".json")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/json.png")));
        } else if (filename.endsWith(".rb")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_RUBY);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/ruby.png")));
        } else if (filename.endsWith(".py")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/python.png")));
        } else if (filename.endsWith(".go")) {
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/go.png")));
        } else if (filename.endsWith(".java")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/java.png")));
        } else if (filename.endsWith(".d") || filename.endsWith(".h")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/c.png")));
        } else if (filename.endsWith(".cs")) {
        	textareas.get(tab1.getSelectedIndex()).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSHARP);
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/csharp.png")));
        } else {
            tab1.setIconAt(tab1.getSelectedIndex(), new javax.swing.ImageIcon(getClass().getResource("icons/other.png")));
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
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

    private JTextArea area;
    private JMenu jMenu3;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem3;
    private JScrollPane jScrollPane1;
    private JMenu jmNewTab;
    private JMenu jmOpenFile;
    private JMenuItem jmSaveFile;
    private JTabbedPane tab1;
    private JPopupMenu.Separator jSeparator1;
    private RSyntaxTextArea rTextArea1;
}
