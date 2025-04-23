package com.example.ac2remedios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicamentoDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MedicamentosDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MEDICAMENTOS = "medicamentos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_HORARIO = "horario";
    private static final String COLUMN_TOMADO = "tomado";

    public MedicamentoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_MEDICAMENTOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOME + " TEXT NOT NULL,"
                + COLUMN_DESCRICAO + " TEXT,"
                + COLUMN_HORARIO + " TEXT NOT NULL,"
                + COLUMN_TOMADO + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAMENTOS);
        onCreate(db);
    }

    public long inserirMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, medicamento.getNome());
        values.put(COLUMN_DESCRICAO, medicamento.getDescricao());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        values.put(COLUMN_HORARIO, sdf.format(medicamento.getHorario()));
        values.put(COLUMN_TOMADO, medicamento.isTomado() ? 1 : 0);
        long id = db.insert(TABLE_MEDICAMENTOS, null, values);
        db.close();
        return id;
    }

    public Medicamento buscarMedicamentoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDICAMENTOS,
                new String[]{COLUMN_ID, COLUMN_NOME, COLUMN_DESCRICAO, COLUMN_HORARIO, COLUMN_TOMADO},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Medicamento medicamento = cursorToMedicamento(cursor);
            cursor.close();
            db.close();
            return medicamento;
        }
        db.close();
        return null;
    }

    public List<Medicamento> listarMedicamentos() {
        List<Medicamento> listaMedicamentos = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MEDICAMENTOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Medicamento medicamento = cursorToMedicamento(cursor);
                listaMedicamentos.add(medicamento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listaMedicamentos;
    }

    public int atualizarMedicamento(Medicamento medicamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, medicamento.getNome());
        values.put(COLUMN_DESCRICAO, medicamento.getDescricao());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        values.put(COLUMN_HORARIO, sdf.format(medicamento.getHorario()));
        values.put(COLUMN_TOMADO, medicamento.isTomado() ? 1 : 0);
        int rowsAffected = db.update(TABLE_MEDICAMENTOS, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(medicamento.getId())});
        db.close();
        return rowsAffected;
    }

    public void excluirMedicamento(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICAMENTOS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    private Medicamento cursorToMedicamento(Cursor cursor) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Medicamento medicamento = new Medicamento(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)),
                null,
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOMADO)) == 1
        );
        medicamento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        try {
            medicamento.setHorario(sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORARIO))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return medicamento;
    }
}
