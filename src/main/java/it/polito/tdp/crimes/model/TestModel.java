package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		Model model= new Model();
		model.creaGrafo("arson", 3);
		System.out.println(model.getMedia());
	}

}
