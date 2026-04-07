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

/**
 * Casciian - Java Text User Interface library
 *
 * <p>
 * This library is a text-based windowing system loosely reminiscent of
 * Borland's <a href="http://en.wikipedia.org/wiki/Turbo_Vision">Turbo
 * Vision</a> library.  Jexer's goal is to enable people to get up and
 * running with minimum hassle and lots of polish.
 * </p>
 */
module com.idataconnect.salinas.toolkit {
    requires java.base;
    requires transitive java.desktop;

    exports com.idataconnect.salinas.toolkit;
    exports com.idataconnect.salinas.toolkit.backend;
    exports com.idataconnect.salinas.toolkit.bits;
    exports com.idataconnect.salinas.toolkit.effect;
    exports com.idataconnect.salinas.toolkit.event;
    exports com.idataconnect.salinas.toolkit.help;
    exports com.idataconnect.salinas.toolkit.io;
    exports com.idataconnect.salinas.toolkit.layout;
    exports com.idataconnect.salinas.toolkit.menu;
    exports com.idataconnect.salinas.toolkit.net;
    exports com.idataconnect.salinas.toolkit.tackboard;
    exports com.idataconnect.salinas.toolkit.terminal;
    exports com.idataconnect.salinas.toolkit.texteditor;
}
