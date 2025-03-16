package Controller;

import java.util.ArrayList;
import java.util.Random;

import Model.Art;
import Model.DAO;
import Model.DTO_ANIMAL;
import Model.DTO_USER;

public class Controller {
	Random rd = new Random();
	DAO dao = new DAO();
	int cnt = 0;
	Art art = new Art();

	// 1. 회원가입
	public void join(DTO_USER user) {
		// 사용자 정보를 DB에 저장하고 결과를 반환받음
		cnt = dao.join(user);

		// 회원가입 성공 여부에 따라 메시지 출력
		if (cnt > 0) {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t  회원가입 성공");
			System.out.println("--------------------------------------------------------------");
		} else {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t  회원가입 실패");
			System.out.println("--------------------------------------------------------------");
		}
	}

	// 2. 로그인
	public DTO_USER login(DTO_USER user) {
		// 로그인 처리 후 해당 정보를 반환
		return dao.login(user);
	}

	// 2-1. 동물 생성
	public DTO_ANIMAL createAnimal(DTO_ANIMAL animalInfo) {
		// 동물 정보를 DB에 저장하고 결과를 반환받음
		cnt = dao.createAnimal(animalInfo);

		// 동물 생성 성공 여부를 출력
		if (cnt > 0) {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t동물이 생성되었어요!!");
			System.out.println("--------------------------------------------------------------");
			art.animalPicture(animalInfo.getType());
		} else {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t동물 생성 실패");
			System.out.println("--------------------------------------------------------------");
		}

		// 방금 생성한 동물 정보를 다시 조회하여 반환
		animalInfo = findAnimal(animalInfo);
		return animalInfo;
	}

	// 2-2. DB에 저장된 동물 정보 찾기
	public DTO_ANIMAL findAnimal(DTO_ANIMAL animal) {
		DTO_ANIMAL animalInfo = null;

		// DB에서 해당 동물 정보를 조회
		animalInfo = dao.findAnimal(animal);

		return animalInfo;
	}

	// 2-3. 상점 - 랜덤 뽑기 기능
	public DTO_ANIMAL randomDrow(DTO_ANIMAL animalInfo) {
		// 인기도가 100 미만이면 뽑기 불가능
		if (animalInfo.getPopularity() < 100) {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t       인기도가 부족하여 장난감을 뽑을 수 없습니다.");
		} else {
			// 랜덤박스 애니메이션 출력
			art.shopRandombox();

			// 랜덤으로 장난감 하나를 가져옴
			String toy = dao.getRandomToy();
			System.out.println("\t\t   뽑기에서 나온 장난감은?\n");
			System.out.println("\t\t     두구두구두~~~");

			// 동물의 장난감 정보 업데이트 및 인기도 차감
			animalInfo = dao.updateAnimalToy(toy, animalInfo);
			animalInfo.setPopularity(animalInfo.getPopularity() - 100);

			// 랜덤박스 개봉 애니메이션 및 장난감 출력
			art.randomboxOpen();
			art.picture(toy);
			System.out.println("\t\t\t>> " + toy);
		}
		return animalInfo;
	}

	// 2-4. 행동에 따른 데이터 처리
	public int doAction(DTO_ANIMAL animalInfo, int energyCost, int minPop, int maxPop, boolean isSleep) {
		int energy = 1;

		// 동물이 자는 상태라면 에너지를 10으로 초기화
		if (isSleep) {
			animalInfo.setEnergy(10);
			System.out.println("\t\t  " + animalInfo.getA_name() + "의 에너지가 10으로 초기화되었습니다.");
		} else {
			// 에너지가 부족하면 에너지 부족 메시지 출력
			if (animalInfo.getEnergy() < energyCost) {
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t 에너지가 부족해요!");
				energy = 0; // 에너지 부족 시 0 반환
			} else {
				// 에너지를 소모하고 인기도와 관람객 수를 증가
				animalInfo.setEnergy(animalInfo.getEnergy() - energyCost);
				int getPop = rd.nextInt(maxPop - minPop + 1);
				animalInfo.setPopularity(animalInfo.getPopularity() + getPop);
				animalInfo.setVisitor(animalInfo.getVisitor() + getPop);

				// DB에 변경된 정보를 업데이트
				dao.updateAction(animalInfo);
				System.out.println("--------------------------------------------------------------");
				System.out.println("\t\t\t  소모된 에너지: " + energyCost);
				System.out.println("\t\t\t  획득한 인기도: " + getPop);
				System.out.println("\t\t\t획득한 관람객 수: " + getPop);
				System.out.println("--------------------------------------------------------------");
			}
		}
		return energy; // 최종 에너지 상태 반환
	}

	// 2-5. 가방 확인
	public void checkToyBag(DTO_ANIMAL animal) {
		// 동물의 가방에서 장난감을 가져옴
		String toy = dao.toyBag(animal);
		System.out.println("--------------------------------------------------------------");
		System.out.println("뒤적뒤적 가방을 보니..?");

		// 장난감이 있으면 그 장난감을 출력, 없으면 가방이 비어있다고 출력
		if (toy != null) {
			System.out.println("장난감 목록: " + toy);
		} else {
			System.out.println("텅.....");
		}
	}

	// 3. 전체 랭킹
	public ArrayList<DTO_ANIMAL> getAnimalRanking() {
		// DB에서 동물 랭킹 정보를 조회하여 반환
		return dao.getAnimalRanking();
	}

	// 4. 회원탈퇴
	public void delete(DTO_USER user) {
		// 탈퇴 처리하고 결과를 반환받음
		cnt = dao.delete(user);

		// 회원탈퇴 성공 여부에 따라 메시지 출력
		if (cnt > 0) {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t  회원탈퇴 성공");
			System.out.println("--------------------------------------------------------------");
		} else {
			System.out.println("--------------------------------------------------------------");
			System.out.println("\t\t\t  회원탈퇴 실패");
			System.out.println("--------------------------------------------------------------");
		}
	}
}