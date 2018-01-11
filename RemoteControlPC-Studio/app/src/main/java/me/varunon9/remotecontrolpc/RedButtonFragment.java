package me.varunon9.remotecontrolpc;

/**
 * Created by david on 13/12/17.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class RedButtonFragment extends Fragment implements View.OnClickListener {
    private Button butt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_red_button, container, false);
        butt = (Button) rootView.findViewById(R.id.redButton);

        butt.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("DON'T CLICK ON THE RED BUTTON");
    }

    @Override
    public void onClick(View view) {
        if(view == butt){
            final Socket socket;

            try {
                socket = new Socket("192.168.1.78", 3000);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataOutputStream dos;
                        try {
                            dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeInt(12);
                            dos.close();
                            socket.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        Toast.makeText(getContext(),"Boutton cliqué",Toast.LENGTH_SHORT).show();
    }}
}
