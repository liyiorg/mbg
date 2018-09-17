package com.github.liyiorg.mbg.support.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/**
 * 
 * @author LiYi
 *
 */
public abstract class MbgExample<T extends MbgGeneratedCriteria>{
	
	protected List<T> oredCriteria = new ArrayList<T>();

	protected String orderByClause;

	protected boolean distinct;

	protected String databaseType;

	protected Long limitStart;

	protected Long limitEnd;

	protected String base_Column_List;

	protected String blob_Column_List;
	
	public List<T> getOredCriteria() {
        return oredCriteria;
    }

    public T or() {
        T criteria = createCriteriaInternal();
        criteria.prefix = MbgGeneratedCriteria.PREFIX_OR;
        criteria.prefixInner = MbgGeneratedCriteria.PREFIX_AND;
        oredCriteria.add(criteria);
        return criteria;
    }
    
    public void or(Consumer<T> consumer) {
        consumer.accept(or());
    }
    
    public T and() {
        T criteria = createCriteriaInternal();
        criteria.prefix = MbgGeneratedCriteria.PREFIX_AND;
        criteria.prefixInner = MbgGeneratedCriteria.PREFIX_OR;
        oredCriteria.add(criteria);
        return criteria;
    }
    
    public void and(Consumer<T> consumer) {
        consumer.accept(and());
    }

    protected abstract T createCriteriaInternal();


	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setLimitStart(Long limitStart) {
		this.limitStart = limitStart;
	}

	public Long getLimitStart() {
		return limitStart;
	}

	public void setLimitEnd(Long limitEnd) {
		this.limitEnd = limitEnd;
	}

	public Long getLimitEnd() {
		return limitEnd;
	}

	public void limit(Long limitStart, Long limitEnd) {
		this.limitStart = limitStart;
		this.limitEnd = limitEnd;
	}

	public void setBase_Column_List(String base_Column_List) {
		this.base_Column_List = base_Column_List;
	}

	public String getBase_Column_List() {
		return base_Column_List;
	}

	public void setBlob_Column_List(String blob_Column_List) {
		this.blob_Column_List = blob_Column_List;
	}

	public String getBlob_Column_List() {
		return blob_Column_List;
	}

	protected void clear() {
		orderByClause = null;
		distinct = false;
		limitStart = null;
		limitEnd = null;
		base_Column_List = null;
		blob_Column_List = null;
		oredCriteria.clear();
	}
}
