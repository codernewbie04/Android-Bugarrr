package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.AktivitasItem;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.LatihanModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;
import com.soopconcept.bugarrr.utils.DateConvert;
import com.soopconcept.bugarrr.utils.Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailOlahragaAct extends AppCompatActivity {
    private TextView txt_olahraga, txt_deskripsi, aktivitas_kosong, txt_tambah;
    private EditText et_nama_olahraga, et_start_time, et_end_time, et_kalori;
    private BottomSheetBehavior bottomSheetBehavior;
    private ProgressBar prog_olahraga;
    private RelativeLayout rlprogress;
    private RecyclerView workouts;
    private FirebaseFirestore db;
    private Timestamp st_time, ed_time;
    private ImageView back_btn;
    private FirebaseAuth mAuth;
    private Button btn_tambah;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_olahraga);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mContext = this;
        txt_tambah = findViewById(R.id.txt_tambah);
        back_btn = findViewById(R.id.back_btn);
        prog_olahraga = findViewById(R.id.prog_olahraga);
        txt_olahraga = findViewById(R.id.txt_olahraga);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        aktivitas_kosong = findViewById(R.id.aktivitas_kosong);

        et_nama_olahraga = findViewById(R.id.et_nama_olahraga);
        et_start_time = findViewById(R.id.et_start_time);
        et_end_time = findViewById(R.id.et_end_time);
        et_kalori = findViewById(R.id.et_kalori);
        btn_tambah = findViewById(R.id.btn_tambah);

        rlprogress = findViewById(R.id.rlprogress);

        workouts = findViewById(R.id.workouts);
        workouts.setNestedScrollingEnabled(false);
        workouts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet_behavior_id);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int i) {
                // do something when state changes
            }

            @Override
            public void onSlide(View view, float v) {
                // do something when slide happens
            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setupAktivitas();
    }

    private void initEvent() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate_form())
                    tambah();
            }
        });

        et_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_start_time.setText(selectedHour + ":" + selectedMinute);

                        String start = DateConvert.getDateFormatFirebase()+ " " +selectedHour + ":" + selectedMinute+ ":00";
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            java.util.Date date = dateFormatter.parse(start);
                            st_time = new Timestamp(date);
                            Toast.makeText(mContext, String.valueOf(st_time.getSeconds()), Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            st_time = new Timestamp(new java.util.Date());
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        et_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_end_time.setText(selectedHour + ":" + selectedMinute);

                        String end = DateConvert.getDateFormatFirebase()+ " " +selectedHour + ":" + selectedMinute+ ":00";
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        try {
                            java.util.Date date = dateFormatter.parse(end);
                            ed_time = new Timestamp(date);
                        } catch (ParseException e) {
                            ed_time = new Timestamp(new java.util.Date());
                            e.printStackTrace();
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private boolean validate_form() {
        return (!Validate.cek(et_nama_olahraga) && !Validate.cek(et_start_time) && !Validate.cek(et_end_time) && !Validate.cek(et_kalori));
    }

    private void tambah() {
        show_loading();
        Map<String, Object> data = new HashMap<>();
        data.put("calories", FieldValue.increment(Integer.parseInt(et_kalori.getText().toString())));
        data.put("exercise", FieldValue.increment(DateConvert.getSelisihMenit(st_time, ed_time)));

        db.collection("users").document(mAuth.getUid()).collection("activities").document(DateConvert.getDateFormatFirebase())
                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                tambahWorkout();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FancyToast.makeText(mContext,"Data gagal tersimpan kedalam database!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        tambah();
                        hide_loading();
                    }
                });
    }

    private void tambahWorkout() {
        show_loading();
        LatihanModel data = new LatihanModel();
        data.setCalories_burned(Integer.parseInt(et_kalori.getText().toString()));
        data.setEnd_time(ed_time);
        data.setStart_time(st_time);
        data.setWorkout_name(et_nama_olahraga.getText().toString());
        Map<String, Object> data_value = new HashMap<>();
        data_value.put("data", FieldValue.arrayUnion(data));
        db.collection("users").document(mAuth.getUid()).collection("workouts").document(DateConvert.getDateFormatFirebase())
                .set(data_value, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FancyToast.makeText(mContext,"Berhasil menambah data!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                hide_loading();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                reFresh();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FancyToast.makeText(mContext,"Data gagal tersimpan kedalam database!!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        tambahWorkout();
                        hide_loading();
                    }
                });
    }

    private void reFresh() {
        listAktivitas();
        setupAktivitas();
    }

    private void show_loading()
    {
        rlprogress.setVisibility(View.VISIBLE);
    }

    private void hide_loading()
    {
        rlprogress.setVisibility(View.GONE);
    }

    private void listAktivitas() {
        DocumentReference query = db.collection("users").document(mAuth.getUid()).collection("workouts").document(DateConvert.getDateFormatFirebase());
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        WorkoutsModel latihan = document.toObject(WorkoutsModel.class);
                        if(latihan.getData().size() > 0){
                            workouts.setVisibility(View.VISIBLE);
                            aktivitas_kosong.setVisibility(View.GONE);
                            AktivitasItem adapter = new AktivitasItem(mContext, latihan);
                            workouts.setAdapter(adapter);
                        }
                    }
                }
            }
        });
    }

    private void setupAktivitas() {
        DocumentReference query = db.collection("users").document(mAuth.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        UserModel user = document.toObject(UserModel.class);
                        prog_olahraga.setMax(user.getActivity_target().getExercise());
                        txt_deskripsi.setText("Latihan sebanyak "+ user.getActivity_target().getExercise() +" Menit untuk menutup ring kamu. Olahraga akan otomatis terhitung pada saat kamu melakukan gerakan berjalan atau yang lebih intens.");
                        getAktivitas(user.getActivity_target().getExercise());
                    }
                    listAktivitas();
                }
            }
        });
    }

    private void getAktivitas(final int exercise) {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid())
                .collection("activities").document(DateConvert.getDateFormatFirebase()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                AktivitasModel data = document.toObject(AktivitasModel.class);
                                int total_olahraga = data.getExercise();
                                int total_kurang = exercise - total_olahraga;
                                if(total_kurang > 0){
                                    txt_deskripsi.setText("Latihan sebanyak "+ total_kurang +" Menit untuk menutup ring kamu. Olahraga akan otomatis terhitung pada saat kamu melakukan gerakan berjalan atau yang lebih intens.");
                                } else {
                                    txt_deskripsi.setText("Hebat! Kamu telah menutup ring olahraga.");
                                }
                                prog_olahraga.setProgress(total_olahraga);
                                txt_olahraga.setText(total_olahraga + " Menit");
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}