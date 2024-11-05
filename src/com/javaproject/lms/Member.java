package com.javaproject.lms;

public class Member {
 private int memberId;
 private String name;
 private String email;

 // Constructor, Getters, and Setters
 public Member(int memberId, String name, String email) {
     this.memberId = memberId;
     this.name = name;
     this.email = email;
 }

 public int getMemberId() { return memberId; }
 public String getName() { return name; }
 public String getEmail() { return email; }
}

