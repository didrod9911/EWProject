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
	private List<Entry<String, Integer>> sortMap;	//�����ǿ�
	private JTable table;							//�����ǿ�
	private JLabel w1;		//�ܾ� 1����
	private JLabel w2;		//�ܾ� 1����
	private JLabel w3;		//�ܾ� 1����
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
	
	private Timer t1;			//�ð�ī��Ʈ progressbar�� Timer
	private int sec;			//�ð�ī��Ʈ�� �ð�����
	private int sec2;			//�ܾ�Ʋ������ xǥ�ø� ���� �ð�����
	
	private boolean myRetire;	//�� Ż�� ����
	private boolean myTurn;		//�������� �� ���̸� true
	
	private int fnum;
	private String word;
	private int signal;
	private String message="";
	
	//�����ǿ� ������
	private HashMap<String,Integer> chart = new HashMap <String,Integer>();
	private String header[]= {"����","�г���","kill ����"};//ǥ����
	private String contents[][] = {//ǥ����
			{"1��","",""},
			{"2��","",""},
			{"3��","",""},
			{"4��","",""},
			{"5��","",""},
			{"6��","",""}};
	private DefaultTableModel model = new DefaultTableModel(contents,header);//ǥ����
	
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
		lblgameover.setFont(new Font("�޸ո���ü", Font.PLAIN, 99));
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
		nickname01.setFont(new Font("�޸ո���ü", Font.PLAIN, 20));
		nickname01.setHorizontalAlignment(SwingConstants.CENTER);
		nickname01.setBounds(35, 480, 100, 25);
		contentPane.add(nickname01);
		
		turn01 = new JLabel("");
		turn01.setFont(new Font("�޸ո���ü", Font.PLAIN, 22));
		turn01.setHorizontalAlignment(SwingConstants.CENTER);
		turn01.setBounds(35, 335, 100, 37);
		contentPane.add(turn01);
		
		//���Ӵܾ��Է�â
		mytext = new JTextField();
		mytext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("�Է��Ѵ�: "+mytext.getText());
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
		w2.setFont(new Font("����", Font.BOLD, 65));
		w2.setBounds(390, 70, 90, 90);
		contentPane.add(w2);
		
		w1 = new JLabel("\uB530");
		w1.setHorizontalAlignment(SwingConstants.CENTER);
		w1.setFont(new Font("����", Font.BOLD, 65));
		w1.setBounds(270, 70, 90, 90);
		contentPane.add(w1);
		
		w3 = new JLabel("\uB530");
		w3.setHorizontalAlignment(SwingConstants.CENTER);
		w3.setForeground(Color.RED);
		w3.setFont(new Font("����", Font.BOLD, 78));
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
		nickname02.setFont(new Font("�޸ո���ü", Font.PLAIN, 20));
		nickname02.setHorizontalAlignment(SwingConstants.CENTER);
		nickname02.setBounds(170, 480, 100, 25);
		contentPane.add(nickname02);
		
		nickname03 = new JLabel("");
		nickname03.setFont(new Font("�޸ո���ü", Font.PLAIN, 20));
		nickname03.setHorizontalAlignment(SwingConstants.CENTER);
		nickname03.setBounds(310, 480, 100, 25);
		contentPane.add(nickname03);
		
		nickname04 = new JLabel("");
		nickname04.setFont(new Font("�޸ո���ü", Font.PLAIN, 20));
		nickname04.setHorizontalAlignment(SwingConstants.CENTER);
		nickname04.setBounds(450, 480, 100, 25);
		contentPane.add(nickname04);
		
		nickname05 = new JLabel("");
		nickname05.setFont(new Font("�޸ո���ü", Font.PLAIN, 20));
		nickname05.setHorizontalAlignment(SwingConstants.CENTER);
		nickname05.setBounds(590, 480, 100, 25);
		contentPane.add(nickname05);
		
		nickname06 = new JLabel("");
		nickname06.setFont(new Font("�޸ո���ü", Font.PLAIN, 20));
		nickname06.setHorizontalAlignment(SwingConstants.CENTER);
		nickname06.setBounds(730, 480, 100, 25);
		contentPane.add(nickname06);
		
		turn02 = new JLabel("");
		turn02.setFont(new Font("�޸ո���ü", Font.PLAIN, 22));
		turn02.setHorizontalAlignment(SwingConstants.CENTER);
		turn02.setBounds(170, 335, 100, 37);
		contentPane.add(turn02);
		
		turn03 = new JLabel("");
		turn03.setFont(new Font("�޸ո���ü", Font.PLAIN, 22));
		turn03.setHorizontalAlignment(SwingConstants.CENTER);
		turn03.setBounds(310, 335, 100, 37);
		contentPane.add(turn03);
		
		turn04 = new JLabel("");
		turn04.setFont(new Font("�޸ո���ü", Font.PLAIN, 22));
		turn04.setHorizontalAlignment(SwingConstants.CENTER);
		turn04.setBounds(450, 335, 100, 37);
		contentPane.add(turn04);
		
		turn05 = new JLabel("");
		turn05.setFont(new Font("�޸ո���ü", Font.PLAIN, 22));
		turn05.setHorizontalAlignment(SwingConstants.CENTER);
		turn05.setBounds(590, 335, 100, 37);
		contentPane.add(turn05);
		
		turn06 = new JLabel("");
		turn06.setFont(new Font("�޸ո���ü", Font.PLAIN, 22));
		turn06.setHorizontalAlignment(SwingConstants.CENTER);
		turn06.setBounds(730, 335, 100, 37);
		contentPane.add(turn06);
		
		//������
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(25, 90, 200, 122);
		contentPane.add(scrollPane_1);
		table = new JTable(model);
		scrollPane_1.setViewportView(table);
		
		//�ð�ī��Ʈ�� progressbar
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
		
		//����ȭ�鿡�� ���ӽ��۽�ȣ ������ ������ ������ ù�ܾ�/ù������ġ
		word = inputStream.readUTF();
		System.out.println("ù�ܾ�: "+ word);	
		showWord(word);	//�ܾ����
		fnum = inputStream.readInt();
		System.out.println("ù��°����: "+fnum);
		
		if(fnum == myLocation){
			//������ġ�� �� ��ġ�� �ܾ��Է�â Ȱ��ȭ
			mytext.setEnabled(true);
		}
		
		//����ǥ �ʱ⼼��(�г���/ų����Ʈ(0��))
		//HashMap�� �г���/ų����Ʈ �߰�
		for(int i=0; i<this.members.length; i++) {
			if(this.members[i] != null) {
				chart.put(this.members[i].getNickname(), this.members[i].getKillPoint());
			}
		}
		
		//����ǥ ����
		sortMap = new ArrayList<Entry<String, Integer>>(chart.entrySet());
		Collections.sort(sortMap, new Comparator<Entry<String, Integer>>() {
			// compare�� ���� ��
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2)
			{
				// ���� �������� ����
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});
		
		//����ǥ ���
		for(int i = 0;i<sortMap.size();i++)
		{
			for(int j = 1;j<3;j++) {
				contents[i][j]= String.valueOf(sortMap.get(i).getKey());
				j+=1;
				contents[i][j]= String.valueOf(sortMap.get(i).getValue());
			}
		}
		model.setDataVector(contents, header);
		
		//����â ���鼭 �ش� �� ���� ���� ���̹��� ����
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
		
		//����â ���鼭 �����ƹ�Ÿ/�г��� ����
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
		
		//ù������ġ�� �� ��ġ�� myTurn true
		if(myLocation == fnum) {
			myTurn = true;
		} else {
			myTurn = false;
		}
		
		Thread th2 = new Thread(this);
		th2.start();
	}
	
	//�ܾ���� �޼ҵ�
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
				//�ܾ����
				showWord(word);
				
				signal = inputStream.readInt();
				System.out.println("��ȣ���� ��ȣ : " + signal);
				
				//����ä�� ��ȣ
				if(signal == 4) {
					textArea.append("\n"+inputStream.readUTF());
				}
				
				//�Է´ܾ� �����ȣ
				if(signal == 7) {
					System.out.println("����");
					mytext.setText("");
					
					//�����̹��� ���
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
				
				if(signal == 8) {//�����ȣ
					//��������� progressbar�� Timer ����, �����̴� �����ϰ� �ٽ� ����)					
					t1.cancel();
					
					System.out.println("����");
					mytext.setText("");
					word = inputStream.readUTF();
					fnum = inputStream.readInt();
					System.out.println("��������: "+fnum);
					
					if(myLocation == fnum) {
						myTurn = true;
						mytext.setEnabled(true);
					} else {
						myTurn = false;
						mytext.setEnabled(false);
					}

					//�ٿ���� ���̹��� ����)
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
				
				//����Ż����ȣ
				if(signal == 9) {
					int retireUserNum = inputStream.readInt();	//Ż��������ġ
					int nextUserNum = inputStream.readInt();	//����������ġ
					int killUserNum = inputStream.readInt();	//ų������ġ
					String nextWord = inputStream.readUTF();	//�����ܾ�
					word = nextWord;							//�����ܾ��
					
					System.out.println("Ż��������ȣ : " + retireUserNum);
					System.out.println("����������ġ : " + nextUserNum);
					System.out.println("ų�ѻ����ġ : " + killUserNum);
					System.out.println("�����ܾ� : " + nextWord);
					
					//ų������ȣ�� 1~6��, 0�̶�°� ��ǻ�Ͱ� �������� �� �ܾ Ż���ߴٴ� �ǹ��̹Ƿ� ų����Ʈ����X
					if(killUserNum != 0) {
						//ų�ѻ�� ų����Ʈ �߰�						
						int killPoint = members[killUserNum - 1].getKillPoint() + 1;
						members[killUserNum - 1].setKillPoint(killPoint);
						
						//ų����Ʈ�� ��ȭ�� �־����Ƿ� ����ǥ �ٽü���
						//����ǥ Ŭ����
						chart.clear();
						//����ǥ Ŭ����
						sortMap.clear();			
						
						//�г���,ų����Ʈ ����
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) {
								chart.put(members[i].getNickname(), members[i].getKillPoint());
							}
						}
						
						sortMap = new ArrayList<Entry<String, Integer>>(chart.entrySet());
						
						//����ǥ ����
						Collections.sort(sortMap, new Comparator<Entry<String, Integer>>() {
							public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2){
								return obj2.getValue().compareTo(obj1.getValue());
							}
						});
						
						//����ǥ���
						for(int i = 0;i<sortMap.size();i++) {
							for(int j = 1;j<3;j++) {
								contents[i][j]= String.valueOf(sortMap.get(i).getKey());
								j+=1;
								contents[i][j]= String.valueOf(sortMap.get(i).getValue());
							}
						}
						model.setDataVector(contents, header);
					}
					
					//Ż���ڰ� �����̸� �ܾ��Է�â ��Ȱ��ȭ
					if(retireUserNum == myLocation) {
						mytext.setVisible(false);		//�ܾ��Է�â ��
						progressBar.setVisible(false);	//�ð�ī��Ʈ�� progressbar ��
						
						myTurn = false;					//Ż�������� �ڽ������� ��������
					}
					
					//�������ʰ� �����̸� �ܾ��Է�â Ȱ��ȭ
					if(nextUserNum == myLocation) {
						mytext.setEnabled(true);
						myTurn = true;
					}
					
					//Ż���� Ż���̹��� ó��
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
					
					//�������� ���� ���̹��� ����
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
				
				//������ 1�ν�ȣ				
				if(signal == 11) {
					System.out.println("������ 1�ν�ȣ");

					String nickname = inputStream.readUTF();		//����� �г���
					int avatarNum = inputStream.readInt();			//����� �ƹ�Ÿ��ȣ
					int location = inputStream.readInt();			//����� ��ġ
					int killpoint = inputStream.readInt();			//����� ų����Ʈ
					int retireUserNum = inputStream.readInt();		//Ż��������ġ
					int killerNum = inputStream.readInt();

					System.out.println("�г��� : " + nickname);
					System.out.println("�ƹ�Ÿ��ȣ : " + avatarNum);
					System.out.println("ų����Ʈ : " + killpoint);

					t1.cancel();

					//����ǥ ����
					if(killerNum != 0) {
						//ų�ѻ�� ų����Ʈ �߰�						
						killpoint++;
						members[killerNum - 1].setKillPoint(killpoint);
						
						//����ǥ Ŭ����
						chart.clear();
						sortMap.clear();
						
						//����ǥ �ٽ� ����
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

					//Ż���� �̹�������
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

					//����ڿ��� ȭ��ǥ
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

					//�������� �ȳ��޼���
					textArea.append("\n[�ȳ�] : ������ ����Ǿ����ϴ�");
					textArea.append("\n[�ȳ�] : ���ڴ� " + nickname + " �� �Դϴ�");
					textArea.append("\n[�ȳ�] : ų����Ʈ " + killpoint + "��");
					lblgameover.setVisible(true);
				}//11����ȣ-end
				
				
				//maxTurn ��ȣ
				if(signal == 12) {
					System.out.println("max�� ��ȣ");
					
					//Ÿ�̸� ����
					t1.cancel();
					
					//������ ��
					int userCnt = inputStream.readInt();
					
					//������ ���� �ȳ��޽����� ���
					textArea.append("\n[�ȳ�] : ������ ����Ǿ����ϴ�");
					for(int i=0; i<userCnt; i++) {
						textArea.append("\n[�ȳ�] : �������ڴ� "
										+ inputStream.readUTF()
										+ " ��(ų����Ʈ : " + inputStream.readInt()
										+ "��)�Դϴ�");
					}
					lblgameover.setVisible(true);
				}//12����ȣ-end
				
				//���������
				if(signal == 13) {
					int exitLocation = inputStream.readInt();			//����������ġ
					int nextLocation = inputStream.readInt();			//����������ġ
					boolean needNextTurn = inputStream.readBoolean();	//���������� ��� ���̾��� ������� true
					
					//�������� �̹���ó��
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
					
					//���������� ���� ���̾��� ������� ������ ȭ��ǥ �̵�
					if(needNextTurn) {
						//�������� �������ʸ� �ܾ�â Ȱ��ȭ
						if(nextLocation == myLocation) {
							myTurn = true;
							mytext.setEnabled(true);
						}
						
						//���������� ���� ���̾��� ������� Ÿ�̸� ���� �ʿ�
						//�������� Ÿ�̸� ���� ��ȣ ����(����Ÿ�̸� ���)
						t1.cancel();
						
						//�������� �̹�������
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
					
					//�ش� ���������迭 ����
					members[exitLocation - 1] = null;
				}//13����ȣ-end
				
				//Ÿ�̸ӿ� progressbar ���ý�ȣ
				if(signal == 20) {	
					t1 = new Timer();
					sec = 10000;
					
					//progressbar����
					t1.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							//�ð��� �� �ǰ�
							if(sec == -1000) {
								//���� Ż�����°� �ƴϰ� & �����̸� ��ȣ ����
								if(!myRetire && myTurn) {
									try {
										members[myLocation - 1].setRetire(true);	//Ż������ true
										outputStream.writeInt(5);					//����Ż����ȣ
										outputStream.writeInt(myLocation);			//Ż��������ġ(����ġ)
									} catch (IOException e) {
										//e.printStackTrace();
									}									
								}

								//Ÿ�̸�����
								//�� ���� �ƴϰų� �̹� Ż�����¸� ��ȣ�� ������ �ʰ� Ÿ�̸Ӹ� ����
								t1.cancel();
							}

							progressBar.setValue(sec);
							progressBar.setString(sec/1000 + "��");

							sec -= 1000;
						}
					}, 0, 1000);
				}//20����ȣ-end
			}
		}catch(IOException e) {
		}//catch end
	}//run end
}