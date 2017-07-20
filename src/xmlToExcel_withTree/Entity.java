package xmlToExcel_withTree;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

public class Entity {
	
	private String name;
	private int id;
	private Entity parent;
	private List<Entity> children;
	private String context;
	
	Entity(){
		this.name = "";
		this.parent = null;
		this.children = new ArrayList<>();
	}
	
	Entity(String name, int id, Entity parent){
		this.name = name;
		this.id = id;
		this.parent = parent;
		this.children = new ArrayList<>();
	}
	
	public boolean hasChildren() {
		return (children.size() == 0) ? false : true;
	}
	
	public void addChild(Entity child) {
		children.add(child);
	}
	
	public String getName() {
		return name;
	}
	
	public int getID() {
		return id;
	}
	
	public Entity getParent() {
		return parent;
	}
	
	public void setContext(String context) {
		this.context = context;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContext() {
		return context;
	}
	
	public List<Entity> getChildren(){
		return children;
	}
	
	public int timesOccur(String name) {
		int times = 0;
		
		for(Entity entity : children) 
			if(entity.getName().equals(name))
				times++;
		
		return times;
	}
}
