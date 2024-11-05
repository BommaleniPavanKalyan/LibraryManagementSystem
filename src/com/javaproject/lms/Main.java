package com.javaproject.lms;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. View Books");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Book Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Book Author: ");
                    String author = scanner.nextLine();
                    libraryManager.addBook(title, author);;
                    break;
                case 2:
                	System.out.println("Enter member name");
                	String name = scanner.nextLine();
                	System.out.println("Enter member email");
                	String email = scanner.nextLine();
                	
                	int newMemberId = libraryManager.addMember(name, email);

                	if (newMemberId != -1) {
                	    System.out.println("New member added with ID: " + newMemberId);
                	} else {
                	    System.out.println("Failed to add member.");
                	}
                	break;
                case 3:
                    libraryManager.viewBooks();;
                    break;

                case 4:
                    System.out.print("Enter Book ID to borrow: ");
                    int bookId = scanner.nextInt();
                    System.out.print("Enter Member ID: ");
                    int memberId = scanner.nextInt();
                    libraryManager.borrowBook(bookId, memberId);;
                    break;

                case 5:
                    System.out.print("Enter Book ID to return: ");
                    int returnBookId = scanner.nextInt();
                    System.out.print("Enter Member ID: ");
                    int returnMemberId = scanner.nextInt();
                    libraryManager.returnBook(returnBookId, returnMemberId);;
                    break;

                case 6:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}


