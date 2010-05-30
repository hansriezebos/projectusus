public class Tasks {
    void namensgebung() {
        // TODO Namensgebung geradeziehen: Kind -> Metrics. Was noch?
    }

    void introduceNullObjects() {
        // TODO Null-Objekte in die RawData-Hierarchie einf��hren.
    }

    void maximizeTablesInCockpitAndHotspots() {
        // TODO Tabellen sollten ab einer gewissen Fensterbreite "magnetisch" die ganze Breite des Fensters einnehmen.
    }

    void useEclipseDisplayInPackagenames() {
        // TODO Packagenamen ��berall mit Eclipse Appearance Regeln darstellen (Package View, Hotspots, ...).
    }

    void changesForCockpit() {
        // TODO Cockpit: Ein Trend-Pfeil pro Metrik.
        // Werte seit letztem CheckIn / CheckOut? Oder seit dem Start? Oder mit Reset-Button?
        // Dazu Level sehr exakt bestimmen.
    }

    void makeCalculationsPluggable() {
        // TODO Berechnungsalgorithmen f��r die Violations pluggable machen. Dazu Implementierung eines
        // Visitor-Collectors, der den RawData Baum besucht und sich aus allen Knoten das Gew��nschte
        // zusammensucht: Hotspots, Violations, Metrikwerte etc.
    }

    void convertViolationFunction() {
        // TODO Violations: Linear statt konstant. Ergibt sich ganz einfach aus dem vorherigen.
    }

    void makeJobCancellable() {
        // TODO UsusModel: Berechnung abbrechen, wenn der Benutzer dies verlangt. Danach voller Rebuild.
    }

    void improveGraphLayoutsWithZest() {
        // TODO ZEST Layouting verbessern.
    }

    void createMudHoleHotspots() {
        // TODO Aggregierte Hotspots = "Schlamml��cher"
    }

    void makeMetricsPluggable() {
        // TODO Metriken pluggable machen: Collectors zuf��gen, Werte in RawData Struktur werden in einer Map
        // gehalten, pro Metrik ein gespeichertes Werteobjekt.
    }

    void createNewMetrics() {
        // TODO Metriken: # SuppressWarnings
        // Code Smells
        // Feature Envy
        // Useless Comments
        // Klassenzyklen innerhalb / zwischen Paketen
    }

    void createRealAcd() {
        // TODO RealACD ("Metriken ohne Augenwischerei"): Interface und Implementierung unifizieren.
    }

    void createDifferentCohesionMetrics() {
        // TODO Koh�sionsmetriken werfen alle Arten von Referenzen in einen Topf. Unterscheiden!
        // - Parameter sind keine Referenzen in unserem Sinne
        // - Attribute, die State machen, schon.
        // Verschiedene ACD-Klassen machen und schauen, wie sich die Geflechte verhalten.
    }

    void pimpDependencyGraphForRemoving() {
        // TODO DependencyGraph: Man kann ausgew�hlte Knoten aus den Graphen entfernen
    }

    void createPackageView() {
        // TODO PackageView: Packages ausw�hlen, die enthaltenen Klassen im Class View darstellen
    }

    void createTrends() {
        // TODO Wertehistorie: Verbesserte und verschlechterte Werte ausweisen und danach sortieren.
    }

    void createExplanatoryDocumentation() {
        // TODO Doku: Warum soll ich meinen Code �ndern? Wo kann ich weiterlesen?
        // - Position Paper: Usus vs. Checkstyle, PMD, ...
    }

    void repairAndExtendCheatsheet() {
        // TODO CheatSheet reparieren und erweitern
    }

    void createProjectYellowHotspots() {
        // TODO Hotspots f��r YellowCount am Projekt
    }

    void createPackagecycleHotspots() {
        // TODO Hotspots f��r Package Cycles?? Hotspot ^=^ Zyklus-Objekt. Relevant: # Packages im Zyklus
    }

    void createShadowForMarc() {
        // TODO Schatten am InfoPresenter (extra f��r Marc :-)
    }

    void createNatureInsteadSeperateView() {
        // TODO Projektaktivierung in den Project Prefs statt in einem eigenen View?
        // Decorator am Projekt. Eigene Nature zuf��gen.
    }

    void createRefactorings() {
        // TODO Refactorings einbauen: Feature Envy Code an die richtige Stelle verschieben.
    }

    void createRemoveUselessComments() {
        // TODO "Remove useless comments" entfernt alle nutzlosen Kommentare (oder alle? ;-)
    }

    void createCancelInGraphUpdates() {
        // TODO Graph Views: Canceln eines wartenden Updates (durch Verschieben des Sliders ausgel��st)?
    }
}
