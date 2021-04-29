import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;

public class Theme {

    ArrayList<Color> currentTheme = new ArrayList<Color>();
    
    //DEFAULT THEME
    Color roadColorDefault = Color.rgb(208, 211, 212), stripeColorDefault = Color.rgb(253, 254, 254), warningColorDefault = Color.rgb(243, 156, 18);
    Color clearedAreaColorDefault = Color.rgb( 133, 193, 233), HUDColorDefault = Color.rgb(127, 179, 213), backgroundColorDefault = Color.rgb(214, 234, 248);
    Color outlineColorDefault = Color.rgb(23, 32, 42), obstacleColor1Default = Color.rgb(231, 76, 60), obstacleColor2Default = Color.rgb(146, 43, 33);
    Color grassColorDefault = Color.rgb(162, 217, 206), safeColorDefault = Color.rgb(46, 204, 113), dangerColorDefault = Color.rgb(231, 76, 60);

    Color displacedThresholdColorDefault = Color.rgb(120, 40, 31), TORAColorDefault = Color.rgb(27, 79, 114), LDAColorDefault = Color.rgb(81, 46, 95); 
    Color ASDAColorDefault = Color.rgb(20, 90, 50), TODAColorDefault = Color.rgb(125, 102, 8), clearwayColorDefault, stopwayColorDefault, RESAColorDefault = Color.rgb(23, 32, 42);
    Color stripEndColorDefault, blastProtColorDefault, ALSColorDefault, TOCSColorDefault, runwayStripColorDefault;

    ArrayList<Color> defaultTheme = new ArrayList<Color>( Arrays.asList(roadColorDefault, stripeColorDefault, warningColorDefault, clearedAreaColorDefault, 
    HUDColorDefault, backgroundColorDefault, outlineColorDefault, obstacleColor1Default, obstacleColor2Default, grassColorDefault, safeColorDefault, 
    dangerColorDefault, displacedThresholdColorDefault, TORAColorDefault, LDAColorDefault, ASDAColorDefault, TODAColorDefault, clearwayColorDefault, 
    stopwayColorDefault, RESAColorDefault, stripEndColorDefault, blastProtColorDefault, ALSColorDefault, TOCSColorDefault, runwayStripColorDefault) );

    //DARK THEME
    Color roadColorDark = Color.LIGHTGRAY, stripeColorDark = Color.WHITE, warningColorDark = Color.GOLDENROD;
    Color clearedAreaColorDark = Color.DODGERBLUE, HUDColorDark = Color.LIGHTSTEELBLUE, backgroundColorDark = Color.LIGHTCYAN;
    Color outlineColorDark = Color.BLACK, obstacleColor1Dark  = Color.FIREBRICK, obstacleColor2Dark  = Color.CRIMSON;
    Color grassColorDark = Color.DARKSEAGREEN, safeColorDark = Color.GREEN, dangerColorDark = Color.RED;

    Color displacedThresholdColorDark = Color.NAVY, TORAColorDark = Color.ORANGERED, LDAColorDark = Color.DARKMAGENTA, ASDAColorDark = Color.MAROON, TODAColorDark = Color.SIENNA;
    Color clearwayColorDark, stopwayColorDark, RESAColorDark = Color.DEEPPINK;
    Color stripEndColorDark, blastProtColorDark, ALSColorDark, TOCSColorDark, runwayStripColorDark;

    ArrayList<Color> darkTheme = new ArrayList<Color>( Arrays.asList(roadColorDark, stripeColorDark, warningColorDark, clearedAreaColorDark, 
    HUDColorDark, backgroundColorDark, outlineColorDark, obstacleColor1Dark, obstacleColor2Dark, grassColorDark, safeColorDark, 
    dangerColorDark, displacedThresholdColorDark, TORAColorDark, LDAColorDark, ASDAColorDark, TODAColorDark, clearwayColorDark, 
    stopwayColorDark, RESAColorDark, stripEndColorDark, blastProtColorDark, ALSColorDark, TOCSColorDark, runwayStripColorDark) );

    //PASTELLE THEME
    Color roadColorPastelle = Color.LIGHTGRAY, stripeColorPastelle = Color.WHITE, warningColorPastelle = Color.GOLDENROD;
    Color clearedAreaColorPastelle = Color.DODGERBLUE, HUDColorPastelle = Color.LIGHTSTEELBLUE, backgroundColorPastelle = Color.LIGHTCYAN;
    Color outlineColorPastelle = Color.BLACK, obstacleColor1Pastelle = Color.FIREBRICK, obstacleColor2Pastelle = Color.CRIMSON;
    Color grassColorPastelle = Color.DARKSEAGREEN, safeColorPastelle = Color.GREEN, dangerColorPastelle = Color.RED;

    Color displacedThresholdColorPastelle = Color.NAVY, TORAColorPastelle = Color.ORANGERED, LDAColorPastelle = Color.DARKMAGENTA, ASDAColorPastelle = Color.MAROON, TODAColorPastelle = Color.SIENNA;
    Color clearwayColorPastelle, stopwayColorPastelle, RESAColorPastelle = Color.DEEPPINK;
    Color stripEndColorPastelle, blastProtColorPastelle, ALSColorPastelle, TOCSColorPastelle, runwayStripColorPastelle;

    ArrayList<Color> pastelleTheme = new ArrayList<Color>( Arrays.asList(roadColorPastelle, stripeColorPastelle, warningColorPastelle, clearedAreaColorPastelle, 
    HUDColorPastelle, backgroundColorPastelle, outlineColorPastelle, obstacleColor1Pastelle, obstacleColor2Pastelle, grassColorPastelle, safeColorPastelle, 
    dangerColorPastelle, displacedThresholdColorPastelle, TORAColorPastelle, LDAColorPastelle, ASDAColorPastelle, TODAColorPastelle, clearwayColorPastelle, 
    stopwayColorPastelle, RESAColorPastelle, stripEndColorPastelle, blastProtColorPastelle, ALSColorPastelle, TOCSColorPastelle, runwayStripColorPastelle) );

    //COLORBLIND THEME
    Color roadColorColorblind = Color.LIGHTGRAY, stripeColorColorblind = Color.WHITE, warningColorColorblind = Color.GOLDENROD;
    Color clearedAreaColorColorblind = Color.DODGERBLUE, HUDColorColorblind = Color.LIGHTSTEELBLUE, backgroundColorColorblind = Color.LIGHTCYAN;
    Color outlineColorColorblind = Color.BLACK, obstacleColor1Colorblind = Color.FIREBRICK, obstacleColor2Colorblind = Color.CRIMSON;
    Color grassColorColorblind = Color.DARKSEAGREEN, safeColorColorblind = Color.GREEN, dangerColorColorblind = Color.RED;

    Color displacedThresholdColorColorblind = Color.NAVY, TORAColorColorblind = Color.ORANGERED, LDAColorColorblind = Color.DARKMAGENTA, ASDAColorColorblind = Color.MAROON, TODAColorColorblind = Color.SIENNA;
    Color clearwayColorColorblind, stopwayColorColorblind, RESAColorColorblind = Color.DEEPPINK;
    Color stripEndColorColorblind, blastProtColorColorblind, ALSColorColorblind, TOCSColorColorblind, runwayStripColorColorblind;

    ArrayList<Color> colorblindTheme = new ArrayList<Color>( Arrays.asList(roadColorColorblind, stripeColorColorblind, warningColorColorblind, clearedAreaColorColorblind, 
    HUDColorColorblind, backgroundColorColorblind, outlineColorColorblind, obstacleColor1Colorblind, obstacleColor2Colorblind, grassColorColorblind, safeColorColorblind, 
    dangerColorColorblind, displacedThresholdColorColorblind, TORAColorColorblind, LDAColorColorblind, ASDAColorColorblind, TODAColorColorblind, clearwayColorColorblind, 
    stopwayColorColorblind, RESAColorColorblind, stripEndColorColorblind, blastProtColorColorblind, ALSColorColorblind, TOCSColorColorblind, runwayStripColorColorblind) );

    //CONTRAST THEME
    Color roadColorContrast = Color.LIGHTGRAY, stripeColorContrast = Color.WHITE, warningColorContrast = Color.GOLDENROD;
    Color clearedAreaColorContrast = Color.DODGERBLUE, HUDColorContrast = Color.LIGHTSTEELBLUE, backgroundColorContrast = Color.LIGHTCYAN;
    Color outlineColorContrast = Color.BLACK, obstacleColor1Contrast = Color.FIREBRICK, obstacleColor2Contrast = Color.CRIMSON;
    Color grassColorContrast = Color.DARKSEAGREEN, safeColorContrast = Color.GREEN, dangerColorContrast = Color.RED;

    Color displacedThresholdColorContrast = Color.NAVY, TORAColorContrast = Color.ORANGERED, LDAColorContrast = Color.DARKMAGENTA, ASDAColorContrast = Color.MAROON, TODAColorContrast = Color.SIENNA;
    Color clearwayColorContrast, stopwayColorContrast, RESAColorContrast = Color.DEEPPINK;
    Color stripEndColorContrast, blastProtColorContrast, ALSColorContrast, TOCSColorContrast, runwayStripColorContrast;

    ArrayList<Color> contrastTheme = new ArrayList<Color>( Arrays.asList(roadColorContrast, stripeColorContrast, warningColorContrast, clearedAreaColorContrast, 
    HUDColorContrast, backgroundColorContrast, outlineColorContrast, obstacleColor1Contrast, obstacleColor2Contrast, grassColorContrast, safeColorContrast, 
    dangerColorContrast, displacedThresholdColorContrast, TORAColorContrast, LDAColorContrast, ASDAColorContrast, TODAColorContrast, clearwayColorContrast, 
    stopwayColorContrast, RESAColorContrast, stripEndColorContrast, blastProtColorContrast, ALSColorContrast, TOCSColorContrast, runwayStripColorContrast) );

    Theme (String name) {
        switch(name) {
            case "default" : 
                currentTheme = defaultTheme;
            break;
            case "dark" :
                currentTheme = darkTheme;
            break;
            case "pastelle" :
                currentTheme = pastelleTheme;
            break;
            case "colorblind" :
                currentTheme = colorblindTheme;
            break;
            case "contrast" :
                currentTheme = contrastTheme;
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
            case "pastelle" :
                currentTheme = pastelleTheme;
            break;
            case "colorblind" :
                currentTheme = colorblindTheme;
            break;
            case "contrast" :
                currentTheme = contrastTheme;
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
