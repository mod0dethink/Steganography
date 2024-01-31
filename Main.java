import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args) {
        // 現在のフォルダーからファイル一覧を取得
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println(listOfFiles[i].getName() + " ");
            }
        }

        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.print("秘匿するファイル名を入力してください:");
        String selectedFileName = scanner.nextLine();
        String filePath = "";
        boolean fileExists = false;

        for (File file : listOfFiles) {
            if (file.getName().equals(selectedFileName)) {
                filePath = file.getAbsolutePath(); // filePathに絶対パスを代入
                fileExists = true;
                break;
            }
        }

        if (!fileExists) {
            System.out.println("存在しません");
            return; // ファイルが存在しない場合は処理を終了
        }

        System.out.print("秘匿データを隠す先の画像ファイル名(.png)を入力してください:");
        String selectedImageName = scanner.nextLine();
        String imagePath = ""; // imagePathの宣言
        boolean imageExists = false;

        if (!selectedImageName.endsWith(".png")) {
            System.out.println("エラー: .png形式のファイルを選択してください。");
        } else {
            for (File file : listOfFiles) {
                if (file.getName().equals(selectedImageName) && file.isFile()) {
                    imagePath = file.getAbsolutePath(); // imagePathに絶対パスを代入
                    imageExists = true;
                    break;
                }
            }

            if (!imageExists) {
                System.out.println("存在しません");
                return; // 画像ファイルが存在しない場合は処理を終了
            }
        }

        // 出力画像のパスを定義
        String outputImagePath = imagePath.replace(".png", "_stego.png");

        try {
            // ファイルを画像に埋め込む
            Steganography.embedFileIntoImage(imagePath, filePath, outputImagePath);
            System.out.println("ファイルを画像に埋め込みました");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = new byte[(int) file.length()];
        
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileContent);
        }
        
        return fileContent;
    }

    public static void writeFile(String filePath, byte[] content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content);
        }
    }

}
