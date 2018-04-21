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

public class FavoritesFragment extends DialogFragment{
    private String text;

    public void setListeningText(String text) {
        this.text = text;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        View form = getActivity().getLayoutInflater().inflate(R.layout.fragment_favorites, null);
        TextView textView = form.findViewById(R.id.tv_fragment_favorites);
        String title;
        if (text == null) {
            text = "<p> &emsp;  Преподавателям школы “Седжонг” за интересное и продуктивное обучение корейскому языку, а именно: Ким Виктории, Шин Ирине и И Хёшин (이효신). Также авторам используемых в приложении ресурсов и программных средств: </p>\n" +
                    "<p> &emsp;  - Joan Zapata за Android PdfViewer (https://github.com/barteksc/AndroidPdfViewer) <br />\n" +
                    "&emsp;  - Florent Champigny за MaterialViewPager (http://www.florentchampigny.com/) <br />\n" +
                    "&emsp;  - Evgenii Zagumennyi за Android-ExpandIcon (https://github.com/zagum/Android-ExpandIcon) <br />\n" +
                    "&emsp;  - Yesid Lazaro за GmailBackground (https://github.com/yesidlazaro/GmailBackground) <br />\n" +
                    "&emsp;  - FlowingDrawer (https://github.com/mxn21/FlowingDrawer) <br />\n" +
                    "&emsp;  - Yavor Ivanov за fab-speed-dial (https://github.com/yavski/fab-speed-dial) <br />\n" +
                    "&emsp;  - Android Sliding Up Panel (https://github.com/umano/AndroidSlidingUpPanel) <br />\n" +
                    "&emsp;  - Davide Steduto за FlipView (https://github.com/davideas/FlipView) <br />\n" +
                    "&emsp;  - Basil Miller за NavigationTabStrip (https://github.com/Devlight/NavigationTabStrip) <br />\n" +
                    "&emsp;  - EuroMillions за FloatingToolbar (https://github.com/rubensousa/FloatingToolbar) <br />\n" +
                    "&emsp;  - Jeff Gilfelt за SQLiteAssetHelper  (https://github.com/jgilfelt/android-sqlite-asset-helper) <br />\n" +
                    "&emsp;  - Владимр Ким. Корейские пословицы и поговорки https://koryo-saram.ru/  </p>\n" +
                    "<p>&emsp; С уважением,<br/>\n " +
                    "&emsp; Максим Петров <br/>\n"+
                    "&emsp; flymr92@gmail.com</p>\n";
            title = " " + getActivity().getResources().getString(R.string.thanks_to);
        }else {
            title = getActivity().getResources().getString(R.string.listening_text);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml("<big>"+text+"</big>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml("<big>"+text+"</big>"));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = (builder.setTitle(title).setView(form).setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create());
        return dialog;
    }



}
