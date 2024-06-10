package AICheckingApp.rs.ac.university;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.hsmf.extractor.OutlookTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DragAndDropApp extends JFrame {

	public DragAndDropApp() {
		setTitle("Drag and Drop PDF and Word Files");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane);

		new DropTarget(textArea, new FileDropTargetListener(textArea));

		setVisible(true);

        Scanner s = new Scanner(System.in);

	}

	private class FileDropTargetListener extends DropTargetAdapter {
		private JTextArea textArea;

		public FileDropTargetListener(JTextArea textArea) {
			this.textArea = textArea;
		}

		@Override
		public void drop(DropTargetDropEvent event) {
			try {
				event.acceptDrop(DnDConstants.ACTION_COPY);
				Transferable transferable = event.getTransferable();
				DataFlavor[] flavors = transferable.getTransferDataFlavors();
				for (DataFlavor flavor : flavors) {
					if (flavor.isFlavorJavaFileListType()) {
						List<File> files = (List<File>) transferable.getTransferData(flavor);
						for (File file : files) {
							processFile(file);
						}
					}
				}
				event.dropComplete(true);
			} catch (Exception e) {
				e.printStackTrace();
				event.dropComplete(false);
			}
		}

		private void processFile(File file) {
			String extension = getFileExtension(file);
			try {
				if ("pdf".equalsIgnoreCase(extension)) {
					extractTextFromPDF(file);
				} else if ("doc".equalsIgnoreCase(extension) || "docx".equalsIgnoreCase(extension)) {
					extractTextFromWord(file);
				} else {
					textArea.append("Unsupported file type: " + extension + "\n");
				}
			} catch (IOException e) {
				textArea.append("Error processing file: " + file.getName() + "\n");
				e.printStackTrace();
			}
		}

		private String getFileExtension(File file) {
			String name = file.getName();
			int lastIndexOf = name.lastIndexOf('.');
			return lastIndexOf == -1 ? "" : name.substring(lastIndexOf + 1);
		}

		private void extractTextFromPDF(File file) throws IOException {
			try (PDDocument document = PDDocument.load(file)) {
				PDFTextStripper pdfStripper = new PDFTextStripper();
				String text = pdfStripper.getText(document);
				textArea.append("PDF Content of " + file.getName() + ":\n");
				textArea.append(text + "\n");
			}
		}

		private void extractTextFromWord(File file) throws IOException {
			try (FileInputStream fis = new FileInputStream(file);
				 HWPFDocument document = new HWPFDocument(fis);
//				 XWPFDocument document = new XWPFDocument(fis);
				 WordExtractor extractor = new WordExtractor(document)) {

				String text = extractor.getText();

				// Ensure textArea append happens on the EDT
				SwingUtilities.invokeLater(() -> {
					textArea.append("Word Content of " + file.getName() + ":\n");
					textArea.append(text + "\n");
				});


			} catch (IOException e) {
				// Handle IOException (file not found, unable to read, etc.)
				throw e;
			} catch (Exception e) {
				// Handle other potential exceptions
				e.printStackTrace();
			}
		}


	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new DragAndDropApp());




	}
}

