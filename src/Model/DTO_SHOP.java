package Model;

import java.util.Random;

public class DTO_SHOP {

   private String toy_id;
   private String toy_name;
   static Random rd = new Random();

   // 생성자
   public DTO_SHOP(String toy_id, String toy_name) {
      this.toy_id = toy_id;
      this.toy_name = toy_name;
   }

   // getter, setter
   public String getToy_id() {
      return toy_id;
   }

   public void setToy_id(String toy_id) {
      this.toy_id = toy_id;
   }

   public String getToy_name() {
      return toy_name;
   }

   public void setToy_name(String toy_name) {
      this.toy_name = toy_name;
   }

}