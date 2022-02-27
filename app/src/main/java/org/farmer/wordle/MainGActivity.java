package org.farmer.wordle;

import static org.farmer.wordle.Data.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.GradientDrawable;

import com.hardgusol.sol.R;

import sol.Solver;

public class MainGActivity extends Activity {


    private static final String USER_THEME_ID = "USER_THEME_ID";
    private static final String USER_SCORE = "USER_SCORE";

    public static final int KEYBOARD[] = {R.id.keys1, R.id.keys2, R.id.keys3};


    public static final int LAYOUT_ROWS[] = {
            R.id.row1, R.id.row2, R.id.row3, R.id.row4, R.id.row5, R.id.row6};


    private TextView letterTvs[][];
    private Map<String, TextView> keyboardMap;
    private Toast toast;
    private int row;


    private TextView currTextView;

    final static int NUM_OF_COLUMNS = 5;


    protected void ring() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        RingtoneManager.getRingtone(this, uri).play();
    }


    protected void popup(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    // On create
    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {


        //setTheme(R.style.Purple_Theme);

        int themeIDIndex = readPrefInt(USER_THEME_ID);
        setTheme(themeIDs[themeIDIndex]);


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_g);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle("Solver");


        // ring();

        keyboardMap = new HashMap<String, TextView>();
        for (int id : KEYBOARD) {
            ViewGroup group = (ViewGroup) findViewById(id);
            for (int i = 0; i < group.getChildCount(); i++) {
                View view = group.getChildAt(i);
                if (view instanceof TextView) {
                    view.setOnClickListener((v) -> keyboardClicked(v));
                    keyboardMap.put(((TextView) view).getText().toString(),
                            (TextView) view);
                }
            }
        }

        View view = findViewById(R.id.back2_key);
        view.setOnClickListener((v) -> backspaceClicked(v));

        view = findViewById(R.id.back1_key);
        view.setOnClickListener((v) -> backspaceClicked(v));

        view = findViewById(R.id.enter_key);
        view.setOnClickListener((v) -> enterClicked(v));


        letterTvs = new TextView[LAYOUT_ROWS.length][];
        int row = 0;
        for (int id : LAYOUT_ROWS) {
            ViewGroup group = (ViewGroup) findViewById(id);
            letterTvs[row] = new TextView[group.getChildCount()];
            for (int i = 0; i < group.getChildCount(); i++) {
                TextView textView = (TextView) group.getChildAt(i);
                textView.setOnClickListener((v) -> textViewClicked((TextView) v));

                letterTvs[row][i] = textView;
                int bgcolor = colorGreen;
                int textColor = colorDeepGreen;

                if (row > 0) {
                    bgcolor = colorYellow;
                    textColor = colorBrown;
                }
                if (row > 3) {
                    bgcolor = colorGrey;
                    textColor = colorDeepGrey;
                }

                textView.setTag(bgcolor);
                setTextViewBgColor(textView, (Integer) textView.getTag(), colorBorder);
                textView.setTextColor(textColor);
            }


            row++;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get id
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh_action:
                refresh();
                break;


            case R.id.help_action:
                help();
                break;

            case R.id.about_action:
                about();
                break;

            case R.id.theme_action:
                showThemeDialog();
                break;

            default:
                return false;
        }

        return true;
    }


    private void showThemeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Theme:").setItems(themes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int clickedIndex) {
                savePrefInt(USER_THEME_ID, clickedIndex);
                showToastString("Need to restart the app to take effect");
            }
        });

        builder.create().show();
    }


    private void savePrefInt(String key, int num) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, num);
        editor.commit();
    }

    private int readPrefInt(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(key, 0);
    }


    public void textViewClicked(TextView textView) {
        if (currTextView != null)
            setTextViewBgColor(currTextView, (Integer) currTextView.getTag(), colorBorder);

        currTextView = textView;
        setTextViewBgColor(currTextView, (Integer) currTextView.getTag(), colorBorderFocus);
    }

    public void keyboardClicked(View v) {

        CharSequence s = ((TextView) v).getText();


        if (currTextView != null) {

            currTextView.setText(s);
            setTextViewBgColor(currTextView, (Integer) currTextView.getTag(), colorBorderFocus);
        }else{
            popup("Tap a cell first !!");
        }

    }


    private void setTextViewBgColor(TextView tv, int bgcolor, int strokeColor) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bgcolor);
        gd.setStroke(7, strokeColor);
        tv.setBackground(gd);
    }


    public void enterClicked(View v) {

        String[] green = new String[]{"", "", "", "", ""};
        String[] yellow1 = new String[]{"", "", "", "", ""};
        String[] yellow2 = new String[]{"", "", "", "", ""};
        String[] yellow3 = new String[]{"", "", "", "", ""};
        String gray = "";

        String[][] colorArr = new String[][]{green, yellow1, yellow2, yellow3};

        int i = 0;

        while (i < letterTvs.length) {
            for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                String letter = ((String) letterTvs[i][j].getText()).toLowerCase();
                if (i < 4) {
                    colorArr[i][j] = letter;
                } else {
                    gray += letter;
                }
            }
            i++;
        }


        /*   for testing
        String msg = "Green 3: " + green[2];
        msg += "\nGreen 4: " + green[3];
        msg += "\nYellow1 3: " + yellow1[2];
        msg += "\nYellow2 3: " + yellow2[2];
        msg += "\nYellow3 3: " + yellow3[2];
        msg += "\nGray: " + gray;
        popup(msg); */


        Solver solver = new Solver(green, yellow1, yellow2, yellow3, gray);
        String matchWords = solver.solve(5);
        int numOfMatch = solver.getNumOfMatch();


        if (numOfMatch == 0) {
            showBasicDialog("", "No Matches");
            return;
        } else {


            String result = "Total Matches:" + numOfMatch + "\n\n" + reduceLines(matchWords, 20, true);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_show_matches, null);
            TextView matchesTextView = (TextView) mView.findViewById(R.id.matchesTextView);
            matchesTextView.setText(result);
            mBuilder.setView(mView);


            Button showAllMatchButton = (Button) mView.findViewById(R.id.showAllMatchButton);
            Button cancelButton = (Button) mView.findViewById(R.id.cancelButton);

            final AlertDialog dialog = mBuilder.create();

            if (numOfMatch > 70)
                showAllMatchButton.setOnClickListener((view) -> {
                    allMatchedWords = "Total:" + numOfMatch + "\n\n" + reduceLines(matchWords, 1000, true);
                    Intent intent = new Intent();
                    intent.setClassName(this, ShowALLMatchesActivity.class.getName());
                    startActivity(intent);
                    dialog.dismiss();
                });

            cancelButton.setOnClickListener((view) -> dialog.dismiss());

            dialog.show();

        }

    }


    static String reduceLines(String lines, int rows, boolean uppercase) {
        String out = "";

        if (lines == null || lines.isEmpty())
            return "";

        String[] arr = lines.split("\n");

        for (int i = 0; i < arr.length; i++) {
            if (i < rows) {
                String line = arr[i].trim();
                if (uppercase) line = line.toUpperCase();

                out += line + "\n";
            } else {
                out += "... ... ...";
                break;
            }

        }

        return out;
    }

    // backspaceClicked
    public void backspaceClicked(View v) {

        if (currTextView != null) {
            setTextViewBgColor(currTextView, (Integer) currTextView.getTag(), colorBorderFocus);
            currTextView.setText("");

        }

    }

    // refresh
    private void refresh() {
        for (TextView r[] : letterTvs) {
            for (TextView t : r) {
                t.setText("");
            }
        }

        for (TextView t : keyboardMap.values().toArray(new TextView[0])) {
            t.setTextColor(colorWhite);
        }


        row = 0;

        showToastString("Restart");
    }


    private void help() {
        Intent intent = new Intent(this, HelpHtmlActivity.class);
        startActivity(intent);
    }

    private boolean about() {

        String msg = "This app is based on \ngithub.com/billthefarmer/gurgle\n\nLicence GNU GPLv3";
        showBasicDialog("about", msg);

        return true;
    }


    void showToastString(String text) {

        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    void showToastLongString(String text) {

        toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    void showBasicDialog(String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok", null);
        dialog.setNegativeButton("Cancel", null);
        dialog.create().show();
    }
}
