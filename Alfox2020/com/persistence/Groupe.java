/*
 * Projet  : Alfox
 * Fichier : Groupe.java
 * Description : Classe interface de la table Groupe
 * Cette table stocke les infos sur les loueurs connus du logiciel
 */
package com.persistence;

import java.sql.*;
import java.util.ArrayList;

public class Groupe {

    private String nom;              // non null
    private String prenom;           // non null
    private String telephone;        // non null
    private String mail;             // non null, unique

    /**
     * Créer un nouvel objet persistant
     *
     * @param con
     * @param nom
     * @param prenom
     * @param telephone
     * @param mail
     * @return
     * @ return retourne un loueur si le mail est unique sinon null
     * @throws Exception impossible d'accéder à la ConnexionMySQL ou le mail est
     * deja dans la BD
     *
     */
    static public Groupe create(Connection con, String nom, String prenom, String telephone, String mail) throws Exception {
        Groupe loueur = new Groupe(nom, prenom, telephone, mail);

        String queryString
                = "insert into loueur (Nom, Prenom, Telephone, Mail)"
                + " values ("
                + Utils.toString(nom) + ", "
                + Utils.toString(prenom) + ", "
                + Utils.toString(telephone) + ", "
                + Utils.toString(mail)
                + ")";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
        return loueur;
    }

    /**
     * update de l'objet loueur dans la ConnexionMySQL
     *
     * @param con
     * @throws Exception impossible d'accéder à la ConnexionMySQL
     */
    public void save(Connection con) throws Exception {
        String queryString
                = "update loueur set "
                + " Nom =" + Utils.toString(nom) + ","
                + " Prenom =" + Utils.toString(prenom) + ","
                + " Telephone =" + Utils.toString(telephone) + ","
                + " Mail =" + Utils.toString(mail)
                + " where Nom ='" + nom + "' and Prenom='" + prenom + "';";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString, Statement.NO_GENERATED_KEYS);
    }

    public static ArrayList<Groupe> getList(Connection con) throws Exception {
        String queryString = "select * from loueur";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        ArrayList<Groupe> contrats = new ArrayList<>();
        while (lResult.next()) {
            contrats.add(creerParRequete(lResult));
        }
        return contrats;
    }

    /**
     * Retourne un loueur trouve par son nom et prénom, saved is true
     *
     * @param con
     * @param id
     * @return loueur trouvé par nom et prénom
     * @throws java.lang.Exception
     */
    public static Groupe getById(Connection con, int id) throws Exception {
        String queryString = "select * from loueur"
                + " where ID='" + id + "';";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // y en a t'il au moins un ?
        if (lResult.next()) {
            return creerParRequete(lResult);
        } else {
            return null;
        }
    }

    /**
     * Retourne un loueur trouve par son nom et prénom, saved is true
     *
     * @param con
     * @param nom nom du loueur recherché
     * @param prenom prénom du loueur recherché
     * @return loueur trouvé par nom et prénom
     * @throws java.lang.Exception
     */
    public static Groupe getByNom(Connection con, String nom, String prenom) throws Exception {
        String queryString = "select * from loueur"
                + " where Nom='" + nom + "'"
                + " and Prenom='" + prenom + "';";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // y en a t'il au moins un ?
        if (lResult.next()) {
            return creerParRequete(lResult);
        } else {
            return null;
        }
    }

    public static Groupe getByContratID(Connection con, String IDContrat) throws Exception {
        String queryString = "SELECT * FROM loueur JOIN contrat ON loueur.ID = contrat.LoueurID WHERE Numero = '" + IDContrat + "'";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        // y en a t'il au moins un ?
        if (lResult.next()) {
            return creerParRequete(lResult);
        } else {
            return null;
        }
    }

    /**
     * suppression de l'objet loueur dans la BD
     *
     * @param con
     * @return
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public boolean delete(Connection con) throws Exception {
        String queryString = "delete from loueur"
                + " where Nom='" + nom + "'"
                + " and Prenom='" + prenom + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }

    /**
     * suppression de l'objet loueur dans la BD
     *
     * @param con
     * @return
     * @throws SQLException impossible d'accéder à la ConnexionMySQL
     */
    public static boolean delete(Connection con, String ID) throws Exception {
        String queryString = "delete from loueur where ID='" + ID + "'";
        Statement lStat = con.createStatement();
        lStat.executeUpdate(queryString);
        return true;
    }

    private static Groupe creerParRequete(ResultSet result) throws Exception {
        String lNom = result.getString("Nom");
        String lPrenom = result.getString("Prenom");
        String lTelephone = result.getString("Telephone");
        String lMail = result.getString("Mail");
        return new Groupe(lNom, lPrenom, lTelephone, lMail);
    }

    /**
     * Cree et initialise completement Loueur
     */
    private Groupe(String nom, String prenom, String telephone, String mail) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.mail = mail;
    }

    /**
     * Indique le nb de Groupe dans la base de données
     *
     * @param con
     * @return le nombre de Groupe
     * @throws java.lang.Exception
     */
    public static int size(Connection con) throws Exception {
        String queryString = "select count(*) as count from loueur";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next()) {
            return (lResult.getInt("count"));
        } else {
            return 0;
        }
    }

    /*
        retourne l'ID d'un objet
     */
    public int getID(Connection con) throws Exception {
        String queryString = "select ID from loueur"
                + " where Nom='" + nom + "'"
                + " and Prenom='" + prenom + "'";
        Statement lStat = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        ResultSet lResult = lStat.executeQuery(queryString);
        if (lResult.next()) {
            return lResult.getInt("ID");
        } else {
            return 0;
        }
    }

    // --------------------- les assesseurs ----------------------------
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setTelephone(String telephone) throws Exception {
        this.telephone = telephone;
    }

    public void setMail(String mail) throws Exception {
        this.mail = mail;
    }

    /**
     * toString() operator overload
     *
     * @return the result string
     */
    @Override
    public String toString() {
        return " Nom =  " + nom + "\t"
                + " Prenom = " + Utils.toString(prenom)
                + " Telephone = " + Utils.toString(telephone)
                + " Mail = " + Utils.toString(mail)
                + " ";
    }
}
