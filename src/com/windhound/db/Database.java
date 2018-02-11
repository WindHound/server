import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceAsStream;

public class Database {

    private String hostname, sid, user, password, port;

    /**
     * Initialise from variables
     * @param hostname
     * @param port
     * @param sid
     * @param user
     * @param password
     */
    Database(String hostname, String port, String sid, String user, String password){
        this.hostname = hostname;
        this.port = port;
        this.sid = sid;
        this.user = user;
        this.password = password;
    }

    /**
     * Initialise from properties file
     * @param propertiesPath
     */
    Database(String propertiesPath) {
        Properties prop = new Properties();
        InputStream input = getResourceAsStream(propertiesPath);

        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.hostname = prop.getProperty("hostname");
        this.port = prop.getProperty("port");
        this.sid = prop.getProperty("sid");
        this.user = prop.getProperty("user");
        this.password = prop.getProperty("password");
    }


    public ResultSet query(String query) {

        ResultSet rs = null;
        Connection con;

        try{
            con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@"+ hostname + ":" + port +":"+ sid ,user ,password);

            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);

        }catch(Exception e){ System.out.println(e);}

        return rs;
    }

    //LOCATION
    public ArrayList<Location> getLocationSet(String boatID, String raceID) throws SQLException {
        ArrayList<Location> locationSet = new ArrayList<>();
        Location temp = null;

        String str = "select * from LOCATION where BOAT_ID=" + boatID + " AND STAGE_ID=" + raceID + " ORDER BY LOCATION_ID DESC";
        ResultSet rs = query(str);


        while(rs.next()) {
            try {
                temp = new Location(rs.getInt(1), rs.getInt(2), rs.getFloat(3),
                        rs.getFloat(4), rs.getFloat(5), rs.getFloat(6),
                        rs.getFloat(7), rs.getInt(8));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            locationSet.add(temp);
        }

        return locationSet;
    }

    public void addLocation(Location l) {

        String variables = "stage_id, boat_id, longitude, latitude, acc, gyro, compass";
        String values    = l.getRaceID() +","+ l.getUserID() +","+ l.getLongitude() +","+ l.getLatitude() +","+
                l.getAccelerometer() +","+ l.getGyro() +","+ l.getCompass();

        ResultSet rs = query("insert into location ("+ variables +") values ("+ values +")");
    }


    //BOAT
    public void addBoat(Boat b) {

        String variables = "NAME, SKIPPER, SAILNO, LENGTH, BEAM, DISPLACEMENT, DRAFT, CLASS, TYPE, GPH, OFFSHORE_TOD, " +
                "OFFSHORE_TOT, OFFSHORE_TRIPLE_L, OFFSHORE_TRIPLE_M, OFFSHORE_TRIPLE_H, INSHORE_TOD, INSHORE_TOT, " +
                "INSHORE_TRIPLE_L, INSHORE_TRIPLE_M, INSHORE_TRIPLE_H";

        String values = "'" + b.getName() +"','"+ b.getSkipper() +"','"+ b.getSailNo() +"','"+ b.getLength() +"','"+ b.getBeam() +"','"+
                b.getDisplacement() +"','"+ b.getDraft() +"','"+ b.getBoatClass() +"','"+ b.getType() +"','"+ b.getGph() +"','"+
                b.getOffshoreToD() +"','"+ b.getOffshoreToT() +"','"+ b.getOffshoreTnoL() +"','"+ b.getOffshoreTnoM() +"','"+
                b.getOffshoreTnoH() +"','"+ b.getInshoreToD() +"','"+ b.getInshoreToT() +"','"+ b.getOffshoreTnoL() +"','"+
                b.getInshoreTnoM() +"','"+ b.getInshoreTnoH() + "'";

        String query = "insert into boat ("+variables+") values ("+values+")";
        System.out.println(query);

        query(query);
    }

    public Boat getBoat(int boatID) {
        ResultSet rs = query("select * from boat where boat_id=" + boatID);
        Boat b = null;

        try {
            rs.next();
            b = new Boat(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
                    rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getString(8),
                    rs.getString(9), rs.getDouble(10), rs.getDouble(11),
                    rs.getDouble(12), rs.getDouble(13), rs.getDouble(14),
                    rs.getDouble(15), rs.getDouble(16), rs.getDouble(17), rs.getDouble(18),
                    rs.getDouble(19), rs.getDouble(20));

            b.setBoatID(rs.getInt(21));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return b;
    }

    public void deleteBoat(int boatID) {
        ResultSet rs = query("delete from boat where boat_id=" + boatID);
    }


    //USER
    public void addUser(User u) {
        String variables = "name, username, password, email, telno";
        String values = "'" + u.getName() +"','"+ u.getUsername() +"','"+ u.getPassword() +"','"+ u.getEmail()
                +"','"+ u.getTelNo();

        query("insert into user ("+variables+") values ("+values+")");
    }

    private User getUser(String query) {
        ResultSet rs = query("select * from user where " + query);

        User u = null;

        try {
            rs.next();

            u = new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getInt(6));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }

    public User getUserByUsername(String username) {
        return getUser("username=" + username);
    }

    public User getUserByID(int userID) {
        return getUser("user_id=" + userID);
    }

    public void deleteUser(int userID) {
        query("delete from boat where user_id=" + userID);
    }


    //REL_BOAT_USER
    public void addBoatUserRelation(RelBoatUser r) {

    }
}


