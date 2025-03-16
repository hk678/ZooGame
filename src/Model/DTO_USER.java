package Model;

public class DTO_USER {

   private String id;
   private String pw;
   private String name;

   // 회원가입용 생성자
   public DTO_USER(String id, String pw, String name) {
      this.id = id;
      this.pw = pw;
      this.name = name;
   }

   // 로그인용 생성자
   public DTO_USER(String id, String pw) {
      this.id = id;
      this.pw = pw;
   }

   // getter, setter
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getPw() {
      return pw;
   }

   public void setPw(String pw) {
      this.pw = pw;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

}