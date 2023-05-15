package com.example.vaxpet.models

class CitasModel {
    private var id: String? = null
    private var idMascota: String? = null
    private var corte: Boolean? = null
    private var banio: Boolean? = null
    private var hora: String? = null
    private var fecha: String? = null
    private var idPropietario: String? = null

    constructor(){}

    constructor(id: String?, idMascota: String, corte: Boolean, banio: Boolean, hora: String, fecha: String, idPropietario: String?){
        this.id = id
        this.idMascota = idMascota
        this.corte = corte
        this.banio = banio
        this.hora = hora
        this.fecha = fecha
        this.idPropietario = idPropietario
    }

    fun setId(id: String){
        this.id = id
    }

    fun getId(): String? {
        return this.id
    }

    fun setIdMascota(value: String){
        this.idMascota = value
    }

    fun getIdMascota(): String? {
        return this.idMascota
    }

    fun setCorte(value: Boolean){
        this.corte = value
    }

    fun getCorte(): Boolean? {
        return this.corte
    }

    fun setBanio(value: Boolean){
        this.banio = value
    }

    fun getBanio(): Boolean? {
        return this.banio
    }

    fun setHora(value: String){
        this.hora = value
    }

    fun getHora(): String? {
        return this.hora
    }

    fun setFecha(value: String){
        this.fecha = value
    }

    fun getFecha(): String? {
        return this.fecha
    }

    fun setIdPropietario(value: String){
        this.idPropietario = value
    }

    fun getIdPropietario(): String? {
        return this.idPropietario
    }




}