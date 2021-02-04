package ewFinal;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class EWgameframe5 extends JFrame implements Runnable {
	private  Socket socket;
	private  DataInputStream inputStream;
	private  DataOutputStream outputStream;
	private String myNickname;
	private int myLocation;
	private JPanel contentPane;
	private JTextField messagebox;
	private JTextField mytext;
	private EWMember[] members;
	private List<Entry<String, Integer>> sortMap;	//점수판용
	private JTable table;							//점수판용
	private JLabel w1;		//단어 1글자
	private JLabel w2;		//단어 1글자
	private JLabel w3;		//단어 1글자
	private JLabel lblgameover;
	private JTextArea textArea = new JTextArea();
	private JProgressBar progressBar = new JProgressBar();
	private JScrollPane scrollPane_1;
	
	private JLabel img01;
	private JLabel img02;
	private JLabel img03;
	private JLabel img04;
	private JLabel img05;
	private JLabel img06;
	private JLabel nickname01;
	private JLabel nickname02;
	private JLabel nickname03;
	private JLabel nickname04;
	private JLabel nickname05;
	private JLabel nickname06;
	private JLabel turn01;
	private JLabel turn02;
	private JLabel turn03;
	private JLabel turn04;
	private JLabel turn05;
	private JLabel turn06;
	
	private Timer t1;			//시간카운트 progressbar용 Timer
	private int sec;			//시간카운트용 시간변수
	private int sec2;			//단어틀렸을때 x표시를 위한 시간변수
	
	private boolean myRetire;	//내 탈락 상태
	private boolean myTurn;		//지금턴이 내 턴이면 true
	
	private int fnum;
	private String word;
	private int signal;
	private String message="";
	
	//점수판용 변수들
	private HashMap<String,Integer> chart = new HashMap <String,Integer>();
	private String header[]= {"순위","닉네임","kill 점수"};//표수정
	private String contents[][] = {//표수정
			{"1위","",""},
			{"2위","",""},
			{"3위","",""},
			{"4위","",""},
			{"5위","",""},
			{"6위","",""}};
	private DefaultTableModel model = new DefaultTableModel(contents,header);//표수정
	
	private JLabel lblNewLabel;
	
	public EWgameframe5(Socket s1, DataOutputStream outputStream, DataInputStream inputStream,
						String myNickname, int myLocation, EWMember[] members) throws IOException {
		this.socket=s1;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.myNickname = myNickname;
		this.myLocation = myLocation;
		this.members = members;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1150, 250, 892, 575);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		messagebox = new JTextField();
		messagebox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
				{
					message = messagebox.getText();
					try {
						System.out.println(myNickname+" : "+message);
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
		
		lblgameover = new JLabel("   Game Over");
		lblgameover.setForeground(Color.RED);
		lblgameover.setBackground(new Color(255, 255, 240));
		lblgameover.setFont(new Font("휴먼매직체", Font.PLAIN, 99));
		lblgameover.setBounds(25, 335, 805, 170);
		lblgameover.setVisible(false);
		lblgameover.setOpaque(true);
		contentPane.add(lblgameover);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(275, 60, 320, 125);
		contentPane.add(lblNewLabel);
		messagebox.setBounds(630, 260, 230, 31);
		contentPane.add(messagebox);
		messagebox.setColumns(10);
		
		img01 = new JLabel("");
		img01.setHorizontalAlignment(SwingConstants.CENTER);
		img01.setBounds(35, 365, 100, 100);
		contentPane.add(img01);
		
		nickname01 = new JLabel("");
		nickname01.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		nickname01.setHorizontalAlignment(SwingConstants.CENTER);
		nickname01.setBounds(35, 480, 100, 25);
		contentPane.add(nickname01);
		
		turn01 = new JLabel("");
		turn01.setFont(new Font("휴먼매직체", Font.PLAIN, 22));
		turn01.setHorizontalAlignment(SwingConstants.CENTER);
		turn01.setBounds(35, 335, 100, 37);
		contentPane.add(turn01);
		
		//게임단어입력창
		mytext = new JTextField();
		mytext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("입력한답: "+mytext.getText());
					outputStream.writeInt(4);
					outputStream.writeUTF(mytext.getText());
					outputStream.writeInt(myLocation);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		mytext.setBounds(260, 240, 350, 35);
		contentPane.add(mytext);
		mytext.setColumns(10);
		mytext.setEnabled(false);
		
		JLabel label_1 = new JLabel("");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/cline.png")));
		label_1.setBounds(500, 170, 110, 50);
		contentPane.add(label_1);
		
		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/bline2.png")));
		label.setBounds(260, 170, 110, 50);
		contentPane.add(label);
		
		JLabel label_2 = new JLabel("");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/bline2.png")));
		label_2.setBounds(380, 170, 110, 50);
		contentPane.add(label_2);
		
		w2 = new JLabel("\uB530");
		w2.setHorizontalAlignment(SwingConstants.CENTER);
		w2.setFont(new Font("굴림", Font.BOLD, 65));
		w2.setBounds(390, 70, 90, 90);
		contentPane.add(w2);
		
		w1 = new JLabel("\uB530");
		w1.setHorizontalAlignment(SwingConstants.CENTER);
		w1.setFont(new Font("굴림", Font.BOLD, 65));
		w1.setBounds(270, 70, 90, 90);
		contentPane.add(w1);
		
		w3 = new JLabel("\uB530");
		w3.setHorizontalAlignment(SwingConstants.CENTER);
		w3.setForeground(Color.RED);
		w3.setFont(new Font("굴림", Font.BOLD, 78));
		w3.setBounds(510, 70, 90, 90);
		contentPane.add(w3);
		
		img02 = new JLabel("");
		img02.setHorizontalAlignment(SwingConstants.CENTER);
		img02.setBounds(170, 365, 100, 100);
		contentPane.add(img02);
		
		img03 = new JLabel("");
		img03.setHorizontalAlignment(SwingConstants.CENTER);
		img03.setBounds(310, 365, 100, 100);
		contentPane.add(img03);
		
		img04 = new JLabel("");
		img04.setHorizontalAlignment(SwingConstants.CENTER);
		img04.setBounds(450, 365, 100, 100);
		contentPane.add(img04);
		
		img05 = new JLabel("");
		img05.setHorizontalAlignment(SwingConstants.CENTER);
		img05.setBounds(590, 365, 100, 100);
		contentPane.add(img05);
		
		img06 = new JLabel("");
		img06.setHorizontalAlignment(SwingConstants.CENTER);
		img06.setBounds(730, 365, 100, 100);
		contentPane.add(img06);
		
		nickname02 = new JLabel("");
		nickname02.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		nickname02.setHorizontalAlignment(SwingConstants.CENTER);
		nickname02.setBounds(170, 480, 100, 25);
		contentPane.add(nickname02);
		
		nickname03 = new JLabel("");
		nickname03.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		nickname03.setHorizontalAlignment(SwingConstants.CENTER);
		nickname03.setBounds(310, 480, 100, 25);
		contentPane.add(nickname03);
		
		nickname04 = new JLabel("");
		nickname04.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		nickname04.setHorizontalAlignment(SwingConstants.CENTER);
		nickname04.setBounds(450, 480, 100, 25);
		contentPane.add(nickname04);
		
		nickname05 = new JLabel("");
		nickname05.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		nickname05.setHorizontalAlignment(SwingConstants.CENTER);
		nickname05.setBounds(590, 480, 100, 25);
		contentPane.add(nickname05);
		
		nickname06 = new JLabel("");
		nickname06.setFont(new Font("휴먼매직체", Font.PLAIN, 20));
		nickname06.setHorizontalAlignment(SwingConstants.CENTER);
		nickname06.setBounds(730, 480, 100, 25);
		contentPane.add(nickname06);
		
		turn02 = new JLabel("");
		turn02.setFont(new Font("휴먼매직체", Font.PLAIN, 22));
		turn02.setHorizontalAlignment(SwingConstants.CENTER);
		turn02.setBounds(170, 335, 100, 37);
		contentPane.add(turn02);
		
		turn03 = new JLabel("");
		turn03.setFont(new Font("휴먼매직체", Font.PLAIN, 22));
		turn03.setHorizontalAlignment(SwingConstants.CENTER);
		turn03.setBounds(310, 335, 100, 37);
		contentPane.add(turn03);
		
		turn04 = new JLabel("");
		turn04.setFont(new Font("휴먼매직체", Font.PLAIN, 22));
		turn04.setHorizontalAlignment(SwingConstants.CENTER);
		turn04.setBounds(450, 335, 100, 37);
		contentPane.add(turn04);
		
		turn05 = new JLabel("");
		turn05.setFont(new Font("휴먼매직체", Font.PLAIN, 22));
		turn05.setHorizontalAlignment(SwingConstants.CENTER);
		turn05.setBounds(590, 335, 100, 37);
		contentPane.add(turn05);
		
		turn06 = new JLabel("");
		turn06.setFont(new Font("휴먼매직체", Font.PLAIN, 22));
		turn06.setHorizontalAlignment(SwingConstants.CENTER);
		turn06.setBounds(730, 335, 100, 37);
		contentPane.add(turn06);
		
		//점수판
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(25, 90, 200, 122);
		contentPane.add(scrollPane_1);
		table = new JTable(model);
		scrollPane_1.setViewportView(table);
		
		//시간카운트용 progressbar
		progressBar.setBounds(260, 275, 350, 17);
		progressBar.setMaximum(10000);
		contentPane.add(progressBar);
		progressBar.setVisible(true);
		progressBar.setForeground(new Color(102,0,250));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(630, 20, 230, 236);
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea);
		
		textArea.setEditable(false);
		
		//레디화면에서 게임시작신호 받을시 서버가 보내준 첫단어/첫시작위치
		word = inputStream.readUTF();
		System.out.println("첫단어: "+ word);	
		showWord(word);	//단어출력
		fnum = inputStream.readInt();
		System.out.println("첫번째순서: "+fnum);
		
		if(fnum == myLocation){
			//시작위치가 내 위치면 단어입력창 활성화
			mytext.setEnabled(true);
		}
		
		//점수표 초기세팅(닉네임/킬포인트(0점))
		//HashMap에 닉네임/킬포인트 추가
		for(int i=0; i<this.members.length; i++) {
			if(this.members[i] != null) {
				chart.put(this.members[i].getNickname(), this.members[i].getKillPoint());
			}
		}
		
		//점수표 정렬
		sortMap = new ArrayList<Entry<String, Integer>>(chart.entrySet());
		Collections.sort(sortMap, new Comparator<Entry<String, Integer>>() {
			// compare로 값을 비교
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2)
			{
				// 내림 차순으로 정렬
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		
		//점수표 출력
		for(int i = 0;i<sortMap.size();i++)
		{
			for(int j = 1;j<3;j++) {
				contents[i][j]= String.valueOf(sortMap.get(i).getKey());
				j+=1;
				contents[i][j]= String.valueOf(sortMap.get(i).getValue());
			}
		}
		model.setDataVector(contents, header);
		
		//게임창 열면서 해당 턴 유저 위에 턴이미지 세팅
		if(fnum == 1)
			turn01.setText("MyTurn!");
		if(fnum == 2)
			turn02.setText("MyTurn!");
		if(fnum == 3)
			turn03.setText("MyTurn!");
		if(fnum == 4)
			turn04.setText("MyTurn!");
		if(fnum == 5)
			turn05.setText("MyTurn!");
		if(fnum == 6)
			turn06.setText("MyTurn!");
		
		//게임창 열면서 유저아바타/닉네임 세팅
		for(int i=0; i<this.members.length; i++) {
			if(this.members[i] != null) {
				int location = this.members[i].getLocation();
				int avatarNum = this.members[i].getAvatarNum();
				String avatarImage = "/image/00" + avatarNum + ".png";
				
				if(location == 1) {
					img01.setIcon(new ImageIcon(EWgameframe5.class.getResource(avatarImage)));
					nickname01.setText(this.members[i].getNickname());
				}
				if(location == 2) {
					img02.setIcon(new ImageIcon(EWgameframe5.class.getResource(avatarImage)));
					nickname02.setText(this.members[i].getNickname());
				}
				if(location == 3) {
					img03.setIcon(new ImageIcon(EWgameframe5.class.getResource(avatarImage)));
					nickname03.setText(this.members[i].getNickname());
				}
				if(location == 4) {
					img04.setIcon(new ImageIcon(EWgameframe5.class.getResource(avatarImage)));
					nickname04.setText(this.members[i].getNickname());
				}
				if(location == 5) {
					img05.setIcon(new ImageIcon(EWgameframe5.class.getResource(avatarImage)));
					nickname05.setText(this.members[i].getNickname());
				}
				if(location == 6) {
					img06.setIcon(new ImageIcon(EWgameframe5.class.getResource(avatarImage)));
					nickname06.setText(this.members[i].getNickname());
				}
			}
		}
		
		myRetire = false;
		
		//첫시작위치가 내 위치면 myTurn true
		if(myLocation == fnum) {
			myTurn = true;
		} else {
			myTurn = false;
		}
		
		Thread th2 = new Thread(this);
		th2.start();
	}
	
	//단어출력 메소드
	public void showWord(String word) {
		w1.setText(word.substring(0,1));
		w2.setText(word.substring(1,2));
		w3.setText(word.substring(2));
	}
	
	@Override
	public void run() {
		try {
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			
			while(inputStream != null){
				//단어출력
				showWord(word);
				
				signal = inputStream.readInt();
				System.out.println("신호들어옴 신호 : " + signal);
				
				//유저채팅 신호
				if(signal == 4) {
					textArea.append("\n"+inputStream.readUTF());
				}
				
				//입력단어 오답신호
				if(signal == 7) {
					System.out.println("오답");
					mytext.setText("");
					
					//오답이미지 출력
					sec2 = 300;
					Timer t2 = new Timer();
					t2.schedule(new TimerTask() {
						@Override
						public void run() {
							lblNewLabel.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/incorrect.png")));							
							
							if(sec2 == 0) {
								lblNewLabel.setIcon(null);
								t2.cancel();
							}
							
							sec2 -= 300;
						}
					}, 0, 300);				
				}
				
				if(signal == 8) {//정답신호
					//만들어졌던 progressbar용 Timer 종료, 정답이니 종료하고 다시 세팅)					
					t1.cancel();
					
					System.out.println("정답");
					mytext.setText("");
					word = inputStream.readUTF();
					fnum = inputStream.readInt();
					System.out.println("다음순서: "+fnum);
					
					if(myLocation == fnum) {
						myTurn = true;
						mytext.setEnabled(true);
					} else {
						myTurn = false;
						mytext.setEnabled(false);
					}

					//다움순서 턴이미지 세팅)
					if(fnum == 1) {
						turn01.setText("MyTurn!");
						
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(fnum == 2) {
						turn02.setText("MyTurn!");
						
						turn01.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(fnum == 3) {
						turn03.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(fnum == 4) {
						turn04.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(fnum == 5) {
						turn05.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn06.setText(null);
					}
					if(fnum == 6) {
						turn06.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
					}
				}
				
				//유저탈락신호
				if(signal == 9) {
					int retireUserNum = inputStream.readInt();	//탈락유저위치
					int nextUserNum = inputStream.readInt();	//다음유저위치
					int killUserNum = inputStream.readInt();	//킬유저위치
					String nextWord = inputStream.readUTF();	//다음단어
					word = nextWord;							//다음단어세팅
					
					System.out.println("탈락유저번호 : " + retireUserNum);
					System.out.println("다음유저위치 : " + nextUserNum);
					System.out.println("킬한사람위치 : " + killUserNum);
					System.out.println("다음단어 : " + nextWord);
					
					//킬유저번호는 1~6번, 0이라는건 컴퓨터가 랜덤으로 낸 단어에 탈락했다는 의미이므로 킬포인트로직X
					if(killUserNum != 0) {
						//킬한사람 킬포인트 추가						
						int killPoint = members[killUserNum - 1].getKillPoint() + 1;
						members[killUserNum - 1].setKillPoint(killPoint);
						
						//킬포인트에 변화가 있었으므로 점수표 다시세팅
						//점수표 클리어
						chart.clear();
						//점수표 클리어
						sortMap.clear();			
						
						//닉네임,킬포인트 세팅
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) {
								chart.put(members[i].getNickname(), members[i].getKillPoint());
							}
						}
						
						sortMap = new ArrayList<Entry<String, Integer>>(chart.entrySet());
						
						//점수표 정렬
						Collections.sort(sortMap, new Comparator<Entry<String, Integer>>() {
							public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2){
								return obj2.getValue().compareTo(obj1.getValue());
							}
						});
						
						//점수표출력
						for(int i = 0;i<sortMap.size();i++) {
							for(int j = 1;j<3;j++) {
								contents[i][j]= String.valueOf(sortMap.get(i).getKey());
								j+=1;
								contents[i][j]= String.valueOf(sortMap.get(i).getValue());
							}
						}
						model.setDataVector(contents, header);
					}
					
					//탈락자가 본인이면 단어입력창 비활성화
					if(retireUserNum == myLocation) {
						mytext.setVisible(false);		//단어입력창 끔
						progressBar.setVisible(false);	//시간카운트용 progressbar 끔
						
						myTurn = false;					//탈락했으니 자신의턴은 이제없음
					}
					
					//다음차례가 본인이면 단어입력창 활성화
					if(nextUserNum == myLocation) {
						mytext.setEnabled(true);
						myTurn = true;
					}
					
					//탈락자 탈락이미지 처리
					if(retireUserNum == 1) {
						img01.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 2) {
						img02.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 3) {
						img03.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 4) {
						img04.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 5) {
						img05.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 6) {
						img06.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					
					//다음차례 유저 턴이미지 세팅
					if(nextUserNum == 1) {
						turn01.setText("MyTurn!");
						
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(nextUserNum == 2) {
						turn02.setText("MyTurn!");
						
						turn01.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(nextUserNum == 3) {
						turn03.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(nextUserNum == 4) {
						turn04.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(nextUserNum == 5) {
						turn05.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn06.setText(null);
					}
					if(nextUserNum == 6) {
						turn06.setText("MyTurn!");
						
						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
					}
				}
				
				//최후의 1인신호				
				if(signal == 11) {
					System.out.println("최후의 1인신호");

					String nickname = inputStream.readUTF();		//우승자 닉네임
					int avatarNum = inputStream.readInt();			//우승자 아바타번호
					int location = inputStream.readInt();			//우승자 위치
					int killpoint = inputStream.readInt();			//우승자 킬포인트
					int retireUserNum = inputStream.readInt();		//탈락유저위치
					int killerNum = inputStream.readInt();

					System.out.println("닉네임 : " + nickname);
					System.out.println("아바타번호 : " + avatarNum);
					System.out.println("킬포인트 : " + killpoint);

					t1.cancel();

					//점수표 세팅
					if(killerNum != 0) {
						//킬한사람 킬포인트 추가						
						killpoint++;
						members[killerNum - 1].setKillPoint(killpoint);
						
						//점수표 클리어
						chart.clear();
						sortMap.clear();
						
						//점수표 다시 세팅
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) {
								chart.put(members[i].getNickname(), members[i].getKillPoint());
							}
						}

						sortMap = new ArrayList<Entry<String, Integer>>(chart.entrySet());

						Collections.sort(sortMap, new Comparator<Entry<String, Integer>>() {
							public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2){
								return obj2.getValue().compareTo(obj1.getValue());
							}
						});
						for(int i = 0;i<sortMap.size();i++) {
							for(int j = 1;j<3;j++) {
								contents[i][j]= String.valueOf(sortMap.get(i).getKey());
								j+=1;
								contents[i][j]= String.valueOf(sortMap.get(i).getValue());
							}
						}
						model.setDataVector(contents, header);
					}

					//탈락자 이미지세팅
					if(retireUserNum == 1) {
						img01.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 2) {
						img02.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 3) {
						img03.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 4) {
						img04.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 5) {
						img05.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}
					if(retireUserNum == 6) {
						img06.setIcon(new ImageIcon(EWgameframe5.class.getResource("/image/retire.png")));
					}

					//우승자에게 화살표
					if(location == 1) {
						turn01.setText("MyTurn!");

						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(location == 2) {
						turn02.setText("MyTurn!");

						turn01.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(location == 3) {
						turn03.setText("MyTurn!");

						turn01.setText(null);
						turn02.setText(null);
						turn04.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(location == 4) {
						turn04.setText("MyTurn!");

						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn05.setText(null);
						turn06.setText(null);
					}
					if(location == 5) {
						turn05.setText("MyTurn!");

						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn06.setText(null);
					}
					if(location == 6) {
						turn06.setText("MyTurn!");

						turn01.setText(null);
						turn02.setText(null);
						turn03.setText(null);
						turn04.setText(null);
						turn05.setText(null);
					}

					//게임종료 안내메세지
					textArea.append("\n[안내] : 게임이 종료되었습니다");
					textArea.append("\n[안내] : 승자는 " + nickname + " 님 입니다");
					textArea.append("\n[안내] : 킬포인트 " + killpoint + "점");
					lblgameover.setVisible(true);
				}//11번신호-end
				
				
				//maxTurn 신호
				if(signal == 12) {
					System.out.println("max턴 신호");
					
					//타이머 정지
					t1.cancel();
					
					//생존자 수
					int userCnt = inputStream.readInt();
					
					//생존자 정보 안내메시지로 출력
					textArea.append("\n[안내] : 게임이 종료되었습니다");
					for(int i=0; i<userCnt; i++) {
						textArea.append("\n[안내] : 공동승자는 "
										+ inputStream.readUTF()
										+ " 님(킬포인트 : " + inputStream.readInt()
										+ "점)입니다");
					}
					lblgameover.setVisible(true);
				}//12번신호-end
				
				//유저퇴장시
				if(signal == 13) {
					int exitLocation = inputStream.readInt();			//나간유저위치
					int nextLocation = inputStream.readInt();			//다음유저위치
					boolean needNextTurn = inputStream.readBoolean();	//나간유저가 방금 턴이었던 유저라면 true
					
					//퇴장유저 이미지처리
					if(exitLocation == 1) {
						img01.setIcon(null);
						nickname01.setText("");
					}
					if(exitLocation == 2) {
						img02.setIcon(null);
						nickname02.setText("");
					}
					if(exitLocation == 3) {
						img03.setIcon(null);
						nickname03.setText("");
					}
					if(exitLocation == 4) {
						img04.setIcon(null);
						nickname04.setText("");
					}
					if(exitLocation == 5) {
						img05.setIcon(null);
						nickname05.setText("");
					}
					if(exitLocation == 6) {
						img06.setIcon(null);
						nickname06.setText("");
					}
					
					//나간유저가 지금 턴이었던 유저라면 유저턴 화살표 이동
					if(needNextTurn) {
						//다음턴이 본인차례면 단어창 활성화
						if(nextLocation == myLocation) {
							myTurn = true;
							mytext.setEnabled(true);
						}
						
						//나간유저가 지금 턴이었던 유저라면 타이머 리셋 필요
						//서버에서 타이머 리셋 신호 보냄(기존타이머 취소)
						t1.cancel();
						
						//다음유저 이미지세팅
						if(nextLocation == 1) {
							turn01.setText("MyTurn!");
							
							turn02.setText(null);
							turn03.setText(null);
							turn04.setText(null);
							turn05.setText(null);
							turn06.setText(null);
						}
						if(nextLocation == 2) {
							turn02.setText("MyTurn!");
							
							turn01.setText(null);
							turn03.setText(null);
							turn04.setText(null);
							turn05.setText(null);
							turn06.setText(null);
						}
						if(nextLocation == 3) {
							turn03.setText("MyTurn!");
							
							turn01.setText(null);
							turn02.setText(null);
							turn04.setText(null);
							turn05.setText(null);
							turn06.setText(null);
						}
						if(nextLocation == 4) {
							turn04.setText("MyTurn!");
							
							turn01.setText(null);
							turn02.setText(null);
							turn03.setText(null);
							turn05.setText(null);
							turn06.setText(null);
						}
						if(nextLocation == 5) {
							turn05.setText("MyTurn!");
							
							turn01.setText(null);
							turn02.setText(null);
							turn03.setText(null);
							turn04.setText(null);
							turn06.setText(null);
						}
						if(nextLocation == 6) {
							turn06.setText("MyTurn!");
							
							turn01.setText(null);
							turn02.setText(null);
							turn03.setText(null);
							turn04.setText(null);
							turn05.setText(null);
						}
					}
					
					//해당 유저정보배열 제거
					members[exitLocation - 1] = null;
				}//13번신호-end
				
				//타이머용 progressbar 세팅신호
				if(signal == 20) {	
					t1 = new Timer();
					sec = 10000;
					
					//progressbar세팅
					t1.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							//시간이 다 되고
							if(sec == -1000) {
								//내가 탈락상태가 아니고 & 내턴이면 신호 보냄
								if(!myRetire && myTurn) {
									try {
										members[myLocation - 1].setRetire(true);	//탈락상태 true
										outputStream.writeInt(5);					//유저탈락신호
										outputStream.writeInt(myLocation);			//탈락유저위치(내위치)
									} catch (IOException e) {
										//e.printStackTrace();
									}									
								}

								//타이머종료
								//내 턴이 아니거나 이미 탈락상태면 신호는 보내지 않고 타이머만 종료
								t1.cancel();
							}

							progressBar.setValue(sec);
							progressBar.setString(sec/1000 + "초");

							sec -= 1000;
						}
					}, 0, 1000);
				}//20번신호-end
			}
		}catch(IOException e) {
		}//catch end
	}//run end
}