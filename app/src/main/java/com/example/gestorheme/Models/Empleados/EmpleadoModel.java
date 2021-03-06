package com.example.gestorheme.Models.Empleados;

import java.io.Serializable;

public class EmpleadoModel implements Serializable {
    private String nombre = "";
    private String apellidos = "";
    private long fecha = 0;
    private String telefono = "";
    private String email = "";
    private long empleadoId = 0;
    private float redColorValue = 0;
    private float greenColorValue = 0;
    private float blueColorValue = 0;
    private long comercioId = 0;
    private boolean is_empleado_jefe = false;
    private String unique_deviceId;

    public EmpleadoModel(String nombre, String apellidos, long fecha, String telefono, String email, long empleadoId, float redColorValue, float greenColorValue, float blueColorValue, long comercioId, boolean isEmpleadoJefe) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fecha = fecha;
        this.telefono = telefono;
        this.email = email;
        this.empleadoId = empleadoId;
        this.redColorValue = redColorValue;
        this.greenColorValue = greenColorValue;
        this.blueColorValue = blueColorValue;
        this.comercioId = comercioId;
        this.is_empleado_jefe = isEmpleadoJefe;
    }

    public EmpleadoModel() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public float getRedColorValue() {
        return redColorValue;
    }

    public void setRedColorValue(float redColorValue) {
        this.redColorValue = redColorValue;
    }

    public float getGreenColorValue() {
        return greenColorValue;
    }

    public void setGreenColorValue(float greenColorValue) {
        this.greenColorValue = greenColorValue;
    }

    public float getBlueColorValue() {
        return blueColorValue;
    }

    public void setBlueColorValue(float blueColorValue) {
        this.blueColorValue = blueColorValue;
    }

    public long getComercioId() {
        return comercioId;
    }

    public void setComercioId(long comercioId) {
        this.comercioId = comercioId;
    }

    public boolean isEmpleadoJefe() {
        return is_empleado_jefe;
    }

    public void setEmpleadoJefe(boolean empleadoJefe) {
        is_empleado_jefe = empleadoJefe;
    }

    public String getUnique_deviceId() {
        return unique_deviceId;
    }

    public void setUnique_deviceId(String unique_deviceId) {
        this.unique_deviceId = unique_deviceId;
    }
}
