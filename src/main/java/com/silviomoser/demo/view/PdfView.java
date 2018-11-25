package com.silviomoser.demo.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.silviomoser.demo.data.AbstractEntity;
import com.silviomoser.demo.data.type.HasLabel;
import com.silviomoser.demo.utils.PdfReport;
import org.apache.commons.beanutils.PropertyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PdfView extends AbstractITextPdfView {

    private static Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    private static Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA);

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<AbstractEntity> items = (List<AbstractEntity>) model.get("entries");
        Class type = items.get(0).getClass();
        generatePdfListReport(items, type, document);
    }

    public static <T> void generatePdfListReport(List<T> items, Class<T> tClass, Document document) {


        final List<String> pdfHeaders = new ArrayList<>(0);
        final List<String[]> pdfItems = new ArrayList<>(items.size());

        for (Field f : tClass.getDeclaredFields()) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof PdfReport) {
                    PdfReport pdfReport = (PdfReport) annotation;
                    pdfHeaders.add(pdfReport.header());
                }
            }
        }

        int index = 0;
        final int[] maxLengths = new int[pdfHeaders.size()];
        for (String label : pdfHeaders) {
            maxLengths[index] = label.length();
            index++;
        }

        try {
            for (T it : items) {
                final List<String> line = new ArrayList<>();
                index=0;
                for (Field f : tClass.getDeclaredFields()) {
                    final Annotation[] annotations = f.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof PdfReport) {
                            final Object object = PropertyUtils.getProperty(it, f.getName());
                            String out;
                            if (object.getClass() == LocalDateTime.class) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                out = ((LocalDateTime)object).format(formatter);
                            } else {
                                if (object instanceof HasLabel) {
                                    out = ((HasLabel) object).getLabel();
                                } else {
                                    out = object.toString();
                                }
                            }
                            line.add(out);
                            if (out.length()> maxLengths[index]) {
                                maxLengths[index] = out.length();
                            }
                            index++;
                        }
                    }
                }
                pdfItems.add(line.toArray(new String[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfPTable table = new PdfPTable(pdfHeaders.size());
            table.setWidthPercentage(100);
            //table.setWidths(new int[]{1, 3, 3});

            table.setWidths(maxLengths);


            pdfHeaders.forEach(it -> addCell(table, it));

            pdfItems.forEach(it -> addRow(table, it));


            document.add(table);


        } catch (DocumentException ex) {

            ex.printStackTrace();
        }


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
