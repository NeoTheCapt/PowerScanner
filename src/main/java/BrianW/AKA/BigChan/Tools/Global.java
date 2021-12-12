package BrianW.AKA.BigChan.Tools;

import java.util.concurrent.ExecutorService;

public class Global {
    public static BrianW.AKA.BigChan.GUI.PowerTab PowerTab;
    public static String configFile = "powerscanner.conf.ini";
    public static String[] fileExt = new String[]{"doc",};
    public static Config config;
    //	public static InteractionServer interactionServer;
    public static ExecutorService fixedThreadPool;
}