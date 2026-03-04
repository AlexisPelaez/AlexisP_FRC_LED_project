// ...existing code...
package frc.robot.subsystems.leds;


import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.signals.StripTypeValue;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;

import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
//import com.ctre.phoenix6.controls.SetLEDs;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.SingleFadeAnimation;
import com.ctre.phoenix6.controls.TwinkleOffAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.DutyCycleOut;

public class CANdleSystem {

    // SUGGESTION: make constant static final and reuse it instead of magic numbers
    private static final int LED_COUNT = 264;
    public CANdle candle = new CANdle(LedConstants.candleCanId);
    private final StatusSignal<Current> current = candle.getOutputCurrent();
    private final StatusSignal<Temperature> temp = candle.getDeviceTemp();
    private final StatusSignal<Integer> fault = candle.getFaultField();
    
    //CUSTOMIZE AND CREATE ANIMATIONS HERE
    // SUGGESTION: use LED_COUNT to compute end index (LED_COUNT - 1)
    private final RainbowAnimation rainbow = new RainbowAnimation(0, LED_COUNT - 1);
    private final FireAnimation fire = new FireAnimation(0, LED_COUNT - 1);
    private final LarsonAnimation larson = new LarsonAnimation(0, LED_COUNT - 1);
    // speed, brightness, LED count


    public CANdleSystem() {
        var candleConfig = new CANdleConfiguration();
        candleConfig.LED.StripType = StripTypeValue.GRB;
        candleConfig.LED.BrightnessScalar = 0.1;
        //Change the brightness (Ex. 0.1 = 10% brightness)

        BaseStatusSignal.setUpdateFrequencyForAll(50.0, current, temp, fault);
        ParentDevice.optimizeBusUtilizationForAll(candle);
        
        // SUGGESTION: guard apply with try/catch to surface errors during init
        try {
            candle.getConfigurator().apply(candleConfig);
        } catch (Exception ex) {
            // consider logging to WPILib logger or DriverStation output
            System.err.println("CANdle configuration failed: " + ex.getMessage());
        }
    }
    public void rainbowAnim(){candle.setControl(rainbow);}
    public void fireAnim(){candle.setControl(fire);}
    public void larsonAnim(){candle.setControl(larson);}
    
    // SUGGESTION: helpers for common operations
    // public void stopAnim() {
    //     candle.setLEDs(0, 0, 0, 0, LED_COUNT);
    // }


    public StatusSignal<Current> getOutputCurrent() {
        return current;
    }

    public StatusSignal<Temperature> getDeviceTemp() {
        return temp;
    }

    public StatusSignal<Integer> getFaultField() {
        return fault;
    }

    // SUGGESTION: allow changing brightness at runtime (applies minimal config)
    public void setBrightnessScalar(double scalar) {
        var cfg = new CANdleConfiguration();
        cfg.LED.BrightnessScalar = scalar;
        try {
            candle.getConfigurator().apply(cfg);
        } catch (Exception ex) {
            System.err.println("Failed to apply brightness scalar: " + ex.getMessage());
        }
    }
    
}
