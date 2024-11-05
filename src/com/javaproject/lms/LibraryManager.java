package com.javaproject.lms;

import java.sql.*;
import java.time.LocalDate;

public class LibraryManager {

    public void addBook(String title, String author) {
        String sql = "INSERT INTO books (title, author, is_available) VALUES (?, ?, true)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Available Books:");
            while (rs.next()) {
                System.out.printf("ID: %d, Title: %s, Author: %s, Available: %s\n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getBoolean("is_available") ? "Yes" : "No");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isMemberValid(int memberId) {
        String checkMember = "SELECT member_id FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkMember)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if member exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void borrowBook(int bookId, int memberId) {
        if (!isMemberValid(memberId)) {
            System.out.println("Member ID " + memberId + " does not exist.");
            return;
        }

        String checkAvailability = "SELECT is_available FROM books WHERE book_id = ?";
        String borrowBook = "UPDATE books SET is_available = false WHERE book_id = ?";
        String insertBorrow = "INSERT INTO borrowed_books (book_id, member_id, borrow_date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkAvailability);
             PreparedStatement borrowStmt = conn.prepareStatement(borrowBook);
             PreparedStatement insertStmt = conn.prepareStatement(insertBorrow)) {

            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getBoolean("is_available")) {
                conn.setAutoCommit(false);

                // Mark book as borrowed
                borrowStmt.setInt(1, bookId);
                borrowStmt.executeUpdate();

                // Record borrow information
                insertStmt.setInt(1, bookId);
                insertStmt.setInt(2, memberId);
                insertStmt.setDate(3, Date.valueOf(LocalDate.now()));
                insertStmt.executeUpdate();

                conn.commit();
                System.out.println("Book borrowed successfully!");
            } else {
                System.out.println("Book is not available for borrowing.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void returnBook(int bookId, int memberId) {
        String updateAvailability = "UPDATE books SET is_available = true WHERE book_id = ?";
        String updateReturnDate = "UPDATE borrowed_books SET return_date = ? WHERE book_id = ? AND member_id = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement availStmt = conn.prepareStatement(updateAvailability);
             PreparedStatement returnStmt = conn.prepareStatement(updateReturnDate)) {

            // Set the return date
            returnStmt.setDate(1, Date.valueOf(LocalDate.now()));
            returnStmt.setInt(2, bookId);
            returnStmt.setInt(3, memberId);
            int rowsAffected = returnStmt.executeUpdate();

            if (rowsAffected > 0) {
                // Mark book as available
                availStmt.setInt(1, bookId);
                availStmt.executeUpdate();
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("No record found for this book and member combination.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int addMember(String memberName, String MemberEmail) {
        String addMemberQuery = "INSERT INTO members (name, email) VALUES (?, ?)";
        int memberId = -1;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(addMemberQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, memberName);
            stmt.setString(2, MemberEmail);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the auto-generated member_id
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        memberId = generatedKeys.getInt(1);
                        System.out.println("Member added successfully with ID: " + memberId);
                    }
                }
            } else {
                System.out.println("Failed to add member.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return memberId; // Return the new member ID or -1 if unsuccessful
    }



}

