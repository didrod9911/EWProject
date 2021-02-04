package ewFinal;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;

public class EWsetting extends JFrame {

	private JPanel contentPane;
	private JTextField nick_textField;
	private String nickname = null;
	
	private Socket s1;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	private int cliSig;
	private int charI = 0;
	
	private String[] img = {"/image/001.png","/image/002.png", "/image/003.png", "/image/004.png", "/image/005.png", "/image/006.png", "/image/007.png", "/image/008.png"};


	/**
	 * Create the frame.
	 */
	public EWsetting(Socket s1, DataOutputStream outputStream, DataInputStream inputStream) {
		
		this.s1 = s1;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 466);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nick_textField = new JTextField();
		nick_textField.setText("\uB2C9\uB124\uC784\uC744 \uC785\uB825\uD558\uC138\uC694");
		nick_textField.setBounds(129, 46, 254, 36);
		contentPane.add(nick_textField);
		nick_textField.setColumns(10);
		nick_textField.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mouseClicked(MouseEvent e) {
	            nick_textField.setText("");
	         }
	      });
		
		//닉네임중복체크버튼
		JButton btnNewButton = new JButton("\uD655\uC778");
		btnNewButton.setBackground(new Color(255, 250, 205));
		btnNewButton.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String imsi = nick_textField.getText();
				cliSig = 0;
				
				try {
					outputStream.writeInt(cliSig);
					outputStream.writeUTF(imsi);
					
					if(inputStream != null) {
						if(inputStream.readInt() == 100) {	//신호가 100번이면
							if(inputStream.readBoolean()) {
								JOptionPane.showMessageDialog(contentPane, "중복되는 닉네임");
								nick_textField.setText("");
							} else {
								JOptionPane.showMessageDialog(contentPane, "사용가능 닉네임");
								nickname = imsi;
							}
						}
					}
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(399, 46, 97, 36);
		contentPane.add(btnNewButton);
		
		
		JLabel lblNewLabel_1 = new JLabel("\uCE90\uB9AD\uD130\uB97C \uC120\uD0DD\uD558\uC138\uC694");
		lblNewLabel_1.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(211, 113, 166, 24);
		contentPane.add(lblNewLabel_1);
		
		class charSelect implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = ((JButton)e.getSource()).getActionCommand();
				charI = Integer.parseInt(name.substring(name.length()-1));
				System.out.println(charI+"번 째 이미지");
			}
		}
		
		class focus implements FocusListener{
			@Override
			public void focusGained(FocusEvent e) {
				JButton b = (JButton)e.getSource();
				b.setBackground(Color.yellow);
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				JButton b = (JButton)e.getSource();
				b.setBackground(SystemColor.text);
				
			}
		}
		
		ActionListener sel = new charSelect();
		FocusListener foc = new focus();
		
		ButtonGroup charG = new ButtonGroup();
		
		JButton charB1 = new JButton("");
		charB1.setBackground(new Color(255, 255, 240));
		charB1.setActionCommand("1");
		charB1.addActionListener(sel);
		charB1.addFocusListener(foc);
		charB1.setIcon(new ImageIcon(EWsetting.class.getResource(img[0])));
		charB1.setBounds(94, 157, 70, 68);
		charB1.setBorderPainted(false);
		contentPane.add(charB1);
		charG.add(charB1);
		
		JButton charB2 = new JButton("");
		charB2.setBackground(new Color(255, 255, 240));
		charB2.setActionCommand("2");
		charB2.addActionListener(sel);
		charB2.addFocusListener(foc);
		charB2.setIcon(new ImageIcon(EWsetting.class.getResource(img[1])));
		charB2.setBounds(195, 157, 70, 68);
		charB2.setBorderPainted(false);
		contentPane.add(charB2);
		charG.add(charB2);
		
		JButton charB3 = new JButton("");
		charB3.setBackground(new Color(255, 255, 240));
		charB3.setActionCommand("3");
		charB3.addActionListener(sel);
		charB3.addFocusListener(foc);
		charB3.setIcon(new ImageIcon(EWsetting.class.getResource(img[2])));
		charB3.setBounds(345, 157, 70, 68);
		charB3.setBorderPainted(false);
		contentPane.add(charB3);
		charG.add(charB3);
		
		JButton charB4 = new JButton("");
		charB4.setBackground(new Color(255, 255, 240));
		charB4.setActionCommand("4");
		charB4.addActionListener(sel);
		charB4.addFocusListener(foc);
		charB4.setIcon(new ImageIcon(EWsetting.class.getResource(img[3])));
		charB4.setBounds(458, 157, 70, 68);
		charB4.setBorderPainted(false);
		contentPane.add(charB4);
		charG.add(charB4);
		
		JButton charB5 = new JButton("");
		charB5.setBackground(new Color(255, 255, 240));
		charB5.setActionCommand("5");
		charB5.addActionListener(sel);
		charB5.addFocusListener(foc);
		charB5.setIcon(new ImageIcon(EWsetting.class.getResource(img[4])));
		charB5.setBounds(94, 247, 70, 68);
		charB5.setBorderPainted(false);
		contentPane.add(charB5);
		charG.add(charB5);
		
		JButton charB6 = new JButton("");
		charB6.setBackground(new Color(255, 255, 240));
		charB6.setActionCommand("6");
		charB6.addActionListener(sel);
		charB6.addFocusListener(foc);
		charB6.setIcon(new ImageIcon(EWsetting.class.getResource(img[5])));
		charB6.setBounds(195, 247, 70, 68);
		charB6.setBorderPainted(false);
		contentPane.add(charB6);
		charG.add(charB6);
		
		JButton charB7 = new JButton("");
		charB7.setBackground(new Color(255, 255, 240));
		charB7.setActionCommand("7");
		charB7.addActionListener(sel);
		charB7.addFocusListener(foc);
		charB7.setIcon(new ImageIcon(EWsetting.class.getResource(img[6])));
		charB7.setBounds(345, 247, 70, 68);
		charB7.setBorderPainted(false);
		contentPane.add(charB7);
		charG.add(charB7);
		
		JButton charB8 = new JButton("");
		charB8.setBackground(new Color(255, 255, 240));
		charB8.setActionCommand("8");
		charB8.addActionListener(sel);
		charB8.addFocusListener(foc);
		charB8.setIcon(new ImageIcon(EWsetting.class.getResource(img[7])));
		charB8.setBounds(458, 247, 70, 68);
		charB8.setBorderPainted(false);
		contentPane.add(charB8);
		charG.add(charB8);
		
		
		//설정완료
		JButton btnNewButton_1 = new JButton("\uC124\uC815\uC644\uB8CC");
		btnNewButton_1.setBackground(new Color(255, 250, 205));
		btnNewButton_1.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		btnNewButton_1.setFocusPainted(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//닉네임빈칸
				if(nickname==null) JOptionPane.showMessageDialog(contentPane, "닉네임을 입력하세요");
				
				//사진선택안함
				if(charI==0) JOptionPane.showMessageDialog(contentPane, "캐릭터를 선택하세요");
				
				//둘다선택했음
				if(nickname!=null&&charI!=0) {
					cliSig = 1;
					try {
						outputStream.writeInt(cliSig);
						outputStream.writeUTF(nickname);
						outputStream.writeInt(charI);
						//창전환
						dispose();
						setVisible(false);
						new EWReady(s1,outputStream,inputStream).setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		});
		btnNewButton_1.setBounds(225, 337, 153, 59);
		contentPane.add(btnNewButton_1);
	}
}
