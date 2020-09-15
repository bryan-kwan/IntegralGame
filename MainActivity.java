package com.bryan.kwan.integralquiz;

//basic infrastructure from https://www.youtube.com/watch?v=ja1Jli7bHNM&feature=youtu.be&t=3298


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.judemanutd.katexview.KatexView;





public class MainActivity extends AppCompatActivity {

    Button btn_go, btn_answer0, btn_answer1, btn_answer2, btn_answer3;
    KatexView tv_question;
    TextView tv_xp;
    TextView tv_correct;
    TextView tv_score;
    ProgressBar prog_xp;

    Game g = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_go = findViewById(R.id.btn_go);
        btn_answer0 = findViewById(R.id.btn_answer0);
        btn_answer1 = findViewById(R.id.btn_answer1);
        btn_answer2 = findViewById(R.id.btn_answer2);
        btn_answer3 = findViewById(R.id.btn_answer3);

        tv_score = findViewById(R.id.tv_score);
        tv_correct = findViewById(R.id.tv_correct);
        tv_question = findViewById(R.id.tv_question);
        tv_xp = findViewById(R.id.tv_xp);

        prog_xp = findViewById(R.id.prog_xp);

        tv_score.setText("0");
        tv_question.setText("");
        tv_correct.setText("Press Go");
        tv_xp.setText("");

        View.OnClickListener goButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button go_button = (Button) v;

                go_button.setVisibility(View.INVISIBLE);
                nextTurn();

            }
        };

        btn_go.setOnClickListener(goButtonClickListener);

        View.OnClickListener answerButtonClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Button buttonClicked = (Button) v;

                double answerSelected = Double.parseDouble(buttonClicked.getText().toString());

                g.checkAnswer(answerSelected);
                tv_score.setText(Integer.toString(g.getScore()));
                nextTurn();

            }
        };

        btn_answer0.setOnClickListener(answerButtonClickListener);
        btn_answer1.setOnClickListener(answerButtonClickListener);
        btn_answer2.setOnClickListener(answerButtonClickListener);
        btn_answer3.setOnClickListener(answerButtonClickListener);


    }

    private void nextTurn(){
        //create new question
        g.makeNewQuestion();
        //set text on answer buttons
        double [] answer = g.getCurrentQuestion().getAnswerArray();
        btn_answer0.setText(Double.toString(answer[0]));
        btn_answer1.setText(Double.toString(answer[1]));
        btn_answer2.setText(Double.toString(answer[2]));
        btn_answer3.setText(Double.toString(answer[3]));

        //enable answer buttons

        btn_answer0.setEnabled(true);
        btn_answer1.setEnabled(true);
        btn_answer2.setEnabled(true);
        btn_answer3.setEnabled(true);
          //set question text


      // tv_question.setText(g.getCurrentQuestion().getIntegralString());

        //$$"\\begin{aligned}\\end{aligned}$$"

            String tex =  "$$"  + "\\int_{" + g.getCurrentQuestion().getLowerlimit() +
                    "}^{" + g.getCurrentQuestion().getUpperlimit() + "} "
                    + g.getCurrentQuestion().getIntegralString() + "  dx"  + "$$";
            tv_question.setText(tex);



        //set correct message
        tv_correct.setText(g.getNumberCorrect() + "/" + (g.getTotalQuestions() - 1));

        //xp bar


        prog_xp.setProgress((int) (g.getNumberCorrect()));




    }
}
