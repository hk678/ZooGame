package Model;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

public class DAO {
   Random rd = new Random();
   Connection conn = null;
   PreparedStatement psmt = null;
   ResultSet rs = null;
   
   // .env 파일 로드
   Dotenv dotenv = Dotenv.configure().directory("./").load();  

   // DB 접속
   public void con() { 
      try {
         Class.forName("oracle.jdbc.driver.OracleDriver");
         
         // .env에서 값 가져오기
         String url = dotenv.get("DB_URL");
         String userId = dotenv.get("DB_USER");
         String userPw = dotenv.get("DB_PASSWORD");

         conn = DriverManager.getConnection(url, userId, userPw);

         if (conn == null) {
            System.out.println("접근 권한이 없습니다");
         }
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("con 오류");
      }
   }

    // DB 연결 끊기
   public void close() {
      try {
         if (rs != null) { 
            rs.close();   // 사용한 ResultSet 종료
         }
         if (psmt != null) {
            psmt.close(); // 사용한 PreparedStatement 종료
         }
         if (conn != null) {
            conn.close(); // DB 연결 종료
         }
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("close 오류");
      }
   }

   // 1. 회원가입 메서드
   public int join(DTO_USER dto) {
      int cnt = 0;
      
      try {
         con(); // DB 연결
         
         // 회원 정보 INSERT SQL 문
         String sql = "INSERT INTO TB_USER VALUES(?,?,?)";
         
         // PrepareStatement 객체 -> SQL 문장을 작성하거나 실행할 수 있는 객체
         psmt = conn.prepareStatement(sql);

         // SQL 문장의 ? 부분을 채우는 부분
         psmt.setString(1, dto.getId());
         psmt.setString(2, dto.getPw());
         psmt.setString(3, dto.getName());

         // SQL 실행 후 영향받은 행(row) 수 반환
         cnt = psmt.executeUpdate(); 

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 종료
      }
      return cnt; // 회원가입 성공 여부 반환
   }

   // 2. 로그인 메서드
   public DTO_USER login(DTO_USER dto) { 
      DTO_USER info = null; // 로그인 성공 시 사용자 정보를 저장할 객체
      
      try {
         con(); // DB 연결

         // 입력한 ID와 PW가 일치하는 회원 검색
         String sql = "SELECT * FROM TB_USER WHERE ID = ? AND PW = ?";
         psmt = conn.prepareStatement(sql);
         psmt.setString(1, dto.getId());
         psmt.setString(2, dto.getPw());

         rs = psmt.executeQuery();
         
         // 로그인 성공 시 사용자 정보 저장
         if (rs.next()) {

            String ID = rs.getString("ID");
            String PW = rs.getString("PW");

            info = new DTO_USER(ID, PW);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      }
      return info; // 로그인 성공 시 사용자 정보 반환, 실패 시 null 반환
   }


   // 2-1. 동물 생성 메서드
   public int createAnimal(DTO_ANIMAL animal) { 
      int cnt = 0; // 삽입 성공 여부 (1: 성공, 0: 실패)
      
      try {
         con(); // DB 연결
         
         // 새로운 동물 추가 (에너지 10, 인기도 0, 관람객 수 0, 장난감 NULL)
         String sql = "INSERT INTO TB_ANIMAL VALUES(?,?,?, 10, 0, 0, NULL)";
         psmt = conn.prepareStatement(sql);

         // SQL 문장의 ? 부분을 채우는 부분
         psmt.setString(1, animal.getA_name());
         psmt.setString(2, animal.getId());
         psmt.setString(3, animal.getType());

         // SQL 실행 후 결과 반환 (1: 성공, 0: 실패)
         cnt = psmt.executeUpdate();

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      }
      return cnt; // 삽입 성공 여부 반환
   }

   // 2-2. 동물 찾기 메서드
   public DTO_ANIMAL findAnimal(DTO_ANIMAL animal) { 
      DTO_ANIMAL animalInfo = null; // 찾은 동물 정보를 저장할 객체
      
      try {
         con(); // DB 연결
         
         // 해당 ID를 가진 동물 조회
         String sql = "SELECT * FROM TB_ANIMAL WHERE ID = ?";
         psmt = conn.prepareStatement(sql);
         psmt.setString(1, animal.getId());

         rs = psmt.executeQuery();
         
         // 동물이 존재하면 정보를 저장
         if (rs.next()) { // 동물이 있음
            String name = rs.getString("A_NAME");
            String id = rs.getString("ID");
            String type = rs.getString("TYPE");
            int energy = rs.getInt("ENERGY");
            int visitor = rs.getInt("VISITOR");
            int popularity = rs.getInt("POPULARITY");
            String toy = rs.getString("TOY");

            // DTO_ANIMAL 객체 생성 후 저장
            animalInfo = new DTO_ANIMAL(name, id, type, energy, visitor, popularity, toy);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      }
      return animalInfo; // 동물 정보 반환 (없으면 null 반환)
   }

   // 2-3. 상점(1) - 랜덤 장난감 선택 메서드
   public String getRandomToy() {
       String selectedToy = null; // 선택된 장난감 이름

       try {
          con(); // DB 연결

           // 상점에서 모든 장난감 목록 가져오기
           String sql = "SELECT * FROM TB_SHOP";
           psmt = conn.prepareStatement(sql);
           rs = psmt.executeQuery();

           // 장난감 목록 저장을 위한 리스트
           ArrayList<DTO_SHOP> toyList = new ArrayList<>();

           // 장난감 목록을 리스트에 저장
           while (rs.next()) {
               String toyId = rs.getString(1);
               String toyName = rs.getString(2);
               toyList.add(new DTO_SHOP(toyId, toyName));
           }

           // 리스트에서 랜덤하게 하나 선택
           if (!toyList.isEmpty()) {
               int randomIndex = rd.nextInt(toyList.size());
               selectedToy = toyList.get(randomIndex).getToy_name();
           }
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           close(); // DB 연결 해제
       }
       return selectedToy; // 랜덤 장난감 반환 (없으면 null)
   }

   // 2-3. 상점(2) - 동물의 장난감과 인기도 업데이트 메서드
   public DTO_ANIMAL updateAnimalToy(String newToy, DTO_ANIMAL animalInfo) {
       try {
           con(); // DB 연결

           // 현재 동물의 장난감 목록 조회
           String sqlSelect = "SELECT TOY FROM TB_ANIMAL WHERE ID = ?";
           psmt = conn.prepareStatement(sqlSelect);
           psmt.setString(1, animalInfo.getId());
           rs = psmt.executeQuery();

           String currentToys = "";
           if (rs.next()) {
               currentToys = rs.getString("TOY");
               if (currentToys == null) {
                   currentToys = "";
               }
           }

           // 기존 장난감 목록에 새 장난감 추가
           String updatedToys = currentToys.isEmpty() ? newToy : currentToys + ", " + newToy;

           // 동물의 장난감과 인기도 업데이트
           String sqlUpdate = "UPDATE TB_ANIMAL SET TOY = ?, POPULARITY = ? WHERE ID = ?";
           psmt = conn.prepareStatement(sqlUpdate);
           psmt.setString(1, updatedToys);
           psmt.setInt(2, animalInfo.getPopularity() - 100); // 인기도 감소
           psmt.setString(3, animalInfo.getId());

           int result = psmt.executeUpdate();
           if (result == 0) {
               System.out.println("장난감 저장 실패 (해당 ID의 동물이 없음)");
           } else {
              animalInfo.setToy(updatedToys); // DTO 객체에 변경 사항 반영
           }

       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           close(); // DB 연결 해제
       }
       return animalInfo;
   }

   // 2-4. 행동 - 에너지, 인기도, 관람객 수 업데이트 메서드
   public void updateAction(DTO_ANIMAL animalInfo) {
      try {
         con(); // DB 연결
         
         // 동물 상태 업데이트 (에너지, 인기도, 관람객 수)
         String sql = "UPDATE TB_ANIMAL SET ENERGY = ?, POPULARITY = ?, VISITOR = ? WHERE ID = ?";
         psmt = conn.prepareStatement(sql);
         psmt.setInt(1, animalInfo.getEnergy());
         psmt.setInt(2, animalInfo.getPopularity());
         psmt.setInt(3, animalInfo.getVisitor());
         psmt.setString(4, animalInfo.getId());
         
         int result = psmt.executeUpdate();
			if (result == 0) {
				System.out.println("동물 상태 업데이트 실패 (해당 ID의 동물이 없음)");
			}
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      }
   }

   // 2-5. 가방 - 보유하고 있는 장난감 확인
   public String toyBag (DTO_ANIMAL animal) {
      String toy ="";
      try {
         con(); // DB 연결
         
         String sql = "SELECT TOY FROM TB_ANIMAL WHERE ID = ?";
         psmt = conn.prepareStatement(sql);
         psmt.setString(1, animal.getId());
         
         rs = psmt.executeQuery();
         
         if (rs.next()) { // 동물이 있음
            toy = rs.getString("TOY");
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      } 
      return toy; 
   }
   
   // 3. 랭킹확인 - 관람객 수 기준으로 동물 랭킹 조회 메서드
   public ArrayList<DTO_ANIMAL> getAnimalRanking() {// 동물 랭킹 확인 메소드
      ArrayList<DTO_ANIMAL> rankingList = new ArrayList();
      
      try {
         con(); // DB 연결
         
         // 관람객 수 기준으로 내림차순 정렬하여 동물 랭킹 조회
         String sql = "SELECT ID, A_NAME, VISITOR, TYPE FROM TB_ANIMAL ORDER BY VISITOR DESC";
         psmt = conn.prepareStatement(sql);
         rs = psmt.executeQuery();
         
         // 조회된 데이터를 리스트에 저장
         while (rs.next()) {
            String id = rs.getString("ID");
            String a_name = rs.getString("A_NAME");
            int visitor = rs.getInt("VISITOR");
            String type = rs.getString("TYPE");
            
            DTO_ANIMAL animal = new DTO_ANIMAL(id, a_name, type, visitor);
            
            rankingList.add(animal);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      }
      return rankingList;
   }

   // 4. 회원탈퇴 메서드
   public int delete(DTO_USER dto) { 
      int cnt = 0;
      
      try {
         con(); // DB 연결
         
         // 입력된 ID와 비밀번호가 일치하는 회원 정보 삭제
         String sql = "DELETE FROM TB_USER WHERE ID = ? AND PW = ?";
         psmt = conn.prepareStatement(sql);
         psmt.setString(1, dto.getId());
         psmt.setString(2, dto.getPw());
         
         cnt = psmt.executeUpdate(); // 실행 결과(삭제된 행 수) 반환
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         close(); // DB 연결 해제
      }
      return cnt;
   }
}
