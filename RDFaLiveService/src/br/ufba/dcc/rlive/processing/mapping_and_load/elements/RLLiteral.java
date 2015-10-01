package br.ufba.dcc.rlive.processing.mapping_and_load.elements;

public class RLLiteral {
	private String litPredicate;
	private String litValue;
	
	
	
	public String toString(){
		return this.litValue;
	}
	
	public String getLitPredicate() {
		return litPredicate;
	}
	
	public void setLitPredicate(String litPredicate) {
		this.litPredicate = litPredicate;
	}
	
	public String getLitValue() {
		return litValue;
	}
	
	public void setLitValue(String litValue) {
		this.litValue = litValue;
	}
	
}
