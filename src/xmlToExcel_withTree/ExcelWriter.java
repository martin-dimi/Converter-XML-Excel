package xmlToExcel_withTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	private XSSFWorkbook workbook;
	private String fileName;
	private XSSFCellStyle header;
	private XSSFCellStyle id;
	private XSSFCellStyle pId;

	ExcelWriter() {

	}

	public void writeToExcel(Entity tree, File output) throws IOException {
		fileName = tree.getName();
		workbook = new XSSFWorkbook();
		setColors();

		process(tree);

		writeFile(output);
	}

	private void process(Entity node) {

		XSSFSheet sheet = workbook.getSheet(node.getName());
		if (sheet == null)
			sheet = workbook.createSheet(node.getName());

		node.setID(sheet.getLastRowNum() + 1);

		writeID(node, sheet);

		for (int i = 0; i < node.getChildren().size(); i++) {
			Entity child = node.getChildren().get(i);
			if (child.hasChildren())
				process(child);

			writeTopicToSheet(child, sheet, i);
			writeToSheet(child, sheet, i);
		}

	}

	private void writeTopicToSheet(Entity child, XSSFSheet sheet, int col) {

		Row row = sheet.getRow(0);
		Cell cell = row.createCell(col + 1);
		cell.setCellValue(child.getName());
		cell.setCellStyle(header);

	}

	private void writeID(Entity node, XSSFSheet sheet) {

		Row head = sheet.createRow(0);
		Cell cell = head.createCell(0);
		cell.setCellStyle(header);
		cell.setCellValue("ID");

		cell = head.createCell(node.getChildren().size() + 1);
		cell.setCellStyle(header);
		cell.setCellValue("Parent ID");

		Row row = sheet.createRow(node.getID());
		cell = row.createCell(0);
		cell.setCellStyle(id);
		cell.setCellValue(node.getID());

		cell = row.createCell(node.getChildren().size() + 1);
		cell.setCellStyle(pId);
		if (node.getParent() != null)
			cell.setCellValue(node.getParent().getID());
		else
			cell.setCellValue(0);

	}

	private void writeToSheet(Entity child, XSSFSheet sheet, int col) {

		Row row = sheet.getRow(child.getParent().getID());
		if (row == null)
			row = sheet.createRow(child.getParent().getID());

		row.createCell(col + 1).setCellValue(child.getContext());
	}

	private void setColors() {

		header = workbook.createCellStyle();
		header.setAlignment(HorizontalAlignment.CENTER);
		header.setFillForegroundColor(IndexedColors.AQUA.index);
		header.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		id = workbook.createCellStyle();
		id.setAlignment(HorizontalAlignment.CENTER);
		id.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);
		id.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		pId = workbook.createCellStyle();
		pId.setAlignment(HorizontalAlignment.CENTER);
		pId.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		pId.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	}

	private void writeFile(File output) {
		try {
			FileOutputStream out = new FileOutputStream(output + "/" + fileName + ".xlsx");
			workbook.write(out);
			workbook.close();
		} catch (FileNotFoundException e) {
			System.out.print("File is currently open");
		} catch (IOException e) {
			System.out.print("File doesn't exist");
		}

	}
}
