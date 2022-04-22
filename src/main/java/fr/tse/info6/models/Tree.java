package fr.tse.info6.models;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
/**
 * Class Tree :
 * 
 * Classe qui gere l'arbre qui affiche tous les groupes et leurs sous-groupes
 * Elle ne possede qu'une methode setModel
 * 
 */
public class Tree {

	/**
	 * @param JTree tree, l'element JSwing sur lequel on travaille, puis qu'on retourne
	 * @param String str, le nom de l'utilisteur, qui sera aussi le nom de l'arbre, ou son noeud principal
	 * @param List<Group> groups, la liste de Grou^p que l'on veut implementer dans l'arbre
	 * 
	 * @return JTree, l'abre sur lequel on a travaille
	 * 
	 * On travaille de maniere similaire a Group.selectAllProjects(). 
	 * Bien que peu optimale, elle permet de savoir quel groupes est sous-groupes de quel groupes, etc...
	 * Elle est cependant limite a pouvoir inclure dans l'arbre des sous-sous-sous-groupes au maximum.
	 */
	public static JTree setModel(JTree tree, String str, final List<Group> groups) {

		tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(str) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				DefaultMutableTreeNode node0;
				DefaultMutableTreeNode node1;
				DefaultMutableTreeNode node2;

				for (Group grp : groups) {
					node0 = new DefaultMutableTreeNode(grp);
					if (grp.getGroups() != null) {
						for (Group sub : grp.getGroups()) {
							if (sub.getGroups() == null) {
								node0.add(new DefaultMutableTreeNode(sub));
							} else {
								node1 = new DefaultMutableTreeNode(sub);
								for (Group subsub : sub.getGroups()) {
									if (subsub.getGroups() == null) {
										node1.add(new DefaultMutableTreeNode(subsub));
									} else {
										node2 = new DefaultMutableTreeNode(subsub);
										for (Group subsubsub : subsub.getGroups()) {
											node2.add(new DefaultMutableTreeNode(subsubsub));
										}
										node1.add(node2);
									}
								}
								node0.add(node1);
							}

						}
					}
					add(node0);
				}
			}
		}));

		return tree;
	}

}
