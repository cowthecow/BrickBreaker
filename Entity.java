package com.laserinfinite.java;

import java.awt.*;

public interface Entity {

    boolean lifeOver();
    void update();
    void draw(Graphics2D g);
}
