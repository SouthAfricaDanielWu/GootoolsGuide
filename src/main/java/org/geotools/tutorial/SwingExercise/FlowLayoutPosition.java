package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import java.awt.*;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-18 14:01
 */
public class FlowLayoutPosition extends JFrame {
    public FlowLayoutPosition() throws HeadlessException {
        setTitle("流布局");
        Container container=getContentPane();
        //align=2 靠右 / =1 中间 /=0左边
        setLayout(new FlowLayout(1,10,10));
        for (int i=0;i<10;i++)
        {
            container.add(new Button("button"+i));
        }
        setSize(300,200);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


    }
    public static void main(String args[])
    {
        new FlowLayoutPosition();
    }
}
