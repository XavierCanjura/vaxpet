package com.example.vaxpet

class mascotaData {
    private var id: String? = null
    private var idPropietario: String? = null
    private var nombre: String? = null
    private var raza: String? = null
    private var sexo: String? = null
    private var fecha_nacimineto: String? = null
    private var descripcion: String? = null
    private var imagen: String? = null

    constructor(){}

    constructor(id: String?, idPropietario: String, nombre: String, raza: String, sexo: String, fechaNacimiento: String, descripcion: String, imagen: String)
    {
        this.id = id
        this.idPropietario = idPropietario
        this.nombre = nombre
        this.raza = raza
        this.sexo = sexo
        this.fecha_nacimineto = fechaNacimiento
        this.descripcion = descripcion
        this.imagen = imagen
    }


    // Getter and Setter Methods
    // For id
    fun setId(id: String){
        this.id = id
    }

    fun getId(): String? {
        return this.id
    }

    // For idPropietario
    fun setIdPropietario(idPropietario: String){
        this.idPropietario = idPropietario
    }

    fun getIdPropietario(): String? {
        return this.idPropietario
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

    //For sexo
    fun setSexo(sexo: String){
        this.sexo = sexo
    }

    fun getSexo(): String? {
        return this.sexo
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

    //For imagen
    fun setImagen(imagen: String){
        this.imagen = imagen
    }

    fun getImagen(): String? {
        return this.imagen
    }
}