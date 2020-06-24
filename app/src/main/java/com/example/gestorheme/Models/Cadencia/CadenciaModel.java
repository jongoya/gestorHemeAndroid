package com.example.gestorheme.Models.Cadencia;

import com.example.gestorheme.Common.Constants;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class CadenciaModel implements Serializable {
    private String cadencia = "";
    private long candenciaTime = 0;
    private double percentageExtra = 0.3;

    public CadenciaModel(String cadencia) {
        this.cadencia = cadencia;
        this.candenciaTime = getCandeciaTimeForCadencia(cadencia);
    }

    private long getCandeciaTimeForCadencia(String cadenciaString) {
        switch (cadencia) {
            case Constants.unaSemana:
                return cadenciaFor1Semana();
            case Constants.dosSemanas:
                return cadenciaFor2Semanas();
            case Constants.tresSemanas:
                return cadenciaFor3Semanas();
            case Constants.unMes:
                return cadenciaFor1Mes();
            case Constants.unMesUnaSemana:
                return cadenciaFor1Mes1Semana();
            case Constants.unMesDosSemanas:
                return cadenciaFor1Mes2Semanas();
            case Constants.unMesTresSemanas:
                return cadenciaFor1Mes3Semanas();
            case Constants.dosMeses:
                return cadenciaFor2Meses();
            case Constants.dosMesesYUnaSemana:
                return cadenciaFor2Meses1Semana();
            case Constants.dosMesesYDosSemanas:
                return cadenciaFor2Meses2Semanas();
            case Constants.dosMesesYTresSemanas:
                return cadenciaFor2Meses3Semanas();
            case Constants.tresMeses:
                return cadenciaFor3Meses();
            default:
                return cadenciaForMasDe3Meses();
        }
    }

    private long cadenciaFor1Semana() {
        return calculateCadenciaWithNumberOfDays(7);
    }

    private long cadenciaFor2Semanas() {
        return calculateCadenciaWithNumberOfDays(15);
    }

    private long cadenciaFor3Semanas() {
        return calculateCadenciaWithNumberOfDays(21);
    }

    private long cadenciaFor1Mes() {
        return calculateCadenciaWithNumberOfDays(30);
    }

    private long cadenciaFor1Mes1Semana() {
        return calculateCadenciaWithNumberOfDays(37);
    }

    private long cadenciaFor1Mes2Semanas() {
        return calculateCadenciaWithNumberOfDays(45);
    }

    private long cadenciaFor1Mes3Semanas() {
        return calculateCadenciaWithNumberOfDays(51);
    }

    private long cadenciaFor2Meses() {
        return calculateCadenciaWithNumberOfDays(60);
    }

    private long cadenciaFor2Meses1Semana() {
        return calculateCadenciaWithNumberOfDays(67);
    }

    private long cadenciaFor2Meses2Semanas() {
        return calculateCadenciaWithNumberOfDays(75);
    }

    private long cadenciaFor2Meses3Semanas() {
        return calculateCadenciaWithNumberOfDays(81);
    }

    private long cadenciaFor3Meses() {
        return calculateCadenciaWithNumberOfDays(90);
    }

    private long cadenciaForMasDe3Meses() {
        return calculateCadenciaWithNumberOfDays(97);
    }

    private long calculateCadenciaWithNumberOfDays(int numeroDeDias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -numeroDeDias);

        Calendar actualCalendar = Calendar.getInstance();
        actualCalendar.setTime(new Date());
        long diferencia = actualCalendar.getTimeInMillis() - cal.getTimeInMillis();
        return (cal.getTimeInMillis() - (long)(diferencia * percentageExtra)) / 1000;//respuesta en segundos
    }

    public String getCadencia() {
        return cadencia;
    }

    public void setCadencia(String cadencia) {
        this.cadencia = cadencia;
    }

    public long getCandenciaTime() {
        return candenciaTime;
    }

    public void setCandenciaTime(long candenciaTime) {
        this.candenciaTime = candenciaTime;
    }

    public double getPercentageExtra() {
        return percentageExtra;
    }

    public void setPercentageExtra(double percentageExtra) {
        this.percentageExtra = percentageExtra;
    }
}
