package fr.tse.info6.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Namespace;
import org.gitlab4j.api.models.Owner;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Visibility;

/**
 * Classe CreateProjectsFromCsvFile
 * 
 * Classe qui permet de cr\u00E9er des projets en masse avec leurs membres \u00E0 partir d'un fichier csv
 *
 */
public class CreateProjectsFromCsvFile {
	
	/**
	 * Cr\u00E9e un projet avec ses membres
	 * @param name	Nom du projet \u00E0 créer
	 * @param nameSpaceId	Groupe parent du projet \u00E0 cr\u00E9er
	 * @param memberUsernames	Liste des membres du groupe
	 * @return Le projet qu'on vient de cr\u00E9er
	 * @throws GitLabApiException
	 */
	public static Project createProject(String name, int nameSpaceId, ArrayList<String> memberUsernames) throws GitLabApiException {
		Project projectSpec = new Project();
		projectSpec.setName(name);
		if(nameSpaceId != -1) {
			Namespace nameSpace2 = new Namespace();
			nameSpace2.setId(nameSpaceId);
			projectSpec.setNamespace(nameSpace2);
			//projectSpec.setPath(nameSpace + "/" + name);
		}
		projectSpec.setVisibility(Visibility.PRIVATE);

		//This is to set current user as the owner of the project but doesn't work so far
		Owner projectOwner = new Owner();
		projectOwner.setId(GitLabInstance.GIT_INSTANCE.getUserApi().getCurrentUser().getId());
		projectSpec.setOwner(projectOwner);
		//
		
		Project newProject = GitLabInstance.GIT_INSTANCE.getProjectApi().createProject(projectSpec, null);
		
		for(String username : memberUsernames) {
			int userId = GitLabInstance.GIT_INSTANCE.getUserApi().getUser(username).getId();
			GitLabInstance.GIT_INSTANCE.getProjectApi().addMember(newProject.getId(), userId, AccessLevel.MAINTAINER);
		}	
		return newProject;
	}
	
	/**
	 * Cr\u00E9e les projets \u00E0 partir du fichier csv
	 * @param csvFile	Le fichier qu'a entr\u00E9 l'utilisateur
	 * @param id Identifiant du groupe parent
	 * @return Liste des projets cr\u00E9\u00E9s
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Project> createProjects(File csvFile, Integer groupId) throws GitLabApiException {
		List<fr.tse.info6.models.Project> projects = new ArrayList<fr.tse.info6.models.Project>();

		HashMap<String,ArrayList<String>> csvContent = ReadCsvFile.readcsv(csvFile);
		for(String projectName : csvContent.keySet()) {
			System.out.println(projectName);
			for(String username : csvContent.get(projectName)) 
				System.out.println(username);
			projects.add(new fr.tse.info6.models.Project(createProject(projectName, groupId, csvContent.get(projectName))));
		}
		return projects;
	}
}
