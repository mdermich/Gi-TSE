package fr.tse.info6.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupParams;

/**
 * Classe CreateGroupsFromCsvFile
 * 
 * Classe qui permet de cr\u00E9er des groupes en masse avec leurs administrateurs \u00E0 partir d'un fichier csv
 *
 */
public class CreateGroupsFromCsvFile {
	
	/**
	 * Cr\u00E9e un groupe avec ses administrateurs
	 * @param name	Nom du groupe
	 * @param memberUsernames	Liste des administrateurs du groupe
	 * @param id Identifiant du groupe parent
	 * @return le groupe qu'on vient de cr\u00E9er
	 * @throws GitLabApiException
	 */
	public static Group createGroup(String name, ArrayList<String> memberUsernames, Integer id) throws GitLabApiException {
		GroupParams groupParameters = new GroupParams();
		groupParameters.withName(name);
		groupParameters.withPath(name);
		if(id!=-1) groupParameters.withParentId(id);

		
		Group newGroup = GitLabInstance.GIT_INSTANCE.getGroupApi().createGroup(groupParameters);

		for(String username : memberUsernames) {
			int userId = GitLabInstance.GIT_INSTANCE.getUserApi().getUser(username).getId();
			GitLabInstance.GIT_INSTANCE.getGroupApi().addMember(newGroup.getId(), userId, AccessLevel.MAINTAINER);
		}	
		return newGroup;
	}
	
	/**
	 * Cr\u00E9e les groupes \u00E0 partir du fichier csv
	 * @param csvFile	Le fichier qu'a entr\u00E9 l'utilisateur
	 * @param id Identifiant du groupe parent
	 * @return Liste des groupes cr\u00E9\u00E9s
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Group> createGroups(File csvFile, Integer id) throws GitLabApiException {
		List<fr.tse.info6.models.Group> groups = new ArrayList<fr.tse.info6.models.Group>();

		HashMap<String,ArrayList<String>> csvContent = ReadCsvFile.readcsv(csvFile);
		for(String groupName : csvContent.keySet()) {
			System.out.println(groupName);
			for(String username : csvContent.get(groupName)) 
				System.out.println(username);
			groups.add(new fr.tse.info6.models.Group(createGroup(groupName, csvContent.get(groupName), id)));
		}
		return groups;
	}
	
}
