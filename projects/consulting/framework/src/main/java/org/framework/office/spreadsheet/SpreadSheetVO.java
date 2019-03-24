package org.framework.office.spreadsheet;

public class SpreadSheetVO {
	private Object [][] spreadSheetData ;
	private boolean openFile ;
	private String fileFullName ;
	private String [] columns ;
	public Object[][] getSpreadSheetData() {
		return spreadSheetData;
	}
	public void setSpreadSheetData(Object[][] spreadSheetData) {
		this.spreadSheetData = spreadSheetData;
	}
	public boolean isOpenFile() {
		return openFile;
	}
	public void setOpenFile(boolean openFile) {
		this.openFile = openFile;
	}
	public String getFileFullName() {
		return fileFullName;
	}
	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}
	public String[] getColumns() {
		return columns;
	}
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
}
