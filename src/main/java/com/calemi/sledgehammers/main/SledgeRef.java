package com.calemi.sledgehammers.main;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SledgeRef {

    public static final String NAME = "Sledgehammers";
    public static final String ID = "sledgehammers";

    public static final ResourceLocation HAMMER_OVERLAY = ResourceLocation.fromNamespaceAndPath(SledgeRef.ID, "textures/gui/hammer_overlay.png");

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static <T> ResourceKey<T> createKey(String name, ResourceKey<? extends Registry<T>> registryKey) {
        return ResourceKey.create(registryKey, rl(name));
    }
}