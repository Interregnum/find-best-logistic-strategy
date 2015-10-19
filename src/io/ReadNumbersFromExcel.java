package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Mengchao Zhong
 */
public class ReadNumbersFromExcel {

	private String filePath;

	public ReadNumbersFromExcel(String filePath) {
		super();
		this.filePath = filePath;
	}
	
	/**
	 * @return List of numbers.
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public List<Double> readDoublesFromColumn(int column) throws IOException {
		List<Double> list = new ArrayList<Double>();
		
		FileInputStream file = new FileInputStream(new File(this.filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = worksheet.iterator();
        
        System.out.print("Loading...");
        while(rowIterator.hasNext()) {
        	Row row = rowIterator.next();
        	if(column < row.getLastCellNum()) {
	            Cell cell = row.getCell(column);
	            cell.setCellType(0);
	            list.add(cell.getNumericCellValue());
        	}
        }
        System.out.println("Done");
        
        return list;
	}
	
	/**
	 * @return List of numbers.
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public List<Integer> readIntegersFromColumn(int column) throws IOException {
		List<Integer> list = new ArrayList<Integer>();
		
		FileInputStream file = new FileInputStream(new File(this.filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet worksheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = worksheet.iterator();
        
        System.out.print("Loading...");
        while(rowIterator.hasNext()) {
        	Row row = rowIterator.next();
        	if(column < row.getLastCellNum()) {
	            Cell cell = row.getCell(column);
	            cell.setCellType(0);
	            list.add(new Double(cell.getNumericCellValue()).intValue());
        	}
        }
        System.out.println("Done");
        
        return list;
	}
}
