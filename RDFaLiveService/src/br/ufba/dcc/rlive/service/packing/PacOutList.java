package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNList;

@XmlRootElement
public class PacOutList {
	private PNList list;
	private boolean responseOk;
	private String info;
	
	public PacOutList(){}

	public PNList getList() {
		return list;
	}

	public void setList(PNList list) {
		this.list = list;
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
