package com.example.etdapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

class Etudiant {
    private int stdId;
    private String nom,prenom,tele,classe;
    private Bitmap imgae;
    private ArrayList<Notes> notes= new ArrayList<Notes>();
    public Bitmap getImgae() {
        return imgae;
    }

    public void setImgae(Bitmap imgae) {
        this.imgae = imgae;
    }

    public Etudiant(int stdId, String nom, String prenom, String tele, String classe) {
        this.stdId = stdId;
        this.nom = nom;
        this.prenom = prenom;
        this.tele = tele;
        this.classe = classe;
    }

    public ArrayList<Notes> getNotes() {
        return notes;
    }
    public void addNote(Notes N){
        this.notes.add(N);
    }
    public int getStdId() {
        return stdId;
    }

    public void setStdId(int stdId) {
        this.stdId = stdId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getClasse() {
        return classe;
    }

    @Override
    public String toString() {
        return "Etudiant{" +
                "stdId=" + stdId +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tele='" + tele + '\'' +
                ", classe='" + classe + '\'' +
                ", imgae=" + imgae +
                ", notes=" + notes +
                '}';
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
}
