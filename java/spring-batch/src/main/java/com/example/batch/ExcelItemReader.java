package com.example.batch;

import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

public class ExcelItemReader implements ItemReader<Employee> {
    
    private Resource resource;
    private Workbook workbook;
    private Iterator<Row> rowIterator;
    private boolean initialized = false;
    
    public ExcelItemReader(Resource resource) {
        this.resource = resource;
    }
    
    @Override
    public Employee read() throws Exception {
        if (!initialized) {
            initialize();
        }
        
        if (rowIterator != null && rowIterator.hasNext()) {
            Row row = rowIterator.next();
            return mapRowToEmployee(row);
        }
        
        return null;
    }
    
    private void initialize() throws IOException {
        workbook = WorkbookFactory.create(resource.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        rowIterator = sheet.iterator();
        
        // Skip header row
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        
        initialized = true;
    }
    
    private Employee mapRowToEmployee(Row row) {
        Employee employee = new Employee();
        
        employee.setFirstName(getCellValueAsString(row.getCell(0)));
        employee.setLastName(getCellValueAsString(row.getCell(1)));
        employee.setEmail(getCellValueAsString(row.getCell(2)));
        employee.setDepartment(getCellValueAsString(row.getCell(3)));
        employee.setSalary(getCellValueAsBigDecimal(row.getCell(4)));
        
        return employee;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
    
    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        
        return switch (cell.getCellType()) {
            case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> {
                try {
                    yield new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    yield BigDecimal.ZERO;
                }
            }
            default -> BigDecimal.ZERO;
        };
    }
}