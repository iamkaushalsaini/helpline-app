package com.hlit.helplinetelecom;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BackableFragment extends Fragment implements View.OnKeyListener {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP){
            if(keyCode == KeyEvent.KEYCODE_BACK){
                onBackButtonPressed();
                return true;
            }
        }
        return false;
    }

    protected abstract void onBackButtonPressed();
}

