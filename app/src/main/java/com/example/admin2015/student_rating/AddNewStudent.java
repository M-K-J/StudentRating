package com.example.admin2015.student_rating;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by admin2015 on 21.04.2015.
 */
public class AddNewStudent extends Activity implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Button btn =(Button)v;
        switch (v.getId()){
            case R.id.addbtn:
                Intent intent = new Intent(this,AddActivity.class);
                startActivity(intent);
                break;
            case R.id.searchbtn:
                Intent search_intent= new Intent(this,SearchActivity.class);
                startActivity(search_intent);
                break;
            case R.id.deletebtn:
                Intent delete_intent = new Intent(this,DeleteActivity.class);
                startActivity(delete_intent);
                break;


        }

    }

    Button add,search,edit,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_student);
        add = (Button)findViewById(R.id.addbtn);
        search=(Button)findViewById(R.id.searchbtn);
        edit=(Button)findViewById(R.id.editbtn);
        delete=(Button)findViewById(R.id.deletebtn);


        add.setOnClickListener(this);
        search.setOnClickListener(this);
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        /*adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,adminevents);
        listview = (ListView)findViewById(R.id.listView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent delete = new Intent(AddNewStudent.this,DeleteActivity.class);
                        startActivity(delete);
                        break;
                    case 1:
                        Intent add = new Intent(AddNewStudent.this,AddActivity.class);
                        startActivity(add);
                        break;
                    case 3:
                        Intent intent = new Intent(AddNewStudent.this, SearchActivity.class);
                        startActivity(intent);
                    default:
                          break;
                }

            }
        });

*/

    }
}
