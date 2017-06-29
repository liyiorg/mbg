package com.github.liyiorg.mbg.bean;

import java.util.HashMap;

/**
 * 
 * @author LiYi
 *
 * @param <Model>
 * @param <Example>
 */
public class ModelExample<Model, Example> extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5803116520717573552L;

	public ModelExample(){
		super();
	}
	
	public ModelExample(Model record, Example example) {
		super();
		setRecord(record);
		setExample(example);
	}

	@SuppressWarnings("unchecked")
	public Model getRecord() {
		return (Model)this.get("record");
	}

	public void setRecord(Model record) {
		this.put("record", record);
	}
	
	@SuppressWarnings("unchecked")
	public Example getExample() {
		return (Example)this.get("example");
	}

	public void setExample(Example example) {
		this.put("example", example);
	}

}
