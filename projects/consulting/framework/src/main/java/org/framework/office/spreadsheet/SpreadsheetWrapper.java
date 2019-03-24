package org.framework.office.spreadsheet;

import java.io.File;
import java.util.Date;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class SpreadsheetWrapper {
	public void createSpreadSheet(SpreadSheetVO spreadSheetVO) throws Exception{
		final Object[][] data = spreadSheetVO.getSpreadSheetData();
		String[] columns = spreadSheetVO.getColumns();
		TableModel model = new DefaultTableModel(data, columns);  
		final File file = new File(spreadSheetVO.getFileFullName());
		SpreadSheet.createEmpty(model).saveAs(file);
		if(spreadSheetVO.isOpenFile()){
			OOUtils.open(file);
		}
	}
	public void readSpreadSheet(SpreadSheetVO spreadSheetVO)throws Exception{
		// Load the file.
		 File file = new File("F:\\shahbaz\\work\\keplWork\\Consulting\\temperature.ods");
		 final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
		 // Change date.
		 sheet.getCellAt("I10").setValue(new Date());
		 // Change strings.
		 sheet.setValueAt("Filling test", 1, 1);
		// sheet.getCellAt("B27").setValue("On site support");
		 // Change number.
		 //sheet.getCellAt("F24").setValue(3);
		 // Or better yet use a named range
		 // (relative to the first cell of the range, wherever it might be).
		 //sheet.getSpreadSheet().getTableModel("Products").setValueAt(1, 5, 4);
		 // Save to file and open it.
		 File outputFile = new File("fillingTest.ods");
		 OOUtils.open(sheet.getSpreadSheet().saveAs(outputFile));
	}

}
