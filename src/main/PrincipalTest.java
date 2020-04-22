package main;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class PrincipalTest {

	@Test
	public void testEstUnNombre() {
		
		assertTrue( Principal.estUnNombre( "Céline Frites 2" ) );
		assertFalse( Principal.estUnNombre( "Céline Frites 2.5" ) );
		
	}
	
	@Test
	public void testIsAllTrue() {
		
		assertTrue( Principal.isAllTrue( true, true, true ) );
		assertFalse( Principal.isAllTrue( true, false, true ) );
		assertFalse( Principal.isAllTrue( false, false, false ) );
		
	}
	
	@Test
	public void testFormerFacture() {
		
		ArrayList<String> clients = new ArrayList<String>();
		ArrayList<String> plats = new ArrayList<String>();
		ArrayList<String> commandes = new ArrayList<String>();
		
		clients.add( "Antoine" );
		plats.add( "Poulet 10.5" );
		commandes.add( "Antoine Poulet 2" );
		
		assertNotNull( Principal.formerFacture( clients, plats, commandes ) );
		
	}
	
	@Test
	public void testValidationToutesCommandes() {
		
		ArrayList<String> clients = new ArrayList<String>();
		ArrayList<String> plats = new ArrayList<String>();
		ArrayList<String> commandes = new ArrayList<String>();
		
		clients.add( "Antoine" );
		plats.add( "Poulet 10.5" );
		commandes.add( "Antoine Poulet 2" );
		
		assertTrue( Principal.validationToutesCommandes( clients, plats, commandes ) );
		
	}
	
	@Test
	public void testSeparation() {
		
		ArrayList<String> fichier = new ArrayList<String>();
		String debut = "Clients :";
		String fin = "Fin";
		
		fichier.add( "Clients :" );
		fichier.add( "Antoine" );
		fichier.add( "Fin" );
		
		assertNotNull( Principal.separation( fichier, debut, fin ) );
		
	}
	
	@Test
	public void testPreparation() throws IOException {
		
		ArrayList<String> fichier = new ArrayList<String>();
		
		assertNotNull( Principal.preparation( fichier ) );
		
	}
	
	@Test
	public void testMain() throws IOException {
	    Principal.main(new String[] {});
	}

}
