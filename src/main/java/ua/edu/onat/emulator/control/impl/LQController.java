package ua.edu.onat.emulator.control.impl;

import Jama.Matrix;
import ua.edu.onat.emulator.control.Controller;

public class LQController implements Controller {

	private Matrix gain;

	@Override
	public Matrix makeControlSignal(Matrix setpoints, Matrix outputs) {
		Matrix controls = gain.uminus().times(outputs);
		controls.plusEquals(setpoints); // FIXME Matrix sizes must agree.

		return controls;
	}

	public Matrix getGain() {
		return gain;
	}

	public void setGain(Matrix gain) {
		this.gain = gain;
	}

	@Override
	public boolean isControlling() {
		return true;
	}

}
