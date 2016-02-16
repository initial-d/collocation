package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class A extends Thread {
	public void run() {
		System.out.println(merge.a);
	}
}
public class merge {
	public static int a = -1;
	public static void main(String [] args) throws IOException {
		a = 2;
		new A().start();
		a = 3;
		new A().start();
		a = 4;
		new A().start();
		
		FileOutputStream out = null;
		out = new FileOutputStream(new File("F:\\eclipse\\english.txt"));
		out.write(("" + "\r\n").getBytes() );
	}
}
