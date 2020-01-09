/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChartBuilder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

/**
 * @author Adish
 */
public class LineChart_AWT extends ApplicationFrame {

    double[][] valer;

    public LineChart_AWT(String applicationTitle, String chartTitle, double[][] ss) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Time/mins",
                "% Error",
                createDataset(ss),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
    }

    private CategoryDataset createDataset(double[][] ss) {
        final String Temperature = "Temperature";
        final String Humidity = "Humidity";
        final String Luminosity = "Luminosity";
        final String Rainfall = "Rainfall";
        final String Pressure = "Atm Pressure";
        final String WindDirection = "Wind Direction";
        final String WindSpeed = "Wind Speed";

        final String predict_20 = "20";
        final String predict_40 = "40";
        final String predict_60 = "60";
        valer = ss;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(valer[0][0], Temperature, predict_20);
        dataset.addValue(valer[0][1], Humidity, predict_20);
        dataset.addValue(valer[0][2], Luminosity, predict_20);
        dataset.addValue(valer[0][3], Rainfall, predict_20);
        dataset.addValue(valer[0][4], Pressure, predict_20);
        dataset.addValue(valer[0][5], WindDirection, predict_20);
        dataset.addValue(valer[0][6], WindSpeed, predict_20);

        dataset.addValue(valer[1][0], Temperature, predict_40);
        dataset.addValue(valer[1][1], Humidity, predict_40);
        dataset.addValue(valer[1][2], Luminosity, predict_40);
        dataset.addValue(valer[1][3], Rainfall, predict_40);
        dataset.addValue(valer[1][4], Pressure, predict_40);
        dataset.addValue(valer[1][5], WindDirection, predict_40);
        dataset.addValue(valer[1][6], WindSpeed, predict_40);

        dataset.addValue(valer[2][0], Temperature, predict_60);
        dataset.addValue(valer[2][1], Humidity, predict_60);
        dataset.addValue(valer[2][2], Luminosity, predict_60);
        dataset.addValue(valer[2][3], Rainfall, predict_60);
        dataset.addValue(valer[2][4], Pressure, predict_60);
        dataset.addValue(valer[2][5], WindDirection, predict_60);
        dataset.addValue(valer[2][6], WindSpeed, predict_60);
        return dataset;
    }

}
