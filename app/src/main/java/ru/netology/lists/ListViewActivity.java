package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "my_pref";
    private static final String SHARED_PREF_KEY = "my_key";
    private static final String ATTRIBUTE_NAME_TITLE = "title";
    private static final String ATTRIBUTE_NAME_SUBTITLE = "subtitle";
    private List<Map<String, String>> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list = findViewById(R.id.list);
//        final List<Map<String, String>> values = prepareContent(values);

        final BaseAdapter listContentAdapter = createAdapter(values);
        ListView listView = findViewById(R.id.list);
        list.setAdapter(listContentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });


        init();


        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                values.clear();
                init();
                listContentAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        String[] from = {ATTRIBUTE_NAME_TITLE, ATTRIBUTE_NAME_SUBTITLE};
        int[] to = {R.id.textViewTitle, R.id.textViewSubtitle};
        return new SimpleAdapter(this, values, R.layout.list_item, from, to);
    }

    private void init() {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String saveStr = preferences.getString(SHARED_PREF_KEY, "");
        if (saveStr.isEmpty()) {
            String resourseStr = getString(R.string.large_text);
            values.addAll(prepareContent(resourseStr));
            preferences.edit().putString(SHARED_PREF_KEY, resourseStr).apply();


        } else {
            values.addAll(prepareContent(saveStr));
        }
    }

    @NonNull
    private List<Map<String, String>> prepareContent(String value) {
        String[] strings = getString(R.string.large_text).split("\n\n");
        List<Map<String, String>> list = new ArrayList<>();
        for (String str : strings) {
            Map<String, String> map = new HashMap<>();
            map.put(ATTRIBUTE_NAME_TITLE, str.length() + "");
            map.put(ATTRIBUTE_NAME_SUBTITLE, str);
            list.add(map);
        }
        return list;
    }
}
