package fr.tse.info6.vues;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.apache.http.client.ClientProtocolException;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;

import fr.tse.info6.controllers.Archive;
import fr.tse.info6.controllers.CipherToken;
import fr.tse.info6.controllers.CreateProjectsFromCsvFile;
import fr.tse.info6.controllers.CreateProjectsGroupsFromCsvFile;
import fr.tse.info6.controllers.CreateGroupsFromCsvFile;
import fr.tse.info6.controllers.GitLabInstance;
import fr.tse.info6.controllers.ImportUserProjects;
import fr.tse.info6.controllers.Login;
import fr.tse.info6.models.Group;
import fr.tse.info6.models.Project;
import fr.tse.info6.models.Tree;

public class Interface implements ActionListener {

	private static boolean flag = false;

	private static String access_token;
	private static ArrayList<String> members;

	private static List<Project> projects;
	private static List<Project> allprojects;
	private static List<Project> archivedprojects = new ArrayList<>();

	private static List<Group> groups, allgroups;

	private static Group selectedGroup;
	
	private static User user;
	private static JFrame frame;
	private static String user_info;
	private static JToggleButton toggleButton;
	private static JTextField txtTrierPar;

	private static JPanel panel;
	private static JLabel userLabel, infoLabel, prNameLabel, prMembersLabel, addMemberLabel, addProjectLabel, txtConnect;

	// Page de connexion
	private static JTextField userText, namePrText, membersPrText;
	private static JLabel lblMotDePasse;
	private static JPasswordField pwText;
	private static JButton connect_button, crProjects_button;
	private static JButton dev_button;
	private static JButton archiveIconLabel, unarchiveIconLabel;
	private static JLabel connexionMessage;
	private static JCheckBox rememberMeBtn, showArchivedProjectsBtn;

	private static Color c;
	private static ImageIcon logo;
	private static ImageIcon logoGitse;

	// Page après connexion
	private static JTable projectsTable;
	private static JTable grpTable, groupMembersTable;

	private static JScrollPane leftPanel, groupMembersPane, centerPane;
	private static JTree tree;
	private static JButton sortButton;
	private static JComboBox<Object> comboBox;
	private static JTextField txtAfficher;
	private static boolean grpOrPrj;
	private static JMenuItem mntmAddPrjCSV, mntmAddPrj;
	private static JMenuItem mntmAddGrpCSV;
	private static JMenuItem mntmAddBoth;

	JMenuItem mntLogOut;
	JMenuItem mntClose;
	
	//Visualisation d'un projet
	private static JFrame displayProjectsFrame;
	private static JPanel displayProjectsPanel;
	private  static Font sousTitreFont, contenueFont;
	private static JLabel projectIDLabel, projectNameLabel, projectStatisticsLabel,projectDateModifiedLabel,projectDateCreationLabel;
	private static JScrollPane sp;
	
	//Creation d'un projet sans csv 
	private static JFrame createProjectsFrame;
	private  static JPanel createProjectsPanel;
	private  static JButton createProject_button, addMember_button;
	
	// CSV
	private static JFrame csvFrame;
	private static JPanel csvPanel;
	private static JPanel csvPanel2;
	private static JLabel label;
	static JTextField textField;
	static JFileChooser fileChooser;
	static String selectedPath;
	JTextArea affichageSelectedFile;
	static private JButton nextButtonPrj;
	static private JButton nextButtonGrp;
	static private JButton nextButtonBoth;

	private static int mode;
	public static final int MODE_OPEN = 1;
	public static final int MODE_SAVE = 2;

	public Interface() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, IOException {

		projects = new ArrayList<Project>();
		allprojects = new ArrayList<Project>();

		allgroups = new ArrayList<Group>();
		groups = new ArrayList<Group>();
		grpOrPrj = true;

		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		c = new Color(218, 218, 218);
		panel.setBackground(Color.LIGHT_GRAY);
		logo = new ImageIcon(this.getClass().getResource("/logoTSE2.png"));
		JLabel logoLabel = new JLabel(logo);
		logoLabel.setBounds(50, 0, 200, 70);

		panel.add(logoLabel);



		CipherToken CT = new CipherToken();
		if (CT.dechiffrer().equals("")) {
			

			JPanel panel_1 = new JPanel();
			panel_1.setBounds(287, 63, 409, 401);
			panel.add(panel_1);
			panel_1.setLayout(null);
			
			// Le radioButton prend le focus \u00E0 la place du text. Pour que celui puisse voir qu'il est s\u00E9lecionn\u00E9.
			JRadioButton btn = new JRadioButton("text");
		    panel_1.add(btn);
		    
			txtConnect = new JLabel("Connectez-vous \u00E0 votre compte Gitlab");
			txtConnect.setForeground(Color.BLACK);
			txtConnect.setFont(new Font("Tahoma", Font.PLAIN, 20));
			txtConnect.setBounds(37, 106, 384, 22);
			panel_1.add(txtConnect);

			
			userLabel = new JLabel("Nom d'utilisateur :");
			userLabel.setBounds(frame.getWidth() / 2 - 150, 300, 120, 25);
			panel.add(userLabel);

			
			userText = new JTextField();
			userText.setMargin(new Insets(2, 10, 2, 2));
			userText.setFont(new Font("Tahoma", Font.PLAIN, 18));
			userText.setForeground(Color.LIGHT_GRAY);
			userText.setText("Nom d'utilisateur");
			userText.setBounds(37, 154, 336, 36);
			panel_1.add(userText);
			userText.setColumns(10);
			userText.addMouseListener(new MouseAdapter() {
				  @Override
				  public void mouseClicked(MouseEvent e) {
					  userText.setText("");
					  userText.setForeground(Color.BLACK);
				  }
				});
			
			pwText = new JPasswordField();
			pwText.setText("Mot de passe");
			pwText.setMargin(new Insets(2, 10, 2, 2));
			pwText.setForeground(Color.LIGHT_GRAY);
			pwText.setFont(new Font("Tahoma", Font.PLAIN, 18));
			pwText.setColumns(10);
			pwText.setBounds(37, 221, 336, 36);
			panel_1.add(pwText);
			pwText.addMouseListener(new MouseAdapter() {
				  @Override
				  public void mouseClicked(MouseEvent e) {
					  pwText.setText("");
					  pwText.setForeground(Color.BLACK);
				  }
				});

			
			
			lblMotDePasse = new JLabel("<HTML><U>Mot de passe oubli\u00E9 ?</U></HTML>");
			lblMotDePasse.setForeground(Color.BLUE);
			lblMotDePasse.setFont(new Font("Tahoma", Font.PLAIN, 11));
			lblMotDePasse.setBounds(271, 273, 384, 22);
			lblMotDePasse.addMouseListener(new MouseAdapter() {
				  @Override
				  public void mouseClicked(MouseEvent e) {
					  try {
						Desktop.getDesktop().browse(new URI("https://gitlab.com/users/password/new"));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				  }
				});
			panel_1.add(lblMotDePasse);

			
			connect_button = new JButton("Se connecter");
			connect_button.setRolloverEnabled(false);
			connect_button.setRequestFocusEnabled(false);
			connect_button.setFont(new Font("Tahoma", Font.ITALIC, 18));
			connect_button.setBounds(37, 326, 336, 29);
			panel_1.add(connect_button);
			connect_button.setFocusable(false);
			connect_button.addActionListener(this);

			
			rememberMeBtn = new JCheckBox("Se souvenir de moi");
			rememberMeBtn.setBounds(37, 273, 139, 23);
			rememberMeBtn.setSize(160, 25);
			panel_1.add(rememberMeBtn);

			dev_button = new JButton("Dev");
			dev_button.setSize(60, 20);
			dev_button.setLocation((frame.getWidth() - 75), (frame.getHeight() - 60));
			panel.add(dev_button);
			dev_button.setContentAreaFilled(false);
			dev_button.setFocusable(false);
			dev_button.addActionListener(this);
			dev_button.setVisible(false);
			
			logoGitse = new ImageIcon(this.getClass().getResource("/gitse2.png"));
			JLabel logoGitseLabel = new JLabel(logoGitse);
			logoGitseLabel.setBounds((panel_1.getWidth()-80)/2, 25, 80, 60);
			panel_1.add(logoGitseLabel);

			connexionMessage = new JLabel();
			connexionMessage.setBounds(frame.getWidth() / 2 - 150, 420, 300, 40);
			panel.add(connexionMessage);
			frame.setVisible(true);
			panel.setVisible(true);

		} else {
			userLabel = new JLabel("Vous 	\r\n"
					+ "\u00eates d\u00E9j\u00E0 connect\u00E9. Chargement de vos informations...");
			userLabel.setBounds(frame.getWidth() / 2 - 250, 230, 600, 25);
			userLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

			panel.add(userLabel);

			frame.setVisible(true);
			panel.setVisible(true);

			access_token = CT.dechiffrer();
			System.out.println(access_token);
			welcomePopUp(access_token);
		}

	}

	public void welcomePopUp(final String tk) {
		SwingWorker<?, ?> swingWorker = new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				txtConnect.setForeground(new Color(137,194,88));
				txtConnect.setText("Chargement de vos informations...");
				return 0;
			}

			@Override
			protected void done() {
				System.out.println("Cr\u00E9ation de l'instance gitlab...");

				GitLabInstance.createGitLabInstance(tk);

				try {
					user = ImportUserProjects.importUser();

					System.out.println("Copie des projets...");
					projects = ImportUserProjects.importProjects();
					allprojects.addAll(projects);

					System.out.println("Copie des groupes...");
					allgroups = ImportUserProjects.importGroups();
					groups = ImportUserProjects.transformGroups(allgroups);

				} catch (GitLabApiException e) {
					e.printStackTrace();
				}

				panel.setVisible(false);
				connectedPage();

			}

		};
		swingWorker.execute();

	}

	public void connectedPage() {

		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 984, 32);
		frame.getContentPane().add(menuBar);

		JMenu mnAddPrj = new JMenu("Ajouter projet(s)");
		mnAddPrj.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mnAddPrj.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		mnAddPrj.add(Box.createHorizontalGlue());
		menuBar.add(mnAddPrj);

		mntmAddPrj = new JMenuItem("Ajouter un projet");
		mntmAddPrj.addActionListener(this);
		mnAddPrj.add(mntmAddPrj);

		mntmAddPrjCSV = new JMenuItem("Ajouter des projets \u00E0 partir d'un CSV");
		mnAddPrj.add(mntmAddPrjCSV);

		mntmAddPrjCSV.addActionListener(this);

		JMenu mnAddGrp = new JMenu("Ajouter groupe(s)");
		mnAddGrp.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		menuBar.add(mnAddGrp);

		mntmAddGrpCSV = new JMenuItem("Ajouter des groupes \u00E0 partir d'un CSV");
		mnAddGrp.add(mntmAddGrpCSV);
		mntmAddGrpCSV.addActionListener(this);

		JMenu mnAddBoth = new JMenu("Ajouter projet et groupe");
		mnAddBoth.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		menuBar.add(mnAddBoth);

		mntmAddBoth = new JMenuItem("Ajouter \u00E0 partir d'un CSV");
		mnAddBoth.add(mntmAddBoth);
		mntmAddBoth.addActionListener(this);

		JMenu mnSession = new JMenu("Session");
		mnSession.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(mnSession);

		mntLogOut = new JMenuItem("Se d\u00E9connecter");
		mnSession.add(mntLogOut);
		mntLogOut.addActionListener(this);

		mntClose = new JMenuItem("Fermer");
		mnSession.add(mntClose);
		mntClose.addActionListener(this);
		
		// Tableau des membres du groupe
				groupMembersPane = new JScrollPane();
				groupMembersPane.setBounds(198, 55, 776, 112);
				frame.getContentPane().add(groupMembersPane);

				groupMembersTable = new JTable();

				groupMembersTable.setRowHeight(20);
				groupMembersTable.setSelectionBackground(Color.LIGHT_GRAY);
				groupMembersTable.setIntercellSpacing(new Dimension(0, 0));
				groupMembersTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
				groupMembersTable.getTableHeader().setFont(new Font("Tahoma", Font.ITALIC, 12));
				groupMembersTable.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
								"Nom du membre", "R\u00f4le du membre"
						})

				{
					Class[] columnTypes = new Class[] {
							String.class, String.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
					boolean[] columnEditables = new boolean[] {
							false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
				groupMembersTable.getColumnModel().getColumn(0).setResizable(false);
				groupMembersTable.getColumnModel().getColumn(1).setResizable(false);

				groupMembersPane.setViewportView(groupMembersTable);

				groupMembersPane.setVisible(false);
				////////////////////////////////////

		centerPane = new JScrollPane();
		centerPane.setBackground(Color.WHITE);
		centerPane.setBounds(198, 55, 776, 452);
		frame.getContentPane().add(centerPane);

		// Toute la partie pour la tableau des projets
		projectsTable = new JTable();
		projectsTable.setRequestFocusEnabled(false);
		projectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		projectsTable.setRowHeight(30);
		projectsTable.setSelectionBackground(Color.LIGHT_GRAY);
		projectsTable.setIntercellSpacing(new Dimension(0, 0));
		projectsTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
		projectsTable.getTableHeader().setFont(new Font("Tahoma", Font.ITALIC, 12));
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) projectsTable.getTableHeader()
				.getDefaultRenderer();
		renderer.setHorizontalAlignment(JLabel.LEFT);
		projectsTable.setShowVerticalLines(false);
		projectsTable.setShowHorizontalLines(false);
		projectsTable.setToolTipText("");

		projectsTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Nom du projet",
				"Cr\u00E9\u00E9 le", "Modifi\u00E9 le", "Nombre branches", "Nombre collaborateurs" }) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		DefaultTableModel model = (DefaultTableModel) projectsTable.getModel();
		for (Project project : projects) {
			model.addRow(new Object[] { project.getName(), project.getCreated_at(), project.getLast_modif(),
					project.getNbr_branches(), project.getNbr_collaborators() });
		}

		projectsTable.getColumnModel().getColumn(0).setResizable(false);
		projectsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		projectsTable.getColumnModel().getColumn(0).setMinWidth(124);
		projectsTable.getColumnModel().getColumn(1).setResizable(false);
		projectsTable.getColumnModel().getColumn(2).setResizable(false);
		projectsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		projectsTable.getColumnModel().getColumn(3).setResizable(false);
		projectsTable.getColumnModel().getColumn(3).setPreferredWidth(93);
		projectsTable.getColumnModel().getColumn(4).setResizable(false);
		projectsTable.getColumnModel().getColumn(4).setPreferredWidth(99);
		centerPane.setViewportView(projectsTable);

		projectsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					Project project = Project.getProject(projects, projectsTable.getValueAt(row, 0));
					if (project != null) {

						try {
							displayProject(project);
						} catch (GitLabApiException e1) {
							e1.printStackTrace();
						}
						
					} 
				}
			}
		});

		// Toute la partie pour le tableau des groupes
		grpTable = new JTable();
		grpTable.setRequestFocusEnabled(false);
		grpTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		grpTable.setRowHeight(30);
		grpTable.setSelectionBackground(Color.LIGHT_GRAY);
		grpTable.setIntercellSpacing(new Dimension(0, 0));
		grpTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
		grpTable.getTableHeader().setFont(new Font("Tahoma", Font.ITALIC, 12));
		DefaultTableCellRenderer renderer2 = (DefaultTableCellRenderer) grpTable.getTableHeader().getDefaultRenderer();
		renderer2.setHorizontalAlignment(JLabel.LEFT);
		grpTable.setShowVerticalLines(false);
		grpTable.setShowHorizontalLines(false);
		grpTable.setToolTipText("");

		grpTable.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Nom du groupe", "Chemin",
				"Cr\u00E9\u00E9 le", "Nombre de membres", "Nombre de projets" }) {

			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class, Object.class, Object.class, Object.class, Object.class };

			public Class<?> getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { true, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		DefaultTableModel modelgrp = (DefaultTableModel) grpTable.getModel();
		for (Group group : groups) {
			modelgrp.addRow(new Object[] { group.getPath(), group.getName(), group.getCreated_at(),
					group.getNbr_membres(), group.getNbr_projects() });
		}

		grpTable.getColumnModel().getColumn(0).setResizable(false);
		grpTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		grpTable.getColumnModel().getColumn(0).setMinWidth(124);
		grpTable.getColumnModel().getColumn(1).setResizable(false);
		grpTable.getColumnModel().getColumn(1).setPreferredWidth(175);

		grpTable.getColumnModel().getColumn(2).setResizable(false);
		grpTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		grpTable.getColumnModel().getColumn(3).setResizable(false);
		grpTable.getColumnModel().getColumn(3).setPreferredWidth(93);
		grpTable.getColumnModel().getColumn(4).setResizable(false);
		grpTable.getColumnModel().getColumn(4).setPreferredWidth(99);
		grpTable.setVisible(false);

		JPanel botPanel = new JPanel();
		botPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		botPanel.setBounds(198, 515, 776, 42);
		frame.getContentPane().add(botPanel);
		botPanel.setLayout(null);
		
		showArchivedProjectsBtn = new JCheckBox("Projets archiv\u00E9s");
		showArchivedProjectsBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		showArchivedProjectsBtn.setBounds(619, 10, 135, 23);
		//showArchivedProjectsBtn.setSize(50, 50);
		showArchivedProjectsBtn.addActionListener(this);
		botPanel.add(showArchivedProjectsBtn);

		txtTrierPar = new JTextField();
		txtTrierPar.setBorder(null);
		txtTrierPar.setOpaque(false);
		txtTrierPar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtTrierPar.setText("Trier par :");
		txtTrierPar.setBounds(10, 0, 113, 38);
		botPanel.add(txtTrierPar);
		txtTrierPar.setColumns(10);

		toggleButton = new JToggleButton("par ordre croissant");
		toggleButton.setContentAreaFilled(false);
		toggleButton.setFocusable(false);
		toggleButton.setFocusPainted(false);
		toggleButton.setBounds(294, 7, 160, 25);
		botPanel.add(toggleButton);

		comboBox = new JComboBox<Object>();
		comboBox.setRequestFocusEnabled(false);
		comboBox.setFocusable(false);
		comboBox.setFocusTraversalKeysEnabled(false);
		comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] { "Nom", "Date de cr\u00E9ation",
				"Derni\u00E8re modification", "Nombre de branches", "Nombre de collaborateurs" }));
		comboBox.setBounds(108, 7, 174, 25);
		botPanel.add(comboBox);

		sortButton = new JButton("Trier");
		sortButton.setContentAreaFilled(false);
		sortButton.setFocusPainted(false);
		sortButton.setFocusTraversalKeysEnabled(false);
		sortButton.setFocusable(false);
		sortButton.setFont(new Font("Tahoma", Font.ITALIC, 13));
		sortButton.setBounds(464, 0, 135, 40);
		sortButton.addActionListener(this);
		botPanel.add(sortButton);

		leftPanel = new JScrollPane();
		leftPanel.setBounds(10, 55, 181, 502);
		frame.getContentPane().add(leftPanel);

		txtAfficher = new JTextField();
		txtAfficher.setFocusTraversalKeysEnabled(false);
		txtAfficher.setFocusable(false);
		txtAfficher.setEditable(false);
		txtAfficher.setVerifyInputWhenFocusTarget(false);
		txtAfficher.setRequestFocusEnabled(false);
		txtAfficher.setOpaque(false);
		txtAfficher.setHorizontalAlignment(SwingConstants.LEFT);
		txtAfficher.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtAfficher.setText("Groupes / Sous-groupes :");
		leftPanel.setColumnHeaderView(txtAfficher);
		txtAfficher.setColumns(10);

		tree = new JTree();
		tree.setFocusable(false);

		tree = Tree.setModel(tree, user.getName(), groups);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// Boucle qui permet d'afficher l'arbre en entier dès le d\u00E9part. (de
		// l'auto-expand)
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		tree.addTreeSelectionListener(new SelectionListener());

		leftPanel.setViewportView(tree);

		if (flag == true) {
			grpOrPrj = false;
			grpTable.setVisible(true);
			projectsTable.setVisible(false);
			centerPane.setViewportView(grpTable);
			comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] { "Nom du groupe", "Chemin",
					"Date de cr\u00E9ation", "Nombre de membres", "Nombre de projets" }));
		} else {
			grpOrPrj = true;
			grpTable.setVisible(false);
			projectsTable.setVisible(true);
			centerPane.setViewportView(projectsTable);
			comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] { "Nom du projet", "Date de cr\u00E9ation",
					"Derni\u00E8re modification", "Nombre de branches", "Nombre de collaborateurs" }));
		}

		toggleButton.addChangeListener((ChangeListener) new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				if (toggleButton.isSelected()) {
					toggleButton.setText("par ordre d\u00E9croissant");
				} else {
					toggleButton.setText("par ordre croissant");
				}
			}
		});

		frame.revalidate();
		frame.repaint();

		if (flag = true) {

			JOptionPane.showMessageDialog(frame,
					"Vous pouvez maintenant acc\u00E9der \u00E0 vos groupes. Vous pouvez toujours choisir d'afficher vos projets \u00E0 l'aide du menu \u00E0 gauche!",
					user.getName(), JOptionPane.INFORMATION_MESSAGE);

		}

		else {
			JOptionPane.showMessageDialog(frame,
					"Vous pouvez maintenant acc\u00E9der \u00E0 vos projets. Vous pouvez toujours choisir d'afficher vos groupes \u00E0 l'aide du menu \u00E0 gauche!",
					user.getName(), JOptionPane.INFORMATION_MESSAGE);
		}

	}


	/**
	* Permet de visualiser les donnees du projets et ses stats en detailles
	* permet aussi d'archiver, desarchiver un projet si on est proprietaire (owner) 
	* du projet
	*
	* @param  None
	* @return None
	*/
	public void displayProject(Project project) throws GitLabApiException {


		displayProjectsFrame = new JFrame();

		
		displayProjectsFrame.setBackground(Color.WHITE);
		displayProjectsFrame.getContentPane().setBackground(Color.WHITE);
		displayProjectsFrame.setResizable(true);
		displayProjectsFrame.setBounds(frame.getX()+100, frame.getY()+100, 1100, 650);
		displayProjectsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		displayProjectsPanel = new JPanel();
		displayProjectsFrame.getContentPane().add(displayProjectsPanel);
		displayProjectsPanel.setLayout(null);
		displayProjectsPanel.setVisible(true);

		displayProjectsPanel.setBackground(Color.WHITE);
		
		String project_name = project.getName();
		try { 
			project_name = project_name.substring(0, 1).toUpperCase() + project_name.substring(1);
        }
		catch (Exception e)	{}
		
		sousTitreFont = new Font ("Serif", Font.PLAIN, 24);
		contenueFont = new Font ("Serif", Font.PLAIN, 18);
		
		projectNameLabel = new JLabel(project_name);
		projectNameLabel.setBounds(60, 15, 500, 44);
		projectNameLabel.setFont(new Font("Serif", Font.PLAIN, 40));
		displayProjectsPanel.add(projectNameLabel);
		

		projectIDLabel = new JLabel("Project ID:" + project.getId());
		projectIDLabel.setBounds(60, 70, 300, 24);
		projectIDLabel.setFont(sousTitreFont);
		displayProjectsPanel.add(projectIDLabel);

		projectStatisticsLabel = new JLabel("Statistiques du projet :");
		projectStatisticsLabel.setBounds(60, 220, 300, 30);
		projectStatisticsLabel.setFont(sousTitreFont);
		displayProjectsPanel.add(projectStatisticsLabel);
		
		
		
		List<Member> members = project.getMembers();
		//  On remplis le JTable avec les membres et leur role au seins du groupe
		
	    JTable jt=new JTable();
	    if (members.size()<=1) {
	    	jt.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Nom du membre",  "R\u00f4le du membre" }){
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] { false, false};

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}});
	    }
	    
	    if (members.size()>1) {
	    	jt.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Nom des membres",  "Rôle des membres" }){
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] { false, false};

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}});  
	    }
	    
	    DefaultTableModel model = (DefaultTableModel) jt.getModel();
	    for (Member  m :members) {
					String access_level = "";
					switch(m.getAccessLevel())
					{
					case OWNER:
						access_level += "Owner";
						break;
					case ADMIN:
						access_level += "Admin";
						break;
					case MAINTAINER:
						access_level += "Maintainer";
						break;
					case DEVELOPER:
						access_level += "Developer";
						break;
					case REPORTER:
						access_level += "Reporter";
						break;
					case GUEST:
						access_level += "Guest";
						break;
					case NONE:
						access_level += "None";
						break;
					case INVALID:
						access_level += "Invalid";
						break;
					case MINIMAL_ACCESS:
						access_level += "Minimal Access";
						break;
					default:
						break;
					}
					model.addRow(new Object[] { m.getName(), access_level });
				
		}	
	    // Permet de scroller si le nombre de membre est trop  grand
	    sp=new JScrollPane();    
        sp.setViewportView(jt);
	    sp.setBounds(390,76,300,169);
	    sp.setVisible(true);
	    displayProjectsPanel.add(sp);
		
		String str = project.getCreated_at();
        String[] dates_created = str.split(" ");
        String date_created = dates_created[0];
        
        // On appelle le constructeur de la calsse Histogram qui va creer les graphiques pour les stats et les sauvegarder
        // dans le  dossier ressources du projet, cette creation se fait dynamiquement (ie a chaque fois qu'on clique sur 
        // un projet)
        Histogram Histo = new Histogram(project,getMaxBranchesFromGroup(),  getMaxCommitFromGroup() );


		projectDateCreationLabel = new JLabel("Projet cr\u00E9e le " + date_created);
		projectDateCreationLabel.setBounds(60, 95, 300, 44);
		projectDateCreationLabel.setFont(sousTitreFont);
		displayProjectsPanel.add(projectDateCreationLabel);
		
        String date_modified = dates_created[0];

		projectDateModifiedLabel = new JLabel("Projet modifi\u00E9 le " + date_modified);
		projectDateModifiedLabel.setBounds(60, 125, 300, 44);
		projectDateModifiedLabel.setFont(sousTitreFont);
		displayProjectsPanel.add(projectDateModifiedLabel);

		
		//  on recupere l'image depuis ressource, on la  redimensionne puis on l'ajoute dans le panel
		try {
			BufferedImage img = ImageIO.read(new File ("src/main/resources/commitTot.png"));
			ImageIcon nombreCommitTotHisto = new ImageIcon(img);
			nombreCommitTotHisto.getImage().flush();
			Image image = nombreCommitTotHisto.getImage(); // transform it 
			Image newimg = image.getScaledInstance(280, 280,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
			ImageIcon nombreCommitTotHisto2 = new ImageIcon(newimg); 
			JLabel nombreCommitTotHistoLabel = new JLabel(nombreCommitTotHisto2);
			nombreCommitTotHistoLabel.setBounds(60, 317, 280, 280);
			displayProjectsPanel.add(nombreCommitTotHistoLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//  on recupere l'image depuis ressource, on la  redimensionne puis on l'ajoute dans le panel
		try {
			BufferedImage img = ImageIO.read(new File ("src/main/resources/brancheTot.png"));
			ImageIcon nombreBrancheTotHisto = new ImageIcon(img);
			nombreBrancheTotHisto.getImage().flush();
			Image image = nombreBrancheTotHisto.getImage(); // transform it 
			Image newimg = image.getScaledInstance(280, 280, java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
			ImageIcon nombreBrancheTotHisto2 = new ImageIcon(newimg); 
			JLabel nombreBrancheTotHistoLabel = new JLabel(nombreBrancheTotHisto2);
			nombreBrancheTotHistoLabel.setBounds(390, 317, 280, 280);
			displayProjectsPanel.add(nombreBrancheTotHistoLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Permet d'archiver/desarchiver le projet
		//If the current user is the creator of the selected project
		if(ImportUserProjects.currentUserIsCreator(project)) {
			//check if the project is already archived
			if(archivedprojects.contains(project)) {
				//The user can unarchive a project
				System.out.println("Already archived");
				JLabel archiveProjectLabel = new JLabel("D\u00E9sarchiver le projet: ");
				archiveProjectLabel.setBounds(780, 20, 250, 44);
				archiveProjectLabel.setFont(sousTitreFont);
				displayProjectsPanel.add(archiveProjectLabel);
				ImageIcon archiveIcon = new ImageIcon(this.getClass().getResource("/archive.png"));
		        unarchiveIconLabel = new JButton(archiveIcon);
		        unarchiveIconLabel.addActionListener(this);
		        unarchiveIconLabel.putClientProperty("ProjectId", project.getId());
		        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		        unarchiveIconLabel.setCursor(cursor);
		        unarchiveIconLabel.setBounds(1000, 30, 24, 24);
		        displayProjectsPanel.add(unarchiveIconLabel);
			}else {
				//The user can archive the project
				JLabel archiveProjectLabel = new JLabel("Archiver le projet: ");
				archiveProjectLabel.setBounds(810, 20, 200, 44);
				archiveProjectLabel.setFont(sousTitreFont);
				displayProjectsPanel.add(archiveProjectLabel);
				ImageIcon archiveIcon = new ImageIcon(this.getClass().getResource("/archive.png"));
		        archiveIconLabel = new JButton(archiveIcon);
		        archiveIconLabel.addActionListener(this);
		        archiveIconLabel.putClientProperty("ProjectId", project.getId());
		        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		        archiveIconLabel.setCursor(cursor);
		        archiveIconLabel.setBounds(1000, 30, 24, 24);
		        displayProjectsPanel.add(archiveIconLabel);
			}
			
		}
		
		//  on recupere l'image depuis ressource, on la  redimensionne puis on l'ajoute dans le panel
		try {
			BufferedImage img2 = ImageIO.read(new File ("src/main/resources/commit.png"));
			ImageIcon nombreCommitHisto = new ImageIcon(img2);
			nombreCommitHisto.getImage().flush();
			Image image2 = nombreCommitHisto.getImage(); // transform it 
			Image newimg2 = image2.getScaledInstance(300, 280,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
			ImageIcon nombreCommitHisto2 = new ImageIcon(newimg2); 
			JLabel nombreCommitHistoLabel = new JLabel(nombreCommitHisto2);
			nombreCommitHistoLabel.setBounds(760, 60, 300, 280);
			displayProjectsPanel.add(nombreCommitHistoLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//  on recupere l'image depuis ressource, on la  redimensionne puis on l'ajoute dans le panel
		try {
			BufferedImage img3 = ImageIO.read(new File ("src/main/resources/mergeRequest.png"));
			ImageIcon mergeHisto = new ImageIcon(img3);
			mergeHisto.getImage().flush();
			Image image3 = mergeHisto.getImage(); // transform it 
			Image newimg3 = image3.getScaledInstance(300, 280,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly  
			ImageIcon mergeHisto2 = new ImageIcon(newimg3); 
			JLabel mergeHistoLabel = new JLabel(mergeHisto2);
			mergeHistoLabel.setBounds(760, 340, 300, 280);
			displayProjectsPanel.add(mergeHistoLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	

		displayProjectsFrame.setVisible(true);
	}
	
	// get the max commit from a group of projects
	/** 
	* Permet de recuperer le nombre max de commit total au seins de projets d'un groupe
	* si on  est pas dans un  groupe,  on va considerer l'ensemble des projets de  l'utilisateur
	*
	* @param  None
	* @return maxCommit 	Max(commitTotal) des projets d'un groupe (ou de tout  les projets si pas de  groupe
	* selectionne)
	*/
	public long getMaxCommitFromGroup() 
	{
	

		long maxCommit = 0;
		 for (Project  p : projects) {
			 if (p.getCommitCount()>maxCommit) {
				 maxCommit = p.getCommitCount();
			 }
		 }
		 return maxCommit;
	}
	
	// get the max branches from a group of projects
	/**
	* Permet de recuperer le nombre max de branches  total au seins de projets d'un groupe
	* si on  est pas dans un  groupe,  on va considerer l'ensemble des projets de  l'utilisateur
	*
	* @param  None
	* @return maxBranches 	Max(branchesTotal) des projets d'un groupe (ou de tout  les projets si pas de  groupe
	* selectionne)
	*/
	public long getMaxBranchesFromGroup() {
			
		long maxBranches = 0;
		for (Project  p : projects) {
			 if (p.getNbr_branches()>maxBranches) {
				 maxBranches = p.getNbr_branches();
			 }
		 }
		 return maxBranches;
	}
	
	/**
	* Permet de creer un projet en inserant manuellement les donnees du projets 
	* membres, nom du projet
	*
	* @param  None
	* @return None
	*/
	public void createProjectsDisplay() {


		members = new ArrayList<String>();

		createProjectsFrame = new JFrame();

		createProjectsFrame.setBackground(Color.WHITE);
		createProjectsFrame.getContentPane().setBackground(Color.WHITE);
		createProjectsFrame.setResizable(true);
		createProjectsFrame.setBounds(0, 0, 1000, 600);
		createProjectsFrame.setLocationRelativeTo(null);
		createProjectsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		createProjectsPanel = new JPanel();
		createProjectsFrame.getContentPane().add(createProjectsPanel);
		createProjectsPanel.setLayout(null);
		createProjectsPanel.setVisible(true);

		c = new Color(218, 218, 218);
		createProjectsPanel.setBackground(c);

		infoLabel = new JLabel("Veuillez saisir les informations relatives au projet \u00E0 cr\u00E9er. ");
		infoLabel.setBounds(createProjectsFrame.getWidth() / 2 - 250, 190, 400, 25);
		createProjectsPanel.add(infoLabel);

		prNameLabel = new JLabel("Nom du projet :");
		prNameLabel.setBounds(createProjectsFrame.getWidth() / 2 - 150, 250, 120, 25);
		createProjectsPanel.add(prNameLabel);

		namePrText = new JTextField(30);
		namePrText.setBounds(prNameLabel.getWidth() + prNameLabel.getX(), 250, 165, 25);
		createProjectsPanel.add(namePrText);

		prMembersLabel = new JLabel("Username du membre \u00E0 ajouter :");
		prMembersLabel.setBounds(createProjectsFrame.getWidth() / 2 - 250, 300, 220, 25);
		createProjectsPanel.add(prMembersLabel);

		membersPrText = new JTextField(30);
		membersPrText.setBounds(prMembersLabel.getWidth() + prMembersLabel.getX(), 300, 165, 25);
		createProjectsPanel.add(membersPrText);

		addMember_button = new JButton("Ajouter le membre");
		addMember_button.setSize(160, 25);
		addMember_button.setLocation(membersPrText.getWidth() + membersPrText.getX() + 30, 300);
		createProjectsPanel.add(addMember_button);
		// addMember_button.setContentAreaFilled(false);
		addMember_button.setBackground(Color.WHITE);
		addMember_button.setFocusable(false);
		addMember_button.addActionListener(this);

		addMemberLabel = new JLabel();
		addMemberLabel.setBounds(createProjectsFrame.getWidth() / 2 - 250, 350, 400, 25);
		createProjectsPanel.add(addMemberLabel);
		addMemberLabel.setForeground(Color.BLACK);
		addMemberLabel.setVisible(false);

		createProject_button = new JButton("Cr\u00E9er le projet");
		createProject_button.setSize(120, 25);
		createProject_button.setLocation((createProjectsFrame.getWidth() - createProject_button.getWidth()) / 2, 380);
		createProjectsPanel.add(createProject_button);
		createProject_button.setBackground(Color.WHITE);

		// createProject_button.setContentAreaFilled(false);
		createProject_button.setFocusable(false);
		createProject_button.addActionListener(this);

		addProjectLabel = new JLabel();
		addProjectLabel.setBounds(createProjectsFrame.getWidth() / 2 - 250, 430, 400, 25);
		createProjectsPanel.add(addProjectLabel);
		addProjectLabel.setForeground(Color.BLACK);
		addProjectLabel.setVisible(false);

		createProjectsFrame.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == connect_button) {
			String userEntered = userText.getText();
			String pwEntered = String.valueOf(pwText.getPassword());
			System.out.println(userEntered + ", " + pwEntered);
			String access_token = Login.Creds(userEntered, pwEntered);
			try {
				user_info = Login.getUserInfo(access_token);
				if (access_token == "erreur") {
					txtConnect.setForeground(Color.RED);
					txtConnect.setText(
							"Mot de passe ou nom erroné.");
					userText.setText("");
					pwText.setText("");
				} else {
					System.out.println(user_info);
					if (rememberMeBtn.isSelected()) {
						CipherToken CT = new CipherToken();
						CT.chiffrer(access_token);
						welcomePopUp(access_token);
					} else {
						welcomePopUp(access_token);
					}

				}

			} catch (ClientProtocolException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NoSuchPaddingException e1) {
				e1.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			} catch (InvalidAlgorithmParameterException e1) {
				e1.printStackTrace();
			} catch (BadPaddingException e1) {
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == mntmAddPrjCSV) {
			// String access_token = Login.Creds(userEntered, pwEntered);
			try {
				importcsvPrj();

			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == mntmAddGrpCSV) {
			// String access_token = Login.Creds(userEntered, pwEntered);
			try {
				importcsvGrp();

			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == mntmAddBoth) {
			// String access_token = Login.Creds(userEntered, pwEntered);
			try {
				importBoth();

			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == mntmAddPrj) {
			createProjectsDisplay();
		}

		if (e.getSource() == mntClose) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}

		if (e.getSource() == mntLogOut) {
			//on se deconnecte de l'application (on devra saisir a nouveau nos id et mdp 
			// en cas de reconnexion
			String path = System.getProperty("user.dir") + "/src/notAToken.txt";
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(path));
				writer.write("");
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}

		if (e.getSource() == crProjects_button) {
			// si on clique  sur l'option "creer un projet"

			createProjectsDisplay();
		}

		if (e.getSource() == addMember_button) {
			//  si on  clique sur le bouton  "ajouter un membre"
			String memberPrEntered = membersPrText.getText();
			members.add(memberPrEntered);
			addMemberLabel.setText(membersPrText.getText() + " a \u00E9t\u00E9 ajout\u00E9(e) avec succ\u00E9s!");
			addMemberLabel.setVisible(true);
			membersPrText.setText("");
		}

		if (e.getSource() == createProject_button) {

			String namePrEntered = namePrText.getText();

			try {
				fr.tse.info6.models.Project tmpPrj;
				try {
					tmpPrj = new Project(CreateProjectsFromCsvFile.createProject(namePrEntered, 0, members));
					allprojects.add(tmpPrj);
					addProjectLabel.setVisible(true);
					addMemberLabel.setVisible(false);
					namePrText.setText("");
					JOptionPane.showMessageDialog(createProjectsFrame, "Votre projet a \u00E9t\u00E9 cr\u00E9e avec succ\u00E9s!");
					createProjectsFrame.setVisible(false);
				} catch (GitLabApiException e1) {
					e1.printStackTrace();
				}
				
			} catch (NullPointerException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(createProjectsFrame, "Il y'a un problème dans un username ou dans un nom de projet");
				createProjectsFrame.setVisible(false);
			}

		}

		if (e.getSource() == sortButton) {
			sortProjects();
		}

		if (e.getSource() == nextButtonPrj) {
			List<fr.tse.info6.models.Project> tmpPrj = new ArrayList<fr.tse.info6.models.Project>();
			try {
				if(selectedGroup != null){
					tmpPrj = CreateProjectsFromCsvFile.createProjects(new File(selectedPath), selectedGroup.getId());
					for(Project tmp : tmpPrj) {
						selectedGroup.addProject(GitLabInstance.GIT_INSTANCE.getProjectApi().getProject(tmp.getId()));
					}
				}
				else {
					tmpPrj = CreateProjectsFromCsvFile.createProjects(new File(selectedPath), -1);
				}
				allprojects.addAll(tmpPrj);
				projects.addAll(tmpPrj);
				DefaultTableModel model = (DefaultTableModel) projectsTable.getModel();
				model.setRowCount(0);
				for (Project project : projects) {
					model.addRow(new Object[] { project.getName(), project.getCreated_at(), project.getLast_modif(),
							project.getNbr_branches(), project.getNbr_collaborators() });
				}
				JOptionPane.showMessageDialog(csvFrame, "Vos projets sont bien cr\u00E9es!");
				csvFrame.setVisible(false);
				// welcomePopUp(Login.Creds(userEntered, pwEntered));

			} catch (GitLabApiException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(csvFrame, "Il y'a un problème dans un username ou dans un nom de projet");
			}

		}
		if (e.getSource() == nextButtonGrp) {

				List<fr.tse.info6.models.Group> tmpGrp = new ArrayList<fr.tse.info6.models.Group>();
				try {
					if(selectedGroup != null)
					{
						tmpGrp = CreateGroupsFromCsvFile.createGroups(new File(selectedPath), selectedGroup.getId());

					}
					else 
					{
						tmpGrp = CreateGroupsFromCsvFile.createGroups(new File(selectedPath), -1);
					}

					allgroups.addAll(tmpGrp);
					groups = ImportUserProjects.transformGroups(allgroups);


					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
					root.removeAllChildren();
					model.reload();
					model.setRoot(null);

					tree = Tree.setModel(tree, user.getName(), groups);
					tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

					// Boucle qui permet d'afficher l'arbre en entier dès le d\u00E9part. (de
					// l'auto-expand)
					for (int i = 0; i < tree.getRowCount(); i++) {
						tree.expandRow(i);
					}

					JOptionPane.showMessageDialog(csvFrame, "Vos groupes sont bien cr\u00E9es!");
					csvFrame.setVisible(false);
					// welcomePopUp(Login.Creds(userEntered, pwEntered));

				} catch (GitLabApiException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(csvFrame, "Il y'a un problème dans un username ou dans un nom de projet");
				}


		}

		if (e.getSource() == nextButtonBoth) {

			List<fr.tse.info6.models.Project> tmpPrj = new ArrayList<fr.tse.info6.models.Project>();

			List<fr.tse.info6.models.Group> tmpGrp = new ArrayList<fr.tse.info6.models.Group>();
			HashMap<List<fr.tse.info6.models.Group>, List<fr.tse.info6.models.Project>> groups_projects = new HashMap<List<fr.tse.info6.models.Group>, List<fr.tse.info6.models.Project>>();
			try {
				if(selectedGroup != null)
					groups_projects = CreateProjectsGroupsFromCsvFile.createProjectsInsideGroups(new File(selectedPath), selectedGroup.getId());
				else
					groups_projects = CreateProjectsGroupsFromCsvFile.createProjectsInsideGroups(new File(selectedPath), -1);

				for (List<fr.tse.info6.models.Group> key : groups_projects.keySet()) {
					tmpGrp.addAll(key);
					tmpPrj.addAll(groups_projects.get(key));
				}

				// On met bien les nouveaux groupes en subgroupes si on a selectionn\u00E9 un groupe dans l'arbre
				for(Group g : tmpGrp) {
					boolean group_exists = false;
					for(int i = 0; i < allgroups.size(); i++){
						if(allgroups.get(i).getId().intValue() == g.getId().intValue()) {
							System.out.println("change group");
							if(selectedGroup != null) {
								g.setParent_id(selectedGroup.getId());
							}
							allgroups.set(i, g);
							group_exists = true;
							break;
						}
					}
					if(!group_exists) {
						if(selectedGroup != null) {
							g.setParent_id(selectedGroup.getId());
						}
						allgroups.add(g);
					}
					
				}
				allprojects.addAll(tmpPrj);
				projects.addAll(tmpPrj);
				groups = ImportUserProjects.transformGroups(allgroups);
				DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				root.removeAllChildren();
				model.reload();
				model.setRoot(null);

				tree = Tree.setModel(tree, user.getName(), groups);
				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				// Boucle qui permet d'afficher l'arbre en entier dès le d\u00E9part. (de
				// l'auto-expand)
				for (int i = 0; i < tree.getRowCount(); i++) {
					tree.expandRow(i);
				}
				//}
				
				JOptionPane.showMessageDialog(csvFrame, "Vos groupes sont bien cr\u00E9es!");
				csvFrame.setVisible(false);
				// welcomePopUp(Login.Creds(userEntered, pwEntered));

			} catch (GitLabApiException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(csvFrame, "Il y'a un problème dans un username ou dans un nom de projet");
			}

		}
		
		if (e.getSource() == archiveIconLabel) {
			int result = JOptionPane.showConfirmDialog(displayProjectsFrame,"Voulez-vous vraiment archiver le projet ?", "Archivage",
		               JOptionPane.YES_NO_OPTION,
		               JOptionPane.QUESTION_MESSAGE);
		    if(result == JOptionPane.YES_OPTION){
				Integer projectToArchive = (Integer) archiveIconLabel.getClientProperty("ProjectId");
				try {
					Archive.archiveProj(access_token, projectToArchive);
				} catch (GitLabApiException e1) {
					e1.printStackTrace();
				}
				
				//remove archived project from projects and allprojects list
				for(Project project : projects) {
					if(project.getId().intValue() == projectToArchive.intValue()) {
						projects.remove(project);
						archivedprojects.add(project);
						break;
					}
				}
				for(Project project : allprojects) {
					if(project.getId().intValue() == projectToArchive.intValue()) {
						allprojects.remove(project);
						break;
					}
				}
				
				//Refresh page
				DefaultTableModel model = (DefaultTableModel) projectsTable.getModel();
				model.setRowCount(0);
				for (Project project : projects) {
					model.addRow(new Object[] { project.getName(), project.getCreated_at(), project.getLast_modif(),
							project.getNbr_branches(), project.getNbr_collaborators() });
				}
				
				displayProjectsFrame.setVisible(false);
				JOptionPane.showMessageDialog(csvFrame, "Ce projet a bien \u00E9t\u00E9 archiv\u00E9");
		    }
		}
		
		if (e.getSource() == unarchiveIconLabel) {
			int result = JOptionPane.showConfirmDialog(displayProjectsFrame,"Voulez-vous vraiment d\u00E9sarchiver le projet ?", "d\u00E9sarchivage",
		               JOptionPane.YES_NO_OPTION,
		               JOptionPane.QUESTION_MESSAGE);
		    if(result == JOptionPane.YES_OPTION){
				Integer projectToUnarchive = (Integer) unarchiveIconLabel.getClientProperty("ProjectId");
				try {
					Archive.unarchiveProj(access_token, projectToUnarchive);
				} catch (GitLabApiException e1) {
					e1.printStackTrace();
				}
				
				for(Project project : archivedprojects) {
					if(project.getId().intValue() == projectToUnarchive.intValue()) {
						archivedprojects.remove(project);
						allprojects.add(project);
						break;
					}
				}
				
				//Refresh page
				DefaultTableModel model = (DefaultTableModel) projectsTable.getModel();
				model.setRowCount(0);
				for (Project project : projects) {
					model.addRow(new Object[] { project.getName(), project.getCreated_at(), project.getLast_modif(),
							project.getNbr_branches(), project.getNbr_collaborators() });
				}
				
				displayProjectsFrame.setVisible(false);
				JOptionPane.showMessageDialog(csvFrame, "Ce projet a bien \u00E9t\u00E9 d\u00E9sarchiv\u00E9");
		    }
		}
		
		if (e.getSource() == showArchivedProjectsBtn) {
			if(showArchivedProjectsBtn.isSelected()) {
				if(selectedGroup == null) {
					archivedprojects = new ArrayList<Project>();
					try {
						archivedprojects = ImportUserProjects.importArchivedProjects();
					} catch (GitLabApiException e1) {
						e1.printStackTrace();
					}
					projects.addAll(archivedprojects);
					allprojects.addAll(archivedprojects);
				}
				else {
					projects.addAll(archivedprojects);
				}
				
			}
			else {
				if(selectedGroup == null) {
					projects.removeAll(archivedprojects);
					allprojects.removeAll(archivedprojects);
				}
				else {
					projects.removeAll(archivedprojects);
				}
			}
			//Refresh page
			DefaultTableModel model = (DefaultTableModel) projectsTable.getModel();
			model.setRowCount(0);
			for (Project project : projects) {
				model.addRow(new Object[] { project.getName(), project.getCreated_at(), project.getLast_modif(),
						project.getNbr_branches(), project.getNbr_collaborators() });
			}
		}
	}
	class SelectionListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent se) {
			JTree tree = (JTree) se.getSource();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			// TreePath path = tree.getSelectionPath();
			if (selectedNode != null) {
				if (selectedNode.getUserObject().toString().equals(user.getName())) {
					// Cas ou on choisit tout les projets
					showArchivedProjectsBtn.setSelected(false);
					selectedGroup = null;
					projects.clear();
					projects.addAll(allprojects);					
					groupMembersPane.setVisible(false);
					centerPane.setBounds(198, 55, 776, 452);
				} else {

					// Cas ou on choisit un groupe en particulier
					projects.clear();
					selectedGroup = (Group) selectedNode.getUserObject();
					projects = selectedGroup.selectAllProjects();
					
					showArchivedProjectsBtn.setSelected(false);
					try {
						archivedprojects = ImportUserProjects.deleteArchived(projects);
					} catch (GitLabApiException e) {
						e.printStackTrace();
					}
					try {
						List<Member> mbs = GitLabInstance.GIT_INSTANCE.getGroupApi().getAllMembers(selectedGroup.getId());
						DefaultTableModel modelgrp = (DefaultTableModel) groupMembersTable.getModel();
						modelgrp.setRowCount(0);
						if(mbs!=null)
							for(Member m : mbs) {


								String access_level = "";
								switch(m.getAccessLevel()){
									case OWNER:
										access_level += "Owner";
										break;
									case ADMIN:
										access_level += "Admin";
										break;
									case MAINTAINER:
										access_level += "Maintainer";
										break;
									case DEVELOPER:
										access_level += "Developer";
										break;
									case REPORTER:
										access_level += "Reporter";
										break;
									case GUEST:
										access_level += "Guest";
										break;
									case NONE:
										access_level += "None";
										break;
									case INVALID:
										access_level += "Invalid";
										break;
									case MINIMAL_ACCESS:
										access_level += "Minimal Access";
										break;
									default:
										break;
								}
								modelgrp.addRow(new Object[] { m.getName(), access_level });
							}
					} catch (GitLabApiException e) {
						e.printStackTrace();
					}


					groupMembersPane.setVisible(true);
					centerPane.setBounds(198, 179, 776, 328);
				}

			}
			sortProjects();
			



		}

	}

	/**
	 * Permet, comme son nom l'indique, de trier tout les projets ou groupes, à l'aide de la classe SortProjBy/SortGrpBy
	 * On redéfinit alors le model du JTable projectTable ou grpTable, en fonction de la liste triée.
	 * @param None
	 * @return None
	 * 
	 */
	public void sortProjects() {
		if (grpOrPrj) {
			if (toggleButton.getText() == "par ordre croissant")
				projects = Project.sortBy(projects, comboBox.getSelectedItem().toString(), true);
			else
				projects = Project.sortBy(projects, comboBox.getSelectedItem().toString(), false);
			DefaultTableModel model = (DefaultTableModel) projectsTable.getModel();
			model.setRowCount(0);
			for (Project project : projects) {
				model.addRow(new Object[] { project.getName(), project.getCreated_at(), project.getLast_modif(),
						project.getNbr_branches(), project.getNbr_collaborators() });
			}
		} else {
			if (toggleButton.getText() == "par ordre croissant")
				groups = Group.sortBy(groups, comboBox.getSelectedItem().toString(), true);
			else
				groups = Group.sortBy(groups, comboBox.getSelectedItem().toString(), false);

			DefaultTableModel modelgrp = (DefaultTableModel) grpTable.getModel();
			modelgrp.setRowCount(0);
			for (Group group : groups) {
				modelgrp.addRow(new Object[] { group.getPath(), group.getName(), group.getCreated_at(),
						group.getNbr_membres(), group.getNbr_projects() });
			}
		}
	}

	public void importcsvPrj() throws UnsupportedLookAndFeelException {
		csvFrame = new JFrame();
		csvFrame.setVisible(true);
		csvFrame.setBounds(100, 100, 500, 400);
		csvFrame.setResizable(false);

		csvPanel = new JPanel();
		csvFrame.add(csvPanel);

		/*
		 * label = new JLabel(
		 * "<html>Veuillez choisir un fichier CSV.<br/>Le fichier doit être dans cette format :<br/>groupname1,username1,username2,username3<br/>groupname2,username4,username5,username6</html>"
		 * ); csvPanel.add(label);
		 */
		label = new JLabel(
				"<html>Veuillez choisir un fichier CSV.<br/>Le fichier doit \u00eatre dans ce format :<br/>username,projectname<br/>username,projectname<br/>username,projectname</html>");
		csvPanel.add(label); /*
								 * label = new JLabel("<html><br/>Pick a file</html>"); csvPanel.add(label);
								 */
		textField = new JTextField(30);
		textField.setEditable(false);
		csvPanel.add(textField);

		JButton button = new JButton("Browse");
		csvPanel.add(button);
		csvPanel2 = new JPanel();
		affichageSelectedFile = new JTextArea(10, 30);
		csvPanel.add(affichageSelectedFile);
		affichageSelectedFile.setEditable(false);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (mode == MODE_OPEN) {
					if (fileChooser.showOpenDialog(csvPanel2) == JFileChooser.APPROVE_OPTION) {
						selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(selectedPath);
						File selectedFile = new File(selectedPath);
						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							System.out.println(input);
							affichageSelectedFile.read(input, "READING FILE :-)");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} else if (mode == MODE_SAVE) {
					if (fileChooser.showSaveDialog(csvPanel2) == JFileChooser.APPROVE_OPTION) {
						selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(selectedPath);
						File selectedFile = new File(selectedPath);

						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							System.out.println(input);
							affichageSelectedFile.read(input, "READING FILE :-)");
							affichageSelectedFile.setEditable(false);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		setMode(MODE_SAVE);
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
		fileChooser.setFileFilter(filter);
		nextButtonPrj = new JButton("Next");
		csvPanel.add(nextButtonPrj, BorderLayout.SOUTH);
		nextButtonPrj.addActionListener(this);
		csvPanel2.add(fileChooser);
		csvFrame.setVisible(true);
		csvPanel.setVisible(true);
		csvFrame.setLocationRelativeTo(null);

	}

	public void importcsvGrp() throws UnsupportedLookAndFeelException {
		csvFrame = new JFrame();
		csvFrame.setVisible(true);
		csvFrame.setBounds(100, 100, 500, 400);
		csvFrame.setResizable(false);

		csvPanel = new JPanel();
		csvFrame.add(csvPanel);

		/*
		 * label = new JLabel(
		 * "<html>Veuillez choisir un fichier CSV.<br/>Le fichier doit être dans cette format :<br/>groupname1,username1,username2,username3<br/>groupname2,username4,username5,username6</html>"
		 * ); csvPanel.add(label);
		 */
		label = new JLabel(
				"<html>Veuillez choisir un fichier CSV.<br/>Le fichier doit \u00eatre dans ce format :<br/>username,groupname<br/>username,groupname<br/>username,groupname</html>");
		csvPanel.add(label); /*
								 * label = new JLabel("<html><br/>Pick a file</html>"); csvPanel.add(label);
								 */
		textField = new JTextField(30);
		textField.setEditable(false);
		csvPanel.add(textField);

		JButton button = new JButton("Browse");
		csvPanel.add(button);
		csvPanel2 = new JPanel();
		affichageSelectedFile = new JTextArea(10, 30);
		csvPanel.add(affichageSelectedFile);
		affichageSelectedFile.setEditable(false);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (mode == MODE_OPEN) {
					if (fileChooser.showOpenDialog(csvPanel2) == JFileChooser.APPROVE_OPTION) {
						selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(selectedPath);
						File selectedFile = new File(selectedPath);
						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							System.out.println(input);
							affichageSelectedFile.read(input, "READING FILE :-)");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} else if (mode == MODE_SAVE) {
					if (fileChooser.showSaveDialog(csvPanel2) == JFileChooser.APPROVE_OPTION) {
						selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(selectedPath);
						File selectedFile = new File(selectedPath);

						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							System.out.println(input);
							affichageSelectedFile.read(input, "READING FILE :-)");
							affichageSelectedFile.setEditable(false);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		setMode(MODE_SAVE);
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
		fileChooser.setFileFilter(filter);
		nextButtonGrp = new JButton("Next");
		csvPanel.add(nextButtonGrp, BorderLayout.SOUTH);
		nextButtonGrp.addActionListener(this);
		csvPanel2.add(fileChooser);
		csvFrame.setVisible(true);
		csvPanel.setVisible(true);
		csvFrame.setLocationRelativeTo(null);

	}

	public void importBoth() throws UnsupportedLookAndFeelException {
		csvFrame = new JFrame();
		csvFrame.setVisible(true);
		csvFrame.setBounds(100, 100, 500, 400);
		csvFrame.setResizable(false);

		csvPanel = new JPanel();
		csvFrame.add(csvPanel);

		/*
		 * label = new JLabel(
		 * "<html>Veuillez choisir un fichier CSV.<br/>Le fichier doit être dans cette format :<br/>groupname1,username1,username2,username3<br/>groupname2,username4,username5,username6</html>"
		 * ); csvPanel.add(label);
		 */
		label = new JLabel(
				"<html>Veuillez choisir un fichier CSV.<br/>Le fichier doit \u00eatre dans ce format :<br/>username,groupname,projectname<br/>username,groupname,projectname<br/>username,groupname</html>");
		csvPanel.add(label); /*
								 * label = new JLabel("<html><br/>Pick a file</html>"); csvPanel.add(label);
								 */
		textField = new JTextField(30);
		textField.setEditable(false);
		csvPanel.add(textField);

		JButton button = new JButton("Browse");
		csvPanel.add(button);
		csvPanel2 = new JPanel();
		affichageSelectedFile = new JTextArea(10, 30);
		csvPanel.add(affichageSelectedFile);
		affichageSelectedFile.setEditable(false);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (mode == MODE_OPEN) {
					if (fileChooser.showOpenDialog(csvPanel2) == JFileChooser.APPROVE_OPTION) {
						selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(selectedPath);
						File selectedFile = new File(selectedPath);
						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							System.out.println(input);
							affichageSelectedFile.read(input, "READING FILE :-)");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} else if (mode == MODE_SAVE) {
					if (fileChooser.showSaveDialog(csvPanel2) == JFileChooser.APPROVE_OPTION) {
						selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
						textField.setText(selectedPath);
						File selectedFile = new File(selectedPath);

						try {
							BufferedReader input = new BufferedReader(
									new InputStreamReader(new FileInputStream(selectedFile)));
							System.out.println(input);
							affichageSelectedFile.read(input, "READING FILE :-)");
							affichageSelectedFile.setEditable(false);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		setMode(MODE_SAVE);
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV FILES", "csv");
		fileChooser.setFileFilter(filter);
		nextButtonBoth = new JButton("Next");
		csvPanel.add(nextButtonBoth, BorderLayout.SOUTH);
		nextButtonBoth.addActionListener(this);
		csvPanel2.add(fileChooser);
		csvFrame.setVisible(true);
		csvPanel.setVisible(true);
		csvFrame.setLocationRelativeTo(null);

	}

	public static void setMode(int mode) {
		Interface.mode = mode;
	}

}
