import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 

public class FileHandler {

    // ファイルからデータを読み込むメソッド
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath); 
        byte[] fileContent = new byte[(int) file.length()]; // ファイルのサイズに合わせたbyte配列を作成
        
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileContent); // ファイルの内容をbyte配列に読み込む
        } 
        
        return fileContent; 
    }

    // ファイルにデータを書き込むメソッド
    public static void writeFile(String filePath, byte[] content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content); // byte配列の内容をファイルに書き込む
        } 
    }
}
