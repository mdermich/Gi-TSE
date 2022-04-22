package fr.tse.info6.controllers;

import java.util.ArrayList;
import java.util.List;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupFilter;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.ProjectFilter;
import org.gitlab4j.api.models.User;

/**
 * Classe ImportUserProjects
 * 
 * Classe qui permet d'importer les projets et les groupes de l'utilisateur courant
 *
 */
public class ImportUserProjects {
	
	/**
	 * Import des projets non archiv\u00E9s de l'utilisateur courant
	 * @return une liste des projets non archiv�s dans lesquels l'utilisateur est membre
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Project> importProjects() throws GitLabApiException {
		
		//On d�clare un fitre pour sp�cifier les projets non archiv�s dans lesquels l'utilisateur courant est membre
		ProjectFilter filter = new ProjectFilter().withStatistics(true).withMembership(true).withArchived(false);;
		//Importer les projets en y appliquant le filtre
		List<Project> projects = GitLabInstance.GIT_INSTANCE.getProjectApi().getProjects(filter);
		
		fr.tse.info6.models.Project project;
		List<fr.tse.info6.models.Project> listProjects = new ArrayList<fr.tse.info6.models.Project>();
		
		//Remplir la liste des projets
		for(Project proj : projects) {
			project = new fr.tse.info6.models.Project(proj);
			listProjects.add(project);
		}
		return listProjects;
		
	}
	
	/**
	 * Import des projets archiv\u00E9s de l'utilisateur courant
	 * @return une liste des projets archiv�s dans lesquels l'utilisateur est membre
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Project> importArchivedProjects() throws GitLabApiException {
		
		//On d�clare un fitre pour sp�cifier les projets non archiv�s dans lesquels l'utilisateur courant est membre
		ProjectFilter filter = new ProjectFilter().withStatistics(true).withMembership(true).withArchived(true);;
		//Importer les projets en y appliquant le filtre
		List<Project> projects = GitLabInstance.GIT_INSTANCE.getProjectApi().getProjects(filter);
		
		fr.tse.info6.models.Project project;
		List<fr.tse.info6.models.Project> listProjects = new ArrayList<fr.tse.info6.models.Project>();
	
		//Remplir la liste des projets
		for(Project proj : projects) {
			project = new fr.tse.info6.models.Project(proj);
			listProjects.add(project);
		}
		return listProjects;
		
	}
	
	/**
	 * Import de l'utilisateur courant
	 * @return l'utilisateur connect\u00E9
	 * @throws GitLabApiException
	 */
	public static User importUser() throws GitLabApiException{
		User user = GitLabInstance.GIT_INSTANCE.getUserApi().getCurrentUser();
		return user;
	}
	
	/**
	 * Import des groupes de l'utilisateur courant
	 * @return une liste des groupes dans lesquels l'utilisateur est membre
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Group> importGroups() throws GitLabApiException{
		
		System.out.println("R\u00E9cup\u00E9ration des donn\u00E9es des groupes...");
		//Importer les groupes depuis l'api gtilab4j
		GroupFilter filter = new GroupFilter().withMinAccessLevel(AccessLevel.GUEST);

		List<Group> groups = GitLabInstance.GIT_INSTANCE.getGroupApi().getGroups(filter);
		
		fr.tse.info6.models.Group group;
		List<fr.tse.info6.models.Group> listGroups = new ArrayList<fr.tse.info6.models.Group>();
		//Remplir la liste des groupes � retourner
		for(Group grp : groups) {
			group = new fr.tse.info6.models.Group(grp);
			listGroups.add(group);
		}
		System.out.println("Tout les groupes sont r\u00E9cup\u00E9r\u00E9s.");

		return listGroups;
	}
	
	/**
	 * 
	 * @param listGroups
	 * @return
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Group> transformGroups(List<fr.tse.info6.models.Group> listGroups) throws GitLabApiException
	{
		List<fr.tse.info6.models.Group> tmplistGroups = new ArrayList<fr.tse.info6.models.Group>(); 
		tmplistGroups.addAll(listGroups);


		for(fr.tse.info6.models.Group grp : tmplistGroups)
		{
			grp.addSubs(tmplistGroups);
		}
		List<fr.tse.info6.models.Group> tmp = new ArrayList<fr.tse.info6.models.Group>(tmplistGroups);
		for(fr.tse.info6.models.Group grp : tmp)
		{
			if(grp.getParent_id()!=null)
			{
				//System.out.println(grp.getName() + " removed.");
				tmplistGroups.remove(grp);
				
			}

			
		}



		return tmplistGroups;
	}
	
	/**
	 * Suppression des projets archiv\u00E9s dans une liste
	 * @param projects	une liste de projets GitLab 
	 * @return une liste qui contient que les projets non archiv\u00E9s
	 * @throws GitLabApiException
	 */
	public static List<fr.tse.info6.models.Project> deleteArchived(List<fr.tse.info6.models.Project> projects) throws GitLabApiException {
		List<fr.tse.info6.models.Project> archivedProjects = new ArrayList<>();
		for(fr.tse.info6.models.Project project : projects) {
			org.gitlab4j.api.models.Project proj = GitLabInstance.GIT_INSTANCE.getProjectApi().getProject(project.getId());
			if(proj.getArchived()) {
				archivedProjects.add(project);
			}
		}
		projects.removeAll(archivedProjects);
		return archivedProjects;
	}
	
	/**
	 * Sp\u00E9cifie si l'utilisateur connect\u00E9 est le cr\u00E9ateur d'un projet
	 * @param project	dont on veut savoir si l'user courant est le cr\u00E9ateur
	 * @return un boolean : True si le user est le cr\u00E9ateur est False dans le cas inverse
	 * @throws GitLabApiException
	 */
	public static boolean currentUserIsCreator(fr.tse.info6.models.Project project) throws GitLabApiException {
		int currentUserId = GitLabInstance.GIT_INSTANCE.getUserApi().getCurrentUser().getId();
		int projectCreator = GitLabInstance.GIT_INSTANCE.getProjectApi().getProject(project.getId()).getCreatorId();
		if(currentUserId ==  projectCreator)
			return true;
		else
			return false;
	}

}

