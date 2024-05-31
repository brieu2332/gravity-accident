package com.example.connectedheart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button startAnimationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        startAnimationButton = findViewById(R.id.start_animation_button);

        // Carregar a animação
        final Animation moveDownAnimation = AnimationUtils.loadAnimation(this, R.anim.move_down);

        // Configurar o botão para iniciar a animação quando clicado
        startAnimationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(moveDownAnimation);
            }

        });


    }
}
