package com.metinhocam.guessbirthdate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.metinhocam.guessbirthdate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int birthDay = 0;
    private int factor = 1;
    private int[] cards;
    private String[] questions;
    private SwitchCompat switchCompat;
    SharedPreferences sharedPreferences;

    private int card_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        switchCompat = binding.switchMode;

        sharedPreferences = getSharedPreferences("theme_mode", MODE_PRIVATE);
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn",false);
        if(isDarkModeOn){
            implementDarkMode();
            switchCompat.setChecked(true);
        }else{
            implementLightMode();
            switchCompat.setChecked(false);
        }


        cards = new int[]{R.drawable.cardone,
                R.drawable.cardtwo,
                R.drawable.cardthree,
                R.drawable.cardfour,
                R.drawable.cardfive};
        questions = new String[]{getResources().getString(R.string.question_one),
                getResources().getString(R.string.question_two),
                getResources().getString(R.string.question_three),
                getResources().getString(R.string.question_four),
                getResources().getString(R.string.question_five)};

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    implementDarkMode();
                    sharedPreferences.edit().putBoolean("isDarkModeOn", true).apply();
                } else {
                    implementLightMode();
                    sharedPreferences.edit().putBoolean("isDarkModeOn", false).apply();
                }
            }
        });
        binding.buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (card_position == 4) {
                    binding.buttonNo.setEnabled(false);
                    binding.buttonYes.setEnabled(false);
                    showResult(birthDay);
                } else {
                    card_position++;
                    factor *= 2;
                    System.out.println(birthDay);
                    binding.imageviewDayCard.setImageResource(cards[card_position]);
                    binding.textviewQuestion.setText(questions[card_position]);
                }


            }
        });
        binding.buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card_position == 4) {
                    binding.buttonNo.setEnabled(false);
                    binding.buttonYes.setEnabled(false);
                    birthDay += factor;
                    showResult(birthDay);
                } else {
                    card_position++;
                    birthDay += factor;
                    factor *= 2;
                    binding.imageviewDayCard.setImageResource(cards[card_position]);
                    binding.textviewQuestion.setText(questions[card_position]);
                    System.out.println(birthDay);
                }
            }
        });
        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonPlay.setVisibility(View.GONE);
                binding.imageviewDayCard.setVisibility(View.VISIBLE);
                binding.imageviewDayCard.setImageResource(cards[card_position]);
                binding.buttonNo.setVisibility(View.VISIBLE);
                binding.buttonYes.setVisibility(View.VISIBLE);
                binding.textviewQuestion.setText(questions[card_position]);
                switchCompat.setVisibility(View.GONE);
            }
        });

    }

    private void showResult(int birthday) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message;
        String title;
        int icon;
        if (birthday == 0) {
            title = getResources().getString(R.string.zero_title);
            message = getResources().getString(R.string.answer_zero_birthday);
            icon = R.drawable.confused;
        } else {
            title = getResources().getString(R.string.answer_title);
            message = getResources().getString(R.string.answer) + " " + birthday;
            icon = R.drawable.smiley;
        }
        builder.setTitle(title)
                .setIcon(icon)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.play_again), (dialogInterface, i) -> restartGame())
                .setNegativeButton(getResources().getString(R.string.exit_game), (dialogInterface, i) -> finish()).show();

    }

    public void restartGame() {
        binding.textviewQuestion.setText(getResources().getString(R.string.beginning_title));
        binding.imageviewDayCard.setVisibility(View.GONE);
        binding.buttonYes.setVisibility(View.GONE);
        binding.buttonNo.setVisibility(View.GONE);
        binding.buttonPlay.setVisibility(View.VISIBLE);
        binding.buttonNo.setEnabled(true);
        binding.buttonYes.setEnabled(true);
        switchCompat.setVisibility(View.VISIBLE);
        birthDay = 0;
        card_position = 0;
        factor = 1;

    }

    private void implementDarkMode() {
        AppCompatDelegate
                .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void implementLightMode() {
        AppCompatDelegate
                .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}