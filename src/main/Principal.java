// Créé par Antoine La Boissière

package main;

import java.io.*;
import java.util.ArrayList;
import java.time.LocalDate;

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

			envoyerFacture( facture );

		} else {

			erreur();

		}

	}

	public static ArrayList<String> preparation( ArrayList<String> fichier ) throws IOException {

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

	public static ArrayList<String> separation( ArrayList<String> fichier, String debut, String fin ) {

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

	public static boolean validationToutesCommandes( ArrayList<String> clients, ArrayList<String> plats,
			ArrayList<String> commandes ) {

		boolean validation = true;

		for ( int y = 0; y < commandes.size(); y++ ) {

			validation = validationUneCommande( clients, plats, commandes.get( y ) );

			if ( !validation ) {

				y = commandes.size();

			}

		}

		return validation;

	}

	public static boolean validationUneCommande( ArrayList<String> clients, ArrayList<String> plats,
			String commandes ) {

		boolean validation = false;
		boolean estUnClient = false;
		boolean estUnPlat = false;
		boolean estUneQuantite = false;

		for ( int y = 0; y < clients.size(); y++ ) {

			if ( commandes.contains( clients.get( y ) ) ) {

				estUnClient = true;

			}

		}

		erreurValidation( estUnClient, "Le nom", commandes.split( " " )[0] );

		for ( int y = 0; y < plats.size(); y++ ) {

			if ( commandes.contains( plats.get( y ).split( " " )[0] ) ) {

				estUnPlat = true;

			}

		}

		erreurValidation( estUnPlat, "Le plat", commandes.split( " " )[1] );

		estUneQuantite = estUnNombre( commandes );

		validation = isAllTrue( estUnClient, estUnPlat, estUneQuantite );

		return validation;

	}

	public static void erreurValidation( boolean validation, String variable, String erreur ) {

		if ( !validation ) {

			messageErreurValidation( variable, erreur );

		}

	}

	public static void messageErreurValidation( String variable, String erreur ) {

		System.out.println( "Erreur : " + variable + " " + erreur + " de la commande n'est pas valide !" );

	}

	public static boolean estUnNombre( String commandes ) {

		boolean validation = false;

		try {

			Integer.parseInt( commandes.split( " " )[2] );

			validation = true;

		} catch ( NumberFormatException ex ) {

			messageErreurValidation( "La quantité", commandes.split( " " )[2] );

		}

		return validation;

	}

	public static boolean isAllTrue( boolean var1, boolean var2, boolean var3 ) {

		if ( var1 && var2 && var3 ) {

			return true;

		} else {

			return false;

		}

	}

	public static ArrayList<Client> formerFacture( ArrayList<String> clients, ArrayList<String> plats,
			ArrayList<String> commandes ) {

		ArrayList<Client> facture = new ArrayList<Client>();

		ArrayList<String> platsTitre = new ArrayList<String>();
		ArrayList<String> platsPrix = new ArrayList<String>();

		ArrayList<String> commandesNom = new ArrayList<String>();
		ArrayList<String> commandesTitre = new ArrayList<String>();
		ArrayList<String> commandesQuantite = new ArrayList<String>();

		// Taxes

		final float TPS = 0.05f;
		final float TVQ = 0.09975f;

		// Séparer les plats

		separerPlats( plats, platsTitre, platsPrix );

		// Séparer les commandes

		separerCommandes( commandes, commandesNom, commandesTitre, commandesQuantite );

		// Former le prix
		
		formerPrix( facture, platsTitre, platsPrix, commandesNom, commandesTitre, commandesQuantite, clients );

		// Appliquer les taxes

		appliquerTaxes( facture, TPS, TVQ );

		return facture;

	}

	public static void separerPlats( ArrayList<String> plats, ArrayList<String> platsTitre,
			ArrayList<String> platsPrix ) {

		for ( int y = 0; y < plats.size(); y++ ) {

			platsTitre.add( plats.get( y ).split( " " )[0] );
			platsPrix.add( plats.get( y ).split( " " )[1] );

		}

	}

	public static void separerCommandes( ArrayList<String> commandes, ArrayList<String> commandesNom,
			ArrayList<String> commandesTitre, ArrayList<String> commandesQuantite ) {

		for ( int y = 0; y < commandes.size(); y++ ) {

			commandesNom.add( commandes.get( y ).split( " " )[0] );
			commandesTitre.add( commandes.get( y ).split( " " )[1] );
			commandesQuantite.add( commandes.get( y ).split( " " )[2] );

		}

	}

	public static void formerPrix( ArrayList<Client> facture, ArrayList<String> platsTitre,
			ArrayList<String> platsPrix, ArrayList<String> commandesNom, ArrayList<String> commandesTitre,
			ArrayList<String> commandesQuantite, ArrayList<String> clients ) {

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

	}
	
	public static void appliquerTaxes(ArrayList<Client> facture, float TPS, float TVQ) {
		
		for ( int j = 0; j < facture.size(); j++ ) {

			float prixAvantTaxes, prixApresTaxes;
			prixAvantTaxes = facture.get( j ).prix;
			prixApresTaxes = prixAvantTaxes + prixAvantTaxes * TPS + prixAvantTaxes * TVQ;

			facture.get( j ).prix = prixApresTaxes;

		}
		
	}

	public static void envoyerFacture( ArrayList<Client> facture )
			throws FileNotFoundException, UnsupportedEncodingException {

		String fichierRetour = "";

		fichierRetour += "Bienvenue  chez  Barette !\n" + "Factures :\n";

		for ( int i = 0; i < facture.size(); i++ ) {

			fichierRetour += facture.get( i ).afficher();

		}

		System.out.println( fichierRetour );

		PrintWriter writerPrint = new PrintWriter( "Facture-du-" + LocalDate.now() + ".txt", "UTF-8" );

		try (FileWriter writerFile = new FileWriter( "Facture-du-" + LocalDate.now() + ".txt" );
				BufferedWriter bw = new BufferedWriter( writerFile )) {

			bw.write( fichierRetour );

		} catch ( IOException e ) {

			System.err.format( "IOException: %s%n", e );

		}

	}

	public static void erreur() throws FileNotFoundException, UnsupportedEncodingException {

		String erreur = "Vous avez une erreur dans l'une de vos commandes !\n"
				+ "Vérifiez le nom de la commande ou du client !";

		PrintWriter writerPrint = new PrintWriter( "Facture-du-" + LocalDate.now() + ".txt", "UTF-8" );

		try (FileWriter writer = new FileWriter( "Facture-du-" + LocalDate.now() + ".txt" );
				BufferedWriter bw = new BufferedWriter( writer )) {

			bw.write( erreur );

		} catch ( IOException e ) {

			System.err.format( "IOException: %s%n", e );

		}

	}
}