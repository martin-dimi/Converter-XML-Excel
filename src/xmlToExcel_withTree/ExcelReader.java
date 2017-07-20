package xmlToExcel_withTree;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private XSSFWorkbook workbook;
	private Map<String, Integer> ids;

	ExcelReader() {
		ids = new HashMap<>();
	}

	public Entity read(XSSFWorkbook workbook) {
		this.workbook = workbook;
		Entity tree = null;
		tree = getTree(tree, workbook.getSheetAt(0));
		return tree;
	}

	public Entity getTree(Entity tree, XSSFSheet sheet) {

		if (tree == null) {
			tree = new Entity();
			tree.setName(sheet.getSheetName());
			tree.setID(1);
		}

		int rowNum = 1;
		if (ids.containsKey(tree.getName())) {
			rowNum = ids.get(tree.getName());
		}

		Row headers = sheet.getRow(0);
		Row row = sheet.getRow(rowNum);
		for (int colNum = 1; colNum < row.getLastCellNum() - 1; colNum++) {
			Entity child = new Entity();
			child.setName(headers.getCell(colNum).getStringCellValue());
			child.setContext(row.getCell(colNum).getStringCellValue());
			child.setID(tree.timesOccur(child.getName()));
			child.setParent(tree);
			String name = child.getName();

			if (ids.containsKey(name))
				ids.put(name, ids.get(name) + 1);
			else
				ids.put(name, 1);

			if (child.getContext().contains("\n")) {
				XSSFSheet childSheet = workbook.getSheet(child.getName());
				if (childSheet != null) {
					getTree(child, childSheet);
				}
			}
			tree.addChild(child);
		}
		return tree;
	}
}
