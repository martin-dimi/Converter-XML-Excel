package xmlToExcel_withTree;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {
	
	private String context;
	private Entity node;
	
	SAXHandler(Entity tree){
		this.node = tree;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(node == null)
			node = new Entity(qName, 1, null);
		
		else {
			Entity child = new Entity(qName, node.timesOccur(qName) + 1, node);
			node.addChild(child);
			node = child;
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		node.setContext(context);
		
		if(node.getParent() != null)
			node = node.getParent();
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		context = new String(ch, start, length);
	}
	
	public Entity getTree() {
	//	fixTree(node);
		return node;
	}
	
	private void fixTree(Entity node) {
		
		List<Entity> children = node.getChildren();
		
		for(int i = 0; i < children.size(); i++) {
			Entity child = children.get(i);
			Entity temp = new Entity(children.get(i).getName() + "s", 1, node);
			
			for(int j = i + 1; j < node.getChildren().size(); j++) {
				if(child.getName().equals(children.get(j).getName())) {
					temp.addChild(children.get(j));
					node.getChildren().remove(j);
					j--;
				}	
			}
			if(child.hasChildren())
				fixTree(child);
			if (temp.hasChildren()) {
				temp.getChildren().add(0, child);
				node.getChildren().remove(i);
				node.getChildren().add(i, temp);
			}
				
		}
		
	}

}
