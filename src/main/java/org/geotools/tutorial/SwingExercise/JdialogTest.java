package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-18 11:13
 */
public class JdialogTest extends JFrame {
    public static void main(String args[])
    {
        new JdialogTest();
    }
    public JdialogTest()
    {
        JFrame jFrame=new JFrame();
        Container container=jFrame.getContentPane();
        container.setLayout(null);
        JLabel jLabel=new JLabel("Test");
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(jLabel);
        container.setBackground(Color.YELLOW);

        JButton jButton=new JButton("弹出dialog");
        jButton.setBounds(10,10,100,21);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MyDialog(JdialogTest.this).setVisible(true);
            }
        });
        container.add(jButton);
        container.setVisible(true);
        jFrame.setVisible(true);//设置窗体可见
        jFrame.setSize(500,300);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//给予窗体关闭方式

    }

}
class MyDialog extends JDialog
{
    public MyDialog(Frame frame)
    {
        super(frame,"New Dialog",true);
        Container container=new Container();
        container.add(new JLabel("对话框"));
        setBounds(120,120,100,100);
    }
}
