package fr.tse.info6.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tse.info6.models.User;
/**
*
*	Class Login
*
*	Classe permettant de se connecter au Gitlab prive de TSE, de recuperer l'acess token de l'utilisateur et ses informations
*/

public class Login {
	// On initialise des variables necessaires pour faire une requete HTML
	private static CloseableHttpClient httpClient;
	static CloseableHttpResponse response;
	private static final String get_user_info_request = "https://code.telecomste.fr/api/v4/user"; // On recupere le profil de l'utilisateur sur l'url prive du Gitlab de Telecom
    
/**
*
*	Methode Creds :
*
*	Methode permettant de se connecter au GitLab TSE en fournissant le nom d'utilisateur et mot de passe, retourne l'access token de l'utilisateur
*/
    public static String Creds(String userName, String password) {
		try {
			URL url = new URL("https://code.telecomste.fr/oauth/token"); // On defini l'url prive tout en indiquant qu'il s'agit d'une requete pour obtenir le token
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); // On effectue la connexion

			String data = "grant_type=password&username=" + userName + "&password=" + password; // On indique le nom d'utilisateur et mot de passe
			connection.setRequestMethod("POST"); // On defini la methode HTML, POST permet d'envoyer des informations
			connection.setDoOutput(true); // On souhaite recevoir une reponse du serveur
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // On defini le format de ce qu'on envoi
			connection.setRequestProperty("Accept", "application/json");  // On informe qu'on accepte une reponse du serveur en format JSON

			PrintStream os = new PrintStream(connection.getOutputStream()); // On envoi notre requete
			os.print(data);
			os.close();

			String response = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // On recupere la reponse du serveur
			String line;

			while ((line = in.readLine()) != null)
				response += line; // On recupere ligne par ligne pour n'en faire qu'une seule sous forme de String
			in.close();
			System.out.println(response);
			String access_token = response.substring(response.indexOf("\"access_token\":\"") + 16, response.indexOf("\",")); // On va chercher seulement l'access token de l'utilisateur
			System.out.println(access_token);

			return access_token; // On return l'access token

		} catch (IOException e) {
			System.out.println("incorrect");
			String access_token = "erreur";
			return access_token;			
		}

	}
    
    public static String getUserInfo(String access_token) throws URISyntaxException, ClientProtocolException, IOException {
    	httpClient = HttpClients.createDefault();
    	List<NameValuePair> get_parameters = new ArrayList<NameValuePair>();
    	get_parameters.add(new BasicNameValuePair("access_token", access_token));
    	HttpGet get = new HttpGet(get_user_info_request);
    	URI uri = new URIBuilder(get.getURI())
    		      .addParameters(get_parameters)
    		      .build();
    	((HttpRequestBase) get).setURI(uri);
    	response = httpClient.execute(get);
    	String response2_string = EntityUtils.toString(response.getEntity());
    	
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	User user_info = mapper.readValue(response2_string, new TypeReference<User>( ) {});		
    	
    	return user_info.toString(); 
    }
}
