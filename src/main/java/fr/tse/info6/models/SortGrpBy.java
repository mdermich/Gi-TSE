package fr.tse.info6.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
/**
 * Class SortGrpBy :
 * 
 * Classe qui ne possede qu'une et unique utilite :
 * Elle implemente une comparator, et permet de donc comparer deux Group
 * 
 * A noter que celle fonction n'est plus utilise, mais il reste bon de la garder.
 */
public class SortGrpBy implements Comparator<Group> {
	private static String sort;
	private static boolean order;

	/**
	 * @param String s, pour sort, donc le parametre en fonction duquel on veut trier
	 * @param boolean o, pour order, donc soit croissant soit decroissant
	 */
	SortGrpBy(String s, boolean o) {
		sort = s;
		order = o;
	}

	/**
	 * @override la fonction compare, pour comparer 2 Group.
	 * @param Group a
	 * @param Group b
	 * @return int 
	 * un entier negatif si a<b, positif si a>b, nul si a==b)
	 */
	@Override
	public int compare(Group a, Group b) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		if (order) {
			if (sort.equals("Nom"))
				return a.getName().compareToIgnoreCase(b.getName());
			if (sort.equals("Chemin"))
				return a.getPath().compareToIgnoreCase(b.getPath());
			else if (sort.equals("Date de cr\u00E9ation")) {
				try {
					return sdf.parse(a.getCreated_at()).compareTo(sdf.parse(b.getCreated_at()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else if (sort.equals("Nombre de membres"))
				return a.getNbr_membres() - b.getNbr_membres();
			else
				return a.getNbr_projects() - b.getNbr_projects();
			
			
		} else {
			if (sort.equals("Nom"))
				return b.getName().compareToIgnoreCase(a.getName());
			if (sort.equals("Chemin"))
				return b.getPath().compareToIgnoreCase(a.getPath());
			else if (sort.equals("Date de cr\u00E9ation")) {
				try {
					return sdf.parse(b.getCreated_at()).compareTo(sdf.parse(a.getCreated_at()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else if (sort.equals("Nombre de membres"))
				return b.getNbr_membres() - a.getNbr_membres();
			else
				return b.getNbr_projects() - a.getNbr_projects();
		}
		return 0;
	}

}
