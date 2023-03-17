package swing.sample;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class XYLineChartExample extends JFrame {

    private Timer timer;

    public XYLineChartExample() {
        super("XY Line Chart Example with JFreechart");

        XYDataset dataset = createDataset(getCoordinates());
        JPanel chartPanel = createChartPanel(dataset);
        add(chartPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createChartPanel(XYDataset dataset) {
        String chartTitle = "Objects Movement Chart";

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                "", "", dataset, PlotOrientation.VERTICAL, false, true, false);

        XYPlot plot = chart.getXYPlot();

        ValueAxis range = plot.getRangeAxis();
        range.setVisible(false);
        ValueAxis domain = plot.getDomainAxis();
        domain.setVisible(false);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        //comment below line to see connected lines
        renderer.setSeriesLinesVisible(0, false);

        List<Coordinate> coordinates = getCoordinates();
        renderer.setBaseToolTipGenerator((xyDataset, series, item) -> {
            Coordinate coordinate = coordinates.get(item);
            return String.format("<html><p style='color:#0000ff;'>%s</p></html>", coordinate.getName());
        });
        plot.setRenderer(renderer);
        //addAnnotationsToPlot(plot);

        timer = new Timer(5000, e -> {
            XYDataset newDataset = createDataset(getCoordinates1());
            chart.getXYPlot().setDataset(newDataset);
        });
        return new ChartPanel(chart);
    }

    private XYDataset createDataset(List<Coordinate> coordinates) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Object 1");

        for (Coordinate coordinate : coordinates) {
            XYDataItem dataItem = new XYDataItem(coordinate.getX(), coordinate.getY());
            series1.add(dataItem);
        }

        dataset.addSeries(series1);
        return dataset;
    }

    public void startTimer() {
        timer.start();
    }

    private List<Coordinate> getCoordinates() {
        return List.of(
                new Coordinate(-31, -43, "ST-772"),
                new Coordinate(-239, -56, "ST-500"),
                new Coordinate(230, -27, "ST-1105"),
                new Coordinate(645, -366, "STÖ-995"),
                new Coordinate(149, 1310, "ST-689"),
                new Coordinate(293, -66, "ST-1034"),
                new Coordinate(264, 354, "ST-1171")
        );
    }

    private List<Coordinate> getCoordinates1() {
        return List.of(
                new Coordinate(-31, -43, "ST-772"),
                new Coordinate(-239, -56, "ST-500"),
                new Coordinate(230, -27, "ST-1105"),
                new Coordinate(645, -366, "STÖ-995"),
                new Coordinate(149, 1310, "ST-689")
        );
    }

    private void addAnnotationsToPlot(XYPlot plot) {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("location_icon.png"));
        for (Coordinate c : getCoordinates()) {
            plot.addAnnotation(new XYImageAnnotation(c.getX(), c.getY(), icon.getImage()));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            XYLineChartExample lineChart = new XYLineChartExample();
            lineChart.setVisible(true);
            lineChart.startTimer();
        });
    }
}
