package org.examples.sb.helpers;

public class ThreadLocalRegionResolver implements RegionResolver{

    private static final ThreadLocal<String> currentRegion = new ThreadLocal<>();

    @Override
    public String resolveCurrentRegion() {
        return currentRegion.get();
    }

    public static void setCurrentRegion(String region) {
        currentRegion.set(region);
    }

    public static void clear() {
        currentRegion.remove();
    }

}
