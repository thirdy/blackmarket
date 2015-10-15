package io.jexiletools.es.model.json;

import java.util.List;

public class Info {
//	public Info() {
//	}
	String	descrText;
    List<String>  flavourText;
    String  fullName  ;
    String  fullNameTokenized;
    String  icon;
    String  name;
    String  typeLine;
    
	@Override
	public String toString() {
		return "Info [descrText=" + descrText + ", flavourText=" + flavourText + ", fullName=" + fullName
				+ ", fullNameTokenized=" + fullNameTokenized + ", icon=" + icon + ", name=" + name + ", typeLine="
				+ typeLine + "]";
	}
	public String getDescrText() {
		return descrText;
	}
	public void setDescrText(String descrText) {
		this.descrText = descrText;
	}
	public List<String> getFlavourText() {
		return flavourText;
	}
	public void setFlavourText(List<String> flavourText) {
		this.flavourText = flavourText;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getFullNameTokenized() {
		return fullNameTokenized;
	}
	public void setFullNameTokenized(String fullNameTokenized) {
		this.fullNameTokenized = fullNameTokenized;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeLine() {
		return typeLine;
	}
	public void setTypeLine(String typeLine) {
		this.typeLine = typeLine;
	}


    

	
}