package br.ufba.dcc.rlive.processing.base.elements;

public class RLTriple {
	private String tripleSubject;
	private String triplePredicate;
	private String tripleObject;
	
	
	
	
	public RLTriple(){}
	
	public RLTriple(String sub, String pred, String obj){
		this.tripleSubject = sub;
		this.triplePredicate = pred;
		this.tripleObject = obj;
	}
	
	public String toString(){
		String strSjt = this.tripleSubject;
		String strPcd = this.triplePredicate;
		String strObj = this.tripleObject;
		
		int indexSjt = this.tripleSubject.lastIndexOf("/") + 1;
		int indexPcd = this.triplePredicate.lastIndexOf("/") + 1;
		
		if(indexSjt != -1){
			strSjt = this.tripleSubject.substring(indexSjt);
		}
		if(indexPcd != -1){
			strPcd = this.triplePredicate.substring(indexPcd);
		}
		
		return strSjt + " | " + strPcd + " | " + strObj;
	}

	public String getTripleSubject() {
		return tripleSubject;
	}

	public void setTripleSubject(String tripleSubject) {
		this.tripleSubject = tripleSubject;
	}

	public String getTriplePredicate() {
		return triplePredicate;
	}

	public void setTriplePredicate(String triplePredicate) {
		this.triplePredicate = triplePredicate;
	}

	public String getTripleObject() {
		return tripleObject;
	}

	public void setTripleObject(String tripleObject) {
		this.tripleObject = tripleObject;
	}
	
}
