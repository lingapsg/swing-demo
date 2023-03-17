package swing.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class XYLineChartExample extends JFrame {
 
    public XYLineChartExample() {
        super("XY Line Chart Example with JFreechart");
 
        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);
 
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
 
    private JPanel createChartPanel() {
        String chartTitle = "Objects Movement Chart";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        XYDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                "", "", dataset, PlotOrientation.VERTICAL, false, true, false);

        XYPlot plot = chart.getXYPlot();
        ValueAxis range = plot.getRangeAxis();
        range.setVisible(false);
        ValueAxis domain = plot.getDomainAxis();
        domain.setVisible(false);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseToolTipGenerator((xyDataset, series, item) -> {
            Number x = xyDataset.getX(series, item);
            Number y = xyDataset.getY(series, item);
            return "<html><p style='color:#0000ff;'>Hello</p></html>";
        });
        plot.setRenderer(renderer);

        return new ChartPanel(chart);
    }
 
    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Object 1");

        series1.add(-31, -43);
        series1.add(-239, -56);
        series1.add(230, -27);
        series1.add(645, -366);
        series1.add(149, 1310);
        series1.add(293, -66);
        series1.add(264, 354);

        dataset.addSeries(series1);

        return dataset;
    }


 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new XYLineChartExample().setVisible(true);
            }
        });
    }
}
