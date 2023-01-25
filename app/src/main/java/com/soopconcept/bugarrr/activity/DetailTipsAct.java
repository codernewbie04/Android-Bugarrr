package com.soopconcept.bugarrr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.PicassoTrustAll;
import com.squareup.picasso.Picasso;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DetailTipsAct extends AppCompatActivity {
    String title, content, created_at, image, subtitle;
    TextView tv_title, tv_tanggal, tv_content;
    ImageView ivImage, back_btn;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tips);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        tv_title = findViewById(R.id.title);
        tv_tanggal = findViewById(R.id.tanggal);
        tv_content = findViewById(R.id.content);
        ivImage = findViewById(R.id.image);
        back_btn = findViewById(R.id.back_btn);
        mContext = this;
    }

    @SuppressLint("WrongConstant")
    private void initValue() {
        title   = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content").replace("\\n", System.getProperty("line.separator"));
        image   = getIntent().getStringExtra("image");
        created_at  = getIntent().getStringExtra("created_at");
        subtitle  = getIntent().getStringExtra("subtitle");

        tv_title.setText(title);
        tv_tanggal.setText(created_at +", Admin Bugarrr");
        tv_content.setText(content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_content.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
        Picasso.with(mContext)
                .load(image)
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .centerCrop()
                .into(ivImage);
    }

    private void initEvent() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}