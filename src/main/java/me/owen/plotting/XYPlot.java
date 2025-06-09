package me.owen.plotting;

import me.owen.objects.DataRow;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class XYPlot extends JFrame {
    public XYPlot(HashMap<List<DataRow>, String> hashDatasets) {
        super("GC-Peak-Picker");

        // Create dataset
        XYSeriesCollection dataset = createDataset(hashDatasets);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "GC-TRACE",
                "Time (min)", "Intensity (counts)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        org.jfree.chart.plot.XYPlot plot = chart.getXYPlot();
        XYItemRenderer renderer = plot.getRenderer();

// Set stroke (thickness) for each series
        float lineThickness = 2.0f;
        for (int i = 0; i < plot.getDataset().getSeriesCount(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(lineThickness));
        }
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 20));
        // Axis label fonts
        chart.getXYPlot().getDomainAxis().setLabelFont(new Font("SansSerif", Font.BOLD, 20));
        chart.getXYPlot().getRangeAxis().setLabelFont(new Font("SansSerif", Font.BOLD, 20));

// Tick label fonts
        chart.getXYPlot().getDomainAxis().setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        chart.getXYPlot().getRangeAxis().setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));

// Title font
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 24));


        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private XYSeriesCollection createDataset(HashMap<List<DataRow>, String> hashDatasets) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for(List<DataRow> set : hashDatasets.keySet()) {
            XYSeries series = new XYSeries(hashDatasets.get(set));
            for(DataRow row : set) {
                series.add(row.getTime(),row.getIntensity());
            }

            dataset.addSeries(series);
        }

        return dataset;
    }
}
