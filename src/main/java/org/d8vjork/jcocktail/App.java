package org.d8vjork.jcocktail;

import java.awt.FileDialog;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jtattoo.plaf.smart.SmartLookAndFeel;

import org.apache.cxf.jaxrs.utils.ResourceUtils;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Main app
 */
@SuppressWarnings("serial")
public class App extends JFrame {
    Tab curTab;

    private App() {
        initComponents();
        curTab = project.getTabs().get(tabs.getSelectedIndex());
    }

    public static URL getPathFromResources(String path) {
        ClassLoader classLoader = App.class.getClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            throw new IllegalArgumentException("File with path \"" + path + "\" not found!");
        } else {
            System.out.println("File loaded: " + resource.getPath());
            return resource;
        }
    }

    private void initComponents() {

        project = new Project(this);
        jScrollPane1 = new JScrollPane();
        tabs = new JTabbedPane();
        filler1 = new Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        toolbar = new JToolBar();
        codeLang = new JLabel();
        fileHash = new JLabel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Execute on close window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                project.saveProject();
                System.exit(0);
            }
        });

        this.setFocusable(true);

        setTitle("Cocktail");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(new ImageIcon(getPathFromResources("app-logo.png")).getImage());

        Tab newTab = new Tab("New");
        project.getTabs().add(newTab);
        tabs.addTab(newTab.getTitle(), newTab.getPane());

        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabsStateChanged(evt);
            }
        });

        // jmDebug.setText("Debug");
        // jmDebug.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent evt) {
        //         debugKeyPressed(evt);
        //     }
        // });
        // jmMenuBar.add(jmDebug);

        setJMenuBar(new AppMenu(this));

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

    public void projectTreeNodeFileClicked(TreeSelectionEvent evt) {
        Object that = project.getTree().getLastSelectedPathComponent();
        
        openNewTab(that.toString());
    }

    private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {
        // getSelectedIndex() because it change index
        fileHash.setText(project.getTabs().get(tabs.getSelectedIndex()).getChecksum());
        codeLang.setText(project.getTabs().get(tabs.getSelectedIndex()).getCodeLang());
        System.out.println("tabs=" + tabs.getSelectedIndex() + ", project=" + project.getTabs().size());
    }

    public void jmSaveFileActionPerformed(ActionEvent evt) {
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

    public void debugKeyPressed(MouseEvent evt) {
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

    public void jmSaveFileAsActionPerformed(ActionEvent evt) {
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

    public void jmOpenFileActionPerformed(ActionEvent evt) {
        FileDialog fd = new FileDialog(this, "Open...", FileDialog.LOAD);
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

                System.out.println(project.getTabs());

                // Add to tablist
                project.getTabs().remove(index);
                project.getTabs().add(index, newTab);
                tabs.setIconAt(index, new ImageIcon(getPathFromResources("icons/" + project.getTabs().get(index).getFileExt() + ".png")));

                // Set filename as tab title and path as tooltip
                tabs.setTitleAt(index, fd.getFile());
                tabs.setToolTipTextAt(index, path);

                // MD5 Hash
                fileHash.setText(newTab.getChecksum());
                codeLang.setText(newTab.getCodeLang());
            }
        }
    }

    public void jmOpenFolderActionPerformed(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int r = fc.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {
            if (new File(fc.getSelectedFile().getAbsolutePath() + File.separator + ".project").exists() == true) {
                project.openProject(fc.getSelectedFile().getAbsolutePath());
            }

            project = new Project(this, fc.getSelectedFile().getName(), fc.getSelectedFile().getAbsolutePath());
            jScrollPane1.setViewportView(project.getTree());
        }
    }

    public void jmAboutMouseClicked(MouseEvent evt) {
        JOptionPane.showMessageDialog(this, "Cocktail Editor v.X.Y", "About", JOptionPane.PLAIN_MESSAGE, new javax.swing.ImageIcon(getPathFromResources("app-logo.png")));
    }

    public void jmNewTabActionPerformed(ActionEvent evt) {
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
                newTab = new Tab("New " + project.getTabs().size());
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

    public void jmCloseTabActionPerformed(ActionEvent evt) {
        int index = tabs.getSelectedIndex();

        if (index == 0) {
            // Reset current tab textarea
            curTab.setTitle("New");
            // Reset tab title filename and icon
            tabs.setTitleAt(index, "New");
            tabs.setIconAt(index, null);
            //tabList.remove(index);
        } else {
            tabs.remove(index);
            // Remove from ArrayList
            project.getTabs().remove(index);
        }
    }

    public Tab getCurrentTab() {
        return curTab;
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new App().setVisible(true);
        });
    }

    private Project project;
    private JScrollPane jScrollPane1;
    private Box.Filler filler1;
    private JTabbedPane tabs;
    private JToolBar toolbar;
    private JLabel codeLang, fileHash;
}
