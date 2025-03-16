package Model;

public class DTO_ANIMAL {

   private String a_name;
   private String id;
   private String type;
   private int energy;
   private int visitor;
   private int popularity;
   private String toy;

   // 생성자
   public DTO_ANIMAL(String a_name, String id, String type, int energy, int visitor, int popularity, String toy) {
      this.a_name = a_name;
      this.id = id;
      this.type = type;
      this.energy = energy;
      this.visitor = visitor;
      this.popularity = popularity;
      this.toy = toy;
   }

   public DTO_ANIMAL(String a_name, String id, String type) {
      this.a_name = a_name;
      this.id = id;
      this.type = type;
   }

   public DTO_ANIMAL(String id, String a_name, String type, int visitor) {
     this.id = id;
      this.a_name = a_name;
      this.type = type;
      this.visitor = visitor;
   }

   public DTO_ANIMAL(String id) {
      this.id = id;
   }
   
   // getter, setter
   public String getA_name() {
      return a_name;
   }

   public void setA_name(String a_name) {
      this.a_name = a_name;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public int getEnergy() {
      return energy;
   }

   public void setEnergy(int energy) {
      this.energy = energy;
   }

   public int getVisitor() {
      return visitor;
   }

   public void setVisitor(int visitor) {
      this.visitor = visitor;
   }

   public int getPopularity() {
      return popularity;
   }

   public void setPopularity(int popularity) {
      this.popularity = popularity;
   }

   public String getToy() {
      return toy;
   }

   public void setToy(String toy) {
      this.toy = toy;
   }

}
