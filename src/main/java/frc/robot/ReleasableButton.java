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
    boolean buttonState = false;

    boolean updateButton(boolean isPressed) {
        if(!buttonState && isPressed) {
            buttonState = true;
            return true;
        } else if(buttonState && !isPressed)
            buttonState = false;
        return false;
    }

    // boolean updateButton(int buttonIndex, boolean isPressed) {
    //     if(!buttonStates.get(buttonIndex) && isPressed) {
    //         buttonStates.set(buttonIndex, true);
    //         return true;
    //     } else if(buttonStates.get(buttonIndex) && !isPressed)
    //         buttonStates.set(buttonIndex, false);
    //     return false;
    // }

    //  int getButtonId(String name) {
    //     int i = buttonNames.indexOf(name);
    //     if(i == -1) {
    //         buttonNames.add(name);
    //         i = buttonNames.indexOf(name);
    //     }
    //     return i;
    // }
}
