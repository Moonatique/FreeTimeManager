package fr.unice.polytech.freetime.apple.firstuse;

import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import fr.unice.polytech.freetime.apple.R;
import fr.unice.polytech.freetime.apple.services.ImportCalendarService;

/**
 * Created by Hakim on 21/06/2014.
 */
public class ImportCalendarFragment extends Fragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private CalendarReceiver calendarReceiver;
    private ListView calendarList;
    private HashMap<String, Long> calendarIds = new HashMap<String, Long>();
    private ArrayList<String> calendarNames;

    public static ImportCalendarFragment newInstance(int sectionNumber) {
        ImportCalendarFragment fragment = new ImportCalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ImportCalendarFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_import_calendar, container, false);

        IntentFilter filter = new IntentFilter(CalendarReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        calendarReceiver = new CalendarReceiver();
        getActivity().registerReceiver(calendarReceiver, filter);

        Intent importCalendarIntent = new Intent(getActivity(), ImportCalendarService.class);
        importCalendarIntent.putExtra(ImportCalendarService.PARAM_METHOD, ImportCalendarService.METHOD_GET_ALL_CALENDARS);
        getActivity().startService(importCalendarIntent);

        calendarNames = new ArrayList<String>();


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(calendarReceiver);
    }

    public class CalendarReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "all_calendars_requested";

        @Override
        public void onReceive(Context context, Intent intent) {

            calendarIds = (HashMap<String, Long>) intent.getSerializableExtra(ImportCalendarService.PARAM_OUT_CALENDARS);
            calendarNames.clear();
            calendarNames.addAll(calendarIds.keySet());

            final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, calendarNames);
            calendarList = (ListView) getActivity().findViewById(R.id.calendarListView);
            calendarList.setAdapter(adapter);

            calendarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    long selectedCalId = calendarIds.get(calendarList.getItemAtPosition(position));

                    Intent importCalendarIntent = new Intent(getActivity(), ImportCalendarService.class);
                    importCalendarIntent.putExtra(ImportCalendarService.PARAM_METHOD, ImportCalendarService.METHOD_IMPORT);
                    importCalendarIntent.putExtra(ImportCalendarService.PARAM_IN_CAL_ID, selectedCalId);
                    getActivity().startService(importCalendarIntent);
                }
            });
        }
    }
}
