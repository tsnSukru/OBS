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

public class DanismanGirisi {

	Veritabani veritabani = new Veritabani();

	public JFrame frame;
	private JTextField tfDanismanNo;
	private JTextField tfSifre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DanismanGirisi window = new DanismanGirisi();
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
	public DanismanGirisi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 853, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		tfDanismanNo = new JTextField("104201503");
		tfDanismanNo.setBounds(304, 190, 178, 19);
		frame.getContentPane().add(tfDanismanNo);
		tfDanismanNo.setColumns(10);

		JButton BtnGiris = new JButton("Giris");
		BtnGiris.setBackground(Color.LIGHT_GRAY);
		BtnGiris.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
							veritabani.password);
					PreparedStatement statement = connection
							.prepareStatement("SELECT count(AdvisorID) as varmi FROM tbl_Advisor WHERE AdvisorID='"
									+ tfDanismanNo.getText() + "'" + "AND Password='" + tfSifre.getText() + "'");
					java.sql.ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						if (rs.getInt("varmi") == 1) {
							JOptionPane.showMessageDialog(frame, "Giris Basarili! Hosgeldiniz");
							DanismanEkrani frm1 = new DanismanEkrani(tfDanismanNo.getText());
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
		BtnGiris.setBounds(343, 293, 85, 21);
		frame.getContentPane().add(BtnGiris);

		JLabel lblNewLabel = new JLabel("Danisman Numarasi");
		lblNewLabel.setBounds(304, 167, 178, 13);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblSifre = new JLabel("Sifre");
		lblSifre.setBounds(304, 227, 178, 13);
		frame.getContentPane().add(lblSifre);

		tfSifre = new JTextField("11111");
		tfSifre.setColumns(10);
		tfSifre.setBounds(304, 248, 178, 19);
		frame.getContentPane().add(tfSifre);
	}

}
