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
package demo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.awt.Font;

import com.idataconnect.salinas.toolkit.TAction;
import com.idataconnect.salinas.toolkit.TApplication;
import com.idataconnect.salinas.toolkit.TEditorWindow;
import com.idataconnect.salinas.toolkit.TLabel;
import com.idataconnect.salinas.toolkit.TTimer;
import com.idataconnect.salinas.toolkit.TWidget;
import com.idataconnect.salinas.toolkit.TWindow;
import com.idataconnect.salinas.toolkit.bits.Animation;
import com.idataconnect.salinas.toolkit.bits.ImageUtils;
import com.idataconnect.salinas.toolkit.effect.GradientCellTransform;
import com.idataconnect.salinas.toolkit.event.TCommandEvent;
import com.idataconnect.salinas.toolkit.layout.StretchLayoutManager;
import com.idataconnect.salinas.toolkit.tackboard.Bitmap;
import com.idataconnect.salinas.toolkit.tackboard.MousePointer;
import com.idataconnect.salinas.toolkit.tackboard.TackboardItem;
import com.idataconnect.salinas.toolkit.tackboard.Text;
import static com.idataconnect.salinas.toolkit.TCommand.*;
import static com.idataconnect.salinas.toolkit.TKeypress.*;

/**
 * Pixel-based operations.
 */
public class DemoPixelsWindow extends TWindow {

    // ------------------------------------------------------------------------
    // Variables --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Translated strings.
     */
    private ResourceBundle i18n = null;

    /**
     * Timer that moves things.
     */
    private TTimer timer3;

    /**
     * Timer label is updated with timer ticks.
     */
    TLabel timerLabel;

    /**
     * Direction for the bitmaps to move.
     */
    boolean direction = true;

    /**
     * The floating text.
     */
    Text floatingText = null;

    // ------------------------------------------------------------------------
    // Constructors -----------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Public constructor.
     *
     * @param parent the main application
     */
    @SuppressWarnings("this-escape")
    public DemoPixelsWindow(final TApplication parent) {
        // Construct a demo window.  X and Y don't matter because it will be
        // centered on screen.
        super(parent, "", 0, 0, 72, 17, CENTERED | RESIZABLE);
        i18n = ResourceBundle.getBundle(DemoPixelsWindow.class.getName(),
            getLocale());
        setTitle(i18n.getString("windowTitle"));

        setLayoutManager(new StretchLayoutManager(getWidth() - 2,
                getHeight() - 2));

        int row = 1;
        int col = 43;

        // Add some widgets
        addLabel(i18n.getString("customMouseLabel"), 1, row);
        TWidget first = addButton(i18n.getString("customMouseButton"), col, row,
            new TAction() {
                public void DO() {
                    TackboardItem mouse = getApplication().getCustomMousePointer();
                    if (mouse != null) {
                        // Turn it off.
                        getApplication().setCustomMousePointer(null);
                    } else {
                        // Demo icon removed due to licensing.
                        messageBox(i18n.getString("windowTitle"), "Demo icon removed for licensing compliance.");
                    }
                }
            }
        );
        row += 2;

        addLabel(i18n.getString("floatingTextLabel"), 1, row);
        addButton(i18n.getString("floatingTextButton"), col, row,
            new TAction() {
                public void DO() {
                    if (floatingText == null) {
                        int fontSize = 31;
                        // Use system font instead of deleted 5th Grade Cursive
                        Font font = new Font(Font.SANS_SERIF, Font.ITALIC, fontSize);

                        floatingText = new Text(30, 21, 2,
                            i18n.getString("heatFromFire"), font, fontSize,
                            new java.awt.Color(0xF7, 0xA8, 0xB8));
                        addOverlay(floatingText);
                    } else {
                        floatingText.remove();
                        floatingText = null;
                    }
                }
            }
        );
        row += 2;

        addLabel(i18n.getString("textField1"), 1, row);
        TWidget field = addField(col, row, 15, false, "Field text");
        row += 2;

        // TODO: more things

        // Put some floating things on the screen.
        try {
            // Demo animations and icons removed for licensing compliance.

            timer3 = getApplication().addTimer(100, true,
                new TAction() {
                    public void DO() {
                        // System.err.println("Pixels: tick");

                        List<TackboardItem> items;
                        items = new ArrayList<TackboardItem>();
                        if (underlay != null) {
                            items.addAll(underlay.getItems());
                        }
                        if (overlay != null) {
                            items.addAll(overlay.getItems());
                        }
                        int i = 0;
                        for (TackboardItem item: items) {
                            if (item instanceof Text) {
                                continue;
                            }

                            i++;
                            int x = item.getX();
                            int y = item.getY();
                            if (i % 2 == 0) {
                                if (direction) {
                                    item.setX(x + 1);
                                } else {
                                    item.setX(x - 1);
                                }
                            } else {
                                if (direction) {
                                    item.setY(y + 1);
                                } else {
                                    item.setY(y - 1);
                                }
                            }
                            if ((item.getX() < 0)
                                || (item.getX() > 300)
                                || (item.getY() < 0)
                                || (item.getY() > 200)
                            ) {
                                direction = !direction;
                            }
                        }
                    }
                }
            );
        } catch (Exception e) {
            new com.idataconnect.salinas.toolkit.TExceptionDialog(getApplication(), e);
        }

        activate(first);

        statusBar = newStatusBar(i18n.getString("statusBar"));
        statusBar.addShortcutKeypress(kbF1, cmHelp,
            i18n.getString("statusBarHelp"));
        statusBar.addShortcutKeypress(kbF2, cmShell,
            i18n.getString("statusBarShell"));
        statusBar.addShortcutKeypress(kbF3, cmOpen,
            i18n.getString("statusBarOpen"));
        statusBar.addShortcutKeypress(kbF10, cmExit,
            i18n.getString("statusBarExit"));
    }

    // ------------------------------------------------------------------------
    // TWindow ----------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * We need to override onClose so that the timer will no longer be called
     * after we close the window.  TTimers currently are completely unaware
     * of the rest of the UI classes.
     */
    @Override
    public void onClose() {
        super.onClose();
        // Just in case, make sure timer stops ticking.
        if (timer3 != null) {
            timer3.setRecurring(false);
        }
        getApplication().removeTimer(timer3);
    }

    /**
     * Method that subclasses can override to handle posted command events.
     *
     * @param command command event
     */
    @Override
    public void onCommand(final TCommandEvent command) {
        if (command.equals(cmOpen)) {
            try {
                String filename = fileOpenBox(".");
                if (filename != null) {
                    try {
                        new TEditorWindow(getApplication(),
                            new File(filename));
                    } catch (IOException e) {
                        messageBox(i18n.getString("errorTitle"),
                            MessageFormat.format(i18n.
                                getString("errorReadingFile"), e.getMessage()));
                    }
                }
            } catch (IOException e) {
                        messageBox(i18n.getString("errorTitle"),
                            MessageFormat.format(i18n.
                                getString("errorOpeningFile"), e.getMessage()));
            }
            return;
        }

        // Didn't handle it, let children get it instead
        super.onCommand(command);
    }

    /**
     * Enable or disable a pre-defined gradient for this window's color.
     *
     * @param useGradient if true, paint this window with a gradient
     */
    public void setUseGradient(final boolean useGradient) {
        if (useGradient) {
            Color PINK = new Color(0xf7, 0xa8, 0xb8);
            Color BLUE = new Color(0x55, 0xcd, 0xfc);
            setDrawPreTransform(new GradientCellTransform(
                GradientCellTransform.Layer.BACKGROUND, BLUE, BLUE,
                PINK, PINK));
        } else {
            setDrawPreTransform(null);
        }
    }

}
