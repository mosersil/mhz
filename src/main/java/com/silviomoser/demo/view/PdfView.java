package com.silviomoser.demo.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.silviomoser.demo.api.shop.ShopHelper;
import com.silviomoser.demo.data.AbstractEntity;
import com.silviomoser.demo.data.ShopItemPurchase;
import com.silviomoser.demo.data.ShopTransaction;
import com.silviomoser.demo.data.type.HasLabel;
import com.silviomoser.demo.data.type.ShopItemType;
import com.silviomoser.demo.data.type.ShopOrderStatusType;
import com.silviomoser.demo.utils.FormatUtils;
import com.silviomoser.demo.utils.PdfReport;
import org.apache.commons.beanutils.PropertyUtils;
import sun.font.FontFamily;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silviomoser.demo.api.shop.ShopHelper.calculateTotal;
import static com.silviomoser.demo.api.shop.ShopHelper.formatCurrency;


public class PdfView extends AbstractITextPdfView {

    private static CMYKColor MHZ_BLUE = new CMYKColor(0.59f, 0.32f, 0.f, 0.62f);

    private static Font HEAD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    private static Font BODY_FONT = FontFactory.getFont(FontFactory.HELVETICA);

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model.containsKey("entries")) {
            List<AbstractEntity> items = (List<AbstractEntity>) model.get("entries");
            Class type = items.get(0).getClass();
            generatePdfListReport(items, type, document);
        }
        if (model.containsKey("transaction")) {
            ShopTransaction transaction = (ShopTransaction) model.get("transaction");
            generatePdfTransaction(transaction, document, response);
        }
    }

    private void generatePdfTransaction(ShopTransaction transaction, Document document, HttpServletResponse response) {
        try {
            Image logo = Image.getInstance("classpath:logo/logo.jpg");
            logo.scaleToFit(100, 100);

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            PdfContentByte canvas = writer.getDirectContent();


            canvas.setColorStroke(MHZ_BLUE);
            canvas.moveTo(36, 722);
            canvas.lineTo(559, 722);
            canvas.closePathStroke();

            String documentTitle = null;
            if (transaction.getStatus().equals(ShopOrderStatusType.PAYED)) {
                documentTitle = String.format("Ihr Einkauf vom %s", transaction.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            } else {
                documentTitle = String.format("Rechnung " + transaction.getId());
            }

            placeAbsoluteText(writer, documentTitle, 200, 750);


            PdfPTable table = new PdfPTable(5);
            table.setSpacingBefore(20);
            table.setPaddingTop(100);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 2, 5, 3, 3});

            addCell(table, "#");
            addCell(table, "Anzahl");
            addCell(table, "Artikel");
            addCell(table, "Stückpreis");
            addCell(table, "Gesamtpreis");
            int position = 1;
            for (ShopItemPurchase it : transaction.getShopItemPurchases()) {
                addRow(table, new String[]{"" + position, "" + it.getAmount(), it.getItem().getName(), formatCurrency(it.getItem().getPrice(), "CHF"), formatCurrency(calculateTotal(it), "CHF")},
                        new int[]{Element.ALIGN_CENTER, Element.ALIGN_RIGHT, Element.ALIGN_LEFT, Element.ALIGN_RIGHT, Element.ALIGN_RIGHT});
                position++;
            }
            addRow(table, new String[]{"", "", "", "", ""});
            addRow(table, new String[]{"", "", "", "TOTAL", "" + formatCurrency(calculateTotal(transaction), "CHF")},
                    new int[]{Element.ALIGN_LEFT, Element.ALIGN_LEFT, Element.ALIGN_LEFT, Element.ALIGN_RIGHT, Element.ALIGN_RIGHT});


            document.add(logo);

            Paragraph table_title = new Paragraph("Bestellung: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            table_title.setSpacingBefore(30);
            document.add(table_title);
            document.add(table);

            Paragraph address_paragraph = new Paragraph("Lieferadresse: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            address_paragraph.setAlignment(Element.ALIGN_LEFT);
            address_paragraph.setSpacingBefore(30);
            document.add(address_paragraph);
            Paragraph address = new Paragraph(FormatUtils.fullAddress(transaction.getPerson()), new Font(Font.FontFamily.HELVETICA, 12));
            address_paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(address);


            Paragraph payment_title = new Paragraph("Zahlung: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            payment_title.setAlignment(Element.ALIGN_LEFT);
            payment_title.setSpacingBefore(30);
            document.add(payment_title);
            Paragraph payment_paragraph = new Paragraph(ShopHelper.formatStatus(transaction), new Font(Font.FontFamily.HELVETICA, 12));
            address_paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(payment_paragraph);

            if (transaction.getStatus().equals(ShopOrderStatusType.PAYED)) {
                List<ShopItemPurchase> ticketPurchases = transaction.getShopItemPurchases().stream().filter(shopItemPurchase -> shopItemPurchase.getItem().getShopItemType().equals(ShopItemType.TICKET)).collect(Collectors.toList());

                if (ticketPurchases != null && ticketPurchases.size() > 0) {
                    document.newPage();
                    int yPos = 806;
                    for (ShopItemPurchase it : ticketPurchases) {
                        PdfContentByte canvasTickets = writer.getDirectContent();
                        Rectangle rect = new Rectangle(36, yPos, 559, yPos - 200);
                        rect.setBorder(Rectangle.BOX);
                        rect.setBorderWidth(1);
                        Image ticketLogo = Image.getInstance("classpath:logo/logo.jpg");
                        ticketLogo.scaleToFit(100, 100);
                        ticketLogo.setAbsolutePosition(45, yPos - 100);
                        canvasTickets.addImage(ticketLogo);
                        canvasTickets.rectangle(rect);


                        StringBuilder titleTextBuilder = new StringBuilder();
                        ColumnText ctTitle = new ColumnText(canvasTickets);

                        if (it.getAmount() > 1) {
                            titleTextBuilder.append(it.getItem().getName() + "\n" + it.getAmount() + " Personen");
                        } else {
                            titleTextBuilder.append(it.getItem().getName() + "\n" + "1 Person");
                        }
                        titleTextBuilder.append(" (" + FormatUtils.toFirstLastName(it.getTransaction().getPerson()) + ")");
                        Phrase titleText = new Phrase(titleTextBuilder.toString());

                        titleText.setFont(new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
                        ctTitle.setSimpleColumn(titleText, 200, yPos - 50, 560, yPos - 80, 15, Element.ALIGN_LEFT);
                        ctTitle.go();

                        ColumnText ct = new ColumnText(canvasTickets);
                        Phrase myText = new Phrase(it.getItem().getDescription());
                        myText.setFont(new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC));
                        ct.setSimpleColumn(myText, 48, yPos - 120, 560, yPos - 200, 15, Element.ALIGN_LEFT);
                        ct.go();

                        yPos = yPos - 200;
                    }
                }

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                index = 0;
                for (Field f : tClass.getDeclaredFields()) {
                    final Annotation[] annotations = f.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof PdfReport) {
                            final Object object = PropertyUtils.getProperty(it, f.getName());
                            String out;
                            if (object.getClass() == LocalDateTime.class) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                out = ((LocalDateTime) object).format(formatter);
                            } else {
                                if (object instanceof HasLabel) {
                                    out = ((HasLabel) object).getLabel();
                                } else {
                                    out = object.toString();
                                }
                            }
                            line.add(out);
                            if (out.length() > maxLengths[index]) {
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

    private static void addRow(PdfPTable table, String[] it, int[] alignments) {
        int i = 0;
        for (String anIt : it) {
            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase(anIt, BODY_FONT));
            hcell.setHorizontalAlignment(alignments[i]);
            table.addCell(hcell);
            i++;
        }
    }

    private static void addCell(PdfPTable table, String it) {
        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase(it, HEAD_FONT));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

    }

    private static void placeAbsoluteText(PdfWriter writer, String text, int x, int y) throws
            IOException, DocumentException {
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.saveState();
        cb.beginText();
        cb.moveText(x, y);
        cb.setFontAndSize(bf, 16);
        cb.showText(text);
        cb.endText();
        cb.restoreState();
    }
}
