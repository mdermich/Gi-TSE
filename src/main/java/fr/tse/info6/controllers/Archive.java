package fr.tse.info6.controllers;

import java.io.IOException;

import org.gitlab4j.api.GitLabApiException;


/**
*	Class Archive :
*
*	Classe permettant d'archiver ou de desarchiver un projet.
*/

public class Archive {
	
	/**
	* Methode archiveProj :
	*
	* Fonction qui, avec l'access token et l'id d'un projet, permet d'archiver ce dernier.
	*/
	public static void archiveProj(String access_token, int project_Id) throws GitLabApiException {
		/*String command = "curl --request POST --header \"Authorization: Bearer " + access_token
				+ "\" \"https://code.telecomste.fr/api/v4/projects/" + project_Id + "/archive\""; // La requete qui va demander au serveur d'archiver un projet
		try {
			Runtime.getRuntime().exec(new String[] { "/bin/sh"//$NON-NLS-1$
	                , "-c", command }); // On execute la commande d'archivage
		} catch (IOException e) {
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}*/
		
		GitLabInstance.GIT_INSTANCE.getProjectApi().archiveProject(project_Id);
	}
	/**
	* Methode unarchiveProj :
	*
	* Fonction qui, avec l'access token et l'id d'un projet, permet de desarchiver ce dernier.
	*/
	
	public static void unarchiveProj(String access_token, int project_Id) throws GitLabApiException {
		/*String command = "curl --request POST --header \"Authorization: Bearer " + access_token
				+ "\" \"https://code.telecomste.fr/api/v4/projects/" + project_Id + "/unarchive\""; // La requete qui va demander au serveur de desarchiver un projet
		try {
			Runtime.getRuntime().exec(new String[] { "/bin/sh"//$NON-NLS-1$
	                , "-c", command }); // On execute la commande de desarchivage
		} catch (IOException e) {
			try {
				Runtime.getRuntime().exec(command);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}*/
		GitLabInstance.GIT_INSTANCE.getProjectApi().unarchiveProject(project_Id);
	}

}
