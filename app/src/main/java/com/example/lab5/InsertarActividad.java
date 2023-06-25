package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lab5.model.Actividad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsertarActividad extends AppCompatActivity {

    EditText titulo,descripcion, fechai,inicio,fin,fechaf;

    Button addImagge, addActivity;

    Uri imageUri;

    ImageView imageView;

    ProgressDialog progressDialog;

    String filename;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_actividad);


        titulo=findViewById(R.id.ti_titulo);
        descripcion=findViewById(R.id.ti_desc);
        fechai =findViewById(R.id.ti_fechai);
        fechaf =findViewById(R.id.ti_fechaf);
        inicio=findViewById(R.id.ti_inicio);
        fin=findViewById(R.id.ti_fin);

        imageView=findViewById(R.id.imageView7);
        auth= FirebaseAuth.getInstance();





        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(inicio);
            }
        });

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(fin);
            }
        });

        addImagge=findViewById(R.id.addimage);
        addImagge.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,1);

        });

        fechai.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);

                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    fechai.setText(current);
                    fechai.setSelection(sel < current.length() ? sel : current.length());


                }

            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fechaf.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);

                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    fechaf.setText(current);
                    fechaf.setSelection(sel < current.length() ? sel : current.length());


                }

            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addActivity=findViewById(R.id.addActivity);

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFechas()) {
                    insertarActividad();
                }

                

            }
        });







    }

    private boolean validarFechas() {
        String fechaInicioText = fechai.getText().toString();
        String fechaFinText = fechaf.getText().toString();
        String inicioText = inicio.getText().toString();
        String finText = fin.getText().toString();

        String tituloText = titulo.getText().toString().trim();
        String descripcionText = descripcion.getText().toString().trim();

        if (!validarFormatoFecha(fechaInicioText)) {
            fechai.setError("Formato de fecha inválido");
            fechai.requestFocus();
            return false;
        }

        if (!validarFormatoFecha(fechaFinText)) {
            fechaf.setError("Formato de fecha inválido");
            fechaf.requestFocus();
            return false;
        }

        if (tituloText.isEmpty()) {
            titulo.setError("Campo requerido");
            titulo.requestFocus();
            return false;
        }

        if (descripcionText.isEmpty()) {
            descripcion.setError("Campo requerido");
            descripcion.requestFocus();
            return false;
        }

        if (fechaInicioText.isEmpty()) {
            fechai.setError("Campo requerido");
            fechai.requestFocus();
            return false;
        }

        if (fechaFinText.isEmpty()) {
            fechaf.setError("Campo requerido");
            fechaf.requestFocus();
            return false;
        }

        if (inicioText.isEmpty()) {
            inicio.setError("Campo requerido");
            inicio.requestFocus();
            return false;
        }

        if (finText.isEmpty()) {
            fin.setError("Campo requerido");
            fin.requestFocus();
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date fechaInicio = dateFormat.parse(fechaInicioText + " " + inicioText);
            Date fechaFin = dateFormat.parse(fechaFinText + " " + finText);

            if (fechaInicio != null && fechaFin != null && fechaFin.before(fechaInicio)) {
                // La fecha de finalización es anterior a la fecha de inicio
                fechaf.requestFocus();
                fechaf.setError("La fecha de finalización no puede ser anterior a la fecha de inicio");
                fin.requestFocus();
                fin.setError("La fecha de finalización no puede ser anterior a la fecha de inicio");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Manejar el error en caso de que el formato de fecha/hora sea incorrecto
        }

        return true;
    }

    private boolean validarFormatoFecha(String fecha) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void insertarActividad() {
        if (!validarImagen()) {
            return;
        }


        final ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        SimpleDateFormat formatter=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now= new Date();
        filename=formatter.format(now);
        FirebaseStorage.getInstance().getReference("images/"+filename).putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){

                                        String tituloText = titulo.getText().toString();
                                        String descripcionText = descripcion.getText().toString();
                                        String fechaTexti = fechai.getText().toString();
                                        String fechaTextf = fechaf.getText().toString();
                                        String inicioText = inicio.getText().toString();
                                        String finText = fin.getText().toString();
                                        String id_user=auth.getCurrentUser().getUid();
                                        Actividad actividad=new Actividad();
                                        actividad.setTitulo(tituloText);
                                        actividad.setDescripcion(descripcionText);
                                        actividad.setFechai(fechaTexti);
                                        actividad.setFechaf(fechaTextf);
                                        actividad.setInicio(inicioText);
                                        actividad.setFin(finText);
                                        actividad.setImagenUrl(task.getResult().toString());
                                        actividad.setUser(id_user);

                                        //



                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                        Date fechaInicio = null;

                                        try {
                                            fechaInicio = dateFormat.parse(fechaTexti + " " + inicioText);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            // Manejar el error en caso de que el formato de fecha/hora sea incorrecto
                                        }

                                        if (fechaInicio != null) {
                                            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                                            Date fechaFinal = null;
                                            try {
                                                fechaInicio = dateFormat.parse(fechaTexti + " " + inicioText);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                // Manejar el error en caso de que el formato de fecha/hora sea incorrecto
                                            }

                                            if (fechaInicio != null) {

                                            }
                                        }



                                        //
                                        db= FirebaseFirestore.getInstance();
                                        db.collection("actividades")
                                                .add(actividad)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        Toast.makeText(InsertarActividad.this, "Actividad insertada correctamente", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        Log.e("msg","Actividad insertadda correctamente");
                                                        Intent intent= new Intent(InsertarActividad.this,ListCreateActivity.class);
                                                        startActivity(intent);



                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        e.printStackTrace();

                                                    }
                                                });



                                    }

                                }
                            });


                        }

                    }
                });
    }

    private boolean validarImagen() {
        if (imageUri == null) {
            Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showTimePickerDialog(EditText editText) {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(InsertarActividad.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Format the selected time as desired (e.g., HH:mm)
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        editText.setText(formattedTime);
                    }
                }, hour, minute, false);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            getImageinImageView(imageUri, imageView);
        }
    }
    private void getImageinImageView(Uri uri, ImageView imageView) {

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageView.setImageBitmap(bitmap);
    }

}