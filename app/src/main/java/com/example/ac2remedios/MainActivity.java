package com.example.ac2remedios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MedicamentoAdapter medicamentoAdapter;
    private List<Medicamento> listaMedicamentos;
    private MedicamentoDAO medicamentoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMedicamentos);
        Button btnAdicionar = findViewById(R.id.btnAdicionarMedicamento);

        medicamentoDAO = new MedicamentoDAO(this);
        listaMedicamentos = medicamentoDAO.listarMedicamentos();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicamentoAdapter = new MedicamentoAdapter(this, listaMedicamentos);
        recyclerView.setAdapter(medicamentoAdapter);

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroMedicamentoActivity.class);
                startActivity(intent);
            }
        });

        medicamentoAdapter.setOnItemClickListener(new MedicamentoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Medicamento medicamento = listaMedicamentos.get(position);
                Intent intent = new Intent(MainActivity.this, CadastroMedicamentoActivity.class);
                intent.putExtra("medicamento_id", medicamento.getId());
                startActivity(intent);
            }
        });

        medicamentoAdapter.setOnItemLongClickListener(new MedicamentoAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(int position) {
                Medicamento medicamento = listaMedicamentos.get(position);
                excluirMedicamento(medicamento);
                return true;
            }
        });

        medicamentoAdapter.setOnMarcarTomadoClickListener(new MedicamentoAdapter.OnMarcarTomadoClickListener() {
            @Override
            public void onMarcarTomadoClick(int position) {
                Medicamento medicamento = listaMedicamentos.get(position);
                medicamento.setTomado(!medicamento.isTomado());
                medicamentoDAO.atualizarMedicamento(medicamento);
                medicamentoAdapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarListaMedicamentos();
    }

    private void atualizarListaMedicamentos() {
        listaMedicamentos.clear();
        listaMedicamentos.addAll(medicamentoDAO.listarMedicamentos());
        medicamentoAdapter.notifyDataSetChanged();
    }

    private void excluirMedicamento(Medicamento medicamento) {
        medicamentoDAO.excluirMedicamento(medicamento.getId());
        atualizarListaMedicamentos();
        Toast.makeText(this, "Medicamento excluído", Toast.LENGTH_SHORT).show();
    }
}