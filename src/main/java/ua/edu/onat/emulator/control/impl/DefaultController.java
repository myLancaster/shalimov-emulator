package ua.edu.onat.emulator.control.impl;

import Jama.Matrix;
import ua.edu.onat.emulator.control.Controller;

public class DefaultController implements Controller {

	@Override
	public Matrix makeControlSignal(Matrix setpoints, Matrix outputs) {
		Matrix controls = outputs.uminus();
		controls.plusEquals(setpoints);

		return controls;
	}

	@Override
	public boolean isControlling() {
		return false;
	}

}
