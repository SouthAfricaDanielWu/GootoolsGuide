package org.geotools.tutorial.SharpFileRead;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.tutorial.style.StyleLab;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

/**
 * 此类为 读取当前选择文件夹下所有shp类型文件内容，
 * 并根据sharp 点、线、面实体分别进行渲染
 */
public class ReadAllSharpFile {

    private static ArrayList<File> ListFile=new ArrayList<>();
    private static String filePath;
    private static List<Layer> Layers=new ArrayList<>();
    private static  StyleLab styleLab;

    public static void main(String[] args) {
        styleLab=new StyleLab();
        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal=fileChooser.showOpenDialog(fileChooser);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            filePath=fileChooser.getSelectedFile().getAbsolutePath();
            //这个就是你选择的文件夹的路径
            System.out.println(filePath);
        }
        File file = new File(filePath);
        if (file != null) {
            GetAllFile(file);
        }else
        {
            return;
        }
        if(ListFile!=null)
        {
            Iterator <File> ListIterator= ListFile.iterator();
            while (ListIterator.hasNext())
            {
                GetSharp(ListIterator.next());
            }
        }
        //MapContent内容
        //创建映射内容，并将我们的shapfile添加进去
        MapContent mapContent = new MapContent();
        //设置容器的标题
        mapContent.setTitle("Appleyk's GeoTools");
        if (Layers!=null)
        {
            mapContent.addLayers(Layers);
            JMapFrame.showMap(mapContent);
        }

    }

    private static void GetSharp(File file) {
        try {
            // 思路   每一个shp文件获取到一个图层 继而将layer添加到集合中
            //根据路径创建图层过程
            //2.得到打开的文件的数据源
            FileDataStore store = null;
            store = FileDataStoreFinder.getDataStore(file);
            //3.设置数据源的编码，防止中文乱码
            ((ShapefileDataStore) store).setCharset(Charset.forName("UTF-8"));
            //4.以java对象的方式访问地理信息
            SimpleFeatureSource featureSource = store.getFeatureSource();
            //7.创建简单样式
            Style style =styleLab.createStyle2(featureSource);
            //Style style = SLD.createSimpleStyle(featureSource.getSchema());

            //8.显示【shapfile地理信息+样式】
            Layer layer = new FeatureLayer(featureSource, style);

            Layers.add(layer);
        }catch (Exception e)
        { e.printStackTrace(); }
    }

    public static void GetAllFile(File file) {
        File [] FileList=file.listFiles();
        for ( File fl : FileList)
        {
            if (fl.isFile())
            {
                if(fl.getName().endsWith(".shp"))
                {
                    System.out.println(fl);
                    ListFile.add(fl);
                }
                else {
                    if (fl.isDirectory()) {
                        GetAllFile(fl);
                    }
                }
            }
        }
    }


}
