package test;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import test.UserSentence;

public class all_test {

	public static void main(String[] args) throws Exception {
		//while(true)
		{
			//Scanner s = new Scanner(System.in);
			//String EnglishIn = s.nextLine();
			
			String path = "E:\\STUDY\\JAVA\\collocation\\test\\cet4.txt";

			
			List<String> strSents = new ArrayList<String>();
			UserSentence usrsen = new UserSentence();
			strSents = usrsen.Getsents(path);
			
			long pre = System.currentTimeMillis();
			for(String str: strSents)
			{
				usrsen.SetStr(str);
				usrsen.test();
			}
			usrsen.Getresult();
			long post=System.currentTimeMillis();
			System.out.println("running time:"+(post-pre)+"ms");
		}
		
	}

}
