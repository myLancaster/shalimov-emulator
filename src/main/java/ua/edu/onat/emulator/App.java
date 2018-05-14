package ua.edu.onat.emulator;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import Jama.Matrix;
import ua.edu.onat.emulator.control.impl.LQController;
import ua.edu.onat.emulator.gui.EmulatorFrame;
import ua.edu.onat.emulator.model.StateSpaceModel;
import ua.edu.onat.emulator.model.impl.Forge;

public class App {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("contexts/forge-context.xml");
		StateSpaceModel technologicObject = context.getBean("forge", Forge.class);
		technologicObject.setSamplingTime(1000);
		context.close();

		LQController controller = new LQController();
		controller.setGain(new Matrix(new double[][] { { -0.0072, -0.0033 } }));
		technologicObject.setController(controller);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				EmulatorFrame emulator = new EmulatorFrame(technologicObject);
				emulator.setVisible(true);
				emulator.pack();
				emulator.start();
			}
		});
	}

}
