package lynk.dev;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Playertracker implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("playertracker");

	private static final Path DATA_PATH = Paths.get("unique_players.txt");

	public static Set<String> uniquePlayers = loadPlayerData();
	public static String[][] players = {};

	private static Set<String> loadPlayerData() {
		Set<String> uniquePlayers = new HashSet<>();
		try {
			if (!Files.exists(DATA_PATH)) {
				Files.createFile(DATA_PATH);
			}
			List<String> allLines = Files.readAllLines(DATA_PATH);
			uniquePlayers.addAll(allLines);
		} catch (IOException e) {
			e.printStackTrace();
			// Handle error or create the file if it doesn't exist
		}
		return uniquePlayers;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Playertracker: Hello Fabric world!");

		ModRegistries.registerModStuffs();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;
			String playerUuid = player.getUuidAsString();
			// Check if the player is already in the set of unique players
			if(!uniquePlayers.contains(playerUuid)){
				uniquePlayers.add(playerUuid);

				try {
					Files.write(DATA_PATH, uniquePlayers);
				} catch (IOException e) {
					e.printStackTrace();
					// Handle failure to write to the file
				}
			}
		});
	}
}