package com.example.nbhung.myapplicationgreendao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nbhung.myapplicationgreendao.db.DaoSession;
import com.example.nbhung.myapplicationgreendao.db.Student;
import com.example.nbhung.myapplicationgreendao.db.StudentDao;

/**
 * Created by nbhung on 7/18/2017.
 */

public class ModifyStudentList extends AppCompatActivity {
    private Student student;
    private DaoSession mDaoSession;
    private EditText edtNameStudent, edtAgeStudent;
    private Button btnSave;
    private boolean createNew = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_student_list);
        btnSave = (Button) findViewById(R.id.btn_save_student);
        edtNameStudent = (EditText) findViewById(R.id.edt_name_ofstudent);
        edtAgeStudent = (EditText) findViewById(R.id.edt_age_ofstudent);
        mDaoSession = ((AppController) getApplication()).getDaoSession();
        handleIntent(getIntent());
        setOnclick();
    }

    private void handleIntent(Intent intent) {
        createNew = intent.getBooleanExtra("create", false);
        if (!createNew) {
            student = (Student) intent.getSerializableExtra("student");
            edtNameStudent.setText(student.getName());
            edtAgeStudent.setText(student.getAge());
        }
    }

    private void setOnclick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createNew) {
                    insertItem();
                } else {
                    updateItem(student.getId());
                }
            }
        });
    }

    private void updateItem(long id) {
        StudentDao studentDao = mDaoSession.getStudentDao();
        Student student = new Student();
        student.setId(id);
        student.setName(edtNameStudent.getText().toString());
        student.setAge(Integer.parseInt(edtAgeStudent.getText().toString()));
        studentDao.saveInTx(student);
        Toast.makeText(getApplicationContext(), "item update", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void insertItem() {
        StudentDao studentDao = mDaoSession.getStudentDao();
        Student student = new Student();
        student.setName(edtNameStudent.getText().toString());
        student.setAge(Integer.parseInt(edtAgeStudent.getText().toString()));
        studentDao.saveInTx(student);
        Toast.makeText(getApplicationContext(), "Item inserted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
