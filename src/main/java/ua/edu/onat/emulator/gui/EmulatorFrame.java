package ua.edu.onat.emulator.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.edu.onat.emulator.control.Controller;
import ua.edu.onat.emulator.control.impl.DefaultController;
import ua.edu.onat.emulator.control.impl.LQController;
import ua.edu.onat.emulator.model.StateSpaceModel;

public class EmulatorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int COUNT = 2 * 60;
	private StateSpaceModel technologicObject;
	private Timer timer;

	public EmulatorFrame(StateSpaceModel technologicObject) {
		this.technologicObject = technologicObject;

		this.setTitle("Эмулятор работы химического реактора");
		this.setPreferredSize(new Dimension(1280, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();

		JMenu controllerMenu = new JMenu("Контроллер");

		JMenuItem onOffControllerMenuItem = new JMenuItem("Вкл/выкл");
		onOffControllerMenuItem.addActionListener(new ActionListener() { // TODO Add setpoint changing context.

			ClassPathXmlApplicationContext context;

			@Override
			public void actionPerformed(ActionEvent e) { // TODO Add controller state changing log.
				context = new ClassPathXmlApplicationContext("contexts/controller-context.xml");

				Controller controller = technologicObject.getController();

				// FIXME TechnologicObject must receive controller instance from Spring.
				if (controller.isControlling()) {
					controller = context.getBean("defaultController", DefaultController.class);
				} else {
					controller = context.getBean("lqController", LQController.class);
				}

				technologicObject.setController(controller);

				context.close();
			}
		});

		controllerMenu.add(onOffControllerMenuItem);
		menuBar.add(controllerMenu);

		JMenuItem aboutMeMenuItem = new JMenuItem("О проекте");
		aboutMeMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						createAndShowAboutMe();
					}
				});
			}
		});

		menuBar.add(aboutMeMenuItem);

		this.setJMenuBar(menuBar);

		JPanel channelPanel = new JPanel();
		channelPanel.setPreferredSize(new Dimension((int) (this.getWidth() * 0.7), this.getHeight() / 2));

		final DynamicTimeSeriesCollection phDataset = new DynamicTimeSeriesCollection(1, COUNT, new Second());
		phDataset.setTimeBase(new Second());
		phDataset.addSeries(new float[1], 0, "Кислотно-щелочной баланс");

		JFreeChart firstChart = createChart(phDataset, "Кислотно-щелочной баланс", "чч:мм:сс", "pH, %");

		channelPanel.add(new ChartPanel(firstChart));

		this.add(channelPanel);

		timer = new Timer(technologicObject.getSamplingTime(), new ActionListener() {

			float[] newData = new float[1];

			@Override
			public void actionPerformed(ActionEvent arg0) {
				technologicObject.tick();
				double[][] outputs = technologicObject.getOutputs().getArray();
				newData[0] = (float) outputs[0][0];
				phDataset.advanceTime();
				phDataset.appendData(newData);
			}
		});

		// JPanel bottomPanel = new JPanel();
		// JLabel logLabel = new JLabel("Log:");
		// bottomPanel.add(logLabel); // TODO Add log table.
	}

	public void start() {
		timer.start();
	}

	public StateSpaceModel getTechnologicObject() {
		return technologicObject;
	}

	public void setTechnologicObject(StateSpaceModel technologicObject) {
		this.technologicObject = technologicObject;
	}

	private JFreeChart createChart(final XYDataset dataset, String title, String timeUnits, String valueUnits) {
		final JFreeChart result = ChartFactory.createTimeSeriesChart(title, timeUnits, valueUnits, dataset, false,
				false, false);
		XYPlot plot = result.getXYPlot();
		ValueAxis domain = plot.getDomainAxis();
		domain.setAutoRange(true);
		return result;
	}

	private void createAndShowAboutMe() {
		JFrame frame = new JFrame("О проекте");
		frame.setPreferredSize(new Dimension(896, 504));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JEditorPane htmlPane = new JEditorPane();
		htmlPane.setPreferredSize(frame.getSize());
		htmlPane.setEditable(false);
		htmlPane.setContentType("text/html");

		try {
			File file = new File("html/about-me.html");
			htmlPane.setPage(file.toURI().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		frame.add(htmlPane);
		frame.setVisible(true);
		frame.pack();
	}

}
