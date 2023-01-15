package com.example.dinim3akalpha001;

import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.dinim3akalpha001.MongoController.db;
import static com.example.dinim3akalpha001.ProfileController.gridBucket;
import static com.example.dinim3akalpha001.SignupController2.getuEmail;
import static com.example.dinim3akalpha001.SignupController2.getuJob;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class CarController implements Initializable {
    @FXML
    private Button Car;
    @FXML
    private Button Arrow;

    @FXML
    protected void Upload() throws IOException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            mongoupload(file.getPath(),file.getName());
            //Image image = new Image(file.toURI().toString());
            //ImageView img = new ImageView(image);
            //img.fitWidthProperty().bind(Car.widthProperty());
            //img.fitHeightProperty().bind(Car.heightProperty());
            //Car.setGraphic(img);
            saveToFileSystem(file.getName());
        }
    }

    public ObjectId mongoupload(String filePath, String fileName) {
        ObjectId fileId = null;
        try {
            InputStream inStream = new FileInputStream(new File(filePath));
            GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(new Document("type", "image").append("content_type", "image/png"));
            fileId = gridBucket.uploadFromStream(fileName, inStream, uploadOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.getCollection("users").updateOne(eq("email", getuEmail()), set("carImage",fileId));
        return fileId;
    }

    private void saveToFileSystem(String fileName) throws IOException {
        GridFSDownloadOptions downloadOptions = new GridFSDownloadOptions().revision(0);
        File file = new File("c:/DiniM3ak/"+fileName);
        file.getParentFile().mkdirs(); // Will create parent directories if not exists
        file.createNewFile();
        try (FileOutputStream streamToDownloadTo = new FileOutputStream(file)) {
            gridBucket.downloadToStream(fileName, streamToDownloadTo, downloadOptions);
            streamToDownloadTo.flush();
        }
        Image image = new Image(file.toURI().toString());
        ImageView img = new ImageView(image);
        img.fitWidthProperty().bind(Car.widthProperty());
        img.fitHeightProperty().bind(Car.heightProperty());
        Car.setGraphic(img);
    }

    @FXML
    private void handleArrow() throws IOException {
        new DiniController().handleScenes("ProfileDriver.fxml",Arrow);
    }
    @FXML
    private void handlePayment() throws IOException {
        new DiniController().handleScenes("PaymentSee.fxml",Arrow);
    }
    @FXML
    private void handleMenu() throws IOException {
        new DiniController().handleScenes(getuJob().equals("Driver")?"HomeDriver.fxml":"HomeRider.fxml",Arrow);
    }
    @FXML
    private void uploadDetails() {

    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Document user = db.getCollection("users").find(eq("email",getuEmail())).first();
        try {
            saveToFileSystem(db.getCollection("fs.files").find(eq("_id",user.getObjectId("carImage"))).first().getString("filename"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
