package ua.edu.onat.emulator.model.impl;

import Jama.Matrix;
import ua.edu.onat.emulator.model.TechnologicObject;

public class Reactor extends TechnologicObject {
	
	@Override
	public void tick() {
		Matrix nextStates = system.times(states);
		nextStates.plusEquals(input.times(controller.makeControlSignal(setpoints, outputs)));

		outputs = states;

		states = nextStates;
	}
	
}
