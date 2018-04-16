package br.iesb.a1631088056.schallenge.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import br.iesb.a1631088056.schallenge.activities.TelaBem;
import br.iesb.a1631088056.schallenge.helpers.dummy.DummyContent;

public class Receiver extends BroadcastReceiver {


    public static final String ACTION = "br.iesb.a1631088056.schallenge.helpers.RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {


        DummyContent.DummyItem bem = (DummyContent.DummyItem) intent.getSerializableExtra("Bem");

        Log.d("Receiver.class", "HelloReceiver !!!");

        Intent notifIntent = new Intent(context,TelaBem.class);

        notifIntent.putExtra( "Bem", bem);

        String msg = intent.getStringExtra("Agendamento para Inventariar Bem "+ bem.mNomeBem);
        notifIntent.putExtra("msg", msg);

        NotificationUtil.notify(context,1,notifIntent,"Inventariar "+bem.mNomeBem, msg);
    }

}
