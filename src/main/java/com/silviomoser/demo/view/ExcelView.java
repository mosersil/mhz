package com.silviomoser.demo.view;

import com.silviomoser.demo.data.AbstractEntity;
import com.silviomoser.demo.utils.XlsReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelView extends AbstractXlsView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        // change the file name
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xlsx\"");


        List<AbstractEntity> entries = (List<AbstractEntity>) model.get("entries");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("1");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);


        Row header = sheet.createRow(0);
        int cellCounter=0;
        for (Field f : entries.get(0).getClass().getDeclaredFields()) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof XlsReport) {
                    XlsReport pdfReport = (XlsReport) annotation;
                    header.createCell(cellCounter).setCellValue(pdfReport.header());
                    header.getCell(cellCounter).setCellStyle(style);
                    cellCounter++;
                }
            }
        }


        int rowCount = 1;

        for(AbstractEntity user : entries){
            int rowCellCounter = 0;
            Row userRow =  sheet.createRow(rowCount++);
            for (Field f : user.getClass().getDeclaredFields()) {
                Annotation[] annotations = f.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof XlsReport) {
                        XlsReport pdfReport = (XlsReport) annotation;
                        if (pdfReport.header().equals(header.getCell(rowCellCounter).getStringCellValue())) {
                            String test = BeanUtils.getSimpleProperty(user, f.getName());
                            userRow.createCell(rowCellCounter).setCellValue(test);
                        }
                        rowCellCounter++;
                    }
                }
            }
        }

        log.debug("Built workbook: " + workbook);
    }

}
