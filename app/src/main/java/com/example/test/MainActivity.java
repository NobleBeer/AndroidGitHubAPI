package com.example.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText orgEditText;
    private Button searchButton;
    private ListView repoListView;
    private TextView userTextView;
    private Button backButton;

    private GitHubService gitHubService;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        orgEditText = findViewById(R.id.orgEditText);
        searchButton = findViewById(R.id.searchButton);
        repoListView = findViewById(R.id.repoListView);
        userTextView = findViewById(R.id.userTextView);
        backButton = findViewById(R.id.backButton);

        GitHubClient gitHubClient = new GitHubClient();
        gitHubService = gitHubClient.getGitHubService();

        searchButton.setOnClickListener(v -> performSearch());

        orgEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });

        backButton.setOnClickListener(v -> {
            orgEditText.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            userTextView.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            repoListView.setAdapter(null);
        });
    }


    private void performSearch() {
        String orgName = orgEditText.getText().toString();
        if (!orgName.isEmpty()) {
            saveQuery(orgName);

            orgEditText.setVisibility(View.GONE);
            searchButton.setVisibility(View.GONE);
            userTextView.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);

            getRepositories(orgName);
        } else {
            Toast.makeText(MainActivity.this, "Введите название организации",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQuery(String query) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_QUERY, query);
        db.insert(DBHelper.TABLE_NAME, null, values);
    }

    private List<String> getHistory() {
        List<String> historyList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String query = cursor.getString(cursor
                        .getColumnIndexOrThrow(DBHelper.COLUMN_QUERY));
                historyList.add(query);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }

    private void getRepositories(String name) {
        Call<List<Repository>> call = gitHubService.getRepositories(name);
        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                if (response.isSuccessful()) {
                    List<Repository> repositories = response.body();
                    if (repositories != null && !repositories.isEmpty()) {
                        RepositoryAdapter adapter = new RepositoryAdapter(MainActivity.this, repositories);
                        repoListView.setAdapter(adapter);

                        orgEditText.setVisibility(View.GONE);
                        searchButton.setVisibility(View.GONE);
                        userTextView.setVisibility(View.VISIBLE);
                        userTextView.setText(name);
                        backButton.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this, "Репозитории не найдены", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}