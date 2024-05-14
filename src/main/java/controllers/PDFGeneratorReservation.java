package controllers;
import models.Reservation;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PDFGeneratorReservation {
    public void generatePDF(String filename, List<Reservation> reservations) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.setNonStrokingColor(Color.RED); // Changer la couleur du texte à Bleu
                contentStream.setStrokingColor(Color.BLACK); // Couleur des lignes du tableau en Noir

                // Dessiner le titre centré "PDF resrvation" en bleu
                String title = "Rapport reservation";
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000f * 24f;
                float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(titleX, 750);
                contentStream.showText(title);
                contentStream.endText();

                float margin = 50;
                float y = 700;
                float tableWidth = 500;

                // Dessiner le fond blanc du tableau
                contentStream.setNonStrokingColor(Color.WHITE); // Changer la couleur du fond à Blanc
                contentStream.fillRect(margin, y, tableWidth, -20 * (reservations.size() + 1)); // Calculer la hauteur en fonction du nombre de lignes

                // Dessiner le tableau avec des bordures arrondies
                drawRoundedRect(contentStream, margin, y, tableWidth, -20 * (reservations.size() + 1), 10);

                // En-tête du tableau
                String[] headers = {"ID", "Payment Mode", "Date", "ID Event"};
                float[] columnWidths = {100, 150, 150, 100}; // Ajuster la largeur des colonnes
                drawTable(contentStream, y, tableWidth, margin, headers, columnWidths, PDType1Font.HELVETICA_BOLD, 12f);

                // Contenu du tableau
                for (Reservation reservation : reservations) {
                    y -= 20; // Ajuster la hauteur de chaque ligne
                    String[] rowData = {
                            String.valueOf(reservation.getId_reservation()),
                            reservation.getMode_paiement(),
                            reservation.getDate_reservation().toString(), // Assurez-vous que la date est au format String
                            String.valueOf(reservation.getEvent_id()),
                    };
                    drawTable(contentStream, y, tableWidth, margin, rowData, columnWidths, PDType1Font.HELVETICA, 12f);
                }
            }

            document.save(filename);
            System.out.println("PDF généré avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawTable(PDPageContentStream contentStream, float y, float tableWidth, float margin,
                           String[] content, float[] columnWidths, PDType1Font font, float fontSize) throws IOException {
        final int cols = content.length;
        float rowHeight = fontSize + 2 * 2; // Augmenter le padding pour une ligne plus grande

        // Dessiner les lignes du tableau
        contentStream.setLineWidth(0.5f);
        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(Color.BLACK); // Couleur noire pour les lignes et les informations

        float textY = y - fontSize;
        float nextX = margin; // Ajout de cette ligne pour garder la trace de la position actuelle en x
        for (int i = 0; i < content.length; i++) {
            float textWidth = font.getStringWidth(content[i]) * fontSize / 1000f;
            float cellWidth = columnWidths[i];
            float textX = nextX + (cellWidth - textWidth) / 2; // Centrer le texte
            contentStream.beginText();
            contentStream.newLineAtOffset(textX, textY);
            contentStream.showText(content[i]);
            contentStream.endText();
            nextX += columnWidths[i]; // Mise à jour de nextX pour la prochaine colonne

            // Dessiner les lignes verticales du tableau
            contentStream.moveTo(nextX, y); // Utilisation de nextX pour la position de la ligne verticale
            contentStream.lineTo(nextX, y - 20);
            contentStream.stroke();
        }

        // Dessiner la ligne horizontale en bas du tableau
        contentStream.moveTo(margin, y - 20);
        contentStream.lineTo(margin + tableWidth, y - 20); // Ajustement de la position de la ligne horizontale
        contentStream.stroke();
    }

    private void drawRoundedRect(PDPageContentStream contentStream, float x, float y, float width, float height, float radius) throws IOException {
        contentStream.moveTo(x + radius, y);
        contentStream.lineTo(x + width - radius, y);
        contentStream.curveTo(x + width - radius / 2, y, x + width, y + radius / 2, x + width, y + radius);
        contentStream.lineTo(x + width, y + height - radius);
        contentStream.curveTo(x + width, y + height - radius / 2, x + width - radius / 2, y + height, x + width - radius, y + height);
        contentStream.lineTo(x + radius, y + height);
        contentStream.curveTo(x + radius / 2, y + height, x, y + height - radius / 2, x, y + height - radius);
        contentStream.lineTo(x, y + radius);
        contentStream.curveTo(x, y + radius / 2, x + radius / 2, y, x + radius, y);
        contentStream.closePath();
        contentStream.stroke();
    }
}

