package Polygen.Model.ImageProcessing;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import java.io.ByteArrayInputStream;
import java.io.File;


public class ImageProcessing {

    private File file = null;
    private Mat originalMat;
    private Mat processedImgMat;
    private ImageFilter imageFilter = new ImageFilter();


	public ImageProcessing() throws Exception {

	    System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

	}

	public void loadImage(File quickLoadFile) {
        Mat imageMat;
        if (quickLoadFile != null) file = quickLoadFile;
        else {
            Polygen.Model.FileSearcher fileSearcher = new Polygen.Model.FileSearcher();

            try {
                file = fileSearcher.openFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file == null) return;
        }
        imageMat = Imgcodecs.imread(file.toString());
        if (imageMat.empty() && file.toString() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Loading File");
            alert.setHeaderText("Could not load the image");
            alert.setContentText(file.toString().substring(file.toString().lastIndexOf("\\") + 1) + " is not a supported file"); //Windows Path muss an alle Betriebsysteme angepasst werden
            alert.showAndWait();
            loadImage(null); //Wenn eine nicht lesbare Datei geöffnet wurde, öffnet sich die Methode immer wieder selber
        }

        this.originalMat = imageMat;
	}

	private void writeImage(Mat imgMat, String filename) {
		String filePathName = "Resources/" + filename;
	    Imgcodecs.imwrite(filePathName, imgMat,
	    		new MatOfInt(Imgcodecs.CV_IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY,
	    					 Imgcodecs.CV_IMWRITE_PNG_STRATEGY_FIXED));
	}

    public Image drawImage() { //Wird in UiController von EventHandlern ausgeführt
        Mat processedImgMat = imageFilter.processMat(originalMat);
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", processedImgMat, byteMat); //imgMat = Mat die gezeichnet werden soll
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public void setBlurFilter(int blurFilter) { imageFilter.setBlurFilter(blurFilter); }
    public void setEdgeExtraction0(int edgeExtraction0) { imageFilter.setEdgeExtraction0(edgeExtraction0); }
    public void setEdgeExtraction1(int edgeExtraction1) { imageFilter.setEdgeExtraction1(edgeExtraction1); }
    public void setEdgeExtraction2(int edgeExtraction2) { imageFilter.setEdgeExtraction2(edgeExtraction2); }
    public void setValues(float[] values) { imageFilter.setValues(values); }
    public void setStates(boolean[] states) { imageFilter.setStates(states); }
    public Mat getOriginalMat() { return originalMat; }
    public Mat getProcessedImgMat() { return processedImgMat; }
    public File getFile() { return file; }
}
