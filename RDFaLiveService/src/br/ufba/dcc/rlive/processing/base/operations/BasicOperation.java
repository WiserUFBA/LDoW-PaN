package br.ufba.dcc.rlive.processing.base.operations;

import java.util.Collection;
import java.util.Iterator;

import org.apache.jena.atlas.AtlasException;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;

import com.sun.org.apache.xerces.internal.util.URI;

public class BasicOperation {
	
	public static boolean isLDResourceURI(String obj, String pred){
		try {
			if(obj != null && !obj.isEmpty()){
				if (!isBlankNode(obj)){
					//verifica a formacao
					checksURIFormation(obj);
					
					//verifica alguns detalhes para descartar a possibilidade de ser uri de recurso LD antes de acessar a rede
					checksSpecialDetails(obj, pred);
					
					//verifica na rede se responde como recurso LD (overhead)
					//checksNetResponse(str);
				}
			}
			else{
				return false;
			}
			
		} catch (AtlasException e) {// nao obtem resposta ao request (fora do ar). Pode ou nao ser um recurso (nao se sabe).
			return false;
		} catch (Exception e) {// todas as outras excecoes
			return false;
		}
		
		return true;
	}
	
	public static boolean isBlankNode(String str) throws Exception{
		if(str != null && !str.isEmpty()){
			if(str.substring(0, 2).equals("_:")){
				try{
					Integer.parseInt(str.substring(2, str.length()));
					return true;
				}
				catch (Exception e) {
					return false;
				}
			}
			else{
				return false;
			}
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
	public static void checksURIFormation(String str) throws Exception{
		if(str != null && !str.isEmpty()){
			@SuppressWarnings("unused")
			URI uri = new URI(str); // verifica a formacao
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
	private static void checksSpecialDetails(String str, String pred) throws Exception{
		if(str != null && !str.isEmpty()){
			String str2 = getURIWithoutPrefix(str);
			
			if(str2.contains(".") || pred.contains("Page") || pred.contains("page") || pred.contains("site")){
				throw new Exception("Can be file or webpage.");
			}
			// melhorar essa avaliacao
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
	/*
	private static void checksNetResponse(String str) throws Exception{
		if(str != null && !str.isEmpty()){
			System.out.print("request for "+str);
			//@SuppressWarnings("unused")
			//Dataset dataset = DatasetFactory.create(str); // verifica na web de dados
			
			DefaultHttpClient httpclient = new DefaultHttpClient(); // inicializa um DefaultHttpClient para executar a requisicao
	        HttpGet httpGet = new HttpGet(str); // inicializa um HttpGet com a uri a ser dereferenciada
	        httpGet.setHeader("Accept", "application/rdf+xml"); // seta o elemento Accept do cabecalho da requisicao para application/rdf+xml
	        
		    HttpResponse response = httpclient.execute(httpGet); // executa o request
		        
		    if(response.getStatusLine().toString().contains("200 OK")){
		        if(!response.getFirstHeader("Content-Type").getValue().contains("application/rdf+xml")){
		        	System.out.println(" not RDF");
		        	throw new Exception("not RDF");
		        }
		    }
		    else{
		    	System.out.println(" not 200 OK");
		    	throw new Exception("not 200 OK");
		    }
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	*/
	
	public static String getURIWithoutPrefix(String uri) throws Exception{
		if(uri != null && !uri.isEmpty()){
			String str = uri.substring(uri.lastIndexOf("/")+1);
			str = str.substring(str.lastIndexOf("#")+1);
			
			if(str == "" || str.length() == 0){
				str = uri.substring(0, uri.length()-1);
				str = getURIWithoutPrefix(str);
			}
			
			return str;
		}
		else{
			throw new Exception("Null str or str is empty. ");
		}
	}
	
	public static RLAtom getAtomWithBiggerDegree(RLNetwork rlNet) throws Exception{
		if(rlNet != null && rlNet.getNetGraph() != null){
			Collection<RLAtom> atons = rlNet.getNetGraph().getVertices();
			Iterator<RLAtom> itr = atons.iterator();
			int biggerDegree = 0, degree = 0;
			RLAtom atom = new RLAtom(), resAtom = new RLAtom();
			
			while(itr.hasNext()){
				atom = itr.next();
				degree = rlNet.getNetGraph().degree(atom);
				if(degree >= biggerDegree){
					resAtom = atom;
					biggerDegree = degree;
				}
			}
			
			return resAtom;
		}
		else{
			throw new Exception("Null rlNet or rlNet.getNetGraph(). ");
		}
	}
	
	public static String removeMetaRefs(String str){
		String result = str;
		
		int pos1 = str.lastIndexOf("@");
		if(pos1 != -1){
			if(str.substring(pos1).length() == 3){
				result = str.substring(0, pos1);
			}
		}
		
		pos1 = str.lastIndexOf("^^http://");
		if(pos1 != -1){
			result = str.substring(0, pos1);
		}
		
		return result;
	}
	
}
