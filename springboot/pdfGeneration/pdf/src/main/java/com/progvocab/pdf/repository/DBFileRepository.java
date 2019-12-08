package com.progvocab.pdf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.progvocab.pdf.entity.DBFile;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, String> {

}