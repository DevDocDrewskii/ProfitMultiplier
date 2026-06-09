package me.docdrewskii.profitmultiplier.api;

import org.bukkit.Material;

import java.util.Map;
import java.util.UUID;

public interface ProfitMultiplierAPI {

    long getSold(UUID playerId, Material material);

    long getTotalSold(UUID playerId);

    Map<Material, Long> getAllSold(UUID playerId);

    double getMultiplier(UUID playerId, Material material);

    double getMultiplierAt(Material material, long soldCount);

    long getActiveThreshold(UUID playerId, Material material);

    long getNextThreshold(UUID playerId, Material material);

    long getRemainingToNextThreshold(UUID playerId, Material material);

    double getBonusTotal(UUID playerId);

    double getLastBonus(UUID playerId);

    long addSold(UUID playerId, Material material, int amount);

    void setSold(UUID playerId, Material material, long amount);

    void addBonus(UUID playerId, double amount);

    boolean resetPlayer(UUID playerId);

    int resetAll();

    long getLastReset();

    double calculateSaleValue(Material material, long previousTotal, int amount, double basePerUnit);
}
