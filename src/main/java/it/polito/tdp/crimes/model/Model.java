package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.sun.javafx.font.directwrite.DWGlyphLayout;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Event e;
	private Graph<String,DefaultWeightedEdge> grafo;
	private EventsDao dao;
	private Map<Long,Event> crimini;
	private List<Event> listaCrimini;
	private List<String> listaVertici;
	private List<Crimini> listaArchi;
	private double media;
	private List<Crimini> result =new ArrayList<>();
	private List<String> best =new ArrayList<>();
	
	
	public Model() {
		super();
		this.grafo=new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.dao=new EventsDao();
		this.crimini=new HashMap<Long,Event>();
		dao.listAllEvents(crimini);
		this.listaCrimini=new ArrayList<>(crimini.values());
		this.listaArchi=new ArrayList<>();
	}
	
	public List<Event> getList(){
		return listaCrimini;
	}
	
	public void creaGrafo(String categoria,int mese) {
		this.listaVertici = new ArrayList<>(dao.getVertex(categoria, mese)); 
		listaArchi = dao.getArchi(categoria, mese);
		
		//Aggiunta vertici
		Graphs.addAllVertices(grafo, listaVertici);
		//Aggiunta Archi
		for(Crimini c : listaArchi) {
			if(c.getPeso()!=0){
				if(this.grafo.getEdge(c.getType1(), c.getType2())==null ) {
				Graphs.addEdgeWithVertices(grafo, c.getType1(), c.getType2(), c.getPeso());
				}
		}

	}
		

}
	
	public double getMedia() {
		double somma=0.0;
		double tot=0.0;
		
		for(Crimini c : listaArchi) {
			somma += c.getPeso();
			if(c.getPeso()!=0) {
				tot++;
			}
		}
		
		

	
		return somma/tot;
	}
	
	public List<Crimini> ottieniArchi(){
		
		for(Crimini c : listaArchi) {
			if(c.getPeso()>this.getMedia())
			result.add(new Crimini(c.getType1(),c.getType2(),c.getPeso()));
		}
		return result;
	}
	
	public List<String> trovaPercorso(String sorgente, String destinazione) {
		List<String> parziale = new ArrayList<>();
		this.best = new ArrayList<>();
		parziale.add(sorgente);
		trovaRiscorsivo(destinazione,parziale, 0);
		return this.best;
	}

	private void trovaRiscorsivo(String destinazione, List<String> parziale, int L) {

		//CASO TERMINALE? -> quando l'ultimo vertice inserito in parziale è uguale alla destinazione
		if(parziale.get(parziale.size() - 1).equals(destinazione)) {
			if(parziale.size() > this.best.size()) {
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		
		//scorro i vicini dell'ultimo vertice inserito in parziale
		for(String vicino : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size() -1 ))) {
			//cammino aciclico -> controllo che il vertice non sia già in parziale
			if(!parziale.contains(vicino)) {
				//provo ad aggiungere
				parziale.add(vicino);
				//continuo la ricorsione
				this.trovaRiscorsivo(destinazione, parziale, L+1);
				//faccio backtracking
				parziale.remove(parziale.size() -1);
			}
		}
	}

		
	
	
	
	
	}
	

	
	
	

