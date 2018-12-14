package Packet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyPanel1 extends JPanel {
    private JComboBox<String> consept;
    private JComboBox<String> year;
    private JComboBox<String> entity;
    private JButton button;
    private JButton nextButton;
    private ActionListener listener;
    MyPanel1(String[] conseptArg){
        setLayout(new GridLayout(4,2));
        consept = new JComboBox<>(conseptArg);
        add(new JLabel("Choose concepts"));
        add(consept);
        consept.addActionListener(e->setSearchDisable());
    }
    String getConsept(){
        return (String)consept.getSelectedItem();
    }
    void setYears(String[] yearArg){
        year = new JComboBox(yearArg);
        add(new JLabel("Choose years"));
        add(year);
    }
    void resetYears(String[] yearArg){
        year.removeAllItems();
        for(String elem: yearArg) {
            year.addItem(elem);
        }
    }
    void resetEntities(String[] entityArg){
        entity.removeAllItems();
        for(String elem: entityArg) {
            entity.addItem(elem);
        }
    }
    void setEntityies(String[] entityies){
        entity = new JComboBox<>(entityies);
        add(new Label("Choose entities"));
        add(entity);
    }
    void setButton(){
        nextButton = new JButton("next");
        add(nextButton);
        nextButton.addActionListener(listener);
        button = new JButton("Search");
        button.addActionListener(listener);
        button.setEnabled(false);
        add(button);
    }
    void setButtonListeners(ActionListener listener){
        this.listener = listener;
    }
    String[] getAllResult(){
        String[] result = new String[2];
        result[0]=(String) year.getSelectedItem();
        result[1]=(String) entity.getSelectedItem();
        return result;
    }
    void setSearchDisable(){
        button.setEnabled(false);
    }
    void setSearchEnable(){
        button.setEnabled(true);
    }
}
