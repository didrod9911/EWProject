package ewFinal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerClass {
	
	private ArrayList<UserThread> users = new ArrayList<UserThread>();	//접속유저쓰레드
	private EWMember[] members = {null, null, null, null, null, null};	//접속유저정보배열
	private ArrayList<String> dic = new EWWords().dic;					//단어모음 ArrayList
	private ArrayList<String> ans = new ArrayList<String>();			//사용한 단어 ArrayList
	
	//게임용 변수
	private int start = 0; 												//처음 시작 인원
	private int ready = 0; 												//전원 레디완료 여부 확인
	private String fWord = dic.get((int)(Math.random()*dic.size()));	//처음 제시어
	private String fWordK = fWord;										//처음제시어 계속 기억할 변수
	private int fNum = 1; 												//시작하는 사람 번호(첫시작은 항상 1번부터)
	private int okay = 0; 												//정답자 위치
	private int temp = 0; 												//임시저장용
	private int lastOne = 0; 											//최후의 1인 판독기
	private int maxTrun = 50;											//최종턴 = 50턴
	private int nowTurn = 1;											//현재 턴인 유저의 위치
	
	public ServerClass() {}
	public ServerClass(int portNo) throws IOException {
		Socket s1 = null;
		ServerSocket ss1 = new ServerSocket(portNo);
		
		System.out.println("서버가동중...");
		
		while(true) {
			s1 = ss1.accept();
			DataOutputStream outputStream = new DataOutputStream(s1.getOutputStream());
			
			System.out.println("접속IP\t: " + s1.getLocalAddress());
			System.out.println("접속포트\t: " + s1.getLocalPort());
			
			//정원 = 6명, 현재 5명 이하면 접속OK
			if(users.size() <= 5) {
				UserThread user = new UserThread(s1);	//쓰레드 생성 후
				outputStream.writeInt(0);				//접속가능신호
				
				//접속과 동시에 유저정보 기본값으로 세팅
				for(int i=0; i<members.length; i++) {
					if(members[i] == null) {
						members[i] = new EWMember();	//객체생성
						members[i].setS1(s1);			//유저소켓
						members[i].setNickname("");		//닉네임
						members[i].setAvatarNum(-1);	//아바타번호
						members[i].setLocation(i+1);	//위치
						members[i].setKillPoint(0);		//킬포인트
						members[i].setRetire(false);	//탈락상태
						members[i].setReady(false);		//레디상태
						members[i].setScreenNum(2);		//유저의 화면번호
						
						break;
					}
				}
				
				//접속유저 확인용
				for(int i=0; i<members.length; i++) {
					if(members[i] != null) {
						System.out.print("유저있음 " + members[i].getLocation() + "번  ");
					} else {
						System.out.print("유저없음 Null  ");
					}
				}
				
				user.start();							//스타트
				users.add(user);						//인원체크용 ArrayList에 추가
			}else {
				outputStream.writeInt(1);				//접속불가신호
			}
			
			System.out.println("현재 접속자수 : " + users.size() + "명");
		}//while(true)-end
	}//ServerClass생성자-end
	
	//접속 유저 전용 쓰레드
	class UserThread extends Thread{
		Socket socket;
		DataInputStream inputStream;
		DataOutputStream outputStream;
		int signal;		//신호
		String aWord;	//클라이언트에서 입력한 게임단어 저장용
		
		public UserThread() {}
		public UserThread(Socket s1) throws IOException {
			socket = s1;
			inputStream = new DataInputStream(s1.getInputStream());
			outputStream = new DataOutputStream(s1.getOutputStream());
		}
		
		public void run() {			
			try {
				while(inputStream != null) {
					//클라이언트에서 보낸 신호정보 저장
					signal = inputStream.readInt();
					
					//닉네임 중복체크 신호
					if(signal == 0) {
						System.out.println("중복체크신호");
						String nickname = inputStream.readUTF();
						Boolean overlap = false;
						
						//중복닉네임 있으면 중복처리
						for (int i=0; i < members.length; i++) {
							if(members[i] != null) {
								if(nickname.equals(members[i].getNickname())) {
									overlap = true;
									break;
								}																	
							}
						}
						
						//중복확인신호
						outputStream.writeInt(100);
						outputStream.writeBoolean(overlap);
					}//0번신호-end
					
					
					// 클라이언트가 닉네임/아바타 선택후 '설정완료'클릭시
					if(signal == 1) {
						System.out.println("1번신호 들어옴");
						String nickname = inputStream.readUTF();
						int avatarNum = inputStream.readInt();
						
						System.out.println("닉네임 : " + nickname);
						System.out.println("아바타번호 : " + avatarNum);
						
						//멤버정보객체 배열에 닉네임과 아바타번호 세팅
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) {
								//소켓으로 해당유저 검색
								if(members[i].getS1().equals(socket)) {
									members[i].setNickname(nickname);
									members[i].setAvatarNum(avatarNum);
									int location = members[i].getLocation();
									boolean ready = members[i].isReady();
									members[i].setScreenNum(3);	//화면번호, 3번화면 : 레디화면
									
									System.out.println((location) + "번 유저");
									
									//모든 유저에게 들어온 유저의 닉네임/아바타정보/위치 전송
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
						
						//레디창에 들어온 유저들에게 입장한 유저 알림메시지
						for(int i=0; i<members.length; i++) {
							//해당위치에 유저가 존재하고 아바타번호가 -1이 아니면(아바타번호는 1~8)
							//아바타번호가 -1이면 아직 설정창에서 아바타를 설정하지 않아서 레디창에 없다는 의미
							if(members[i] != null && (members[i].getAvatarNum() != -1)) {
								Socket s = members[i].getS1();
								
								for(int j=0; j<users.size(); j++) {
									if(s.equals(users.get(j).socket)) {
										users.get(j).outputStream.writeInt(4);
										users.get(j).outputStream.writeUTF("[" + nickname + "] 님이 입장하셨습니다");
									}
								}
							}
						}
					}//1번신호-end
					
					//레디버튼 클릭시
					if(signal == 2) {
						int loc = inputStream.readInt();
						
						ready += 1; //레디 완료시 마다 +1
						members[loc-1].setReady(true);
						System.out.println("2번신호 들어옴");
						System.out.println("위치번호 : " + loc);
						
						////////////->3번 신호: 레디 완료 신호///////////
						for (int i=0; i < users.size();i++) {
							System.out.println("레디 완료 신호 보냄: "+(i+1)+"번");
							users.get(i).outputStream.writeInt(3);//다른 유저 레디 완료 신호
							users.get(i).outputStream.writeInt(loc);//레디완료 유저 번호
						}
						System.out.println("레디인원수 : " + ready);
						if (ready >= users.size() && (ready != 1)) { //레디완료 카운트 == 쓰레드 크기 (들어온 사람 수)
							
							for(int i=(members.length-1); i >= 0; i--) {
								if(members[i] != null) {
									start = i+1;
									break;
								}
							}
							
							System.out.println("게임시작");//위 조건 충족 시 게임시작
							
							//////////////-> 5번 신호: 게임 시작 신호//////////////
							for (int i =0 ; i<users.size();i++) {//(참가자 전원에게 보냄)
								users.get(i).outputStream.writeInt(5); //게임 시작 신호
								users.get(i).outputStream.writeUTF(fWord); // 첫단어 배포
								users.get(i).outputStream.writeInt(fNum); //처음 시작하는 사람 배포
								
								users.get(i).outputStream.writeInt(20);	//게임카운트용 progressbar시작신호
							}
							
							ready = 0; //레디 완료 카운트 초기화
						}
					}//2번신호-end
					
					//클라이언트가 채팅 입력시
					if(signal == 3) {
						System.out.println("3번신호 들어옴 채팅입력");
						String nickname = inputStream.readUTF();
						String chat = inputStream.readUTF();
						
						System.out.println("닉네임 : " + nickname);
						System.out.println("채팅내용 : " + chat);
						
						//우선 귓속말체크
						//귓속말형식 : '/w [닉네임] [할말]'
						if(chat.charAt(0) == '/') {
							try {
								if( (chat.charAt(1) == 'w') || (chat.charAt(1) == 'W') || (chat.charAt(1) == 'ㅈ')) {
									//여기까지 오면 일단 /w or /W or /ㅈ (귓속말명령어)은 입력
									//공백을 기준으로 나누기
									String[] arrChar = chat.split(" ");
									
									//길이가 3미만(1 or 2) = 닉네임도 안쓰거나 닉네임은 썼지만 할말을 안쓴경우
									if(arrChar.length < 3) {
										//강제 예외발생
										throw new ArrayIndexOutOfBoundsException();
									}
									
									//여기까지 오면 제대로 된 귓속말
									String targetNickname = arrChar[1];			//대상 닉네임저장
									String whisper = "";						//보낼 채팅 초기화
									
									for(int i=2; i<arrChar.length; i++) {		//배열로 나눠진 채팅 합치기
										whisper += " " + arrChar[i];
									}
									
									for(int i=0; i<members.length; i++) {
										//귓속말 보낼 유저 탐색
										if(members[i] != null) {
											if(targetNickname.equals(members[i].getNickname())) {
												Socket targetSocket = members[i].getS1();
												
												//대상 유저 찾아서 채팅내용 전송
												for(int j=0; j<users.size(); j++) {
													if(targetSocket.equals(users.get(j).socket)) {
														users.get(j).outputStream.writeInt(4);
														users.get(j).outputStream.writeUTF(nickname + "(귓속말) ===> " + whisper);

														break;	//내부 for문 탈출
													}												
												}
												
												break;	//바깥 for문 탈출
											}
										}
									}
								} else {
									System.out.println("귓속말 형식 틀림");
									throw new StringIndexOutOfBoundsException();
								}	
							} catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
								//귓속말 발신인에게 형식 메시지 보내기
								outputStream.writeInt(4);
								outputStream.writeUTF("[알림] ==> 귓속말은 \"/w 닉네임 채팅내용\" "
														+ "또는 \"/W 닉네임 채팅내용\" "
														+ "또는 \"/ㅈ 닉네임 채팅내용\"입니다");
							}							
						} else {
							//일반채팅
							System.out.println("일반채팅");
							//전원에게 채팅전송
							for(int i=0; i<users.size(); i++) {
								users.get(i).outputStream.writeInt(4);
								users.get(i).outputStream.writeUTF(nickname + " ===> " + chat);
							}
						}						
					}//3번신호-end
					
					//게임진행중 게임단어 입력시
					if(signal == 4) {
						aWord = inputStream.readUTF();		//유저가 입력한 단어
						temp = inputStream.readInt();		//입력한 유저위치
						System.out.println("4번신호 들어옴");
						System.out.println("입력단어 : " + aWord);
						
						if (HeadLaw(fWord, aWord))  { //정답일 경우 조건	
							ans.add(aWord);
							okay = temp; //답안 맞춘 사람의 순서 기록
							// 중복 답안을 피하기 위해 정답 arrayList에 들어감
							fWord = aWord;
							// 끝말잇기 이어가기 위해 첫 단어 변수에다 넣음
							
							for(int i=(members.length-1); i >= 0; i--) {
								if(members[i] != null) {
									start = i+1;
									break;
								}
							}
							
							////////////////->8번 신호 보내기 : 맞췄을 때 정답 신호//////////////
							maxTrun--;
							
							for (int i=0;i < users.size();i++) {
								users.get(i).outputStream.writeInt(8);
								//정답시 8번 신호(참가자 전원에게 보냄)
								users.get(i).outputStream.writeUTF(fWord);
								//다음 단어 fWord
								
								nowTurn = StartLoc((okay % start), start) + 1;
								users.get(i).outputStream.writeInt(nowTurn);
								//다음 시작 유저 위치 보낼것

								users.get(i).outputStream.writeInt(20);
								//프로그래스 바 진행 신호
								
								if(maxTrun == 0) {
									int userCnt = 0;								
									//남아있는 유저수
									for(int j=0; j<members.length; j++) {
										if((members[j] != null) && (!members[j].isRetire()))
											userCnt++;
									}
									
									//접속자 전원에게 생존자 정보 전송
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
									
									//레디신호 초기화
									ready = 0;
								}
							}
						}//정답일경우 if문-end
						////////////////->7번 신호 보내기 : 오답 신호////////////
						else {
							for(int i=0 ; i != users.size(); i++) {
								users.get(i).outputStream.writeInt(7);
								//오답시 7번 신호(참가자 전원)
							}
						}
					}//4번신호-end
					
					//유저 탈락시
					if(signal == 5) {
						System.out.println("5번신호 들어옴 탈락신호");
						int ord = inputStream.readInt();	//탈락자 번호(위치)
						EWMember loc = members[ord -1];		//해당 탈락자의 정보
						
						if(!fWord.equals(fWordK)) { //첫 제시어와 제출한 답안이 다를 경우
													//컴퓨터가 랜덤으로 제시한 단어에 탈락했을경우 kill점수는 올라가지 않음
							//이전 사람 킬점수 추가 로직
							//okay : 바로 이전 정답으로 턴을 넘겼던 유저위치
							int score = members[okay-1].getKillPoint();
							members[okay-1].setKillPoint(score+1);
						}

						lastOne += 1; //탈락자 명수 +1 //최후의 1인 계산때 사용
						
						fWord = dic.get((int)(Math.random()*dic.size())); //첫 제시어 초기화
						fWordK= fWord;//킬 추가 판독기
						
						System.out.println("위치번호 : " + loc.getLocation());
						
						fNum = loc.getLocation();
						members[ord-1].setRetire(true);
						loc.setRetire(true); //탈락자 탈락 처리
						
						//다음유저위치 구하는 로직에 필요한 변수 세팅작업(start)
						for(int i=(members.length-1); i >= 0; i--) {
							if(members[i] != null) {
								start = i+1;
								break;
							}
						}
						
						fNum = StartLoc((fNum % start), start);	//다음턴 위치 구하기
						nowTurn = fNum + 1;
						
						maxTrun--;
						
						if (lastOne == users.size()-1) {
							////////////->10번 신호: 우승자 정보 신호////////////	
							for(int i=0; i<members.length; i++) {
								if((members[i] != null) && (!members[i].isRetire())) {
									for(int j=0; j<users.size(); j++) {
										users.get(j).outputStream.writeInt(11);
										users.get(j).outputStream.writeUTF(members[i].getNickname());
										users.get(j).outputStream.writeInt(members[i].getAvatarNum());
										users.get(j).outputStream.writeInt(members[i].getLocation());	//우승자 위치
										users.get(j).outputStream.writeInt(members[i].getKillPoint());
										users.get(j).outputStream.writeInt(ord);	//탈락자 위치
										users.get(j).outputStream.writeInt(okay);	//공격자 위치
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
						/////////////->9번 신호 보내기 : 탈락자 정보 신호/////////////
						else {
							for (int i=0 ;i < users.size(); i++) {
								users.get(i).outputStream.writeInt(9);					//9번신호: 유저 탈락 신호
								users.get(i).outputStream.writeInt(loc.getLocation());	//탈락 유저 번호
								users.get(i).outputStream.writeInt(fNum+1);				//다음위치
								users.get(i).outputStream.writeInt(okay);				//킬한사람위치
								users.get(i).outputStream.writeUTF(fWord);				//새로운 첫 제시어
								
								users.get(i).outputStream.writeInt(20);					//프로그래스 바 진행 신호
							}
							
							if(maxTrun == 0) {
								int userCnt = 0;
								
								//남아있는 유저수 카운트
								for(int i=0; i<members.length; i++) {
									if((members[i] != null) && (!members[i].isRetire()))
										userCnt++;
								}
								
								//접속자 전원에게 생존자 정보 전송
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
					}//5번신호-end
					
					//전체유저정보요청
					if(signal == 30) {
						outputStream.writeInt(30);
						
						int userCnt = users.size();		//현재 접속한 유저수
						outputStream.writeInt(userCnt);	//접속유저수 전송
						
						for(int i=0; i<members.length; i++) {
							if(members[i] != null) { //위치 닉네임 아바타 레디
								outputStream.writeInt(members[i].getLocation());	//위치
								outputStream.writeUTF(members[i].getNickname());	//닉네임
								outputStream.writeInt(members[i].getAvatarNum());	//아바타번호
								outputStream.writeBoolean(members[i].isReady());	//레디여부
							}
						}
					}
					
					//클라이언트 자신의정보 요청신호
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
			//퇴장처리
			} finally {
				//Thread저장해놓은 ArrayList에서 해당유저 삭제
				for(int i=0; i<users.size(); i++) {
					if(socket.equals(users.get(i).socket))
						users.remove(i);											
				}
				
				//다음차례 위치찾기용 변수세팅
				int size = 0;
				for(int i=(members.length-1); i >= 0; i--) {
					if(members[i] != null) {
						size = i+1;
						break;
					}
				}
				
				//유저정보객체 배열에서 해당유저 객체 null로
				for(int i=0; i<members.length; i++) {
					if((members[i] != null) && (socket.equals(members[i].getS1()))) {
						try {
							//유저의 화면이 2번(아바타세팅화면)이 아니면(아바타 세팅화면은 채팅창이 없으므로 보낼필요X)
							if(members[i].getScreenNum() != 2) {
								int exitUserNum = members[i].getLocation();				//퇴장유저위치
								int nextUserNum = StartLoc((exitUserNum % size), size);	//다음유저위치
								boolean needNextTurn = false;							//퇴장한 유저가 당시에 턴이었을 경우 true
								
								//퇴장한 유저가 지금 턴인 유저라면 true
								if(members[i].getLocation() == nowTurn)
									needNextTurn = true;
								
								nowTurn = nextUserNum + 1;
								
								//전원에게 퇴장신호 및 퇴장유저정보 전송
								for(int j=0; j<users.size(); j++) {
									users.get(j).outputStream.writeInt(13);
									users.get(j).outputStream.writeInt(exitUserNum);
									users.get(j).outputStream.writeInt(nextUserNum + 1);
									users.get(j).outputStream.writeBoolean(needNextTurn);
									users.get(j).outputStream.writeInt(4);	//채팅신호
									users.get(j).outputStream.writeUTF("[알림] : "+ members[i].getNickname() + " 님이 퇴장하셨습니다");
									
									//퇴장유저가 지금턴의 유저일때만 타이머 리셋
									if(needNextTurn) {
										users.get(j).outputStream.writeInt(20);
									}
								}									
							}
							
							//퇴장한 유저가 레디상태였으면 레디카운트 -1
							if((members[i].getScreenNum() == 3) && (members[i].isReady())) {
								ready--;
							}
						} catch (IOException e2) {
							//e2.printStackTrace();
						}

						members[i] = null;
						System.out.println((i+1) + "번째 유저 접속종료");
					}
				}
				
				//확인용로직
				for(int i=0; i<members.length; i++) {
					if(members[i] != null) {
						System.out.print("유저있음 " + members[i].getLocation() + "번  ");
					} else {
						System.out.print("유저없음 Null  ");
					}
				}
				System.out.println("users.size() = " + users.size());
				System.out.println();
			}//finally-end
		}//run()-end
		
		//다음차례 위치리턴 메소드
		public int StartLoc(int fNum, int start) {
			while (true) {
				if (members[fNum] != null) {
					if(members[fNum].isRetire()) { //탈락자 위치부터 탈락했는지 여부에 따라 0~5 반복
						fNum += 1;
					}
					else { //탈락자 아닌 사람 번호를 찾을 경우 멈춤
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
		
		//게임단어검사 메소드
		public boolean HeadLaw(String fWord ,String aWord) {
			if(		(aWord.length() ==3)
					&& (dic.indexOf(aWord) != -1)  
					&& (ans.indexOf(aWord) == -1)) {
				// 조건 1. 글자수 3글자, 사전에 있는지 여부, 기존 정답과 중복 여부
				
			///////////////////////////// 두음 법칙 /////////////////////////////////////
				if (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '녀') 
						&& (aWord.charAt(0) =='녀' || aWord.charAt(0) == '여')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '뇨') 
						&& (aWord.charAt(0) =='뇨' || aWord.charAt(0) == '요')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '뉴') 
						&& (aWord.charAt(0) =='뉴' || aWord.charAt(0) == '유')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '니') 
						&& (aWord.charAt(0) =='니' || aWord.charAt(0) == '이')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '랴') 
						&& (aWord.charAt(0) =='랴' || aWord.charAt(0) == '야')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '려') 
						&& (aWord.charAt(0) =='려' || aWord.charAt(0) == '여')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '례') 
						&& (aWord.charAt(0) =='례' || aWord.charAt(0) == '예')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '료') 
						&& (aWord.charAt(0) =='료' || aWord.charAt(0) == '요')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '류') 
						&& (aWord.charAt(0) =='류' || aWord.charAt(0) == '유')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '리') 
						&& (aWord.charAt(0) =='리' || aWord.charAt(0) == '이')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '라') 
						&& (aWord.charAt(0) =='라' || aWord.charAt(0) == '나')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '래') 
						&& (aWord.charAt(0) =='래' || aWord.charAt(0) == '내')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '로') 
						&& (aWord.charAt(0) =='로' || aWord.charAt(0) == '노')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '뢰') 
						&& (aWord.charAt(0) =='뢰' || aWord.charAt(0) == '뇌')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '루') 
						&& (aWord.charAt(0) =='루' || aWord.charAt(0) == '누')) {
					return true;
				}
				else if  (		(aWord.length() == 3) 
						&& (fWord.charAt(2) == '르') 
						&& (aWord.charAt(0) =='르' || aWord.charAt(0) == '느')) {
					return true;
				}
		//////////////////////////두음법칙 제외/////////////////////////////////////
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
			System.out.println("포트번호 입력하시오");
			System.exit(1);
		}
		
		new ServerClass(Integer.parseInt(args[0]));
	}
}
