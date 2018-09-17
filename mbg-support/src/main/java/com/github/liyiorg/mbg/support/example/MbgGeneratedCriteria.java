package com.github.liyiorg.mbg.support.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author LiYi
 *
 */
public abstract class MbgGeneratedCriteria {

	protected List<Criterion> criteria;
	
	protected static final String PREFIX_OR = "or";

    protected static final String PREFIX_AND = "and";

    protected String prefix;

    protected String prefixInner;
    
    public void reversePrefix() {
        prefixInner = PREFIX_OR.equals(prefixInner) ? PREFIX_AND : PREFIX_OR;
    }

	protected MbgGeneratedCriteria() {
		super();
		criteria = new ArrayList<Criterion>();
	}

	public boolean isValid() {
		return criteria.size() > 0;
	}

	public List<Criterion> getAllCriteria() {
		return criteria;
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	public String getPrefix() {
	        return prefix;
	}

	protected void addCriterion(String condition) {
		if (condition == null) {
			throw new RuntimeException("Value for condition cannot be null");
		}
		criteria.add(new Criterion(condition).prefix(prefixInner));
	}

	protected void addCriterion(String condition, Object value) {
        addCriterion(condition, value, condition);
    }
	
	protected void addCriterion(String condition, Object value, String property) {
		if (value == null) {
			throw new RuntimeException("Value for " + property + " cannot be null");
		}
		criteria.add(new Criterion(condition, value).prefix(prefixInner));
	}

	protected void addCriterion(String condition, Object value1, Object value2, String property) {
		if (value1 == null || value2 == null) {
			throw new RuntimeException("Between values for " + property + " cannot be null");
		}
		criteria.add(new Criterion(condition, value1, value2).prefix(prefixInner));
	}

	protected void addCriterionForJDBCDate(String condition, Date value, String property) {
		if (value == null) {
			throw new RuntimeException("Value for " + property + " cannot be null");
		}
		addCriterion(condition, new java.sql.Date(value.getTime()), property);
	}

	protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
		if (values == null || values.size() == 0) {
			throw new RuntimeException("Value list for " + property + " cannot be null or empty");
		}
		List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
		Iterator<Date> iter = values.iterator();
		while (iter.hasNext()) {
			dateList.add(new java.sql.Date(iter.next().getTime()));
		}
		addCriterion(condition, dateList, property);
	}

	protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
		if (value1 == null || value2 == null) {
			throw new RuntimeException("Between values for " + property + " cannot be null");
		}
		addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
	}

	protected void addCriterionForJDBCTime(String condition, Date value, String property) {
		if (value == null) {
			throw new RuntimeException("Value for " + property + " cannot be null");
		}
		addCriterion(condition, new java.sql.Time(value.getTime()), property);
	}

	protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
		if (values == null || values.size() == 0) {
			throw new RuntimeException("Value list for " + property + " cannot be null or empty");
		}
		List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
		Iterator<Date> iter = values.iterator();
		while (iter.hasNext()) {
			timeList.add(new java.sql.Time(iter.next().getTime()));
		}
		addCriterion(condition, timeList, property);
	}

	protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
		if (value1 == null || value2 == null) {
			throw new RuntimeException("Between values for " + property + " cannot be null");
		}
		addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
	}

    public AddCriterion addCriterions() {
        return new AddCriterion(this);
    }

}
