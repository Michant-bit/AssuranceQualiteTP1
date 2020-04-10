// Créé par Antoine La Boissière

package projet;

import java.io.*;
import java.util.ArrayList;

public class Principal {

	public static void main( String[] args ) throws IOException {

		ArrayList<String> fichier = new ArrayList<String>();

		ArrayList<Client> facture = new ArrayList<Client>();

		ArrayList<String> clients = new ArrayList<String>();
		ArrayList<String> plats = new ArrayList<String>();
		ArrayList<String> commandes = new ArrayList<String>();

		boolean validation;

		fichier = preparation( fichier );

		clients = separation( fichier, "Clients", "Plats" );
		plats = separation( fichier, "Plats", "Commandes" );
		commandes = separation( fichier, "Commandes", "Fin" );

		validation = validationToutesCommandes( clients, plats, commandes );

		if ( validation ) {

			facture = formerFacture( clients, plats, commandes );
			
			envoyerFacture(facture);

		} else {
			
			erreur();

		}

	}

	private static ArrayList<String> preparation( ArrayList<String> fichier ) throws IOException {

		BufferedReader lecteurAvecBuffer = null;
		String ligne = "";

		try {

			lecteurAvecBuffer = new BufferedReader( new FileReader( "test.txt" ) );

		}

		catch ( FileNotFoundException exc ) {

			System.out.println( "Erreur d'ouverture" );

		}

		while ( ( ligne = lecteurAvecBuffer.readLine() ) != null ) {

			fichier.add( ligne );

		}

		lecteurAvecBuffer.close();

		return fichier;

	}

	private static ArrayList<String> separation( ArrayList<String> fichier, String debut, String fin ) {

		ArrayList<String> liste = new ArrayList<String>();

		int indDebut = 0;
		int indFin = 0;

		for ( int i = 0; i < fichier.size(); i++ ) {

			if ( fichier.get( i ).contains( debut ) ) {

				indDebut = i + 1;

			}

			if ( fichier.get( i ).contains( fin ) ) {

				indFin = i;

			}

		}

		for ( int i = indDebut; i < indFin; i++ ) {

			liste.add( fichier.get( i ) );

		}

		return liste;
	}

	private static boolean validationToutesCommandes( ArrayList<String> clients, ArrayList<String> plats,
			ArrayList<String> commandes ) {

		boolean validation = true;

		for ( int y = 0; y < commandes.size(); y++ ) {

			validation = validationUneCommande( clients, plats, commandes.get( y ) );
			
			if(!validation) {
				
				y = commandes.size();
				
			}

		}

		return validation;

	}

	private static boolean validationUneCommande( ArrayList<String> clients, ArrayList<String> plats,
			String commandes ) {

		boolean validation = false;
		boolean estUnClient = false;
		boolean estUnPlat = false;

		for ( int y = 0; y < clients.size(); y++ ) {

			if ( commandes.contains( clients.get( y ) ) ) {

				estUnClient = true;

			}

		}

		for ( int y = 0; y < plats.size(); y++ ) {

			if ( commandes.contains( plats.get( y ).split( " " )[0] ) ) {

				estUnPlat = true;

			}

		}

		if ( estUnClient && estUnPlat ) {

			validation = true;

		}

		return validation;

	}

	private static ArrayList<Client> formerFacture( ArrayList<String> clients, ArrayList<String> plats,
			ArrayList<String> commandes ) {

		ArrayList<Client> facture = new ArrayList<Client>();

		ArrayList<String> platsTitre = new ArrayList<String>();
		ArrayList<String> platsPrix = new ArrayList<String>();

		ArrayList<String> commandesNom = new ArrayList<String>();
		ArrayList<String> commandesTitre = new ArrayList<String>();
		ArrayList<String> commandesQuantite = new ArrayList<String>();

		// Séparer les plats

		for ( int y = 0; y < plats.size(); y++ ) {

			platsTitre.add( plats.get( y ).split( " " )[0] );
			platsPrix.add( plats.get( y ).split( " " )[1] );

		}

		// Séparer les commandes

		for ( int y = 0; y < commandes.size(); y++ ) {

			commandesNom.add( commandes.get( y ).split( " " )[0] );
			commandesTitre.add( commandes.get( y ).split( " " )[1] );
			commandesQuantite.add( commandes.get( y ).split( " " )[2] );

		}

		// Former le prix

		for ( int i = 0; i < commandesTitre.size(); i++ ) {

			for ( int y = 0; y < platsTitre.size(); y++ ) {

				if ( commandesTitre.get( i ).contains( platsTitre.get( y ) ) ) {

					if ( facture.size() == 0 ) {

						Client client = new Client();
						client.nom = commandesNom.get( i );
						client.prix = Float.parseFloat( commandesQuantite.get( y ) )
								* Float.parseFloat( platsPrix.get( i ) );

						facture.add( client );

						clients.remove( 0 );

					} else {

						for ( int x = 0; x < facture.size(); x++ ) {

							if ( commandesNom.get( i ).contains( facture.get( x ).nom ) ) {

								facture.get( x ).prix += Float.parseFloat( commandesQuantite.get( y ) )
										* Float.parseFloat( platsPrix.get( i ) );

							} else if ( x == facture.size() - 1 ) {

								Client client = new Client();
								client.nom = commandesNom.get( i );
								client.prix = Float.parseFloat( commandesQuantite.get( y ) )
										* Float.parseFloat( platsPrix.get( i ) );

								facture.add( client );

								clients.remove( 0 );

								x = facture.size();

							}

						}

					}

				}

			}

		}

		// Chercher si un client n'a pas de commande

		if ( clients.size() != 0 ) {

			for ( int i = 0; i < clients.size(); i++ ) {

				Client client = new Client();
				client.nom = clients.get( i );
				client.prix = 0;

				facture.add( client );

				clients.remove( 0 );

			}

		}

		return facture;

	}

	private static void envoyerFacture( ArrayList<Client> facture ) {

		String fichierRetour = "";

		fichierRetour += "Bienvenue  chez  Barette !\n"
				+ "Factures :\n";

		for ( int i = 0; i < facture.size(); i++ ) {

			fichierRetour += facture.get( i ).afficher();

		}

		try (FileWriter writer = new FileWriter( "Sortie.txt" ); BufferedWriter bw = new BufferedWriter( writer )) {

			bw.write( fichierRetour );

		} catch ( IOException e ) {

			System.err.format( "IOException: %s%n", e );

		}

	}
	
	private static void erreur() {
		
		String erreur = "Vous avez une erreur dans l'une de vos commandes !\n"
				+ "Vérifiez le nom de la commande ou du client !";
		
		try (FileWriter writer = new FileWriter( "Sortie.txt" ); BufferedWriter bw = new BufferedWriter( writer )) {

			bw.write( erreur );

		} catch ( IOException e ) {

			System.err.format( "IOException: %s%n", e );

		}
		
	}
}