/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.db;

import java.io.IOException;
import java.sql.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import ru.viljinsky.db.form.*;


class TableList extends ArrayList<Dataset>{
    
}

public class DataModule {
    private static DataModule instance = null;
    private static Connection connection = null;
    
    public static String USER_NAME = "root";
    public static String PASSWORD = "root";
    public static String SCHEMA = "schedule";
    
    private static TableList tableList = null;
    
    protected boolean active = false;
    
    
    public Boolean isActive(){
        return active;
    }
    
    private DataModule(){
    }
    
    public static DataModule getInstance(){
        if (instance == null){
            instance = new DataModule();
        }
        return instance;
    }
    
    public boolean open() throws DataException{
        active=false;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/"+SCHEMA, USER_NAME, PASSWORD);
            active = true;
        } catch (ClassNotFoundException e){
            throw new DataException("Class not found \n"+e.getMessage());
        } catch (SQLException e){
            throw new DataException("Ошибка при подключении к базе данных \n"+e.getMessage()); 
        }
        return true;
    }
    
    public void close() throws DataException{
        try{
            if (connection!=null) connection.close();
            active=false;
        } catch (Exception e){
            throw new DataException(e.getMessage());
        }
    }
    /**
     * Возвращает спсок имён всех представлений базы данных
     * @return
     * @throws Exception 
     */
    public String[] getViewNames() throws Exception{
        List<String> result = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = connection.createStatement();
            rs=stmt.executeQuery("select table_name from information_schema.tables where table_schema='"+SCHEMA+"' and table_type='VIEW'");
            while (rs.next()){
                result.add(rs.getString("table_name"));
            }                        
        } finally{
            if (rs!=null) rs.close();
            if (stmt!=null) stmt.close();;
        }
        return result.toArray(new String[result.size()]);
    }
    
    /**
     * Возвращает список всех имён таблиц
     * @return
     * @throws Exception 
     */
    public String[] getTableNames() throws Exception{
        List<String> result = new ArrayList<>();
        Statement stmt=null;
        ResultSet rs = null;
        try{
            stmt = connection.createStatement();
            rs=stmt.executeQuery("select table_name from information_schema.tables where table_schema='"+SCHEMA+"' and table_type='BASE TABLE'");
            while (rs.next()){
                result.add(rs.getString("table_name"));
            }
        } finally {
            if (rs!=null) rs.close();
            if (stmt!=null) stmt.close();
        }
        return result.toArray(new String[result.size()]);
    }
    /**
     * Возвращает список датасетов соответсвующих таблицам базы данных
     * @return
     * @throws Exception 
     */
    public List<Dataset> getTables() throws Exception{
        Dataset t;
        if (tableList==null){
            tableList = new TableList();
        
        
            for (String tableName:getTableNames()){
                t = new Dataset(instance);
                t.setTableName(tableName);
                updateFieldUsage(t);
                tableList.add(t);
            }
        }
        return tableList;
    }
    
    public DatasetForm[] getDatasetForms() throws Exception{
        List<DatasetForm> result = new ArrayList<DatasetForm>();
        DatasetForm form;
        for (String tableName:getTableNames()){
            form = DatasetForm.createForm(this, tableName);
            if (!form.isEmpty())
                result.add(form);
        }
        return result.toArray(new DatasetForm[result.size()]);
    }

    public void updateFieldUsage(Dataset dataset) throws Exception{
        Statement stmt=null;
        ResultSet rs = null;
        DataField f;
        try{
            stmt=connection.createStatement();
            rs = stmt.executeQuery("select constraint_name,table_name,column_name,referenced_table_name,referenced_column_name from information_schema.key_column_usage where table_schema= '"+SCHEMA+"' and table_name='"+dataset.getTableName()+"'");
            while (rs.next()){
                f = dataset.getFieldByName(rs.getString("column_name"));
                if (rs.getString("constraint_name").equals("PRIMARY")){
                    f.primaryKey= true;
                }
                if (rs.getString("referenced_table_name")!=null){
                    f.referencedTable=rs.getString("referenced_table_name");
                    f.referencedColumn=rs.getString("referenced_column_name");
                }
            }
        } finally {
            if(rs!=null) rs.close();
            if (stmt!=null) stmt.close();
        }
    }
    public DataField[] getDataFields(String sql)throws SQLException{
        DataField[] result = {};
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmeta = rs.getMetaData();
            result = new DataField[rsmeta.getColumnCount()];
            for (int i=0;i<result.length;i++){
                result[i]= new DataField(this, rsmeta, i+1);
            }
            
            
            
        } finally {
            if (rs!=null) rs.close();
            if (stmt!=null) stmt.close();
        }
        
        return result;
    }

    /**
     * Создание пустого датасета;
     * для открытия датасета необходимо задать имя таблицы или запросж
     * @return 
     */
    public Dataset createDataset(){
        return new Dataset(instance);
    }
    
    /**
     *  Создание датасета от произвольного датасета на основе запроса
     * @param sql - запрос к базе данных
     * @return
     * @throws Exception 
     */
    
    public Dataset createDataset(String sql) throws Exception{
        Dataset result = new Dataset(instance);
        result.setSQL(sql);
        return result;
    }
    
    /**
     * Создание датасет на основе запроса с пивязкой к конкретной талице
     * @param tableName - имя таблицы
     * @param sql - запрос соответсвующей таблице
     * @return - датасет
     * @throws Exception:
     *  - отсутсвует таблица с указанным именем
     *  - ошибка в запросе
     */
    public Dataset createTable(String tableName,String sql) throws Exception{
        Dataset result = new Dataset(instance);
        result.tableName = tableName;
        result.setSQL(sql);
        return result;
    }
    
    /**
     * Создание датасет соответсвующего таблице базы данных
     * @param tableName - имя таблицы
     * @return - датасет
     * @throws Exception:
     * 
     */
    public Dataset createTable(String tableName) throws Exception{
        Dataset result = new Dataset(instance);
        try{
            result.setTableName(tableName);
        } catch (Exception e){
            throw new Exception("Ошибка при создании таблицы "+tableName+"\n"+e.getMessage());
        }
        return result;
    }
    
    public Connection getConnection(){
        return connection;
    }
    
    /**
     * Проверка соедиенеия на наличия запущенных трансакций
     * @return
     * @throws SQLException 
     */
    public boolean isAutoCommit() throws SQLException{
        return connection.getAutoCommit();
    }
    
    @Deprecated
    public void setAutoCommit(Boolean value) throws SQLException{
        connection.setAutoCommit(value);
    }
    /**
     * Запускает транзакцию
     * @throws Exception 
     */
    public void begin() throws Exception{
        connection.setAutoCommit(false);
    }
    /**
     * Завершает транзакцию с сохранением изменений
     * @throws SQLException 
     */
    public void commit() throws SQLException{
        connection.commit();
        connection.setAutoCommit(false);
    }
    /**
     * Завершает транзакцию с откатом изменений с начала транзакции
     * @throws SQLException 
     */
    public void rallback() throws SQLException{
        connection.rollback();
        connection.setAutoCommit(false);
    }
    
    /**
     * Выполенеие запроса без параметров
     * @param sql
     * @return
     * @throws Exception 
     */
    public boolean execute(String sql) throws Exception{
        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage()+"\n"+sql);
            throw new Exception(e);
        } finally {
            if (stmt!=null) stmt.close();;
        }
    }
    
    /**
     * Выполнение запроса с параметрами
     * @param sql
     * @param values
     * @return
     * @throws Exception 
     */
    public boolean execute(String sql,HashMap<Integer,Object> values) throws Exception{
        try{
            PreparedStatement pstm = connection.prepareStatement(sql);
            for (Integer k:values.keySet()){
                pstm.setObject(k+1, values.get(k));
            }
            return pstm.execute();
        } catch (SQLException e){
            System.err.println(sql);
            System.err.println(values);
            System.err.println(e.getMessage());
            throw new Exception("Ошибка при выполнении Datamodule.execute\n '"+e.getMessage()+"'");
        }
    }
}
