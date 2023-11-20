package webBackend;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * Data Access Object (DAO) for interacting with the database and managing GraphicCard, Brand, and Comparison entities.
 */
public class GraphicDAO {

    // Creates new Sessions when interacting with the database
    private SessionFactory sessionFactory;

    /**
     * Empty constructor for GraphicDAO.
     */
    public GraphicDAO() {
    }

    /**
     * Sets up the session factory.
     * Call this method first before interacting with the database.
     */
    public void init() {
        try {
            // Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            // Load configuration from the hibernate configuration file.
            // Here, we are using a configuration file that specifies Java annotations.
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            // Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
            try {
                // Create the session factory - the goal of the init method.
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                // The registry would be destroyed by the SessionFactory,
                // but we had trouble building the SessionFactory, so destroy it manually
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            // Output the result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            // Make sure to log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
        }
    }

    /**
     * Saves or updates the Comparison entity in the database.
     *
     * @param comparison The Comparison object to be saved or updated.
     */
    public void saveAndMerge(Comparison comparison) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        // Query to retrieve GraphicCard based on the model
        String queryStr = "from GraphicCard where model='" + comparison.getBrand().getGraphicCard().getModel() + "'";
        List<GraphicCard> graphicCardList = session.createQuery(queryStr).getResultList();

        // Check if a matching GraphicCard exists
        if (graphicCardList.size() == 1) {
            comparison.getBrand().setGraphicCard(graphicCardList.get(0));
        } else if (graphicCardList.size() == 0) {
            // Save or update GraphicCard if not found
            session.saveOrUpdate(comparison.getBrand().getGraphicCard());
        }

        // Query to retrieve Brand based on brand name and GraphicCard id
        queryStr = "from Brand where brand='" + comparison.getBrand().getBrand() + "'";
        queryStr += " and id_product=" + comparison.getBrand().getGraphicCard().getId();

        List<Brand> brandList = session.createQuery(queryStr).getResultList();

        // Check if a matching Brand exists
        if (brandList.size() == 1) {
            comparison.setBrand(brandList.get(0));
        } else if (brandList.size() == 0) {
            // Save or update Brand if not found
            session.saveOrUpdate(comparison.getBrand());
        }

        // Query to check if a Comparison with the same URL already exists
        queryStr = "from Comparison where url='" + comparison.getUrl() + "'";
        List<Comparison> comparisonList = session.createQuery(queryStr).getResultList();

        // Save or update Comparison if not found
        if (comparisonList.size() == 0) {
            session.saveOrUpdate(comparison);
        }

        // Commit the transaction and close the session
        session.getTransaction().commit();
        session.close();
    }
}
