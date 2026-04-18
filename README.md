# 📚 Library Management System

A Java-based Library Management System with MySQL database.

---

## 👩‍💻 Developed By
**Diksha Dhakne**  
Mini Project — Java + MySQL

---

## 📌 Description
This project is a Library Management System built using Java Swing for the UI and MySQL for the database. It allows librarians to manage books, students, issue and return books with automatic fine calculation.

---

## ✨ Features
- ➕ Add new books to the library
- 📖 View all available books
- 📤 Issue books to students (max 3 books per student)
- 🔄 Return books with automatic fine calculation
- 💰 Fine system — ₹10/day after 7 days
- 📊 Track which student issued which book
- 📅 Issue date and return date tracking
- 🔽 Dropdown selection for students and books
- 🌙 Modern dark theme UI

---

## 🛠️ Technologies Used
| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Java Swing | GUI / UI design |
| MySQL | Database |
| JDBC | Database connectivity |

---

## 🗄️ Database Schema

```sql
CREATE DATABASE library_db;
USE library_db;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100),
    price DOUBLE
);

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE issue_books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    book_id INT,
    issue_date DATE,
    return_date DATE,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

---

## 📁 Project Structure
```
LibraryManagement/
 ┣ DBConnection.java      → MySQL connection
 ┣ Theme.java             → UI colors and styling
 ┣ MainMenu.java          → Main dashboard
 ┣ AddBook.java           → Add new books
 ┣ ViewBooks.java         → View all books
 ┣ IssueBook.java         → Issue and return books
 ┣ mysql-connector.jar    → MySQL JDBC driver
 ┗ README.md              → Project documentation
```

---

## ▶️ How to Run

### Prerequisites
- Java JDK 8 or above
- MySQL Server
- VS Code or any Java IDE

### Steps
1. Clone this repository
2. Import project in VS Code
3. Setup MySQL database using the SQL queries above
4. Update DBConnection.java with your MySQL password
5. Compile and run:

```bash
javac -cp ".;mysql-connector-j-8.3.0.jar" *.java
java -cp ".;mysql-connector-j-8.3.0.jar" MainMenu
```

---

## 🎥 Demo Video

https://drive.google.com/file/d/1z68uMdQ2ioM6zn3zXcyfE5AqZI8FkpQw/view?usp=sharing

---

## 📋 Business Rules
- Maximum **3 books** can be issued per student at a time
- Fine of **₹10 per day** is charged after 7 days
- Return date is automatically recorded
- Fine is calculated automatically

---

## 🙏 Acknowledgement
This project was developed as a Mini Project for academic purposes.

> **College:** MIT Academy Of Enginnering  
> **Department:** IT 
> **Year:** 2026
