package org.geotools.tutorial.SwingExercise;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

/**
 * Description:
 * Name:JunFengGu
 * Date:2019-07-19 13:11
 */
public class JTreeTest extends JFrame {


    public static void main(String args[])
    {
        JTreeTest jTreeTest=new JTreeTest();
        jTreeTest.setVisible(true);



    }

    public JTreeTest() throws HeadlessException {
        super();
        setTitle("树节点");
        setBounds(100,100,240,150);
        DefaultMutableTreeNode root=new DefaultMutableTreeNode("根节点");
        DefaultMutableTreeNode nodeFirst=new DefaultMutableTreeNode("一级子节点A");
        root.add(nodeFirst);

        DefaultMutableTreeNode nodeSecond=new DefaultMutableTreeNode("二级子节点",false);
        nodeFirst.add(nodeSecond);

        root.add(new DefaultMutableTreeNode("一级子节点B"));

        //利用根节点直接创建树
        final JTree treeRoot=new JTree(root);
        getContentPane().add(treeRoot,BorderLayout.WEST);

        TreeSelectionModel treeSelectionModel=treeRoot.getSelectionModel();
        treeSelectionModel.setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        treeRoot.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (!treeRoot.isSelectionEmpty())
                {
                    TreePath[] treePaths=treeRoot.getSelectionPaths();
                    for (int i=0;i<treePaths.length;i++)
                    {
                        TreePath treePath2=treePaths[i];
                        Object[]path=treePath2.getPath();
                        for (int j=0;j<path.length;j++)
                        {
                            DefaultMutableTreeNode node;
                            node= (DefaultMutableTreeNode) path[j];
                            String s=node.getUserObject()+(j==(path.length-1)?"":"-->");
                            System.out.println(s);
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        });







//        //利用树模型创建树，默认判断方式
//        DefaultTreeModel defaultTreeModel=new DefaultTreeModel(root);
//        JTree Jtree=new JTree(defaultTreeModel);
//        getContentPane().add(Jtree,BorderLayout.CENTER);
//
//
//        //利用树模型创建树，默认判断方式
//        DefaultTreeModel TreeModelPointed=new DefaultTreeModel(root,true);
//        JTree JtreePointed=new JTree(TreeModelPointed);
//        getContentPane().add(JtreePointed,BorderLayout.EAST);


    }
}
