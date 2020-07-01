package id.co.myproject.angkutapps.view.profil.dialog_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import id.co.myproject.angkutapps.R;
import id.co.myproject.angkutapps.adapter.rv_chat;
import id.co.myproject.angkutapps.helper.Utils;
import id.co.myproject.angkutapps.model.Chat;

public class Df_chat extends DialogFragment {

    RecyclerView rvChat;
    EditText etPesan;
    ImageView btnSend;
    rv_chat rvAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private String temp_key;
    DatabaseReference root;
    ArrayList<Chat> list = new ArrayList<>();
    SharedPreferences sharedPreferences;

    String noHpUser;
    String noHpDriver;

    public Df_chat(String noHpUser) {
        this.noHpUser = noHpUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_messaging, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChat = view.findViewById(R.id.rvPesan);
        etPesan = view.findViewById(R.id.etPesan);
        btnSend = view.findViewById(R.id.imgButtonSend);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setHasFixedSize(false);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        noHpDriver = sharedPreferences.getString(Utils.NOHP_DRIVER_KEY, "");

        root = myRef.child(noHpUser+"-"+noHpDriver);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                Calendar calendar = Calendar.getInstance();
                String tgl = DateFormat.format("dd/MM/yyyy HH:mm", calendar.getTime()).toString();

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("msg",etPesan.getText().toString().trim());
                map2.put("waktu", tgl);
                map2.put("kondisi","Driver");

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversatin(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversatin(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String chat_msg, chat_user_name, chat_kondisi, chat_waktu;

    private void append_chat_conversatin(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chat_kondisi = (String) ((DataSnapshot) i.next()).getValue();
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_waktu = (String) ((DataSnapshot) i.next()).getValue();

            list.add(new Chat(chat_kondisi, chat_msg, chat_waktu));
            rvAdapter =new rv_chat(list, getContext());
            rvChat.setAdapter(rvAdapter);
            rvChat.scrollToPosition(rvAdapter.getItemCount()-1);

            //            chat_conversation.append(chat_kondisi + " : " + chat_msg + " : "+chat_user_name+" : "+chat_waktu +"\n");
        }
    }
}
