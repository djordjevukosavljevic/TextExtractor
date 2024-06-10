Drag and Drop PDF and Word Files
Welcome to the Drag and Drop PDF and Word Files application! This Java application allows users to drag and drop PDF and Word files onto a text area to view their contents. It supports .pdf, .doc, and .docx file formats, extracting and displaying their text contents within a graphical user interface (GUI).

Features
Drag and Drop Support: Easily drag and drop your PDF and Word files into the application.
PDF Text Extraction: Extracts and displays text content from PDF files.
Word Document Text Extraction: Supports both .doc (using HWPFDocument) and .docx (using XWPFDocument) formats for text extraction.
User-Friendly Interface: Simple and clean interface with a non-editable text area for displaying file contents.
Getting Started
Prerequisites
To run this application, you need to have the following installed:

Java Development Kit (JDK) 8 or later
Maven (optional, for building the project)
Installation
Clone the Repository

bash
Copy code
git clone https://github.com/djordjevukosavljevic/DragAndDropApp.git
cd DragAndDropApp
Compile the Application

If you have Maven installed, you can compile the application using:

bash
Copy code
mvn clean install
Alternatively, you can compile it manually:

bash
Copy code
javac -cp .:path/to/dependencies/* -d bin src/main/java/AICheckingApp/rs/ac/university/*.java
Run the Application

After compilation, run the application:

bash
Copy code
java -cp .:bin:path/to/dependencies/* AICheckingApp.rs.ac.university.DragAndDropApp
Usage
Launch the Application

After running the application, a window titled "Drag and Drop PDF and Word Files" will appear.

Drag and Drop Files

Drag one or more .pdf, .doc, or .docx files from your file explorer.
Drop them into the text area in the application window.
View Extracted Text

The text content of the dropped files will be extracted and displayed in the text area.
Each file’s content will be prefixed with the file name and extension for easy identification.
