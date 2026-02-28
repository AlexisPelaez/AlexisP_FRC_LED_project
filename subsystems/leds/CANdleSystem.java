package frc.robot.subsystems.leds;


import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.signals.StripTypeValue;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;

import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.SingleFadeAnimation;
import com.ctre.phoenix6.controls.TwinkleOffAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.DutyCycleOut;

public class CANdleSystem {

    private final int LED_COUNT = 256;
    public CANdle candle = new CANdle(LedConstants.candleCanId);
    private final StatusSignal<Current> current = candle.getOutputCurrent();
    private final StatusSignal<Temperature> temp = candle.getDeviceTemp();
    private final StatusSignal<Integer> fault = candle.getFaultField();

    //CUSTOMIZE AND CREATE ANIMATIONS HERE
    private final RainbowAnimation rainbow = new RainbowAnimation(0, 255);
    private final FireAnimation fire = new FireAnimation(0, 255);
    private final LarsonAnimation larson = new LarsonAnimation(0, 256);
    // speed, brightness, LED count


    public CANdleSystem() {
        var candleConfig = new CANdleConfiguration();
        candleConfig.LED.StripType = StripTypeValue.RGB;
        candleConfig.LED.BrightnessScalar = 0.1;
        //Change the brightness (Ex. 0.1 = 10% brightness)

        BaseStatusSignal.setUpdateFrequencyForAll(50.0, current, temp, fault);
        ParentDevice.optimizeBusUtilizationForAll(candle);
        
        candle.getConfigurator().apply(candleConfig);
    }
    public void rainbowAnim(){candle.setControl(rainbow);}
    public void fireAnim(){candle.setControl(fire);}
    public void larsonAnim(){candle.setControl(larson);}
    public void clear(){
        candle.setControl(candle.setLeds);
    
    }
}