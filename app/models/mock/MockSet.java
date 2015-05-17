package models.mock;

import java.util.List;

import play.db.ebean.Model;

public class MockSet {

	public List<Model> models;
	
	public MockSet(List<Model> models) {
		super();
		this.models = models;
	}
}
