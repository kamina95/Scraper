package webBackend;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class GraphicDAO {
    //Creates new Sessions when we need to interact with the database
    private SessionFactory sessionFactory;


    /** Empty constructor */
    public GraphicDAO() {
    }


    /** Sets up the session factory.
     *  Call this method first.  */
    public void init(){
        try {
            //Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //Load configuration from hibernate configuration file.
            //Here we are using a configuration file that specifies Java annotations.
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            //Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
            try {
                //Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory,
                        but we had trouble building the SessionFactory, so destroy it manually */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy( registry );
            }

            //Ouput result
            System.out.println("Session factory built.");

        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }


    /** Adds a new cereal to the database */
    public void addGraphicCard(GraphicCardsAnnotation graphicCardsAnnotation){
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Create an instance of a Cereal class
//        CerealAnnotation cereal = new CerealAnnotation();
//
//        //Set values of Cereal class that we want to add
//        cereal.setBrandId(2);
//        cereal.setProductTypeId(1);
//        cereal.setUrlId(1);
//        cereal.setWeight(2566);
//        cereal.setPrice(9.5f);
//
//        //Start transaction
        session.beginTransaction();
//
//        //Add Cereal to database - will not be stored until we commit the transaction
        session.save(graphicCardsAnnotation);
//
//        //Commit transaction to save it to database
        session.getTransaction().commit();
//
//        //Close the session and release database connection
        session.close();
        System.out.println("Cereal added to database with ID: " + graphicCardsAnnotation.getId());
    }

}
