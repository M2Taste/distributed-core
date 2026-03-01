A scalable core system for Minecraft servers built on Paper, MySQL, and Redis.
The project focuses on clean architecture, thread safety, and network-ready design.
It serves as a foundation for larger Minecraft networks with multiple servers and an optional proxy layer (Velocity).


Key Features
  Paper plugin (1.21.x)
  
  Asynchronous player data processing
  
  MySQL as the source of truth
  
  Redis as an in-memory cache with TTL
  
  Redis Pub/Sub for cache invalidation
  
  Thread-safe architecture
  
  Fat-JAR build using Shadow
