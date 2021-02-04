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

	private String myNickname;		//내 닉네임
	private int myAvatarNum;		//내 아바타번호
	private int myLocation;			//내 위치
	
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
		
		//게임시작라벨
		lblstart = new JLabel("\uC7A0\uC2DC\uD6C4 \uAC8C\uC784\uC774 \uC2DC\uC791\uB429\uB2C8\uB2E4.");
		lblstart.setForeground(Color.RED);
		lblstart.setBackground(Color.ORANGE);
		lblstart.setFont(new Font("휴먼매직체", Font.BOLD, 72));
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
		ready01.setFont(new Font("휴먼매직체", Font.BOLD, 16));
		ready01.setBounds(38, 10, 100, 25);
		contentPane.add(ready01);

		ready02 = new JLabel("");
		ready02.setHorizontalAlignment(SwingConstants.CENTER);
		ready02.setFont(new Font("휴먼매직체", Font.BOLD, 16));
		ready02.setBounds(178, 10, 100, 25);
		contentPane.add(ready02);

		ready03 = new JLabel("");
		ready03.setHorizontalAlignment(SwingConstants.CENTER);
		ready03.setFont(new Font("휴먼매직체", Font.BOLD, 16));
		ready03.setBounds(318, 10, 100, 25);
		contentPane.add(ready03);

		ready04 = new JLabel("");
		ready04.setHorizontalAlignment(SwingConstants.CENTER);
		ready04.setFont(new Font("휴먼매직체", Font.BOLD, 16));
		ready04.setBounds(458, 10, 100, 25);
		contentPane.add(ready04);

		ready05 = new JLabel("");
		ready05.setHorizontalAlignment(SwingConstants.CENTER);
		ready05.setFont(new Font("휴먼매직체", Font.BOLD, 16));
		ready05.setBounds(598, 10, 100, 25);
		contentPane.add(ready05);

		ready06 = new JLabel("");
		ready06.setHorizontalAlignment(SwingConstants.CENTER);
		ready06.setFont(new Font("휴먼매직체", Font.BOLD, 16));
		ready06.setBounds(738, 10, 100, 25);
		contentPane.add(ready06);

		ready_btn = new JButton("READY");
		ready_btn.setBackground(new Color(255, 250, 205));
		ready_btn.setFont(new Font("휴먼매직체", Font.BOLD, 30));
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
		
		outputStream.writeInt(31);		//자신의 정보 요청 신호
		outputStream.writeInt(30);		//입장유저 전원의 정보 요청 신호
		
		Thread th1 = new Thread(this);
		th1.start();
	}

	@Override
	public void run() {
		
		try {
			
			while (inputStream != null) {

				signal = inputStream.readInt();
				System.out.println("신호들어옴 신호 : " + signal);
				
				//유저입장신호
				if (signal == 2) {
					nickname = inputStream.readUTF();
					avatarnum = inputStream.readInt();
					location = inputStream.readInt();
					boolean ready = inputStream.readBoolean();	//레디유무
					
					System.out.println("닉네임 : " + nickname);
					System.out.println("아바타 : " + avatarnum);
					System.out.println("위치 : " + location);
					System.out.println("레디여부 : " + ready);
					
					//유저 닉네임/아바타/레디 세팅로
					show(location, nickname, avatarnum, ready);
					
					//게임유저정보배열에 유저정보 객체생성/값세팅
					members[location-1] = new EWMember();			//객체생성
					members[location-1].setNickname(nickname);		//닉네임
					members[location-1].setAvatarNum(avatarnum);	//아바타번호
					members[location-1].setLocation(location);		//위치
					members[location-1].setKillPoint(0);			//킬포인트
					members[location-1].setRetire(false);			//탈락상태
					members[location-1].setReady(ready);			//레디상태
				}
				
				//유저 레디신호
				if( signal == 3) {
					location = inputStream.readInt();
					System.out.println("위치 : " + location);
					
					switch (location) {
					case 1:
						ready01.setText("READY");
						ready01.setFont(new Font("굴림", Font.BOLD, 22));
						ready01.setForeground(Color.RED);
						ready01.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready01.setVisible(true);
						
						break;
					case 2:
						ready02.setText("READY");
						ready02.setFont(new Font("굴림", Font.BOLD, 22));
						ready02.setForeground(Color.RED);
						ready02.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready02.setVisible(true);
						break;
					case 3:
						ready03.setText("READY");
						ready03.setFont(new Font("굴림", Font.BOLD, 22));
						ready03.setForeground(Color.RED);
						ready03.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready03.setVisible(true);
						break;
					case 4:
						ready04.setText("READY");
						ready04.setFont(new Font("굴림", Font.BOLD, 22));
						ready04.setForeground(Color.RED);
						ready04.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready04.setVisible(true);
						break;
					case 5:
						ready05.setText("READY");
						ready05.setFont(new Font("굴림", Font.BOLD, 22));
						ready05.setForeground(Color.RED);
						ready05.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready05.setVisible(true);
						break;
					case 6:
						ready06.setText("READY");
						ready06.setFont(new Font("굴림", Font.BOLD, 22));
						ready06.setForeground(Color.RED);
						ready06.setHorizontalAlignment(SwingConstants.CENTER);
						
						ready06.setVisible(true);
						break;
					default:
						break;
					}
				}
				
				//유저 채팅신호
				if(signal == 4) {
					String chat = inputStream.readUTF();
					textArea.append("\n" + chat);
				}
				
				//게임시작신호
				if(signal == 5) {
					System.out.println("5번신호받음 게임시작신호");
					
					//서버가 보내주는 첫단어/첫시작위치는 게임창 생성자에서 받아서 세팅
					
					//창전환
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
				
				//유저 퇴장신호
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
							
							//해당 유저정보배열 제거
							members[i] = null;
						}
					}
				}
				
				//기존유저정보 받기
				if(signal == 30) {	
					int cnt = inputStream.readInt();
					System.out.println("정원 " + cnt);
					
					for (int i=0; i<cnt; i++) {
						int location = inputStream.readInt();
						System.out.println("location: " + location);
						nickname = inputStream.readUTF();
						avatarnum = inputStream.readInt();
						boolean ready = inputStream.readBoolean();
						
						//유저세팅
						show(location, nickname, avatarnum, ready);
						
						//내 위치의 유저정보는 이미 세팅되어있기에 세팅하지 않는다
						if(myLocation != location) {
							//유저정보배열에 유저상태 세팅
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
				
				//자신의 정보 요청신호
				if(signal == 31) {
					myNickname = inputStream.readUTF();		//내 닉네임
					myAvatarNum = inputStream.readInt();	//내 아바타번호
					myLocation = inputStream.readInt();		//내 위치			==> 여기서 세팅한후 이 변수들은 노터치
					
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
		System.out.println("스레드끝");
	}
	
	public void show(int location, String nickname, int avatarnum, boolean ready) {
		switch (location) {
		case 1:
			//아바타번호가 -1이면 아직 닉네임/아바타 세팅 안한 상태이기에 아무것도 안한다
			if(avatarnum != -1) {
				img01.setIcon(new ImageIcon(EWReady4.class.getResource(img[avatarnum - 1])));
				nickname01.setText(nickname);

				img01.setVisible(true);
				nickname01.setVisible(true);

				//레디상태면 레디표시
				if(ready) {
					ready01.setText("READY");
					ready01.setFont(new Font("굴림", Font.BOLD, 22));
					ready01.setForeground(Color.RED);
					ready01.setHorizontalAlignment(SwingConstants.CENTER);

					ready01.setVisible(true);
				}


				//아래 case들도 모두 같은 형식으로 수정됨
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
					ready02.setFont(new Font("굴림", Font.BOLD, 22));
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
					ready03.setFont(new Font("굴림", Font.BOLD, 22));
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
					ready04.setFont(new Font("굴림", Font.BOLD, 22));
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
					ready05.setFont(new Font("굴림", Font.BOLD, 22));
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
					ready06.setFont(new Font("굴림", Font.BOLD, 22));
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
				outputStream.writeInt(2);			//레디신호
				outputStream.writeInt(myLocation);	//내위치
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}	
}
