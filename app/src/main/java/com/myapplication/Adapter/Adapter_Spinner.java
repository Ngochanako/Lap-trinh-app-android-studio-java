package com.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Spinner extends ArrayAdapter<String> {
    public Adapter_Spinner(@NonNull Activity context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }
}
