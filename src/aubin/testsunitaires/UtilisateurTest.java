package aubin.testsunitaires;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import aubin.classes.Outils;
import aubin.classes.Point;
import aubin.classes.Utilisateur;

public class UtilisateurTest {

	private Utilisateur utilisateur1;
	private Utilisateur utilisateur2;

	@Before
	public void setUp() throws Exception {
		utilisateur1 = new Utilisateur();

		int unId = 111;
		String unPseudo = "toto";
		String unMdpSha1 = "abcdef";
		String uneAdrMail = "toto@free.fr";
		String unNumTel = "1122334455";
		int unNiveau = 1;
		Date uneDateCreation = Outils.convertirEnDateHeure("21/06/2016 14:00:00");
		int unNbTraces = 3;
		Date uneDateDerniereTrace = Outils.convertirEnDateHeure("28/06/2016 14:00:00");
		utilisateur2 = new Utilisateur(unId, unPseudo, unMdpSha1, uneAdrMail, unNumTel, unNiveau, uneDateCreation, unNbTraces, uneDateDerniereTrace);
	}

	@Test
	public void testGetId() {
		assertEquals(0, utilisateur1.getId());
		assertEquals(111, utilisateur2.getId());
	}

	@Test
	public void testSetId() {
		utilisateur1.setId(111);
		assertEquals(111, utilisateur1.getId());
	}

	@Test
	public void testGetPseudo() {
		assertEquals("", utilisateur1.getPseudo());
		assertEquals("toto", utilisateur2.getPseudo());
	}

	@Test
	public void testSetPseudo() {
		utilisateur1.setPseudo("toto");
		assertEquals("toto", utilisateur1.getPseudo());
	}

	@Test
	public void testGetMdpSha1() {
		assertEquals("", utilisateur1.getMdpSha1());
		assertEquals("abcdef", utilisateur2.getMdpSha1());
	}

	@Test
	public void testSetMdpSha1() {
		utilisateur1.setMdpSha1("abcdef");
		assertEquals("abcdef", utilisateur1.getMdpSha1());
	}

	@Test
	public void testGetAdrMail() {
		assertEquals("", utilisateur1.getAdrMail());
		assertEquals("toto@free.fr", utilisateur2.getAdrMail());
	}

	@Test
	public void testSetAdrMail() {
		utilisateur1.setAdrMail("toto@free.fr");
		assertEquals("toto@free.fr", utilisateur1.getAdrMail());
	}

	@Test
	public void testGetNumTel() {
		assertEquals("", utilisateur1.getNumTel());
		assertEquals("11.22.33.44.55", utilisateur2.getNumTel());
	}

	@Test
	public void testSetNumTel() {
		utilisateur1.setNumTel("11.22.33.44.55");
		assertEquals("11.22.33.44.55", utilisateur1.getNumTel());
	}

	@Test
	public void testGetNiveau() {
		assertEquals(0, utilisateur1.getNiveau());
		assertEquals(1, utilisateur2.getNiveau());
	}

	@Test
	public void testSetNiveau() {
		utilisateur1.setNiveau(1);
		assertEquals(1, utilisateur1.getNiveau());
	}

	@Test
	public void testGetDateCreation() {
		assertEquals(null, utilisateur1.getDateCreation());
		assertEquals("21/06/2016 14:00:00", Outils.formaterDateHeureFR(utilisateur2.getDateCreation()));
	}

	@Test
	public void testSetDateCreation() throws ParseException {
		utilisateur1.setDateCreation(Outils.convertirEnDateHeure("21/06/2016 14:00:00"));
		assertEquals("21/06/2016 14:00:00", Outils.formaterDateHeureFR(utilisateur1.getDateCreation()));
	}

	@Test
	public void testGetNbTraces() {
		assertEquals(0, utilisateur1.getNbTraces());
		assertEquals(3, utilisateur2.getNbTraces());
	}

	@Test
	public void testSetNbTraces() {
		utilisateur1.setNbTraces(3);
		assertEquals(3, utilisateur1.getNbTraces());
	}

	@Test
	public void testGetDateDerniereTrace() {
		assertEquals(null, utilisateur1.getDateDerniereTrace());
		assertEquals("28/06/2016 14:00:00", Outils.formaterDateHeureFR(utilisateur2.getDateDerniereTrace()));
	}

	@Test
	public void testSetDateDerniereTrace() throws ParseException {
		utilisateur1.setDateDerniereTrace(Outils.convertirEnDateHeure("28/06/2016 14:00:00"));
		assertEquals("28/06/2016 14:00:00", Outils.formaterDateHeureFR(utilisateur1.getDateDerniereTrace()));
	}

	@Test
	public void testToString() {
		String msg = "";
		msg += "id : " + "0" + "\n";
		msg += "pseudo : " + "" + "\n";
		msg += "mdpSha1 : " + "" + "\n";
		msg += "adrMail : " + "" + "\n";
		msg += "numTel : " + "" + "\n";
		msg += "niveau : " + "0" + "\n";
		msg += "nbTraces : " + "0" + "\n";
		assertEquals("Test toString", msg, utilisateur1.toString());

		msg = "";
		msg += "id : " + "111" + "\n";
		msg += "pseudo : " + "toto" + "\n";
		msg += "mdpSha1 : " + "abcdef" + "\n";
		msg += "adrMail : " + "toto@free.fr" + "\n";
		msg += "numTel : " + "11.22.33.44.55" + "\n";
		msg += "niveau : " + "1" + "\n";
		msg += "dateCreation : " + "21/06/2016 14:00:00" + "\n";
		msg += "nbTraces : " + "3" + "\n";
		msg += "dateDerniereTrace : " + "28/06/2016 14:00:00" + "\n";
		assertEquals("Test toString", msg, utilisateur2.toString());
	}

}