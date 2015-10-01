package br.ufba.dcc.rlive.service.packing;

import java.util.ArrayList;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;

public class PacInSimilar {
	private String pred;
	private String endpoint;
	private ArrayList<String> objs;
	private RLAtom referential;
	
	public String getPred() {
		return pred;
	}
	
	public void setPred(String pred) {
		this.pred = pred;
	}
	
	public String getEndpoint() {
		return endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public ArrayList<String> getObjs() {
		return objs;
	}
	
	public void setObjs(ArrayList<String> objs) {
		this.objs = objs;
	}
	
	public RLAtom getReferential() {
		return referential;
	}
	
	public void setReferential(RLAtom referential) {
		this.referential = referential;
	}
	
}
