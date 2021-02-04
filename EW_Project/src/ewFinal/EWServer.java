package ewFinal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerClass {
	
	private ArrayList<UserThread> users = new ArrayList<UserThread>();	//��������������
	private EWMember[] members = {null, null, null, null, null, null};	//�������������迭
	private ArrayList<String> dic = new EWWords().dic;					//�ܾ���� ArrayList
	private ArrayList<String> ans = new ArrayList<String>();			//����� �ܾ� ArrayList
	
	//���ӿ� ����
	private int start = 0; 												//ó�� ���� �ο�
	private int ready = 0; 												//���� ����Ϸ� ���� Ȯ��
	private String fWord = dic.get((int)(Math.random()*dic.size()));	//ó�� ���þ�
	private String fWordK = fWord;										//ó�����þ� ��� ����� ����
	private int fNum = 1; 												//�����ϴ� ��� ��ȣ(ù������ �׻� 1������)
	private int okay = 0; 												//������ ��ġ
	private int temp = 0; 												//�ӽ������
	private int lastOne = 0; 											//������ 1�� �ǵ���
	private int maxTrun = 50;											//������ = 50��
	private int nowTurn = 1;											//���� ���� ������ ��ġ
	
	public ServerClass() {}
	public ServerClass(int portNo) throws IOException {
		Socket s1 = null;
		ServerSocket ss1 = new ServerSocket(portNo);
		
		System.out.println("����������...");
		
		while(true) {
			s1 = ss1.accept();
			DataOutputStream outputStream = new DataOutputStream(s1.getOutputStream());
			
			System.out.println("����IP\t: " + s1.getLocalAddress());
			System.out.println("������Ʈ\t: " + s1.getLocalPort());
			
			//���� = 6��, ���� 5�� ���ϸ� ����OK
			if(users.size() <= 5) {
				UserThread user = new UserThread(s1);	//������ ���� ��
				outputStream.writeInt(0);				//���Ӱ��ɽ�ȣ
				
				//���Ӱ� ���ÿ� �������� �⺻������ ����
				for(int i=0; i<members.length; i++) {
					if(members[i] == null) {
						members[i] = new EWMember();	//��ü����
						members[i].setS1(s1);			//��������
						members[i].setNickname("");		//�г���
						members[i].setAvatarNum(-1);	//�ƹ�Ÿ��ȣ
						members[i].setLocation(i+1);	//��ġ
						members[i].setKillPoint(0);		//ų����Ʈ
						members[i].setRetire(false);	//Ż������
						members[i].setReady(false);		//�������
						members[i].setScreenNum(2);		//������ ȭ���ȣ
						
						break;
					}
				}
				
				//�������� Ȯ�ο�
				for(int i=0; i<members.length; i++) {
					if(members[i] != null) {
						System.out.print("�������� " + members[i].getLocation() + "��  ");
					} else {
						System.out.print("�������� Null  ");
					}
				}
				
				user.start();							//��ŸƮ
				users.add(user);						//�ο�üũ�� ArrayList�� �߰�
			}else {
				outputStream.writeInt(1);				//���ӺҰ���ȣ
			}
			
			System.out.println("���� �����ڼ� : " + users.size() + "��");
		}//while(true)-end
	}//ServerClass������-end
	
	//���� ���� ���� ������
	class UserThread extends Thread{
		Socket socket;
		DataInputStream inputStream;
		DataOutputStream outputStream;
		int signal;		//��ȣ
		String aWord;	//Ŭ���̾�Ʈ���� �Է��� ���Ӵܾ� �����
		
		public UserThread() {}
		public UserThread(Socket s1) throws IOException {
			socket = s1;
			inputStream = new DataInputStream(s1.getInputStream());
			outputStream = new DataOutputStream(s1.getOutputStream());
		}
		
		public void run() {			
			try {
				while(inputStream != null) {
					//Ŭ���̾�Ʈ���� ���� ��ȣ���� ����
					signal = inputStream.readInt();
					
					//�г��� �ߺ�üũ ��ȣ
					if(signal == 0) {
						System.out.println("�ߺ�üũ��ȣ");
						String nickname = inputStream.readUTF();
						Boolean overlap = false;
						
						//�ߺ��г��� ������ �ߺ�ó��
						for (int i=0; i < members.length; i++) {
							if(members[i] != null) {
								if(nickname.equals(members[i].getNickname())) {
									overlap = true;
									break;
								}																	
							}
						}
						
						//�ߺ�Ȯ�ν�ȣ
						outputStream.writeInt(100);
						outputStream.writeBoolean(overlap);
					}//0����ȣ-end
					
					
					// Ŭ���̾�Ʈ�� �г���/�ƹ�Ÿ ������ '�����Ϸ�'Ŭ����
					if(signal == 1) {
						System.out.println("1����ȣ ����");
						String nickname = inputStream.readUTF();
						int avatarNum = inputStream.readInt();
						
						System.out.println("�г��� : " + nickname);
						System.out.println("�ƹ�Ÿ��ȣ : " + avatarNum);
						
						//���������ü �迭�� �г��Ӱ� �ƹ�Ÿ��ȣ ����
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) {
								//�������� �ش����� �˻�
								if(members[i].getS1().equals(socket)) {
									members[i].setNickname(nickname);
									members[i].setAvatarNum(avatarNum);
									int location = members[i].getLocation();
									boolean ready = members[i].isReady();
									members[i].setScreenNum(3);	//ȭ���ȣ, 3��ȭ�� : ����ȭ��
									
									System.out.println((location) + "�� ����");
									
									//��� �������� ���� ������ �г���/�ƹ�Ÿ����/��ġ ����
									for(int j=0; j<users.size(); j++) {
										users.get(j).outputStream.writeInt(2);
										users.get(j).outputStream.writeUTF(nickname);
										users.get(j).outputStream.writeInt(avatarNum);
										users.get(j).outputStream.writeInt(location);
										users.get(j).outputStream.writeBoolean(ready);
									}
									
									break;
								}
							}
						}
						
						//����â�� ���� �����鿡�� ������ ���� �˸��޽���
						for(int i=0; i<members.length; i++) {
							//�ش���ġ�� ������ �����ϰ� �ƹ�Ÿ��ȣ�� -1�� �ƴϸ�(�ƹ�Ÿ��ȣ�� 1~8)
							//�ƹ�Ÿ��ȣ�� -1�̸� ���� ����â���� �ƹ�Ÿ�� �������� �ʾƼ� ����â�� ���ٴ� �ǹ�
							if(members[i] != null && (members[i].getAvatarNum() != -1)) {
								Socket s = members[i].getS1();
								
								for(int j=0; j<users.size(); j++) {
									if(s.equals(users.get(j).socket)) {
										users.get(j).outputStream.writeInt(4);
										users.get(j).outputStream.writeUTF("[" + nickname + "] ���� �����ϼ̽��ϴ�");
									}
								}
							}
						}
					}//1����ȣ-end
					
					//�����ư Ŭ����
					if(signal == 2) {
						int loc = inputStream.readInt();
						
						ready += 1; //���� �Ϸ�� ���� +1
						members[loc-1].setReady(true);
						System.out.println("2����ȣ ����");
						System.out.println("��ġ��ȣ : " + loc);
						
						////////////->3�� ��ȣ: ���� �Ϸ� ��ȣ///////////
						for (int i=0; i < users.size();i++) {
							System.out.println("���� �Ϸ� ��ȣ ����: "+(i+1)+"��");
							users.get(i).outputStream.writeInt(3);//�ٸ� ���� ���� �Ϸ� ��ȣ
							users.get(i).outputStream.writeInt(loc);//����Ϸ� ���� ��ȣ
						}
						System.out.println("�����ο��� : " + ready);
						if (ready >= users.size() && (ready != 1)) { //����Ϸ� ī��Ʈ == ������ ũ�� (���� ��� ��)
							
							for(int i=(members.length-1); i >= 0; i--) {
								if(members[i] != null) {
									start = i+1;
									break;
								}
							}
							
							System.out.println("���ӽ���");//�� ���� ���� �� ���ӽ���
							
							//////////////-> 5�� ��ȣ: ���� ���� ��ȣ//////////////
							for (int i =0 ; i<users.size();i++) {//(������ �������� ����)
								users.get(i).outputStream.writeInt(5); //���� ���� ��ȣ
								users.get(i).outputStream.writeUTF(fWord); // ù�ܾ� ����
								users.get(i).outputStream.writeInt(fNum); //ó�� �����ϴ� ��� ����
								
								users.get(i).outputStream.writeInt(20);	//����ī��Ʈ�� progressbar���۽�ȣ
							}
							
							ready = 0; //���� �Ϸ� ī��Ʈ �ʱ�ȭ
						}
					}//2����ȣ-end
					
					//Ŭ���̾�Ʈ�� ä�� �Է½�
					if(signal == 3) {
						System.out.println("3����ȣ ���� ä���Է�");
						String nickname = inputStream.readUTF();
						String chat = inputStream.readUTF();
						
						System.out.println("�г��� : " + nickname);
						System.out.println("ä�ó��� : " + chat);
						
						//�켱 �ӼӸ�üũ
						//�ӼӸ����� : '/w [�г���] [�Ҹ�]'
						if(chat.charAt(0) == '/') {
							try {
								if( (chat.charAt(1) == 'w') || (chat.charAt(1) == 'W') || (chat.charAt(1) == '��')) {
									//������� ���� �ϴ� /w or /W or /�� (�ӼӸ���ɾ�)�� �Է�
									//������ �������� ������
									String[] arrChar = chat.split(" ");
									
									//���̰� 3�̸�(1 or 2) = �г��ӵ� �Ⱦ��ų� �г����� ������ �Ҹ��� �Ⱦ����
									if(arrChar.length < 3) {
										//���� ���ܹ߻�
										throw new ArrayIndexOutOfBoundsException();
									}
									
									//������� ���� ����� �� �ӼӸ�
									String targetNickname = arrChar[1];			//��� �г�������
									String whisper = "";						//���� ä�� �ʱ�ȭ
									
									for(int i=2; i<arrChar.length; i++) {		//�迭�� ������ ä�� ��ġ��
										whisper += " " + arrChar[i];
									}
									
									for(int i=0; i<members.length; i++) {
										//�ӼӸ� ���� ���� Ž��
										if(members[i] != null) {
											if(targetNickname.equals(members[i].getNickname())) {
												Socket targetSocket = members[i].getS1();
												
												//��� ���� ã�Ƽ� ä�ó��� ����
												for(int j=0; j<users.size(); j++) {
													if(targetSocket.equals(users.get(j).socket)) {
														users.get(j).outputStream.writeInt(4);
														users.get(j).outputStream.writeUTF(nickname + "(�ӼӸ�) ===> " + whisper);

														break;	//���� for�� Ż��
													}												
												}
												
												break;	//�ٱ� for�� Ż��
											}
										}
									}
								} else {
									System.out.println("�ӼӸ� ���� Ʋ��");
									throw new StringIndexOutOfBoundsException();
								}	
							} catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
								//�ӼӸ� �߽��ο��� ���� �޽��� ������
								outputStream.writeInt(4);
								outputStream.writeUTF("[�˸�] ==> �ӼӸ��� \"/w �г��� ä�ó���\" "
														+ "�Ǵ� \"/W �г��� ä�ó���\" "
														+ "�Ǵ� \"/�� �г��� ä�ó���\"�Դϴ�");
							}							
						} else {
							//�Ϲ�ä��
							System.out.println("�Ϲ�ä��");
							//�������� ä������
							for(int i=0; i<users.size(); i++) {
								users.get(i).outputStream.writeInt(4);
								users.get(i).outputStream.writeUTF(nickname + " ===> " + chat);
							}
						}						
					}//3����ȣ-end
					
					//���������� ���Ӵܾ� �Է½�
					if(signal == 4) {
						aWord = inputStream.readUTF();		//������ �Է��� �ܾ�
						temp = inputStream.readInt();		//�Է��� ������ġ
						System.out.println("4����ȣ ����");
						System.out.println("�Է´ܾ� : " + aWord);
						
						if (HeadLaw(fWord, aWord))  { //������ ��� ����	
							ans.add(aWord);
							okay = temp; //��� ���� ����� ���� ���
							// �ߺ� ����� ���ϱ� ���� ���� arrayList�� ��
							fWord = aWord;
							// �����ձ� �̾�� ���� ù �ܾ� �������� ����
							
							for(int i=(members.length-1); i >= 0; i--) {
								if(members[i] != null) {
									start = i+1;
									break;
								}
							}
							
							////////////////->8�� ��ȣ ������ : ������ �� ���� ��ȣ//////////////
							maxTrun--;
							
							for (int i=0;i < users.size();i++) {
								users.get(i).outputStream.writeInt(8);
								//����� 8�� ��ȣ(������ �������� ����)
								users.get(i).outputStream.writeUTF(fWord);
								//���� �ܾ� fWord
								
								nowTurn = StartLoc((okay % start), start) + 1;
								users.get(i).outputStream.writeInt(nowTurn);
								//���� ���� ���� ��ġ ������

								users.get(i).outputStream.writeInt(20);
								//���α׷��� �� ���� ��ȣ
								
								if(maxTrun == 0) {
									int userCnt = 0;								
									//�����ִ� ������
									for(int j=0; j<members.length; j++) {
										if((members[j] != null) && (!members[j].isRetire()))
											userCnt++;
									}
									
									//������ �������� ������ ���� ����
									for(int j=0; j<users.size(); j++) {
										users.get(j).outputStream.writeInt(12);
										users.get(j).outputStream.writeInt(userCnt);

										for(int k=0; k<members.length; k++) {
											if(members[k] != null) {
												users.get(j).outputStream.writeUTF(members[k].getNickname());
												users.get(j).outputStream.writeInt(members[k].getKillPoint());
											}
										}
									}
									
									//�����ȣ �ʱ�ȭ
									ready = 0;
								}
							}
						}//�����ϰ�� if��-end
						////////////////->7�� ��ȣ ������ : ���� ��ȣ////////////
						else {
							for(int i=0 ; i != users.size(); i++) {
								users.get(i).outputStream.writeInt(7);
								//����� 7�� ��ȣ(������ ����)
							}
						}
					}//4����ȣ-end
					
					//���� Ż����
					if(signal == 5) {
						System.out.println("5����ȣ ���� Ż����ȣ");
						int ord = inputStream.readInt();	//Ż���� ��ȣ(��ġ)
						EWMember loc = members[ord -1];		//�ش� Ż������ ����
						
						if(!fWord.equals(fWordK)) { //ù ���þ�� ������ ����� �ٸ� ���
													//��ǻ�Ͱ� �������� ������ �ܾ Ż��������� kill������ �ö��� ����
							//���� ��� ų���� �߰� ����
							//okay : �ٷ� ���� �������� ���� �Ѱ�� ������ġ
							int score = members[okay-1].getKillPoint();
							members[okay-1].setKillPoint(score+1);
						}

						lastOne += 1; //Ż���� ��� +1 //������ 1�� ��궧 ���
						
						fWord = dic.get((int)(Math.random()*dic.size())); //ù ���þ� �ʱ�ȭ
						fWordK= fWord;//ų �߰� �ǵ���
						
						System.out.println("��ġ��ȣ : " + loc.getLocation());
						
						fNum = loc.getLocation();
						members[ord-1].setRetire(true);
						loc.setRetire(true); //Ż���� Ż�� ó��
						
						//����������ġ ���ϴ� ������ �ʿ��� ���� �����۾�(start)
						for(int i=(members.length-1); i >= 0; i--) {
							if(members[i] != null) {
								start = i+1;
								break;
							}
						}
						
						fNum = StartLoc((fNum % start), start);	//������ ��ġ ���ϱ�
						nowTurn = fNum + 1;
						
						maxTrun--;
						
						if (lastOne == users.size()-1) {
							////////////->10�� ��ȣ: ����� ���� ��ȣ////////////	
							for(int i=0; i<members.length; i++) {
								if((members[i] != null) && (!members[i].isRetire())) {
									for(int j=0; j<users.size(); j++) {
										users.get(j).outputStream.writeInt(11);
										users.get(j).outputStream.writeUTF(members[i].getNickname());
										users.get(j).outputStream.writeInt(members[i].getAvatarNum());
										users.get(j).outputStream.writeInt(members[i].getLocation());	//����� ��ġ
										users.get(j).outputStream.writeInt(members[i].getKillPoint());
										users.get(j).outputStream.writeInt(ord);	//Ż���� ��ġ
										users.get(j).outputStream.writeInt(okay);	//������ ��ġ
									}
								}
							}
							
							fWord = dic.get((int)(Math.random()*dic.size()));
							fWordK = fWord;
							fNum = 1;
							okay = 0;
							temp = 0;
							ready = 0;
						}
						/////////////->9�� ��ȣ ������ : Ż���� ���� ��ȣ/////////////
						else {
							for (int i=0 ;i < users.size(); i++) {
								users.get(i).outputStream.writeInt(9);					//9����ȣ: ���� Ż�� ��ȣ
								users.get(i).outputStream.writeInt(loc.getLocation());	//Ż�� ���� ��ȣ
								users.get(i).outputStream.writeInt(fNum+1);				//������ġ
								users.get(i).outputStream.writeInt(okay);				//ų�ѻ����ġ
								users.get(i).outputStream.writeUTF(fWord);				//���ο� ù ���þ�
								
								users.get(i).outputStream.writeInt(20);					//���α׷��� �� ���� ��ȣ
							}
							
							if(maxTrun == 0) {
								int userCnt = 0;
								
								//�����ִ� ������ ī��Ʈ
								for(int i=0; i<members.length; i++) {
									if((members[i] != null) && (!members[i].isRetire()))
										userCnt++;
								}
								
								//������ �������� ������ ���� ����
								for(int i=0; i<users.size(); i++) {
									users.get(i).outputStream.writeInt(12);
									users.get(i).outputStream.writeInt(userCnt);
									
									for(int j=0; j<members.length; j++) {
										if(members[j] != null) {
											users.get(i).outputStream.writeUTF(members[j].getNickname());
											users.get(i).outputStream.writeInt(members[j].getKillPoint());
										}
									}
								}
							}
						}
					}//5����ȣ-end
					
					//��ü����������û
					if(signal == 30) {
						outputStream.writeInt(30);
						
						int userCnt = users.size();		//���� ������ ������
						outputStream.writeInt(userCnt);	//���������� ����
						
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) { //��ġ �г��� �ƹ�Ÿ ����
								outputStream.writeInt(members[i].getLocation());	//��ġ
								outputStream.writeUTF(members[i].getNickname());	//�г���
								outputStream.writeInt(members[i].getAvatarNum());	//�ƹ�Ÿ��ȣ
								outputStream.writeBoolean(members[i].isReady());	//���𿩺�
							}
						}
					}
					
					//Ŭ���̾�Ʈ �ڽ������� ��û��ȣ
					if(signal == 31) {
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) {
								if(socket.equals(members[i].getS1())) {
									outputStream.writeInt(31);
									outputStream.writeUTF(members[i].getNickname());
									outputStream.writeInt(members[i].getAvatarNum());
									outputStream.writeInt(members[i].getLocation());
								}
							}
						}
					}					
				}//while(inputStream != null)-end
			} catch (IOException e) {
				//e.printStackTrace();
			//����ó��
			} finally {
				//Thread�����س��� ArrayList���� �ش����� ����
				for(int i=0; i<users.size(); i++) {
					if(socket.equals(users.get(i).socket))
						users.remove(i);											
				}
				
				//�������� ��ġã��� ��������
				int size = 0;
				for(int i=(members.length-1); i >= 0; i--) {
					if(members[i] != null) {
						size = i+1;
						break;
					}
				}
				
				//����������ü �迭���� �ش����� ��ü null��
				for(int i=0; i<members.length; i++) {
					if((members[i] != null) && (socket.equals(members[i].getS1()))) {
						try {
							//������ ȭ���� 2��(�ƹ�Ÿ����ȭ��)�� �ƴϸ�(�ƹ�Ÿ ����ȭ���� ä��â�� �����Ƿ� �����ʿ�X)
							if(members[i].getScreenNum() != 2) {
								int exitUserNum = members[i].getLocation();				//����������ġ
								int nextUserNum = StartLoc((exitUserNum % size), size);	//����������ġ
								boolean needNextTurn = false;							//������ ������ ��ÿ� ���̾��� ��� true
								
								//������ ������ ���� ���� ������� true
								if(members[i].getLocation() == nowTurn)
									needNextTurn = true;
								
								nowTurn = nextUserNum + 1;
								
								//�������� �����ȣ �� ������������ ����
								for(int j=0; j<users.size(); j++) {
									users.get(j).outputStream.writeInt(13);
									users.get(j).outputStream.writeInt(exitUserNum);
									users.get(j).outputStream.writeInt(nextUserNum + 1);
									users.get(j).outputStream.writeBoolean(needNextTurn);
									users.get(j).outputStream.writeInt(4);	//ä�ý�ȣ
									users.get(j).outputStream.writeUTF("[�˸�] : "+ members[i].getNickname() + " ���� �����ϼ̽��ϴ�");
									
									//���������� �������� �����϶��� Ÿ�̸� ����
									if(needNextTurn) {
										users.get(j).outputStream.writeInt(20);
									}
								}									
							}
							
							//������ ������ ������¿����� ����ī��Ʈ -1
							if((members[i].getScreenNum() == 3) && (members[i].isReady())) {
								ready--;
							}
						} catch (IOException e2) {
							//e2.printStackTrace();
						}

						members[i] = null;
						System.out.println((i+1) + "��° ���� ��������");
					}
				}
				
				//Ȯ�ο����
				for(int i=0; i<members.length; i++) {
					if(members[i] != null) {
						System.out.print("�������� " + members[i].getLocation() + "��  ");
					} else {
						System.out.print("�������� Null  ");
					}
				}
				System.out.println("users.size() = " + users.size());
				System.out.println();
			}//finally-end
		}//run()-end
		
		//�������� ��ġ���� �޼ҵ�
		public int StartLoc(int fNum, int start) {
			while (true) {
				if (members[fNum] != null) {
					if(members[fNum].isRetire()) { //Ż���� ��ġ���� Ż���ߴ��� ���ο� ���� 0~5 �ݺ�
						fNum += 1;
					}
					else { //Ż���� �ƴ� ��� ��ȣ�� ã�� ��� ����
						break;
					}
					fNum %= start;
				}
				else {
					fNum +=1;
					fNum %= start;
				}
			}
			return fNum;
		}
		
		//���Ӵܾ�˻� �޼ҵ�
		public boolean HeadLaw(String fWord ,String aWord) {
			if(		(aWord.length() ==3)
					&& (dic.indexOf(aWord) != -1)  
					&& (ans.indexOf(aWord) == -1)) {
				// ���� 1. ���ڼ� 3����, ������ �ִ��� ����, ���� ����� �ߺ� ����
				
			///////////////////////////// ���� ��Ģ /////////////////////////////////////
				if (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '��') 
						&& (aWord.charAt(0) =='��' || aWord.charAt(0) == '��')) {
					return true;
				}
		//////////////////////////������Ģ ����/////////////////////////////////////
				else if(fWord.charAt(2) ==aWord.charAt(0)) {
					return true;
				}
				else return false;
				
			}
			else return false;
		}
	}
}

public class EWServer {
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		if(args.length != 1) {
			System.out.println("��Ʈ��ȣ �Է��Ͻÿ�");
			System.exit(1);
		}
		
		new ServerClass(Integer.parseInt(args[0]));
	}
}
