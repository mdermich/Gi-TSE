package fr.tse.info6.models;

import java.io.BufferedReader;
import java.io.File;
import java.util.Objects;
/**
 * Class Csv :
 * 
 * La classe Csv permet de travailler plus facilement sur nos fichiers CSV
 * @param File, pour ouvrir et stocker le fichier ouvert
 * @param BufferedReader, pour lire le fichier ouvert
 * 
 * Elle ne possede que des getters ett setters, ainsi qu'un override de hashCode() et equals().
 */
public class Csv {
	private static BufferedReader buffer;
	private File csvproject;
	
	
	public Csv(File csvproject){
		super();
		this.csvproject=csvproject;
	}
	
	public File getcsvproject() {
		return csvproject;
	}
	
	public void setcsvproject(File csvproject){
		this.csvproject=csvproject;
	}
	
	public BufferedReader getBuffer() {
		return buffer;
	}

	public void setBuffer(BufferedReader buffer) {
		Csv.buffer = buffer;
	}

	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this==obj){
			return true;
		}
		if (obj==null){
			return false;
		}
		if (getClass()!=obj.getClass()) {
			return false;
		}
		Csv other=(Csv) obj;
		return Objects.equals(csvproject, other.csvproject);
	}
	
}
