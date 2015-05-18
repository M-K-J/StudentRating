package com.example.admin2015.student_rating;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by admin2015 on 25.04.2015.
 */
public class DeleteActivity extends Activity {
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<Student> students;
    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_activity);

        students = DatabaseHandler.getInstance(this).getStudentsByName("");
        data = new String[students.size()];
        for (int i = 0; i < students.size(); i++) {
            data[i] = students.get(i).toString();
        }
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete this student?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler.getInstance(getApplicationContext()).deleteStudentById(students.get(position).id);
                        dialog.dismiss();
                        students = DatabaseHandler.getInstance(DeleteActivity.this).getStudentsByName("");
                        data = new String[students.size()];
                        for (int i = 0; i < students.size(); i++) {
                            data[i] = students.get(i).toString();
                        }
                        adapter = new ArrayAdapter<String>(DeleteActivity.this, android.R.layout.simple_list_item_1, data);
                        listView.setAdapter(adapter);
                    }
                });
                builder.create().show();
            }
        });

    }
}
