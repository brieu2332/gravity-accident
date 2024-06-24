package com.example.connectedheart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoricoActivity extends AppCompatActivity {

    private TextView textViewHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        textViewHistorico = findViewById(R.id.textViewHistorico);
        HistoricoCompleto();
    }

    private void HistoricoCompleto() {
        SharedPreferences sharedPreferences = getSharedPreferences("HistoricoSimulacoes", Context.MODE_PRIVATE);
        String historicoCompleto = sharedPreferences.getString("historico", "Sem histórico disponível.");
        textViewHistorico.setText(historicoCompleto);

        if (!historicoCompleto.equals("Sem histórico disponível.")) {
            String[] entradasHistorico = historicoCompleto.split("\n");
            StringBuilder historicoFormatado = new StringBuilder();
            for (String entrada : entradasHistorico) {
                historicoFormatado.append(entrada).append("\n--------------------\n");
            }
            textViewHistorico.setText(historicoFormatado.toString());
        } else {
            textViewHistorico.setText(historicoCompleto);
        }
    }
}
