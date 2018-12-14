package Packet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DBworket {
    private String url = "jdbc:mysql://localhost:3306/odb";
    private String username;
    private String password;
    private Connection conn= null;
    private Statement st = null;
    private ResultSet answer = null;
    DBworket(String username, String password){
        this.username = username;
        this.password = password;
    }
    String getTablename(String name){
        String result = "";
        try {
            conn = DriverManager.getConnection(url,username,password);
            st = conn.createStatement();
            answer = st.executeQuery("select tablename from consept where name='"+name.trim()+"';");
            while(answer.next()){
                result = answer.getString(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String();
        } finally {
            try {
                conn.close();
                st.close();
                answer.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    String[] getConsepts(){
        List<String> result = new ArrayList<>(2);
        try {
            conn = DriverManager.getConnection(url,username,password);
            st = conn.createStatement();
            answer = st.executeQuery("select name from consept where type='measure';");
            while(answer.next()){
                result.add(answer.getString("name"));
            }
            return result.toArray(new String[]{});
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
        } finally {
            try {
                conn.close();
                st.close();
                answer.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    String[] getYears(String tablename){
        List<String> result = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url,username,password);
            st = conn.createStatement();
            answer = st.executeQuery("select distinct time from  "+tablename+" order by time;");
            result.add("All");
            while(answer.next()){
                result.add(answer.getString(1));
            }
            return result.toArray(new String[]{});
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
        } finally {
            try {
                conn.close();
                st.close();
                answer.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    String[] getEntities() {
        List<String> result = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
//            answer = st.executeQuery("select distinct geo from  " + tablename + ";");
            answer = st.executeQuery("select name from entities_country;");
            result.add("All");
            while (answer.next()) {
                result.add(answer.getString(1));
            }
            return result.toArray(new String[]{});
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
        } finally {
            try {
                conn.close();
                st.close();
                answer.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    void showTable(String query){
        ResultSet rs;
        try {
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        JTable table = null;
        try {
            table = new JTable(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, new JScrollPane(table));
    }
    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }
    String formQuery(String tablename, String year, String contryName){
        System.out.println(tablename);
        System.out.println(year);
        System.out.println(contryName);
        String columnName = getColumnName(tablename);
        if(year.trim().equals("All")&&contryName.trim().equals("All")){
            return "select c.name, t.time, t."+columnName.trim()+" from "+tablename+" as t left join entities_country" +
                    " as c on c.country=t.geo;";
        } else if(year.trim().equals("All")&&!contryName.trim().equals("All")){
            return "select c.name, t.time, t."+columnName.trim()+" from "+tablename+" as t left join entities_country" +
                    " as c on c.country=t.geo where geo=\""+getGeo(contryName.trim())+"\";";
        } else if(contryName.trim().equals("All")&&!year.trim().equals("All")){
            return "select c.name, t.time, t."+columnName.trim()+" from "+tablename+" as t left join entities_country" +
                    " as c on c.country=t.geo where time=\""+year.trim()+"\";";
        } else{
            System.out.println("select c.name, t.time, t."+columnName.trim()+" from "+tablename+" as t left join entities_country" +
                    " as c on c.country=t.geo where time=\""+year.trim()+
                    " and geo=\""+getGeo(contryName.trim())+"\";");
            return "select c.name, t.time, t."+columnName.trim()+" from "+tablename+" as t left join entities_country" +
                    " as c on c.country=t.geo where time=\""+year.trim()+
                    "\" and geo=\""+getGeo(contryName.trim())+"\";";
        }

        /*
        SELECT c.name,d.time,d.energy_production_total FROM odb.energy_production_total as d
left join entities_country as c on c.country=d.geo;
         */
    }
    private String getColumnName(String tableName){
        String result = "";
        try {
            conn = DriverManager.getConnection(url,username,password);
            st = conn.createStatement();
            answer = st.executeQuery(
                    "SELECT dpColumnName FROM consept where tablename='"+tableName+"';");
            while(answer.next()){
                result = answer.getString(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String();
        } finally {
            try {
                conn.close();
                st.close();
                answer.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private String getGeo(String contryName){
        ResultSet geo = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
            geo = st.executeQuery("select country from entities_country where name=\""+
                    contryName.trim()+"\"");
            geo.next();
            return geo.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String();
        } finally {
            try {
                conn.close();
                st.close();
                geo.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
