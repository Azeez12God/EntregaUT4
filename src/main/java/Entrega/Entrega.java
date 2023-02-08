package Entrega;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class Entrega {
    static Scanner tec = new Scanner(System.in);
    static Cromo c;
    static List<Cromo> cromos = new ArrayList<>();
    static List<Document> documents = new ArrayList<>();
    static MongoClient mc;
    static MongoDatabase mdb;
    static MongoCollection mco;
    public static void main(String[] args) {

        int opcion = 1;
        mc = new MongoClient("localhost");
        mdb = mc.getDatabase("cromium");
        mco = mdb.getCollection("cromos");
        boolean valido = false;



        while (opcion!=0){
            System.out.println("¿Que quieres hacer?\n-----------------------\n\n1.- Insertar documento/s\n2.- Modificar nombre de cromo / Modificar posicion álbum de todos los de un equipo\n3.- Borrar algún documento." +
                    "\n4.- Listar todos los cromos.\n5.- Listar los cromos de un mismo equipo.\n\n0.- Salir del programa");
            try {
                opcion = tec.nextInt();
                valido = true;
            } catch (NumberFormatException nfe){
                System.out.println("El formato no es válido. Por favor pon un número.");
                valido = false;
            }
            while(!valido) {
                try {
                    switch (opcion) {
                        case 1:
                            System.out.println("¿Cómo quieres insertar?\n\n\t1.- De uno en uno\n\t2.- En grupo");
                            int insertar = tec.nextInt();
                            while (insertar < 1 || insertar > 2) {
                                System.out.println("Dime un número entre 1 y 2");
                                insertar = tec.nextInt();
                            }

                            if (insertar == 1) {
                                insertarDocumento(1);
                                System.out.println("Añadido correctamente.");
                            } else {
                                System.out.println("¿Y cuántos quieres añadir?");
                                int cuantos = tec.nextInt();
                                while (cuantos < 1) {
                                    System.out.println("Dime un número mayor que 1 para añadir en grupo.");
                                    cuantos = tec.nextInt();
                                }
                                insertarDocumento(cuantos);
                                System.out.println("Añadidos correctamente.");
                            }
                            break;

                        case 2:
                            System.out.println("¿Qué quieres modificar?\n\n\t1.- El nombre de un cromo\n\t2.- La posición del álbum de un equipo");
                            insertar = tec.nextInt();
                            while (insertar < 1 || insertar > 2) {
                                System.out.println("Dime un número entre 1 y 2");
                                insertar = tec.nextInt();
                            }

                            if (insertar == 1) {
                                modificaDocumento(1);
                                System.out.println("Modificado correctamente");
                            } else {
                                modificaDocumento(2);
                                System.out.println("Modificados correctamente");
                            }
                            break;

                        case 3:
                            System.out.println("¿Cómo quieres borrar?\n\n\t1.- Un solo cromo\n\t2.- Borrar todos los que estén por debajo de una id\n\t3.- Borrar todos los cromos");
                            insertar = tec.nextInt();
                            while (insertar < 1 || insertar > 3) {
                                System.out.println("Dime un número entre 1 y 2");
                                insertar = tec.nextInt();
                            }
                            borraDocumento(insertar);
                            System.out.println("Operación realizada correctamente.");
                            break;

                        case 4:
                            listaCromos();
                            break;

                        case 5:
                            listaCromosEquipo();
                            break;
                    }
                } catch (NumberFormatException nfe){
                    System.out.println("El formato no es válido. Por favor pon un número.");
                }
            }
            mc.close();
        }
    }

    public static void insertarDocumento(int numDocs){
        try {
            for (int i = 0; i < numDocs; i++) {
                System.out.println("DOCUMENTO " + (i + 1));
                System.out.println("-----------------------------");
                System.out.println("¿Cuál es el id del cromo?");
                Long id = tec.nextLong();
                tec.nextLine();
                System.out.println("Dime el nombre del jugador del cromo:");
                String nom = tec.nextLine();
                System.out.println("Dime el nombre del equipo de fútbol del cromo");
                String equipo = tec.nextLine();
                System.out.println("Dime la posición del álbum del cromo");
                int pos = tec.nextInt();

                c = new Cromo(id, nom, equipo, pos);
                cromos.add(c);
            }
            if (numDocs > 1) {
                for (Cromo c : cromos) {
                    Document doc = new Document("_id", c.getId())
                            .append("nombre", c.getNombre())
                            .append("equipo", c.getEquipo())
                            .append("posicion", c.getPosicionAlbum());
                    documents.add(doc);
                }
                mco.insertMany(documents);
            } else {
                Document doc2 = new Document("_id", c.getId())
                        .append("nombre", c.getNombre())
                        .append("equipo", c.getEquipo())
                        .append("posicion", c.getPosicionAlbum());
                mco.insertOne(doc2);
            }
        } catch (NumberFormatException nfe){
            System.out.println("El formato no es válido. Por favor pon un número.");
        }
    }

    public static void modificaDocumento(int numDocs){
        try {
            if (numDocs > 1) {
                tec.nextLine();
                System.out.println("Dime el nombre del equipo de fútbol al que le quieres cambiar el número de posición de álbum (sumar)");
                String teamEdit = tec.nextLine();
                System.out.println("Y ahora dime cuanto le quieres sumar a sus posiciones");
                int posEdit = tec.nextInt();

                Document filtrar = new Document("equipo", teamEdit);
                Document update = new Document("$inc", new Document("posicion", posEdit));
                mco.updateMany(filtrar, update);
            } else {
                System.out.println("Dime la id del cromo al que le quieres cambiar el nombre");
                int idEdit = tec.nextInt();
                tec.nextLine();
                System.out.println("Y ahora dime el nombre que le quieres poner al cromo:");
                String nomEdit = tec.nextLine();

                Document filtrar = new Document("_id", idEdit);
                Document update = new Document("$set", new Document("nombre", nomEdit));

                mco.updateOne(filtrar, update);
            }
        } catch (NumberFormatException nfe){
            System.out.println("El formato no es válido. Por favor pon un número.");
        }
    }

    public static void borraDocumento(int insertado)
    {
        try {
            int id;
            Document doc;
            switch (insertado) {
                case 1:
                    System.out.println("¿Cuál es la id del cromo que quieres borrar?");
                    id = tec.nextInt();
                    doc = new Document("_id", id);
                    mco.deleteOne(doc);
                    break;

                case 2:
                    System.out.println("¿A partir de que id quieres borrar?");
                    id = tec.nextInt();
                    doc = new Document("_id", new Document("$lte", id));
                    mco.deleteMany(doc);
                    break;

                case 3:
                    doc = new Document("_id", new Document("$lt", 1));
                    mco.deleteMany(doc);
                    doc = new Document("_id", new Document("$gte", 1));
                    mco.deleteMany(doc);
                    break;
            }
        }catch (NumberFormatException nfe){
            System.out.println("El formato no es válido. Por favor pon un número.");
        }
    }

    public static void listaCromos(){
        int i = 1;
        List<Cromo> cromos = new ArrayList<>();
        Document doc = new Document();
        FindIterable fit = mco.find();
        MongoCursor mcu = fit.iterator();

        if(mcu.hasNext()){
            System.out.println("LISTA DE TODOS LOS CROMOS\n--------------------------");
            while (mcu.hasNext())
            {
                doc = (Document) mcu.next();
                Cromo c = new Cromo();
                c.setId(doc.getLong("_id"));
                c.setNombre(doc.getString("nombre"));
                c.setEquipo(doc.getString("equipo"));
                c.setPosicionAlbum(doc.getInteger("posicion"));
                cromos.add(c);
            }
            for (Cromo c:cromos) {
                System.out.println("Cromo " + i + ":\n\t- ID: " + c.getId() + "\n\t- Nombre: " + c.getNombre() + "\n\t- Equipo: "+c.getEquipo()+"\n\t- Posición álbum: " + c.getPosicionAlbum());
                System.out.println();
                i++;
            }
        }

        else
            System.out.println("No hay cromos en la colección.");
    }

    public static void listaCromosEquipo(){
        int i = 1;
        tec.nextLine();
        System.out.println("Dime el equipo del que quieres listar los cromos");
        String equipo = tec.nextLine();

        List<Document> docs = (List<Document>) mco.find(and(gte("posicion",1), eq("equipo", equipo)))
                .projection(fields(excludeId(), include("nombre", "equipo"))).into(new ArrayList<>());

        for(Document result:docs)
        {
            System.out.println("DOCUMENTO " + i + "\n------------------------------");
            System.out.println("\tNombre: " + result.getString("nombre") +
                    "\n\tEquipo: " + result.getString("equipo"));
            i++;
        }

        System.out.println();
    }
}
