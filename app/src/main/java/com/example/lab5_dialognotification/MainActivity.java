package com.example.lab5_dialognotification;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edtId, edtName, edtAddress, edtPhone;
    private Button btnAdd, btnRead, btnEdit, btnDelete;

    // Khai báo biến Database
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Database
        dbHelper = new DatabaseHelper(this);

        // Ánh xạ
        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);

        btnAdd = findViewById(R.id.btnAdd);
        btnRead = findViewById(R.id.btnRead);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // --- XỬ LÝ NÚT THÊM ---
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = Integer.parseInt(edtId.getText().toString());
                    String name = edtName.getText().toString();
                    String address = edtAddress.getText().toString();
                    String phone = edtPhone.getText().toString();

                    boolean isInserted = dbHelper.addStudent(id, name, address, phone);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                        clearFields(); // Xóa trắng các ô nhập liệu sau khi thêm
                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi: ID này đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập Mã ID hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // --- XỬ LÝ NÚT ĐỌC (HIỂN THỊ RA LOGCAT) ---
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getAllStudents();
                if (cursor.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Database trống, chưa có sinh viên nào!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // In ra Logcat
                Log.d("DATA_SINH_VIEN", "========= DANH SÁCH SINH VIÊN =========");
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0); // Cột ID
                    String name = cursor.getString(1); // Cột Tên
                    String address = cursor.getString(2); // Cột Địa chỉ
                    String phone = cursor.getString(3); // Cột SĐT

                    Log.d("DATA_SINH_VIEN", "ID: " + id + " | Tên: " + name + " | ĐC: " + address + " | SĐT: " + phone);
                }
                Log.d("DATA_SINH_VIEN", "=======================================");

                Toast.makeText(MainActivity.this, "Đã in danh sách ra Logcat!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- XỬ LÝ NÚT SỬA ---
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = Integer.parseInt(edtId.getText().toString()); // Dùng ID làm khóa để sửa
                    String name = edtName.getText().toString();
                    String address = edtAddress.getText().toString();
                    String phone = edtPhone.getText().toString();

                    boolean isUpdated = dbHelper.updateStudent(id, name, address, phone);
                    if (isUpdated) {
                        Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(MainActivity.this, "Không tìm thấy Mã ID này để sửa!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập Mã ID cần sửa!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // --- XỬ LÝ NÚT XÓA ---
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = Integer.parseInt(edtId.getText().toString()); // Chỉ cần ID để xóa

                    boolean isDeleted = dbHelper.deleteStudent(id);
                    if (isDeleted) {
                        Toast.makeText(MainActivity.this, "Đã xóa sinh viên!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(MainActivity.this, "Không tìm thấy Mã ID này để xóa!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập Mã ID cần xóa!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm phụ để xóa trắng các ô nhập liệu
    private void clearFields() {
        edtId.setText("");
        edtName.setText("");
        edtAddress.setText("");
        edtPhone.setText("");
        edtId.requestFocus(); // Đưa con trỏ chuột về lại ô ID
    }
}