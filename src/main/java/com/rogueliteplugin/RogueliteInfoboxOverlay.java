package com.rogueliteplugin;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.ProgressBarComponent;

import java.awt.*;

public class RogueliteInfoboxOverlay extends Overlay {
    private final RoguelitePlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    public RogueliteInfoboxOverlay(RoguelitePlugin plugin) {
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        panelComponent.setPreferredSize(new Dimension(250, 0));

        long currentProgress = 1;
        long challengeGoal = 1;

        if (plugin.anyChallengeActive()) {
            currentProgress = plugin.getCurrentChallengeProgress();
            challengeGoal = plugin.getCurrentChallengeGoal();
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Current challenge")
                    .right("Progress")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(plugin.getCurrentChallengeFormatted())
                    .right(currentProgress + "")
                    .build());
        } else {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Challenge complete! You can get a new card pack in the panel.")
                    .build());
        }

        panelComponent.getChildren().add(LineComponent.builder().build());

        ProgressBarComponent bar = new ProgressBarComponent();
        bar.setMinimum(0);
        bar.setMaximum(challengeGoal);
        bar.setValue(currentProgress);
        bar.setPreferredSize(new Dimension(220, 12));
        if (currentProgress >= challengeGoal)
            bar.setForegroundColor(new Color(0, 170, 0));
        else
            bar.setForegroundColor(new Color(255, 152, 31));

        panelComponent.getChildren().add(bar);

        return panelComponent.render(graphics);
    }
}
