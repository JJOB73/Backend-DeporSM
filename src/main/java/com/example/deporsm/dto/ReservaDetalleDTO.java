package com.example.deporsm.dto;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ReservaDetalleDTO {

    private Integer id;
    private Integer usuarioId;
    private String usuarioNombre;
    private Integer instalacionId;
    private String instalacionNombre;
    private String instalacionUbicacion;
    private String instalacionImagenUrl;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private String estado;
    private String estadoPago;
    private String metodoPago;
    private String comentarios;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructor
    public ReservaDetalleDTO(Integer id, Integer usuarioId, String usuarioNombre, 
                          Integer instalacionId, String instalacionNombre, 
                          String instalacionUbicacion, String instalacionImagenUrl,
                          Date fecha, Time horaInicio, Time horaFin, 
                          String estado, String estadoPago, String metodoPago,
                          String comentarios, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.instalacionId = instalacionId;
        this.instalacionNombre = instalacionNombre;
        this.instalacionUbicacion = instalacionUbicacion;
        this.instalacionImagenUrl = instalacionImagenUrl;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
        this.comentarios = comentarios;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
    
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    public Integer getInstalacionId() {
        return instalacionId;
    }
    
    public void setInstalacionId(Integer instalacionId) {
        this.instalacionId = instalacionId;
    }
    
    public String getInstalacionNombre() {
        return instalacionNombre;
    }
    
    public void setInstalacionNombre(String instalacionNombre) {
        this.instalacionNombre = instalacionNombre;
    }
    
    public String getInstalacionUbicacion() {
        return instalacionUbicacion;
    }
    
    public void setInstalacionUbicacion(String instalacionUbicacion) {
        this.instalacionUbicacion = instalacionUbicacion;
    }
    
    public String getInstalacionImagenUrl() {
        return instalacionImagenUrl;
    }
    
    public void setInstalacionImagenUrl(String instalacionImagenUrl) {
        this.instalacionImagenUrl = instalacionImagenUrl;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public Time getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public Time getHoraFin() {
        return horaFin;
    }
    
    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getEstadoPago() {
        return estadoPago;
    }
    
    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public String getComentarios() {
        return comentarios;
    }
    
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
