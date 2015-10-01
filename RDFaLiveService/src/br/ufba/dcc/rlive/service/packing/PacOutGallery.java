package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNGallery;

@XmlRootElement
public class PacOutGallery {
	private PNGallery gallery;
	private boolean responseOk;
	private String info;
	
	public PacOutGallery(){}

	public PNGallery getGallery() {
		return gallery;
	}

	public void setGallery(PNGallery gallery) {
		this.gallery = gallery;
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
