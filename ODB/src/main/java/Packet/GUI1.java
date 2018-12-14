package Packet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI1 extends JFrame implements ActionListener {
    private MyPanel1 panel;
    private DBworket worket;
    private String tablename="economic_growth_over_the_past_10_years";
    private JMenuBar menuBar;
    private JMenuItem regist, exit;

    public GUI1(){
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300,200);
        menuBar = new JMenuBar();
        regist = new JMenuItem("Registration");
        regist.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "This function will be added\n maybe."));
        menuBar.add(regist);
        exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        menuBar.add(exit);
        setJMenuBar(menuBar);

        worket = new DBworket("user","password");
        panel = new MyPanel1(worket.getConsepts());
        panel.setButtonListeners(this);
        add(panel);
        panel.setYears(new String[]{"1890","2010"});
        panel.setEntityies(new String[]{"urk","usa"});
        panel.setButton();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand()=="next"){
            panel.setSearchEnable();
            System.out.println(panel.getConsept());
            System.out.println(tablename=worket.getTablename(panel.getConsept()));
            panel.resetYears(worket.getYears(tablename));
            panel.resetEntities(worket.getEntities());

        } else if(e.getActionCommand()=="Search"){
            String[] result = panel.getAllResult();
//            System.out.print("["+tablename+",");
//            for(String elem: result){
//                System.out.print(elem+",");
//            }
//            System.out.println("]");
//            System.out.println(worket.formQuery(tablename,result[0],result[1]));
            worket.showTable(worket.formQuery(tablename,result[0],result[1]));

        }
    }


}
