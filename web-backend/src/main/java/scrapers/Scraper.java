package scrapers;

import org.hibernate.Session;
import webBackend.Brand;
import webBackend.Comparison;
import webBackend.GraphicCard;
import webBackend.GraphicDAO;

import java.util.List;


public abstract class Scraper implements Runnable{

    public GraphicDAO graphicDAO;
    private volatile boolean stop = false;
    public static int count = 0;
    public void run() {
        if (stop) return;
        scrapeAll();
        System.out.println(count);
    }

    public void stop() {
        stop = true;
    }

    public abstract void scrapeAll();

    public void setHibernate(GraphicDAO graphicDAO){
        this.graphicDAO = graphicDAO;
    }

    public void createClasses(String model, String brandName, String url, String imgUrl, Double price, String description) {
        model = model.replace("NVIDIA", "").replace("RTX","").replace("GeForce","").replace("Nvidia","").trim();
        GraphicCard graphicCard = new GraphicCard();
        graphicCard.setModel(model);
        graphicCard.setDescription(description);

        Brand brand = new Brand();
        brand.setBrand(brandName);
        brand.setImg_url(imgUrl);

        Comparison comparison = new Comparison();
        comparison.setUrl(url);
        comparison.setPrice(price);
        createTables(graphicCard, brand, comparison);
    }

    public void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison){
        if (!brand.getBrand().isEmpty() && !graphicCard.getModel().isEmpty() && !graphicCard.getDescription().isEmpty()
                && !brand.getImg_url().isEmpty() && comparison.getPrice() != -1 && !comparison.getUrl().isEmpty()
                && !graphicCard.getDescription().isEmpty()) {
            graphicDAO.addGraphicCard(graphicCard);
            brand.setId_product(graphicCard);
            graphicDAO.addBrand(brand);
            comparison.setIdCards(brand);
            graphicDAO.addComparison(comparison);
            count++;
        }
    }

//    public void saveAndMerge(Student student) throws Exception {
//        //Get a new Session instance from the session factory and start transaction
//        Session session = sessionFactory.getCurrentSession();
//        session.beginTransaction();
//
//        //First find or create university
//        String queryStr = "from University where name='" + student.getDegree().getUniversity().getName() + "'";
//        List<University> universityList = session.createQuery(queryStr).getResultList();
//
//        //University is in database
//        if(universityList.size() == 1) {//Found a single University
//            //Update university location, if we want to
//            universityList.get(0).setLocation(student.getDegree().getUniversity().getLocation());
//
//            //Set mapped university in degree
//            student.getDegree().setUniversity(universityList.get(0));
//        }
//        //No university with that name in database
//        else if (universityList.size() == 0){
//            session.saveOrUpdate(student.getDegree().getUniversity());
//        }
//        //Error
//        else{
//            throw new Exception("Multiple universities with the same name");
//        }
//
//        //Next find or create degree. Need to search by university id to handle problem
//        //of degrees with the same name at different universities.
//        queryStr = "from Degree where title='" + student.getDegree().getTitle() + "'";
//        queryStr += " and university_id=" + student.getDegree().getUniversity().getId();
//        List<Degree> degreeList = session.createQuery(queryStr).getResultList();
//
//        //Degree is in database
//        if(degreeList.size() == 1) {//Found a single Degree
//            //Update degree description, if we want to
//            degreeList.get(0).setDescription(student.getDegree().getDescription());
//
//            //Set mapped university in degree
//            student.setDegree(degreeList.get(0));
//        }
//        //No degree with that name in database
//        else if (degreeList.size() == 0){
//            session.saveOrUpdate(student.getDegree());
//        }
//        //Error
//        else{
//            throw new Exception("Multiple degrees with the same name");
//        }
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

}