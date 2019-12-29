package com.silviomoser.mhz.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.silviomoser.mhz.data.ShopTransaction;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by silvio on 07.08.18.
 */
@Slf4j
public class PdfBuilder {

    private static Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    private static Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA);


    public static ByteArrayInputStream generateReceipt(ShopTransaction shopTransaction) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            final List<String[]> pdfItems = new ArrayList<>(shopTransaction.getShopItemPurchases().size());

            shopTransaction.getShopItemPurchases().forEach(it -> {
                String[] line = new String[] {""+it.getAmount(), it.getItem().getName(), ""+it.getItem().getPrice()};
                pdfItems.add(line);
            });


            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            //table.setWidths(new int[]{1, 3, 3});


            addCell(table, "Menge");
            addCell(table, "Artikel");
            addCell(table, "Preis");


            pdfItems.forEach(it -> addRow(table, it));


            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();

        } catch (DocumentException ex) {
            log.error(ex.getMessage(), ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    
    private static void addRow(PdfPTable table, String[] it) {
        for (String anIt : it) {
            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase(anIt, BODY_FONT));
            hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(hcell);
        }
    }

    private static void addCell(PdfPTable table, String it) {
        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase(it, HEAD_FONT));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

    }
}