
package ru.viljinsky.db;

import ru.viljinsky.db.DataField;
import ru.viljinsky.db.DataModule;
import ru.viljinsky.db.Dataset;

/**
 *
 * @author vadik
 */
public class FullSQL {
    private static DataModule dm = DataModule.getInstance();


    private static String getStr(String tableName) throws Exception {
        String rs = "";
        Dataset dataset = dm.createTable(tableName);
        for (DataField field : dataset.getFields()) {
            if (!rs.isEmpty()) {
                rs += ",\n    ";
            }
            rs += tableName + "." + field.getColumnName();
        }
        return rs;
    }

    public static String getFullSQL(String tableName) throws Exception {
        Dataset dataset = dm.createTable(tableName);
        String str = "";
        String str2 = "from " + tableName;
        for (DataField field : dataset.getFields()) {
            if (!str.isEmpty()) {
                str += ",";
            }
            str += "\n    " + tableName + "." + field.getColumnName();
            if (!field.getReferencedTable().isEmpty()) {
                str += ",\n    " + getStr(field.getReferencedTable());
                str2 += "\n    left join " + field.getReferencedTable() + " on \n        " + tableName + "." + field.getColumnName() + "=" + field.getReferencedTable() + "." + field.getReferencedColumn();
            }
        }
        return "select " + str + "\n" + str2 + ";";
    }
    
}
