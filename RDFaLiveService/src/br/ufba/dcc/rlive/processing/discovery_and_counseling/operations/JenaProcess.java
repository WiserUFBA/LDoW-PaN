package br.ufba.dcc.rlive.processing.discovery_and_counseling.operations;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaProcess {
	
	public static QueryExecution qexec;
	
	public static Model getModelBydereferenceURI(String URI) throws Exception{
		if(URI != null && !URI.isEmpty()){
			String acceptType = "application/rdf+xml";
			InputStream inBodyMessage = null;
			DefaultHttpClient httpclient = new DefaultHttpClient(); // inicializa um DefaultHttpClient para executar a requisicao
	        HttpGet httpGet = new HttpGet(URI); // inicializa um HttpGet com a uri a ser dereferenciada
	        httpGet.setHeader("Accept", acceptType); // seta o elemento Accept do cabecalho da requisicao para application/rdf+xml
	        
	    	HttpResponse response = httpclient.execute(httpGet); // executa o request
	    	
	    	// se a requisicao foi respondida de forma correta (200 OK) e o tipo de conteudo da resposta esta em rdf...
	    	if(response.getStatusLine().toString().contains("200 OK") && response.getFirstHeader("Content-Type").getValue().contains(acceptType)){
	            HttpEntity entity = response.getEntity();
	            inBodyMessage = entity.getContent(); // recebe o conteudo do body da mensagem de resposta
	            
	            // api jena para manipulacao de grafos rdf
	    		Model model = ModelFactory.createDefaultModel(); // cria um modelo vazio (um grafo vazio)
	    		model.read(inBodyMessage, null); // preenche o modelo (grafo) com a resposta do dereferenciamento da uri
	    		
	    		return model;
	    	}
	    	else{
	    		return null;
	    	}
		}
		else{
			throw new Exception("Null URI or URI is empty. ");
		}
	}
	
	public static Dataset getDatasetBydereferenceURI(String URI) throws Exception{
		if(URI != null && !URI.isEmpty()){
			Dataset dataset = DatasetFactory.create(URI); // cria um dataset pra ser usado no QueryExecutionFactory.create com a uri da fonte de dado na web
			return dataset;
		}
		else{
			throw new Exception("Null URI or URI is empty. ");
		}
	}
	
	public static ResultSet executeSPARQLByDataset(String queryStr, Dataset dataset) throws Exception{
		if(queryStr != null && !queryStr.isEmpty() && dataset != null){
			Query query = QueryFactory.create(queryStr); // cria um objeto Query para ser usado durante a consulta
			JenaProcess.qexec = QueryExecutionFactory.create(query, dataset); // relaciona a query criada anteriormente ao conjunto de dados a ser questionado. Pode ser um Model, Um Dataset...
			
			ResultSet results = JenaProcess.qexec.execSelect() ; // executa a consulta sobre os dados
			return results;
		}
		else{
			throw new Exception("Null queryStr or dataset. Or queryStr is empty.");
		}
	}
	
	public static ResultSet executeSPARQLByModel(String queryStr, Model model) throws Exception{
		if(queryStr != null && !queryStr.isEmpty() && model != null){
			Query query = QueryFactory.create(queryStr); // cria um objeto Query para ser usado durante a consulta
			JenaProcess.qexec = QueryExecutionFactory.create(query, model); // relaciona a query criada anteriormente ao conjunto de dados a ser questionado. Pode ser um Model, Um Dataset...
			
			ResultSet results = JenaProcess.qexec.execSelect() ; // executa a consulta sobre os dados
			return results;
		}
		else{
			throw new Exception("Null queryStr or dataset. Or queryStr is empty.");
		}
	}
	
	public static ResultSet executeSPARQLOnEndPoint(String endpoint, String queryStr) throws Exception{
		if(queryStr != null && !queryStr.isEmpty() && endpoint != null && !endpoint.isEmpty()){
			Query query = QueryFactory.create(queryStr);
			
			JenaProcess.qexec = QueryExecutionFactory.sparqlService(endpoint, query);
			
			ResultSet results = JenaProcess.qexec.execSelect();
			return results;
		}
		else{
			throw new Exception("Null queryStr or endpoint. Or queryStr or endpoint is empty.");
		}
	}
	
}
