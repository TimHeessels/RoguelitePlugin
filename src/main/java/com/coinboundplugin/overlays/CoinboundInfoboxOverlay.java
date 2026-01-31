package com.coinboundplugin.overlays;

import com.coinboundplugin.CoinboundPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;

import java.awt.*;

public class CoinboundInfoboxOverlay extends Overlay {
    private final CoinboundPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    public CoinboundInfoboxOverlay(CoinboundPlugin plugin) {
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panelComponent.setPreferredSize(new Dimension(250, 0));

        //Display welcome message on first launch
        if (plugin.gamemodeSetupState == CoinboundPlugin.SetupStage.DropAllItems) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Welcome to the Coinbound game mode. Please drop all items you got from tutorial island.")
                    .build());
            return panelComponent.render(graphics);
        }
        //Go fill up inventory with flyers
        if (plugin.gamemodeSetupState == CoinboundPlugin.SetupStage.GetFlyers || plugin.fillerItemsShort > 0) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Please go to the Al Kharid flyerer and use the drop-trick to get " + plugin.fillerItemsShort + " more flyers to fill up your inventory.")
                    .build());
            return panelComponent.render(graphics);
        }
        if (plugin.fillerItemsShort < 0) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("You can drop " + Math.abs(plugin.fillerItemsShort) + " flyers as you have too many.")
                    .build());
        }
        long currentCoins = plugin.currentCoins;
        int packsBought = plugin.getPackBought();
        int availablePacks = plugin.getAvailablePacksToBuy();

        int targetTier = packsBought + availablePacks + 1;

        long previous = (targetTier <= 1) ? 0 : plugin.peakCoinsRequiredForPack(targetTier - 1);
        long next = plugin.peakCoinsRequiredForPack(targetTier);

        long barGoal = Math.max(1, next - previous);
        long barProgress = Math.max(0, currentCoins - previous);

        if (plugin.getAvailablePacksToBuy() > 1) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("You have " + plugin.getAvailablePacksToBuy() + " packs available to buy! Press the button at the top of the screen to open them.")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder().build());
        }
        if (plugin.getAvailablePacksToBuy() == 1) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("You have a pack available to buy! Press the button at the top of the screen to open it.")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder().build());
        }

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Next pack at")
                .right(next + " gp")
                .build());

        panelComponent.getChildren().add(LineComponent.builder().build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Progress towards next pack")
                .build());
        ProgressBarComponent bar = new ProgressBarComponent();
        bar.setMinimum(0);
        bar.setMaximum(barGoal);
        bar.setValue(barProgress);
        bar.setPreferredSize(new Dimension(220, 12));
        if (barProgress >= barGoal)
            bar.setForegroundColor(new Color(0, 170, 0));
        else
            bar.setForegroundColor(new Color(255, 152, 31));

        panelComponent.getChildren().add(bar);

        return panelComponent.render(graphics);
    }
}
