package com.example.andre.tfg_securitycams;

/**
 * Created by andre on 08/03/2017.
 */

public class ALTACAM {
    private String nom, ip, port, user, pass;

    public ALTACAM(){
    }

    public ALTACAM(String nom, String ip, String port, String user, String pass) {
        this.nom = nom;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
