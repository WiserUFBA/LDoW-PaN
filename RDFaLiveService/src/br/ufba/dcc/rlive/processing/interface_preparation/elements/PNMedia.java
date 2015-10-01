package br.ufba.dcc.rlive.processing.interface_preparation.elements;

public class PNMedia {
	private String mediaURL;
	private String mediaURLAux;
	private String mediaCaption;
	private String mediaInfo;
	
	
	
	public PNMedia(){}
	
	public String toString(){
		return this.mediaURL;
	}

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public String getMediaURLAux() {
		return mediaURLAux;
	}

	public void setMediaURLAux(String mediaURLAux) {
		this.mediaURLAux = mediaURLAux;
	}

	public String getMediaCaption() {
		return mediaCaption;
	}

	public void setMediaCaption(String mediaCaption) {
		this.mediaCaption = mediaCaption;
	}

	public String getMediaInfo() {
		return mediaInfo;
	}

	public void setMediaInfo(String mediaInfo) {
		this.mediaInfo = mediaInfo;
	}
	
}
