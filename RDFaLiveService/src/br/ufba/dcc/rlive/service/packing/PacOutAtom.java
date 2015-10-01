package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.RLAtom;

@XmlRootElement
public class PacOutAtom {
	private RLAtom atom;
	private boolean responseOk;
	private String info;
	
	public PacOutAtom(){}

	public RLAtom getAtom() {
		return atom;
	}

	public void setAtom(RLAtom atom) {
		this.atom = atom;
	}

	public boolean isResponseOk() {
		return responseOk;
	}

	public void setResponseOk(boolean responseOk) {
		this.responseOk = responseOk;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
