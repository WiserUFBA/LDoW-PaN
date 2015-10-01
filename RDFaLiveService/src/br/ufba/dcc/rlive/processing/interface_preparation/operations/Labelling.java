package br.ufba.dcc.rlive.processing.interface_preparation.operations;

import br.ufba.dcc.rlive.processing.base.operations.BasicOperation;

public class Labelling {
	
	public static String defineAtomLabel(String uri) throws Exception{ // nao funciona para recursos identificados por ids
		if(uri != null && !uri.isEmpty()){
			String str = "";
			if(!BasicOperation.isBlankNode(uri)){
				str = BasicOperation.getURIWithoutPrefix(uri);
				str = str.replace("_", " ");
			}
			else{
				// ainda precisa resolver o blank node
				str = uri;
			}
			
			if(!str.contains(" ")){
				str = UCSpaces(str);
			}
			
			return str;
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
	public static String defineLinkLabel(String uri) throws Exception{
		if(uri != null && !uri.isEmpty()){
			String str = BasicOperation.getURIWithoutPrefix(uri);
			
			str = UCSpaces(str);
			
			return str;
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
	private static String UCSpaces(String str) throws Exception{
		if(str != null && !str.isEmpty()){
			String strRes = str;
			int count = 0;
			for (char c : str.toCharArray()) {
			    if (Character.isUpperCase(c)) {
			    	if(count > 0 && str.charAt(count-1) != ' ' && !Character.isUpperCase(str.charAt(count-1)) ){
			    		// insere um espaco antes do caracter upper case
				    	strRes = new StringBuilder(str).insert(count, " ").toString();
				    	strRes = UCSpaces(strRes);
			    	}
			    }
			    count++;
			}
			
			try{
				strRes = strRes.replaceFirst(strRes.substring(0, 1), strRes.substring(0, 1).toUpperCase());
			}
			catch(RuntimeException e){ } // nao existe upper case para esse caracter
			
			return  strRes;
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
}
