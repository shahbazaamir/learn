package com.example.filedemo.repository;

import com.example.filedemo.model.DBFile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, String> {
	
	List<DBFile> findByFileName (String fileName);
}
