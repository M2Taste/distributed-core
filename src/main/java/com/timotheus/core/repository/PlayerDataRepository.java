package com.timotheus.core.repository;

import com.timotheus.core.model.PlayerData;

import java.util.Optional;
import java.util.UUID;

public interface PlayerDataRepository {

    Optional<PlayerData> load(UUID uuid);

    void save(PlayerData data);
}