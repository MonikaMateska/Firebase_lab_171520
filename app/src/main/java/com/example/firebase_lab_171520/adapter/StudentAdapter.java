package com.example.firebase_lab_171520.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase_lab_171520.R;
import com.example.firebase_lab_171520.models.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Optional;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> students;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference studentsRef = database.getReference("students");
    private FirebaseAuth mAuth;

    public StudentAdapter(List<Student> students) {
        this.students = students;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, null, false);
       return new StudentViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bindData(this.students.get(position));
    }

    public void updateData(List<Student> students) {
        this.students = students;
        this.notifyDataSetChanged();
    }

    public StudentAdapter() {
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView etItemIndex, etItemName, etItemSurname, etItemPhone, etItemAddress;
        private EditText itemIndex, itemName, itemSurname, itemPhone, itemAddress;
        private Button btnEditItemStudent, btnDeleteItemStudent, btnUpdateItemStudent;

        public StudentViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            etItemIndex = itemView.findViewById(R.id.etItemIndex);
            etItemName = itemView.findViewById(R.id.etItemName);
            etItemSurname = itemView.findViewById(R.id.etItemSurname);
            etItemPhone = itemView.findViewById(R.id.etItemPhone);
            etItemAddress = itemView.findViewById(R.id.etItemAddress);
            btnEditItemStudent = itemView.findViewById(R.id.btnEditItemStudent);
            btnDeleteItemStudent = itemView.findViewById(R.id.btnDeleteItemStudent);
            btnUpdateItemStudent = itemView.findViewById(R.id.btnUpdateItemStudent);

            itemIndex = itemView.findViewById(R.id.itemIndex);
            itemName = itemView.findViewById(R.id.itemName);
            itemSurname = itemView.findViewById(R.id.itemSurname);
            itemPhone = itemView.findViewById(R.id.itemPhone);
            itemAddress = itemView.findViewById(R.id.itemAddress);

            itemIndex.setVisibility(View.GONE);
            itemName.setVisibility(View.GONE);
            itemSurname.setVisibility(View.GONE);
            itemPhone.setVisibility(View.GONE);
            itemAddress.setVisibility(View.GONE);
            btnUpdateItemStudent.setVisibility(View.GONE);

            btnDeleteItemStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String index = etItemIndex.getText().toString();
                    deleteStudent(index, context);
                }
            });

            btnEditItemStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemIndex.setText(etItemIndex.getText().toString());
                    etItemIndex.setVisibility(View.GONE);
                    itemIndex.setVisibility(View.VISIBLE);

                    itemName.setText(etItemName.getText().toString());
                    etItemName.setVisibility(View.GONE);
                    itemName.setVisibility(View.VISIBLE);

                    itemSurname.setText(etItemSurname.getText().toString());
                    etItemSurname.setVisibility(View.GONE);
                    itemSurname.setVisibility(View.VISIBLE);

                    itemPhone.setText(etItemPhone.getText().toString());
                    etItemPhone.setVisibility(View.GONE);
                    itemPhone.setVisibility(View.VISIBLE);

                    itemAddress.setText(etItemAddress.getText().toString());
                    etItemAddress.setVisibility(View.GONE);
                    itemAddress.setVisibility(View.VISIBLE);

                    btnEditItemStudent.setVisibility(View.GONE);
                    btnUpdateItemStudent.setVisibility(View.VISIBLE);
                }
            });

            btnUpdateItemStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String prevIndex = etItemIndex.getText().toString();
                    String index = itemIndex.getText().toString();
                    String name = itemName.getText().toString();
                    String surname = itemSurname.getText().toString();
                    String phone = itemPhone.getText().toString();
                    String address = itemAddress.getText().toString();

                    if (index.isEmpty() || name.isEmpty() || surname.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        Toast.makeText(context, "Please enter all the fields", Toast.LENGTH_LONG).show();
                        return;
                    }
                    editStudent(prevIndex, index, name, surname, phone, address, context);
                    etItemIndex.setText(itemIndex.getText().toString());
                    itemIndex.setVisibility(View.GONE);
                    etItemIndex.setVisibility(View.VISIBLE);

                    etItemName.setText(itemName.getText().toString());
                    itemName.setVisibility(View.GONE);
                    etItemName.setVisibility(View.VISIBLE);

                    etItemSurname.setText(itemSurname.getText().toString());
                    itemSurname.setVisibility(View.GONE);
                    etItemSurname.setVisibility(View.VISIBLE);

                    etItemPhone.setText(itemPhone.getText().toString());
                    itemPhone.setVisibility(View.GONE);
                    etItemPhone.setVisibility(View.VISIBLE);

                    etItemAddress.setText(itemAddress.getText().toString());
                    itemAddress.setVisibility(View.GONE);
                    etItemAddress.setVisibility(View.VISIBLE);

                    btnEditItemStudent.setVisibility(View.VISIBLE);
                    btnUpdateItemStudent.setVisibility(View.GONE);
                }
            });
        }

        public void bindData(final Student student) {
            etItemIndex.setText(student.getIndex());
            etItemName.setText(student.getName());
            etItemSurname.setText(student.getSurname());
            etItemPhone.setText(student.getPhone());
            etItemAddress.setText(student.getAddress());
        }

        public void deleteStudent(String index, Context context) {
            Query studentsQuery = studentsRef.orderByChild("index").equalTo(index);

            studentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot studentSnapshot: dataSnapshot.getChildren()) {
                        studentSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Deletion failed", Toast.LENGTH_LONG).show();
                }
            });
        }

        public void editStudent(String index,
                                String itemIndex,
                                String itemName,
                                String itemSurname,
                                String itemPhone,
                                String itemAddress,
                                Context context) {
            String userId = mAuth.getUid();
            Student updatedStudent = new Student(userId, itemIndex, itemName, itemSurname, itemPhone, itemAddress);

            Query studentsQuery = studentsRef.orderByChild("index").equalTo(index);

            studentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot studentSnapshot: dataSnapshot.getChildren()) {
                        studentSnapshot.getRef().setValue(updatedStudent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Updating data failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
