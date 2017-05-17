package primary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

class Tab {

	private String title;
	private String checksum;
	private String codeLang;
	private String file_ext;
	private JScrollPane pane;
	private RSyntaxTextArea textarea;
	private File file;
	
	Tab(String title) {
		this.title = title;
		this.setTextarea();
		this.setPane();
	}
	
	Tab(String title, File file) {
		this.title = title;
		this.setFile(file);
		this.setTextarea();
		this.setPane();
	}
	
	public void setTextarea() {
		this.textarea = new RSyntaxTextArea();
		
		// RSyntaxTextArea styles and properties 
		this.textarea.setColumns(20);
		this.textarea.setRows(5);
		this.textarea.setCloseCurlyBraces(true);
		this.textarea.setMarginLineEnabled(true);
		this.textarea.setCodeFoldingEnabled(true);
		this.textarea.setTemplatesEnabled(true);
		
        this.setEditorTheme(this.textarea, "themes/monokai.xml");
	}
	
	public RSyntaxTextArea getTextarea() {
		return this.textarea;
	}
	
	public void setPane() {
		if (this.textarea != null) {
			JScrollPane pane = new JScrollPane();
			pane.setViewportView(this.textarea);
			this.pane = pane;
		}
	}
	
	public JScrollPane getPane() {
		return this.pane;
	}
	
	public void setFile(File file) {
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
		if (this.file.isFile())
			this.title = file.getName();
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
				this.codeLang = textarea.SYNTAX_STYLE_NONE;
			} else {
				this.codeLang = "text/" + ext;
			}
		} else {
			this.codeLang = textarea.SYNTAX_STYLE_NONE;
		}
	}
	
	public String getCodeLang() {
		return this.codeLang;
	}
	
	public void setFileExt() {
		if (this.file != null)
			this.file_ext = FilenameUtils.getExtension(this.file.getName());
	}
	
	public String getFileExt() {
		return this.file_ext;
	}
	
	private void setEditorTheme(RSyntaxTextArea textarea, String tpl) {
    	try {
            Theme theme = Theme.load(getClass().getResourceAsStream(tpl));
            theme.apply(textarea);
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
    }
}
