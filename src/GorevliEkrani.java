import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import com.classes.Ders;
import com.classes.Ogrenci;
import com.classes.Veritabani;

public class GorevliEkrani {

	Ders ders = new Ders();
	Veritabani veritabani = new Veritabani();
	Ogrenci ogrenci = new Ogrenci();

	private String username;

	public JFrame frame;
	private JComboBox cbFakulte, cbOgrenciBolum, cbDanisman, cbDersBolum;
	private JTextField tfOgrenciNo, tfarananogrenci, tfSifre, tfOgrenciSoyadi, tfOgrenciAdi;
	private JTextField tfDersKodu, tfDersAdi;
	private JLabel lbGorevliAdi;

	DefaultTableModel derslerModel = new DefaultTableModel();
	Object[] dersColumn = { "Ders Kodu", "Ders Adi", "Bolum Adi" };
	Object[] dersRow = new Object[4];
	DefaultTableModel studentModel = new DefaultTableModel();
	Object[] studentColumn = { "Ogrenci Numarasi", "Ogrenci Adi", "Ogrenci Soyadi", "Fakulte", "Bolum", "Danisman",
			"Sifre" };
	Object[] studentRow = new Object[7];
	private JTable tblDersler;
	private JTable tblOgrenciler;
	private JTextField tfArananDers;
	String facultyID;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GorevliEkrani window = new GorevliEkrani();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GorevliEkrani() {
		initialize();
	}

	public GorevliEkrani(String userno) {
		username = userno;
		initialize();
		refreshInfo();
		refreshLesson();
		refreshStudent();
		comboboxYenile();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1096, 614);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBounds(0, 0, 1072, 577);
		frame.getContentPane().add(tabbedPane);

		JPanel pnlBilgiEkrani = new JPanel();
		pnlBilgiEkrani.setBackground(Color.WHITE);
		tabbedPane.addTab("Bilgi Ekrani", null, pnlBilgiEkrani, null);
		pnlBilgiEkrani.setLayout(null);

		lbGorevliAdi = new JLabel("Gorevli Adi Soyadi");
		lbGorevliAdi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbGorevliAdi.setBounds(420, 225, 174, 30);
		pnlBilgiEkrani.add(lbGorevliAdi);

		JLabel lblGorevliNumarasi = new JLabel(username);
		lblGorevliNumarasi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGorevliNumarasi.setBounds(420, 190, 174, 25);
		pnlBilgiEkrani.add(lblGorevliNumarasi);

		JPanel pnlDersEkrani = new JPanel();
		pnlDersEkrani.setBackground(Color.WHITE);
		tabbedPane.addTab("Ders Ekrani", null, pnlDersEkrani, null);
		pnlDersEkrani.setLayout(null);

		tfDersKodu = new JTextField();
		tfDersKodu.setBounds(10, 87, 193, 19);
		pnlDersEkrani.add(tfDersKodu);
		tfDersKodu.setColumns(10);

		tfDersAdi = new JTextField();
		tfDersAdi.setBounds(10, 139, 193, 19);
		pnlDersEkrani.add(tfDersAdi);
		tfDersAdi.setColumns(10);

		cbDersBolum = new JComboBox();
		cbDersBolum.setBounds(10, 33, 193, 21);
		pnlDersEkrani.add(cbDersBolum);

		JLabel lblNewLabel_1 = new JLabel("Bolum");
		lblNewLabel_1.setBounds(10, 10, 193, 13);
		pnlDersEkrani.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Ders Kodu");
		lblNewLabel_1_1.setBounds(10, 64, 193, 13);
		pnlDersEkrani.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Ders Adi");
		lblNewLabel_1_2.setBounds(10, 116, 193, 13);
		pnlDersEkrani.add(lblNewLabel_1_2);

		JLabel lblNewLabel_1_3 = new JLabel("Dersler");
		lblNewLabel_1_3.setBounds(737, 90, 98, 13);
		pnlDersEkrani.add(lblNewLabel_1_3);

		JButton btnDuzenle = new JButton("Duzenle");
		btnDuzenle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblDersler.getSelectedRow() > -1) {
					int duzenlensinmi = JOptionPane.showConfirmDialog(frame, "Ders Duzenlensinmi?");
					if (duzenlensinmi == 0) {
						try {
							Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
									veritabani.password);
							PreparedStatement statement = connection.prepareStatement(
									"SELECT PK_DepartmentID FROM tbl_Department WHERE Department_Name='"
											+ cbDersBolum.getSelectedItem() + "'");
							ResultSet rs = statement.executeQuery();
							while (rs.next()) {
								ders.bolumKodu = rs.getString(1);
							}
							statement = connection.prepareStatement(
									"UPDATE tbl_Lesson SET LessonName='" + tfDersAdi.getText() + "',DepartmentID='"
											+ ders.bolumKodu + "' WHERE PK_LessonID='" + tfDersKodu.getText() + "'");
							int basarili = statement.executeUpdate();
							refreshLesson();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Lutfen Duzeltilecek Dersi Seciniz");
				}
			}
		});
		btnDuzenle.setBounds(10, 203, 85, 21);
		pnlDersEkrani.add(btnDuzenle);

		JButton btnSil = new JButton("Sil");
		btnSil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblDersler.getSelectedRow() > -1) {
					int silinsinmi = JOptionPane.showConfirmDialog(frame, "Ders silinsinmi?");
					if (silinsinmi == 0) {
						try {
							Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
									veritabani.password);
							PreparedStatement statement = connection
									.prepareStatement("Delete tbl_Lesson WHERE PK_LessonID='"
											+ tblDersler.getValueAt(tblDersler.getSelectedRow(), 0) + "'");
							int basarili = statement.executeUpdate();
							refreshLesson();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Lutfen Silinecek Dersi Seciniz");
				}
			}
		});
		btnSil.setBounds(118, 203, 85, 21);
		pnlDersEkrani.add(btnSil);

		JButton btnYeniDers = new JButton("Yeni Ders Kaydi");
		btnYeniDers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tfDersAdi.getText().isEmpty() | tfDersKodu.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Lutfen alanlari doldurunuz");
				} else {
					try {// combobox'daki secilen bolumun kodunu bulduk
						Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
								veritabani.password);
						PreparedStatement statement = connection
								.prepareStatement("SELECT PK_DepartmentID FROM tbl_Department WHERE Department_Name='"
										+ cbDersBolum.getSelectedItem().toString() + "'");
						ResultSet rs = statement.executeQuery();
						while (rs.next()) {
							ders.bolumKodu = rs.getString(1).replace(" ", "");
						}
						statement = connection.prepareStatement("INSERT INTO tbl_Lesson VALUES('" + tfDersKodu.getText()
								+ "','" + tfDersAdi.getText() + "','" + ders.bolumKodu + "')");
						int basarili = statement.executeUpdate();
						JOptionPane.showMessageDialog(frame, "Ders Kaydedildi!");
						refreshLesson();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(frame,
								"Ders kodu onceden kullanilmis veya yanlis veri tipi girisi");
					}
				}
			}
		});
		btnYeniDers.setBounds(32, 253, 154, 21);
		pnlDersEkrani.add(btnYeniDers);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		scrollPane.setBounds(477, 116, 580, 424);
		pnlDersEkrani.add(scrollPane);

		tblDersler = new JTable();
		tblDersler.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cbDersBolum.setSelectedItem(tblDersler.getValueAt(tblDersler.getSelectedRow(), 2));
				tfDersAdi.setText(tblDersler.getValueAt(tblDersler.getSelectedRow(), 1).toString());
				tfDersKodu.setText(tblDersler.getValueAt(tblDersler.getSelectedRow(), 0).toString());
			}
		});
		derslerModel.setColumnIdentifiers(dersColumn);
		tblDersler.setBounds(574, 96, -68, -52);
		scrollPane.setViewportView(tblDersler);

		JLabel lblNewLabel_2 = new JLabel("Aramak istediginiz dersin adi");
		lblNewLabel_2.setBounds(477, 37, 253, 13);
		pnlDersEkrani.add(lblNewLabel_2);

		tfArananDers = new JTextField();
		tfArananDers.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				deleteAllRows(derslerModel);
				tblDersler.setModel(derslerModel);
				try {
					Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
							veritabani.password);
					PreparedStatement statement = connection.prepareStatement(
							"SELECT * FROM tbl_Lesson WHERE LessonName LIKE '" + tfArananDers.getText() + "%'");
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						dersRow[0] = rs.getString("PK_LessonID");
						dersRow[1] = rs.getString("LessonName");
						derslerModel.addRow(dersRow);
						tblDersler.setModel(derslerModel);
					}
					connection.close();
				} catch (SQLException a) {
					a.printStackTrace();
				}
			}
		});
		tfArananDers.setBounds(477, 61, 253, 19);
		pnlDersEkrani.add(tfArananDers);
		tfArananDers.setColumns(10);

		JButton btnDersYenile = new JButton("Yenile");
		btnDersYenile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshLesson();
			}
		});
		btnDersYenile.setBounds(32, 300, 154, 21);
		pnlDersEkrani.add(btnDersYenile);

		JPanel pnlOgrenciEkrani = new JPanel();
		pnlOgrenciEkrani.setBackground(Color.WHITE);
		tabbedPane.addTab("Ogrenci Ekrani", null, pnlOgrenciEkrani, null);
		pnlOgrenciEkrani.setLayout(null);

		tfOgrenciNo = new JTextField();
		tfOgrenciNo.setBounds(10, 33, 177, 19);
		pnlOgrenciEkrani.add(tfOgrenciNo);
		tfOgrenciNo.setColumns(10);

		tfOgrenciAdi = new JTextField();
		tfOgrenciAdi.setColumns(10);
		tfOgrenciAdi.setBounds(10, 85, 177, 19);
		pnlOgrenciEkrani.add(tfOgrenciAdi);

		tfOgrenciSoyadi = new JTextField();
		tfOgrenciSoyadi.setColumns(10);
		tfOgrenciSoyadi.setBounds(10, 137, 177, 19);
		pnlOgrenciEkrani.add(tfOgrenciSoyadi);

		cbFakulte = new JComboBox();
		cbFakulte.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				cbBolumYenile();
			}
		});
		cbFakulte.addInputMethodListener(new InputMethodListener() {
			@Override
			public void caretPositionChanged(InputMethodEvent event) {

			}

			@Override
			public void inputMethodTextChanged(InputMethodEvent event) {
			}
		});
		cbFakulte.setBounds(10, 189, 177, 21);
		pnlOgrenciEkrani.add(cbFakulte);

		cbOgrenciBolum = new JComboBox();
		cbOgrenciBolum.setBounds(10, 243, 177, 21);
		pnlOgrenciEkrani.add(cbOgrenciBolum);

		cbDanisman = new JComboBox();
		cbDanisman.setBounds(10, 297, 177, 21);
		pnlOgrenciEkrani.add(cbDanisman);

		tfSifre = new JTextField();
		tfSifre.setColumns(10);
		tfSifre.setBounds(10, 351, 177, 19);
		pnlOgrenciEkrani.add(tfSifre);

		tfarananogrenci = new JTextField();
		tfarananogrenci.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				deleteAllRows(studentModel);
				tblOgrenciler.setModel(studentModel);
				try {
					Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
							veritabani.password);
					PreparedStatement statement = connection.prepareStatement(
							"SELECT * FROM tbl_Student INNER JOIN tbl_Faculty on tbl_Student.FacultyID=tbl_Faculty.PK_FacultyID INNER JOIN tbl_Department on tbl_Student.DepartmentID=tbl_Department.PK_DepartmentID INNER JOIN tbl_Advisor on tbl_Advisor.AdvisorID=tbl_Student.AdvisorID Where PK_StudentID LIKE '%"
									+ tfarananogrenci.getText() + "%'");
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						studentRow[0] = rs.getInt("PK_StudentID");
						studentRow[1] = rs.getString("Student_Firstname").replace(" ", "");
						studentRow[2] = rs.getString("Student_Lastname").replace(" ", "");
						studentRow[3] = rs.getString("Faculty_Name");
						studentRow[4] = rs.getString("Department_Name");
						studentRow[5] = rs.getString("AdvisorFirstname") + " " + rs.getString("AdvisorLastname");
						studentRow[6] = rs.getString("Password").replace(" ", "");
						studentModel.addRow(studentRow);
						tblOgrenciler.setModel(studentModel);
					}
					connection.close();
				} catch (SQLException b) {
					b.printStackTrace();
				}
			}
		});
		tfarananogrenci.setBounds(477, 68, 222, 19);
		pnlOgrenciEkrani.add(tfarananogrenci);
		tfarananogrenci.setColumns(10);

		JLabel lblNewLabel = new JLabel("Aranacak Ogrencinin Numarasi");
		lblNewLabel.setBounds(477, 42, 222, 13);
		pnlOgrenciEkrani.add(lblNewLabel);

		JButton btnNewButton = new JButton("Ogrenciyi Duzenle");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int duzenlensinmi = JOptionPane.showConfirmDialog(frame, "Ogrenci Duzenlensinmi?");
				if (duzenlensinmi == 0) {
					try {
						Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
								veritabani.password);
						PreparedStatement statement = connection
								.prepareStatement("SELECT PK_FacultyID FROM tbl_Faculty WHERE Faculty_Name='"
										+ cbFakulte.getSelectedItem() + "'");
						ResultSet rs = statement.executeQuery();
						while (rs.next()) {
							ogrenci.fakulteKodu = rs.getString(1);
						}
						statement = connection
								.prepareStatement("SELECT PK_DepartmentID FROM tbl_Department WHERE Department_Name='"
										+ cbOgrenciBolum.getSelectedItem() + "'");
						rs = statement.executeQuery();
						while (rs.next()) {
							ogrenci.BolumKodu = rs.getString(1);
						}
						statement = connection.prepareStatement("UPDATE tbl_Student SET PK_StudentID='"
								+ tfOgrenciNo.getText() + "',FacultyID='" + ogrenci.fakulteKodu + "',DepartmentID='"
								+ ogrenci.BolumKodu + "',AdvisorID='" + cbDanisman.getSelectedItem()
								+ "',Student_Firstname='" + tfOgrenciAdi.getText() + "',Student_Lastname='"
								+ tfOgrenciSoyadi.getText() + "',Password='" + tfSifre.getText()
								+ "' WHERE PK_StudentID='" + tfOgrenciNo.getText() + "'");
						int basarili = statement.executeUpdate();
						refreshStudent();
						connection.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(10, 400, 166, 21);
		pnlOgrenciEkrani.add(btnNewButton);

		JButton btnOgrenciyiSil = new JButton("Ogrenci Kaydini Sil");
		btnOgrenciyiSil.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblOgrenciler.getSelectedRow() > -1) {
					int silinsinmi = JOptionPane.showConfirmDialog(frame, "Ogrencinin kaydi silinsinmi?");
					if (silinsinmi == 0) {
						try {
							Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
									veritabani.password);
							PreparedStatement statement = connection.prepareStatement(
									"Delete From tbl_Lessons_Notes Where StudentID='" + tfOgrenciNo.getText() + "'");
							// once ogrencinin aldigi ders kayitlari silinir
							int basarili = statement.executeUpdate();
							connection = DriverManager.getConnection(veritabani.url, veritabani.user,
									veritabani.password);
							statement = connection.prepareStatement(// ogrenci silinir
									"Delete From tbl_Student Where PK_StudentID='" + tfOgrenciNo.getText() + "'");
							basarili = statement.executeUpdate();
							refreshStudent();
							connection.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Silinecek ogrenciyi seciniz!");
				}
			}
		});
		btnOgrenciyiSil.setBounds(107, 451, 166, 21);
		pnlOgrenciEkrani.add(btnOgrenciyiSil);

		JButton btnOgrenciEkle = new JButton("Ogrenci Ekle");
		btnOgrenciEkle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tfOgrenciAdi.getText().isEmpty() | tfOgrenciNo.getText().isEmpty()
						| tfOgrenciSoyadi.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Lutfen alanlari doldurunuz");
				} else {
					try {// fakultenin kodu bulundu
						Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
								veritabani.password);
						PreparedStatement statement = connection
								.prepareStatement("SELECT PK_FacultyID FROM tbl_Faculty WHERE Faculty_Name='"
										+ cbFakulte.getSelectedItem() + "'");
						ResultSet rs = statement.executeQuery();
						while (rs.next()) {
							ogrenci.fakulteKodu = rs.getString(1);
						}
						statement = connection// bolumun kodu bulundu
								.prepareStatement("SELECT PK_DepartmentID FROM tbl_Department WHERE Department_Name='"
										+ cbOgrenciBolum.getSelectedItem() + "'");
						rs = statement.executeQuery();
						while (rs.next()) {
							ogrenci.BolumKodu = rs.getString(1);
						}
						connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
						statement = connection.prepareStatement(
								"INSERT INTO tbl_Student (PK_StudentID,FacultyID,DepartmentID,AdvisorID,Student_Firstname,Student_Lastname,Password) VALUES ('"
										+ tfOgrenciNo.getText() + "','" + ogrenci.fakulteKodu + "','"
										+ ogrenci.BolumKodu + "','" + cbDanisman.getSelectedItem() + "','"
										+ tfOgrenciAdi.getText() + "','" + tfOgrenciSoyadi.getText() + "','"
										+ tfSifre.getText() + "')");
						int basarili = statement.executeUpdate();
						refreshStudent();
						connection.close();
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(frame, "Ogrenci Numarasi Onceden Kullanilmis!");
					}
				}

			}
		});
		btnOgrenciEkle.setBounds(237, 400, 166, 21);
		pnlOgrenciEkrani.add(btnOgrenciEkle);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		studentModel.setColumnIdentifiers(studentColumn);
		scrollPane_1.setBounds(477, 116, 580, 424);
		pnlOgrenciEkrani.add(scrollPane_1);

		tblOgrenciler = new JTable();
		tblOgrenciler.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cbFakulte.setSelectedItem(tblOgrenciler.getValueAt(tblOgrenciler.getSelectedRow(), 3));
				cbBolumYenile();
				cbOgrenciBolum.setSelectedItem(tblOgrenciler.getValueAt(tblOgrenciler.getSelectedRow(), 4));
				tfOgrenciNo.setText(tblOgrenciler.getValueAt(tblOgrenciler.getSelectedRow(), 0).toString());
				tfOgrenciAdi.setText(tblOgrenciler.getValueAt(tblOgrenciler.getSelectedRow(), 1).toString());
				tfOgrenciSoyadi.setText(tblOgrenciler.getValueAt(tblOgrenciler.getSelectedRow(), 2).toString());
				tfSifre.setText(tblOgrenciler.getValueAt(tblOgrenciler.getSelectedRow(), 6).toString());
			}
		});
		tblOgrenciler.setBounds(901, 39, 1, 1);
		scrollPane_1.setViewportView(tblOgrenciler);

		JLabel lblNewLabel_3 = new JLabel("Ogrenci No");
		lblNewLabel_3.setBounds(10, 10, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3);

		JLabel lblNewLabel_3_1 = new JLabel("Ogrenci Adi");
		lblNewLabel_3_1.setBounds(10, 62, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3_1);

		JLabel lblNewLabel_3_2 = new JLabel("Ogrenci Soyadi");
		lblNewLabel_3_2.setBounds(10, 114, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3_2);

		JLabel lblNewLabel_3_3 = new JLabel("Fakulte");
		lblNewLabel_3_3.setBounds(10, 166, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3_3);

		JLabel lblNewLabel_3_4 = new JLabel("Bolum");
		lblNewLabel_3_4.setBounds(10, 220, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3_4);

		JLabel lblNewLabel_3_5 = new JLabel("Danisman");
		lblNewLabel_3_5.setBounds(10, 274, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3_5);

		JLabel lblNewLabel_3_6 = new JLabel("Sifresi");
		lblNewLabel_3_6.setBounds(10, 328, 177, 13);
		pnlOgrenciEkrani.add(lblNewLabel_3_6);
	}

	public void refreshInfo() {
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM tbl_Authorized WHERE PK_AuthorizedID='" + username + "'");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				lbGorevliAdi.setText(rs.getString("AuthorizedFirstname").replace(" ", "") + " "
						+ rs.getString("AuthorizedLastname").replace(" ", ""));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void refreshLesson() {
		deleteAllRows(derslerModel);
		tblDersler.setModel(derslerModel);
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Lesson INNER JOIN tbl_Department on tbl_Department.PK_DepartmentID=tbl_Lesson.DepartmentID");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				dersRow[0] = rs.getString("PK_LessonID");
				dersRow[1] = rs.getString("LessonName");
				dersRow[2] = rs.getString("Department_Name");
				derslerModel.addRow(dersRow);
				tblDersler.setModel(derslerModel);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void refreshStudent() {
		deleteAllRows(studentModel);
		tblOgrenciler.setModel(studentModel);
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Student INNER JOIN tbl_Faculty on tbl_Student.FacultyID=tbl_Faculty.PK_FacultyID INNER JOIN tbl_Department on tbl_Student.DepartmentID=tbl_Department.PK_DepartmentID INNER JOIN tbl_Advisor on tbl_Advisor.AdvisorID=tbl_Student.AdvisorID");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				studentRow[0] = rs.getInt("PK_StudentID");
				studentRow[1] = rs.getString("Student_Firstname").replace(" ", "");
				studentRow[2] = rs.getString("Student_Lastname").replace(" ", "");
				studentRow[3] = rs.getString("Faculty_Name");
				studentRow[4] = rs.getString("Department_Name");
				studentRow[5] = rs.getString("AdvisorFirstname") + " " + rs.getString("AdvisorLastname");
				studentRow[6] = rs.getString("Password").replace(" ", "");
				studentModel.addRow(studentRow);
				tblOgrenciler.setModel(studentModel);
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void comboboxYenile() {
		try {// Ogrenci bolumunun fakulte combobox'ini yeniler
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_Faculty");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				cbFakulte.addItem(rs.getObject("Faculty_Name"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {// ders bolumunun combobox'ini yeniler
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_Department");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				cbDersBolum.addItem(rs.getObject("Department_Name"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cbBolumYenile();
		try {// danisman combobox'ini yeniler
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM tbl_Advisor");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				cbDanisman.addItem(rs.getObject("AdvisorID"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void cbBolumYenile() {
		try {// ders bolumunun combobox'ini dinamik olarak yeniler
			cbOgrenciBolum.removeAllItems();
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(// fakulte ismine gore veritabanindan fakulte
																		// kodunu bulur
					"SELECT PK_FacultyID FROM tbl_Faculty WHERE Faculty_Name='" + cbFakulte.getSelectedItem() + "'");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				facultyID = rs.getString(1);
			}
			connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			statement = connection.prepareStatement("SELECT * FROM tbl_Department WHERE FacultyID='" + facultyID + "'");
			rs = statement.executeQuery();
			while (rs.next()) {
				cbOgrenciBolum.addItem(rs.getObject("Department_Name"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteAllRows(DefaultTableModel model) {
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}
}
//connection = DriverManager.getConnection(url, user, password);