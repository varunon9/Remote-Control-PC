package me.varunon9.remotecontrolpc;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by alex on 12/03/18.
 */

public class ShortcutFragment extends Fragment {

    protected final static String command = "LAUNCH";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_shortcut, container, false);

        ShortcutTask shortcut = new ShortcutTask(rootView);
        shortcut.execute();

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.shortcut);

    }

    private static class ShortcutTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private Context context;
        private ViewGroup rootView;

        public ShortcutTask(View v) {
            rootView = (ViewGroup) v;
            context = v.getContext();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {
            String str = new String();
            MainActivity.sendMessageToServer("SHORTCUT");
            while (str == null) {
                try {
                    str = (String) MainActivity.objectInputStream.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ArrayList<String> shct = new ArrayList<String>();
            int next = str.indexOf("\n");
            int iMax = str.length() - 1;

            while (next != iMax) {
                shct.add(str.substring(0, next-1));
                str = str.substring(next+1);
                iMax -= next+1;
                next = str.indexOf("\n");
            }

            return shct;
        }

        @Override
        protected void onPostExecute(ArrayList<String> shct) {
            Iterator i = shct.iterator();

            while (i.hasNext()) {
                final String s = (String) i.next();
                Button b = new Button(context);
                b.setText(s);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.sendMessageToServer(command);
                        MainActivity.sendMessageToServer(s);
                    }
                });
                rootView.addView(b);
            }
        }

    }

}
