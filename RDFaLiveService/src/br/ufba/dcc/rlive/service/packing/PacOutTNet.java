package br.ufba.dcc.rlive.service.packing;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.mapping_and_load.elements.TransNetwork;

@XmlRootElement
public class PacOutTNet {
	private TransNetwork tNet;
	private boolean responseOk;
	private String info;
	
	public PacOutTNet(){}

	public TransNetwork gettNet() {
		return tNet;
	}

	public void settNet(TransNetwork tNet) {
		this.tNet = tNet;
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
