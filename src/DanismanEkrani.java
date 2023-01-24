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
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;

import com.classes.Ogrenci;
import com.classes.Veritabani;

public class DanismanEkrani {

	Veritabani veritabani = new Veritabani();
	Ogrenci ogrenci = new Ogrenci();

	int basarilimi = 0;
	private String username;

	public JFrame frame;
	private JTextField tfarananogrenci;
	private JLabel lblDanismanAdSoyad;
	private JLabel lbOgrenciNo;
	private JTable tblStudent, tblOnayBekleyen, tblAldigiDersler;
	private DefaultTableModel studentModel = new DefaultTableModel();
	Object[] studentColumn = { "Ogrenci Numarasi", "Fakulte", "Bolum", "Adi", "Soyadi", "Sifresi" };
	Object[] studentRow = new Object[6];
	private DefaultTableModel OnayBekleyenModel = new DefaultTableModel();
	Object[] OnayBekleyenColumn = { "Ders No", "Ders Adi" };
	Object[] OnayBekleyenRow = new Object[2];
	private DefaultTableModel aldigiDerslerModel = new DefaultTableModel();
	Object[] aldigiDerslerColumn = { "Ders No", "Ders Adi", "Basari Durumu" };
	Object[] aldigiDerslerRow = new Object[3];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DanismanEkrani window = new DanismanEkrani();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DanismanEkrani() {
		initialize();
		refreshStudent();
	}

	public DanismanEkrani(String userno) {
		username = userno;
		initialize();
		refreshInfo();
		refreshStudent();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1022, 598);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBounds(0, 0, 1008, 561);
		frame.getContentPane().add(tabbedPane);

		JPanel pnlBilgi = new JPanel();
		pnlBilgi.setBackground(Color.WHITE);
		tabbedPane.addTab("Danisman Bilgi Ekrani", null, pnlBilgi, null);
		pnlBilgi.setLayout(null);

		JLabel lbDanismanNo = new JLabel(username);
		lbDanismanNo.setBounds(457, 273, 120, 13);
		pnlBilgi.add(lbDanismanNo);

		lblDanismanAdSoyad = new JLabel();
		lblDanismanAdSoyad.setBounds(457, 296, 120, 13);
		pnlBilgi.add(lblDanismanAdSoyad);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("Ogrenci Bilgi Ekrani", null, panel_1, null);
		panel_1.setLayout(null);

		tfarananogrenci = new JTextField();
		tfarananogrenci.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {// ogrenci textfielda girilen degere gore dinamik bir sekilde
													// veritabaninda arastirilir
				deleteAllRows(studentModel);
				tblStudent.setModel(studentModel);
				try {
					Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
							veritabani.password);
					PreparedStatement statement = connection.prepareStatement(
							"SELECT * FROM tbl_Student Full OUTER JOIN tbl_Faculty on tbl_Student.FacultyID=tbl_Faculty.PK_FacultyID Full OUTER JOIN tbl_Department on tbl_Student.DepartmentID=tbl_Department.PK_DepartmentID Full OUTER JOIN tbl_Advisor on tbl_Advisor.AdvisorID=tbl_Student.AdvisorID Where PK_StudentID LIKE '%"
									+ tfarananogrenci.getText() + "%'");
					ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						studentRow[0] = rs.getInt("PK_StudentID");
						studentRow[1] = rs.getString("Faculty_Name").replace(" ", "");
						studentRow[2] = rs.getString("Department_Name").replace(" ", "");
						studentRow[3] = rs.getString("Student_Firstname").replace(" ", "");
						studentRow[4] = rs.getString("Student_Lastname").replace(" ", "");
						studentRow[5] = rs.getString("Password").replace(" ", "");
						studentModel.addRow(studentRow);
						tblStudent.setModel(studentModel);
					}
					connection.close();
				} catch (SQLException b) {
					b.printStackTrace();
				}
			}
		});
		tfarananogrenci.setBounds(657, 50, 257, 19);
		panel_1.add(tfarananogrenci);
		tfarananogrenci.setColumns(10);

		JLabel lblNewLabel = new JLabel("Aranacak Ogrencinin Numarasi");
		lblNewLabel.setBounds(657, 27, 257, 13);
		panel_1.add(lblNewLabel);

		JLabel lbOgrenciAdi = new JLabel("");
		lbOgrenciAdi.setBounds(10, 53, 120, 13);
		panel_1.add(lbOgrenciAdi);

		lbOgrenciNo = new JLabel("");
		lbOgrenciNo.setBounds(10, 27, 120, 13);
		panel_1.add(lbOgrenciNo);

		JLabel lblNewLabel_1 = new JLabel("Aldigi Dersler");
		lblNewLabel_1.setBounds(10, 136, 199, 19);
		panel_1.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Onay Bekleyen Dersler");
		lblNewLabel_1_1.setBounds(265, 136, 199, 22);
		panel_1.add(lblNewLabel_1_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(512, 104, 481, 420);
		panel_1.add(scrollPane);

		tblStudent = new JTable();
		tblStudent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ogrenci.BolumKodu = tblStudent.getValueAt(tblStudent.getSelectedRow(), 2).toString();
				lbOgrenciNo.setText(tblStudent.getValueAt(tblStudent.getSelectedRow(), 0).toString());
				lbOgrenciAdi.setText(tblStudent.getValueAt(tblStudent.getSelectedRow(), 3).toString() + " "
						+ tblStudent.getValueAt(tblStudent.getSelectedRow(), 4).toString());
				refreshAldigiDersler();
				refreshOnayBekleyenDersler();
			}
		});
		tblStudent.setBounds(461, 80, -42, -42);
		studentModel.setColumnIdentifiers(studentColumn);
		scrollPane.setViewportView(tblStudent);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(265, 168, 206, 260);
		panel_1.add(scrollPane_1);

		tblOnayBekleyen = new JTable();
		tblOnayBekleyen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point point = e.getPoint();
				int row = table.rowAtPoint(point);
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					dersOnayla();
				}
			}
		});
		tblOnayBekleyen.setBounds(362, 69, 290, -41);
		OnayBekleyenModel.setColumnIdentifiers(OnayBekleyenColumn);
		scrollPane_1.setViewportView(tblOnayBekleyen);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 168, 199, 260);
		aldigiDerslerModel.setColumnIdentifiers(aldigiDerslerColumn);
		panel_1.add(scrollPane_2);

		tblAldigiDersler = new JTable();
		tblAldigiDersler.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point point = e.getPoint();
				int row = table.rowAtPoint(point);
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					dersIptal();
				}
			}
		});
		tblAldigiDersler.setBounds(380, 27, 27, 42);
		scrollPane_2.setViewportView(tblAldigiDersler);

		JButton btnOnayla = new JButton("Onayla");
		btnOnayla.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblOnayBekleyen.getSelectedRow() > -1) {// tabloda secili ders varsa onaylanir yoksa uyari verir
					dersOnayla();
				} else {
					JOptionPane.showMessageDialog(frame, "Lutfen onaylanacak dersi seciniz!");
				}
			}
		});
		btnOnayla.setBounds(325, 438, 91, 21);
		panel_1.add(btnOnayla);

		JButton btnIptal = new JButton("Onay\u0131 Kald\u0131r");
		btnIptal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tblAldigiDersler.getSelectedRow() > -1) {
					dersIptal();
				} else {
					JOptionPane.showMessageDialog(frame, "Lutfen iptal edilecek dersi seciniz!");
				}
			}
		});
		btnIptal.setBounds(68, 438, 91, 21);
		panel_1.add(btnIptal);
	}

	public void refreshInfo() {
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection
					.prepareStatement("SELECT* FROM tbl_Advisor WHERE AdvisorID='" + username + "'");
			java.sql.ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				lblDanismanAdSoyad.setText(rs.getString("AdvisorFirstname").replace(" ", "") + " "
						+ rs.getString("AdvisorLastname").replace(" ", ""));
			}
			connection.close();
		} catch (SQLException a) {
			a.printStackTrace();

		}
	}

	public void refreshStudent() {
		deleteAllRows(studentModel);
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Student Full OUTER JOIN tbl_Faculty on tbl_Student.FacultyID=tbl_Faculty.PK_FacultyID Full OUTER JOIN tbl_Department on tbl_Student.DepartmentID=tbl_Department.PK_DepartmentID WHERE AdvisorID='"
							+ username + "'");
			java.sql.ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				studentColumn[0] = rs.getString("PK_StudentID");
				studentColumn[1] = rs.getString("Faculty_Name");
				studentColumn[2] = rs.getString("Department_Name");
				studentColumn[3] = rs.getString("Student_Firstname").replace(" ", "");
				studentColumn[4] = rs.getString("Student_Lastname").replace(" ", "");
				studentColumn[5] = rs.getString("Password");
				studentModel.addRow(studentColumn);
				tblStudent.setModel(studentModel);
			}
			connection.close();
		} catch (SQLException a) {
			a.printStackTrace();

		}
	}

	public void refreshOnayBekleyenDersler() {
		deleteAllRows(OnayBekleyenModel);
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Lessons_Notes FULL OUTER JOIN tbl_Lesson on tbl_Lessons_Notes.LessonID=tbl_Lesson.PK_LessonID WHERE StudentID='"
							+ lbOgrenciNo.getText() + "' AND Confirmation='False'");
			java.sql.ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				OnayBekleyenRow[0] = rs.getString("PK_LessonID");
				OnayBekleyenRow[1] = rs.getString("LessonName");
				OnayBekleyenModel.addRow(OnayBekleyenRow);
				tblOnayBekleyen.setModel(OnayBekleyenModel);
			}
			connection.close();
		} catch (SQLException a) {
			a.printStackTrace();

		}
	}

	public void refreshAldigiDersler() {
		deleteAllRows(aldigiDerslerModel);
		try {// ogrencinin aldigi dersleri yenileyen kisim
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM tbl_Lessons_Notes FULL OUTER JOIN tbl_Lesson on tbl_Lessons_Notes.LessonID=tbl_Lesson.PK_LessonID WHERE StudentID='"
							+ lbOgrenciNo.getText() + "' AND Confirmation='True'");
			java.sql.ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				aldigiDerslerRow[0] = rs.getString("LessonID");
				aldigiDerslerRow[1] = rs.getString("LessonName");
				basarilimi = rs.getInt("SuccessStatus");
				if (basarilimi > 0) {
					aldigiDerslerRow[2] = "Basarili";
				} else {
					aldigiDerslerRow[2] = "Basarisiz";
				}
				aldigiDerslerModel.addRow(aldigiDerslerRow);
				tblAldigiDersler.setModel(aldigiDerslerModel);
			}
		} catch (SQLException a) {
			a.printStackTrace();
		}
	}

	private void dersOnayla() {
		if (tblOnayBekleyen.getSelectedRow() > -1) {
			try {
				Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
						veritabani.password);
				PreparedStatement statement = connection
						.prepareStatement("UPDATE tbl_Lessons_Notes SET Confirmation='True' WHERE LessonID='"
								+ tblOnayBekleyen.getValueAt(tblOnayBekleyen.getSelectedRow(), 0) + "' AND StudentID='"
								+ lbOgrenciNo.getText() + "'");
				int onaylandi = statement.executeUpdate();
				refreshOnayBekleyenDersler();
				refreshAldigiDersler();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(frame, "Lutfen eklenecek dersi seciniz");
		}

	}

	private void dersIptal() {
		try {
			Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user, veritabani.password);
			PreparedStatement statement = connection
					.prepareStatement("UPDATE tbl_Lessons_Notes SET Confirmation='False' WHERE LessonID='"
							+ tblAldigiDersler.getValueAt(tblAldigiDersler.getSelectedRow(), 0) + "' AND StudentID='"
							+ lbOgrenciNo.getText() + "'");
			int onaylandi = statement.executeUpdate();
			refreshOnayBekleyenDersler();
			refreshAldigiDersler();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static void deleteAllRows(DefaultTableModel model) {
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
	}
}
