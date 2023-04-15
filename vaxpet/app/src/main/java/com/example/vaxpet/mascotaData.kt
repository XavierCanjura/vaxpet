package com.example.vaxpet

class mascotaData {
    private var id: String? = null
    private var nombre: String? = null
    private var raza: String? = null
    private var fecha_nacimineto: String? = null
    private var descripcion: String? = null

    constructor(){}


    // Getter and Setter Methods
    // For id
    fun setId(id: String){
        this.id = id
    }

    fun getId(): String? {
        return this.id
    }

    //For nombre
    fun setNombre(nombre: String){
        this.nombre = nombre
    }

    fun getNombre(): String? {
        return this.nombre
    }

    //For raza
    fun setRaza(raza: String){
        this.raza = raza
    }

    fun getRaza(): String? {
        return this.raza
    }

    //For fecha_nacimineto
    fun setFechaNacimiento(fechaNacimiento: String){
        this.fecha_nacimineto = fechaNacimiento
    }

    fun getFechaNacimiento(): String? {
        return this.fecha_nacimineto
    }

    //For descripcion
    fun setDescripcion(descripcion: String){
        this.descripcion = descripcion
    }

    fun getDescripcion(): String? {
        return this.descripcion
    }
}