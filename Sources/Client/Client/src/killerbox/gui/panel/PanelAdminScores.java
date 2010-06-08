package killerbox.gui.panel;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

/**
 * Permet de represente le panel d'administration des scores et utilisateurs.
 * 
 * @author Fabrizio Beretta Piccoli
 * @author Valentin Delaye
 * @version 1.0
 * @see AbstractTablePanel
 */
@SuppressWarnings("serial")
public class PanelAdminScores extends AbstractTableScoresPanel
{
	/**
	 * Message de confirmation de suppression de compte
	 */
	private static final String CONFIRM_DELETE_ACCOUNT = "Etes-vous sur de vouloir supprimer le compte de ";

	/**
	 * Message de confirmation de reinitialisation du score
	 */
	private static final String CONFIRM_MODIFY_SCORE = "Etes-vous sur de vouloir remettre le score a zero de ";

	/**
	 * Message de confirmation de reinitialisation du mot de passe
	 */
	private static final String CONFIRM_MODIFY_PASS = "Etes-vous sur de vouloir reinitialiser le mot de passe de ";

	/**
	 * Titre du panel
	 */
	private JLabel labTitle = new JLabel("Gestion des comptes et scores");

	/**
	 * Bouton retour
	 */
	private JButton btnForward = new JButton("Retour");

	/**
	 * Bouton valider action
	 */
	private JButton btnValidate = new JButton("Valider");

	/**
	 * Indique le numero de la ligne selectionne dans la table
	 */
	private int rowSelected;

	/**
	 * Combo box pour les actions de l'utilisateur
	 */
	private JComboBox comAction = new JComboBox(new Object[] { "Supprimer le compte",
			"Reinitialiser le mot de passe", "Reinitialiser le score" });

	/**
	 * Constructeur
	 * @param window Reference sur la vue
	 */
	public PanelAdminScores(final BaseWindow window)
	{
		super(window);

		this.scoresTable.setAutoCreateRowSorter(true);

		// Selection simple
		this.scoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Config de composants
		this.btnValidate.setEnabled(false);

		// Ajout des composants
		this.add(this.labTitle);
		this.addTable();
		this.add(this.btnForward);
		this.add(this.comAction);
		this.add(this.btnValidate);

		// Ecouter des evenements
		this.btnForward.addActionListener(new ActionListener()
		{
			/**
			 * Quand l'utilisateur clique sur retour
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				window.setPanel(PANEL_SET_ACCOUNT);
			}
		});

		this.btnValidate.addActionListener(new ActionListener()
		{

			/**
			 * Quand l'utilisateur clique sur valide
			 */
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Recuperer les informations de la ligne selectionne
				String user = (String) scoresTable.getModel().getValueAt(rowSelected, 0);

				// Recuperer l'action
				int action = comAction.getSelectedIndex();

				switch (action)
				{
					// Supprimer le compte
					case 0:
					{
						// Ok
						if (JOptionPane.showConfirmDialog(window, CONFIRM_DELETE_ACCOUNT + user
								+ " ?", window.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
						{

							clearJTable();
							// Demande la suppression du compte
							controller.requestDeleteAccount(user);

							// Actualiser les donnes presentes
							controller.requestScore();

							JOptionPane.showMessageDialog(window,
									"L'utilisateur a bien ete supprime", window.getTitle(),
									JOptionPane.INFORMATION_MESSAGE);

						}

						break;

					}

						// Reinitialiser le mot de passe
					case 1:
					{
						// Ok
						if (JOptionPane.showConfirmDialog(window, CONFIRM_MODIFY_PASS + user
								+ " ?", window.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
						{
							clearJTable();
							controller.requestModifyPass(user, "1234");

							// Actualiser les donnes presentes
							controller.requestScore();

							// Afficher la confirmation
							JOptionPane.showMessageDialog(window, "Le score de " + user
									+ " a bien ete reinitialiser a 1234", window.getTitle(),
									JOptionPane.INFORMATION_MESSAGE);

						}

						break;
					}

						// Reinitialiser le score
					case 2:
					{
						// Ok
						if (JOptionPane.showConfirmDialog(window, CONFIRM_MODIFY_SCORE + user
								+ " ?", window.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
						{
							clearJTable();
							controller.requestModifyScore(user, 0);

							// Actualiser les donnes presentes
							controller.requestScore();

							// Afficher la confirmation
							JOptionPane.showMessageDialog(window, "Le score de " + user
									+ " a bien ete remis a 0", window.getTitle(),
									JOptionPane.INFORMATION_MESSAGE);

						}

						break;
					}
				}

			}
		});

		this.scoresTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener()
				{
					/**
					 * Quand l'utilisateur change de ligne
					 */
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						btnValidate.setEnabled(true);
						ListSelectionModel selection = (ListSelectionModel) e.getSource();
						rowSelected = selection.getMinSelectionIndex();

					}
				});

		this.loader.start();

	}

	/**
	 * Permet de retourner le bouton principal. Null s'il n'y a
	 * aucun bouton principal sur le Panel
	 * @return Le bouton principal du panel
	 */
	@Override
	public JButton getDefaultButton()
	{
		return this.btnForward;
	}

	/**
	 * Est appele quand les nouvelles donnees sont arrivees
	 */
	@Override
	public void refreshData()
	{
		super.refreshData();

		scoresTable.setEnabled(true);
	}

	/**
	 * Action a effectuer lorsque l'administrateur demande de modifier des information
	 * du compte. Permet de desactiver temporairement le JTable en attendant les nouvelles
	 * informations
	 */
	private void clearJTable()
	{
		scoresTable.setEnabled(false);
		scoresTable.clearSelection();
		btnValidate.setEnabled(false);
		repaint();
		validate();
	}

}
