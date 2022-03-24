package aubin.testsvisuels;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import aubin.classes.Outils;
import aubin.classes.PasserelleServicesWebXML;
import aubin.classes.PointDeTrace;
import aubin.classes.Trace;
import aubin.classes.Utilisateur;

public class TestPasserelleServicesWebXML {

	public static void main(String[] args) throws ParseException {
		
		String msg;
	
		// test visuel de la méthode getTousLesUtilisateurs
		ArrayList<Utilisateur> lesUtilisateurs = new ArrayList<Utilisateur>();
		msg = PasserelleServicesWebXML.getTousLesUtilisateurs("europa", Outils.sha1("Mdputilisateur1"), lesUtilisateurs);
		// affichage de la réponse
		System.out.println(msg);
		// affichage du nombre d'utilisateurs
		System.out.println("Nombre d'utilisateurs : " + lesUtilisateurs.size());
		// affichage de tous les utilisateurs
		for (Utilisateur unUtilisateur : lesUtilisateurs)
		{ System.out.println(unUtilisateur.toString());
		}

		/*
		// test visuel de la méthode getLesUtilisateursQueJautorise
		ArrayList<Utilisateur> lesUtilisateurs = new ArrayList<Utilisateur>();
		msg = PasserelleServicesWebXML.getLesUtilisateursQueJautorise("europa", Outils.sha1("Mdputilisateur1"), lesUtilisateurs);
		// affichage de la réponse
		System.out.println(msg);
		// affichage du nombre d'utilisateurs
		System.out.println("Nombre d'utilisateurs : " + lesUtilisateurs.size());
		// affichage de tous les utilisateurs
		for (Utilisateur unUtilisateur : lesUtilisateurs)
		{ System.out.println(unUtilisateur.toString());
		}
		*/
		/*
		// test visuel de la méthode getLesUtilisateursQuiMautorisent
		ArrayList<Utilisateur> lesUtilisateurs = new ArrayList<Utilisateur>();
		msg = PasserelleServicesWebXML.getLesUtilisateursQuiMautorisent("europa", Outils.sha1("Mdputilisateur1"), lesUtilisateurs);
		// affichage de la réponse
		System.out.println(msg);
		// affichage du nombre d'utilisateurs
		System.out.println("Nombre d'utilisateurs : " + lesUtilisateurs.size());
		// affichage de tous les utilisateurs
		for (Utilisateur unUtilisateur : lesUtilisateurs)
		{ System.out.println(unUtilisateur.toString());
		}
		*/

		// test visuel de la méthode getLesParcoursDunUtilisateur
		Trace laTrace = new Trace();
		msg = PasserelleServicesWebXML.getUnParcoursEtSesPoints("europa", Outils.sha1("Mdputilisateur1"), 2, laTrace);
		// affichage de la réponse
		System.out.println(msg);
		// affichage de la trace
		System.out.println(laTrace.toString());

		
		// test visuel de la méthode getUnParcoursEtSesPoints
		ArrayList<Trace> lesTraces = new ArrayList<Trace>();
		msg = PasserelleServicesWebXML.getLesParcoursDunUtilisateur("europa", Outils.sha1("Mdputilisateur1"),
				"callisto", lesTraces);
		// affichage de la réponse
		System.out.println(msg);
		// affichage du nombre de traces
		System.out.println("Nombre de traces : " + lesTraces.size());
		// affichage de toutes les traces
		for (Trace uneTrace : lesTraces)
		{ System.out.println(uneTrace.toString());
		}


	} // fin Main
} // fin class
