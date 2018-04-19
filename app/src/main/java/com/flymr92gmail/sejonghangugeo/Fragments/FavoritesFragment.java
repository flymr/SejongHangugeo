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
            text = "<p>&emsp;Так говорят о человеке, который возвысившись, возомнил себя пупом земли. Русский аналог – «Из грязи – в князи».</p>\n" +
                    "<p>&emsp;Избрание дракона идеалом человеческих стремлений произошло потому, что корейцы свято верили, что именно это мифическое животное двигает облака и насылает на землю дожди, туманы, и молнии. Словом, обладает огромной властью. Не случайно короля почитали, как олицетворение дракона, а сам престол называли троном дракона.</p>\n" +
                    "<p>&emsp;Другое выражение: «Пробираться через драконовы ворота» означало – сделать чиновничью карьеру. На реке Янцзы есть место под названием «Драконовы ворота», где течение настолько сильное, что, говорят, даже рыба, преодолевшая его, становится драконом. Так что, заслужить похвалу, что ты прошел Драконовы ворота, как видите, не просто.</p>\n" +
                    "<p>&emsp;И хотелось бы упомянуть эпическую поэму «Песнь дракона», объявшую тему, ни много ни мало, управления всей вселенной. Она была опубликована в 1447 году и является первой литературной работой, написанной на корейском алфавите. Поэма восхваляет основателей королевской династии Ли. Почти все корейцы знают первые строки поэмы:</p>\n" +
                    "<p>&emsp;Дерево с крепкими корнями щедро плодоносит и не сгибается от ветра,<br />\n" +
                    "&emsp;Глубокий колодец никогда не высыхает,<br />\n" +
                    "&emsp;Текущая вода обязательно достигает моря.</p>";
            title = getActivity().getResources().getString(R.string.thanks_to);
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
