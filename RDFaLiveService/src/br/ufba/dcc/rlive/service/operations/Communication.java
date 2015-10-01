package br.ufba.dcc.rlive.service.operations;

import java.util.Collection;
import java.util.Iterator;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLLink;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLNetwork;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.TransNetwork;

public class Communication {
	
	public static TransNetwork RLNetToTransNet(RLNetwork rlNet) throws Exception{
		if(rlNet != null && rlNet.getNetGraph() != null){
			TransNetwork tNet = new TransNetwork();
			
			tNet.setVertices(rlNet.getNetGraph().getVertices());
			tNet.setEdges(rlNet.getNetGraph().getEdges());
			
			return tNet;
		}
		else{
			throw new Exception("Null rlNet or rlNet.getNetGraph(). ");
		}
	}
	
	public static RLNetwork TransNetToRLNet(TransNetwork tNet) throws Exception{
		if(tNet != null){
			RLNetwork rlNet = new RLNetwork();
			// add vertices
			addVertices(tNet.getVertices(), rlNet);
			// add arestas e conecta os elemento na rede
			addEdges(tNet.getEdges(), rlNet);
			
			return rlNet;
		}
		else{
			throw new Exception("Null tNet. ");
		}
	}
	
	private static void addVertices(Collection<RLAtom> vertices, RLNetwork rlNet) throws Exception{
		if(vertices != null && !vertices.isEmpty() && rlNet != null && rlNet.getNetGraph() != null){
			Iterator<RLAtom> itr = vertices.iterator();
			
			while (itr.hasNext()) {
				RLAtom atom = itr.next();
				rlNet.getNetGraph().addVertex(atom);
			}
		}
		else{
			throw new Exception("Null vertices, rlNet or rlNet.getNetGraph(). Or vertices is empty. ");
		}
	}
	
	private static void addEdges(Collection<RLLink> edges, RLNetwork rlNet) throws Exception{
		if(edges != null && !edges.isEmpty() && rlNet != null && rlNet.getNetGraph() != null){
			Iterator<RLLink> itr = edges.iterator();
			
			while (itr.hasNext()) {
				RLLink edge = itr.next();
				// encontra os vertices da aresta e os conecta adicionando a rede
				connectEdge(edge, rlNet);
			}
		}
		else{
			throw new Exception("Null edges, rlNet or rlNet.getNetGraph(). Or edges is empty. ");
		}
	}
	
	private static void connectEdge(RLLink edge, RLNetwork rlNet) throws Exception{
		if(edge != null && rlNet != null && rlNet.getNetGraph() != null){
			Collection<RLAtom> atons = rlNet.getNetGraph().getVertices();
			Iterator<RLAtom> itr = atons.iterator();
			RLAtom sujAtom = null, objAtom = null;
			boolean sujOk = false, objOk = false;
			
			while (itr.hasNext()) {
				RLAtom atom = itr.next();
				if(atom.getAtomUID().equals(edge.getLinkSubject().getSpecURI())){
					sujAtom = atom; sujOk = true;
				}
				else{
					if(atom.getAtomUID().equals(edge.getLinkObject().getSpecURI())){
						objAtom = atom; objOk = true;
					}
				}
				if(sujOk && objOk){
					rlNet.getNetGraph().addEdge(edge, sujAtom, objAtom);
					return;
				}
			}
		}
		else{
			throw new Exception("Null edge, rlNet or rlNet.getNetGraph(). ");
		}
	}
	
}
