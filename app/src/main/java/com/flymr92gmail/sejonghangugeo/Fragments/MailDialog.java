package com.flymr92gmail.sejonghangugeo.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.flymr92gmail.sejonghangugeo.R;
import com.flymr92gmail.sejonghangugeo.Utils.PrefManager;

public class MailDialog extends DialogFragment {
    private View form = null;
    private Dialog dialog;
    private TextInputEditText etUserMail, etTitle, etBody;
    private PrefManager prefManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prefManager = new PrefManager(getActivity());
        dialog = null;
        form = getActivity().getLayoutInflater().inflate(R.layout.fragment_mail, null);
        etUserMail = form.findViewById(R.id.user_mail_et);
        etTitle = form.findViewById(R.id.massage_header_et);
        etBody = form.findViewById(R.id.massage_body);
        etUserMail.setText(prefManager.getUserMail());
        etTitle.setText(prefManager.getMailTitle());
        etBody.setText(prefManager.getMailBody());
        if (prefManager.getUserMail().equals(""))etUserMail.requestFocus();
        else if (prefManager.getMailTitle().equals(""))etTitle.requestFocus();
        else etBody.requestFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = (builder.setTitle(R.string.send_massage).setView(form).setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveMail();
                if (!sendMassage()) return;
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                   saveMail();
                    }
                }).create());
        return dialog;
    }

    private boolean sendMassage(){
        String massage = etBody.getText().toString();
        String title = etUserMail.getText().toString();
        if (massage.equals("")){
            Toast.makeText(getActivity(),R.string.write_message_theme, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (title.equals("")){
            Toast.makeText(getActivity(),R.string.write_message, Toast.LENGTH_SHORT).show();
            return false;
        }
        BackgroundMail.newBuilder(getActivity())
                .withUsername(R.string.app_mail)
                .withPassword("123")
                .withMailto(R.string.app_mail_outbox)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(getString(R.string.message_subject, etUserMail.getText().toString(),etTitle.getText().toString()))
                .withBody(etBody.getText().toString())
                .withProcessVisibility(true)
                .withSendingMessageSuccess(R.string.send_success)
                .withSendingMessageError(R.string.send_error)
                .withSendingMessage(R.string.sending)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        clearMail();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {

                    }
                })
                .send();
        return true;
    }

    private void clearMail(){
        prefManager.setMailBody("");
        prefManager.setMailTitle("");
    }

    private void saveMail(){
        prefManager.setUserMail(etUserMail.getText().toString());
        prefManager.setMailTitle(etTitle.getText().toString());
        prefManager.setMailBody(etBody.getText().toString());
    }


}
