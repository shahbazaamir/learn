package com.progvocab.pdf.controller;

public class UploadFileResponse {

	private String fileName;
	private String fileDownloadUri;
	private String contentType;
	private long size;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public UploadFileResponse(String fileName, String fileDownloadUri, String contentType, long size) {
		// TODO Auto-generated constructor stub
	}

}
