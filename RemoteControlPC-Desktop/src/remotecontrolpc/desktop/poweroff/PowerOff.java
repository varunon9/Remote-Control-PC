/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolpc.desktop.poweroff;

/**
 *
 * @author varun
 */
public class PowerOff {
    String os;
    Runtime runtime;
    PowerOff() {
        os = System.getProperty("os.name");
        runtime = Runtime.getRuntime();
    }
    void shutdown() {     
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                runtime.exec("shutdown -h now"); //or
                //runtime.exec("systemctl poweroff");
            } else if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("shutdown -s");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("shutdown error");
            e.printStackTrace();
        }
        
    }
    void restart() {     
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                runtime.exec("systemctl reboot");
            } else if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("shutdown -r");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("restart error");
            e.printStackTrace();
        }
        
    }
    void logOff() {     
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                runtime.exec("systemctl hibernate");
            } else if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("shutdown -l");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("Log off error");
            e.printStackTrace();
        }
        
    }
    void suspend() {     
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                runtime.exec("systemctl suspend");
            } else if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("suspend error");
            e.printStackTrace();
        }
        
    }
    void lock() {     
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                runtime.exec("systemctl lock");
            } else if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("Rundll32.exe user32.dll,LockWorkStation");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("pc lock error");
            e.printStackTrace();
        }
        
    }
    
    public static void main(String args[]) {
        PowerOff powerOff = new PowerOff();
        powerOff.shutdown();
    }
}
