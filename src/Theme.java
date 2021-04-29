import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;

public class Theme {

    ArrayList<Color> currentTheme = new ArrayList<Color>();
    
    //DEFAULT THEME
    Color roadColorDefault = Color.rgb(208, 211, 212), stripeColorDefault = Color.rgb(253, 254, 254), warningColorDefault = Color.rgb(243, 156, 18);
    Color clearedAreaColorDefault = Color.rgb( 133, 193, 233), HUDColorDefault = Color.rgb(174, 214, 241), backgroundColorDefault = Color.rgb(214, 234, 248);
    Color outlineColorDefault = Color.rgb(23, 32, 42), obstacleColor1Default = Color.rgb(231, 76, 60), obstacleColor2Default = Color.rgb(146, 43, 33);
    Color grassColorDefault = Color.rgb(162, 217, 206), safeColorDefault = Color.rgb(46, 204, 113), dangerColorDefault = Color.rgb(231, 76, 60);

    Color displacedThresholdColorDefault = Color.rgb(203, 67, 53), TORAColorDefault = Color.rgb(22, 160, 133), LDAColorDefault = Color.rgb(165, 105, 189); 
    Color ASDAColorDefault = Color.rgb(202, 111, 30), TODAColorDefault = Color.rgb(154, 125, 10), clearwayColorDefault, stopwayColorDefault, RESAColorDefault = Color.rgb(33, 47, 61);
    Color stripEndColorDefault, blastProtColorDefault, ALSColorDefault, TOCSColorDefault, runwayStripColorDefault;

    ArrayList<Color> defaultTheme = new ArrayList<Color>( Arrays.asList(roadColorDefault, stripeColorDefault, warningColorDefault, clearedAreaColorDefault, 
    HUDColorDefault, backgroundColorDefault, outlineColorDefault, obstacleColor1Default, obstacleColor2Default, grassColorDefault, safeColorDefault, 
    dangerColorDefault, displacedThresholdColorDefault, TORAColorDefault, LDAColorDefault, ASDAColorDefault, TODAColorDefault, clearwayColorDefault, 
    stopwayColorDefault, RESAColorDefault, stripEndColorDefault, blastProtColorDefault, ALSColorDefault, TOCSColorDefault, runwayStripColorDefault) );

    //DARK THEME
    Color roadColorDark = Color.rgb(93, 109, 126), stripeColorDark = Color.rgb(234, 236, 238), warningColorDark = Color.rgb(186, 74, 0);
    Color clearedAreaColorDark = Color.rgb(40, 116, 166), HUDColorDark = Color.rgb(26, 82, 118), backgroundColorDark = Color.rgb(27, 38, 49);
    Color outlineColorDark = Color.rgb(235, 245, 251), obstacleColor1Dark  = Color.rgb(192, 57, 43), obstacleColor2Dark  = Color.rgb(203, 67, 53);
    Color grassColorDark = Color.rgb(31, 97, 141), safeColorDark = Color.rgb(17, 122, 101), dangerColorDark = Color.rgb(176, 58, 46);

    Color displacedThresholdColorDark = Color.rgb(236, 112, 99), TORAColorDark = Color.rgb(244, 208, 63), LDAColorDark = Color.rgb(235, 222, 240); 
    Color ASDAColorDark = Color.rgb(125, 206, 160), TODAColorDark = Color.rgb(229, 152, 102);
    Color clearwayColorDark, stopwayColorDark, RESAColorDark = Color.rgb(240, 178, 122);
    Color stripEndColorDark, blastProtColorDark, ALSColorDark, TOCSColorDark, runwayStripColorDark;

    ArrayList<Color> darkTheme = new ArrayList<Color>( Arrays.asList(roadColorDark, stripeColorDark, warningColorDark, clearedAreaColorDark, 
    HUDColorDark, backgroundColorDark, outlineColorDark, obstacleColor1Dark, obstacleColor2Dark, grassColorDark, safeColorDark, 
    dangerColorDark, displacedThresholdColorDark, TORAColorDark, LDAColorDark, ASDAColorDark, TODAColorDark, clearwayColorDark, 
    stopwayColorDark, RESAColorDark, stripEndColorDark, blastProtColorDark, ALSColorDark, TOCSColorDark, runwayStripColorDark) );

    //BLACK AND WHITE THEME
    Color roadColorMonochrome = Color.rgb(133, 146, 158), stripeColorMonochrome = Color.rgb(253, 254, 254), warningColorMonochrome = Color.rgb(86, 101, 115);
    Color clearedAreaColorMonochrome = Color.rgb(178, 186, 187), HUDColorMonochrome = Color.rgb(214, 219, 223), backgroundColorMonochrome = Color.rgb(234, 236, 238);
    Color outlineColorMonochrome = Color.rgb(23, 32, 42), obstacleColor1Monochrome = Color.rgb(46, 64, 83), obstacleColor2Monochrome = Color.rgb(93, 109, 126);
    Color grassColorMonochrome = Color.rgb(213, 216, 220), safeColorMonochrome = Color.rgb(204, 209, 209), dangerColorMonochrome = Color.rgb(127, 140, 141);

    Color displacedThresholdColorMonochrome = Color.rgb(23, 32, 42), TORAColorMonochrome = Color.rgb(23, 32, 42), LDAColorMonochrome = Color.rgb(23, 32, 42); 
    Color ASDAColorMonochrome = Color.rgb(23, 32, 42), TODAColorMonochrome = Color.rgb(23, 32, 42);
    Color clearwayColorMonochrome, stopwayColorMonochrome, RESAColorMonochrome = Color.rgb(23, 32, 42);
    Color stripEndColorMonochrome, blastProtColorMonochrome, ALSColorMonochrome, TOCSColorMonochrome, runwayStripColorMonochrome;

    ArrayList<Color> monochromeTheme = new ArrayList<Color>( Arrays.asList(roadColorMonochrome, stripeColorMonochrome, warningColorMonochrome, clearedAreaColorMonochrome, 
    HUDColorMonochrome, backgroundColorMonochrome, outlineColorMonochrome, obstacleColor1Monochrome, obstacleColor2Monochrome, grassColorMonochrome, safeColorMonochrome, 
    dangerColorMonochrome, displacedThresholdColorMonochrome, TORAColorMonochrome, LDAColorMonochrome, ASDAColorMonochrome, TODAColorMonochrome, clearwayColorMonochrome, 
    stopwayColorMonochrome, RESAColorMonochrome, stripEndColorMonochrome, blastProtColorMonochrome, ALSColorMonochrome, TOCSColorMonochrome, runwayStripColorMonochrome) );
    
    Theme (String name) {
        switch(name) {
            case "default" : 
                currentTheme = defaultTheme;
            break;
            case "dark" :
                currentTheme = darkTheme;
            break;
            case "monochrome" :
                currentTheme = monochromeTheme;
            break;
        }
    }

    public void changeTheme (String name) {
        switch(name) {
            case "default" : 
                currentTheme = defaultTheme;
            break;
            case "dark" :
                currentTheme = darkTheme;
            break;
            case "monochrome" :
                currentTheme = monochromeTheme;
            break;
        }
    }

        //BASE COLORS
        //Color roadColor, stripeColor, warningColor, clearedAreaColor, HUDColor, backgroundColor;
        //Color outlineColor, obstacleColor1, obstacleColor2, grassColor, safeColor, dangerColor;

        //Color displacedThresholdColor, TORAColor, LDAColor, ASDAColor, TODAColor, clearwayColor; 
        //Color stopwayColor, RESAColor, stripEndColor, blastProtColor, ALSColor, TOCSColor, runwayStripColor;

    public Color getRoadColor() { return currentTheme.get(0); }
    public Color getStripeColor() { return currentTheme.get(1); }
    public Color getWarningColor() { return currentTheme.get(2); }
    public Color getClearedAreaColor() { return currentTheme.get(3); }
    public Color getHUDColor() { return currentTheme.get(4); }
    public Color getBackgroundColor() { return currentTheme.get(5); }
    public Color getOutlineColor() { return currentTheme.get(6); }
    public Color getObstacleColor1() { return currentTheme.get(7); }
    public Color getObstacleColor2() { return currentTheme.get(8); }
    public Color getGrassColor() { return currentTheme.get(9); }
    public Color getSafeColor() { return currentTheme.get(10); }
    public Color getDangerColor() { return currentTheme.get(11); }

    public Color getDisplacedThresholdColor() { return currentTheme.get(12); }
    public Color getTORAColor() { return currentTheme.get(13); }
    public Color getLDAColor() { return currentTheme.get(14); }
    public Color getASDAColor() { return currentTheme.get(15); }
    public Color getTODAColor() { return currentTheme.get(16); }
    public Color getClearwayColor() { return currentTheme.get(17); }
    public Color getStopwayColor() { return currentTheme.get(18); }
    public Color getRESAColor() { return currentTheme.get(19); }
    public Color getStripEndColor() { return currentTheme.get(20); }
    public Color getBlastProtColor() { return currentTheme.get(21); }
    public Color getALSColor() { return currentTheme.get(22); }
    public Color getTOCSColor() { return currentTheme.get(23); }
    public Color getRunwayStripColor() { return currentTheme.get(24); }
}



