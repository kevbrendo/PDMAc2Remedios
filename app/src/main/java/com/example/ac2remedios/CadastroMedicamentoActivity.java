package com.example.ac2remedios;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CadastroMedicamentoActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextDescricao;
    private EditText editTextHorario;
    private Button btnSalvar;
    private MedicamentoDAO medicamentoDAO;
    private Medicamento medicamentoEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento);

        editTextNome = findViewById(R.id.editTextNome);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextHorario = findViewById(R.id.editTextHorario);
        btnSalvar = findViewById(R.id.btnSalvarMedicamento);

        medicamentoDAO = new MedicamentoDAO(this);
        medicamentoEdicao = null;

        int medicamentoId = getIntent().getIntExtra("medicamento_id", -1);
        if (medicamentoId != -1) {
            medicamentoEdicao = medicamentoDAO.buscarMedicamentoPorId(medicamentoId);
            if (medicamentoEdicao != null) {
                editTextNome.setText(medicamentoEdicao.getNome());
                editTextDescricao.setText(medicamentoEdicao.getDescricao());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                editTextHorario.setText(sdf.format(medicamentoEdicao.getHorario()));
            }
        }

        editTextHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int horaAtual = calendar.get(Calendar.HOUR_OF_DAY);
                int minutoAtual = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CadastroMedicamentoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                editTextHorario.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                            }
                        }, horaAtual, minutoAtual, true);
                timePickerDialog.show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                String descricao = editTextDescricao.getText().toString();
                String horarioStr = editTextHorario.getText().toString();

                if (nome.isEmpty() || horarioStr.isEmpty()) {
                    Toast.makeText(CadastroMedicamentoActivity.this, "Nome e Horário são obrigatórios", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                try {
                    Date horario = sdf.parse(horarioStr);
                    if (medicamentoEdicao == null) {
                        Medicamento novoMedicamento = new Medicamento(nome, descricao, horario, false);
                        medicamentoDAO.inserirMedicamento(novoMedicamento);
                        Toast.makeText(CadastroMedicamentoActivity.this, "Medicamento cadastrado", Toast.LENGTH_SHORT).show();
                    } else {
                        medicamentoEdicao.setNome(nome);
                        medicamentoEdicao.setDescricao(descricao);
                        medicamentoEdicao.setHorario(horario);
                        medicamentoDAO.atualizarMedicamento(medicamentoEdicao);
                        Toast.makeText(CadastroMedicamentoActivity.this, "Medicamento atualizado", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } catch (ParseException e) {
                    Toast.makeText(CadastroMedicamentoActivity.this, "Formato de horário inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
