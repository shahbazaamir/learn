package com.example.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class BatchConfig {

    @Bean
    public ItemReader<Employee> reader(@Value("${input.file:employees.csv}") String fileName) {
        if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            return new ExcelItemReader(new ClassPathResource(fileName));
        } else {
            return csvReader(fileName);
        }
    }
    
    private FlatFileItemReader<Employee> csvReader(String fileName) {
        FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(fileName));
        reader.setLinesToSkip(1);
        
        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("firstName", "lastName", "email", "department", "salary");
        
        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);
        
        return reader;
    }

    @Bean
    public Step importStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                          ItemReader<Employee> reader, EmployeeProcessor processor, EmployeeStatusWriter writer) {
        return new StepBuilder("importStep", jobRepository)
                .<Employee, EmployeeStatus>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importEmployeeJob(JobRepository jobRepository, Step importStep) {
        return new JobBuilder("importEmployeeJob", jobRepository)
                .start(importStep)
                .build();
    }
}