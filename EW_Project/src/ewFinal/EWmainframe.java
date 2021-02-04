package ewFinal;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EWmainframe extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if(args.length != 1) {
					System.out.println("포트번호 입력하시오");
					System.exit(1);
				}
				try {
					EWmainframe frame = new EWmainframe(args[0]);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EWmainframe(String portno) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 466);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//게임시작
		JButton btnStart = new JButton("\uAC8C\uC784\uC2DC\uC791");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnStart.setBackground(new Color(255, 255, 205));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnStart.setBackground(new Color(255, 255, 240));
			}
		});
		
		btnStart.setForeground(SystemColor.textHighlight);
		btnStart.setBackground(new Color(255, 255, 240));
		btnStart.setFont(new Font("휴먼매직체", Font.PLAIN, 28));
		btnStart.setFocusPainted(false);
		btnStart.setBorderPainted(false);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Socket s1 = new Socket("127.0.0.1",Integer.parseInt(portno));
					System.out.println("서버에 연결");
					
					DataOutputStream outputStream = new DataOutputStream(s1.getOutputStream());
					DataInputStream inputStream = new DataInputStream(s1.getInputStream());
					
					//정원체크 및 입장제한
					if(inputStream != null) {
						int signal = inputStream.readInt();
						System.out.println("EWmainframe에서 " + signal + "번신호 들어옴");
						
						//입장가능
						if(signal == 0) {
							System.out.println(" 입장가능");
							//창전환
							dispose();
							setVisible(false);
							new EWsetting(s1, outputStream, inputStream).setVisible(true);
						}
						
						//입장불가
						if(signal == 1) {
							System.out.println(" 입장불가");
							JOptionPane.showMessageDialog(contentPane, "정원이 다 찼습니다, 잠시 기다려 주세요");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnStart.setBounds(106, 181, 147, 62);
		contentPane.add(btnStart);
		
		//게임종료
		JButton btnClose = new JButton("\uAC8C\uC784\uC885\uB8CC");
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnClose.setBackground(new Color(255, 255, 205));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnClose.setBackground(new Color(255, 255, 240));
			}
		});
		btnClose.setForeground(new Color(255, 0, 0));
		btnClose.setBackground(new Color(255, 255, 240));
		btnClose.setFont(new Font("휴먼매직체", Font.PLAIN, 28));
		//btnClose.setContentAreaFilled(false);
		btnClose.setFocusPainted(false);
		btnClose.setBorderPainted(false);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(350, 181, 147, 62);
		contentPane.add(btnClose);
		
		JLabel lblNewLabel = new JLabel("\uCFF5\uCFF5\uB530 \uAC8C\uC784");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("휴먼매직체", Font.BOLD, 92));
		lblNewLabel.setBounds(25, 33, 569, 125);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(EWmainframe.class.getResource("/image/3333333333333.PNG")));
		lblNewLabel_1.setBounds(12, 273, 588, 133);
		contentPane.add(lblNewLabel_1);
	}
}
