/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.geotools.tutorial.style;

import java.awt.Color;
import java.io.File;
import java.nio.charset.Charset;

import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.dialog.JExceptionReporter;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.geotools.xml.styling.SLDParser;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
    /**
     * 该类主要内容为 矢量图层的渲染 分为以下三种方式
     * ①SLD文件渲染
     * ②用户自定义渲染
     * ③对点、线、面实体加以区分，分别渲染
     */
public class StyleLab {

    static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    public static void main(String[] args) throws Exception {
        StyleLab me = new StyleLab();
        me.displayShapefile();
    }

    // docs end main

    // docs start display
    /**
     * Prompts the user for a shapefile (unless a filename is provided on the command line; then
     * creates a simple Style and displays the shapefile on screen
     * 流程：
     * 1.根据选择框拿到file的路径，
     * 2.根据路径，利用FileDataStoreFinder拿到文件数据源store
     * 3.利用store获取到特征数据源featureSource
     * 4.根据路径file和featureSource，构建渲染Style,返回一个Style类文件
     * 5.利用返回的一个Style类文件以及featureSource构建图层
     * 6.添加图层进行显示
     */
    private void displayShapefile() throws Exception {
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        ((ShapefileDataStore) store).setCharset(Charset.forName("UTF-8"));
        FeatureSource featureSource = store.getFeatureSource();

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("StyleLab");

        // Create a basic Style to render the features
        Style style = createStyle(file,featureSource);

        // Add the features and the associated Style object to
        // the MapContent as a new Layer
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);

        // Now display the map
        JMapFrame.showMap(map);
    }

    // docs end display

    // docs start create style
    /**
     * 利用SLD文件进行渲染，如果文件夹存在渲染文件，就以文件为标准进行渲染，否则弹框选择
     * Create a Style to display the features. If an SLD file is in the same directory as the
     * shapefile then we will create the Style by processing this. Otherwise we display a
     * JSimpleStyleDialog to prompt the user for preferences.
     */
    private Style createStyle(File file, FeatureSource featureSource) {
        File sld = toSLDFile(file);
        if (sld != null) {
            //若存在与shp文件同名的sld文件，就以当前方式进行渲染，返回一个style类文件
            return createFromSLD(sld);
        }
        //若sld文件不存在，进行弹框选择，返回一个选择后的文件

        SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
        return JSimpleStyleDialog.showDialog(null, schema);
    }

    // docs end create style

    // docs start sld
    /** Figure out if a valid SLD file is available.
     * 判断是否有与shp文件同名的sld文件
     * */
    public File toSLDFile(File file) {
        String path = file.getAbsolutePath();
        String base = path.substring(0, path.length() - 4);
        String newPath = base + ".sld";
        File sld = new File(newPath);
        if (sld.exists()) {
            return sld;
        }
        newPath = base + ".SLD";
        sld = new File(newPath);
        if (sld.exists()) {
            return sld;
        }
        return null;
    }

    /** Create a Style object from a definition in a SLD document
     * 解析sld文件，创建style
     * */
    private Style createFromSLD(File sld) {
        try {
            SLDParser stylereader = new SLDParser(styleFactory, sld.toURI().toURL());
            Style[] style = stylereader.readXML();
            return style[0];

        } catch (Exception e) {
            JExceptionReporter.showDialog(e, "Problem creating style");
        }
        return null;
    }

    // docs end sld

    // docs start alternative
    /**
     * 创建方式第二种，根据所添加shp文件的形式进行判断，对点、线、面 实体分别渲染
     * This methods works out what sort of feature geometry we have in the shapefile
     * and then delegates to an appropriate style creating method.
     * note:
     * 1.Each of the geometry specific methods is creating a type of Symbolizer: the class that controls how features are rendered
     * 2.Each method wraps the symbolizer in a Rule, then a FeatureTypeStyle, and finally a Style
     * 3.In real life, it is common to have more than one Rule in a FeatureTypeStyle. For example, we might create one rule to draw features when the map is zoomed out, and another for when we are displaying fine details.
     */
    public  Style createStyle2(FeatureSource featureSource) {
        SimpleFeatureType schema = (SimpleFeatureType) featureSource.getSchema();
        Class geomType = schema.getGeometryDescriptor().getType().getBinding();

        if (Polygon.class.isAssignableFrom(geomType)
                || MultiPolygon.class.isAssignableFrom(geomType)) {
            return createPolygonStyle();

        } else if (LineString.class.isAssignableFrom(geomType)
                || MultiLineString.class.isAssignableFrom(geomType)) {
            return createLineStyle();

        } else {
            return createPointStyle();
        }
    }

    // docs end alternative

    /** 渲染多边形
     * Create a Style to draw polygon features with a thin blue outline and a cyan fill */
    private Style createPolygonStyle() {

        // create a partially opaque outline stroke
        //创建一个部分不透明的外轮廓stroke笔画
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(Color.BLACK),
                        filterFactory.literal(1),
                        filterFactory.literal(0.5));

        // create a partial opaque fill
        //创建一个部分透明的填充
        Fill fill =
                styleFactory.createFill(
                        filterFactory.literal(Color.GREEN), filterFactory.literal(0.5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);
        //创建一个rule，这个规则的Symbolizer部分由sym控制，其他部分可以另加
        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        //根据rule创建 FeatureTypeStyle fts
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});

        //将FeatureTypeStyle纳入到style中
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /** Create a Style to draw line features as thin blue lines */
    private Style createLineStyle() {
        Stroke stroke =
                styleFactory.createStroke(
                        filterFactory.literal(Color.YELLOW), filterFactory.literal(1));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /** Create a Style to draw point features as circles with blue outlines and cyan fill */
    private Style createPointStyle() {
        Graphic gr = styleFactory.createDefaultGraphic();

        Mark mark = styleFactory.getCircleMark();

        mark.setStroke(
                styleFactory.createStroke(
                        filterFactory.literal(Color.RED), filterFactory.literal(1)));

        mark.setFill(styleFactory.createFill(filterFactory.literal(Color.CYAN)));

        gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(mark);
        gr.setSize(filterFactory.literal(5));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        PointSymbolizer sym = styleFactory.createPointSymbolizer(gr, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[] {rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
}
// docs end alternative
// docs end source
