package webBackend;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class GraphicDAO {
    //Creates new Sessions when we need to interact with the database
    private SessionFactory sessionFactory;


    /**
     * Empty constructor
     */
    public GraphicDAO() {
    }


    /**
     * Sets up the session factory.
     * Call this method first.
     */
    public void init() {
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
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                    /* The registry would be destroyed by the SessionFactory,
                        but we had trouble building the SessionFactory, so destroy it manually */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            //Ouput result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }

    public void saveAndMerge(Comparison comparison) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //First find or create university
        String queryStr = "from GraphicCard where model='" + comparison.getBrand().getGraphicCard().getModel() + "'";
        List<GraphicCard> graphicCardList = session.createQuery(queryStr).getResultList();

        if (graphicCardList.size() == 1) {//Found a single University
            //Update university location, if we want to
            //graphicCardList.get(0).setLocation(student.getDegree().getUniversity().getLocation());

            //Set mapped university in degree
            comparison.getBrand().setGraphicCard(graphicCardList.get(0));
        }
        //No university with that name in database
        else if (graphicCardList.size() == 0) {
            session.saveOrUpdate(comparison.getBrand().getGraphicCard());
        }
        //Error
//        else {
//            throw new Exception("Multiple graphic cards with the same name");
//        }

        queryStr = "from Brand where brand='" + comparison.getBrand().getBrand() + "'";
        queryStr += " and id_product=" + comparison.getBrand().getGraphicCard().getId();

        List<Brand> brandList = session.createQuery(queryStr).getResultList();

        //Degree is in database
        if (brandList.size() == 1) {//Found a single Degree
            //Set mapped university in degree
            comparison.setBrand(brandList.get(0));
        }
        //No degree with that name in database
        else if (brandList.size() == 0) {
            session.saveOrUpdate(comparison.getBrand());
        }
        //Error
//        else {
//            throw new Exception("Multiple degrees with the same name");
//        }
        session.saveOrUpdate(comparison);
        session.getTransaction().commit();
        System.out.println(comparison.getBrand().getGraphicCard().getModel() + " " + comparison.getBrand().getGraphicCard().getDescription() + " " + comparison.getBrand().getBrand() + " " + comparison.getPrice());

        //Close the session and release database connection
        session.close();
    }

//
//        //Finally save or update student
//        queryStr = "from Student where firstName='" + student.getFirstName() + "' and surname='" + student.getSurname() + "'";
//        List<Student> studentList = session.createQuery(queryStr).getResultList();
//
//        //Student is in database
//        if(studentList.size() == 1) {//Found a single Student
//            //Update student if necessary
//            studentList.get(0).setAge(student.getAge());//Update mapped student object
//            System.out.println("Age: " + studentList.get(0).getAge());
//        }
//        //No student with that name in database
//        else if (studentList.size() == 0){
//            //Create new student
//            session.saveOrUpdate(student);
//        }
//        //Error
//        else{
//            throw new Exception("Multiple students with the same first name and surname");
//        }
//
//        //Commit transaction to save chagnes made to mapped classes
//        session.getTransaction().commit();
//
//        //Close the session and release database connection
//        session.close();
//        System.out.println("Student ID: " + student.getId() + "; Degree ID: " + student.getDegree().getId() + "; University ID: " + student.getDegree().getUniversity().getId());
//    }


    /**
     * Adds a new cereal to the database
     */

    //TODO Change to interface and hold the different tables on a interface, less for repetition
    public void addGraphicCard(GraphicCard graphicCard) {
        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

//        //Start transaction
        session.beginTransaction();
//
//        //Add Cereal to database - will not be stored until we commit the transaction
        session.save(graphicCard);
//
//        //Commit transaction to save it to database
        session.getTransaction().commit();
//
//        //Close the session and release database connection
        session.close();
        System.out.println("GraphicCard added to database with ID: " + graphicCard.getId());
    }

    public void addBrand(Brand brand) {

        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        session.save(brand);

        session.getTransaction().commit();

        session.close();
        System.out.println("Brand added to database with ID: " + brand.getId());
    }

    public void addComparison(Comparison comparison) {

        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        session.save(comparison);

        session.getTransaction().commit();

        session.close();
        System.out.println("Comparison added to database with ID: " + comparison.getId());
    }

}
