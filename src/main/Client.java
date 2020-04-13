package main;

import java.text.DecimalFormat;

public class Client {
	
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	String nom;
	float prix;
	
	public Client() {
		
	}
	
	public Client(String nom, float prix) {
		
		this.nom = nom;
		this.prix += prix;
		
	}
	
	public String afficher() {
		
		return nom + " " + df.format(prix) + " $\n";
		
	}

}
