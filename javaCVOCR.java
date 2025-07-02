import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.*;
import org.bytedeco.tesseract.TessBaseAPI;

import java.io.File;

import static org.bytedeco.leptonica.global.lept.*;
import static org.bytedeco.tesseract.global.tesseract.*;

public class JavaCVOCR {

    public static void main(String[] args) {
        String imagePath = "src/main/resources/sample.png"; 
        String tessDataPath = "tessdata"; 

        TessBaseAPI api = new TessBaseAPI();
        if (api.Init(tessDataPath, "eng") != 0) {
            System.err.println("Could not initialize tesseract.");
            return;
        }
        PIX image = pixRead(imagePath);
        if (image == null) {
            System.err.println("Could not read image.");
            return;
        }

        api.SetImage(image);
        BytePointer outText = api.GetUTF8Text();

        System.out.println("Extracted Text:\n" + outText.getString());
        outText.deallocate();
        api.End();
        pixDestroy(image);
    }
}

