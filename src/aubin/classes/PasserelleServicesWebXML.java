// Projet TraceGPS - API Java
// Fichier : PasserelleServicesWeb.java
// Cette classe hérite de la classe Passerelle
// Elle fournit des méthodes pour appeler les différents services web
// Elle utilise le modèle Jaxp pour parcourir le document XML
// Le modèle Jaxp fait partie du JDK (et également du SDK Android)
//
/**
 * Description
 * Cette classe hérite de la classe Passerelle
 * Elle fournit des méthodes pour appeler les différents services web
 * Elle utilise le modèle Jaxp pour parcourir le document XML
 */
package aubin.classes;

import java.util.ArrayList;
import java.util.Date;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PasserelleServicesWebXML extends PasserelleXML {

	// attributs privés
	private static String formatDateUS = "yyyy-MM-dd HH:mm:ss";

	// Adresse de l'hébergeur Internet
	//private static String _adresseHebergeur = "http://sio.lyceedelasalle.fr/tracegps/api/";
	// Adresse du localhost en cas d'exécution sur le poste de développement (projet de tests des classes)
	private static String _adresseHebergeur = "http://localhost/ws-php-aubin/traceGpsJquery/api/";

	// Noms des services web déjà traités par la passerelle
	private static String _urlArreterEnregistrementParcours = "ArreterEnregistrementParcours";
	private static String _urlChangerDeMdp = "ChangerDeMdp";
	private static String _urlConnecter = "Connecter";
	private static String _urlCreerUnUtilisateur = "CreerUnUtilisateur";
	private static String _urlDemanderMdp = "DemanderMdp";
	private static String _urlDemanderUneAutorisation = "DemanderUneAutorisation";
	private static String _urlDemarrerEnregistrementParcours = "DemarrerEnregistrementParcours";
	private static String _urlEnvoyerPosition = "EnvoyerPosition";
	private static String _urlGetLesParcoursDunUtilisateur = "GetLesParcoursDunUtilisateur";
	private static String _urlGetLesUtilisateursQueJautorise = "GetLesUtilisateursQueJautorise";
	private static String _urlGetLesUtilisateursQuiMautorisent = "GetLesUtilisateursQuiMautorisent";
	private static String _urlGetTousLesUtilisateurs = "GetTousLesUtilisateurs";
	private static String _urlGetUnParcoursEtSesPoints = "GetUnParcoursEtSesPoints";
	private static String _urlRetirerUneAutorisation = "RetirerUneAutorisation";
	private static String _urlSupprimerUnUtilisateur = "SupprimerUnUtilisateur";
	private static String _urlSupprimerUnParcours = "SupprimerUnParcours";

	// -------------------------------------------------------------------------------------------------
	// ------------------------------------- méthodes déjà développées ---------------------------------
	// -------------------------------------------------------------------------------------------------

	/**
	 * Description
	 *  Méthode statique pour se connecter (service Connecter)
	*    @param pseudo : le pseudo de l'utilisateur qui fait appel au service web
	 *   @param mdpSha1 : le mot de passe hashé en sha1
	* 		@author aubin
	*/
	public static String connecter(String pseudo, String mdpSha1)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlConnecter;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * 	Méthode statique pour obtenir la liste de tous les utilisateurs de niveau 1 (service GetTousLesUtilisateurs)
	 * @param pseudo : le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 : le mot de passe hashé en sha1
	 * @param lesUtilisateurs : collection (vide) à remplir à partir des données fournies par le service web
	 * @author aubin
 	 */

	public static String getTousLesUtilisateurs(String pseudo, String mdpSha1, ArrayList<Utilisateur> lesUtilisateurs)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlGetTousLesUtilisateurs;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			NodeList listeNoeudsUtilisateurs = leDocument.getElementsByTagName("utilisateur");
			/* Exemple de données obtenues pour un utilisateur :
				<utilisateur>
					<id>2</id>
					<pseudo>callisto</pseudo>
					<adrMail>delasalle.sio.eleves@gmail.com</adrMail>
					<numTel>22.33.44.55.66</numTel>
					<niveau>1</niveau>
					<dateCreation>2018-01-19 20:11:24</dateCreation>
					<nbTraces>2</nbTraces>
					<dateDerniereTrace>2018-01-19 13:08:48</dateDerniereTrace>
				</utilisateur>
			 */

			// vider d'abord la collection avant de la remplir
			lesUtilisateurs.clear();

			// parcours de la liste des noeuds <utilisateur> et ajout dans la collection lesUtilisateurs
			for (int i = 0 ; i <= listeNoeudsUtilisateurs.getLength()-1 ; i++)
			{	// création de l'élément courant à chaque tour de boucle
				Element courant = (Element) listeNoeudsUtilisateurs.item(i);

				// lecture des balises intérieures
				int unId = Integer.parseInt(courant.getElementsByTagName("id").item(0).getTextContent());
				String unPseudo = courant.getElementsByTagName("pseudo").item(0).getTextContent();
				String unMdpSha1 = "";								// par sécurité, on ne récupère pas le mot de passe
				String uneAdrMail = courant.getElementsByTagName("adrMail").item(0).getTextContent();
				String unNumTel = courant.getElementsByTagName("numTel").item(0).getTextContent();
				int unNiveau = Integer.parseInt(courant.getElementsByTagName("niveau").item(0).getTextContent());
				Date uneDateCreation = Outils.convertirEnDate(courant.getElementsByTagName("dateCreation").item(0).getTextContent(), formatDateUS);
				int unNbTraces = Integer.parseInt(courant.getElementsByTagName("nbTraces").item(0).getTextContent());
				Date uneDateDerniereTrace = null;
				if (unNbTraces > 0)
					uneDateDerniereTrace = Outils.convertirEnDate(courant.getElementsByTagName("dateDerniereTrace").item(0).getTextContent(), formatDateUS);

				// crée un objet Utilisateur
				Utilisateur unUtilisateur = new Utilisateur(unId, unPseudo, unMdpSha1, uneAdrMail, unNumTel, unNiveau, uneDateCreation, unNbTraces, uneDateDerniereTrace);

				// ajoute l'utilisateur à la collection lesUtilisateurs
				lesUtilisateurs.add(unUtilisateur);
			}

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour créer un utilisateur (service CreerUnUtilisateur)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param adrMail son adresse mail
	 * @param numTel  son numéro de téléphone
	 * @author aubin
	 */
	public static String creerUnUtilisateur(String pseudo, String adrMail, String numTel)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlCreerUnUtilisateur;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&adrMail=" + adrMail;
			urlDuServiceWeb += "&numTel=" + numTel;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Ce service permet à un administrateur de supprimer un utilisateur (à condition qu'il ne possède aucune trace enregistrée)
	 * @param pseudo le pseudo de l'administrateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param pseudoAsupprimer le pseudo de l'utilisateur à supprimer
	 * @author aubin
	 */
	public static String supprimerUnUtilisateur(String pseudo, String mdpSha1, String pseudoAsupprimer)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlSupprimerUnUtilisateur;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;
			urlDuServiceWeb += "&pseudoAsupprimer=" + pseudoAsupprimer;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour modifier son mot de passe (service ChangerDeMdp)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param nouveauMdp le mot de passe hashé en sha1
	 * @param confirmationMdp la confirmation du nouveau mot de passe
	 * @author aubin
	 */
	public static String changerDeMdp(String pseudo, String mdpSha1, String nouveauMdp, String confirmationMdp)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlChangerDeMdp;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;
			urlDuServiceWeb += "&nouveauMdp=" + nouveauMdp;
			urlDuServiceWeb += "&confirmationMdp=" + confirmationMdp;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	// -------------------------------------------------------------------------------------------------
	// --------------------------------- méthodes restant à développer ---------------------------------
	// -------------------------------------------------------------------------------------------------

	/**
	 * Description
	 * Méthode statique pour demander un nouveau mot de passe (service DemanderMdp)
	 * @param pseudo
	 * @author aubin
	 */

	public static String demanderMdp(String pseudo) {
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlDemanderMdp;
			urlDuServiceWeb += "?pseudo=" + pseudo;


			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 *  Méthode statique pour obtenir la liste des utilisateurs que j'autorise (service GetLesUtilisateursQueJautorise)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param lesUtilisateurs collection (vide) à remplir à partir des données fournies par le service web
	 *@author aubin
	 */

	public static String getLesUtilisateursQueJautorise(String pseudo, String mdpSha1, ArrayList<Utilisateur> lesUtilisateurs)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlGetLesUtilisateursQueJautorise;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			NodeList listeNoeudsUtilisateurs = leDocument.getElementsByTagName("utilisateur");
			/* Exemple de données obtenues pour un utilisateur :
				<utilisateur>
					<id>2</id>
					<pseudo>callisto</pseudo>
					<adrMail>delasalle.sio.eleves@gmail.com</adrMail>
					<numTel>22.33.44.55.66</numTel>
					<niveau>1</niveau>
					<dateCreation>2018-01-19 20:11:24</dateCreation>
					<nbTraces>2</nbTraces>
					<dateDerniereTrace>2018-01-19 13:08:48</dateDerniereTrace>
				</utilisateur>
			 */

			// vider d'abord la collection avant de la remplir
			lesUtilisateurs.clear();

			// parcours de la liste des noeuds <utilisateur> et ajout dans la collection lesUtilisateurs
			for (int i = 0 ; i <= listeNoeudsUtilisateurs.getLength()-1 ; i++)
			{	// création de l'élément courant à chaque tour de boucle
				Element courant = (Element) listeNoeudsUtilisateurs.item(i);

				// lecture des balises intérieures
				int unId = Integer.parseInt(courant.getElementsByTagName("id").item(0).getTextContent());
				String unPseudo = courant.getElementsByTagName("pseudo").item(0).getTextContent();
				String unMdpSha1 = "";								// par sécurité, on ne récupère pas le mot de passe
				String uneAdrMail = courant.getElementsByTagName("adrMail").item(0).getTextContent();
				String unNumTel = courant.getElementsByTagName("numTel").item(0).getTextContent();
				int unNiveau = Integer.parseInt(courant.getElementsByTagName("niveau").item(0).getTextContent());
				Date uneDateCreation = Outils.convertirEnDate(courant.getElementsByTagName("dateCreation").item(0).getTextContent(), formatDateUS);
				int unNbTraces = Integer.parseInt(courant.getElementsByTagName("nbTraces").item(0).getTextContent());
				Date uneDateDerniereTrace = null;
				if (unNbTraces > 0)
					uneDateDerniereTrace = Outils.convertirEnDate(courant.getElementsByTagName("dateDerniereTrace").item(0).getTextContent(), formatDateUS);

				// crée un objet Utilisateur
				Utilisateur unUtilisateur = new Utilisateur(unId, unPseudo, unMdpSha1, uneAdrMail, unNumTel, unNiveau, uneDateCreation, unNbTraces, uneDateDerniereTrace);

				// ajoute l'utilisateur à la collection lesUtilisateurs
				lesUtilisateurs.add(unUtilisateur);
			}

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour obtenir la liste des utilisateurs qui m'autorisent (service GetLesUtilisateursQuiMautorisent)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1  le mot de passe hashé en sha1
	 * @param lesUtilisateurs  collection (vide) à remplir à partir des données fournies par le service web
	 * @author aubin
	 */
	public static String getLesUtilisateursQuiMautorisent(String pseudo, String mdpSha1, ArrayList<Utilisateur> lesUtilisateurs)
	{

		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlGetLesUtilisateursQuiMautorisent;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			NodeList listeNoeudsUtilisateurs = leDocument.getElementsByTagName("utilisateur");
			/* Exemple de données obtenues pour un utilisateur :
				<utilisateur>
					<id>2</id>
					<pseudo>callisto</pseudo>
					<adrMail>delasalle.sio.eleves@gmail.com</adrMail>
					<numTel>22.33.44.55.66</numTel>
					<niveau>1</niveau>
					<dateCreation>2018-01-19 20:11:24</dateCreation>
					<nbTraces>2</nbTraces>
					<dateDerniereTrace>2018-01-19 13:08:48</dateDerniereTrace>
				</utilisateur>
			 */

			// vider d'abord la collection avant de la remplir
			lesUtilisateurs.clear();

			// parcours de la liste des noeuds <utilisateur> et ajout dans la collection lesUtilisateurs
			for (int i = 0 ; i <= listeNoeudsUtilisateurs.getLength()-1 ; i++)
			{	// création de l'élément courant à chaque tour de boucle
				Element courant = (Element) listeNoeudsUtilisateurs.item(i);

				// lecture des balises intérieures
				int unId = Integer.parseInt(courant.getElementsByTagName("id").item(0).getTextContent());
				String unPseudo = courant.getElementsByTagName("pseudo").item(0).getTextContent();
				String unMdpSha1 = "";								// par sécurité, on ne récupère pas le mot de passe
				String uneAdrMail = courant.getElementsByTagName("adrMail").item(0).getTextContent();
				String unNumTel = courant.getElementsByTagName("numTel").item(0).getTextContent();
				int unNiveau = Integer.parseInt(courant.getElementsByTagName("niveau").item(0).getTextContent());
				Date uneDateCreation = Outils.convertirEnDate(courant.getElementsByTagName("dateCreation").item(0).getTextContent(), formatDateUS);
				int unNbTraces = Integer.parseInt(courant.getElementsByTagName("nbTraces").item(0).getTextContent());
				Date uneDateDerniereTrace = null;
				if (unNbTraces > 0)
					uneDateDerniereTrace = Outils.convertirEnDate(courant.getElementsByTagName("dateDerniereTrace").item(0).getTextContent(), formatDateUS);

				// crée un objet Utilisateur
				Utilisateur unUtilisateur = new Utilisateur(unId, unPseudo, unMdpSha1, uneAdrMail, unNumTel, unNiveau, uneDateCreation, unNbTraces, uneDateDerniereTrace);

				// ajoute l'utilisateur à la collection lesUtilisateurs
				lesUtilisateurs.add(unUtilisateur);
			}

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour demander une autorisation (service DemanderUneAutorisation)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param pseudoDestinataire le pseudo de l'utilisateur à qui on demande l'autorisation
	 * @param texteMessage le texte d'un message accompagnant la demande
	 * @param nomPrenom le nom et le prénom du demandeur
	 * @author aubin
	 */

	public static String demanderUneAutorisation(String pseudo, String mdpSha1, String pseudoDestinataire, String texteMessage, String nomPrenom)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlDemanderUneAutorisation;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;
			urlDuServiceWeb += "&pseudoDestinataire=" + pseudoDestinataire;
			urlDuServiceWeb += "&texteMessage=" + texteMessage;
			urlDuServiceWeb += "&nomPrenom=" + nomPrenom;


			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * description
	 * Méthode statique pour retirer une autorisation (service RetirerUneAutorisation)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param pseudoARetirer le pseudo de l'utilisateur à qui on veut retirer l'autorisation
	 * @param texteMessage le texte d'un message pour un éventuel envoi de courriel
	 * @author aubin
	 */

	public static String retirerUneAutorisation(String pseudo, String mdpSha1, String pseudoARetirer, String texteMessage)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlRetirerUneAutorisation;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;
			urlDuServiceWeb += "&pseudoARetirer=" + pseudoARetirer;
			urlDuServiceWeb += "&texteMessage=" + texteMessage;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Methode statique pour envoyer la position de l'utilisateur (service EnvoyerPosition)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param lePoint un objet PointDeTrace (vide) qui permettra de récupérer le numéro attribué à partir des données fournies par le service web
	 * @author aubin
	 */

	public static String envoyerPosition(String pseudo, String mdpSha1, PointDeTrace lePoint)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlEnvoyerPosition;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp="+ mdpSha1;
			urlDuServiceWeb += "&idTrace=" + lePoint.getIdTrace();
			urlDuServiceWeb += "&dateHeure=" + Outils.formaterDateHeureUS(lePoint.getDateHeure()).replace(" ", "%20");
			urlDuServiceWeb += "&latitude=" + lePoint.getLatitude();
			urlDuServiceWeb += "&longitude=" + lePoint.getLongitude();
			urlDuServiceWeb += "&altitude=" + lePoint.getAltitude();
			urlDuServiceWeb += "&rythmeCardio=" + lePoint.getRythmeCardio();


			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour obtenir un parcours et la liste de ses points (service GetUnParcoursEtSesPoints)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param idTrace l'id de la trace à consulter
	 * @param laTrace objet Trace (vide) à remplir à partir des données fournies par le service web
	 * @author aubin
	 */

	public static String getUnParcoursEtSesPoints(String pseudo, String mdpSha1, int idTrace, Trace laTrace)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML Ã  partir de l'URL du service web et des paramÃ¨tres
			String urlDuServiceWeb = _adresseHebergeur + _urlGetUnParcoursEtSesPoints;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdpSha1=" + mdpSha1;
			urlDuServiceWeb += "&idTrace=" + idTrace;


			// crÃ©ation d'un flux en lecture (InputStream) Ã  partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// crÃ©ation d'un objet org.w3c.dom.Document Ã  partir du flux ; il servira Ã  parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			NodeList listeNoeudsTrace = leDocument.getElementsByTagName("trace");

			// crÃ©ation de l'Ã©lÃ©ment courant Ã  chaque tour de boucle
			Element courant = (Element) listeNoeudsTrace.item(0);

			// lecture des balises intÃ©rieures
			int unId = Integer.parseInt(courant.getElementsByTagName("id").item(0).getTextContent());
			Date DateHeureDebut = Outils.convertirEnDate(courant.getElementsByTagName("dateHeureDebut").item(0).getTextContent(), formatDateUS);
			Date DateHeureFin = Outils.convertirEnDate(courant.getElementsByTagName("dateHeureFin").item(0).getTextContent(), formatDateUS);
			boolean terminee = Boolean.parseBoolean(courant.getElementsByTagName("terminee").item(0).getTextContent());
			int idUtilisateur = Integer.parseInt(courant.getElementsByTagName("idUtilisateur").item(0).getTextContent());

			laTrace.setId(unId);
			laTrace.setDateHeureDebut(DateHeureDebut);
			laTrace.setDateHeureFin(DateHeureFin);
			laTrace.setTerminee(terminee);
			laTrace.setIdUtilisateur(idUtilisateur);

			Element lesPoints = (Element) leDocument.getElementsByTagName("lesPoints").item(0);
			NodeList listeNoeudsPoint = lesPoints.getElementsByTagName("point");

			for (int i = 0 ; i <= listeNoeudsPoint.getLength()-1 ; i++)
			{  Element pointCourant = (Element) listeNoeudsPoint.item(i);

				// lecture des balises intÃ©rieures
				int id = Integer.parseInt(pointCourant.getElementsByTagName("id").item(0).getTextContent());
				double latitude = Double.parseDouble(pointCourant.getElementsByTagName("latitude").item(0).getTextContent());
				double longitude = Double.parseDouble(pointCourant.getElementsByTagName("longitude").item(0).getTextContent());
				Date DateHeure = Outils.convertirEnDate(pointCourant.getElementsByTagName("dateHeure").item(0).getTextContent(), formatDateUS);
				double altitude = Double.parseDouble(pointCourant.getElementsByTagName("altitude").item(0).getTextContent());
				int cardio = Integer.parseInt(pointCourant.getElementsByTagName("rythmeCardio").item(0).getTextContent());



				// crÃ©e un objet pointDeTrace
				PointDeTrace unPoint = new PointDeTrace(idTrace, id, latitude, longitude, altitude, DateHeure, cardio);

				// ajoute l'utilisateur Ã  la collection lesUtilisateurs
				laTrace.ajouterPoint(unPoint);
			}

			return reponse;
		}
		catch(Exception ex)
		{
			String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour obtenir la liste des parcours d'un utilisateur (service GetLesParcoursDunUtilisateur)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param pseudoConsulte pseudo de l'utilisateur dont on veut la liste des parcours
	 * @param lesTraces collection (vide) à remplir à partir des données fournies par le service web
	 * @author aubin
	 */
	public static String getLesParcoursDunUtilisateur(String pseudo, String mdpSha1, String pseudoConsulte, ArrayList<Trace> lesTraces)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlGetLesParcoursDunUtilisateur;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdpSha1=" + mdpSha1;
			urlDuServiceWeb += "&pseudoConsulte=" + pseudoConsulte;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			NodeList listeNoeudsTraces = leDocument.getElementsByTagName("trace");
			/* Exemple de données obtenues pour un utilisateur :
		      <trace>
		        <id>2</id>
		        <dateHeureDebut>2018-01-19 13:08:48</dateHeureDebut>
		        <terminee>1</terminee>
		        <dateHeureFin>2018-01-19 13:11:48</dateHeureFin>
		        <distance>1.2</distance>
		        <idUtilisateur>2</idUtilisateur>
		      </trace>
		      <trace>
		        <id>1</id>
		        <dateHeureDebut>2018-01-19 13:08:48</dateHeureDebut>
		        <terminee>0</terminee>
		        <distance>0.5</distance>
		        <idUtilisateur>2</idUtilisateur>
		      </trace>
			 */

			// vider d'abord la collection avant de la remplir
			lesTraces.clear();

			// parcours de la liste des noeuds <utilisateur> et ajout dans la collection lesUtilisateurs
			for (int i = 0 ; i < listeNoeudsTraces.getLength(); i++)
			{	// création de l'élément courant à chaque tour de boucle
				Element courant = (Element) listeNoeudsTraces.item(i);

				// lecture des balises intérieures
				int unId = Integer.parseInt(courant.getElementsByTagName("id").item(0).getTextContent());
				Date dateHeureDebut = Outils.convertirEnDate(courant.getElementsByTagName("dateHeureDebut").item(0).getTextContent());
				boolean terminee =  Integer.parseInt(courant.getElementsByTagName("terminee").item(0).getTextContent()) == 1;
				Date dateHeureFin = null;
				if(terminee == true) {
					dateHeureFin = Outils.convertirEnDate(courant.getElementsByTagName("dateHeureFin").item(0).getTextContent());
				}
				int idUtilisateur = Integer.parseInt(courant.getElementsByTagName("idUtilisateur").item(0).getTextContent());

				// cr�e un objet Trace
				Trace uneTrace= new Trace(unId, dateHeureDebut, dateHeureFin, terminee, idUtilisateur);

				lesTraces.add(uneTrace);
			}

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour supprimer un parcours (service SupprimerUnParcours)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param idTrace l'id de la trace à supprimer
	 * @author aubin
	 */

	public static String supprimerUnParcours(String pseudo, String mdpSha1, int idTrace)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlSupprimerUnParcours;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;
			urlDuServiceWeb += "&idTrace=" + idTrace;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour démarrer l'enregistrement d'un parcours (service DemarrerEnregistrementParcours)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param laTrace un objet Trace (vide) à remplir à partir des données fournies par le service web
	 * @author aubin
	 */

	public static String demarrerEnregistrementParcours(String pseudo, String mdpSha1, Trace laTrace)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlDemarrerEnregistrementParcours;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();


			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

	/**
	 * Description
	 * Méthode statique pour terminer l'enregistrement d'un parcours (service ArreterEnregistrementParcours)
	 * @param pseudo le pseudo de l'utilisateur qui fait appel au service web
	 * @param mdpSha1 le mot de passe hashé en sha1
	 * @param idTrace l'id de la trace à terminer
	 * @author aubin
	 */

	public static String arreterEnregistrementParcours(String pseudo, String mdpSha1, int idTrace)
	{
		String reponse = "";
		try
		{	// création d'un nouveau document XML à partir de l'URL du service web et des paramètres
			String urlDuServiceWeb = _adresseHebergeur + _urlArreterEnregistrementParcours;
			urlDuServiceWeb += "?pseudo=" + pseudo;
			urlDuServiceWeb += "&mdp=" + mdpSha1;
			urlDuServiceWeb += "&idTrace=" + idTrace;

			// création d'un flux en lecture (InputStream) à partir du service
			InputStream unFluxEnLecture = getFluxEnLecture(urlDuServiceWeb);

			// création d'un objet org.w3c.dom.Document à partir du flux ; il servira à parcourir le flux XML
			Document leDocument = getDocumentXML(unFluxEnLecture);

			// parsing du flux XML
			Element racine = (Element) leDocument.getElementsByTagName("data").item(0);
			reponse = racine.getElementsByTagName("reponse").item(0).getTextContent();

			// retour de la réponse du service web
			return reponse;
		}
		catch (Exception ex)
		{	String msg = "Erreur : " + ex.getMessage();
			return msg;
		}
	}

} // fin de la classe
