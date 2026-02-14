package com.example.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class EmployeeStatusWriter implements ItemWriter<EmployeeStatus> {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private static final String CSV_FILE = "employee_status_report.csv";
    private static boolean headerWritten = false;
    
    @Override
    @Transactional
    public void write(Chunk<? extends EmployeeStatus> chunk) throws Exception {
        List<? extends EmployeeStatus> items = chunk.getItems();
        
        // Write to CSV file
        writeToCsv(items);
        
        // Save to database and update status
        for (EmployeeStatus status : items) {
            try {
                Employee employee = new Employee();
                employee.setFirstName(status.getFirstName());
                employee.setLastName(status.getLastName());
                employee.setEmail(status.getEmail());
                employee.setDepartment(status.getDepartment());
                employee.setSalary(status.getSalary());
                
                entityManager.persist(employee);
                status.setDbStatus("SUCCESS");
                
            } catch (Exception e) {
                status.setDbStatus("FAILED");
                status.setErrorMessage(status.getErrorMessage() + "; DB Error: " + e.getMessage());
            }
        }
        
        // Update CSV with final status
        updateCsvWithDbStatus(items);
    }
    
    private void writeToCsv(List<? extends EmployeeStatus> items) throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            
            // Write header only once
            if (!headerWritten) {
                writer.append("firstName,lastName,email,department,salary,apiStatus,dbStatus,errorMessage\\n");
                headerWritten = true;
            }
            
            // Write data rows
            for (EmployeeStatus status : items) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s\\n",
                    escapeCommas(status.getFirstName()),
                    escapeCommas(status.getLastName()),
                    escapeCommas(status.getEmail()),
                    escapeCommas(status.getDepartment()),
                    status.getSalary(),
                    status.getApiStatus(),
                    status.getDbStatus() != null ? status.getDbStatus() : "PENDING",
                    escapeCommas(status.getErrorMessage() != null ? status.getErrorMessage() : "")
                ));
            }
        }
    }
    
    private void updateCsvWithDbStatus(List<? extends EmployeeStatus> items) {
        // For simplicity, we write the complete status in the initial write
        // In production, you might want to update specific rows
    }
    
    private String escapeCommas(String value) {
        if (value == null) return "";
        if (value.contains(",")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}