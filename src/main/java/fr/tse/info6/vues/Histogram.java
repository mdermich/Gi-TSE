package fr.tse.info6.vues;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import fr.tse.info6.models.Project;
/**
 * Class Histogram
 * 
 * Classe qui permet de g\u00E9n\u00E9rer les diagrammes circulaires et les histogrammes pour les statistiques des projets, 
 * Cette classe stocke les graphs en png dans le dossier /Ressources du projet.
 * 
 */

public class Histogram {
	
	// variables globales de notre programme
	private JFreeChart pieChartNombreCommitTot, barChartNombreCommit, barChartNombreMergeRequest,pieChartNombreBrancheTot;
	private ChartPanel chartPanel;
	private static long maxCommitFromGroup;
	private static long maxBranchesFromGroup;
	private static long commitCount;
	private static long branchCount;
	
	public Histogram(JFreeChart pieChart1, JFreeChart barChart1,  ChartPanel chartPanel,JFreeChart barChart2,  JFreeChart pieChart2) {
		this.pieChartNombreCommitTot = pieChart1;
		this.barChartNombreCommit = barChart1;
		this.barChartNombreMergeRequest = barChart2;
		this.pieChartNombreBrancheTot = pieChart2;
		this.chartPanel = chartPanel;
	}
	
	/**
	 * 
	 * @param project
	 * @param maxBranchesFromGroup
	 * @param maxCommiFromGroup
	 */
	public Histogram(Project project,  long maxBranchesFromGroup, long maxCommiFromGroup) {
		
		// On commence par initialiser nos variables pour les inserer dans nos graphs
		
		// le  max du nb total de branches des projets d'un groupe
		setMaxBranchesFromGroup(maxBranchesFromGroup); 
		// le  max du nb total de commit des projets d'un groupe
		setMaxCommitFromGroup(maxCommiFromGroup);
		// Le nombre total de commit du projet selectionne
		setCommitCount(project.getCommitCount());
		// Le nombre total de branches du projet selectionne
		setBranchCount(project.getNbr_branches());
		
		
		
		// On cree le diagramme circulaire qui va comparer le nombre de commit total du projet par rapport au max 
		// du nb de commit total des projets du groupe (ou de tout les projets si on est pas ds un groupe)
		pieChartNombreCommitTot = ChartFactory.createPieChart(
		         "Nombre de Commit Totales",                       
		         nombreCommitTot(),                    
		         true, true, false);
		PiePlot NombreCommitTotPlot = (PiePlot) pieChartNombreCommitTot.getPlot();
		NombreCommitTotPlot.setSectionPaint( "Commit Tot: " + commitCount, new Color(95, 162, 99));
		NombreCommitTotPlot.setSectionPaint("Commit Tot Max: " + maxCommitFromGroup, new Color(0, 80, 156));

	
		// On save l'image dans le dossier ressource pour l'importer ensuite ds la classe interface
		try {
			ChartUtilities.saveChartAsPNG(new File("src/main/resources/commitTot.png"), pieChartNombreCommitTot, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// On cree le diagramme circulaire qui va comparer le nombre de branches total du projet par rapport au max 
		// du nb de branches total des projets du groupe (ou de tout les projets si on est pas ds un groupe)
		pieChartNombreBrancheTot = ChartFactory.createPieChart(
		         "Nombre de Branches Totales",                       
		         nombreBrancheTot(),                    
		         true, true, false);
		PiePlot NombreBrancheTotPlot = (PiePlot) pieChartNombreBrancheTot.getPlot();
		NombreBrancheTotPlot.setSectionPaint("Branches Tot: " + branchCount, new Color(95, 162, 99));
		NombreBrancheTotPlot.setSectionPaint("Branches Tot Max: " + maxBranchesFromGroup, new Color(0, 80, 156));

	
		// On save l'image dans le dossier ressource pour l'importer ensuite ds la classe interface
		try {
			ChartUtilities.saveChartAsPNG(new File("src/main/resources/brancheTot.png"), pieChartNombreBrancheTot, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//  on cree un histogramme qui va afficher le nombre total de commit par mois pour chaque projet selectionne
		barChartNombreCommit = ChartFactory.createBarChart(
		         "Nombre de commits par mois",           
		         "Mois",            
		         "Nombre de commits",            
		         nombreCommit(project),          
		         PlotOrientation.VERTICAL,           
		         true, true, false);
		CategoryPlot NombreCommitplot = barChartNombreCommit.getCategoryPlot();
		NombreCommitplot.getRenderer().setSeriesPaint(0, new Color(95, 162, 99));
  
	
		// On save l'image dans le dossier ressource pour l'importer ensuite ds la classe interface
		try {
			ChartUtilities.saveChartAsPNG(new File("src/main/resources/commit.png"), barChartNombreCommit, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//  on cree un histogramme qui va afficher le nombre total de merge request par mois pour chaque projet selectionne
		barChartNombreMergeRequest = ChartFactory.createBarChart(
		         "Nombre Merge Request par mois",           
		         "Mois",            
		         "Nombre de merge request",            
		         nombreMergeRequest(project),          
		         PlotOrientation.VERTICAL,           
		         true, true, false);
		CategoryPlot NombreMergeRequestplot = barChartNombreMergeRequest.getCategoryPlot();
		NombreMergeRequestplot.getRenderer().setSeriesPaint(0, new Color(95, 162, 99));
	
		// On save l'image dans le dossier ressource pour l'importer ensuite ds la classe interface
		try {
			ChartUtilities.saveChartAsPNG(new File("src/main/resources/mergeRequest.png"), barChartNombreMergeRequest, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	// ces deux m\u00E9thodes (nombreCommitTot () et nombreBrancheTot() permettent
	//d'inserer les donnees dans les diagrammes circulaires
	/**
	* Insere les donnees dans  le  diagramme circulaire pour le nombre de commit
	*
	* @param  None
	* @return dataset	les data du diagramme
	*/
	 private static PieDataset nombreCommitTot() {
	      DefaultPieDataset dataset = new DefaultPieDataset( );
	      dataset.setValue( "Commit Tot: " + commitCount , commitCount );  
	      dataset.setValue( "Commit Tot Max: " + maxCommitFromGroup , maxCommitFromGroup );   
	      return dataset;         
	   }
	 
	/**
	* Insere les donnees dans  le  diagramme circulaire pour le nombre de branches
	*
	* @param  None
	* @return dataset	les data du diagramme
	*/
	 private static PieDataset nombreBrancheTot() {
	      DefaultPieDataset dataset = new DefaultPieDataset( );
	      dataset.setValue( "Branches Tot: " + branchCount  , branchCount);  
	      dataset.setValue( "Branches Tot Max: " + maxBranchesFromGroup , maxBranchesFromGroup );   
	      return dataset;         
	   }
	  
	// ces deux m\u00E9thodes (nombreCommit () et nombreMergeRequest() permettent
	//d'inserer les donnees dans les histogrammes
	/**
	* Insere les donnees dans  l'histogramme pour les commit 
	*
	* @param  None
	* @return dataset	les data de l'histogramme
	*/
	public CategoryDataset nombreCommit(Project project) {
	      final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
	      Integer commitJanvier = 0;
	      if (project.getListCommits().containsKey(1)) {
	    	  commitJanvier = project.getListCommits().get(1);
	      }
	      
	      Integer commitFevrier = 0;
	      if (project.getListCommits().containsKey(2)) {
	    	  commitFevrier = project.getListCommits().get(2);
	      }
	      
	      Integer commitMars = 0;
	      if (project.getListCommits().containsKey(3)) {
	    	  commitMars = project.getListCommits().get(3);
	      }
	      
	      Integer commitAvril = 0;
	      if (project.getListCommits().containsKey(4)) {
	    	  commitAvril = project.getListCommits().get(4);
	      }
	      
	      Integer commitMai = 0;
	      if (project.getListCommits().containsKey(5)) {
	    	  commitMai = project.getListCommits().get(5);
	      }
	      
	      Integer commitJuin = 0;
	      if (project.getListCommits().containsKey(6)) {
	    	  commitJuin = project.getListCommits().get(6);
	      }
	      
	      Integer commitJuillet = 0;
	      if (project.getListCommits().containsKey(7)) {
	    	  commitJuillet = project.getListCommits().get(7);
	      }
	      
	      Integer commitAout = 0;
	      if (project.getListCommits().containsKey(8)) {
	    	  commitAout = project.getListCommits().get(8);
	      }
	      
	      Integer commitSeptembre = 0;
	      if (project.getListCommits().containsKey(9)) {
	    	  commitSeptembre = project.getListCommits().get(9);
	      }
	      
	      Integer commitOctobre = 0;
	      if (project.getListCommits().containsKey(10)) {
	    	  commitOctobre = project.getListCommits().get(10);
	      }
	      
	     
	      Integer commitNovembre = 0;
	      if (project.getListCommits().containsKey(11)) {
	    	  commitNovembre = project.getListCommits().get(11);
	      }
	      
	      Integer commitDecembre = 0;
	      if (project.getListCommits().containsKey(12)) {
	    	  commitDecembre = project.getListCommits().get(12);
	      }
	      
	      dataset.addValue(commitJanvier, "" , "Jan");  
	      dataset.addValue(commitFevrier , "" , "F\u00E9v");  
	      dataset.addValue(commitMars , "" , "Mar");  
	      dataset.addValue(commitAvril, "" , "Avr");  
	      dataset.addValue(commitMai , "" , "Mai");  
	      dataset.addValue(commitJuin , "" , "Juin");  
	      dataset.addValue(commitJuillet , "" , "Jui");  
	      dataset.addValue(commitAout, "" , "Aou");  
	      dataset.addValue(commitSeptembre , "" , "Sep");  
	      dataset.addValue(commitOctobre , "" , "Oct");  
	      dataset.addValue(commitNovembre , "" , "Nov");  
	      dataset.addValue(commitDecembre , "" , "D\u00E9c");  

	      return dataset; 
	   }

	/**
	* Insere les donnees dans  l'histogramme pour les merges  request 
	*
	* @param  None
	* @return dataset	les data de l'histogramme
	*/
	
	public CategoryDataset nombreMergeRequest(Project project) {
			
	      final DefaultCategoryDataset dataset = new DefaultCategoryDataset();  
	      
	      Integer commitJanvier = 0;
	      if (project.getListMerges().containsKey(1)) {
	    	  commitJanvier = project.getListMerges().get(1);
	      }
	      
	      Integer commitFevrier = 0;
	      if (project.getListMerges().containsKey(2)) {
	    	  commitFevrier = project.getListMerges().get(2);
	      }
	      
	      Integer commitMars = 0;
	      if (project.getListMerges().containsKey(3)) {
	    	  commitMars = project.getListMerges().get(3);
	      }
	      
	      Integer commitAvril = 0;
	      if (project.getListMerges().containsKey(4)) {
	    	  commitAvril = project.getListMerges().get(4);
	      }
	      
	      Integer commitMai = 0;
	      if (project.getListMerges().containsKey(5)) {
	    	  commitMai = project.getListMerges().get(5);
	      }
	      
	      Integer commitJuin = 0;
	      if (project.getListMerges().containsKey(6)) {
	    	  commitJuin = project.getListMerges().get(6);
	      }
	      
	      Integer commitJuillet = 0;
	      if (project.getListMerges().containsKey(7)) {
	    	  commitJuillet = project.getListMerges().get(7);
	      }
	      
	      Integer commitAout = 0;
	      if (project.getListMerges().containsKey(8)) {
	    	  commitAout = project.getListMerges().get(8);
	      }
	      
	      Integer commitSeptembre = 0;
	      if (project.getListMerges().containsKey(9)) {
	    	  commitSeptembre = project.getListMerges().get(9);
	      }
	      
	      Integer commitOctobre = 0;
	      if (project.getListMerges().containsKey(10)) {
	    	  commitOctobre = project.getListMerges().get(10);
	      }
	      
	     
	      Integer commitNovembre = 0;
	      if (project.getListMerges().containsKey(11)) {
	    	  commitNovembre = project.getListMerges().get(11);
	      }
	      
	      Integer commitDecembre = 0;
	      if (project.getListMerges().containsKey(12)) {
	    	  commitDecembre = project.getListMerges().get(12);
	      }
	      
	      dataset.addValue(commitJanvier, "" , "Jan");  
	      dataset.addValue(commitFevrier , "" , "F\u00E9v");  
	      dataset.addValue(commitMars , "" , "Mar");  
	      dataset.addValue(commitAvril, "" , "Avr");  
	      dataset.addValue(commitMai , "" , "Mai");  
	      dataset.addValue(commitJuin , "" , "Juin");  
	      dataset.addValue(commitJuillet , "" , "Jui");  
	      dataset.addValue(commitAout, "" , "Aou");  
	      dataset.addValue(commitSeptembre , "" , "Sep");  
	      dataset.addValue(commitOctobre , "" , "Oct");  
	      dataset.addValue(commitNovembre , "" , "Nov");  
	      dataset.addValue(commitDecembre , "" , "D\u00E9c");   

	      return dataset; 
	   }
	
	// getters et setters 
	
	public JFreeChart getChart1() {
		return pieChartNombreCommitTot;
	}
	
	public JFreeChart getChart3() {
		return barChartNombreMergeRequest;
	}
	
	public JFreeChart getChart2() {
		return barChartNombreCommit;
	}
	
	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public JFreeChart getBarChartNombreBrancheTot() {
		return pieChartNombreBrancheTot;
	}

	public void setBarChartNombreBrancheTot(JFreeChart barChartNombreBrancheTot) {
		this.pieChartNombreBrancheTot = barChartNombreBrancheTot;
	}

	public static long getCommitCount() {
		return commitCount;
	}

	public static void setCommitCount(long commitCount) {
		Histogram.commitCount = commitCount;
	}

	public static long getBranchCount() {
		return branchCount;
	}

	public static void setBranchCount(long branchCount) {
		Histogram.branchCount = branchCount;
	}
	
	public long getMaxCommitFromGroup() {
		return maxCommitFromGroup;
	}

	public void setMaxCommitFromGroup(long maxCommitFromGroup) {
		Histogram.maxCommitFromGroup = maxCommitFromGroup;
	}

	public long getMaxBranchesFromGroup() {
		return maxBranchesFromGroup;
	}

	public void setMaxBranchesFromGroup(long maxBranchesFromGroup) {
		Histogram.maxBranchesFromGroup = maxBranchesFromGroup;
	}


}
