package com.example.admin2015.student_rating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;


public class SearchActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<Student> students;
    private String[] data;
    EditText search;
   Button search_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        search  = (EditText)findViewById(R.id.search);


        search_btn = (Button)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                students = DatabaseHandler.getInstance(SearchActivity.this).getStudentsByName(search.getText().toString());
                data = new String[students.size()];
                for (int i = 0; i < students.size(); i++) {
                    data[i] = students.get(i).toString();
                }
                adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, data);
                listView.setAdapter(adapter);
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        students = DatabaseHandler.getInstance(SearchActivity.this).getStudentsByName(search.getText().toString());
        data = new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            data[i] = students.get(i).toString();
        }
        adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this,StudentInfo.class);
                intent.putExtra("student", students.get(position));
                startActivity(intent);
            }
        });


    }

    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
