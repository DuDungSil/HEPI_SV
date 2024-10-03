package org.hepi.hepi_sv.service;

import java.util.HashMap;

import org.hepi.hepi_sv.vo.ExerciseProfile;

import jakarta.servlet.http.HttpServletRequest;

public class ExerciseService {

    HashMap<String, String> request;
    private HttpServletRequest httpRequest;

    public ExerciseService(HashMap<String, String> request, HttpServletRequest httpRequest) {
        this.request = request;
        this.httpRequest = httpRequest;
    }

    private double getRM1(int liftingWeight, int Reps){
        double W1 = liftingWeight * 0.025 * Reps;
        double RM1 = liftingWeight + W1;
        return RM1;
    }

    private ExerciseProfile bench_analysis(String sex, int bodyWeight, int liftingWeight, int Reps){
        ExerciseProfile bench = new ExerciseProfile();
        double RM1 = liftingWeight;
        if(Reps != 1) RM1 = getRM1(liftingWeight, Reps);

        double SP = RM1 / bodyWeight;

        if(sex.equals("남")){
            if( SP >= 0 && SP < 0.5){
                
            }
        }
        else{

        }

        return bench;
    }


    public String execute() {
        return "A";
    }

}