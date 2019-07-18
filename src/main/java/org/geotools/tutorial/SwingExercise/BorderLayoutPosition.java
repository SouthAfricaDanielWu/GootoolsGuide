package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import java.awt.*;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-18 14:13
 */
public class BorderLayoutPosition extends JFrame {
    String[] border={
            BorderLayout.CENTER,
            BorderLayout.NORTH,
            BorderLayout.SOUTH,
            BorderLayout.WEST,
            BorderLayout.EAST,
    };
    String[] borderName={
            "CENTER",
            "NORTH",
            "SOUTH",
            "WEST",
            "EAST",
    };


    public BorderLayoutPosition(){
        setTitle("边界布局管理器");
        Container c=getContentPane();
        setLayout(new BorderLayout());
        for(int i=0;i<border.length;i++)
        {
            c.add(border[i],new JButton(borderName[i]));
        }
        setSize(350,200);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void main(String args[])
    {
       new BorderLayoutPosition();
    }
}
