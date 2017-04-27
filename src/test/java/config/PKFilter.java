package config;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;

public class PKFilter implements IColumnFilter {

    private String pseudoKey = null;

    public PKFilter(String pseudoKey) {
        this.pseudoKey = pseudoKey;
    }

    /**
     * Tests whether or not the specified column of the specified tableName
     * should be included by this filter.
     *
     * @param tableName The tableName to be tested
     * @param column    The column to be tested
     * @return <code>true</code> if and only if the given parameter set should be included
     */
    @Override
    public boolean accept(String tableName, Column column) {
        return column.getColumnName().equalsIgnoreCase(pseudoKey);
    }
}