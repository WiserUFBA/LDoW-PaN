package test;

import javax.xml.bind.annotation.XmlRootElement;

import br.ufba.dcc.rlive.processing.interface_preparation.elements.PNBox;

@XmlRootElement
public class MixerBoxWithTodo {
	private PNBox box;
	private Todo todo;
	
	public MixerBoxWithTodo(){}

	public PNBox getBox() {
		return box;
	}

	public void setBox(PNBox box) {
		this.box = box;
	}

	public Todo getTodo() {
		return todo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}
	
}
