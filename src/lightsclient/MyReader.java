package lightsclient;


// import statements
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;


// this class only contains methods for reading files
// it will return necessary data after file(s) have been read
public class MyReader {
	
	String os;
	
	public MyReader() {
		os = System.getProperty("sun.desktop");
	}

}
