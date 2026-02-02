package com.coinlockedplugin.save;

import com.coinlockedplugin.data.PackChoiceState;
import com.coinlockedplugin.data.SetupStage;
import com.coinlockedplugin.pack.PackOption;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static net.runelite.client.RuneLite.RUNELITE_DIR;

@Singleton
public class SaveStorage {
    private static final String FILE_NAME = "save.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path baseDir;

    @Inject
    public SaveStorage() {
        this.baseDir = RUNELITE_DIR.toPath().resolve("coinlockedPlugin");
    }

    public SaveData load(String accountKey) {
        Path file = getSaveFile(accountKey);
        if (!Files.exists(file))
            return new SaveData();

        try (Reader r = Files.newBufferedReader(file)) {
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(r);
            if (!root.isJsonObject())
                return new SaveData();

            return deserializeSaveData(root.getAsJsonObject());
        } catch (Exception e) {
            return new SaveData();
        }
    }

    //Save parse from Gson to SaveData with safety for missing fields (instead of just wrecking the whole load)
    private SaveData deserializeSaveData(JsonObject obj) {
        SaveData data = new SaveData();

        // Versioning for if I want to change the save format later
        safe(() -> data.version = obj.get("version").getAsInt());

        safe(() -> data.packChoiceState = gson.fromJson(obj.get("packChoiceState"), PackChoiceState.class));

        safe(() -> data.illegalXPGained = obj.get("illegalXPGained").getAsLong());
        safe(() -> data.packsBought = obj.get("packsBought").getAsInt());
        safe(() -> data.peakWealth = obj.get("peakWealth").getAsLong());
        safe(() -> data.setupStage = gson.fromJson(obj.get("setupStage"), SetupStage.class));

        Type setStringType = new TypeToken<Set<String>>() {
        }.getType();
        safe(() -> data.unlockedIds = gson.fromJson(obj.get("unlockedIds"), setStringType));

        safe(() -> {
            Type t = new TypeToken<List<String>>(){}.getType();
            data.currentPackOptionIds = gson.fromJson(obj.get("currentPackOptionIds"), t);
        });

        safe(() -> data.lastUpdatedEpochMs = obj.get("lastUpdatedEpochMs").getAsLong());

        if (data.unlockedIds == null) data.unlockedIds = new HashSet<>();
        if (data.currentPackOptionIds == null) data.currentPackOptionIds = new ArrayList<>();

        return data;
    }

    private static void safe(Runnable r) {
        try {
            r.run();
        } catch (Exception ignored) {
        }
    }

    public void save(String accountKey, SaveData data) {
        if (accountKey == null || accountKey.isBlank() || data == null)
            return;

        data.lastUpdatedEpochMs = System.currentTimeMillis();

        Path dir = baseDir.resolve(accountKey);
        Path file = dir.resolve(FILE_NAME);
        Path tmp = dir.resolve(FILE_NAME + ".tmp");

        try {
            Files.createDirectories(dir);

            try (Writer w = Files.newBufferedWriter(tmp)) {
                gson.toJson(data, w);
            }

            try {
                Files.move(tmp, file,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            // log if you want
        }
    }

    private Path getSaveFile(String accountKey) {
        return baseDir.resolve(accountKey).resolve(FILE_NAME);
    }
}