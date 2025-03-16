package View;

import java.util.ArrayList;
import java.util.Scanner;

import Controller.Controller;
import Model.Art;
import Model.DTO_ANIMAL;
import Model.DTO_USER;

public class ZooMain {
	static Scanner sc = new Scanner(System.in);;
	Controller controller = new Controller();;
	static Art art = new Art();;
	public static final String b = "\u001B[34m";
	public static final String RESET = "\u001B[0m";

	public static void main(String[] args) {
		// ZooMain 객체 생성
		ZooMain zoo = new ZooMain();

		// 게임을 바로 시작하기
		System.out.print("========================= 자 봐! 동물원 =========================");
		boolean isOn = true;

		art.zooImage(); // 동물원 이미지 출력

		while (isOn) {
			// 메인 메뉴를 출력
			zoo.displayMainMenu();
			int menu = sc.nextInt(); // 메뉴 선택

			switch (menu) {
			case 1:
				zoo.handleJoin();
				break;
			case 2:
				zoo.handleLogin();
				break;
			case 3:
				zoo.handleRanking();
				break;
			case 4:
				zoo.handleDelete();
				break;
			case 5:
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t게임이 종료되었습니다.");
				isOn = false;
				break;
			default:
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t그런 기능은 없어요....");
				System.out.println("--------------------------------------------------------------");
			}
		}
	}

	public void displayMainMenu() {
		// 메인 메뉴를 출력
		System.out.println(b + "==================== 사용하실 기능을 선택해주세요 ====================");
		System.out.println("\t[1]회원가입  [2]로그인  [3]랭킹확인  [4]회원탈퇴  [5]종료");
		System.out.println("--------------------------------------------------------------" + RESET);
		System.out.print("\t\t\t  메뉴 선택 >> ");
	}

	public void handleJoin() {
		// 회원가입 기능 처리
		System.out.println("=========================== 회원가입 ===========================");
		System.out.print("\t\t\t   ID : ");
		String id = sc.next(); // 사용자로부터 ID 입력 받기
		System.out.print("\t\t\t   PW : ");
		String pw = sc.next(); // 사용자로부터 PW 입력 받기
		System.out.print("\t\t\t NAME : ");
		String name = sc.next(); // 사용자로부터 NAME 입력 받기

		// DTO_USER 객체 생성 후 회원가입 처리
		DTO_USER dto = new DTO_USER(id, pw, name);
		controller.join(dto); // 회원가입 처리 요청
	}

	public void handleLogin() {
		// 로그인 화면 출력
		System.out.println("============================ 로그인 ============================");
		System.out.print("\t\t\t   ID : ");
		String id = sc.next(); // 사용자로부터 ID 입력 받기
		System.out.print("\t\t\t   PW : ");
		String pw = sc.next(); // 사용자로부터 PW 입력 받기

		// 로그인 시도
		DTO_USER dto = new DTO_USER(id, pw);
		DTO_ANIMAL animal = new DTO_ANIMAL(id); // 동물 정보 객체 생성

		DTO_USER loginResult = controller.login(dto); // 로그인 결과 확인
		DTO_ANIMAL animalInfo = null;

		// 로그인 성공 시 동물 정보를 찾고, 없으면 새로 생성
		if (loginResult != null) {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t   로그인 성공!");
			System.out.println("--------------------------------------------------------------");

			animalInfo = controller.findAnimal(animal); // 동물 정보 조회

			if (animalInfo == null) { // 동물이 없으면 새로 생성
				System.out.println("\t      등록된 동물이 없습니다. 새로운 동물을 생성해주세요.");
				System.out.println("--------------------------------------------------------------");
				animalInfo = createNewAnimal(id); // 새로운 동물 생성
			} else {
				art.animalPicture(animalInfo.getType()); // 동물 사진 출력
			}

			// 동물이 등록되었으면 게임 루프 시작
			if (animalInfo != null) {
				handleGameLoop(animalInfo);
			}
		} else {
			// 로그인 실패 시 메시지 출력
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t   로그인 실패!");
			System.out.println("--------------------------------------------------------------");
		}
	}

	public DTO_ANIMAL createNewAnimal(String id) {
		// 새 동물 선택 메뉴 출력
		System.out.println("\t\t  [1]원숭이 [2]판다 [3]호랑이 [4]타조");
		System.out.println("--------------------------------------------------------------");
		System.out.print("\t\t\t  메뉴 선택 >> ");
		int animalChoice = sc.nextInt(); // 사용자로부터 동물 선택 받기

		String type = "";

		// 선택한 동물에 따라 타입 설정
		switch (animalChoice) {
		case 1:
			type = "원숭이";
			break;
		case 2:
			type = "판다";
			break;
		case 3:
			type = "호랑이";
			break;
		case 4:
			type = "타조";
			break;
		default:
			// 잘못된 입력 처리, 기본값 원숭이로 설정
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t잘못된 입력입니다. 기본값(원숭이)으로 설정됩니다.");
			System.out.println("--------------------------------------------------------------");
			type = "원숭이";
		}
		System.out.println("\t\t\t  " + type + " 선택 완료!");
		System.out.print("\t\t   " + type + "에 이름을 지어주세요 >> ");
		String name = sc.next(); // 동물 이름 입력 받기

		// DTO_ANIMAL 객체 생성 후 반환
		DTO_ANIMAL animal = new DTO_ANIMAL(name, id, type);
		return controller.createAnimal(animal); // 새 동물 등록
	}

	public void handleGameLoop(DTO_ANIMAL animalInfo) {
		boolean isLoggedIn = true; // 로그인 상태 확인
		System.out.println("\t\t\t 오늘은 뭘 해볼까?");

		// 게임 루프 시작
		while (isLoggedIn) {
			// 현재 동물의 상태 출력
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t에너지: " + animalInfo.getEnergy() + ", 인기도: " + animalInfo.getPopularity()
					+ ", 관람객 수: " + animalInfo.getVisitor());
			System.out.println("--------------------------------------------------------------");
			displayGameMenu(); // 게임 메뉴 표시
			int select = sc.nextInt(); // 메뉴 선택 받기

			// 사용자가 선택한 메뉴에 따른 처리
			switch (select) {
			case 1:
				handleShop(animalInfo); // 상점 처리
				break;
			case 2:
				handleActions(animalInfo); // 동물 행동 처리
				break;
			case 3:
				controller.checkToyBag(animalInfo); // 가방 확인
				break;
			case 4:
				// 로그아웃 처리
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t 로그아웃되었습니다.");
				System.out.println("--------------------------------------------------------------");
				isLoggedIn = false; // 로그인 상태 종료
				break;
			default:
				// 잘못된 메뉴 입력 처리
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t어떤 걸 하자고....?");
			}
		}
	}

	// 게임 메뉴 화면 출력
	public void displayGameMenu() {
		// 게임에서 선택할 수 있는 메뉴 항목들 출력
		System.out.println(b + " [1]상점 들어가기   [2]행동 고르기   [3]가방 보기   [4]오늘은 여기까지만" + RESET);
		System.out.println("--------------------------------------------------------------");
		System.out.print("\t\t\t  메뉴 선택 >> ");
	}

	// 상점 관련 처리
	public void handleShop(DTO_ANIMAL animalInfo) {
		// 상점 관련 이미지 출력
		art.shopImage();
		System.out.println("\t\t\t랜덤 장난감 뽑기!!!!");
		art.shopRandom();
		// 장난감 뽑기 안내 메시지 출력
		System.out.println("\t      인기도 100을 사용하여 뽑기를 진행하시겠습니까?");
		System.out.println("--------------------------------------------------------------");
		System.out.println("\t\t\t[1]뽑기! [2]안뽑을거야!");
		System.out.println("--------------------------------------------------------------");
		System.out.print("\t\t\t  메뉴 선택 >> ");
		int drow = sc.nextInt(); // 메뉴 선택 받기

		// 사용자가 뽑기를 선택하면 뽑기 처리
		if (drow == 1) {
			animalInfo = controller.randomDrow(animalInfo); // 랜덤 뽑기 함수 호출
		}
	}

	// 동물의 행동 처리
	public void handleActions(DTO_ANIMAL animalInfo) {
		boolean keepActing = true; // 행동을 계속 진행할지 여부

		// 행동 루프 시작
		while (keepActing) {
			displayActionMenu(); // 행동 선택 메뉴 출력
			int behaviorChoice = sc.nextInt(); // 사용자가 선택한 행동 받기

			// 선택한 행동을 수행하고, 행동을 계속할지 결정
			keepActing = behavior(behaviorChoice, animalInfo);
		}
	}

	// 행동에 따른 처리
	public boolean behavior(int behaviorChoice, DTO_ANIMAL animalInfo) {
		int energy; // 행동에 따른 에너지 소비를 추적하기 위한 변수

		switch (behaviorChoice) {
		// 음식을 먹는 행동
		case 1:
			energy = controller.doAction(animalInfo, 2, 0, 10, false); // 에너지 소비 계산
			if (energy != 0) {
				// 동물 종류에 따라 다른 이미지를 출력
				if (animalInfo.getType().equals("원숭이")) {
					art.eatingMonkey();
				} else if (animalInfo.getType().equals("판다")) {
					art.eatingPanda();
				} else if (animalInfo.getType().equals("호랑이")) {
					art.eatingTiger();
				} else if (animalInfo.getType().equals("타조")) {
					art.eatingOstrich();
				}
				// 음식 먹는 메시지 출력
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t    가장 좋아하는 음식을 먹어요! 얌얌얌");
			}
			break;
		// 산책하는 행동
		case 2:
			energy = controller.doAction(animalInfo, 2, 0, 10, false);
			if (energy != 0) {
				art.walking(); // 산책 이미지 출력
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t    날씨가 좋아요! 산책을 해요 >-<");
			}
			break;
		// 관람객에게 인사하는 행동
		case 3:
			energy = controller.doAction(animalInfo, 2, 0, 10, false);
			if (energy != 0) {
				art.hello(); // 인사 이미지 출력
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t 동물원을 찾아 와준 관람객들에게 인사를 해요");
			}
			break;
		// 애교 부리는 행동
		case 4:
			energy = controller.doAction(animalInfo, 3, 0, 30, false);
			if (energy != 0) {
				art.charming(); // 애교 이미지 출력
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t   " + animalInfo.getA_name() + "의 애교부리기!!");
			}
			break;
		// 개인기를 보여주는 행동
		case 5:
			energy = controller.doAction(animalInfo, 4, 0, 60, false);
			if (energy != 0) {
				// 동물 종류에 따라 다른 이미지를 출력
				if (animalInfo.getType().equals("원숭이")) {
					art.talentMonkey();
				} else if (animalInfo.getType().equals("판다")) {
					art.talentPanda();
				} else if (animalInfo.getType().equals("호랑이")) {
					art.talentTiger();
				} else if (animalInfo.getType().equals("타조")) {
					art.talentOstrich();
				}
				// 개인기 메시지 출력
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t" + animalInfo.getA_name() + "의 개인기를 보여주었어요!!");
			}
			break;
		// 잠자는 행동
		case 6:
			System.out.println("--------------------------------------------------------------");
			art.sleep(); // 잠자는 이미지 출력
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t쿨쿨... 잠을 자요");
			System.out.println("--------------------------------------------------------------");
			controller.doAction(animalInfo, 10, 0, 0, true); // 에너지 회복 처리
			break;
		// 행동 종료 (게임을 종료하지 않도록)
		case 7:
			return false; // 행동을 종료하고 루프를 빠져나감
		// 잘못된 입력 처리
		default:
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t  잘못된 입력입니다.");
		}

		// 현재 상태 출력 (에너지, 인기도, 관람객 수)
		System.out.println("--------------------------------------------------------------");
		System.out.println("\t\t에너지: " + animalInfo.getEnergy() + ", 인기도: " + animalInfo.getPopularity() + ", 관람객 수: "
				+ animalInfo.getVisitor());

		return true; // 계속해서 행동을 선택할 수 있도록 true 반환
	}

	// 게임 내 행동 메뉴를 출력하는 메서드
	public void displayActionMenu() {
		System.out.println("--------------------------------------------------------------");
		System.out.println("\t\t   [1]밥먹기\t(에너지가 2 만큼 필요해요)");
		System.out.println("\t\t   [2]돌아다니기\t(에너지가 2 만큼 필요해요)");
		System.out.println("\t\t   [3]인사하기\t(에너지가 2 만큼 필요해요)");
		System.out.println("\t\t   [4]애교부리기\t(에너지가 3 만큼 필요해요)");
		System.out.println("\t\t   [5]개인기하기\t(에너지가 4 만큼 필요해요)");
		System.out.println("\t\t   [6]잠자기\t(에너지가 초기화돼요)");
		System.out.println("\t\t   [7]돌아가기");
		System.out.println("--------------------------------------------------------------");
		System.out.print("\t\t\t  메뉴 선택 >> ");
	}

	// 랭킹을 출력하는 메서드
	public void handleRanking() {
		System.out.println("=========================== 랭킹확인 ===========================");
		art.rankingImage(); // 랭킹 관련 이미지를 출력하는 메서드 호출

		// 랭킹 정보를 가져오기
		ArrayList<DTO_ANIMAL> list = controller.getAnimalRanking();

		int rank = 1; // 랭킹 순위를 추적하기 위한 변수
		System.out.println("\t     순위\t아이디\t동물명\t동물종류\t관람객 수");
		System.out.println("--------------------------------------------------------------");

		// 랭킹 정보를 출력
		for (DTO_ANIMAL animal : list) {
			System.out.println("\t     " + rank + "위" + "\t" + animal.getId() + "\t" + animal.getA_name() + "\t"
					+ animal.getType() + "\t" + animal.getVisitor() + "\t");
			rank++; // 순위 증가
		}
		System.out.println();
	}

	// 회원 탈퇴를 처리하는 메서드
	public void handleDelete() {
		System.out.println("=========================== 회원탈퇴 ===========================");
		System.out.print("\t\t\t   ID : ");
		String id = sc.next(); // 사용자 입력을 통해 ID 받기
		System.out.print("\t\t\t   PW : ");
		String pw = sc.next(); // 사용자 입력을 통해 PW 받기

		// 사용자 정보를 담은 DTO_USER 객체 생성
		DTO_USER dto = new DTO_USER(id, pw);
		// 컨트롤러를 통해 회원 탈퇴 처리
		controller.delete(dto);
	}

}