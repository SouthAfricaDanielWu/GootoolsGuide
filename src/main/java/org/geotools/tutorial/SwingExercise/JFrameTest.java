package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import java.awt.*;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-18 11:03
 */
public class JFrameTest extends JFrame {

    public void CrateFrame(String title)
    {
        JFrame jFrame=new JFrame(title);
        Container container= jFrame.getContentPane();
        JLabel jLabel=new JLabel("Test");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(jLabel);
        container.setBackground(Color.YELLOW);
        jFrame.setVisible(true);//设置窗体可见
        jFrame.setSize(500,300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//给予窗体关闭方式
    }

    public static void main(String args[])
    {
        new JFrameTest().CrateFrame("Title");
    }
}
