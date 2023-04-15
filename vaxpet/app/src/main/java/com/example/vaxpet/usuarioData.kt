package com.example.desafiopractico2

public class usuarioData {

/* val nombres = binding.nombres.text.toString();
            val apellidos = binding.apellidos.text.toString();
            val telefono = binding.telefono.text.toString()
            val correo = binding.correo.text.toString()
            val contraseña01 = binding.contrasena01.text.toString()
            val contraseña02 = binding.contrasena02.text.toString()*/
    private var nombres: String? = null
    private var apellidos: String? = null
    private var telefono: String? = null
    private var correo: String? = null
    private var contraseña: String? = null

    private var id: String? = null


    // an empty constructor is
    // required when using
    // Firebase Realtime Database.
    fun usuarioData() {}

    // created getter and setter methods
    // for all our variables.
    fun getnombres(): String? {
        return nombres
    }

    fun setnombres(nombres: String?) {
        this.nombres = nombres
    }

    fun getmapellidos(): String? {
        return apellidos
    }

    fun setapellidos(apellidos: String?) {
        this.apellidos = apellidos
    }

    fun gettelefono(): String? {
        return telefono
    }

    fun settelefono(telefono: String?) {
        this.telefono = telefono
    }

    fun getmcorreo(): String? {
        return correo
    }

    fun setcorreo(correo: String?) {
        this.correo = correo
    }

    fun getcontraseña(): String? {
        return contraseña
    }

    fun setcontraseña(contraseña: String?) {
        this.contraseña = contraseña
    }

    fun getid(): String? {
        return id
    }

    fun setid(id: String?) {
        this.id = id
    }
}