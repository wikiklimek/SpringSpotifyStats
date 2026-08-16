package models;

public class Genre {
 private String name;
 private int topNumber;

 // Getters and Setters
 public int getTopNumber() {
     return topNumber;
 }

 public void setTopNumber(int topNumber) {
     this.topNumber = topNumber;
 }
 
 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }
 
 public Genre(String name, int topNumber)
 {
	 this.name = name;
	 this.topNumber = topNumber;
 }
}
