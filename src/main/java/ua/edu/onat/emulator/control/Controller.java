package ua.edu.onat.emulator.control;

import Jama.Matrix;

public interface Controller {

	Matrix makeControlSignal(Matrix setpoints, Matrix outputs);

	boolean isControlling();

}
