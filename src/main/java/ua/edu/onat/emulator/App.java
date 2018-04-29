package ua.edu.onat.emulator;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.edu.onat.emulator.gui.EmulatorFrame;
import ua.edu.onat.emulator.model.TechnologicObject;
import ua.edu.onat.emulator.model.impl.Reactor;

public class App {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("contexts/reactor-context.xml");
		TechnologicObject technologicObject = context.getBean("reactor", Reactor.class);
		technologicObject.setSamplingTime(2000);
		context.close();

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
