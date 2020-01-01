package org.d8vjork.jcocktail;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import java.awt.event.*;

import org.apache.tika.utils.SystemUtils;

@SuppressWarnings("serial")
public class AppMenu extends JMenuBar {

    private App parent;

    AppMenu(App parent) {
        super();
        this.parent = parent;
        setMenuItems();
    }

    private void setMenuItems() {
        setFilesMenu();
        setEditMenu();
        setViewMenu();
        setAboutMenu();
    }

    private void setFilesMenu() {
        JMenu filesMenu = new JMenu("File");
        JMenuItem openFile = new JMenuItem("Open...");
        JMenuItem openFolder = new JMenuItem("Open Folder...");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save As...");

        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, toggleCtrlOSKey()));
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.jmOpenFileActionPerformed(evt);
            }
        });
        filesMenu.add(openFile);

        openFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, toggleCtrlOSKey()));
        openFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.jmOpenFolderActionPerformed(evt);
            }
        });
        filesMenu.add(openFolder);

        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, toggleCtrlOSKey()));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.jmSaveFileActionPerformed(evt);
            }
        });
        filesMenu.add(save);

        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.jmSaveFileAsActionPerformed(evt);
            }
        });
        filesMenu.add(saveAs);

        add(filesMenu);
    }

    private void setEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem newTab = new JMenuItem("New Tab");
        JMenuItem closeTab = new JMenuItem("Close Tab");

        newTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, toggleCtrlOSKey()));
        newTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.jmNewTabActionPerformed(evt);
            }
        });
        editMenu.add(newTab);

        closeTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, toggleCtrlOSKey()));
        closeTab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.jmCloseTabActionPerformed(evt);
            }
        });
        editMenu.add(closeTab);

        add(editMenu);
    }

    private void setViewMenu() {
        JMenu viewMenu = new JMenu("Theme");
        // JMenuItem theme = new JMenuItem("Theme");
        ButtonGroup themeOptions = new ButtonGroup();

        addThemeItem("Default", "default", themeOptions, viewMenu);
        addThemeItem("Default (System Selection)", "default-alt", themeOptions, viewMenu);
        addThemeItem("Dark", "dark", themeOptions, viewMenu);
        addThemeItem("Monokai", "monokai", themeOptions, viewMenu);
        addThemeItem("Eclipse", "eclipse", themeOptions, viewMenu);
        addThemeItem("IDEA", "idea", themeOptions, viewMenu);
        addThemeItem("Visual Studio", "vs", themeOptions, viewMenu);

        add(viewMenu);
    }

    private void addThemeItem(String name, String themeName, ButtonGroup bg, JMenu menu) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(themeName);

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                parent.getCurrentTab().setEditorTheme(themeName);
            }
        });

        bg.add(item);
        menu.add(item);
    }

    private void setAboutMenu() {
        JMenu aboutMenu = new JMenu("About");

        aboutMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                parent.jmAboutMouseClicked(evt);
            }
        });

        add(aboutMenu);
    }

    private int toggleCtrlOSKey() {
        return getToolkit().getMenuShortcutKeyMaskEx();
    }
}
