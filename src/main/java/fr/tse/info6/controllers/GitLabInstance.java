package fr.tse.info6.controllers;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.Constants.TokenType;
import org.gitlab4j.api.GitLabApi.ApiVersion;

/**
 * Classe GitLabInstance
 * 
 * Classe qui permet de cr\u00E9er une instance GitlabApi de l'utilisateur connecté
 *
 */
public class GitLabInstance {
	/**
	 * Contient l'instance GitLabApi qui va interragir avec le serveur GitlabApi
	 */
	public static GitLabApi GIT_INSTANCE;
	
	/**
	 * Cr\u00E9er l'instance GitLabApi
	 * @param access_token l'authToken de l'utilisateur qui s'est connecté
	 */
	public static void createGitLabInstance(String access_token) {
		GitLabApi gitLabApi = new GitLabApi(ApiVersion.V4, "https://code.telecomste.fr", TokenType.OAUTH2_ACCESS, access_token, (String) null, null);
		GitLabInstance.GIT_INSTANCE = gitLabApi;
	}
}
