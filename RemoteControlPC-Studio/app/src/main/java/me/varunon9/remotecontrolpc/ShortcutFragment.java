package me.varunon9.remotecontrolpc;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by alex on 12/03/18.
 */

public class ShortcutFragment extends Fragment {

    protected final static String command = "LAUNCH";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_shortcut, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.containerView);

        ShortcutTask shortcut = new ShortcutTask(gridView);
        MainActivity.sendMessageToServer("SHORTCUT");
        shortcut.execute();

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.shortcut);

    }

    private class ShortcutTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private final Context mContext;
        private GridView gridView;

        public ShortcutTask(GridView gridView) {
            mContext = getContext();
            this.gridView = gridView;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {
            String str = null;
            try {
                str = (String) MainActivity.objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayList<String> shortcuts = new ArrayList<String>();

            if (str != null) {
                int next = str.indexOf("\n");

                while (next != -1) {
                    shortcuts.add(str.substring(0, next));
                    str = str.substring(next + 1);
                    next = str.indexOf("\n");
                }
            }

            return shortcuts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> shortcuts) {

            gridView.setAdapter(new ShortcutAdapter(mContext, shortcuts));
        }

        private class ShortcutAdapter extends BaseAdapter {

            private final Context mContext;
            private final ArrayList<String> shortcuts;

            public ShortcutAdapter(Context context, ArrayList<String> shortcuts) {
                mContext = context;
                this.shortcuts = shortcuts;
            }

            @Override
            public int getCount() {
                return shortcuts.size();
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return shortcuts.get(position);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                ImageButton imageButton;
                if (convertView == null) {
                    imageButton = new ImageButton(mContext);
                    imageButton.setLayoutParams(new GridView.LayoutParams(200, 200));
                    imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                else {
                    imageButton = (ImageButton) convertView;
                }

                if (getItem(position).equals("gnome-terminal")) {
                    imageButton.setImageResource(R.mipmap.terminal);
                }
                else {
                    imageButton.setImageResource(getResources().getIdentifier((String) getItem(position), "mipmap", getActivity().getPackageName()));
                }
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.sendMessageToServer(command);
                        MainActivity.sendMessageToServer((String) getItem(position));
                    }
                });
                return imageButton;
            }

        }

    }

}
