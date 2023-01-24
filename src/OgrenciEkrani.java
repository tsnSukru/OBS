import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.classes.Ogrenci;
import com.classes.Veritabani;

public class OgrenciEkrani {

	Veritabani veritabani = new Veritabani();
	Ogrenci ogrenci = new Ogrenci();
	int basarilimi = 0;

	public JFrame frame;
	JLabel lbOgrenciNo;
	JLabel lbOgrenciAdi;
	JLabel lbFakulte;
	JLabel lbBolum;

	private JScrollPane scrollPane, scrollPane_1;
	private JTable table, tblBolumDersleri;

	DefaultTableModel notlarModel = new DefaultTableModel();
	Object[] notlarKolon = { "Ders Adi", "Vize", "Final", "Basari Durumu" };
	Object[] notlarSatir = new Object[4];
	String dersAdi, dersKodu, vizeNotu, finalNotu;
	DefaultTableModel BolumDersleriModel = new DefaultTableModel();
	Object[] BolumDersleriKolon = { "Ders Kodu", "Ders Adi" };
	Object[] BolumDersleriSatir = new Object[2];
	DefaultTableModel derslerModel = new DefaultTableModel();
	Object[] dersKolon = { "Ders Kodu", "Ders Adi" };
	Object[] dersSatir = new Object[2];
	DefaultTableModel alinanDerslerModel = new DefaultTableModel();
	Object[] alinanDersKolon = { "Ders Kodu", "Ders Adi" };
	Object[] alinanDersSatir = new Object[2];
	private JScrollPane scrollPane_2;
	private JTable tblAlinanDersler;
	private JTable tblOnayBekleyen;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					OgrenciEkrani window = new OgrenciEkrani();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OgrenciEkrani() {
		initialize();
	}

	public OgrenciEkrani(String data) {
		ogrenci.numara = data;
		initialize();
		refresh();
		refreshNotes();
		refreshLessons();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 900, 545);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 886, 508);

		frame.getContentPane().add(tabbedPane);
		JPanel pnlBilgi = new JPanel();
		pnlBilgi.setBackground(Color.WHITE);
		tabbedPane.addTab("Bilgi Ekrani", null, pnlBilgi, null);
		pnlBilgi.setLayout(null);

		lbOgrenciNo = new JLabel("ogrenci no");
		lbOgrenciNo.setBounds(328, 232, 221, 19);
		pnlBilgi.add(lbOgrenciNo);

		lbOgrenciAdi = new JLabel("ogrenci adi soyadi");
		lbOgrenciAdi.setBounds(328, 261, 221, 16);
		pnlBilgi.add(lbOgrenciAdi);

		lbFakulte = new JLabel("fakulte");
		lbFakulte.setBounds(328, 287, 221, 16);
		pnlBilgi.add(lbFakulte);

		lbBolum = new JLabel("bolum");
		lbBolum.setBounds(328, 313, 221, 16);
		pnlBilgi.add(lbBolum);

		JPanel pnlNotlar = new JPanel();
		pnlNotlar.setBackground(Color.WHITE);
		tabbedPane.addTab("Notlar", null, pnlNotlar, null);
		pnlNotlar.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 861, 461);
		pnlNotlar.add(scrollPane);

		table = new JTable();
		notlarModel.setColumnIdentifiers(notlarKolon);
		table.setBounds(498, 10, 225, 175);
		scrollPane.setViewportView(table);

		JPanel pnlDersSecim = new JPanel();
		pnlDersSecim.setBackground(Color.WHITE);
		tabbedPane.addTab("Ders Secimi", null, pnlDersSecim, null);
		pnlDersSecim.setLayout(null);

		JButton btnEkle = new JButton("Ekle");
		btnEkle.setBackground(Color.LIGHT_GRAY);
		btnEkle.setBounds(741, 402, 85, 21);
		btnEkle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblBolumDersleri.getSelectedRow() > -1) {
					dersEkle();
				} else {
					JOptionPane.showMessageDialog(frame, "Lutfen eklenecek dersi seciniz!");
				}
			}
		});
		pnlDersSecim.add(btnEkle);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(660, 72, 211, 320);
		pnlDersSecim.add(scrollPane_1);

		tblBolumDersleri = new JTable();
		tblBolumDersleri.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point point = e.getPoint();
				int row = table.rowAtPoint(point);
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					dersEkle();
				}
			}
		});
		derslerModel.setColumnIdentifiers(dersKolon);
		scrollPane_1.setViewportView(tblBolumDersleri);

		scrollPane_2 = new JScrollPane();
		alinanDerslerModel.setColumnIdentifiers(alinanDersKolon);
		scrollPane_2.setBounds(10, 72, 211, 320);
		pnlDersSecim.add(scrollPane_2);

		tblAlinanDersler = new JTable();
		tblAlinanDersler.setBounds(128, 426, -46, -38);
		scrollPane_2.setViewportView(tblAlinanDersler);

		JScrollPane scrollPane_1_1 = new JScrollPane();
		BolumDersleriModel.setColumnIdentifiers(BolumDersleriKolon);
		scrollPane_1_1.setBounds(331, 72, 211, 320);
		pnlDersSecim.add(scrollPane_1_1);

		tblOnayBekleyen = new JTable();
		tblOnayBekleyen.setBounds(85, 381, 175, 32);
		scrollPane_1_1.setViewportView(tblOnayBekleyen);

		JLabel lblNewLabel = new JLabel("Onaylanan Dersler");
		lblNewLabel.setBounds(10, 41, 177, 21);
		pnlDersSecim.add(lblNewLabel);

		JLabel lblOnayBekleyenDersler = new JLabel("Onay Bekleyen Dersler");
		lblOnayBekleyenDersler.setBounds(331, 41, 177, 21);
		pnlDersSecim.add(lblOnayBekleyenDersler);

		JLabel lblDersler = new JLabel("Dersler");
		lblDersler.setBounds(660, 45, 177, 21);
		pnlDersSecim.add(lblDersler);
	}

	public void refresh() {
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM tbl_Student WHERE PK_StudentID='" + ogrenci.numara + "'");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				ogrenci.Adi = rs.getString("Student_Firstname").replace(" ", "");
				ogrenci.Soyadi = rs.getString("Student_Lastname").replace(" ", "");
				ogrenci.fakulteKodu = rs.getString("FacultyID");
				ogrenci.BolumKodu = rs.getString("DepartmentID");
			}
			lbOgrenciNo.setText(ogrenci.numara);
			lbOgrenciAdi.setText(ogrenci.Adi + " " + ogrenci.Soyadi);
			statement = connection
					.prepareStatement("SELECT * FROM tbl_Faculty WHERE PK_FacultyID='" + ogrenci.fakulteKodu + "'");
			rs = statement.executeQuery();
			while (rs.next()) {
				ogrenci.Fakulte = rs.getString("Faculty_Name");
			}
			lbFakulte.setText(ogrenci.Fakulte);
			statement = connection
					.prepareStatement("SELECT * FROM tbl_Department WHERE PK_DepartmentID='" + ogrenci.BolumKodu + "'");
			rs = statement.executeQuery();
			while (rs.next()) {
				ogrenci.Bolum = rs.getString("Department_Name");
			}
			lbBolum.setText(ogrenci.Bolum);
			connection.close();
		} catch (SQLException e) {
			System.out.println("Basarisiz");
		}

	}

	public void refreshNotes() {
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Lessons_Notes FULL OUTER JOIN tbl_Lesson on tbl_Lessons_Notes.LessonID=tbl_Lesson.PK_LessonID WHERE StudentID='"
							+ ogrenci.numara + "'");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				dersAdi = rs.getString("LessonName");
				vizeNotu = String.valueOf(rs.getInt("Midterm"));
				finalNotu = String.valueOf(rs.getInt("Final"));
				basarilimi = rs.getInt("SuccessStatus");
				notlarSatir[0] = dersAdi;
				notlarSatir[1] = vizeNotu;
				notlarSatir[2] = finalNotu;
				if (basarilimi > 0) {
					notlarSatir[3] = "Basarili";
				} else {
					notlarSatir[3] = "Basarisiz";
				}
				notlarModel.addRow(notlarSatir);
				table.setModel(notlarModel);
			}
		} catch (SQLException e) {
			System.out.println(e);
			System.out.println("Basarisiz2");
		}
	}

	public void refreshLessons() {
		try {// Bolum Dersleri
			deleteAllRows(BolumDersleriModel);
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM  tbl_Lesson  WHERE DepartmentID='" + ogrenci.BolumKodu + "'");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				BolumDersleriSatir[0] = rs.getString("PK_LessonID");
				BolumDersleriSatir[1] = rs.getString("LessonName");
				BolumDersleriModel.addRow(BolumDersleriSatir);
				tblBolumDersleri.setModel(BolumDersleriModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {// onay bekleyen
			deleteAllRows(derslerModel);
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Lessons_Notes INNER JOIN tbl_Lesson on tbl_Lessons_Notes.LessonID=tbl_Lesson.PK_LessonID WHERE DepartmentID='"
							+ ogrenci.BolumKodu + "'AND Confirmation='False' AND StudentID='" + lbOgrenciNo.getText()
							+ "'");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				dersSatir[0] = rs.getString("PK_LessonID");
				dersSatir[1] = rs.getString("LessonName");
				derslerModel.addRow(dersSatir);
				tblOnayBekleyen.setModel(derslerModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {// onaylanan
			deleteAllRows(alinanDerslerModel);
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Lessons_Notes INNER JOIN tbl_Lesson on tbl_Lessons_Notes.LessonID=tbl_Lesson.PK_LessonID WHERE StudentID='"
							+ lbOgrenciNo.getText() + "' AND Confirmation='True'");
			java.sql.ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				alinanDersSatir[0] = rs.getString("LessonID");
				alinanDersSatir[1] = rs.getString("LessonName");
				alinanDerslerModel.addRow(alinanDersSatir);
				tblAlinanDersler.setModel(alinanDerslerModel);
			}
		} catch (SQLException a) {
			a.printStackTrace();
		}
	}

	public void dersEkle() {
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT count(StudentID) as varmi FROM tbl_Lessons_Notes WHERE StudentID='"
							+ ogrenci.numara + "'" + "AND LessonID='"
							+ tblBolumDersleri.getValueAt(tblBolumDersleri.getSelectedRow(), 0) + "'");
			java.sql.ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				if (rs.getInt("varmi") == 1) {

					JOptionPane.showMessageDialog(frame, "Ders Zaten Se�ili");
				} else {
					PreparedStatement statement1 = connection.prepareStatement(
							"INSERT INTO tbl_Lessons_Notes (LessonID,StudentID,SuccessStatus,Confirmation) VALUES('"
									+ tblBolumDersleri.getValueAt(tblBolumDersleri.getSelectedRow(), 0) + "','"
									+ ogrenci.numara + "','0','0')");
					int kaydedildi = statement1.executeUpdate();
					refreshLessons();
				}
			}
			connection.close();
		} catch (SQLException a) {
			a.printStackTrace();
		}
	}

	public static void deleteAllRows(DefaultTableModel model) {
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}
}