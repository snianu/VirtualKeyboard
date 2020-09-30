package com.example.samplevk;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private imeSyncCallback mImeSyncCallback;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        this.getWindow().setDecorFitsSystemWindows(false);

        final EditText et = findViewById(R.id.editTextTextPersonName);
        et.setShowSoftInputOnFocus(false);
        et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final int bottomMargin = et.getBottom();
                int mask = 0;
                if ((View.SYSTEM_UI_FLAG_HIDE_NAVIGATION & view.getWindowSystemUiVisibility()) == 0) {
                    mask = mask | WindowInsets.Type.navigationBars();
                }
                if ((View.SYSTEM_UI_FLAG_FULLSCREEN & view.getWindowSystemUiVisibility()) == 0) {
                    mask = mask | WindowInsets.Type.statusBars();
                }
                mImeSyncCallback =
                        new imeSyncCallback(
                                view,
                                mask, // Overlay, insets that should be merged with the deferred insets
                                WindowInsets.Type.ime() // Deferred, insets that will animate
                        );
                view.setWindowInsetsAnimationCallback(mImeSyncCallback);
                view.setOnApplyWindowInsetsListener(mImeSyncCallback);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        Button eb = findViewById(R.id.button);
        eb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}