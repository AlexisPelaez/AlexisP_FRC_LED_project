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

import java.io.Console;
import java.util.logging.Logger;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Power;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution;



public class LEDSubsystem{
    
    private static final int NUM_PANELS = 4; // CHANGE FOR AMOUNT OF PANELS
    private static final int ROWS = 8;  //CHANGE FOR AMOUNT OF ROWS
    private static final int COLS = 32; //CHANGE FOR AMOUNT OF COLS

    private final AddressableLED led;
    private final AddressableLEDBuffer buffer;
    PowerDistribution pdh = new PowerDistribution(14,ModuleType.kRev);
    double voltage;
    private Timer flashTimer = new Timer();
    private boolean voltageDanger = false;
    private boolean flashState = false;
    private boolean fastMode = false;
    private Timer fastFlashTimer = new Timer();
    private boolean fastFlashState = false;


    public LEDSubsystem(int pwmPort) { 
        led = new AddressableLED(pwmPort);
        buffer = new AddressableLEDBuffer(NUM_PANELS * ROWS * COLS); //LENGTH GOES IN
        led.setLength(buffer.getLength());
        led.start();
        flashTimer.start();
        fastFlashTimer.start();
    }

    public void setInfo(){
        led.setData(buffer);
    }
    // HELPER FUNCTIONS BELOW
    public void clear(){
        for(int i = 0; i < buffer.getLength(); i++){
            buffer.setRGB(i, 0, 0, 0);
        }
    }

    public void fill(int r, int g, int b){
        for(int i = 0; i < buffer.getLength();i++){
            buffer.setRGB(i, r, g, b);
        }
    }

    public void fillRow(int panel, int row, int r, int g, int b) {
        for (int col = 1; col <= COLS; col++) {
            setPixel(panel, row, col, r, g, b);
        }
    }
    public void fillColumn(int panel, int col, int r, int g, int b) {
        for (int rowI = 1; rowI <= ROWS; rowI++) {
            setPixel(panel, rowI, col, r, g, b);
        }
    }

    // SETS A SPECIFIC LED WITH COLORS OF YOUR CHOICE
    public void setPixel(int panel, int row, int col, int r, int g, int b){
        int index = map(panel, row, col);
        buffer.setRGB(index, r, g, b);
        SmartDashboard.putNumber("mapped index output", index);
    }

    public int[] randomizer(){
        int[] values = new int[6];
        values[0] = (int)(Math.random()*4);
        values[1] = (int)(Math.random()*8)+1;
        values[2] = (int)(Math.random()*32)+1;
        values[3] = (int)(Math.random()*123)+1;
        values[4] = (int)(Math.random()*123)+1;
        values[5] = (int)(Math.random()*123)+1;
        return values;
    }

    //SPECIAL FUNCTIONS

    // SPEEDS UP THE LED CHANGES
    public void Overclock(boolean enable) {
        fastMode = enable;
    }

    // CODE THAT MAKES THE LED UPDATE ANY # of ms
    public void startFastUpdates() {
        Thread t = new Thread(() -> {
            while (true) {
                if(fastMode){
                    //IF THERE IS A CERTAIN FUNCTION YOU WANT TO OVERCLOCK
                    //PUT IT RIGHT BELOW THIS TEXT
                    flashEffect();
                    led.setData(buffer);
                }
                try {
                    Thread.sleep(2); // update every 2ms (200 Hz)
                                            // ALSO THE NUMBER TO INCREASE SPEED
                } catch (Exception e) {}
            }
        });
        t.setDaemon(true);
        t.start();
    }
    
    // CHECKS VOLTAGE
    public void voltageChecker(){
        voltage = pdh.getVoltage();
        SmartDashboard.putNumber("PDH VOLTAGE", voltage);
        SmartDashboard.putBoolean("Voltage Danger", voltageDanger);

        if (voltage < 12) {
            voltageDanger = true; 
        }
        if (voltage > 10.8) { // must rise ABOVE this to clear
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
                fill(10, 0, 0);
            }
            // Flash OFF
            else {
                clear();
            }

            led.setData(buffer);
            return;   // Prevents other LED code from overwriting the warning
        }
        if (!fastMode) {
            led.setData(buffer);
        }

    }

    //ALGORITHM FOR LED 8x32 MATRIX

    private int map(int panel, int row, int col){
        // Convert to 0-based
        int zeroCol = col - 1;
        int zeroRow = row - 1;

        // Each panel has 256 LEDs
        int panelOffset = panel * 256;
        // Base offset for this column
        int base = zeroCol * 8;

        int index = 0;
        if (zeroCol % 2 == 0) {
            // even column → top to bottom
            index = base + zeroRow;
        } else {
            // odd column → bottom to top
            index = base + (7 - zeroRow);
        }

        // update coordinates 
        SmartDashboard.putNumber("zerocol", row);
        SmartDashboard.putNumber("zerorow", col);
        SmartDashboard.putNumber("panel", panel);

        return panelOffset + index;
    }
    public void sparkle(){
      int val[] = randomizer();
      setPixel(val[0], val[1], val[2], val[3], val[4], val[5]);
    }
    public void flashEffect() {
        if (!fastMode) return;

        // Toggle every 50ms (20 flashes per second)
        if (fastFlashTimer.get() > 0.05) {
            fastFlashState = !fastFlashState;
            fastFlashTimer.reset();
        }

        if (fastFlashState) {
            int val[] = randomizer();
            setPixel(val[0], val[1], val[2], val[3], val[4], val[5]);
        } else {
            // OFF phase — clear the whole matrix
            clear();
        }
    }

}
