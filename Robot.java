// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Percent;

import java.util.Random;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution;


/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  LED_Subsystem led;
  XboxController control = new XboxController(0) ;
  public Robot() {
    
  }
  @Override 
  public void robotInit() {
    led = new LED_Subsystem(9); // PUT LED PWMPORT HERE
    led.setInfo();
    led.startFastUpdates();
  }
  @Override
  public void robotPeriodic() {
    //led.setInfo(); commented as it made it crash out and fly
    led.voltageChecker();
  }
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() { 

  }
  @Override
  public void teleopPeriodic() {
    if(control.getStartButtonPressed()){
      led.Overclock(true); // OVERCLOCK
    }
    if(control.getAButton()){
      // led.fill(0, 0, 10);
      led.fillRectangle(1, 2, 3, 3, 3, 2, 5, 3);
    }
    if(control.getBButton()){
      led.clear();
      led.Overclock(false);
    }
    if(control.getXButton()){
      led.sparkle();
    }
    if(control.getYButton()){
      led.fillRow(0, 1, 10, 10, 10);
      led.fillRow(0, 8, 10, 10, 10);
      led.fillRow(0, 4, 10, 10, 10);
      led.fillRow(0, 5, 10, 10, 10);
      led.fillRow(0, 2, 0, 0, 10);
      led.fillRow(0, 3, 0, 0, 10);
      led.fillRow(0, 2, 0, 0, 10);
      led.fillRow(0, 3, 0, 0, 10);
      led.fillRow(0, 6, 0, 0, 10);
      led.fillRow(0, 7, 0, 0, 10);
    }
  }
  
  @Override
  public void disabledInit() {

  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
   
}
