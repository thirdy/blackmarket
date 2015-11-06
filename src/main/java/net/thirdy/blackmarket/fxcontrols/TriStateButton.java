/*
 * Copyright (C) 2015 thirdy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.thirdy.blackmarket.fxcontrols;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import net.thirdy.blackmarket.fxcontrols.TriStateButton.State;


/**
 * @author thirdy
 *
 */
public class TriStateButton extends Button {
	
	private static final Map<State, State> TRANSITION_TABLE = ImmutableMap.of(
			State.And, State.Or,
			State.Or,  State.No,
			State.No,  State.And);
	
	private ObjectProperty<State> state = new SimpleObjectProperty<>();
	public ObjectProperty<State> stateProperty() {return state;}
	
	public TriStateButton() {
		this(State.And);
	}
	
	public TriStateButton(State s) {
		state.set(s);
		textProperty().bind(state.asString());
		setOnAction(e -> toggleState());
	}

	private void toggleState() {
		state.set(TRANSITION_TABLE.get(state.get()));
	}

	public static enum State {
		And, Or, No;
	}
	
}
