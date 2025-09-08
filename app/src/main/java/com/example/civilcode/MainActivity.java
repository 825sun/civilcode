package com.example.civilcode;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText numberInput;
    private Button searchButton;
    private TextView resultText;
    private JSONArray allArticlesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberInput = findViewById(R.id.numberInput);
        searchButton = findViewById(R.id.searchButton);
        resultText = findViewById(R.id.resultText);

        loadJsonFromAsset();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArticle();
            }
        });
    }

    private void loadJsonFromAsset() {
        try {
            InputStream is = getAssets().open("csvjson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            allArticlesArray = new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载法条数据失败！", Toast.LENGTH_LONG).show();
        }
    }

    private void searchArticle() {
        String inputNumberStr = numberInput.getText().toString().trim();

        if (inputNumberStr.isEmpty()) {
            Toast.makeText(this, "请输入法条编号", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int inputNumber = Integer.parseInt(inputNumberStr);
            String resultContent = "未找到对应法条。";

            for (int i = 0; i < allArticlesArray.length(); i++) {
                JSONObject articleObject = allArticlesArray.getJSONObject(i);
                int articleNumber = articleObject.getInt("法条编号");  // 修改为正确的键名

                if (articleNumber == inputNumber) {
                    resultContent = articleObject.getString("内容");  // 修改为正确的键名
                    break;
                }
            }

            resultText.setText(resultContent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "查询出错", Toast.LENGTH_SHORT).show();
        }
    }
}