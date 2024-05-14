package controllers;


import models.Product;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

public class PDFGenerator {

    public static void generatePDF(Set<Product> products, String filePath) {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            PdfPTable table = new PdfPTable(7); // Nombre de colonnes
            addTableHeader(table);
            addRows(table, products);

            document.add(table);
            document.close();

            System.out.println("PDF créé avec succès !");
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addTableHeader(PdfPTable table) {
        table.addCell("ID");
        table.addCell("Categorie ID");
        table.addCell("Tags ID");
        table.addCell("Nom");
        table.addCell("Description");
        table.addCell("Image");
        table.addCell("Prix");
    }

    private static void addRows(PdfPTable table, Set<Product> products) {
        for (Product product : products) {
            table.addCell(String.valueOf(product.getIdP()));
            table.addCell(String.valueOf(product.getIdCategorie()));
            table.addCell(String.valueOf(product.getIdTag()));
            table.addCell(product.getNomP());
            table.addCell(product.getDescriptionP());
            table.addCell(product.getImageP());
            table.addCell(String.valueOf(product.getPrixP()));
        }
    }
}