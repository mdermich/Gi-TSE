package fr.tse.info6.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import fr.tse.info6.models.Csv;

/**
 * Classe ReadCsvFile
 * 
 * Classe qui permet de parser les fichier csv
 * 
 *
 */
public class ReadCsvFile {
	
	/**
	 * Parse le fichier csv contenant 2 colonnes (username,projectName OU username,groupName)
	 * 
	 * @param csvFile	le fichier csv qu'on veut lire
	 * @return une HashMap contenant le projectName (ou le groupName) avec ses usernames correspondants
	 */
	public static HashMap<String, ArrayList<String>> readcsv(File csvFile) {
		//Déclarer un objet de type Csv
		Csv csv = new Csv(csvFile);
		//Déclarer l'hashMap qu'on va retourner
		HashMap<String, ArrayList<String>> csv_content = new HashMap<String, ArrayList<String>> ();
		//Attribut qui contient le projectName (ou le groupName) de la ligne précédente
		String temp = null;
		String line = "";
		try {
			csv.setBuffer(new BufferedReader(new FileReader(csv.getcsvproject())));
			ArrayList <String> list = new ArrayList<String>();
			//Tant que le csv contient des lignes qu'on n'a pas encore lu
			while((line = csv.getBuffer().readLine()) != null){
				//On divise chaque ligne en deux colonnes
				String[] column = line.split(",");
				//On compare la deuxièmme colonne (column[1]) qui contient le projectName(ou le groupName) avec le projectName (ou le groupName) de la ligne précedente (temp)
				if(column[1].equals(temp)){
					//On ajoute le username de cette ligne (column[0]) à la liste (qui contient les usernames) relative à la key column[1] dans csv_content
					csv_content.get(column[1]).add(column[0]);
				}
				else{
					//On initialise la liste qui va contenir les membres des projets
					list = new ArrayList<String>();
					//On ajoute le projectName (column[1]) et sa liste des membres (list) ds csv_content
					csv_content.put(column[1], list);
					//On ajoute le current username (column[0]) à la liste des membres du projectName (column[1])
					csv_content.get(column[1]).add(column[0]);
				}
				temp=column[1];
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return csv_content;
	}
	
	
	/**
	 * Parse le fichier csv contenant 3 colonnes (username,groupName,projectName)
	 * 
	 * @param csvFile	le fichier csv qu'on veut lire
	 * @return une hashMap contenant chaque groupe avec la liste des projets et des membres correspondants
	 */
	public static HashMap<String, HashMap<String, ArrayList<String>>> readcsv2(File csvFile) {
		//Déclarer un objet de type Csv
		Csv csv = new Csv(csvFile);
		//Déclarer l'hashMap qu'on va retourner
		HashMap<String, HashMap<String, ArrayList<String>>> csv_content = new HashMap<String, HashMap<String, ArrayList<String>>> ();
		String line = "";
		try {
			csv.setBuffer(new BufferedReader(new FileReader(csv.getcsvproject())));
			ArrayList <String> list = new ArrayList<String>();
			//Tant que le csv contient des lignes qu'on n'a pas encore lu
			while((line = csv.getBuffer().readLine()) != null){
				Boolean foundGrp = false;
				Boolean foundPrjt = false;
				String[] column = line.split(",");
				//On divise chaque ligne en trois colonnes
				for(String key : csv_content.keySet()) {
					if(column[1].equals(key)) {
						foundGrp = true;
						for(String key2 : csv_content.get(key).keySet()) {
							if(column[2].equals(key2)) {
								foundPrjt = true;
								csv_content.get(column[1]).get(column[2]).add(column[0]);
							}
						}
						if(!foundPrjt) {
							list = new ArrayList<String>();
							csv_content.get(column[1]).put(column[2], list);
							csv_content.get(column[1]).get(column[2]).add(column[0]);
						}
						
					}
				}
				if(!foundGrp) {
					list = new ArrayList<String>();
					HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
					hashMap.put(column[2], list);
					hashMap.get(column[2]).add(column[0]);
					csv_content.put(column[1], hashMap);
				}
				
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return csv_content;
	}
}
