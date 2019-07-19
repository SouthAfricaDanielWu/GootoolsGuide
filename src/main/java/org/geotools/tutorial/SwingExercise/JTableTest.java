package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import java.awt.*;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-19 11:25
 */
public class JTableTest extends JFrame {
    public static void main(String args[])
    {
        JTableTest jTableTest=new JTableTest();
        jTableTest.setVisible(true);

    }



    public JTableTest() throws HeadlessException {
        super();
        setTitle("表格");
        setBounds(100,100,240,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String []columnNames={"A","B"};
        String[][] tableValues={{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"}};
        JTable table=new JTable(tableValues,columnNames);
        JScrollPane scrollPane=new JScrollPane(table);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
    }
}
