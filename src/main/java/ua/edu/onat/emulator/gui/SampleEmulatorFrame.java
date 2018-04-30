package ua.edu.onat.emulator.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;

import ua.edu.onat.emulator.model.StateSpaceModel;

public class SampleEmulatorFrame extends JFrame {

	private static final long serialVersionUID = 4929521182645563441L;
	private static final int COUNT = 2 * 60;
	private StateSpaceModel technologicObject;
	private Timer timer;
	
	public SampleEmulatorFrame(StateSpaceModel technologicObject) {
		this.setTechnologicObject(technologicObject);
		
		this.setTitle("Эмулятор работы экструдера");
		this.setPreferredSize(new Dimension(800, 600));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel channelPanel = new JPanel();
		channelPanel
				.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));

		final DynamicTimeSeriesCollection cartPositionDataset = new DynamicTimeSeriesCollection(1, COUNT, new Second());
		cartPositionDataset.setTimeBase(new Second());
		cartPositionDataset.addSeries(new float[1], 0, "Кислотно-щелочной баланс реактора");

		JFreeChart firstChart = createChart(cartPositionDataset, "Кислотно-щелочной баланс реактора", "минуты", "pH, %");

		channelPanel.add(new ChartPanel(firstChart));
		
		this.add(channelPanel);
		
		timer = new Timer(technologicObject.getSamplingTime(), new ActionListener() {

			float[] newData = new float[1];

			@Override
			public void actionPerformed(ActionEvent arg0) {
				technologicObject.tick();
				double[][] outputs = technologicObject.getOutputs().getArray();
				newData[0] = (float) outputs[0][0];
				cartPositionDataset.advanceTime();
				cartPositionDataset.appendData(newData);
			}
		});
	}
	
	public void start() {
		timer.start();
	}
	
	private JFreeChart createChart(final XYDataset dataset, String title, String timeUnits, String valueUnits) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(title, timeUnits, valueUnits, dataset, false,
				false, false);
		XYPlot plot = result.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		return result;
	}

	public StateSpaceModel getTechnologicObject() {
		return technologicObject;
	}

	public void setTechnologicObject(StateSpaceModel technologicObject) {
		this.technologicObject = technologicObject;
	}

}
