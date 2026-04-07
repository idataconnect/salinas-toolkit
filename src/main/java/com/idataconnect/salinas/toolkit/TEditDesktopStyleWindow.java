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
package com.idataconnect.salinas.toolkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import com.idataconnect.salinas.toolkit.TButton;
import com.idataconnect.salinas.toolkit.TComboBox;
import com.idataconnect.salinas.toolkit.bits.BorderStyle;
import com.idataconnect.salinas.toolkit.bits.CellAttributes;
import com.idataconnect.salinas.toolkit.event.TKeypressEvent;
import com.idataconnect.salinas.toolkit.event.TMouseEvent;
import static com.idataconnect.salinas.toolkit.TKeypress.*;

/**
 * TEditDesktopStyleWindow provides an easy UI for users to alter the running
 * border styles and button style.
 *
 */
public class TEditDesktopStyleWindow extends TWindow {

    // ------------------------------------------------------------------------
    // Variables --------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Translated strings.
     */
    private ResourceBundle i18n = null;

    /**
     * The left-side list of border names pane.
     */
    private TList borderNames;

    /**
     * The selected choice for border style.
     */
    private TComboBox borderStyle;

    /**
     * The style to show for the selected border name.
     */
    private BorderStyle shownBorderStyle = BorderStyle.NONE;

    /**
     * The border styles being edited.
     */
    private Properties editBorderStyles = new Properties();

    /**
     * The selected choice for button style.
     */
    private TComboBox buttonStyle;

    /**
     * Example button 1.
     */
    private TButton button1;

    /**
     * Example button 2.
     */
    private TButton button2;

    // ------------------------------------------------------------------------
    // Constructors -----------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Public constructor.  The window will be centered on screen.
     *
     * @param application the TApplication that manages this window
     */
    @SuppressWarnings("this-escape")
    public TEditDesktopStyleWindow(final TApplication application) {

        // Register with the TApplication
        super(application, "", 0, 0, 70, 22, MODAL);
        i18n = ResourceBundle.getBundle(TEditDesktopStyleWindow.class.getName(),
            getLocale());
        setTitle(i18n.getString("windowTitle"));

        // Initialize with the first border
        List<String> borders = new ArrayList<String>();
        borders.add("com.idataconnect.salinas.toolkit.TEditColorTheme.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TEditColorTheme.options.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TMenu.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TPanel.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TRadioGroup.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TScreenOptions.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TScreenOptions.grid.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TScreenOptions.options.borderStyle");
        borders.add("com.idataconnect.salinas.toolkit.TWindow.borderStyleForeground");
        borders.add("com.idataconnect.salinas.toolkit.TWindow.borderStyleInactive");
        borders.add("com.idataconnect.salinas.toolkit.TWindow.borderStyleModal");
        borders.add("com.idataconnect.salinas.toolkit.TWindow.borderStyleMoving");
        Collections.sort(borders);
        assert (borders.size() > 0);
        for (String borderName: borders) {
            editBorderStyles.put(borderName, System.getProperty(borderName,
                    "default"));
        }

        borderNames = addList(borders, 2, 2, 43, 7,
            new TAction() {
                // When the user presses Enter
                public void DO() {
                    updateShownBorderStyle();
                }
            },
            new TAction() {
                // When the user navigates with keyboard
                public void DO() {
                    updateShownBorderStyle();
                }
            },
            new TAction() {
                // When the user navigates with keyboard
                public void DO() {
                    updateShownBorderStyle();
                }
            }
        );
        borderNames.setSelectedIndex(0);

        List<String> borderStyles = BorderStyle.getStyleNames();
        borderStyle = addComboBox(47, 2, 18, borderStyles, 0, 7,
            new TAction() {
                public void DO() {
                    String borderName = borderNames.getSelected();
                    assert (borderName != null);
                    String newBorderStyle = borderStyle.getText();

                    editBorderStyles.setProperty(borderName, newBorderStyle);
                    updateShownBorderStyle();
                }
            });

        updateShownBorderStyle();

        List<String> buttonStyles = new ArrayList<String>();
        buttonStyles.add("square");
        buttonStyles.add("round");
        buttonStyles.add("diamond");
        buttonStyles.add("arrowleft");
        buttonStyles.add("arrowright");
        buttonStyle = addComboBox(2, 11, 18, buttonStyles, 0, 6,
            new TAction() {
                public void DO() {
                    String newButtonStyle = buttonStyle.getText();
                    button1.setStyle(newButtonStyle);
                    button2.setStyle(newButtonStyle);
                }
            });
        String buttonStyleString = System.getProperty("com.idataconnect.salinas.toolkit.TButton.style",
            "square");
        buttonStyle.setText(buttonStyleString);

        button1 = addButton(i18n.getString("button1"), 24, 11, null);
        button2 = addButton(i18n.getString("button2"), 24, 14, null);
        button1.setStyle(buttonStyleString);
        button2.setStyle(buttonStyleString);

        addButton(i18n.getString("okButton"), 6, getHeight() - 4,
            new TAction() {
                public void DO() {
                    for (String name: editBorderStyles.stringPropertyNames()) {
                        String value = editBorderStyles.getProperty(name);
                        System.setProperty(name, value);
                    }
                    String newButtonStyle = buttonStyle.getText();
                    System.setProperty("com.idataconnect.salinas.toolkit.TButton.style", newButtonStyle);
                    getApplication().closeWindow(TEditDesktopStyleWindow.this);
                }
            }
        );

        addButton(i18n.getString("cancelButton"), getWidth() - 16,
            getHeight() - 4,
            new TAction() {
                public void DO() {
                    getApplication().closeWindow(TEditDesktopStyleWindow.this);
                }
            }
        );

        // Default to the border list
        activate(borderNames);

        // Add shortcut text
        newStatusBar(i18n.getString("statusBar"));
    }

    // ------------------------------------------------------------------------
    // Event handlers ---------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Handle keystrokes.
     *
     * @param keypress keystroke event
     */
    @Override
    public void onKeypress(final TKeypressEvent keypress) {
        // Escape - behave like cancel
        if (keypress.equals(kbEsc)) {
            getApplication().closeWindow(this);
            return;
        }

        // Pass to my parent
        super.onKeypress(keypress);
    }

    // ------------------------------------------------------------------------
    // TWindow ----------------------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Draw me on screen.
     */
    @Override
    public void draw() {
        super.draw();
        CellAttributes attr = new CellAttributes();

        // Draw the label on borderKeys
        attr.setTo(getTheme().getColor("twindow.background.modal"));
        if (borderNames.isActive()) {
            attr.setForeColor(getTheme().getColor("tlabel").getForeColor());
            attr.setBold(getTheme().getColor("tlabel").isBold());
        }
        putStringXY(3, 2, i18n.getString("borderName"), attr);

        // Draw the label on borderStyles
        attr.setTo(getTheme().getColor("twindow.background.modal"));
        if (borderStyle.isActive()) {
            attr.setForeColor(getTheme().getColor("tlabel").getForeColor());
            attr.setBold(getTheme().getColor("tlabel").isBold());
        }
        putStringXY(48, 2, i18n.getString("borderStyle"), attr);

        // Draw the border style example box
        attr.setTo(getTheme().getColor("twindow.background"));
        CellAttributes border = new CellAttributes();
        border.setTo(getTheme().getColor("twindow.border"));
        drawBox(borderNames.getX() + borderNames.getWidth() + 3,
            borderNames.getY() + 3, getWidth() - 3, borderNames.getY() + 8,
            border, attr, shownBorderStyle, false);

        // Draw the label on buttonStyles
        attr.setTo(getTheme().getColor("twindow.background.modal"));
        if (buttonStyle.isActive()) {
            attr.setForeColor(getTheme().getColor("tlabel").getForeColor());
            attr.setBold(getTheme().getColor("tlabel").isBold());
        }
        putStringXY(3, 11, i18n.getString("buttonStyle"), attr);

    }

    // ------------------------------------------------------------------------
    // TEditDesktopStyleWindow ------------------------------------------------
    // ------------------------------------------------------------------------

    /**
     * Update the shown border with the style of the selected border name.
     */
    private void updateShownBorderStyle() {
        String borderName = borderNames.getSelected();
        assert (borderName != null);

        String borderStyleName = editBorderStyles.getProperty(borderName,
            "default");
        BorderStyle style = BorderStyle.getStyle(borderStyleName);

        if (borderStyleName.toLowerCase().equals("default")) {
            // This is ugly! But we put the default border style of the Jexer
            // widgets here.

            // TWindow
            if (borderName.equals("com.idataconnect.salinas.toolkit.TWindow.borderStyleForeground")) {
                style = BorderStyle.DOUBLE;
            }
            if (borderName.equals("com.idataconnect.salinas.toolkit.TWindow.borderStyleModal")) {
                style = BorderStyle.DOUBLE;
            }
            if (borderName.equals("com.idataconnect.salinas.toolkit.TWindow.borderStyleMoving")) {
                style = BorderStyle.SINGLE;
            }
            if (borderName.equals("com.idataconnect.salinas.toolkit.TWindow.borderStyleInactive")) {
                style = BorderStyle.SINGLE;
            }

            // TMenu
            if (borderName.equals("com.idataconnect.salinas.toolkit.TMenu.borderStyle")) {
                style = BorderStyle.SINGLE;
            }

            // TEditColorThemeWindow
            if (borderName.equals("com.idataconnect.salinas.toolkit.TEditColorTheme.borderStyle")) {
                style = BorderStyle.DOUBLE;
            }
            if (borderName.equals("com.idataconnect.salinas.toolkit.TEditColorTheme.options.borderStyle")) {
                style = BorderStyle.SINGLE;
            }

            // TEditDesktopStyleWindow
            if (borderName.equals("com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle")) {
                style = BorderStyle.DOUBLE;
            }

            // TPanel
            if (borderName.equals("com.idataconnect.salinas.toolkit.TPanel.borderStyle")) {
                style = BorderStyle.NONE;
            }

            // TRadioGroup
            if (borderName.equals("com.idataconnect.salinas.toolkit.TRadioGroup.borderStyle")) {
                style = BorderStyle.SINGLE_V_DOUBLE_H;
            }

            // TScreenOptionsWindow
            if (borderName.equals("com.idataconnect.salinas.toolkit.TScreenOptions.borderStyle")) {
                style = BorderStyle.SINGLE;
            }
            if (borderName.equals("com.idataconnect.salinas.toolkit.TScreenOptions.grid.borderStyle")) {
                style = BorderStyle.SINGLE;
            }
            if (borderName.equals("com.idataconnect.salinas.toolkit.TScreenOptions.options.borderStyle")) {
                style = BorderStyle.SINGLE;
            }

        }
        shownBorderStyle = style;

        borderStyle.setText(borderStyleName);
    }

    /**
     * Set the border style for the window when it is the foreground window.
     *
     * @param borderStyle the border style string, one of: "default", "none",
     * "single", "double", "singleVdoubleH", "singleHdoubleV", or "round"; or
     * null to use the value from com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle.
     */
    @Override
    public void setBorderStyleForeground(final String borderStyle) {
        if (borderStyle == null) {
            String style = System.getProperty("com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle",
                "double");
            super.setBorderStyleForeground(style);
        } else {
            super.setBorderStyleForeground(borderStyle);
        }
    }

    /**
     * Set the border style for the window when it is the modal window.
     *
     * @param borderStyle the border style string, one of: "default", "none",
     * "single", "double", "singleVdoubleH", "singleHdoubleV", or "round"; or
     * null to use the value from com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle.
     */
    @Override
    public void setBorderStyleModal(final String borderStyle) {
        if (borderStyle == null) {
            String style = System.getProperty("com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle",
                "double");
            super.setBorderStyleModal(style);
        } else {
            super.setBorderStyleModal(borderStyle);
        }
    }

    /**
     * Set the border style for the window when it is an inactive/background
     * window.
     *
     * @param borderStyle the border style string, one of: "default", "none",
     * "single", "double", "singleVdoubleH", "singleHdoubleV", or "round"; or
     * null to use the value from com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle.
     */
    @Override
    public void setBorderStyleInactive(final String borderStyle) {
        if (borderStyle == null) {
            String style = System.getProperty("com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle",
                "double");
            super.setBorderStyleInactive(style);
        } else {
            super.setBorderStyleInactive(borderStyle);
        }
    }

    /**
     * Set the border style for the window when it is being dragged/resize.
     *
     * @param borderStyle the border style string, one of: "default", "none",
     * "single", "double", "singleVdoubleH", "singleHdoubleV", or "round"; or
     * null to use the value from com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle.
     */
    @Override
    public void setBorderStyleMoving(final String borderStyle) {
        if (borderStyle == null) {
            String style = System.getProperty("com.idataconnect.salinas.toolkit.TEditDesktopStyle.borderStyle",
                "double");
            super.setBorderStyleMoving(style);
        } else {
            super.setBorderStyleMoving(borderStyle);
        }
    }

}
