package org.farmer.wordle;

import android.graphics.Color;

import com.hardgusol.sol.R;

public class Data {


    public static final String[] themes =  {
            "Blue",
            "Red",
            "Black",
            "Orange",
            "Cyan",
            "Purple",
            "Brown",
            "Green"
    };

    public static   final int[] themeIDs =  {
            R.style.Blue_Theme,
            R.style.Red_Theme,
            R.style.Black_Theme,
            R.style.Orange_Theme,
            R.style.Cyan_Theme,
            R.style.Purple_Theme,
            R.style.Brown_Theme,
            R.style.Green_Theme
    };



    public static final int colorGreen = Color.parseColor("#B5FF93");
    public static final int colorYellow = Color.parseColor("#FFFB08");

    public static final int colorDeepGreen = Color.parseColor("#104009");
    public static final int colorBrown = Color.parseColor("#784C16");
    public static final int colorDeepGrey = Color.parseColor("#2B2A28");

    public static final int colorGrey = Color.parseColor("#D5D0D9");
    public static final int colorWhite = Color.parseColor("#FFFFFF");
    public static final int colorRed = Color.parseColor("#FC0808");
    public static final int colorBlack = Color.parseColor("#000000");





    public static final int colorBorderFocus =  colorRed;
    public static final int colorBorder =  colorBlack;

    public static String  allMatchedWords  = "";

}
