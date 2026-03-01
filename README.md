Skalierbares Core-System für Minecraft-Server auf Basis von Paper, MySQL und Redis.
Der Fokus liegt auf sauberer Architektur, Thread-Safety und Netzwerk-Tauglichkeit.
Das Projekt bildet die Grundlage für größere Minecraft-Netzwerke mit mehreren Servern und optionalem Proxy (Velocity).


Paper Plugin (1.21.x)

Asynchrone Player-Datenverarbeitung

MySQL als Source of Truth

Redis als In-Memory Cache (TTL)

Redis Pub/Sub zur Cache-Invalidierung

Thread-sichere Architektur

Fat-JAR Build (Shadow)
