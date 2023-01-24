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

public class GorevliGirisEkrani {

	Veritabani veritabani = new Veritabani();

	public JFrame frame;
	private JTextField tfGorevliNo;
	private JTextField tfSifre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GorevliGirisEkrani window = new GorevliGirisEkrani();
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
	public GorevliGirisEkrani() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 827, 529);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		tfGorevliNo = new JTextField("300540301");
		tfGorevliNo.setBounds(278, 203, 176, 19);
		frame.getContentPane().add(tfGorevliNo);
		tfGorevliNo.setColumns(10);

		JLabel lblNewLabel = new JLabel("Gorevli Numarasi");
		lblNewLabel.setBounds(278, 180, 176, 13);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblSifre = new JLabel("Sifre");
		lblSifre.setBounds(278, 238, 176, 13);
		frame.getContentPane().add(lblSifre);

		tfSifre = new JTextField("1111");
		tfSifre.setColumns(10);
		tfSifre.setBounds(278, 261, 176, 19);
		frame.getContentPane().add(tfSifre);

		JButton btnGiris = new JButton("Giris");
		btnGiris.setBackground(Color.LIGHT_GRAY);
		btnGiris.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Connection connection = DriverManager.getConnection(veritabani.url, veritabani.user,
							veritabani.password);
					PreparedStatement statement = connection.prepareStatement(
							"SELECT count(PK_AuthorizedID) as varmi FROM tbl_Authorized WHERE PK_AuthorizedID='"
									+ tfGorevliNo.getText() + "'" + "AND Password='" + tfSifre.getText() + "'");
					java.sql.ResultSet rs = statement.executeQuery();
					while (rs.next()) {
						if (rs.getInt("varmi") == 1) {
							JOptionPane.showMessageDialog(frame, "Giris Basarili! Hosgeldiniz");
							GorevliEkrani frm1 = new GorevliEkrani(tfGorevliNo.getText());
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
		btnGiris.setBounds(278, 310, 104, 21);
		frame.getContentPane().add(btnGiris);
	}

}
