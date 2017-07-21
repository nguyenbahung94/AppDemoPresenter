package com.example.nbhung.myapplicationgreendao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nbhung.myapplicationgreendao.db.DaoSession;
import com.example.nbhung.myapplicationgreendao.db.Student;
import com.example.nbhung.myapplicationgreendao.db.StudentDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private List<Student> listStudent = new ArrayList<>();
    private ArrayAdapter<Student> studentArrayAdapter;
    private ListView mListView;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daoSession = ((AppController) getApplication()).getDaoSession();
        mListView = (ListView) findViewById(R.id.lv_student);
        btnAdd = (Button) findViewById(R.id.btn_additem);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });
        setupListView();

    }

    private void setupListView() {
        studentArrayAdapter = new ArrayAdapter<Student>(this, android.R.layout.simple_expandable_list_item_1, listStudent);
        mListView.setAdapter(studentArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), listStudent.get(i).toString(), Toast.LENGTH_SHORT).show();
                deleteStudent(listStudent.get(i));
            }
        });
    }

    private void showOptions(int position) {
        final Student selectedStudent = listStudent.get(position);
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        String[] options = new String[2];
        options[0] = "Edit " + selectedStudent.getName();
        options[1] = "Delete " + selectedStudent.getName();

        alBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    proceedToUpdateItem(selectedStudent);
                } else if (which == 1) {
                    //   deleteStudent(selectedStudent.getId());
                }
            }
        });
    }

    private void fetchStudentList() {
        studentArrayAdapter.clear();
        StudentDao studentDao = daoSession.getStudentDao();
        listStudent.addAll(studentDao.loadAll());
        studentArrayAdapter.notifyDataSetChanged();
    }

    private void deleteStudent(Student student) {
        StudentDao studentDao = daoSession.getStudentDao();
        studentDao.delete(student);
        fetchStudentList();
    }

    private void proceedToUpdateItem(Student student) {
        Intent intent = new Intent(this, ModifyStudentList.class);
        intent.putExtra("create", false);
        intent.putExtra("student", (Serializable) student);
        startActivity(intent);
    }

    private void addItem(View view) {
        // Go to add item activity
        Intent intent = new Intent(this, ModifyStudentList.class);
        intent.putExtra("create", true);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchStudentList();
    }
}
