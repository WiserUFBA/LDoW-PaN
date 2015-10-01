package br.ufba.dcc.rlive.processing.mapping_and_load.elements;


public class RLLink {
	private RLSpecifier linkSubject;
	private RLSpecifier linkPredicate;
	private RLSpecifier linkObject;
	
	
	
	public RLLink(){
		this.linkSubject = new RLSpecifier("subject");
		this.linkPredicate = new RLSpecifier("predicate");
		this.linkObject = new RLSpecifier("object");
	}
	
	public String toString(){
		return this.linkPredicate.getLabel();
	}

	public RLSpecifier getLinkSubject() {
		return linkSubject;
	}

	public void setLinkSubject(RLSpecifier linkSubject) {
		this.linkSubject = linkSubject;
	}

	public RLSpecifier getLinkPredicate() {
		return linkPredicate;
	}

	public void setLinkPredicate(RLSpecifier linkPredicate) {
		this.linkPredicate = linkPredicate;
	}

	public RLSpecifier getLinkObject() {
		return linkObject;
	}

	public void setLinkObject(RLSpecifier linkObject) {
		this.linkObject = linkObject;
	}

}
