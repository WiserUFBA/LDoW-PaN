package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;

@XmlRootElement
public class PacOutBox {
	private PNBox box;
	private boolean responseOk;
	private String info;
	
	public PacOutBox(){}

	public PNBox getBox() {
		return box;
	}

	public void setBox(PNBox box) {
		this.box = box;
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
