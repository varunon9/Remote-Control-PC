/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.imageviewer;

import static com.sun.org.apache.xml.internal.serialize.LineSeparator.Windows;
import java.io.IOException;

/**
 *
 * @author KULDEEP KUMAR
 */
public class ImageViewer {
    
    public static void main(String []args) throws IOException{
    String path="apple.jpg";
    String expr = "mspaint " + path;
    Runtime.getRuntime().exec(expr);
    System.out.println("Dsafs");
    }
}
