package br.ufba.dcc.rlive.processing.interface_preparation.elements;

import java.util.ArrayList;
import java.util.List;

public class PNHeadline {
	private String atomUID;
	private String atomLabel;
	private List<PNNews> news;
	
	
	
	
	public PNHeadline(){
		this.atomUID = new String();
		this.atomLabel = new String();
		this.news = new ArrayList<PNNews>();
	}

	public String getAtomUID() {
		return atomUID;
	}

	public void setAtomUID(String atomUID) {
		this.atomUID = atomUID;
	}

	public String getAtomLabel() {
		return atomLabel;
	}

	public void setAtomLabel(String atomLabel) {
		this.atomLabel = atomLabel;
	}

	public List<PNNews> getNews() {
		return news;
	}

	public void setNews(List<PNNews> news) {
		this.news = news;
	}
	
}
