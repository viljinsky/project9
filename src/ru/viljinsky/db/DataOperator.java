package ru.viljinsky.db;

import ru.viljinsky.db.DataField;
import ru.viljinsky.db.DataModule;
import ru.viljinsky.db.Dataset;

/**
 * Получение запросов добавления,изменения, удаления
 * @author vadik
 * DataOperator dop = new DataOperator(<tableName>);
 */
public class DataOperator {
    DataModule dm = DataModule.getInstance();
    static String insertPattern = "insert into %s (%s) values (%s)";
    static String deletePattern = "delete from %s where %s";
    static String updatePattern = "update %s set %s where %s";
    String insertSQL;

    public String getInsertSQL() {
        return insertSQL;
    }

    public String getUpdateSQL() {
        return updateSQL;
    }

    public String getDeleteSQL() {
        return deleteSQL;
    }
    SqlParams insertParams;
    String updateSQL;
    SqlParams updateParams;
    String deleteSQL;
    SqlParams deleteParams;
    Dataset dataset;
    String tableName;

    public DataOperator(Dataset dataset) throws Exception {
        this.dataset = dataset;
        this.tableName = dataset.getTableName();
        prepareInsertSQL();
        prepareUpdateSQL();
        prepareDeleteSQL();
    }

    public DataOperator(String tableName) throws Exception {
        this.tableName = tableName;
        this.dataset = dm.createTable(tableName);
        prepareInsertSQL();
        prepareUpdateSQL();
        prepareDeleteSQL();
    }

    public DatasetValues getInsertParamValues(Dataset ds, Object[] newRowset) {
        DatasetValues values = new DatasetValues();
        String columnName;
        for (Integer k : insertParams.keySet()) {
            columnName = insertParams.get(k).split("\\.")[1];
            values.put(k, newRowset[ds.getColumnIndex(tableName, columnName) - 1]);
        }
        return values;
    }

    public DatasetValues getUpdateParamValues(Dataset ds, Object[] oldRowset, Object[] newRowset) {
        DatasetValues values = new DatasetValues();
        String columnName;
        String prefix;
        System.out.println(updateParams);
        for (int k : updateParams.keySet()) {
            columnName = updateParams.get(k).split("\\.")[1];
            prefix = updateParams.get(k).split("\\.")[0];
            System.out.println(prefix+"."+columnName);
            switch (prefix) {
                case "new":
                    values.put(k, newRowset[ds.getColumnIndex(tableName, columnName) - 1]);
                    break;
                case "old":
                    values.put(k, oldRowset[ds.getColumnIndex(tableName, columnName) - 1]);
                    break;
            }
        }
        return values;
    }

    public DatasetValues getDeleteParamValues(Dataset ds, Object[] oldRowset) {
        DatasetValues values = new DatasetValues();
        String columnName;
        for (int k : deleteParams.keySet()) {
            columnName = deleteParams.get(k).split("\\.")[1];
            values.put(k, oldRowset[ds.getColumnIndex(tableName, columnName) - 1]);
        }
        return values;
    }

    private void prepareInsertSQL() {
        String fieldString = "";
        String valuesString = "";
        String columnName;
        insertParams = new SqlParams();
        int count = 0;
        for (DataField field : dataset.getFields()) {
            columnName = field.getColumnName();
            if (!fieldString.isEmpty()) {
                fieldString += ",";
                valuesString += ",";
            }
            fieldString += columnName;
            valuesString += "?";
            insertParams.put(count++, "new." + columnName);
        }
        insertSQL = String.format(insertPattern, tableName, fieldString, valuesString);
    }

    private void prepareUpdateSQL() {
        String whereString = "";
        String setString = "";
        SqlParams params = new SqlParams();
        Integer count = 0;
        String columnName;
        for (DataField field : dataset.getFields()) {
            if (!field.getTableName().equals(tableName)){
                continue;
            }
            
            if (!setString.isEmpty()) {
                setString += ",";
            }
            columnName = field.getColumnName();
            setString += columnName + "=?";
            params.put(count++, "new." + columnName);
        }
        
        for (DataField field : dataset.getFields()) {
            if (!field.getTableName().equals(tableName)){
                continue;
            }
            if (field.isPrimary()) {
                if (!whereString.isEmpty()) {
                    whereString += " and ";
                }
                columnName = field.getColumnName();
                whereString += columnName + "=?";
                params.put(count++, "old." + columnName);
            }
        }
        updateParams = params;
        updateSQL = String.format(updatePattern, tableName, setString, whereString);
    }

    private void prepareDeleteSQL() {
        String whereString = "";
        SqlParams params = new SqlParams();
        String columnName;
        int count = 0;
        for (DataField field : dataset.getFields()) {
            if (field.isPrimary()) {
                if (!whereString.isEmpty()) {
                    whereString += " and ";
                }
                columnName = field.getColumnName();
                whereString += field.getColumnName() + "=?";
                params.put(count++, "old." + columnName);
            }
        }
        deleteParams = params;
        deleteSQL = String.format(deletePattern, tableName, whereString);
    }
    
}
