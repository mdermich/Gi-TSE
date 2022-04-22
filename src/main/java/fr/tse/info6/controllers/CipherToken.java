package fr.tse.info6.controllers;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
*
*	Class CipherToken :
*
*	Classe permettant de chifrrer et dechiffrer l'access token de l'utilisateur
*/
public class CipherToken {

	private static final String Iv_Texte = "CeciEstUnTexteIV"; // On creer une variable qui nous servira de vecteur d'initialisation pour le chiffrement AES (128 bits obligatoires => fonctionnement de l'AES)
	private static final String Cl_Texte = "CeciAussiPourAleatoirePourSecKey"; // On creer une variable qui nous servira de cle (256bits dans ce cas pour plus de securite)

	// Creation des variables necessaires a l'utilisation de l'algorithme
	private IvParameterSpec IV; // Vecteur d'initialisation
	private SecretKeySpec Cl;  // Cle
	private Cipher cipher; // Methode de chiffrement

	/**
	*
	*	Methode CipherToken (constructeur) :
	*
	*	Constructeur permettant d'initialiser l'algorithme de chiffrement AES
	*/
	public CipherToken() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
		IV = new IvParameterSpec(Iv_Texte.getBytes("UTF-8")); // On fournit notre vecteur d'initialisation cree au debut
		Cl = new SecretKeySpec(Cl_Texte.getBytes("UTF-8"), "AES"); // On fournit notre cle creee au debut
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); // On choisit d'utiliser le chiffrement AES en mode CBC (Cipher Block Chaining) avec du padding (on va remplir les matrices partiellement vide) afin d'encore plus securiser 
	}

/**
*
*	Methode chiffrer :
*
*	Methode permettant de chiffrer l'access token et d'enregistrer le resultat dans un fichier txt
*/

	public void chiffrer(String AccessToken)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {

		cipher.init(Cipher.ENCRYPT_MODE, Cl, IV); // Initialisation
		byte[] accessTokenChiffr = cipher.doFinal(AccessToken.getBytes()); // On fournit notre access token, l'AES le chiffre et nous renvoi le resultat avec un format base64
		String Chiffr = Base64.encodeBase64String(accessTokenChiffr); // On recupere notre token chiffre sous forme de String

		String path = System.getProperty("user.dir") + "/src/notAToken.txt"; // On va sauvegarder le nouveau token chiffre dans un fichier texte dans le dossier /src du projet
		BufferedWriter writer = new BufferedWriter(new FileWriter(path)); // On utilise un Buffer pour ecrire dans le fichier ou sera stocke notre token chiffre
		writer.write(Chiffr); // On ecrit dans ce fichier ce qui est contenu dans notre String
		writer.close();

	}

/**
*
*	Methode dechiffrer :
*
*	Methode permettant de dechiffrer l'access token present dans le fichier txt
*/
	public String dechiffrer() throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IOException {

		cipher.init(Cipher.DECRYPT_MODE, Cl, IV); // Initialisation avec les memes variables utilisees pour le chiffrement
		String path = System.getProperty("user.dir") + "/src/notAToken.txt"; // On indique ou se trouve notre token chiffre
		System.out.println(path);
		BufferedReader reader = new BufferedReader(new FileReader(path)); // On utilise un buffer pour lire le fichier
		String currentLine = reader.readLine(); // On recupere ligne par ligne
		System.out.println(currentLine);
		reader.close();
		if(currentLine!=null) // Si on a pas une ligne vide :
		{
			byte[] bytesDechiffr = cipher.doFinal(Base64.decodeBase64(currentLine)); // On dechiffre notre token chiffre
			return new String(bytesDechiffr); // On le met sous forme de String et on le return
		}
		else return "";
	}

}
