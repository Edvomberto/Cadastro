package br.com.caelum.cadastro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.caelum.cadastro.dao.AlunoDAO;

/**
 * Created by android6017 on 31/05/16.
 */
public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent data) {

        Toast.makeText(context, "SMS do Aluno: ", Toast.LENGTH_LONG).show();

        Bundle bundle = data.getExtras();
        Object[] mensagens = (Object[]) bundle.get("pdus");

        byte[] mensagem = (byte[]) mensagens[0];

        String formato = (String) bundle.get("format");

        SmsMessage sms = SmsMessage.createFromPdu(mensagem);


        AlunoDAO dao = new AlunoDAO(context);
        if (dao.isAluno( (sms.getDisplayOriginatingAddress())))
        {
            Toast.makeText(context, "SMS do Aluno: " + sms.getMessageBody(), Toast.LENGTH_LONG).show();
        }
        dao.close();

    }
}
