package killerbox.gui.panel;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import killerbox.gui.*;
import static killerbox.gui.panel.EnumPanel.*;

@SuppressWarnings("serial")
public class PanelAdminScores extends AbstractTablePanel
{
	/**
	 * Message
	 */
	private static final String CONFIRM_DELETE_ACCOUNT = "Etes-vous sur de vouloir supprimer le compte de ";

	/**
	 * Message
	 */
	private static final String CONFIRM_MODIFY_SCORE = "Etes-vous sur de vouloir remettre le score a zero de ";

	/**
	 * Message
	 */
	private static final String CONFIRM_MODIFY_PASS = "Etes-vous sur de vouloir reinitialiser le mot de passe de ";

	/**
	 * Label de titre
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
	 * @param base Reference sur la vue
	 */
	public PanelAdminScores(final BaseWindow base)
	{
		super(base);
		
		this.tableScore.setAutoCreateRowSorter(true);

		// Selection simple
		this.tableScore.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Config de composants
		this.btnValidate.setEnabled(false);

		// Ajout des composant
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
				base.setPanel(PANEL_SET_ACCOUNT);
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
				String user = (String) tableScore.getModel().getValueAt(rowSelected, 0);
				System.out.println(user);

				// Recuperer l'action
				int action = comAction.getSelectedIndex();

				switch (action)
				{
					// Supprimer le compte
					case 0:
					{
						// Ok
						if (JOptionPane.showConfirmDialog(base, CONFIRM_DELETE_ACCOUNT + user
								+ " ?", base.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
						{
							// Ne plus rien selectionner

							// Demande la suppression du compte
							base.getListener().requestDeleteAccount(user);

							// Actualiser les donnes presentes
							base.getListener().requestScore();

							JOptionPane.showMessageDialog(base,
									"L'utilisateur a bien ete supprime", base.getTitle(),
									JOptionPane.INFORMATION_MESSAGE);

							// Charger ces donnes et mettre a jour le tableau
							loadData(scores.getUsers(), scores.getScores(), scores.getAdmin());
							

						}

						break;

					}

						// Reinitialiser le mot de passe
					case 1:
					{
						// Ok
						if (JOptionPane.showConfirmDialog(base, CONFIRM_MODIFY_PASS + user
								+ " ?", base.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
						{
							base.getListener().requestModifyPass(user, "1234");

							// Actualiser les donnes presentes
							base.getListener().requestScore();

							// Afficher la confirmation
							JOptionPane.showMessageDialog(base, "Le score de " + user
									+ " a bien ete reinitialiser a 1234", base.getTitle(),
									JOptionPane.INFORMATION_MESSAGE);

							// Charger ces donnes et mettre a jour le tableau
							loadData(scores.getUsers(), scores.getScores(), scores.getAdmin());

						}
						
						break;
					}

						// Reinitialiser le score
					case 2:
					{
						// Ok
						if (JOptionPane.showConfirmDialog(base, CONFIRM_MODIFY_SCORE + user
								+ " ?", base.getTitle(), JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION)
						{
							base.getListener().requestModifyScore(user, 0);

							// Actualiser les donnes presentes
							base.getListener().requestScore();

							// Afficher la confirmation
							JOptionPane.showMessageDialog(base, "Le score de " + user
									+ " a bien ete remis a 0", base.getTitle(),
									JOptionPane.INFORMATION_MESSAGE);

							// Charger ces donnes et mettre a jour le tableau
							loadData(scores.getUsers(), scores.getScores(), scores.getAdmin());

						}

						break;
					}
				}

			}
		});

		this.tableScore.getSelectionModel().addListSelectionListener(
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

}
