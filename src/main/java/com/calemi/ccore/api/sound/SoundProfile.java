package com.calemi.ccore.api.sound;

import com.calemi.ccore.api.math.MathHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.Random;

public class SoundProfile {

    private final Random random = new Random();

    private final SoundEvent soundEvent;
    private SoundSource soundSource = SoundSource.AMBIENT;
    private float minVolume = 1;
    private float maxVolume = 1;
    private float minPitch = 1;
    private float maxPitch = 1;

    public SoundProfile(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    public SoundProfile setSource(SoundSource soundSource) {
        this.soundSource = soundSource;
        return this;
    }

    public SoundProfile setVolume(float minVolume, float maxVolume) {
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
        return this;
    }

    public SoundProfile setVolume(float volume) {
        return setVolume(volume, volume);
    }

    public SoundProfile setPitch(float minPitch, float maxPitch) {
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        return this;
    }

    public SoundProfile setPitch(float pitch) {
        return setPitch(pitch, pitch);
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public SoundSource getSoundSource() {
        return soundSource;
    }

    public float getVolume() {
        if (minVolume == maxVolume) return minVolume;
        return MathHelper.randomRange(minVolume, maxVolume);
    }

    public float getPitch() {
        if (minPitch == maxPitch) return minPitch;
        return MathHelper.randomRange(minPitch, maxPitch);
    }
}