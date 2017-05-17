package primary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class IO {

	public static BufferedReader readFile(String ruta) throws IOException {
		return new BufferedReader(new FileReader(new File(ruta)));
	}
	
	public static BufferedWriter writeFile(String ruta) throws IOException {
		return new BufferedWriter(new FileWriter(new File(ruta)));
	}
	
}