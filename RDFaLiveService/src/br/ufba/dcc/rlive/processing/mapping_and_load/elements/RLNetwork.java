package br.ufba.dcc.rlive.processing.mapping_and_load.elements;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class RLNetwork {
	private DirectedSparseMultigraph <RLAtom,RLLink> netGraph;
	
	
	public RLNetwork(){
		this.netGraph = new DirectedSparseMultigraph<RLAtom,RLLink>();
	}

	public DirectedSparseMultigraph<RLAtom, RLLink> getNetGraph() {
		return netGraph;
	}

}
