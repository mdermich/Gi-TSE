package fr.tse.info6.models;
/**
 * Class User :
 * 
 * La classe User est notre propre version de org.gitlab4j.api.models.User
 * Elle permet de travailler legerement plus vite sur des listes du'utilisateurs, comme
 * la taille de nos User est plus legere que celle des org.gitlab4j.api.models.User.
 * 
 * Cette classe de possede que des getters & setters (et un toString pour le debug)
 * 
 * 
 */
public class User {
	
	private String name;
	private String username;
	private String state;
	private String avatar_url;
	private String web_url;
	private String created_at;
	private String bio;
	private String email;
	

	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getWeb_url() {
		return web_url;
	}
	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", username=" + username + ", state=" + state + ", avatar_url=" + avatar_url
				+ ", web_url=" + web_url + ", created_at=" + created_at + ", bio=" + bio + ", email=" + email + ", id="
				+ id + "]";
	}
	
}
