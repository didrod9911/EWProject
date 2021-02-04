package ewFinal;

import java.net.Socket;

public class EWMember {
	
	//유저정보클래스
	
	private Socket s1;			//소켓
	private String nickname;	//닉네임
	private int avatarNum;		//아바타번호
	private int location;		//위치
	private int killPoint;		//킬포인트
	private boolean retire;		//탈락여부
	private boolean ready;		//레디여부
	private int screenNum;		//현재화면
	
	public EWMember() {}
	public EWMember(Socket s12, int i, int j, boolean b) {
		
	}

	public Socket getS1() {
		return s1;
	}

	public void setS1(Socket s1) {
		this.s1 = s1;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getAvatarNum() {
		return avatarNum;
	}

	public void setAvatarNum(int avatarNum) {
		this.avatarNum = avatarNum;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getKillPoint() {
		return killPoint;
	}

	public void setKillPoint(int killPoint) {
		this.killPoint = killPoint;
	}
	
	public boolean isRetire() {
		return retire;
	}
	
	public void setRetire(boolean retire) {
		this.retire = retire;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public int getScreenNum() {
		return screenNum;
	}
	
	public void setScreenNum(int screenNum) {
		this.screenNum = screenNum;
	}
}
