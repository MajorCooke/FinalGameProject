package javagames.util;

import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

/**
 * This class handles the attributes and behaviors for mouse input.
 *
 * @author Timothy Wright
 *
 */
public class RelativeMouseInput
    implements MouseListener, MouseMotionListener, MouseWheelListener {

  private static final int BUTTON_COUNT = 3;

  private Point           mousePos;
  private Point           currentPos;
  private final boolean[] mouse;
  private final int[]     polled;
  private int             notches;
  private int             polledNotches;

  private int             dx, dy;
  private Robot           robot;
  private final Component component;
  private boolean         relative;

  public RelativeMouseInput(final Component component) {

    this.component = component;
    try {
      robot = new Robot();
    }
    catch (final Exception e) {
      // Handle exception [game specific]
      e.printStackTrace();
    }

    mousePos = new Point(0, 0);
    currentPos = new Point(0, 0);
    mouse = new boolean[RelativeMouseInput.BUTTON_COUNT];
    polled = new int[RelativeMouseInput.BUTTON_COUNT];
  }

  public synchronized void poll() {

    if (isRelative()) {
      mousePos = new Point(dx, dy);
    }
    else {
      mousePos = new Point(currentPos);
    }
    dx = dy = 0;

    polledNotches = notches;
    notches = 0;

    for (int i = 0; i < mouse.length; ++i) {
      if (mouse[i]) {
        polled[i]++;
      }
      else {
        polled[i] = 0;
      }
    }
  }

  public boolean isRelative() {
    return relative;
  }

  public void setRelative(final boolean relative) {
    this.relative = relative;
    if (relative) {
      centerMouse();
    }
  }

  public Point getPosition() {
    return mousePos;
  }

  public int getNotches() {
    return polledNotches;
  }

  public boolean buttonDown(final int button) {
    return polled[button - 1] > 0;
  }

  public boolean buttonDownOnce(final int button) {
    return polled[button - 1] == 1;
  }

  @Override
  public synchronized void mousePressed(final MouseEvent e) {
    final int button = e.getButton() - 1;
    if ((button >= 0) && (button < mouse.length)) {
      mouse[button] = true;
    }
  }

  @Override
  public synchronized void mouseReleased(final MouseEvent e) {
    final int button = e.getButton() - 1;
    if ((button >= 0) && (button < mouse.length)) {
      mouse[button] = false;
    }
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    // Not needed
  }

  @Override
  public synchronized void mouseEntered(final MouseEvent e) {
    mouseMoved(e);
  }

  @Override
  public synchronized void mouseExited(final MouseEvent e) {
    mouseMoved(e);
  }

  @Override
  public synchronized void mouseDragged(final MouseEvent e) {
    mouseMoved(e);
  }

  @Override
  public synchronized void mouseMoved(final MouseEvent e) {
    if (isRelative()) {
      final Point p = e.getPoint();
      final Point center = getComponentCenter();
      dx += p.x - center.x;
      dy += p.y - center.y;
      centerMouse();
    }
    else {
      currentPos = e.getPoint();
    }
  }

  @Override
  public synchronized void mouseWheelMoved(final MouseWheelEvent e) {
    notches += e.getWheelRotation();
  }

  private Point getComponentCenter() {
    final int w = component.getWidth();
    final int h = component.getHeight();
    return new Point(w / 2, h / 2);
  }

  private void centerMouse() {
    if ((robot != null) && component.isShowing()) {
      final Point center = getComponentCenter();
      SwingUtilities.convertPointToScreen(center, component);
      robot.mouseMove(center.x, center.y);
    }
  }

}
