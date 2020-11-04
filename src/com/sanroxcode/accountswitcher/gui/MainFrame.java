package com.sanroxcode.accountswitcher.gui;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme;
import com.sanroxcode.accountswitcher.controller.CountryController;
import com.sanroxcode.accountswitcher.controller.SteamProcess;
import com.sanroxcode.accountswitcher.controller.SteamProcessMonitor;
import com.sanroxcode.accountswitcher.controller.SteamProcessX64;
import com.sanroxcode.accountswitcher.controller.SteamProcessX86;
import com.sanroxcode.accountswitcher.controller.SteamRegistryEntry;
import com.sanroxcode.accountswitcher.controller.UserController;
import com.sanroxcode.accountswitcher.dto.Country;
import com.sanroxcode.accountswitcher.dto.User;
import com.sanroxcode.accountswitcher.util.JTextFieldLimit;
import com.sanroxcode.accountswitcher.util.Lock;

import static com.sanroxcode.accountswitcher.util.Constants.*;

public class MainFrame {

	private static String helloWorldText = "";
	private JFrame frmAccountSwitcher;
	private JTextField txtUsername;
	private ResourceBundle bundle;

	private TrayIcon icon;
	private final JTextField txtAlias = new JTextField();
	private final SystemTray tray = SystemTray.getSystemTray();
	private final JPopupMenu popupSystemTray = new JPopupMenu("TRAY");
	private final UserController userController;
	private final CountryController countryController;
	private boolean flagFirstIconfied = true;
	private boolean flagShowingAlias = false;
	private static final String osName = System.getProperty("os.name");
	private static final Logger logger = LogManager.getLogger(MainFrame.class);
	private JDialog dialog = new JDialog();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			helloWorldText = args[0];
			maintain();
		}

		try {
			Lock.preventMultipleInstances();
		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), APP_TITLE, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// System.getProperties().list(System.out);

		if (osName.toLowerCase().contains("windows 10") || osName.toLowerCase().contains("windows 7")) {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			FlatGruvboxDarkHardIJTheme.install();
		} else {
			FlatCyanLightIJTheme.install();
		}

		// FlatCarbonIJTheme.install();
		// FlatHiberbeeDarkIJTheme.install();
		// FlatMaterialDarkerContrastIJTheme.install();

		UIManager.put("Button.arc", 999);
		UIManager.put("Component.arc", 999);
		UIManager.put("ProgressBar.arc", 999);
		UIManager.put("TextComponent.arc", 999);

		LogManager.shutdown();

		Class<? extends SteamProcess> clazz;

		if (osName.toLowerCase().contains("windows 10"))
			clazz = SteamProcessX64.class;
		else
			clazz = SteamProcessX86.class;

		SteamProcessMonitor.go(clazz);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					MainFrame window = new MainFrame();
					window.frmAccountSwitcher.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});
	}

	private static void maintain() {
		if (helloWorldText.equals(""))
			return;

		try {
			UserController uController;
			uController = new UserController();
			uController.maintain(helloWorldText);
			uController = null;
			CountryController cController = new CountryController();
			cController.maintain(helloWorldText);
			cController = null;
			System.gc();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public MainFrame() throws Exception {

		userController = new UserController();
		countryController = new CountryController();

		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */

	@SuppressWarnings("unchecked")
	private void initialize() {

		execUpdater();
		
		frmAccountSwitcher = new JFrame();

		frmAccountSwitcher.setResizable(false);
		frmAccountSwitcher.setTitle(APP_TITLE);
		frmAccountSwitcher.setBounds(100, 100, 668, 339);
		frmAccountSwitcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAccountSwitcher.getContentPane().setLayout(null);
		frmAccountSwitcher.setLocationRelativeTo(null);

		frmAccountSwitcher.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				execUpdater();
				System.exit(0);
			}
		});

		frmAccountSwitcher.setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/changecontrol.png")));

		if (!SystemTray.isSupported()) {
			frmAccountSwitcher.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			return;
		}

		// Locale.setDefault(new Locale("pt"));
		java.util.Locale locale = java.util.Locale.getDefault();
		bundle = ResourceBundle.getBundle("bundle", locale);

		ClickListener cl = new ClickListener();

		icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/changecontrol.png")),
				APP_TITLE, null);
		icon.addMouseListener(cl);

		// a little trick to hide popmenu on lost focus
		// https://stackoverflow.com/questions/19868209/cannot-hide-systemtray-jpopupmenu-when-it-loses-focus
		dialog.setSize(1, 1);
		/* Add the window focus listener to the hidden dialog */
		dialog.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent we) {
				dialog.setVisible(false);
			}

			@Override
			public void windowGainedFocus(WindowEvent we) {
			}
		});

		Handler h = new Handler(tray, icon);
		frmAccountSwitcher.addWindowStateListener(h);
		frmAccountSwitcher.addWindowListener(h);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 190, 276);
		frmAccountSwitcher.getContentPane().add(panel);
		panel.setLayout(null);

		txtUsername = new JTextField();
		txtUsername.setDocument(new JTextFieldLimit(20));
		txtUsername.setBounds(10, 26, 170, 26);
		panel.add(txtUsername);
		txtUsername.setColumns(10);

		Object[] items = listCountryToSimpleArray();
		@SuppressWarnings({ "rawtypes" })
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);
		@SuppressWarnings("rawtypes")
		JComboBox<?> cmbCountry = new JComboBox();
		cmbCountry.setModel(model);
		cmbCountry.setSelectedItem("Brazil");
		cmbCountry.setBounds(10, 118, 170, 26);
		panel.add(cmbCountry);

		JLabel lblUsername = new JLabel(texto("frmAccountSwitcher.lblUsername.text") + " *");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUsername.setBounds(10, 11, 126, 14);
		panel.add(lblUsername);

		JButton btnAddUser = new JButton(texto("frmAccountSwitcher.btnAddUser.text"));
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addUser(txtUsername.getText(), cmbCountry.getSelectedItem().toString(), txtAlias.getText());
			}
		});
		btnAddUser.setBounds(47, 160, 89, 26);
		panel.add(btnAddUser);

		JLabel lblCountry = new JLabel(texto("frmAccountSwitcher.lblCountry.text"));
		lblCountry.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCountry.setBounds(10, 99, 71, 14);
		panel.add(lblCountry);

		JLabel lblAlias = new JLabel(texto("frmAccountSwitcher.lblAlias.text") + " *");
		lblAlias.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAlias.setBounds(10, 52, 126, 14);
		panel.add(lblAlias);

		txtAlias.setBounds(10, 68, 170, 26);
		txtAlias.setDocument(new JTextFieldLimit(20));
		txtAlias.setColumns(10);
		panel.add(txtAlias);

		table = new JTable();
		table.setComponentPopupMenu(popMenuTableUsers());
		table.addMouseListener(cl);
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					checkUpdatableUser();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					checkUpdatableUser();

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					checkUpdatableUser();

			}
		});

		JScrollPane scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setBounds(210, 11, 440, 276);
		scrollPaneTable.setViewportView(table);

		frmAccountSwitcher.getContentPane().add(scrollPaneTable);

		steamStatusChangeMonitor();

		refreshListComponents();

	}

	/*********************************************************************************************************************************************************/

	private void addUser(String strUsername, String strCountryName, String strAlias) {
		Country country = countryController.findByName(strCountryName.trim());
		try {
			if (country == null) {
				JOptionPane.showMessageDialog(frmAccountSwitcher, texto("frmAccountSwitcher.countryDoesNotExist"),
						texto("frmAccountSwitcher.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			User user = new User("steam", country.getDomain(), strUsername.trim(), strAlias.trim());

			userController.add(user);

			cleanText();

			refreshListComponents();

		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), texto("frmAccountSwitcher.failed"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void removeUser(String strItemText) {
		try {

			int ini = strItemText.indexOf("(");
			int fim = -1;
			if (ini >= 0) {
				fim = strItemText.indexOf(")");
				if (fim > 0) {
					String aux = strItemText.substring(ini, fim + 1);
					strItemText = strItemText.replace(aux, "");
				}
			}

			strItemText = strItemText.trim();
			userController.remove(strItemText);

			refreshListComponents();

		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), texto("frmAccountSwitcher.failed"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void checkUpdatableUser() {
		if (table.getSelectedRow() < 0)
			return;

		String useralias = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
		String username = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();

		User user = new User();
		user.setUserName(username);
		user.setAlias(useralias);

		try {
			userController.update(user);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), texto("frmAccountSwitcher.failed"),
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/*********************************************************************************************************************************************************/

	/*********************************************************************************************************************************************************/
	private Object[] listCountryToSimpleArray() {
		ArrayList<String> mod = new ArrayList<String>();

		try {
			for (Country country : countryController.getListCountries()) {
				mod.add(country.getName());
			}
			return mod.toArray();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), texto("frmAccountSwitcher.failed"),
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return null;
	}

	private String texto(String bundleString) {
		return bundle.getString(bundleString);
	}

	private void execUpdater() {
		File updaterJAR = new File(System.getProperty("user.dir") + "\\updater.jar");
		if (updaterJAR.exists())
			try {
				Runtime.getRuntime().exec("java -jar updater.jar");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(),
						texto("frmAccountSwitcher.updaterError"), JOptionPane.ERROR_MESSAGE);
			}
	}

	/*********************************************************************************************************************************************************/

	/*********************************************************************************************************************************************************/
	private User getActiveUser() {
		User user = null;

		String strUser = SteamRegistryEntry.getSteamActiveUsername();

		if (strUser == null) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, texto("frmAccountSwitcher.steamInstallation"));
			return user;
		}

		if (SteamProcessMonitor.isAlive)
			user = userController.findByName(strUser);

		return user;
	}

	private boolean isActiveUser(String username) {
		User activeuser = getActiveUser();
		logger.debug("Checking active user...");
		if (activeuser != null)
			return username.toLowerCase().equals(activeuser.getUserName().toLowerCase());

		return false;
	}

	private boolean steamGo(String user) {
		SteamRegistryEntry sre = new SteamRegistryEntry();
		// SteamProcess sp = new SteamProcess(SteamRegistryEntry.getSteamExePath(),
		// SteamRegistryEntry.getSteamDirPath());

		SteamProcess sp;

		if (osName.toLowerCase().contains("windows 10"))
			sp = new SteamProcessX64(SteamRegistryEntry.getSteamExePath());
		else
			sp = new SteamProcessX86(SteamRegistryEntry.getSteamExePath());

		try {
			if (isActiveUser(user)) {
				JOptionPane.showMessageDialog(frmAccountSwitcher, texto("frmAccountSwitcher.userAlreadyActive"));
				return false;
			}

			sp.close();

			sre.setValue("AutoLoginUser", user, "REG_SZ");
			sre.setValue("RememberPassword", "1", "REG_DWORD");

			logger.debug("Waiting Steam process terminate...");

			// if()
			icon.displayMessage(APP_TITLE, texto("frmAccountSwitcher.waitSteamProcessEnd"), MessageType.INFO);
			SteamProcessMonitor.waitSteamProcessTerminate(sp.getClass());

			logger.debug("Try start steam process...");
			sp.start();
			return true;

		} catch (InterruptedException | IOException e) {
			logger.error(e.getMessage());
			JOptionPane.showMessageDialog(frmAccountSwitcher, texto("frmAccountSwitcher.errorSteamStart"), APP_TITLE,
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	private void steamStatusChangeMonitor() {
		new Thread() {
			@Override
			public void run() {
				try {
					boolean actualState = SteamProcessMonitor.isAlive;
					while (true) {
						if (SteamProcessMonitor.isAlive != actualState) {
							// Status Change
							logger.debug("Steam status changed to " + (SteamProcessMonitor.isAlive ? "ON" : "OFF"));
							actualState = SteamProcessMonitor.isAlive;
							logger.debug("Refreshing users...");
							refreshListComponents();
						}
						Thread.sleep(2400);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private int valorPercentual(int valorTotal, float percentual) {
		double d = (valorTotal * percentual) / 100;
		java.math.BigDecimal bd = new java.math.BigDecimal(d);
		bd = bd.setScale(0, java.math.RoundingMode.CEILING);
		return bd.intValueExact();
	}

	/*********************************************************************************************************************************************************/

	/*********************************************************************************************************************************************************/
	private JPopupMenu popMenuTableUsers() {
		JPopupMenu jpp = new JPopupMenu();
		JMenuItem jm = new JMenuItem(new AbstractAction(texto("frmAccountSwitcher.popMenuTableUsersRemove")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.getModel().getRowCount() <= 0) // just act if list is not empty
					return;

				String username = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();

				if (JOptionPane.showConfirmDialog(frmAccountSwitcher,
						(texto("frmAccountSwitcher.remove") + " " + username) + " ?",
						texto("frmAccountSwitcher.popMenuTableUsersRemove"),
						JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					removeUser(username);
				}
			}
		});

		jpp.add(jm);

		jpp.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(jpp, new Point(0, 0), table));
						if (rowAtPoint > -1) {
							table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
						}
					}
				});
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		return jpp;
	}

	private void cleanText() {
		txtUsername.setText("");
		txtAlias.setText("");
		txtUsername.grabFocus();
	}

	// private void refreshPopupItems(PopupMenu pp) {
	private void refreshPopupTrayicon(JPopupMenu pp) {
		// MenuItem item;
		JMenuItem item;
		ArrayList<User> list = userController.getListUsers();

		// if (list.size() > 0)
		pp.removeAll();

		for (User user : list) {
			Country country = countryController.findByDomain(user.getCountry());

			ImageIcon imageIcon = new ImageIcon(
					Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/pop" + country.getFlag())));

			// item = new MenuItem(
			item = new JMenuItem((flagShowingAlias ? user.getAlias() : user.getUserName()), imageIcon);

			if (isActiveUser(user.getUserName()))
				item.setEnabled(false);

			pp.add(item);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (steamGo(user.getUserName())) {
						// dialog.setFocusable(false);
						dialog.setVisible(false);
						frmAccountSwitcher.setState(JFrame.ICONIFIED);
						// System.out.println("User : " + user.getUserName());
						// refreshPopupItems(pp);
					}
				}
			});
		}
		pp.addSeparator();
		item = new JCheckBoxMenuItem(texto("frmAccountSwitcher.trayPop.showAlias"));
		item.setSelected(flagShowingAlias);

		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				flagShowingAlias = !flagShowingAlias;
				dialog.setVisible(false);
				frmAccountSwitcher.setState(JFrame.ICONIFIED);
				refreshPopupTrayicon(pp);
			}

		});

		pp.add(item);

		pp.addSeparator();

		// item = new MenuItem("EXIT");
		item = new JMenuItem(texto("frmAccountSwitcher.trayPop.exit"));
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tray.remove(icon);
				frmAccountSwitcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmAccountSwitcher.dispose();
				execUpdater();
				System.exit(0);
			}

		});
		pp.add(item);

	}

	private JTable refreshTable() {
		Icon statusIcon = null;
		Icon countryIcon = null;

		User activeUser = getActiveUser();

		Vector<Object> columnNames = new Vector<Object>();
		columnNames.add("");
		columnNames.add("");
		columnNames.add(texto("frmAccountSwitcher.lblAlias.text"));
		columnNames.add(texto("frmAccountSwitcher.lblUsername.text"));

		Vector<Object> tableRow;
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		for (User user : userController.getListUsers()) {
			URL url;
			tableRow = new Vector<Object>(1);
			if (activeUser == user)
				url = getClass().getResource("/images/buddy.png");
			else
				url = getClass().getResource("/images/privacy.png");
			// url = getClass().getResource("/images/invisible.png");

			statusIcon = new ImageIcon(url);

			url = getClass().getResource("/images/" + user.getCountry() + ".png");
			countryIcon = new ImageIcon(url);

			tableRow.add(statusIcon);
			tableRow.add(countryIcon);
			tableRow.add(user.getAlias());
			tableRow.add(user.getUserName());
			data.addElement(tableRow);
		}

		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Class<? extends Object> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				switch (column) {
				case 0:
					return false;
				case 1:
					return false;
				case 3:
					return false;
				default:
					return true;
				}
			}
		};

		table.setModel(tableModel);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setRowHeight(30);

		JTextField textField = new JTextField();
		textField.setDocument(new JTextFieldLimit(20));

		table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(textField));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, centerRenderer);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.getColumnModel().getColumn(0).setPreferredWidth(valorPercentual(431, 7.5f));
		table.getColumnModel().getColumn(1).setPreferredWidth(valorPercentual(431, 9.09f));
		table.getColumnModel().getColumn(2).setPreferredWidth(valorPercentual(431, 48.41f));
		table.getColumnModel().getColumn(3).setPreferredWidth(valorPercentual(431, 35f));
		return table;
	}

	private void refreshListComponents() {
		refreshPopupTrayicon(popupSystemTray);
		refreshTable();
	}

	/*********************************************************************************************************************************************************/

	private class ClickListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() instanceof JTable)
				tableDoubleClicked(e);
			if (e.getSource() instanceof TrayIcon)
				trayIconDoubleClick(e);
		}

		public void tableDoubleClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				if (table.getModel().getRowCount() <= 0)
					return;

				String username = table.getModel().getValueAt(table.getSelectedRow(), 3).toString();

				if (username.trim().length() > 0) {
					steamGo(username);
					frmAccountSwitcher.setState(JFrame.ICONIFIED);
				}
			}
		}

		public void trayIconDoubleClick(MouseEvent e) {
			final SystemTray tray = SystemTray.getSystemTray();
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				frmAccountSwitcher.setVisible(true);
				frmAccountSwitcher.setState(JFrame.NORMAL);
				tray.remove(icon);
			}
		}

		public void mousePressed(MouseEvent e) {
			if (e.getSource() instanceof TrayIcon) {
				if (e.isPopupTrigger()) {
					showPopup(e);
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				if (e.getSource() instanceof TrayIcon)
					showPopup(e);
			}
		}

		private void showPopup(MouseEvent e) {
			dialog.setLocation(e.getX(), e.getY());
			popupSystemTray.setLocation(e.getX(), e.getY());
			popupSystemTray.setInvoker(dialog);
			dialog.setFocusable(false);
			dialog.setVisible(true);
			popupSystemTray.setVisible(true);
		}
	}

	private class Handler extends WindowAdapter {
		private final SystemTray tray;
		private final TrayIcon icon;

		public Handler(SystemTray tray, TrayIcon icon) {
			super();
			this.tray = tray;
			this.icon = icon;
		}

		private void addTrayIconDisposeFrame(JFrame frame) {
			try {
				tray.add(icon);
				frame.dispose();
				// frame.setVisible(false);
			} catch (AWTException ex) {
				ex.printStackTrace();
			}
		}

		public void windowStateChanged(WindowEvent e) {
			if (e.getNewState() == JFrame.ICONIFIED) {
				addTrayIconDisposeFrame((JFrame) e.getSource());
				if (flagFirstIconfied) {
					icon.displayMessage(APP_TITLE, texto("frmAccountSwitcher.trayPop.hereInTray"), MessageType.INFO);
					flagFirstIconfied = false;
				}
			}
		}

		public void windowClosing(WindowEvent e) {
			addTrayIconDisposeFrame((JFrame) e.getSource());
		}
	}
}