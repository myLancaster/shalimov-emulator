package ua.edu.onat.emulator.model;

import Jama.Matrix;
import ua.edu.onat.emulator.control.Controller;

public abstract class TechnologicObject implements StateSpaceModel {

	protected Matrix system;
	protected Matrix input;
	protected Matrix output;
	protected Matrix feedforward;

	protected int samplingTime;

	protected Matrix setpoints;
	protected Matrix states;
	protected Matrix outputs;

	protected Controller controller;

	@Override
	public void tick() {
		throw new java.lang.UnsupportedOperationException("Not implemented, yet");
	}

	public Matrix getSystem() {
		return system;
	}

	public void setSystem(Matrix system) {
		this.system = system;
		states = new Matrix(system.getColumnDimension(), 1);
	}

	public Matrix getInput() {
		return input;
	}

	public void setInput(Matrix input) {
		this.input = input;
		outputs = new Matrix(1, input.getRowDimension());
	}

	public Matrix getOutput() {
		return output;
	}

	public void setOutput(Matrix output) {
		this.output = output;
	}

	public Matrix getFeedforward() {
		return feedforward;
	}

	public void setFeedforward(Matrix feedforward) {
		this.feedforward = feedforward;
	}

	@Override
	public int getSamplingTime() {
		return samplingTime;
	}

	public void setSamplingTime(int samplingTime) {
		this.samplingTime = samplingTime;
	}

	public Matrix getSetpoints() {
		return setpoints;
	}

	public void setSetpoints(Matrix setpoints) {
		this.setpoints = setpoints;
	}

	public Matrix getStates() {
		return states;
	}

	public void setStates(Matrix states) {
		this.states = states;
	}

	public Matrix getOutputs() {
		return outputs;
	}

	public void setOutputs(Matrix outputs) {
		this.outputs = outputs;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

}
