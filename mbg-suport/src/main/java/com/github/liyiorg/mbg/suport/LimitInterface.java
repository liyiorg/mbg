package com.github.liyiorg.mbg.suport;

public interface LimitInterface {

    public void setLimitStart(Long limitStart);

    public Long getLimitStart();

    public void setLimitEnd(Long limitEnd);

    public Long getLimitEnd();
    
    public void setOrderByClause(String orderByClause);
    
    public String getOrderByClause();
    
    public String getDatabaseType();
    
    public void setDatabaseType(String databaseType);
}
