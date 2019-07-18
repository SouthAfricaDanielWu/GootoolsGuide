package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import java.awt.*;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-18 13:37
 */
public class DrawIcon implements Icon {
    private int width;
    private int height;

    public DrawIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.fillOval(x,y,width,height);
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }


    public static void main(String args[])
    {
        DrawIcon drawIcon=new DrawIcon(10,10);
        JFrame jf=new JFrame();
        Container container=jf.getContentPane();
        JLabel jLabel=new JLabel("test",drawIcon,SwingConstants.CENTER);
        container.add(jLabel);
        container.setVisible(true);
        jf.setVisible(true);//设置窗体可见
        jf.setSize(500,300);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//给予窗体关闭方式
    }
}
