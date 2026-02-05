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

    public LEDSubsystem(int pwmPort) { 
        led = new AddressableLED(pwmPort);
        buffer = new AddressableLEDBuffer(NUM_PANELS * ROWS * COLS); //LENGTH GOES IN
        led.setLength(buffer.getLength());
        led.start();
        flashTimer.start();
    }

    public void setInfo(){
        led.setData(buffer);
    }

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

    public void setPixel(int panel, int row, int col, int r, int g, int b){
        int index = map(panel, row, col);
        buffer.setRGB(index, r, g, b);
    }

    public void voltageChecker(){
        voltage = pdh.getVoltage();
        SmartDashboard.putNumber("PDH VOLTAGE", voltage);
        SmartDashboard.putBoolean("Voltage Danger", voltageDanger);

        if (voltage < 12) {
            voltageDanger = true; 
        }
        if (voltage > 10) { // must rise ABOVE this to clear
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
        led.setData(buffer);
    }

    //ALGORITHM FOR LED 8x32 MATRIX
   //POTENTIALLY UPDATE ALGORITHM (not sure if it works 100%...)

    private int map(int panel, int row, int col){
        // Convert to 0-based
        int zeroCol = col - 1;
        int zeroRow = row - 1;

        // Each panel has 256 LEDs
        int panelOffset = panel * 256;

        // Base offset for this column
        int base = zeroCol * 8;

        int index;
        if (zeroCol % 2 == 0) {
            // even column → top to bottom
            index = base + zeroRow;
        } else {
            // odd column → bottom to top
            index = base + (7 - zeroRow);
        }

        return panelOffset + index;
        /*
        // Convert to 0‑based
        int zeroRow = row - 1;
        int zeroCol = col - 1;

        // Each panel has 256 LEDs
        int panelOffset = panel * (ROWS * COLS);

        // Row-major order (left → right, top → bottom)
        int index = panelOffset + zeroRow * COLS + zeroCol;

        return index;
        */
    }

}

