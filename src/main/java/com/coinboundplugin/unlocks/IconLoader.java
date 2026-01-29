package com.coinboundplugin.unlocks;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.ImageUtil;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

@Slf4j
public final class IconLoader
{
    private static final String FALLBACK_ICON = "/icons/notFound.png";

    private IconLoader() {}

    public static ImageIcon load(String name)
    {
        String path = "/icons/" + name;
        BufferedImage img = null;

        try
        {
            img = ImageUtil.loadImageResource(IconLoader.class, path);
        }
        catch (Exception e)
        {
            log.warn("Failed to load icon: {}", path, e);
        }

        if (img == null)
        {
            log.warn("Icon not found: {}, using fallback", path);
            try
            {
                img = ImageUtil.loadImageResource(IconLoader.class, FALLBACK_ICON);
            }
            catch (Exception e)
            {
                log.error("Failed to load fallback icon: {}", FALLBACK_ICON, e);
            }
        }

        return img == null ? null : new ImageIcon(img);
    }
}