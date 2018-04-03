package com.flymr92gmail.sejonghangugeo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.flymr92gmail.sejonghangugeo.R;

/**
 * Created by hp on 4/3/2018.
 */

public class FavoritesFragment extends DialogFragment{
    private View form = null;
    private Dialog dialog;
    private TextView textView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = null;
        form = getActivity().getLayoutInflater().inflate(R.layout.fragment_favorites, null);
        textView = form.findViewById(R.id.tv_fragment_favorites);
        String string = getActivity().getResources().getString(R.string.favorites_content);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(string));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = (builder.setTitle("Благодарности").setView(form).setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create());
        return dialog;
    }



}
