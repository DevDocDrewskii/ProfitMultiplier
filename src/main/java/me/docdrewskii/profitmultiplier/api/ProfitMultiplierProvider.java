package me.docdrewskii.profitmultiplier.api;

public final class ProfitMultiplierProvider {

    private static ProfitMultiplierAPI api;

    private ProfitMultiplierProvider() {
    }

    public static ProfitMultiplierAPI get() {
        if (api == null) {
            throw new IllegalStateException("ProfitMultiplier API is not initialized. "
                    + "Ensure ProfitMultiplier is enabled and declared as a (soft)depend.");
        }
        return api;
    }

    public static boolean isAvailable() {
        return api != null;
    }

    public static void register(ProfitMultiplierAPI implementation) {
        api = implementation;
    }

    public static void unregister() {
        api = null;
    }
}
