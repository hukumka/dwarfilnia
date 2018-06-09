package nktl.math;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
 */
public class Format {
    public static final String sample_text = "-0.00e-100";
    private static DecimalFormat formNoE, formE;

    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        formNoE = new DecimalFormat("#.#######", dfs);
        formE = new DecimalFormat("#.##E0", dfs);
    }

    public static String format(double value){
        if (value == 0) return "0";
        //*
        if ((value < 100000 && value > 0.0001) ||
                (value > -100000 && value < -0.0001)){
            if ((int)value == value) return "" + (int)value;
            String str = formNoE.format(value);
            return //str.length() > 8 ? str.substring(0, 8) :
                    str;
        }/**/
        return formE.format(value);
    }
}
