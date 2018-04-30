package ua.edu.onat.emulator.model.impl;

import Jama.Matrix;
import ua.edu.onat.emulator.model.StateSpaceModel;

public class Grinder extends StateSpaceModel {

	@Override
	public void tick() {
		Matrix nextStates = system.times(states);
		nextStates.plusEquals(input.times(controller.makeControlSignal(setpoints, outputs)));

		outputs = output.times(states);
		outputs.plusEquals(feedforward.times(controller.makeControlSignal(setpoints, outputs)));

		states = nextStates;
	}

}
