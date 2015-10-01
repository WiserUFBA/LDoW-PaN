package br.ufba.dcc.rlive.processing.mapping_and_load.elements;

public class RLSpecifier {
	private String specType;
	private String specURI;
	private String specPresentSpec;
	private String label;
	
	
	
	public RLSpecifier(){}
	
	public RLSpecifier(String type){
		this.specType = type;
	}

	public String getSpecType() {
		return specType;
	}

	public void setSpecType(String specType) {
		this.specType = specType;
	}

	public String getSpecURI() {
		return specURI;
	}

	public void setSpecURI(String specURI) {
		this.specURI = specURI;
	}

	public String getSpecPresentSpec() {
		return specPresentSpec;
	}

	public void setSpecPresentSpec(String specPresentSpec) {
		this.specPresentSpec = specPresentSpec;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
