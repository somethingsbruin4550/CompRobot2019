/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

//import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Add;

/**
 * Add your docs here.
 */
public class ReleasableButton {
    ArrayList<String> buttonNames = new ArrayList<String>();
    ArrayList<Boolean> buttonStates = new ArrayList<Boolean>();

    boolean updateButton(String name, boolean isPressed) {
        int buttonIndex = getButtonId(name);
        return updateButton(buttonIndex, isPressed);
    }

    boolean updateButton(int buttonIndex, boolean isPressed) {
        if(!buttonStates.get(buttonIndex) && isPressed) {
            buttonStates.set(buttonIndex, true);
            return true;
        } else if(buttonStates.get(buttonIndex) && !isPressed)
            buttonStates.set(buttonIndex, false);
        return false;
    }

    private int getButtonId(String name) {
        int i = buttonNames.indexOf(name);
        if(i == -1) {
            buttonNames.add(name);
            i = buttonNames.indexOf(name);
        }
        return i;
    }
}
