package br.ufba.dcc.rlive.processing.base.elements;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RLTriples {
	private List<RLTriple> triplesList;
	private String triplesHome;
	
	
	
	
	public RLTriples(){
		this.triplesList = new ArrayList<RLTriple>();
	}
	
	public String toString(){
		String str = this.triplesHome + "\n\n";
		
		int i;
		for(i=0; i<this.triplesList.size(); i++){
			str = str + this.triplesList.get(i).toString() + "\n";
		}
		str = str + "\n" + i + " triles";
		
		return str;
	}

	public List<RLTriple> getTriplesList() {
		return triplesList;
	}

	public void setTriplesList(List<RLTriple> triplesList) {
		this.triplesList = triplesList;
	}

	public String getTriplesHome() {
		return triplesHome;
	}

	public void setTriplesHome(String triplesHome) {
		this.triplesHome = triplesHome;
	}
	
}
