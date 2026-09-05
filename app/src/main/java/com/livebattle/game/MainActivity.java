package com.livebattle.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final int BG_PICK=10, MUSIC_PICK=11;
    private Uri backgroundUri; private final ArrayList<Uri> musicUris=new ArrayList<>(); private EditText endpoint; private TextView musicStatus;
    @Override public void onCreate(Bundle b){super.onCreate(b);buildUi();}
    private TextView text(String s,int sp){TextView t=new TextView(this);t.setText(s);t.setTextColor(Color.WHITE);t.setTextSize(sp);t.setPadding(4,8,4,8);return t;}
    private Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);return b;}
    private void buildUi(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(22,20,22,20);root.setBackgroundColor(Color.rgb(8,9,13));
        TextView title=text("🎮 LIVE BATTLE",28);title.setGravity(Gravity.CENTER);root.addView(title,new LinearLayout.LayoutParams(-1,70));
        TextView sub=text("Jogo nativo Android",15);sub.setGravity(Gravity.CENTER);root.addView(sub);
        Button bg=button("🖼️ Carregar arte da rodada");root.addView(bg);TextView bgStatus=text("Arte: padrão",13);root.addView(bgStatus);bg.setOnClickListener(v->pickBackground());
        Button mus=button("🎵 Adicionar músicas");root.addView(mus);musicStatus=text("Playlist: 0 músicas",13);root.addView(musicStatus);mus.setOnClickListener(v->pickMusic());
        TextView conn=text("🔌 Conector de eventos",19);conn.setPadding(0,20,0,5);root.addView(conn);
        endpoint=new EditText(this);endpoint.setHint("URL do conector (opcional)");endpoint.setHintTextColor(Color.GRAY);endpoint.setTextColor(Color.WHITE);endpoint.setSingleLine(true);root.addView(endpoint);
        Space sp=new Space(this);root.addView(sp,new LinearLayout.LayoutParams(1,0,1));
        Button start=button("▶  INICIAR JOGO");start.setTextSize(20);start.setTextColor(Color.WHITE);root.addView(start,new LinearLayout.LayoutParams(-1,64));
        TextView hint=text("O jogo entra em tela cheia. Toque 5 vezes para sair.",12);hint.setTextColor(Color.GRAY);root.addView(hint);setContentView(root);
        bg.setOnClickListener(v->pickBackground()); start.setOnClickListener(v->startGame());
    }
    private void pickBackground(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("image/*");i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,BG_PICK);}
    private void pickMusic(){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("audio/*");i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);i.addCategory(Intent.CATEGORY_OPENABLE);startActivityForResult(i,MUSIC_PICK);}
    @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(c!=RESULT_OK||d==null)return;if(r==BG_PICK&&d.getData()!=null){backgroundUri=d.getData();musicStatus.setText(musicStatus.getText());}if(r==MUSIC_PICK){musicUris.clear();if(d.getClipData()!=null)for(int x=0;x<d.getClipData().getItemCount();x++)musicUris.add(d.getClipData().getItemAt(x).getUri());else if(d.getData()!=null)musicUris.add(d.getData());musicStatus.setText("Playlist: "+musicUris.size()+" músicas");}}
    private void startGame(){Intent i=new Intent(this,GameActivity.class);if(backgroundUri!=null)i.putExtra("bg",backgroundUri.toString());ArrayList<String> ms=new ArrayList<>();for(Uri u:musicUris)ms.add(u.toString());i.putStringArrayListExtra("music",ms);i.putExtra("endpoint",endpoint.getText().toString().trim());startActivity(i);}
}
