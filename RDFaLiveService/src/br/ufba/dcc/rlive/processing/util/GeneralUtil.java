package br.ufba.dcc.rlive.processing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.knallgrau.utils.textcat.TextCategorizer;

public class GeneralUtil {
	
	// convert InputStream to String
	public static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
	}
	
	public static String getCategorizeText(String txt){
		TextCategorizer guesser = new TextCategorizer();
		String resp = guesser.categorize(txt);

		return resp;
	}
	
}
