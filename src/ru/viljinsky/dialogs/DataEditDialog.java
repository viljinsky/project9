/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ru.viljinsky.db.*;
/**
 *
 * @author вадик
 */
public class DataEditDialog extends BaseDialog{
    Dataset dataset;
    DataEditPanel dataEditPanel;
    HashMap<String,JComponent> controls;
    
    class SelectButton extends JButton implements ActionListener{
        String referencedTableName;
        String referencedColumnName;
        String columnName;
        
        public SelectButton(String columnName,String refrencedTableName,String referencedColumnName) {
            super("...");
            this.columnName=columnName;
            this.referencedTableName=refrencedTableName;
            this.referencedColumnName=referencedColumnName;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            HashMap<String,Object> keyValues;
            keyValues = new HashMap<>();
            JComponent c = controls.get(columnName);
            JTextField f = (JTextField)c;
            Object value;
            value = (f.getText().isEmpty()?null:Integer.valueOf(f.getText()));
            
            keyValues.put(referencedColumnName, value);
            try{
                DataSelectDialog dlg = new DataSelectDialog();
                dlg.setTitle("select from '"+referencedTableName+"'");
                dlg.setTableName(referencedTableName);
                dlg.setValues(keyValues);
                dlg.setVisible(true);
            if (dlg.getResult()==RESULT_OK){
                keyValues = dlg.getValues();
                System.out.println(keyValues);
                for (String k:keyValues.keySet()){
                    if (keyValues.get(k)==null){
                        f.setText("");
                    } else
                        f.setText(keyValues.get(k).toString());
                    break;
                }
                System.out.println("OK");
            }
            } catch (Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
        
    }
    
    class DataEditPanel extends JPanel{
        
        public DataEditPanel(){
//            controls = new HashMap<>();
            
            BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(boxLayout);
        }
        
        public void addColumn(DataField field){
            String columnName = field.getColumnName();
            
            Box box = Box.createHorizontalBox();
            
            JLabel label = new JLabel(columnName);
            box.add(label);
            
            box.add(Box.createHorizontalStrut(6));
            
            JTextField text = new JTextField(20);
            box.add(text);
            
            if (field.isReferenced()){
                SelectButton btn = new SelectButton(field.getColumnName(), field.getReferencedTable(),field.getReferencedColumn());
                box.add(btn);
            }
            
            box.setMaximumSize(new Dimension(Short.MAX_VALUE,20));
            
            add(box);
            add(Box.createVerticalStrut(12));
            controls.put(columnName, text);
        }
    }

    @Override
    public void initComponents(Container content) {
        super.initComponents(content); //To change body of generated methods, choose Tools | Templates.
        controls = new HashMap<>();
        dataEditPanel = new DataEditPanel();
        content.add(dataEditPanel,BorderLayout.CENTER);
    }
    
    
    public void setDataSet(Dataset dataset){
        this.dataset=dataset;
        for (int i=0;i<dataset.getColumnCount();i++){
            dataEditPanel.addColumn(dataset.getColumn(i));
        }
        dataEditPanel.add( Box.createVerticalGlue());
        dataEditPanel.setBorder(new EmptyBorder(12,6 ,12, 6));
        
    }
    
    public void setValues(DataRowset rowset){
        JTextField  textField;
        for (String fieldName:rowset.getFieldNames()){
            JComponent c= controls.get(fieldName);
            if (c!=null){
                textField = (JTextField)c;
                if (rowset.getValue(fieldName)!=null)
                    textField.setText(rowset.getValue(fieldName).toString());
            }
        }
    }
    
    public Object[] getValues(){
        JComponent c;
        JTextField f;
        String value;
        Object[] result = new Object[controls.size()];
        int k=0;
        for (int i=0;i<dataset.getColumnCount();i++){
            c=controls.get(dataset.getColumn(i).getColumnName());
            if (c!=null){
                f=(JTextField)c;
                value = f.getText();
                result[k]=(value.isEmpty()?null:value.toString());
                k+=1;
            }
        }
        
        return result;
    }
    
}
