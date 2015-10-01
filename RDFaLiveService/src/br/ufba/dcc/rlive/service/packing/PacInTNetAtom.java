package br.ufba.dcc.rlive.service.packing;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;
import br.ufba.dcc.rlive.processing.mapping_and_load.elements.TransNetwork;

public class PacInTNetAtom {
	private TransNetwork tNet;
	private RLAtom atom;
	
	public PacInTNetAtom(){}

	public TransNetwork gettNet() {
		return tNet;
	}

	public void settNet(TransNetwork tNet) {
		this.tNet = tNet;
	}

	public RLAtom getAtom() {
		return atom;
	}

	public void setAtom(RLAtom atom) {
		this.atom = atom;
	}
	
}
