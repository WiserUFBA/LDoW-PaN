package br.ufba.dcc.rlive.processing.interface_preparation.elements;

public class PNNews {
	private String newsUrl;
	private String newsSnippet;
	private String newsPubDate;
	private String newsSource;
	private String newsBylineOriginal;
	private String newsAbstract;
	private String newsMultimediaUrl;
	
	public PNNews(){
		this.newsUrl = new String();
		this.newsSnippet = new String();
		this.newsPubDate = new String();
		this.newsSource = new String();
		this.newsBylineOriginal = new String();
		this.newsAbstract = new String();
		this.newsMultimediaUrl = new String();
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getNewsSnippet() {
		return newsSnippet;
	}

	public void setNewsSnippet(String newsSnippet) {
		this.newsSnippet = newsSnippet;
	}

	public String getNewsPubDate() {
		return newsPubDate;
	}

	public void setNewsPubDate(String newsPubDate) {
		this.newsPubDate = newsPubDate;
	}

	public String getNewsSource() {
		return newsSource;
	}

	public void setNewsSource(String newsSource) {
		this.newsSource = newsSource;
	}

	public String getNewsBylineOriginal() {
		return newsBylineOriginal;
	}

	public void setNewsBylineOriginal(String newsBylineOriginal) {
		this.newsBylineOriginal = newsBylineOriginal;
	}

	public String getNewsAbstract() {
		return newsAbstract;
	}

	public void setNewsAbstract(String newsAbstract) {
		this.newsAbstract = newsAbstract;
	}

	public String getNewsMultimediaUrl() {
		return newsMultimediaUrl;
	}

	public void setNewsMultimediaUrl(String newsMultimediaUrl) {
		this.newsMultimediaUrl = newsMultimediaUrl;
	}
	
}
