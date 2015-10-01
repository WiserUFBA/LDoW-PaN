package br.ufba.dcc.rlive.processing.interface_preparation.operations;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;

import br.ufba.dcc.rlive.processing.base.operations.BasicOperation;
import br.ufba.dcc.rlive.processing.discovery_and_counseling.operations.Discovery;
import br.ufba.dcc.rlive.processing.discovery_and_counseling.operations.JenaProcess;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNEntry;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNGallery;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNHeadline;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNInList;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNMedia;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNOutList;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLLink;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLLiteral;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;
import br.ufba.dcc.rlive.processing.mapping_and_load.operations.Mounting;

public class Making {
	
	public static PNBox makeBox(RLAtom atom, RLNetwork rlNet) throws Exception{
		if(atom != null && rlNet != null && rlNet.getNetGraph() != null && rlNet.getNetGraph().getVertexCount() != 0){
			PNBox box = new PNBox();
			box.setReferential(atom);
			box.setOutPredicates(findOutPredicates(atom, rlNet));
			box.setInPredicates(findInPredicates(atom, rlNet));
			
			return box;
		}
		else{
			throw new Exception("Null atom, rlNet or rlNet.getNetGraph(). Or rlNet.getNetGraph() contains zero vertices. ");
		}
	}
	
	private static List<PNEntry> findOutPredicates(RLAtom atom, RLNetwork rlNet) throws Exception{
		if(atom != null && rlNet != null && rlNet.getNetGraph() != null){
			List<PNEntry> entries = new ArrayList<PNEntry>();
			Collection<RLLink> links = rlNet.getNetGraph().getOutEdges(atom);
			
			if(links != null && !links.isEmpty()){
				boolean typeOk = false;
				Iterator<RLLink> itr = links.iterator(); // arestas de saida
				while(itr.hasNext()){
					RLLink link = itr.next();
					if(!typeOk){
						if(link.getLinkPredicate().getSpecURI().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")){
							if(link.getLinkObject().getSpecURI().contains("Person")){
								atom.setAtomType("Person"); typeOk = true;} else {
							if(link.getLinkObject().getSpecURI().contains("Organisation") || link.getLinkObject().getSpecURI().contains("Company")){
								atom.setAtomType("Organization"); typeOk = true;} else {
							if(link.getLinkObject().getSpecURI().contains("Place")){
								atom.setAtomType("Place"); typeOk = true;}
							// outros tipos podem ser definidos aqui
							}}
						}
					}
					PNEntry entry = new PNEntry(link.getLinkPredicate().getSpecURI(), Mounting.netContains(link.getLinkObject().getSpecURI(), rlNet));
					entry.setLabelKey(link.getLinkPredicate().getLabel());
					entries.add(entry);
				}
				
				if(!typeOk){
					atom.setAtomType("Unknows");
				}
			}
			
			return entries;
		}
		else{
			throw new Exception("Null atom, rlNet or rlNet.getNetGraph(). ");
		}
	}
	
	private static List<PNEntry> findInPredicates(RLAtom atom, RLNetwork rlNet) throws Exception{
		if(atom != null && rlNet != null && rlNet.getNetGraph() != null){
			List<PNEntry> entries = new ArrayList<PNEntry>();
			Collection<RLLink> links = rlNet.getNetGraph().getInEdges(atom);
			
			
			if(links != null && !links.isEmpty()){
				Iterator<RLLink> itr = links.iterator(); // arestas de entrada
				while(itr.hasNext()){
					RLLink link = itr.next();
					PNEntry entry = new PNEntry(link.getLinkPredicate().getSpecURI(),  Mounting.netContains(link.getLinkSubject().getSpecURI(), rlNet));
					entry.setLabelKey(link.getLinkPredicate().getLabel());
					entries.add(entry);
				}
			}
			
			return entries;
		}
		else{
			throw new Exception("Null atom, rlNet or rlNet.getNetGraph(). ");
		}
	}
	
	public static PNOutList makeOutList(PNBox box, String pred) throws Exception{
		if(box != null && pred != null && !pred.isEmpty()){
			PNOutList list = new PNOutList();
			
			list.setPredicate(Labelling.defineLinkLabel(pred));
			list.setReference(box.getReferential());
			list.setConnectElements(findObjects(box, pred));
			
			return list;
		}
		else{
			throw new Exception("Null box or pred. Or pred is empty. ");
		}
	}
	
	private static Collection<RLAtom> findObjects(PNBox box, String pred) throws Exception{
		if(box != null && pred != null && !pred.isEmpty()){
			Collection<RLAtom> atons = new ArrayList<RLAtom>();
			List<PNEntry> outEntries = box.getOutPredicates();
			Iterator<PNEntry> itr = outEntries.iterator();
			
			while(itr.hasNext()) {
				PNEntry entry = itr.next();
				if(entry.getKey().equals(pred)){
					RLAtom atom = boxOutContains(entry.getValue().getAtomUID(), box);
					if(atons != null){
						atons.add(atom);
					}
				}
			}
			
			return atons;
		}
		else{
			throw new Exception("Null box or pred. Or pred is empty. ");
		}
	}
	
	public static PNInList makeInList(PNBox box, String pred) throws Exception{
		if(box != null && pred != null && !pred.isEmpty()){
			PNInList list = new PNInList();
			
			list.setPredicate(Labelling.defineLinkLabel(pred));
			list.setReference(box.getReferential());
			list.setConnectElements(findSubjects(box, pred));
			
			return list;
		}
		else{
			throw new Exception("Null box or pred. Or pred is empty. ");
		}
	}
	
	private static Collection<RLAtom> findSubjects(PNBox box, String pred) throws Exception{
		if(box != null && pred != null && !pred.isEmpty()){
			Collection<RLAtom> atons = new ArrayList<RLAtom>();
			List<PNEntry> inEntries = box.getInPredicates();
			Iterator<PNEntry> itr = inEntries.iterator();
			
			while(itr.hasNext()) {
				PNEntry entry = itr.next();
				if(entry.getKey().equals(pred)){
					RLAtom atom = boxInContains(entry.getValue().getAtomUID(), box);
					if(atom != null){
						atons.add(atom);
					}
				}
			}
			
			return atons;
		}
		else{
			throw new Exception("Null box or pred. Or pred is empty. ");
		}
	}
	
	public static RLAtom boxContains(String atomUID, PNBox box) throws Exception{
		if(box != null && atomUID != null && !atomUID.isEmpty()){
			Collection<PNEntry> boxOutAtons = box.getOutPredicates();
			Collection<PNEntry> boxInAtons = box.getInPredicates();
						
			Iterator<PNEntry> outItr = boxOutAtons.iterator();
			while (outItr.hasNext()){
				PNEntry outEntry = outItr.next();
				if(outEntry.getValue().getAtomUID().equals(atomUID)){
					return outEntry.getValue();
				}
			}
			
			Iterator<PNEntry> inItr = boxInAtons.iterator();
			while (inItr.hasNext()){
				PNEntry inEntry = inItr.next();
				if(inEntry.getValue().getAtomUID().equals(atomUID)){
					return inEntry.getValue();
				}
			}
			
			return null;
		}
		else{
			throw new Exception("Null box or atomUID. Or atomUID is empty. ");
		}
	}
	
	public static RLAtom boxOutContains(String atomUID, PNBox box) throws Exception{
		if(box != null && atomUID != null && !atomUID.isEmpty()){
			Collection<PNEntry> boxOutAtons = box.getOutPredicates();
						
			Iterator<PNEntry> itr = boxOutAtons.iterator();
			while (itr.hasNext()){
				PNEntry entry = itr.next();
				if(entry.getValue().getAtomUID().equals(atomUID)){
					return entry.getValue();
				}
			}
			return null;
		}
		else{
			throw new Exception("Null box or atomUID. Or atomUID is empty. ");
		}
	}
	
	public static RLAtom boxInContains(String atomUID, PNBox box) throws Exception{
		if(box != null && atomUID != null && !atomUID.isEmpty()){
			Collection<PNEntry> boxInAtons = box.getInPredicates();
			
			Iterator<PNEntry> itr = boxInAtons.iterator();
			while (itr.hasNext()){
				PNEntry entry = itr.next();
				if(entry.getValue().getAtomUID().equals(atomUID)){
					return entry.getValue();
				}
			}
			return null;
		}
		else{
			throw new Exception("Null box or atomUID. Or atomUID is empty. ");
		}
	}
	
	public static PNGallery makeGallery(RLAtom atom) throws Exception{
		if(atom != null){
			PNGallery gallery = new PNGallery();
			List<PNMedia> galleryList = new ArrayList<PNMedia>();
			List<RLLiteral> literals = atom.getAtomLiterals();
			String pred = new String(), value = new String();
			
			// recupera imagens do atomo
			for(int i=0; i<literals.size(); i++){
				pred = literals.get(i).getLitPredicate();
				value = literals.get(i).getLitValue();
				PNMedia media = new PNMedia();
				media.setMediaURL(value); media.setMediaURLAux(value); media.setMediaCaption(atom.getAtomLabel()); media.setMediaInfo(atom.getAtomLabel());
				if(containsMedia(pred, value) && !galleryList.contains(media)){
					galleryList.add(media);
				}
			}
			
			// descobre novas imagens na web 2.0
			List<PNMedia> flickrList = Discovery.discoveryFlickrImages(atom.getAtomLabel().split("!"));
			flickrList.addAll(galleryList); // merge as duas listas
			galleryList = flickrList; // as fotos do flickr aparecem primeiro na lista
			
			List<PNMedia> youTubeList = Discovery.discoveryYouTubeVideos(atom.getAtomLabel().split("!"));
			galleryList.addAll(youTubeList);
			
			gallery.setAtomUID(atom.getAtomUID());
			gallery.setAtomLabel(atom.getAtomLabel());
			gallery.setMedia(galleryList);
			
			return gallery;
		}
		else{
			throw new Exception("Null atom. ");
		}
	}
	
	public static PNGallery makeGalleryFlickrLD(String atomURI) throws Exception{
		if(atomURI != null && !atomURI.isEmpty()){
			String flickrURI = "http://www4.wiwiss.fu-berlin.de/flickrwrappr/photos/" + BasicOperation.getURIWithoutPrefix(atomURI);
			PNGallery gallery = new PNGallery(); gallery.setAtomUID(atomURI); gallery.setAtomLabel(Labelling.defineAtomLabel(atomURI));
			PNMedia photo = new PNMedia();
			
			Dataset dataset = JenaProcess.getDatasetBydereferenceURI(flickrURI); // gargalo de desempenho
			String queryStr = "PREFIX foaf: <http://xmlns.com/foaf/0.1/> SELECT ?z WHERE { ?x foaf:page ?z }";
			ResultSet results = JenaProcess.executeSPARQLByDataset(queryStr, dataset);
			
			// cria a galeria com fotos recuperadas na nuvem de dados do flickr
			int count = 0;
			while(results.hasNext()){
				if(count<20){
				    QuerySolution soln = results.nextSolution() ;
					Resource o = soln.getResource("z");
					photo = Discovery.discoveryFlickrImagesLDByID(BasicOperation.getURIWithoutPrefix(o.toString()));
					gallery.getMedia().add(photo);
					count++;
				}
				else{
					break;
				}
			}
			
			JenaProcess.qexec.close();
			
			return gallery;
		}
		else{
			throw new Exception("Null atomURI or atomURI is empty. ");
		}
	}
	
	public static PNHeadline makeNews(RLAtom atom) throws Exception{
		if(atom != null){
			PNHeadline headline = new PNHeadline();
			headline.setAtomUID(atom.getAtomUID());
			headline.setAtomLabel(atom.getAtomLabel());
			headline.setNews(Discovery.discoveryNYTNews(atom.getAtomLabel().replaceAll(" ", "%20").split("!")));
		
			return headline;
		}
		else{
			throw new Exception("Null atom. ");
		}
	}
	
	private static boolean containsMedia(String pred, String value){
		if( pred.contains("thumbnail") ||
			pred.contains("depiction") ||
			pred.contains("image") || 
			pred.contains("img") ||
			value.contains(".png") ||
			value.contains(".jpg") ||
			value.contains(".jpeg") )
			return true;
		else
			return false;
	}
	
	private static ArrayList<String> getOptimizedList(ArrayList<String> objList){
		// realiza uma eleicao na lista original e cria uma lista otimizada
		ArrayList<String> optimizedList = new ArrayList<String>();
		int limit = 1; // quantidade de objs escolhida da lista original (por exemplo, 2 objetos serao escolhidos aleatoriamente)
		
		Random generator = new Random();
		
		for(int i=0; i<limit; i++){
			optimizedList.add(objList.get(generator.nextInt(objList.size())));
		}
		
		return optimizedList;
	}
	
	public static PNOutList makeOutList(String endpoint, String pred, ArrayList<String> objList, RLAtom atomRef) throws Exception{
		if(pred != null && !pred.isEmpty()){
			PNOutList outList = new PNOutList();
			
			ArrayList<String> optimizedList = getOptimizedList(objList);
			
			String queryStr = "SELECT ?s  WHERE {";
			for(int i=0; i<optimizedList.size(); i++){
				queryStr = queryStr + "?s <"+pred+"> <"+optimizedList.get(i)+"> .";
			}
			queryStr = queryStr + "} LIMIT 20";
			
			//System.out.println(queryStr); // query formulada
			
			ResultSet results = JenaProcess.executeSPARQLOnEndPoint(endpoint, queryStr);
			
			outList.setPredicate(Labelling.defineLinkLabel(pred));
			atomRef.setAtomType(Labelling.defineAtomLabel(optimizedList.get(0)));
			outList.setReference(atomRef); // seta o referencial com um atomo vazio (o referencial aqui nao eh importante)
			
			while(results.hasNext()){
				QuerySolution soln = results.nextSolution();
				Resource s = soln.getResource("s");
				
				String strDecode = URLDecoder.decode(s.toString(), "UTF-8");
				//System.out.println(URLDecoder.decode(s.toString(), "UTF-8")); // resultados da consulta
				
				if(!s.toString().equals(atomRef.getAtomUID())){
					RLAtom atom = new RLAtom();
					atom.setAtomLabel(Labelling.defineAtomLabel(strDecode));
					atom.setAtomUID(s.toString());
					outList.getConnectElements().add(atom);
				}
			}
			
			JenaProcess.qexec.close();
			
			return outList;
		}
		else{
			throw new Exception("Null pred or pred is empty. ");
		}
	}
	
}
