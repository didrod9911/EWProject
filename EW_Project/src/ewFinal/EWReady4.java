package ewFinal;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EWReady4 extends JFrame implements Runnable, ActionListener {

	private JPanel contentPane;
	private Socket s1;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private EWMember[] members = {null, null, null, null, null, null};
	
	private String[] img = { "/image/001.png", "/image/002.png", "/image/003.png", "/image/004.png", "/image/005.png",
			"/image/006.png", "/image/007.png", "/image/008.png" };

	private int signal;
	private int location;
	private int avatarnum;
	private String nickname;

	private String myNickname;		//�� �г���
	private int myAvatarNum;		//�� �ƹ�Ÿ��ȣ
	private int myLocation;			//�� ��ġ
	
	JLabel img01;
	JLabel img02;
	JLabel img03;
	JLabel img04;
	JLabel img05;
	JLabel img06;
	JLabel nickname01;
	JLabel nickname02;
	JLabel nickname03;
	JLabel nickname04;
	JLabel nickname05;
	JLabel nickname06;
	JLabel ready01;
	JLabel ready02;
	JLabel ready03;
	JLabel ready04;
	JLabel ready05;
	JLabel ready06;
	JButton ready_btn;
	JTextArea textArea = new JTextArea();
	JTextField messagebox = new JTextField();
	String message="";
	JLabel lblstart;
	 

	public EWReady4(Socket s1, DataOutputStream outputStream, DataInputStream inputStream) throws IOException {	
		this.s1 = s1;
		this.outputStream = outputStream;
		this.inputStream = inputStream;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 892, 575);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 240));
		contentPane.setForeground(new Color(255, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//���ӽ��۶�
		lblstart = new JLabel("\uC7A0\uC2DC\uD6C4 \uAC8C\uC784\uC774 \uC2DC\uC791\uB429\uB2C8\uB2E4.");
		lblstart.setForeground(Color.RED);
		lblstart.setBackground(Color.ORANGE);
		lblstart.setFont(new Font("�޸ո���ü", Font.BOLD, 72));
		lblstart.setBounds(38, 167, 800, 81);
		lblstart.setVisible(false);
		lblstart.setOpaque(true);
		contentPane.add(lblstart);

		img01 = new JLabel("");
		img01.setHorizontalAlignment(SwingConstants.CENTER);
		img01.setBounds(38, 38, 100, 100);
		contentPane.add(img01);

		img02 = new JLabel("");
		img02.setHorizontalAlignment(SwingConstants.CENTER);
		img02.setBounds(178, 38, 100, 100);
		contentPane.add(img02);

		img03 = new JLabel();
		img03.setHorizontalAlignment(SwingConstants.CENTER);
		img03.setBounds(318, 38, 100, 100);
		contentPane.add(img03);

		img04 = new JLabel("");
		img04.setHorizontalAlignment(SwingConstants.CENTER);
		img04.setBounds(458, 38, 100, 100);
		contentPane.add(img04);

		img05 = new JLabel("");
		img05.setHorizontalAlignment(SwingConstants.CENTER);
		img05.setBounds(598, 38, 100, 100);
		contentPane.add(img05);

		img06 = new JLabel("");
		img06.setHorizontalAlignment(SwingConstants.CENTER);
		img06.setBounds(738, 38, 100, 100);
		contentPane.add(img06);

		nickname01 = new JLabel("");
		nickname01.setHorizontalAlignment(SwingConstants.CENTER);
		nickname01.setBounds(38, 148, 100, 20);
		contentPane.add(nickname01);

		nickname02 = new JLabel("");
		nickname02.setHorizontalAlignment(SwingConstants.CENTER);
		nickname02.setBounds(178, 148, 100, 20);
		contentPane.add(nickname02);

		nickname03 = new JLabel("");
		nickname03.setHorizontalAlignment(SwingConstants.CENTER);
		nickname03.setBounds(318, 148, 100, 20);
		contentPane.add(nickname03);

		nickname04 = new JLabel("");
		nickname04.setHorizontalAlignment(SwingConstants.CENTER);
		nickname04.setBounds(458, 148, 100, 20);
		contentPane.add(nickname04);

		nickname05 = new JLabel("");
		nickname05.setHorizontalAlignment(SwingConstants.CENTER);
		nickname05.setBounds(598, 148, 100, 20);
		contentPane.add(nickname05);

		nickname06 = new JLabel("");
		nickname06.setHorizontalAlignment(SwingConstants.CENTER);
		nickname06.setBounds(738, 148, 100, 20);
		contentPane.add(nickname06);

		ready01 = new JLabel("");
		ready01.setHorizontalAlignment(SwingConstants.CENTER);
		ready01.setFont(new Font("�޸ո���ü", Font.BOLD, 16));
		ready01.setBounds(38, 10, 100, 25);
		contentPane.add(ready01);

		ready02 = new JLabel("");
		ready02.setHorizontalAlignment(SwingConstants.CENTER);
		ready02.setFont(new Font("�޸ո���ü", Font.BOLD, 16));
		ready02.setBounds(178, 10, 100, 25);
		contentPane.add(ready02);

		ready03 = new JLabel("");
		ready03.setHorizontalAlignment(SwingConstants.CENTER);
		ready03.setFont(new Font("�޸ո���ü", Font.BOLD, 16));
		ready03.setBounds(318, 10, 100, 25);
		contentPane.add(ready03);

		ready04 = new JLabel("");
		ready04.setHorizontalAlignment(SwingConstants.CENTER);
		ready04.setFont(new Font("�޸ո���ü", Font.BOLD, 16));
		ready04.setBounds(458, 10, 100, 25);
		contentPane.add(ready04);

		ready05 = new JLabel("");
		ready05.setHorizontalAlignment(SwingConstants.CENTER);
		ready05.setFont(new Font("�޸ո���ü", Font.BOLD, 16));
		ready05.setBounds(598, 10, 100, 25);
		contentPane.add(ready05);

		ready06 = new JLabel("");
		ready06.setHorizontalAlignment(SwingConstants.CENTER);
		ready06.setFont(new Font("�޸ո���ü", Font.BOLD, 16));
		ready06.setBounds(738, 10, 100, 25);
		contentPane.add(ready06);

		ready_btn = new JButton("READY");
		ready_btn.setBackground(new Color(255, 250, 205));
		ready_btn.setFont(new Font("�޸ո���ü", Font.BOLD, 30));
		ready_btn.setFocusPainted(false);
		ready_btn.setBounds(318, 185, 200, 50);
		contentPane.add(ready_btn);
		ready_btn.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 260, 800, 200);
		contentPane.add(scrollPane);
		
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		messagebox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					message = messagebox.getText();
					try {
						DataOutputStream outputstream = new DataOutputStream(s1.getOutputStream());
						int signal = 3;
						outputstream.writeInt(signal);
						outputstream.writeUTF(myNickname);
						outputstream.writeUTF(message);
						
					} catch (IOException e) {
						//e.printStackTrace();
					}
					
					messagebox.setText("");
				}
			}
		});
		
		messagebox.setBounds(30, 480, 800, 35);
		contentPane.add(messagebox);
		messagebox.setColumns(10);
		
		outputStream.writeInt(31);		//�ڽ��� ���� ��û ��ȣ
		outputStream.writeInt(30);		//�������� ������ ���� ��û ��ȣ
		
		Thread th1 = new Thread(this);
		th1.start();
	}

	@Override
	public void run() {
		
		try {
			
			while (inputStream != null) {

				signal = inputStream.readInt();
				System.out.println("��ȣ���� ��ȣ : " + signal);
				
				//���������ȣ
				if (signal == 2) {
					nickname = inputStream.readUTF();
					avatarnum = inputStream.readInt();
					location = inputStream.readInt();
					boolean ready = inputStream.readBoolean();	//��������
					
					System.out.println("�г��� : " + nickname);
					System.out.println("�ƹ�Ÿ : " + avatarnum);
					System.out.println("��ġ : " + location);
					System.out.println("���𿩺� : " + ready);
					
					//���� �г���/�ƹ�Ÿ/���� ���÷�
					show(location, nickname, avatarnum, ready);
					
					//�������������迭�� �������� ��ü����/������
					members[location-1] = new EWMember();			//��ü����
					members[location-1].setNickname(nickname);		//�г���
					members[location-1].setAvatarNum(avatarnum);	//�ƹ�Ÿ��ȣ
					members[location-1].setLocation(location);		//��ġ
					members[location-1].setKillPoint(0);			//ų����Ʈ
					members[location-1].setRetire(false);			//Ż������
					members[location-1].setReady(ready);			//�������
				}
				
				//���� �����ȣ
				if( signal == 3) {
					location = inputStream.readInt();
					System.out.println("��ġ : " + location);
					
					switch (location) {
					case 1:
						ready01.setText("READY");
						ready01.setFont(new Font("����", Font.BOLD, 22));
						ready01.setForeground(Color.RED);
						ready01.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready01.setVisible(true);
						
						break;
					case 2:
						ready02.setText("READY");
						ready02.setFont(new Font("����", Font.BOLD, 22));
						ready02.setForeground(Color.RED);
						ready02.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready02.setVisible(true);
						break;
					case 3:
						ready03.setText("READY");
						ready03.setFont(new Font("����", Font.BOLD, 22));
						ready03.setForeground(Color.RED);
						ready03.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready03.setVisible(true);
						break;
					case 4:
						ready04.setText("READY");
						ready04.setFont(new Font("����", Font.BOLD, 22));
						ready04.setForeground(Color.RED);
						ready04.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready04.setVisible(true);
						break;
					case 5:
						ready05.setText("READY");
						ready05.setFont(new Font("����", Font.BOLD, 22));
						ready05.setForeground(Color.RED);
						ready05.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready05.setVisible(true);
						break;
					case 6:
						ready06.setText("READY");
						ready06.setFont(new Font("����", Font.BOLD, 22));
						ready06.setForeground(Color.RED);
						ready06.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready06.setVisible(true);
						break;
					default:
						break;
					}
				}
				
				//���� ä�ý�ȣ
				if(signal == 4) {
					String chat = inputStream.readUTF();
					textArea.append("\n" + chat);
				}
				
				//���ӽ��۽�ȣ
				if(signal == 5) {
					System.out.println("5����ȣ���� ���ӽ��۽�ȣ");
					
					//������ �����ִ� ù�ܾ�/ù������ġ�� ����â �����ڿ��� �޾Ƽ� ����
					
					//â��ȯ
					ready_btn.setVisible(false);
					lblstart.setVisible(true);
					
					try {
						java.lang.Thread.sleep(3000);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					dispose();
					setVisible(false);
					if(myLocation == 1)
						new EWgameframe(s1,outputStream, inputStream, myNickname, myLocation, members).setVisible(true);
					if(myLocation == 2)
						new EWgameframe2(s1,outputStream, inputStream, myNickname, myLocation, members).setVisible(true);
					if(myLocation == 3)
						new EWgameframe3(s1,outputStream, inputStream, myNickname, myLocation, members).setVisible(true);
					if(myLocation == 4)
						new EWgameframe4(s1,outputStream, inputStream, myNickname, myLocation, members).setVisible(true);
					if(myLocation == 5)
						new EWgameframe5(s1,outputStream, inputStream, myNickname, myLocation, members).setVisible(true);
					
					break;
				}
				
				//���� �����ȣ
				if(signal == 13) {
					int location = inputStream.readInt();
					
					for(int i=0; i<members.length; i++) {
						if( (members[i] != null) && (location == members[i].getLocation())) {
							if(location == 1) {
								img01.setIcon(null);
								nickname01.setText("");
								ready01.setVisible(false);
							}
							if(location == 2) {
								img02.setIcon(null);
								nickname02.setText("");
								ready02.setVisible(false);
							}
							if(location == 3) {
								img03.setIcon(null);
								nickname03.setText("");
								ready03.setVisible(false);
							}
							if(location == 4) {
								img04.setIcon(null);
								nickname04.setText("");
								ready04.setVisible(false);
							}
							if(location == 5) {
								img05.setIcon(null);
								nickname05.setText("");
								ready05.setVisible(false);
							}
							if(location == 6) {
								img06.setIcon(null);
								nickname06.setText("");
								ready06.setVisible(false);
							}
							
							//�ش� ���������迭 ����
							members[i] = null;
						}
					}
				}
				
				//������������ �ޱ�
				if(signal == 30) {	
					int cnt = inputStream.readInt();
					System.out.println("���� " + cnt);
					
					for (int i=0; i<cnt; i++) {
						int location = inputStream.readInt();
						System.out.println("location: " + location);
						nickname = inputStream.readUTF();
						avatarnum = inputStream.readInt();
						boolean ready = inputStream.readBoolean();
						
						//��������
						show(location, nickname, avatarnum, ready);
						
						//�� ��ġ�� ���������� �̹� ���õǾ��ֱ⿡ �������� �ʴ´�
						if(myLocation != location) {
							//���������迭�� �������� ����
							members[location-1] = new EWMember();
							members[location-1].setNickname(nickname);
							members[location-1].setAvatarNum(avatarnum);
							members[location-1].setLocation(location);
							members[location-1].setKillPoint(0);
							members[location-1].setRetire(false);
							members[location-1].setReady(ready);							
						}
					}
				}
				
				//�ڽ��� ���� ��û��ȣ
				if(signal == 31) {
					myNickname = inputStream.readUTF();		//�� �г���
					myAvatarNum = inputStream.readInt();	//�� �ƹ�Ÿ��ȣ
					myLocation = inputStream.readInt();		//�� ��ġ			==> ���⼭ �������� �� �������� ����ġ
					
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		System.out.println("�����峡");
	}
	
	public void show(int location, String nickname, int avatarnum, boolean ready) {
		switch (location) {
		case 1:
			//�ƹ�Ÿ��ȣ�� -1�̸� ���� �г���/�ƹ�Ÿ ���� ���� �����̱⿡ �ƹ��͵� ���Ѵ�
			if(avatarnum != -1) {
				img01.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname01.setText(nickname);

				img01.setVisible(true);
				nickname01.setVisible(true);

				//������¸� ����ǥ��
				if(ready) {
					ready01.setText("READY");
					ready01.setFont(new Font("����", Font.BOLD, 22));
					ready01.setForeground(Color.RED);
					ready01.setHorizontalAlignment(SwingConstants.CENTER);

					ready01.setVisible(true);
				}


				//�Ʒ� case�鵵 ��� ���� �������� ������
			}

			break;
		case 2:
			if(avatarnum != -1) {
				img02.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname02.setText(nickname);
				
				img02.setVisible(true);
				nickname02.setVisible(true);

				if(ready) {
					ready02.setText("READY");
					ready02.setFont(new Font("����", Font.BOLD, 22));
					ready02.setForeground(Color.RED);
					ready02.setHorizontalAlignment(SwingConstants.CENTER);

					ready02.setVisible(true);
				}
			}
			break;
		case 3:
			if(avatarnum != -1) {
				img03.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname03.setText(nickname);
				
				img03.setVisible(true);
				nickname03.setVisible(true);

				if(ready) {
					ready03.setText("READY");
					ready03.setFont(new Font("����", Font.BOLD, 22));
					ready03.setForeground(Color.RED);
					ready03.setHorizontalAlignment(SwingConstants.CENTER);

					ready03.setVisible(true);
				}
			}
			break;
		case 4:
			if(avatarnum != -1) {
				img04.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname04.setText(nickname);
				
				img04.setVisible(true);
				nickname04.setVisible(true);

				if(ready) {
					ready04.setText("READY");
					ready04.setFont(new Font("����", Font.BOLD, 22));
					ready04.setForeground(Color.RED);
					ready04.setHorizontalAlignment(SwingConstants.CENTER);

					ready04.setVisible(true);
				}
			}
			break;
		case 5:
			if(avatarnum != -1) {
				img05.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname05.setText(nickname);
				
				img05.setVisible(true);
				nickname05.setVisible(true);

				if(ready) {
					ready05.setText("READY");
					ready05.setFont(new Font("����", Font.BOLD, 22));
					ready05.setForeground(Color.RED);
					ready05.setHorizontalAlignment(SwingConstants.CENTER);

					ready05.setVisible(true);
				}
			}
			break;
		case 6:
			if(avatarnum != -1) {
				img06.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname06.setText(nickname);
				
				img06.setVisible(true);
				nickname06.setVisible(true);

				if(ready) {
					ready06.setText("READY");
					ready06.setFont(new Font("����", Font.BOLD, 22));
					ready06.setForeground(Color.RED);
					ready06.setHorizontalAlignment(SwingConstants.CENTER);

					ready06.setVisible(true);
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ready_btn) {
			try {
				outputStream.writeInt(2);			//�����ȣ
				outputStream.writeInt(myLocation);	//����ġ
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}	
}
