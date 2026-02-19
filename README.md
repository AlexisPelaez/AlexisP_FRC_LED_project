# FRC LED PROJECT - How it works

## Disclaimer: The algorithm for this LED subsystem was built for a 8x32 LED matrix.
---

# LEDSubsystem

## The stuff you need to input!

So, all you need to change are how many 8x32 LED Matrices you are using. Very simple!

## Our Variables 
/*
    private static final int NUM_PANELS = 4; // CHANGE FOR AMOUNT OF PANELS
    private static final int ROWS = 8;
    private static final int COLS = 32;

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
*/

The first three variables are fairly simple to understand.

**LED OBJECTS:** These are objects that are simply to set up and update the LEDs

**PDH OBJECT:** This is set up in order to collect the voltage. It takes in a CAN id and the type of PDH (For example ModuleType.kRev because it is a Rev Robotics PDH).

**flashTimer, voltageDanger, and flashState:** These are meant for the voltagechecker function that will appear later on.
In essence, if the voltage is ever too low, the LEDs have a defense measure which will begin flashing red in quick succession. (Not the most practical but it helps visually)

**fastFlashTimer and fastFlashState:** These are meant for the sparkle function which will make the LEDs essentially turn on random LEDs at a fast rate.

## Our Constructor:
/*
  public LEDSubsystem(int pwmPort) { 
          led = new AddressableLED(pwmPort);
          buffer = new AddressableLEDBuffer(NUM_PANELS * ROWS * COLS); //LENGTH GOES IN
          led.setLength(buffer.getLength());
          led.start();
          flashTimer.start();
          fastFlashTimer.start();
  }
*/
