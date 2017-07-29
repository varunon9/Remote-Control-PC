/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poweroff;

import mousekeyboardcontrol.MouseKeyboardControl;

/**
 *
 * @author varun
 */
public class PowerOff {
    String os;
    Runtime runtime;
    public PowerOff() {
        os = System.getProperty("os.name");
        runtime = Runtime.getRuntime();
    }
    
    public void shutdown() {     
        try {
            if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("shutdown -s");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("shutdown error");
            e.printStackTrace();
        }
        
    }
    
    public void restart() {     
        try {
            if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("shutdown -r");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("restart error");
            e.printStackTrace();
        }
        
    }
    
    public void suspend() {     
        try {
            if ("Windows 8.1".equals(os) || "Windows 8.0".equals(os)) {
                runtime.exec("Rundll32.exe powrprof.dll,SetSuspendState Sleep");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch(Exception e) {
            System.out.println("suspend error");
            e.printStackTrace();
        }   
    }
    
    public void lock() {     
        try {
            if ("Linux".equals(os) || "Mac OS X".equals(os)) {
                new MouseKeyboardControl().ctrlAltL();
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
        powerOff.lock();
    }
}
