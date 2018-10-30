package playaudio.android.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mMainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainView = findViewById(R.id.mainView);

        findViewById(R.id.addBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainView.addView(new AudioView(MainActivity.this), 400, 200);
            }
        });
    }
}
