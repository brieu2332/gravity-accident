package com.example.connectedheart;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ImageView pessoaImageView;
    private ImageView imageViewCapacete;
    private ImageView imageViewObjeto;
    private Objeto objeto;
    private Pessoa pessoa;
    private Button iniciarAnimacaoButton;
    private Button irParaSaidaButton;

    private double tempo;
    private double velocidade;
    private double forca;
    private boolean usarCapacete;
    private double massa;
    private double altura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentViews();
        getDataFromIntent();
        intentButtons();
    }

    private void intentViews() {
        iniciarAnimacaoButton = findViewById(R.id.start_animation_button);
        irParaSaidaButton = findViewById(R.id.start_telaSaida_button);
        imageViewObjeto = findViewById(R.id.imageViewObjeto);
        pessoaImageView = findViewById(R.id.pessoaImageView);
        imageViewCapacete = findViewById(R.id.imageViewCapacete);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        objeto = new Objeto(imageViewObjeto, screenHeight);
        pessoa = new Pessoa(pessoaImageView);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        tempo = intent.getDoubleExtra("tempo", 0.0);
        velocidade = intent.getDoubleExtra("velocidade", 0.0);
        forca = intent.getDoubleExtra("forca", 0.0);
        usarCapacete = intent.getBooleanExtra("usarCapacete", false);
        massa = intent.getDoubleExtra("massa", 0.0);
        altura = intent.getDoubleExtra("altura", 0.0);
        imageViewCapacete.setVisibility(usarCapacete ? View.VISIBLE : View.INVISIBLE);
    }

    private void intentButtons() {
        iniciarAnimacaoButton.setOnClickListener(v -> iniciarAnimacao(tempo, velocidade, forca));
        irParaSaidaButton.setOnClickListener(v -> iniciarSaidaActivity(velocidade, forca, tempo, usarCapacete, massa, altura));
    }

    private void iniciarAnimacao(double tempo, double velocidade, double forca) {
        objeto.setDuracao((long) (tempo * 1000));
        objeto.iniciarAnimacao(velocidade, forca);
    }

    class Objeto {
        private ImageView imageViewObjeto;
        private ValueAnimator animator;
        private int screenHeight;
        private long duracao;
        private boolean colisaoDetectada = false;
        private int framesParaParar = 0;
        private int framesAposColisao = 3; // Frames para parar a caixa em cima da view pessoa

        public Objeto(ImageView imageViewObjeto, int screenHeight) {
            this.imageViewObjeto = imageViewObjeto;
            this.screenHeight = screenHeight;
        }

        public void setDuracao(long duracao) {
            this.duracao = duracao;
        }

        public void iniciarAnimacao(double velocidade, double forca) {
            animator = ValueAnimator.ofFloat(0, screenHeight - imageViewObjeto.getHeight());
            animator.setDuration(duracao);
            animator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                imageViewObjeto.setTranslationY(value);
                if (MainActivity.this.CollisionDetected(imageViewObjeto, pessoaImageView)) {
                    colisaoDetectada = true;
                }

                if (colisaoDetectada) {
                    framesParaParar++;
                    if (framesParaParar >= framesAposColisao) {
                        pausarAnimacao();
                        colisaoDetectada = false;
                        framesParaParar = 3;
                    }
                }
            });

            animator.start();
        }

        public void pausarAnimacao() {
            if (animator != null) {
                animator.pause();
            }
        }
    }

    class Pessoa {
        private ImageView pessoaImageView;
        private int vidaPessoa = 100;

        public Pessoa(ImageView pessoaImageView) {
            this.pessoaImageView = pessoaImageView;
        }
        /*  public void diminuirVidaPessoa(double forca) {
            if (vidaPessoa > 0) {
                vidaPessoa -= forca;
                System.out.printf("A vida da pessoa agora é: %.0f\n", vidaPessoa);
            }
            if (forca >= 0) {
                if (forca >= 15) {
                    System.out.println("Um acidente seria fatal");
                } else if (forca >= 7) {
                    System.out.println("Um acidente seria de grau hospitalar");
                } else if (forca >= 4) {
                    System.out.println("Um acidente incidente maior, primeiros socorros necessario.");
                } else {
                    System.out.println("Um acidente de incidente pequeno");
                }
            }
        }*/
    }

    private boolean CollisionDetected(View view1, View view2) {
        Rect rect1 = new Rect();
        view1.getHitRect(rect1);
        Rect rect2 = new Rect();
        view2.getHitRect(rect2);
        return Rect.intersects(rect1, rect2);
    }

    private void iniciarSaidaActivity(double velocidade, double forca, double tempo, boolean usarCapacete, double massa, double altura) {
        Intent intent = new Intent(this, SaidaActivity.class);
        intent.putExtra("velocidade", velocidade);
        intent.putExtra("forca", forca);
        intent.putExtra("tempo", tempo);
        intent.putExtra("usarCapacete", usarCapacete);
        intent.putExtra("massa", massa);
        intent.putExtra("altura", altura);
        startActivity(intent);
    }
}
