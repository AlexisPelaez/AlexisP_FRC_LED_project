// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Percent;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Power;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.LEDPattern;
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
  LEDSubsystem led;
  XboxController control = new XboxController(0) ;
  public Robot() {
    
  }
  @Override 
  public void robotInit() {
    led = new LEDSubsystem(9); // PUT LED PWMPORT HERE
    led.setInfo();
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
    if(control.getAButton()){
      led.fill(0, 0, 10);
    }
    if(control.getBButton()){
      led.clear();
    }
    if(control.getXButton()){
      // led.fillRow(1, 1, 10, 10, 10);
      led.setPixel(0, 1, 1, 10, 10, 10);
    }
    if(control.getYButton()){
      led.fillRow(1, 3, 10,10,0);
      led.fillColumn(1,4,20,30,4);
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
