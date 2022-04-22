package fr.tse.info6.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
/**
 * Class SortProjBy :
 * 
 * Classe qui ne possede qu'une et unique utilite :
 * Elle implemente une comparator, et permet de donc comparer deux Project
 * 
 */
public class SortProjBy implements Comparator<Project> {
	private static String sort;
	private static boolean order;

	/**
	 * @param String s, pour sort, donc le parametre en fonction duquel on veut trier
	 * @param boolean o, pour order, donc soit croissant soit decroissant
	 */
	SortProjBy(String s, boolean o) {
		sort = s;
		order = o;
	}
/**
	 * @override la fonction compare, pour comparer 2 Group.
	 * @param Project a
	 * @param Project b
	 * @return int 
	 * un entier negatif si a<b, positif si a>b, nul si a==b)
	 */
	@Override
	public int compare(Project a, Project b) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		if (order) {
			if (sort.equals("Nom du projet"))
				return a.getName().compareToIgnoreCase(b.getName());
			else if (sort.equals("Date de cr\u00E9ation")) {
				try {
					return sdf.parse(a.getCreated_at()).compareTo(sdf.parse(b.getCreated_at()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (sort.equals("Derni\u00E8re modification")) {
				try {
					return sdf.parse(a.getLast_modif()).compareTo(sdf.parse(b.getLast_modif()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (sort.equals("Nombre de branches"))
				return a.getNbr_branches() - b.getNbr_branches();
			else
				return a.getNbr_collaborators() - b.getNbr_collaborators();
			
			
		} else {
			if (sort.equals("Nom du projet"))
				return b.getName().compareToIgnoreCase(a.getName());
			else if (sort.equals("Date de cr\u00E9ation")) {
				try {
					return sdf.parse(b.getCreated_at()).compareTo(sdf.parse(a.getCreated_at()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (sort.equals("Derni\u00E8re modification")) {
				try {
					return sdf.parse(b.getLast_modif()).compareTo(sdf.parse(a.getLast_modif()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (sort.equals("Nombre de branches"))
				return b.getNbr_branches() - a.getNbr_branches();
			else
				return b.getNbr_collaborators() - a.getNbr_collaborators();
		}
		return 0;
	}

}
