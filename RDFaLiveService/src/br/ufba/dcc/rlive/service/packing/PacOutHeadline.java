package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNHeadline;

@XmlRootElement
public class PacOutHeadline {
	private PNHeadline headline;
	private boolean responseOk;
	private String info;
	
	public PacOutHeadline(){}

	public PNHeadline getHeadline() {
		return headline;
	}

	public void setHeadline(PNHeadline headline) {
		this.headline = headline;
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
