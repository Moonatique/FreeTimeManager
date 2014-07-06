package fr.unice.polytech.freetime.app.TopView;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import fr.unice.polytech.freetime.app.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieving the currently selected item number
        int position = getArguments().getInt("position");

        String url = getArguments().getString("url");

        // List of rivers
        String[] menus = getResources().getStringArray(R.array.menus);

        // Creating view corresponding to the fragment
        View v = inflater.inflate(R.layout.fragment_layout, container, false);

        // Updating the action bar title
        getActivity().getActionBar().setTitle(menus[position]);

        //Initializing and loading url in webview
        WebView webView = (WebView)v.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return v;
    }
}
