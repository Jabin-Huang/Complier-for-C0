package demo.main;

import javax.swing.*;
import java.io.*;

public class File_op {

    //返回打开文件的路径
    public static String open(JButton button) throws IOException {
        JFileChooser chooser = new JFileChooser();
        //只能选文件
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //禁止多选
        chooser.setMultiSelectionEnabled(false);
        //默认目录
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        //已确认的状态
        if (chooser.showOpenDialog(button) ==JFileChooser.APPROVE_OPTION){
            File openFile = chooser.getSelectedFile();
            return openFile.getPath();
        }
        else return null;
    }


    public static void save(JButton button){
         //TODO
    }

    //文件内容转String
    public static String fileToString(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer buffer = new StringBuffer();
        line = reader.readLine();
        while(line != null){
            buffer.append(line);
            buffer.append("\n");
            line = reader.readLine();
        }
        reader.close();
        is.close();
        return buffer.toString();
    }
}
