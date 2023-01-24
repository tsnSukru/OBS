import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.classes.Veritabani;

public class OgrenciGirisEkrani {

	Veritabani veritabani = new Veritabani();

	public JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					OgrenciGirisEkrani window = new OgrenciGirisEkrani();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public OgrenciGirisEkrani() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 692, 461);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textField = new JTextField("204410001");
		textField.setBounds(249, 145, 155, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField("11111");
		textField_1.setColumns(10);
		textField_1.setBounds(249, 200, 155, 19);
		frame.getContentPane().add(textField_1);

		JButton btnNewButton = new JButton("Giris");
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
							veritabani.password);
					PreparedStatement statement = connection.prepareStatement(
							"SELECT count(PK_StudentID) as varmi FROM tbl_Student WHERE PK_StudentID='"
									+ textField.getText() + "'" + "AND Password='" + textField_1.getText() + "'");
					java.sql.ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						if (rs.getInt("varmi") == 1) {
							JOptionPane.showMessageDialog(frame, "Giris Basarili! Hosgeldiniz");
							OgrenciEkrani frm1 = new OgrenciEkrani(textField.getText());
							frm1.frame.setVisible(true);
							frame.dispose();
						} else {
							JOptionPane.showMessageDialog(frame, "Yanlis Kullanici Adi Veya Sifre");
						}
					}
					connection.close();
				} catch (SQLException a) {
					a.printStackTrace();

				}
			}
		});
		btnNewButton.setBounds(249, 254, 85, 21);
		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("Ogrenci Numarasi");
		lblNewLabel.setBounds(249, 122, 155, 13);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblSifre = new JLabel("Sifre");
		lblSifre.setBounds(249, 177, 155, 13);
		frame.getContentPane().add(lblSifre);
	}
}