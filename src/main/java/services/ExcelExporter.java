package services;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import models.Post;

import java.io.FileOutputStream;
import java.io.IOException;


public class ExcelExporter {
    public static void exportToExcel(ObservableList<Post> posts, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Posts");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Content");
        headerRow.createCell(2).setCellValue("Timestamp");

        // Populate data rows
        for (int i = 0; i < posts.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Post post = posts.get(i);
            row.createCell(0).setCellValue(post.getIdPost());
            row.createCell(1).setCellValue(post.getContentPost());
            row.createCell(2).setCellValue(post.getTimeStamp_envoi().toString());
        }

        // Write workbook to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
            System.out.println("Excel file exported successfully.");
        }
    }
}
