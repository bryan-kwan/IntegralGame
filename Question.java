package com.bryan.kwan.integralquiz;



import java.util.Random;

//import org.apache.commons.math3.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;


import static org.apache.commons.math3.analysis.FunctionUtils.multiply;




//import com.wolfram.client.*;


public class Question {

    private UnivariateFunction integral;


    private double answer;
    private int[] limits = new int[2];


    private String type;

    private String integralString;
    private double[] coefficients;
    private Pair<UnivariateFunction, String> pair;




    private String lowerlimit;
    private String upperlimit;
    //four choices
    private double [] answerArray;

    //answer key
    private int answerPosition;

    //generate new question
    public Question() {

        Random randomNumberMaker = new Random();
        //choose question type
        this.type = chooseType();

        //maybe change this so that limits can be negative and multiples of pi
        this.limits[0] = randomNumberMaker.nextInt(10);
        //this makes it so that the upper limit is higher than the lower
        //there is a fatal error when upper limit <= lower limit
        this.limits[1] = this.limits[0] + Math.abs(randomNumberMaker.nextInt(5)) + 1;
        this.lowerlimit = Integer.toString(this.limits[0]);
        this.upperlimit = Integer.toString(this.limits[1]);


        //make coefficients for polynomials
        this.coefficients = makeCoefficients();



        //makes a question according to type

        if(this.type.equals("polynomial")){
        this.pair = makePolynomial(this.coefficients);
        }
        else if(this.type.equals("radical")){
            this.pair = makeRadical(this.coefficients);
        }
        else if(this.type.equals("partialFractions")){
            this.pair = makePartialFractions();
        }
        else if(this.type.equals("trigSub")){
            this.pair = makeTrigSub();
        }
        else if(this.type.equals("trigSubFraction")){
            this.pair = makeTrigSubFraction();
        }
        else if(this.type.equals("trigParts")){
            this.pair = makeTrigParts();
        }
        else if(this.type.equals("longDivision")){
            this.pair = makeLongDivision();
        }
        else if(this.type.equals("trigIdentity")){
            this.pair = makeTrigIdentity();
        }
        else if (this.integral==null){
            //if roll gives null, make a polynomial be default
           this.pair = makePolynomial(this.coefficients);
        }

        this.integral = this.pair.getLeft();
        this.integralString = this.pair.getRight();
        this.answer = evaluateIntegral(this.integral);


        this.answerPosition = randomNumberMaker.nextInt(4);
        this.answerArray = new double[]{0, 1, 2, 3};
        for (int i = 0; i < 4; i++) {
            double temp = randomNumberMaker.nextInt(20) + 1;
            this.answerArray[i] = answer + temp;
//            this.answerArray[i] = answer + randomNumberMaker.nextDouble();
        }
        this.answerArray = shuffleArray(this.answerArray);
        answerArray[answerPosition] = answer;
    }

    //shuffle function
        private double [] shuffleArray(double[] array){
            int index;
            double temp;
            Random randomNumberGenerator = new Random();
            for (int i = array.length - 1;i>0;i--){
                index = randomNumberGenerator.nextInt(i+1);
                temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
            return array;
        }

        //generate random polynomial to integrate
//    private String createPolynomial() {
//        Random randomNumberGenerator = new Random();
//        int degree = randomNumberGenerator.nextInt(5);
//        int coefficient;
//        String polynomial = "";
//        for (int i = degree + 1; i > 0; i--) {
//            coefficient = randomNumberGenerator.nextInt(10);
//            if (coefficient != 0) {
//                if (polynomial == "") {
//                    polynomial = coefficient + "x" + "^" + i;
//                } else if (degree != 0) {
//                    //maybe make it so that half the time it is - instead of +
//                    polynomial = polynomial + " + " + coefficient + "x" + "^" + i;
//                } else if (degree == 0) {
//                    polynomial = polynomial + coefficient;
//                }
//            }
//        }
//        return polynomial;
//    }

    private String chooseType(){
        Random randomNumberGenerator = new Random();
        //bound 1 gives only 0
        int diceRoll = randomNumberGenerator.nextInt(8);
        String type = "no_type";
        if(diceRoll==0){
             type = "polynomial";
        }
        if(diceRoll==1){
            type = "radical";
        }
        if(diceRoll==2){
            type = "partialFractions";
        }
        if(diceRoll==3){
            type = "trigSub";
        }
        if(diceRoll==4){
            type = "trigSubFraction";
        }
        if(diceRoll==5){
            type = "trigParts";
        }
        if(diceRoll==6){
            type = "trigIdentity";
        }
        if(diceRoll==7){
            type = "longDivision";
        }
        return type;
    }


    private double[] makeCoefficients(){
        Random randomNumberGenerator = new Random();
        // polynomials up to degree 5
        //degree is actually one less than degree variable
        int degree = randomNumberGenerator.nextInt(5) + 2;
        double [] coefficients = new double[degree+1];
        for(int i = 0;i<degree;i++){
            double number = randomNumberGenerator.nextInt(10);
            coefficients[i] = number;
        }
        return coefficients;
    }
    private Pair<UnivariateFunction, String> makePolynomial(double[] coeff){

        UnivariateFunction polynomial = new PolynomialFunction(coeff);
        String polynomialString = polynomial.toString();
        Pair<UnivariateFunction, String> newPair = Pair.of(polynomial, polynomialString);
        return newPair;
    }

    private Pair<UnivariateFunction, double[]> differentiatePolynomial(double[] tempCoeff){
        double [] newCoefficients = tempCoeff;
        for(int i =0;i<tempCoeff.length-1;i++){
            //differentiation of a polynomial
            newCoefficients[i]*= i;
        }
        double[] derivativeCoefficients = new double[tempCoeff.length-1];
        for(int i = 1;i<tempCoeff.length-1;i++){
            derivativeCoefficients[i-1] = newCoefficients[i];
        }
        if(derivativeCoefficients.length ==1 && derivativeCoefficients[0]==0){
            derivativeCoefficients[0] = 1;
        }
        UnivariateFunction derivativePolynomial = new PolynomialFunction(derivativeCoefficients);
        Pair<UnivariateFunction, double[]> newPair = Pair.of(derivativePolynomial, derivativeCoefficients);

        return newPair;

    }

    private Pair<UnivariateFunction, String> makeRadical(double[] coeff){

        //need to clone b/c coeff array gets changed after differentiation
            double[] originalCoeff = coeff.clone();

            UnivariateFunction polynomial = makePolynomial(coeff).getLeft();
            String polynomialString = polynomial.toString();
            Pair<UnivariateFunction, double[]> derivativePair = differentiatePolynomial(coeff);
            UnivariateFunction polynomial_derivative = derivativePair.getLeft();
            String polynomial_derivative_string = polynomial_derivative.toString();
            double[] tempDerivativeCoefficients = derivativePair.getRight();


            //puts each coefficient array into the max size
        //if max degree of polynomials is changed this has to change as well

        //length of array turns out to be one less than specified
            double[] radCoefficients = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            for(int i = 0;i<originalCoeff.length;i++){
                radCoefficients[i] = originalCoeff[i];
            }
        if(originalCoeff.length<8){
            int j = 8-originalCoeff.length;
            for(int k = 0;k<j;k++){

                radCoefficients[originalCoeff.length + k] = 0;
            }

        }


        double[] derivativeCoefficients = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
        for(int i = 0;i<tempDerivativeCoefficients.length;i++){
            derivativeCoefficients[i] = tempDerivativeCoefficients[i];
            if(tempDerivativeCoefficients.length<7){
                int j = 7-tempDerivativeCoefficients.length;
                for(int k = 0;k<j;k++){
                    derivativeCoefficients[tempDerivativeCoefficients.length + k] = 0;
                }
            }
        }

        //testing
//        for(int i = 0;i<coeff.length-1;i++){
//            Log.e("COEFFARRAY", String.valueOf(coeff[i]));
//
//        }
//        for(int i = 0;i<originalCoeff.length-1;i++){
//            Log.e("ORIGCOEFF", String.valueOf(originalCoeff[i]));
//
//        }
//        for(int i = 0;i<radCoefficients.length-1;i++){
//            String test = (String) String.valueOf(radCoefficients[i]);
//            Log.e("COEFFICIENTSARRAY", test);
//
//        }
//
//        for(int i = 0;i<derivativeCoefficients.length-1;i++){
//            Log.e("DERIVCOEFF", String.valueOf(derivativeCoefficients[i]));
//
//        }

        //manually typing in equation
           UnivariateFunction radical = v -> (derivativeCoefficients[0] +
                   derivativeCoefficients[1]*v + derivativeCoefficients[2]*Math.pow(v, 2)
                   + derivativeCoefficients[3]*Math.pow(v, 3)
                   + derivativeCoefficients[4]*Math.pow(v, 4) +
                   derivativeCoefficients[5]*Math.pow(v, 5)
                   + derivativeCoefficients[6]*Math.pow(v, 6))
                   * (Math.sqrt(radCoefficients[0]
                   + radCoefficients[1]*v + radCoefficients[2]*Math.pow(v, 2)
                   + radCoefficients[3]*Math.pow(v, 3) + radCoefficients[4]*Math.pow(v, 4)
                   + radCoefficients[5]*Math.pow(v, 5) + radCoefficients[6]*Math.pow(v, 6)
                    + radCoefficients[7]*Math.pow(v, 7)));

           //make string
           String radicalString =  "(" + polynomial_derivative_string + ")" + "\\sqrt{" + polynomialString + "}";


            Pair<UnivariateFunction, String> newPair = Pair.of(radical, radicalString);
            return newPair;
        }
        //makes a partial fractions problem in the form a/(x^2 + bx + c) where denominator can be factored
        private Pair<UnivariateFunction, String> makePartialFractions(){
            Random randomNumberGenerator = new Random();
            int a = randomNumberGenerator.nextInt(10) + 1;
            int b = randomNumberGenerator.nextInt(10) + 1;
            int top = randomNumberGenerator.nextInt(10) + 1;
            UnivariateFunction partialFractionsProblem = v-> top/(Math.pow(v, 2) + (a+b)*v + a*b);

            String partialFractionsString = "\\frac{" + top + "}{" + "x^2" + "+" + (a+b) + "x" + "+" + (a*b) + "}";
            Pair<UnivariateFunction, String> newPair = Pair.of(partialFractionsProblem, partialFractionsString);
            return newPair;
        }


        //makes a trig sub problem in the form \\root(ax^2 + b^2)
    private Pair<UnivariateFunction, String> makeTrigSub(){
        Random randomNumberGenerator = new Random();
        int a = randomNumberGenerator.nextInt(10) + 1;
        int b = randomNumberGenerator.nextInt(10) + 1;
        UnivariateFunction trigSubProblem = v-> Math.sqrt(a*a*Math.pow(v, 2) + (b*b));
        String trigSubString = "\\sqrt{" + (a*a) + "x^2" + "+" + (b*b) + "}";
        Pair<UnivariateFunction, String> newPair = Pair.of(trigSubProblem, trigSubString);
        return newPair;
    }

    //makes a trig sub problem in the form \\sqrt{a}{\\root{b^2x^2 + c^2}}
    private Pair<UnivariateFunction, String> makeTrigSubFraction(){
        Random randomNumberGenerator = new Random();
        int a = randomNumberGenerator.nextInt(10) + 1;
        int b = randomNumberGenerator.nextInt(10) + 1;
        int c = randomNumberGenerator.nextInt(10) + 1;
        UnivariateFunction trigSubFractionProblem = v-> c/(Math.sqrt(a*a*Math.pow(v, 2) + b*b));
        String trigSubFractionString = "\\frac{" + c + "}{\\sqrt{" + (a*a) + "x^2" + "+" + (b*b) + "}}";
        Pair<UnivariateFunction, String> newPair = Pair.of(trigSubFractionProblem, trigSubFractionString);
        return newPair;
    }

    //make a parts problem in the form a*x^n *(sin or cos)(bx)
    private Pair<UnivariateFunction, String> makeTrigParts(){
        Random randomNumberGenerator = new Random();
        int n = randomNumberGenerator.nextInt(10) + 1;
        int a = randomNumberGenerator.nextInt(10) + 1;
        int b = randomNumberGenerator.nextInt(10) + 1;
        UnivariateFunction trigPartsProblem = v-> a*Math.pow(v, n)*Math.sin(b*v);
        String trigPartsString = a + "x^{" + n + "}Sin(" + b + "x" + ")";
        Pair<UnivariateFunction, String> newPair = Pair.of(trigPartsProblem, trigPartsString);
        return newPair;
    }
    //make a long division problem in the form ax^3 + bx^2 + cx + d/(x+e)
    private Pair<UnivariateFunction, String> makeLongDivision(){
        Random randomNumberGenerator = new Random();
        int a = randomNumberGenerator.nextInt(10) + 1;
        int b = randomNumberGenerator.nextInt(10) + 1;
        int c = randomNumberGenerator.nextInt(10) + 1;
        int d = randomNumberGenerator.nextInt(10) + 1;
        int e = randomNumberGenerator.nextInt(10) + 1;
        UnivariateFunction Problem = v-> (a*Math.pow(v, 3) + b*Math.pow(v, 2) + c*v)/(v + e);
        String problemString = "\\frac{" + a + "x^3 + " + b + "x^2 + " + c + "x + " + d + "}{x + " + e + "}";
        Pair<UnivariateFunction, String> newPair = Pair.of(Problem, problemString);
        return newPair;
    }
//make trig identity problem in the form sin^n(x)cos^m(x)
private Pair<UnivariateFunction, String> makeTrigIdentity(){
    Random randomNumberGenerator = new Random();
    int n = randomNumberGenerator.nextInt(10) + 1;
    int m = randomNumberGenerator.nextInt(10) + 1;
    UnivariateFunction Problem = v-> Math.pow(Math.sin(v), n)*Math.pow(Math.cos(v), m);
    String problemString = "Sin^{" + n + "}(x)" + "Cos^{" + m + "}(x)";
    Pair<UnivariateFunction, String> newPair = Pair.of(Problem, problemString);
    return newPair;
}



    private double evaluateIntegral(UnivariateFunction problem){

        //calculate the answer to the integral (inputted as an univariatefunction)

        //need to add check for if integrating 0 and make answer C


        //can adjust accuracy here if necessary
//         double relativeAccuracy = 1.0e-12; //1.0e-12
//         double absoluteAccuracy = 1.0e-8; //1.0e-8
//          int minimalIterationCount = 1; //this is the problem for error --> too small
//          int maximalIterationCount = 50; //10
        UnivariateIntegrator integrator  = new SimpsonIntegrator(); //relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount

        return integrator.integrate(10000, problem, this.limits[0], this.limits[1]); //100 maxEval

    }





    public UnivariateFunction getIntegral() {
        return integral;
    }

    public void setIntegral(UnivariateFunction integral) {
        this.integral = integral;
    }

    public double getAnswer() {
        return answer;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }

    public double[] getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(double[] answerArray) {
        this.answerArray = answerArray;
    }

    public int getAnswerPosition() {
        return answerPosition;
    }

    public void setAnswerPosition(int answerPosition) {
        this.answerPosition = answerPosition;
    }
    public int[] getLimits() {
        return limits;
    }

    public void setLimits(int[] limits) {
        this.limits = limits;
    }

    public String getIntegralString() {
        return integralString;
    }

    public void setIntegralString(String integralString) {
        this.integralString = integralString;
    }

    public String getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(String lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public String getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(String upperlimit) {
        this.upperlimit = upperlimit;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }









}





