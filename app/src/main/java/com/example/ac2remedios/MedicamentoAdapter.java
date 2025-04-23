package com.example.ac2remedios;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MedicamentoAdapter extends RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder> {

    private Context context;
    private List<Medicamento> listaMedicamentos;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;
    private OnMarcarTomadoClickListener mTomadoListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

    public interface OnMarcarTomadoClickListener {
        void onMarcarTomadoClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;
    }

    public void setOnMarcarTomadoClickListener(OnMarcarTomadoClickListener listener) {
        mTomadoListener = listener;
    }

    public MedicamentoAdapter(Context context, List<Medicamento> listaMedicamentos) {
        this.context = context;
        this.listaMedicamentos = listaMedicamentos;
    }

    @NonNull
    @Override
    public MedicamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicamento, parent, false);
        return new MedicamentoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicamentoViewHolder holder, int position) {
        Medicamento medicamento = listaMedicamentos.get(position);
        holder.textViewNome.setText(medicamento.getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.textViewHorario.setText(sdf.format(medicamento.getHorario()));

        if (medicamento.isTomado()) {
            holder.btnTomado.setText("Tomado");
            holder.btnTomado.setBackgroundColor(Color.GREEN);
        } else {
            holder.btnTomado.setText("Marcar Tomado");
            holder.btnTomado.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return listaMedicamentos.size();
    }

    public class MedicamentoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNome;
        public TextView textViewHorario;
        public Button btnTomado;

        public MedicamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNomeMedicamento);
            textViewHorario = itemView.findViewById(R.id.textViewHorarioMedicamento);
            btnTomado = itemView.findViewById(R.id.btnMarcarTomado);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            return mLongListener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });

            btnTomado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTomadoListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mTomadoListener.onMarcarTomadoClick(position);
                        }
                    }
                }
            });
        }
    }
}