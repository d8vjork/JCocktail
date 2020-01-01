package org.d8vjork.jcocktail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.fife.ui.rsyntaxtextarea.Theme;

class Tab extends JTabbedPane implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String checksum;
    private String codeLang;
    private String file_ext;
    private JScrollPane pane;
    private Textarea textarea;
    private File file;

    Tab(String title) {
        this.title = title;
        this.setTextarea();
        this.setPane();
    }

    Tab(String title, File file) {
        this.title = title;
        this.setTextarea();
        this.setPane();
        this.setFile(file);
    }

    public void setTextarea() {
        Textarea.setTemplatesEnabled(true);
        textarea = new Textarea();

        textarea.setEditable(true);
        textarea.setColumns(20);
        textarea.setRows(5);
        textarea.setCloseCurlyBraces(true);
        textarea.setMarginLineEnabled(true);
        textarea.setCodeFoldingEnabled(true);
        textarea.setMarkOccurrences(true);
        textarea.setCodeFoldingEnabled(true);

        this.setEditorTheme("monokai");
        this.textarea = textarea;
    }

    public Textarea getTextarea() {
        return this.textarea;
    }

    public void setPane() {
        if (this.textarea == null) {
            this.setTextarea();
        }

        JScrollPane pane = new JScrollPane();
        pane.setViewportView(this.textarea);
        this.pane = pane;
    }

    public JScrollPane getPane() {
        return this.pane;
    }

    public void setFile(File file) {
        BufferedReader br = null;

        try {
            br = IO.readFile(file.getPath());
            String lineStr;

            while ((lineStr = br.readLine()) != null) {
                System.out.println(lineStr);
                this.textarea.append("\n" + lineStr);
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Error trying to read file");
            ioe.printStackTrace(); // FindBugs
        } catch (Exception e) { // never happen
            JOptionPane.showMessageDialog(null, "Unknown error trying to read file");
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                //
            }
        }

        this.textarea.setCaretPosition(0);
        this.textarea.discardAllEdits();
        this.textarea.requestFocus();

        this.file = file;
        this.setFileExt();
        this.setCodeLang();
        this.setChecksum();
    }

    public File getFile() {
        return this.file;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle() {
        if (this.file.isFile()) {
            this.title = file.getName();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setChecksum() {
        if (this.file.isFile()) {
            try {
                InputStream is = new FileInputStream(file);
                this.checksum = DigestUtils.md5Hex(is);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al calcular hash del archivo");
            }
        }
    }

    public String getChecksum() {
        return this.checksum;
    }

    public void setCodeLang() {
        if (this.file != null) {
            String ext = this.file_ext;

            if (ext == "txt") {
                this.codeLang = Textarea.SYNTAX_STYLE_NONE;
            } else {
                this.codeLang = "text/" + ext;
            }
        } else {
            this.codeLang = Textarea.SYNTAX_STYLE_NONE;
        }
    }

    public String getCodeLang() {
        return this.codeLang;
    }

    public void setFileExt() {
        if (this.file != null) {
            this.file_ext = FilenameUtils.getExtension(this.file.getName());
        }
    }

    public String getFileExt() {
        return this.file_ext;
    }

    public void setEditorTheme(String themeName) {
        try {
            String resource = "/org/fife/ui/rsyntaxtextarea/themes/" + themeName + ".xml";
            Theme theme = Theme.load(getClass().getResourceAsStream(resource));
            theme.apply(this.textarea);

            System.out.println("Theme: " + themeName + " loaded");
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
    }
}
