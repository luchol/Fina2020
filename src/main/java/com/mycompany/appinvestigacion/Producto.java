/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appinvestigacion;

/**
 *
 * @author luis
 */
public class Producto {
        private Integer codigo;
    private String descripcion;
    private Integer cantidad;
    private Integer iva;
    private Float precio;
    private Integer marca;
    
    public Producto(Integer codigo, String descripcion, Integer cantidad, Integer iva, Float precio, Integer marca){
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.iva = iva;
        this.precio = precio;       
        this.marca = marca;       
    }
    
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }
    public void setMarca(Integer marca) {
        this.marca = marca;
    }
    
      public Integer getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Integer getIva() {
        return iva;
    }

    public Float getPrecio() {
        return precio;
    }
    public Integer getMarca() {
        return marca;
    }
    
}
