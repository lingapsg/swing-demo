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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class XYLineChartExample extends JFrame {

    private Timer timer;
    private boolean enabled = true;

    private ActionListener timerListener;

    public XYLineChartExample() {
        super("Map Coordinates Plot");
        JPanel mainPanel = new JPanel();

        // chart
        JFreeChart chart = createChart();
        mainPanel.add(new ChartPanel(chart), BorderLayout.CENTER);

        // button
        JPanel buttonPanel = createButtonPanel(chart);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        setSize(640, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createButtonPanel(JFreeChart chart) {
        JPanel buttonPanel = new JPanel();
        LayoutManager buttonLayout = new FlowLayout();
        buttonPanel.setLayout(buttonLayout);

        JButton reloadButton = new JButton("Reload");
        reloadButton.addActionListener(e -> {
            stopTimerAndRemoveListener();
            chart.getXYPlot().setDataset(createDataset(getCoordinates()));
            addListenerAndStartTimer();
        });
        buttonPanel.add(reloadButton);

        JButton enableDisableButton = new JButton("Disable Auto-reload");
        enableDisableButton.addActionListener(e -> {
            if (enabled) {
                stopTimerAndRemoveListener();
                enabled = false;
                enableDisableButton.setText("Enable Auto-reload");
            } else {
                addListenerAndStartTimer();
                enabled = true;
                enableDisableButton.setText("Disable Reload");
            }
        });
        buttonPanel.add(enableDisableButton);

        JButton aboutButton = new JButton("About");
        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "About");
        });

        buttonPanel.add(aboutButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            dispose();
        });
        buttonPanel.add(exitButton);
        return buttonPanel;
    }

    private JFreeChart createChart() {
        String chartTitle = "Map Coordinates Chart";

        XYDataset dataset = createDataset(getCoordinates());
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

        timerListener = timerListener(chart);
        timer = new Timer(5000, timerListener);
        return chart;
    }

    private ActionListener timerListener(JFreeChart chart) {
        return e -> {
            XYDataset newDataset = createDataset(getCoordinates1());
            chart.getXYPlot().setDataset(newDataset);
        };
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

    private void stopTimerAndRemoveListener() {
        timer.removeActionListener(timerListener);
        timer.stop();
    }

    private void addListenerAndStartTimer() {
        timer.addActionListener(timerListener);
        startTimer();
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
