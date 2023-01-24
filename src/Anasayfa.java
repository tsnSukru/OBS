import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Anasayfa {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Anasayfa window = new Anasayfa();
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
	public Anasayfa() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 800, 530);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnGiris = new JButton("Ogrenci Girisi");
		btnGiris.setBackground(Color.LIGHT_GRAY);
		btnGiris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OgrenciGirisEkrani frm1 = new OgrenciGirisEkrani();
				frm1.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnGiris.setBounds(100, 234, 122, 31);
		frame.getContentPane().add(btnGiris);

		JButton btnDanismanGirisi = new JButton("Danisman Girisi");
		btnDanismanGirisi.setBackground(Color.LIGHT_GRAY);
		btnDanismanGirisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DanismanGirisi frm2 = new DanismanGirisi();
				frm2.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnDanismanGirisi.setBounds(330, 234, 135, 31);
		frame.getContentPane().add(btnDanismanGirisi);

		JButton btnYetkiliGirisi = new JButton("Yetkili Girisi");
		btnYetkiliGirisi.setBackground(Color.LIGHT_GRAY);
		btnYetkiliGirisi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GorevliGirisEkrani frm3 = new GorevliGirisEkrani();
				frm3.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnYetkiliGirisi.setBounds(574, 234, 122, 31);
		frame.getContentPane().add(btnYetkiliGirisi);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("img\\ogrenciSimgesi.png"));
		lblNewLabel.setBounds(100, 69, 120, 120);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon("img\\danismanSimgesi.png"));
		lblNewLabel_1.setBounds(330, 69, 120, 120);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setIcon(new ImageIcon("img\\yetkiliSimgesi.png"));
		lblNewLabel_2.setBounds(574, 69, 120, 120);
		frame.getContentPane().add(lblNewLabel_2);
	}
}
