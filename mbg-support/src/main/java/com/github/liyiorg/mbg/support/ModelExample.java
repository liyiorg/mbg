package com.github.liyiorg.mbg.support;

public class ModelExample<Model, Example> {

	private Model record;

	private Example example;

	public ModelExample() {
	}

	public ModelExample(Model record, Example example) {
		super();
		this.record = record;
		this.example = example;
	}

	public Model getRecord() {
		return record;
	}

	public void setRecord(Model record) {
		this.record = record;
	}

	public Example getExample() {
		return example;
	}

	public void setExample(Example example) {
		this.example = example;
	}

}
