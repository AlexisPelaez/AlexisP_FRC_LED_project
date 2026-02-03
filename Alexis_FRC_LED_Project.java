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
  private final int kPort=9;
  private final int kLength=1024;
  AddressableLED led1;
  AddressableLEDBuffer led1_Buffer; 
  Units percent;
  // private LEDPattern pattern1;
  // private LEDPattern pattern2;
  // private LEDPattern pattern3;
  //LEDPattern pattern = LEDPattern.rainbow(255, 128);
  private static final Distance LEDlen = Meters.of(1.0/120.0);
  private LEDPattern m_scrollingRainbow;
  private AddressableLEDBufferView set1;
  private AddressableLEDBufferView set2;
  private AddressableLEDBufferView set3;
  
  //TIMER OBJECTS
  private final Timer timer = new Timer();
  private boolean flashState = false;
  private Timer flashTimer = new Timer();
  
  private boolean voltageDanger = false;
  private boolean state = true;
  XboxController control = new XboxController(0);
  boolean startSequence = false;
  int sequenceStep = 0;
  PowerDistribution pdh = new PowerDistribution(14,ModuleType.kRev);
  double voltage = pdh.getVoltage();
  private int LEDindex;

  //AddressableLEDBufferView led_pt1 = led1_Buffer.createView(0, kLength-1);
  public Robot() {
    
  }
  @Override 
  public void robotInit() {

    //Create the LED object, LEDBuffer, and set the LED
    led1= new AddressableLED(kPort);
    led1_Buffer= new AddressableLEDBuffer(kLength);
    led1.setLength(led1_Buffer.getLength());
    
    //Separate the LEDBuffer into sets (ex. two sets made here since two matrix LEDs)
    set1 = led1_Buffer.createView(0, 255);
    set2 = led1_Buffer.createView(256, 511);
    set3 = led1_Buffer.createView(512, 767);

    // //Create the patterns you want for each LED set
    // pattern1 = LEDPattern.gradient(GradientType.kDiscontinuous,Color.kWhite,Color.kDarkBlue).atBrightness(Percent.of(3)).reversed();
    // pattern2 = LEDPattern.rainbow(255, 128).atBrightness(Percent.of(3));
    // pattern3 = LEDPattern.gradient(GradientType.kDiscontinuous,Color.kWhite,Color.kDarkBlue).atBrightness(Percent.of(3)).reversed();
    
    // //(OPTIONAL: Make the LED sets scroll)
    // pattern1 = pattern1.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern2 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern3 = pattern3.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);

    // //Apply the changes of each pattern to each LED set 
    // pattern1.applyTo(set1);
    // pattern2.applyTo(set2);
    // pattern3.applyTo(set3);

    //Set data from the LED to the LEDBuffer and start the code
    
    led1.setData(led1_Buffer);
    led1.start();
    flashTimer.start();


    //CODE FOR RAINBOW LED COLOR
    // LEDPattern pattern = LEDPattern.rainbow(255, 128).atBrightness(Percent.of(3)); // Create scrolling version
    // m_scrollingRainbow = pattern.scrollAtAbsoluteSpeed( MetersPerSecond.of(1), LEDlen ); // Apply scrolling pattern initially
    // m_scrollingRainbow.applyTo(led1_Buffer);
    // led1.setData(led1_Buffer);
    // led1.start();

    // CODE FOR LED COLOR
    // LEDPattern pattern = LEDPattern.solid(Color.kWhite).atBrightness(Percent.of(1));
  } // runs once when the robot program starts }
  @Override
  public void robotPeriodic() {
    
    // pattern1.applyTo(set1);   // only LEDs 0–255
    // pattern2.applyTo(set2);
    // pattern3.applyTo(set3);   // only LEDs 256–511
    voltage = pdh.getVoltage();
    SmartDashboard.putNumber("PDH VOLTAGE", voltage);
    SmartDashboard.putBoolean("Voltage Danger", voltageDanger);

    if (voltage < 11.0) { //Set it at 7? 
       voltageDanger = true; 
    }
    if (voltage > 12.5) { // must rise ABOVE this to clear
      voltageDanger = false;
    }

    if(voltageDanger){
        // Toggle every 0.25 seconds
        if (flashTimer.get() > 0.25) {
            flashState = !flashState;
            flashTimer.reset();
        }

        // Flash red ON
        if (flashState) {
            for (int i = 0; i < led1_Buffer.getLength(); i++) {
                led1_Buffer.setRGB(i, 10, 0, 0);
            }
        }
        // Flash OFF
        else {
            for (int i = 0; i < led1_Buffer.getLength(); i++) {
                led1_Buffer.setRGB(i, 0, 0, 0);
            }
        }

        led1.setData(led1_Buffer);
        return;   // Prevents other LED code from overwriting the warning
    }
    led1.setData(led1_Buffer);
  }

  @Override
  public void autonomousInit() {
    // pattern1 = LEDPattern.gradient(GradientType.kContinuous, Color.kDarkOrange, Color.kRed).atBrightness(Percent.of(3));
    // pattern2 = LEDPattern.gradient(GradientType.kContinuous, Color.kDarkOrange, Color.kRed).atBrightness(Percent.of(3));
    // pattern3 = LEDPattern.gradient(GradientType.kContinuous, Color.kDarkOrange, Color.kRed).atBrightness(Percent.of(3));

    // pattern1 = pattern1.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern2 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern3 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
  }
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // pattern1 = LEDPattern.gradient(GradientType.kContinuous,Color.kCyan, Color.kDarkRed, Color.kWhite).atBrightness(Percent.of(3)).reversed();
    // pattern2 = LEDPattern.gradient(GradientType.kContinuous,Color.kCyan, Color.kDarkRed, Color.kWhite).atBrightness(Percent.of(3));
    // pattern3 = LEDPattern.gradient(GradientType.kContinuous,Color.kCyan, Color.kDarkRed, Color.kWhite).atBrightness(Percent.of(3));

    // pattern1 = pattern1.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern2 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern3 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    
  }
  @Override
  public void teleopPeriodic() {
    if(control.getStartButtonPressed()){
      startSequence = true;
      sequenceStep = 0;
      timer.reset();
      timer.start();
    }
     // RUN THE SEQUENCE
    if (startSequence) {

        double t = timer.get();

        if (sequenceStep == 0) {
            for (LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++) {
                led1_Buffer.setRGB(LEDindex, 10, 0, 0);
            }
            if (t > 0.3) {
                sequenceStep = 1;
                timer.reset();
            }
        }
        else if (sequenceStep == 1) {
            for (LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++) {
                led1_Buffer.setRGB(LEDindex, 0, 10, 0);
            }
            if (t > 0.3) {
                sequenceStep = 2;
                timer.reset();
            }
        }
        else if (sequenceStep == 2) {
            for (LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++) {
                led1_Buffer.setRGB(LEDindex, 0, 0, 10);
            }
            if (t > 0.3) {
                sequenceStep = 0;
                timer.reset();
            }
        }
        if(control.getXButton()){
          startSequence=false;
          timer.stop();
        }

        led1.setData(led1_Buffer);
        return;
    }

    if(control.getAButtonPressed()){
      for(LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++){
        led1_Buffer.setRGB(LEDindex, 0, 10, 0);
      }
      led1.setData(led1_Buffer);

    }
    if(control.getYButtonPressed()){
      for(LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++){
        led1_Buffer.setRGB(LEDindex, 0, 0, 10);
      }
      led1.setData(led1_Buffer);

    }
    if(control.getBButtonPressed()){
      for(LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++){
        led1_Buffer.setRGB(LEDindex, 10, 0, 0);
      }
      led1.setData(led1_Buffer);

    }
    if(control.getXButtonPressed()){
      for(LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++){
        led1_Buffer.setRGB(LEDindex, 0, 0, 0);
      }
      led1.setData(led1_Buffer);
    }
    // if(control.getRightBumperButtonPressed()){
    //   for(LEDindex = 0; LEDindex < led1_Buffer.getLength(); LEDindex++){
    //     led1_Buffer.setRGB(spacer(0, LEDindex), 1, 0, 0);
    //   }
    //   led1.setData(led1_Buffer);
    // }


    led1.setData(led1_Buffer);
  }
  
  @Override
  public void disabledInit() {
    // pattern1 = LEDPattern.rainbow(255, 128).atBrightness(Percent.of(3));//gradient(GradientType.kDiscontinuous,Color.kWhite,Color.kDarkBlue).atBrightness(Percent.of(3)).reversed();
    // pattern2 = LEDPattern.rainbow(255, 128).atBrightness(Percent.of(3));
    // pattern3 = LEDPattern.rainbow(255, 128).atBrightness(Percent.of(3));

    // pattern1 = pattern1.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern2 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
    // pattern3 = pattern2.scrollAtAbsoluteSpeed(MetersPerSecond.of(1), LEDlen);
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
  
  public int spacer(int erow,int ecol){
    if(erow<9){
      int row=9-erow;
      int col=ecol;
      int space=((col-1)/2)*16;
      if(col%2==0){
        space+=15;
        space-=(row-1);
      }else{
        space+=(row-1);
      }
      return space;
    }
    else{
      int row=erow-8;
      int col=33-ecol;
      int space=((col-1)/2)*16;
      if(col%2==0){
        space+=15;
        space-=(row-1);
      }else{
        space+=(row-1);
      }
      return space+256;
    }
   }
   
}
