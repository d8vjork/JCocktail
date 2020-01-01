package org.d8vjork.jcocktail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class IO {

    // TODO: Move file...

	public static BufferedReader readFile(String path) throws IOException {
		return new BufferedReader(new FileReader(new File(path)));
	}

	public static BufferedWriter writeFile(String path) throws IOException {
		return new BufferedWriter(new FileWriter(new File(path)));
    }



}
