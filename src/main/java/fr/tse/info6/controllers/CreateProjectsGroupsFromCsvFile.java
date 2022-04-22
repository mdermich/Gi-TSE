package fr.tse.info6.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gitlab4j.api.GitLabApiException;

import fr.tse.info6.models.Group;
import fr.tse.info6.models.Project;

/**
 * Classe CreateProjectsGroupsFromCsvFile
 * 
 * Classe qui permet de cr\u00E9er des projets et des groupes en masse avec leurs membres \u00E0 partir d'un fichier csv
 *
 */
public class CreateProjectsGroupsFromCsvFile {
	/**
	 * Permet de cr\u00E9er les projets dans des groupes en masse. Les groupes peuvent d\u00E9j\u00E0 exister ou pas
	 * @param csvFile	Le fichier qu'a entr\u00E9 l'utilisateur
	 * @param id	Identifiant du groupe parent
	 * @return Une HashMap des listes des groupes et projets cr\u00E9\u00E9s
	 * @throws GitLabApiException
	 */
	public static HashMap<List<Group>, List<Project>> createProjectsInsideGroups(File csvFile, Integer id) throws GitLabApiException {
		List<Group> groups = new ArrayList<Group>();
		List<Project> projects = new ArrayList<Project>();
		Group grp = new Group();
		Project prj;
		HashMap<String, HashMap<String, ArrayList<String>>> csvContent = ReadCsvFile.readcsv2(csvFile);
		for(String groupPath : csvContent.keySet()) {
			String completePath;
			if(id != -1) {
				completePath = GitLabInstance.GIT_INSTANCE.getGroupApi().getGroup(id).getFullPath() + "/" + groupPath;
			}
			else {
				completePath = groupPath;
			}
			//Check if the group doesn't exist
			try {
				grp = new Group(GitLabInstance.GIT_INSTANCE.getGroupApi().getGroup(completePath));
			} catch(GitLabApiException e){
				System.out.println("doesn't exist");
				grp = new Group(CreateGroupsFromCsvFile.createGroup(groupPath, new ArrayList<String>(), id)); // create the group first, with no members
				System.out.println("created");
			}
			
			//Then we create the projects inside this group
			HashMap<String,ArrayList<String>> projectsOfGroup = csvContent.get(groupPath);
			for(String projectName : projectsOfGroup.keySet()) {
				System.out.println(projectName);
				for(String username : projectsOfGroup.get(projectName)) 
					System.out.println(username);
				
				int nameSpaceId = GitLabInstance.GIT_INSTANCE.getGroupApi().getGroup(completePath).getId();
				org.gitlab4j.api.models.Project prjtmp = CreateProjectsFromCsvFile.createProject(projectName, nameSpaceId, projectsOfGroup.get(projectName));
				List<org.gitlab4j.api.models.Project> prjs = new ArrayList<org.gitlab4j.api.models.Project>();
				grp.addProject(prjtmp);
				prjs.add(prjtmp);
				prj = new Project(prjtmp);
				projects.add(prj);
				//grp.setProjects(prjs);
				groups.add(grp);
			}
		}
		
		HashMap<List<Group>, List<Project>> groups_projects = new HashMap<List<Group>, List<Project>>();
		groups_projects.put(groups, projects);
		return groups_projects;
	}
}
