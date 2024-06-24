package com.example.connectedheart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private EditText editTextNumberAltura;
    private EditText editTextNumberPeso;
    private Switch switchCapacete;
    private TextView textViewHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editTextNumberAltura = findViewById(R.id.editTextNumberAltura);
        editTextNumberPeso = findViewById(R.id.editTextNumberPeso);
        switchCapacete = findViewById(R.id.switchCapacete);
        textViewHistorico = findViewById(R.id.textViewHistorico);



        Button irParaHistoricoButton = findViewById(R.id.start_telaHistorico_button);
        irParaHistoricoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HistoricoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void iniciarSimulacao(View v) {
        try {
            double altura = Double.parseDouble(editTextNumberAltura.getText().toString());
            double massa = Double.parseDouble(editTextNumberPeso.getText().toString());

            CalculoGravitacional calculo = new CalculoGravitacional(altura, massa);
            calculo.calcularTudo();

            boolean usarCapacete = switchCapacete.isChecked();

            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("tempo", calculo.getTempo());
            intent.putExtra("velocidade", calculo.getVelocidade());
            intent.putExtra("forca", calculo.getForca());
            intent.putExtra("usarCapacete", usarCapacete);
            intent.putExtra("altura", altura);
            intent.putExtra("massa", massa);

            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite valores válidos!", Toast.LENGTH_SHORT).show();
        }
    }

    class CalculoGravitacional {
        private final double gravidadeTerra = 9.780327;
        private double altura;
        private double velocidade;
        private double tempo;
        private double forca;
        private double massa;

        public CalculoGravitacional(double altura, double massa) {
            this.altura = altura;
            this.massa = massa;
        }

        public void calcularVelocidade() {
            velocidade = Math.sqrt(2 * gravidadeTerra * altura);
        }

        public void calcularTempo() {
            tempo = Math.sqrt((2 * altura) / gravidadeTerra);
        }

        public void calcularForca() {
            forca = massa * gravidadeTerra;
        }

        public void calcularTudo() {
            calcularVelocidade();
            calcularTempo();
            calcularForca();
        }

        public double getVelocidade() {
            return velocidade;
        }

        public double getTempo() {
            return tempo;
        }

        public double getForca() {
            return forca;
        }
    }
}
