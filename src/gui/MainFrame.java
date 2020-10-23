package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import controller.CountryController;
import controller.SteamProcess;
import controller.SteamProcessMonitor;
import controller.SteamRegistryEntry;
import controller.UserController;
import dto.Country;
import dto.User;
import util.JTextFieldLimit;
import util.Lock;

import javax.swing.JLabel;
import java.awt.Font;
//import java.awt.MenuItem;
//import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.AWTException;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

import javax.swing.ListSelectionModel;

public class MainFrame {

	private final HashMap<Integer, String> mapFKeys = new HashMap<Integer, String>();
	private JFrame frmAccountSwitcher;
	private JTextField txtUsername;
	private JList<String> list;
	private Map<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>();

	private TrayIcon icon;
	private final JTextField txtAlias = new JTextField();
	private final SystemTray tray = SystemTray.getSystemTray();
	private final JPopupMenu popupSystemTray = new JPopupMenu("TRAY");
	private final UserController userController = new UserController();
	private final CountryController countryController = new CountryController();
	private boolean flagFirstIconfied = true;
	private boolean flagShowingAlias = false;
	private final static String APP_TITLE = "Account Switcher";
	JDialog dialog = new JDialog();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			Lock.preventMultipleInstances();
		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), APP_TITLE, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// FlatCarbonIJTheme.install();
		// FlatGruvboxDarkHardIJTheme.install();
		// FlatHiberbeeDarkIJTheme.install();
		// FlatMaterialDarkerContrastIJTheme.install();

		FlatCyanLightIJTheme.install();

		UIManager.put("Button.arc", 999);
		UIManager.put("Component.arc", 999);
		UIManager.put("ProgressBar.arc", 999);
		UIManager.put("TextComponent.arc", 999);

		SteamProcessMonitor.go();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					MainFrame window = new MainFrame();
					window.frmAccountSwitcher.setVisible(true);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {

		frmAccountSwitcher = new JFrame();

		frmAccountSwitcher.setResizable(false);
		frmAccountSwitcher.setTitle(APP_TITLE);
		frmAccountSwitcher.setBounds(100, 100, 541, 339);
		frmAccountSwitcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAccountSwitcher.getContentPane().setLayout(null);
		frmAccountSwitcher.setLocationRelativeTo(null);

		frmAccountSwitcher.setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/changecontrol.png")));

		if (!SystemTray.isSupported()) {
			frmAccountSwitcher.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			return;
		}

		DoubleClickListener dcl = new DoubleClickListener();

		/*
		 * icon = new
		 * TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(
		 * "/images/changecontrol.png")), APP_TITLE, popupSystemTray);
		 */
		icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/changecontrol.png")),
				APP_TITLE, null);
		icon.addMouseListener(dcl);

		// a little trick to hide popmenu on lost focus
		// https://stackoverflow.com/questions/19868209/cannot-hide-systemtray-jpopupmenu-when-it-loses-focus
		dialog.setSize(10, 10);
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
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);
		JComboBox<?> cmbCountry = new JComboBox();
		cmbCountry.setModel(model);
		cmbCountry.setSelectedItem("Brazil");
		cmbCountry.setBounds(10, 118, 170, 26);
		panel.add(cmbCountry);

		JLabel lblNewLabel = new JLabel("User Name *");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(10, 11, 71, 14);
		panel.add(lblNewLabel);

		JButton btnAddUser = new JButton("Add");
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addUser(txtUsername.getText(), cmbCountry.getSelectedItem().toString(), txtAlias.getText());
			}
		});
		btnAddUser.setBounds(47, 160, 89, 26);
		panel.add(btnAddUser);

		JLabel lblNewLabel_1 = new JLabel("Country");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(10, 99, 71, 14);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Alias *");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(10, 52, 46, 14);
		panel.add(lblNewLabel_2);

		txtAlias.setBounds(10, 68, 170, 26);
		txtAlias.setDocument(new JTextFieldLimit(20));
		txtAlias.setColumns(10);
		panel.add(txtAlias);

		JScrollPane scrollPane;
		scrollPane = new JScrollPane();
		scrollPane.setBounds(210, 11, 269, 276);
		frmAccountSwitcher.getContentPane().add(scrollPane);

		list = new JList<String>();
		list.setVisibleRowCount(12);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new CompoundBorder());
		list.setFont(new Font("Tahoma", Font.BOLD, 12));

		imageMap = createImageMap();
		popMapFKeys();

		scrollPane.setViewportView(list);
		list.setComponentPopupMenu(popMenuListUsers());
		list.setCellRenderer(new CountryListRenderer());

		JToggleButton toggleAlias = new JToggleButton("");
		toggleAlias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateAndRepaintListUser((JToggleButton) arg0.getSource());
			}
		});
		URL url = getClass().getResource("/images/buddy.png");
		toggleAlias.setIcon(new ImageIcon(url));
		toggleAlias.setToolTipText("Showing User name");
		toggleAlias.setBounds(482, 11, 36, 33);
		frmAccountSwitcher.getContentPane().add(toggleAlias);

		list.addMouseListener(dcl);
		list.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				listKeyRelease(arg0.getKeyCode());
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		steamStatusChangeMonitor();

		refreshList();
		refreshPopupItems(popupSystemTray);
	}

	/*********************************************************************************************************************************************************/

	private void addUser(String strUsername, String strCountryName, String strAlias) {
		Country country = countryController.findByName(strCountryName.trim());
		try {
			if (country == null) {
				JOptionPane.showMessageDialog(frmAccountSwitcher, "Country does not exists...", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			User user = new User("steam", country.getDomain(), strUsername.trim(), null, strAlias.trim());

			userController.add(user);

			cleanText();

			refreshList();
			refreshPopupItems(popupSystemTray);
		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
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

			refreshList();
			refreshPopupItems(popupSystemTray);

		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void removeUser(int index) {
		try {

			User user = userController.getListUsers().get(index);
			removeUser(user.getUserName());
		} catch (Exception | Error e) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, e.getMessage(), "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String findUserByKey(String vKey) {

		for (User user : userController.getListUsers()) {
			if (user.getShortcutKey().equals(vKey))
				return user.getUserName();
		}
		return "";
	}

	private String findUserByIndex(int index) {
		User user = userController.getListUsers().get(index);
		return user.getUserName();
	}

	/*********************************************************************************************************************************************************/

	/*********************************************************************************************************************************************************/
	private DefaultListModel<String> listUserToListModel() {
		DefaultListModel<String> mod = new DefaultListModel<String>();

		for (User user : userController.getListUsers()) {
			mod.addElement(user.toString(flagShowingAlias));
		}

		return mod;
	}

	private Object[] listCountryToSimpleArray() {
		ArrayList<String> mod = new ArrayList<String>();

		for (Country country : countryController.getListCountries()) {
			mod.add(country.getName());
		}

		return mod.toArray();
	}

	private void popMapFKeys() {
		mapFKeys.put(KeyEvent.VK_F1, "F1");
		mapFKeys.put(KeyEvent.VK_F2, "F2");
		mapFKeys.put(KeyEvent.VK_F3, "F3");
		mapFKeys.put(KeyEvent.VK_F4, "F4");
		mapFKeys.put(KeyEvent.VK_F5, "F5");
		mapFKeys.put(KeyEvent.VK_F6, "F6");
		mapFKeys.put(KeyEvent.VK_F7, "F7");
		mapFKeys.put(KeyEvent.VK_F8, "F8");
		mapFKeys.put(KeyEvent.VK_F9, "F9");
		mapFKeys.put(KeyEvent.VK_F10, "F10");
		mapFKeys.put(KeyEvent.VK_F11, "F11");
		mapFKeys.put(KeyEvent.VK_F12, "F12");
	}

	/*********************************************************************************************************************************************************/

	/*********************************************************************************************************************************************************/
	private User getActiveUser() {
		User user = null;

		String strUser = SteamRegistryEntry.getSteamActiveUsername();

		if (strUser == null) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, "Please Check steam installation.");
			return user;
		}

		if (SteamProcessMonitor.isAlive)
			user = userController.findByName(strUser);

		return user;
	}

	private boolean isActiveUser(String username) {
		User activeuser = getActiveUser();

		if (activeuser != null)
			return username.toLowerCase().equals(activeuser.getUserName().toLowerCase());

		return false;
	}

	private boolean steamGo(String user) {
		SteamRegistryEntry sre = new SteamRegistryEntry();
		SteamProcess sp = new SteamProcess(SteamRegistryEntry.getSteamExePath());

		if (isActiveUser(user)) {
			JOptionPane.showMessageDialog(frmAccountSwitcher, "User already active.");
			return false;
		}

		sre.setValue("AutoLoginUser", user, "REG_SZ");
		sre.setValue("RememberPassword", "1", "REG_DWORD");
		sp.start();
		return true;
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
							System.out.println("Steam status changed to " + (isAlive() ? "ON" : "OFF"));
							actualState = SteamProcessMonitor.isAlive;
							refreshList();
							refreshPopupItems(popupSystemTray);
						}
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	/*********************************************************************************************************************************************************/

	/*********************************************************************************************************************************************************/
	private JPopupMenu popMenuListUsers() {
		JPopupMenu jpp = new JPopupMenu();
		JMenuItem jm = new JMenuItem(new AbstractAction("Remove") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (list.getModel().getSize() <= 0) // just act if list is not empty
					return;

				if (JOptionPane.showConfirmDialog(frmAccountSwitcher, ("Remove " + list.getSelectedValue()) + " ?",
						"Delete User", JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					// removeUser(list.getSelectedValue());
					removeUser(list.getSelectedIndex());
				}
			}
		});

		jpp.add(jm);
		return jpp;
	}

	private void cleanText() {
		txtUsername.setText("");
		txtAlias.setText("");
		txtUsername.grabFocus();
	}

	private void refreshList() {
		imageMap = createImageMap();
		list.setModel(listUserToListModel());
	}

	// private void refreshPopupItems(PopupMenu pp) {
	private void refreshPopupItems(JPopupMenu pp) {
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
						frmAccountSwitcher.setState(JFrame.ICONIFIED);
						// System.out.println("User : " + user.getUserName());
						refreshPopupItems(pp);
					}
				}
			});
		}

		pp.addSeparator();

		// item = new MenuItem("EXIT");
		item = new JMenuItem("EXIT");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tray.remove(icon);
				frmAccountSwitcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmAccountSwitcher.dispose();
				System.exit(0);
			}

		});
		pp.add(item);

	}

	private void listKeyRelease(int keycode) {
		String user = "";
		String value = mapFKeys.get(keycode);
		if (value != null) {
			user = findUserByKey(value);
			System.out.println(value + " " + user);
			if (!user.equals("")) {
				steamGo(user);
				frmAccountSwitcher.setState(JFrame.ICONIFIED);
			}
		}
	}

	private Map<String, ImageIcon> createImageMap() {
		Map<String, ImageIcon> map = new HashMap<>();
		Map<String, ImageIcon> mapCountry = new HashMap<>();
		ArrayList<User> listUsers = userController.getListUsers();
		ArrayList<Country> listCountries = countryController.getListCountries();

		try {

			for (Country country : listCountries) {
				URL url = getClass().getResource("/images/" + country.getFlag());
				mapCountry.put(country.getDomain(), new ImageIcon(url));
			}

			for (User user : listUsers) {
				map.put(user.toString(flagShowingAlias), mapCountry.get(user.getCountry()));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	private void updateAndRepaintListUser(JToggleButton toggle) {
		if (!toggle.isSelected()) {
			// Listing username
			URL url = getClass().getResource("/images/buddy.png");
			toggle.setIcon(new ImageIcon(url));
			toggle.setToolTipText("Showing User name");
			flagShowingAlias = false;
		} else {
			URL url = getClass().getResource("/images/invisible.png");
			toggle.setIcon(new ImageIcon(url));
			toggle.setToolTipText("Showing Alias");
			flagShowingAlias = true;
		}

		refreshList();
		refreshPopupItems(popupSystemTray);
	}

	/*********************************************************************************************************************************************************/

	private class DoubleClickListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() instanceof JList)
				listDoubleClick(e);
			if (e.getSource() instanceof TrayIcon)
				trayIconDoubleClick(e);
			if ((e.getSource() instanceof JList) && (e.getButton() == MouseEvent.BUTTON3)) {
				System.out.println("...");
				jlistPopMenu(e);
			}

		}

		public void listDoubleClick(MouseEvent e) {
			JList<?> jlist = (JList<?>) e.getSource();
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				int index = list.locationToIndex(e.getPoint());
				if (index >= 0) {
					Object o = jlist.getModel().getElementAt(index);
					// String[] splitted = o.toString().trim().split(" ");
					String user = findUserByIndex(index);
					steamGo(user);
					frmAccountSwitcher.setState(JFrame.ICONIFIED);					
					// JOptionPane.showMessageDialog(theList, "Double-clicked on: " + o.toString());
					System.out.println("Double-clicked on: " + o.toString());
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
			if (e.getButton() == MouseEvent.BUTTON3 && e.getSource() instanceof JList)
				jlistPopMenu(e);
			if (e.getSource() instanceof TrayIcon) {
				if (e.isPopupTrigger()) {
					System.out.println("TrayIcon popup mousePressed");
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				if (e.getSource() instanceof JList)
					jlistPopMenu(e);
				if (e.getSource() instanceof TrayIcon)
					showPopup(e);
				// System.out.println("TrayIcon popup mouseReleased");
			}
		}

		public void jlistPopMenu(MouseEvent e) {

			// if (e.isPopupTrigger()) { // if the event shows the menu
			JList<?> jlist = (JList<?>) e.getSource();
			jlist.setSelectedIndex(jlist.locationToIndex(e.getPoint())); // select the item
			JPopupMenu jPopupMenu = jlist.getComponentPopupMenu();
			jPopupMenu.show(jlist, e.getX(), e.getY()); // and show the menu
		}

		private void showPopup(MouseEvent e) {
			dialog.setLocation(e.getX(), e.getY());
			popupSystemTray.setLocation(e.getX(), e.getY());
			popupSystemTray.setInvoker(dialog);
			dialog.setVisible(true);
			popupSystemTray.setVisible(true);
		}
	}

	public class CountryListRenderer extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> l, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(l, value, index, isSelected, cellHasFocus);
			label.setIcon(imageMap.get((String) value));
			label.setHorizontalTextPosition(JLabel.RIGHT);
			return label;
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
					icon.displayMessage(APP_TITLE, "We are here in system tray...", MessageType.INFO);
					flagFirstIconfied = false;
				}
			}
		}

		public void windowClosing(WindowEvent e) {
			System.out.println("windowClosing");
			addTrayIconDisposeFrame((JFrame) e.getSource());
		}
	}
}
