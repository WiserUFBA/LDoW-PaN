package br.ufba.dcc.rlive.processing.interface_preparation.elements;

import java.util.ArrayList;
import java.util.List;

import br.ufba.dcc.rlive.processing.base.operations.BasicOperation;

public class PNGallery {
	private String atomUID;
	private String atomLabel;
	private List <PNMedia> media;
	
	
	
	
	public PNGallery(){
		this.atomUID = new String();
		this.atomLabel = new String();
		this.media = new ArrayList<PNMedia>();
	}
	
	public String toString(){
		String str;
		try {
			str = this.atomLabel + "\n\n";
			for(int i=0; i<this.media.size(); i++){
				str = str + BasicOperation.getURIWithoutPrefix(this.media.toString()) + "\n";
			}
		} catch (Exception e) {
			str = null;
		}
		
		return str;
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

	public List<PNMedia> getMedia() {
		return media;
	}

	public void setMedia(List<PNMedia> media) {
		this.media = media;
	}
	
}
