package com.ethernet389.logging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public final String FILENAME = "section_name";

    public final String WARNING_RED = "#FF3333";
    public final String SAFETY_GREEN = "#81CA4D";

    EditText inputFIO;
    Button addButton;
    TextView listOfMembers;
    Button getButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputFIO = findViewById(R.id.input_FIO);
        addButton = findViewById(R.id.add_button);
        listOfMembers = findViewById(R.id.list_of_members);
        getButton = findViewById(R.id.get_button);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            openFileOutput(FILENAME, MODE_PRIVATE)
                    )
            );
            bw.write("");
            listOfMembers.setText("");
            bw.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Файл для записи не найден!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка записи данных!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void addNewMember(View view) {
        class FormatFIOException extends Exception{
            private int number;
            public FormatFIOException(String message, int number){
                super(message);
                this.number = number;
            }
            public int getNumber() {return number;}
        }

        class FIO{
            private String f;
            private String i;
            private String o;

            public FIO(String[] _FIO) throws FormatFIOException{
                if (_FIO.length != 3){
                    throw new FormatFIOException(
                            "Кол-во слов в ФИО не равно трём!", 1
                    );
                }
                f = _FIO[0];
                i = _FIO[1];
                o = _FIO[2];
            }

            public String getFIO(){
                return f + " " + i + " " + o + "\n";
            }
        }

        String color_status = SAFETY_GREEN;
        try{
            FIO new_member = new FIO(
                    inputFIO.getText().toString().split(" ")
            );

            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            openFileOutput(FILENAME, MODE_APPEND)
                    )
            );

            bw.write(new_member.getFIO());
            bw.close();
        } catch (FormatFIOException e){
            color_status = WARNING_RED;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            color_status = WARNING_RED;
            Toast.makeText(this, "Файл для записи не найден!", Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException e) {
            color_status = WARNING_RED;
            Toast.makeText(this, "Ошибка записи данных!", Toast.LENGTH_SHORT).show();
        }

        addButton.setBackgroundColor(Color.parseColor(color_status));
    }

    public void getMembers(View view) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            openFileInput(FILENAME)
                    )
            );

            listOfMembers.setText("");
            for(String line; (line = br.readLine()) != null;) listOfMembers.append(line + "\n");

            br.close();
        } catch (FileNotFoundException e) {
            getButton.setBackgroundColor(Color.parseColor(WARNING_RED));
            Toast.makeText(this, "Файл для чтения не найден!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            getButton.setBackgroundColor(Color.parseColor(WARNING_RED));
            Toast.makeText(this, "Ошибка чтения данных!", Toast.LENGTH_SHORT).show();
        }
    }
}