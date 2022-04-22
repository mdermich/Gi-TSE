package fr.tse.info6.models;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.ProjectStatistics;

import fr.tse.info6.controllers.GitLabInstance;
/**
 * Class Project :
 * 
 * La classe Project est notre propre version de org.gitlab4j.api.models.Project
 * Elle permet de travailler legerement plus vite sur des listes de projets, comme
 * la taille de nos Project est plus legere que celle des org.gitlab4j.api.models.Project.
 * 
 * Au même titre que Group, on travaille principalement sur 2 listes de Group dans le code principal.
 * 
 */
public class Project {
	private String name;
	private Integer id;
	private String created_at;
	private String last_modif;
	private int nbr_collaborators;
	private int nbr_branches;
	private List<Member> members;
	private long commitCount;
	private String avatarUrl; 
	private String projectOwner; 
	private ProjectStatistics stat;
	private List<Branch> branches; 
	
	private HashMap<Integer, Integer> listCommits = new HashMap<>(); 
	private HashMap<Integer, Integer> listMerges = new HashMap<>();


	public Project()
	{

	}

	/**
	 * Constructeur peu utilise, mais utile si on veut cree un projet en particulier.
	 */
	public Project(String name, String created_at, String last_modif, int nbr_collaborators, int nbr_branches, List<Member> members, long commitCount, String avatarUrl, String projectOwner, ProjectStatistics stat, Integer id, List<Branch> branches) {
		this.name = name;
		this.created_at = created_at;
		this.last_modif = last_modif;
		this.nbr_collaborators = nbr_collaborators;
		this.nbr_branches = nbr_branches;
		this.setMembers(members);
		this.setCommitCount(commitCount);
		this.setAvatarUrl(avatarUrl);
		this.setProjectOwner(projectOwner);
		this.setId(id);
		this.setBranches(branches);
	}

	/**
	 * Le constructeur le plus utilise, qui instancie un Project en fonction d'un org.gitlab4j.api.models.Project proj
	 * @param org.gitlab4j.api.models.Project proj
	 */
	@SuppressWarnings({ "deprecation" })
	public Project(org.gitlab4j.api.models.Project proj) throws GitLabApiException
	{
		name = proj.getName();
		id = proj.getId();

		stat = proj.getStatistics();

		if (avatarUrl == null) {
			avatarUrl = "/Project_logo3.png";
		}
		else {
			avatarUrl = proj.getAvatarUrl();
		}


		// Permet d'obtenir la date sous le format souhaite
		StringBuffer stringBuffer = new StringBuffer();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		simpleDateFormat.format(proj.getCreatedAt(), stringBuffer, new FieldPosition(0));
		created_at = stringBuffer.toString();

		StringBuffer stringBuffer2 = new StringBuffer();
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		simpleDateFormat2.format(proj.getLastActivityAt(), stringBuffer2, new FieldPosition(0));
		last_modif = stringBuffer2.toString();

		// On recupere la liste des collaborateurs
		List<Member> collaborators = GitLabInstance.GIT_INSTANCE.getProjectApi().getAllMembers(proj.getId());
		nbr_collaborators = collaborators.size();
		members = collaborators;
		branches = GitLabInstance.GIT_INSTANCE.getRepositoryApi().getBranches(proj.getId());

		nbr_branches = branches.size();

		// On recupere la liste des commits
		List<Commit> commits = GitLabInstance.GIT_INSTANCE.getCommitsApi().getCommits(proj.getId());
		
		if (commits == null) {
			commitCount = 0; 
		}
		else {
			commitCount = commits.size();
		}

		// Pour avoir le nombre de commit par mois
		int tempMonth = 15; //random value / a month that doesn't exist
		for(Commit commit : commits) {
			if(commit.getCommittedDate().getMonth() + 1 == tempMonth){
				this.listCommits.put(tempMonth, this.listCommits.get(tempMonth) + 1);
			}
			else{
				int month = commit.getCommittedDate().getMonth() + 1;
				this.listCommits.put(new Integer(month), new Integer(1));
			}
			tempMonth = commit.getCommittedDate().getMonth() + 1;
		}

		// Pour avoir le nombre de merge par mois
		List<MergeRequest> mergeRequests = GitLabInstance.GIT_INSTANCE.getMergeRequestApi().getMergeRequests(proj.getId());
		tempMonth = 15; //random value / a month that doesn't exist
		for(MergeRequest merge : mergeRequests) {
			if(merge.getCreatedAt().getMonth() + 1 == tempMonth){
				this.listMerges.put(tempMonth, this.listMerges.get(tempMonth) + 1);
			}
			else{
				int month = merge.getCreatedAt().getMonth() + 1;
				this.listMerges.put(new Integer(month), new Integer(1));
			}
			tempMonth = merge.getCreatedAt().getMonth() + 1;
		}


	}

	/**
	 * Permet de trouver un projet en fonction de son nom dans la liste de Project donnee
	 * @param projects La liste de projets
	 * @param object le nom de l'objet
	 * @return Project
	 */
	public static Project getProject(List<Project> projects, Object object) {
		for (Project project : projects) {
			if (project.getName().equals(object)) {
				return project;
			}
		}
		return null;
	}

	/**
	 * Permet, comme son nom l'indique, de trier tout les projets, a l'aide de la classe SortGrpBy
	 * @param la liste de Projet a trier List<Project> 
	 * @param var la parametre selon lequel on trie
	 * @param order decroissant ou croissant
	 * 
	 * @return List<Project> la liste apres tri.
	 */
	public static List<fr.tse.info6.models.Project> sortBy(List<fr.tse.info6.models.Project> projs,String var,boolean order)
	{
		Collections.sort(projs, new SortProjBy(var, order));	

		return projs;
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

	public String getLast_modif() {
		return last_modif;
	}

	public void setLast_modif(String last_modif) {
		this.last_modif = last_modif;
	}

	public int getNbr_collaborators() {
		return nbr_collaborators;
	}

	public void setNbr_collaborators(int nbr_collaborators) {
		this.nbr_collaborators = nbr_collaborators;
	}

	public int getNbr_branches() {
		return nbr_branches;
	}

	public void setNbr_branches(int nbr_branches) {
		this.nbr_branches = nbr_branches;
	}

	@Override
	public String toString() {
		return "Project [name=" + name + ", created_at=" + created_at + ", last_modif=" + last_modif
				+ ", nbr_collaborators=" + nbr_collaborators + ", nbr_branches=" + nbr_branches + "]";
	}

	public List<Member> getMembers() {
		return members;
	}
	public void setMembers(List<Member> members) {
		this.members = members;
	}
	public long getCommitCount() {
		return commitCount;
	}
	public void setCommitCount(long commitCount) {
		this.commitCount = commitCount;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getProjectOwner() {
		return projectOwner;
	}
	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}
	public ProjectStatistics getStat() {
		return stat;
	}
	public void setStat(ProjectStatistics stat) {
		this.stat = stat;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Branch>  getBranches() {
		return branches;
	}
	public void setBranches(List<Branch> branches2) {
		this.branches = branches2;
	}

	public HashMap<Integer, Integer> getListCommits() {
		return listCommits;
	}
	public void setListCommits(HashMap<Integer, Integer> listCommits) {
		this.listCommits = listCommits;
	}

	public HashMap<Integer, Integer> getListMerges() {
		return listMerges;
	}
	public void setListMerges(HashMap<Integer, Integer> listMerges) {
		this.listMerges = listMerges;
	}


}
