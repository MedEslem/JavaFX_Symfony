package services;

import controllers.PostController;
import models.Thread;
import models.Post;
import models.User;
import javafx.scene.image.Image;
import utils.MyDataBase;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService implements IService<Post>{
    private Connection connection;
    private FileInputStream fis ;
    public static File file ;
    public PostService(){
        connection = MyDataBase.getInstance().getConnection();
    }
    public int getDisId(String title) throws SQLException {
        String req = "SELECT id FROM thread WHERE titleThread = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setString(1, title);

            try  {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("idThread");
                } else {
                    throw new IllegalArgumentException("Thread not found for title: " + title);
                }
            }catch(Exception e ){
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            // Log or re-throw the exception
            throw new SQLException("Error finding Thread ID: " + e.getMessage(), e);
        }
        return 0 ;
    }
    @Override
    public void add(Post Post) throws SQLException {
        String req = "INSERT INTO `post` (`contentPost`, `TimeStamp_envoi`, `senderId`,`threadId`,`image`)"
                + "VALUES (?,?,?,?,?)";
        try{
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, Post.getContentPost());
            pst.setTimestamp(2, Post.getTimeStamp_envoi());
            pst.setInt(3,Post.getSender().getId());
            pst.setInt(4, PostController.thrdId);
            if(file == null){

                pst.setNull(5, java.sql.Types.BLOB);

            }else{
                fis = new FileInputStream(file);

                pst.setBinaryStream(5, fis);

            }
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Post p) throws SQLException {
        String req = "UPDATE post" +
                "                SET contentPost = ?" +
                "               WHERE idPost = ?";
        PreparedStatement pst = connection.prepareStatement(req);
        pst.setString(1,p.getContentPost());
        pst.setInt(2,p.getIdPost());
        pst.executeUpdate();

    }

    @Override
    public void delete(int id) throws SQLException {
        try {
            PreparedStatement pre = connection.prepareStatement("Delete from post where idPost=? ;");
            pre.setInt(1, id);
            if (pre.executeUpdate() != 0) {
                System.out.println("Post Deleted");

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public Post getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Post> getAll() throws SQLException {
        List<Post> Posts  = new ArrayList<>();
        String req = "SELECT post.contentPost, post.TimeStamp_envoi, user.username AS sender  " +
                "FROM post" +
                " JOIN user ON post.senderId = user.id " +
                "JOIN thread ON post.idPost = thread.idThread;";


        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery(req);

        while (rst.next()) {
            String content =  rst.getString(1);
            Timestamp date = rst.getTimestamp(2);
            User user1 = new User(rst.getString(3));

            Post Post = new Post(content,date,user1);
            Posts.add(Post);
        }
        return Posts;
    }
    public List<Post> afficherByThreadId(int id)  {
        List<Post> Posts  = new ArrayList<>();
        String req = "SELECT post.contentPost, post.TimeStamp_envoi, user.username AS sender ,post.idPost,post.image,user.id  " +
                "FROM post" +
                " JOIN user ON post.senderId = user.id " +
                "Where post.threadId = ?;";
        try {
            PreparedStatement pre = connection.prepareStatement(req);
            pre.setInt(1, id);
            ResultSet rst = pre.executeQuery();

            while (rst.next()) {
                String contentpost = rst.getString(1);
                Timestamp date = rst.getTimestamp(2);
                int idUser = rst.getInt(6);
                User user1 = new User(idUser,rst.getString(3));
                int ident = rst.getInt(4);


                InputStream is = rst.getBinaryStream(5);
                if (is != null) {
                    OutputStream os = new FileOutputStream(new File("photo.jpg"));
                    byte[] content = new byte[1024];
                    int size = 0;

                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();
                    Image image = new Image("file:photo.jpg",100,150,true,true);
                    Post Post1 = new Post(ident, contentpost, date, user1,image);
                    Posts.add(Post1);
                }else{

                    System.out.println("null");
                    Post Post = new Post(ident, contentpost, date, user1);
                    Posts.add(Post);
                }}
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(FileNotFoundException e ){
            System.out.println(e.getMessage());
        }catch(IOException e ){
            System.out.println(e.getMessage());
        }

        return Posts;

    }

}
