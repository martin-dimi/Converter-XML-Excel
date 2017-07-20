package xmlToExcel_withTree;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

public class Converter {
	
	public static boolean completed;

	public static void writeToXML(File excel, File output) {
		try {
			completed = false;
			System.out.println("Engaging: excel reader");
			XSSFWorkbook workbook = new XSSFWorkbook(excel);
			ExcelReader reader = new ExcelReader();
			System.out.println("File open");

			Entity tree = reader.read(workbook);
			System.out.println("File read, tree created");

			XMLWriter writer = new XMLWriter();
			writer.write(tree, output);
			System.out.println("XML written");
			System.out.println("---------------------------");
			completed = true;

		} catch (IOException | InvalidFormatException e) {
			System.out.println("Error: File not found");
		}

	}

	public static void writeToExcel(File xml, File output) {

		try {
			completed = false;
			System.out.println("Engaging: parsing Tree");
			Entity tree = null;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			SAXHandler handler = new SAXHandler(tree);
			parser.parse(xml, handler);
			tree = handler.getTree();
			System.out.println("Parse completed");

			System.out.println("Engaging: writing excel file");
			ExcelWriter writer = new ExcelWriter();
			writer.writeToExcel(tree, output);
			System.out.println("Write completed");
			System.out.println("---------------------------");
			completed = true;

		} catch (SAXException e) {
			System.out.println("Error 1: XML is invalid");
		} catch (ParserConfigurationException e) {
			System.out.println("Error 2: Parse failed");
		} catch (IOException e) {
			System.out.println("Error 3: ioException");
		}
	}

}
