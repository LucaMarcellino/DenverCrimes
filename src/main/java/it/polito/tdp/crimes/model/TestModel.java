package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		Model model= new Model();
		model.creaGrafo("white-collar-crime", 7);
		System.out.println(model.getMedia());
		Crimini c = new Crimini("forgery-checks","fraud-identity-theft",15);
	//	System.out.println(model.trovaPercoso(c));
	}

	

	
}
