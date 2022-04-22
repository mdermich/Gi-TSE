package fr.tse.info6.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import fr.tse.info6.controllers.GitLabInstance;


/**
 * Class Group :
 * 
 * La classe Group est notre propre version de org.gitlab4j.api.models.Group
 * Elle permet de travailler legerement plus vite sur des listes de groupes, comme
 * la taille de nos Groupes est plus legere que celle des org.gitlab4j.api.models.Group.
 * 
 * Au même titre que Project, on travaille principalement sur 2 listes de Group dans le code principal.
 * 
 */
public class Group {
	private String name;
	private String path;
	private String created_at;
	private Integer id;
	private Integer parent_id;
	private List<Member> membres;
	private int nbr_membres;
	private List<Project> projects;
	private List<Group> groups;
	private int nbr_projects;

	/**
	 * On initialise au moins la liste de projet du groupe si on ne l'instancie sans valeurs.
	 */
	public Group()
	{
		this.projects = new ArrayList<>();
	}

	/**
	 * Constructeur peu utilise, mais utile si on veut cree un groupe en particulier.
	 */
	public Group(String name, String path, String created_at, Integer id, List<Member> membres, List<Group> groups,
			int nbr_membres, List<Project> pjs, int nbr_projects, int parent_id) {
		this.name = name;
		this.path = path;
		this.created_at = created_at;
		this.id = id;
		this.parent_id = parent_id;
		this.membres = membres;
		this.nbr_membres = nbr_membres;
		this.projects = pjs;
		this.nbr_projects = nbr_projects;
	}

	/**
	 * Le constructeur le plus utilise, qui instancie un Group en fonction d'un org.gitlab4j.api.models.Group grp
	 * @param grp le groupe à copier (venant de l'API Gitlab4j)
	 */
	public Group(org.gitlab4j.api.models.Group grp) throws GitLabApiException {
		name = grp.getName();
		path = grp.getPath();
		id = grp.getId();
		parent_id = grp.getParentId();

		List<Member> mbs = GitLabInstance.GIT_INSTANCE.getGroupApi().getAllMembers(id);
		nbr_membres = mbs.size();

		projects =	(GitLabInstance.GIT_INSTANCE.getGroupApi().getProjects(id) != null) ? GitLabInstance.GIT_INSTANCE.getGroupApi().getProjects(id) : new ArrayList<Project>();
		nbr_projects = projects.size();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		created_at = simpleDateFormat.format(grp.getCreatedAt());
	}


	/**
	 * La fonction addSubs permet d'ajouter les sous-groupes d'un Group dans sa liste 'groups'
	 * Elle permet alors de savoir quel groupe est le parent de quel groupe.
	 * @param grps la liste de groupe
	 */
	public void addSubs(List<Group> grps) {
		groups = new ArrayList<Group>();
		for (Group grp : grps) {
			if (grp.getParent_id() != null) {
				if (grp.getParent_id().equals(id)) {
					groups.add(grp);
				}
			}
		}
	}

	/**
	 * Fonction qui check si un groupe possede des sous-groupes, et recupere ses projets, puis si ce sous-groupe possedes des sous-groupes...
	 * @return la liste de projets que ce groupes et tous ses sous-groupes contiennent.
	 */
	public List<fr.tse.info6.models.Project> selectAllProjects() {
		List<Project> prjs = new ArrayList<Project>();
		List<fr.tse.info6.models.Project> result = new ArrayList<fr.tse.info6.models.Project>();
		List<Group> tmpGrps = this.getGroups();
		if (this.getGroups() != null) {
			if (tmpGrps.size() == 0) {
				prjs.addAll(this.getProjects());
			} else {
				prjs.addAll(this.getProjects());
				for (Group grp : tmpGrps) {
					if (grp.getGroups().size() == 0) {
						prjs.addAll(grp.getProjects());
					} else {
						prjs.addAll(grp.getProjects());
						List<Group> subGrps = grp.getGroups();
						for (Group subgrp : subGrps) {
							if (subgrp.getGroups().size() == 0) {
								prjs.addAll(subgrp.getProjects());
							} else {
								prjs.addAll(subgrp.getProjects());
								List<Group> subsubGrps = subgrp.getGroups();
								for (Group subsubgrps : subsubGrps) {
									prjs.addAll(subsubgrps.getProjects());
								}
							}

						}
					}

				}
			}
		}
		for (Project projs : prjs) {
			fr.tse.info6.models.Project p;
			try {
				p = new fr.tse.info6.models.Project(projs);
				result.add(p);

			} catch (GitLabApiException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Project> getProjects() {
		return projects;
	}
	
	public void addProject(Project p)
	{
		this.projects.add(p);
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public int getNbr_projects() {
		return nbr_projects;
	}

	public void setNbr_projects(int nbr_projects) {
		this.nbr_projects = nbr_projects;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public List<Member> getMembres() {
		return membres;
	}

	public void setMembres(List<Member> membres) {
		this.membres = membres;
	}

	public int getNbr_membres() {
		return nbr_membres;
	}

	public void setNbr_membres(int nbr_membres) {
		this.nbr_membres = nbr_membres;
	}

	@Override
	public String toString() {
		return name;
	}

	public static List<fr.tse.info6.models.Group> sortBy(List<fr.tse.info6.models.Group> projs, String var,
			boolean order) {
		Collections.sort(projs, new SortGrpBy(var, order));

		return projs;
	}

}
