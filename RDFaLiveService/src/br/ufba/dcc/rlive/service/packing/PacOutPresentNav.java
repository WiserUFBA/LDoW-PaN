package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNGallery;
import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNHeadline;

@XmlRootElement
public class PacOutPresentNav {
	private PNBox box;
	private PNGallery gallery;
	private PNHeadline headline;
	private boolean responseOk;
	private String info;
	
	public PacOutPresentNav(){}

	public PNBox getBox() {
		return box;
	}

	public void setBox(PNBox box) {
		this.box = box;
	}

	public PNGallery getGallery() {
		return gallery;
	}

	public void setGallery(PNGallery gallery) {
		this.gallery = gallery;
	}

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
