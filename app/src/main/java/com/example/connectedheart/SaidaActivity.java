package com.example.connectedheart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaidaActivity extends AppCompatActivity {

    private TextView textViewSaidaTempo;
    private TextView textViewSaidaVelocidade;
    private TextView textViewSaidaForca;
    private ImageView imageViewPonto;
    private ImageView imageViewGrafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saida);

        textViewSaidaTempo = findViewById(R.id.textViewSaidaTempo);
        textViewSaidaVelocidade = findViewById(R.id.textViewSaidaVelocidade);
        textViewSaidaForca = findViewById(R.id.textViewSaidaForca);
        imageViewPonto = findViewById(R.id.imageViewPonto);
        imageViewGrafico = findViewById(R.id.imageViewGraficolimmite);

        Intent intent = getIntent();
        double tempo = intent.getDoubleExtra("tempo", 0.0);
        double velocidade = intent.getDoubleExtra("velocidade", 0.0);
        double forca = intent.getDoubleExtra("forca", 0.0);
        double altura = intent.getDoubleExtra("altura", 0.0);
        double massa = intent.getDoubleExtra("massa", 0.0);
        boolean usarCapacete = intent.getBooleanExtra("usarCapacete", false);

        String mTempo = String.format("Tempo da Queda do Objeto: %.2f s", tempo);
        String mVelocidade = String.format("Velocidade da Queda do Objeto: %.2f m/s", velocidade);
        String mForca = String.format("Força gerada pela Queda do Objeto: %.2f N", forca);

        textViewSaidaTempo.setText(mTempo);
        textViewSaidaVelocidade.setText(mVelocidade);
        textViewSaidaForca.setText(mForca);

        ViewTreeObserver vto = imageViewGrafico.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageViewGrafico.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                reposicionarPonto(altura, massa, usarCapacete);
            }
        });

        // Salvar no históricoo
        salvarNoHistorico(velocidade, forca, tempo, usarCapacete, massa, altura);
    }

    private void reposicionarPonto(double altura, double massa, boolean usarCapacete) {
        int graficoWidth = imageViewGrafico.getWidth();
        int graficoHeight = imageViewGrafico.getHeight();

        double massaMin = 1.0;
        double massaMax = 10.0;
        double alturaMin = 1.0;
        double alturaMax = 15.0;

        double xRatio = (massa - massaMin) / (massaMax - massaMin);
        double yRatio = (alturaMax - altura) / (alturaMax - alturaMin);

        int x = (int) (xRatio * graficoWidth);
        int y = (int) (yRatio * graficoHeight);

        // Ajuste do ponto se o capacete estiver sendo usado
        if (usarCapacete) {
            y += graficoHeight * 0.2;
            x += graficoWidth * 0.2; // Ajuste y para uma área menos severa
            y = Math.min(y, graficoHeight - imageViewPonto.getHeight()); // Garantir que não saia do limite inferior
            x = Math.min(x, graficoWidth - imageViewPonto.getWidth());
        }

        x = Math.max(0, Math.min(x, graficoWidth - imageViewPonto.getWidth()));
        y = Math.max(0, Math.min(y, graficoHeight - imageViewPonto.getHeight()));

        imageViewPonto.setX(imageViewGrafico.getX() + x);
        imageViewPonto.setY(imageViewGrafico.getY() + y);
    }

    private void salvarNoHistorico(double velocidade, double forca, double tempo, boolean usarCapacete, double massa, double altura) {
        SharedPreferences sharedPreferences = getSharedPreferences("HistoricoSimulacoes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Obter o histórico existente
        String historico = sharedPreferences.getString("historico", "");

        // Formatar a data e hora atual
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Adicionar a nova entrada ao histórico
        String novaEntrada = String.format(
                "Data: %s, Tempo: %.1f, Velocidade: %.1f, Força: %.1f, Capacete: %s, Massa: %.1f, Altura: %.1f\n",
                timeStamp, tempo, velocidade, forca, usarCapacete ? "Sim" : "Não", massa, altura
        );

        // Adicionar a nova entrada no início do histórico (para ter as mais recentes no topo)
        historico = novaEntrada + historico;

        editor.putString("historico", historico);
        editor.apply();
    }
}
