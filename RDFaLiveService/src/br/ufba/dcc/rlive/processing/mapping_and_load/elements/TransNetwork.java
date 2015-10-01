package br.ufba.dcc.rlive.processing.mapping_and_load.elements;

import java.util.ArrayList;
import java.util.Collection;

public class TransNetwork {
	private Collection<RLAtom> vertices;
	private Collection<RLLink> edges;
	
	
	
	public TransNetwork(){
		this.vertices = new ArrayList<RLAtom>();
		this.edges = new ArrayList<RLLink>();
	}

	public Collection<RLAtom> getVertices() {
		return vertices;
	}

	public void setVertices(Collection<RLAtom> vertices) {
		this.vertices = vertices;
	}

	public Collection<RLLink> getEdges() {
		return edges;
	}

	public void setEdges(Collection<RLLink> edges) {
		this.edges = edges;
	}
	
}
