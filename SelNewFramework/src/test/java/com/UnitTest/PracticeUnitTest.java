package com.UnitTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PracticeUnitTest {
	static String reuse=System.getProperty("user.dir") + "\\resources\\Reuse.txt";
	static String reusevar=System.getProperty("user.dir") + "\\resources\\reusevar.properties";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	/*String strs1 = "CUsername|DPassword|Eother";

		
		String[] arrSplit = strs1.split("\\|");
		writeReuse(arrSplit);*/
		
		/*for (int i = 0; i < arrSplit.length; i++) {
			System.out.println(arrSplit[i]);
			String ReuseVar2 = arrSplit[i];
			
		}
		
		*/

		
		
		//writeReuse();;
		searchString(reuse, "Password");

	}


public static void writeReuse(String strs []) throws IOException {
	

	 FileWriter fw = new FileWriter(reuse);
	    

	    for (int i = 0; i < strs.length; i++) {
	      fw.write(strs[i] + "\n");
	    }
	    fw.close();
	  }


public static void writeReuse2(String Reusestr) throws IOException {

	Properties prop = new Properties();
	OutputStream output = null;

	try {

		output = new FileOutputStream(reusevar);

		// set the properties value
		prop.setProperty("Reuse", Reusestr);
		

		// save properties to project root folder
		prop.store(output, null);

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
  }

	public static String searchString(String fileName, String phrase) throws IOException {
		String line = "";
		Scanner fileScanner = new Scanner(new File(fileName));
		int lineID = 0;
		List lineNumbers = new ArrayList();
		Pattern pattern = Pattern.compile(phrase);
		Matcher matcher = null;
		while (fileScanner.hasNextLine()) {
			line = fileScanner.nextLine();
			lineID++;
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				// lineNumbers.add(lineID);
				System.out.println("found " + line);
				return line;

			}

		}
		return line;
		

		
		

	}
}
	

