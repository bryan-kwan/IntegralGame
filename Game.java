package com.bryan.kwan.integralquiz;

import java.util.List;

public class Game {

    private List<Question> questions;
    private int numberCorrect;
    private int numberIncorrect;
    private int score;
    private int totalQuestions;

    private int xp;




    private Question currentQuestion;


    public Game(){
        numberCorrect = 0;
        numberIncorrect = 0;
        score = 0;
        currentQuestion = new Question();
      // questions = new ArrayList<Question>();



    }

    public void makeNewQuestion(){
        currentQuestion = new Question();
        totalQuestions++;

       // questions.add(currentQuestion);
    }

    public boolean checkAnswer(double submittedAnswer){
        boolean isCorrect;
        if (currentQuestion.getAnswer() == submittedAnswer){
            numberCorrect++;


            isCorrect=true;
        }
        else{
            numberIncorrect++;
            isCorrect=false;

        }
        score = numberCorrect*10 - numberIncorrect*10;
        return isCorrect;
    }




















    public void setXp(int xp) {
        this.xp = xp;
    }
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getNumberCorrect() {
        return numberCorrect;
    }

    public void setNumberCorrect(int numberCorrect) {
        this.numberCorrect = numberCorrect;
    }

    public int getNumberIncorrect() {
        return numberIncorrect;
    }

    public void setNumberIncorrect(int numberIncorrect) {
        this.numberIncorrect = numberIncorrect;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }
}
