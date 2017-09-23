package me.varunon9.remotecontrolpc.help;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import me.varunon9.remotecontrolpc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private WebView helpFragmentWebView;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        helpFragmentWebView = (WebView) rootView.findViewById(R.id.helpFragmentWebView);
        helpFragmentWebView.loadUrl(getString(R.string.help_url));
        // Enable Javascript
        WebSettings webSettings = helpFragmentWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        helpFragmentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
                //you might need this
                view.bringToFront();
            }
            @Override
            public void onPageStarted(WebView view, String url,  Bitmap favicon) {
                view.setVisibility(View.GONE);//hide the webview that will display your dialog
            }
        });
        Toast.makeText(getActivity(), "Loading, official github repository",
                Toast.LENGTH_LONG).show();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.help));
    }

}
