package br.ufba.dcc.rlive.service.packing;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;

public class PacInBoxPred {
	private PNBox box;
	private String pred;
	
	public PacInBoxPred(){}

	public PNBox getBox() {
		return box;
	}

	public void setBox(PNBox box) {
		this.box = box;
	}

	public String getPred() {
		return pred;
	}

	public void setPred(String pred) {
		this.pred = pred;
	}
	
}
