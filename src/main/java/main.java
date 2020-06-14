package main.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    //variables
    private static Connection conn;
    private static Statement state;
    private static ResultSet res;
    private static Scanner scan = new Scanner(System.in);


    public static void main(String args[]) throws Exception {

        //Conexio a la base de dades
        Class.forName("org.sqlite.JDBC");
        //per provar canviar ruta el arxiu sql esta dins la carpeta base dins aquest mateix projecte
        conn = DriverManager.getConnection("jdbc:sqlite:D:/Escritorio/JOSE CASA/Estudios/CFGS/base de datos/SQLite_JRodriguez/base/Agenda_JRodriguez.sqlite");

        //repeticio bucle
        boolean rep = true;

        //taules
        crearTaules();
        //inserir informació
        inserirInfo();

        //Bucle amb les questions
        while (rep) {
            System.out.println("Que vols fer?? consultar | inserir | modificar | eliminar --> ");
            String action = scan.next();

            if (action.equalsIgnoreCase("consultar")) {
                System.out.println("Vols consultar tots el contactes o un en concret:  tots | concret -->");
                String list = scan.next();
                if (list.equalsIgnoreCase("tots"))
                    seleccionarTots();
                else if (list.equalsIgnoreCase("concret")) {
                    System.out.println("Introdueix el Id del contacte --> ");
                    selecionarId(scan.next());
                }
            } else if (action.equalsIgnoreCase("inserir"))
                inserir();
            else if (action.equalsIgnoreCase("modificar")) {
                System.out.println("Id del contacte: ");
                String id = scan.next();
                selecionarId(id);
                modificar(id);
            } else if (action.equalsIgnoreCase("eliminar")) {
                System.out.println("Id del contacte: ");
                eliminar(scan.next());
            } else
                System.out.println("Accio invalida");

            System.out.println("Vol continuar? (si, no)");
            if (!scan.next().equalsIgnoreCase("si")) {
                rep = false;
                System.out.println("Gracis per fer servir el programa :D");
            }
        }

        res.close();
        state.close();
        conn.close();
    }

    private static void crearTaules() throws Exception {
        state = conn.createStatement();

        state.execute("DROP TABLE IF EXISTS Contactes");
        state.execute("DROP TABLE IF EXISTS Emails");
        state.execute("DROP TABLE IF EXISTS Telefons");

        // Contactes
        String sql = "CREATE TABLE Contactes " +
                "(Id INT PRIMARY KEY NOT NULL," +
                " Nom VARCHAR(30) NOT NULL, " +
                " Direccio VARCHAR(30) NOT NULL)";
        state.execute(sql);
        // Emails
        sql = "CREATE TABLE Emails " +
                "(Id int not null," +
                " Email VARCHAR(50) NOT NULL, " +
                " FOREIGN KEY(Id) REFERENCES Contactes (Id) ON UPDATE CASCADE ON DELETE CASCADE)";
        state.execute(sql);
        // Telefons
        sql = "CREATE TABLE Telefons " +
                "(Id int not null," +
                " Telefon INT NOT NULL, " +
                " FOREIGN KEY(Id) REFERENCES Contates (Id) ON UPDATE CASCADE ON DELETE CASCADE)";
        state.execute(sql);
    }

    private static void inserirInfo() throws Exception {
        // Contactes
        state.execute("INSERT INTO Contactes VALUES (1,'Abdul', 'almudaina');");
        state.execute("INSERT INTO Contactes VALUES (2,'Biel', 'garau');");
        state.execute("INSERT INTO Contactes VALUES (3,'Carla', 'mestral');");
        state.execute("INSERT INTO Contactes VALUES (4,'Diego', 'ederico');");
        state.execute("INSERT INTO Contactes VALUES (5,'Eduardo', 'colon');");
        state.execute("INSERT INTO Contactes VALUES (6,'Felix', 'alcalde');");
        state.execute("INSERT INTO Contactes VALUES (7,'Guillem', 'miguel');");
        state.execute("INSERT INTO Contactes VALUES (8,'Hugo', 'pelayo');");
        state.execute("INSERT INTO Contactes VALUES (9,'Ines', 'lcancia');");
        state.execute("INSERT INTO Contactes VALUES (10,'Juan', 'alcantara');");

        // Emails
        state.execute("INSERT INTO Emails VALUES (1,'abdul@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (2,'biel@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (3,'carla@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (4,'diego@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (5,'eduardo@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (6,'felix@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (7,'guillem@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (8,'hugo@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (9,'ines@gmail.com');");
        state.execute("INSERT INTO Emails VALUES (10,'juan@gmail.com');");

        // Telefons
        state.execute("INSERT INTO Telefons VALUES (1,600000001);");
        state.execute("INSERT INTO Telefons VALUES (2,600000002);");
        state.execute("INSERT INTO Telefons VALUES (3,600000003);");
        state.execute("INSERT INTO Telefons VALUES (4,600000004);");
        state.execute("INSERT INTO Telefons VALUES (5,600000005);");
        state.execute("INSERT INTO Telefons VALUES (6,600000006);");
        state.execute("INSERT INTO Telefons VALUES (7,600000007);");
        state.execute("INSERT INTO Telefons VALUES (8,600000008);");
        state.execute("INSERT INTO Telefons VALUES (9,600000009);");
        state.execute("INSERT INTO Telefons VALUES (10,600000010);");
    }

    private static void seleccionarTots() throws Exception {
        res = state.executeQuery("SELECT * FROM Contactes;");

        System.out.println("+----------------+------------------------------+---------------------------------+");
        while (res.next()) {
            String id = res.getString("Id");
            String nom = res.getString("Nom");
            String dir = res.getString("Direccio");

            System.out.printf("| ID: %9s | Nom: %-20s | Direccio: %-20s |\n",
                    id, nom, dir);
        }
        System.out.println("+----------------+------------------------------+---------------------------------+\n");
    }

    private static void selecionarId(String ID) throws Exception {
        res = state.executeQuery("SELECT C.Id, C.Nom, C.Direccio, E.Email, T.Telefon FROM Contactes C" +
                " LEFT JOIN Emails E ON C.Id = E.Id" +
                " LEFT JOIN Telefons T ON C.Id = T.Id" +
                " WHERE C.Id = '" + ID + "';");

        String id = "";
        String nom = "";
        String dir = "";
        List<String> emails = new ArrayList<>();
        List<Integer> telefons = new ArrayList<>();

        while (res.next()) {
            id = res.getString("Id");
            nom = res.getString("Nom");
            dir = res.getString("Direccio");
            if (!emails.contains(res.getString("Email")))
                emails.add(res.getString("Email"));
            if (!telefons.contains(res.getInt("Telefon")))
                telefons.add(res.getInt("Telefon"));
        }
        System.out.printf("+----------------+------------------------------+---------------------------------+-----------------------------------------------------------+--------------------------------------------------------------+\n" +
                        "| ID: %9s | Nom: %-20s | Direccio: %-20s | Email: %-50s | Telefon: %-50s |" +
                        "\n+----------------+------------------------------+---------------------------------+-----------------------------------------------------------+--------------------------------------------------------------+\n",
                id, nom, dir, emails, telefons);
    }

    private static void inserir() throws Exception {
        System.out.println("Id del contacte: ");
        String id = scan.next();
        System.out.println("Nom del contacte: ");
        String nom = scan.next();
        System.out.println("Direccio del contacte: ");
        String dir = scan.next();

        state.execute("INSERT INTO Contactes VALUES ('" + id + "', '" + nom + "', '" + dir + "');");

        System.out.println("Quants emails vols afegir? ");
        int nEmails = scan.nextInt();

        if (nEmails > 0) {
            for (int i = 0; i < nEmails; i++) {
                System.out.println("Email: ");
                String emails = scan.next();
                state.execute("INSERT INTO EMAILS VALUES ('" + id + "', '" + emails + "');");
            }
        } else if (nEmails == 0)
            System.out.println("Emails no afegits");
        else
            System.out.println("Numero de emails no valit");

        System.out.println("Quants telefons vols afegir");
        int nTelf = scan.nextInt();

        if (nTelf > 0) {
            for (int i = 0; i < nTelf; i++) {
                System.out.println("Telefons: ");
                int tel = scan.nextInt();
                state.execute("INSERT INTO Telefons VALUES ('" + id + "', '" + tel + "');");
            }
        } else if (nTelf == 0)
            System.out.println("Telefons no afegits");
        else
            System.out.println("Numero de telefons no valid.");

        System.out.println("Contacte afegit correctament.");

    }

    private static void modificar(String id) throws Exception {
        System.out.println("Nou nom: ");
        String nom = scan.next();
        System.out.println("Nova direccio (nomes posar el nom del carrer): ");
        String direccio = scan.next();

        state.executeUpdate("UPDATE Contactes SET Nom = '" + nom + "', Direccio = '" + direccio + "' WHERE ID = '" + id + "';");
        state.execute("DELETE FROM Emails WHERE Id = '" + id + "';");
        state.execute("DELETE FROM Telefons WHERE Id = '" + id + "';");

        System.out.println("Quants emails vols afegir? ");
        int nEmails = scan.nextInt();

        if (nEmails > 0) {
            for (int i = 0; i < nEmails; i++) {
                System.out.println("Email: ");
                String emails = scan.next();
                state.execute("INSERT INTO Emails VALUES ('" + id + "', '" + emails + "');");
            }
        } else if (nEmails == 0)
            System.out.println("Emails no afegits.");
        else
            System.out.println("Numero de emails no valid");

        System.out.println("Quants telefons vols afegir ");
        int nTelf = scan.nextInt();

        if (nTelf > 0) {
            for (int i = 0; i < nTelf; i++) {
                System.out.println("Telefon: ");
                int telf = scan.nextInt();
                state.execute("INSERT INTO Telefons VALUES ('" + id + "', '" + telf + "');");
            }
        } else if (nTelf == 0)
            System.out.println("Telefons no afegits");
        else
            System.out.println("Quantitat de telefons no valid.");
    }

    private static void eliminar(String id) throws Exception {
        System.out.println("Estas segur de eliminar el contacte?? No es podra recuperar el contacte (si, no)");
        String conf = scan.next();

        if (conf.equalsIgnoreCase("si")) {
            state.execute("DELETE FROM Contactes WHERE Id = '" + id + "';");
            state.execute("DELETE FROM Emails WHERE Id = '" + id + "';");
            state.execute("DELETE FROM Telefons WHERE Id = '" + id + "';");
            System.out.println("Contacte eliminat");
        } else if (conf.equalsIgnoreCase("no"))
            System.out.println("Contacte no eliminat");
        else
            System.out.println("No heu confirmat, contacte no eliminat");
    }
}

