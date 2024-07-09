package ovh.cosmicdan.simpledaylengthextender.forge;

import net.dries007.tfc.config.TFCConfig;

public class TfcHelper {
    public static boolean isTimeStopEnabled() {
        return TFCConfig.SERVER.enableTimeStopWhenServerEmpty.get();
    }
}
