package com.eligibilityChecker.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomizedMethods {
	public LinkedHashMap<String, String> readTestdatawithrowvalue(String ScriptName, String SheetName) throws IOException {

		XSSFWorkbook ExcelWBook = null;
		LinkedHashMap<String,String> hmDataDrivenTestDataAll = new LinkedHashMap<String,String>();
		FileInputStream testdatafile = null;
		testdatafile = checkFileExists(ScriptName);
		if(testdatafile==null) {
			System.out.println("File path is wrong, not able to read the excel path" + ScriptName);
			return null;
		}
		ExcelWBook = new XSSFWorkbook(testdatafile);
		XSSFSheet ExcelWSheet = ExcelWBook.getSheet(SheetName);
		int iTotalRows = ExcelWSheet.getLastRowNum();
		try {
			for (int cc=1; cc<=iTotalRows ; cc++) {
				Row eachRow = ExcelWSheet.getRow(cc);
				String sMessageName = eachRow.getCell(0).toString();
				String sMessageValue = eachRow.getCell(1).toString();
				hmDataDrivenTestDataAll.put(sMessageName, sMessageValue);
			}
			testdatafile.close();
			return hmDataDrivenTestDataAll;
		} catch (Exception ee) {
			testdatafile.close();
			return null;
		}
	}

	private static FileInputStream checkFileExists(String sTestDataExcelName) {
		FileInputStream testdatafile = null;
		String filepath = "";
		try {
			filepath = System.getProperty("user.dir") + "\\TestInputs\\" + sTestDataExcelName;
			//System.out.println("filepath1: " + filepath);
			testdatafile = new FileInputStream (new File (filepath));
			return testdatafile;
		} catch (FileNotFoundException fe) {
			try {
				filepath = System.getProperty("user.dir") + "\\TestInputs\\" + sTestDataExcelName + ".xlsx";
				testdatafile = new FileInputStream (new File (filepath));
				//System.out.println("filepath1: " + filepath);
				return testdatafile;
			}  catch (Exception e1) {
				try {
					filepath = sTestDataExcelName;
					testdatafile = new FileInputStream (new File (filepath));
					//System.out.println("filepath1: " + filepath);
					return testdatafile;
				} catch (Exception ee) {
					System.out.println("File path is wrong, not able to read the excel path" + filepath);
					return null;
				}
			}
		}
	}
}
