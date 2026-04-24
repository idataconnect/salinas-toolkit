/*
 * Casciian - Java Text User Interface
 *
 * Written 2013-2025 by Autumn Lamonte
 *
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software to the
 * public domain worldwide. This software is distributed without any
 * warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along
 * with this software. If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.idataconnect.salinas.toolkit.backend;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Wrapper for integrating with Swing, because JFrame and JComponent have
 * separate hierarchies.
 */
public class SwingComponent {

    // ------------------------------------------------------------------------
    // Variables --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * If true, use triple buffering when drawing to a JFrame.
     */
    public static boolean tripleBuffer = true;

    /**
     * The frame reference, if we are drawing to a JFrame.
     */
    private JFrame frame;

    /**
     * The component reference, if we are drawing to a JComponent.
     */
    private JComponent component;

    /**
     * An optional border in pixels to add.
     */
    private static final int BORDER = 1;

    /**
     * Adjustable Insets for this component.  This has the effect of adding a
     * black border around the drawing area.
     */
    Insets adjustInsets = null;

    // ------------------------------------------------------------------------
    // Constructors -----------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Construct using a JFrame.
     *
     * @param frame the JFrame to draw to
     */
    @SuppressWarnings("this-escape")
    public SwingComponent(final JFrame frame) {
        this.frame = frame;
        adjustInsets = new Insets(BORDER, BORDER, BORDER, BORDER);
        setupFrame();
    }

    /**
     * Construct using a JComponent.
     *
     * @param component the JComponent to draw to
     */
    @SuppressWarnings("this-escape")
    public SwingComponent(final JComponent component) {
        this.component = component;
        adjustInsets = new Insets(BORDER, BORDER, BORDER, BORDER);
        setupComponent();
    }

    /**
     * Construct using both a JFrame and a JComponent.
     *
     * @param frame the JFrame to draw to
     * @param component the JComponent to draw to
     */
    @SuppressWarnings("this-escape")
    public SwingComponent(final JFrame frame, final JComponent component) {
        this.frame = frame;
        this.component = component;
        adjustInsets = new Insets(BORDER, BORDER, BORDER, BORDER);
        setupFrame();
        setupComponent();
    }

    // ------------------------------------------------------------------------
    // SwingComponent ---------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Get the BufferStrategy object needed for triple-buffering.
     *
     * @return the BufferStrategy
     * @throws IllegalArgumentException if this function is called when
     * not rendering to a JFrame
     */
    public BufferStrategy getBufferStrategy() {
        if (frame != null) {
            return frame.getBufferStrategy();
        } else {
            throw new IllegalArgumentException("BufferStrategy not used " +
                "for JComponent access");
        }
    }

    /**
     * Get the JFrame reference.
     *
     * @return the frame, or null if this is drawing to a JComponent
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Get the JComponent reference.
     *
     * @return the component, or null if this is drawing to a JFrame
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * Setup to render to an existing JComponent.
     */
    public final void setupComponent() {
        component.setBackground(Color.black);

        if (System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseImage") != null) {
            component.setCursor(getMouseImage());
        } else if (System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseStyle") != null) {
            setMouseStyle(System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseStyle"));
        } else if (System.getProperty("com.idataconnect.salinas.toolkit.textMouse",
                "true").equals("false")
        ) {
            // If the user has suppressed the text mouse, don't kill the X11
            // mouse.
            component.setCursor(Cursor.getPredefinedCursor(
                Cursor.DEFAULT_CURSOR));
        } else {
            // Kill the X11 cursor
            // Transparent 16 x 16 pixel cursor image.
            BufferedImage cursorImg = new BufferedImage(16, 16,
                BufferedImage.TYPE_INT_ARGB);
            // Create a new blank cursor.
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
            component.setCursor(blankCursor);
        }

        // Be capable of seeing Tab / Shift-Tab
        component.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Setup to render to an existing JFrame.
     */
    public final void setupFrame() {
        frame.setTitle("Console Application");
        frame.setBackground(Color.black);
        frame.pack();

        if (System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseImage") != null) {
            frame.setCursor(getMouseImage());
        } else if (System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseStyle") != null) {
            setMouseStyle(System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseStyle"));
        } else if (System.getProperty("com.idataconnect.salinas.toolkit.textMouse",
                "true").equals("false")
        ) {
            // If the user has suppressed the text mouse, don't kill the X11
            // mouse.
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            // Kill the X11 cursor
            // Transparent 16 x 16 pixel cursor image.
            BufferedImage cursorImg = new BufferedImage(16, 16,
                BufferedImage.TYPE_INT_ARGB);
            // Create a new blank cursor.
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
            frame.setCursor(blankCursor);
        }

        // Be capable of seeing Tab / Shift-Tab
        frame.setFocusTraversalKeysEnabled(false);

        // Setup triple-buffering
        if (tripleBuffer) {
            frame.setIgnoreRepaint(true);
            frame.createBufferStrategy(3);
        }
    }

    /**
     * Load an image named in com.idataconnect.salinas.toolkit.Swing.mouseImage as the mouse cursor.
     * The image must be on the classpath.
     *
     * @return the cursor
     */
    private Cursor getMouseImage() {
        Cursor cursor = Cursor.getDefaultCursor();
        String filename = System.getProperty("com.idataconnect.salinas.toolkit.Swing.mouseImage");
        assert (filename != null);

        try {
            ClassLoader loader = Thread.currentThread().
                getContextClassLoader();

            java.net.URL url = loader.getResource(filename);
            if (url == null) {
                // User named a file, but it's not on the classpath.  Bail
                // out.
                return cursor;
            }

            BufferedImage cursorImage = ImageIO.read(url);
            java.awt.Dimension cursorSize = Toolkit.getDefaultToolkit().
                getBestCursorSize(
                        cursorImage.getWidth(), cursorImage.getHeight());

            cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                new Point((int) Math.min(cursorImage.getWidth() / 2,
                        cursorSize.getWidth() - 1),
                    (int) Math.min(cursorImage.getHeight() / 2,
                        cursorSize.getHeight() - 1)),
                "custom cursor");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cursor;
    }

    /**
     * Set the mouse pointer (cursor) style.
     *
     * @param mouseStyle the mouse pointer style string, one of: "default",
     * "none", "hand", "text", "move", or "crosshair"
     */
    public void setMouseStyle(final String mouseStyle) {
        assert (mouseStyle != null);

        String styleLower = mouseStyle.toLowerCase();

        Cursor cursor = Cursor.getDefaultCursor();

        if (styleLower.equals("none")) {
            // Transparent 16 x 16 pixel cursor image.
            BufferedImage cursorImg = new BufferedImage(16, 16,
                BufferedImage.TYPE_INT_ARGB);
            // Create a new blank cursor.
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        } else if (styleLower.equals("default")) {
            cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        } else if (styleLower.equals("hand")) {
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        } else if (styleLower.equals("text")) {
            cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
        } else if (styleLower.equals("move")) {
            cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        } else if (styleLower.equals("crosshair")) {
            cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        }

        if (frame != null) {
            frame.setCursor(cursor);
        }
        if (component != null) {
            component.setCursor(cursor);
        }
    }

    /**
     * Set the window title.
     *
     * @param title the new title
     */
    public void setTitle(final String title) {
        if (frame != null) {
            frame.setTitle(title);
        }
    }

    /**
     * Paints this component.
     *
     * @param g the graphics context to use for painting
     */
    public void paint(Graphics g) {
        if (component != null) {
            component.paint(g);
        } else {
            frame.paint(g);
        }
    }

    /**
     * Repaints this component.
     */
    public void repaint() {
        if (component != null) {
            component.repaint();
        } else {
            frame.repaint();
        }
    }

    /**
     * Repaints the specified rectangle of this component.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    public void repaint(int x, int y, int width, int height) {
        if (component != null) {
            component.repaint(x, y, width, height);
        } else {
            frame.repaint(x, y, width, height);
        }
    }

    /**
     * If a border has been set on this component, returns the border's
     * insets; otherwise calls super.getInsets.
     *
     * @return the value of the insets property
     */
    public Insets getInsets() {
        Insets swingInsets = null;
        if (component != null) {
            swingInsets = component.getInsets();
        } else {
            swingInsets = frame.getInsets();
        }
        Insets result = new Insets(swingInsets.top + adjustInsets.top,
            swingInsets.left + adjustInsets.left,
            swingInsets.bottom + adjustInsets.bottom,
            swingInsets.right + adjustInsets.right);
        return result;
    }

    /**
     * Returns the current width of this component.
     *
     * @return the current width of this component
     */
    public int getWidth() {
        if (component != null) {
            return component.getWidth();
        }
        return frame.getWidth();
    }

    /**
     * Returns the current height of this component.
     *
     * @return the current height of this component
     */
    public int getHeight() {
        if (component != null) {
            return component.getHeight();
        }
        return frame.getHeight();
    }

    /**
     * Gets the font of this component.
     *
     * @return this component's font; if a font has not been set for this
     * component, the font of its parent is returned
     */
    public Font getFont() {
        if (component != null) {
            return component.getFont();
        }
        return frame.getFont();
    }

    /**
     * Sets the font of this component.
     *
     * @param f the font to become this component's font; if this parameter
     * is null then this component will inherit the font of its parent
     */
    public void setFont(final Font f) {
        if (component != null) {
            component.setFont(f);
        } else {
            frame.setFont(f);
        }
    }

    /**
     * Shows or hides this Window depending on the value of parameter b.
     *
     * @param b if true, make visible, else make invisible
     */
    public void setVisible(final boolean b) {
        if (frame != null) {
            frame.setVisible(b);
        } else {
            component.setVisible(b);
        }
    }

    /**
     * Creates a graphics context for this component. This method will return
     * null if this component is currently not displayable.
     *
     * @return a graphics context for this component, or null if it has none
     */
    public Graphics getGraphics() {
        if (component != null) {
            return component.getGraphics();
        }
        return frame.getGraphics();
    }

    /**
     * Releases all of the native screen resources used by this Window, its
     * subcomponents, and all of its owned children. That is, the resources
     * for these Components will be destroyed, any memory they consume will
     * be returned to the OS, and they will be marked as undisplayable.
     */
    public void dispose() {
        if (frame != null) {
            frame.dispose();
        } else {
            component.getParent().remove(component);
        }
    }

    /**
     * Resize the component to match the font dimensions.
     *
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    public final void setDimensions(final int width, final int height) {
        if (SwingUtilities.isEventDispatchThread()) {
            setDimensionsImpl(width, height);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setDimensionsImpl(width, height);
                }
            });
        }
    }

    /**
     * Resize the component to match the font dimensions.
     *
     * @param width the new width in pixels
     * @param height the new height in pixels
     */
    private void setDimensionsImpl(final int width, final int height) {
        // Figure out the thickness of borders and use that to set the
        // final size.
        if (component != null) {
            component.setPreferredSize(new java.awt.Dimension(
                width + adjustInsets.left + adjustInsets.right,
                height + adjustInsets.top + adjustInsets.bottom));
            if (frame != null) {
                frame.pack();
            } else {
                component.setSize(width + adjustInsets.left + adjustInsets.right,
                    height + adjustInsets.top + adjustInsets.bottom);
            }
        } else if (frame != null) {
            Insets insets = frame.getInsets();
            frame.setSize(width + insets.left + insets.right +
                adjustInsets.left + adjustInsets.right,
                height + insets.top + insets.bottom +
                adjustInsets.top + adjustInsets.bottom);
        }
    }

    /**
     * Adds the specified component listener to receive component events from
     * this component. If listener l is null, no exception is thrown and no
     * action is performed.
     *
     * @param l the component listener
     */
    public void addComponentListener(ComponentListener l) {
        if (component != null) {
            component.addComponentListener(l);
        } else {
            frame.addComponentListener(l);
        }
    }

    /**
     * Adds the specified focus listener to receive focus events from this
     * focus. If listener l is null, no exception is thrown and no action is
     * performed.
     *
     * @param l the focus listener
     */
    public void addFocusListener(FocusListener l) {
        if (component != null) {
            component.addFocusListener(l);
        } else {
            frame.addFocusListener(l);
        }
    }

    /**
     * Adds the specified key listener to receive key events from this
     * component. If l is null, no exception is thrown and no action is
     * performed.
     *
     * @param l the key listener.
     */
    public void addKeyListener(KeyListener l) {
        if (component != null) {
            component.addKeyListener(l);
        } else {
            frame.addKeyListener(l);
        }
    }

    /**
     * Adds the specified mouse listener to receive mouse events from this
     * component. If listener l is null, no exception is thrown and no action
     * is performed.
     *
     * @param l the mouse listener
     */
    public void addMouseListener(MouseListener l) {
        if (component != null) {
            component.addMouseListener(l);
        } else {
            frame.addMouseListener(l);
        }
    }

    /**
     * Adds the specified mouse motion listener to receive mouse motion
     * events from this component. If listener l is null, no exception is
     * thrown and no action is performed.
     *
     * @param l the mouse motion listener
     */
    public void addMouseMotionListener(MouseMotionListener l) {
        if (component != null) {
            component.addMouseMotionListener(l);
        } else {
            frame.addMouseMotionListener(l);
        }
    }

    /**
     * Adds the specified mouse wheel listener to receive mouse wheel events
     * from this component. Containers also receive mouse wheel events from
     * sub-components.
     *
     * @param l the mouse wheel listener
     */
    public void addMouseWheelListener(MouseWheelListener l) {
        if (component != null) {
            component.addMouseWheelListener(l);
        } else {
            frame.addMouseWheelListener(l);
        }
    }

    /**
     * Adds the specified window listener to receive window events from this
     * window. If l is null, no exception is thrown and no action is
     * performed.
     *
     * @param l the window listener
     */
    public void addWindowListener(WindowListener l) {
        if (frame != null) {
            frame.addWindowListener(l);
        }
    }

    /**
     * Requests that this Component get the input focus, if this Component's
     * top-level ancestor is already the focused Window.
     */
    public void requestFocusInWindow() {
        if (component != null) {
            component.requestFocusInWindow();
        } else {
            frame.requestFocusInWindow();
        }
    }

}
