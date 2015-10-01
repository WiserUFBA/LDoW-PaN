package br.ufba.dcc.rlive.processing.discovery_and_counseling.operations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import br.ufba.dcc.rlive.processing.analytical_filtering.operations.Removal;
import br.ufba.dcc.rlive.processing.base.elements.RLTriple;
import br.ufba.dcc.rlive.processing.base.elements.RLTriples;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNMedia;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNNews;
import br.ufba.dcc.rlive.processing.interface_preparation.operations.Making;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;
import br.ufba.dcc.rlive.processing.mapping_and_load.operations.Mounting;
import br.ufba.dcc.rlive.processing.util.GeneralUtil;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Discovery {
	private static String flickrApiKey = "b049c948c9b9b4fa1e2dabf7c2740d43";
	private static String flickrSharedSecret = "b049c948c9b9b4fa1e2dabf7c2740d43";
	//private static String flickrServer = "https://api.flickr.com/";
	
	public static List<PNMedia> discoveryFlickrImages(String[] tags) throws Exception{
		if(tags != null && tags.length > 0){
			List<PNMedia> flickrPhotosList = new ArrayList<PNMedia>();
				
			Flickr flickr = new Flickr(flickrApiKey, flickrSharedSecret, new REST());
			Flickr.debugStream = false;
			
			SearchParameters searchParams=new SearchParameters();
		    searchParams.setSort(SearchParameters.INTERESTINGNESS_ASC);
		    
		    searchParams.setTags(tags);
		    
		    PhotosInterface photosInterface = flickr.getPhotosInterface();
		    PhotoList<Photo> photoList = photosInterface.search(searchParams, 10, 1); // quantidade de fotos retornadas (5)
		    
		    if(photoList != null){
		        for(int i=0; i<photoList.size(); i++){
		           Photo photo = (Photo)photoList.get(i);
		           PNMedia media = new PNMedia();
		           
		           String description = photo.getDescription();
		           if(description == null)
		        	   description = photo.getTitle();
		           media.setMediaURL(photo.getLargeUrl()); media.setMediaURLAux(photo.getSmallSquareUrl()); media.setMediaCaption(photo.getTitle()); media.setMediaInfo(description);
		           flickrPhotosList.add(media);
		        }
		    }
		    
		    return flickrPhotosList;
		}
		else{
			throw new Exception("Null tags or tags is empty. ");
		}
	}
	
	public static PNMedia discoveryFlickrImagesLDByID(String imageID) throws Exception{
		if(imageID != null && !imageID.isEmpty()){
			PNMedia flickrPhoto = new PNMedia();
		   
		    //initialize Flickr object with key and rest
		    Flickr flickr = new Flickr(flickrApiKey, flickrSharedSecret, new REST());
		    Flickr.debugStream = false;

		    //Initialize PhotosInterface object
		    PhotosInterface photosInterface = flickr.getPhotosInterface();
		    //Execute search with photo ID you want to get information
		    Photo photo = photosInterface.getPhoto(imageID);
		    if(photo != null){
		    	String description = photo.getDescription();
		    	if(description == null)
		    		description = photo.getTitle();
		    	flickrPhoto.setMediaURL(photo.getLargeUrl());
		    	flickrPhoto.setMediaURLAux(photo.getSmallSquareUrl());
		    	flickrPhoto.setMediaCaption(photo.getTitle());
		    	flickrPhoto.setMediaInfo(description);
		    }
		    
		    return flickrPhoto;
		}
		else{
			throw new Exception("Null tags or tags is empty. ");
		}
	}
	
	public static List<PNMedia> discoveryYouTubeVideos(String[] tags) throws Exception{
		if(tags != null && tags.length > 0){
			List<PNMedia> youTubeVideosList = new ArrayList<PNMedia>();
			String apiKey = "AIzaSyAUc_AJ3i_ofQmXrnMByiypkG5nfux5TPY";
	
			YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {}
			}).setApplicationName("youtube-cmdline-search-sample").build();
	
			YouTube.Search.List search = youtube.search().list("id,snippet");
	
			search.setKey(apiKey);
			search.setQ(tags[0]);
			search.setType("video");
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(5L);
			
			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();
	
			if (searchResultList != null) {
				Iterator<SearchResult> itr = searchResultList.iterator();
				while(itr.hasNext()){
					SearchResult singleVideo = (SearchResult) itr.next();
					ResourceId rId = singleVideo.getId();
					PNMedia media = new PNMedia();
					media.setMediaURL("http://www.youtube.com/watch?v="+rId.getVideoId());
					
					if (rId.getKind().equals("youtube#video")) {
						Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");
						media.setMediaURLAux(thumbnail.getUrl());
						media.setMediaCaption(singleVideo.getSnippet().getTitle());
						String description = singleVideo.getSnippet().getDescription();
			        	if(description == null)
			        		description = singleVideo.getSnippet().getTitle();
			        	media.setMediaInfo(description);
			        	youTubeVideosList.add(media);
					}
				}
			}
			
			return youTubeVideosList;
		}
		else{
			throw new Exception("Null tags or tags is empty. ");
		}
	}
	
	public static List<PNNews> discoveryNYTNews(String[] tags) throws Exception{
		if(tags != null && tags.length > 0){
			List<PNNews> NYTNewsList = new ArrayList<PNNews>();
			String apiKey = "eb11b7a77a33e49e3a7af37b6e97c8ae:3:68214823";
			String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json?q="+tags[0]+"&api-key="+apiKey;
			
			// getHTTP na url do servico e trata o json que eh retornado
			String acceptType = "application/json";
			InputStream inBodyMessage = null;
			DefaultHttpClient httpclient = new DefaultHttpClient(); // inicializa um DefaultHttpClient para executar a requisicao
	        HttpGet httpGet = new HttpGet(url); // inicializa um HttpGet com a uri a ser dereferenciada
	        httpGet.setHeader("Accept", acceptType); // seta o elemento Accept do cabecalho da requisicao para application/rdf+xml
	        
	    	HttpResponse response = httpclient.execute(httpGet); // executa o request
	    	
	    	// se a requisicao foi respondida de forma correta (200 OK) e o tipo de conteudo da resposta esta em rdf...
	    	if(response.getStatusLine().toString().contains("200 OK") && response.getFirstHeader("Content-Type").getValue().contains(acceptType)){
	            HttpEntity entity = response.getEntity();
	            inBodyMessage = entity.getContent(); // recebe o conteudo do body da mensagem de resposta
	            String jsonString = GeneralUtil.getStringFromInputStream(inBodyMessage);
            
	            JsonParser parser = new JsonParser();
	            JsonObject joOri = (JsonObject)parser.parse(jsonString);
	            
	            if(joOri.getAsJsonPrimitive("status").getAsString().equalsIgnoreCase("OK")){
	            	JsonObject joResponse = joOri.getAsJsonObject("response");
		            JsonArray jaDocs = joResponse.getAsJsonArray("docs");
		            for(int i=0; i<jaDocs.size(); i++){
		            	PNNews news = new PNNews();
		            	joOri = jaDocs.get(i).getAsJsonObject();   	
		            	
		            	try{
		            		news.setNewsUrl(joOri.getAsJsonPrimitive("web_url").getAsString());
						}catch(ClassCastException e){news.setNewsUrl(null);}
		            	
		            	try{
		            		news.setNewsSnippet(joOri.getAsJsonPrimitive("snippet").getAsString());
						}catch(ClassCastException e){news.setNewsSnippet(null);}
		            	
		            	try{
		            		news.setNewsAbstract(joOri.getAsJsonPrimitive("abstract").getAsString());
						}catch(ClassCastException e){
							news.setNewsAbstract(null);
						}
		            		
		            	try{
		            		news.setNewsSource(joOri.getAsJsonPrimitive("source").getAsString());
						}catch(ClassCastException e){news.setNewsSource(null);}
		            	
		            	try{
		            		news.setNewsPubDate(joOri.getAsJsonPrimitive("pub_date").getAsString());
						}catch(ClassCastException e){news.setNewsPubDate(null);}
		            	
		            	try{
		            		joResponse = joOri.getAsJsonObject("byline");
		            		news.setNewsBylineOriginal(joResponse.getAsJsonPrimitive("original").getAsString());
						}catch(ClassCastException e){news.setNewsBylineOriginal(null);}
		            	
		            	if(joOri.getAsJsonArray("multimedia").size() > 0){
			            	joResponse = joOri.getAsJsonArray("multimedia").get(0).getAsJsonObject();
			            	try{
			            		news.setNewsMultimediaUrl(joResponse.getAsJsonPrimitive("url").getAsString());
							}catch(ClassCastException e){news.setNewsMultimediaUrl(null);}
		            	}
		            	else{
		            		news.setNewsMultimediaUrl(null);
		            	}
		            	NYTNewsList.add(news);
		            }
	            }
	    	}
	    	else{
	    		throw new Exception("Response: "+response.getStatusLine().toString()+".\n Content-Type: "+response.getFirstHeader("Content-Type").getValue());
	    	}
			
			return NYTNewsList;
		}
		else{
			throw new Exception("Null tags or tags is empty. ");
		}
	}
	
	public static PNBox discoveryResourceOnWD(String uriAtom) throws Exception{
		Model model = JenaProcess.getModelBydereferenceURI(uriAtom);
	
		RLAtom atom = new RLAtom();
		RLTriples triples = new RLTriples();
		PNBox box = new PNBox();
		
		// consulta o model para retornar apenas os dados interesantes para o tooltip
		// String queryStr = "";
		// ResultSet results = JenaProcess.executeSPARQLByDataset(queryStr, model);
		// monta um objeto RLTriples e passa para Mounting.buildRLNet(trs);
		
				
		StmtIterator itr = model.listStatements();
		while(itr.hasNext()) {
			Statement stmt = itr.nextStatement();
			
			RLTriple triple = new RLTriple();
		    triple.setTripleSubject(stmt.getSubject().toString());
		    triple.setTriplePredicate(stmt.getPredicate().toString());
		    triple.setTripleObject(stmt.getObject().toString());
		   
		    triples.getTriplesList().add(triple);
		}
		
		triples = Removal.level1(triples);
		RLNetwork rlNet = Mounting.buildRLNet(triples);
		atom = Mounting.netContains(uriAtom, rlNet);
		box = Making.makeBox(atom, rlNet);
		
		return box;
	}
	
}
