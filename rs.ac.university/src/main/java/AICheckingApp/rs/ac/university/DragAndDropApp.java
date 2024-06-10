package AICheckingApp.rs.ac.university;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

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
						event.dropComplete(true);
						return;
					}
				}
				event.dropComplete(false);
			} catch (Exception e) {
				e.printStackTrace();
				event.dropComplete(false);
			}
		}

		private void processFile(File file) {
			String fileName = file.getName();
			String fileExtension = getFileExtension(file);

			SwingUtilities.invokeLater(() -> {
				textArea.append("File: " + fileName + " (." + fileExtension + ")\n");
			});

			try {
				switch (fileExtension.toLowerCase()) {
					case "pdf":
						extractTextFromPDF(file);
						break;
					case "doc":
						extractTextFromDoc(file);
						break;
					case "docx":
						extractTextFromDocx(file);
						break;
					default:
						SwingUtilities.invokeLater(() -> {
							textArea.append("Unsupported file type: " + fileExtension + "\n");
						});
				}
			} catch (IOException e) {
				e.printStackTrace();
				SwingUtilities.invokeLater(() -> {
					textArea.append("Error processing file: " + fileName + "\n");
				});
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
				SwingUtilities.invokeLater(() -> {
					textArea.append("PDF Content of " + file.getName() + ":\n");
					textArea.append(text + "\n");
				});
			}
		}

		private void extractTextFromDoc(File file) throws IOException {
			try (FileInputStream fis = new FileInputStream(file);
				 HWPFDocument document = new HWPFDocument(fis);
				 WordExtractor extractor = new WordExtractor(document)) {

				String text = extractor.getText();
				SwingUtilities.invokeLater(() -> {
					textArea.append("Word Content of " + file.getName() + ":\n");
					textArea.append(text + "\n");
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void extractTextFromDocx(File file) throws IOException {
			try (FileInputStream fis = new FileInputStream(file);
				 XWPFDocument document = new XWPFDocument(fis);
				 XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

				String text = extractor.getText();
				SwingUtilities.invokeLater(() -> {
					textArea.append("Word Content of " + file.getName() + ":\n");
					textArea.append(text + "\n");
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(DragAndDropApp::new);
	}
}
